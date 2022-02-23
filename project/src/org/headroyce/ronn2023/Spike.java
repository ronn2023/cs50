package org.headroyce.ronn2023;

import javafx.scene.paint.Color;

public class Spike extends Obstacle{
    /**
     * create an object of Spike and set its color to turquoise
     * @param width
     * @param height
     */
    public Spike(double width, double height){
        super(width, height);
        this.setColor(Color.TURQUOISE);
        this.addHP(1);
    }

    /**
     * overrides the damage method in Mob to ensure the SpikeWall doesn't
     * disappear
     * @return
     */
    public int damage(){
        this.addHP(-this.getHP());
        return 1;
    }

    /**
     * handle collisions with the player
     * @param other
     */
    public void collidePlayer(Mob other){

        if (other.velY < this.velY) {
            other.velX *= -1;
            other.velY = this.velY * 2;
            other.y += 15;
        } else if (other.velY > this.velY) {
//            other.velX *= -1;
            other.velY *= -1;
            other.y -= 20;
        }


    }

}
