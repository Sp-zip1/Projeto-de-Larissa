package io.github.some_example_name;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.Carta;
import Entidades.CartaAtq;
import Flyweight.CartaFactory;
import Flyweight.FactoryCartaAtq;
import Flyweight.FactoryCartaHab;
import Flyweight.FactoryCartaPod;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
public class Main extends ApplicationAdapter {
    private float inimigoOffsetX = 0;
    private float inimigoOffsetY = 0;
    private float tremorTimer = 0f;
    private float delta;
    private SpriteBatch batch;
    private Stage stage;
    private ArrayList<Carta> descarte = new ArrayList<>();
    private ArrayList<ImageButton> botoesCartas = new ArrayList<>();
    private ArrayList<Carta> deckPlayer = new ArrayList<>();
    private ArrayList<Carta> mãoPlayer = new ArrayList<>();
    private Texture slice, burst, wraith, endTurnTex, backGround;
    private ImageButton endTurnBtn;
    private boolean turnoJogador = true;
    private Inimigo inimigo;
    private ShapeRenderer barraVidaIn;
    Jogador jogador = new Jogador(100, 0, 0, 3);
    void criarBarraHPIn(BitmapFont font){
        barraVidaIn.begin(ShapeRenderer.ShapeType.Filled);
        barraVidaIn.setColor(1, 0, 0, 1);
        // calcular largura proporcional (vida atual / vida máxima)
        float maxHP = 100f;
        float larguraMax = 200;
        float larguraAtual = (inimigo.getHPInimigo() / maxHP) * larguraMax;
        // posição da barra
        float x = 800;
        float y = 520;
        float altura = 20;
        barraVidaIn.rect(x, y, larguraAtual, altura);
        barraVidaIn.setColor(0.3f, 0.3f, 0.3f, 1);
        barraVidaIn.rect(x + larguraAtual, y, larguraMax - larguraAtual, altura);
        barraVidaIn.end();
        String textoVida = inimigo.getHPInimigo() + " / " + (int)maxHP;
        float textoLargura = font.getRegion().getRegionWidth();
        font.draw(batch, textoVida, x + larguraMax/2f - textoVida.length()*3, y + altura - 5);
    }
    void criardeck() {
        batch = new SpriteBatch();
        slice = new Texture(Gdx.files.internal("slice.png"));
        burst = new Texture(Gdx.files.internal("burst.png"));
        wraith = new Texture(Gdx.files.internal("wraith.png"));
        endTurnTex = new Texture(Gdx.files.internal("slice.png"));

        CartaFactory fabAtq = new FactoryCartaAtq(slice, "slice", 0);
        CartaFactory fabHab = new FactoryCartaHab(burst, "burst", 1);
        CartaFactory fabPoder = new FactoryCartaPod("wraith", wraith, 3);

        for (int i = 0; i < 6; i++) deckPlayer.add(fabAtq.criarCarta());
        for (int i = 0; i < 6; i++) deckPlayer.add(fabHab.criarCarta());
        for (int i = 0; i < 2; i++) deckPlayer.add(fabPoder.criarCarta());
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
                if (descarte.isEmpty()) break;
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
                        carta.executarEfeitos(jogador, inimigo);
                        reposicionarCartas();
                    }
                    if(carta instanceof CartaAtq){
                        tremerInimigo();
                    }
                }
            });

            botoesCartas.add(button);
            stage.addActor(button);
        }
        reposicionarCartas();
    }
    void passarTurno(){
        inimigo.ExecutarAçãoI(jogador);
        turnoJogador = true;
        jogador.mana = 3;
        //ACHO QUE CORRIGI O PROBLEMA MAS PRECISO FAZER MAS TESTES
        descarte.addAll(mãoPlayer);
        puxarNovasCartas();
    }
    public void botãoTurno(){
        TextureRegionDrawable drawable = new TextureRegionDrawable(endTurnTex);
        endTurnBtn = new ImageButton(drawable);
        endTurnBtn.setSize(120, 50);
        endTurnBtn.setPosition(1000, 50);
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
    public void tremerInimigo() {
        tremorTimer = 0.3f; // duração do tremor em segundos
    }
    public void inimigoTremerEfeito(){
        if (tremorTimer > 0) {
            tremorTimer -= delta;
            // DESLOCAMENTO DO INIMIGO DURANTE ATAQUE
            inimigoOffsetX = (float)(Math.random() * 10 - 5); // -5 até +5 px
            inimigoOffsetY = (float)(Math.random() * 10 - 5);
        } else {
            inimigoOffsetX = 0;
            inimigoOffsetY = 0;
        }
    }
    public void reposicionarCartas() {
        float larguraTotal = mãoPlayer.size() * 100;
        float centroTela = Gdx.graphics.getWidth() / 2f;
        float xInicial = centroTela - (larguraTotal / 2f);

        for (int i = 0; i < botoesCartas.size(); i++) {
            ImageButton button = botoesCartas.get(i);
            float x = xInicial + i * 100;
            button.setPosition(x, 20);
        }
    }

    @Override
    public void create() {
        barraVidaIn = new ShapeRenderer();
        Texture inimigoTex = new Texture("BossAparencia.png");
        inimigo = new Inimigo(100, 3, inimigoTex);
        criardeck();
        Collections.shuffle(deckPlayer, new Random());
        mãoPlayer = new ArrayList<>(deckPlayer.subList(0, 6));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        backGround = new Texture("BackGround.png");
        puxarNovasCartas();
        botãoTurno();
    }
    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        delta = Gdx.graphics.getDeltaTime();
        batch.begin();
        batch.draw(backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        inimigoTremerEfeito();
        stage.act(Gdx.graphics.getDeltaTime());
        BitmapFont font = new BitmapFont();
        batch.draw(inimigo.getInimigoImg(), 800+inimigoOffsetX, 200+inimigoOffsetY, 300, 300);
        font.draw(batch, "Vida jogador"+jogador.HPPlayer, 200, 50);
        font.draw(batch, "Mana: " + jogador.mana, 50, 200);
        font.draw(batch, turnoJogador ? "Seu Turno" : "Turno do Inimigo", 300, 400);
        criarBarraHPIn(font);
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
