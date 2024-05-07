package Client;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");

            while (true) {
                printMenu();
                int option = getOption(userInput);
                out.writeInt(option);

                switch (option) {
                    case 1:
                        listTitles(in);
                        break;
                    case 2:
                        getBookInfo(userInput, out, in);
                        break;
                    case 3:
                        addBook(userInput, out, in);
                        break;
                    case 4:
                        deleteBook(userInput, out, in);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option!");
                }
            }
        } catch (IOException ex) {
            System.err.println("Error connecting to server!");
            ex.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println("Menu:");
        System.out.println("1 - List all titles");
        System.out.println("2 - Get book info");
        System.out.println("3 - Add a book");
        System.out.println("4 - Delete a book");
        System.out.println("5 - Quit");
    }

    private static int getOption(BufferedReader userInput) throws IOException {
        System.out.print("Enter option: ");
        try {
            String input = userInput.readLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Usage for selecting option = integer.");
            return 0;
        }
    }

    private static void listTitles(DataInputStream in) throws IOException {
        int numBooks = in.readInt();
        System.out.println("Number of books: " + numBooks);

        for (int i = 0; i < numBooks; i++) {
            String title = in.readUTF();
            System.out.println(title);
        }
    }

    private static void getBookInfo(BufferedReader userInput, DataOutputStream out, DataInputStream in) throws IOException {
        System.out.print("Enter book title: ");
        String title = userInput.readLine();
        out.writeUTF(title);

        String response = in.readUTF();
        if (response.equals("FOUND")) {
            int length = in.readInt();
            byte[] bookBytes = new byte[length];
            in.readFully(bookBytes);
            BookInfo book = BookInfo.fromBytes(bookBytes);
            System.out.println(book);
        } else {
            System.out.println("Book not found.");
        }
    }

    private static void addBook(BufferedReader userInput, DataOutputStream out, DataInputStream in) throws IOException {
        System.out.print("Enter book details (Title, Pages, Author, Series): ");
        String bookDetails = userInput.readLine();
        out.writeUTF(bookDetails);

        String response = in.readUTF();
        if (response.equals("ADDED")) {
            System.out.println("Book added successfully.");
        } else if (response.equals("ALREADY_EXISTS")) {
            System.out.println("This book already exists in the database.");
        }else if (response != "ADDED" || response != "ALREADY_EXISTS"){
            System.out.println("Inavlid book details format.");
        }
    }

    private static void deleteBook(BufferedReader userInput, DataOutputStream out, DataInputStream in) throws IOException {
        System.out.print("Enter book title to delete: ");
        String title = userInput.readLine();
        out.writeUTF(title);

        String response = in.readUTF();
        if (response.equals("DELETED")) {
            System.out.println("Book deleted successfully.");
        } else if (response.equals("NOT_FOUND")) {
            System.out.println("Book not found.");
        } else {
            System.out.println(response); // Error message from server
        }
    }
}
