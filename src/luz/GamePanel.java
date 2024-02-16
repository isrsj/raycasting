package luz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author jacob
 */
public class GamePanel extends JPanel {

    Mouse mouse = new Mouse();
    Figuras figuras = new Figuras();
    Calculos calculos = new Calculos();
    Constantes constantes = new Constantes();

    private ArrayList<Rectangle2D> rectangulos;
    private ArrayList<Point2D> esquinaRectangulo;
    private ArrayList<Point2D> puntoSombra;
    private ArrayList<Polygon> sombra;
    private ArrayList<Line2D> lineas;
    private int[] verticeSombraX, verticeSombraY;
    private int[] poligonoX, poligonoY;
    private int contadorCuadrosVisibles;
    private Boolean dibujado, esquinaGuardada, puntoGuardado, estaEnCuadro;

    public GamePanel() {
        this.setLayout(null);
        this.setBackground(Color.black);
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
        dibujado = false;
        esquinaGuardada = false;
        puntoGuardado = false;
        estaEnCuadro = false;
        rectangulos = new ArrayList<Rectangle2D>();
        esquinaRectangulo = new ArrayList<Point2D>();
        puntoSombra = new ArrayList<Point2D>();
        sombra = new ArrayList<Polygon>();
        lineas = new ArrayList<Line2D>();
        verticeSombraX = new int[0];
        verticeSombraY = new int[0];
        poligonoX = new int[4];
        poligonoY = new int[4];
        contadorCuadrosVisibles = 0;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        pintarFondo(graphics2D);
        añadirRectangulo();
        añadirEsquina();
        dibujarRectangulo(graphics2D, colorRectangulo());
        moverPuntosSombra();
        sombra();
        inicializarSombras(graphics2D);
        dibujarSomnbra(graphics2D);
        coordenadasCursor(graphics2D);
        dibujarMarcoRectangulo(graphics2D, constantes.LUZ);
        eliminarSombraExtra();

        repaint();
    }

    public void pintarFondo(Graphics2D graphics2D) {
        if (mouse.getPressed()) {
            campoVision(graphics2D);
        } else {
            this.setBackground(Color.black);
        }
    }

    public void coordenadasCursor(Graphics2D graphics2D) {
        graphics2D.setColor(Color.red);
        cuadrosVisibles();
        String datos = "Mouse en  x: " + mouse.getxCursor() + "  y: " + mouse.getyCursor();
        graphics2D.drawString(datos, 100, 50);
    }

    public void cuadrosVisibles() {
        contadorCuadrosVisibles = 0;
        for (int i = 0; i < rectangulos.size(); i++) {
            double distancia = calculos.distanciaEntrePuntos(rectangulos.get(i).getCenterX(), rectangulos.get(i).getCenterY());
            if (distancia <= constantes.RADIO_SOMBRA) {
                contadorCuadrosVisibles++;
            }
        }
    }

    public void campoVision(Graphics2D graphics2D) {
        graphics2D.setPaint(constantes.LUZ);
        float[] distribucion = {0.0f, 0.3f, 1.0f};
        Color[] colores = {constantes.LUZ, new Color(0x6AA87D), Color.BLACK};
        RadialGradientPaint radialGradient = new RadialGradientPaint(mouse.getPuntoCursorFloat(), constantes.RADIO_SOMBRA, distribucion, colores, CycleMethod.NO_CYCLE);
        graphics2D.setPaint(radialGradient);
        encerrarLuz(graphics2D);
    }

    public void encerrarLuz(Graphics2D graphics2D) {
        if (!loContiene(0) && !loContiene(1) && !loContiene(2) && !loContiene(3) && !loContiene(4) && !loContiene(5) && !loContiene(6)) {
            graphics2D.fillOval((int) mouse.getxCursor() - 300, (int) mouse.getyCursor() - 300, 600, 600);
        } else {
            contadorCuadrosVisibles = 0;
        }
    }

