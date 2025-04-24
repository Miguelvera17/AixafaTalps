package talps.m8.uf3.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import talps.m8.uf3.AixafaTalps;

public class PressStartScreen implements Screen {
    final AixafaTalps game;
    Texture fondoTextura;
    BitmapFont font;
    GlyphLayout layout;
    Music musicaFondo;

    float blinkAlpha = 1f;
    float blinkTimer = 0f;

    // Posiciones para cada texto
    Rectangle rectTimeTrial, rectHeartsMode;

    public PressStartScreen(final AixafaTalps game) {
        this.game = game;

        fondoTextura = new Texture(Gdx.files.internal("fondo.png"));

        // Carga la fuente BitmapFont personalizada
        FileHandle fontFile = Gdx.files.internal("fonts/space.fnt");
        font = new BitmapFont(fontFile);
        font.getData().setScale(4);  // Escala el tamaño de la fuente
        layout = new GlyphLayout();

        FileHandle file = Gdx.files.internal("sounds/intro.ogg");
        if (file.exists()) {
            musicaFondo = Gdx.audio.newMusic(file);
            musicaFondo.setLooping(true);
            musicaFondo.setVolume(0.5f);
            musicaFondo.play();
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                float x = screenX;
                float y = Gdx.graphics.getHeight() - screenY;

                if (rectTimeTrial.contains(x, y)) {
                    game.setScreen(new GameScreen(game));
                    dispose();
                } else if (rectHeartsMode.contains(x, y)) {
                    game.setScreen(new GameScreenConCorazones(game));
                    dispose();
                }

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

        // Texto 1 - Time Trial
        String texto1 = "Press to start Time Trial Mode";
        layout.setText(font, texto1);
        float x1 = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float y1 = Gdx.graphics.getHeight() * 0.5f + layout.height;
        font.draw(game.batch, layout, x1, y1);
        rectTimeTrial = new Rectangle(x1, y1 - layout.height, layout.width, layout.height);

        // Texto 2 - Hearts Mode
        String texto2 = "Press to start Hearts Mode";
        layout.setText(font, texto2);
        float x2 = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float y2 = Gdx.graphics.getHeight() * 0.5f - layout.height * 2;
        font.draw(game.batch, layout, x2, y2);
        rectHeartsMode = new Rectangle(x2, y2 - layout.height, layout.width, layout.height);

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
        if (musicaFondo != null) musicaFondo.dispose();
    }
}
