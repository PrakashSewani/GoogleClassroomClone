import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class ClassServer {

    public static final int GET_CLASSROOMS_CODE = 400;
    public static final int CREATE_CLASSROOM_CODE = 401;
    public static final int JOIN_CLASSROOM_CODE = 402;
    public static final int UPDATE_CLASSROOM_CODE = 403;
    public static final int GET_CLASSROOM_INFO_CODE = 404;

    private ServerSocket classServer;
    private Socket classClient;
    private BufferedReader reader;
    private PrintWriter writer;

    ClassServer () {
        try {
            classServer = new ServerSocket(4244);
            System.out.println("Class Server Started.\n" + classServer);
            while (true) {
                classClient = classServer.accept();
                System.out.println(classClient);
                reader = new BufferedReader(new InputStreamReader(classClient.getInputStream()));
                writer = new PrintWriter(classClient.getOutputStream(), true);

                int code = Integer.parseInt(reader.readLine());
                System.out.println(code);
                switch(code){
                    case GET_CLASSROOMS_CODE:
                        writeClassroomDataToSocket(Integer.parseInt(reader.readLine()));
                        break;
                    case CREATE_CLASSROOM_CODE:
                        readClassroomDataFromSocket();
                        break;
                    case JOIN_CLASSROOM_CODE:
                        joinClassroomForUser();
                        break;
                    case UPDATE_CLASSROOM_CODE:
                        updateClassroom();
                        break;
                    case GET_CLASSROOM_INFO_CODE:
                        grtClassroomInfo();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }
                if(reader != null) {
                    reader.close();
                }
                if(classClient != null) {
                    classClient.close();
                }
                if(classServer != null) {
                    classServer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeClassroomDataToSocket(int userId) {
        ResultSet rs = DatabaseHandler.getClassroomsForUser(userId);
        boolean isNext = true;
        try {
            if (rs != null && rs.next()) {
                writer.println(true);
                while(isNext) {
                    writer.println(rs.getInt("id"));

                    writer.println(rs.getString("name"));

                    String temp = rs.getString("description");
                    temp = temp.trim();
                    writer.println(temp.toCharArray().length + 2);
                    writer.println(temp);

                    writer.println(rs.getInt("code"));

                    writer.println(rs.getInt("owner_id"));

                    writer.println(rs.getString("username"));

                    isNext = rs.next();
                    writer.println(isNext);
                }
            } else {
                writer.println(false);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void readClassroomDataFromSocket() {
        try {
            String className = reader.readLine();
            System.out.println("className: " + className);

            int length = Integer.parseInt(reader.readLine());
            char[] temp = new char[length];
            reader.read(temp, 0, length);
            String classDescription = String.valueOf(temp);
            classDescription = classDescription.trim();
            System.out.println("classDescription: " + classDescription);

            int ownerId = Integer.parseInt(reader.readLine());
            System.out.println("OwnerId: " + ownerId);

            int classCode = ThreadLocalRandom.current().nextInt(100000, 1000000);
            System.out.println("classCode: " + classCode);

            boolean created = DatabaseHandler.createClassroom(className, classDescription, classCode, ownerId);
            writer.println(created);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void joinClassroomForUser() {
        try {
            int userId = Integer.parseInt(reader.readLine());
            int code = Integer.parseInt(reader.readLine());

            writer.println(DatabaseHandler.joinClassroom(userId, DatabaseHandler.getClassroomId(code)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateClassroom() {
        try {
            int classId = Integer.parseInt(reader.readLine());

            String className = reader.readLine();

            int length = Integer.parseInt(reader.readLine());
            char[] buffer = new char[length];
            reader.read(buffer, 0, length);
            String classDescription = String.valueOf(buffer);
            classDescription = classDescription.trim();
            System.out.println("classDescription: " + classDescription);

            writer.println(DatabaseHandler.updateClassroom(classId, className, classDescription));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void grtClassroomInfo() {
        boolean isNext = true;
        try {
            int classId = Integer.parseInt(reader.readLine());
            System.out.println(classId);
            ResultSet rs = DatabaseHandler.getClassroomInfo(classId);

            if (rs != null && rs.next()) {
                writer.println(true);
                while (isNext) {
                    writer.println(rs.getString("name"));
                    System.out.println(rs.getString("name"));

                    isNext = rs.next();
                    writer.println(isNext);
                }
            } else {
                writer.println(false);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
