package dev.rafaelreis.geral;

public class Usuario {
    private String nome;
    private int pontos;
    private boolean moderador;

    private Usuario(String nome, int pontos) {
        this.pontos = pontos;
        this.nome = nome;
        this.moderador = false;
    }

    public static Usuario of(String nome, int pontos) {
        return new Usuario(nome, pontos);
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

}
