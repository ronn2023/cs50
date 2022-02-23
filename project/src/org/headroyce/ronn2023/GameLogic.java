package org.headroyce.ronn2023;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Represents the logic of our game
 */
public class GameLogic {

    public enum DIRECTION {
        LEFT,
        UP,
        RIGHT,
        DOWN,
        STOP,
        NONE
    }

    private int TIME_ELAPSED = 0;
    // The game step in milliseconds
    public static final int GAME_STEP_TIMER = 17;
    private GameTimer gameTimer;

    private boolean gameOver;
    private int endScore;
    private Random rand;

    //Spike Wall
    private static final int SPIKE_SIDE_SPAWN_CHANCE = 49;
    private static final int SPIKE_SPAWN_TIME = 1000;
    private int SPIKE_SPAWN_TIMER = 1000;
    private Spike Onscreen = null;

    // The player
    private Ball player;
    private HashMap<DIRECTION, Boolean> forcesOnPlayer;

    private int PlayerScore;

    private static final int PLAYER_FLASH_TIME = 500;
    private int flashTimer = 0;

    private static final int PLAYER_SCORING_TIME = 1000;
    private int PLAYER_SCORING_TIMER = 1000;

    private static final int PLAYER_SCORING_POINTS = 10;

    private static final int ENEMY_SPAWN_TIME = 150;
    private static final int ENEMY_DIRECTION_PROBABILITY = 5;
    private static final int ENEMY_SPAWN_PROBABILITY = 5;
    private static final int OBSTACLE_SPAWN_PROBABILITY = 10;
    private int ENEMY_SPAWN_TIMER = 150;


    // Enemy Elements

    private ArrayList<Mob> mobs;

    // Width and height of the canvas
    private double width, height;

    public GameLogic(double width, double height){
        rand = new Random();

        gameTimer = new GameTimer();

        this.width = Math.abs(width);
        this.height = Math.abs(height);

        player = new Ball();
        mobs = new ArrayList<>();

        forcesOnPlayer = new HashMap<>();

        reset();
    }

    /**
     * Renders the game elements onto a canvas
     * @param canvas the canvas to render onto
     */
    public void render(Canvas canvas){

        // Update width and height
        width = canvas.getWidth();
        height = canvas.getHeight();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        player.render(canvas);
        for( int i = 0; i < mobs.size(); i++ ){
            Mob enemy = mobs.get(i);

            int min = (int)enemy.getWidth();
            if( enemy.x < 0 ){
                int maxW = (int)(width-min+1);
                enemy.x = rand.nextInt(maxW-min+1)+min;
            }

            enemy.render(canvas);
        }

        // Draw lives and score last so that the balls go under them
        Text lives = new Text("Lives: " + Math.round(player.getHP()));

        gc.strokeText("Score: " + getPlayerScore(), 10, 30);
        gc.strokeText(lives.getText(),width - 10 - lives.getLayoutBounds().getWidth(), 20);
    }

    /**
     * Pause or unpause the game
     * @param setPaused true to pause, false otherwise
     */
    public void pause(boolean setPaused ){
        if( setPaused ){
            gameTimer.stop();
        }
        else {
            gameTimer.start();
        }
    }

    /**
     * accessor for the score of the player
     * @return
     */
    public int getPlayerScore(){
        return this.PlayerScore;
    }

    /**
     * increases the score of the player for use in GameGUI
     * @param amount
     */
    public void addScore(int amount){
        this.PlayerScore += amount;
    }

    /**
     * resets the canvas to its normal state
     */
    public void reset(){
        player.x = 200;
        player.y = 400;
        player.setRadius(10);

        player.velX = player.velY = 0;
        player.setVelocityBoundX(-7, 7);
        player.setVelocityBoundY(-7,7);

//        while (player.getHP() != 3){
            player.setHP(3);
//        }

        mobs.clear();
        forcesOnPlayer.clear();

        gameOver = false;
        PlayerScore = 0;

        Onscreen = null;

        mobs.clear();

    }

    /**
     * returns the value of the boolean gameOver
     * @return
     */
    public boolean isGameOver(){
        return gameOver;
    }

