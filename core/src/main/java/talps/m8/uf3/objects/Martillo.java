package talps.m8.uf3.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import talps.m8.uf3.utils.Settings;

public class Martillo {
    private Sprite sprite;
    private boolean visible = false;
    private long tiempoInicio;
    private float angulo = Settings.MARTILLO_ANGULO_INICIAL;
    private final Texture textura;

    public Martillo(Texture textura) {
        this.textura = textura;
    }

    public void mostrar(float x, float y) {
        sprite = new Sprite(textura);
        sprite.setOrigin(Settings.MARTILLO_ORIGEN_X, Settings.MARTILLO_ORIGEN_Y);
        sprite.setPosition(x, y);
        sprite.setRotation(Settings.MARTILLO_ANGULO_INICIAL);
        angulo = Settings.MARTILLO_ANGULO_INICIAL;
        visible = true;
        tiempoInicio = TimeUtils.nanoTime();
    }

    public void render(SpriteBatch batch) {
        if (!visible) return;

        float tiempo = (TimeUtils.nanoTime() - tiempoInicio) / 1_000_000_000f;

        if (tiempo < Settings.MARTILLO_DURACION) {
            angulo = 50f * (tiempo / Settings.MARTILLO_DURACION);
            sprite.setRotation(angulo);
            sprite.draw(batch);
        } else {
            visible = false;
            sprite = null;
        }
    }
}
