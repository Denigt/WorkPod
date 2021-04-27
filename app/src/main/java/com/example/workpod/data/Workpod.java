package com.example.workpod.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Workpod {
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

    public static List<Workpod> leerJSON(String textoJSON){
        ArrayList<Workpod> lstWorkpods = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(textoJSON);

            JSONArray lstWorkpodsJSON = json.getJSONArray("workpods");
            for (int i = 0; i < lstWorkpodsJSON.length(); i++){
                JSONObject workpodJSON = lstWorkpodsJSON.getJSONObject(i);

                lstWorkpods.add(new Workpod(Integer.parseInt(workpodJSON.get("id").toString()),
                        Double.parseDouble(workpodJSON.get("x").toString()),
                        Double.parseDouble(workpodJSON.get("y").toString())));
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
}
