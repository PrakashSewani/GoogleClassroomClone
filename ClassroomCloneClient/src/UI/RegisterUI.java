package UI;

import backend.UserClient;

import javax.swing.*;
import java.awt.*;

public class RegisterUI extends JPanel{

    private static final String TAG = "RegisterUI: ";

    private JPanel navPanel;
    private JLabel lblLogo;
    private JPanel buttonPanel;
    private JButton btnLogin;
    private JButton btnNavRegister;

    private JPanel registerPanel;
    private JLabel lblName;
    private JTextField txtName;
    private JLabel lblEmail;
    private JTextField txtEmail;
    private JLabel lblPassword;
    private JPasswordField txtPassword;
    private JLabel lblConfirmPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;

    private JPanel imagePanel;

    private boolean isValid = false;

    RegisterUI() {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(241, 246, 253));
        this.setOpaque(false);

        initNavBar();
        initRegisterPanel();
        initImgPanel();
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
        c.insets = new Insets(25, 10, 25, 10);

        lblLogo = new JLabel(LMSConstants.logoIcn);
        navPanel.add(lblLogo, BorderLayout.WEST);

        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        btnLogin = new JButton("Login");
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnLogin.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        buttonPanel.add(btnLogin);

        btnNavRegister = new JButton("Register");
        btnNavRegister.setEnabled(false);
        btnNavRegister.setOpaque(true);
        btnNavRegister.setBorderPainted(false);
        btnNavRegister.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnNavRegister.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        buttonPanel.add(btnNavRegister);
        navPanel.add(buttonPanel, BorderLayout.EAST);

        this.add(navPanel, c);
    }

    private void initRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.15;
        c.weighty = 0.9;
        c.fill = GridBagConstraints.BOTH;

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 0);
        lblName = new JLabel("Name:");
        lblName.setFont(LMSConstants.LBL_FONT);
        registerPanel.add(lblName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 20, 0);
        txtName = new JTextField();
        txtName.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtName.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        txtName.setFont(LMSConstants.TXT_FONT);
        txtName.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        registerPanel.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 0);
        lblEmail = new JLabel("Email:");
        lblEmail.setFont(LMSConstants.LBL_FONT);
        registerPanel.add(lblEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 20, 0);
        txtEmail = new JTextField();
        txtEmail.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtEmail.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        txtEmail.setFont(LMSConstants.TXT_FONT);
        txtEmail.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        registerPanel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 0);
        lblPassword = new JLabel("Password:");
        lblPassword.setFont(LMSConstants.LBL_FONT);
        registerPanel.add(lblPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 20, 0);
        txtPassword = new JPasswordField();
        txtPassword.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtPassword.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        txtPassword.setFont(LMSConstants.TXT_FONT);
        txtPassword.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        registerPanel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 0);
        lblConfirmPassword = new JLabel("Confirm Password:");
        lblConfirmPassword.setFont(LMSConstants.LBL_FONT);
        registerPanel.add(lblConfirmPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 20, 0);
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtConfirmPassword.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        txtConfirmPassword.setFont(LMSConstants.TXT_FONT);
        txtConfirmPassword.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        registerPanel.add(txtConfirmPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 0, 0);
        btnRegister = new JButton("Register");
        btnRegister.setOpaque(true);
        btnRegister.setBorderPainted(false);
        btnRegister.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnRegister.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        btnRegister.setFont(LMSConstants.BTN_FONT);
        registerPanel.add(btnRegister, gbc);

        this.add(registerPanel, c);
    }

    private void initImgPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.85;
        c.weighty = 0.9;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(150, 150, 150, 150);

        imagePanel = new BackgroundPanel(LMSConstants.bgImg.getImage(), BackgroundPanel.SCALED);
        imagePanel.setOpaque(false);


        this.add(imagePanel, c);
    }

    private void registerListeners() {
        btnLogin.addActionListener(e -> {
            MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(RegisterUI.this);
            mainUI.cardLayout.show(mainUI.getContentPane(), MainUI.LOGIN_CARD);
        });

        btnRegister.addActionListener(e -> {
            System.out.println(TAG + "registerListeners: btnLog");

            MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(RegisterUI.this);
            String name = txtName.getText();
            String email = txtEmail.getText();
            String password = String.valueOf(txtPassword.getPassword());
            String confirmPassword = String.valueOf(txtConfirmPassword.getPassword());
            boolean valid = true;
            String message = "";
            if (!validateName(name)) {
                isValid = false;
                message += "Invalid name.";
            }
            if (!validateEmail(email)) {
                valid = false;
                message += "\nInvalid email.";
            }
            if (!validatePassword(password)) {
                valid = false;
                message += "\nPassword must be of at least 6 characters long.";
            }
            if (!password.equals(confirmPassword)) {
                valid = false;
                message += "\nPassword and Confirm password do not match.";
            }
            if (!valid) {
                JOptionPane.showMessageDialog(mainUI,
                        message,
                        "Invalid credentials",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                new Thread(() -> {
                    btnRegister.setEnabled(false);
                    System.out.println(TAG + "registerListeners: Inside thread");
                    isValid = new UserClient().registerUser(txtName.getText(), txtEmail.getText(), String.valueOf(txtPassword.getPassword()));
                    System.out.println(TAG + "registerListeners: " + isValid);
                    if (isValid) {
                        txtName.setText("");
                        txtEmail.setText("");
                        txtPassword.setText("");
                        txtConfirmPassword.setText("");
                        JOptionPane.showMessageDialog(mainUI,
                                "Registered successfull!!",
                                "success",
                                JOptionPane.INFORMATION_MESSAGE);
                        mainUI.cardLayout.show(mainUI.getContentPane(), MainUI.LOGIN_CARD);
                    } else {
                        JOptionPane.showMessageDialog(mainUI,
                                "Registration failed",
                                "Registration error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    btnRegister.setEnabled(true);
                }).start();
            }
        });
    }

    private boolean validateName (String name) {
        return name.matches( "[A-Z][a-z]*\\s[A-Z][a-z]*" );
    }

    private boolean validateEmail (String email) {
        return email.matches( "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$" );
    }

    private boolean validatePassword (String password) {
        return password.length() >= 6;
    }
}
