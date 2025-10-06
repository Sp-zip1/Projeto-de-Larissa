package Flyweight;

import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class CartaFlyweight {
    private final String nome;
    private final Texture imagem;
    private final int custo;
    private final Sound somC;
    private final TipoC tipoC;

    public CartaFlyweight(String nome, Texture imagem, int custo, Sound somC, TipoC tipoC) {
        this.nome = nome;
        this.imagem = imagem;
        this.custo = custo;
        this.somC = somC;
        this.tipoC = tipoC;
    }

    public String getNome() {
        return nome;
    }

    public Texture getImagem() {
        return imagem;
    }

    public int getCusto() {
        return custo;
    }

    public Sound getSomC() {
        return somC;
    }

    public TipoC getTipoC() {
        return tipoC;
    }
}

