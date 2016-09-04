package org.thesteve0.steptwo;

/**
 * This class is nothing more than an object to store the x and y offsets
 * Only applies while we keep using discrete grids.
 * Created by steve on 9/2/2016.
 */
public class Move {
    private long deltaX = 0;
    private long deltaY = 0;
    private double lastDirection = 0;

    public long getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(long deltaX) {
        this.deltaX = deltaX;
    }

    public long getDeltaY() {
        return deltaY;
    }

    public void setDeltaY(long deltaY) {
        this.deltaY = deltaY;
    }
}
