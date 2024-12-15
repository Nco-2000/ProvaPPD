package src;

public class Fork {
    
    private String name;
    private int id;

    private Philosopher user;

    public Fork (int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Philosopher getUser() {
        return this.user;
    }

    public String getUserName() {
        try {
            if (this.getUser() != null) {
                return this.user.getName();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
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

    public boolean isBeingUsedBy(Philosopher philosopher) {
        try {
            if (this.user.equals(philosopher)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public synchronized void delete() {
        this.user = null;
    }

}
