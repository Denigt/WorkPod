package com.example.workpod;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Prueba_Persistencia_Clase_Workpod extends AppCompatActivity implements View.OnClickListener {

    //CONSTANTES
    public static final String INSERT = "INSERT";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
    public static final String SELECT = "SELECT";
    public static final String URL_SERVIDOR = "https://dev.workpod.app";
    public static final String URL_INSERT = URL_SERVIDOR + "/Workpod_Insert.php";
    public static final String URL_UPDATE = URL_SERVIDOR + "/workpod_update.php";
    public static final String URL_DELETE = URL_SERVIDOR + "/workpod_delete.php";
    public static final String URL_SELECT = URL_SERVIDOR + "/obtener_workpods.php";

    public String sentencia;

    //ELEMENTOS PARA LA CONEXIÓN CON EL SERVIDOR
    //CREAMOS UN OBJETO DE LA CLASE HttpURLConnection PARA ABRIR UNA CONEXIÓN HTTP
    HttpURLConnection urlConn;
    String devuelve = "";

    //DECLARACIÓN VARIABLES XML
    private Button btnInsert, btnUpdate, btnDelete, btnListar;
    private EditText eTLatitud, eTLongitud, eTUbicacion, etID;
    TextView tVListarWorkpod;

    //IP DEL SERVIDOR
    String IP = "https://dev.workpod.app";
    //RUTAS DE LOS WEB SERVICES
    String listar = IP + "/obtener_workpods.php";
    String insertar = IP + "/Workpod_Insert.php";
    String actualizar = IP + "/workpod_update.php";
    String borrar = IP + "/workpod_delete.php";

    //INSTANCIA DE LA CLASE DATABASE
    conectionDataBase conexion;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba__persistencia__clase__workpod);

        //INICIALIZAMOS LAS VARIABLES XML CON JAVA
        btnInsert = (Button) findViewById(R.id.BtnInsert);
        btnUpdate = (Button) findViewById(R.id.BtnUpdate);
        btnDelete = (Button) findViewById(R.id.BtnDelete);
        btnListar = (Button) findViewById(R.id.BtnListar);
        tVListarWorkpod = (TextView) findViewById(R.id.TVListarWorkpod);
        eTLatitud = (EditText) findViewById(R.id.ETLatitud);
        eTLongitud = (EditText) findViewById(R.id.ETLongitud);
        eTUbicacion = (EditText) findViewById(R.id.ETUbicacion);
        etID = (EditText) findViewById(R.id.ETID);

        btnInsert.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnListar.setOnClickListener(this);

    }

    //METODOS

    /**
     * Método para comprobar si estás conectado a internet, es necesario
     * ya que sin internet, no podremos interactuar con la BD
     *
     * @param context contexto de la app
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void conectadoInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //SI EL NETWORKINFO ES NULL O SI ISCONNECTED DEVUELVE FALSE ES QUE NO HAY INTERNET
        if (networkInfo == null || (networkInfo.isConnected() == false)) {
            Toast.makeText(getApplicationContext(), "No estás conectado a internet", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnListar) {
            //COMPROBAMOS SI ESTAMOS CONECTADOS A INTERNET
            conectadoInternet(getApplicationContext());
            //REALIZAMOS LA CONEXIÓN CON LA BD
            conexion = new conectionDataBase();
            //LE PASAMOS LA IP Q LLEVA AL PHP Q LISTA Y EL 1 ES X Q ERA LA OPCIÓN DEL PHP PARA MOSTRAR INFO
            conexion.execute(listar, "1");
            //tVListarWorkpod.setText(listar);
        } else if (v.getId() == R.id.BtnInsert) {
            conexion = new conectionDataBase();
            conexion.execute(insertar, "2", eTLatitud.getText().toString(), eTLongitud.getText().toString(), eTUbicacion.getText().toString());   // Parámetros que recibe doInBackground
        } else if (v.getId() == R.id.BtnUpdate) {
            conexion = new conectionDataBase();
            conexion.execute(actualizar, "3", etID.getText().toString(), eTLatitud.getText().toString(), eTLongitud.getText().toString(), eTUbicacion.getText().toString());
        } else if (v.getId() == R.id.BtnDelete) {
            conexion = new conectionDataBase();
            conexion.execute(borrar, "4", etID.getText().toString());
        }

    }

    //MÉTODOS

    /**
     * EN ESTE MÉTODO NOS ABRIREMOS UNA CONEXIÓN, INTRODUCIREMOS LA URL QUE NOS LLEVARÁ AL PHP QUE NOS INTERESA
     * DEL SERVIDOR (YA SEA EL DE BORRADO, INSERCIÓN...) Y NOS CONECTAMOS A ESE PHP (ESPECIFICAMOS QUE NOS
     * VAMOS A CONECTAR A UN ARCHIVO TIPO JSON)
     *
     * @param cadena
     */
    public void conexionServidor(String cadena) {

        try {
            URL url = new URL(cadena);
            //ABRO CONEXIÓN
            urlConn = (HttpURLConnection) url.openConnection();
            //PODER METER DATOS DE ENTRADA
            urlConn.setDoInput(true);
            //PODER METER DATOS DE SALIDA
            urlConn.setDoOutput(true);
            //CACHÉ A FALSE PARA QUE LO HAGA NUEVO
            urlConn.setUseCaches(false);
            //ESPECIFICAMOS QUE EL CONTENIDO DEL ARCHIVO QUE SE VA A ABRIR EN LA CONEXIÓN ES UN JSON
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Accept", "application/json");
            //NOS CONECTAMOS
            urlConn.connect();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Problemas al abrir la conexión", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * EN ESTE MÉTODO VALIDAMOS LO QUE NOS DEVUELVE EL PHP, PRIMERO SE HA HECHO LA CONEXIÓN CON EL MÉTODO
     * conexionServidor, LUEGO HEMOS CREADO UN OBJETO JSON CON LOS PARÁMETROS QUE SE NECESITA (URL Y ATRIBUTOS DE LA TABLA)
     * Y EN ESTE MÉTODO VALIDAMOS QUE CONEXIÓN ES CORRECTA (LÍNEA 191) QUE LA SENTENCIA DML ES CORRECTA (LINEA 206)
     *
     * @param jsonParam ES UN OBJETO DE LA CLASE JSONOBJECT QUE HEMOS USADO PARA CREAR EL JSON QUE VA A REALIZAR
     *                  LA SENTENCIA DML
     */
    public void validacion(JSONObject jsonParam) {
        try {
            OutputStream os = urlConn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonParam.toString());
            writer.flush();
            writer.close();

            //OBTENGO UNA RESPUESTA Y LA VALIDO
            int respuesta = urlConn.getResponseCode();
            StringBuilder result = new StringBuilder();

            //VALIDAMOS LA RESPUESTA
            if (respuesta == HttpURLConnection.HTTP_OK) {

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }

                //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena

                //Accedemos al vector de resultados

                String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultJSON.equals("1")) {
                    devuelve = "Sentencia ejecutada correctamente";

                } else if (resultJSON.equals("2")) {
                    devuelve = "se ha producido un error al ejecutar la sentencia";
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * HACEMOS QUE LAS TAREAS PESADAS (INSERT, UPDATE, DELETE, GET SE EJECUTEN EN UN HILO EN SEGUNDO PLANO)
     * EL HILO SE ENCUENTRA DENTRO DE ASYNCTASK ES UN HILO PARA TRABAJAR CON TAREAS ASÍNCRONAS
     */
    public class conectionDataBase extends AsyncTask<String, Void, String> {
        //ASYNCTASK ESTÁ OBSOLETA PARA API 30 PERO COMO TRABAJAMOS CON LA 21 LA TENEMOS QUE UTILIZAR

        //EN ESTA SOBREESCRITURA SE REALIZARÁN TODAS LAS ACCIONES
        @Override
        //LOS PARAMETROS DEL CONSTRUCTOR SERÁN LOS QUE QUEREMOS EJECUTAR EN EL HILO
        protected String doInBackground(String... parametros) {
            String cadena = parametros[0];//LE PASAMOS LA URL
            URL url = null; // Url de donde queremos obtener información


            if (parametros[1].equals("1")) {

                try {
                    url = new URL(cadena);
                    //ABRIMOS CONEXIÓN
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();
                    //SI LA RESPUESTA DE LA CONEXIÓN ES CORRECTA ES CORRECTA
                    if (respuesta == HttpURLConnection.HTTP_OK) {

                        //PREPARAMOS LA CADENA DE ENTRADA
                        InputStream in = new BufferedInputStream(connection.getInputStream());

                        //LA INTRODUCIMOS EN UN BUFFEREDREADER
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        //EL JSONOBJECT NECESITA UN STRING, POR ELLO TRANSFORMAMOS EL BUFFEREDREADER A STRING
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        if (resultJSON.equals("1")) {      // hay alumnos a mostrar
                            JSONArray workpodsJSON = respuestaJSON.getJSONArray("workpods");   // estado es el nombre del campo en el JSON

                            for (int i = 0; i < workpodsJSON.length(); i++) {

                                devuelve += "ID:" + workpodsJSON.getJSONObject(i).getString("id") + "\n" +
                                        "Latitud: " + workpodsJSON.getJSONObject(i).getString("x") + "\n" +
                                        "Longitud: " + workpodsJSON.getJSONObject(i).getString("y") + "\n" +
                                        "Ubicación: " + workpodsJSON.getJSONObject(i).getString("ubicacion") + "\n" +
                                        "----------------------------------------------------------" + "\n";
                            }
                        } else if (resultJSON == "2") {
                            devuelve = "No hay workpods";
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Problema", Toast.LENGTH_LONG).show();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //INSERTAR
            } else if (parametros[1].equals("2")) {
                try {
                    //CONECTAMOS CON EL SERVIDOR
                    conexionServidor(URL_INSERT);
                    //CREO EL OBJETO JSON
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("x", parametros[2]);
                    jsonParam.put("y", parametros[3]);
                    jsonParam.put("ubicacion", parametros[4]);
                    //VALIDAMOS CONEXIÓN Y SENTENCIAS EJECUTADAS
                    validacion(jsonParam);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return devuelve;

                //ACTUALIZAR
            } else if (parametros[1].equals("3")) {
                try {
                    //CONECTAMOS CON EL SERVIDOR
                    conexionServidor(URL_UPDATE);
                    //CREO EL OBJETO JSON
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("id", parametros[2]);
                    jsonParam.put("x", parametros[3]);
                    jsonParam.put("y", parametros[4]);
                    jsonParam.put("ubicacion", parametros[5]);
                    //VALIDAMOS CONEXIÓN Y SENTENCIAS EJECUTADAS
                    validacion(jsonParam);
                    return devuelve;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //BORRAR
            } else if (parametros[1] == "4") {
                try {
                    //CONECTAMOS CON EL SERVIDOR
                    conexionServidor(URL_DELETE);
                    //CREO EL OBJETO JSON
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("id", parametros[2]);
                    //VALIDAMOS CONEXIÓN Y SENTENCIAS EJECUTADAS
                    validacion(jsonParam);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return devuelve;
        }


        //EN ESTA SOBREESCRITURA INDICAREMOS DONDE QUEREMOS MOSTRAR LA INFORMACIÓN
        @Override
        protected void onPostExecute(String s) {
            tVListarWorkpod.setText(s);
            super.onPostExecute(s);
        }


    }
}

