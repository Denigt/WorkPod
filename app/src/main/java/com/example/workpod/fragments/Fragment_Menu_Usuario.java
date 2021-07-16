package com.example.workpod.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.workpod.ValoracionWorkpod;
import com.example.workpod.WorkpodActivity;
import com.example.workpod.adapters.Adaptador_LsV_Menu_Usuario;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.otherclass.LsV_Menu_Usuario;
import com.example.workpod.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Menu_Usuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Menu_Usuario extends Fragment implements AdapterView.OnItemClickListener {

    // PARAMETROS PARA LA GESTION DEL LISTVIEW
    private ListView lsV_Menu_Usuario;
    ArrayList<LsV_Menu_Usuario> aLstMU = new ArrayList<>();

    // PARAMETROS PARA EL FRAGMENT, NO SE USA, SE PUEDE BORRAR
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // VARIABLES DE USO PRIVADO SIN GETTERS NI SETTERS
    private Adaptador_LsV_Menu_Usuario aMU;


    public Fragment_Menu_Usuario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuUsuario.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Menu_Usuario newInstance(String param1, String param2) {
        Fragment_Menu_Usuario fragment = new Fragment_Menu_Usuario();
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
        View view = inflater.inflate(R.layout.fragment_menu_usuario, container, false);
        //ARMAMOS EL LSV
        lsV_Menu_Usuario = (ListView) view.findViewById(R.id.LsV_Menu_Usuario);
        aLstMU.add(new LsV_Menu_Usuario(0, R.drawable.fill_icon_tarjeta, "Perfil de pago"));
        aLstMU.add(new LsV_Menu_Usuario(1, R.drawable.fill_icon_user, "Perfil"));
        aLstMU.add(new LsV_Menu_Usuario(2, R.drawable.fill_icon_historial, "Histórico de transacciones"));
        aLstMU.add(new LsV_Menu_Usuario(3, R.drawable.fill_icon_settings, "Configuración"));
        aLstMU.add(new LsV_Menu_Usuario(4, R.drawable.fill_icon_phone, "Soporte"));
        aLstMU.add(new LsV_Menu_Usuario(5, R.drawable.fill_icon_friends, "Invita a un amigo"));
        aMU = new Adaptador_LsV_Menu_Usuario(view.getContext(), aLstMU);
        lsV_Menu_Usuario.setAdapter(aMU);
        lsV_Menu_Usuario.setOnItemClickListener(this);

        //REMARCAR EL ICONO DEL NV (SOLO PARA CUANDO EL USUARIO ESTÉ EN UNA SESIÓN)
        if (InfoApp.USER.getReserva() != null) {
            if (InfoApp.USER.getReserva().getEstado().equalsIgnoreCase("En Uso") && (!ValoracionWorkpod.boolReservaFinalizada)) {
                WorkpodActivity.btnNV.getMenu().findItem(R.id.inv_menu_user).setChecked(true);
            }
        }

        return view;
    }

    // LISTENERS
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        onClickPago(i);
        onClickPerfil(i);
        onClickTransacciones(i);
        onClickConfiguracion(i);
        onClickSoporte(i);
        onClickInvita(i);
    }

    // EVENTOS ON CLICK PARA LOS ITEMS DEL LISTVIEW
    private void onClickPago(int index){
        if (index == InfoFragment.PAGO) {

        }
    }

    private void onClickPerfil(int index){
        if (index == InfoFragment.PERFIL) {
            // ALMACENAR CUAL ES EL FRAGMENT QUE SE MUESTRA AL USUARIO Y CUAL FUE EL ULTIMO MOSTRADO
            InfoFragment.anterior = InfoFragment.actual;
            InfoFragment.actual = InfoFragment.PERFIL;

            Fragment_Perfil fragmentTransaction = new Fragment_Perfil();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentTransaction).commit();
        }
    }

    private void onClickTransacciones(int index){
        if (index == InfoFragment.TRANSACCIONES) {
            // ALMACENAR CUAL ES EL FRAGMENT QUE SE MUESTRA AL USUARIO Y CUAL FUE EL ULTIMO MOSTRADO
            InfoFragment.anterior = InfoFragment.actual;
            InfoFragment.actual = InfoFragment.TRANSACCIONES;

            Fragment_Transaction_History fragmentTransaction = new Fragment_Transaction_History();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentTransaction).commit();
            //CAMBIAMOS LA SELECCIÓN AL ICONO DE TRANSACCIONES
            WorkpodActivity.btnNV.setSelectedItemId(R.id.inv_folder);
            WorkpodActivity.boolLoc=false;
        }
    }

    private void onClickConfiguracion(int index){
        if (index == InfoFragment.CONFIGURACION) {
            // ALMACENAR CUAL ES EL FRAGMENT QUE SE MUESTRA AL USUARIO Y CUAL FUE EL ULTIMO MOSTRADO
            InfoFragment.anterior = InfoFragment.actual;
            InfoFragment.actual = InfoFragment.CONFIGURACION;
        }
    }

    private void onClickSoporte(int index){
        if (index == InfoFragment.SOPORTE) {
            // ALMACENAR CUAL ES EL FRAGMENT QUE SE MUESTRA AL USUARIO Y CUAL FUE EL ULTIMO MOSTRADO
            InfoFragment.anterior = InfoFragment.actual;
            InfoFragment.actual = InfoFragment.SOPORTE;

            fragment_support fragmentSupport = new fragment_support();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSupport).commit();
            //CAMBIAMOS LA SELECCIÓN AL ICONO DE SOPORTE
            WorkpodActivity.btnNV.setSelectedItemId(R.id.inv_support);
            WorkpodActivity.boolLoc=false;
        }
    }

    private void onClickInvita(int index){
        if (index == InfoFragment.INVITA) {
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (InfoApp.USER.getReserva() != null) {
                if (InfoApp.USER.getReserva().getEstado().equalsIgnoreCase("En Uso") && (!ValoracionWorkpod.boolReservaFinalizada)) {
                    WorkpodActivity.btnNV.getMenu().findItem(R.id.inv_location).setChecked(true);
                }
            }
            super.onDestroy();
        } catch (NullPointerException e) {
            super.onDestroy();
        }
    }
}