package com.workpodapp.workpod.otherclass;

public class Spinner_Years_Transaction_History {
    private int codigo;
    private String titulo;

    public Spinner_Years_Transaction_History() {
    }

    public Spinner_Years_Transaction_History(int codigo, String titulo) {
        this.codigo = codigo;
        this.titulo = titulo;
    }

    //GETTERS Y SETTERS
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
