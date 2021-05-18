package UI;

import backend.AnnouncementClient;
import backend.UserClient;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ExpandedAnnouncementPanel extends JPanel {

    private final AnnouncementClient.Announcement announcement;
    private final int ownerId;
    private boolean containsFile;

    private JLabel lblTitle;
    private JLabel lblPostedDate;
    private JLabel lblSubmissionDate;
    private JTextArea lblDescription;
    private JButton btnEdit;
    private JLabel lblMarks;

    private JPanel filePanel;
    private JLabel lblFile;
    private JButton btnDownload;

    private JButton btnSubmit;
    private JButton btnViewSubmission;

    ExpandedAnnouncementPanel (AnnouncementClient.Announcement announcement, int ownerId) {
        this.announcement = announcement;
        this.ownerId = ownerId;
        initPanel();
        registerListeners();
    }

    private void initPanel() {
        this.setLayout(new GridBagLayout());
        this.setBackground(LMSConstants.ANNOUNCEMENT_PANEL_BACKGROUND_COLOR);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(10, 10, 0, 0);
        lblTitle = new JLabel(this.announcement.announcementTitle);
        this.add(lblTitle, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(10, 0, 10, 10);
        btnEdit = new JButton(LMSConstants.editIcn);
        btnEdit.setOpaque(false);
        btnEdit.setBorderPainted(false);

        String grade = "Ungraded";
        if (announcement.marks != -1) {
            grade = announcement.marks + " / " + announcement.maxMarks;
        }
        lblMarks = new JLabel(grade);
        if (ownerId == UserClient.userId) {
            this.add(btnEdit, c);
        } else {
            if (announcement.isAssignment) {
                this.add(lblMarks, c);
            }
        }

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 10, 0, 0);
        lblPostedDate = new JLabel(this.announcement.postingDate);
        lblPostedDate.setOpaque(false);
//        lblPostedDate.setBackground(new Color(200, 100, 200));
        this.add(lblPostedDate, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 0, 0, 10);
        lblSubmissionDate = new JLabel(announcement.submissionDate);
        lblSubmissionDate.setFont(LMSConstants.LBL_FONT);
        lblSubmissionDate.setOpaque(false);
        if (announcement.isAssignment) {
            this.add(lblSubmissionDate, c);
        }

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        lblDescription = new JTextArea(this.announcement.announcementDescription);
        lblDescription.setLineWrap(true);
        lblDescription.setEditable(false);
        //lblDescription.setDisabledTextColor(Color.WHITE);
        lblDescription.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        lblDescription.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        this.add(lblDescription, c);

        containsFile = this.announcement.filePath != null && !(this.announcement.filePath.equals("null"));
        System.out.println("containsFile: " + containsFile);
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weighty = 0;
        c.weightx = 0;
        filePanel = new JPanel(new FlowLayout());
        //filePanel.setBackground();
        filePanel.setOpaque(false);
        lblFile = new JLabel(this.announcement.filePath);
        filePanel.add(lblFile);
        String iconPath = ExpandedAnnouncementPanel.class.getClassLoader().getResource("download.png").getPath().replaceAll("%20", " ");
        btnDownload = new JButton(new ImageIcon(iconPath));
        filePanel.add(btnDownload);
        if (containsFile) {
            this.add(filePanel, c);
        }

        c.gridy = 4;
        c.anchor = GridBagConstraints.CENTER;
        btnSubmit = new JButton("Upload Assignment");
        btnSubmit.setOpaque(true);
        btnSubmit.setBorderPainted(false);
        btnSubmit.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnSubmit.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        btnSubmit.setFont(LMSConstants.BTN_FONT);

        btnViewSubmission = new JButton("View Submissions");
        btnViewSubmission.setOpaque(true);
        btnViewSubmission.setBorderPainted(false);
        btnViewSubmission.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnViewSubmission.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        btnViewSubmission.setFont(LMSConstants.BTN_FONT);

        if (announcement.isAssignment) {
            if (this.ownerId != UserClient.userId) {
                this.add(btnSubmit, c);
            } else {
                this.add(btnViewSubmission, c);
            }
        }
    }

    private void registerListeners() {
        btnDownload.addActionListener(e -> {
            MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(ExpandedAnnouncementPanel.this);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Destination Folder");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            int state = fileChooser.showSaveDialog(mainUI);
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                new AnnouncementClient().getFileForAnnouncement(this.announcement.announcementId, file);
            }
        });

        btnEdit.addActionListener(e -> {
            MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(ExpandedAnnouncementPanel.this);
            new AnnouncementDialog(mainUI, announcement);
        });

        btnSubmit.addActionListener(e -> {
            MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(ExpandedAnnouncementPanel.this);
            new SubmitAssignmentDialog(mainUI, announcement.announcementId);
        });

        btnViewSubmission.addActionListener(e -> {
            MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(ExpandedAnnouncementPanel.this);
            new ViewSubmissionDialog(mainUI, announcement.announcementId);
        });
    }

    public void submitted() {
        btnSubmit.setEnabled(false);
        btnSubmit.setText("Submitted");
    }

}
