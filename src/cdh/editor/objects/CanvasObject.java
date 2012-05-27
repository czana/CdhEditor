package cdh.editor.objects;

import java.awt.geom.Ellipse2D;

public abstract class CanvasObject extends Ellipse2D.Double implements Paintable{

    private double centerCorrection;

    public CanvasObject(double x, double y, double size) {
        super(x - size / 2, y - size / 2, size, size);
        centerCorrection = size / 2;
    }

    public void updatePosition(int x, int y) {
        this.x = x - centerCorrection;
        this.y = y - centerCorrection;
    }
}
