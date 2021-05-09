package com.example.workpod.data;

import org.json.JSONObject;

import java.sql.Date;
import java.util.List;

public class Tarjeta implements DataDb{
    private int numero;
    private Date caducidad;
    private int clave;
    private String titular;
    private Usuario usuario;

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
    public List<Tarjeta> JSONaList(JSONObject JSON) {
        return null;
    }

    @Override
    public String getTabla() {
        return "tarjeta";
    }

    @Override
    public String getID() {
        return String.valueOf(numero);
    }
}
