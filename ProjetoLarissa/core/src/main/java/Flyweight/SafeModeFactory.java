package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class SafeModeFactory extends CartaFactory {
    public SafeModeFactory(Texture img, Sound som) {
        super("Safe Mode", img, 2, som, TipoC.POD);
    }

    @Override
    public Carta criarCarta() {
        return criarCartaBase().adicionarEfeito(Efeito.imunedanoJ(1));
    }
}
