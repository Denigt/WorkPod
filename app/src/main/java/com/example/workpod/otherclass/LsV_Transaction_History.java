package com.example.workpod.otherclass;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class LsV_Transaction_History {
    private int codigo;
    private String ubicacion;
    private ZonedDateTime fechaEntrada;
    private ZonedDateTime fechaSalida;
    private Double precio;
    private String oferta;
    private String tarjeta;


    public LsV_Transaction_History(int codigo, String ubicacion, ZonedDateTime fechaEntrada, ZonedDateTime fechaSalida,
                                   Double precio, String oferta, String tarjeta) {
        this.codigo = codigo;
        this.ubicacion = ubicacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.precio = precio;
        this.oferta = oferta;
        this.tarjeta = tarjeta;
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

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public ZonedDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(ZonedDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
}