    public Boolean loContiene(int indice) {
        return rectangulos.get(indice).contains(mouse.getPuntoCursor());
    }

    public void añadirRectangulo() {
        for (int i = 0; i < 7 && !dibujado; i++) {
            rectangulos.add(crearRectangulo());
        }
        dibujado = true;
    }

    public Rectangle2D crearRectangulo() {
        double x = Math.random() * (500 - 1 + 1) + 1;
        double y = Math.random() * (500 - 100 + 1) + 100;
        int lado = (int) (Math.random() * (80 - 50 + 1) + 50);
        return figuras.nuevoRectangulo(x, y, lado, lado);
    }

    public void añadirEsquina() {
        for (int i = 0; !esquinaGuardada && i < rectangulos.size(); i++) {
            esquinaRectangulo.add(figuras.puntoInferiorIzquierda(rectangulos.get(i)));
            esquinaRectangulo.add(figuras.puntoSuperiorIzquierda(rectangulos.get(i)));
            esquinaRectangulo.add(figuras.puntoSuperiorDerecha(rectangulos.get(i)));
            esquinaRectangulo.add(figuras.puntoInferiorDerecha(rectangulos.get(i)));
        }
        esquinaGuardada = true;
    }

    public void dibujarRectangulo(Graphics2D graphics2D, Color color) {
        for (int i = 0; i < rectangulos.size(); i++) {
            graphics2D.setColor(color);
            graphics2D.fill(rectangulos.get(i));
        }
    }

    public Color colorRectangulo() {
        return Color.black;
    }

    public void moverPuntosSombra() {
        if (mouse.getPressed()) {
            for (int i = 0; i < esquinaRectangulo.size() && !puntoGuardado; i++) {
                añadirPuntoSombra(i);
            }
            puntoGuardado = true;
        } else {
            puntoSombra.removeAll(puntoSombra);
            puntoGuardado = false;
        }
        if (mouse.getArrastrando()) {
            puntoSombra.removeAll(puntoSombra);
            for (int i = 0; i < esquinaRectangulo.size(); i++) {
                añadirPuntoSombra(i);
            }
        }
    }

    public void añadirPuntoSombra(int i) {
        double m = calculos.pendienteLinea(esquinaRectangulo.get(i), mouse.getPuntoCursor());
        double x = calculos.coordenadaXSombra(esquinaRectangulo.get(i), mouse.getPuntoCursor());
        double y = calculos.coordenadaYSombra(mouse.getPuntoCursor(), x, m);
        puntoSombra.add(figuras.nuevoPunto(x, y));
    }

    public void sombra() {
        verticeSombraX = null;
        verticeSombraY = null;
        int j = 0;
        verticeSombraX = new int[esquinaRectangulo.size() * 2];
        verticeSombraY = new int[esquinaRectangulo.size() * 2];
        j = 0;
        for (int i = 0; j < esquinaRectangulo.size(); i += 2) {
            verticeSombraX[i] = toInteger(esquinaRectangulo.get(j).getX());
            verticeSombraY[i] = toInteger(esquinaRectangulo.get(j).getY());
            j++;
        }
        j = 0;
        for (int i = 1; j < puntoSombra.size(); i += 2) {
            verticeSombraX[i] = toInteger(puntoSombra.get(j).getX());
            verticeSombraY[i] = toInteger(puntoSombra.get(j).getY());
            j++;
        }
    }

    public int toInteger(double numero) {
        return (int) numero;
    }

    public void inicializarSombras(Graphics2D graphics2D) {
        for (int i = 0; i < rectangulos.size(); i++) {
            inicioSombra(rectangulos.get(i), i * 4, graphics2D);
        }
    }

