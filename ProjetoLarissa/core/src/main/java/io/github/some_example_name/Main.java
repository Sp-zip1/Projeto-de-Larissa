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
    public SpriteBatch batch;
    //OQ ESTAVA NO MAIN AGORA VAI PARA TELA BATALHA
    @Override
    public void create() {
        batch = new SpriteBatch();
        telaMapa = new TelaMapa(this);
        setScreen(telaMapa);
    }
    @Override
    public void render(){
        super.render();
    }
    @Override
    public void dispose(){
        telaMapa.dispose();
    }
}