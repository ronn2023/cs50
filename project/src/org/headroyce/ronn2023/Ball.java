package org.headroyce.ronn2023;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents an oval ball
 */
public class Ball extends Mob{
    private double radius;


    /**
     * Creates a ball with a radius of one
     */
    public Ball(){
        this(1);
    }

    /**
     * Creates a ball with a custom radius (in pixels)
     * @param radius the radius (in pixels) to set of the ball; Non-positives are reset to one
     */
    public Ball( double radius ){
        setColor(Color.BLACK);
        if( radius <= 0 ){
            radius = 1;
        }
        this.addHP(1);
        this.radius = radius;
    }


    public int scored(){
        return 200;
    }

    /**
     * Set the radius of this ball.  A ball's radius must be positive.
     * @param radius the new, positive, radius of this ball
     * @return true if the radius is set, false if radius is not changed
     */
    public boolean setRadius(double radius){
        boolean rtn = false;

        if( radius > 0 ) {
            super.setHeight(radius*2);
            super.setWidth(radius*2);
            this.radius = radius;
            rtn = true;
        }

        return rtn;
    }
    public boolean setWidth(double w){
        boolean rtn = false;

        if( w > 0 ) {
            super.setWidth(w);
            this.radius = w/2;
            rtn = true;
        }

        return rtn;
    }

    /**
     * Set the height of this ball.  An ball's height must be positive.
     * @param h the new, positive, height of this ball
     * @return true if the height is set, false if height is not changed
     */
    public boolean setHeight(double h){
        boolean rtn = false;

        if( h > 0 ) {
            super.setHeight(h);
            this.radius = h/2;
            rtn = true;
        }

        return rtn;
    }
    /**
     * Get the current radius of this ball
     * @return a positive radius
     */
    public double getRadius(){
        return this.radius;
    }

    /**
     * Sets the color of the ball
     * @param c the new color of the ball (cannot be null)
     * @return true if the color has changed, false otherwise
     */


    /**
     * Check to see if two balls overlap each other
     * @param other the other ball
     * @return true is this ball intersects with other, false otherwise
     */
    public boolean intersects(Ball other){
        double xd = (other.x-this.x);
        xd *= xd;

        double yd = (other.y-this.y);
        yd *= yd;

        double rad = (other.radius+this.radius);
        rad *= rad;                  // (r1 + r2)^2

        double distance = xd + yd;  // x^2+y^2
        return (distance < rad);
    }

    /**
     * renders current ball object to the canvas
     * @param canvas main scene
     */
    public void render( Canvas canvas ){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(this.getColor());
        gc.fillOval(x,y, 2*radius, 2*radius);

    }
}
