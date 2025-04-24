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

    float alpha = 0f;
    float fadeSpeedIn = 2.0f;

    float fadeOutAlpha = 0f;
    float fadeOutSpeed = 3.0f;
    boolean touched = false;
    boolean fadingOut = false;
    boolean transitioning = false;

    public StartScreen(final AixafaTalps game) {
        this.game = game;
        fondoTextura = new Texture(Gdx.files.internal("fondoInicial.png"));
        fondoSprite = new Sprite(fondoTextura);
        fondoSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Transición automática después de 3 segundos
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!fadingOut && !transitioning) {
                    fadingOut = true;
                }
            }
        }, 3f);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touched = true;
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (alpha < 1f) {
            alpha += delta * fadeSpeedIn;
            if (alpha > 1f) alpha = 1f;
        }

        if (touched && !fadingOut) {
            fadingOut = true;
        }

        if (fadingOut) {
            fadeOutAlpha += delta * fadeOutSpeed;
            if (!transitioning && fadeOutAlpha >= 1f) {
                transitioning = true;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        game.setScreen(new PressStartScreen(game));
                        dispose();
                    }
                }, 0.1f);
            }
        }

        game.batch.begin();

        fondoSprite.setColor(1, 1, 1, alpha);
        fondoSprite.setPosition(0, 0);
        fondoSprite.draw(game.batch);

        // Capa negra encima para efecto de salida
        game.batch.setColor(0, 0, 0, fadeOutAlpha);
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
