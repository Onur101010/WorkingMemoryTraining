package com.onuroapplications.memorytrainer;

/**
 * This singleton Class provides getter and setter methods to manipulate
 * points earned in the games
 */
public class PointsManager {

    private int absolutePointsNBack = 0;
    private int relativePointsNBack = 0;
    private int absolutePointsSequence = 0;
    private int relativePointsSequence = 0;
    private static PointsManager instance = new PointsManager();

    //only instance of this class
    private PointsManager(){
    }

    public static PointsManager getInstance() {
        return instance;
    }

    public int getAbsolutePointsNBack() {
        return absolutePointsNBack;
    }

    public void setAbsolutePointsNBack(int points) {
        this.absolutePointsNBack = points;
    }

    public int getAbsolutePointsSequence() {
        return absolutePointsSequence;
    }

    public void setAbsolutePointsSequence(int points) {
        this.absolutePointsSequence = points;
    }
}
