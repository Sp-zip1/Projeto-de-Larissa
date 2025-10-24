package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import Flyweight.CartaFactory;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class GolpeFactory extends CartaFactory {
    public GolpeFactory(Texture img, Sound som) {
        super("Golpe", img, 1, som, TipoC.ATK);
    }
    @Override
    public Carta criarCarta() {
        return criarCartaBase().adicionarEfeito(Efeito.dano(10));
    }
}