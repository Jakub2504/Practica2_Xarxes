import java.io.*;
import java.net.Socket;

public class Client {
    private static final int PORT = 1234;
    private static final String HOST = "127.0.0.1";
    private static Socket socket;

    public static void main(String[] args) throws IOException {
        try {
            socket = new Socket(HOST, PORT);
            System.out.println("Connectat al servidor.");

            // Establir canals d'entrada i sortida de dades.
            DataInputStream serverIn = new DataInputStream(socket.getInputStream());
            DataOutputStream clientOut = new DataOutputStream(socket.getOutputStream());

            // Manejar la señal SIGINT per tancar el client correctament
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Tancant el client...");
                    clientOut.writeUTF("FI"); // Enviar missatge de tancament al servidor
                    clientOut.flush();
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error al tancar el client: " + e.getMessage());
                }
            }));
            // Crear thread per llegir missatges des de la consola i enviar-los al servidor.
            Thread consoleReaderThread = new Thread(() -> {
                try {
                    BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
                    String userInput;
                    while ((userInput = consoleIn.readLine()) != null) {
                        if (!userInput.isEmpty()) {
                            clientOut.writeUTF(userInput);
                            clientOut.flush();
                        }
                        if (userInput.equalsIgnoreCase("FI")) {
                            break;
                        }
                    }
                    System.out.println("Finalitzada la conversa.");
                    System.exit(0); // Acabar el client quan es finalitza desde la consola.
                } catch (IOException e) {
                    System.err.println("Error al llegir de la consola: " + e.getMessage());
                }
            });
            consoleReaderThread.start();
        } catch (IOException e) {
            System.err.println("Error en el client: " + e.getMessage());
        }
    }
}

