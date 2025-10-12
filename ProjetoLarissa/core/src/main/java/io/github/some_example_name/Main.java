package io.github.some_example_name;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.Carta;
import Entidades.TipoC;
import Flyweight.CartaFactory;
import Flyweight.FactoryCartas;
import Telas.TelaMapa;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
//PRIODIADES:
//MUDANÇA DE FASE
//NOVAS CARTAS
//ORGANIZAR ESSE CODIGO MELHOR(CODIGO DA RECOMPENSA ESTA MUITO CONFUSO EU PRECISO MODULARIZAR MAIS OU FAZER DE OUTRA MANEIRA
public class Main extends Game {
    public TelaMapa telaMapa;
    public ArrayList<Inimigo> todosInimigos = new ArrayList<>();
    public SpriteBatch batch;
    public Texture slice, burst, wraith;
    public Texture inimigoBossHit, inimigoBoss;
    public Texture endTurnTex, backGround;
    public Texture TextJog, playerDanTex;
    public Music backgroundMusic;
    public Texture inimigo,inimigoHit, inimigo1, inimigo1Hit, inimigo2, inimigo2Hit;
    //OQ ESTAVA NO MAIN AGORA VAI PARA TELA BATALHA
    @Override
    public void create() {
        batch = new SpriteBatch();
        carregarTexturasESons();
        telaMapa = new TelaMapa(this, this);
        setScreen(telaMapa);
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
        inimigo2Hit = new Texture("binary-slime-hit.png");
    }
}