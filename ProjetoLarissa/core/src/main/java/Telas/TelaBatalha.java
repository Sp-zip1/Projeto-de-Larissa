package Telas;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.Carta;
import Entidades.TipoC;
import Flyweight.CartaFactory;
import Flyweight.FactoryCartas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TelaBatalha implements Screen {

    // === VARIÁVEIS PRINCIPAIS ===
    private SpriteBatch batch;
    private Stage stage;
    private ShapeRenderer barraVidaIn, barraVidaP;
    private BitmapFont font;

    private CartaFactory fabrCAtk, fabrCHab, fabrCPod;
    private ArrayList<ImageButton> botoesCartas = new ArrayList<>();
    private ArrayList<ImageButton> botoesRecompensa = new ArrayList<>();

    private Texture slice, burst, wraith, endTurnTex, backGround, TextJog;
    private Texture inimigoTex, inimigoDanTex, playerDanTex;
    private ImageButton endTurnBtn, volumeBtn;

    private Inimigo inimigo;
    private Jogador jogador;

    private boolean turnoJogador = true, mostrandoRecompensa;
    private boolean musicEnabled = true;
    private Music backgroundMusic;

    private float inimigoOffsetX = 0, playerOffsetX = 0;
    private float inimigoOffsetY = 0, playerOffsetY = 0;
    private float tremorTimer = 0f, tremorTimerP = 0f;

    // === CONSTRUTOR ===
    public TelaBatalha() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        barraVidaIn = new ShapeRenderer();
        barraVidaP = new ShapeRenderer();
        font = new BitmapFont();

        jogador = new Jogador(100, 0, 0, 3,TextJog);
        inimigo = new Inimigo(100, 3, new Texture("tangled-wires.png"), 100);

        carregarTexturasESons();
        fabricaCarta();
        criardeck();
        puxarNovasCartas();
        botãoTurno();

        backgroundMusic.setLooping(true);
        if (musicEnabled) backgroundMusic.play();
    }

    // === CICLO DE VIDA ===
    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        this.atualizarTremores(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Desenha jogador e inimigo
        batch.draw(jogador.getImgPlayer(),
                200 + playerOffsetX, 200 + playerOffsetY, 200, 200);
        batch.draw(inimigo.getInimigoImg(),
                800 + inimigoOffsetX, 200 + inimigoOffsetY, 200, 200);

        batch.end();
        criarBarraHPJo();
        criarBarraHPIn();
        botãoTurno();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        barraVidaIn.dispose();
        barraVidaP.dispose();
        backgroundMusic.dispose();
    }

    // === MÉTODOS DE SUPORTE ===

    private void carregarTexturasESons() {
        slice = new Texture(Gdx.files.internal("slice.png"));
        burst = new Texture(Gdx.files.internal("burst.png"));
        wraith = new Texture(Gdx.files.internal("wraith.png"));
        endTurnTex = new Texture(Gdx.files.internal("slice.png"));
        backGround = new Texture(Gdx.files.internal("Background.png"));
        TextJog = new Texture(Gdx.files.internal("Player.png"));
        playerDanTex = new Texture(Gdx.files.internal("player-hit.png"));
        inimigoTex = new Texture(Gdx.files.internal("tangled-wires.png"));
        inimigoDanTex = new Texture(Gdx.files.internal("tangled-wires-hit.png"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sons/analog-texture.mp3"));
    }

    private void fabricaCarta() {
        fabrCAtk = new FactoryCartas(0, "golpe", slice, TipoC.ATK,
                Gdx.audio.newSound(Gdx.files.internal("Sons/slap-hurt-pain-sound-effect.mp3")));
        fabrCHab = new FactoryCartas(1, "burst", burst, TipoC.HAB,
                Gdx.audio.newSound(Gdx.files.internal("Sons/card-woosh.mp3")));
        fabrCPod = new FactoryCartas(2, "wraith", wraith, TipoC.POD,
                Gdx.audio.newSound(Gdx.files.internal("Sons/card-woosh.mp3")));
    }

    private void criardeck() {
        for (int i = 0; i < 6; i++) jogador.deckPlayer.add(fabrCAtk.criarCarta());
        for (int i = 0; i < 6; i++) jogador.deckPlayer.add(fabrCHab.criarCarta());
        for (int i = 0; i < 2; i++) jogador.deckPlayer.add(fabrCPod.criarCarta());
        Collections.shuffle(jogador.deckPlayer, new Random());
    }

    private void criarBarraHPJo() {
        float maxHPJ = 100f;
        float larguraMaxJ = 200;
        float larguraAtualJ = (jogador.getHPPlayer() / maxHPJ) * larguraMaxJ;
        float x = 200, y = 520, altura = 20;
        barraVidaP.begin(ShapeRenderer.ShapeType.Filled);
        barraVidaP.setColor(1, 0, 0, 1);
        barraVidaP.rect(x, y, larguraAtualJ, altura);
        barraVidaP.setColor(0.3f, 0.3f, 0.3f, 1);
        barraVidaP.rect(x + larguraAtualJ, y, larguraMaxJ - larguraAtualJ, altura);
        barraVidaP.end();
    }

    private void criarBarraHPIn() {
        float maxHP = 100f;
        float larguraMax = 200;
        float larguraAtual = (inimigo.getHPInimigo() / maxHP) * larguraMax;
        float x = 800, y = 520, altura = 20;
        barraVidaIn.begin(ShapeRenderer.ShapeType.Filled);
        barraVidaIn.setColor(1, 0, 0, 1);
        barraVidaIn.rect(x, y, larguraAtual, altura);
        barraVidaIn.setColor(0.3f, 0.3f, 0.3f, 1);
        barraVidaIn.rect(x + larguraAtual, y, larguraMax - larguraAtual, altura);
        barraVidaIn.end();
    }

    private void puxarNovasCartas() {
        jogador.mãoPlayer.clear();
        for (ImageButton b : botoesCartas) b.remove();
        botoesCartas.clear();

        ArrayList<Carta> novas = jogador.puxarCartasDoDeck(6);
        Collections.shuffle(novas);
        jogador.mãoPlayer.addAll(novas);

        for (int i = 0; i < jogador.mãoPlayer.size(); i++) {
            final Carta carta = jogador.mãoPlayer.get(i);
            TextureRegionDrawable drawable = new TextureRegionDrawable(carta.getImagem());
            ImageButton button = new ImageButton(drawable);
            button.setSize(100, 150);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (jogador.mana >= carta.custo) {
                        button.remove();
                        jogador.mana -= carta.custo;
                        jogador.mãoPlayer.remove(carta);
                        botoesCartas.remove(button);
                        jogador.descarte.add(carta);
                        carta.executarEfeitos(jogador, inimigo);
                        carta.somc.play();
                        reposicionarCartas();
                        if (carta.getTipoC().equals(TipoC.ATK)) tremer(true);
                    }
                }
            });
            botoesCartas.add(button);
            stage.addActor(button);
        }
        reposicionarCartas();
    }

    private void reposicionarCartas() {
        float larguraTotal = jogador.mãoPlayer.size() * 100;
        float centroTela = Gdx.graphics.getWidth() / 2f;
        float xInicial = centroTela - (larguraTotal / 2f);
        for (int i = 0; i < botoesCartas.size(); i++) {
            ImageButton button = botoesCartas.get(i);
            float x = xInicial + i * 100;
            button.setPosition(x, 20);
        }
    }

    private void botãoTurno() {
        TextureRegionDrawable drawable = new TextureRegionDrawable(endTurnTex);
        endTurnBtn = new ImageButton(drawable);
        endTurnBtn.setSize(120, 50);
        endTurnBtn.setPosition(1000, 50);
        endTurnBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (turnoJogador) passarTurno();
            }
        });
        stage.addActor(endTurnBtn);
    }

    private void passarTurno() {
        int atual = jogador.getHPPlayer();
        inimigo.ExecutarAçãoI(jogador);
        if (atual > jogador.getHPPlayer()) tremer(false);
        if (inimigo.getHPInimigo() <= 0) {
            entrarEstadoRecompensa();
        } else {
            turnoJogador = true;
            jogador.mana = 3;
            jogador.descarte.addAll(jogador.mãoPlayer);
            puxarNovasCartas();
        }
    }

    private void entrarEstadoRecompensa() {
        if (mostrandoRecompensa) return;
        mostrandoRecompensa = true;
        ocultarElementosDoJogo();
        criarBotoesRecompensa();
    }

    private void criarBotoesRecompensa() {
        if (!botoesRecompensa.isEmpty()) return;
        ArrayList<Carta> opcoes = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int tipo = random.nextInt(3);
            if (tipo == 0) opcoes.add(fabrCAtk.criarCarta());
            else if (tipo == 1) opcoes.add(fabrCHab.criarCarta());
            else opcoes.add(fabrCPod.criarCarta());
        }

        for (int i = 0; i < opcoes.size(); i++) {
            final Carta carta = opcoes.get(i);
            TextureRegionDrawable drawable = new TextureRegionDrawable(carta.getImagem());
            ImageButton botaoCarta = new ImageButton(drawable);
            botaoCarta.setSize(150, 200);
            botaoCarta.setPosition(400 + i * 200, 400);
            botaoCarta.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    jogador.deckPlayer.add(carta);
                    for (ImageButton b : botoesRecompensa) b.remove();
                    botoesRecompensa.clear();
                    mostrandoRecompensa = false;
                    inimigo = new Inimigo(100, 3, inimigoTex, 100);
                    puxarNovasCartas();
                }
            });
            stage.addActor(botaoCarta);
            botoesRecompensa.add(botaoCarta);
        }
    }

    private void ocultarElementosDoJogo() {
        for (ImageButton b : botoesCartas) b.remove();
        botoesCartas.clear();
        if (endTurnBtn != null) endTurnBtn.setVisible(false);
        if (volumeBtn != null) volumeBtn.setVisible(false);
    }

    private void atualizarTremores(float delta) {
        aplicarTremor(true, delta, inimigoTex, inimigoDanTex);
        aplicarTremor(false, delta, TextJog, playerDanTex);
    }

    private void tremer(boolean isInimigo) {
        if (isInimigo) tremorTimer = 0.3f;
        else tremorTimerP = 0.3f;
    }

    private void aplicarTremor(boolean isInimigo, float delta, Texture normalTex, Texture hitTex) {
        if (isInimigo) {
            if (tremorTimer > 0) {
                inimigo.setInimigoImg(hitTex);
                tremorTimer -= delta;
                inimigoOffsetX = (float)(Math.random() * 10 - 5);
                inimigoOffsetY = (float)(Math.random() * 10 - 5);
            } else {
                inimigoOffsetX = 0;
                inimigoOffsetY = 0;
                inimigo.setInimigoImg(normalTex);
            }
        } else {
            if (tremorTimerP > 0) {
                jogador.setImgPlayer(hitTex);
                tremorTimerP -= delta;
                playerOffsetX = (float)(Math.random() * 10 - 5);
                playerOffsetY = (float)(Math.random() * 10 - 5);
            } else {
                playerOffsetX = 0;
                playerOffsetY = 0;
                jogador.setImgPlayer(normalTex);
            }
        }
    }
}
