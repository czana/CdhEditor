package cdh.editor;

import cdh.editor.objects.Waypoint;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

public class Canvas extends JPanel {

    private ArrayList<Ellipse2D.Double> list = new ArrayList<Ellipse2D.Double>();
    private Ellipse2D.Double draggedObject = null;

    public Canvas() {
        setPreferredSize(new Dimension(1200, 720));
        setBackground(Color.WHITE);

        bindListeners();
    }

    private void bindListeners() {
        MouseSupport mouseSupport = new MouseSupport();
        this.addMouseListener(mouseSupport);
        this.addMouseMotionListener(mouseSupport);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Ellipse2D.Double object : list) {
            ((Waypoint) object).paint(g);
        }

        for (int i = 1; i < list.size(); i++) {
            Ellipse2D.Double d1 = list.get(i);
            Ellipse2D.Double d2 = list.get(i - 1);
            Point middle = new Point((int) (d1.getCenterX() + d2.getCenterX()) / 2 - 10, (int) (d1.getCenterY() + d2.getCenterY()) / 2 - 10);

            String angle = "";
            double x = d1.x - d2.x;
            double y = d1.y - d2.y;
            if (x != 0)
                if (x > 0)
                    angle = "" + (int)(Math.toDegrees(Math.atan(y / x)) + 90);
                else
                    angle = "" + (int)(Math.toDegrees(Math.atan(y / x)) + 270);
            g2.drawString(angle, middle.x, middle.y);
            
            g2.drawLine((int) d1.getCenterX(), (int) d1.getCenterY(), (int) d2.getCenterX(), (int) d2.getCenterY());
        }
    }

    public Ellipse2D.Double findFigure(Point point) {
        for (Ellipse2D.Double object : list) {
            if (object.contains(point))
                return object;
        }
        return null;
    }

    private class MouseSupport extends MouseAdapter implements MouseMotionListener {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                list.add(new Waypoint(e.getX(), e.getY(), 15, 15));
                System.out.println("created");
                draggedObject = null;
                repaint();
            } else
                draggedObject = findFigure(e.getPoint());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (draggedObject != null) {
                ((Waypoint) draggedObject).updatePosition(e.getX(), e.getY());
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }
}
