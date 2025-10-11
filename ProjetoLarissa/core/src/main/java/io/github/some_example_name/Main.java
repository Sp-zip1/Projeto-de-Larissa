package io.github.some_example_name;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.Carta;
import Entidades.TipoC;
import Flyweight.CartaFactory;
import Flyweight.FactoryCartas;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
//PRIODIADES:
//MUDANÇA DE FASE
//NOVAS CARTAS
//ORGANIZAR ESSE CODIGO MELHOR(CODIGO DA RECOMPENSA ESTA MUITO CONFUSO EU PRECISO MODULARIZAR MAIS OU FAZER DE OUTRA MANEIRA
public class Main extends ApplicationAdapter {
    private float inimigoOffsetX = 0, playerOffsetX =0;
    private float inimigoOffsetY = 0, playeroOffsetY = 0;
    private float tremorTimer = 0f, tremorTimerP = 0f;
    private float delta;
    private SpriteBatch batch;
    private Stage stage;
    private CartaFactory fabrCAtk;
    private CartaFactory fabrCHab;
    private CartaFactory fabrCPod;
    private ArrayList<ImageButton> botoesCartas = new ArrayList<>();
    private Texture slice, burst, wraith, endTurnTex, backGround, TextJog;
    private ImageButton endTurnBtn;
    private boolean turnoJogador = true, mostrandoRecompensa;
    private Inimigo inimigo;
    private ShapeRenderer barraVidaIn, barraVidaP;
    private Music backgroundMusic;
    private boolean musicEnabled = true;
    private ImageButton volumeBtn;
    Jogador jogador;
    public Texture inimigoDanTex, playerDanTex;
    public Texture inimigoTex;
    private ArrayList<ImageButton> botoesRecompensa = new ArrayList<>();
    private BitmapFont font;
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

    public void fabricaCarta(){
        fabrCAtk = new FactoryCartas(0, "golpe", slice, TipoC.ATK, Gdx.audio.newSound(Gdx.files.internal("Sons/slap-hurt-pain-sound-effect.mp3")));
        fabrCHab = new FactoryCartas(1, "burst", burst, TipoC.HAB, Gdx.audio.newSound(Gdx.files.internal("Sons/card-woosh.mp3")));
        fabrCPod = new FactoryCartas(3, "wraith", wraith, TipoC.POD, Gdx.audio.newSound(Gdx.files.internal("Sons/card-woosh.mp3")));
    }

    void criarBarraHPJo(){
        float maxHPJ = 100f;
        float larguraMaxJ = 200;
        float larguraAtualJ = (jogador.getHPPlayer() / maxHPJ) * larguraMaxJ;

        float x = 200;
        float y = 520;
        float altura = 20;
        barraVidaP.begin(ShapeRenderer.ShapeType.Filled);
        barraVidaP.setColor(1, 0, 0, 1);
        barraVidaP.rect(x, y, larguraAtualJ, altura);
        barraVidaP.setColor(0.3f, 0.3f, 0.3f, 1);
        barraVidaP.rect(x + larguraAtualJ, y, larguraMaxJ - larguraAtualJ, altura);
        barraVidaP.end();
    }

    void criarBarraHPIn(){
        float maxHP = 100f;
        float larguraMax = 200;
        float larguraAtual = (inimigo.getHPInimigo() / maxHP) * larguraMax;
        float x = 800;
        float y = 520;
        float altura = 20;
        barraVidaIn.begin(ShapeRenderer.ShapeType.Filled);
        barraVidaIn.setColor(1, 0, 0, 1);
        barraVidaIn.rect(x, y, larguraAtual, altura);
        barraVidaIn.setColor(0.3f, 0.3f, 0.3f, 1);
        barraVidaIn.rect(x + larguraAtual, y, larguraMax - larguraAtual, altura);
        barraVidaIn.end();
    }

    void criardeck() {
        batch = new SpriteBatch();
        slice = new Texture(Gdx.files.internal("slice.png"));
        burst = new Texture(Gdx.files.internal("burst.png"));
        wraith = new Texture(Gdx.files.internal("wraith.png"));
        endTurnTex = new Texture(Gdx.files.internal("slice.png"));
        for (int i = 0; i < 6; i++) jogador.deckPlayer.add(fabrCAtk.criarCarta());
        for (int i = 0; i < 6; i++) jogador.deckPlayer.add(fabrCHab.criarCarta());
        for (int i = 0; i < 2; i++) jogador.deckPlayer.add(fabrCPod.criarCarta());
    }

