package Entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Nodo {
    public Vector2 position;
    public Texture texture;
    public List<Nodo> connectedNodes;
    public boolean unlocked = false;
    public boolean completed = false;
    public Nodo(float x, float y, Texture texture) {
        this.position = new Vector2(x, y);
        this.texture = texture;
        this.connectedNodes = new ArrayList<>();
    }
    public void connect(Nodo other) {
        if (!connectedNodes.contains(other)) {
            connectedNodes.add(other);
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 64, 64);
    }

    public boolean isClicked(float x, float y) {
        return x > position.x - 32 && x < position.x + 32 && y > position.y - 32 && y < position.y + 32;
    }
}
