package talps.m8.uf3.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

import talps.m8.uf3.AixafaTalps;

public class PressStartScreen implements Screen {
    final AixafaTalps game;
    Texture fondoTextura;
    BitmapFont font;
    GlyphLayout layout;
    Music musicaFondo; // Cambiar a Music

    float blinkAlpha = 1f;
    float blinkTimer = 0f;

    public PressStartScreen(final AixafaTalps game) {
        this.game = game;

        fondoTextura = new Texture(Gdx.files.internal("fondo.png"));

        font = new BitmapFont();
        font.getData().setScale(6);
        font.setColor(1, 1, 1, blinkAlpha);

        layout = new GlyphLayout();

        // Intentar cargar la música de fondo
        FileHandle file = Gdx.files.internal("sounds/Afterburner.ogg");
        if (file.exists()) {
            Gdx.app.log("Music", "Archivo de música encontrado.");
            musicaFondo = Gdx.audio.newMusic(file);
            musicaFondo.setLooping(true); // Hacer que la música se repita
            musicaFondo.setVolume(0.5f); // Ajusta el volumen si es necesario
            musicaFondo.play(); // Reproducir la música de fondo
        } else {
            Gdx.app.error("Music Error", "El archivo de música no se encontró: sounds/intro.ogg");
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                dispose();
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        blinkTimer += delta;
        blinkAlpha = 0.5f + 0.5f * (float) Math.sin(blinkTimer * 2f);
        font.setColor(1, 1, 1, blinkAlpha);

        game.batch.begin();

        game.batch.draw(fondoTextura, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        String mensaje = "PRESS TO START";
        layout.setText(font, mensaje);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2;
        font.draw(game.batch, layout, x, y);

        game.batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        fondoTextura.dispose();
        font.dispose();
        if (musicaFondo != null) {
            musicaFondo.dispose(); // Liberar la música cuando se termina
        }
    }
}
