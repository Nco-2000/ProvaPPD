package src;

public class Fork {
    
    private String name;

    private Philosopher user;

    public Fork (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Philosopher getUser() {
        return this.user;
    }

    public synchronized void pickUp(Philosopher philosopher) throws InterruptedException { //synchronized = Região Crítica.

        while (this.user != null && !this.user.equals(philosopher)) { 
            wait();    
        }

        this.user = philosopher;

    }

    public synchronized void pickDown(Philosopher philosopher) {
        if (this.user == (philosopher)) {
            this.user = null;
            notifyAll(); //Libera thread bloqueada, que realizará o pickUp(), já quem deixou o garfo vai pensar.
        }
    }

    public boolean isBeingUsed() {
        return this.user != null;
    }

}
