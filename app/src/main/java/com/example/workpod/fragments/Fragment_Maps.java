package com.example.workpod.fragments;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.workpod.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Permissions;
import java.security.acl.Permission;

public class Fragment_Maps extends DialogFragment implements OnMapReadyCallback {

    // CODIGOS PARA LA SOLICITUD DE PERMISOS
    private final int LOCATION_PERMISSION_CODE = 1003;

    // VARIABLES PARA LA GESTION DEL MAPA Y LA LOCALIZACION
    private GoogleMap mMap;
    private LocationManager locationService;
    private LatLng posicion;

    //CONSTRUCTOR POR DEFECTO
    public Fragment_Maps() {
    }

    //SOBREESCRITURAS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // COMPROBAR Y SOLICITAR PERMISOS
        if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
            locationService = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        else{
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
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
        return view;
    }

    /**
     * Modifica el mapa cuando esta disponible
     * El movil ha de tener instalados los servicios de google
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            locationService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, new UbicacionListener());
        }catch (SecurityException e) {  }

        // Establecer zoom y posicion inicial del mapa (Posicion inicial puerta del Sol)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(40.41704890982951, -3.703483401820587)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                // SI SE RECHAZA DAR EL PERMISO SE PONE EL LOCATION SERVICE A NULL
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    locationService = null;
                break;
        }
    }

    // CLASE QUE FUNCIONARA COMO EL LISTENER DE LA UBICACION
    public class UbicacionListener implements LocationListener, Runnable {
        Marker markPosicion;

        public UbicacionListener() {
            markPosicion = null;
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
                    if (markPosicion == null)
                        markPosicion = mMap.addMarker(new MarkerOptions().position(posicion).title("Mi posición"));
                    else
                        markPosicion.setPosition(posicion);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
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
            if (locationService == null){
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
}