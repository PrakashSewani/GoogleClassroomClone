package UI;

import backend.ClassClient;
import backend.UserClient;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JPanel {

    private static final String TAG = "LoginUI: ";

    private JPanel navPanel;
    private JLabel lblLogo;
    private JPanel buttonPanel;
    private JButton btnNavLogin;
    private JButton btnRegister;

    private JPanel loginPanel;
    private JLabel lblEmail;
    private JTextField txtEmail;
    private JLabel lblPassword;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    private JPanel imagePanel;

    private boolean isValid;

    LoginUI() {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(241, 246, 253));
        this.setOpaque(false);

        initNavBar();
        initLoginPanel();
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
        btnNavLogin = new JButton("Login");
        btnNavLogin.setEnabled(false);
        btnNavLogin.setOpaque(true);
        btnNavLogin.setBorderPainted(false);
        btnNavLogin.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnNavLogin.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        buttonPanel.add(btnNavLogin);

        btnRegister = new JButton("Register");
        btnRegister.setOpaque(true);
        btnRegister.setBorderPainted(false);
        btnRegister.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnRegister.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        buttonPanel.add(btnRegister);
        navPanel.add(buttonPanel, BorderLayout.EAST);

        this.add(navPanel, c);
    }

    private void initLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);

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
        gbc.insets = new Insets(0, 10, 0, 0);
        lblEmail = new JLabel("Email:");
        lblEmail.setFont(LMSConstants.LBL_FONT);
        loginPanel.add(lblEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 10, 20, 0);
        txtEmail = new JTextField();
        txtEmail.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtEmail.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        txtEmail.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        txtEmail.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        loginPanel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 0);
        lblPassword = new JLabel("Password:");
        lblPassword.setFont(LMSConstants.LBL_FONT);
        loginPanel.add(lblPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 20, 0);
        txtPassword = new JPasswordField();
        txtPassword.setBackground(LMSConstants.TXT_BACKGROUND_COLOR);
        txtPassword.setForeground(LMSConstants.TXT_FOREGROUND_COLOR);
        txtPassword.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        txtPassword.setFont(LMSConstants.TXT_FONT);
        loginPanel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 0, 0);
        btnLogin = new JButton("Login");
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnLogin.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        btnLogin.setFont(LMSConstants.BTN_FONT);
        loginPanel.add(btnLogin, gbc);

        this.add(loginPanel, c);
    }

    private void initImgPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.85;
        c.weighty = 0.9;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(150, 150,150,150);

        imagePanel = new BackgroundPanel(LMSConstants.bgImg.getImage(), BackgroundPanel.SCALED);
        imagePanel.setOpaque(false);

        this.add(imagePanel, c);
    }

    private void registerListeners() {
        btnLogin.addActionListener(e -> {
            btnLogin.setEnabled(false);
            System.out.println(TAG + "registerListeners: btnLog");

            new Thread(() -> {
                System.out.println(TAG + "registerListeners: Inside thread");
                MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(LoginUI.this);
                isValid = new UserClient().loginUser(txtEmail.getText(), String.valueOf(txtPassword.getPassword()));
                System.out.println(TAG + "registerListeners: " + isValid);
                if(isValid) {
                    txtEmail.setText("");
                    txtPassword.setText("");
                    mainUI.homePagePanel.btnUser.setText(UserClient.userName);
                    new ClassClient().getClassroomsForUser(UserClient.userId);
                    mainUI.homePagePanel.addClasses();
                    mainUI.cardLayout.show(mainUI.getContentPane(), MainUI.HOME_PAGE_CARD);
                    mainUI.homePagePanel.initFloatingButton();
                } else {
                    JOptionPane.showMessageDialog(mainUI,
                            "Wrong email or password.",
                            "Login error",
                            JOptionPane.ERROR_MESSAGE);
                }
                btnLogin.setEnabled(true);
            }).start();
        });

        btnRegister.addActionListener(e -> {
            MainUI mainUI = (MainUI) SwingUtilities.getWindowAncestor(LoginUI.this);
            mainUI.cardLayout.show(mainUI.getContentPane(), MainUI.REGISTER_CARD);
        });
    }
}
