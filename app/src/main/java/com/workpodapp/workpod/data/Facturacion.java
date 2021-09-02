package com.workpodapp.workpod.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Facturacion implements DataDb{
    private int id;
    private Direccion facturacion;
    private Direccion postal;
    private String nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Direccion getFacturacion() {
        return facturacion;
    }

    public void setFacturacion(Direccion facturacion) {
        this.facturacion = facturacion;
    }

    public Direccion getPostal() {
        return postal;
    }

    public void setPostal(Direccion postal) {
        this.postal = postal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public DataDb JSONaData(JSONObject json) {
        Facturacion factura = new Facturacion();
        try {
            JSONObject facturaJSON = json.getJSONObject("facturacion");

            if (facturaJSON.has("id") && !facturaJSON.isNull("id"))
                factura.setId(facturaJSON.getInt("id"));
            if (facturaJSON.has("nombre") && !facturaJSON.isNull("nombre"))
                factura.setNombre(facturaJSON.getString("nombre"));

            factura.setFacturacion(Direccion.fromJSON(facturaJSON, "direccionFac", "ciudadFac", "provinciaFac", "paisFac", "codPostalFac"));
            factura.setPostal(Direccion.fromJSON(facturaJSON, "direccionPos", "ciudadPos", "provinciaPos", "paisPos", "codPostal"));

        }catch(Exception e){
            Log.e("ERROR JSON_FACTURACION", e.getMessage());
        }

        return factura;
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
    public List<Facturacion> JSONaList(JSONObject json) {
        ArrayList<Facturacion> lstFactura = new ArrayList<>();
        try {
            JSONArray lstFacturasJSON = json.getJSONArray("facturacion");
            for (int i = 0; i < lstFacturasJSON.length(); i++){
                Facturacion factura = new Facturacion();
                JSONObject facturaJSON = lstFacturasJSON.getJSONObject(i);

                if (facturaJSON.has("id") && !facturaJSON.isNull("id"))
                    factura.setId(facturaJSON.getInt("id"));
                if (facturaJSON.has("nombre") && !facturaJSON.isNull("nombre"))
                    factura.setNombre(facturaJSON.getString("nombre"));

                factura.setFacturacion(Direccion.fromJSON(facturaJSON, "direccionFac", "ciudadFac", "provinciaFac", "paisFac", "codPostalFac"));
                factura.setPostal(Direccion.fromJSON(facturaJSON, "direccionPos", "ciudadPos", "provinciaPos", "paisPos", "codPostal"));

                lstFactura.add(factura);
            }
        }catch(Exception e){
            Log.e("ERROR JSON_FACTURACION", e.getMessage());
        }

        return lstFactura;
    }

    @Override
    public String getTabla() {
        return "dirFactutacion";
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
