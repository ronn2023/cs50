package org.headroyce.ronn2023;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents an rectangular obstacle
 */
public class Obstacle extends Mob{
    /**
     * Creates an obstacle with a width of ten and height of five
     */
    public Obstacle(){
        this(10, 40);
    }

    /**
     * Creates an obstacle with a custom width and height (in pixels)
     * @param width the width (in pixels) to set of the obstacle; Non-positives are reset to ten
     * @param height the height (in pixels) to set of the obstacle; Non-positives are reset to ten
     */
    public Obstacle( double width, double height ){
        setColor(Color.PURPLE);
        if( width <= 0 ){
            width = 10;
        }
        if( height <= 0 ){
            height = 10;
        }
        this.addHP(1);

        setWidth(width);
        setHeight(height);

    }
    public void collidePlayer(Mob player){
        player.velX *= -1;
        player.velY *= -1;
//        if (player.velY > 0){
//            player.y = this.y - player.getWidth()/2;
//            player.velY = player.velY * -1;
//        }
//        else if (player.velY < 0){
//            player.y = this.y + this.getHeight();
//            player.velY = player.velY * -1;
//        }
//        if (player.velX > 0){
//            player.x = this.x - player.getRadius();
//            player.velX = player.velX * -1;
//        }
//        if (player.velX > 0){
//            player.x = this.x + player.getRadius();
//            player.velX = player.velX * -1;
//        }
    }


    /**
     * Check to see if the obstacle overlaps with a ball (circular)
     * @param other the ball to check intersection with Obstacle
     * @return true is this object intersects with other, false otherwise
     */
    public boolean intersects(Ball other){
        if (this.x > other.x + other.getRadius()){
            return false;
        }
        if (this.x + this.getWidth() < other.x){
            return false;
        }
        if (this.y + this.getHeight() > other.y){
            return false;
        }
        if (this.y < other.y + other.getRadius()){
            return false;
        }

        return true;
    }


    /**
     * render the obstacle as a rectangle
     * @param canvas
     */
    public void render( Canvas canvas ){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(this.getColor());
        gc.fillRect(x, y, this.getWidth(), this.getHeight());
//        System.out.println(this.getColor() + " " + this.getWidth() + " " + this.getHeight());
    }
}
