package UI;

import javax.swing.*;
import java.awt.*;

public class EditProfileUI {
    private JFrame frame;
    private JPanel UserUI;
    private JLabel Username;
    private JTextField Usernamefield;
    private JLabel EmailID;
    private JTextField EmailIDfield;
    private JButton Okbutton;
    private JLabel OldPass;
    private JLabel Newpass;
    private JLabel Confirm;
    private JTextField Oldpassfield;
    private JTextField Newpassfield;
    private JTextField Confirmfield;

    EditProfileUI(){
        frame = new JFrame("Edit Profile");
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

        Username = new JLabel("Enter New Username: ");

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

        EmailID = new JLabel("Enter new Email ID: ");

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

        OldPass = new JLabel("Enter Old Password: ");

        UserUI.add(OldPass,abc4);

        GridBagConstraints abc5 = new GridBagConstraints();
        abc5.gridx = 1;
        abc5.gridy = 3;

        Oldpassfield = new JTextField();
        Oldpassfield.setPreferredSize(new Dimension(100,24));

        UserUI.add(Oldpassfield,abc5);

        GridBagConstraints abc7 = new GridBagConstraints();
        abc7.gridx = 0;
        abc7.gridy = 4;

        Newpass = new JLabel("Enter New Password: ");

        UserUI.add(Newpass,abc7);

        GridBagConstraints abc8 = new GridBagConstraints();
        abc8.gridx = 1;
        abc8.gridy = 4;

        Newpassfield = new JTextField();
        Newpassfield.setPreferredSize(new Dimension(100,24));

        UserUI.add(Newpassfield,abc8);

        GridBagConstraints abc9 = new GridBagConstraints();
        abc9.gridx = 0;
        abc9.gridy = 5;

        Confirm = new JLabel("Confirm New Password: ");

        UserUI.add(Confirm,abc9);

        GridBagConstraints abc10 = new GridBagConstraints();
        abc10.gridx = 1;
        abc10.gridy = 5;

        Confirmfield = new JTextField();
        Confirmfield.setPreferredSize(new Dimension(100,24));

        UserUI.add(Confirmfield,abc10);

        GridBagConstraints abc11 = new GridBagConstraints();
        abc11.gridx = 0;
        abc11.gridy = 6;
        abc11.anchor=GridBagConstraints.CENTER;

        Okbutton = new JButton("Confirm Profile Edit");

        UserUI.add(Okbutton,abc11);

        frame.add(UserUI,c);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EditProfileUI::new);
    }

}