    public void inicioSombra(Rectangle2D rectangulo, int i, Graphics2D graphics2D) {
        // II II   SI SI   SD SD   ID ID
        // II SI SD ID
        // II SI SD ID
        // II SI SD ID
        // II SI SD ID
        switch (calculos.determinarCuadrante(rectangulo.getCenterX(), rectangulo.getCenterY(), mouse.getPuntoCursor())) {
            case 1 -> {// punto SD ----- 3 12
                int cuadrante = calculos.determinarCuadrante(esquinaRectangulo.get(i + 2), mouse.getPuntoCursor());
                cursorEnEsquinaSuperiorDerecha(cuadrante, asignarInicio(i), graphics2D);
            }
            case 2 -> {// punto SI
                int cuadrante = calculos.determinarCuadrante(esquinaRectangulo.get(i + 1), mouse.getPuntoCursor());
                cursorEnEsquinaSuperiorIzquierda(cuadrante, asignarInicio(i), graphics2D);
            }
            case 3 -> {// punto II
                int cuadrante = calculos.determinarCuadrante(esquinaRectangulo.get(i), mouse.getPuntoCursor());
                cursorEnEsquinaInferiorIzquierda(cuadrante, asignarInicio(i), graphics2D);
            }
            case 4 -> {// punto ID ------
                int cuadrante = calculos.determinarCuadrante(esquinaRectangulo.get(i + 3), mouse.getPuntoCursor());
                cursorEnEsquinaInferiorDerecha(cuadrante, asignarInicio(i), graphics2D);
            }
        }
    }

    public int asignarInicio(int esquina) {
        return esquina * 2;
    }

    public void cursorEnEsquinaSuperiorDerecha(int cuadrante, int inicio, Graphics2D graphics2D) {
        //  E  P
        // 0    II II   SI SI   SD SD   ID ID    8
        // 8    II II   SI SI   SD SD   ID ID    16
        // 16   II II   SI SI   SD SD   ID ID    24
        // 24   II II   SI SI   SD SD   ID ID    32
        // 32   II II   SI SI   SD SD   ID ID    40
        // 40   II II   SI SI   SD SD   ID ID    48
        // 49   II II   SI SI   SD SD   ID ID    56
        switch (cuadrante) {
            case 1 -> {// inicio sombra SI termina ID

                if (lineas.size() > asignarLugar(inicio) + 1) {
                    lineas.add(asignarLugar(inicio), figuras.nuevaLinea(verticeSombraX[inicio + 2], verticeSombraY[inicio + 2], verticeSombraX[inicio + 3], verticeSombraY[inicio + 3]));
                    lineas.add(asignarLugar(inicio) + 1, figuras.nuevaLinea(verticeSombraX[inicio + 6], verticeSombraY[inicio + 6], verticeSombraX[inicio + 7], verticeSombraY[inicio + 7]));
                }
                llenarArreglo(inicio + 2, inicio + 3, inicio + 7, inicio + 6);
                sombra.add(0, figuras.poligonoSombra(poligonoX, poligonoY, poligonoX.length));
                vaciarArreglo();
                dibujarPoligono(graphics2D, 0);
            }
            case 2 -> {// inicio sombra SI termina SD

                if (lineas.size() > asignarLugar(inicio) + 1) {
                    lineas.add(asignarLugar(inicio), figuras.nuevaLinea(verticeSombraX[inicio + 2], verticeSombraY[inicio + 2], verticeSombraX[inicio + 3], verticeSombraY[inicio + 3]));
                    lineas.add(asignarLugar(inicio) + 1, figuras.nuevaLinea(verticeSombraX[inicio + 4], verticeSombraY[inicio + 4], verticeSombraX[inicio + 5], verticeSombraY[inicio + 5]));
                }
                if (sombra.size() > 0) {
                    llenarArreglo(inicio + 2, inicio + 3, constantes.PUNTO_INFERIOR_IZQUIERDO, constantes.PUNTO_INFERIOR_DERECHO, inicio + 5, inicio + 4);
                    sombra.add(1, figuras.poligonoSombra(poligonoX, poligonoY, poligonoX.length));
                    vaciarArreglo();
                    dibujarPoligono(graphics2D, 1);
                }
            }
            case 3 -> {
                rellenarRectangulo(graphics2D, inicio);
            }
            case 4 -> {// inicio sombra SD termina ID

                if (lineas.size() > asignarLugar(inicio) + 1) {
                    lineas.add(asignarLugar(inicio), figuras.nuevaLinea(verticeSombraX[inicio + 4], verticeSombraY[inicio + 4], verticeSombraX[inicio + 5], verticeSombraY[inicio + 5]));
                    lineas.add(asignarLugar(inicio) + 1, figuras.nuevaLinea(verticeSombraX[inicio + 6], verticeSombraY[inicio + 6], verticeSombraX[inicio + 7], verticeSombraY[inicio + 7]));
                }
                if (sombra.size() > 2) {
                    llenarArreglo(inicio + 4, inicio + 5, inicio + 7, inicio + 6);
                    sombra.add(3, figuras.poligonoSombra(poligonoX, poligonoY, poligonoX.length));
                    vaciarArreglo();
                    dibujarPoligono(graphics2D, 3);
                }
            }
        }
    }

