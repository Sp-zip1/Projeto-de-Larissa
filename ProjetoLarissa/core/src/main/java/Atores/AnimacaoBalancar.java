package Atores;

public class AnimacaoBalancar implements AnimacaoInimigo {
    private float tempo = 0f;
    private float amplitudeRotacao = 5f; // Graus de rotação
    private float velocidade = 1f;

    public AnimacaoBalancar() {}

    public AnimacaoBalancar(float amplitudeRotacao, float velocidade) {
        this.amplitudeRotacao = amplitudeRotacao;
        this.velocidade = velocidade;
    }

    @Override
    public void atualizar(float delta) {
        tempo += delta;
    }

    @Override
    public float getOffsetX() {
        return 0;
    }

    @Override
    public float getOffsetY() {
        return 1f;
    }

    @Override
    public float getEscala() {
        return 1f;
    }

    @Override
    public float getRotacao() {
        return amplitudeRotacao * (float)Math.sin(tempo * velocidade);
    }

    @Override
    public float getAlpha() {
        return 1f;
    }

    @Override
    public void resetar() {
        tempo = 0f;
    }
}