package com.workpodapp.workpod.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.workpodapp.workpod.InitActivity;
import com.workpodapp.workpod.ValoracionWorkpod;
import com.workpodapp.workpod.WorkpodActivity;
import com.workpodapp.workpod.adapters.Adaptador_LsV_Menu_Usuario;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.otherclass.LsV_Menu_Usuario;
import com.workpodapp.workpod.R;

import java.io.File;
import java.util.ArrayList;

public class Fragment_Menu_Usuario extends Fragment implements AdapterView.OnItemClickListener {

    // PARAMETROS PARA LA GESTION DEL LISTVIEW
    private ListView lsV_Menu_Usuario;
    ArrayList<LsV_Menu_Usuario> aLstMU = new ArrayList<>();

    // VARIABLES DE USO PRIVADO SIN GETTERS NI SETTERS
    private Adaptador_LsV_Menu_Usuario aMU;

    public Fragment_Menu_Usuario() {
        // Required empty public constructor
    }

    public static Fragment_Menu_Usuario newInstance(String param1, String param2) {
        Fragment_Menu_Usuario fragment = new Fragment_Menu_Usuario();
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
        View view = inflater.inflate(R.layout.fragment_menu_usuario, container, false);
        //ARMAMOS EL LSV
        lsV_Menu_Usuario = (ListView) view.findViewById(R.id.LsV_Menu_Usuario);
        aLstMU.add(new LsV_Menu_Usuario(0, R.drawable.fill_icon_tarjeta, "Perfil de pago"));
        aLstMU.add(new LsV_Menu_Usuario(1, R.drawable.fill_icon_user, "Perfil"));
        aLstMU.add(new LsV_Menu_Usuario(2, R.drawable.fill_icon_historial, "Histórico de transacciones"));
        aLstMU.add(new LsV_Menu_Usuario(3, R.drawable.fill_icon_settings, "Configuración"));
        aLstMU.add(new LsV_Menu_Usuario(4, R.drawable.fill_icon_phone, "Soporte"));
        aLstMU.add(new LsV_Menu_Usuario(5, R.drawable.fill_icon_friends, "Invita a un amigo"));
        aLstMU.add(new LsV_Menu_Usuario(6, R.drawable.fill_icon_friends, "Canjear códigos de descuento"));
        aLstMU.add(new LsV_Menu_Usuario(7, R.drawable.empty_icon_lock, "Cerrar sesión"));
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
        onClickDescuentos(i);
        onClickCerrar(i);
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
            // ALMACENAR CUAL ES EL FRAGMENT QUE SE MUESTRA AL USUARIO Y CUAL FUE EL ULTIMO MOSTRADO
            InfoFragment.anterior = InfoFragment.actual;
            InfoFragment.actual = InfoFragment.INVITA;

            Fragment_invita_Amigo fragmentInvitaAmigo = new Fragment_invita_Amigo();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentInvitaAmigo).commit();
        }
    }
    private void onClickDescuentos(int index) {
        if (index == InfoFragment.DESCUENTOS) {
            // ALMACENAR CUAL ES EL FRAGMENT QUE SE MUESTRA AL USUARIO Y CUAL FUE EL ULTIMO MOSTRADO
            InfoFragment.anterior = InfoFragment.actual;
            InfoFragment.actual = InfoFragment.DESCUENTOS;
            //CANJEARCODIGOS APUNTA A TRUE AL ACCEDER DESDE EL MENÚ DE USUARIO
            Fragment_Canjear_Codigos.canjearCodigosMU=true;

            Fragment_Canjear_Codigos fragment_canjear_codigos = new Fragment_Canjear_Codigos();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragment_canjear_codigos).commit();
        }
    }
    private void onClickCerrar(int index){
        if (index == InfoFragment.CERRAR) {
            File fileLogin = getActivity().getFileStreamPath(InfoApp.LOGFILE);
            if (fileLogin.delete()){
                Toast.makeText(requireContext(), "Se ha cerrado la sesion", Toast.LENGTH_SHORT).show();
                Intent activity = new Intent(requireContext(), InitActivity.class);
                startActivity(activity);
                getActivity().finish();
            }
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