package com.example.workpod.fragments;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.workpod.R;
import com.example.workpod.basic.Database;
import com.example.workpod.data.Workpod;
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

public class Fragment_Maps extends DialogFragment implements OnMapReadyCallback, View.OnClickListener {

    // CODIGOS PARA LA SOLICITUD DE PERMISOS
    private final int LOCATION_PERMISSION_CODE = 1003;
    private boolean havePermission = false;

    // VARIABLES PARA LA GESTION DEL MAPA Y LA LOCALIZACION
    private final double errorMedida = 0.01;
    private final int defaultZoom = 12;
    private GoogleMap mMap;
    private LocationManager locationService;
    private LatLng posicion;
    private int TAM_ICON = 120;

    // VARIABLES PARA LOS CONTROLES DEL FRAGMENT
    private ImageButton btnCentrar;

    // ALMACENAMIENTO DE DATOS
    private List<Workpod> lstWorkpods;

    // INFORMAR DE QUE TODOS LOS HILOS HAN DE FINALIZAR
    private boolean killHilos = false;

    //CONSTRUCTOR POR DEFECTO
    public Fragment_Maps() {
        lstWorkpods = new ArrayList<>();
    }

    //SOBREESCRITURAS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // COMPROBAR Y SOLICITAR PERMISOS
        locationService = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
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

        // Establecer listeners de los controles
        btnCentrar.setOnClickListener(this);

        // Leer workpods del JSON
        /*BufferedReader JSONReader = new BufferedReader(new InputStreamReader(getActivity().getResources().openRawResource(R.raw.pruebas)));
        try {
            String JSONString = "";
            String aux;
            while((aux = JSONReader.readLine()) != null)
                JSONString += aux;


            lstWorkpods.addAll(new Workpod().JSONaList(new JSONObject(JSONString)));

            System.err.println(JSONString);
        }catch(Exception e){
            System.err.println("Error al abrir el fichero");
            e.printStackTrace();
        }*/

        return view;
    }

    /**
     * Modifica el mapa cuando esta disponible
     * El movil ha de tener instalados los servicios de google
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Iniciar el hilo para solicitar la ubicacion
        try {
            locationService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, new UbicacionListener());
        }catch (SecurityException e) {  }

        // Establecer workpods en el mapa
        Database<Workpod> dbWorkpod = new Database<>(Database.SELECTALL, new Workpod());
        dbWorkpod.postRun(()->{lstWorkpods.addAll(dbWorkpod.getLstSelect());});
        dbWorkpod.postRunOnUI(getActivity(), () ->{dibujaWorkpods();});
        dbWorkpod.start();
    }

    // LISTENERS
    @Override
    public void onClick(View v) {
        btnCentrarOnClick(v);
    }

    // CLASE QUE FUNCIONARA COMO EL LISTENER DE LA UBICACION
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
                if(!killHilos) {
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
            if(!killHilos)
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
                        markPosicion.setIcon(VectortoBitmap(getContext() , R.drawable.button_btn_centrar, TAM_ICON, TAM_ICON));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicion, defaultZoom));
                    }
                    else
                        markPosicion.setPosition(posicion);

                }
            }
        }

        /**
         * Informa de los pasos a seguir para poder obtener la ubicacion
         * Debe correrse en un runOnUIThread
         * @return true si no se podra obtener la ubicacion
         */
        public boolean compruebaGPS(){
            boolean error = false;

            if (!havePermission){
                Toast.makeText(getContext(), "Conceda permisos para acceder a la ubicación", Toast.LENGTH_LONG).show();
                error = true;
            }
            else if (!locationService.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(getContext(), "Active el GPS del teléfono", Toast.LENGTH_LONG).show();
                error = true;
            }

            return error;
        }
    }

    // ONCLICK LISTENERS
    /**
     * Centra el mapa sobre la posicion del usuario
     * @param v Control pulsado
     */
    private void btnCentrarOnClick(View v){
        if (v.getId() == btnCentrar.getId() && posicion != null){
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

    // METODOS
    private void dibujaWorkpods(){
        Marker markPosicion;

        for(Workpod workpod : lstWorkpods){
            LatLng posicion = new LatLng(workpod.getLat(), workpod.getLon());

            markPosicion = mMap.addMarker(new MarkerOptions().position(posicion).title(workpod.getDescripcion()));
            markPosicion.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }
    }

    /**
     * Transforma un vectorAsset en un bitmapDrawable
     * @param context Contexto de la app
     * @param drawable Imagen vectorial
     * @param width Ancho de la imagen en px
     * @param height Alto de la imagen en px
     * @return Bitmap a partir del vectorAsset
     */
    private static BitmapDescriptor VectortoBitmap(Context context, int drawable, int width, int height){
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

    @Override
    public void onDestroy() {
        killHilos = true;
        locationService.removeUpdates(new UbicacionListener());

        super.onDestroy();
    }
}