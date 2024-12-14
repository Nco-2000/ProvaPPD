package src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhilosophersManager {
    
    public int lastUsedId = 0;

    public int numberOfActivePhilosophers = 0;
    public List<Philosopher> activePhilosophers = new ArrayList<Philosopher>();

    public List<PhilosopherLog> registeredPhilosophers = new ArrayList<PhilosopherLog>();

    private int numberOfForks;
    public List<Fork> forks = new ArrayList<Fork>();

    public int getNumberOfActimePhilosophers() {
        return this.numberOfActivePhilosophers;
    }

    public int getNumberOfForks() {
        return this.numberOfForks;
    }

    public synchronized int createUniqueId() {
        this.lastUsedId++;
        return this.lastUsedId;
    }

    public synchronized Philosopher getPhilosopher(int id) {
        for (Philosopher philosopher : activePhilosophers) {
            if (philosopher.getId() == id) {
                return philosopher;
            }
        }
        return null;
    }

    public synchronized Philosopher createPhilosopher(ClientProcessor clientProcessor) {
        Integer newId = this.createUniqueId();
        Philosopher newPhilosopher = new Philosopher(newId, ("Philosopher_" + newId.toString()), clientProcessor);
        this.activePhilosophers.add(newPhilosopher);
        this.numberOfActivePhilosophers++;
        this.remanageAllForks();
        return newPhilosopher;
    }

    public synchronized Philosopher createPhilosopher(ClientProcessor clientProcessor, Integer id) {
        if (this.getPhilosopher(id) == null) {
            if (this.getPhilosopherLog(id) == null) { //Se o id nao for de um filosofo em uso, mas for de um nunca antes registrado
                Philosopher newPhilosopher = new Philosopher(id, ("Philosopher_" + id.toString()), clientProcessor);
                this.activePhilosophers.add(newPhilosopher);
                this.numberOfActivePhilosophers++;
                this.remanageAllForks();
                return newPhilosopher;
            } else { //Se não, carregar um filosofo.
                PhilosopherLog log = this.getPhilosopherLog(id);
                Philosopher reloadedPhilosopher = new Philosopher(id, log.getName(), clientProcessor, log.getNumberOfMeals(), log.getNumberOfThoughts());
                this.activePhilosophers.add(reloadedPhilosopher);
                this.numberOfActivePhilosophers++;
                this.remanageAllForks();
                return reloadedPhilosopher;
            }
        }
        return null;
    }

    public synchronized boolean deletePhilosopher(int id) {
        for (Iterator<Philosopher> iterator = activePhilosophers.iterator(); iterator.hasNext();) {
            Philosopher philosopher = iterator.next();
            if (philosopher.getId() == id) {
                this.savePhilosopherToLog(id);
                iterator.remove();
                this.numberOfActivePhilosophers--;
                this.remanageAllForks();
                return true;
            }
        }
        return false;
    }

    public PhilosopherLog getPhilosopherLog(int id) {
        for (PhilosopherLog philosopherLog : this.registeredPhilosophers) {
            if (philosopherLog.getId() == id) {
                return philosopherLog;
            }
        }
        return null;
    }

    public PhilosopherLog savePhilosopherToLog(int id) {
        Philosopher activePhilosopher = this.getPhilosopher(id);

        PhilosopherLog philosopherLog = new PhilosopherLog(activePhilosopher);

        if (this.getPhilosopherLog(id) != null) {
            this.registeredPhilosophers.set(id - 1, philosopherLog);
        } else {
            this.registeredPhilosophers.add(philosopherLog);
        }
        return philosopherLog;
    }

    public synchronized void remanageAllForks() {
        if (this.numberOfActivePhilosophers == 1) {
            this.numberOfForks = 2;
        } else {
            this.numberOfForks = numberOfActivePhilosophers;
        }

        if (this.forks.size() < this.numberOfForks) {
            int forksToAdd = this.numberOfForks - this.forks.size();
            for (int i = 0; i < forksToAdd; i++) {
                if (this.forks.isEmpty()) {
                    this.forks.add(new Fork(1, "FORK_1"));
                } else {
                    this.forks.add(new Fork(this.forks.getLast().getId() + 1, "FORK_" + this.forks.getLast().getId().toString()));
                }
            }
        } else if (this.forks.size() > this.numberOfForks) {
            int forksToDelete = this.forks.size() - this.numberOfForks;
            for (int i = 0; i < forksToDelete; i++) {
                this.forks.remove(this.forks.getLast());
            }
        }

        for (int i = 0; i < this.numberOfActivePhilosophers; i++) {
            Philosopher philosopher = this.activePhilosophers.get(i);
    
            Fork leftFork = this.forks.get(i);
            Fork rightFork = this.forks.get((i + 1) % this.forks.size());
            
            philosopher.assignForks(rightFork, leftFork);
        }
    }
    
}