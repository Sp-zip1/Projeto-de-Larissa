package Ações;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EfeitoVisual {
    private float x, y;
    private String texto;
    private Color cor;
    private float tempo;
    private float duracao;

    public EfeitoVisual(float x, float y, String texto, Color cor, float duracao) {
        this.x = x;
        this.y = y;
        this.texto = texto;
        this.cor = cor;
        this.duracao = duracao;
        this.tempo = 0;
    }

    public boolean atualizar(float delta) {
        tempo += delta;
        y += 50 * delta; // sobe lentamente
        return tempo >= duracao;
    }

    public void desenhar(SpriteBatch batch, BitmapFont font) {
        float alpha = 1f - (tempo / duracao);
        font.setColor(cor.r, cor.g, cor.b, alpha);
        font.draw(batch, texto, x, y);
    }
}
