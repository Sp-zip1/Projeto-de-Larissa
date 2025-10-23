package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class TrojanFactory extends CartaFactory {
    public TrojanFactory(Texture img, Sound som) {
        super("Trojan", img, 2, som, TipoC.HAB);
    }
    @Override
    public Carta criarCarta() {
        return criarCartaBase().adicionarEfeito(Efeito.danoContinuoInimigo(1, 99));
    }
}