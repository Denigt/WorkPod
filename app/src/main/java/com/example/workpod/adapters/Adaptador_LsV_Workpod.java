package com.example.workpod.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.workpod.R;
import com.example.workpod.data.Workpod;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    //SOBREESCRITURAS
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        //INFLAMOS EL LAYOUT DE LOS ITEMS DEL LSV
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_lsv_workpods, null);
        //DECLARAMOS VARIABLES
        LinearLayout lLEstadoLsV = (LinearLayout) view.findViewById(R.id.LLEstadoLsV);
        TextView txtNombre = (TextView) view.findViewById(R.id.txtNombre);
        TextView txtEstado = (TextView) view.findViewById(R.id.txtEstado);
        TextView txtNumPersonas = (TextView) view.findViewById(R.id.txtNumPersonas);
        TextView txtUso = (TextView) view.findViewById(R.id.txtUso);
        TextView txtLimpieza = (TextView) view.findViewById(R.id.txtLimpieza);
        ImageView iV_Icon_Historial = (ImageView) view.findViewById(R.id.IV_Icon_Historial);
        ImageView iV_Icon_Limpieza = (ImageView) view.findViewById(R.id.IV_Icon_Limpieza);

        //CONTROL DEL ESTADO
        try {
            if (lstWorkpods.get(i).isMantenimiento()) {
                txtEstado.setText("Mantenimiento");
                //APIS SUPERIORES A LA 21
                txtEstado.setBackgroundTintList(context.getResources().getColorStateList(R.color.orange));
                //API 21
                txtEstado.setBackground(context.getDrawable(R.drawable.rounded_back_button_orange));
            } else if ((lstWorkpods.get(i).getReserva() == 0)) {
                txtEstado.setText("Disponible");
                txtEstado.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
            } else {
                txtEstado.setText("Reservado");
                //APIS SUPERIORES A LA 21
                txtEstado.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
                //API 21
                txtEstado.setBackground(context.getDrawable(R.drawable.rounded_back_button_red));
            }
        } catch (NullPointerException e) {

        }
        txtNumPersonas.setText(String.valueOf(lstWorkpods.get(i).getNumUsuarios()));
        txtNombre.setText(lstWorkpods.get(i).getNombre());

        //CONTROLAMOS QUE NO CASQUE LA APP SI UNA FECHA APUNTA A NULL
        if (lstWorkpods.get(i).getLimpieza() != null) {
            txtLimpieza.setText(String.valueOf(lstWorkpods.get(i).getLimpieza().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"))));
        } else {
            txtLimpieza.setText("");
            iV_Icon_Limpieza.setVisibility(View.GONE);
        }
        if (lstWorkpods.get(i).getUltimoUso() != null) {

            txtUso.setText(String.valueOf(lstWorkpods.get(i).getUltimoUso().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"))));
        } else {
            txtUso.setText("");
            iV_Icon_Historial.setVisibility(View.GONE);
        }

        //CAMBIO COLOR AL ICONO
        iV_Icon_Historial.setImageTintList(ColorStateList.valueOf(Color.parseColor("#58B1E3")));
        iV_Icon_Limpieza.setImageTintList(ColorStateList.valueOf(Color.parseColor("#58B1E3")));
        return view;
    }
}
