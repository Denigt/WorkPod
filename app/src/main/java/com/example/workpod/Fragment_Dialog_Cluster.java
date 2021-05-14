package com.example.workpod;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.workpod.data.Ubicacion;
import com.example.workpod.data.Workpod;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


public class Fragment_Dialog_Cluster extends DialogFragment implements View.OnClickListener {

    //DECLARAMOS VARIABLES DEL XML
    private ListView lsV_Workpods;
    private Ubicacion ubicacion;
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


    public Fragment_Dialog_Cluster(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Fragment_Dialog_Cluster() {
        ubicacion = new Ubicacion();
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
        return view;
    }

    //ESTA SOBREESCRITURA PERMITE CREAR UN DIALOGRESULT
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return createDialog();
    }

    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ESPECIFICAMOS DONDE VAMOS A CREAR (INFLAR) EL DIALOGRESULT
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment__dialog__cluster, null);
        builder.setView(view);

        //INICIALIZAMOS LOS ELEMENTOS DEL XML
        lsV_Workpods = (ListView) view.findViewById(R.id.LsV_Workpods);
        tVTituloDialogCluster = (TextView) view.findViewById(R.id.TVTituloDialogCluster);
        iVSalirDialogCluster = (ImageView) view.findViewById(R.id.IVSalirDialogCluster);

        //MÉTODO PARA ARMAR EL LSV
        construyendo_LsV(view);

        //LISTENER
        iVSalirDialogCluster.setOnClickListener(this);

        //RETORNAMOS EL OBJETO BUILDER CON EL MÉTODO CREATE
        return builder.create();
    }


    /**
     * En este método armamos el LsV pasandole el adapatador y la lista con los workpods en los que hay ubicaciones
     *
     * @param view instancia de la clase View
     */
    private void construyendo_LsV(View view) {

        final Adaptador_LsV_Workpod adaptadorLsVWorkpod = new Adaptador_LsV_Workpod(view.getContext(), ubicacion.getWorkpods());
        lsV_Workpods.setAdapter(adaptadorLsVWorkpod);

        tVTituloDialogCluster.setText(String.valueOf(ubicacion.getDireccion()));
    }

    @Override
    public void onClick(View v) {
        //CIERRA EL CUADRO DE DIALOGO
        if (v.getId() == R.id.IVSalirDialogCluster) {
            dismiss();
        }
    }
}