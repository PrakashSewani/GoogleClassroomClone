import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserServer {

    public static final int LOGIN_CODE = 100;
    public static final int REGISTER_CODE = 101;
    public static final int UPDATE_CODE = 102;

    private ServerSocket loginServer;
    private Socket loginClient;
    private BufferedReader reader;
    private PrintWriter writer;

    UserServer() {
        try {
            loginServer = new ServerSocket(4241);
            System.out.println("User Server Started.\n" + loginServer);
            while(true) {
                loginClient = loginServer.accept();
                System.out.println(loginClient.getInetAddress().getCanonicalHostName());
                System.out.println(loginClient);
                reader = new BufferedReader(new InputStreamReader(loginClient.getInputStream()));
                writer = new PrintWriter(loginClient.getOutputStream(), true);

                int code = Integer.parseInt(reader.readLine());
                System.out.println(code);
                switch(code) {
                    case LOGIN_CODE:
                        loginUser();
                        break;
                    case REGISTER_CODE:
                        registerUser();
                        break;
                    case UPDATE_CODE:
                        updateUser();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (loginClient != null) {
                    loginClient.close();
                }
                if (loginServer != null) {
                    loginServer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loginUser() {
        try {
            String email = reader.readLine();
            String password = reader.readLine();

            System.out.println("Email id: " + email + "\nPassword: " + password);
            ResultSet rs = DatabaseHandler.verifyUser(email, password);
            if(rs != null && rs.next()) {
                writer.println(true);
                System.out.println(true);
                writer.println(rs.getInt("id"));
                writer.println(rs.getString("name"));
                writer.println(rs.getString("email"));
                writer.println(rs.getString("password"));
            } else {
                writer.println(false);
                System.out.println(false);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void registerUser() {
        try {
            String name = reader.readLine();
            String email = reader.readLine();
            String password = reader.readLine();

            System.out.println("Name: " + name + "\nEmail: " + email + "\nPassword: " + password);
            writer.println(DatabaseHandler.registerUser(name, email, password));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUser() {
        try {
            int id = Integer.parseInt(reader.readLine());
            String name = reader.readLine();
            String email = reader.readLine();
            String password = reader.readLine();
            System.out.println(id + name + email + password);
            writer.println(DatabaseHandler.updateUser(id, name, email, password));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
