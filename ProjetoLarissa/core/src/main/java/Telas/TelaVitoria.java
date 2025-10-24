package Telas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;

public class TelaVitoria implements Screen {

    private final Game game;
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;
    private Texture fundoVitoria, vitoria;
    public TelaVitoria(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        fundoVitoria = new Texture("testFundo.png");
        vitoria = new Texture("vitoria.png");
        font = new BitmapFont(Gdx.files.internal("fonte/fontejogo.fnt"));
        font.getData().setScale(1f);
        font.setColor(Color.GOLD);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.WHITE;

        TextButton botaoReiniciar = new TextButton("Voltar ao Menu", style);
        botaoReiniciar.setPosition(Gdx.graphics.getWidth()/2f - 100, 150);
        botaoReiniciar.setSize(200, 60);
        botaoReiniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(Main.getInstance().telaMenu);
                dispose();
            }
        });

        stage.addActor(botaoReiniciar);
    }

    @Override
    public void render(float delta) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            if (fundoVitoria != null)
                batch.draw(fundoVitoria, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            String textoVitoria = "VOCÊ VENCEU!";
            float textoY = screenHeight - 150; // 150px do topo
            float textoX = screenWidth/2f - 150;
            font.draw(batch, textoVitoria, textoX, textoY);
            float botaoY = 150;
            float espacoDisponivel = textoY - botaoY;
            float imagemLargura = vitoria.getWidth();
            float imagemAltura = vitoria.getHeight();
            float imagemY = botaoY + (espacoDisponivel - imagemAltura) / 2f;
            float imagemX = (screenWidth - imagemLargura) / 2f;
            batch.draw(vitoria, imagemX, imagemY);
            batch.end();

            stage.act(delta);
            stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        if (fundoVitoria != null) fundoVitoria.dispose();
    }
}
