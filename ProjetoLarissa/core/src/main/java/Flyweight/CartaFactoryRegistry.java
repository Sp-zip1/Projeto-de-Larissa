package Flyweight;

import Entidades.Carta;
import Entidades.TipoC;

import java.util.*;

/**
 * Registry centralizado que gerencia todas as factories de cartas.
 */
public class CartaFactoryRegistry {
    private static final Map<String, CartaFactory> factories = new HashMap<>();
    public static void registrar(String chave, CartaFactory factory) {
        factories.put(chave.toLowerCase(), factory);
    }
    public static Carta criar(String nome) {
        CartaFactory factory = factories.get(nome.toLowerCase());
        if (factory == null) {
            System.err.println("Factory não encontrada para: " + nome);
            return null;
        }
        return factory.criarCarta();
    }
    public static boolean existe(String nome) {
        return factories.containsKey(nome.toLowerCase());
    }
    public static ArrayList<Carta> gerarRecompensasAleatorias(int quantidade) {
        ArrayList<Carta> recompensas = new ArrayList<>();
        List<String> chaves = new ArrayList<>();

        // Filtra apenas cartas que não são maldições
        for (Map.Entry<String, CartaFactory> entry : factories.entrySet()) {
            CartaFactory factory = entry.getValue();
            if (factory.getFlyweight().getTipo() != TipoC.CUR) {
                chaves.add(entry.getKey());
            }
        }

        Collections.shuffle(chaves);

        for (int i = 0; i < Math.min(quantidade, chaves.size()); i++) {
            Carta carta = criar(chaves.get(i));
            if (carta != null) {
                recompensas.add(carta);
            }
        }

        return recompensas;
    }
    public static Set<String> obterTodasChaves() {
        return factories.keySet();
    }
    public static void limpar() {
        factories.clear();
    }
    public static int tamanho() {
        return factories.size();
    }
}