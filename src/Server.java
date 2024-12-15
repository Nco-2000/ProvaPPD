package src;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    
    private int port;
    private String serverName;

    private PhilosophersManager philosophersManager = new PhilosophersManager();
    public PhilosophersManagerGUI gui;

    public Server () {
        this.port = 12345;
        this.gui = new PhilosophersManagerGUI(this.philosophersManager);
    }

    public Server (int port) {
        this.port = port;
        this.gui = new PhilosophersManagerGUI(this.philosophersManager);
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
