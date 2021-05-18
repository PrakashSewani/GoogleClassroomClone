package backend;

import UI.LMSConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserClient {
    private static final String TAG = "UserClient: ";

    public static final int LOGIN_CODE = 100;
    public static final int REGISTER_CODE = 101;
    public static final int UPDATE_CODE = 102;

    private Socket userSocket;
    private PrintWriter writer;
    private BufferedReader reader;

    public static int userId;
    public static String emailId;
    public static String userName;
    public static String password;

    private void initConnections() {
        try {
            userSocket = new Socket(LMSConstants.SERVER_ADDR, LMSConstants.USER_SERVER_PORT);
            System.out.println(TAG + "initConnection: " + userSocket);
            System.out.println(TAG + "initConnection: " + userSocket.getInetAddress().getCanonicalHostName());
            writer = new PrintWriter(userSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean loginUser(String email, String passwd) {
        initConnections();
        boolean isValid = false;

        try {
            writer.println(LOGIN_CODE);

            writer.println(email);
            writer.println(passwd);
            System.out.println(TAG + "loginUser: " + "Waiting for server...");

            isValid = Boolean.parseBoolean(reader.readLine());
            if (isValid) {
                userId = Integer.parseInt(reader.readLine());
                userName = reader.readLine();
                emailId = reader.readLine();
                password = reader.readLine();
                System.out.println(TAG + "loginUser: " + "userId: " + userId);
                System.out.println(TAG + "loginUser: " + "name: " + userName);
                System.out.println(TAG + "loginUser: " + "emailId: " + emailId);
                System.out.println(TAG + "loginUser: " + "password: " + password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return isValid;
    }

    public boolean registerUser(String name, String email, String password) {
        initConnections();
        boolean isRegistered = false;

        try {
            writer.println(REGISTER_CODE);

            writer.println(name);
            writer.println(email);
            writer.println(password);
            System.out.println(TAG + "registerUser: " + "Waiting for server...");
            isRegistered = Boolean.parseBoolean(reader.readLine());
            System.out.println(TAG + "registerUser: " + isRegistered);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return isRegistered;
    }

    public boolean updateUser(String name, String email, String password) {
        if(name.equals(UserClient.userName) && email.equals(UserClient.emailId) && password.equals(UserClient.password)) {
            return false;
        }
        initConnections();
        boolean isUpdated = false;

        try {
            writer.println(UPDATE_CODE);

            writer.println(UserClient.userId);
            writer.println(name);
            writer.println(email);
            writer.println(password);

            isUpdated = Boolean.parseBoolean(reader.readLine());

            if(isUpdated) {
                UserClient.userName = name;
                UserClient.emailId = email;
                UserClient.password = password;
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return isUpdated;
    }

    public static void logoutUser() {
        userId = -1;
        userName = "";
        emailId = "";
        password = "";
    }

    private void closeConnections() {
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (userSocket != null) {
                userSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
