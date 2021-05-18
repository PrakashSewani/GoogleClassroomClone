package UI;

import backend.AnnouncementClient;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

public class AnnouncementDialog {

    private static final String TAG = "PostAnnouncementDialog: ";

    private AnnouncementClient.Announcement announcement;
//    private int announcementId;
//    private int classroomId;

    private JDialog dialog;
    private JLabel lblAnnouncementTitle;
    private JTextField txtAnnouncementTitle;
    private JLabel lblAnnouncementDescription;
    private JScrollPane descScrollPane;
    private JTextArea txtAnnouncementDescription;
    private JLabel lblFile;
    private JButton btnFile;
    private JFileChooser fileChooser;
    private JLabel lblSubmissionDate;
    private JDatePickerImpl datePicker;
    private JLabel lblMaxMarks;
    private JTextField txtMaxMarks;
    private JButton btnPostAnnouncement;
    private JButton btnUpdateAnnouncement;

    private JPanel radioBtnPanel;
    private JLabel lblType;
    private JRadioButton btnAnnouncementType;
    private JRadioButton btnAssignmentType;

    private boolean newFile = false;

    AnnouncementDialog (Frame owner, AnnouncementClient.Announcement announcement) {
        this.announcement = announcement;
        String fileName = this.announcement.filePath;
//        this.announcementId = announcementId;
//        this.classroomId = classroomId;
        dialog = new JDialog(owner, true);
        dialog.setLayout(new GridBagLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(owner);

        if (fileName.equals("null")) {
            fileName = "No File Selected.";
        }
        initDialogBox(this.announcement.announcementTitle, this.announcement.announcementDescription, fileName);
        registerListeners((MainUI) owner);

        dialog.pack();
        dialog.setVisible(true);
    }

    AnnouncementDialog(Frame owner, int classroomId) {
//        this(owner, -1, "", "", "No File Selected.", classroomId);
        this(owner, new AnnouncementClient.Announcement(-1, "", "", "", "", false, classroomId, "", -1, -1));
    }

    private void initDialogBox(String title, String description, String fileName) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 0, 10);
        lblAnnouncementTitle = new JLabel("Title:");
        lblAnnouncementTitle.setFont(LMSConstants.LBL_FONT);
        lblAnnouncementTitle.setHorizontalAlignment(SwingConstants.LEFT);
        dialog.add(lblAnnouncementTitle, c);

        c.gridy = 1;
        c.insets = new Insets(0, 10, 10, 10);
        txtAnnouncementTitle = new JTextField();
        txtAnnouncementTitle.setFont(LMSConstants.TXT_FONT);
        txtAnnouncementTitle.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtAnnouncementTitle.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        txtAnnouncementTitle.setText(title);
        dialog.add(txtAnnouncementTitle, c);

        c.gridy = 2;
        c.insets = new Insets(0, 10, 0, 10);
        lblAnnouncementDescription = new JLabel("Description:");
        lblAnnouncementDescription.setFont(LMSConstants.LBL_FONT);
        lblAnnouncementDescription.setHorizontalAlignment(SwingConstants.LEFT);
        dialog.add(lblAnnouncementDescription, c);

        c.gridy = 3;
        c.insets = new Insets(0, 10, 10, 10);
        txtAnnouncementDescription = new JTextArea(50, 50);
        txtAnnouncementDescription.setFont(LMSConstants.TXT_FONT);
        txtAnnouncementDescription.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtAnnouncementDescription.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        txtAnnouncementDescription.setText(description);
        txtAnnouncementDescription.setLineWrap(true);

        descScrollPane = new JScrollPane(txtAnnouncementDescription);
        descScrollPane.setPreferredSize(new Dimension(txtAnnouncementDescription.getPreferredSize().width, 100));
        descScrollPane.setBorder(BorderFactory.createEmptyBorder());
        descScrollPane.setOpaque(false);
        descScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        dialog.add(descScrollPane, c);

        c.gridy = 4;
        c.insets = new Insets(0, 10, 0, 10);
        lblFile = new JLabel("File:");
        lblFile.setFont(LMSConstants.LBL_FONT);
        lblFile.setHorizontalAlignment(SwingConstants.LEFT);
        dialog.add(lblFile, c);

        c.gridy = 5;
        c.insets = new Insets(0, 10, 10, 10);
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        btnFile = new JButton(fileName);
        dialog.add(btnFile, c);

        c.gridy = 6;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        radioBtnPanel = new JPanel(new GridBagLayout());
        radioBtnPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        lblType = new JLabel("Type: ");
        lblType.setFont(LMSConstants.LBL_FONT);
        lblType.setHorizontalAlignment(SwingConstants.LEFT);
        radioBtnPanel.add(lblType, gbc);

        gbc.gridy = 1;
        btnAnnouncementType = new JRadioButton("Announcement", true);
        btnAnnouncementType.setFont(LMSConstants.BTN_FONT);
        radioBtnPanel.add(btnAnnouncementType, gbc);

        gbc.gridx = 1;
        btnAssignmentType = new JRadioButton("Assignment");
        btnAssignmentType.setFont(LMSConstants.BTN_FONT);
        radioBtnPanel.add(btnAssignmentType, gbc);
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(btnAnnouncementType);
        btnGroup.add(btnAssignmentType);
        dialog.add(radioBtnPanel, c);

        c.gridy = 7;
        c.insets = new Insets(0, 10, 0, 10);
        lblSubmissionDate = new JLabel("Submission Date:");
        lblSubmissionDate.setFont(LMSConstants.LBL_FONT);
        lblSubmissionDate.setHorizontalAlignment(SwingConstants.LEFT);
        lblSubmissionDate.setVisible(false);
        dialog.add(lblSubmissionDate, c);

