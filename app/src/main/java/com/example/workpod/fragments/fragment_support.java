package com.example.workpod.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workpod.R;
import com.example.workpod.WorkpodActivity;
import com.example.workpod.adapters.Adaptador_LsV_Menu_Usuario;
import com.example.workpod.adapters.Adaptador_LsV_Support;
import com.example.workpod.otherclass.LsV_Menu_Usuario;
import com.example.workpod.otherclass.LsV_Support;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_support#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_support extends Fragment {
    private TextView tVEmail;
    private TextView tVTlfn;
    private ListView lsV_Support;
    ArrayList<LsV_Support> aLstSupport = new ArrayList<>();

    public fragment_support() {
        // Required empty public constructor
    }


    public static fragment_support newInstance(String param1, String param2) {
        fragment_support fragment = new fragment_support();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        tVTlfn = (TextView) view.findViewById(R.id.iTVSupport);
        lsV_Support = (ListView) view.findViewById(R.id.LsV_Support);
        construyendo_LsV(view);
        return view;
    }



    /**
     * Método para construir el ListView donde se encontrará el email, el tlfn y la dirección de la compañía
     *
     * @param view instancia de la clase View
     */
    public void construyendo_LsV(View view) {

        aLstSupport.add(new LsV_Support(0, R.drawable.fill_icon_interrogacion, "Preguntas Frecuentes"));
        aLstSupport.add(new LsV_Support(1, R.drawable.fill_icon_gmail, "workpodtfg@gmail.com"));
        aLstSupport.add(new LsV_Support(2, R.drawable.fill_icon_llamar, "Contacta por Teléfono "));
        final Adaptador_LsV_Support aSuport = new Adaptador_LsV_Support(view.getContext(), aLstSupport);
        lsV_Support.setAdapter(aSuport);
        lsV_Support.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                LsV_Support lsV_support = (LsV_Support) aSuport.getItem(i);
                if (lsV_support.getCodigo() == 2) {
                    Fragment_Dialog_Call fragmentDialogCall=new Fragment_Dialog_Call();
                    fragmentDialogCall.show(getActivity().getSupportFragmentManager(),"DialogToCall");
                }
            }
        });
    }
}