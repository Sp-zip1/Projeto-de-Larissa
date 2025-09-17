package Entidades;

import com.badlogic.gdx.graphics.Texture;

public class CartaAtq extends Carta{
    public Integer dano;

    public CartaAtq(Integer custo, String nome, Texture imagem, Integer dano) {
        super(custo, nome, imagem);
        this.dano = dano;
    }

    public Integer getDano() {
        return dano;
    }

    public void setDano(Integer dano) {
        this.dano = dano;
    }

    @Override
    public String toString() {
        return "CartaAtq{" +
            "dano=" + dano +
            ", custo=" + custo +
            ", nome='" + nome + '\'' +
            ", imagem=" + imagem +
            '}';
    }
}
