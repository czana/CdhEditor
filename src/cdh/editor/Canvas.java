package cdh.editor;

import cdh.editor.objects.CanvasObject;
import cdh.editor.objects.Waypoint;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.RenderingHints;

public class Canvas extends JPanel {

    private ArrayList<CanvasObject> waypoints = new ArrayList<CanvasObject>();
    private CanvasObject draggedObject = null;

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

        paintCanvasObjects(g);
        paintLineBetweenWaypointsWithAngle(g2);

    }

    private void paintCanvasObjects(Graphics g) {
        for (CanvasObject object : waypoints) {
            object.paint(g);
        }
    }

    private void paintLineBetweenWaypointsWithAngle(Graphics g2) {
        for (int i = 1; i < waypoints.size(); i++) {
            CanvasObject co1 = waypoints.get(i);
            CanvasObject co2 = waypoints.get(i - 1);

            g2.drawLine((int) co1.getCenterX(), (int) co1.getCenterY(), (int) co2.getCenterX(), (int) co2.getCenterY());

            Point middle = new Point((int) (co1.getCenterX() + co2.getCenterX()) / 2 - 10, (int) (co1.getCenterY() + co2.getCenterY()) / 2 - 10);
            g2.drawString(Integer.toString(getAngle(co1, co2)), (int)middle.getX(), (int)middle.getY());
        }
    }

    private int getAngle(CanvasObject co1, CanvasObject co2) {
        double angle = 90;
        double x = co1.x - co2.x;
        double y = co1.y - co2.y;

        if (x != 0) {
            angle += Math.toDegrees(Math.atan(y / x));
            if (x <= 0)
                angle += 180;
            
            return (int) angle;
        } else {
            return 0;
        }
    }

    public CanvasObject findObjectToDrag(Point point) {
        for (CanvasObject object : waypoints) {
            if (object.contains(point))
                return object;
        }
        return null;
    }

    public void createNewWaypoint(Point point) {
        waypoints.add(new Waypoint(point.getX(), point.getY()));
    }

// private classes
    private class MouseSupport extends MouseAdapter implements MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                createNewWaypoint(e.getPoint());
                repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            draggedObject = findObjectToDrag(e.getPoint());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            draggedObject = null;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (draggedObject != null) {
                draggedObject.updatePosition(e.getX(), e.getY());
                repaint();
            }
        }
    }
}
