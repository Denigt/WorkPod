package com.example.workpod.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sesion implements DataDb{
    private int id;
    private ZonedDateTime entrada;
    private ZonedDateTime salida;
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

    public ZonedDateTime getEntrada() {
        return entrada;
    }

    public void setEntrada(ZonedDateTime entrada) {
        this.entrada = entrada;
    }

    public void setEntrada(String entrada) {
        String[] aux = entrada.split(" ");
        String[] diaS = null;
        String[] horaS = null;
        if (aux.length == 1){
            diaS = aux[0].split("-");
            horaS = aux[1].split(":");
        }
        if (aux.length > 1){
            diaS = aux[0].split("-");
        }
        try{
            if (diaS != null && horaS != null && diaS.length > 2 && horaS.length > 2)
                this.entrada = ZonedDateTime.of(Integer.parseInt(diaS[0]), Integer.parseInt(diaS[1]), Integer.parseInt(diaS[2]),
                    Integer.parseInt(horaS[0]), Integer.parseInt(horaS[1]), Integer.parseInt(horaS[2]), 0, ZoneId.systemDefault());
            else if (diaS != null && diaS.length > 2 )
                this.entrada = ZonedDateTime.of(Integer.parseInt(diaS[0]), Integer.parseInt(diaS[1]), Integer.parseInt(diaS[2]),
                        0, 0, 0, 0, ZoneId.systemDefault());
        }catch (NumberFormatException e){

        }
    }

    public ZonedDateTime getSalida() {
        return salida;
    }

    public void setSalida(ZonedDateTime salida) {
        this.salida = salida;
    }

    public void setSalida(String salida) {
        String[] aux = salida.split(" ");
        String[] diaS = null;
        String[] horaS = null;
        if (aux.length > 1){
            diaS = aux[0].split("-");
            horaS = aux[1].split(":");
        }
        else{
            diaS = aux[0].split("-");
        }
        try{
            if (diaS != null && horaS != null && diaS.length > 2 && horaS.length > 2)
                this.entrada = ZonedDateTime.of(Integer.parseInt(diaS[0]), Integer.parseInt(diaS[1]), Integer.parseInt(diaS[2]),
                        Integer.parseInt(horaS[0]), Integer.parseInt(horaS[1]), Integer.parseInt(horaS[2]), 0, ZoneId.systemDefault());
            else if (diaS != null && diaS.length > 2 )
                this.entrada = ZonedDateTime.of(Integer.parseInt(diaS[0]), Integer.parseInt(diaS[1]), Integer.parseInt(diaS[2]),
                        0, 0, 0, 0, ZoneId.systemDefault());
        }catch (NumberFormatException e){

        }
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
    public JSONObject dataAJSON() {
        JSONObject json = new JSONObject();
        try {
        }catch(Exception e){

        }

        return json;
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
                sesion.setEntrada(sesionJSON.getString("entrada"));
                sesion.setSalida(sesionJSON.getString("salida"));
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
