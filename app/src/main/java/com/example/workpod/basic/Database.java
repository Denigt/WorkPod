package com.example.workpod.basic;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.workpod.data.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase para realizar consultas a la base de datos
 * Se ha de ejecutar en un hilo distinto al de la UI debido a la politica de seguridad de Android
 *
 * @param <T> Tabla en la que se han de realizar las consultas
 */
public class Database<T extends DataDb> extends Thread {

    /**
     * Almacena los nombres de las tablas de las bases de datos
     */
    public final static List<String> TABLAS;

    /**
     * Indica la direccion en la que se encuentra el servidor con los PHP para acceder a la base de datos
     */
    public static final String URL_SERVIDOR = "https://dev.workpod.app";

//--TIPOS DE CONSULTA------------------------
    public static final int SELECTALL = 0;
    public static final int SELECT = 1;
    public static final int INSERT = 2;
    public static final int UPDATE = 3;
    public static final int DELETE = 4;
//-------------------------------------------
    /**
     * Consulta que se va a realizar
     */
    private int tipoConsulta;

    /**
     * Dato a buscar en un select, insertar, actualizar o eliminar
     * En el caso del select este dato ha de tener solo el id, el objeto se rellena tras buscar la informacion
     */
    private T dato;

    /**
     * Objeto con los datos modificados para actualizar el antiguo
     */
    private T datoUpdate;

    /**
     * Datos recuperados de un selectAll
     */
    private List<T> lstSelect;

    /**
     * Indica si se ha terminado de ejecutar la consulta
     */
    private boolean finish;

    /**
     * Indica el tipo de base de datos que se esta utilizando
     *  0 MySQL
     *  1 My SQL Azure
     * -1 SQLite (SOLO PRUEBAS)
     */
    private int tipoDB;


    // CODIGO A EJECUTAR TRAS LA EJECUCION DEL RUN
    /**
     * Runnable que se ejecuta en un hilo separado tras la ejecucion de la consulta
     */
    private Runnable postRun;
    /**
     * Runnable que se ejecuta tras la consulta en el hilo de la interfaz, necesita que se ke pase la Activity principal
     */
    private Runnable postRunOnUI;
    /**
     * Activity necesaria para que se pueda ejecutar el Runnable posterior a la consulta en la interfaz
     */
    private Activity activity;

    /**
     * Inicializar variables estaticas
     */
    static{
        TABLAS = new LinkedList();
        TABLAS.add("workpod");
        TABLAS.add("usuario");
    }

    // HILOS A EJECUTAR
    @Override
    public void run() {
        finish = false;
        switch (tipoConsulta){
            case SELECTALL:
                lstSelect = select(dato);
                break;
        }
        // INDICAR FINALIZACION DE LA CONSULTA
        finish = true;

        // EJECUTAR CODIGO POSTCONSULTA
        try {
            if (postRun != null) {
                Thread posConsulta = new Thread(postRun);
                posConsulta.start();
                posConsulta.join();
            }

            if (activity != null && postRunOnUI != null)
                activity.runOnUiThread(postRunOnUI);
        }catch (Exception e) {
            System.err.println("Error al ejecutar los metodos postConsulta\n" +
                    "com.example.workpod -> basic -> Database.class metodo run");
            e.printStackTrace();
        }
    }

    /**
     * Se ejecuta tras la realizacion de una consulta en otro hilo
     * @param method Runnable a ejecutar
     */
    public void postRun(Runnable method){
        postRun = method;
    }

    /**
     * Se ejecuta tras la realizacion de una consulta la interfaz de la activity
     * @param method Runnable a ejecutar
     */
    public void postRunOnUI(Activity contextActivity, Runnable method){
        activity = contextActivity;
        postRunOnUI = method;
    }

