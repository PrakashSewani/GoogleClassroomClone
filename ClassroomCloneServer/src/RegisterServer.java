import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RegisterServer {

    ServerSocket registerServer;
    Socket registerClient;
    BufferedReader reader;
    PrintWriter writer;

    RegisterServer() {
        try {
            registerServer = new ServerSocket(4240);
            System.out.println("Register Server Started.\n" + registerServer);
            while(true) {
                registerClient = registerServer.accept();
                System.out.println(registerClient.getInetAddress().getCanonicalHostName());
                System.out.println(registerClient);
                reader = new BufferedReader(new InputStreamReader(registerClient.getInputStream()));
                writer = new PrintWriter(registerClient.getOutputStream(), true);
                new Thread(this::readCredentialsFromSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readCredentialsFromSocket() {
        try {
            String name = reader.readLine();
            String email = reader.readLine();
            String password = reader.readLine();
            registerCredentials(name, email, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerCredentials(String name, String email, String password) {
        System.out.println("Name: " + name + "\nEmail: " + email + "\nPassword: " + password);
        writer.println(DatabaseHandler.registerUser(name, email, password));
    }
}
