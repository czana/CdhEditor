package cdh.editor.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

public class Waypoint extends Ellipse2D.Double {

    public Waypoint(double x, double y, double size, int colRange) {
        super(x, y, size, size);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.BLACK);
        g2.fill(this);
    }

    public void updatePosition(int x, int y) {
        this.x = x - width / 2;
        this.y = y - width / 2;
    }
}