    public void cursorEnEsquinaInferiorDerecha(int cuadrante, int inicio, Graphics2D graphics2D) {
        switch (cuadrante) {
            case 1 -> {// inicio sombra SD termina ID

                if (lineas.size() > asignarLugar(inicio) + 1) {
                    lineas.add(asignarLugar(inicio), figuras.nuevaLinea(verticeSombraX[inicio + 4], verticeSombraY[inicio + 4], verticeSombraX[inicio + 5], verticeSombraY[inicio + 5]));
                    lineas.add(asignarLugar(inicio) + 1, figuras.nuevaLinea(verticeSombraX[inicio + 6], verticeSombraY[inicio + 6], verticeSombraX[inicio + 7], verticeSombraY[inicio + 7]));
                }
                llenarArreglo(inicio + 4, inicio + 5, inicio + 7, inicio + 6);
                sombra.add(0, figuras.poligonoSombra(poligonoX, poligonoY, poligonoX.length));
                vaciarArreglo();
                dibujarPoligono(graphics2D, 0);
            }
            case 2 -> {
                rellenarRectangulo(graphics2D, inicio);
            }
            case 3 -> {// inicio sombra II termina ID

                if (lineas.size() > asignarLugar(inicio) + 1) {
                    lineas.add(asignarLugar(inicio), figuras.nuevaLinea(verticeSombraX[inicio], verticeSombraY[inicio], verticeSombraX[inicio + 1], verticeSombraY[inicio + 1]));
                    lineas.add(asignarLugar(inicio) + 1, figuras.nuevaLinea(verticeSombraX[inicio + 6], verticeSombraY[inicio + 6], verticeSombraX[inicio + 7], verticeSombraY[inicio + 7]));
                }
                if (sombra.size() > 1) {
                    llenarArreglo(inicio, inicio + 1, constantes.PUNTO_SUPERIOR_IZQUIERDO, constantes.PUNTO_SUPERIOR_DERECHO, inicio + 7, inicio + 6);
                    sombra.add(2, figuras.poligonoSombra(poligonoX, poligonoY, poligonoX.length));
                    vaciarArreglo();
                    dibujarPoligono(graphics2D, 2);
                }
            }
            case 4 -> {// inicio sombra SD termina II

                if (lineas.size() > asignarLugar(inicio) + 1) {
                    lineas.add(asignarLugar(inicio), figuras.nuevaLinea(verticeSombraX[inicio + 4], verticeSombraY[inicio + 4], verticeSombraX[inicio + 5], verticeSombraY[inicio + 5]));
                    lineas.add(asignarLugar(inicio) + 1, figuras.nuevaLinea(verticeSombraX[inicio], verticeSombraY[inicio], verticeSombraX[inicio + 1], verticeSombraY[inicio + 1]));
                }
                if (sombra.size() > 2) {
                    llenarArreglo(inicio + 4, inicio + 5, inicio + 1, inicio);
                    sombra.add(3, figuras.poligonoSombra(poligonoX, poligonoY, poligonoX.length));
                    vaciarArreglo();
                    dibujarPoligono(graphics2D, 3);
                }
            }
        }
    }

