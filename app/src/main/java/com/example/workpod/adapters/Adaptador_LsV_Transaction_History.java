package com.example.workpod.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.otherclass.LsV_Transaction_History;

import java.util.ArrayList;

public class Adaptador_LsV_Transaction_History extends BaseAdapter {

    Context context;
    ArrayList<LsV_Transaction_History> aLstTransactions = new ArrayList<>();

    public Adaptador_LsV_Transaction_History() {
    }

    public Adaptador_LsV_Transaction_History(Context context, ArrayList<LsV_Transaction_History> aLstTransactions) {
        this.context = context;
        this.aLstTransactions = aLstTransactions;
    }

    @Override
    public int getCount() {
        return aLstTransactions.size();
    }

    @Override
    public Object getItem(int i) {
        return aLstTransactions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return aLstTransactions.get(i).getCodigo();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_lsv_transaction_history, null);
        TextView tVUbicacion;
        TextView tVFecha;
        TextView tVHora;
        tVUbicacion = (TextView) view.findViewById(R.id.iTVUbicacion);
        tVFecha = (TextView) view.findViewById(R.id.iTVFecha);
        tVHora = (TextView) view.findViewById(R.id.iTVHora);
        tVUbicacion.setText(aLstTransactions.get(i).getUbicacion());
        tVFecha.setText(aLstTransactions.get(i).getFecha());
        tVHora.setText(aLstTransactions.get(i).getHora());
        return view;
    }
}
