package com.example.workpod.fragments;

import android.Manifest;
//IMPRESCINDIBLE PARA QUE FUNCIONE EN APIS INFERIORES A LA 23
 import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workpod.R;

//PARA CREAR UN DIALOGRESULT DEBEMOS HEREDAR DE LA CLASE DIALOGFRAGMENT
public class Fragment_Dialog_Call extends DialogFragment implements View.OnClickListener {

    //XML
    private ImageView iVSalir;
    private ImageView iVLlamar;


    //PERMISOS
    private static final int PERMISO_LLAMADA = 50;

    public Fragment_Dialog_Call() {
        // Required empty public constructor
    }

    //SOBREESCRITURAS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_call, container, false);
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
        View v = inflater.inflate(R.layout.fragment_dialog_call, null);
        builder.setView(v);

        //PARA QUE FUNCIONEN LOS ELEMENTOS DEL XML (BTN, IV...) EN UN DIALOG, LAS INSTANCIAS HAN DE ESTAR
        // EN EL MÉTODO onCreateDialog
        iVSalir = (ImageView) v.findViewById(R.id.IVSalir);
        iVLlamar = (ImageView) v.findViewById(R.id.IVLlamar);

        //ESTABLECEMOS EVENTOS PARA LOS CONTROLES
        iVSalir.setOnClickListener(this);
        iVLlamar.setOnClickListener(this);

        //RETORNAMOS EL OBJETO BUILDER CON EL MÉTODO CREATE
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.IVSalir) {
            dismiss();
        } else if (v.getId() == R.id.IVLlamar) {
            llamar(v);
        }
    }

    //MÉTODOS

    /**
     * Método que nos permitirá realizar una llamada al número especificado en el TextView
     *
     * @param v instancia de la clase View
     */
    public void llamar(View v) {
        try {
            //VERIFICAMOS SI TENEMOS LOS PERMISOS PARA LLAMAR
            int permiso_Llamada = ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE);
            if (permiso_Llamada != PackageManager.PERMISSION_GRANTED) {
                //  Toast.makeText(v.getContext(), "No tienes permiso para llamar", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISO_LLAMADA);
            } else {
                Log.i("Mensaje", "Se tiene permiso para llamar");
                //COMPROBAMOS QUE EL Nº DE TLFN NO ESTÉ VACÍO.
                String dial = "tel:618.950.208";//PONER OBLIGATORIAMENTE tel
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));//ESTO SERÁ LA LLAMADA

            }
        } catch (Exception e) {

        }
    }
}