package com.example.workpod.data;

import android.util.Log;

import com.example.workpod.basic.Method;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sesion implements DataDb {
    private int id;
    private ZonedDateTime entrada;
    private ZonedDateTime salida;
    private double precio;
    private double tiempo;
    private int descuento;
    private Usuario usuario;
    private Workpod workpod;
    private Direccion direccion;

    //CONSTRUCTOR POR DEFECTO

    public Sesion() {
    }

    public Sesion(int id, ZonedDateTime entrada, ZonedDateTime salida, double precio, int descuento, Direccion direccion) {
        this.id = id;
        this.entrada = entrada;
        this.salida = salida;
        this.precio = precio;
        this.tiempo = tiempo;
        this.descuento = descuento;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ZonedDateTime getEntrada() {
        return entrada;
    }

    public void setEntrada(ZonedDateTime entrada) {
        this.entrada = entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = Method.stringToDate(entrada, ZoneId.systemDefault());
    }

    public ZonedDateTime getSalida() {
        return salida;
    }

    public void setSalida(ZonedDateTime salida) {
        this.salida = salida;
    }

    public void setSalida(String salida) {
        this.salida = Method.stringToDate(salida, ZoneId.systemDefault());
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

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    @Override
    public DataDb JSONaData(JSONObject JSON) {
        return null;
    }

    @Override
    public JSONObject dataAJSON() {
        JSONObject json = new JSONObject();
        try {
        } catch (Exception e) {

        }

        return json;
    }

    @Override
    public List<Sesion> JSONaList(JSONObject json) {
        ArrayList<Sesion> lstSesiones = new ArrayList<>();
        try {
            JSONArray lstSesionesJSON = json.getJSONArray("sesion");
            for (int i = 0; i < lstSesionesJSON.length(); i++) {
                Sesion sesion = new Sesion();
                JSONObject sesionJSON = lstSesionesJSON.getJSONObject(i);

                if (sesionJSON.has("id") && !sesionJSON.isNull("id"))
                    sesion.setId(sesionJSON.getInt("id"));
                if (sesionJSON.has("entrada") && !sesionJSON.isNull("entrada"))
                    sesion.setEntrada(sesionJSON.getString("entrada"));
                if (sesionJSON.has("salida") && !sesionJSON.isNull("salida"))
                    sesion.setSalida(sesionJSON.getString("salida"));
                if (sesionJSON.has("precio") && !sesionJSON.isNull("precio"))
                    sesion.setPrecio(sesionJSON.getDouble("precio"));
                if (sesionJSON.has("tiempo") && !sesionJSON.isNull("tiempo"))
                    sesion.setTiempo(sesionJSON.getDouble("tiempo"));
                if (sesionJSON.has("descuento") && !sesionJSON.isNull("descuento"))
                    sesion.setDescuento(sesionJSON.getInt("descuento"));
                sesion.setDireccion(Direccion.fromJSON(sesionJSON, "direccion", "ciudad", "provincia", "pais", "codPostal"));

                lstSesiones.add(sesion);
            }
        } catch (Exception e) {
            Log.e("ERROR JSON_SESION", e.getMessage());
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
