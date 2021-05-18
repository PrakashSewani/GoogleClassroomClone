package UI;

import javax.swing.*;
import java.awt.*;

public class ChatPanel {
    private JPanel TeacherName;
    private JPanel ChatPanel;
    private JPanel TypePanel;
    private JPanel MainPanel;
    private JLabel Teacher;
    private JTextField Message;
    private JButton Send;
    private JFrame frame;

    ChatPanel(){
        frame = new JFrame("Chat");
        frame.setPreferredSize(new Dimension(700,500));
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        ChatBoxUI();
        frame.setVisible(true);
    }

    private void ChatBoxUI(){
        MainPanel = new JPanel(new GridBagLayout());
        MainPanel.setBackground(new Color(100,225,100));

        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridx = 0;
        c1.gridy = 0;
        c1.weightx = 1;
        c1.weighty = 1;
        c1.fill = GridBagConstraints.BOTH;

        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = 0;
        c2.weightx = 1;
        c2.weighty = 0.05;
        c2.fill =GridBagConstraints.BOTH;

        TeacherName = new JPanel(new BorderLayout());
        TeacherName.setBackground(new Color(225,100,100));

        Teacher = new JLabel("Teacher's Name");

        TeacherName.add(Teacher,BorderLayout.WEST);

        MainPanel.add(TeacherName,c2);

        GridBagConstraints c3 = new GridBagConstraints();
        c3.gridx = 0;
        c3.gridy = 1;
        c3.weightx = 1;
        c3.weighty = 0.9;
        c3.fill = GridBagConstraints.BOTH;

        ChatPanel = new JPanel(new GridBagLayout());
        ChatPanel.setBackground(new Color(100,100,225));

        MainPanel.add(ChatPanel,c3);

        GridBagConstraints c4 = new GridBagConstraints();
        c4.gridx = 0;
        c4.gridy = 2;
        c4.weightx = 1;
        c4.weighty = 0.05;
        c4.fill = GridBagConstraints.BOTH;

        TypePanel = new JPanel(new BorderLayout());
        TypePanel.setBackground(new Color(225,225,225));

        MainPanel.add(TypePanel,c4);

        Message = new JTextField();
        Message.setPreferredSize(new Dimension(600,24));

        TypePanel.add(Message,BorderLayout.WEST);

        Send = new JButton("Send");
        Send.setPreferredSize(new Dimension(100,24));

        TypePanel.add(Send,BorderLayout.EAST);

        frame.add(MainPanel,c1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatPanel::new);
    }
}
