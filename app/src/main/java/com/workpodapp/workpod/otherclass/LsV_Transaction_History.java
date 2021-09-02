package com.workpodapp.workpod.otherclass;

import java.time.ZonedDateTime;

public class LsV_Transaction_History {
    private int codigo;
    private String ubicacion;
    private ZonedDateTime fechaEntrada;
    private ZonedDateTime fechaSalida;
    private Double precio;
    private String oferta;

    public LsV_Transaction_History(int codigo, String ubicacion, ZonedDateTime fechaEntrada, ZonedDateTime fechaSalida,
                                   Double precio, String oferta) {
        this.codigo = codigo;
        this.ubicacion = ubicacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.precio = precio;
        this.oferta = oferta;
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

    public ZonedDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(ZonedDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getOferta() {
        return oferta;
    }

    public void setOferta(String oferta) {
        this.oferta = oferta;
    }

    public ZonedDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(ZonedDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
}
