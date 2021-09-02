package com.workpodapp.workpod.fragments;

//IMPRESCINDIBLE PARA QUE FUNCIONE EN APIS INFERIORES A LA 23

import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.workpodapp.workpod.R;



//PARA CREAR UN DIALOGRESULT DEBEMOS HEREDAR DE LA CLASE DIALOGFRAGMENT
public class Fragment_Dialog_Calendar extends DialogFragment implements View.OnClickListener {

    //XML
    private ImageView iVDialogCalendarSalir;
    public Fragment_Dialog_Calendar() {
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
        View view = inflater.inflate(R.layout.fragment_dialog_calendar, container, false);
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
        View v = inflater.inflate(R.layout.fragment_dialog_calendar, null);
        builder.setView(v);

        //ASIGNAMOS VARIABLES DEL XML
        iVDialogCalendarSalir = (ImageView) v.findViewById(R.id.IVDialogCalendarSalir);

        //ESTABLECEMOS EVENTOS PARA LOS CONTROLES
        iVDialogCalendarSalir.setOnClickListener(this);

        //RETORNAMOS EL OBJETO BUILDER CON EL MÉTODO CREATE
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        //CIERRA EL CUADRO DE DIALOGO
        if (v.getId() == R.id.IVDialogCalendarSalir) {
            dismiss();
        }
    }

    //MÉTODOS

}