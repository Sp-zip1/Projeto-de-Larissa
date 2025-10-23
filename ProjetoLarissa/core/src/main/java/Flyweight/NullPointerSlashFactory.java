package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class NullPointerSlashFactory extends CartaFactory {
    public NullPointerSlashFactory(Texture img, Sound som) {
        super("Null Pointer Slash", img, 0, som, TipoC.ATK);
    }
    @Override
    public Carta criarCarta() {
        return criarCartaBase().adicionarEfeito(Efeito.nullPointerSlash(12));
    }
}