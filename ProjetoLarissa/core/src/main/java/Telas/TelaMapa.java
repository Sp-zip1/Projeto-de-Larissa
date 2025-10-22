package Telas;

import Atores.Inimigo;
import Ações.Efeito;
import Entidades.Nodo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Game;
import io.github.some_example_name.Main;

import java.util.ArrayList;
import java.util.List;

public class TelaMapa implements Screen {

    private final Game game;
    private final Main main;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private List<Nodo> nodes;
    private Nodo currentNode;
    private ArrayList<Efeito> efeitos = new ArrayList<>(), efeitosSlime = new ArrayList<>(), efeitosCom = new ArrayList<>(), efeitosCmd = new ArrayList<>();
    private ArrayList<Inimigo> inimigosNivel = new ArrayList<>();
    private Texture nodeTexture;

    public TelaMapa(Game game, Main main) {
        this.game = game;
        this.main = main;
        efeitos.add(Efeito.curaIn(4));
        efeitos.add(Efeito.danoExtraIn(1));
        efeitos.add(Efeito.danoJogador(10));
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        nodes = new ArrayList<>();

        nodeTexture = new Texture("node.png");

        // Exemplo de inimigos
        inimigosNivel.add(new Inimigo(55, 55, 0, main.inimigo, main.inimigoHit, main.inimigo, 0f, 0f, efeitos));
        inimigosNivel.add(new Inimigo(70, 70, 0, main.inimigo1, main.inimigo1Hit, main.inimigo1, 0f, 0f, efeitos));
        inimigosNivel.add(new Inimigo(30, 30, 0, main.inimigo2, main.inimigo2Hit, main.inimigo2, 0f, 0f, efeitos));

        gerarMapaFixo();
    }

    private void gerarMapaFixo() {
        float startX = 350;
        float stepX = 180;
        float baseY = 300;
        float offsetY = 150;
        Nodo start = new Nodo(startX, baseY, nodeTexture);
        start.unlocked = true;

        // Caminho superior
        Nodo a1 = new Nodo(startX + stepX, baseY + offsetY, nodeTexture);
        Nodo a2 = new Nodo(startX + stepX * 2, baseY + offsetY, nodeTexture);
        Nodo a3 = new Nodo(startX + stepX * 3, baseY + offsetY, nodeTexture);
        Nodo b1 = new Nodo(startX + stepX, baseY - offsetY, nodeTexture);
        Nodo b2 = new Nodo(startX + stepX * 2, baseY - offsetY, nodeTexture);
        Nodo b3 = new Nodo(startX + stepX * 3, baseY - offsetY, nodeTexture);
        Nodo boss = new Nodo(startX + stepX * 4, baseY, nodeTexture);
        start.connect(a1);
        start.connect(b1);

        a1.connect(a2);
        a2.connect(a3);
        a3.connect(boss);
        b1.connect(b2);
        b2.connect(b3);
        b3.connect(boss);

        nodes.add(start);
        nodes.add(a1); nodes.add(a2); nodes.add(a3);
        nodes.add(b1); nodes.add(b2); nodes.add(b3);
        nodes.add(boss);

        currentNode = start;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Desenha conexões
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GOLD);
        for (Nodo n : nodes) {
            for (Nodo c : n.connectedNodes) {
                shapeRenderer.line(n.position.x, n.position.y, c.position.x, c.position.y);
            }
        }
        shapeRenderer.end();
        batch.begin();
        for (Nodo n : nodes) {
            if (n.completed)
                batch.setColor(Color.GREEN);
            else if (n.unlocked)
                batch.setColor(Color.WHITE);
            else
                batch.setColor(Color.DARK_GRAY);

            n.draw(batch);
        }
        batch.end();

        // Clique do jogador
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();

            for (Nodo n : nodes) {
                if (n.isClicked(x, y) && n.unlocked) {
                    currentNode = n;
                    n.completed = true;

                    for (Nodo c : n.connectedNodes) {
                        c.unlocked = true;
                    }
                    game.setScreen(new TelaBatalha(game, this, inimigosNivel, main));
                    return;
                }
            }
        }
    }

    @Override public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        nodeTexture.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
