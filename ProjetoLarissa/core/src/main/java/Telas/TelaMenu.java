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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;

public class TelaMenu implements Screen {

    private Game game;
    private Main main;
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;

    private Texture backgroundTexture;
    private Texture logoTexture;

    private TextButton jogarButton;
    private TextButton configButton;
    private TextButton sairButton;
    private ImageButton volumeButton;

    private float tempoAnimacao = 0f;
    private float pulsoTitulo = 1f;
    private boolean musicEnabled = true;

    public TelaMenu(Game game, Main main) {
        this.game = game;
        this.main = main;

        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        titleFont = new BitmapFont(Gdx.files.internal("fonte/fontejogo.fnt"));
        titleFont.getData().setScale(1.2f);
        titleFont.setColor(Color.CYAN);

        buttonFont = new BitmapFont(Gdx.files.internal("fonte/fontejogo.fnt"));
        buttonFont.getData().setScale(0.6f);
        buttonFont.setColor(Color.WHITE);

        if (main.menuFundo != null) {
            backgroundTexture = main.menuFundo;
        }

        criarBotoes();
        if (main.MenuMusic != null) {
            main.MenuMusic.setLooping(true);
            if (musicEnabled) {
                main.MenuMusic.play();
            }
        }
    }

    private void criarBotoes() {
        float centerX = Gdx.graphics.getWidth() / 2f;
        float buttonWidth = 300;
        float buttonHeight = 70;
        float spacing = 90;
        float startY = 350;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = buttonFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.CYAN;
        buttonStyle.downFontColor = Color.YELLOW;

        jogarButton = new TextButton("JOGAR", buttonStyle);
        jogarButton.setSize(buttonWidth, buttonHeight);
        jogarButton.setPosition(centerX - buttonWidth / 2, startY);
        jogarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (main.MenuMusic != null) {
                    main.MenuMusic.stop();
                }
                game.setScreen(main.telaMapa);
            }
        });
        stage.addActor(jogarButton);

        // Botão CONFIGURAÇÕES
        configButton = new TextButton("CONFIGURACOES", buttonStyle);
        configButton.setSize(buttonWidth, buttonHeight);
        configButton.setPosition(centerX - buttonWidth / 2, startY - spacing);
        configButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // game.setScreen(new TelaConfig(game, main));
            }
        });
        stage.addActor(configButton);

        // Botão SAIR
        sairButton = new TextButton("SAIR", buttonStyle);
        sairButton.setSize(buttonWidth, buttonHeight);
        sairButton.setPosition(centerX - buttonWidth / 2, startY - spacing * 2);
        sairButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(sairButton);


    if (main.soundA != null && main.soundD != null) {
        TextureRegionDrawable volumeOnDrawable = new TextureRegionDrawable(main.soundA);
        TextureRegionDrawable volumeOffDrawable = new TextureRegionDrawable(main.soundD);
        
        ImageButton.ImageButtonStyle volumeButtonStyle = new ImageButton.ImageButtonStyle();
        volumeButtonStyle.up = volumeOnDrawable;        
        volumeButtonStyle.down = volumeOnDrawable;      
        volumeButtonStyle.checked = volumeOffDrawable;  
        
        volumeButton = new ImageButton(volumeButtonStyle);
        volumeButton.setSize(60, 60);
        volumeButton.setPosition(Gdx.graphics.getWidth() - 80, Gdx.graphics.getHeight() - 80);
        volumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                musicEnabled = !musicEnabled;
                
                volumeButton.setChecked(!musicEnabled);
                
                if (main.MenuMusic != null) {
                    if (musicEnabled) {
                        main.MenuMusic.play();
                    } else {
                        main.MenuMusic.pause();
                    }
                }
              
                volumeButton.setScale(0.8f);
                new Thread(() -> {
                    try {
                        Thread.sleep(100);
                        Gdx.app.postRunnable(() -> volumeButton.setScale(1f));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
        });
        stage.addActor(volumeButton);
    } else {
        System.out.println("Texturas de volume não encontradas! soundA ou soundD está nulo.");
    }
}

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        tempoAnimacao += delta;
        pulsoTitulo = 1f + 0.05f * (float) Math.sin(tempoAnimacao * 3f);
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (backgroundTexture != null) {
            batch.setColor(0.6f, 0.6f, 0.6f, 1f);
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.setColor(1f, 1f, 1f, 1f);
        }
        String titulo = "DEBUG THE WIRE";
        float tituloX = Gdx.graphics.getWidth() / 2f - 200 * pulsoTitulo;
        float tituloY = Gdx.graphics.getHeight() - 100;

        titleFont.getData().setScale(1.2f * pulsoTitulo);
        titleFont.setColor(0, 0, 0, 0.5f);
        titleFont.draw(batch, titulo, tituloX + 3, tituloY - 3);
        titleFont.setColor(Color.CYAN);
        titleFont.draw(batch, titulo, tituloX, tituloY);
        buttonFont.setColor(Color.GRAY);
        buttonFont.draw(batch, "v1.0.0", 10, 30);

        batch.end();

        stage.act(delta);
        stage.draw();
        atualizarHoverBotoes();
    }

    private void atualizarHoverBotoes() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        // Efeito de hover visual (pode adicionar mais efeitos aqui)
        if (jogarButton.hit(mouseX - jogarButton.getX(), mouseY - jogarButton.getY(), false) != null) {
            jogarButton.setScale(1.05f);
        } else {
            jogarButton.setScale(1f);
        }

        if (configButton.hit(mouseX - configButton.getX(), mouseY - configButton.getY(), false) != null) {
            configButton.setScale(1.05f);
        } else {
            configButton.setScale(1f);
        }

        if (sairButton.hit(mouseX - sairButton.getX(), mouseY - sairButton.getY(), false) != null) {
            sairButton.setScale(1.05f);
        } else {
            sairButton.setScale(1f);
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        float centerX = width / 2f;
        float buttonWidth = 300;
        float spacing = 90;
        float startY = 350;

        jogarButton.setPosition(centerX - buttonWidth / 2, startY);
        configButton.setPosition(centerX - buttonWidth / 2, startY - spacing);
        sairButton.setPosition(centerX - buttonWidth / 2, startY - spacing * 2);

        if (volumeButton != null) {
            volumeButton.setPosition(width - 80, height - 80);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        titleFont.dispose();
        buttonFont.dispose();
    }
}