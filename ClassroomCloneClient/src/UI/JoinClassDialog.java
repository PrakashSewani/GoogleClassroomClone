package UI;

import backend.ClassClient;
import backend.UserClient;

import javax.swing.*;
import java.awt.*;

public class JoinClassDialog {

    private static final String TAG = "JoinClassDialog: ";

    private JDialog dialog;
    private JLabel lblClassCode;
    private JTextField txtClassCode;
    private JButton btnJoinClass;

    JoinClassDialog(Frame owner) {
        dialog = new JDialog(owner, true);
        dialog.setLayout(new GridBagLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(owner);

        initDialogBox();
        registerListeners((MainUI) owner);

        dialog.pack();
        dialog.setVisible(true);
    }

    private void initDialogBox() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 10);
        lblClassCode = new JLabel("Class Code: ");
        lblClassCode.setFont(LMSConstants.LBL_FONT);
        lblClassCode.setHorizontalAlignment(SwingConstants.LEFT);
        dialog.add(lblClassCode, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 10, 20, 10);
        txtClassCode = new JTextField();
        txtClassCode.setFont(LMSConstants.TXT_FONT);
        txtClassCode.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtClassCode.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        dialog.add(txtClassCode, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 10);
        btnJoinClass = new JButton("Join Class");
        btnJoinClass.setFont(LMSConstants.BTN_FONT);
        btnJoinClass.setOpaque(true);
        btnJoinClass.setBorderPainted(false);
        btnJoinClass.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnJoinClass.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        dialog.add(btnJoinClass, c);
    }

    private void registerListeners(MainUI mainUI) {
        btnJoinClass.addActionListener(e -> {
            System.out.println(TAG + "registerListeners: Join Class");

            boolean valid = validateCode(txtClassCode.getText());
            if (!valid) {
                JOptionPane.showMessageDialog(dialog,
                        "Invalid classroom code",
                        "Invalid code",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                int code = Integer.parseInt(txtClassCode.getText());
                new Thread(() -> {
                    btnJoinClass.setEnabled(false);
                    boolean joined = new ClassClient().joinClass(code);

                    if (joined) {
                        new ClassClient().getClassroomsForUser(UserClient.userId);
                        mainUI.homePagePanel.addClasses();
                        JOptionPane.showMessageDialog(dialog,
                                "Classroom joined successfully",
                                "Class joining successful",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Classroom with code " + code + "does not exist.",
                                "Class joining failed",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    btnJoinClass.setEnabled(true);
                }).start();
            }
        });
    }

    private static boolean validateCode (String code) {
        try {
            int temp = Integer.parseInt(code);
            if (temp >= 100000 && temp < 1000000) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }
}
