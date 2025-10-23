package Entidades;

import Atores.Inimigo;
import Atores.Jogador;
import Ações.Efeito;
import Flyweight.CartaFlyweight;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Carta {
    private final CartaFlyweight carta;
    public List<Efeito> efeitos = new ArrayList<>();
    public Integer custo;
    public String nome;
    public Texture imagem;
    public Sound somc;
    public TipoC tipoC;

    public Carta(CartaFlyweight carta) {
    this.carta = carta;
        this.custo = carta.getCustoBase();
        this.nome = carta.getNome();
        this.imagem = carta.getImagem();
        this.somc = carta.getSom();
        this.tipoC = carta.getTipo();
    }


    public Integer getCusto() {
        return custo;
    }

    public void setCusto(Integer custo) {
        this.custo = custo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Texture getImagem() {
        return imagem;
    }

    public void setImagem(Texture imagem) {
        this.imagem = imagem;
    }

    public Sound getSomc() {
        return somc;
    }

    public void setSomc(Sound somc) {
        this.somc = somc;
    }

    public TipoC getTipoC() {
        return tipoC;
    }

    public void setTipoC(TipoC tipoC) {
        this.tipoC = tipoC;
    }

    public List<Efeito> getEfeitos() {
        return efeitos;
    }
    public void setEfeitos(List<Efeito> efeitos) {
        this.efeitos = efeitos;
    }

    public void draw(SpriteBatch batch, float x, float y) {
        batch.draw(imagem, x, y, 120, 160); // largura/altura da carta
    }
    public Carta adicionarEfeito(Efeito efeito) {
        efeitos.add(efeito);
        return this;
    }
    // Executa os efeitos da carta sem disparar buffs
    public void executarEfeitosDireto(Jogador jogador, Inimigo inimigo) {
        for (Efeito efeito : efeitos) {
            efeito.aplicar(jogador, inimigo);
        }
    }

    public void executarEfeitos(Jogador jogador, Inimigo inimigo) {
        //Evita stackoverflow dessa maneira
        jogador.getBuffManager().executarBuffTemp(jogador, this);
        executarEfeitosDireto(jogador, inimigo);
    }

}
