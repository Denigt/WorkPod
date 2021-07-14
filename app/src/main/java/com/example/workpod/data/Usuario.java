package com.example.workpod.data;

import android.util.Log;

import com.example.workpod.basic.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Usuario implements DataDb{
    private int id;
    private String verificar;
    private ZonedDateTime fVerificacion;
    private ZonedDateTime fRegistro;
    private String email;
    private String nombre;
    private String apellidos;
    private String dni;
    private String password;
    private double tiempo;
    private Reserva reserva;
    private Tarjeta tarjeta;
    private  List<Sesion> sesiones;
    private  List<Facturacion> dirFacturacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void set(Usuario user) {
        id = user.getId();
        email = user.getEmail();
        nombre = user.getNombre();
        apellidos = user.getApellidos();
        dni = user.getDni();
        password = user.getPassword();
        tiempo = user.getId();
        reserva = user.getReserva();
        tarjeta = user.getTarjeta();
        sesiones = user.getSesiones();
        dirFacturacion = user.getDirFacturacion();
        verificar = user.getVerificar();
        fRegistro = user.getfRegistro();
        fVerificacion = user.getfVerificacion();
    }

    public String getVerificar() {
        return verificar;
    }

    public void setVerificar(String verificar) {
        this.verificar = verificar;
    }

    public ZonedDateTime getfVerificacion() {
        return fVerificacion;
    }

    public void setfVerificacion(ZonedDateTime fVerificacion) {
        this.fVerificacion = fVerificacion;
    }

    public ZonedDateTime getfRegistro() {
        return fRegistro;
    }

    public void setfRegistro(ZonedDateTime fRegistro) {
        this.fRegistro = fRegistro;
    }

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

    public List<Facturacion> getDirFacturacion() {
        return dirFacturacion;
    }

    public void setDirFacturacion(List<Facturacion> dirFacturacion) {
        this.dirFacturacion = dirFacturacion;
    }

    public Usuario() {
        this.email = "";
        this.nombre = "";
        this.apellidos = "";
        this.dni = "";
        this.password = "";
        this.tiempo = 0;
        this.reserva = null;
        this.tarjeta = null;
        this.sesiones = new LinkedList<>();
        this.dirFacturacion = new LinkedList<>();
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

        this.reserva = reserva;

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

            if (usuarioJSON.has("id") && !usuarioJSON.isNull("id"))
                usuario.setId(usuarioJSON.getInt("id"));
            if (usuarioJSON.has("verificar") && !usuarioJSON.isNull("verificar"))
                usuario.setVerificar(usuarioJSON.getString("verificar"));
            if (usuarioJSON.has("fRegistro") && !usuarioJSON.isNull("fRegistro"))
                usuario.setfRegistro(Method.stringToDate(usuarioJSON.getString("fRegistro"), ZoneId.of("UCT")).withZoneSameInstant(ZoneId.systemDefault()));
            if (usuarioJSON.has("fVerificacion") && !usuarioJSON.isNull("fVerificacion"))
                usuario.setfVerificacion(Method.stringToDate(usuarioJSON.getString("fVerificacion"), ZoneId.of("UCT")).withZoneSameInstant(ZoneId.systemDefault()));
            if (usuarioJSON.has("email") && !usuarioJSON.isNull("email"))
                usuario.setEmail(usuarioJSON.getString("email"));
            if (usuarioJSON.has("nombre") && !usuarioJSON.isNull("nombre"))
                usuario.setNombre(usuarioJSON.getString("nombre"));
            if (usuarioJSON.has("apellidos") && !usuarioJSON.isNull("apellidos"))
                usuario.setApellidos(usuarioJSON.getString("apellidos"));
            if (usuarioJSON.has("dni") && !usuarioJSON.isNull("dni"))
                usuario.setDni(usuarioJSON.getString("dni"));
            if (usuarioJSON.has("contrasena") && !usuarioJSON.isNull("contrasena"))
                usuario.setPassword(usuarioJSON.getString("contrasena"));
            if (usuarioJSON.has("reserva") && !usuarioJSON.isNull("reserva"))
                usuario.setReserva((Reserva)new Reserva().JSONaData(usuarioJSON));
            else usuario.setReserva(null);
            if (usuarioJSON.has("facturacion") && !usuarioJSON.isNull("facturacion"))
                usuario.setDirFacturacion(new Facturacion().JSONaList(usuarioJSON));
            else usuario.setDirFacturacion(null);
        }catch(Exception e){
            Log.e("ERROR JSON_USUARIO", e.getMessage());
        }

        return usuario;
    }

    @Override
    public JSONObject dataAJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("verificar", email);
            json.put("fRegistro", Method.dateToString(fRegistro, ZoneId.of("UCT")));
            json.put("fVerificacion", Method.dateToString(fVerificacion, ZoneId.of("UCT")));
            json.put("email", email);
            json.put("nombre", nombre);
            json.put("contrasena", password);
            json.put("apellidos", apellidos);
            json.put("dni", dni);
            if (reserva != null)
                json.put("reserva", reserva);
            if (tarjeta != null)
                json.put("tarjeta", tarjeta.getID());
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

                if (usuarioJSON.has("id") && !usuarioJSON.isNull("id"))
                    usuario.setId(usuarioJSON.getInt("id"));
                if (usuarioJSON.has("email") && !usuarioJSON.isNull("email"))
                    usuario.setEmail(usuarioJSON.getString("email"));
                if (usuarioJSON.has("nombre") && !usuarioJSON.isNull("nombre"))
                    usuario.setNombre(usuarioJSON.getString("nombre"));
                if (usuarioJSON.has("apellidos") && !usuarioJSON.isNull("apellidos"))
                    usuario.setApellidos(usuarioJSON.getString("apellidos"));
                if (usuarioJSON.has("dni") && !usuarioJSON.isNull("dni"))
                    usuario.setDni(usuarioJSON.getString("dni"));
                if (usuarioJSON.has("contrasena") && !usuarioJSON.isNull("contrasena"))
                    usuario.setPassword(usuarioJSON.getString("contrasena"));
                if (usuarioJSON.has("reserva") && !usuarioJSON.isNull("reserva"))
                    usuario.setReserva((Reserva)new Reserva().JSONaData(usuarioJSON));
                else usuario.setReserva(null);

                if (usuarioJSON.has("facturacion") && !usuarioJSON.isNull("facturacion"))
                    usuario.setDirFacturacion(new Facturacion().JSONaList(usuarioJSON));
                else usuario.setDirFacturacion(null);
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

    @Override
    public void setID(String id) {
        this.id = Integer.parseInt(id);
    }
}
