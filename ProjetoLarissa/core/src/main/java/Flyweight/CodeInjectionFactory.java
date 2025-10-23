package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class CodeInjectionFactory extends CartaFactory {
    public CodeInjectionFactory(Texture img, Sound som) {
        super("Code Injection", img, 1, som, TipoC.ATK);
    }

    @Override
    public Carta criarCarta() {
        return criarCartaBase()
                .adicionarEfeito(Efeito.cavarCartas(2))
                .adicionarEfeito(Efeito.dano(2));
    }
}