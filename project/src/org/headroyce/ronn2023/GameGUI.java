package org.headroyce.ronn2023;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.geometry.Pos;

/**
 * Represents the view element of the game
 */
public class GameGUI extends StackPane {

    private GameLogic logic;

    private Canvas gameArea;
    private AnimationTimer animTimer;

    private Button reset;

    private Label score;
    private Label time;


    public GameGUI() {
        gameArea = new Canvas();
        gameArea.heightProperty().bind(this.heightProperty());
        gameArea.widthProperty().bind(this.widthProperty());

        animTimer = new AnimTimer();
        logic = new GameLogic(gameArea.getWidth(), gameArea.getHeight());

        this.getChildren().addAll(gameArea, gameoverScreen());
    }

    /**
     * Pause/Unpause the animation without touching the game timer
     * @param setAnimPause true to pause, false otherwise
     */
    public void pause(boolean setAnimPause) {
        if (setAnimPause) {
            animTimer.stop();
        } else {
            animTimer.start();
        }
    }

    /**
     * Pause/unpause teh animation and game timer
     * @param setAnimPause true to pause the animation timer
     * @param setGamePause true to pause the game timer
     */
    public void pause(boolean setAnimPause, boolean setGamePause ){
        this.pause(setAnimPause);
        logic.pause(setGamePause);
    }

    /**
     * Deal with key presses
     * @param event the event to handle
     */
    public void handleKeyPress(KeyEvent event){
        if( event.getCode() == KeyCode.A){
            logic.applyForce(GameLogic.DIRECTION.LEFT);
        }
        if( event.getCode() == KeyCode.D ) {
            logic.applyForce(GameLogic.DIRECTION.RIGHT);
        }
        if( event.getCode() == KeyCode.W ) {
            logic.applyForce(GameLogic.DIRECTION.UP);
        }
        if( event.getCode() == KeyCode.S ) {
            logic.applyForce(GameLogic.DIRECTION.DOWN);
        }

        if( event.getCode() == KeyCode.SPACE ){
            logic.applyForce(GameLogic.DIRECTION.STOP);
        }
    }

    /**
     * Deal with key releases
     * @param event the event to handle
     */
    public void handleKeyRelease(KeyEvent event){
        if( event.getCode() == KeyCode.A){
            logic.removeForce(GameLogic.DIRECTION.LEFT);
        }
        if( event.getCode() == KeyCode.D ) {
            logic.removeForce(GameLogic.DIRECTION.RIGHT);
        }
        if( event.getCode() == KeyCode.W ) {
            logic.removeForce(GameLogic.DIRECTION.UP);
        }
        if( event.getCode() == KeyCode.S ) {
            logic.removeForce(GameLogic.DIRECTION.DOWN);
        }

        if( event.getCode() == KeyCode.SPACE ){
            logic.removeForce(GameLogic.DIRECTION.STOP);
        }
    }

    /**
     * Updates final score and time elapsed for game over screen.
     */
    public void updateGraphics() {
        GameGUI.this.score.setText("Final Score: " + logic.getEndScore() + " points");
        GameGUI.this.time.setText("Time Elapsed: " + logic.getTimeElapsed() + " seconds");
    }
    /**
     * makes new VBox with gameover screen child nodes.
     * @return  game over text and button overlay
     */
    private VBox gameoverScreen() {
        VBox rtn = new VBox();

        reset = new Button("Reset");
        reset.setPadding(new Insets(10, 10, 10, 10));
        reset.setOnAction(new ResetButton());
        score = new Label("Final Score: ");
        time = new Label("Time Elapsed: ");
        rtn.setAlignment(Pos.CENTER);

        rtn.getChildren().addAll(score, time, reset);

        rtn.setPadding(new Insets(20, 20, 20, 20));
        rtn.setMargin(score, new Insets(20, 20, 20, 20));
        rtn.setMargin(time, new Insets(20, 20, 20, 20));
        rtn.setMargin(reset, new Insets(20, 20, 20, 20));

        return rtn;
    }

    /**
     * Runs once per frame and handles all the drawin/g of each frame
     */
    private class AnimTimer extends AnimationTimer {

        @Override
        public void handle(long now) {
            GraphicsContext gc = gameArea.getGraphicsContext2D();


            gc.clearRect(0,0, gameArea.getWidth(), gameArea.getHeight());

            if(logic.isGameOver()){
                updateGraphics();
                reset.setVisible(true);
                score.setVisible(true);
                time.setVisible(true);
                reset.toFront();
            }
            else {
                logic.render(gameArea);
                reset.setVisible(false);
                score.setVisible(false);
                time.setVisible(false);
            }

        }
    }

    /**
     * handles event, reset button pressed
     */

    private class ResetButton implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            pause(false);
            logic.pause(false);
            logic.reset();
            reset.setVisible(false);
            time.setVisible(false);
            score.setVisible(false);
        }
    }



}




