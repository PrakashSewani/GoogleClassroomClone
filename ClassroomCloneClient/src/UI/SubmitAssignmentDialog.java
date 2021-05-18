package UI;

import backend.AnnouncementClient;
import backend.UserClient;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SubmitAssignmentDialog {

    private int announcementId;

    private JFileChooser fileChooser;
    private final JDialog dialog;
    private JLabel lblFile;
    private JButton btnUploadFile;
    private JButton btnSubmit;

    SubmitAssignmentDialog (Frame owner, int announcementId) {
        this.announcementId = announcementId;

        dialog = new JDialog(owner, true);
        dialog.setLayout(new GridBagLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(owner);

        initDialog();
        registerListeners((MainUI) owner);

        dialog.pack();
        dialog.setVisible(true);
    }

    private void initDialog () {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        lblFile = new JLabel("No File Selected.");
        dialog.add(lblFile, c);

        c.gridy = 1;
        btnUploadFile = new JButton("Upload File");
        dialog.add(btnUploadFile, c);

        c.gridy = 2;
        btnSubmit = new JButton("Submit");
        btnSubmit.setOpaque(true);
        btnSubmit.setBorderPainted(false);
        btnSubmit.setBackground(LMSConstants.BTN_BACKGROUND_COLOR);
        btnSubmit.setForeground(LMSConstants.BTN_FOREGROUND_COLOR);
        btnSubmit.setFont(LMSConstants.BTN_FONT);
        btnSubmit.setEnabled(false);
        dialog.add(btnSubmit, c);
    }

    private void registerListeners (MainUI mainUI) {
        btnUploadFile.addActionListener(e -> {
            fileChooser = new JFileChooser();
            int state = fileChooser.showOpenDialog(dialog);
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                lblFile.setText(file.getName());
                System.out.println(file);
                lblFile.setText(file.getName());
                btnSubmit.setEnabled(true);
            }
        });

        btnSubmit.addActionListener(e -> {
            boolean isSubmitted = new AnnouncementClient().submitAssignment(announcementId, UserClient.userId, fileChooser.getSelectedFile());
            if (isSubmitted) {
                JOptionPane.showMessageDialog(dialog,
                        "Assignment Submitted",
                        "Success",
                        JOptionPane.PLAIN_MESSAGE);
                ExpandedAnnouncementPanel panel = (ExpandedAnnouncementPanel) mainUI.homePagePanel.expandedAnnouncementPanel;
                panel.submitted();
            }
        });
    }
}
