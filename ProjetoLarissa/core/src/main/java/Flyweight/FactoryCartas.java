package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.Collections;

public class FactoryCartas implements CartaFactory{
    public int custo;
    public String nome;
    public Texture img;
    public TipoC tipoc;
    public Sound somc;
    public static ArrayList<FactoryCartas> todas = new ArrayList<>();


    public FactoryCartas(int custo, String nome, Texture img, TipoC tipoc, Sound somc) {
        this.custo = custo;
        this.nome = nome;
        this.img = img;
        this.tipoc = tipoc;
        this.somc = somc;
        todas.add(this);
    }

    public ArrayList<FactoryCartas> getTodas() {
        return todas;
    }
    @Override
    public Carta criarCarta() {
        CartaFlyweight fly = new CartaFlyweight(nome, img, custo, somc, tipoc);
        if (nome.equalsIgnoreCase("golpe")) {
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.dano(10));
        }
        if (nome.equalsIgnoreCase("burst")) {
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.duplicarProxima());
        }
        if (nome.equalsIgnoreCase("wraith")) {
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.forcaCrescente(1, 1, 2));
        }
        if (nome.equalsIgnoreCase("code_injection")) {
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.cavarCartas(2)).adicionarEfeito(Efeito.dano(2));
        }
        if (nome.equalsIgnoreCase("garbage_colector")) {
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.cura(4)).adicionarEfeito(Efeito.removerMão());
        }
        if (nome.equalsIgnoreCase("root_acess")) {
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.manaExtra(1));
        }
        if (nome.equalsIgnoreCase("null_pointer_slash")) {
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.nullPointerSlash(12));
        }
        if (nome.equalsIgnoreCase("systemoverclock")) {
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.systemOverclock());
        }
        if (nome.equalsIgnoreCase("safemode")) {
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.imunedanoJ(1));
        }
        if (nome.equalsIgnoreCase("adaptiveai")) {
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.adaptiveAI());
        }
        if (nome.equalsIgnoreCase("erro404")) {
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC());
        }
        if (nome.equalsIgnoreCase("trojan")) {
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.danoContinuoInimigo(1, 99));
        }
        if(nome.equalsIgnoreCase("ping")){
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.pingOfDeath());
        }
        if(nome.equalsIgnoreCase("restore")){
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.regeneracao(3, 3));
        }
        else {
            return null;
        }
    }
    public static ArrayList<Carta> gerarRecompensasAleatorias(int quantidade) {
        ArrayList<Carta> recompensas = new ArrayList<>();
        ArrayList<FactoryCartas> copia = new ArrayList<>(todas);
        Collections.shuffle(copia);
        int i = 0;
        while (recompensas.size() < quantidade && i < copia.size()) {
            FactoryCartas f = copia.get(i);
            if (f.tipoc != TipoC.CUR) {
                Carta nova = f.criarCarta();
                if (nova != null) {
                    recompensas.add(nova);
                }
            }
            i++;
        }
        return recompensas;
    }
}
