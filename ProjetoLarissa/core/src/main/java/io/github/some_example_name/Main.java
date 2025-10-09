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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
public class Main extends ApplicationAdapter {
    private float inimigoOffsetX = 0, playerOffsetX =0;
    private float inimigoOffsetY = 0, playeroOffsetY = 0;
    private float tremorTimer = 0f, tremorTimerP = 0f;
    private float delta;
    private SpriteBatch batch;
    private Stage stage;
    private ArrayList<ImageButton> botoesCartas = new ArrayList<>();
    private Texture slice, burst, wraith, endTurnTex, backGround, TextJog;
    private ImageButton endTurnBtn;
    private boolean turnoJogador = true;
    private Inimigo inimigo;
    private ShapeRenderer barraVidaIn, barraVidaP;

    private Music backgroundMusic;
    private boolean musicEnabled = true;
    private ImageButton volumeBtn;
 
    Jogador jogador;
    public Texture inimigoDanTex, playerDanTex;
    public Texture inimigoTex;
    void criarBarraHPJo(BitmapFont font){
        barraVidaP.begin(ShapeRenderer.ShapeType.Filled);
        barraVidaP.setColor(1, 0, 0, 1);

        // calcular largura proporcional (vida atual / vida máxima)
        float maxHPJ = 100f;
        float larguraMaxJ = 200;
        float larguraAtualJ = (jogador.getHPPlayer() / maxHPJ) * larguraMaxJ;

        // posição da barra
        float x = 200;
        float y = 520;
        float altura = 20;
        barraVidaP.rect(x, y, larguraAtualJ, altura);
        barraVidaP.setColor(0.3f, 0.3f, 0.3f, 1);
        barraVidaP.rect(x + larguraAtualJ, y, larguraMaxJ - larguraAtualJ, altura);
        barraVidaP.end();
        String textoVidaP = jogador.getHPPlayer() + " / " + (int)maxHPJ;
        font.draw(batch, textoVidaP, x + larguraMaxJ/2f - textoVidaP.length()*3, y + altura - 5);
    }

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

