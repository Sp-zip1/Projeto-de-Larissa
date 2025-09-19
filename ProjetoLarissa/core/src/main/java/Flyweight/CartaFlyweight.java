package Flyweight;

import com.badlogic.gdx.graphics.Texture;

public class CartaFlyweight {
    private final String nome;
    private final Texture imagem;
    private final int custo;

    public CartaFlyweight(String nome, Texture imagem, int custo) {
        this.nome = nome;
        this.imagem = imagem;
        this.custo = custo;
    }

    public String getNome() { return nome; }
    public Texture getImagem() { return imagem; }
    public int getCusto() { return custo; }
}
