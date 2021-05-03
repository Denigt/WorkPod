package com.example.workpod.data;

import org.json.JSONObject;

import java.util.List;

public class Reserva implements DataDb{
    private int id;
    private Usuario usuario;
    private Workpod workpod;

    @Override
    public DataDb JSONaData(JSONObject JSON) {
        return null;
    }

    @Override
    public List<Reserva> JSONaList(JSONObject JSON) {
        return null;
    }

    @Override
    public String getTabla() {
        return "reserva";
    }
}
