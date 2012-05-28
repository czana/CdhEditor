package cdh.editor;

import cdh.editor.objects.CanvasObject;
import cdh.editor.objects.GameObject;
import cdh.editor.objects.Track;
import cdh.editor.objects.Waypoint;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class Canvas extends JPanel implements KeyListener {

    public static ArrayList<Track> tracks = new ArrayList<Track>();
    private ArrayList<CanvasObject> gameObjects = new ArrayList<CanvasObject>();
    private CanvasObject draggedObject = null;
    private JFileChooser fc;
    private BufferedImage myPicture = null;

    public Canvas() {
        setPreferredSize(new Dimension(1200, 720));
        setBackground(Color.WHITE);
        setFocusable(true);
        fc = new JFileChooser();

        bindListeners();

        tracks.add(new Track(true));
    }

    private void bindListeners() {
        MouseSupport mouseSupport = new MouseSupport();
        this.addKeyListener(this);
        this.addMouseListener(mouseSupport);
        this.addMouseMotionListener(mouseSupport);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintImageWithMask(g);
        paintLineBetweenWaypointsWithAngle(g2);
        paintCanvasObjects(g);
    }

    private void paintCanvasObjects(Graphics g) {
        for (CanvasObject object : gameObjects) {
            object.paint(g);
        }

        for (Track track : tracks) {
            for (CanvasObject object : track.getWaypoints()) {
                object.paint(g);
            }
        }
    }

    private void paintLineBetweenWaypointsWithAngle(Graphics g2) {
        for (Track track : tracks) {
            ArrayList<CanvasObject> waypoints = track.getWaypoints();

            for (int i = 1; i < waypoints.size(); i++) {
                CanvasObject co1 = waypoints.get(i);
                CanvasObject co2 = waypoints.get(i - 1);

                if (track.isSelected())
                    g2.setColor(Color.GREEN.darker());
                else
                    g2.setColor(Color.CYAN.darker());

                g2.drawLine((int) co1.getCenterX(), (int) co1.getCenterY(), (int) co2.getCenterX(), (int) co2.getCenterY());

                Point middle = new Point((int) (co1.getCenterX() + co2.getCenterX()) / 2 - 10, (int) (co1.getCenterY() + co2.getCenterY()) / 2 - 10);

                g2.setColor(new Color(70, 70, 70));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                g2.drawString(Integer.toString(getAngle(co1, co2)) + fromUnicode("00B0"), (int) middle.getX() + 5, (int) middle.getY() + 5);
            }
        }
    }

    private void paintImageWithMask(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if (myPicture != null) {
            g.drawImage(myPicture, 0, 0, null);

            Color color = new Color(1, 1, 1, 0.6f);
            g2.setPaint(color);
            g2.fill(new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()));

            g2.setColor(Color.WHITE);
            for (int i = 1; i < 13; i++) {
                int pos = i * 90;
                g2.drawLine(pos + 15, 0, pos + 15, 720);

                if (i <= 7)
                    g2.drawLine(0, pos, 1200, pos);
            }
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
        for (Track track : tracks) {
            for (CanvasObject object : track.getWaypoints()) {
                if (object.contains(point))
                    return object;
            }
        }

        for (CanvasObject object : gameObjects) {
            if (object.contains(point))
                return object;
        }
        return null;
    }

    public static Track findSelectedTrack() {
        for (Track track : tracks) {
            if (track.isSelected())
                return track;
        }
        return null;
    }

    public void createNewWaypoint(Point point) {
        findSelectedTrack().getWaypoints().add(new Waypoint(point.getX(), point.getY()));
    }

    public void createNewGameObject(Point point) {
        gameObjects.add(new GameObject(point.getX(), point.getY()));
    }

    char fromUnicode(String codePoint) {
        return (char) Integer.parseInt(codePoint, 16);
    }

    private void loadImageFromFile() {
        if (fc.showOpenDialog(this.getParent()) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            try {
                myPicture = ImageIO.read(file);
//                myPicture = ImageIO.read(new File("C:/level1map.png"));
            } catch (IOException e) {
                System.out.print("Problem with loading image");
            }
            repaint();
        }
    }

// key listener
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'o') {
            loadImageFromFile();
        }
    }

    public void keyPressed(KeyEvent e) {
        System.out.println("tpes");
    }

    public void keyReleased(KeyEvent e) {
    }

// private classes
    private class MouseSupport extends MouseAdapter implements MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (e.isShiftDown()) {
                    createNewGameObject(e.getPoint());

                } else {
                    draggedObject = findObjectToDrag(e.getPoint());
                    if (draggedObject != null) {
                        findSelectedTrack().getWaypoints().remove(draggedObject);
                    } else {
                        createNewWaypoint(e.getPoint());
                    }
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