    /**
     * handle mob collisions on the walls
     * @param player
     * @return
     */
    private boolean collideWalls(Mob player){

        boolean collided = false;

        // Keep player with the window

        if( player == this.player ) {
            if (player.y + player.getHeight() > height) {
                player.y = height - player.getHeight();
                player.bounceY();
                collided = true;
            }

            if (player.y  < 0) {
                player.y = 0;
                player.bounceY();
                collided = true;
            }
        }


        if( player.x + player.getWidth() > width ){
            player.x = width - player.getWidth();
            player.bounceX();
            collided = true;
        }
        if( player.x  < 0 ){
            player.x = 0;
            player.bounceX();
            collided = true;
        }

        return collided;
    }

    /**
     * move player
     * @param direction
     */
    public void applyForce( DIRECTION direction ) {
        forcesOnPlayer.put(direction, true);
    }

    /**
     * stop moving player
     * @param direction
     */
    public void removeForce(DIRECTION direction){
        forcesOnPlayer.remove(direction);
    }

    /**
     * getter for the score the player had before losing the game
     * @return
     */
    public int getEndScore(){
        return endScore;
    }

    /**
     * getter for the amount of time elapsed
     * @return
     */
    public int getTimeElapsed(){
        return TIME_ELAPSED;
    }

    /**
     * Runs once per game tick which is set dynamically by the GAME_STEP_TIMER
     */
    private class GameTimer extends AnimationTimer {
        // The last nanosecond
        private long lastUpdate;

        public GameTimer() {
            lastUpdate = 0;
        }

        @Override

