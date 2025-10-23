package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class RootAccessFactory extends CartaFactory {
    public RootAccessFactory(Texture img, Sound som) {
        super("Root Access", img, 0, som, TipoC.POD);
    }

    @Override
    public Carta criarCarta() {
        return criarCartaBase().adicionarEfeito(Efeito.manaExtra(1));
    }
}
