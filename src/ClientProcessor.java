package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientProcessor implements Runnable{

    private Socket socket;
    private int Id;

    private Philosopher philisopher;
    private PhilosophersManager philosophersManager;
    private PhilosophersManagerGUI gui;

    public ClientProcessor(Socket socket, PhilosophersManager philosophersManager) {
        this.socket = socket;
        this.philosophersManager = philosophersManager;
    }

    private void close() { //encerra a conexao do cliente.
        try {
            this.socket.close(); //fecha o soecket.
        } catch(IOException e) {
            System.out.println("FATAL ERROR: " + e + " WHILE CLOSING CONNECTION.");
        }
    }

    public String getIdFromMessage(String message) {
        String regex = "[0-9]{1,}";

        Pattern pattern = Pattern.compile(regex); //Usando regex, pattern e matcher para encontrar algo definido dentro da string.
        Matcher matcher = pattern.matcher(message);
        
        if (matcher.find()) {
            return matcher.group(); //retorna o email, caso encontrado.
        } else {
            return null; //retona null caso nao tenha encontrado um email formatado corretamente.
        }
    }

    @Override
    public void run() {
        
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            out.println("220 Connected");

            String userInput;
            while((userInput = in.readLine().toUpperCase()) != null) {
                if (userInput.startsWith("HELLO")) {
                    out.println("HI " + this.Id);
                }

                else if (userInput.startsWith("SETID")) {
                    String newId = this.getIdFromMessage(userInput);
                    if (this.philisopher == null) {

                        if (newId != null) {
                            this.Id = Integer.valueOf(newId);
                            out.println("NEW ID SAVED: " + newId);
                        } else {
                            out.println("INVALID ID.");
                        }
                    } else {
                        out.println("CANNOT CHANGE ID WHILE A PHILOSOPHER WITH THE CURRENT ID IS ACTIVE.");
                    }
                }

                else if (userInput.startsWith("GETID")) {
                    out.println("ID: " + this.Id);
                }

                else if (userInput.startsWith("SHOWTABLE")) {
                    if (this.gui == null) {
                        this.gui = new PhilosophersManagerGUI(this.philosophersManager);
                    } else {
                        out.println("THE GUI IS ALREADY OPENED.");
                    }
                }

                else if (userInput.startsWith("HIDETABLE")) {
                    if (this.gui != null) {
                        this.gui.closeGUI();
                        this.gui = null;
                    }
                    else {
                        out.println("NO GUI IS OPEN TO CLOSE.");
                    }
                }

                else if (userInput.startsWith("STATUS")) {
                    out.println("SERVER STATUS:");
                    out.println("\tNUMBER OF PHILOSOPHERS: " + this.philosophersManager.getNumberOfActimePhilosophers());
                    out.println("\tNUMBER OF FORKS: " + this.philosophersManager.getNumberOfForks());
                }

                else if (userInput.startsWith("STATISTICS")) {
                    if (this.philisopher != null) {
                        out.println("SHOWING CURRENT: " + this.philisopher.getName() + " STATISTICS:");
                        out.println("\tID: " + this.philisopher.getId());
                        out.println("\tNUMBER OF MEALS: " + this.philisopher.getNumberOfMeals());
                        out.println("\tNUMBER OF THOUGHTS: " + this.philisopher.getNumberOfThoughts());
                    } else  if (this.philosophersManager.getPhilosopherLog(this.Id) != null) {
                        PhilosopherLog philosopherLog = this.philosophersManager.getPhilosopherLog(this.Id);
                        out.println("SHOWING STORED: " + philosopherLog.getName() + " STATISTICS:");
                        out.println("\tID: " + philosopherLog.getId());
                        out.println("\tNUMBER OF MEALS: " + philosopherLog.getNumberOfMeals());
                        out.println("\tNUMBER OF THOUGHTS: " + philosopherLog.getNumberOfThoughts());
                    } else {
                        out.println("NO RECORD FOR PHILOSOPHER WITH ID: " + this.Id);
                    }
                }

                else if (userInput.startsWith("START")) {
                    if (this.philisopher != null) {
                        out.println("ERROR: THIS CLIENT ALREADY HAS A RUNNING PHILOSOPHER WITH ID: " + this.philisopher.getId());
                    } else {
                        if (this.Id == 0) {
                            this.philisopher = this.philosophersManager.createPhilosopher(this);
                            this.Id = this.philisopher.getId();
                        } else {
                            this.philisopher = this.philosophersManager.createPhilosopher(this, this.Id);
                        }
                        if (this.philisopher != null) {
                            out.println("PHILOSOPHER: " + this.philisopher.getName() + " CREATED WITH ID: " + this.philisopher.getId());
                        } else {
                            out.println("ERROR: A PHILOSOHER WITH ID: " + this.Id + " ALREADY EXISTS!");
                        }
                    }
                }

                else if (userInput.startsWith("STOP")) {
                    if (this.philisopher != null) {
                        this.philosophersManager.deletePhilosopher(this.Id);
                        this.philisopher = null;
                        out.println("PHILOSOPHER WITH ID: " + this.Id + " DELETED.");
                    } else {
                        out.println("ERROR: THIS CLIENT HAS NO ACCES TO PHILOSOPHER WITH ID: " + this.Id);
                    }
                }

                else if (userInput.startsWith("QUIT")) {
                    out.println("CLOSING CONNECTION..." + this.Id);
                    if (this.philisopher != null) {
                        this.philosophersManager.deletePhilosopher(this.Id);
                    }
                    this.close();
                }

                else {
                    out.println("ERROR: COMMAND UNRECOGNIZED!");
                }
            }


        } catch (IOException e) {
            
        } finally {
            //this.close();
        }


    }
    
    
}
