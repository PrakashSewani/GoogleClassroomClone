package UI;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Area;

public class GradientMatteBorderTest implements Border {

    Insets margin;

    GradientMatteBorderTest (int top, int left, int bottom, int right) {
        super();
        margin = new Insets(top, left, bottom, right);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(new GradientPaint(
                x,
                y,
                new Color(5, 5, 5, 15),
                x,
                y + height,
                new Color(5, 5, 5,0)));

        Area border = new Area(new Rectangle (x, y, width, height));
        border.subtract(new Area (new Rectangle (x + margin.left, y + margin.top,
                width - margin.left - margin.right, height - margin.top - margin.bottom )));
        g2d.fill(border);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return margin;
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}