    private void puxarNovasCartas() {
        jogador.mãoPlayer.clear();
        for (ImageButton b : botoesCartas) {
            b.remove();
        }
        botoesCartas.clear();

        // Garante que os botões fixos estejam visíveis ao retornar do estado de recompensa
        if (endTurnBtn != null) endTurnBtn.setVisible(true);
        if (volumeBtn != null) volumeBtn.setVisible(true);

        ArrayList<Carta> novas = jogador.puxarCartasDoDeck(6);
        jogador.mãoPlayer.addAll(novas);
        for (int i = 0; i < jogador.mãoPlayer.size(); i++) {
            final Carta carta = jogador.mãoPlayer.get(i);
            TextureRegionDrawable drawable = new TextureRegionDrawable(carta.getImagem());
            ImageButton button = new ImageButton(drawable);
            button.setSize(100, 150);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(jogador.mana >= carta.custo) {
                        button.remove();
                        jogador.mana -= carta.custo;
                        jogador.mãoPlayer.remove(carta);
                        botoesCartas.remove(button);
                        jogador.descarte.add(carta);
                        carta.executarEfeitos(jogador, inimigo);
                        carta.somc.play();
                        reposicionarCartas();
                    }
                    if(carta.getTipoC().equals(TipoC.ATK)){
                        tremer(true);
                    }
                }
            });

            botoesCartas.add(button);
            stage.addActor(button);
        }
        reposicionarCartas();
    }

    public void ocultarElementosDoJogo() {
        // Remove os botões da mão do jogador
        for (ImageButton b : botoesCartas) {
            b.remove();
        }
        botoesCartas.clear();

        // Oculta os botões fixos
        if (endTurnBtn != null) endTurnBtn.setVisible(false);
        if (volumeBtn != null) volumeBtn.setVisible(false);
    }

    public void entrarEstadoRecompensa() {
        if (mostrandoRecompensa) return;

        mostrandoRecompensa = true;
        ocultarElementosDoJogo();
        criarBotoesRecompensa();
    }

    public void criarBotoesRecompensa() {
        // Se já houver botões de recompensa, não recrie
        if (!botoesRecompensa.isEmpty()) return;

        ArrayList<Carta> opcoes = new ArrayList<>();
        Random random = new Random();
        // Escolhe aleatoriamente 3 cartas
        for (int i = 0; i < 3; i++) {
            int tipo = random.nextInt(3);
            if (tipo == 0) opcoes.add(fabrCAtk.criarCarta());
            else if (tipo == 1) opcoes.add(fabrCHab.criarCarta());
            else opcoes.add(fabrCPod.criarCarta());
        }

        // Cria os botões de recompensa e adiciona ao stage
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
                    System.out.println("Carta adicionada ao deck: " + carta.getNome());

                    // Remove os botões de recompensa
                    for (ImageButton b : botoesRecompensa) {
                        b.remove();
                    }
                    botoesRecompensa.clear();

                    // Retorna ao jogo
                    mostrandoRecompensa = false;
                    inimigo = new Inimigo(100, 3, inimigoTex, 100);
                    puxarNovasCartas();
                }
            });

            stage.addActor(botaoCarta);
            botoesRecompensa.add(botaoCarta);
        }
    }

    void passarTurno(){
        int atual = jogador.getHPPlayer();
        inimigo.ExecutarAçãoI(jogador);
        if(atual > jogador.getHPPlayer()){
            tremer(false);
            //FALTA ADICIONAR SOM DE DANO AQUI
        }

        if(inimigo.getHPInimigo() <= 0){
            entrarEstadoRecompensa();
        } else {
            turnoJogador = true;
            jogador.mana = 3;
            jogador.descarte.addAll(jogador.mãoPlayer);
            puxarNovasCartas();
        }
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

    public void tremer(boolean isInimigo) {
        if (isInimigo) {
            tremorTimer = 0.3f;
        } else {
            tremorTimerP = 0.3f;
        }
    }

    public void aplicarTremor(boolean isInimigo, float delta, Texture normalTex, Texture hitTex) {
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
                playeroOffsetY = (float)(Math.random() * 10 - 5);
            } else {
                playerOffsetX = 0;
                playeroOffsetY = 0;
                jogador.setImgPlayer(normalTex);
            }
        }
    }

    public void reposicionarCartas() {
        float larguraTotal = jogador.mãoPlayer.size() * 100;
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
        font = new BitmapFont();
        carregarTexturasESons();
        fabricaCarta();
        barraVidaIn = new ShapeRenderer();
        barraVidaP = new ShapeRenderer();
        jogador = new Jogador(100, 0, 0, 3, TextJog);
        inimigo = new Inimigo(100, 3, inimigoTex, 100);
        criardeck();
        Collections.shuffle(jogador.deckPlayer, new Random());
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        botãoTurno();
        criarBotaoVolumeSimples();
        puxarNovasCartas();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sons/analog-texture.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.4f);
        backgroundMusic.play();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        delta = Gdx.graphics.getDeltaTime();
        // LÓGICA DE DESENHO PRINCIPAL DO JOGO (SOMENTE SE NÃO ESTIVER EM RECOMPENSA)
        if (!mostrandoRecompensa) {
            batch.begin();
            batch.draw(backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            aplicarTremor(true, delta, inimigoTex, inimigoDanTex);
            aplicarTremor(false, delta, TextJog, playerDanTex);

            BitmapFont font1 = font;

            batch.draw(jogador.getImgPlayer(),200+playerOffsetX, 200+playeroOffsetY, 300, 300);
            batch.draw(inimigo.getInimigoImg(), 800+inimigoOffsetX, 200+inimigoOffsetY, 300, 300);

            font1.draw(batch, "Mana: " + jogador.mana, 50, 200);
            batch.end();
            criarBarraHPIn();
            criarBarraHPJo();
            batch.begin();
            float xJ = 200;
            float yJ = 520;
            float larguraMaxJ = 200;
            float altura = 20;
            String textoVidaP = jogador.getHPPlayer() + " / 100";
            font1.draw(batch, textoVidaP, xJ + larguraMaxJ/2f - textoVidaP.length()*3, yJ + altura - 5);
            // Texto do HP do Inimigo
            float xI = 800;
            float yI = 520;
            float larguraMaxI = 200;
            String textoVidaI = inimigo.getHPInimigo() + " / 100";
            font1.draw(batch, textoVidaI, xI + larguraMaxI/2f - textoVidaI.length()*3, yI + altura - 5);

            batch.end();
        } else {
            //Desenha uma sobreposição escura e semi-transparente
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            barraVidaP.begin(ShapeRenderer.ShapeType.Filled);
            barraVidaP.setColor(new Color(0f, 0f, 0f, 0.7f)); // Preto com 70% de opacidade
            barraVidaP.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            barraVidaP.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        slice.dispose();
        burst.dispose();
        wraith.dispose();
        stage.dispose();
        barraVidaIn.dispose();
        barraVidaP.dispose();
        font.dispose();
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
    }

    private void criarBotaoVolumeSimples() {
        Texture volumeLigadoTex = new Texture(Gdx.files.internal("volume_on.png"));
        Texture volumeDesligadoTex = new Texture(Gdx.files.internal("volume_mute.png"));

        TextureRegionDrawable drawableLigado = new TextureRegionDrawable(volumeLigadoTex);
        TextureRegionDrawable drawableDesligado = new TextureRegionDrawable(volumeDesligadoTex);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = drawableLigado;

        volumeBtn = new ImageButton(style);
        volumeBtn.setSize(40, 40);
        volumeBtn.setPosition(1350, 830);

        volumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                musicEnabled = !musicEnabled;

                if (backgroundMusic != null) {
                    if (musicEnabled) {
                        backgroundMusic.play();
                        volumeBtn.getStyle().imageUp = drawableLigado;
                    } else {
                        backgroundMusic.pause();
                        volumeBtn.getStyle().imageUp = drawableDesligado;
                    }
                }
            }
        });

        stage.addActor(volumeBtn);
    }

}