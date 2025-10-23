package Flyweight;

import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public abstract class CartaFactory {
    protected final CartaFlyweight flyweight;
    protected CartaFactory(String nome, Texture imagem, int custo, Sound som, TipoC tipo) {
        this.flyweight = CartaFlyweight.obter(nome, imagem, custo, som, tipo);
    }

    public abstract Carta criarCarta();
    protected Carta criarCartaBase() {
        return new Carta(flyweight);
    }

    public CartaFlyweight getFlyweight() {
        return flyweight;
    }
}
