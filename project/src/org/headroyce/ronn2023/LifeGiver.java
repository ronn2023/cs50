package org.headroyce.ronn2023;

import javafx.scene.paint.Color;

public class LifeGiver extends Ball{

    /**
     * default constructor that creates a LifeGiver with a radius of 1
     */
    public LifeGiver(){
        this(1);

    }

    /**
     * constructor that creates an object with a custom radius
     * and sets its color to green
     * @param radius
     */
    public LifeGiver(double radius){
        super(radius);
        this.addHP(1);
        this.setColor(Color.GREEN);

    }

    /**
     * overrides the LifeGiver's damage method that does damage to surrounding
     * mobs in order to give the player a health point
     * @return
     */
    public int damage(){
        //When damage() is run, decrement the LifeGiver's health by itself to remove the LifeGiver from the canvas
        this.addHP(-this.getHP());
        return -1;
    }
}
