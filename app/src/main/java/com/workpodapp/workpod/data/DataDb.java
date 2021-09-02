package com.workpodapp.workpod.data;

import org.json.JSONObject;

import java.util.List;

/**
 * Interfaz que ha de implementar toda clase que represente a un dato de la base de datos
 *
 * Implementa un metodo que permite crear un objeto DataDv a partir de un JSON
 */
public interface DataDb {
    /**
     * Crea un objeto DataDb a partir del JSON
     * El JSON ha de tener un array llamado igual que la tabla a la que referencia el dato
     * @param JSON Objeto JSON desde el que obtener los datos
     * @return Objeto de la Interfaz con los datos que aporta el JSON, null en caso de error
     */
    public DataDb JSONaData(JSONObject JSON);

    /**
     * Crea un objeto JSON a partir del objeto
     * Los campos de JSON han de tener los nombres de las variables de las tablas si se quieren enviar a un PHP
     * @return JSON con cada uno de los datos del objeto
     */
    public JSONObject dataAJSON();

    /**
     * Crea una Lista con los objetos contenidos en el JSON
     * El JSON ha de tener un array llamado igual que la tabla a la que referencia el dato
     * @param JSON Objeto JSON desde el que obtener los datos
     * @return Lista de objetos de la Interfaz con los datos que aporta el JSON, null en caso de error
     */
    public List<? extends DataDb> JSONaList(JSONObject JSON);

    /**
     * Retorna el nombre de la tabla asociada a ese tipo de dato
     * @return Nombre de la tabla
     */
    public String getTabla();

    /**
     * Retorna el id de la clase transformado a String
     * @return ID de la clase
     */
    public String getID();



    /**
     * Establece el id del objeto
     * @param id Id del objeto
     */
    public void setID(String id);
}
