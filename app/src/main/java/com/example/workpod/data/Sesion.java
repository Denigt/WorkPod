package com.example.workpod.data;

import android.util.Log;

import com.example.workpod.basic.Database;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.basic.Method;

import org.json.JSONArray;
import org.json.JSONException;
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
    private int usuario;
    private int workpod;
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
        Usuario retorno = new Usuario();

        if(InfoApp.USER != null) {
            Database<Usuario> bd = new Database<>(Database.SELECTID, InfoApp.USER);
            bd.postRun(()->{
                retorno.set(bd.getDato());
            });
            bd.start();
            try {
                bd.join();
            }catch (InterruptedException e){
                Log.e("ERROR GET USUARIO", e.getMessage());
                return null;
            }
        } else return null;

        return retorno;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public Workpod getWorkpod() {
        Workpod retorno = new Workpod(workpod);

        Database<Workpod> bd = new Database<>(Database.SELECTID, retorno);
        bd.postRun(()->{
            retorno.set(bd.getDato());
        });
        bd.start();
        try {
            bd.join();
        }catch (InterruptedException e){
            Log.e("ERROR GET WORKPOD", e.getMessage());
            return null;
        }

        return retorno;
    }

    public void setWorkpod(int workpod) {
        this.workpod = workpod;
    }

    public Direccion getDireccion() {
        if (direccion == null || !direccion.isInicialized())
            direccion = getWorkpod().getUbicacion().getDireccion();

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
            json.put("id", id);
            json.put("entrada", Method.dateToString(entrada, ZoneId.systemDefault()));
            json.put("salida", Method.dateToString(salida, ZoneId.systemDefault()));
            json.put("tiempo", tiempo);
            json.put("precio", precio);
            json.put("descuento", descuento);
        }catch(JSONException e){
            Log.e("ERROR USUARIO_JSON", e.getMessage());
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
                if (sesionJSON.has("workpod") && !sesionJSON.isNull("workpod"))
                    sesion.setWorkpod(sesionJSON.getInt("workpod"));
                if (sesionJSON.has("usuario") && !sesionJSON.isNull("usuario"))
                    sesion.setWorkpod(sesionJSON.getInt("usuario"));
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
