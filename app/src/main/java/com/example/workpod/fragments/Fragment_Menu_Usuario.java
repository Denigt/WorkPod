package com.example.workpod.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.workpod.WorkpodActivity;
import com.example.workpod.adapters.Adaptador_LsV_Menu_Usuario;
import com.example.workpod.otherclass.LsV_Menu_Usuario;
import com.example.workpod.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Menu_Usuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Menu_Usuario extends Fragment {

    private ListView lsV_Menu_Usuario;
    private FragmentTransaction fTransaction;
    ArrayList<LsV_Menu_Usuario> aLstMU = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        final Adaptador_LsV_Menu_Usuario aMU = new Adaptador_LsV_Menu_Usuario(view.getContext(), aLstMU);
        lsV_Menu_Usuario.setAdapter(aMU);
        lsV_Menu_Usuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                LsV_Menu_Usuario lsV_menu_usuario = (LsV_Menu_Usuario) aMU.getItem(i);
                if (lsV_menu_usuario.getCodigo() == 1) {
                    Toast.makeText(view.getContext(), "Hola Mundo", Toast.LENGTH_SHORT).show();
                }else if(lsV_menu_usuario.getCodigo()==2){
                    Fragment_Transaction_History fragmentTransaction = new Fragment_Transaction_History();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentTransaction).commit();
                    //CAMBIAMOS LA SELECCIÓN AL ICONO DE SOPORTE
                    WorkpodActivity.btnNV.setSelectedItemId(R.id.inv_folder);
                    WorkpodActivity.boolLoc=false;
                } else if (lsV_menu_usuario.getCodigo() == 4) {
                    fragment_support fragmentSupport = new fragment_support();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSupport).commit();
                    //CAMBIAMOS LA SELECCIÓN AL ICONO DE SOPORTE
                    WorkpodActivity.btnNV.setSelectedItemId(R.id.inv_support);
                    WorkpodActivity.boolLoc=false;
                }
            }
        });
        return view;
    }
}