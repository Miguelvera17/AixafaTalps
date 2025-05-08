package talps.m8.uf3.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import talps.m8.uf3.AixafaTalps;

public class GameOverScreen implements Screen {
    final AixafaTalps game;
    Texture fondoTextura;
    BitmapFont font;
    GlyphLayout layout;
    Music introMusic;
    Sound loseSound;

    float blinkAlpha = 1f;
    float blinkTimer = 0f;

    public GameOverScreen(final AixafaTalps game) {
        this.game = game;

        fondoTextura = new Texture(Gdx.files.internal("fondo.png"));

        font = new BitmapFont();
        font.getData().setScale(6);
        font.setColor(1, 0, 0, blinkAlpha);

        layout = new GlyphLayout();

        // Música de fondo
        introMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/intro2.ogg"));
//        introMusic.setLooping(true);
//        introMusic.setVolume(0.5f);

        // Efecto de sonido de perder
        loseSound = Gdx.audio.newSound(Gdx.files.internal("sounds/GameOver.wav"));

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                game.setScreen(new PressStartScreen(game));
                dispose();
                return true;
            }
        });
    }

    @Override
    public void show() {
        loseSound.play();  // Sonido de perder al entrar a la pantalla
        introMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        blinkTimer += delta;
        blinkAlpha = 0.5f + 0.5f * (float)Math.sin(blinkTimer * 2f);
        font.setColor(1, 0, 0, blinkAlpha);

        game.batch.begin();
        game.batch.draw(fondoTextura, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        String mensaje = "GAME OVER";
        layout.setText(font, mensaje);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2;
        font.draw(game.batch, layout, x, y);

        game.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        fondoTextura.dispose();
        font.dispose();
        loseSound.dispose();
        introMusic.dispose();  // Liberar recurso del sonido
    }
}
