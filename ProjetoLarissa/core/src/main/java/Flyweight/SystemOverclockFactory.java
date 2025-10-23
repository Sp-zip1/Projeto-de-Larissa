package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class SystemOverclockFactory extends CartaFactory {
    public SystemOverclockFactory(Texture img, Sound som) {
        super("System Overclock", img, 3, som, TipoC.HAB);
    }
    @Override
    public Carta criarCarta() {
        return criarCartaBase().adicionarEfeito(Efeito.systemOverclock());
    }
}
