package com.example.workpod.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.data.Direccion;
import com.example.workpod.data.Ubicacion;
import com.example.workpod.data.Workpod;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Dialog_Workpod#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Dialog_Workpod extends DialogFragment implements View.OnClickListener {

    //INSTANCIAMOS LA CLASE UBICACION
    Ubicacion ubicacion;
    Workpod workpod;
    //XML
    private TextView tVCapacidad1;
    private TextView tVDireccion;
    private TextView tVCapacidad2;
    private TextView tVPrecio;
    private ImageView iVComoLLegar;
    private Button btnReservarWorkpod;
    private Button btnAbrirAhora;

    //DATOS DE LA TABLA WORKPOD
    private int numUsuarios;
    private double precio;
    private String direccion;


    //CONSTRUCTOR POR DEFECTO
    public Fragment_Dialog_Workpod() {
        ubicacion = new Ubicacion();
    }

    //CONSTRUCTOR CON PARAMETROS


    public Fragment_Dialog_Workpod(Workpod workpod, String direccion) {
        this.workpod = workpod;
        this.direccion = direccion;
    }

    public Fragment_Dialog_Workpod(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public static Fragment_Dialog_Workpod newInstance(String param1, String param2) {
        Fragment_Dialog_Workpod fragment = new Fragment_Dialog_Workpod();
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
        View view = inflater.inflate(R.layout.fragment_dialog_workpod, null);
        builder.setView(view);

        //INICIALIZAMOS LOS ELEMENTOS DEL XML
        tVCapacidad1 = (TextView) view.findViewById(R.id.TVCapacidad1);
        tVDireccion = (TextView) view.findViewById(R.id.TVDireccion);
        tVCapacidad2 = (TextView) view.findViewById(R.id.TVCapacidad2);
        tVPrecio = (TextView) view.findViewById(R.id.TVPrecio);
        iVComoLLegar = (ImageView) view.findViewById(R.id.IVComoLlegar);
        btnAbrirAhora = (Button) view.findViewById(R.id.BtnAbrirAhora);
        btnReservarWorkpod = (Button) view.findViewById(R.id.BtnReservarWorkpod);

        //LE PASAMOS LOS DATOS DEL WORKPOD
        if (workpod == null) {
            if (ubicacion.getWorkpods().get(0).getNumUsuarios() == 1) {
                tVCapacidad1.setText("Capacidad: " + String.valueOf(ubicacion.getWorkpods().get(0).getNumUsuarios()) + " persona");
                tVCapacidad2.setText("Capacidad: " + String.valueOf(ubicacion.getWorkpods().get(0).getNumUsuarios()) + " persona");
                tVPrecio.setText(String.valueOf(String.format("%.2f", ubicacion.getWorkpods().get(0).getPrecio())) + "€/min");
                tVDireccion.setText(ubicacion.getDireccion().toLongString());
            } else {
                tVCapacidad1.setText("Capacidad: " + String.valueOf(ubicacion.getWorkpods().get(0).getNumUsuarios()) + " personas");
                tVCapacidad2.setText("Capacidad: " + String.valueOf(ubicacion.getWorkpods().get(0).getNumUsuarios()) + " personas");
                tVPrecio.setText(String.valueOf(String.format("%.2f", ubicacion.getWorkpods().get(0).getPrecio())) + "€/min");
                tVDireccion.setText(ubicacion.getDireccion().toLongString());
            }
        } else {
            //ESTOS IF SON ESTETICOS, ES PARA QUE PONGA PERSONA O PERSONAS
            if (workpod.getNumUsuarios() == 1) {
                tVCapacidad1.setText("Capacidad: " + String.valueOf(workpod.getNumUsuarios()) + " persona");
                tVCapacidad2.setText("Capacidad: " + String.valueOf(workpod.getNumUsuarios()) + " persona");
                tVPrecio.setText(String.valueOf(String.format("%.2f", workpod.getPrecio())) + "€/min");
                tVDireccion.setText(direccion);
            }else{
                tVCapacidad1.setText("Capacidad: " + String.valueOf(workpod.getNumUsuarios()) + " personas");
                tVCapacidad2.setText("Capacidad: " + String.valueOf(workpod.getNumUsuarios()) + " personas");
                tVPrecio.setText(String.valueOf(String.format("%.2f", workpod.getPrecio())) + "€/min");
                tVDireccion.setText(direccion);
            }
        }


        //LISTENERS
        btnReservarWorkpod.setOnClickListener(this);
        btnAbrirAhora.setOnClickListener(this);

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
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_dialog_workpod, container, false);
        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.BtnAbrirAhora) {
            if (workpod == null) {
                //LLAMAMOS AL FRAGMENT DE SESIÓN FINALIZADA
                Fragment_sesion_finalizada fragmentSesionFinalizada = new Fragment_sesion_finalizada(ubicacion);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSesionFinalizada).commit();
                //CERRAMOS EL DIALOGO EMERGENTE
                dismiss();
            } else {
                //LLAMAMOS AL FRAGMENT DE SESIÓN FINALIZADA
                Fragment_sesion_finalizada fragmentSesionFinalizada = new Fragment_sesion_finalizada(workpod, direccion);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSesionFinalizada).commit();
                //CERRAMOS EL DIALOGO EMERGENTE
                dismiss();
            }

        }
    }
}