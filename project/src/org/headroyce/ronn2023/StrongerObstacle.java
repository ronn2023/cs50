package org.headroyce.ronn2023;

import javafx.scene.paint.Color;

public class StrongerObstacle extends Obstacle{
    /**
     * creates an object with default width and height of 20 and 80
     * respectively
     */
    public StrongerObstacle(){
        this(20, 80);
    }

    /**
     * calls the super class and passes in the desired width and height
     * sets the color of the object to orange
     * @param width
     * @param height
     */
    public StrongerObstacle(double width, double height){
        super(width, height);
        this.setColor(Color.ORANGE);
        this.addHP(1);
    }

    /**
     * overrides the Mob's damage function to do 2 damage to surrounding
     * enemies/player
     * @return 2
     */
    public int damage(){
        this.addHP(-1);
        return 2;
    }
}
