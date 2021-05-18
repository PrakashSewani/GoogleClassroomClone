package UI;

import javax.swing.*;
import java.awt.*;

public class ProfileUI {

    private JFrame frame;
    private JPanel UserUI;
    private JLabel Username;
    private JTextField Usernamefield;
    private JLabel EmailID;
    private JTextField EmailIDfield;
    private JButton EditProfile;
    private JButton Okbutton;

    ProfileUI(){
        frame = new JFrame("User Profile");
        frame.setPreferredSize(new Dimension(500,500));
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        UserProfileUI();

        frame.setVisible(true);
    }

    private void UserProfileUI(){
        UserUI = new JPanel(new GridBagLayout());
        UserUI.setBackground(new Color(100,225,100));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        GridBagConstraints abc = new GridBagConstraints();
        abc.gridx = 0;
        abc.gridy = 0;
        abc.anchor = GridBagConstraints.WEST;

        Username = new JLabel("Username: ");

        UserUI.add(Username,abc);

        GridBagConstraints abc2 = new GridBagConstraints();
        abc2.gridx = 1;
        abc2.gridy = 0;

        Usernamefield = new JTextField();
        Usernamefield.setPreferredSize(new Dimension(100,24));

        UserUI.add(Usernamefield,abc2);

        GridBagConstraints abc6 = new GridBagConstraints();
        abc6.gridx = 0;
        abc6.gridy = 2;
        abc6.anchor = GridBagConstraints.WEST;

        EmailID = new JLabel("Email ID: ");

        UserUI.add(EmailID,abc6);

        GridBagConstraints abc3 = new GridBagConstraints();
        abc3.gridx = 1;
        abc3.gridy = 2;

        EmailIDfield = new JTextField();
        EmailIDfield.setPreferredSize(new Dimension(100,24));

        UserUI.add(EmailIDfield,abc3);

        GridBagConstraints abc4 = new GridBagConstraints();
        abc4.gridx = 0;
        abc4.gridy = 3;

        Okbutton = new JButton("OK");

        UserUI.add(Okbutton,abc4);

        GridBagConstraints abc5 = new GridBagConstraints();
        abc5.gridx = 1;
        abc5.gridy = 3;

        EditProfile = new JButton("Edit Profile");

        UserUI.add(EditProfile,abc5);

        frame.add(UserUI,c);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProfileUI::new);
    }

}
