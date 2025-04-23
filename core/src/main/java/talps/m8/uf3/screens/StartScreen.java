package talps.m8.uf3.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;

import talps.m8.uf3.AixafaTalps;

public class StartScreen implements Screen {
    final AixafaTalps game;
    Texture fondoTextura;
    Sprite fondoSprite;

    float alpha = 1f;
    float fadeSpeed = 0.6f; // Más lento para alargar la transición
    float zoom = 1f;
    float zoomSpeed = 0.008f; // Más lento y progresivo

    boolean fadingOut = false;
    boolean transitioning = false;

    public StartScreen(final AixafaTalps game) {
        this.game = game;
        fondoTextura = new Texture(Gdx.files.internal("fondoInicial.png"));
        fondoSprite = new Sprite(fondoTextura);
        fondoSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fondoSprite.setOrigin(fondoSprite.getWidth() / 2, fondoSprite.getHeight() / 2); // Origen al centro

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (!fadingOut) fadingOut = true;
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (!fadingOut && alpha > 0) {
            alpha -= delta * fadeSpeed;
        }

        if (fadingOut) {
            alpha += delta * fadeSpeed;
            zoom += delta * zoomSpeed * 4f;

            if (!transitioning && alpha >= 1f) {
                transitioning = true;

                // Espera 0.5 segundos más antes de cambiar de pantalla (opcional)
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        game.setScreen(new GameScreen(game));
                        dispose();
                    }
                }, 0.5f);
            }
        }


        game.batch.begin();

        fondoSprite.setScale(zoom);

        // Centrar sprite respecto al centro real de la pantalla
        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;
        fondoSprite.setPosition(centerX - fondoSprite.getOriginX() * zoom,
            centerY - fondoSprite.getOriginY() * zoom);

        fondoSprite.draw(game.batch);

        // Capa negra gradual
        game.batch.setColor(0, 0, 0, alpha);
        game.batch.draw(fondoTextura, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.setColor(1, 1, 1, 1);

        game.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        fondoTextura.dispose();
    }
}
