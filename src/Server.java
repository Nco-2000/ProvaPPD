package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    
    private int port;
    private String serverName;
    private int numberOfForks;

    public Server () {
        this.port = 12345;
        this.numberOfForks = 5;
    }

    public Server (int port, int numberOfForks) {
        this.port = port;
        this.numberOfForks = numberOfForks;
    }

    final List<Fork> forks = new ArrayList<Fork>();
    public void createForks() {
        for (int i = 0; i < numberOfForks; i++) {
            String name = "FORK " + new Integer(i + 1).toString();
            forks.add(new Fork(name));
        }
    }

    public void start() throws IOException {

        this.createForks();

        try (ServerSocket server = new ServerSocket(this.port)) {
            
            System.out.println("Servidor iniciado na porta: " + this.port);

            while (true) { 
                new Thread(new ClientProcessor(server.accept())).start();
            }
        }
    }
}
