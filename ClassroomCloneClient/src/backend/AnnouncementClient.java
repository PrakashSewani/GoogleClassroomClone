package backend;

import UI.LMSConstants;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

public class AnnouncementClient {

    private static final int GET_ANNOUNCEMENTS_CODE = 500;
    private static final int POST_ANNOUNCEMENT_CODE = 501;
    private static final int UPDATE_ANNOUNCEMENT_CODE = 502;
    private static final int GET_ANNOUNCEMENT_FILE_CODE = 503;
    private static final int SUBMIT_ASSIGNMENT_CODE = 504;
    private static final int GET_SUBMISSIONS_CODE = 505;
    private static final int GET_SUBMISSION_FILE_CODE = 506;
    private static final int GRADE_ASSIGNMENT_CODE = 507;

    private Socket announcementSocket;
    private PrintWriter writer;
    private BufferedReader reader;

    public static ArrayList<Announcement> announcementArray = new ArrayList<>();

    private void initConnections() {
        try {
            System.out.println("Connecting with announcement server...");
            announcementSocket = new Socket(LMSConstants.SERVER_ADDR, LMSConstants.ANNOUNCEMENT_SERVER_PORT);
            System.out.println(announcementSocket);
            writer = new PrintWriter(announcementSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(announcementSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Announcement> getAnnouncementsForClass(int classId) {
        initConnections();

        try {
            int announcementId;
            String announcementTitle;
            String announcementDescription;
            String filePath;
            String postingDate;
            boolean isAssignment;
            int classroomId;
            String submissionDate;
            int marks;
            int maxMarks;

            writer.println(GET_ANNOUNCEMENTS_CODE);
            System.out.println(GET_ANNOUNCEMENTS_CODE);

            writer.println(classId);
            System.out.println(classId);

            writer.println(UserClient.userId);
            System.out.println(UserClient.userId);

            announcementArray.clear();
            while (Boolean.parseBoolean(reader.readLine())) {
                announcementId = Integer.parseInt(reader.readLine());
                System.out.println(announcementId);

                announcementTitle = reader.readLine();
                System.out.println(announcementTitle);

                int length = Integer.parseInt(reader.readLine());
                char[] temp = new char[length];
                int len = reader.read(temp, 0, length);
                System.out.println(len);
                announcementDescription = String.valueOf(temp);
                announcementDescription = announcementDescription.trim();
                System.out.println(announcementDescription);

                filePath = reader.readLine();
                System.out.println(filePath);

                postingDate = reader.readLine();
                System.out.println(postingDate);

                isAssignment = reader.readLine().equals("1");
                System.out.println(isAssignment);

                classroomId = Integer.parseInt(reader.readLine());
                System.out.println(classroomId);

                submissionDate = reader.readLine();
                System.out.println(submissionDate);

                marks = Integer.parseInt(reader.readLine());
                System.out.println(marks);

                maxMarks = Integer.parseInt(reader.readLine());
                System.out.println(maxMarks);

                announcementArray.add(new Announcement(announcementId,
                        announcementTitle,
                        announcementDescription,
                        filePath,
                        postingDate,
                        isAssignment,
                        classroomId,
                        submissionDate,
                        marks,
                        maxMarks));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return announcementArray;
    }

    public boolean postAnnouncement(String title,
                                    String description,
                                    File file,
                                    Timestamp postingDate,
                                    boolean isAssignment,
                                    Date submissionDate,
                                    int maxMarks,
                                    int classId) {
        initConnections();
        boolean posted = false;

        writer.println(POST_ANNOUNCEMENT_CODE);
        System.out.println(POST_ANNOUNCEMENT_CODE);

        writer.println(title);
        System.out.println(title);

        description = description.trim();
        writer.println(description.toCharArray().length+1);
        writer.println(description);
        System.out.println(description);

        new FileClient().sendFile(file);
        System.out.println(file);

        writer.println(postingDate);
        System.out.println(postingDate);

        writer.println(isAssignment);
        System.out.println(isAssignment);

        writer.println(submissionDate);
        System.out.println(submissionDate);

        writer.println(maxMarks);
        System.out.println(maxMarks);

        writer.println(classId);
        System.out.println(classId);

        try {
            posted = Boolean.parseBoolean(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return posted;
    }

    public boolean updateAnnouncement(int announcementId,
                                      String title,
                                      String description,
                                      File file,
                                      boolean newFile,
                                      boolean isAssignment,
                                      Date submissionDate,
                                      int maxMarks) {
        initConnections();
        boolean isUpdated = false;

        try{
            writer.println(UPDATE_ANNOUNCEMENT_CODE);
            System.out.println(UPDATE_ANNOUNCEMENT_CODE);

            writer.println(announcementId);
            System.out.println(announcementId);

            writer.println(title);
            System.out.println(title);

            description = description.trim();
            writer.println(description.toCharArray().length+1);
            writer.println(description);
            System.out.println(description);

            writer.println(isAssignment);
            System.out.println(isAssignment);

            writer.println(submissionDate);
            System.out.println(submissionDate);

            writer.println(maxMarks);
            System.out.println(maxMarks);

            writer.println(newFile);
            System.out.println(newFile);

            if (newFile) {
                new FileClient().sendFile(file);
                System.out.println(file);
            }

            isUpdated = Boolean.parseBoolean(reader.readLine());
            System.out.println(isUpdated);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return isUpdated;
    }

    public void getFileForAnnouncement(int announcementId, File file) {
        initConnections();

        writer.println(GET_ANNOUNCEMENT_FILE_CODE);
        writer.println(announcementId);

        String path = file.getAbsolutePath();
        System.out.println(path);
        path = path.substring(0, path.lastIndexOf("/")+1);
        System.out.println(path);
        new FileClient().receiveFile(path);

        closeConnections();
    }

    public void getFileForSubmission(int announcementId, File file) {
        initConnections();

        writer.println(GET_SUBMISSION_FILE_CODE);
        writer.println(announcementId);

        String path = file.getAbsolutePath();
        System.out.println(path);
        path = path.substring(0, path.lastIndexOf("/")+1);
        System.out.println(path);
        new FileClient().receiveFile(path);

        closeConnections();
    }

    public boolean submitAssignment (int announcementId, int userId, File file) {
        initConnections();

        writer.println(SUBMIT_ASSIGNMENT_CODE);

        writer.println(announcementId);

        writer.println(userId);

        new FileClient().sendFile(file);

        boolean isSubmitted = false;
        try {
            isSubmitted = Boolean.parseBoolean(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return isSubmitted;
    }

    public ArrayList<Submission> getSubmissions (int assignmentId) {
        initConnections();
        int id;
        String name;
        String fileName;
        int marks;
        int maxMarks;

        ArrayList<Submission> temp = new ArrayList<>();

        try {
            writer.println(GET_SUBMISSIONS_CODE);

            writer.println(assignmentId);

            while (Boolean.parseBoolean(reader.readLine())) {
                id = Integer.parseInt(reader.readLine());

                name = reader.readLine();

                fileName = reader.readLine();

                marks = Integer.parseInt(reader.readLine());

                maxMarks = Integer.parseInt(reader.readLine());
                temp.add(new Submission(id, name, fileName, marks, maxMarks));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public boolean gradeSubmission(int submissionId, int marks) {
        initConnections();

        writer.println(GRADE_ASSIGNMENT_CODE);
        System.out.println(GRADE_ASSIGNMENT_CODE);

        writer.println(submissionId);
        System.out.println(submissionId);

        writer.println(marks);
        System.out.println(marks);

        boolean isGraded = false;
        try {
            isGraded = Boolean.parseBoolean(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return isGraded;
    }

    private void closeConnections() {
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (announcementSocket != null) {
                announcementSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Announcement {

        public int announcementId;
        public String announcementTitle;
        public String announcementDescription;
        public String filePath;
        public String postingDate;
        public boolean isAssignment;
        public int classroomId;
        public String submissionDate;
        public int marks;
        public int maxMarks;

        public Announcement(int id,
                            String title,
                            String description,
                            String fPath,
                            String date,
                            boolean isAssignment,
                            int classId,
                            String submissionDate,
                            int marks,
                            int maxMarks) {
            this.announcementId = id;
            this.announcementTitle = title;
            this.announcementDescription = description;
            this.filePath = fPath;
            this.postingDate = date;
            this.isAssignment = isAssignment;
            this.classroomId = classId;
            this.submissionDate = submissionDate;
            this.marks = marks;
            this.maxMarks = maxMarks;
        }

        @Override
        public String toString() {
            return "announcementId: " + announcementId
                    + "\nannouncementTitle: " + announcementTitle
                    + "\nannouncementDescription: " + announcementDescription
                    + "\nfilePath: " + filePath
                    + "\npostingDate: " +postingDate
                    + "\nisAssignment: " + isAssignment
                    + "\nclassroomId: " + classroomId
                    + "\nsubmissionDate: " + submissionDate
                    + "\nmarks: " + marks
                    + "\nmaxMarks: " + maxMarks;
        }
    }

    public static class Submission {
        public int id;
        public String userName;
        public String fileName;
        public int marks;
        public int maxMarks;

        public Submission (int id, String userName, String fileName, int marks, int maxMarks) {
            this.id = id;
            this.userName = userName;
            this.fileName = fileName;
            this.marks = marks;
            this.maxMarks = maxMarks;
        }
    }
}
