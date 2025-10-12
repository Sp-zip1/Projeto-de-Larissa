package Telas;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.Carta;
import Entidades.TipoC;
import Flyweight.CartaFactory;
import Flyweight.FactoryCartas;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import io.github.some_example_name.Main;

public class TelaBatalha implements Screen {

    // === VARIÁVEIS PRINCIPAIS ===
    private SpriteBatch batch;
    private Stage stage;
    private ShapeRenderer barraVidaIn, barraVidaP;
    private BitmapFont font;

    private CartaFactory fabrCAtk, fabrCHab, fabrCPod;
    private ArrayList<ImageButton> botoesCartas = new ArrayList<>();
    private ArrayList<ImageButton> botoesRecompensa = new ArrayList<>();
    private ImageButton endTurnBtn, volumeBtn;

    private Inimigo inimigo;
    private Jogador jogador;

    private boolean turnoJogador = true, mostrandoRecompensa;
    private boolean musicEnabled = true;

    private float playerOffsetY = 0, playerOffsetX = 0;
    private float tremorTimer;
    private  float tremorTimerP = 0f;
    private Game game;
    private final TelaMapa telaMapa;
    private ArrayList<Inimigo> inimigos;
    private  Inimigo inimigoSorteado;
    private DragAndDrop dragAndDrop;
    private Main main;
    // === CONSTRUTOR ===
    public TelaBatalha(Game game, TelaMapa telaMapa, ArrayList<Inimigo> inimigos, Main main) {
        this.game = game;
        this.main = main;
        this.telaMapa = telaMapa;
        this.inimigos = inimigos;
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        barraVidaIn = new ShapeRenderer();
        barraVidaP = new ShapeRenderer();
        font = new BitmapFont();
        Random random = new Random();
        int sorteado = random.nextInt(inimigos.size());
        inimigoSorteado = inimigos.get(sorteado);
        jogador = new Jogador(100, 0, 0, 3,main.TextJog);
        inimigo = inimigoSorteado;
        fabricaCarta();
        criardeck();
        puxarNovasCartas();
        botãoTurno();

        main.backgroundMusic.setLooping(true);
        if (musicEnabled) main.backgroundMusic.play();
    }

    // === CICLO DE VIDA ===
    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        this.atualizarTremores(delta, inimigoSorteado);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(main.backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Desenha jogador e inimigo
        batch.draw(jogador.getImgPlayer(), 200 + playerOffsetX, 200 + playerOffsetY, 200, 200);
        batch.draw(inimigo.getInimigoImg(), 800 + inimigo.getOffsetX(), 200 + inimigo.getOffsetY(), 200, 200);

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
        if (main.backgroundMusic != null) {
            main.backgroundMusic.stop();
            main.backgroundMusic.dispose();
        }
    }

    // === MÉTODOS DE SUPORTE ===

    private void fabricaCarta() {
        fabrCAtk = new FactoryCartas(0, "golpe", main.slice, TipoC.ATK,
                Gdx.audio.newSound(Gdx.files.internal("Sons/slap-hurt-pain-sound-effect.mp3")));
        fabrCHab = new FactoryCartas(1, "burst", main.burst, TipoC.HAB,
                Gdx.audio.newSound(Gdx.files.internal("Sons/card-woosh.mp3")));
        fabrCPod = new FactoryCartas(2, "wraith", main.wraith, TipoC.POD,
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
        dragAndDrop = new DragAndDrop();
        for (int i = 0; i < jogador.mãoPlayer.size(); i++) {
            final Carta carta = jogador.mãoPlayer.get(i);
            TextureRegionDrawable drawable = new TextureRegionDrawable(carta.getImagem());
            ImageButton button = new ImageButton(drawable);
            button.setSize(100, 150);
            dragAndDrop.addSource(new Source(button) {
                @Override
                public Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    if (jogador.mana < carta.custo) return null; // sem mana
                    Payload payload = new Payload();
                    payload.setObject(carta);
                    ImageButton dragVisual = new ImageButton(new TextureRegionDrawable(carta.getImagem()));
                    dragVisual.setSize(100, 150);
                    payload.setDragActor(dragVisual);
                    return payload;
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
                    if (target == null) {
                        // solto em lugar inválido
                        return;
                    }

                    // Executa ataque
                    Carta cartaSolta = (Carta) payload.getObject();
                    jogador.mana -= cartaSolta.custo;
                    jogador.mãoPlayer.remove(cartaSolta);
                    botoesCartas.remove(button);
                    jogador.descarte.add(cartaSolta);
                    cartaSolta.executarEfeitos(jogador, inimigo);
                    cartaSolta.somc.play();
                    button.remove();
                    reposicionarCartas();

                    if (cartaSolta.getTipoC() == TipoC.ATK) tremer(true);
                }
            });

            botoesCartas.add(button);
            stage.addActor(button);
        }

        // Define o inimigo como alvo de drop
        Actor inimigoAlvo = new Actor();
        inimigoAlvo.setBounds(700, 150, 300, 300); // posição e tamanho do inimigo
        stage.addActor(inimigoAlvo);

//Cria um ator invisível no centro da tela (para HAB e POD)
        Actor centroAlvo = new Actor();
        float centroX = Gdx.graphics.getWidth() / 2f - 100;
        float centroY = Gdx.graphics.getHeight() / 2f - 100;
        centroAlvo.setBounds(centroX - 150, centroY-100, 400, 400);
        stage.addActor(centroAlvo);

          //Define o inimigo como alvo de drop
        dragAndDrop.addTarget(new Target(inimigoAlvo) {
            @Override
            public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
                Carta carta = (Carta) payload.getObject();
                return carta.getTipoC() == TipoC.ATK;
            }

            @Override
            public void drop(Source source, Payload payload, float x, float y, int pointer) {
            }
        });

       //Define o centro da tela como alvo de drop
        dragAndDrop.addTarget(new Target(centroAlvo) {
            @Override
            public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
                Carta carta = (Carta) payload.getObject();
                return carta.getTipoC() == TipoC.HAB || carta.getTipoC() == TipoC.POD;
            }

            @Override
            public void drop(Source source, Payload payload, float x, float y, int pointer) {
            }
        });
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
        TextureRegionDrawable drawable = new TextureRegionDrawable(main.endTurnTex);
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
                    //inimigo = new Inimigo(100, 3, inimigoTex, 100);
                    //puxarNovasCartas();
                    game.setScreen(telaMapa);
                    dispose();
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

    private void atualizarTremores(float delta, Inimigo inimigo) {
        aplicarTremor(true, delta, inimigo.getInimigoImg(), inimigo.getInimigoDan(), inimigo.getInimigoIdle());
        aplicarTremor(false, delta, main.TextJog, main.playerDanTex, main.TextJog);
    }

    private void tremer(boolean isInimigo) {
        if (isInimigo) tremorTimer = 0.3f;
        else tremorTimerP = 0.3f;
    }

    private void aplicarTremor(boolean isInimigo, float delta, Texture normalTex, Texture hitTex, Texture idleTex) {
        if (isInimigo) {
            if (tremorTimer > 0) {
                inimigo.setInimigoImg(hitTex);
                tremorTimer -= delta;
                inimigo.setOffsetX((float) (Math.random() * 10 - 5));
                inimigo.setOffsetY((float) (Math.random() * 10 - 5));
            } else {
                inimigo.setOffsetX((float) 0);
                inimigo.setOffsetY((float) 0);
                inimigo.setInimigoImg(idleTex);
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