        public void handle(long now) {

            // Covert the time_elapsed from nanoseconds to milliseconds
            long time_elapsed = (now - lastUpdate)/1000000;

            //amount of time Player Flashes
            flashTimer -= time_elapsed;
            if( flashTimer < 0 ){
                player.setColor(Color.BLACK);
            }

            //update score
            PLAYER_SCORING_TIMER -= time_elapsed;
            if( PLAYER_SCORING_TIMER < 0 ){
                PLAYER_SCORING_TIMER = PLAYER_SCORING_TIME;
                addScore(PLAYER_SCORING_POINTS);
                TIME_ELAPSED++;
            }

            SPIKE_SPAWN_TIMER -= time_elapsed;
            if (SPIKE_SPAWN_TIMER < 0){
                if (Onscreen == null){
                    //check to see if obstacle is onscreen or not and either removes the obstacle or renders it accordingly
                    double widthMin = 5;
                    double widthMax = width * 0.4;

                    double spikeWidth = (int)(Math.random() * (widthMax - widthMin + 1)+ widthMin);
                    Onscreen = new Spike(spikeWidth, 30);
                    if (rand.nextInt(100) < 50){
                        Onscreen.x = 1;
                    }else{
                        Onscreen.x = width - (spikeWidth - 1);
                    }
                    Onscreen.y = 1;
                    Onscreen.velY = 2;

                    Onscreen.setVelocityBoundY(2,2);
                    Onscreen.setVelocityBoundX(0,0);
                    mobs.add(Onscreen);
                } else if (Onscreen != null && Onscreen.y + Onscreen.getHeight()> height){
                    mobs.remove(Onscreen);
                    Onscreen = null;
                }

                SPIKE_SPAWN_TIMER = SPIKE_SPAWN_TIME;
            }

            ENEMY_SPAWN_TIMER -= time_elapsed;
            if( ENEMY_SPAWN_TIMER < 0 ){
                int chance = rand.nextInt(100);
                if( chance < OBSTACLE_SPAWN_PROBABILITY ){

                    if( chance < ENEMY_SPAWN_PROBABILITY ) {
                        //randomly spawns either a ball or a lifeGiver
                        if (rand.nextInt(100) < 50){
                            Ball enemy = new Ball();
                            enemy.setRadius(10);
                            enemy.x = -1;
                            enemy.y = -enemy.getRadius();  // off screen
                            enemy.setVelocityBoundX(-5,5);
                            enemy.setVelocityBoundY(0,5);
                            enemy.setColor(Color.RED);
                            enemy.velX = rand.nextInt(5) + 2;
                            enemy.velY = rand.nextInt(5) + 2;
                            mobs.add(enemy);
                        } else{
                            LifeGiver lifeGiver = new LifeGiver();
                            lifeGiver.setRadius(10);

                            lifeGiver.x = -1;
                            lifeGiver.y = -lifeGiver.getRadius();  // off screen

                            lifeGiver.setVelocityBoundX(-5,5);
                            lifeGiver.setVelocityBoundY(-5,5);

                            lifeGiver.setColor(Color.GREEN);
                            lifeGiver.velX = rand.nextInt(5) + 2;
                            lifeGiver.velY = rand.nextInt(5) + 2;



                            mobs.add(lifeGiver);
                        }

                    }
                    else{
                        //randomly spawns either an obstacle or a StrongerObstacle
                        if (rand.nextInt(100)<50){
                            Obstacle enemy = new Obstacle();
                            enemy.x = -1;
                            enemy.y = -enemy.getHeight();  // off screen
                            enemy.setVelocityBoundX(0,0);
                            enemy.setVelocityBoundY(10,10);


                            enemy.velY = 10;
                            mobs.add(enemy);
                        } else if (rand.nextInt(100)>50){
                            StrongerObstacle strong = new StrongerObstacle();
                            strong.x = -1;
                            strong.y = -strong.getHeight();
                            strong.velY = 5;
                            strong.setVelocityBoundX(0,0);
                            strong.setVelocityBoundY(-5,5);
                            mobs.add(strong);
                        }

                    }

                }

                ENEMY_SPAWN_TIMER = ENEMY_SPAWN_TIME;
            }

            if( time_elapsed > GameLogic.GAME_STEP_TIMER) {
                //Map wasd keys to move player

                if( forcesOnPlayer.containsKey(DIRECTION.LEFT) ){
                    player.velX--;
                }
                if( forcesOnPlayer.containsKey(DIRECTION.RIGHT) ){
                    player.velX++;
                }
                if( forcesOnPlayer.containsKey(DIRECTION.UP) ){
                    player.velY--;
                }
                if( forcesOnPlayer.containsKey(DIRECTION.DOWN) ){
                    player.velY++;
                }

                if( forcesOnPlayer.containsKey(DIRECTION.STOP) ){
                    player.velX -= Math.signum(player.velX);
                    player.velY -= Math.signum(player.velY);
                }

                // MOVE EVERYTHING
                player.move();
                for( int i = 0; i < mobs.size(); i++ ){
                    Mob enemy = mobs.get(i);
                    if(enemy instanceof Ball) {
                        if (rand.nextInt(100) < ENEMY_DIRECTION_PROBABILITY &&
                                enemy.getColor() == Color.RED
                        ) {
                            double changeX = Math.signum(player.x - enemy.x);


                            enemy.velX = changeX * Math.abs(enemy.velX);

                        }
                    }

                    enemy.move();
                }




                // CHECK WALLS COLLISIONS ON EVERYTHING
                boolean playerCollided = collideWalls(player);
                for( int i = 0; i < mobs.size(); i++ ){
                    Mob enemy = mobs.get(i);
                    collideWalls(enemy);

                    if( enemy.y + enemy.getHeight() > height ){
                        addScore(enemy.scored());
                        enemy.addHP(-enemy.getHP());
                        mobs.remove(enemy);

                    }
                }
                if (playerCollided){
                    player.addHP(-1);
                }



                // CHECK BALL COLLISIONS ON EVERYTHING
                for( int i = 0; i < mobs.size(); i++ ) {
                    Mob enemy = mobs.get(i);
                    for( int j = i + 1; j < mobs.size(); j++ ) {
                       if (enemy.intersects(mobs.get(j))) {
                           enemy.collidePlayer(mobs.get(j));

                           mobs.get(j).damage();

                           enemy.damage();


                       }

                    }
                    //checks if the player is intersecting the player
                    boolean enemyRemove = enemy.intersects(player);
                    if( enemyRemove ){
                        //Something to add here.
                        enemy.collidePlayer(player);


                        player.addHP(-(enemy.damage()));

                    }
                    //checks if the player is intersecting with the player or the player has collided with a wall
                    playerCollided =  enemyRemove || playerCollided;
                }

                //iterate over mobs arraylist and remove mobs if they have 0 health points or less
                for (int i= 0; i < mobs.size(); i++){
                    if (mobs.get(i).getHP() <= 0){
                        mobs.remove(i);
                        i--;

                    }
                }

                if( playerCollided ){
                    // Stops lives being lost if green
                    addScore(-100);
                    if( flashTimer <= 0 ){
                        if( player.getHP() <= 0 ) {
                            endScore = getPlayerScore();
                            gameOver = true;
                            pause(true);
                        }
                    }

                    flashTimer = PLAYER_FLASH_TIME;
                    player.setColor(Color.GREEN);
                }

                lastUpdate = now;
            }
        }
    }
}
