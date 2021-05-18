package UI;

import backend.UserClient;

import javax.swing.*;
import java.awt.*;

public class ProfileDialog {

    private JDialog dialog;
    private JLabel lblName;
    private JTextField txtName;
    private JLabel lblEmail;
    private JTextField txtEmail;
    private JLabel lblPassword;
    private JPasswordField txtPassword;
    private JButton btnUpdate;

    public ProfileDialog (Frame owner, String name, String email, String password) {
        dialog = new JDialog(owner, true);
        dialog.setLayout(new GridBagLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(owner);

        initDialog(name, email, password);
        registerListeners((MainUI) owner);

        dialog.pack();
        dialog.setVisible(true);
    }

    private void initDialog (String name, String email, String password) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 10);
        lblName = new JLabel("Name: ");
        lblName.setFont(LMSConstants.LBL_FONT);
        lblName.setHorizontalAlignment(SwingConstants.LEFT);
        dialog.add(lblName, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 10, 10, 10);
        txtName = new JTextField(name);
        txtName.setFont(LMSConstants.TXT_FONT);
        txtName.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtName.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        dialog.add(txtName, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 10, 0, 10);
        lblEmail = new JLabel("Email: ");
        lblEmail.setFont(LMSConstants.LBL_FONT);
        lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
        dialog.add(lblEmail, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 10, 10, 10);
        txtEmail = new JTextField(email);
        txtEmail.setFont(LMSConstants.TXT_FONT);
        txtEmail.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtEmail.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        dialog.add(txtEmail, c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(0, 10, 0, 10);
        lblPassword = new JLabel("Password: ");
        lblPassword.setFont(LMSConstants.LBL_FONT);
        lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
        dialog.add(lblPassword, c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(0, 10, 10, 10);
        txtPassword = new JPasswordField(password);
        txtPassword.setFont(LMSConstants.TXT_FONT);
        txtPassword.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtPassword.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        dialog.add(txtPassword, c);

        c.gridx = 0;
        c.gridy = 6;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 10, 10, 10);
        btnUpdate = new JButton("Update Profile");
        btnUpdate.setFont(LMSConstants.BTN_FONT);
        btnUpdate.setOpaque(true);
        btnUpdate.setBorderPainted(false);
        btnUpdate.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnUpdate.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        dialog.add(btnUpdate, c);
    }

    private void registerListeners(MainUI mainUI) {
        btnUpdate.addActionListener(e -> {
            btnUpdate.setEnabled(false);
            String name = txtName.getText();
            String email = txtEmail.getText();
            String password = String.valueOf(txtPassword.getPassword());

            new Thread(() -> {
                boolean isUpdated = new UserClient().updateUser(name, email, password);
                if (isUpdated) {
                    JOptionPane.showMessageDialog(dialog,
                            "Profile updated successfully",
                            "success",
                            JOptionPane.INFORMATION_MESSAGE);
                    mainUI.homePagePanel.btnUser.setText(name);
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Profile update failed",
                            "Update error",
                            JOptionPane.ERROR_MESSAGE);
                }
                btnUpdate.setEnabled(true);
            }).start();
        });
    }
}
