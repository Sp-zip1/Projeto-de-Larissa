package Ações;

import Atores.Jogador;
import Entidades.Carta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class BuffManager {
    public static class BuffBase{}
    public static class BuffTemporario extends BuffBase {
        private final BiConsumer<Jogador, Carta> acao;
        private boolean usado = false;

        public BuffTemporario(BiConsumer<Jogador, Carta> acao) {
            this.acao = acao;
        }

        public void aplicar(Jogador jogador, Carta carta) {
            if (!usado) {
                acao.accept(jogador, carta);
                usado = true;
            }
        }

        public boolean jaUsou() {
            return usado;
        }
    }
private final List<BuffBase> buffs = new ArrayList<>();
public void adicionarBuffTemp(BiConsumer<Jogador, Carta> acao){
    buffs.add(new BuffTemporario(acao));
}

}