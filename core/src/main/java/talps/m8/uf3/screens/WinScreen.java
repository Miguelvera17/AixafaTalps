package talps.m8.uf3.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import talps.m8.uf3.AixafaTalps;

public class WinScreen implements Screen {
    final AixafaTalps game;
    Texture fondoTextura;
    BitmapFont font;
    Music victoryMusic;

    public WinScreen(final AixafaTalps game) {
        this.game = game;

        fondoTextura = new Texture(Gdx.files.internal("fondo.png"));

        font = new BitmapFont();
        font.getData().setScale(3f);

        // Cargar música de victoria
//        victoryMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/marioWin.mp3"));
//        victoryMusic.setLooping(false);
//        victoryMusic.play();
    }

    @Override
    public void show() {
        // Nada necesario aquí de momento
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); // Fondo negro

        // Efecto de parpadeo
        float alpha = (float)Math.abs(Math.sin(TimeUtils.nanoTime() / 500_000_000.0));
        font.setColor(1, 1, 1, alpha);  // Blanco con alpha oscilante

        game.batch.begin();
        game.batch.draw(fondoTextura, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Mensaje de victoria (dos líneas)
        String mensaje = "¡Felicidades!\nHas ganado";
        GlyphLayout layout = new GlyphLayout(font, mensaje, font.getColor(), 0, Align.center, true);
        font.draw(game.batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2f, Gdx.graphics.getHeight() * 0.75f);

        // Instrucción para continuar (con opacidad fija)
        font.setColor(1, 1, 1, 1); // Opacidad completa
        String instruccion = "Toca la pantalla para continuar";
        GlyphLayout instruccionLayout = new GlyphLayout(font, instruccion);
        font.draw(game.batch, instruccionLayout, (Gdx.graphics.getWidth() - instruccionLayout.width) / 2f, Gdx.graphics.getHeight() * 0.55f);

        game.batch.end();

        // Si se toca la pantalla, volver al menú
        if (Gdx.input.justTouched()) {
            victoryMusic.stop();
            game.setScreen(new PressStartScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int w, int h) {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        fondoTextura.dispose();
        font.dispose();
        victoryMusic.dispose();
    }
}
