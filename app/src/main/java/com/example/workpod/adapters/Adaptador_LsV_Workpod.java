package com.example.workpod.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.data.Workpod;

import java.util.ArrayList;
import java.util.List;

public class Adaptador_LsV_Workpod extends BaseAdapter {
    //DECLARAMOS VARIABLES
    Context context;
    List<Workpod> lstWorkpods = new ArrayList<>();

    //CONSTRUCTOR POR DEFECTO
    public Adaptador_LsV_Workpod() {
    }

    //CONSTRUCTOR CON TODOS LOS PARAMETROS

    public Adaptador_LsV_Workpod(Context context, List<Workpod> lstWorkpods) {
        this.context = context;
        this.lstWorkpods = lstWorkpods;
    }

    @Override
    public int getCount() {
        return lstWorkpods.size();
    }

    @Override
    public Object getItem(int i) {
        return lstWorkpods.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstWorkpods.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        //INFLAMOS EL LAYOUT DE LOS ITEMS DEL LSV
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_lsv_workpods, null);

        //DECLARAMOS VARIABLES
        TextView txtNombre=(TextView)view.findViewById(R.id.txtNombre);
        TextView txtEstado = (TextView) view.findViewById(R.id.txtEstado);
        TextView txtNumPersonas = (TextView) view.findViewById(R.id.txtNumPersonas);
        TextView txtUso = (TextView) view.findViewById(R.id.txtUso);
        TextView txtLimpieza = (TextView) view.findViewById(R.id.txtLimpieza);
        ImageView iV_Icon_Historial=(ImageView)view.findViewById(R.id.IV_Icon_Historial);
        ImageView iV_Icon_Limpieza=(ImageView)view.findViewById(R.id.IV_Icon_Limpieza);

        //CONTROL DEL ESTADO
        try {
            if ((lstWorkpods.get(i).getReserva() != null) &&(!lstWorkpods.get(i).isMantenimiento())) {
                txtEstado.setText("Reservado");
                txtEstado.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
            } else if((lstWorkpods.get(i).getReserva()==null)&&(!lstWorkpods.get(i).isMantenimiento())) {
                txtEstado.setText("Disponible");
                txtEstado.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
            }else if(lstWorkpods.get(i).isMantenimiento()){
                txtEstado.setText("Mantenimiento");
                txtEstado.setBackgroundTintList(context.getResources().getColorStateList(R.color.orange));
            }
        } catch (NullPointerException e) {

        }
        txtNumPersonas.setText(String.valueOf(lstWorkpods.get(i).getNumUsuarios()));
        txtNombre.setText(lstWorkpods.get(i).getNombre());

        //CONTROLAMOS QUE NO CASQUE LA APP SI UNA FECHA APUNTA A NULL
        if (lstWorkpods.get(i).getLimpieza() != null) {
            txtLimpieza.setText(String.valueOf(lstWorkpods.get(i).getLimpieza().getDayOfMonth()) + "/" +
                    String.valueOf(lstWorkpods.get(i).getLimpieza().getMonthValue() + "/" + String.valueOf(lstWorkpods.get(i).getLimpieza().getYear())));
        } else {
            txtLimpieza.setText("desconocida");
        }
        if (lstWorkpods.get(i).getUltimoUso() != null) {

            txtUso.setText(String.valueOf(lstWorkpods.get(i).getUltimoUso().getDayOfMonth()) + "/" +
                    String.valueOf(lstWorkpods.get(i).getUltimoUso().getMonthValue() + "/" + String.valueOf(lstWorkpods.get(i).getUltimoUso().getYear())));
        } else {
            txtUso.setText("desconocido");
        }

        //CAMBIO COLOR AL ICONO
        iV_Icon_Historial.setImageTintList(ColorStateList.valueOf(Color.parseColor("#58B1E3")));
        iV_Icon_Limpieza.setImageTintList(ColorStateList.valueOf(Color.parseColor("#58B1E3")));
        return view;
    }
}
