package org.thesteve0.stepone;/*
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

    public DoubleGrid2D trails;
    public SparseGrid2D particles;

    public int gridWidth = 100;
    public int gridHeight = 100;
    public int numParticles = 100;

    public Runner(long seed) {
        super(seed);
    }

    public void start() {
        super.start();
        trails = new DoubleGrid2D(gridWidth, gridHeight);
        particles = new SparseGrid2D(gridWidth, gridHeight);

        Animal p;

        for (int i = 0; i < numParticles; i++) {
            p = new Animal(random.nextInt(3) - 1, random.nextInt(3) - 1);  // random direction
            schedule.scheduleRepeating(p);
            particles.setObjectLocation(p,
                    //new Int2D(random.nextInt(gridWidth),random.nextInt(gridHeight)));  // random location
                    new Int2D(0, 0));  // random location
        }

        // Schedule the decreaser
        Steppable decreaser = new Steppable() {
            private static final long serialVersionUID = 1;

            public void step(SimState state) {
                // decrease the locations
                trails.multiply(0.9);
            }
        };

        schedule.scheduleRepeating(Schedule.EPOCH, 2, decreaser, 1);
    }

    public static void main(String[] args) {
        doLoop(Runner.class, args);
        System.exit(0);
    }
}
