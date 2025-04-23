package talps.m8.uf3.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

import talps.m8.uf3.AixafaTalps;

public class GameScreen implements Screen {
    final AixafaTalps game;
    Texture fondoTextura;
    Texture topoTextura;
    Texture topoAplastadoTextura;
    Texture martilloTextura;
    Array<Agujero> agujeros;
    Random random = new Random();
    float tiempoEntreApariciones = 1.5f;
    long ultimoTiempoAparicion = 0;
    Agujero agujeroActivo = null;
    Sprite martilloSprite;
    boolean martilloVisible = false;
    long tiempoInicioMartillo;
    float duracionMartillo = 0.2f;
    int puntuacion = 0;
    BitmapFont font;

    private static final int NUM_FILAS = 3;
    private static final int NUM_COLUMNAS = 8;

    private final Map<String, float[]> coordenadasConocidas = new HashMap<>();

    public class Agujero {
        float xCentro, yCentro;
        Sprite topoSprite;
        boolean topoVisible = false;
        long tiempoInicioVisibilidad;
        float duracionVisibilidad = 1.0f;
        float anchoAgujeroEnPantalla; // Declaración
        float altoAgujeroEnPantalla; // Declaración
        boolean topoAplastado = false;

        public Agujero(float xCentro, float yCentro, float anchoAgujeroEnPantalla, float altoAgujeroEnPantalla) {
            this.xCentro = xCentro;
            this.yCentro = yCentro;
            this.anchoAgujeroEnPantalla = anchoAgujeroEnPantalla; // Asignación
            this.altoAgujeroEnPantalla = altoAgujeroEnPantalla; // Asignación
        }

        public void mostrarTopo() {
            topoSprite = new Sprite(topoTextura);
            topoSprite.setSize(topoSprite.getWidth() * 0.7f, topoSprite.getHeight() * 0.65f);
            topoSprite.setCenter(xCentro, yCentro + altoAgujeroEnPantalla * 0.3f);
            topoVisible = true;
            topoAplastado = false;
            tiempoInicioVisibilidad = TimeUtils.nanoTime();
        }

        public void ocultarTopo() {
            topoVisible = false;
            topoSprite = null;
            topoAplastado = false;
        }

        public void aplastarTopo() {
            if (topoVisible && topoSprite != null && !topoAplastado) {
                topoSprite.setTexture(topoAplastadoTextura);
                topoAplastado = true;
                mostrarMartillo(topoSprite.getX() + topoSprite.getWidth() / 2f, topoSprite.getY() + topoSprite.getHeight() / 2f);
                topoVisible = false;
                puntuacion++;
            }
        }

        public void render(SpriteBatch batch) {
            if (topoSprite != null) {
                topoSprite.draw(batch);
            }
        }

        public boolean estaVisible() {
            return topoVisible;
        }


        public boolean contiene(float touchX, float touchY) {
            if (topoSprite == null) return false;
            float spriteMinX = topoSprite.getX();
            float spriteMaxX = spriteMinX + topoSprite.getWidth();
            float spriteMinY = topoSprite.getY();
            float spriteMaxY = spriteMinY + topoSprite.getHeight();
            return touchX > spriteMinX && touchX < spriteMaxX && touchY > spriteMinY && touchY < spriteMaxY;
        }
    }

