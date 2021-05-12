package com.example.workpod.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Direccion {
    private String direccion;
    private String ciudad;
    private String provincia;
    private String pais;
    private int codPostal;

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(int codPostal) {
        this.codPostal = codPostal;
    }

    /**
     * Crea una instancia de la clase Direccion a partir de un JSON y el nombre de los campos del mismo
     * @param json Objeto JSON del que obtener los datos
     * @param direccion nombre de campo que almacena la direccion
     * @param ciudad nombre de campo que almacena la ciudad
     * @param provincia nombre de campo que almacena la provincia
     * @param pais nombre de campo que almacena el pais
     * @param codPostal nombre de campo que almacena el codigo postal
     * @return Objeto de la clase direccion
     */
    public static Direccion fromJSON(JSONObject json, String direccion, String ciudad, String provincia, String pais, String codPostal) {
        Direccion dir = getInstance();

        try {
            if (json.has(direccion) && !json.isNull(direccion))
                dir.setDireccion(json.getString(direccion));
            if (json.has(ciudad) && !json.isNull(ciudad))
                dir.setCiudad(json.getString(ciudad));
            if (json.has(provincia) && !json.isNull(provincia))
                dir.setProvincia(json.getString(provincia));
            if (json.has(pais) && !json.isNull(pais))
                dir.setPais(json.getString(pais));
            if (json.has(codPostal) && !json.isNull(codPostal))
                dir.setCodPostal(json.getInt(codPostal));
        }catch (JSONException e){
            Log.e("ERROR JSON_DIRECCION", e.getMessage());
        }

        return dir;
    }

    /**
     * Crea una instancia de la clase Direccion
     * @param direccion da valor al campo direccion
     * @param ciudad da valor al campo ciudad
     * @param provincia da valor al campo provicia
     * @param pais da valor al campo pais
     * @param codPostal da valor al campo codPostal
     * @return Objeto de la clase direccion
     */
    public static Direccion getInstance(String direccion, String ciudad, String provincia, String pais, int codPostal) {
        return new Direccion(direccion, ciudad, provincia, pais, codPostal);
    }

    /**
     * Crea una instancia de la clase Direccion
     * @return Objeto de la clase direccion
     */
    public static Direccion getInstance() {
        return new Direccion("", "", "", "", 0);
    }
    
    private Direccion(String direccion, String ciudad, String provincia, String pais, int codPostal) {
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.pais = pais;
        this.codPostal = codPostal;
    }
}
