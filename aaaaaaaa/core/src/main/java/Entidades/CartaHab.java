package Entidades;

import com.badlogic.gdx.graphics.Texture;

public class CartaHab extends Carta{
    Integer defesa;

    public CartaHab(Integer custo, String nome, Texture imagem, Integer defesa) {
        super(custo, nome, imagem);
        this.defesa = defesa;
    }

    public Integer getDefesa() {
        return defesa;
    }

    public void setDefesa(Integer defesa) {
        this.defesa = defesa;
    }

    @Override
    public String toString() {
        return "CartaHab{" +
            "defesa=" + defesa +
            ", custo=" + custo +
            ", nome='" + nome + '\'' +
            ", imagem=" + imagem +
            '}';
    }
}
