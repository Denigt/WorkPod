package com.workpodapp.workpod.otherclass;

import com.workpodapp.workpod.data.Cupon;

public class LsV_Descuentos {
    //DECLARACIÓN DE ATRIBUTOS
    private int id;
    private String nombreDescuento;
    private String minGratis;

    //CONSTRUCTOR POR DEFECTO
    public LsV_Descuentos() {
    }

    // CONSTRUCTOR MAS SENCILLO
    public LsV_Descuentos(int id, Cupon cupon) {
        this.id = id;
        this.nombreDescuento = cupon.getCampana().getNombre();
        this.minGratis = cupon.getCampana().getDescuento() + " minutos gratis";
    }

    //CONSTRUCTOR CON TODOS LOS ATRIBUTOS
    public LsV_Descuentos(int id, String nombreDescuento, String minGratis) {
        this.id = id;
        this.nombreDescuento = nombreDescuento;
        this.minGratis = minGratis;
    }

    //GETTERS Y SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreDescuento() {
        return nombreDescuento;
    }

    public void setNombreDescuento(String nombreDescuento) {
        this.nombreDescuento = nombreDescuento;
    }

    public String getMinGratis() {
        return minGratis;
    }

    public void setMinGratis(String minGratis) {
        this.minGratis = minGratis;
    }
}
