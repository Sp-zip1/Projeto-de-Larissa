package Telas;

import Entidades.Nodo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Game;

import java.util.ArrayList;
import java.util.List;

public class TelaMapa implements Screen {

    private final Game game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private List<Nodo> nodes;
    private Nodo currentNode;

    public TelaMapa(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        nodes = new ArrayList<>();

        // Exemplo de criação dos nós
        Texture nodeTexture = new Texture("slice.png");

        Nodo n1 = new Nodo(200, 150, nodeTexture);
        Nodo n2 = new Nodo(400, 300, nodeTexture);
        Nodo n3 = new Nodo(600, 150, nodeTexture);

        n1.connect(n2);
        n2.connect(n3);

        n1.unlocked = true;
        currentNode = n1;

        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Desenha conexões
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        for (Nodo n : nodes) {
            for (Nodo c : n.connectedNodes) {
                shapeRenderer.line(n.position.x, n.position.y, c.position.x, c.position.y);
            }
        }
        shapeRenderer.end();

        // Desenha nós
        batch.begin();
        for (Nodo n : nodes) {
            if (n.unlocked)
                batch.setColor(Color.WHITE);
            else
                batch.setColor(Color.DARK_GRAY);

            n.draw(batch);
        }
        batch.end();

        // Detecta clique
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY(); // converte Y
            for (Nodo n : nodes) {
                if (n.isClicked(x, y) && n.unlocked) {
                    currentNode = n;
                    n.completed = true;

                    // desbloqueia nós conectados
                    for (Nodo c : n.connectedNodes) {
                        c.unlocked = true;
                    }

                    // === Muda para tela de batalha ===
                    game.setScreen(new TelaBatalha(game, this));
                    return;
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        for (Nodo n : nodes) n.texture.dispose();
    }

    // outros métodos vazios do Screen
    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
