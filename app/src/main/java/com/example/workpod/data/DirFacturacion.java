package com.example.workpod.data;

import org.json.JSONObject;

import java.util.List;

public class DirFacturacion implements DataDb{
    @Override
    public DataDb JSONaData(JSONObject JSON) {
        return null;
    }

    @Override
    public List<DirFacturacion> JSONaList(JSONObject JSON) {
        return null;
    }

    @Override
    public String getTabla() {
        return "dirFactutacion";
    }
}
