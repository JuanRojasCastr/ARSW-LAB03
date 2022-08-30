package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private int health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());

    private final Object lock;

    private boolean paused = false;


    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb, Object lock) {
        super(name);
        this.lock = lock;
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue=defaultDamageValue;
    }

    public void run() {

        while (immortalsPopulation.size() > 1) {
            Immortal im;

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            synchronized (lock) {
                while (paused) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.fight(im);
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println();
        updateCallback.processReport(immortalsPopulation.get(0).toString() + "WINS" );

    }

    public void fight(Immortal i2) {
        if (i2.getHealth() > 0) {
            i2.changeHealth(i2.getHealth() - defaultDamageValue);
            if (i2.getHealth() == 0) {
                immortalsPopulation.remove(i2);
                i2.pauseInm();
            }
            this.health += defaultDamageValue;
            updateCallback.processReport("Fight: " + this + " vs " + i2+"\n");
        } else {
            updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
        }

    }

    public void changeHealth(int v) {
        health = v;
    }

    public int getHealth() {
        return health;
    }

    public void pauseInm() {
        this.paused = true;
    }

    public void resumeInm() {
        this.paused = false;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

}
