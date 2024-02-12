import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class LoginClient {
    private static final String HOST = "localhost";
    private static final int PORT = 5678; // Must match the server's port

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            // Get username and password from the user
            System.out.println("Enter username:");
            String userName = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            // Send the username and password to the server
            output.writeUTF(userName);
            output.writeUTF(password);
            output.flush();

            // Receive authentication result from server
            boolean isAuthenticated = input.readBoolean();
            if (isAuthenticated) {
                System.out.println("Successful login.");
                // Here you can continue with the change password flow
            } else {
                System.out.println("Username and password pair is incorrect.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
