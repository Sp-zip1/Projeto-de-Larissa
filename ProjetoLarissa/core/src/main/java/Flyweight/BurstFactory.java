package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class BurstFactory extends CartaFactory {
    public BurstFactory(Texture img, Sound som) {
        super("Burst", img, 1, som, TipoC.HAB);
    }

    @Override
    public Carta criarCarta() {
        return criarCartaBase().adicionarEfeito(Efeito.duplicarProxima());
    }
}