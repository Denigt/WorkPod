package com.example.workpod.fragments;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.workpod.Fragment_Dialog_Cluster;
import com.example.workpod.Fragment_Dialog_Workpod;
import com.example.workpod.R;
import com.example.workpod.adapters.Adaptador_Lsv_Search;
import com.example.workpod.basic.Database;
import com.example.workpod.data.Direccion;
import com.example.workpod.data.Ubicacion;
import com.example.workpod.data.Workpod;
import com.example.workpod.otherclass.MapSearchListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Maps extends DialogFragment implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, AdapterView.OnItemClickListener{

    // CODIGOS PARA LA SOLICITUD DE PERMISOS
    private final int LOCATION_PERMISSION_CODE = 1003;
    private boolean havePermission = false;

    // VARIABLES PARA LA GESTION DEL MAPA Y LA LOCALIZACION
    /**
     * Rango en el que el boton de centrar tiene el efecto de acercar el mapa
     */
    private final double errorMedida = 0.01;
    /**
     * Zoom por defecto en el mapa
     */
    private final int defaultZoom = 12;
    /**
     * El mapa
     */
    private GoogleMap mMap;
    /**
     * Administrador de la geolocalizacion, maneja tambien el dibujado de la posicion del usuario
     */
    private LocationManager locationService;
    /**
     * Posicion del usuario
     * Cuidado al leerlo, puede ser null
     */
    private LatLng posicion;

    // TAMANO DE LOS ICONOS Y MARCADORES DE MAPA
    private int TAM_ICON = 130;
    private int TAM_MARKERS = 140;

    // VARIABLES PARA LOS CONTROLES DEL FRAGMENT
    private ImageButton btnCentrar;
    private SearchView etxtBusqueda;
    private ListView lsvBusqueda;

    // ALMACENAMIENTO DE DATOS
    private List<Ubicacion> lstUbicacion;
    Adaptador_Lsv_Search adpBusqueda;

    // INFORMAR DE QUE TODOS LOS HILOS HAN DE FINALIZAR
    private boolean killHilos = false;

    //LISTA CON LOS WORKPODS QUE HAY UN UNA UBICACIÓN
    List<Workpod> lstWorkpod = new ArrayList<>();

    //CONSTRUCTOR POR DEFECTO
    public Fragment_Maps() {
        lstUbicacion = new ArrayList<>();
    }

    //SOBREESCRITURAS
    @Override
    /**
     * Se ejecuta al iniciar el fragment
     * Comprueba que se tengan los permisos necesarios y de no tenerlos los solicita al usuario
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // COMPROBAR Y SOLICITAR PERMISOS
        locationService = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    /**
     * Pone el fragment de maps y establece variables para botonos y cuadros de busqueda
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Introducir el Layout del fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        // Obtener el fragment del mapa e informar de que esta listo para ser usado
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Obtener los controles de fragment
        btnCentrar = view.findViewById(R.id.btnCentrar);
        etxtBusqueda = view.findViewById(R.id.etxtBusqueda);
        lsvBusqueda = view.findViewById(R.id.lsvBusqueda);

        // Adaptador para la lsvBusqueda y el etxtBusqueda
        adpBusqueda = new Adaptador_Lsv_Search(getContext(), R.layout.item_lsv_search);
        lsvBusqueda.setAdapter(adpBusqueda);

        // Establecer listeners de los controles
        btnCentrar.setOnClickListener(this);
        lsvBusqueda.setOnItemClickListener(this);
        etxtBusqueda.setOnQueryTextListener(new MapSearchListener(etxtBusqueda, lsvBusqueda));

        return view;
    }

    /**
     * Es llamado cuando el mapa esta listo
     * El movil ha de tener instalados los servicios de Google
     * Inicia el proveedor de ubicacion y el administrador de la geolocalizacion (locationService)
     * Coloca los workpods en el mapa
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        // Iniciar el hilo para solicitar la ubicacion
        try {
            locationService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, new UbicacionListener());
        } catch (SecurityException e) {
        }

        // Establecer workpods en el mapa
        // Leer workpods del JSON (para cuando no los lea de la base de datos)
        /*BufferedReader JSONReader = new BufferedReader(new InputStreamReader(getActivity().getResources().openRawResource(R.raw.pruebas)));
        try {
            String JSONString = "";
            String aux;
            while((aux = JSONReader.readLine()) != null)
                JSONString += aux;


            lstUbicacion.addAll(new Ubicacion().JSONaList(new JSONObject(JSONString)));

            System.err.println(JSONString);
        }catch(Exception e){
            System.err.println("Error al abrir el fichero");
            e.printStackTrace();
        }*/
        Database<Ubicacion> dbUbicacion = new Database<>(Database.SELECTALL, new Ubicacion());
        dbUbicacion.postRun(() -> {
            if (!dbUbicacion.getError().get())
                lstUbicacion.addAll(dbUbicacion.getLstSelect());
        });
        dbUbicacion.postRunOnUI(getActivity(), () -> {
            if (!dbUbicacion.getError().get()) {
                dibujaWorkpods();
                adpBusqueda.addAll(lstUbicacion);
            } else if (dbUbicacion.getError().code == -404)
                Toast.makeText(getContext(), "No hay conexión a Internet", Toast.LENGTH_LONG).show();
        });
        dbUbicacion.start();
    }

    // LISTENERS
    @Override
    public void onClick(View v) {
        btnCentrarOnClick(v);
    }

    @Override
    /**
     * Se ejecuta al hacer click sobre un marcador
     * Los marcadores tienen en su atributo Tag la ubicacion a la que hacer referencia
     */
    public boolean onMarkerClick(Marker marker) {
        // COMPROBAR QUE EL MARCADOR TIENE DATOS EN SU TAG Y SON INSTACIA DE UBICACION
        if (marker.getTag() != null && marker.getTag() instanceof Ubicacion) {
            try {
                // Ubicacion a la que referencia el marcador (desde ella se pueden ver la lista de workpods del marcador)
                Ubicacion ubicacion = (Ubicacion) marker.getTag();
                //ABRIMOS EL DIALOGO EMERGENTE
                Fragment_Dialog_Cluster fragmentDialogCluster=new Fragment_Dialog_Cluster(ubicacion);
                fragmentDialogCluster.show(getActivity().getSupportFragmentManager(),"WORKPODS EN ESA UBICACIÓN");

                //TODO: Codigo para mostrar lista de workpods al hacer click en una ubicacion
            } catch (Exception e) {
                Log.e("ERROR ONMARKERCLICK", e.getMessage());
            }
        }

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == lsvBusqueda.getId()) {
            Ubicacion seleccion = (Ubicacion) adpBusqueda.getItem(position);
            etxtBusqueda.setQuery(seleccion.getDireccion().toLongString(), true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(seleccion.getPosicion(), defaultZoom));
        }
    }

    // ONCLICK LISTENERS
    /**
     * Centra el mapa sobre la posicion del usuario
     *
     * @param v Control pulsado
     */
    private void btnCentrarOnClick(View v) {
        if (v.getId() == btnCentrar.getId() && posicion != null) {
            // COMPROBAR SI LA CAMARA ESTA MAS O MENOS EN LA POSICION QUE LA DEJA POR DEFECTO EL BOTON CENTRAR
            if (!((mMap.getCameraPosition().target.latitude < posicion.latitude + errorMedida) &&
                    (mMap.getCameraPosition().target.latitude > posicion.latitude - errorMedida) &&
                    (mMap.getCameraPosition().target.longitude < posicion.longitude + errorMedida) &&
                    (mMap.getCameraPosition().target.longitude > posicion.longitude - errorMedida) &&
                    (mMap.getCameraPosition().zoom == defaultZoom)))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicion, defaultZoom));
                // SI LA CAMARA NO ESTA DESPLAZADA ACERCAR ZOOM
            else
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicion, 19));
        }
    }

