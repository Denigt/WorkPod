package com.example.workpod.data;

import org.json.JSONObject;

import java.util.List;

public class Facturacion implements DataDb{
    @Override
    public DataDb JSONaData(JSONObject JSON) {
        return null;
    }

    @Override
    public List<Facturacion> JSONaList(JSONObject JSON) {
        return null;
    }

    @Override
    public String getTabla() {
        return "dirFactutacion";
    }
}
