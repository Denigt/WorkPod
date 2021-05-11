package com.example.workpod.data;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private Reserva limpieza;

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

    public boolean isMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(boolean mantenimiento) {
        this.mantenimiento = mantenimiento;
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

    public Reserva getLimpieza() {
        return limpieza;
    }

    public void setLimpieza(Reserva limpieza) {
        this.limpieza = limpieza;
    }

    public List<Workpod> JSONaList(JSONObject json){
        ArrayList<Workpod> lstWorkpods = new ArrayList<>();
        try {
            JSONArray lstWorkpodsJSON = json.getJSONArray("workpod");
            for (int i = 0; i < lstWorkpodsJSON.length(); i++){
                Workpod workpod = new Workpod();
                JSONObject workpodJSON = lstWorkpodsJSON.getJSONObject(i);

                workpod.setId(workpodJSON.getInt("id"));
                workpod.setNombre(workpodJSON.getString("nombre"));
                workpod.setDescripcion(workpodJSON.getString("descripcion"));
                workpod.setNumUsuarios(workpodJSON.getInt("usuarios"));
                workpod.setPrecio(workpodJSON.getDouble("precio"));
                workpod.setMantenimiento(workpodJSON.getBoolean("mantenimiento"));
                workpod.setLuz(workpodJSON.getBoolean("luz"));

                lstWorkpods.add(workpod);
            }
        }catch(Exception e){

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
}
