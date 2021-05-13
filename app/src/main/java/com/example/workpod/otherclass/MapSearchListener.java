package com.example.workpod.otherclass;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import com.example.workpod.adapters.Adaptador_Lsv_Search;

public class MapSearchListener implements SearchView.OnQueryTextListener {
    Adaptador_Lsv_Search adpResultados;

    public MapSearchListener(Adaptador_Lsv_Search adpResultados) {
        this.adpResultados = adpResultados;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.equals(""))
            adpResultados.getFilter().filter(newText);
        else adpResultados.getFilter().filter("*_*");

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
