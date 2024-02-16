package luz;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 *
 * @author jacob
 */
public class Constantes {

    public final int WIDTH = 600;

    public final int HEIGHT = 600;

    public final Point2D PUNTO_SUPERIOR_DERECHO = new Point2D.Double(600, 0);

    public final Point2D PUNTO_SUPERIOR_IZQUIERDO = new Point2D.Double(0, 0);

    public final Point2D PUNTO_INFERIOR_DERECHO = new Point2D.Double(600, 600);

    public final Point2D PUNTO_INFERIOR_IZQUIERDO = new Point2D.Double(0, 600);

    public final Color LUZ = new Color(0x8de0a6);

    public final float RADIO_SOMBRA = 150;
}
