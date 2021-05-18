package UI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class LMSConstants {
    public static final Color MAIN_BACKGROUND_COLOR = new Color(241, 246, 253);

    public static final Color CLASS_PANEL_BACKGROUND_COLOR = new Color(165, 208, 251);
    public static final Color ANNOUNCEMENT_PANEL_BACKGROUND_COLOR = new Color(165, 208, 251);

    public static final Color BTN_BACKGROUND_COLOR = new Color(4, 14, 32);
    public static final Color BTN_FOREGROUND_COLOR = new Color(250, 250, 250);

    public static final Color TXT_BACKGROUND_COLOR = new Color(224, 228, 237);
    public static final Color TXT_FOREGROUND_COLOR = new Color(31, 31, 31);

    public static final Font BTN_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
    public static final Font TXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
    public static final Font LBL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

    public static final String SERVER_ADDR = "localhost";
    public static final int USER_SERVER_PORT = 4241;
    public static final int CHAT_SERVER_PORT = 4242;
    public static final int FILE_SERVER_PORT = 4243;
    public static final int CLASS_SERVER_PORT = 4244;
    public static final int ANNOUNCEMENT_SERVER_PORT = 4245;

    public static final ImageIcon logoIcn = new ImageIcon(LMSConstants.class.getClassLoader().getResource("logo.png").getPath().replaceAll("%20", " "));
    public static final ImageIcon bgImg = new ImageIcon(LMSConstants.class.getClassLoader().getResource("signup.png").getPath().replaceAll("%20", " "));
    public static final ImageIcon editIcn = new ImageIcon(LMSConstants.class.getClassLoader().getResource("edit.png").getPath().replaceAll("%20", " "));

    public static final MatteBorder SHADOW_BORDER = BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(5, 5, 5, 2));
    public static final Border BORDER = new GradientMatteBorderTest(0, 0, 5, 5);
}
