import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AnnouncementServer {

    private static final int GET_ANNOUNCEMENTS_CODE = 500;
    private static final int POST_ANNOUNCEMENT_CODE = 501;
    private static final int UPDATE_ANNOUNCEMENT_CODE = 502;
    private static final int GET_ANNOUNCEMENT_FILE_CODE = 503;
    private static final int SUBMIT_ASSIGNMENT_CODE = 504;
    private static final int GET_SUBMISSIONS_CODE = 505;
    private static final int GET_SUBMISSION_FILE_CODE = 506;
    private static final int GRADE_ASSIGNMENT_CODE = 507;

    private ServerSocket announcementServer;
    private Socket announcementClient;
    private BufferedReader reader;
    private PrintWriter writer;

    AnnouncementServer () {
        try {
            announcementServer = new ServerSocket(4245);
            System.out.println("Announcement Server Started.\n" + announcementServer);
            while (true) {
                announcementClient = announcementServer.accept();
                System.out.println(announcementClient);
                reader = new BufferedReader(new InputStreamReader(announcementClient.getInputStream()));
                writer = new PrintWriter(announcementClient.getOutputStream(), true);
                int code = Integer.parseInt(reader.readLine());

                switch (code) {
                    case GET_ANNOUNCEMENTS_CODE:
                        getAnnouncementForClass();
                        break;
                    case POST_ANNOUNCEMENT_CODE:
                        postAnnouncementForClass();
                        break;
                    case UPDATE_ANNOUNCEMENT_CODE:
                        updateAnnouncement();
                        break;
                    case GET_ANNOUNCEMENT_FILE_CODE:
                        getFileForAnnouncement();
                        break;
                    case SUBMIT_ASSIGNMENT_CODE:
                        submitAssignment();
                        break;
                    case GET_SUBMISSIONS_CODE:
                        getSubmissions();
                        break;
                    case GET_SUBMISSION_FILE_CODE:
                        getFileForSubmission();
                        break;
                    case GRADE_ASSIGNMENT_CODE:
                        gradeAssignment();
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
                if(announcementClient != null) {
                    announcementClient.close();
                }
                if(announcementServer != null) {
                    announcementServer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getAnnouncementForClass() {
        boolean isNext = true;

        try {
            int classId = Integer.parseInt(reader.readLine());
            int userId = Integer.parseInt(reader.readLine());
            System.out.println(classId);
            ResultSet rs = DatabaseHandler.getAnnouncementForClass(classId);

            if (rs != null && rs.next()) {
                while (isNext && !(rs.getInt("user_id") == userId || rs.getInt("user_id") == 0)) {
                    isNext = rs.next();
                }
                writer.println(isNext);
                while (isNext) {
                    writer.println(rs.getInt("id"));
                    System.out.println(rs.getInt("id"));

                    writer.println(rs.getString("title"));
                    System.out.println(rs.getString("title"));

                    String desc = rs.getString("description");
                    writer.println(desc.toCharArray().length+2);
                    System.out.println(desc.toCharArray().length);
                    writer.println(desc);
                    System.out.println(desc);

                    String filePath = rs.getString("file_path");
                    System.out.println(filePath);
                    String fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
                    System.out.println(fileName);
                    writer.println(fileName);

                    writer.println(rs.getDate("posting_date").toString());
                    System.out.println(rs.getDate("posting_date").toString());

                    writer.println(rs.getInt("is_assignment"));
                    System.out.println(rs.getInt("is_assignment"));

                    writer.println(rs.getInt("classroom_id"));
                    System.out.println(rs.getInt("classroom_id"));

                    writer.println(rs.getDate("submission_date").toString());
                    System.out.println(rs.getDate("submission_date").toString());

                    writer.println(rs.getInt("grade"));
                    System.out.println(rs.getInt("grade"));

                    writer.println(rs.getInt("max_marks"));
                    System.out.println(rs.getInt("max_marks"));

                    System.out.println("UserId: " + rs.getInt("user_id"));

                    isNext = rs.next();
                    if (isNext && !(rs.getInt("user_id") == userId || rs.getInt("user_id") == 0)) {
                        isNext = rs.next();
                    }
                    writer.println(isNext);
                }
            } else {
                writer.println(false);
            }
        } catch(SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    private void postAnnouncementForClass() {
        try {
            String title = reader.readLine();
            System.out.println(title);

            int length = Integer.parseInt(reader.readLine());
            char[] temp = new char[length];
            System.out.println(reader.read(temp, 0, length));
            String description = String.valueOf(temp);
            description = description.trim();
            System.out.println(description);

            String filePath = new FileServer().receiveFile();
            System.out.println(filePath);

            Timestamp currentDate = Timestamp.valueOf(reader.readLine());
            System.out.println(currentDate);

            boolean isAssignment = Boolean.parseBoolean(reader.readLine());
            System.out.println(isAssignment);

            Date submissionDate = Date.valueOf(reader.readLine());
            System.out.println(submissionDate);

            int maxMarks = Integer.parseInt(reader.readLine());
            System.out.println(maxMarks);

            int classId = Integer.parseInt(reader.readLine());
            System.out.println(classId);

            writer.println(DatabaseHandler.postAnnouncementForClass(title, description, filePath, currentDate, isAssignment, submissionDate, maxMarks, classId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAnnouncement() {
        try {
            int id = Integer.parseInt(reader.readLine());
            System.out.println(id);

            String title = reader.readLine();
            System.out.println(title);

            int length = Integer.parseInt(reader.readLine());
            char[] temp = new char[length];
            System.out.println(reader.read(temp, 0, length));
            String description = String.valueOf(temp);
            description = description.trim();
            System.out.println(description);

            boolean isAssignment = Boolean.parseBoolean(reader.readLine());
            System.out.println(isAssignment);

            Date submissionDate = Date.valueOf(reader.readLine());
            System.out.println(submissionDate);

            int maxMarks = Integer.parseInt(reader.readLine());
            System.out.println(maxMarks);

            boolean newFile = Boolean.parseBoolean(reader.readLine());
            System.out.println(newFile);

            String filePath;
            if (newFile) {
                filePath = new FileServer().receiveFile();
                System.out.println(filePath);
                writer.println(DatabaseHandler.updateAnnouncement(id, title, description, filePath, isAssignment, submissionDate, maxMarks));
            } else {
                writer.println(DatabaseHandler.updateAnnouncement(id, title, description, isAssignment, submissionDate, maxMarks));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFileForAnnouncement() {
        try {
            int announcementId = Integer.parseInt(reader.readLine());
            String path = DatabaseHandler.getFileForAnnouncement(announcementId);
            if (path != null) {
                File file = new File(path);
                new FileServer().sendFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFileForSubmission() {
        try {
            int announcementId = Integer.parseInt(reader.readLine());
            String path = DatabaseHandler.getFileForSubmission(announcementId);
            if (path != null) {
                File file = new File(path);
                new FileServer().sendFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void submitAssignment () {
        try {
            int announcmentId = Integer.parseInt(reader.readLine());

            int userId = Integer.parseInt(reader.readLine());

            String filePath = new FileServer().receiveFile();

            writer.println(DatabaseHandler.submitAssignment(announcmentId, userId, filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getSubmissions () {
        try {
            int assignmentId = Integer.parseInt(reader.readLine());

            ResultSet rs = DatabaseHandler.getSubmissions(assignmentId);

            boolean isNext = true;
            if (rs != null && rs.next()) {
                writer.println(true);
                while (isNext) {
                    writer.println(rs.getInt("id"));

                    writer.println(rs.getString("name"));

                    String filePath = rs.getString("file_path");
                    System.out.println(filePath);
                    String fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
                    System.out.println(fileName);
                    writer.println(fileName);

                    writer.println(rs.getInt("grade"));

                    writer.println(rs.getInt("max_marks"));
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

    private void gradeAssignment () {
        try {
            int submissionId = Integer.parseInt(reader.readLine());
            System.out.println(submissionId);

            int marks = Integer.parseInt(reader.readLine());
            System.out.println(marks);

            writer.println(DatabaseHandler.gradeAssignment(submissionId, marks));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
