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
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
    private float tempoRespiracao = 0f;
    private float escalaRespiracao = 1f;
    float larguraDelay;
    private ArrayList<ImageButton> botoesCartas = new ArrayList<>();
    private ArrayList<ImageButton> botoesRecompensa = new ArrayList<>();
    private ImageButton endTurnBtn, volumeBtn;
    private Inimigo inimigo;
    private boolean turnoJogador = true, mostrandoRecompensa;
    private boolean musicEnabled = true, bakcroungVisivel = true;

    private float playerOffsetY = 0, playerOffsetX = 0;
    private float tremorTimer;
    private  float tremorTimerP = 0f;
    private Game game;
    private final TelaMapa telaMapa;
    private ArrayList<Inimigo> inimigos;
    private  Inimigo inimigoSorteado;
    private DragAndDrop dragAndDrop;
    private Main main;
    public ShapeRenderer shapeRaios;
    // === CONSTRUTOR ===
    public TelaBatalha(Game game, TelaMapa telaMapa, ArrayList<Inimigo> inimigos, Main main) {
        this.game = game;
        this.main = main;
        this.telaMapa = telaMapa;
        this.inimigos = inimigos;
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        shapeRaios = new ShapeRenderer();
        barraVidaIn = new ShapeRenderer();
        barraVidaP = new ShapeRenderer();
        font = new BitmapFont(Gdx.files.internal("fonte/fontejogo.fnt"));
        font.getData().setScale(0.5f);
        font.setColor(Color.WHITE);
        Random random = new Random();
        int sorteado = random.nextInt(inimigos.size());
        inimigoSorteado = inimigos.get(sorteado);
        inimigoSorteado.setHPInimigo(inimigoSorteado.getMaxHP());
        inimigo = inimigoSorteado;
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
        tempoRespiracao += delta;
        escalaRespiracao = 1f + 0.03f * (float)Math.sin(tempoRespiracao * 2.5f);
        float largura = 200 * escalaRespiracao;
        float altura = 200 * escalaRespiracao;
        this.atualizarTremores(delta, inimigoSorteado);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (bakcroungVisivel && main.backGround != null) {
            batch.begin();
            batch.draw(main.backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            //ficou feinho
            batch.draw(main.jogador.getImgPlayer(),
                    200 + playerOffsetX - (largura - 200) / 2f,
                    200 + playerOffsetY - (altura - 200) / 2f,
                    largura,
                    altura);
            batch.draw(inimigo.getInimigoImg(), 800 + inimigo.getOffsetX(), 200 + inimigo.getOffsetY(), 200, 200);

            batch.end();
            criarBarraHPIn(760, 540, 200, 30, inimigo.getHPInimigo(), inimigo.getMaxHP());
            criarBarraHPIn(270, 540, 200, 30, main.jogador.getHPPlayer(), 100);
            if (tremorTimerP > 0) {
                desenharRaiosSobreJogador();
            }
        }
        // Desenha jogador e inimigo
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



    private void criarBarraHPIn(float x, float y, float larguraMax, float altura, float hpAtual, float hpMax) {
            float ratio = Math.max(0, Math.min(hpAtual / hpMax, 1));

            float larguraAtual = larguraMax * ratio;
            larguraDelay = Math.max(larguraAtual, larguraDelay - 100 * Gdx.graphics.getDeltaTime());

            Color corVida = new Color();
            if (ratio > 0.5f)
                corVida.set(1 - (ratio - 0.5f) * 2f, 1, 0.2f, 1);
            else
                corVida.set(1, ratio * 2f, 0.1f, 1);

            ShapeRenderer sr = barraVidaIn;
            sr.begin(ShapeRenderer.ShapeType.Filled);

            sr.setColor(0.07f, 0.07f, 0.07f, 1f);
            sr.rect(x, y, larguraMax, altura);

            // Barra vermelha de delay
            sr.setColor(0.7f, 0.1f, 0.1f, 1f);
            sr.rect(x, y, larguraDelay, altura);

            sr.rect(x, y, larguraAtual, altura,
                    new Color(corVida.r * 0.7f, corVida.g * 0.7f, corVida.b * 0.7f, 1),
                    corVida,
                    corVida,
                    new Color(corVida.r * 0.7f, corVida.g * 0.7f, corVida.b * 0.7f, 1));
            sr.end();
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(0, 0, 0, 0.9f);

            sr.rect(x, y, larguraMax, altura);

            int steps = 6;
            float r = altura * 0.3f;
            for (int i = 0; i < steps; i++) {
                float t = (float) i / (steps - 1);
                float offset = (float) Math.sin(t * Math.PI / 2f) * r;
                sr.line(x + offset, y + i * (altura / steps), x, y + i * (altura / steps));
                sr.line(x + larguraMax - offset, y + i * (altura / steps), x + larguraMax, y + i * (altura / steps));
            }

            sr.end();

            batch.begin();
            String texto = (int) hpAtual + " / " + (int) hpMax;
            font.setColor(Color.WHITE);
            BitmapFont.BitmapFontData data = font.getData();
            float textoLargura = data.capHeight * texto.length() / 2f;
            font.draw(batch, texto, x + larguraMax / 2f - textoLargura / 2f, y + altura / 2f + 6);
            batch.end();


    }

    private void puxarNovasCartas() {
        main.jogador.mãoPlayer.clear();
        for (ImageButton b : botoesCartas) b.remove();
        botoesCartas.clear();

        ArrayList<Carta> novas = main.jogador.puxarCartasDoDeck(6);
        Collections.shuffle(novas);
        main.jogador.mãoPlayer.addAll(novas);
        dragAndDrop = new DragAndDrop();
        for (int i = 0; i < main.jogador.mãoPlayer.size(); i++) {
            final Carta carta = main.jogador.mãoPlayer.get(i);
            TextureRegionDrawable drawable = new TextureRegionDrawable(carta.getImagem());
            ImageButton button = new ImageButton(drawable);
            button.setSize(100, 150);
            dragAndDrop.addSource(new Source(button) {
                @Override
                public Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    if (main.jogador.mana < carta.custo) return null; // sem mana
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
                    if(inimigoSorteado.getHPInimigo() > 0) {
                        // Executa ataque
                        Carta cartaSolta = (Carta) payload.getObject();
                        main.jogador.mana -= cartaSolta.custo;
                        main.jogador.mãoPlayer.remove(cartaSolta);
                        botoesCartas.remove(button);
                        main.jogador.descarte.add(cartaSolta);
                        cartaSolta.executarEfeitos(main.jogador, inimigo);
                        cartaSolta.somc.play();
                        button.remove();
                        reposicionarCartas();

                        if (cartaSolta.getTipoC() == TipoC.ATK) tremer(true);
                    }
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
        float larguraTotal = main.jogador.mãoPlayer.size() * 100;
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
        int atual = main.jogador.getHPPlayer();
        inimigo.ExecutarAçãoI(main.jogador);
        if (atual > main.jogador.getHPPlayer()) tremer(false);
        if (inimigo.getHPInimigo() <= 0) {
            entrarEstadoRecompensa();
        } else {
            turnoJogador = true;
            main.jogador.mana = 3;
            main.jogador.descarte.addAll(main.jogador.mãoPlayer);
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
            if (tipo == 0) opcoes.add(main.fabricasCartas.get("root_acess").criarCarta());
            else if (tipo == 1) opcoes.add(main.fabricasCartas.get("garbage_colector").criarCarta());
            else opcoes.add(main.fabricasCartas.get("code_injection").criarCarta());
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
                    main.jogador.deckPlayer.add(carta);
                    bakcroungVisivel = true;
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
        if (endTurnBtn != null) endTurnBtn.setVisible(false);
        bakcroungVisivel = false;
    }

    private void atualizarTremores(float delta, Inimigo inimigo) {
        aplicarTremor(true, delta, inimigo.getInimigoImg(), inimigo.getInimigoDan(), inimigo.getInimigoIdle());
        aplicarTremor(false, delta, main.TextJog, main.playerDanTex, main.TextJog);
    }

    private void tremer(boolean isInimigo) {
        if (isInimigo) tremorTimer = 0.3f;
        else tremorTimerP = 0.3f;
    }
    private void desenharRaiosSobreJogador() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

        shapeRaios.begin(ShapeRenderer.ShapeType.Filled);

        // Calcula o centro exato do sprite do player com escala de respiração
        float largura = 200 * escalaRespiracao;
        float altura = 200 * escalaRespiracao;
        float baseX = 200 + playerOffsetX - (largura - 200) / 2f + largura / 2f;
        float baseY = 200 + playerOffsetY - (altura - 200) / 2f + altura / 2f;

        for (int i = 0; i < 12; i++) {
            float angulo = (float)(Math.random() * Math.PI * 2);
            float distancia = 50 + (float)(Math.random() * 80);

            float x1 = baseX;
            float y1 = baseY;
            float x2 = baseX + (float)Math.cos(angulo) * distancia;
            float y2 = baseY + (float)Math.sin(angulo) * distancia;

            shapeRaios.setColor(1, 1, 1, 0.9f);
            desenharLinhaGrossa(x1, y1, x2, y2, 3);
            int segmentos = 2 + (int)(Math.random() * 3);
            float currentX = x1;
            float currentY = y1;

            for (int j = 0; j < segmentos; j++) {
                float t = (j + 1) / (float)segmentos;
                float nextX = x1 + (x2 - x1) * t + (float)(Math.random() * 20 - 10);
                float nextY = y1 + (y2 - y1) * t + (float)(Math.random() * 20 - 10);

                shapeRaios.setColor(1, 1, 1, 0.7f);
                desenharLinhaGrossa(currentX, currentY, nextX, nextY, 2);

                currentX = nextX;
                currentY = nextY;
            }
        }

        shapeRaios.end();
        shapeRaios.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < 8; i++) {
            float angulo = (float)(Math.random() * Math.PI * 2);
            float distancia = 40 + (float)(Math.random() * 60);

            float x = baseX + (float)Math.cos(angulo) * distancia;
            float y = baseY + (float)Math.sin(angulo) * distancia;

            shapeRaios.setColor(0.3f, 0.7f, 1f, 0.6f);
            shapeRaios.circle(x, y, 3);
        }
        shapeRaios.end();

        // Restaura blending normal
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
    private void desenharLinhaGrossa(float x1, float y1, float x2, float y2, float grossura) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dist = (float)Math.sqrt(dx * dx + dy * dy);

        if (dist == 0) return;

        float perpX = -dy / dist * grossura / 2;
        float perpY = dx / dist * grossura / 2;

        shapeRaios.triangle(
                x1 + perpX, y1 + perpY,
                x1 - perpX, y1 - perpY,
                x2, y2
        );
    }
    private void aplicarTremor(boolean isInimigo, float delta, Texture normalTex, Texture hitTex, Texture idleTex) {
        if (isInimigo) {
            if (tremorTimer > 0) {
                inimigo.setInimigoImg(hitTex);
                desenharRaiosSobreJogador();
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
                main.jogador.setImgPlayer(hitTex);
                tremorTimerP -= delta;
                playerOffsetX = (float)(Math.random() * 10 - 5);
                playerOffsetY = (float)(Math.random() * 10 - 5);
            } else {
                playerOffsetX = 0;
                playerOffsetY = 0;
                main.jogador.setImgPlayer(normalTex);
            }
        }
    }
}
