package Flyweight;

import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Erro404Factory extends CartaFactory {
    public Erro404Factory(Texture img, Sound som) {
        super("Erro 404", img, 0, som, TipoC.CUR);
    }

    @Override
    public Carta criarCarta() {
        return criarCartaBase();
    }
}
