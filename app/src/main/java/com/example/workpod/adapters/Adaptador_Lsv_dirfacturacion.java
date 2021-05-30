package com.example.workpod.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.data.Facturacion;

import java.util.List;

public class Adaptador_Lsv_dirfacturacion extends BaseExpandableListAdapter {
    Context context;
    List<Facturacion> lstFacturacion;

    public Adaptador_Lsv_dirfacturacion(Context context, List<Facturacion> lstDatos){
        this.context = context;
        this.lstFacturacion = lstDatos;
    }

    @Override
    public int getGroupCount() {
        return lstFacturacion.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return lstFacturacion.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lstFacturacion.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return lstFacturacion.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return lstFacturacion.get(groupPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Facturacion facturacion = (Facturacion) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.group_lsv_dirfacturacion, null);
        }
        TextView txtNombre = convertView.findViewById(R.id.txtNombre);

        txtNombre.setText(facturacion.getNombre());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Facturacion facturacion = (Facturacion) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_lsv_dirfacturacion, null);
        }
        TextView txtDireccion = convertView.findViewById(R.id.txtDireccion);
        TextView txtProvincia = convertView.findViewById(R.id.txtProvincia);
        TextView txtPais = convertView.findViewById(R.id.txtPais);
        TextView txtCodPostal = convertView.findViewById(R.id.txtCodPostal);

        TextView txtDireccionF = convertView.findViewById(R.id.txtDireccionF);
        TextView txtProvinciaF = convertView.findViewById(R.id.txtProvinciaF);
        TextView txtPaisF = convertView.findViewById(R.id.txtPaisF);
        TextView txtCodPostalF = convertView.findViewById(R.id.txtCodPostalF);

        txtDireccion.setText(facturacion.getPostal().getDireccion());
        txtProvincia.setText(String.valueOf(facturacion.getPostal().getProvincia()));
        txtPais.setText(facturacion.getPostal().getPais());
        txtCodPostal.setText(String.valueOf(facturacion.getPostal().getCodPostal()));

        txtDireccionF.setText(facturacion.getFacturacion().getDireccion());
        txtProvinciaF.setText(String.valueOf(facturacion.getFacturacion().getProvincia()));
        txtPaisF.setText(facturacion.getFacturacion().getPais());
        txtCodPostalF.setText(String.valueOf(facturacion.getFacturacion().getCodPostal()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
