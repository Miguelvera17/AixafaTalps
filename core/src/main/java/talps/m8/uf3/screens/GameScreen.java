package talps.m8.uf3.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.*;

import talps.m8.uf3.AixafaTalps;
import talps.m8.uf3.objects.Agujero;
import talps.m8.uf3.objects.Martillo;
import talps.m8.uf3.objects.TextoFlotante;
import talps.m8.uf3.utils.Settings;


public class GameScreen implements Screen {
    final AixafaTalps game;
    Texture fondoTextura;
    Texture topoTextura;
    Texture topoAplastadoTextura;
    Texture martilloTextura;
    Texture topoBombaTextura;
    Texture topoBombaAplastadoTextura;


    Array<Agujero> agujeros;
    List<TextoFlotante> textosFlotantes = new ArrayList<>();

    Random random = new Random();
    float tiempoEntreApariciones = Settings.TIEMPO_ENTRE_APARICIONES;
    long ultimoTiempoAparicion = 0;
    Agujero agujeroActivo = null;
    int ultimoNivelAcelerado = 0;


    Martillo martillo;
    int puntuacion = 0;
    BitmapFont font;

    private static final int NUM_FILAS = Settings.NUM_FILAS;
    private static final int NUM_COLUMNAS = Settings.NUM_COLUMNAS;
    private float tiempoRestante = Settings.tiempoRestante;
    private boolean juegoActivo = true;


    private final Map<String, float[]> coordenadasConocidas = new HashMap<>();

    public GameScreen(final AixafaTalps game) {
        this.game = game;
        fondoTextura = new Texture(Gdx.files.internal("fondo.png"));
        topoTextura = new Texture(Gdx.files.internal("topo.png"));
        topoAplastadoTextura = new Texture(Gdx.files.internal("topoAplastado.png"));
        martilloTextura = new Texture(Gdx.files.internal("martillo.png"));
        topoBombaTextura = new Texture(Gdx.files.internal("topo_bomba.png"));
        topoBombaAplastadoTextura = new Texture(Gdx.files.internal("topo_bomba_aplastado.png"));
        font = new BitmapFont();
        font.getData().setScale(Settings.FUENTE_ESCALA_MENU);
        agujeros = new Array<>();

        martillo = new Martillo(martilloTextura);

        // Inicializa posiciones personalizadas (igual que antes)
        inicializarCoordenadas();

        float anchoPantalla = Gdx.graphics.getWidth();
        float altoPantalla = Gdx.graphics.getHeight();
        float anchoFondoOriginal = fondoTextura.getWidth();
        float altoFondoOriginal = fondoTextura.getHeight();

        float escalaX = anchoPantalla / anchoFondoOriginal;
        float escalaY = altoPantalla / altoFondoOriginal;

        float anchoCelda = (anchoFondoOriginal / NUM_COLUMNAS) * escalaX;
        float altoCelda = (altoFondoOriginal / NUM_FILAS) * escalaY;

        for (int fila = 0; fila < NUM_FILAS; fila++) {
            for (int col = 0; col < NUM_COLUMNAS; col++) {
                String clave = (fila + 1) + "-" + (col + 1);
                float[] coords = coordenadasConocidas.getOrDefault(clave,
                    new float[]{(col + 0.5f) * anchoCelda, altoPantalla - ((fila + 0.5f) * altoCelda)});
                agujeros.add(new Agujero(coords[0], coords[1], anchoCelda, altoCelda, topoTextura, topoAplastadoTextura));
            }
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                float worldX = x;
                float worldY = Gdx.graphics.getHeight() - y;

                for (Agujero agujero : agujeros) {
                    if (agujero.estaVisible() && agujero.contiene(worldX, worldY)) {
                        agujero.aplastarTopo();

                        // Crear el texto flotante
                        if (agujero.esBomba()) {
                            tiempoRestante = Math.max(0, tiempoRestante - 1f);
                            textosFlotantes.add(new TextoFlotante(worldX, worldY, "-1", 1f)); // 1 segundo de duración
                        } else {
                            puntuacion++;
                            tiempoRestante += 2.5f;
                            textosFlotantes.add(new TextoFlotante(worldX, worldY, "+2.5", 1f)); // 1 segundo de duración

                            if (puntuacion >= 60) {
                                juegoActivo = false;
                                game.setScreen(new WinScreen(game));
                                dispose();
                                return true;
                            }

                            if (puntuacion % 10 == 0 && puntuacion > ultimoNivelAcelerado && (puntuacion / 10) <= 5) {
                                tiempoEntreApariciones *= 0.95f;
                                ultimoNivelAcelerado = puntuacion;
                            }
                        }

                        martillo.mostrar(worldX, worldY);
                        return true;
                    }
                }
                return false;
            }

        });

    }

    private void inicializarCoordenadas() {
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
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 1, 0, 1);
        game.batch.begin();
        game.batch.draw(fondoTextura, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (Agujero agujero : agujeros) agujero.render(game.batch);
        martillo.render(game.batch);

        font.getData().setScale(Settings.FUENTE_ESCALA_JUEGO);
        GlyphLayout layout = new GlyphLayout(font, "Puntuación: " + puntuacion);
        font.draw(game.batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2f, Gdx.graphics.getHeight() * 0.85f);
        GlyphLayout tiempoLayout = new GlyphLayout(font, "Tiempo: " + (int)tiempoRestante);
        font.draw(game.batch, tiempoLayout, (Gdx.graphics.getWidth() - tiempoLayout.width) / 2f, Gdx.graphics.getHeight() * 0.9f);

        // Renderiza los textos flotantes
        for (TextoFlotante texto : textosFlotantes) {
            texto.actualizar(delta);
            texto.render(game.batch, font);
        }

        // Elimina los textos flotantes que han terminado su vida
        textosFlotantes.removeIf(texto -> !texto.estaVivo());

        game.batch.end();

        if (juegoActivo) {
            tiempoRestante -= delta;
            if (tiempoRestante <= 0) {
                tiempoRestante = 0;
                juegoActivo = false;
                for (Agujero a : agujeros) a.ocultarTopo();
                game.setScreen(new GameOverScreen(game));
                dispose();
            }
        }

        if (juegoActivo && TimeUtils.nanoTime() - ultimoTiempoAparicion > tiempoEntreApariciones * 1_000_000_000L) {
            ultimoTiempoAparicion = TimeUtils.nanoTime();
            for (Agujero a : agujeros) a.ocultarTopo();
            agujeroActivo = agujeros.get(random.nextInt(agujeros.size));

            // 20% de probabilidad de ser topo bomba
            boolean esBomba = random.nextFloat() < 0.2f;
            if (esBomba) {
                agujeroActivo.configurarTipo(true, topoBombaTextura, topoBombaAplastadoTextura);
            } else {
                agujeroActivo.configurarTipo(false, topoTextura, topoAplastadoTextura);
            }

            agujeroActivo.mostrarTopo();
        }

        if (agujeroActivo != null && agujeroActivo.expirado()) agujeroActivo.ocultarTopo();
    }


    @Override public void resize(int w, int h) {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        fondoTextura.dispose();
        topoTextura.dispose();
        topoAplastadoTextura.dispose();
        martilloTextura.dispose();
        font.dispose();
        topoBombaTextura.dispose();
        topoBombaAplastadoTextura.dispose();
    }
}
