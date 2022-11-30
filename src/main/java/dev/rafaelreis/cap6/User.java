package dev.rafaelreis.cap6;

public class User {
    private String nome;
    private int pontos;
    private boolean moderador;

    public User() {}

    public User(String nome) {
        this.nome = nome;
    }

    public User(String nome, int pontos) {
        this.nome = nome;
        this.pontos = pontos;
    }

    public void tornarModerador() {
        this.moderador = true;
    }

    public String getNome() {
        return nome;
    }

    public int getPontos() {
        return pontos;
    }

    public boolean isModerador() {
        return moderador;
    }

    @Override
    public String toString() {
        return String.format(
            "[Nome: %s, Pontos: %d, Moderador:%s]", nome, pontos, moderador);
    }
    
}
