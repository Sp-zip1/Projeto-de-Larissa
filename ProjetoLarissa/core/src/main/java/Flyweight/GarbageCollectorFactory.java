package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class GarbageCollectorFactory extends CartaFactory {
    public GarbageCollectorFactory(Texture img, Sound som) {
        super("Garbage Collector", img, 0, som, TipoC.HAB);
    }

    @Override
    public Carta criarCarta() {
        return criarCartaBase()
                .adicionarEfeito(Efeito.cura(4))
                .adicionarEfeito(Efeito.removerMão());
    }
}