    public void cursorEnEsquinaSuperiorIzquierda(int cuadrante, int inicio, Graphics2D graphics2D) {
        //  E  P
        // 0    II II   SI SI   SD SD   ID ID    8
        // 8    II II   SI SI   SD SD   ID ID    16
        // 16   II II   SI SI   SD SD   ID ID    24
        // 24   II II   SI SI   SD SD   ID ID    32
        // 32   II II   SI SI   SD SD   ID ID    40
        // 40   II II   SI SI   SD SD   ID ID    48
        switch (cuadrante) {
            case 1 -> {// inicio sombra SI termina SD

                if (lineas.size() > asignarLugar(inicio) + 1) {
                    lineas.add(asignarLugar(inicio), figuras.nuevaLinea(verticeSombraX[inicio + 2], verticeSombraY[inicio + 2], verticeSombraX[inicio + 3], verticeSombraY[inicio + 3]));
                    lineas.add(asignarLugar(inicio) + 1, figuras.nuevaLinea(verticeSombraX[inicio + 4], verticeSombraY[inicio + 4], verticeSombraX[inicio + 5], verticeSombraY[inicio + 5]));
                }
                llenarArreglo(inicio + 2, inicio + 3, constantes.PUNTO_INFERIOR_IZQUIERDO, constantes.PUNTO_INFERIOR_DERECHO, inicio + 5, inicio + 4);
                sombra.add(0, figuras.poligonoSombra(poligonoX, poligonoY, poligonoX.length));
                vaciarArreglo();
                dibujarPoligono(graphics2D, 0);
            }
            case 2 -> {// inicio sombra SD termina II

                if (lineas.size() > asignarLugar(inicio) + 1) {
                    lineas.add(asignarLugar(inicio), figuras.nuevaLinea(verticeSombraX[inicio + 4], verticeSombraY[inicio + 4], verticeSombraX[inicio + 5], verticeSombraY[inicio + 5]));
                    lineas.add(asignarLugar(inicio) + 1, figuras.nuevaLinea(verticeSombraX[inicio], verticeSombraY[inicio], verticeSombraX[inicio + 1], verticeSombraY[inicio + 1]));
                }
                if (sombra.size() > 0) {
                    llenarArreglo(inicio + 4, inicio + 5, inicio + 1, inicio);
                    sombra.add(1, figuras.poligonoSombra(poligonoX, poligonoY, poligonoX.length));
                    vaciarArreglo();
                    dibujarPoligono(graphics2D, 1);
                }
            }
            case 3 -> {// inicio sombra SI termina II

                if (lineas.size() > asignarLugar(inicio) + 1) {
                    lineas.add(asignarLugar(inicio), figuras.nuevaLinea(verticeSombraX[inicio + 2], verticeSombraY[inicio + 2], verticeSombraX[inicio + 3], verticeSombraY[inicio + 3]));
                    lineas.add(asignarLugar(inicio) + 1, figuras.nuevaLinea(verticeSombraX[inicio], verticeSombraY[inicio], verticeSombraX[inicio + 1], verticeSombraY[inicio + 1]));
                }
                if (sombra.size() > 1) {
                    llenarArreglo(inicio + 2, inicio + 3, inicio + 1, inicio);
                    sombra.add(2, figuras.poligonoSombra(poligonoX, poligonoY, poligonoX.length));
                    vaciarArreglo();
                    dibujarPoligono(graphics2D, 2);
                }
            }
            case 4 -> {
                rellenarRectangulo(graphics2D, inicio);
            }
        }
    }

