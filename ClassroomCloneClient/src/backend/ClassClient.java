package backend;

import UI.LMSConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClassClient {

    public static final int GET_CLASSROOMS_CODE = 400;
    public static final int CREATE_CLASSROOM_CODE = 401;
    public static final int JOIN_CLASSROOM_CODE = 402;
    public static final int UPDATE_CLASSROOM_CODE = 403;
    public static final int GET_CLASSROOM_INFO_CODE = 404;

    private Socket classSocket;
    private PrintWriter writer;
    private BufferedReader reader;

    public static ArrayList<Classroom> classroomArray = new ArrayList<>();

    private void initConnections() {
        try {
            classSocket = new Socket(LMSConstants.SERVER_ADDR, LMSConstants.CLASS_SERVER_PORT);
            System.out.println(classSocket);
            writer = new PrintWriter(classSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(classSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnections() {
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (classSocket != null) {
                classSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getClassroomsForUser(int userId) {
        initConnections();
        writer.println(GET_CLASSROOMS_CODE);
        writer.println(userId);

        int classId;
        String className;
        String classDescription;
        int classCode;
        int classOwnerId;
        String classOwnerName;

        try {
            classroomArray.clear();
            while (Boolean.parseBoolean(reader.readLine())) {
                classId = Integer.parseInt(reader.readLine());

                className = reader.readLine();

                int length = Integer.parseInt(reader.readLine());
                char[] temp = new char[length];
                reader.read(temp, 0, length);
                classDescription = String.valueOf(temp);
                classDescription = classDescription.trim();

                classCode = Integer.parseInt(reader.readLine());

                classOwnerId = Integer.parseInt(reader.readLine());

                classOwnerName = reader.readLine();

                classroomArray.add(new Classroom(classId, className, classDescription, classCode, classOwnerId, classOwnerName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    public boolean createClassroom(String name, String description, int ownerId) {
        initConnections();
        boolean created = false;

        writer.println(CREATE_CLASSROOM_CODE);

        System.out.println(name);
        writer.println(name);

        description = description.trim();
        System.out.println(description);
        writer.println(description.toCharArray().length+1);
        writer.println(description);

        System.out.println(ownerId);
        writer.println(ownerId);

        try {
            if (Boolean.parseBoolean(reader.readLine())) {
                created = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeConnections();
        return created;
    }

    public boolean joinClass(int code) {
        initConnections();
        boolean joined = false;
        writer.println(JOIN_CLASSROOM_CODE);

        writer.println(UserClient.userId);
        writer.println(code);

        try {
            joined = Boolean.parseBoolean(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return joined;
    }

    public boolean updateClass (int classId, String name, String description) {
        initConnections();
        boolean isUpdated = false;

        try {
            writer.println(UPDATE_CLASSROOM_CODE);
            System.out.println(UPDATE_CLASSROOM_CODE);

            writer.println(classId);
            System.out.println(classId);

            writer.println(name);
            System.out.println(name);

            description = description.trim();
            writer.println(description.toCharArray().length+1);
            writer.println(description);
            System.out.println(description);

            isUpdated = Boolean.parseBoolean(reader.readLine());
            System.out.println(isUpdated);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return isUpdated;
    }

    public ArrayList<String> getClassroomInfo(int classId) {
        initConnections();

        String name;
        ArrayList<String> list = new ArrayList<>();
        try {
            writer.println(GET_CLASSROOM_INFO_CODE);

            writer.println(classId);

            while (Boolean.parseBoolean(reader.readLine())) {
                name = reader.readLine();
                System.out.println(name);
                list.add(name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return list;
    }

    public static class Classroom {

        public int classId;
        public String className;
        public String classDescription;
        public int classCode;
        public int classOwnerId;
        public String classOwnerName;

        public Classroom(int id, String name, String description, int code, int ownerId, String ownerName) {
            classId = id;
            className = name;
            classDescription = description;
            classCode = code;
            classOwnerId = ownerId;
            classOwnerName = ownerName;
        }

        @Override
        public String toString() {
            return "classId: " + classId + "\nclassName: " + className + "\nclassDescription: " + classDescription + "\nclassCode: " + classCode + "\nclassOwnerId: " + classOwnerId + "\nclassOwnerName: " + classOwnerName;
        }
    }
}
