package com.example.workpod.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workpod.R;
import com.example.workpod.ValoracionWorkpod;
import com.example.workpod.WorkpodActivity;
import com.example.workpod.data.Ubicacion;
import com.example.workpod.data.Workpod;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_sesion_finalizada#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_sesion_finalizada extends Fragment implements View.OnClickListener {

    //XML
    private Button btnCerrarWorPod;
    private Button btnContactarSoporte;
    private TextView tVWifi;
    private TextView tVSesionCapacidad;
    private TextView tVSesionDireccion;
    private TextView tVTiempoTranscurrido;

    //BD
    Ubicacion ubicacion;
    Workpod workpod;
    String direccion;

    public Fragment_sesion_finalizada() {
        // Required empty public constructor
    }

    //CONSTRUCTOR CON UBICACION
    public Fragment_sesion_finalizada(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    //CONSTRUCTOR CON WORKPOD
    public Fragment_sesion_finalizada(Workpod workpod, String direccion) {
        this.workpod = workpod;
        this.direccion = direccion;
    }


    public static Fragment_sesion_finalizada newInstance(String param1, String param2) {
        Fragment_sesion_finalizada fragment = new Fragment_sesion_finalizada();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sesion_finalizada, container, false);
        //INICIALIZAMOS ELEMENTOS DEL XML
        btnCerrarWorPod = (Button) view.findViewById(R.id.BtnCerrarWorPod);
        btnContactarSoporte = (Button) view.findViewById(R.id.BtnContactarSoporte);
        tVSesionCapacidad = (TextView) view.findViewById(R.id.TVSesionCapacidad);
        tVSesionDireccion = (TextView) view.findViewById(R.id.TVSesionDireccion);
        tVTiempoTranscurrido = (TextView) view.findViewById(R.id.TVTiempoTranscurrido);
        tVWifi = (TextView) view.findViewById(R.id.TVWifi);

        //CAMBIAMOS DE COLOR A LOS BOTONES (POR LA API 21)
        btnCerrarWorPod.setBackgroundColor(Color.parseColor("#DA4B4B"));
        btnContactarSoporte.setBackgroundColor(Color.parseColor("#C3A240"));

        valoresWorkpod();
        //LISTENERS
        btnCerrarWorPod.setOnClickListener(this);
        btnContactarSoporte.setOnClickListener(this);
        return view;
    }

    /**
     * Metodo donde asignaremos el valor del workpod a los elementos del xml
     */
    private void valoresWorkpod() {
        if (workpod == null) {
            //ESTOS IF SON ESTETICOS, ES PARA QUE PONGA PERSONA O PERSONAS
            if (ubicacion.getWorkpods().get(0).getNumUsuarios() == 1) {
                tVSesionCapacidad.setText("Capacidad: " + String.valueOf(ubicacion.getWorkpods().get(0).getNumUsuarios()) + " persona");
                tVSesionDireccion.setText(ubicacion.getDireccion().toLongString());
                tVWifi.setText(ubicacion.getWorkpods().get(0).getNombre());
                tVTiempoTranscurrido.setText("00:00");
            } else {
                tVSesionCapacidad.setText("Capacidad: " + String.valueOf(ubicacion.getWorkpods().get(0).getNumUsuarios()) + " personas");
                tVSesionDireccion.setText(ubicacion.getDireccion().toLongString());
                tVWifi.setText(ubicacion.getWorkpods().get(0).getNombre());
                tVTiempoTranscurrido.setText("00:00");
            }

        } else {
            //ESTOS IF SON ESTETICOS, ES PARA QUE PONGA PERSONA O PERSONAS
            if(workpod.getNumUsuarios()==1){
                tVSesionCapacidad.setText("Capacidad: " + String.valueOf(workpod.getNumUsuarios()) + " persona");
                tVSesionDireccion.setText(direccion);
                tVWifi.setText(workpod.getNombre());
                tVTiempoTranscurrido.setText("00:00");
            }else{
                tVSesionCapacidad.setText("Capacidad: " + String.valueOf(workpod.getNumUsuarios()) + " personas");
                tVSesionDireccion.setText(direccion);
                tVWifi.setText(workpod.getNombre());
                tVTiempoTranscurrido.setText("00:00");
            }

        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnCerrarWorPod) {
            Intent activity = new Intent(getActivity(), ValoracionWorkpod.class);
            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
        } else if (v.getId() == R.id.BtnContactarSoporte) {
            fragment_support fragmentSupport = new fragment_support();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSupport).commit();
            //CAMBIAMOS LA SELECCIÓN AL ICONO DE SOPORTE
            WorkpodActivity.btnNV.setSelectedItemId(R.id.inv_support);
            WorkpodActivity.boolLoc = false;
        }
    }
}