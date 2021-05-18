package UI;

import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {

//    public final JFrame frame;
    public CardLayout cardLayout;

    private JPanel registerPanel;
    private JPanel loginPanel;
    public HomePageUI homePagePanel;

    public static final String REGISTER_CARD = "registerPanel";
    public static final String LOGIN_CARD = "loginPanel";
    public static final String HOME_PAGE_CARD = "homePagePanel";

    MainUI() {
        this.setTitle("Learning Management System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(cardLayout = new CardLayout());
        this.setBackground(LMSConstants.MAIN_BACKGROUND_COLOR);

        initLoginPanel();
        initRegisterPanel();
        initHomePagePanel();

        this.setVisible(true);
    }

    private void initRegisterPanel() {
        registerPanel = new RegisterUI();
        this.add(registerPanel, REGISTER_CARD);
    }

    private void initLoginPanel() {
        loginPanel = new LoginUI();
        this.add(loginPanel, LOGIN_CARD);
    }

    private void initHomePagePanel() {
        homePagePanel = new HomePageUI();
        this.add(homePagePanel, HOME_PAGE_CARD);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::new);
    }
}
