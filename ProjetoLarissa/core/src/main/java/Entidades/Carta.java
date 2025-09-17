package Entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Carta {
    public Integer custo;
    public String nome;
    public Texture imagem;

    public Carta(Integer custo, String nome, Texture imagem) {
        this.custo = custo;
        this.nome = nome;
        this.imagem = imagem;
    }

    public Integer getCusto() {
        return custo;
    }

    public void setCusto(Integer custo) {
        this.custo = custo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Texture getImagem() {
        return imagem;
    }

    public void setImagem(Texture imagem) {
        this.imagem = imagem;
    }
    public void draw(SpriteBatch batch, float x, float y) {
        batch.draw(imagem, x, y, 120, 160); // largura/altura da carta
    }
}
