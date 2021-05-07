package com.example.workpod.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.otherclass.Spinner_Years_Transaction_History;

import java.util.ArrayList;
import java.util.List;

public class Adaptador_Spinner extends BaseAdapter {
    Context context;
    List<Spinner_Years_Transaction_History> lstSpinner=new ArrayList<>();

    //CONSTRUCTOR POR DEFECTO
    public Adaptador_Spinner() {
    }

    //CONSTRUCTOR CON LOS PARÁMETROS
    public Adaptador_Spinner(Context context, List<Spinner_Years_Transaction_History> lstSpinner) {
        this.context = context;
        this.lstSpinner = lstSpinner;
    }

    @Override
    public int getCount() {
        return lstSpinner.size();
    }

    @Override
    public Object getItem(int i) {
        return lstSpinner.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstSpinner.get(i).getCodigo();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        view=inflater.inflate(R.layout.item_spinner_years_transaction_history,null);
        //CREAMOS LOS ELEMENTOS QUE TENDRÁ EL XML DEL SPINNER
        TextView TVSpinnerYearSesion;
        //INSTANCIAMOS LOS ELEMENTOS
        TVSpinnerYearSesion=(TextView)view.findViewById(R.id.TVSpinnerYearSesion);
        //LE PASAMOS EL AÑO COMO TÍTULO AL SPINNER
        TVSpinnerYearSesion.setText(lstSpinner.get(i).getTitulo());
        return view;
    }
}
