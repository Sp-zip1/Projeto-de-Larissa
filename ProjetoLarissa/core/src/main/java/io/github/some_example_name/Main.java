package io.github.some_example_name;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.Carta;
import Entidades.TipoC;
import Flyweight.CartaFactory;
import Flyweight.FactoryCartas;
import Telas.TelaMapa;
import Telas.TelaMenu;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

import java.util.*;

//PRIODIADES:
//MUDANÇA DE FASE
//NOVAS CARTAS
//ORGANIZAR ESSE CODIGO MELHOR(CODIGO DA RECOMPENSA ESTA MUITO CONFUSO EU PRECISO MODULARIZAR MAIS OU FAZER DE OUTRA MANEIRA
public class Main extends Game {
    public TelaMapa telaMapa;
    public ArrayList<Inimigo> todosInimigos = new ArrayList<>();
    public SpriteBatch batch;
    public Texture iconduplication, attackicon;
    public Texture slice, burst, wraith, code_injection, garbage_colector, root_acess, nullpointerslash, overclock, safemode, adaptiveai, erro404;
    public Texture inimigoBossHit, inimigoBoss;
    public Texture endTurnTex, backGround, soundA, soundD;
    public Texture TextJog, playerDanTex, playerAtkTex, playerMorteText;
    public Music backgroundMusic;
    public Jogador jogador;
    public Sound somHitJ;
    public TelaMenu telaMenu;
    public Texture brancofundo;
    public BitmapFont fontGrande, fontPequena;
    public Map<String, FactoryCartas> fabricasCartas;
    public Texture inimigo,inimigoHit, inimigo1, inimigo1Hit, inimigo2, inimigo2Hit;
    public static Main instance;
    //OQ ESTAVA NO MAIN AGORA VAI PARA TELA BATALHA
    public static Main getInstance() {
        return instance;
    }
    @Override
    public void create() {
        instance = this;
        batch = new SpriteBatch();
        carregarTexturasESons();
        System.out.println("Main.instance: " + (instance != null ? "OK" : "NULL"));
        System.out.println("iconduplication: " + (iconduplication != null ? "OK" : "NULL"));
        jogador = new Jogador(100, 0, 0, 3,TextJog, somHitJ);
        fabricaCarta();
        criardeck();
        telaMapa = new TelaMapa(this, this);
        telaMenu = new TelaMenu(this, this);
        //setScreen(new TelaMenu(this, this));
        setScreen(telaMenu);
    }
    @Override
    public void render(){
        super.render();
    }
    @Override
    public void dispose(){
    }
    private void carregarTexturasESons() {
        slice = new Texture(Gdx.files.internal("slice.png"));
        burst = new Texture(Gdx.files.internal("burst.png"));
        wraith = new Texture(Gdx.files.internal("wraith.png"));
        endTurnTex = new Texture(Gdx.files.internal("slice.png"));
        backGround = new Texture(Gdx.files.internal("Background.png"));
        TextJog = new Texture(Gdx.files.internal("Player.png"));
        playerDanTex = new Texture(Gdx.files.internal("player-hit.png"));
        inimigoBoss = new Texture(Gdx.files.internal("tangled-wires.png"));
        inimigoBossHit = new Texture(Gdx.files.internal("tangled-wires-hit.png"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sons/analog-texture.mp3"));
        inimigo = new Texture("compiler-monster.png");
        inimigoHit = new Texture("compiler-monster-hit.png");
        inimigo1 = new Texture("binary-slime.png");
        inimigo1Hit = new Texture("binary-slime-hit.png");
        inimigo2 = new Texture("beast-cmd.png");
        inimigo2Hit = new Texture("cmd-hit.png");
        code_injection = new Texture("code_injection.png");
        garbage_colector = new Texture("garbage_colector.png");
        root_acess = new Texture("root_acess.png");
        soundA = new Texture("volume_on.png");
        soundD = new Texture("volume_mute.png");
        playerAtkTex = new Texture("PlayerdAMA.png");
        playerMorteText = new Texture("PlayerMorto.png");
        somHitJ = Gdx.audio.newSound(Gdx.files.internal("Sons/ouch-robot.mp3"));
        iconduplication = new Texture("duplicationicon.png");
        brancofundo = new Texture("branco.png");
        attackicon = new Texture("attackicon.png");
        nullpointerslash = new Texture("nullpointer.png");
        overclock = new Texture("overclock.png");
        safemode = new Texture("safemode.png");
        adaptiveai = new Texture("adaptiveai.png");
        erro404 = new Texture("erro404.png");
    }
    private void fabricaCarta() {
        fabricasCartas = new HashMap<>();
        adicionarFactory("golpe", 0, slice, TipoC.ATK, "Sons/slap-hurt-pain-sound-effect.mp3");
        adicionarFactory("burst", 1, burst, TipoC.HAB, "Sons/card-woosh.mp3");
        adicionarFactory("wraith", 2, wraith, TipoC.POD, "Sons/card-woosh.mp3");
        adicionarFactory("code_injection", 1, code_injection, TipoC.ATK, "Sons/slap-hurt-pain-sound-effect.mp3");
        adicionarFactory("garbage_colector", 0, garbage_colector, TipoC.HAB, "Sons/card-woosh.mp3");
        adicionarFactory("root_acess", 0, root_acess, TipoC.POD,"Sons/card-woosh.mp3");
        adicionarFactory("null_pointer_slash", 0, nullpointerslash, TipoC.ATK, "Sons/slap-hurt-pain-sound-effect.mp3");
        adicionarFactory("systemoverclock", 3, overclock,TipoC.HAB, "Sons/card-woosh.mp3");
        adicionarFactory("safemode", 2, safemode, TipoC.POD, "Sons/card-woosh.mp3");
        adicionarFactory("adaptiveai", 3, adaptiveai, TipoC.HAB, "Sons/card-woosh.mp3");
        adicionarFactory("erro404", 0, erro404, TipoC.CUR, "Sons/card-woosh.mp3");
    }
    private void criardeck() {
        for (int i = 0; i < 6; i++) jogador.deckPlayer.add(fabricasCartas.get("golpe").criarCarta());
        for (int i = 0; i < 6; i++) jogador.deckPlayer.add(fabricasCartas.get("burst").criarCarta());
        for (int i = 0; i < 2; i++) jogador.deckPlayer.add(fabricasCartas.get("wraith").criarCarta());
        Collections.shuffle(jogador.deckPlayer, new Random());
    }
    private void adicionarFactory(String nome, int custo, Texture img, TipoC tipo, String somPath) {
        Sound som = Gdx.audio.newSound(Gdx.files.internal(somPath));
        FactoryCartas factory = new FactoryCartas(custo, nome, img, tipo, som);
        fabricasCartas.put(nome.toLowerCase(), factory);
    }

}