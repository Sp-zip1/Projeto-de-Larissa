package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class WraithFactory extends CartaFactory {
    public WraithFactory(Texture img, Sound som) {
        super("Reforçar", img, 2, som, TipoC.POD);
    }

    @Override
    public Carta criarCarta() {
        return criarCartaBase().adicionarEfeito(Efeito.forcaCrescente(1, 1, 2));
    }
}