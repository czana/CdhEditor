package cdh.editor.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

public class GameObject extends CanvasObject {

    private final static int SIZE = 65;
    private String type = "tower spot";

    public GameObject(double x, double y) {
        super(x, y, SIZE);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.CYAN.darker());
        g2.fill(this);

        drawTextInside(g2);
    }

    private void drawTextInside(Graphics2D g2) {
        g2.setColor(new Color(70, 70, 70));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        Point point = getPointForString();
        g2.drawString(type, (int) point.getX(), (int) point.getY());
    }

    private Point getPointForString() {
        int py = (int) getCenterY() + 3;
        int px = (int) getCenterX();
        px -= type.length() * 2.4;
        
        return new Point(px, py);
    }
}