    // METODOS PRIVADOS PARA LAS CONSULTAS
    /**
     * Realiza un select de una tabla retornando todos los datos de la misma
     * @param tipo Objeto de la clase que almacena los datos de la tabla a la que se realiza la consulta
     * @return Lista con los valores retornados
     */
    private List<T> select(T tipo){
        List<T> retorno = null;
        // realizacion del select aqui
        if (TABLAS.contains(tipo.getTabla())){
            String urlString = String.format("%s/php/%s/%s.php", URL_SERVIDOR, tipo.getTabla(), "selectAll");

            try{
                URL url = new URL(urlString);
                // ABRIMOS CONEXIÓN
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int respuesta = connection.getResponseCode();

                // SI LA RESPUESTA DE LA CONEXIÓN ES CORRECTA ES CORRECTA
                if (respuesta == HttpURLConnection.HTTP_OK) {

                    // PREPARAMOS LA CADENA DE ENTRADA
                    String result = new String();
                    InputStreamReader in = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);

                    // CREAR UN READER A PARTIR DE LA INPUTSTREAM
                    BufferedReader reader = new BufferedReader(in);

                    // LEER LA ENTRADA Y ALMACENARLA EN UNA STRING
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result += line;
                    }

                    // TRANSFORMAR STRING A JSON
                    JSONObject json = new JSONObject(result);

                    // OBTENER LOS OBJETOS ENUMERADOS EN EL JSON
                    retorno = (List<T>) tipo.JSONaList(json);
                } else {
                    System.err.println("No se ha podido conectar con el servidor");
                }
            }catch (MalformedURLException e) {
                System.err.println("URL invalida");
            }catch (IOException e) {
                System.err.println("Error al leer los datos del servidor");
            }catch(JSONException e){
                System.err.println("Error obtener JSON");
            }
        }
        return retorno;
    }

    /**
     * Realiza un select de una tabla retornando los datos que cumplan la condicion
     * @param tabla Tabla a la que se le realiza la consulta
     * @param where Condicion de la consulta
     * @return Lista con los valores retornados
     */
    private List select(String tabla, String where){
        List retorno = null;
        // realizacion del select aqui
        return retorno;
    }

    /**
     * Inserta el objeto en la tabla que le corresponda
     * @param obj Objeto a insertar
     * @return true si se ha podido ingresar el objeto
     */
    private boolean insert(Object obj){
        boolean retorno = false;
        // realizacion del insert aqui
        return retorno;
    }

    /**
     * Actualiza el objeto en la tabla que le corresponda
     * @param obj Objeto a actualizar
     * @return true si se ha podido actualizar el objeto
     */
    private boolean update(Object obj){
        boolean retorno = false;
        // realizacion del update aqui
        return retorno;
    }

    /**
     * Busca y elimina el objeto en la tabla que le corresponda
     * @param obj Objeto a borrar
     * @return true si se ha podido eliminar el objeto
     */
    private boolean delete(Object obj){
        boolean retorno = false;
        // realizacion del delete aqui
        return retorno;
    }

    /**
     * Elimina el los registros de las tablas que cumplan la condicion
     * @param tabla Tabla a la que se le realiza el delete
     * @param where Condicion del delete
     * @return true si se han podido eliminar los registros
     */
    private boolean delete(String tabla, String where){
        boolean retorno = false;
        // realizacion del delete aqui
        return retorno;
    }

//== CONSTRUCTORES ==============================================================
    public Database(int tipoConsulta, T dato) {
        if (tipoConsulta == 3 || tipoConsulta < 0  || tipoConsulta > 4)
            tipoConsulta = -1;

        this.tipoConsulta = tipoConsulta;
        this.dato = dato;
        this.lstSelect = new LinkedList<>();
    }

    public Database(int tipoConsulta, T dato, T datoUpdate) {
        if (tipoConsulta < 0  || tipoConsulta > 4)
            tipoConsulta = -1;

        this.tipoConsulta = tipoConsulta;
        this.dato = dato;
        this.datoUpdate = datoUpdate;
        this.lstSelect = new LinkedList<>();
    }

//== GETTERS Y SETTERS ==========================================================
    public int getTipoConsulta() {
        return tipoConsulta;
    }

    public synchronized  T getDato() {
        return dato;
    }

    public T getDatoUpdate() {
        return datoUpdate;
    }

    public List<T> getLstSelect() {
        return lstSelect;
    }

    public boolean isFinish() {
        return finish;
    }
}
