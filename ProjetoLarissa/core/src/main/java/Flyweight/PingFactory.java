package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class PingFactory extends CartaFactory {
    public PingFactory(Texture img, Sound som) {
        super("Ping", img, 1, som, TipoC.ATK);
    }
    @Override
    public Carta criarCarta() {
        return criarCartaBase().adicionarEfeito(Efeito.pingOfDeath());
    }
}
