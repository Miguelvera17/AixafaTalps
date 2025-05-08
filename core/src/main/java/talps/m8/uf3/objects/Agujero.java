package talps.m8.uf3.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import talps.m8.uf3.utils.Settings;

public class Agujero {
    private float x, y;
    private float width, height;
    private Sprite topoSprite;
    private boolean visible = false;
    private boolean aplastado = false;
    private long tiempoVisible;
    private long tiempoAparecer;
    boolean esBomba;
    private boolean esCorazon = false;
    private Sound sonidoAplastar;

    private float alpha = Settings.ALPHA;
    private final float DURACION_FADE_IN = Settings.DURACION_FADE_IN;

    private Texture topoTextura;
    private Texture topoAplastadoTextura;

    public Agujero(float x, float y, float width, float height, Texture topo, Texture topoAplastado, Sound sonidoAplastar) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.topoTextura = topo;
        this.topoAplastadoTextura = topoAplastado;
        this.sonidoAplastar = sonidoAplastar;
    }

    public void mostrarTopo() {
        topoSprite = new Sprite(topoTextura);
        topoSprite.setSize(topoSprite.getWidth() * Settings.TOPO_ESCALA_ANCHO, topoSprite.getHeight() * Settings.TOPO_ESCALA_ALTO);
        topoSprite.setCenter(x, y + height * Settings.TOPO_DESPLAZAMIENTO_Y);
        visible = true;
        aplastado = false;
        tiempoVisible = TimeUtils.nanoTime();
        tiempoAparecer = tiempoVisible;
        alpha = 0f;
    }

    public void ocultarTopo() {
        visible = false;
        topoSprite = null;
        aplastado = false;
    }

    public void aplastarTopo() {
        if (visible && !aplastado && topoSprite != null) {
            topoSprite.setTexture(topoAplastadoTextura);
            aplastado = true;
            visible = false;
            if (sonidoAplastar != null) {
                sonidoAplastar.play(0.7f); // Reproducir el sonido de aplastar (ajusta el volumen si es necesario)
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (topoSprite != null) {
            if (!aplastado) {
                float elapsed = (TimeUtils.nanoTime() - tiempoAparecer) / 1_000_000_000f;
                alpha = Math.min(1f, elapsed / DURACION_FADE_IN);  // Aumentar alpha gradualmente
                topoSprite.setAlpha(alpha);
            } else {
                topoSprite.setAlpha(1f); // Si está aplastado, mostrar completo
            }
            topoSprite.draw(batch);
        }
    }

    public boolean estaVisible() { return visible; }

    public boolean contiene(float x, float y) {
        if (topoSprite == null) return false;
        return x > topoSprite.getX() && x < topoSprite.getX() + topoSprite.getWidth() &&
            y > topoSprite.getY() && y < topoSprite.getY() + topoSprite.getHeight();
    }

    public boolean expirado() {
        return visible && (TimeUtils.nanoTime() - tiempoVisible > Settings.TOPO_DURACION_VISIBLE * 1_000_000_000L);
    }

    public void configurarTipo(boolean esBomba, boolean esCorazon, Texture topo, Texture topoAplastado) {
        this.esBomba = esBomba;
        this.esCorazon = esCorazon;
        this.topoTextura= topo;
        this.topoAplastadoTextura = topoAplastado;
    }

    public Texture getTextura() {
        return topoTextura;
    }

    public boolean esCorazon() {
        return esCorazon;
    }


    public boolean esBomba() {
        return esBomba;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }



}
