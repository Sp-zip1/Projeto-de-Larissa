package Atores;

public class AnimacaoFlutuar implements AnimacaoInimigo {
    private float tempo = 0f;
    private float amplitude = 15f; // Altura da flutuação
    private float velocidade = 1.5f; // Velocidade da flutuação

    public AnimacaoFlutuar() {}

    public AnimacaoFlutuar(float amplitude, float velocidade) {
        this.amplitude = amplitude;
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
        return amplitude * (float)Math.sin(tempo * velocidade);
    }

    @Override
    public float getEscala() {
        return 1f;
    }

    @Override
    public float getRotacao() {
        return 0f;
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