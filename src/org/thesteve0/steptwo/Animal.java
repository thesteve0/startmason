package org.thesteve0.steptwo;/*
  Copyright 2006 by Sean Luke and George Mason University
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.distribution.VonMises;

/**
 * A bouncing particle.
 */

public class Animal implements Steppable {
    private static final long serialVersionUID = 1;

    public int xdir;  // -1, 0, or 1
    public int ydir;  // -1, 0, or 1
    private int name;

    //8  will produce a very peaked circular distribution - smaller number make it flatter
    private VonMises vonMises = new VonMises(0.0000001, new ec.util.MersenneTwisterFast());

    public Animal() {
        for (int i = 0; i < 1024; i++) {
            vonMises.nextDouble();
        }
    }

    public Animal(int xdir, int ydir) {
        this.xdir = xdir;
        this.ydir = ydir;

    }


    private Move figureNextMove(Runner runner) {
        Move move = new Move();

        //This result is in radians
        double direction = vonMises.nextDouble();
        //System.out.println(Math.toDegrees(direction));

        //A random distance between 0 and the parameter non-inclusive
        int dist = runner.random.nextInt(20);


        //Use sin and cos to calculate deltas for x and y and put them in the move object

        long x = Math.round(dist * Math.cos(direction));
        long y = Math.round(dist * Math.sin(direction));

        //System.out.println("X:Y " + x + ":" + y);
        move.setDeltaX(x);
        move.setDeltaY(y);

        return move;
    }


    public void step(SimState state) {
        Runner runner = (Runner) state;

        Move moveResult = figureNextMove(runner);

        // We could just store my location internally, but for purposes of
        // show, let's get my position out of the particles grid
        Int2D location = runner.particles.getObjectLocation(this);

        /*// Randomize my direction if requested
        if (randomize)
        {
            xdir = runner.random.nextInt(3) - 1;
            ydir = runner.random.nextInt(3) - 1;
            randomize = false;
        }*/

        // move

        int newx = new Long(location.x + moveResult.getDeltaX()).intValue();
        int newy = new Long(location.y + moveResult.getDeltaY()).intValue();

        // reverse course if hitting boundary
        if (newx < 0) {
            newx++;
            xdir = -xdir;
        } else if (newx >= runner.gridWidth) {
            newx--;
            xdir = -xdir;
        }
        if (newy < 0) {
            newy++;
            ydir = -ydir;
        } else if (newy >= runner.gridHeight) {
            newy--;
            ydir = -ydir;
        }

        //TODO output stats here

        // set my new location
        Int2D newloc = new Int2D(newx, newy);
        runner.particles.setObjectLocation(this, newloc);

        // randomize everyone at that location if need be
        Bag p = runner.particles.getObjectsAtLocation(newloc);
        if (p.numObjs > 1) {
            for (int x = 0; x < p.numObjs; x++)
                //I think in the future I want to actually move this above and move
                //off the spot
                System.out.println("Some points are on top of each other");
        }
    }
}