        CartaFactory fabrCAtk = new FactoryCartas(0, "golpe", slice, TipoC.ATK, Gdx.audio.newSound(Gdx.files.internal("Sons/slap-hurt-pain-sound-effect.mp3")));
        CartaFactory fabrCHab = new FactoryCartas(1, "burst", burst, TipoC.HAB, Gdx.audio.newSound(Gdx.files.internal("Sons/card-woosh.mp3")));
        CartaFactory fabrCPod = new FactoryCartas(3, "wraith", wraith, TipoC.POD, Gdx.audio.newSound(Gdx.files.internal("Sons/card-woosh.mp3")));
        for (int i = 0; i < 6; i++) jogador.deckPlayer.add(fabrCAtk.criarCarta());
        for (int i = 0; i < 6; i++) jogador.deckPlayer.add(fabrCHab.criarCarta());
        for (int i = 0; i < 2; i++) jogador.deckPlayer.add(fabrCPod.criarCarta());
    }
    private void puxarNovasCartas() {
        //PRIORIDADE NUMERO 1 CORRIGIR RECICLAGEM DO DESCARTE, CARTAS SE REPETEM
        jogador.mãoPlayer.clear();
        for (ImageButton b : botoesCartas) {
            b.remove();
        }
        botoesCartas.clear();

        // Puxa cartas até ter 6 na mão
        while (jogador.mãoPlayer.size() < 6) {
            if (jogador.deckPlayer.isEmpty()) {
                if (jogador.descarte.isEmpty()) break;
                jogador.deckPlayer.addAll(jogador.descarte);
                jogador.descarte.clear();
                Collections.shuffle(jogador.deckPlayer, new Random());
            }
            // pega a primeira carta do deck
            Carta carta = jogador.deckPlayer.remove(0);
            jogador.mãoPlayer.add(carta);
        }
        for (int i = 0; i < jogador.mãoPlayer.size(); i++) {
            final Carta carta = jogador.mãoPlayer.get(i);
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
                        jogador.mãoPlayer.remove(carta);
                        botoesCartas.remove(button);
                        jogador.descarte.add(carta);
                        carta.executarEfeitos(jogador, inimigo);
                        carta.somc.play();
                        reposicionarCartas();
                    }
                    if(carta.getTipoC().equals(TipoC.ATK)){
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
        int atual = jogador.getHPPlayer();
        inimigo.ExecutarAçãoI(jogador);
        if(atual > jogador.getHPPlayer()){
            tremerPlayer();
            //FALTA ADICIONAR SOM DE DANO AQUI
        }
        atual = jogador.getHPPlayer();
        turnoJogador = true;
        jogador.mana = 3;
        //ACHO QUE CORRIGI O PROBLEMA MAS PRECISO FAZER MAIS TESTES
        jogador.descarte.addAll(jogador.mãoPlayer);
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
    public void tremerPlayer(){
        tremorTimerP = 0.3f;
    }
    public void inimigoTremerEfeito(){
        if (tremorTimer > 0) {
            inimigo.setInimigoImg(inimigoDanTex);
            tremorTimer -= delta;
            // DESLOCAMENTO DO INIMIGO DURANTE ATAQUE
            inimigoOffsetX = (float)(Math.random() * 10 - 5); // -5 até +5 px
            inimigoOffsetY = (float)(Math.random() * 10 - 5);
        } else {
            inimigoOffsetX = 0;
            inimigoOffsetY = 0;
            inimigo.setInimigoImg(inimigoTex);
        }
    }public void PlayerTremerEfeito(){
        if (tremorTimerP > 0) {
            jogador.setImgPlayer(playerDanTex);
            tremorTimerP -= delta;
            // DESLOCAMENTO DO INIMIGO DURANTE ATAQUE
            playerOffsetX = (float)(Math.random() * 10 - 5); // -5 até +5 px
            playeroOffsetY = (float)(Math.random() * 10 - 5);
        } else {
            playerOffsetX = 0;
            playeroOffsetY = 0;
            jogador.setImgPlayer(TextJog);
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
        barraVidaIn = new ShapeRenderer();
        barraVidaP = new ShapeRenderer();
        inimigoTex = new Texture("tangled-wires.png");
        inimigoDanTex = new Texture("tangled-wires-hit.png");
        TextJog = new Texture(Gdx.files.internal("Player.png"));
        playerDanTex = new Texture("player-hit.png");
        jogador = new Jogador(100, 0, 0, 3, TextJog);
        inimigo = new Inimigo(100, 3, inimigoTex, 100);
        criardeck();
        Collections.shuffle(jogador.deckPlayer, new Random());
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        backGround = new Texture("Background.png");
        puxarNovasCartas();
        botãoTurno();

            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sons/analog-texture.mp3"));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.4f);
            backgroundMusic.play();
                criarBotaoVolumeSimples();
    }
    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        delta = Gdx.graphics.getDeltaTime();
        batch.begin();
        batch.draw(backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        inimigoTremerEfeito();
        PlayerTremerEfeito();
        stage.act(Gdx.graphics.getDeltaTime());
        BitmapFont font = new BitmapFont();
        batch.draw(jogador.getImgPlayer(),200+playerOffsetX, 200+playeroOffsetY, 300, 300);
        batch.draw(inimigo.getInimigoImg(), 800+inimigoOffsetX, 200+inimigoOffsetY, 300, 300);
        //font.draw(batch, "Vida jogador"+jogador.HPPlayer, 200, 50);
        font.draw(batch, "Mana: " + jogador.mana, 50, 200);
        //font.draw(batch, turnoJogador ? "Seu Turno" : "Turno do Inimigo", 300, 400);
        criarBarraHPIn(font);
        criarBarraHPJo(font);
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

        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }

    }

    private void criarBotaoVolumeSimples() {
        // Carrega as texturas - CERTIFIQUE-SE que estes arquivos existem!
        Texture volumeLigadoTex = new Texture(Gdx.files.internal("volume_on.png"));
        Texture volumeDesligadoTex = new Texture(Gdx.files.internal("volume_mute.png"));
        
        TextureRegionDrawable drawableLigado = new TextureRegionDrawable(volumeLigadoTex);
        TextureRegionDrawable drawableDesligado = new TextureRegionDrawable(volumeDesligadoTex);
        
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = drawableLigado;
        
        ImageButton volumeBtn = new ImageButton(style);
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
