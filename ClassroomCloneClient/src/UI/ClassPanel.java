package UI;

import backend.AnnouncementClient;
import backend.ClassClient;
import backend.UserClient;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClassPanel extends JPanel {

    private static  final String TAG = "ClassPanel: ";

    public ClassClient.Classroom classroom;

    public ArrayList<AnnouncementClient.Announcement> announcementArray;

    private JButton btnImage;
    private JButton btnClassName;
    private JButton btnTeacherName;
    private JButton btnClassCode;
    private JButton btnEdit;
    private JButton btnInfo;

    ClassPanel(ClassClient.Classroom classroom) {
        this.classroom = classroom;

        this.setLayout(new GridBagLayout());
        this.setBackground(LMSConstants.CLASS_PANEL_BACKGROUND_COLOR);

        initPanel();
        registerListeners();
    }

    private void initPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 5, 5, 0);
        String infoIconPath = RegisterUI.class.getClassLoader().getResource("info.png").getPath().replaceAll("%20", " ");
        btnInfo = new JButton(new ImageIcon(infoIconPath));
        btnInfo.setOpaque(false);
        btnInfo.setBorderPainted(false);
        this.add(btnInfo, c);

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(5, 0, 5, 0);
//        String editIconPath = RegisterUI.class.getClassLoader().getResource("edit.png").getPath().replaceAll("%20", " ");
        btnEdit = new JButton(LMSConstants.editIcn);
        btnEdit.setOpaque(false);
        btnEdit.setBorderPainted(false);
        if (this.classroom.classOwnerId == UserClient.userId) {
            this.add(btnEdit, c);
        }

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets( 0, 0, 0, 0);
        String classIconPath = RegisterUI.class.getClassLoader().getResource("class.png").getPath().replaceAll("%20", " ");
        ImageIcon icn = new ImageIcon(classIconPath);
        btnImage = new JButton(new ImageIcon(icn.getImage().getScaledInstance(200, -1, Image.SCALE_DEFAULT)));
        btnImage.setOpaque(false);
        btnImage.setContentAreaFilled(false);
        btnImage.setBorderPainted(false);
        this.add(btnImage, c);

        c.gridy = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        btnClassName = new JButton(this.classroom.className);
        btnClassName.setFont(new Font("Times New Roman", Font.BOLD, 28));
        btnClassName.setOpaque(false);
        btnClassName.setContentAreaFilled(false);
        btnClassName.setBorderPainted(false);
        btnClassName.setHorizontalAlignment(SwingConstants.LEFT);
        btnClassName.setPreferredSize(new Dimension(200, 30));
        btnClassName.setMaximumSize(new Dimension(200, 30));
        this.add(btnClassName, c);

        c.gridy = 3;
        btnTeacherName = new JButton(this.classroom.classOwnerName);
        btnTeacherName.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        btnTeacherName.setOpaque(false);
        btnTeacherName.setContentAreaFilled(false);
        btnTeacherName.setBorderPainted(false);
        btnTeacherName.setHorizontalAlignment(SwingConstants.LEFT);
        this.add(btnTeacherName, c);

        c.gridy = 4;
        btnClassCode = new JButton(String.valueOf(this.classroom.classCode));
        btnClassCode.setFont(new Font("Times New Roman", Font.ITALIC, 14));
        btnClassCode.setOpaque(false);
        btnClassCode.setContentAreaFilled(false);
        btnClassCode.setBorderPainted(false);
        btnClassCode.setHorizontalAlignment(SwingConstants.LEFT);
        this.add(btnClassCode, c);
    }

    private void registerListeners() {
        btnImage.addActionListener(e -> loadAnnouncements());
        btnClassName.addActionListener(e -> loadAnnouncements());
        btnTeacherName.addActionListener(e -> loadAnnouncements());
        btnClassCode.addActionListener(e -> loadAnnouncements());

        btnEdit.addActionListener(e -> {
            MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(ClassPanel.this);
//            new ClassDialog(mainUI, classId, className, classDescription, String.valueOf(classCode));
            new ClassDialog(mainUI, classroom);
        });

        btnInfo.addActionListener(e -> {
            MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(ClassPanel.this);
            new ClassInfoDialog(mainUI, new ClassClient().getClassroomInfo(this.classroom.classId));
        });
    }

    private void loadAnnouncements() {
        HomePageUI.selectedClassId = this.classroom.classId;
        HomePageUI.selectedClassOwnerId = this.classroom.classOwnerId;
        MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(ClassPanel.this);
        new Thread(() -> {
            announcementArray = new AnnouncementClient().getAnnouncementsForClass(this.classroom.classId);
            System.out.println(TAG + "loadAnnouncements: " + announcementArray);

            int components = mainUI.homePagePanel.announcementPanel.getComponents().length;
            int announcements = announcementArray.size();
            System.out.println(TAG + "loadAnnouncements: " + components + " : " + announcements*2);
            mainUI.homePagePanel.addAnnouncements(announcementArray);
            mainUI.homePagePanel.revalidate();
            mainUI.homePagePanel.repaint();
            mainUI.homePagePanel.cardLayout.show(mainUI.homePagePanel.containerPanel, HomePageUI.SHRUNK_ANNOUNCEMENT_CARD);

            mainUI.homePagePanel.initFloatingButton();
            mainUI.homePagePanel.glassPanel.revalidate();
            mainUI.homePagePanel.glassPanel.repaint();
        }).start();
    }
}