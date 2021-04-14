package com.example.workpod.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.workpod.R;
import com.example.workpod.ValoracionWorkpod;
import com.example.workpod.WorkpodActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_sesion_finalizada#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_sesion_finalizada extends Fragment {

    //XML
    private Button btnCerrarWorPod;
    private Button btnContactarSoporte;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_sesion_finalizada() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_sesion_finalizada.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_sesion_finalizada newInstance(String param1, String param2) {
        Fragment_sesion_finalizada fragment = new Fragment_sesion_finalizada();
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
        View view=inflater.inflate(R.layout.fragment_sesion_finalizada,container,false);
        btnCerrarWorPod=(Button)view.findViewById(R.id.BtnCerrarWorPod);
        btnContactarSoporte=(Button)view.findViewById(R.id.BtnContactarSoporte);
        btnCerrarWorPod.setBackgroundColor(Color.parseColor("#DA4B4B"));
        btnContactarSoporte.setBackgroundColor(Color.parseColor("#C3A240"));
        btnCerrarWorPod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(getActivity(), ValoracionWorkpod.class);
                startActivity(activity);
            }
        });
        return view;
    }


}