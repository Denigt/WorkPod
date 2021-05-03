package com.example.workpod.data;

import org.json.JSONObject;

import java.sql.Date;
import java.util.List;

public class Sesion implements DataDb{
    private int id;
    private Date entrada;
    private Date salida;
    private double precio;
    private Usuario usuario;
    private Workpod workpod;

    @Override
    public DataDb JSONaData(JSONObject JSON) {
        return null;
    }

    @Override
    public List<Sesion> JSONaList(JSONObject JSON) {
        return null;
    }

    @Override
    public String getTabla() {
        return "sesion";
    }
}
