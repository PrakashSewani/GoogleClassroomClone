import java.sql.*;

public class DatabaseHandler {

    private static Connection dbConnection;

    private static final String VERIFY_USER_QUERY = "SELECT `id`, `name`, `email`, `password` FROM `user` WHERE `email` = ? AND `password` = ?;";
    private static final String REGISTER_USER_QUERY = "INSERT INTO `user` (`name`, `email`, `password`) VALUES (?, ?, ?);";
    private static final String UPDATE_USER_QUERY = "UPDATE `user` SET `name` = ?, `email` = ?, `password` = ? WHERE `id` = ?;";

    private static final String GET_CLASSROOMS_QUERY = "SELECT c.`id`, c.`name`, c.`description`, c.`code`, c.`owner_id`, u.`name` AS `username` FROM `user_classroom` uc JOIN `classroom` c ON uc.`classroom_id` = c.`id` JOIN `user` u ON c.`owner_id` = u.`id` WHERE uc.`user_id` = ?;";
    private static final String CREATE_CLASSROOM_QUERY = "INSERT INTO `classroom` (`name`, `description`, `code`, `owner_id`) VALUES (?, ?, ?, ?);";
    private static final String JOIN_CLASSROOM_QUERY = "INSERT INTO `user_classroom` (`user_id`, `classroom_id`) VALUES (?, ?);";
    private static final String UPDATE_CLASSROOM_QUERY = "UPDATE `classroom` SET `name` = ?, `description` = ? WHERE `id` = ?;";
    private static final String GET_CLASSROOM_INFO_QUERY = "SELECT u.`name` FROM `user_classroom` uc JOIN `user` u ON uc.`user_id` = u.`id` WHERE uc.`classroom_id` = ?;";

    private static final String GET_ANNOUNCEMENTS_QUERY = "SELECT a.`id`, a.`title`, a.`description`, a.`file_path`, a.`posting_date`, a.`is_assignment`, a.`classroom_id`, a.`submission_date`, s.`grade`, a.`max_marks`, s.`user_id` FROM `announcement` a LEFT JOIN `submission` s ON a.`id` = s.`assignment_id` WHERE a.`classroom_id` = ? ORDER BY a.posting_date DESC;";
    private static final String POST_ANNOUNCEMENT_QUERY = "INSERT INTO `announcement` (`title`, `description`, `file_path`, `posting_date`, `is_assignment`, `submission_date`, `max_marks`, `classroom_id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_ANNOUNCEMENT_WITH_FILE_QUERY = "UPDATE `announcement` SET `title` = ?, `description` = ?, `file_path` = ?, `is_assignment` = ?, `submission_date` = ?, `max_marks` = ? WHERE `id` = ?;";
    private static final String UPDATE_ANNOUNCEMENT_WITHOUT_FILE_QUERY = "UPDATE `announcement` SET `title` = ?, `description` = ?, `is_assignment` = ?, `submission_date` = ?, `max_marks` = ? WHERE `id` = ?;";
    private static final String SUBMIT_ASSIGNMENT_QUERY = "INSERT INTO `submission` (`user_id`, `assignment_id`, `file_path`, `grade`) VALUES (?, ?, ?, ?)";
    private static final String GET_SUBMISSIONS_QUERY = "SELECT s.`id`, u.`name`, s.file_path, s.`grade`, a.max_marks FROM `submission` s JOIN `announcement` a ON s.`assignment_id` = a.`id` JOIN `user` u ON s.`user_id` = u.`id` WHERE s.`assignment_id` = ?;";
    private static final String GRADE_ASSIGNMENT_QUERY = "UPDATE `submission` SET `grade` = ? WHERE `id` = ?;";

