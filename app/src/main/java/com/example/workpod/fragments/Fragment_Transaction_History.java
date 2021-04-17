package com.example.workpod.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.workpod.R;
import com.example.workpod.adapters.Adaptador_LsV_Transaction_History;
import com.example.workpod.otherclass.LsV_Transaction_History;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Transaction_History#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Transaction_History extends Fragment {
    private ListView lsV_Transaction;
    private ArrayList<LsV_Transaction_History> aLstTransaction = new ArrayList<LsV_Transaction_History>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Transaction_History() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Transaction_History.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Transaction_History newInstance(String param1, String param2) {
        Fragment_Transaction_History fragment = new Fragment_Transaction_History();
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
        View view = inflater.inflate(R.layout.fragment__transaction__history, container, false);
        lsV_Transaction = (ListView) view.findViewById(R.id.LsV_Transaction_History);
        construyendo_LsV(view);
        return view;
    }

    /**
     * Método para construir el ListView donde se encontrará el email, el tlfn y la dirección de la compañía
     *
     * @param view instancia de la clase View
     */
    public void construyendo_LsV(View view) {

        aLstTransaction.add(new LsV_Transaction_History(0, "C/piruletaaa,2", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(1, "C/piruletaaa,2", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(2, "C/piruletaaa,2", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(3, "C/piruletaaa,2", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(4, "C/piruletaaa,2", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(5, "C/piruletaaa,2", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(6, "C/piruletaaa,2", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(7, "C/piruletaaa,2", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(8, "C/piruletaaa,2", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(9, "C/piruletaaa,2", "01-01-2001", "5:20 Horas"));
        final Adaptador_LsV_Transaction_History aTransaction = new Adaptador_LsV_Transaction_History(view.getContext(), aLstTransaction);
        lsV_Transaction.setAdapter(aTransaction);
        lsV_Transaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                LsV_Transaction_History lsV_Transaction = (LsV_Transaction_History) aTransaction.getItem(i);
                if (lsV_Transaction.getCodigo() == 1) {
                    Toast.makeText(getActivity(), "Heyyyy", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}