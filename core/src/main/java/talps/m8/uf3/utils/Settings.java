package talps.m8.uf3.utils;

public class Settings {

    // Martillo
    public static final float MARTILLO_DURACION = 0.2f;
    public static final float MARTILLO_ANGULO_INICIAL = 0f;
    public static final float MARTILLO_ORIGEN_X = 800f;
    public static final float MARTILLO_ORIGEN_Y = 100f;

    // Agujero
    public static final float TOPO_DURACION_VISIBLE = 1.0f;
    public static final float TOPO_ESCALA_ANCHO = 0.7f;
    public static final float TOPO_ESCALA_ALTO = 0.65f;
    public static final float TOPO_DESPLAZAMIENTO_Y = 0.3f;
    public static float ALPHA = 0f;  // ← Nuevo
    public static final float DURACION_FADE_IN = 0.3f;

    // GameScreen
    public static final int NUM_FILAS = 3;
    public static final int NUM_COLUMNAS = 8;
    public static final float TIEMPO_ENTRE_APARICIONES = 1.5f;
    public static float tiempoRestante = 60f;

    // Escalado fuente
    public static final float FUENTE_ESCALA_MENU = 2f;
    public static final float FUENTE_ESCALA_JUEGO = 4f;

    private Settings() {
        // Constructor privado para que no se pueda instanciar
    }
}

