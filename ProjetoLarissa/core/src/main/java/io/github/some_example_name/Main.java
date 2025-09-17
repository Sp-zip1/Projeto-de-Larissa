package io.github.some_example_name;

import Entidades.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
    private Random rand;
    Inimigo inimigoTeste = new Inimigo(20, 2);
    private ArrayList<ImageButton> botoesCartas = new ArrayList<>();
    private ArrayList<Carta> deckPlayer = new ArrayList<>();
    private ArrayList<Carta> mãoPlayer = new ArrayList<>();
    private Texture slice, burst, wraith;
    private int mana = 3;
    void criardeck() {
        batch = new SpriteBatch();
        slice = new Texture(Gdx.files.internal("slice.png"));
        burst = new Texture(Gdx.files.internal("burst.png"));
        wraith = new Texture(Gdx.files.internal("wraith.png"));
        for (int i = 0; i < 6; i++) deckPlayer.add(new CartaAtq(0, "Slice", slice, 1));
        for (int i = 0; i < 6; i++) deckPlayer.add(new CartaHab(1, "Burst", burst, 1));
        for (int i = 0; i < 2; i++) deckPlayer.add(new CartaPoder(3, "Wraith", wraith, 3));
    }

    @Override
    public void create() {
        criardeck();
        Collections.shuffle(deckPlayer, new Random());
        mãoPlayer = new ArrayList<>(deckPlayer.subList(0, 6));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Criar botões clicáveis para cada carta na mão
        for (int i = 0; i < mãoPlayer.size(); i++) {
            Carta carta = mãoPlayer.get(i);
            TextureRegionDrawable drawable = new TextureRegionDrawable(carta.getImagem());
            ImageButton button = new ImageButton(drawable);
            button.setPosition(50 + i * 150, 20);
            button.setSize(100, 150);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    button.remove();
                    mana = mana - carta.custo;
                }
            });
            botoesCartas.add(button);
            stage.addActor(button);
        }
    }

    @Override
    public void render() {
        batch.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        BitmapFont font = new BitmapFont();
        font.draw(batch, "Mana: " + mana, 50, 200);
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
