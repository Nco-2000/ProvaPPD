package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    
    private int port;
    private String serverName;
    private int numberOfForks;

    private PhilosophersManager philosophersManager = new PhilosophersManager();

    public Server () {
        this.port = 12345;
        this.numberOfForks = 5;
    }

    public Server (int port, int numberOfForks) {
        this.port = port;
        this.numberOfForks = numberOfForks;
    }

    public void start() throws IOException {

        try (ServerSocket server = new ServerSocket(this.port)) {
            
            System.out.println("Servidor iniciado na porta: " + this.port);

            while (true) { 
                new Thread(new ClientProcessor(server.accept(), philosophersManager)).start();
            }
        }
    }
}
