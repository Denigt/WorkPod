package com.workpodapp.workpod.data;

import android.util.Log;

import com.workpodapp.workpod.basic.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Campana implements DataDb{
    private int id;
    private String mascara;
    private String nombre;
    private String descripcion;
    private int descuento;
    private int consumoMin;
    private ZonedDateTime finCanjeo;
    private int tipo;

    public void set(Campana campana) {
        id = campana.getId();
        mascara = campana.getMascara();
        nombre = campana.getNombre();
        descripcion = campana.getDescripcion();
        descuento = campana.getDescuento();
        consumoMin = campana.getConsumoMin();
        finCanjeo = campana.getFinCanjeo();
        tipo = campana.getTipo();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    public int getConsumoMin() {
        return consumoMin;
    }

    public void setConsumoMin(int consumoMin) {
        this.consumoMin = consumoMin;
    }

    public ZonedDateTime getFinCanjeo() {
        return finCanjeo;
    }

    public void setFinCanjeo(ZonedDateTime finCanjeo) {
        this.finCanjeo = finCanjeo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Campana() {
        id = 0;
        mascara = "";
        nombre = "";
        descripcion = "";
        descuento = 0;
        consumoMin = 0;
        finCanjeo = ZonedDateTime.of(2000,01,01,0,0,0,0,ZonedDateTime.now().getZone());
        tipo = 0;
    }
    public Campana(int id, String mascara, String nombre, String descripcion, int descuento, int consumoMin, ZonedDateTime finCanjeo, int tipo) {
        setId(id);
        setMascara(mascara);
        setNombre(nombre);
        setDescripcion(descripcion);
        setDescuento(descuento);
        setConsumoMin(consumoMin);
        setFinCanjeo(finCanjeo);
        setTipo(tipo);
    }

    @Override
    public DataDb JSONaData(JSONObject json) {
        Campana campana = new Campana();
        try {
            JSONObject campanaJSON = json.getJSONObject("campana");

            if (campanaJSON.has("id") && !campanaJSON.isNull("id"))
                campana.setId(campanaJSON.getInt("id"));
            if (campanaJSON.has("mascara") && !campanaJSON.isNull("mascara"))
                campana.setMascara(campanaJSON.getString("mascara"));
            if (campanaJSON.has("nombre") && !campanaJSON.isNull("nombre"))
                campana.setNombre(campanaJSON.getString("nombre"));
            if (campanaJSON.has("descripcion") && !campanaJSON.isNull("descripcion"))
                campana.setDescripcion(campanaJSON.getString("descripcion"));
            if (campanaJSON.has("descuento") && !campanaJSON.isNull("descuento"))
                campana.setDescuento(campanaJSON.getInt("descuento"));
            if (campanaJSON.has("consumoMin") && !campanaJSON.isNull("consumoMin"))
                campana.setConsumoMin(campanaJSON.getInt("consumoMin"));
            if (campanaJSON.has("finCanjeo") && !campanaJSON.isNull("finCanjeo"))
                campana.setFinCanjeo(Method.stringToDate(campanaJSON.getString("finCanjeo"), ZoneId.of("UCT")).withZoneSameInstant(ZoneId.systemDefault()));
            if (campanaJSON.has("tipo") && !campanaJSON.isNull("tipo"))
                campana.setTipo(campanaJSON.getInt("tipo"));
        }catch(Exception e){
            Log.e("ERROR JSON_USUARIO", e.getMessage());
            campana = null;
        }

        return campana;
    }

    @Override
    public JSONObject dataAJSON() {
        JSONObject json = new JSONObject();
        try {
        }catch(Exception e){
            Log.e("ERROR USUARIO_JSON", e.getMessage());
        }

        return json;
    }

    @Override
    public List<Campana> JSONaList(JSONObject json) {
        ArrayList<Campana> lstCampanas = new ArrayList<>();
        try {
            JSONArray lstCampanasJSON = json.getJSONArray("campana");
            for (int i = 0; i < lstCampanasJSON.length(); i++){
                Campana campana = new Campana();
                JSONObject campanaJSON = lstCampanasJSON.getJSONObject(i);

                if (campanaJSON.has("id") && !campanaJSON.isNull("id"))
                    campana.setId(campanaJSON.getInt("id"));
                if (campanaJSON.has("mascara") && !campanaJSON.isNull("mascara"))
                    campana.setMascara(campanaJSON.getString("mascara"));
                if (campanaJSON.has("nombre") && !campanaJSON.isNull("nombre"))
                    campana.setNombre(campanaJSON.getString("nombre"));
                if (campanaJSON.has("descripcion") && !campanaJSON.isNull("descripcion"))
                    campana.setDescripcion(campanaJSON.getString("descripcion"));
                if (campanaJSON.has("descuento") && !campanaJSON.isNull("descuento"))
                    campana.setDescuento(campanaJSON.getInt("descuento"));
                if (campanaJSON.has("consumoMin") && !campanaJSON.isNull("consumoMin"))
                    campana.setConsumoMin(campanaJSON.getInt("consumoMin"));
                if (campanaJSON.has("finCanjeo") && !campanaJSON.isNull("finCanjeo"))
                    campana.setFinCanjeo(Method.stringToDate(campanaJSON.getString("finCanjeo"), ZoneId.of("UCT")).withZoneSameInstant(ZoneId.systemDefault()));
                if (campanaJSON.has("tipo") && !campanaJSON.isNull("tipo"))
                    campana.setTipo(campanaJSON.getInt("tipo"));

                lstCampanas.add(campana);
            }
        }catch(Exception e){
            Log.e("ERROR JSON_USUARIO", e.getMessage());
        }

        return lstCampanas;
    }

    @Override
    public String getTabla() {
        return "campana";
    }

    @Override
    public String getID() {
        return String.valueOf(id);
    }

    @Override
    public void setID(String id) {
        this.id = Integer.parseInt(id);
    }
}
