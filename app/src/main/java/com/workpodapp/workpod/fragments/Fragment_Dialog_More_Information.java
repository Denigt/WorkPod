package com.workpodapp.workpod.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.workpodapp.workpod.R;

public class Fragment_Dialog_More_Information extends DialogFragment implements View.OnClickListener {

    //XML
    private ImageView iVSalirDialogMoreInformation;
    private TextView tV_Titulo_Cupon;
    private TextView tV_Descripcion_Campana;
    private TextView tV_Condiciones_Uso;
    private TextView tV_Titulo_Fecha_Validez;
    private TextView tV_Fecha_Validez;
    private TextView tV_Minutos_Minimos;

    public Fragment_Dialog_More_Information() {

    }

    public static Fragment_Dialog_More_Information newInstance(String param1, String param2) {
        Fragment_Dialog_More_Information fragment = new Fragment_Dialog_More_Information();
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

        View view = inflater.inflate(R.layout.fragment_dialog_more_information, container, false);
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
        View view = inflater.inflate(R.layout.fragment_dialog_more_information, null);
        builder.setView(view);

        //INSTANCIAMOS ELEMENTOS DEL XML
        tV_Condiciones_Uso=view.findViewById(R.id.TV_Condiciones_Uso);
        tV_Descripcion_Campana=view.findViewById(R.id.TV_Descripcion_Campana);
        tV_Fecha_Validez=view.findViewById(R.id.TV_Fecha_Validez);
        tV_Minutos_Minimos=view.findViewById(R.id.TV_Minutos_Minimos);
        tV_Titulo_Cupon=view.findViewById(R.id.TV_Titulo_Cupon);
        tV_Titulo_Fecha_Validez=view.findViewById(R.id.TV_Titulo_Fecha_Validez);
        iVSalirDialogMoreInformation=view.findViewById(R.id.IVSalirDialogMoreInformation);
        tV_Minutos_Minimos.setText("Minutos mínimos para canjear este cupón: "+ Html.fromHtml("<font color='#FF58B1E3'>14</font>"));

        //LISTENERS
        iVSalirDialogMoreInformation.setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==iVSalirDialogMoreInformation.getId()){
            dismiss();
        }
    }
}