package UI;

import backend.AnnouncementClient;
import backend.UserClient;

import javax.swing.*;
import java.awt.*;

public class AnnouncementPanel extends JPanel {

    public int ownerId;
    public AnnouncementClient.Announcement announcement;

    private JButton btnTitle;
    private JLabel lblMarks;
    private JLabel lblPostedDate;
    private JLabel lblSubmissionDate;
    private JTextArea lblContent;
    private JButton btnEdit;

    AnnouncementPanel (AnnouncementClient.Announcement announcement, int ownerId) {
        this.announcement = announcement;
        this.ownerId = ownerId;

        this.setLayout(new GridBagLayout());
        this.setBackground(LMSConstants.ANNOUNCEMENT_PANEL_BACKGROUND_COLOR);

        initPanel();
    }

    private void initPanel() {

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(10, 10, 0, 0);
        btnTitle = new JButton(this.announcement.announcementTitle);
        this.add(btnTitle, c);

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
        lblPostedDate.setFont(LMSConstants.LBL_FONT);
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
        c.insets = new Insets(10, 10, 10,10);
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        lblContent = new JTextArea();
        if(this.announcement.announcementDescription.length() <= 300) {
            lblContent.setText(this.announcement.announcementDescription);
        } else {
            lblContent.setText(this.announcement.announcementDescription.substring(0, 300) + "....");
        }
        lblContent.setLineWrap(true);
        lblContent.setEditable(false);
        lblContent.setDisabledTextColor(Color.BLACK);
        lblContent.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        lblContent.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        this.add(lblContent, c);

        registerListeners();
    }

    private void registerListeners() {
        btnTitle.addActionListener(e -> {
            MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(AnnouncementPanel.this);
            mainUI.homePagePanel.initExpandedAnnouncementPanel(announcement, ownerId);
            mainUI.homePagePanel.cardLayout.show(mainUI.homePagePanel.containerPanel, HomePageUI.EXPANDED_ANNOUNCEMENT_CARD);
        });

        btnEdit.addActionListener(e -> {
            MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(AnnouncementPanel.this);
            new AnnouncementDialog(mainUI, announcement);
        });
    }
}
