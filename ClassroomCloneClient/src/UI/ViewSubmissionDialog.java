package UI;

import backend.AnnouncementClient;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class ViewSubmissionDialog {
    private final int assignmentId;
    private ArrayList<AnnouncementClient.Submission> submissionArrayList;

    private final JDialog dialog;

    ViewSubmissionDialog (Frame owner, int assignmentId) {
        this.assignmentId = assignmentId;
        dialog = new JDialog(owner, true);
        dialog.setLayout(new GridBagLayout());
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(owner);

        getSubmissions();
        initDialog((MainUI) owner);

        dialog.pack();
        dialog.setVisible(true);
    }

    private void getSubmissions () {
        submissionArrayList = new AnnouncementClient().getSubmissions(assignmentId);
    }

    private void initDialog (MainUI mainUI) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 10, 0);
        c.gridwidth = GridBagConstraints.REMAINDER;

        for (int i = 0; i < submissionArrayList.size(); i++) {
            AnnouncementClient.Submission temp = submissionArrayList.get(i);
            c.gridy = i;
            dialog.add(new SubmissionPanel(temp), c);
        }
    }

    private class SubmissionPanel extends JPanel {

        AnnouncementClient.Submission submission;

        JLabel lblUsername;
        JButton btnFile;
        JTextField txtMarks;
        JLabel lblMaxMarks;
        JButton btnGrade;

        SubmissionPanel (AnnouncementClient.Submission submission) {
            this.submission = submission;
            this.setLayout(new GridBagLayout());
            this.setOpaque(true);
//            this.setBackground(LMSConstants.CLASS_PANEL_BACKGROUND_COLOR);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 20, 10, 0);
            c.anchor = GridBagConstraints.WEST;
            lblUsername = new JLabel(submission.userName);
            this.add(lblUsername, c);

            c.gridx = 1;
            c.insets = new Insets(10, 20, 10,0);
            btnFile = new JButton(submission.fileName);
            this.add(btnFile, c);

            c.gridx = 2;
            String marks = String.valueOf(submission.marks);
            if (submission.marks == -1) {
                marks = "";
            }
            txtMarks = new JTextField(marks, 3);
            this.add(txtMarks, c);

            c.gridx = 3;
            c.insets = new Insets(10, 10, 10, 20);
            lblMaxMarks = new JLabel("/ " + submission.maxMarks);
            this.add(lblMaxMarks, c);

            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(0, 0, 10, 0);
            btnGrade = new JButton("Grade");
            btnGrade.setOpaque(true);
            btnGrade.setBorderPainted(false);
            btnGrade.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
            btnGrade.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
            btnGrade.setFont(LMSConstants.BTN_FONT);
            this.add(btnGrade, c);

            registerListeners();
        }

        private void registerListeners () {
            btnFile.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select Destination Folder");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int state = fileChooser.showSaveDialog(dialog);
                if (state == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    new AnnouncementClient().getFileForSubmission(this.submission.id, file);
                }
            });

            btnGrade.addActionListener(e -> {
                boolean isValid = validateMarks();
                if (isValid) {
                    boolean isGraded = new AnnouncementClient().gradeSubmission(submission.id, Integer.parseInt(txtMarks.getText()));
                    if (isGraded) {
                        JOptionPane.showMessageDialog(dialog,
                                "Assignment graded successfully.");
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Assignment grading failed.",
                                "Grading failed",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Enter valid marks.",
                            "Invalid marks.",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        private boolean validateMarks () {
            int marks;
            try {
                marks = Integer.parseInt(txtMarks.getText());
            } catch (NumberFormatException e) {
                return false;
            }
            return marks >= 0 && marks <= submission.maxMarks;
        }
    }
}
