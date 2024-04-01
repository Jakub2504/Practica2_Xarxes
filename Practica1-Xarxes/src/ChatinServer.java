import java.io.*;
import java.net.*;

public class ChatinServer {
    private static final int PORT = 1234;
    private static ServerSocket serverSocket;
    private static Socket socket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Chatín: «el xat pequeñín»");
            System.out.println("Esperant connexió del client...");
            socket = serverSocket.accept();
            System.out.println("Client connectat.");

            // Establir canals d'entrada i sortida de dades.
            DataInputStream clientIn = new DataInputStream(socket.getInputStream());
            DataOutputStream serverOut = new DataOutputStream(socket.getOutputStream());

            // Manejar la señal SIGINT per tancar el servidor correctament
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Tancant el servidor...");
                    serverOut.writeUTF("FI"); // Enviar missatge de tancament al client
                    serverOut.flush();
                    socket.close();
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("Error al tancar el servidor: " + e.getMessage());
                }
            }));

            // Crear thread per llegir missatges del client
            Thread clientReaderThread = new Thread(() -> {
                try {
                    String clientInput;
                    while ((clientInput = clientIn.readUTF()) != null) {
                        if (!clientInput.isEmpty()) {
                            System.out.println("Client: " + clientInput);
                        }
                        if (clientInput.equalsIgnoreCase("FI")) {
                            break;
                        }
                    }
                    System.out.println("El client ha finalitzat la conversa.");
                    System.exit(0); // Acabar el servidor quan es finalitza desde la consola del client
                } catch (IOException e) {
                    System.err.println("Error al llegir del client: " + e.getMessage());
                }
            });
            clientReaderThread.start();

            // Crear thread per llegir missatges des de la consola i enviar-los al client.
            Thread consoleReaderThread = new Thread(() -> {
                try {
                    BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
                    String userInput;
                    while ((userInput = consoleIn.readLine()) != null) {
                        if (!userInput.isEmpty()) {
                            serverOut.writeUTF(userInput);
                            serverOut.flush();
                        }
                        if (userInput.equalsIgnoreCase("FI")) {
                            break;
                        }
                    }
                    System.out.println("Finalitzada la conversa.");
                    System.exit(0); // Acabar el servidor quan es finalitza desde la consola.
                } catch (IOException e) {
                    System.err.println("Error al llegir de la consola: " + e.getMessage());
                }
            });
            consoleReaderThread.start();

        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}
