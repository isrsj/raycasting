package luz;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

/**
 *
 * @author jacob
 */
public class Mouse implements MouseListener, MouseMotionListener {

    private Boolean pressed, arrastrando, loContiene;
    private static double xCursor, yCursor;
    private static Point2D puntoCursor;

    public Mouse() {
        pressed = false;
        loContiene = false;
        arrastrando = false;
        yCursor = 400;
        xCursor = 400;
        puntoCursor = new Point2D.Double();
    }

    public Boolean getLoContiene() {
        return loContiene;
    }

    public void setLoContiene(Boolean loContiene) {
        this.loContiene = loContiene;
    }

    public Point2D getPuntoCursor() {
        return puntoCursor;
    }
    
    public Point2D getPuntoCursorFloat() {
        Point2D punto = new Point2D.Float((float)xCursor, (float)yCursor);
        return punto;
    }

    public Boolean getArrastrando() {
        return arrastrando;
    }

    public void setArrastrando(Boolean arrastrando) {
        this.arrastrando = arrastrando;
    }

    public void setPuntoCursor(Point2D puntoCursor) {
        this.puntoCursor = puntoCursor;
    }

    public double getxCursor() {
        return xCursor;
    }

    public void setxCursor(double xCursor) {
        this.xCursor = xCursor;
    }

    public double getyCursor() {
        return yCursor;
    }

    public void setyCursor(double yCursor) {
        this.yCursor = yCursor;
    }

    public Boolean getPressed() {
        return pressed;
    }

    public void setPressed(Boolean pressed) {
        this.pressed = pressed;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressed = true;
        yCursor = e.getY();
        xCursor = e.getX();
        puntoCursor = new Point2D.Double(xCursor, yCursor);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false;
        arrastrando = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        arrastrando = true;
        yCursor = e.getY();
        xCursor = e.getX();
        puntoCursor = new Point2D.Double(xCursor, yCursor);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
