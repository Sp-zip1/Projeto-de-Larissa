package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.CartaPoder;
import com.badlogic.gdx.graphics.Texture;

public class FactoryCartaPod implements CartaFactory{
    String nome;
    Texture imagem;
    Integer custo;

    public FactoryCartaPod(String nome, Texture imagem, Integer custo) {
        this.nome = nome;
        this.imagem = imagem;
        this.custo = custo;
    }

    @Override
    public Carta criarCarta() {
        CartaFlyweight fly = CartaFlyweightFactory.getCarta(nome, imagem, custo);
        return new CartaPoder(fly.getCusto(), fly.getNome(), fly.getImagem()).adicionarEfeito(Efeito.danoExtra(2));
    }
}
