package luz;

import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author jacob
 */
public class Figuras {

    private Rectangle2D rectangulo;
    private Line2D linea;

    public Figuras() {
        linea = new Line2D.Double();
        rectangulo = new Rectangle2D.Double();
    }

    public Rectangle2D nuevoRectangulo(double x, double y, int w, int h) {
        rectangulo = new Rectangle2D.Double(x, y, w, h);
        return rectangulo;
    }

    public Line2D nuevaLinea(double xInicial, double yInicial, double xFinal, double yFinal) {
        linea = new Line2D.Double(xInicial, yInicial, xFinal, yFinal);
        return linea;
    }

    public Point2D nuevoPunto(double x, double y) {
        return new Point2D.Double(x, y);
    }

    public Point2D puntoSuperiorDerecha(Rectangle2D rectangulo) {
        return new Point2D.Double(rectangulo.getX() + rectangulo.getWidth() - 1, rectangulo.getY());
    }

    public Point2D puntoInferiorDerecha(Rectangle2D rectangulo) {
        return new Point2D.Double(rectangulo.getX() + rectangulo.getWidth() - 1, rectangulo.getY() + rectangulo.getHeight() - 1);
    }

    public Point2D puntoSuperiorIzquierda(Rectangle2D rectangulo) {
        return new Point2D.Double(rectangulo.getX(), rectangulo.getY());
    }

    public Point2D puntoInferiorIzquierda(Rectangle2D rectangulo) {
        return new Point2D.Double(rectangulo.getX(), rectangulo.getY() + rectangulo.getHeight() - 1);
    }

    public Polygon poligonoSombra(int[] x, int[] y, int puntos) {
        return new Polygon(x, y, puntos);
    }
}
