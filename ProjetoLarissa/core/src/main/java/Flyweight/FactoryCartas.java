package Flyweight;

import Ações.Efeito;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class FactoryCartas implements CartaFactory{
    public int custo;
    public String nome;
    public Texture img;
    public TipoC tipoc;
    public Sound somc;

    public FactoryCartas(int custo, String nome, Texture img, TipoC tipoc, Sound somc) {
        this.custo = custo;
        this.nome = nome;
        this.img = img;
        this.tipoc = tipoc;
        this.somc = somc;
    }

    @Override
    public Carta criarCarta() {
        CartaFlyweight fly = new CartaFlyweight(nome, img, custo, somc, tipoc);
        if(nome.equalsIgnoreCase("golpe")){
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.dano(10));
        }
        if (nome.equalsIgnoreCase("burst")){
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.duplicarProxima());
        }
        if (nome.equalsIgnoreCase("wraith")){
            return new Carta(fly.getCusto(), fly.getNome(), fly.getImagem(), fly.getSomC(), fly.getTipoC()).adicionarEfeito(Efeito.danoExtra(2));
        }
        else {
            return null;
        }
    }
}
