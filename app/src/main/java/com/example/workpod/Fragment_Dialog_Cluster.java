package com.example.workpod;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.workpod.adapters.Adaptador_LsV_Workpod;
import com.example.workpod.data.Reserva;
import com.example.workpod.data.Workpod;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


public class Fragment_Dialog_Cluster extends DialogFragment implements View.OnClickListener {

    //DECLARAMOS VARIABLES DEL XML
    private ListView lsV_Workpods;
    private List<Workpod> lstWorkpods = new ArrayList<>();
    private TextView tVTituloDialogCluster;
    private ImageView iVSalirDialogCluster;

    //VARIABLES DE WORKPOD
    private int capacidad;
    private double precio;
    private String direccion;
    private String nombre;
    private ZonedDateTime ultUso;
    private ZonedDateTime ultLimpieza;
    private Reserva reserva;

    //CONSTRUCTOR


    public Fragment_Dialog_Cluster(List<Workpod> lstWorkpods) {
        this.lstWorkpods = lstWorkpods;
    }

    public Fragment_Dialog_Cluster() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment_Dialog_Cluster newInstance(String param1, String param2) {
        Fragment_Dialog_Cluster fragment = new Fragment_Dialog_Cluster();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__dialog__cluster, container, false);
        //INICIALIZAMOS LOS ELEMENTOS DEL XML
        lsV_Workpods = (ListView) view.findViewById(R.id.LsV_Workpods);
        tVTituloDialogCluster = (TextView) view.findViewById(R.id.TVTituloDialogCluster);
        iVSalirDialogCluster=(ImageView)view.findViewById(R.id.IVSalirDialogCluster);

        //MÉTODO PARA ARMAR EL LSV
        construyendo_LsV(view);

        //LISTENER
        iVSalirDialogCluster.setOnClickListener(this);
        return view;
    }

    /**
     * En este método armamos el LsV pasandole el adapatador y la lista con los workpods en los que hay ubicaciones
     *
     * @param view instancia de la clase View
     */
    private void construyendo_LsV(View view) {

        final Adaptador_LsV_Workpod adaptadorLsVWorkpod = new Adaptador_LsV_Workpod(view.getContext(), lstWorkpods);
        lsV_Workpods.setAdapter(adaptadorLsVWorkpod);
        for (Workpod workpod : lstWorkpods) {
            tVTituloDialogCluster.setText(String.valueOf(workpod.getUbicacion().getDireccion()));
        }
    }

    @Override
    public void onClick(View v) {
        //CIERRA EL CUADRO DE DIALOGO
        if (v.getId() == R.id.IVSalirDialogCluster) {
            dismiss();
        }
    }
}