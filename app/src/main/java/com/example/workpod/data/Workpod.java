package com.example.workpod.data;

import android.util.Log;

import com.example.workpod.basic.Method;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Workpod implements DataDb{
    private int id;
    private String nombre;
    private String descripcion;
    private int numUsuarios;
    private double precio;
    private boolean luz;
    private boolean mantenimiento;
    private Reserva reserva;
    private ZonedDateTime ultimoUso;
    private ZonedDateTime limpieza;
    private Ubicacion ubicacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNumUsuarios() {
        return numUsuarios;
    }

    public void setNumUsuarios(int numUsuarios) {
        this.numUsuarios = numUsuarios;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isLuz() {
        return luz;
    }

    public void setLuz(boolean luz) {
        this.luz = luz;
    }

    public void setLuz(int luz) {
        this.luz = (luz < 1);
    }

    public boolean isMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(boolean mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public void setMantenimiento(int mantenimiento) {
        this.mantenimiento = (mantenimiento < 1);
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public ZonedDateTime getUltimoUso() {
        return ultimoUso;
    }

    public void setUltimoUso(ZonedDateTime ultimoUso) {
        this.ultimoUso = ultimoUso;
    }

    public ZonedDateTime getLimpieza() {
        return limpieza;
    }

    public void setLimpieza(ZonedDateTime limpieza) {
        this.limpieza = limpieza;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Workpod> JSONaList(JSONObject json){
        ArrayList<Workpod> lstWorkpods = new ArrayList<>();
        try {
            JSONArray lstWorkpodsJSON = json.getJSONArray("workpod");
            for (int i = 0; i < lstWorkpodsJSON.length(); i++){
                Workpod workpod = new Workpod();
                JSONObject workpodJSON = lstWorkpodsJSON.getJSONObject(i);

                if (workpodJSON.has("id") && !workpodJSON.isNull("id"))
                    workpod.setId(workpodJSON.getInt("id"));
                if (workpodJSON.has("nombre") && !workpodJSON.isNull("nombre"))
                    workpod.setNombre(workpodJSON.getString("nombre"));
                if (workpodJSON.has("descripcion") && !workpodJSON.isNull("descripcion"))
                    workpod.setDescripcion(workpodJSON.getString("descripcion"));
                if (workpodJSON.has("usuarios") && !workpodJSON.isNull("usuarios"))
                    workpod.setNumUsuarios(workpodJSON.getInt("usuarios"));
                if (workpodJSON.has("precio") && !workpodJSON.isNull("precio"))
                    workpod.setPrecio(workpodJSON.getDouble("precio"));
                if (workpodJSON.has("mantenimiento") && !workpodJSON.isNull("mantenimiento"))
                    workpod.setMantenimiento(workpodJSON.getInt("mantenimiento"));
                if (workpodJSON.has("luz") && !workpodJSON.isNull("luz"))
                    workpod.setLuz(workpodJSON.getInt("luz"));
                if (workpodJSON.has("ultLimpieza") && !workpodJSON.isNull("ultLimpieza"))
                    workpod.setLimpieza(Method.stringToDate(workpodJSON.getString("ultLimpieza"), ZoneId.systemDefault()));
                if (workpodJSON.has("ultUso") && !workpodJSON.isNull("ultUso"))
                    workpod.setUltimoUso(Method.stringToDate(workpodJSON.getString("ultUso"), ZoneId.systemDefault()));

                lstWorkpods.add(workpod);
            }
        }catch(Exception e){
            Log.e("ERROR JSON_WORKPOD", e.getMessage());
        }

        return lstWorkpods;
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
    public String getTabla() {
        return "workpod";
    }

    @Override
    public String getID() {
        return String.valueOf(id);
    }

    public Workpod(int id, String descripcion, Reserva reserva, boolean mantenimiento) {
        this.id = id;
        this.descripcion = descripcion;
        this.reserva = reserva;
        this.mantenimiento = mantenimiento;
    }

    public Workpod(int id) {
        this.id = id;
    }

    public Workpod() {
    }

    public Workpod(int id, String nombre, String descripcion, int numUsuarios, double precio, boolean luz, boolean mantenimiento, Reserva reserva, ZonedDateTime ultimoUso, ZonedDateTime limpieza, Ubicacion ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.numUsuarios = numUsuarios;
        this.precio = precio;
        this.luz = luz;
        this.mantenimiento = mantenimiento;
        this.reserva = reserva;
        this.ultimoUso = ultimoUso;
        this.limpieza = limpieza;
        this.ubicacion=ubicacion;
    }
}