    public void cursorEnEsquinaInferiorIzquierda(int cuadrante, int inicio, Graphics2D graphics2D) {
        //  E  P
        // 0    II II   SI SI   SD SD   ID ID    8
        // 8    II II   SI SI   SD SD   ID ID    16
        // 16   II II   SI SI   SD SD   ID ID    24
        // 24   II II   SI SI   SD SD   ID ID    32
        // 32   II II   SI SI   SD SD   ID ID    40
        // 40   II II   SI SI   SD SD   ID ID    48
        switch (cuadrante) {
            case 1 -> {
                rellenarRectangulo(graphics2D, inicio);
            }
            case 2 -> {// inicio sombra SI termina II

                if (lineas.size() > asignarLugar(inicio) + 1) {
                    lineas.add(asignarLugar(inicio), figuras.nuevaLinea(verticeSombraX[inicio + 2], verticeSombraY[inicio + 2], verticeSombraX[inicio + 3], verticeSombraY[inicio + 3]));
                    lineas.add(asignarLugar(inicio) + 1, figuras.nuevaLinea(verticeSombraX[inicio], verticeSombraY[inicio], verticeSombraX[inicio + 1], verticeSombraY[inicio + 1]));
                }
                if (sombra.size() > 0) {
                    llenarArreglo(inicio + 2, inicio + 3, inicio + 1, inicio);
                    sombra.add(1, figuras.poligonoSombra(poligonoX, poligonoY, poligonoX.length));
                    vaciarArreglo();
                    dibujarPoligono(graphics2D, 1);
                }
            }
            case 3 -> {// inicio sombra SI termina ID

                if (lineas.size() > asignarLugar(inicio) + 1) {
                    lineas.add(asignarLugar(inicio), figuras.nuevaLinea(verticeSombraX[inicio + 2], verticeSombraY[inicio + 2], verticeSombraX[inicio + 3], verticeSombraY[inicio + 3]));
                    lineas.add(asignarLugar(inicio) + 1, figuras.nuevaLinea(verticeSombraX[inicio + 6], verticeSombraY[inicio + 6], verticeSombraX[inicio + 7], verticeSombraY[inicio + 7]));
                }
                if (sombra.size() > 1) {
                    llenarArreglo(inicio + 2, inicio + 3, inicio + 7, inicio + 6);
                    sombra.add(2, figuras.poligonoSombra(poligonoX, poligonoY, poligonoX.length));
                    vaciarArreglo();
                    dibujarPoligono(graphics2D, 2);
                }
            }
            case 4 -> {// inicio sombra II termina ID ---- arreglar sombra

                if (lineas.size() > asignarLugar(inicio) + 1) {
                    lineas.add(asignarLugar(inicio), figuras.nuevaLinea(verticeSombraX[inicio], verticeSombraY[inicio], verticeSombraX[inicio + 1], verticeSombraY[inicio + 1]));
                    lineas.add(asignarLugar(inicio) + 1, figuras.nuevaLinea(verticeSombraX[inicio + 6], verticeSombraY[inicio + 6], verticeSombraX[inicio + 7], verticeSombraY[inicio + 7]));
                }
                if (sombra.size() > 2) {
                    llenarArreglo(inicio, inicio + 1, constantes.PUNTO_SUPERIOR_IZQUIERDO, constantes.PUNTO_SUPERIOR_DERECHO, inicio + 7, inicio + 6);
                    sombra.add(3, figuras.poligonoSombra(poligonoX, poligonoY, poligonoX.length));
                    vaciarArreglo();
                    dibujarPoligono(graphics2D, 3);
                }
            }
        }
    }

    public int asignarLugar(int inicio) {
        return inicio/4;
    }

    public void rellenarRectangulo(Graphics2D graphics2D, int inicio) {
        if (mouse.getPressed() || mouse.getArrastrando()) {
            this.setBackground(Color.BLACK);
            graphics2D.setColor(constantes.LUZ);
            graphics2D.fill(rectangulos.get(detectarRectangulo(inicio)));
        }
    }

