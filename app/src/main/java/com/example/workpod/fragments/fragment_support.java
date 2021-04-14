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
    private static final int PERMISO_LLAMADA = 50;
    private TextView tVEmail;
    private TextView tVTlfn;
    private ListView lsV_Support;
    ArrayList<LsV_Support> aLstSupport = new ArrayList<>();
    //private Link

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_support() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_support.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_support newInstance(String param1, String param2) {
        fragment_support fragment = new fragment_support();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        //  tVEmail = (TextView) view.findViewById(R.id.TVEmail);
        tVTlfn = (TextView) view.findViewById(R.id.iTVSupport);
        lsV_Support = (ListView) view.findViewById(R.id.LsV_Support);
//        tVEmail.setText(Html.fromHtml("<a href=\"mailto:workpodtfg@gmail.com\">workpodtfg@gmail.com </a>"));
        //CAMBIAR EL COLOR DEL LINK
        // tVEmail.setLinkTextColor(Color.WHITE);
        //   tVEmail.setMovementMethod(LinkMovementMethod.getInstance());

      /*tVTlfn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamar(v);
            }
        });*/
        construyendo_LsV(view);
        return view;


    }

    /**
     * Método que nos permitirá realizar una llamada al número especificado en el TextView
     *
     * @param v instancia de la clase View
     */
    public void llamar(View v) {
        try {
            //VERIFICAMOS SI TENEMOS LOS PERMISOS PARA LLAMAR
            int permiso_Llamada = ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE);
            if (permiso_Llamada != PackageManager.PERMISSION_GRANTED) {
                //  Toast.makeText(v.getContext(), "No tienes permiso para llamar", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISO_LLAMADA);
            } else {
                Log.i("Mensaje", "Se tiene permiso para llamar");
                //COMPROBAMOS QUE EL Nº DE TLFN NO ESTÉ VACÍO.
                String dial = "tel:618.950.208";//PONER OBLIGATORIAMENTE tel
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));//ESTO SERÁ LA LLAMADA

            }
        } catch (Exception e) {

        }
    }

    public void construyendo_LsV(View view) {

        aLstSupport.add(new LsV_Support(0, R.drawable.fill_icon_gmail, "workpodtfg@gmail.com"));
        aLstSupport.add(new LsV_Support(1, R.drawable.fill_icon_llamar, "618.950.208"));
        aLstSupport.add(new LsV_Support(2, R.drawable.fill_icon_direccion, "Calle Falsa 1 2 3"));
        final Adaptador_LsV_Support aSuport = new Adaptador_LsV_Support(view.getContext(), aLstSupport);
        lsV_Support.setAdapter(aSuport);
        lsV_Support.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                LsV_Support lsV_support = (LsV_Support) aSuport.getItem(i);
                if (lsV_support.getCodigo() == 1) {
                    llamar(view);
                }
            }
        });
    }
}