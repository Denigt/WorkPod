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

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.basic.Method;
import com.example.workpod.scale.Scale_TextView;

import java.util.ArrayList;
import java.util.List;

//PARA CREAR UN DIALOGRESULT DEBEMOS HEREDAR DE LA CLASE DIALOGFRAGMENT
public class Fragment_Dialog_Call extends DialogFragment implements View.OnClickListener {

    //XML
    private ImageView iVSalir;
    private ImageView iVLlamar;
    private TextView tVFgmDialogCallTlfn;

    //COLECCIONES
    List<Scale_TextView> lstTv;

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
        // EN EL M??TODO onCreateDialog
        iVSalir = (ImageView) v.findViewById(R.id.IVSalir);
        iVLlamar = (ImageView) v.findViewById(R.id.IVLlamar);
        tVFgmDialogCallTlfn=v.findViewById(R.id.tVFgmDialogCallTlfn);

        //ESTABLECEMOS EVENTOS PARA LOS CONTROLES
        iVSalir.setOnClickListener(this);
        iVLlamar.setOnClickListener(this);

        //ESCALAMOS ELEMENTOS
        escalarElementos();

        //RETORNAMOS EL OBJETO BUILDER CON EL M??TODO CREATE
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

    //M??TODOS

    /**
     * M??todo que nos permitir?? realizar una llamada al n??mero especificado en el TextView
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
                //COMPROBAMOS QUE EL N?? DE TLFN NO EST?? VAC??O.
                String dial = "tel:618.950.208";//PONER OBLIGATORIAMENTE tel
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));//ESTO SER?? LA LLAMADA

            }
        } catch (Exception e) {

        }
    }
    /**
     * Este m??todo sirve de ante sala para el m??todo de la clase Methods donde escalamos los elementos del xml.
     * En este m??todo inicializamos las colecciones donde guardamos los elementos del xml que vamos a escalar y
     * donde especificamos el width que queremos (match_parent, wrap_content o ""(si no ponemos nada significa que
     * el elemento tiene unos dp definidos que queremos que se conserven tanto en dispositivos grandes como en peque??os.
     * Tambi??n especificamos en la List el estilo de letra (bold, italic, normal) y el tama??o de la fuente del texto tanto
     * para dispositivos peque??os como para dispositivos grandes).
     *
     * Como el m??todo scale de la clase Methods no es un activity o un fragment no podemos inicializar nuestro objeto de la clase
     * DisplayMetrics con los par??metros reales de nuestro m??vil, es por ello que lo inicializamos en este m??todo.
     *
     * En resumen, en este m??todo inicializamos el metrics y las colecciones y se lo pasamos al m??todo de la clase Methods
     *
     */
    private void escalarElementos() {
        //INICIALIZAMOS COLECCIONES
        this.lstTv=new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstTv.add(new Scale_TextView(tVFgmDialogCallTlfn,"wrap_content","bold",35,35,35));

        Method.scaleTv(metrics, lstTv);
    }
}