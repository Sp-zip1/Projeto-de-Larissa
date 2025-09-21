package Flyweight;

import Entidades.Carta;
import Entidades.CartaAtq;
import com.badlogic.gdx.graphics.Texture;

public class FactoryCartaAtq implements CartaFactory{
    private final Texture imagem;
    private final String nome;
    private final Integer custo;
    private final Integer dano;

    public FactoryCartaAtq(Texture imagem, String nome, Integer custo, Integer dano) {
        this.imagem = imagem;
        this.nome = nome;
        this.custo = custo;
        this.dano = dano;
    }

    @Override
    public Carta criarCarta() {
        CartaFlyweight fly = CartaFlyweightFactory.getCarta(nome, imagem, custo);
        return new CartaAtq(fly.getCusto(), fly.getNome(), fly.getImagem(), dano);
    }
}
