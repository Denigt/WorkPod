package com.workpodapp.workpod.data;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;

import com.workpodapp.workpod.basic.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Instalacion implements DataDb {
    // Caducidad de la reserva expresada en minutos
    public static final int CADUCIDAD = 20;

    private String id;
    private String idfb;
    private int usuario;

    public void set(Instalacion reserva)  {
        try{
            id = reserva.getId();
            idfb = reserva.getIdfb();
            usuario = reserva.getUsuario();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdfb() {
        return idfb;
    }

    public void setIdfb(String id) {
        this.idfb = id;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public Instalacion(ContentResolver resolver) {
        this.id = Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);
        this.idfb = Method.getFBid();
        this.usuario = 0;
    }

    public Instalacion(ContentResolver resolver, int usuario) {
        this.id = Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);
        this.idfb = Method.getFBid();
        this.usuario = usuario;
    }

    @Override
    public JSONObject dataAJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("idfb", idfb);
            json.put("usuario", usuario);
        } catch (JSONException e) {
            Log.e("ERROR INSTALACION_JSON", e.getMessage());
        }

        return json;
    }

    @Override
    public DataDb JSONaData(JSONObject json) {
        return null;
    }

    @Override
    public List<Instalacion> JSONaList(JSONObject json) {
        return null;
    }

    @Override
    public String getTabla() {
        return "instalacion";
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }
}
