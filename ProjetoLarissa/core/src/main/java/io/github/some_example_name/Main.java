package io.github.some_example_name;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.*;
import Flyweight.CartaFactory;
import Flyweight.FactoryCartaAtq;
import Flyweight.FactoryCartaHab;
import Flyweight.FactoryCartaPod;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Stage stage;
    private ArrayList<Carta> descarte = new ArrayList<>();
    private ArrayList<ImageButton> botoesCartas = new ArrayList<>();
    private ArrayList<Carta> deckPlayer = new ArrayList<>();
    private ArrayList<Carta> mãoPlayer = new ArrayList<>();
    private Texture slice, burst, wraith, endTurnTex;
    private ImageButton endTurnBtn;
    private boolean turnoJogador = true;
    Jogador jogador = new Jogador(100, 0, 0, 3);
    Inimigo inimigo = new Inimigo(100);

    void criardeck() {
        batch = new SpriteBatch();
        slice = new Texture(Gdx.files.internal("slice.png"));
        burst = new Texture(Gdx.files.internal("burst.png"));
        wraith = new Texture(Gdx.files.internal("wraith.png"));
        endTurnTex = new Texture(Gdx.files.internal("slice.png"));

        CartaFactory fabAtq = new FactoryCartaAtq(slice, "slice", 0, 3);
        CartaFactory fabHab = new FactoryCartaHab(burst, "burst", 1);
        CartaFactory fabPoder = new FactoryCartaPod("wraith", wraith, 3);

        for (int i = 0; i < 6; i++) deckPlayer.add(fabAtq.criarCarta());
        for (int i = 0; i < 6; i++) deckPlayer.add(fabHab.criarCarta());
        for (int i = 0; i < 2; i++) deckPlayer.add(fabPoder.criarCarta());
    }

    @Override
    public void create() {
        criardeck();
        Collections.shuffle(deckPlayer, new Random());
        mãoPlayer = new ArrayList<>(deckPlayer.subList(0, 6));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        puxarNovasCartas();
        botãoTurno();
    }
    private void botãoTurno(){
        TextureRegionDrawable drawable = new TextureRegionDrawable(endTurnTex);
        endTurnBtn = new ImageButton(drawable);
        endTurnBtn.setSize(120, 50);
        endTurnBtn.setPosition(600, 20);
        endTurnBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (turnoJogador) {
                    passarTurno();
                }
            }
        });
        stage.addActor(endTurnBtn);
    }
    private void puxarNovasCartas() {
        //PRIORIDADE NUMERO 1 CORRIGIR RECICLAGEM DO DESCARTE, CARTAS SE REPETEM
        mãoPlayer.clear();
        for (ImageButton b : botoesCartas) {
            b.remove();
        }
        botoesCartas.clear();

        // Puxa cartas até ter 6 na mão
        while (mãoPlayer.size() < 6) {
            if (deckPlayer.isEmpty()) {
                if (descarte.isEmpty()) break; // não há mais cartas disponíveis
                // recicla o descarte e embaralha
                deckPlayer.addAll(descarte);
                descarte.clear();
                Collections.shuffle(deckPlayer, new Random());
            }
            // pega a primeira carta do deck
            Carta carta = deckPlayer.remove(0);
            mãoPlayer.add(carta);
        }
        for (int i = 0; i < mãoPlayer.size(); i++) {
            final Carta carta = mãoPlayer.get(i);
            TextureRegionDrawable drawable = new TextureRegionDrawable(carta.getImagem());
            ImageButton button = new ImageButton(drawable);
            button.setPosition(350 + i * 100, 20);
            button.setSize(100, 150);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(jogador.mana >= carta.custo) {
                        button.remove();
                        jogador.mana -= carta.custo;
                        mãoPlayer.remove(carta);
                        botoesCartas.remove(button);
                        descarte.add(carta);
                        if(carta instanceof CartaAtq){
                            inimigo.setHPInimigo(
                                    inimigo.getHPInimigo() - ((CartaAtq) carta).dano
                            );
                        }
                    }
                }
            });

            botoesCartas.add(button);
            stage.addActor(button);
        }
    }
    void passarTurno(){
        turnoJogador = true;
        jogador.mana = 3;
        //ACHO QUE CORRIGI O PROBLEMA MAS PRECISO FAZER MAS TESTES
        descarte.addAll(mãoPlayer);
        puxarNovasCartas();
    }

    @Override
    public void render() {
        batch.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        BitmapFont font = new BitmapFont();
        font.draw(batch, "Mana: " + jogador.mana, 50, 200);
        font.draw(batch, "Vida inimigo"+inimigo.getHPInimigo(),500, 500);
        font.draw(batch, turnoJogador ? "Seu Turno" : "Turno do Inimigo", 300, 400);
        stage.draw();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        slice.dispose();
        burst.dispose();
        wraith.dispose();
        stage.dispose();
    }
}