    public int detectarRectangulo(int inicio) {
        return inicio/8;
    }

    public void llenarArreglo(int primerPunto, int segundoPunto, int tercerPunto, int cuartoPunto) {
        poligonoX = crearArreglo(verticeSombraX[primerPunto], verticeSombraX[segundoPunto], verticeSombraX[tercerPunto], verticeSombraX[cuartoPunto]);
        poligonoY = crearArreglo(verticeSombraY[primerPunto], verticeSombraY[segundoPunto], verticeSombraY[tercerPunto], verticeSombraY[cuartoPunto]);
    }

    public int[] crearArreglo(int primerPunto, int segundoPunto, int tercerPunto, int cuartoPunto) {
        int[] arreglo = {primerPunto, segundoPunto, tercerPunto, cuartoPunto};
        return arreglo;
    }

    public void llenarArreglo(int primerPunto, int segundoPunto, Point2D tercerPunto, int cuartoPunto, int quintoPunto) {
        poligonoX = new int[5];
        poligonoY = new int[5];
        poligonoX = crearArreglo(verticeSombraX[primerPunto], verticeSombraX[segundoPunto], toInteger(tercerPunto.getX()), verticeSombraX[cuartoPunto], verticeSombraX[quintoPunto]);
        poligonoY = crearArreglo(verticeSombraY[primerPunto], verticeSombraY[segundoPunto], toInteger(tercerPunto.getY()), verticeSombraY[cuartoPunto], verticeSombraY[quintoPunto]);
    }

    public int[] crearArreglo(int primerPunto, int segundoPunto, int tercerPunto, int cuartoPunto, int quintoPunto) {
        int[] arreglo = {primerPunto, segundoPunto, tercerPunto, cuartoPunto, quintoPunto};
        return arreglo;
    }

    public void llenarArreglo(int primerPunto, int segundoPunto, Point2D tercerPunto, Point2D cuartoPunto, int quintoPunto, int sextoPunto) {
        poligonoX = new int[5];
        poligonoY = new int[5];
        poligonoX = crearArreglo(verticeSombraX[primerPunto], verticeSombraX[segundoPunto], toInteger(tercerPunto.getX()), toInteger(cuartoPunto.getX()), verticeSombraX[quintoPunto], verticeSombraX[sextoPunto]);
        poligonoY = crearArreglo(verticeSombraY[primerPunto], verticeSombraY[segundoPunto], toInteger(tercerPunto.getY()), toInteger(cuartoPunto.getY()), verticeSombraY[quintoPunto], verticeSombraY[sextoPunto]);
    }

    public int[] crearArreglo(int primerPunto, int segundoPunto, int tercerPunto, int cuartoPunto, int quintoPunto, int sextoPunto) {
        int[] arreglo = {primerPunto, segundoPunto, tercerPunto, cuartoPunto, quintoPunto, sextoPunto};
        return arreglo;
    }

    public void dibujarPoligono(Graphics2D graphics2D, int i) {
        graphics2D.setColor(Color.black);
        graphics2D.fillPolygon(sombra.get(i));
    }

    public void vaciarArreglo() {
        poligonoX = null;
        poligonoY = null;
        poligonoX = new int[4];
        poligonoY = new int[4];
    }

    public void dibujarSomnbra(Graphics2D graphics2D) {
        if (!mouse.getPressed()) {
            graphics2D.setColor(Color.BLACK);
        }
        for (int i = 0; i < lineas.size(); i++) {
            graphics2D.draw(lineas.get(i));
        }
        lineas.removeAll(lineas);
    }

    public void dibujarMarcoRectangulo(Graphics2D graphics2D, Color color) {
        for (int i = 0; i < rectangulos.size(); i++) {
            graphics2D.setColor(color);
            graphics2D.draw(rectangulos.get(i));
        }
    }

    public void eliminarSombraExtra() {
        for (int i = 5; i < sombra.size(); i++) {
            sombra.remove(i);
        }
    }
}
