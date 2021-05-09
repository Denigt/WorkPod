package com.example.workpod.data;

import org.json.JSONObject;

import java.util.List;

public class Facturacion implements DataDb{
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
    public List<Facturacion> JSONaList(JSONObject JSON) {
        return null;
    }

    @Override
    public String getTabla() {
        return "dirFactutacion";
    }

    @Override
    public String getID() {
        return "";
    }
}
