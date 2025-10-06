package Flyweight;

import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class CartaFlyweightFactory {
    private static final Map<String, CartaFlyweight> pool = new HashMap<>();
    //DEVE SER CHAMADO NA HORA DE INSTACIAR A CARTA AO INVES DO NEW()
    public static CartaFlyweight getCarta(String nome, Texture imagem, int custo, Sound somC, TipoC tipoC) {
        String chave = nome + "_" + custo;
        if (!pool.containsKey(chave)) {
            pool.put(chave, new CartaFlyweight(nome, imagem, custo, somC, tipoC));
        }
        return pool.get(chave);
    }
}
