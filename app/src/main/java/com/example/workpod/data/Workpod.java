package com.example.workpod.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Workpod implements DataDb{
    private int id;
    private String descripcion;
    private double lat;
    private double lon;
    private Reserva reserva;
    private boolean mantenimiento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double x) {
        this.lat = x;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double y) {
        this.lon = y;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public boolean isMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(boolean mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public List<Workpod> JSONaList(JSONObject json){
        ArrayList<Workpod> lstWorkpods = new ArrayList<>();
        try {
            JSONArray lstWorkpodsJSON = json.getJSONArray("workpod");
            for (int i = 0; i < lstWorkpodsJSON.length(); i++){
                Workpod workpod = new Workpod();
                JSONObject workpodJSON = lstWorkpodsJSON.getJSONObject(i);

                workpod.setId(workpodJSON.getInt("id"));
                workpod.setLat(workpodJSON.getDouble("lat"));
                workpod.setLon(workpodJSON.getDouble("lon"));

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
    public String getTabla() {
        return "workpod";
    }

    public Workpod(int id, String descripcion, double x, double y, Reserva reserva, boolean mantenimiento) {
        this.id = id;
        this.descripcion = descripcion;
        this.lat = x;
        this.lon = y;
        this.reserva = reserva;
        this.mantenimiento = mantenimiento;
    }

    public Workpod(int id, double x, double y) {
        this.id = id;
        this.lat = x;
        this.lon = y;
    }

    public Workpod() {
    }
}
