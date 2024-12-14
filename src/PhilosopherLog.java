package src;

public class PhilosopherLog {

    private int id;

    private String name;

    private int numberOfMeals;
    private int numberOfThoughts;

    public PhilosopherLog (int id, String name, int numberOfMeals, int numberOfThoughts) {
        this.id = id;
        this.name = name;
        this.numberOfMeals = numberOfMeals;
        this.numberOfThoughts = numberOfThoughts;
    }

    public PhilosopherLog (Philosopher philosopher) {
        this.id = philosopher.getId();
        this.name = philosopher.getName();
        this.numberOfMeals = philosopher.getNumberOfMeals();
        this.numberOfThoughts = philosopher.getNumberOfThoughts();
    }

    public int getId () {
        return this.id;
    }

    public int getNumberOfMeals () {
        return this.numberOfMeals;
    }

    public int getNumberOfThoughts () {
        return this.numberOfThoughts;
    }
    
    public String getName() {
        return this.name;
    }
}
