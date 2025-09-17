package Entidades;

import com.badlogic.gdx.graphics.Texture;

public class CartaPoder extends Carta {
    Integer efeito;

    public CartaPoder(Integer custo, String nome, Texture imagem, Integer efeito) {
        super(custo, nome, imagem);
        this.efeito = efeito;
    }

    public Integer getEfeito() {
        return efeito;
    }

    public void setEfeito(Integer efeito) {
        this.efeito = efeito;
    }

    @Override
    public String toString() {
        return "CartaPoder{" +
            "efeito=" + efeito +
            ", custo=" + custo +
            ", nome='" + nome + '\'' +
            ", imagem=" + imagem +
            '}';
    }
}
