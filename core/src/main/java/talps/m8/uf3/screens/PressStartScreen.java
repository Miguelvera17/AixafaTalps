package talps.m8.uf3.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

import talps.m8.uf3.AixafaTalps;
import talps.m8.uf3.utils.Settings; // Asegúrate de que tienes esta clase

public class PressStartScreen implements Screen {
    final AixafaTalps game;
    private Stage stage;
    private Texture fondoTextura;
    private Texture modoNormalTextura;
    private Texture modoCorazonesTextura;
    private Music musicaFondo;
    private BitmapFont font;
    private GlyphLayout layout;
    private String textoTitulo = "Escoge el modo de juego";

    public PressStartScreen(final AixafaTalps game) {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), game.batch);
        Gdx.input.setInputProcessor(stage);

        fondoTextura = new Texture(Gdx.files.internal("fondo.png"));
        modoNormalTextura = new Texture(Gdx.files.internal("timer.png"));
        modoCorazonesTextura = new Texture(Gdx.files.internal("heart.png"));

        // Cargar fuente
        FileHandle fontFile = Gdx.files.internal("fonts/space.fnt"); // Usa tu fuente
        font = new BitmapFont(fontFile);
        font.getData().setScale(Settings.FUENTE_ESCALA_MENU * 1.5f); // Hacer la fuente del título más grande
        font.setColor(Color.WHITE);
        layout = new GlyphLayout();
        layout.setText(font, textoTitulo);

        // Cargar música de fondo
        FileHandle file = Gdx.files.internal("sounds/danger.wav");
        if (file.exists()) {
            musicaFondo = Gdx.audio.newMusic(file);
            musicaFondo.setLooping(true);
            musicaFondo.setVolume(0.5f);
            musicaFondo.play();
        }

        // Crear ImageButtons más grandes
        float botonAncho = Gdx.graphics.getWidth() * 0.6f; // Ancho del 60% de la pantalla (¡más grande!)
        float botonAltoNormal = botonAncho * (float)modoNormalTextura.getHeight() / modoNormalTextura.getWidth();
        float botonAltoCorazones = botonAncho * (float)modoCorazonesTextura.getHeight() / modoCorazonesTextura.getWidth();
        float espacioEntreBotones = botonAncho * 0.05f; // 5% del ancho del botón como espacio (ajustado para botones más grandes)
        float totalAnchoBotones = botonAncho * 2 + espacioEntreBotones;
        float inicioX = (Gdx.graphics.getWidth() - totalAnchoBotones) / 2f;
        float yPosicion = Gdx.graphics.getHeight() * 0.3f; // Ajustar la posición vertical para botones más grandes

        ImageButton botonNormal = new ImageButton(new TextureRegionDrawable(modoNormalTextura));
        botonNormal.setSize(botonAncho, botonAltoNormal);
        botonNormal.setPosition(inicioX, yPosicion);

        ImageButton botonCorazones = new ImageButton(new TextureRegionDrawable(modoCorazonesTextura));
        botonCorazones.setSize(botonAncho, botonAltoCorazones);
        botonCorazones.setPosition(inicioX + botonAncho + espacioEntreBotones, yPosicion);

        // Añadir Listeners para los botones
        botonNormal.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        botonCorazones.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreenConCorazones(game));
                dispose();
            }
        });

        // Añadir los botones al Stage
        stage.addActor(botonNormal);
        stage.addActor(botonCorazones);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        if (musicaFondo != null) musicaFondo.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.getBatch().begin();
        stage.getBatch().draw(fondoTextura, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Dibujar el texto del título en la parte superior
        font.draw(stage.getBatch(), layout, (Gdx.graphics.getWidth() - layout.width) / 2f, Gdx.graphics.getHeight() * 0.85f);

        stage.getBatch().end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        if (musicaFondo != null) musicaFondo.pause();
    }

    @Override
    public void resume() {
        if (musicaFondo != null) musicaFondo.play();
    }

    @Override
    public void hide() {
        if (musicaFondo != null) musicaFondo.pause();
    }

    @Override
    public void dispose() {
        stage.dispose();
        fondoTextura.dispose();
        modoNormalTextura.dispose();
        modoCorazonesTextura.dispose();
        if (musicaFondo != null) musicaFondo.dispose();
        if (font != null) font.dispose();
    }
}
