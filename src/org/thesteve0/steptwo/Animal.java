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
 * An animal moving across the landscape
 *
 * TODO this is really starting to feel like all the movement needs to be in the move method - cover the following things
 1. Generate a new move - make sure to keep direction in the same direction as lastMove
 2. Check to see if someone is already there - if so then move again - this seems recursive -
 basically keep trying to move until we get an empty spot
 3. Update lastMove to be the move we settled on

 TODO finally, once we get this all working then we need to refactor this as a base class to allow people to inherit from
 when creating a new animal model. There will probably be some base parameters object you pass in for each animal type, including
 things like movement parameters, habitat preference matrix, habitat resistance matrix, and habitat mortality matrix.
 probably don't need the resistance matrix

 */


public class Animal implements Steppable {
    private static final long serialVersionUID = 1;

    private int xdir;  // -1, 0, or 1
    private int ydir;  // -1, 0, or 1

    //Name of the individual animal
    private int name = this.hashCode();

    //The SimState that runs the Simulation
    private Runner runner = null;

    //The Last move for this animal - should only be null on first move
    private Move lastMove = null;



    //8  will produce a very peaked circular distribution - smaller number make it flatter
    private VonMises vonMises = null;

    public Animal(Runner runner) {
        this.lastMove = new Move();
        this.runner = runner;
        vonMises = new VonMises(0.0000001, new ec.util.MersenneTwisterFast(runner.random.nextLong()));
        for (int i = 0; i < 1024; i++) {
            vonMises.nextDouble();
        }
    }


    /**
     * This currently just gives the x and y offset to move from the current location.
     * Perhaps this should actually be the new coordinates. I see this evolving quite a bit once
     * we add picking direction based on underlying habitat.
     * <p>
     * Eventually they are going to have to change their direction based on some cumulative benefit of
     * habitat in their perceptual range. Need more thought about to implement this. For now we are agnostic of habitat
     * <p>
     * Movement durations are going to have to be very short per the animals movement rate, thereby reducing
     * perceptual range. But they we are balancing perceptual range with tendency for most animals to keep walking in a
     * straight line
     * <p>
     * Need to also think through how this would look for a mountain lion versus a tiger salamander
     *
     * @return A @Move object which just holds the change in x and y
     */
    private Move makeAMove() {
        Move move = new Move();

        //Todo given how an angle of 0 means no movement in the Y direction I need to change this
        //A result of 0 should really mean keep going in the same direction. I think this means pass in the
        // last angle and then add the direction of the random distribution to the current angle

        //This result is in radians
        double direction = vonMises.nextDouble();


        //A random distance between 0 and the parameter non-inclusive
        int dist = this.runner.random.nextInt(20);


        //Use sin and cos to calculate deltas for x and y and put them in the move object

        long x = Math.round(dist * Math.cos(direction));
        long y = Math.round(dist * Math.sin(direction));

        //System.out.println("X:Y " + x + ":" + y);
        move.setDeltaX(x);
        move.setDeltaY(y);

        return move;
    }


    public void step(SimState state) {


        Move moveResult = makeAMove();

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

        // TODO this is broken currently - I need to figure out to reflect properly
        // I think part of the problem is how I do the move up above - this may really need some refactoring
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


        // make my new location
        Int2D newloc = new Int2D(newx, newy);
        //Rather than doing this below in that loop - give a first movers advantage.
        //If there is someone already in the spot then go ahead and recalculate a new position

        runner.particles.setObjectLocation(this, newloc);

        // TODO this is animal Dependent so I need to make it optional - mtn lions won't share a cell but tiger salamanders will
        // randomize everyone at that location if need be
        Bag p = runner.particles.getObjectsAtLocation(newloc);
        if (p.numObjs > 1) {
            for (int x = 0; x < p.numObjs; x++)

                //I think in the future I want to actually move this above and move
                //off the spot

                System.out.println("Some points are on top of each other");

        }

        //TODO output stats here

    }
}
