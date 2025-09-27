package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.CartaHab;
import com.badlogic.gdx.graphics.Texture;

public class FactoryCartaHab implements CartaFactory {
    private final Texture imagem;
    private final String nome;
    private final Integer custo;

    public FactoryCartaHab(Texture imagem, String nome, Integer custo) {
        this.imagem = imagem;
        this.nome = nome;
        this.custo = custo;
    }

    @Override
    public Carta criarCarta() {
        CartaFlyweight fly = CartaFlyweightFactory.getCarta(nome, imagem, custo);
        return new CartaHab(fly.getCusto(), fly.getNome(), fly.getImagem()).adicionarEfeito(Efeito.cura(10));
    }
}