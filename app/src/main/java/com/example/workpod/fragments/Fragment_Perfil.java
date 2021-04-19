package com.example.workpod.fragments;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.workpod.R;
import com.example.workpod.adapters.Adaptador_LsV_Transaction_History;
import com.example.workpod.otherclass.LsV_Transaction_History;

import java.util.ArrayList;

public class Fragment_Perfil extends Fragment implements View.OnClickListener {

    // PARAMETROS PARA LA INSTANCIACION DEL FRAGMENT, NO SE USA
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // CONTROLES DEL FRAGMENT
    private ImageButton btnEdit;
    private Button btnShowInfo;
    private TextView txtEmail;
    private LinearLayout lytPrivate;

    // VARIABLES QUE MANEJAN EL ESTADO DEL FRAGMENT
    private boolean showInfo = false;

    public Fragment_Perfil() {
    }

    /**
     * Crea una instancia del Fragmento Perfil
     *
     * @param param1 Parametro 1
     * @param param2 Parametro 2
     * @return Un objeto Fragment_Perfil inicializado segun los parametros pasados
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Perfil newInstance(String param1, String param2) {
        Fragment_Perfil fragment = new Fragment_Perfil();
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
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // BUSQUEDA E INICIALIZACION DE LOS CONTROLES DEL LAYOUT
        btnEdit = view.findViewById(R.id.btnEdit);
        btnShowInfo = view.findViewById(R.id.btnShowInfo);
        txtEmail = view.findViewById(R.id.txtEmail);
        lytPrivate = view.findViewById(R.id.lytPrivate);

        // ASIGNACION DE LOS LISTENERS A LOS CONTROLES
        btnShowInfo.setOnClickListener(this);

        return view;
    }

    // LISTENERS DEL FRAGMENT
    @Override
    public void onClick(View v) {
        btnShowInfoOnClick(v);
    }

    // METODOS ONCLICK DE CADA CONTROL
    private void btnShowInfoOnClick(View v) {
        if (v.getId() == btnShowInfo.getId()){
            showInfo = !showInfo;

            if (showInfo) {
                lytPrivate.setVisibility(View.VISIBLE);
                btnShowInfo.setText("Ocultar información personal");
            }else {
                lytPrivate.setVisibility(View.GONE);
                btnShowInfo.setText("Mostrar información personal");
            }
        }
    }
}