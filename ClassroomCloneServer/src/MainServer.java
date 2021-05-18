import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainServer {

    JFrame frame;
    JButton btnStart;
    JButton btnStop;

    MainServer() {
        frame = new JFrame("Learning Management System Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        initServerFrame();

        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                DatabaseHandler.closeConnection();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    private void initServerFrame() {
        btnStart = new JButton("Start Server");
        btnStart.addActionListener(e -> {
            startAllServers();
        });
        frame.add(btnStart, BorderLayout.CENTER);
    }

    private void startAllServers() {
        new Thread(UserServer::new).start();
//        new Thread(ChatServer::new).start();
//        new Thread(FileServer::new).start();
        new Thread(ClassServer::new).start();
        new Thread(AnnouncementServer::new).start();
    }

    public static void main(String[] args) {
        new MainServer();
    }
}
