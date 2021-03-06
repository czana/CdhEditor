package cdh.editor.objects;

import cdh.editor.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Waypoint extends CanvasObject {

    private final static int SIZE = 12;

    public Waypoint(double x, double y) {
        super(x, y, SIZE);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (Canvas.findSelectedTrack().getWaypoints().contains(this)) {
            if (Canvas.findSelectedTrack().getWaypoints().indexOf(this) != Canvas.findSelectedTrack().getWaypoints().size()-1)
                g2.setColor(Color.GREEN.darker());
            else
                g2.setColor(Color.RED.darker());
        } else
            g2.setColor(Color.CYAN.darker());

        g2.fill(this);
    }
}
