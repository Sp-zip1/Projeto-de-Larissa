package Flyweight;

import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

public class CartaFlyweight {
    private static final Map<String, CartaFlyweight> pool = new HashMap<>();

    private final String nome;
    private final Texture imagem;
    private final int custoBase;
    private final Sound som;
    private final TipoC tipo;

    private CartaFlyweight(String nome, Texture imagem, int custoBase, Sound som, TipoC tipo) {
        this.nome = nome;
        this.imagem = imagem;
        this.custoBase = custoBase;
        this.som = som;
        this.tipo = tipo;
    }

    /**
     * Obtém ou cria um Flyweight para a carta.*/
    public static CartaFlyweight obter(String nome, Texture imagem, int custoBase, Sound som, TipoC tipo) {
        String chave = nome.toLowerCase();

        if (!pool.containsKey(chave)) {
            pool.put(chave, new CartaFlyweight(nome, imagem, custoBase, som, tipo));
            System.out.println("Flyweight criado para: " + nome);
        }

        return pool.get(chave);
    }

    public String getNome() {
        return nome;
    }

    public Texture getImagem() {
        return imagem;
    }

    public int getCustoBase() {
        return custoBase;
    }

    public Sound getSom() {
        return som;
    }

    public TipoC getTipo() {
        return tipo;
    }

    public static int getTamanhoPool() {
        return pool.size();
    }

    public static void limparPool() {
        pool.clear();
    }
}