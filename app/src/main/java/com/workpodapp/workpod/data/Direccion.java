package com.workpodapp.workpod.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

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
     * Crea un JSON a partir de una direccion. el JSON tiene los campos direccion, ciudad, provincia, pais y codPostal
     * @param dir Direccion a partir de la que crear el JSON
     * @return JSON
     */
    public static JSONObject toJSON(Direccion dir) {
        JSONObject json = new JSONObject();
        try {
            json.put("direccion", dir.direccion);
            json.put("ciudad", dir.ciudad);
            json.put("provincia", dir.provincia);
            json.put("pais", dir.pais);
            json.put("codPostal", dir.codPostal);
        }catch (JSONException e){
            Log.e("ERROR DIRECCION_JSON", e.getMessage());
        }

        return json;
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

    public String toShortString() {
        return String.format("%s, %s", direccion, ciudad);
    }

    @Override
    public String toString() {
        return String.format("%s, %s | %s %d", direccion, ciudad, provincia, codPostal);
    }

    public String toLongString() {
        return String.format("%s, %s | %s %d (%s)", direccion, ciudad, provincia, codPostal, pais);
    }

    public boolean isInicialized(){
        return !this.equals(getInstance());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Direccion)) return false;
        Direccion direccion1 = (Direccion) o;
        return codPostal == direccion1.codPostal &&
                Objects.equals(direccion, direccion1.direccion) &&
                Objects.equals(ciudad, direccion1.ciudad) &&
                Objects.equals(provincia, direccion1.provincia) &&
                Objects.equals(pais, direccion1.pais);
    }

    @Override
    public int hashCode() {
        return Objects.hash(direccion, ciudad, provincia, pais, codPostal);
    }
}
