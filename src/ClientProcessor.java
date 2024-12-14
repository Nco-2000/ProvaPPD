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

    public ClientProcessor(Socket socket) {
        this.socket = socket;
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
            while((userInput = in.readLine()) != null) {
                if (userInput.startsWith("HELLO")) {
                    out.println("HI " + this.Id);
                }

                else if (userInput.startsWith("SETID")) {
                    String newId = this.getIdFromMessage(userInput);
                    if (newId != null) {
                        this.Id = Integer.valueOf(newId);
                        //TODO garantir que o usuario nao use um id ja em uso, mas possa carregar o progresso de um id ja usado.
                        out.println("NEW ID SAVED: " + newId);
                    } else {
                        out.println("INVALID ID.");
                    }
                }

                else if (userInput.startsWith("GETID")) {
                    out.println("ID: " + this.Id);
                }

                else if (userInput.startsWith("START")) {
                    //TODO fazer o iniciar o filosofo.
                }

                else if (userInput.startsWith("QUIT")) {
                    out.println("CLOSING CONNECTION..." + this.Id);
                    this.close();
                }

                else {
                    out.println("ERROR: COMMAND UNRECOGNIZED!");
                }
            }


        } catch (IOException e) {
            System.out.println("FATAL ERROR: " + e);
        }


    }
    
    
}
