package talps.m8.uf3.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
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

public class GameScreenConCorazones implements Screen {
    final AixafaTalps game;
    Texture fondoTextura;
    Texture topoTextura;
    Texture topoAplastadoTextura;
    Texture martilloTextura;
    Texture topoBombaTextura;
    Texture topoBombaAplastadoTextura;
    Texture topoCorazonTextura;
    Texture topoCorazonAplastadoTextura;
    Texture corazonTextura;
    int nivel = 1;
    Sound hammerSound;
    Sound laughSound;
    Sound smashSound;
    Sound life;
    Sound explosionSound;

    Array<Agujero> agujeros;
    List<TextoFlotante> textosFlotantes = new ArrayList<>();
    Random random = new Random();

    float tiempoEntreApariciones = Settings.TIEMPO_ENTRE_APARICIONES;
    long ultimoTiempoAparicion = 0;
    int ultimoNivelAcelerado = 0;
    List<Agujero> agujerosActivos = new ArrayList<>();

    Martillo martillo;
    int puntuacion = 0;
    int corazones = 5;
    BitmapFont font;

    private static final int NUM_FILAS = Settings.NUM_FILAS;
    private static final int NUM_COLUMNAS = Settings.NUM_COLUMNAS;
    private boolean juegoActivo = true;

    private final Map<String, float[]> coordenadasConocidas = new HashMap<>();