//=== NO TOCAR NADA A PARTIR DE ESTA LINEA ==============================================================

    // CLASE QUE FUNCIONARA COMO EL LISTENER DE LA UBICACION

    /**
     * Esta clase funciona como LocationListener, es decir se ejecuta en segundo plano solicitando y
     * actualizando la posicion del usuario
     */
    public class UbicacionListener implements LocationListener, Runnable {
        Marker markPosicion;

        public UbicacionListener() {
            markPosicion = null;

            // SE COMPRUEBA SI SE TIENEN PERMISOS Y EN CASO DE TENERLOS SE SOLICITA LA ULTIMA UBICACION CONOCIDA PARA INICIAR EL MAPA EN ELLA
            new Thread(() -> {
                Location aux = null;
                while ((!havePermission || aux == null) && !killHilos) {
                    try {
                        aux = locationService.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        havePermission = true;
                    } catch (SecurityException e) {
                        havePermission = false;
                    }
                }
                if (!killHilos) {
                    posicion = new LatLng(aux.getLatitude(), aux.getLongitude());
                    getActivity().runOnUiThread(this);
                }
            }).start();
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (markPosicion != null) {
                        markPosicion.remove();
                        markPosicion = null;
                    }
                    Toast.makeText(getContext(), "Habilite el GPS", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            posicion = new LatLng(location.getLatitude(), location.getLongitude());
            if (!killHilos)
                getActivity().runOnUiThread(this);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        /**
         * Dibuja un marcador en el mapa en la posicion del telefono
         * Si el marcador ya esta dibujado lo desplaza
         * Se ha de correr en un runOnUIThread
         */
        public void run() {
            if (!compruebaGPS()) {
                if (posicion != null) {
                    if (markPosicion == null) {
                        markPosicion = mMap.addMarker(new MarkerOptions().position(posicion).title("Mi posición"));
                        markPosicion.setIcon(VectortoBitmap(getContext(), R.drawable.button_btn_centrar, TAM_ICON, TAM_ICON));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicion, defaultZoom));
                    } else
                        markPosicion.setPosition(posicion);

                }
            }
        }

        /**
         * Informa de los pasos a seguir para poder obtener la ubicacion
         * Debe correrse en un runOnUIThread
         *
         * @return true si no se podra obtener la ubicacion
         */
        public boolean compruebaGPS() {
            boolean error = false;

            if (!havePermission) {
                Toast.makeText(getContext(), "Conceda permisos para acceder a la ubicación", Toast.LENGTH_LONG).show();
                error = true;
            } else if (!locationService.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(getContext(), "Active el GPS del teléfono", Toast.LENGTH_LONG).show();
                error = true;
            }

            return error;
        }
    }

    // METODOS

    /**
     * Dibuja las ubicaciones de los workpods en el mapa
     */
    private void dibujaWorkpods() {
        Marker markPosicion;

        for (Ubicacion ubicacion : lstUbicacion) {
            LatLng posicion = new LatLng(ubicacion.getLat(), ubicacion.getLon());

            markPosicion = mMap.addMarker(new MarkerOptions().position(posicion));
            markPosicion.setTag(ubicacion);
            markPosicion.setIcon(VectortoBitmap(getContext(), R.drawable.markers_cluster, TAM_MARKERS, TAM_MARKERS, String.valueOf(ubicacion.getWorkpods().size()), 60, R.color.blue));
        }
    }

    /**
     * Transforma un vectorAsset en un bitmapDrawable
     *
     * @param context  Contexto de la app
     * @param drawable Imagen vectorial
     * @param width    Ancho de la imagen en px
     * @param height   Alto de la imagen en px
     * @return Bitmap a partir del vectorAsset
     */
    private static BitmapDescriptor VectortoBitmap(Context context, int drawable, int width, int height) {
        // Obtener el Drawable
        Drawable vectorImg = ContextCompat.getDrawable(context, drawable);
        // Establecer tamanos
        vectorImg.setBounds(0, 0, width, height);
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Dibujar en el bitmat
        Canvas canvas = new Canvas(bm);
        vectorImg.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    /**
     * Transforma un vectorAsset en un bitmapDrawable y escribe texto centrado sobre el
     *
     * @param context   Contexto de la app
     * @param drawable  Imagen vectorial
     * @param width     Ancho de la imagen en px
     * @param height    Alto de la imagen en px
     * @param text      Texto a escribir
     * @param textTam   Tamano del texto
     * @param textColor Color del texto (id en los Resources de la app)
     * @return Bitmap a partir del vectorAsset
     */
    private static BitmapDescriptor VectortoBitmap(Context context, int drawable, int width, int height, String text, int textTam, int textColor) {
        // Declarar pincel para hacer el texto
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        Paint pen = new Paint();
        pen.setTextSize(textTam);
        pen.setTextAlign(Paint.Align.CENTER);
        pen.setTypeface(font);
        pen.setColor(context.getResources().getColor(textColor));

        // Obtener el Drawable
        Drawable vectorImg = ContextCompat.getDrawable(context, drawable);
        // Establecer tamanos
        vectorImg.setBounds(0, 0, width, height);
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Dibujar en el bitmat
        Canvas canvas = new Canvas(bm);
        vectorImg.draw(canvas);
        canvas.drawText(text, canvas.getWidth() / 2, (canvas.getHeight() / 2) + (textTam / 4), pen);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    @Override
    /**
     * Ademas de funcionar como metodo onDestroy ordena a todos los hilos que se detengan y al ubicationListener que deje de solicitar la posicion
     */
    public void onDestroy() {
        killHilos = true;
        locationService.removeUpdates(new UbicacionListener());

        super.onDestroy();
    }
}