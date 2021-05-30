package com.example.workpod.data;

import android.util.Log;

import com.example.workpod.basic.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reserva implements DataDb{
    private int id;
    private int usuario;
    private int workpod;
    private ZonedDateTime fecha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public int getWorkpod() {
        return workpod;
    }

    public void setWorkpod(int workpod) {
        this.workpod = workpod;
    }

    public ZonedDateTime getFecha() {
        return fecha;
    }

    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public JSONObject dataAJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("fecha", Method.dateToString(fecha, ZoneId.systemDefault()));
            json.put("usuario", usuario);
            json.put("workpod", workpod);
        }catch(JSONException e){
            Log.e("ERROR RESERVA_JSON", e.getMessage());
        }

        return json;
    }

    @Override
    public DataDb JSONaData(JSONObject json) {
        Reserva reserva = new Reserva();
        try {
            JSONObject reservaJSON = json.getJSONObject("reserva");

                if (reservaJSON.has("id") && !reservaJSON.isNull("id"))
                    reserva.setId(reservaJSON.getInt("id"));
                if (reservaJSON.has("fecha") && !reservaJSON.isNull("fecha"))
                    reserva.setFecha(Method.stringToDate(reservaJSON.getString("fecha"), ZoneId.systemDefault()));
                if (reservaJSON.has("usuario") && !reservaJSON.isNull("usuario"))
                    reserva.setUsuario(reservaJSON.getInt("usuario"));
                if (reservaJSON.has("workpod") && !reservaJSON.isNull("workpod"))
                    reserva.setWorkpod(reservaJSON.getInt("workpod"));

        } catch (Exception e) {
            Log.e("ERROR JSON_RESERVA", e.getMessage());
        }

        return reserva;
    }

    @Override
    public List<Reserva> JSONaList(JSONObject json) {
        ArrayList<Reserva> lstReservas = new ArrayList<>();
        try {
            JSONArray lstReservasJSON = json.getJSONArray("reserva");
            for (int i = 0; i < lstReservasJSON.length(); i++) {
                Reserva reserva = new Reserva();
                JSONObject reservaJSON = lstReservasJSON.getJSONObject(i);

                if (reservaJSON.has("id") && !reservaJSON.isNull("id"))
                    reserva.setId(reservaJSON.getInt("id"));
                if (reservaJSON.has("fecha") && !reservaJSON.isNull("fecha"))
                    reserva.setFecha(Method.stringToDate(reservaJSON.getString("fecha"), ZoneId.systemDefault()));
                if (reservaJSON.has("usuario") && !reservaJSON.isNull("usuario"))
                    reserva.setUsuario(reservaJSON.getInt("usuario"));
                if (reservaJSON.has("workpod") && !reservaJSON.isNull("workpod"))
                    reserva.setWorkpod(reservaJSON.getInt("workpod"));

                lstReservas.add(reserva);
            }
        } catch (Exception e) {
            Log.e("ERROR JSON_RESERVA", e.getMessage());
        }

        return lstReservas;
    }

    @Override
    public String getTabla() {
        return "reserva";
    }

    @Override
    public String getID() {
        return String.valueOf(id);
    }
}
