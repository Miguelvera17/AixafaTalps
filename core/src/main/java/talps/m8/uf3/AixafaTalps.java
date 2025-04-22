package talps.m8.uf3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import talps.m8.uf3.screens.GameScreen;

public class AixafaTalps extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render(); // Llama al método render de la pantalla activa
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
