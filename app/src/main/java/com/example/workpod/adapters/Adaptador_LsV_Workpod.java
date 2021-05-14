package com.example.workpod.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        view = inflater.inflate(R.layout.temp_items_lsv_worpods, null);

        //DECLARAMOS VARIABLES
        TextView iTvNombreWorkpod = (TextView) view.findViewById(R.id.iTVNombreWorkpod);
        TextView iTvDisponibilidad = (TextView) view.findViewById(R.id.iTVDisponibilidad);

        iTvNombreWorkpod.setText(lstWorkpods.get(i).getNombre());
       try{
            if(lstWorkpods.get(i).getReserva()!=null){
                iTvDisponibilidad.setText("No Disponible");
            }else{
                iTvDisponibilidad.setText("Disponible");
            }
        }catch(NullPointerException e){

        }

        return view;
    }
}
