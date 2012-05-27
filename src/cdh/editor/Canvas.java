package cdh.editor;

import cdh.editor.objects.CanvasObject;
import cdh.editor.objects.Waypoint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class Canvas extends JPanel {

    private ArrayList<CanvasObject> waypoints = new ArrayList<CanvasObject>();
    private CanvasObject draggedObject = null;
    private JFileChooser fc;
    BufferedImage myPicture;

    public Canvas() {
        setPreferredSize(new Dimension(1200, 720));
        setBackground(Color.WHITE);
        bindListeners();

//        fc = new JFileChooser();
//        int returnVal = fc.showOpenDialog(null);
//        File file = new File("");
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            file = fc.getSelectedFile();
//        }

        try {
//            myPicture = ImageIO.read(file);
            myPicture = ImageIO.read(new File("C:/level1map.png"));
        } catch (IOException e) {
        }
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

        g.drawImage(myPicture, 0, 0, null);
        Color color = new Color(1, 1, 1, 0.6f);
        g2.setPaint(color);
        g2.fill(new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()));

        for(int i = 1; i < 13; i++){
           int xx = i * 90 + 15;
           g2.drawLine(xx, 0, xx, 720);
        }
        for(int i = 1; i < 8; i++){
           int yy = i * 90;
           g2.drawLine(15, yy, 1185, yy);
        }

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

            g2.setColor(Color.CYAN.darker());
            g2.drawLine((int) co1.getCenterX(), (int) co1.getCenterY(), (int) co2.getCenterX(), (int) co2.getCenterY());

            Point middle = new Point((int) (co1.getCenterX() + co2.getCenterX()) / 2 - 10, (int) (co1.getCenterY() + co2.getCenterY()) / 2 - 10);

            g2.setColor(new Color(150, 150, 150));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
            g2.drawString(Integer.toString(getAngle(co1, co2)) + fromUnicode("00B0"), (int) middle.getX() + 5, (int) middle.getY() + 5);
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
            if (y < 0)
                return 0;
            else
                return 180;
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

    char fromUnicode(String codePoint) {
        return (char) Integer.parseInt(codePoint, 16);
    }

// private classes
    private class MouseSupport extends MouseAdapter implements MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                draggedObject = findObjectToDrag(e.getPoint());
                if (draggedObject != null) {
                    waypoints.remove(draggedObject);
                } else {
                    createNewWaypoint(e.getPoint());
                }
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