    public GameScreen(final AixafaTalps game) {
        this.game = game;
        fondoTextura = new Texture(Gdx.files.internal("fondo.png"));
        topoTextura = new Texture(Gdx.files.internal("topo.png"));
        topoAplastadoTextura = new Texture(Gdx.files.internal("topoAplastado.png"));
        martilloTextura = new Texture(Gdx.files.internal("martillo.png"));
        agujeros = new Array<>();
        font = new BitmapFont();
        font.getData().setScale(2f);

        float anchoPantalla = Gdx.graphics.getWidth();
        float altoPantalla = Gdx.graphics.getHeight();
        float anchoFondoOriginal = fondoTextura.getWidth();
        float altoFondoOriginal = fondoTextura.getHeight();

        float escalaX = anchoPantalla / anchoFondoOriginal;
        float escalaY = altoPantalla / altoFondoOriginal;

        float anchoCeldaOriginal = anchoFondoOriginal / NUM_COLUMNAS;
        float altoCeldaOriginal = altoFondoOriginal / NUM_FILAS;

        float anchoCeldaEnPantalla = anchoCeldaOriginal * escalaX;
        float altoCeldaEnPantalla = altoCeldaOriginal * escalaY;

        coordenadasConocidas.put("1-1", new float[]{380f, 40f});
        coordenadasConocidas.put("1-2", new float[]{700f, 40f});
        coordenadasConocidas.put("1-3", new float[]{1020f, 40f});
        coordenadasConocidas.put("1-4", new float[]{1350f, 40f});
        coordenadasConocidas.put("1-5", new float[]{1650f, 40f});
        coordenadasConocidas.put("1-6", new float[]{1980f, 40f});
        coordenadasConocidas.put("1-7", new float[]{2300f, 40f});
        coordenadasConocidas.put("1-8", new float[]{2620f, 40f});

        coordenadasConocidas.put("2-1", new float[]{420f, 190f});
        coordenadasConocidas.put("2-2", new float[]{730f, 190f});
        coordenadasConocidas.put("2-3", new float[]{1040f, 190f});
        coordenadasConocidas.put("2-4", new float[]{1350f, 190f});
        coordenadasConocidas.put("2-5", new float[]{1650f, 190f});
        coordenadasConocidas.put("2-6", new float[]{1960f, 190f});
        coordenadasConocidas.put("2-7", new float[]{2270f, 190f});
        coordenadasConocidas.put("2-8", new float[]{2580f, 190f});

        coordenadasConocidas.put("3-1", new float[]{450f, 340f});
        coordenadasConocidas.put("3-2", new float[]{750f, 340f});
        coordenadasConocidas.put("3-3", new float[]{1050f, 340f});
        coordenadasConocidas.put("3-4", new float[]{1350f, 340f});
        coordenadasConocidas.put("3-5", new float[]{1650f, 340f});
        coordenadasConocidas.put("3-6", new float[]{1950f, 340f});
        coordenadasConocidas.put("3-7", new float[]{2250f, 340f});
        coordenadasConocidas.put("3-8", new float[]{2550f, 340f});



        for (int fila = 0; fila < NUM_FILAS; fila++) {
            for (int columna = 0; columna < NUM_COLUMNAS; columna++) {
                float centroXPantalla;
                float centroYPantalla;
                String clave = (fila + 1) + "-" + (columna + 1);

                if (coordenadasConocidas.containsKey(clave)) {
                    float[] coords = coordenadasConocidas.get(clave);
                    centroXPantalla = coords[0];
                    centroYPantalla = coords[1];
                } else {
                    centroXPantalla = (columna + 0.5f) * anchoCeldaEnPantalla;
                    centroYPantalla = altoPantalla - ((fila + 0.5f) * altoCeldaEnPantalla);
                }
                agujeros.add(new Agujero(centroXPantalla, centroYPantalla, anchoCeldaEnPantalla, altoCeldaEnPantalla)); // ¡Ahora se pasan las dimensiones!
            }
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                float worldX = screenX;
                float worldY = Gdx.graphics.getHeight() - screenY;

                for (Agujero agujero : agujeros) {
                    if (agujero.estaVisible() && agujero.contiene(worldX, worldY)) {
                        agujero.aplastarTopo();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void mostrarMartillo(float x, float y) {
        martilloSprite = new Sprite(martilloTextura);
        martilloSprite.setCenter(x, y);
        martilloVisible = true;
        tiempoInicioMartillo = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 1, 0, 1);

        game.batch.begin();
        game.batch.draw(fondoTextura, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (Agujero agujero : agujeros) {
            agujero.render(game.batch);
        }

        if (martilloVisible && (TimeUtils.nanoTime() - tiempoInicioMartillo) < duracionMartillo * 1000000000L) {
            martilloSprite.draw(game.batch);
        } else {
            martilloVisible = false;
            martilloSprite = null;
        }

        CharSequence str = "Puntuación: " + puntuacion;
        font.getData().setScale(4f); // Más grande

        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, str);

        float x = (Gdx.graphics.getWidth() - layout.width) / 2f; // Centrado
        float y = Gdx.graphics.getHeight() * 0.85f; // Un poco más abajo

        font.draw(game.batch, layout, x, y);


        game.batch.end();

        // Mostrar nuevo topo si ha pasado el tiempo
        if (TimeUtils.nanoTime() - ultimoTiempoAparicion > tiempoEntreApariciones * 1000000000L) {
            ultimoTiempoAparicion = TimeUtils.nanoTime();

            // Elegir un agujero al azar
            int indiceAleatorio = random.nextInt(agujeros.size);
            Agujero agujeroAleatorio = agujeros.get(indiceAleatorio);

            // Ocultar todos los topos antes de mostrar uno nuevo
            for (Agujero a : agujeros) {
                a.ocultarTopo();
            }

            // Mostrar nuevo topo
            agujeroAleatorio.mostrarTopo();
            agujeroActivo = agujeroAleatorio;
        }

        // Verificar si el topo activo debe desaparecer por tiempo
        if (agujeroActivo != null && agujeroActivo.topoVisible) {
            if (TimeUtils.nanoTime() - agujeroActivo.tiempoInicioVisibilidad > agujeroActivo.duracionVisibilidad * 1000000000L) {
                agujeroActivo.ocultarTopo();
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        // Manejar el redimensionamiento
    }

    @Override
    public void show() {
        // Se llama al mostrar
    }

    @Override
    public void hide() {
        // Se llama al ocultar
    }

    @Override
    public void pause() {
        // Se llama al pausar
    }

    @Override
    public void resume() {
        // Se llama al reanudar
    }

    @Override
    public void dispose() {
        fondoTextura.dispose();
        topoTextura.dispose();
        topoAplastadoTextura.dispose();
        martilloTextura.dispose();
        font.dispose();
    }
}


