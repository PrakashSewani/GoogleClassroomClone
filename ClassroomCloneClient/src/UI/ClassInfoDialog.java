package UI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClassInfoDialog {

    private JDialog dialog;

    ClassInfoDialog (Frame owner, ArrayList<String> studentList) {
        dialog = new JDialog(owner, true);
        dialog.setLayout(new GridBagLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(owner);

        initDialog(studentList);

        dialog.pack();
        dialog.setVisible(true);
    }

    private void initDialog(ArrayList<String> list) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 10, 0, 10);

        JLabel temp;
        for(int i = 0; i < list.size(); i++) {
            c.gridy = i;
            temp = new JLabel(i+1 + ") " + list.get(i));
            dialog.add(temp, c);
        }
    }
}
