package talps.m8.uf3.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;

import talps.m8.uf3.AixafaTalps;
import talps.m8.uf3.utils.Settings;

public class WinScreen implements Screen {
    final AixafaTalps game;
    Texture fondoTextura;
    BitmapFont font;

    public WinScreen(final AixafaTalps game) {
        this.game = game;
        fondoTextura = new Texture(Gdx.files.internal("fondo.png")); // Fondo de la pantalla de victoria (puedes personalizar la imagen)
        font = new BitmapFont();
        font.getData().setScale(3f); // Aumenta el tamaño de la fuente
    }

    @Override
    public void show() {
        // Aquí puedes hacer algún setup inicial si es necesario
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); // Fondo negro
        game.batch.begin();
        game.batch.draw(fondoTextura, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Muestra el mensaje de victoria con un tamaño de fuente más grande
        font.getData().setScale(3f); // Grande para el mensaje de victoria
        String mensaje = "¡Felicidades, has ganado!";
        GlyphLayout layout = new GlyphLayout(font, mensaje);
        font.draw(game.batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2f, Gdx.graphics.getHeight() * 0.75f); // Ajusta la posición Y

        // Muestra la instrucción para continuar, con un tamaño de fuente más grande
        String instruccion = "Toca la pantalla para continuar";
        GlyphLayout instruccionLayout = new GlyphLayout(font, instruccion);
        font.draw(game.batch, instruccionLayout, (Gdx.graphics.getWidth() - instruccionLayout.width) / 2f, Gdx.graphics.getHeight() * 0.55f); // Mueve la instrucción más arriba

        game.batch.end();

        // Espera que el jugador toque la pantalla para ir al menú principal o reiniciar el juego
        if (Gdx.input.isTouched()) {
            game.setScreen(new PressStartScreen(game)); // Redirige al menú principal (o podrías reiniciar el juego)
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
    }
}
