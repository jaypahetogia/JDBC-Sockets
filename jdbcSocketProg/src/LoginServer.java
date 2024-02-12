import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class LoginServer {
    private static final int PORT = 5678; // Server port

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                try (Socket socket = serverSocket.accept();
                     ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                     ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
                    
                    System.out.println("New client connected");
                    
                    // Read the username and password from the client
                    String userName = input.readUTF();
                    String password = input.readUTF();

                    // Authenticate user
                    ClientService clientService = new ClientService();
                    boolean isAuthenticated = clientService.authenticate(userName, password);

                    // Send response back to the client
                    output.writeBoolean(isAuthenticated);
                    output.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
