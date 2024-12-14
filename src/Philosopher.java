package src;

public class Philosopher implements Runnable{
    
    private ClientProcessor associatedClientProcessor;

    private String name;
    private int id;

    private Fork leftFork;
    private Fork rightFork;

    private int numberOfMeals;
    private int numberOfThoughts;

    private String state = "Getting up";

    public Philosopher(int id, String name, ClientProcessor clientProcessor) {
        this.id = id;
        this.name = name;
        this.associatedClientProcessor = clientProcessor;
        this.state = "Waiting for fork assignmet.";
    }

    public Philosopher(int id, String name, ClientProcessor clientProcessor, int numberOfMeals, int numberOfThoughts) {
        this.id = id;
        this.name = name;
        this.associatedClientProcessor = clientProcessor;
        this.numberOfMeals = numberOfMeals;
        this.numberOfThoughts = numberOfThoughts;
        this.state = "Waiting for fork assignmet.";
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getNumberOfMeals() {
        return this.numberOfMeals;
    }

    public int getNumberOfThoughts() {
        return this.numberOfThoughts;
    }

    public void assignForks(Fork rightFork, Fork leftFork) {
        this.rightFork = rightFork;
        this.leftFork = leftFork;
        //this.state = "Assigned forks";
    }
    
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
