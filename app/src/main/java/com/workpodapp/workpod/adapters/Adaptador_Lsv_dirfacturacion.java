package com.workpodapp.workpod.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.workpodapp.workpod.R;
import com.workpodapp.workpod.data.Facturacion;

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
        return (lstFacturacion != null)? lstFacturacion.size() : 0;
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
        if (lstFacturacion != null) {
            Facturacion facturacion = (Facturacion) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.group_lsv_dirfacturacion, null);
            }
            TextView txtNombre = convertView.findViewById(R.id.txtNombre);

            txtNombre.setText(facturacion.getNombre());
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (lstFacturacion != null) {
            Facturacion facturacion = (Facturacion) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.item_lsv_dirfacturacion, null);
            }
            TextView txtDireccion = convertView.findViewById(R.id.txtDireccion);
            TextView txtPais = convertView.findViewById(R.id.txtPais);

            TextView txtDireccionF = convertView.findViewById(R.id.txtDireccionF);
            TextView txtPaisF = convertView.findViewById(R.id.txtPaisF);

            txtDireccion.setText(facturacion.getPostal().getDireccion());
            txtPais.setText(facturacion.getPostal().getPais() + " " + facturacion.getPostal().getCodPostal());

            txtDireccionF.setText(facturacion.getFacturacion().getDireccion());
            txtPaisF.setText(facturacion.getFacturacion().getPais() + " " + facturacion.getFacturacion().getCodPostal());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
