package com.example.workpod.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Usuario implements DataDb{
    private String email;
    private String nombre;
    private String apellidos;
    private String dni;
    private String password;
    private double tiempo;
    private Reserva reserva;
    private Tarjeta tarjeta;
    private  List<Sesion> sesiones;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

    public List<Sesion> getSesiones() {
        return sesiones;
    }

    public void setSesiones(List<Sesion> sesiones) {
        this.sesiones = sesiones;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Usuario() {
    }

    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Usuario(String email, String nombre, String apellidos, String dni, String password, double tiempo, Reserva reserva, Tarjeta tarjeta, List<Sesion> sesiones) {
        this.email = email;

        if (nombre != null) this.nombre = nombre;
        else this.nombre = "";

        if (apellidos != null) this.apellidos = apellidos;
        else this.apellidos = "";

        if (dni != null) this.dni = dni;
        else this.dni = "";

        this.password = password;

        this.tiempo = tiempo;

        if (reserva != null) this.reserva = reserva;
        else this.reserva = new Reserva();

        if (tarjeta != null) this.tarjeta = tarjeta;
        else this.tarjeta = new Tarjeta();

        if (sesiones != null) this.sesiones = sesiones;
        else this.sesiones = new LinkedList<>();
    }

    @Override
    public DataDb JSONaData(JSONObject json) {
        Usuario usuario = new Usuario();
        try {
            JSONObject usuarioJSON = json.getJSONObject("usuario");

            if (usuarioJSON.has("email") && !usuarioJSON.isNull("email"))
                usuario.setEmail(usuarioJSON.getString("email"));
            if (usuarioJSON.has("nombre") && !usuarioJSON.isNull("nombre"))
                usuario.setNombre(usuarioJSON.getString("nombre"));
            if (usuarioJSON.has("apellidos") && !usuarioJSON.isNull("apellidos"))
                usuario.setApellidos(usuarioJSON.getString("apellidos"));
            if (usuarioJSON.has("dni") && !usuarioJSON.isNull("dni"))
                usuario.setDni(usuarioJSON.getString("dni"));
        }catch(Exception e){
            Log.e("ERROR JSON_USUARIO", e.getMessage());
        }

        return usuario;
    }

    @Override
    public JSONObject dataAJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("nombre", nombre);
            json.put("contrasena", password);
            json.put("apellidos", apellidos);
            json.put("dni", dni);
        }catch(JSONException e){
            Log.e("ERROR USUARIO_JSON", e.getMessage());
        }

        return json;
    }

    @Override
    public List<Usuario> JSONaList(JSONObject json) {
        ArrayList<Usuario> lstUsuarios = new ArrayList<>();
        try {
            JSONArray lstUsuariosJSON = json.getJSONArray("usuario");
            for (int i = 0; i < lstUsuariosJSON.length(); i++){
                Usuario usuario = new Usuario();
                JSONObject usuarioJSON = lstUsuariosJSON.getJSONObject(i);

                if (usuarioJSON.has("email") && !usuarioJSON.isNull("email"))
                    usuario.setEmail(usuarioJSON.getString("email"));
                if (usuarioJSON.has("nombre") && !usuarioJSON.isNull("nombre"))
                    usuario.setNombre(usuarioJSON.getString("nombre"));
                if (usuarioJSON.has("apellidos") && !usuarioJSON.isNull("apellidos"))
                    usuario.setApellidos(usuarioJSON.getString("apellidos"));
                if (usuarioJSON.has("dni") && !usuarioJSON.isNull("dni"))
                    usuario.setDni(usuarioJSON.getString("dni"));

                lstUsuarios.add(usuario);
            }
        }catch(Exception e){
            Log.e("ERROR JSON_USUARIO", e.getMessage());
        }

        return lstUsuarios;
    }

    @Override
    public String getTabla() {
        return "usuario";
    }

    @Override
    public String getID() {
        return String.valueOf(email);
    }
}
