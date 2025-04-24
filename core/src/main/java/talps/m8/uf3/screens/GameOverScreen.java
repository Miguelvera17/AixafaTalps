package talps.m8.uf3.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;

import talps.m8.uf3.AixafaTalps;

public class GameOverScreen implements Screen {
    final AixafaTalps game;
    Texture fondoTextura;
    BitmapFont font;
    GlyphLayout layout;

    float blinkAlpha = 1f;
    float blinkTimer = 0f;

    public GameOverScreen(final AixafaTalps game) {
        this.game = game;

        fondoTextura = new Texture(Gdx.files.internal("fondo.png"));

        font = new BitmapFont();
        font.getData().setScale(6); // Tamaño muy grande
        font.setColor(1, 0, 0, blinkAlpha); // Rojo parpadeante

        layout = new GlyphLayout();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                game.setScreen(new PressStartScreen(game)); // O volver a GameScreen si prefieres
                dispose();
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        blinkTimer += delta;
        blinkAlpha = 0.5f + 0.5f * (float)Math.sin(blinkTimer * 2f);
        font.setColor(1, 0, 0, blinkAlpha); // Rojo parpadeante

        game.batch.begin();

        game.batch.draw(fondoTextura, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        String mensaje = "GAME OVER";
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
    }
}
