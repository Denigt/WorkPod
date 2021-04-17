package com.example.workpod.otherclass;

public class LsV_Transaction_History {
    private int codigo;
    private String ubicacion;
    private String fecha;
    private String hora;

    public LsV_Transaction_History(int codigo, String ubicacion, String fecha, String hora) {
        this.codigo = codigo;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
