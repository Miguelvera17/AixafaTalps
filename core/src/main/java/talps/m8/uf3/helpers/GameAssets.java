package talps.m8.uf3.helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class GameAssets {
    public static final String FONDO = "fondo.png";
    public static final String TOPO = "topo.png";
    public static final String TOPO_APLASTADO = "topoAplastado.png";
    public static final String MARTILLO = "martillo.png";

    private final AssetManager assetManager = new AssetManager();

    public void load() {
        assetManager.load(FONDO, Texture.class);
        assetManager.load(TOPO, Texture.class);
        assetManager.load(TOPO_APLASTADO, Texture.class);
        assetManager.load(MARTILLO, Texture.class);
    }

    public void finishLoading() {
        assetManager.finishLoading();
    }

    public Texture getTexture(String path) {
        return assetManager.get(path, Texture.class);
    }

    public void dispose() {
        assetManager.dispose();
    }
}

