package com.example.workpod;

public class LsV_Menu_Usuario {
    private int codigo;
    private int icono;
    private String texto;

    public LsV_Menu_Usuario(int codigo, int icono, String texto) {
        this.codigo = codigo;
        this.icono = icono;
        this.texto = texto;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
