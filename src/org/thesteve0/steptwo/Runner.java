package org.thesteve0.steptwo;/*
  Copyright 2006 by Sean Luke and George Mason University
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.DoubleGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;


public class Runner extends SimState {
    private static final long serialVersionUID = 1;

    public DoubleGrid2D locations;
    public SparseGrid2D particles;

    public int gridWidth = 100;
    public int gridHeight = 100;
    public int numParticles = 40;

    public Runner(long seed) {
        super(seed);
    }


    public void start() {
        super.start();
        //This whole parts sets up the simulation - so any simulation initialization goes here

        locations = new DoubleGrid2D(gridWidth, gridHeight);
        particles = new SparseGrid2D(gridWidth, gridHeight);

        Animal p;


        for (int i = 0; i < numParticles; i++) {
            p = new Animal(this);  // random direction

            schedule.scheduleRepeating(p);
            /*
            TODO So we need to set up multiple starting locations - take total #
            divide it into the number of starting points - and then pass in the starting locations
             */
            particles.setObjectLocation(p,
                    //new Int2D(random.nextInt(gridWidth),random.nextInt(gridHeight)));  // random location
                    new Int2D(50, 50));  // random location
        }

        // Schedule the decreaser
        Steppable decreaser = new Steppable() {
            private static final long serialVersionUID = 1;

            public void step(SimState state) {
               /*
               TODO  anything that we want to update each step we do here.
               1. Output grid statistics
               2. Do the animal update in the animal code
                */
            }
        };

        schedule.scheduleRepeating(Schedule.EPOCH, 2, decreaser, 1);
    }

    public static void main(String[] args) {
        doLoop(Runner.class, args);
        System.exit(0);
    }
}
