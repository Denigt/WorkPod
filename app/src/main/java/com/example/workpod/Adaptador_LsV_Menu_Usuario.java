package com.example.workpod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_LsV_Menu_Usuario extends BaseAdapter {
    Context context;
    ArrayList<LsV_Menu_Usuario>aLstMenuUsuario=new ArrayList<>();

    public Adaptador_LsV_Menu_Usuario() {
    }

    public Adaptador_LsV_Menu_Usuario(Context context, ArrayList<LsV_Menu_Usuario> aLstMenuUsuario) {
        this.context = context;
        this.aLstMenuUsuario = aLstMenuUsuario;
    }

    @Override
    public int getCount() {
        return aLstMenuUsuario.size();
    }

    @Override
    public Object getItem(int i) {
        return aLstMenuUsuario.get(i);
    }

    @Override
    public long getItemId(int i) {
        return aLstMenuUsuario.get(i).getCodigo();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        view=inflater.inflate(R.layout.item_lsv_menu_usuario,null);
        TextView tVIcono;
        ImageView iVIcono;
        tVIcono=(TextView)view.findViewById(R.id.iTV);
        iVIcono=(ImageView)view.findViewById(R.id.iIV);
        tVIcono.setText(aLstMenuUsuario.get(i).getTexto());
        iVIcono.setImageResource(aLstMenuUsuario.get(i).getIcono());
        return view;
    }
}
