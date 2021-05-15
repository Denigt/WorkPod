package com.example.workpod.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.workpod.R;
import com.example.workpod.adapters.Adaptador_LsV_Workpod;
import com.example.workpod.data.Reserva;
import com.example.workpod.data.Ubicacion;
import com.example.workpod.data.Workpod;

import java.time.ZonedDateTime;


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

    //SOBREESCRITURAS
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
        View view = inflater.inflate(R.layout.fragment_dialog_cluster, null);
        builder.setView(view);
        //INICIALIZAMOS LOS ELEMENTOS DEL XML
        lsV_Workpods = (ListView) view.findViewById(R.id.LsV_Workpods);
        tVTituloDialogCluster = (TextView) view.findViewById(R.id.TVTituloDialogCluster);

        //MÉTODO PARA ARMAR EL LSV
        construyendo_LsV(view);
        //RETORNAMOS EL OBJETO BUILDER CON EL MÉTODO CREATE
        return builder.create();
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
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_dialog_cluster, container, false);
        return view;
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
        lsV_Workpods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Workpod workpod = (Workpod) adaptadorLsVWorkpod.getItem(i);
                for (int j = 0; j < ubicacion.getWorkpods().size(); j++) {
                    if (workpod == ubicacion.getWorkpods().get(j)) {
                        Fragment_Dialog_Workpod fragmentDialogWorkpod = new Fragment_Dialog_Workpod(workpod, ubicacion.getDireccion().toLongString());
                        fragmentDialogWorkpod.show(getActivity().getSupportFragmentManager(), "UN SOLO WORPOD EN ESA UBICACIÓN");
                        cerrarFragment();
                    }
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
    }

    public void cerrarFragment() {
        //CERRAR FRAGMENT
        getParentFragmentManager().beginTransaction().remove(this).commit();
        //DEPRECADO
        //getFragmentManager().beginTransaction().remove(this).commit();
        //CIERRA EL ACTIVITY
        //this.getActivity().finish();
    }
}