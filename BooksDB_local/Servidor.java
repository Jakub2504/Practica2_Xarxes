import java.io.*;
import java.net.*;

public class Servidor {
    private static final int PORT = 1234;
    private static ServerSocket serverSocket;
    private static Socket socket;

    public static void main(String[] args) throws IOException {
        try {

            serverSocket = new ServerSocket(PORT);
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
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}
