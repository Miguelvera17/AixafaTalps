package talps.m8.uf3.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextoFlotante {
    private float x, y;
    private String texto;
    private float tiempoVida;

    public TextoFlotante(float x, float y, String texto, float tiempoVida) {
        this.x = x;
        this.y = y;
        this.texto = texto;
        this.tiempoVida = tiempoVida;
    }

    public void actualizar(float delta) {
        tiempoVida -= delta;
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        if (tiempoVida > 0) {
            font.draw(batch, texto, x, y);
        }
    }

    public boolean estaVivo() {
        return tiempoVida > 0;
    }
}
