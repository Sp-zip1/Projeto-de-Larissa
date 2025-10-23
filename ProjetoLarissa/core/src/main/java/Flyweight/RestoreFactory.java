package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class RestoreFactory extends CartaFactory {
    public RestoreFactory(Texture img, Sound som) {
        super("Restore", img, 1, som, TipoC.POD);
    }

    @Override
    public Carta criarCarta() {
        return criarCartaBase().adicionarEfeito(Efeito.regeneracao(3, 3));
    }
}
