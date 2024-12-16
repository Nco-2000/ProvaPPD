package src;

import java.util.Random;

public class Philosopher implements Runnable{
    
    private ClientProcessor associatedClientProcessor;

    private String name;
    private int id;

    private Fork leftFork;
    private Fork rightFork;

    private int numberOfMeals;
    private int numberOfThoughts;

    private final Random random = new Random();

    private String state = "Getting up";

    public Philosopher(int id, String name, ClientProcessor clientProcessor) {
        this.id = id;
        this.name = name;
        this.associatedClientProcessor = clientProcessor;
        this.numberOfMeals = 0;
        this.numberOfThoughts = 0;
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

    public String getState() {
        return this.state;
    }

    public void setState(String newState) {
        this.state = newState;
    }

    public Fork getLeftFork() {
        if (this.hasLeftFork()) {
            return this.leftFork;
        }
        return null;
    }

    public Fork getRightFork() {
        if (this.hasRightFork()) {
            return this.rightFork;
        }
        return null;
    }

    public boolean hasLeftFork() {
        if(this.leftFork == null) {
            return false;
        }
        return this.leftFork.isBeingUsedBy(this);
    }

    public boolean hasRightFork() {
        if(this.rightFork == null) {
            return false;
        }
        return this.rightFork.isBeingUsedBy(this);
    }

    public boolean hasBothForks() {
        return this.hasLeftFork() && this.hasRightFork();
    }

    public void assignForks(Fork rightFork, Fork leftFork) {
        this.rightFork = rightFork;
        this.leftFork = leftFork;
    }
    
    private void sleep(int multiplier)
    {
        try {
            Thread.sleep(multiplier);
        } catch (InterruptedException e) {
        }
    }

    public void think() { 
        this.state = "thinking";
        this.numberOfThoughts++;
        this.thinking();
    }

    public void thinking() {
        // Média em milissegundos
        double average = 5000; // 5 segundos convertidos para milissegundos
        // Desvio padrão em milissegundos
        double stdDev = 2000; // 2 segundos convertidos para milissegundos

        // Gera valor aleatório seguindo a distribuição normal
        double gaussian = random.nextGaussian() * stdDev + average;

        // Garante que o tempo não seja negativo
        int thinkingTime = (int) Math.max(gaussian, 0);

        // Simula o tempo de pensamento
        this.sleep(thinkingTime);
    }

    private void eat() throws InterruptedException {
        if(this.state == "DESACTIVATING") {
            this.stop();
            return;
        }
        
        this.state = "Getting forks";

        while (!this.hasBothForks()) { //Tenta pegar o garfo esquerdo, espera até conseguir, se o gardo da direita estiver ocupado ele larga o gardo da esquerda e tenta tudo de novo.
            
            this.leftFork.pickUp(this);
            this.state = "LEFT FORK (" + this.leftFork.getName() + ") PICKED UP";

            if(!this.rightFork.isBeingUsed())
            {
                this.rightFork.pickUp(this); //Se entre a checagem e ele tentar pegar, outro filósofo pegar, ele toma um segundo wait, nesse caso ele não vai largar o primeiro garfo.
                this.state = "RIGHT FORK (" + this.rightFork.getName() + ") PICKED UP";
            } else {
                this.leftFork.pickDown(this);
            }

            if(!this.hasBothForks()) {
                if(this.hasLeftFork())
                    this.leftFork.pickDown(this);
                if(this.hasRightFork())
                    this.rightFork.pickDown(this);
            }
        }

        this.state = "Eating";
        this.eating();

        this.state = "Releasing forks";
        while (this.hasBothForks()) {
            this.leftFork.pickDown(this);
            this.rightFork.pickDown(this);
        }        
    }

    public void eating() {
        this.numberOfMeals++;

        this.sleep(1000);
    }

    @Override
    public void run() {
        try {
            while (this.state != "DESACTIVATING" || !Thread.currentThread().interrupted()) {
                this.think();
                this.eat();
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        if(this.hasLeftFork())
            this.leftFork.pickDown(this);
        if(this.hasRightFork())
            this.rightFork.pickDown(this);
    }

    public void stop() {
        this.state = "DESACTIVATING";
        if (this.hasLeftFork()) {
            this.leftFork.pickDown(this);
        }
        if (this.hasRightFork()) {
            this.rightFork.pickDown(this);
        }
        try {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
        }
    }
}
