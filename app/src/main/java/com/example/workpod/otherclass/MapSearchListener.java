package com.example.workpod.otherclass;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.workpod.adapters.Adaptador_Lsv_Search;

public class MapSearchListener implements SearchView.OnQueryTextListener {
    SearchView etxtBusqueda;
    ListView lsvUbicacion;

    public MapSearchListener(SearchView etxtBusqueda, ListView lsvUbicacion) {
        this.etxtBusqueda = etxtBusqueda;
        this.lsvUbicacion = lsvUbicacion;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.equals(""))
            getAdapter().getFilter().filter(newText);
        else getAdapter().getFilter().filter("*_*");

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        etxtBusqueda.clearFocus();
        return false;
    }

    private Adaptador_Lsv_Search getAdapter(){
        return (Adaptador_Lsv_Search) lsvUbicacion.getAdapter();
    }
}