        c.gridx = 1;
        lblMaxMarks = new JLabel("Maximum Marks:");
        lblMaxMarks.setFont(LMSConstants.LBL_FONT);
        lblMaxMarks.setHorizontalAlignment(SwingConstants.LEFT);
        lblMaxMarks.setVisible(false);
        dialog.add(lblMaxMarks, c);

        c.gridx = 0;
        c.gridy = 8;
        c.insets = new Insets(0, 10, 10, 10);
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
        datePicker.setVisible(false);
        dialog.add(datePicker, c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        txtMaxMarks = new JTextField();
        txtMaxMarks.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtMaxMarks.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        txtMaxMarks.setFont(LMSConstants.TXT_FONT);
        txtMaxMarks.setVisible(false);
        dialog.add(txtMaxMarks, c);

        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        btnPostAnnouncement = new JButton("Post Announcement");
        btnPostAnnouncement.setFont(LMSConstants.BTN_FONT);
        btnPostAnnouncement.setOpaque(true);
        btnPostAnnouncement.setBorderPainted(false);
        btnPostAnnouncement.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnPostAnnouncement.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);

        btnUpdateAnnouncement = new JButton("Update Announcement");
        btnUpdateAnnouncement.setFont(LMSConstants.BTN_FONT);
        btnUpdateAnnouncement.setOpaque(true);
        btnUpdateAnnouncement.setBorderPainted(false);
        btnUpdateAnnouncement.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnUpdateAnnouncement.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);

        if (this.announcement.announcementId != -1) {
            dialog.add(btnUpdateAnnouncement, c);
        } else {
            dialog.add(btnPostAnnouncement, c);
        }
    }

    private void registerListeners(MainUI mainUI) {
        btnFile.addActionListener(e -> {
            fileChooser = new JFileChooser();
            int state = fileChooser.showOpenDialog(dialog);
            if(state == JFileChooser.APPROVE_OPTION) {
                newFile = true;
                File file = fileChooser.getSelectedFile();
                btnFile.setText(file.getName());
                System.out.println(TAG + "registerListeners: " + file);
            }
        });

        btnPostAnnouncement.addActionListener(e -> {
            String title = txtAnnouncementTitle.getText();
            String description = txtAnnouncementDescription.getText();
            File file = null;
            if (newFile) {
                file = fileChooser.getSelectedFile();
            }
            Timestamp currentDate = new Timestamp(new Date().getTime());
            File finalFile = file;
            boolean isAssignment = btnAssignmentType.isSelected();

            java.sql.Date submissionDate;
            int maxMarks;
            if (isAssignment) {
                Date temp = (Date) datePicker.getModel().getValue();
                submissionDate = new java.sql.Date(temp.getTime());
                maxMarks = Integer.parseInt(txtMaxMarks.getText());
            } else {
                submissionDate = new java.sql.Date(0);
                maxMarks = -1;
            }
            new Thread(() -> {
                boolean posted = new AnnouncementClient().postAnnouncement(title, description, finalFile, currentDate, isAssignment, submissionDate, maxMarks, HomePageUI.selectedClassId);
                if(posted) {
                    System.out.println("Inside if");
                    mainUI.homePagePanel.addAnnouncements(new AnnouncementClient().getAnnouncementsForClass(this.announcement.classroomId));
                    mainUI.homePagePanel.cardLayout.show(mainUI.homePagePanel.containerPanel, HomePageUI.SHRUNK_ANNOUNCEMENT_CARD);
                    mainUI.homePagePanel.revalidate();
                    mainUI.homePagePanel.repaint();
                }
            }).start();
        });

        btnUpdateAnnouncement.addActionListener(e -> {
            btnUpdateAnnouncement.setEnabled(false);
            String title = txtAnnouncementTitle.getText();
            String description = txtAnnouncementDescription.getText();
            File file = null;
            if (newFile) {
                file = fileChooser.getSelectedFile();
            }
            boolean isAssignment = btnAssignmentType.isSelected();
            java.sql.Date submissionDate;
            int maxMarks;
            if (isAssignment) {
                Date temp = (Date) datePicker.getModel().getValue();
                submissionDate = new java.sql.Date(temp.getTime());
                maxMarks = Integer.parseInt(txtMaxMarks.getText());
            } else {
                submissionDate = new java.sql.Date(0);
                maxMarks = -1;
            }

            File finalFile = file;
            new Thread(() -> {
                boolean isUpdated = new AnnouncementClient().updateAnnouncement(this.announcement.announcementId, title, description, finalFile, newFile, isAssignment, submissionDate, maxMarks);
                if (isUpdated) {
                    System.out.println("Updated");
                    mainUI.homePagePanel.addAnnouncements(new AnnouncementClient().getAnnouncementsForClass(this.announcement.classroomId));
                    mainUI.homePagePanel.cardLayout.show(mainUI.homePagePanel.containerPanel, HomePageUI.SHRUNK_ANNOUNCEMENT_CARD);
                    mainUI.homePagePanel.revalidate();
                    mainUI.homePagePanel.repaint();
                }
                btnUpdateAnnouncement.setEnabled(true);
            }).start();
        });

        btnAnnouncementType.addActionListener(e -> {
            if(btnAnnouncementType.isSelected()) {
                lblSubmissionDate.setVisible(false);
                datePicker.setVisible(false);
                lblMaxMarks.setVisible(false);
                txtMaxMarks.setVisible(false);
                dialog.pack();
            }
        });

        btnAssignmentType.addActionListener(e -> {
            if(btnAssignmentType.isSelected()) {
                lblSubmissionDate.setVisible(true);
                datePicker.setVisible(true);
                lblMaxMarks.setVisible(true);
                txtMaxMarks.setVisible(true);
                dialog.pack();
            }
        });
    }
}
