package com.example.workpod.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Ubicacion implements DataDb{
    private int id;
    private String nombre;
    private double lat;
    private double lon;
    private List<Workpod> workpods;
    private Direccion direccion;

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public List<Workpod> getWorkpods() {
        return workpods;
    }

    public void setWorkpods(List<Workpod> workpods) {
        this.workpods = workpods;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Ubicacion() {
        this.id = 0;
        this.lat = 0;
        this.lon = 0;
        this.workpods = new LinkedList<>();
    }

    public Ubicacion(int id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.workpods = new LinkedList<>();
    }

    public Ubicacion(int id, String nombre, double lat, double lon, List<Workpod> workpods, Direccion direccion) {
        this.id = id;
        this.nombre = nombre;
        this.lat = lat;
        this.lon = lon;
        this.workpods = workpods;
        this.direccion = direccion;
    }

    public List<Ubicacion> JSONaList(JSONObject json){
        ArrayList<Ubicacion> lstUbicacion = new ArrayList<>();
        try {
            JSONArray lstWorkpodsJSON = json.getJSONArray("ubicacion");
            for (int i = 0; i < lstWorkpodsJSON.length(); i++){
                Ubicacion ubicacion = new Ubicacion();
                JSONObject ubicacionJSON = lstWorkpodsJSON.getJSONObject(i);

                if (ubicacionJSON.has("id") && !ubicacionJSON.isNull("id"))
                    ubicacion.setId(ubicacionJSON.getInt("id"));
                if (ubicacionJSON.has("nombre") && !ubicacionJSON.isNull("nombre"))
                    ubicacion.setNombre(ubicacionJSON.getString("nombre"));
                if (ubicacionJSON.has("lat") && !ubicacionJSON.isNull("lat"))
                    ubicacion.setLat(ubicacionJSON.getDouble("lat"));
                if (ubicacionJSON.has("lon") && !ubicacionJSON.isNull("lon"))
                    ubicacion.setLon(ubicacionJSON.getDouble("lon"));
                ubicacion.setWorkpods(new Workpod().JSONaList(ubicacionJSON));
                ubicacion.setDireccion(Direccion.fromJSON(ubicacionJSON, "direccion", "ciudad", "provincia", "pais", "codPostal"));

                lstUbicacion.add(ubicacion);
            }
        }catch(Exception e){
            Log.e("ERROR JSON_UBICACION", e.getMessage());
        }

        return lstUbicacion;
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
        return "ubicacion";
    }

    @Override
    public String getID() {
        return String.valueOf(id);
    }
}
