package com.example.workpod.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.workpod.R;
import com.example.workpod.data.Ubicacion;
import com.example.workpod.otherclass.LsV_Menu_Usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adaptador_Lsv_Search extends BaseAdapter implements Filterable {
    private Context context;
    private int resource;
    private List<Ubicacion> lstUbicacion;
    private List<Ubicacion> lstUbicacionFilter;
    private ItemFilter filter = new ItemFilter();

    public Adaptador_Lsv_Search(@NonNull Context context, int resource) {
        this.context = context;
        this.resource = resource;
        lstUbicacion = new ArrayList<>();
        lstUbicacionFilter = new ArrayList<>();
    }

    public Adaptador_Lsv_Search(@NonNull Context context, int resource, @NonNull List<Ubicacion> objects) {
        this.context = context;
        this.resource = resource;
        lstUbicacion = objects;
        lstUbicacionFilter = objects;
    }

    @Override
    public Object getItem(int position) {
        return lstUbicacionFilter.get(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public int getCount() {
        return lstUbicacionFilter.size();
    }

    public void addAll(List<Ubicacion> collection) {
        lstUbicacion = collection;
    }

    public String getItemDireccion(int position) {
        return lstUbicacionFilter.get(position).getDireccion().toShortString();
    }

    public List<Ubicacion> getLstUbicacion() {
        return lstUbicacion;
    }

    public List<String> getLstDireccion() {
        List<String> lstDireccion = new ArrayList<>();
        for (Ubicacion ubc: lstUbicacion) {
            lstDireccion.add(ubc.getDireccion().toLongString());
        }
        return lstDireccion;
    }

    @Override
    public long getItemId(int i) {
        return lstUbicacion.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        Holder views;

        if (view == null) {
            views = new Holder();

            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);

            // BUSCAR LAS VISTAS DEL LAYOUT
            views.txtDireccion = view.findViewById(R.id.txtDireccion);
            views.txtNumWorkpod = view.findViewById(R.id.txtNumWorkpod);

            view.setTag(views);
        } else {
            views = (Holder) view.getTag();
        }
        // ESCRIBIR TEXTO EN LAS MISMAS
        views.txtDireccion.setText(getItemDireccion(i));
        views.txtNumWorkpod.setText(String.valueOf(lstUbicacionFilter.get(i).getWorkpods().size()));
        return view;
    }

    private static class Holder {
        TextView txtDireccion;
        TextView txtNumWorkpod;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Ubicacion> list = lstUbicacion;

            int count = list.size();
            final ArrayList<Ubicacion> nlist = new ArrayList<>(count);

            Ubicacion filterableUbicacion ;

            for (int i = 0; i < count; i++) {
                filterableUbicacion = list.get(i);
                if (filterableUbicacion.getDireccion().toLongString().equalsIgnoreCase(filterString)){
                    nlist.clear();
                    break;
                }
                if (filterableUbicacion.getDireccion().toLongString().toLowerCase().contains(filterString)) {
                    nlist.add(filterableUbicacion);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lstUbicacionFilter = (ArrayList<Ubicacion>) results.values;
            notifyDataSetChanged();
        }
    }
}
