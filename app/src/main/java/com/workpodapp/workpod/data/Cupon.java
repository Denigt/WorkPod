package com.workpodapp.workpod.data;

import android.util.Log;

import com.workpodapp.workpod.basic.Method;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cupon implements DataDb {
    private int id;
    private String codigo;
    private boolean canjeado;
    private ZonedDateTime fInsertado;
    private ZonedDateTime fCanjeado;
    private ZonedDateTime fCaducidad;
    private Campana campana;
    private int usuario;

    public void set(Cupon cupon) {
        id = cupon.getId();
        codigo = cupon.getCodigo();
        canjeado = cupon.isCanjeado();
        fInsertado = cupon.getfInsertado();
        fCanjeado = cupon.getfCanjeado();
        fCaducidad = cupon.getfCaducidad();
        campana = cupon.getCampana();
        usuario = cupon.getUsuario();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public boolean isCanjeado() {
        return canjeado;
    }

    public void setCanjeado(boolean canjeado) {
        this.canjeado = canjeado;
    }

    public void setCanjeado(int canjeado) {
        this.canjeado = canjeado == 1;
    }

    public ZonedDateTime getfInsertado() {
        return fInsertado;
    }

    public void setfInsertado(ZonedDateTime fInsertado) {
        this.fInsertado = fInsertado;
    }

    public ZonedDateTime getfCanjeado() {
        return fCanjeado;
    }

    public void setfCanjeado(ZonedDateTime fCanjeado) {
        this.fCanjeado = fCanjeado;
    }

    public ZonedDateTime getfCaducidad() {
        return fCaducidad;
    }

    public void setfCaducidad(ZonedDateTime fCaducidad) {
        this.fCaducidad = fCaducidad;
    }

    public Campana getCampana() {
        return campana;
    }

    public void setCampana(Campana campana) {
        this.campana = campana;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public Cupon() {
        id = 0;
        codigo = "";
        canjeado = false;
        fInsertado = ZonedDateTime.now();
        fCanjeado = null;
        fCaducidad = ZonedDateTime.of(2000,01,01,0,0,0,0,ZonedDateTime.now().getZone());
        campana = new Campana();
        usuario = 0;
    }

    public Cupon(int id, String codigo, boolean canjeado, ZonedDateTime fInsertado, ZonedDateTime fCanjeado, ZonedDateTime fCaducidad, Campana campana, int usuario) {
        setId(id);
        setCodigo(codigo);
        setCanjeado(canjeado);
        setfInsertado(fInsertado);
        setfCanjeado(fCanjeado);
        setfCaducidad(fCaducidad);
        setCampana(campana);
        setUsuario(usuario);
    }

    @Override
    public DataDb JSONaData(JSONObject json) {
        Cupon cupon = new Cupon();
        try {
            JSONObject cuponJSON = json.getJSONObject("cupon");

            if (cuponJSON.has("id") && !cuponJSON.isNull("id"))
                cupon.setId(cuponJSON.getInt("id"));
            if (cuponJSON.has("codigo") && !cuponJSON.isNull("codigo"))
                cupon.setCodigo(cuponJSON.getString("codigo"));
            if (cuponJSON.has("canjeado") && !cuponJSON.isNull("canjeado"))
                cupon.setCanjeado(cuponJSON.getInt("canjeado"));//error
            if (cuponJSON.has("fInsertado") && !cuponJSON.isNull("fInsertado"))
                cupon.setfInsertado(Method.stringToDate(cuponJSON.getString("fInsertado"), ZoneId.of("UCT")).withZoneSameInstant(ZoneId.systemDefault()));
            if (cuponJSON.has("fCanjeado") && !cuponJSON.isNull("fCanjeado"))
                cupon.setfCanjeado(Method.stringToDate(cuponJSON.getString("fCanjeado"), ZoneId.of("UCT")).withZoneSameInstant(ZoneId.systemDefault()));
            if (cuponJSON.has("fCaducidad") && !cuponJSON.isNull("fCaducidad"))
                cupon.setfCaducidad(Method.stringToDate(cuponJSON.getString("fCaducidad"), ZoneId.of("UCT")).withZoneSameInstant(ZoneId.systemDefault()));
        /*    if (cuponJSON.has("campana") && !cuponJSON.isNull("campana"))
                cupon.setCampana(cuponJSON.getInt("campana"));*/
            if (cuponJSON.has("usuario") && !cuponJSON.isNull("usuario"))
                cupon.setUsuario(cuponJSON.getInt("usuario"));
            if (cuponJSON.has("campana") && !cuponJSON.isNull("campana"))
                cupon.setCampana((Campana) new Campana().JSONaData(cuponJSON));
            else cupon.setCampana(null);
        } catch (Exception e) {
            Log.e("ERROR JSON_USUARIO", e.getMessage());
            cupon = null;
        }

        return cupon;
    }

    @Override
    public JSONObject dataAJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("codigo", codigo);
            json.put("canjeado", canjeado);
            json.put("fInsertado", Method.dateToString(fInsertado, ZoneId.of("UCT")));
            json.put("fCanjeado", Method.dateToString(fCanjeado, ZoneId.of("UCT")));
            json.put("fCaducidad", Method.dateToString(fCaducidad, ZoneId.of("UCT")));
            json.put("campana", campana);
            json.put("usuario", usuario);
        } catch (Exception e) {
            Log.e("ERROR USUARIO_JSON", e.getMessage());
        }

        return json;
    }

    @Override
    public List<Cupon> JSONaList(JSONObject json) {
        ArrayList<Cupon> lstCupones = new ArrayList<>();
        try {
            JSONArray lstCuponesJSON = json.getJSONArray("cupon");
            for (int i = 0; i < lstCuponesJSON.length(); i++) {
                Cupon cupon = new Cupon();
                JSONObject cuponJSON = lstCuponesJSON.getJSONObject(i);

                if (cuponJSON.has("id") && !cuponJSON.isNull("id"))
                    cupon.setId(cuponJSON.getInt("id"));
                if (cuponJSON.has("codigo") && !cuponJSON.isNull("codigo"))
                    cupon.setCodigo(cuponJSON.getString("codigo"));
                if (cuponJSON.has("canjeado") && !cuponJSON.isNull("canjeado"))
                    cupon.setCanjeado(cuponJSON.getInt("canjeado"));//error
                if (cuponJSON.has("fInsertado") && !cuponJSON.isNull("fInsertado"))
                    cupon.setfInsertado(Method.stringToDate(cuponJSON.getString("fInsertado"), ZoneId.of("UCT")).withZoneSameInstant(ZoneId.systemDefault()));
                if (cuponJSON.has("fCanjeado") && !cuponJSON.isNull("fCanjeado"))
                    cupon.setfCanjeado(Method.stringToDate(cuponJSON.getString("fCanjeado"), ZoneId.of("UCT")).withZoneSameInstant(ZoneId.systemDefault()));
                if (cuponJSON.has("fCaducidad") && !cuponJSON.isNull("fCaducidad"))
                    cupon.setfCaducidad(Method.stringToDate(cuponJSON.getString("fCaducidad"), ZoneId.of("UCT")).withZoneSameInstant(ZoneId.systemDefault()));
              /*  if (cuponJSON.has("campana") && !cuponJSON.isNull("campana"))
                    cupon.setCampana(cuponJSON.getInt("campana"));*/
                if (cuponJSON.has("usuario") && !cuponJSON.isNull("usuario"))
                    cupon.setUsuario(cuponJSON.getInt("usuario"));
                if (cuponJSON.has("campana") && !cuponJSON.isNull("campana"))
                    cupon.setCampana((Campana) new Campana().JSONaData(cuponJSON));
                else cupon.setCampana(null);

                lstCupones.add(cupon);
            }
        } catch (Exception e) {
            Log.e("ERROR JSON_USUARIO", e.getMessage());
        }

        return lstCupones;
    }

    @Override
    public String getTabla() {
        return "cupon";
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