    private static void initDb() {
        try {
            System.out.println("Initializing DB...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lms", "root", "");
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public static ResultSet verifyUser(String email, String password) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            PreparedStatement userStatement = dbConnection.prepareStatement(VERIFY_USER_QUERY);
            userStatement.setString(1, email);
            userStatement.setString(2, password);
            return userStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static boolean registerUser(String name, String email, String password) {
        if (dbConnection == null) {
            initDb();
        }
        try{
            PreparedStatement registerStatement = dbConnection.prepareStatement(REGISTER_USER_QUERY);
            registerStatement.setString(1, name);
            registerStatement.setString(2, email);
            registerStatement.setString(3, password);
            System.out.println(registerStatement);
            registerStatement.execute();
            return true;
        } catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return false;
    }

    public static boolean updateUser(int id, String name, String email, String password) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            System.out.println("updateUser");
            PreparedStatement updateUserStatement = dbConnection.prepareStatement(UPDATE_USER_QUERY);
            updateUserStatement.setString(1, name);
            updateUserStatement.setString(2, email);
            updateUserStatement.setString(3, password);
            updateUserStatement.setInt(4, id);
            System.out.println(updateUserStatement.executeUpdate());

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public static ResultSet getClassroomsForUser(int userId) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            PreparedStatement getClassStatement = dbConnection.prepareStatement(GET_CLASSROOMS_QUERY);
            getClassStatement.setInt(1, userId);
            return getClassStatement.executeQuery();
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static boolean createClassroom(String name, String description, int code, int ownerId) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            PreparedStatement createClassroomStatement = dbConnection.prepareStatement(CREATE_CLASSROOM_QUERY);
            createClassroomStatement.setString(1, name);
            createClassroomStatement.setString(2, description);
            createClassroomStatement.setInt(3, code);
            createClassroomStatement.setInt(4, ownerId);
            System.out.println(createClassroomStatement);
            createClassroomStatement.execute();

            return joinClassroom(ownerId, getClassroomId(code));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static int getClassroomId(int code) {
        try {
            String getClassroomIdQuery = "SELECT `id` FROM `classroom` WHERE `code` = ?;";
            PreparedStatement getClassroomIdStatement =dbConnection.prepareStatement(getClassroomIdQuery);
            getClassroomIdStatement.setInt(1, code);
            ResultSet rs = getClassroomIdStatement.executeQuery();
            if(rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    public static boolean joinClassroom(int userId, int classroomId) {
        if(classroomId == -1) {
            return false;
        }
        try {
            PreparedStatement joinClassroomStatement = dbConnection.prepareStatement(JOIN_CLASSROOM_QUERY);
            joinClassroomStatement.setInt(1, userId);
            joinClassroomStatement.setInt(2, classroomId);
            joinClassroomStatement.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static ResultSet getAnnouncementForClass(int classId) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            PreparedStatement getAnnouncementStatement = dbConnection.prepareStatement(GET_ANNOUNCEMENTS_QUERY);
            getAnnouncementStatement.setInt(1, classId);
            return getAnnouncementStatement.executeQuery();
        } catch (SQLException thowables) {
            thowables.printStackTrace();
        }
        return null;
    }

    public static boolean postAnnouncementForClass(String title,
                                                   String description,
                                                   String filePath,
                                                   Timestamp currentDate,
                                                   boolean is_assignment,
                                                   Date submissionDate,
                                                   int maxMarks,
                                                   int classId) {
        if (dbConnection == null) {
            initDb();
        }
        PreparedStatement postAnnouncementStatement = null;
        try {
            postAnnouncementStatement = dbConnection.prepareStatement(POST_ANNOUNCEMENT_QUERY);
            postAnnouncementStatement.setString(1, title);
            postAnnouncementStatement.setString(2, description);
            postAnnouncementStatement.setString(3, filePath);
            postAnnouncementStatement.setTimestamp(4, currentDate);
            postAnnouncementStatement.setBoolean(5, is_assignment);
            postAnnouncementStatement.setDate(6, submissionDate);
            postAnnouncementStatement.setInt(7, maxMarks);
            postAnnouncementStatement.setInt(8, classId);
            System.out.println(postAnnouncementStatement);
            postAnnouncementStatement.execute();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if(postAnnouncementStatement != null) {
                    postAnnouncementStatement.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public static String getFileForAnnouncement(int id) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            String getFilePathQuery = "SELECT `file_path` FROM `announcement` WHERE `id` = ?;";
            PreparedStatement getFileStatement = dbConnection.prepareStatement(getFilePathQuery);
            getFileStatement.setInt(1, id);
            ResultSet rs = getFileStatement.executeQuery();
            if (rs != null && rs.next()) {
                return rs.getString("file_path");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static String getFileForSubmission(int id) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            String getFilePathQuery = "SELECT `file_path` FROM `submission` WHERE `id` = ?;";
            PreparedStatement getFileStatement = dbConnection.prepareStatement(getFilePathQuery);
            getFileStatement.setInt(1, id);
            ResultSet rs = getFileStatement.executeQuery();
            if (rs != null && rs.next()) {
                return rs.getString("file_path");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static boolean updateAnnouncement(int id,
                                             String title,
                                             String description,
                                             String filePath,
                                             boolean isAssignment,
                                             Date submissionDate,
                                             int maxMarks) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            System.out.println("updateClassroom");
            PreparedStatement updateAnnouncementStatement = dbConnection.prepareStatement(UPDATE_ANNOUNCEMENT_WITH_FILE_QUERY);
            updateAnnouncementStatement.setString(1, title);
            updateAnnouncementStatement.setString(2, description);
            updateAnnouncementStatement.setString(3, filePath);
            updateAnnouncementStatement.setBoolean(4, isAssignment);
            updateAnnouncementStatement.setDate(5, submissionDate);
            updateAnnouncementStatement.setInt(6, maxMarks);
            updateAnnouncementStatement.setInt(7, id);
            System.out.println(updateAnnouncementStatement.executeUpdate());

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static boolean updateAnnouncement(int id,
                                             String title,
                                             String description,
                                             boolean isAssignment,
                                             Date submissionDate,
                                             int maxMarks) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            System.out.println("updateClassroom");
            PreparedStatement updateAnnouncementStatement = dbConnection.prepareStatement(UPDATE_ANNOUNCEMENT_WITHOUT_FILE_QUERY);
            updateAnnouncementStatement.setString(1, title);
            updateAnnouncementStatement.setString(2, description);
            updateAnnouncementStatement.setBoolean(3, isAssignment);
            updateAnnouncementStatement.setDate(4, submissionDate);
            updateAnnouncementStatement.setInt(5, maxMarks);
            updateAnnouncementStatement.setInt(6, id);
            System.out.println(updateAnnouncementStatement.executeUpdate());

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static void closeConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static boolean updateClassroom(int id, String name, String description) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            System.out.println("updateClassroom");
            PreparedStatement updateClassroomStatement = dbConnection.prepareStatement(UPDATE_CLASSROOM_QUERY);
            updateClassroomStatement.setString(1, name);
            updateClassroomStatement.setString(2, description);
            updateClassroomStatement.setInt(3, id);
            System.out.println(updateClassroomStatement.executeUpdate());

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static ResultSet getClassroomInfo(int classId) {
        if(dbConnection == null) {
            initDb();
        }
        try {
            PreparedStatement getClassInfoStatement = dbConnection.prepareStatement(GET_CLASSROOM_INFO_QUERY);
            getClassInfoStatement.setInt(1, classId);
            return getClassInfoStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static boolean submitAssignment (int announcementId, int userId, String filePath) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            PreparedStatement submitAssignmentStatement = dbConnection.prepareStatement(SUBMIT_ASSIGNMENT_QUERY);
            submitAssignmentStatement.setInt(1, userId);
            submitAssignmentStatement.setInt(2, announcementId);
            submitAssignmentStatement.setString(3, filePath);
            submitAssignmentStatement.setInt(4, -1);
            System.out.println(submitAssignmentStatement);
            submitAssignmentStatement.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static ResultSet getSubmissions (int assignmentId) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            PreparedStatement getSubmissionStatement = dbConnection.prepareStatement(GET_SUBMISSIONS_QUERY);
            getSubmissionStatement.setInt(1, assignmentId);
            return getSubmissionStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static boolean gradeAssignment (int submissionId, int marks) {
        if (dbConnection == null) {
            initDb();
        }
        try {
            PreparedStatement gradeAssignmentStatement = dbConnection.prepareStatement(GRADE_ASSIGNMENT_QUERY);
            gradeAssignmentStatement.setInt(1, marks);
            gradeAssignmentStatement.setInt(2, submissionId);
            gradeAssignmentStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
