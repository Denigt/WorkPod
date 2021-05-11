package com.example.workpod.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ubicacion implements DataDb{
    private int id;
    private String nombre;
    private double lat;
    private double lon;
    private List<Workpod> workpods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Ubicacion> JSONaList(JSONObject json){
        ArrayList<Ubicacion> lstUbicacion = new ArrayList<>();
        try {
            JSONArray lstWorkpodsJSON = json.getJSONArray("ubicacion");
            for (int i = 0; i < lstWorkpodsJSON.length(); i++){
                Ubicacion ubicacion = new Ubicacion();
                JSONObject ubicacionJSON = lstWorkpodsJSON.getJSONObject(i);

                ubicacion.setId(ubicacionJSON.getInt("id"));
                ubicacion.setLat(ubicacionJSON.getDouble("lat"));
                ubicacion.setLon(ubicacionJSON.getDouble("lon"));

                lstUbicacion.add(ubicacion);
            }
        }catch(Exception e){

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
