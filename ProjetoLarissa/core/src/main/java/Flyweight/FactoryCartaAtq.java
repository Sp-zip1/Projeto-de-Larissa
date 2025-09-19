package Flyweight;

import Entidades.Carta;
import Entidades.CartaAtq;
import com.badlogic.gdx.graphics.Texture;

public class FactoryCartaAtq implements CartaFactory{
    private final Texture imagem;
    private final String nome;
    private final Integer custo;

    public FactoryCartaAtq(Texture imagem, String nome, Integer custo) {
        this.imagem = imagem;
        this.nome = nome;
        this.custo = custo;
    }

    @Override
    public Carta criarCarta() {
        CartaFlyweight fly = CartaFlyweightFactory.getCarta(nome, imagem, custo);
        return new CartaAtq(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getCusto());
    }
}
