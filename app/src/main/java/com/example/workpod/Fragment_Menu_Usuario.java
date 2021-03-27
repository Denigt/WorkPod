package com.example.workpod;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Menu_Usuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Menu_Usuario extends Fragment {

    private ListView lSV_Menu_Usuario;
    ArrayList<LsV_Menu_Usuario>aLstMU=new ArrayList<>();
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
        View view=inflater.inflate(R.layout.fragment_menu_usuario,container,false);
        //ARMAMOS EL LSV
        lSV_Menu_Usuario=(ListView)view.findViewById(R.id.LsV_Menu_Usuario);
        aLstMU.add(new LsV_Menu_Usuario(0,R.drawable.ic_payment_profile,"Perfil de pago"));
        aLstMU.add(new LsV_Menu_Usuario(1,R.drawable.ic_perfil,"Perfil"));
        aLstMU.add(new LsV_Menu_Usuario(2,R.drawable.ic_transaction_history,"Histórico de transacciones"));
        aLstMU.add(new LsV_Menu_Usuario(3,R.drawable.ic_configuration,"Configuración"));
        aLstMU.add(new LsV_Menu_Usuario(4,R.drawable.ic_support,"Soporte"));
        aLstMU.add(new LsV_Menu_Usuario(5,R.drawable.ic_invited_friend,"Invita a un amigo"));
        final Adaptador_LsV_Menu_Usuario aMU=new Adaptador_LsV_Menu_Usuario(view.getContext(),aLstMU);
        lSV_Menu_Usuario.setAdapter(aMU);
        lSV_Menu_Usuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                LsV_Menu_Usuario lsV_menu_usuario=(LsV_Menu_Usuario)aMU.getItem(i);
                if(lsV_menu_usuario.getCodigo()==1){
                    Toast.makeText(view.getContext(), "Hola Mundo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}