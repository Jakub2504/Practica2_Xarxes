import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345;
    private static BooksDB booksDB;

    public static void main(String[] args) {
        try {
            booksDB = new BooksDB("booksDB.dat");
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client.Client connected: " + clientSocket.getInetAddress());

                // Handle client request in a separate thread
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException ex) {
            System.err.println("Error starting the server!");
            ex.printStackTrace();
        } finally {
            try {
                if (booksDB != null)
                    booksDB.close();
            } catch (IOException ex) {
                System.err.println("Error closing database!");
            }
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

            int option;
            while ((option = in.readInt()) != 5) {
                switch (option) {
                    case 1:
                        listTitles(out);
                        break;
                    case 2:
                        getBookInfo(in, out);
                        break;
                    case 3:
                        addBook(in, out);
                        break;
                    case 4:
                        deleteBook(in, out);
                        break;
                    default:
                        out.writeUTF("Invalid option!");
                }
            }
            clientSocket.close();
            System.out.println("Client.Client disconnected: " + clientSocket.getInetAddress());
        } catch (IOException ex) {
            System.err.println("Error handling client request!");
            ex.printStackTrace();
        }
    }

    private static void listTitles(DataOutputStream out) throws IOException {
        int numBooks = booksDB.getNumBooks();
        out.writeInt(numBooks);

        for (int i = 0; i < numBooks; i++) {
            BookInfo book = booksDB.readBookInfo(i);
            out.writeUTF(book.getTitle());
        }
    }

    private static void getBookInfo(DataInputStream in, DataOutputStream out) throws IOException {
        String title = in.readUTF();
        int index = booksDB.searchBookByTitle(title);

        if (index != -1) {
            BookInfo book = booksDB.readBookInfo(index);
            out.writeUTF("FOUND");
            byte[] bookBytes = book.toBytes();
            out.writeInt(bookBytes.length);
            out.write(bookBytes);
        } else {
            out.writeUTF("NOT_FOUND");
        }
    }

    private static void addBook(DataInputStream in, DataOutputStream out) throws IOException {
        String bookDetails = in.readUTF();
        String[] details = bookDetails.split(", ");

        if (details.length < 2 || details.length > 4) {
            out.writeUTF("Invalid book details format!");
            return;
        }

        String title = details[0];
        int pages = Integer.parseInt(details[1]);
        String author = (details.length >= 3) ? details[2] : "";
        String series = (details.length == 4) ? details[3] : "";

        BookInfo book = new BookInfo(title, pages, author, series);
        boolean success = booksDB.insertNewBook(book);

        if (success) {
            out.writeUTF("ADDED");
        } else {
            out.writeUTF("ALREADY_EXISTS");
        }
    }


    private static void deleteBook(DataInputStream in, DataOutputStream out) throws IOException {
        String title = in.readUTF();
        boolean success = booksDB.deleteByTitle(title);

        if (success) {
            out.writeUTF("DELETED");
        } else {
            out.writeUTF("NOT_FOUND");
        }
    }
}
