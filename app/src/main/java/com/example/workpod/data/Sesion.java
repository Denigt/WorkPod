package com.example.workpod.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Sesion implements DataDb{
    private int id;
    private Date entrada;
    private Date salida;
    private double precio;
    private double tiempo;
    private int descuento;
    private Usuario usuario;
    private Workpod workpod;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getSalida() {
        return salida;
    }

    public void setSalida(Date salida) {
        this.salida = salida;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Workpod getWorkpod() {
        return workpod;
    }

    public void setWorkpod(Workpod workpod) {
        this.workpod = workpod;
    }

    @Override
    public DataDb JSONaData(JSONObject JSON) {
        return null;
    }

    @Override
    public List<Sesion> JSONaList(JSONObject json) {
        ArrayList<Sesion> lstSesiones = new ArrayList<>();
        try {
            JSONArray lstSesionesJSON = json.getJSONArray("sesion");
            for (int i = 0; i < lstSesionesJSON.length(); i++){
                Sesion sesion = new Sesion();
                JSONObject sesionJSON = lstSesionesJSON.getJSONObject(i);

                sesion.setId(sesionJSON.getInt("id"));
                sesion.setEntrada(Date.valueOf(sesionJSON.getString("entrada")));
                sesion.setSalida(Date.valueOf(sesionJSON.getString("salida")));
                sesion.setPrecio(sesionJSON.getDouble("precio"));
                sesion.setTiempo(sesionJSON.getDouble("tiempo"));
                sesion.setDescuento(sesionJSON.getInt("descuento"));

                lstSesiones.add(sesion);
            }
        }catch(Exception e){

        }

        return lstSesiones;
    }

    @Override
    public String getTabla() {
        return "sesion";
    }

    @Override
    public String getID() {
        return String.valueOf(id);
    }
}
