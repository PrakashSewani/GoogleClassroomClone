package UI;

import backend.ClassClient;
import backend.UserClient;

import javax.swing.*;
import java.awt.*;

public class ClassDialog {

    private static final String TAG = "CreateClassDialog: ";

//    private int classId;
    private final ClassClient.Classroom classroom;

    private final JDialog dialog;
    private JLabel lblClassName;
    private JTextField txtClassName;
    private JLabel lblClassDescription;
    private JTextArea txtClassDescription;
    private JLabel lblClassCode;
    private JTextField txtClassCode;
    private JButton btnCreateClass;
    private JButton btnUpdateClass;

//    ClassDialog(Frame owner, int classId, String className, String description, String classCode) {
    ClassDialog (Frame owner, ClassClient.Classroom classroom) {
        this.classroom = classroom;
//        this.classId = classId;
        dialog = new JDialog(owner, true);
        dialog.setLayout(new GridBagLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(owner);

        initDialogBox();
        registerListener((MainUI) owner);

        dialog.pack();
        dialog.setVisible(true);
    }

    ClassDialog(Frame owner) {
//        this(owner, -1, "", "", "");
        this (owner, new ClassClient.Classroom(-1, "", "", -1, -1, ""));
    }

    private void initDialogBox() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10,  0, 10);
        lblClassName = new JLabel("Class Name: ");
        lblClassName.setFont(LMSConstants.LBL_FONT);
        lblClassName.setHorizontalAlignment(SwingConstants.LEFT);
        dialog.add(lblClassName, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 10,  10, 10);
        txtClassName = new JTextField();
        txtClassName.setFont(LMSConstants.TXT_FONT);
        txtClassName.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtClassName.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        txtClassName.setText(this.classroom.className);
        dialog.add(txtClassName, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 10,  0, 10);
        lblClassDescription = new JLabel("Class Description: ");
        lblClassDescription.setFont(LMSConstants.LBL_FONT);
        lblClassDescription.setHorizontalAlignment(SwingConstants.LEFT);
        dialog.add(lblClassDescription, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 10,  10, 10);
        txtClassDescription = new JTextArea();
        txtClassDescription.setFont(LMSConstants.TXT_FONT);
        txtClassDescription.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtClassDescription.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        txtClassDescription.setLineWrap(true);
        txtClassDescription.setPreferredSize(new Dimension(txtClassDescription.getPreferredSize().width, 100));
        txtClassDescription.setText(this.classroom.classDescription);
        dialog.add(txtClassDescription, c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(0, 10,  0, 10);
        lblClassCode = new JLabel("Class Code: ");
        lblClassCode.setFont(LMSConstants.LBL_FONT);
        lblClassCode.setHorizontalAlignment(SwingConstants.LEFT);
        dialog.add(lblClassCode, c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(0, 10,  10, 10);
        txtClassCode = new JTextField();
        txtClassCode.setFont(LMSConstants.TXT_FONT);
        txtClassCode.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtClassCode.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        txtClassCode.setEditable(false);
        txtClassCode.setText(String.valueOf(this.classroom.classCode));
        dialog.add(txtClassCode, c);

        c.gridx = 0;
        c.gridy = 6;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 10,  10, 10);
        btnCreateClass = new JButton("Create Class");
        btnCreateClass.setFont(LMSConstants.BTN_FONT);
        btnCreateClass.setOpaque(true);
        btnCreateClass.setBorderPainted(false);
        btnCreateClass.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnCreateClass.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);

        btnUpdateClass = new JButton("Update Class");
        btnUpdateClass.setFont(LMSConstants.BTN_FONT);
        btnUpdateClass.setOpaque(true);
        btnUpdateClass.setBorderPainted(false);
        btnUpdateClass.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnUpdateClass.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);

        if (this.classroom.classId != -1) {
            dialog.add(btnUpdateClass, c);
        } else {
            dialog.add(btnCreateClass, c);
        }
    }

    private void registerListener(MainUI mainUI) {
        btnCreateClass.addActionListener(e -> {
            btnCreateClass.setEnabled(false);
            System.out.println(TAG + "registerListeners: Create Class");

            String className = txtClassName.getText();
            String classDescription = txtClassDescription.getText();
            System.out.println(TAG + "registerListeners: " + UserClient.userId);
            new Thread(() -> {
                boolean created = new ClassClient().createClassroom(className, classDescription, UserClient.userId);
                if(created) {
                    new ClassClient().getClassroomsForUser(UserClient.userId);
                    mainUI.homePagePanel.addClasses();
                }
                btnCreateClass.setEnabled(true);
            }).start();
        });

        btnUpdateClass.addActionListener(e -> {
            btnUpdateClass.setEnabled(false);
            System.out.println(TAG + "registerListeners: Update Class");

            String className = txtClassName.getText();
            System.out.println(className);
            String classDescription = txtClassDescription.getText();
            System.out.println(classDescription);
            new Thread(() -> {
                boolean isUpdated = new ClassClient().updateClass(this.classroom.classId, className, classDescription);
                if (isUpdated) {
                    System.out.println("Updated");
                    new ClassClient().getClassroomsForUser(UserClient.userId);
                    mainUI.homePagePanel.addClasses();
                    mainUI.homePagePanel.revalidate();
                    mainUI.homePagePanel.repaint();
                }
                btnUpdateClass.setEnabled(true);
            }).start();
        });
    }
}