    public GameScreenConCorazones(final AixafaTalps game) {
        this.game = game;
        fondoTextura = new Texture(Gdx.files.internal("fondo.png"));
        topoTextura = new Texture(Gdx.files.internal("topo.png"));
        topoAplastadoTextura = new Texture(Gdx.files.internal("topoAplastado.png"));
        martilloTextura = new Texture(Gdx.files.internal("martillo.png"));
        topoBombaTextura = new Texture(Gdx.files.internal("topo_bomba.png"));
        topoBombaAplastadoTextura = new Texture(Gdx.files.internal("topo_bomba_aplastado.png"));
        topoCorazonTextura = new Texture(Gdx.files.internal("topo_corazon.png"));
        topoCorazonAplastadoTextura = new Texture(Gdx.files.internal("topo_corazon_aplastado.png"));
        smashSound = Gdx.audio.newSound(Gdx.files.internal("sounds/smash.wav"));
        corazonTextura = new Texture(Gdx.files.internal("corazon.png"));
        font = new BitmapFont();
        font.getData().setScale(Settings.FUENTE_ESCALA_MENU);
        agujeros = new Array<>();
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explode1.wav"));
        laughSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laugh.wav"));
        life = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));
        inicializarCoordenadas();

        martillo = new Martillo(martilloTextura);

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
                agujeros.add(new Agujero(coords[0], coords[1], anchoCelda, altoCelda, topoTextura, topoAplastadoTextura, laughSound));
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

                        if (agujero.esBomba()) {
                            if (explosionSound != null) {
                                smashSound.stop();
                                life.stop();
                                explosionSound.play(0.5f);
                            }
                            corazones--;
                            textosFlotantes.add(new TextoFlotante(worldX, worldY, "-1", 1f));
                        } else if (agujero.esCorazon()) {
                            if (life != null) {
                                smashSound.stop();
                                explosionSound.stop();
                                life.play(0.5f);
                            }
                            corazones++;
                            textosFlotantes.add(new TextoFlotante(worldX, worldY, "+1", 1f));
                        } else {
                            if (smashSound != null) {
                                explosionSound.stop();
                                life.stop();
                                smashSound.play(1.0f);
                            }
                            puntuacion++;
                            textosFlotantes.add(new TextoFlotante(worldX, worldY, "", 1f));
                            int nuevoNivel = (puntuacion / 20) + 1;
                            if (nuevoNivel > nivel) {
                                nivel = nuevoNivel;
                            }
                            if (puntuacion >= 60) {
                                juegoActivo = false;
                                game.setScreen(new WinScreen(game));
                                dispose();
                                return true;
                            }

                            if (puntuacion % 10 == 0 && puntuacion > ultimoNivelAcelerado && (puntuacion / 10) <= 5) {
                                tiempoEntreApariciones *= 0.80f;
                                ultimoNivelAcelerado = puntuacion;
                            }
                        }

                        if (corazones <= 0) {
                            juegoActivo = false;
                            game.setScreen(new GameOverScreen(game));
                            dispose();
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

        for (Agujero agujero : agujeros) {
            if (agujero.expirado()) {
                if (!agujero.esBomba()) {
                    corazones--;
                    textosFlotantes.add(new TextoFlotante(agujero.getX(), agujero.getY(), "-1 <3", 1f));
                    if (corazones <= 0) {
                        juegoActivo = false;
                        game.setScreen(new GameOverScreen(game));
                        dispose();
                    }
                }
                agujero.ocultarTopo();
            }
            agujero.render(game.batch);
        }

        martillo.render(game.batch);

        font.getData().setScale(Settings.FUENTE_ESCALA_JUEGO);

        GlyphLayout layout = new GlyphLayout(font, "Puntuación: " + puntuacion);
        font.draw(game.batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2f, Gdx.graphics.getHeight() * 0.85f);

        GlyphLayout nivelLayout = new GlyphLayout(font, "Nivel: " + nivel);
        font.draw(game.batch, nivelLayout, (Gdx.graphics.getWidth() - nivelLayout.width) / 2f, Gdx.graphics.getHeight() * 0.90f);  // ↑ Aumenté la posición Y para que esté encima

        float corazonAncho = 80;
        float corazonAlto = 80;
        float espacioEntreCorazones = 20;
        float anchoTotalCorazones = corazones * corazonAncho + (corazones - 1) * espacioEntreCorazones;
        float inicioX = (Gdx.graphics.getWidth() - anchoTotalCorazones) / 2f;
        float posicionY = Gdx.graphics.getHeight() * 0.75f; // ↓ Deja los corazones en esta posición

        for (int i = 0; i < corazones; i++) {
            float x = inicioX + i * (corazonAncho + espacioEntreCorazones);
            game.batch.draw(corazonTextura, x, posicionY, corazonAncho, corazonAlto);
        }


        for (TextoFlotante texto : textosFlotantes) {
            texto.actualizar(delta);
            texto.render(game.batch, font);
        }
        textosFlotantes.removeIf(texto -> !texto.estaVivo());

        game.batch.end();

        if (juegoActivo && TimeUtils.nanoTime() - ultimoTiempoAparicion > tiempoEntreApariciones * 1_000_000_000L) {
            ultimoTiempoAparicion = TimeUtils.nanoTime();
            for (Agujero a : agujeros) a.ocultarTopo();
            agujerosActivos.clear();

            int numTopos = Math.min((puntuacion / 20) + 1, agujeros.size);
            Set<Integer> indicesSeleccionados = new HashSet<>();

            while (indicesSeleccionados.size() < numTopos) {
                int index = random.nextInt(agujeros.size);
                if (indicesSeleccionados.add(index)) {
                    Agujero a = agujeros.get(index);

                    float tipo = random.nextFloat();
                    if (tipo < 0.2f) {
                        a.configurarTipo(true, false, topoBombaTextura, topoBombaAplastadoTextura);
                    } else if (tipo < 0.3f) {
                        a.configurarTipo(false, true, topoCorazonTextura, topoCorazonAplastadoTextura);
                    } else {
                        a.configurarTipo(false, false, topoTextura, topoAplastadoTextura);
                    }
                    a.mostrarTopo();
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        fondoTextura.dispose();
        topoTextura.dispose();
        topoAplastadoTextura.dispose();
        martilloTextura.dispose();
        topoBombaTextura.dispose();
        topoBombaAplastadoTextura.dispose();
        topoCorazonTextura.dispose();
        topoCorazonAplastadoTextura.dispose();
        corazonTextura.dispose();
        if (explosionSound != null) {
            explosionSound.dispose();
        }
        if (smashSound != null) {
            smashSound.dispose();
        }
        if (laughSound != null) {
            laughSound.dispose();
        }
        if (life != null) {
            life.dispose();
        }
    }

    @Override
    public void show() {}

}
