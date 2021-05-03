package com.example.workpod.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Workpod implements DataDb{
    private int id;
    private String ubicacion;
    private double x;
    private double y;
    private Reserva reserva;
    private boolean mantenimiento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
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
                workpod.setX(workpodJSON.getDouble("x"));
                workpod.setY(workpodJSON.getDouble("y"));

                lstWorkpods.add(workpod);
            }
        }catch(Exception e){

        }

        return lstWorkpods;
    }

    public Workpod(int id, String ubicacion, double x, double y, Reserva reserva, boolean mantenimiento) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.x = x;
        this.y = y;
        this.reserva = reserva;
        this.mantenimiento = mantenimiento;
    }

    public Workpod(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Workpod() {
    }

    @Override
    public DataDb JSONaData(JSONObject JSON) {
        return null;
    }

    @Override
    public String getTabla() {
        return "workpod";
    }
}
