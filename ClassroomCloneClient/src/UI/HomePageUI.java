package UI;

import backend.AnnouncementClient;
import backend.ClassClient;
import backend.UserClient;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class HomePageUI extends JPanel {

    private static final String TAG = "HomePageUI: ";

    public JPanel containerPanel;
    public CardLayout cardLayout;

    private JPanel navPanel;
    private JLabel lblLogo;
    private JPanel buttonPanel;
    private JButton btnCreateClass;
    private JButton btnJoinClass;
    private JPanel userPanel;
    public JButton btnUser;
    private JButton btnLogout;

    public JPanel classroomPanel;
    private JScrollPane classroomScrollPane;
    private GridBagConstraints classroomGBC;

    private JLabel lblSelectClass;

    public JPanel announcementPanel;
    private JScrollPane announcementScrollPane;
    private GridBagConstraints announcementGBC;

    public JPanel glassPanel;
    private JButton btnNewAnnouncement;

    public JPanel expandedAnnouncementPanel;

    public static final String SHRUNK_ANNOUNCEMENT_CARD = "announcementPanel";
    public static final String EXPANDED_ANNOUNCEMENT_CARD = "expandedAnnouncementPanel";
    public static final String SELECT_CLASS_CARD = "selectClassPanel";

    public static int selectedClassId = -1;
    public static int selectedClassOwnerId = -1;

    HomePageUI() {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(241, 246, 253));
        this.setOpaque(false);

        initNavBar();
        initClassroomPanel();
        initContainerPanel();
        initSelectClassPanel();
        initAnnouncementPanel();
        registerListeners();
    }

    private void initNavBar() {
        navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 1.0;
        c.insets = new Insets(25, 10, 25, 10);

        lblLogo = new JLabel(LMSConstants.logoIcn);
        navPanel.add(lblLogo, BorderLayout.WEST);

        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        btnCreateClass = new JButton("Create Class");
        btnCreateClass.setOpaque(true);
        btnCreateClass.setBorderPainted(false);
        btnCreateClass.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnCreateClass.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        buttonPanel.add(btnCreateClass);

        btnJoinClass = new JButton("Join Class");
        btnJoinClass.setOpaque(true);
        btnJoinClass.setBorderPainted(false);
        btnJoinClass.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnJoinClass.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        buttonPanel.add(btnJoinClass);
        navPanel.add(buttonPanel, BorderLayout.CENTER);

        userPanel = new JPanel(new FlowLayout());
        userPanel.setOpaque(false);
        btnUser = new JButton(UserClient.userName);
        btnUser.setBorderPainted(false);
        btnUser.setContentAreaFilled(false);
        userPanel.add(btnUser);

        btnLogout = new JButton("Logout");
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(false);
        btnLogout.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnLogout.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        userPanel.add(btnLogout);
        navPanel.add(userPanel, BorderLayout.EAST);

        this.add(navPanel, c);
    }

    private void initClassroomPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weighty = 1.0;
        c.ipadx = 50;
        c.ipady = 50;
        c.fill = GridBagConstraints.BOTH;

        classroomPanel = new JPanel();
        classroomPanel.setLayout(new GridBagLayout());
        classroomPanel.setOpaque(false);

        classroomScrollPane = new JScrollPane(classroomPanel);
        classroomScrollPane.setOpaque(false);
        classroomScrollPane.setPreferredSize(new Dimension(200, -1));
        classroomScrollPane.setMinimumSize(new Dimension(200, -1));
        classroomScrollPane.setBorder(BorderFactory.createEmptyBorder());

        this.add(classroomScrollPane, c);
    }

    public void addClasses() {
        for(Component c : classroomPanel.getComponents()) {
            System.out.println(TAG + "addClasses: c1: " + c.toString());
        }
        classroomPanel.removeAll();
        revalidate();
        repaint();

        classroomGBC = new GridBagConstraints();
        classroomGBC.gridx = 0;

        classroomGBC.weightx = 1.0;
        classroomGBC.weighty = 0;
        classroomGBC.anchor = GridBagConstraints.NORTH;
        classroomGBC.insets = new Insets(0, 0, 10, 0);

        ArrayList<ClassClient.Classroom> temp = ClassClient.classroomArray;
        if(temp.size() == 1) {
            classroomGBC.weighty = 1.0;
        }
        for (int i = 0; i < temp.size(); i++) {
            ClassClient.Classroom temp1 = temp.get(i);
            System.out.println(TAG + "addClasses: " + temp1.toString());
            System.out.println("----------------------------------------");

            classroomGBC.gridy = i;
            classroomPanel.add(new ClassPanel(temp1), classroomGBC);

            classroomGBC.weighty = 0.1;
        }
    }

    private void initContainerPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;

        containerPanel= new JPanel(cardLayout = new CardLayout());
        containerPanel.setOpaque(false);

        this.add(containerPanel, c);
    }

    private void initSelectClassPanel() {
        lblSelectClass = new JLabel("Select a class.");
        lblSelectClass.setOpaque(false);
        lblSelectClass.setHorizontalAlignment(SwingConstants.CENTER);
        containerPanel.add(lblSelectClass, SELECT_CLASS_CARD);
    }

    private void initAnnouncementPanel() {
        announcementPanel = new JPanel();
        announcementPanel.setLayout(new GridBagLayout());
        announcementPanel.setOpaque(false);

        announcementScrollPane = new JScrollPane(announcementPanel);
        announcementScrollPane.setBorder(BorderFactory.createEmptyBorder());
        announcementScrollPane.setOpaque(false);

        containerPanel.add(announcementScrollPane, SHRUNK_ANNOUNCEMENT_CARD);
    }

    public void addAnnouncements(ArrayList<AnnouncementClient.Announcement> temp) {
        for(Component c : announcementPanel.getComponents()) {
            System.out.println(TAG + "addAnnouncements: c: " + c.toString());
        }
        announcementPanel.removeAll();
        revalidate();
        repaint();

        announcementGBC = new GridBagConstraints();
        announcementGBC.gridx = 0;
        announcementGBC.weightx = 1.0;
        announcementGBC.weighty = 0;
        announcementGBC.anchor = GridBagConstraints.NORTH;
        announcementGBC.fill = GridBagConstraints.HORIZONTAL;
        announcementGBC.gridwidth = GridBagConstraints.REMAINDER;
        announcementGBC.insets = new Insets(0, 0, 10, 0);

        if(temp.size() == 1) {
            announcementGBC.weighty = 1.0;
        }
        for (int i = 0; i < temp.size(); i++) {
            AnnouncementClient.Announcement temp1 = temp.get(i);
            announcementGBC.gridy = i;
            announcementPanel.add(new AnnouncementPanel(temp1, HomePageUI.selectedClassOwnerId), announcementGBC);

//            announcementGBC.gridy = i+1;
//            announcementPanel.add(Box.createRigidArea(new Dimension(10, 10)), announcementGBC);

            announcementGBC.weighty += 0.01;
        }

    }

    public void initFloatingButton() {
        glassPanel = new JPanel(new GridBagLayout());
        glassPanel.setOpaque(false);

        glassPanel.removeAll();
        glassPanel.revalidate();
        glassPanel.repaint();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.insets = new Insets(5, 50, 50, 50);
        btnNewAnnouncement = new JButton("+");
        btnNewAnnouncement.setOpaque(true);
        btnNewAnnouncement.setBorderPainted(true);
        btnNewAnnouncement.setBorder(new RoundedBorder(10)); //10 is the radius
        btnNewAnnouncement.setForeground(Color.BLUE);
        btnNewAnnouncement.setVisible(true);
        if (UserClient.userId == selectedClassOwnerId) {
            glassPanel.add(btnNewAnnouncement, c);
        }

        MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(HomePageUI.this);
        if (mainUI != null) {
            mainUI.setGlassPane(glassPanel);
            glassPanel.setVisible(true);
        } else {
            System.out.println(TAG + "initFloatingButton: Failed to initFloating button");
        }

        btnNewAnnouncement.addActionListener(e -> {
            if(selectedClassId != -1) {
                new AnnouncementDialog(mainUI, selectedClassId);
            } else {
                JOptionPane.showMessageDialog(mainUI,
                        "Please select a class to post an announcement.",
                        "No class selected.",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public void initExpandedAnnouncementPanel (AnnouncementClient.Announcement announcement, int ownerId) {
        expandedAnnouncementPanel = new ExpandedAnnouncementPanel(announcement, ownerId);
        containerPanel.add(expandedAnnouncementPanel, EXPANDED_ANNOUNCEMENT_CARD);
    }

    private void registerListeners() {
        btnCreateClass.addActionListener(e -> new ClassDialog((Frame) SwingUtilities.getWindowAncestor(HomePageUI.this)));


        btnJoinClass.addActionListener(e -> new JoinClassDialog((Frame) SwingUtilities.getWindowAncestor(HomePageUI.this)));

        btnUser.addActionListener(e ->
                new ProfileDialog((Frame) SwingUtilities.getWindowAncestor(HomePageUI.this),
                        UserClient.userName, UserClient.emailId, UserClient.password));

        btnLogout.addActionListener(e -> {
            UserClient.logoutUser();
            MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(HomePageUI.this);
            mainUI.cardLayout.show(mainUI.getContentPane(), MainUI.LOGIN_CARD);
        });
    }

    //-------------------------Remove  this------------------------------//
    private static class RoundedBorder implements Border {

        private final int radius;


        RoundedBorder(int radius) {
            this.radius = radius;
        }


        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }


        public boolean isBorderOpaque() {
            return true;
        }


        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
}
