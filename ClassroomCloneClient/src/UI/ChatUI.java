package UI;

import javax.swing.*;
import java.awt.*;

public class ChatUI {

    private JFrame frame;
    private JLabel lblName;
    private JPanel messagePanel;
    private JTextField txtMessage;
    private JButton btnSend;

    private JPanel userPanel;

    ChatUI() {
        frame = new JFrame("Learning Management System");
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 800));
        frame.setMinimumSize(new Dimension(800, 800));

        initPanel();
        initUserPanel();

        frame.pack();
        frame.setVisible(true);
    }

    private void initPanel() {
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(5, 10, 5, 10);
        c.weighty = 0;
        c.weightx = 1.0;
//        c.fill = GridBagConstraints.HORIZONTAL;
        lblName = new JLabel("Recipient Name");
        lblName.setOpaque(true);
        lblName.setBackground(Color.PINK);
        lblName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        frame.add(lblName, c);

        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        messagePanel = new JPanel(new GridBagLayout());
        messagePanel.setBackground(Color.ORANGE);
        frame.add(messagePanel, c);

        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LAST_LINE_START;
        c.weighty = 0;
        c.weightx = 1.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        txtMessage = new JTextField();
        txtMessage.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));
        frame.add(txtMessage, c);

        c.gridx = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridwidth = 1;
//        c.weightx = 1.0;
        btnSend = new JButton("Send");
        btnSend.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));
        frame.add(btnSend);
    }

    private void initUserPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.weighty = 1.0;
        c.weightx = 0.25;
        c.insets = new Insets(5, 10, 5, 10);

        userPanel = new JPanel(new GridBagLayout());
        userPanel.setBackground(Color.CYAN);
        frame.add(userPanel, c);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatUI::new);
    }
}
