package io.github.some_example_name;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.Carta;
import Entidades.TipoC;
import Flyweight.*;
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
    public Texture iconduplication, attackicon, regenicon, nohitico;
    public Texture slice, burst, wraith, code_injection, garbage_colector, root_acess, nullpointerslash, overclock, safemode, adaptiveai, erro404, trojan, ping;
    public Texture inimigoBossHit, inimigoBoss, inimigoBossattack;
    public Texture endTurnTex, backGround, soundA, soundD, menuFundo;
    public Texture TextJog, playerDanTex, playerAtkTex, playerMorteText;
    public Music backgroundMusic, MenuMusic;
    public Jogador jogador;
    public Texture restore, inirar, inirarHit, dot, inifox, inifoxHit, inifoxattack, inirarattack;
    public Sound somHitJ;
    public TelaMenu telaMenu;
    public Texture brancofundo;
    public BitmapFont fontGrande, fontPequena;
    public Texture inimigo, inimigoHit, inimigo1, inimigo1Hit, inimigo2, inimigo2Hit, inimigoattack, inimigo1attack, inimigo2attack;
    public static Main instance;
    public Sound somAtaque, somHabilidade;

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
        jogador = new Jogador(100, 0, 0, 3, TextJog, somHitJ);
        fabricaCarta();
        criardeck();
        telaMapa = new TelaMapa(this, this);
        telaMenu = new TelaMenu(this, this);
        //setScreen(new TelaMenu(this, this));
        setScreen(telaMenu);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }

    private void carregarTexturasESons() {
        slice = new Texture(Gdx.files.internal("Plasma.png"));
        burst = new Texture(Gdx.files.internal("Recurvidade.png"));
        wraith = new Texture(Gdx.files.internal("wraith.png"));
        endTurnTex = new Texture(Gdx.files.internal("node.png"));
        backGround = new Texture(Gdx.files.internal("testFundo.png"));
        menuFundo = new Texture(Gdx.files.internal("FundoMenu.png"));
        TextJog = new Texture(Gdx.files.internal("Player.png"));
        playerDanTex = new Texture(Gdx.files.internal("player-hit.png"));
        inimigoBoss = new Texture(Gdx.files.internal("tangled-wires.png"));
        inimigoBossHit = new Texture(Gdx.files.internal("tangled-wires-hit.png"));
        inimigoBossattack = new Texture(Gdx.files.internal("boss-attack.png"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sons/analog-texture.mp3"));
        MenuMusic = Gdx.audio.newMusic(Gdx.files.internal("Sons/MENU.mp3"));
        inimigo = new Texture("compiler-monster.png");
        inimigoHit = new Texture("compiler-monster-hit.png");
        inimigoattack = new Texture("compiler-attack.png");
        inimigo1 = new Texture("binary-slime.png");
        inimigo1Hit = new Texture("binary-slime-hit.png");
        inimigo1attack = new Texture("slime-attack.png");
        inimigo2 = new Texture("beast-cmd.png");
        inimigo2Hit = new Texture("cmd-hit.png");
        inimigo2attack = new Texture("cmd-attack.png");
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
        trojan = new Texture("trojan.png");
        ping = new Texture("ping.png");
        restore = new Texture("restore.png");
        inirar = new Texture("beast-rar.png");
        inirarHit = new Texture("rar-hit.png");
        inirarattack = new Texture("rar-attack.png");
        inifox = new Texture("Firefox.png");
        inifoxHit = new Texture("Firefox-hit.png");
        inifoxattack = new Texture("FireFox-attack.png");
        regenicon = new Texture("regenicon.png");
        nohitico = new Texture("ivulneravel.png");
        dot = new Texture("poison.png");
        somAtaque = Gdx.audio.newSound(Gdx.files.internal("Sons/slap-hurt-pain-sound-effect.mp3"));
        somHabilidade = Gdx.audio.newSound(Gdx.files.internal("Sons/card-woosh.mp3"));
    }

    private void fabricaCarta() {
        CartaFactoryRegistry.registrar("golpe",
                new GolpeFactory(slice, somAtaque));
        CartaFactoryRegistry.registrar("burst",
                new BurstFactory(burst, somHabilidade));
        CartaFactoryRegistry.registrar("wraith",
                new WraithFactory(wraith, somHabilidade));
        CartaFactoryRegistry.registrar("code_injection",
                new CodeInjectionFactory(code_injection, somAtaque));
        CartaFactoryRegistry.registrar("garbage_colector",
                new GarbageCollectorFactory(garbage_colector, somHabilidade));
        CartaFactoryRegistry.registrar("root_acess",
                new RootAccessFactory(root_acess, somHabilidade));
        CartaFactoryRegistry.registrar("null_pointer_slash",
                new NullPointerSlashFactory(nullpointerslash, somAtaque));
        CartaFactoryRegistry.registrar("systemoverclock",
                new SystemOverclockFactory(overclock, somHabilidade));
        CartaFactoryRegistry.registrar("safemode",
                new SafeModeFactory(safemode, somHabilidade));
        CartaFactoryRegistry.registrar("adaptiveai",
                new AdaptiveAIFactory(adaptiveai, somHabilidade));
        CartaFactoryRegistry.registrar("erro404",
                new Erro404Factory(erro404, somHabilidade));
        CartaFactoryRegistry.registrar("trojan",
                new TrojanFactory(trojan, somHabilidade));
        CartaFactoryRegistry.registrar("ping",
                new PingFactory(ping, somAtaque));
        CartaFactoryRegistry.registrar("restore",
                new RestoreFactory(restore, somHabilidade));
        System.out.println("Cartas registradas: " + CartaFactoryRegistry.tamanho());
        System.out.println("Flyweights criados: " + CartaFlyweight.getTamanhoPool());
    }

    private void criardeck() {
        for (int i = 0; i < 6; i++) jogador.deckPlayer.add(CartaFactoryRegistry.criar("golpe"));
        for (int i = 0; i < 6; i++) jogador.deckPlayer.add(CartaFactoryRegistry.criar("burst"));
        for (int i = 0; i < 2; i++) jogador.deckPlayer.add(CartaFactoryRegistry.criar("wraith"));
        Collections.shuffle(jogador.deckPlayer, new Random());
    }
}