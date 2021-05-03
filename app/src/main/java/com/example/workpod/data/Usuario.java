package com.example.workpod.data;

import org.json.JSONObject;

import java.util.List;

public class Usuario implements DataDb{
    private String email;
    private String nombre;
    private String apellidos;
    private String password;
    private double tiempo;
    private Reserva reserva;
    private List<Tarjeta> tarjetas;
    private  List<Sesion> sesiones;

    @Override
    public DataDb JSONaData(JSONObject JSON) {
        return null;
    }

    @Override
    public List<Usuario> JSONaList(JSONObject JSON) {
        return null;
    }

    @Override
    public String getTabla() {
        return "usuario";
    }
}
