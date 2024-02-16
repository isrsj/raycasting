package luz;

import java.awt.geom.Point2D;

/**
 *
 * @author jacob
 */
public class Calculos {

    Constantes constantes = new Constantes();
    Mouse mouse = new Mouse();

    public Calculos() {

    }

    public double pendienteLinea(Point2D esquinaFigura, Point2D cursor) {
        return (cursor.getY() - esquinaFigura.getY()) / (cursor.getX() - esquinaFigura.getX());
    }

    public double coordenadaYSombra(Point2D cursor, double x, double m) {
        return -(m * (cursor.getX() - x)) + cursor.getY();
    }

    public double coordenadaXSombra(Point2D esquinaFigura, Point2D cursor) {
        if (esquinaFigura.getX() < cursor.getX()) {
            return constantes.PUNTO_SUPERIOR_IZQUIERDO.getX();
        } else {
            return constantes.PUNTO_SUPERIOR_DERECHO.getX();
        }
    }

    public int determinarCuadrante(Point2D origen, Point2D cursor) {
        //Nos situamos en el cuadrante 1 y 4
        if (origen.getX() < cursor.getX()) {
            if (origen.getY() > cursor.getY()) {
                return 1;
            } else {
                return 4;
            }
        } else {
            //Nos situamoss en el cuadrante 2 y 3
            if (origen.getY() > cursor.getY()) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    public int determinarCuadrante(double origenX, double origenY, Point2D cursor) {
        //Nos situamos en el cuadrante 1 y 4
        if (origenX < cursor.getX()) {
            if (origenY > cursor.getY()) {
                return 1;
            } else {
                return 4;
            }
        } else {
            //Nos situamoss en el cuadrante 2 y 3
            if (origenY > cursor.getY()) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    public double distanciaEntrePuntos(double x2, double y2) {
        return Math.sqrt(Math.pow((x2 - mouse.getxCursor()), 2) + Math.pow((y2 - mouse.getyCursor()), 2));
    }
}
