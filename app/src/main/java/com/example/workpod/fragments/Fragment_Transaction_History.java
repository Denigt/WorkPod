package com.example.workpod.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.workpod.R;
import com.example.workpod.adapters.Adaptador_LsV_Transaction_History;
import com.example.workpod.otherclass.LsV_Transaction_History;

import java.util.ArrayList;

public class Fragment_Transaction_History extends Fragment implements View.OnClickListener {

    //ARRAYLIST CON TODAS LAS SESIONES DEL USUARIO
    private ArrayList<LsV_Transaction_History> aLstTransaction = new ArrayList<LsV_Transaction_History>();
    //ARRAYLIST CON LAS ÚLTIMAS 5 SESIONES DEL USUARIO
    private ArrayList<LsV_Transaction_History> aLstTransactionIniciales = new ArrayList<>();
    //ARRAYLIST CON LA ÚLTIMA SESIÓN DEL USUARIO
    private ArrayList<LsV_Transaction_History> aLstUltimaTransaction = new ArrayList<>();

    //XML
    private ListView lsV_Transaction;
    private ImageView iVTodasSesiones;
    private ImageView iVUltimaSesion;
    private ImageView iVLimpiarSesiones;


    public Fragment_Transaction_History() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__transaction__history, container, false);
        lsV_Transaction = (ListView) view.findViewById(R.id.LsV_Transaction_History);
        iVLimpiarSesiones = (ImageView) view.findViewById(R.id.IVLimpiarSesiones);
        iVTodasSesiones = (ImageView) view.findViewById(R.id.IVTodasSesiones);
        iVUltimaSesion = (ImageView) view.findViewById(R.id.IVUltimaSesion);

        iVUltimaSesion.setOnClickListener(this);
        iVTodasSesiones.setOnClickListener(this);
        iVLimpiarSesiones.setOnClickListener(this);
        construyendo_LsV(view);
        //PARA ACTIVAR EL MENU EMERGENTE
        setHasOptionsMenu(true);
        return view;
    }

    //SOBREESCRITURAS
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.IVTodasSesiones) {
            final Adaptador_LsV_Transaction_History aTransaction = new Adaptador_LsV_Transaction_History(v.getContext(), aLstTransaction);
            lsV_Transaction.setAdapter(aTransaction);
            lsV_Transaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    LsV_Transaction_History lsV_Transaction = (LsV_Transaction_History) aTransaction.getItem(i);
                }
            });
        } else if (v.getId() == R.id.IVUltimaSesion) {
            mostrarUltimaSesion(v);
        } else if (v.getId() == R.id.IVLimpiarSesiones) {
            limpiarSesiones(v);
        }
    }

    //MÉTODOS

    /**
     * Método para construir el ListView donde se encontrarán las sesiones del usuario con las cabinas workpod
     *
     * @param view instancia de la clase View
     */
    public void construyendo_LsV(View view) {

        aLstTransaction.add(new LsV_Transaction_History(0, "C/Pablo Neruda,2", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(1, "C/Rafael Alberti,7", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(2, "C/Albert Einstein,27", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(3, "C/Arquímides,18", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(4, "C/Chaikoski,47", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(5, "C/Antonio Machado,98", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(6, "Avnd Sócrates,41", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(7, "C/Chopin,2", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(8, "C/Mozart,15", "01-01-2001", "5:20 Horas"));
        aLstTransaction.add(new LsV_Transaction_History(9, "C/Stan Lee,23", "01-01-2001", "5:20 Horas"));

        //COPIAMOS EN ESTE ARRAYLIST LAS ÚLTIMAS 5 SESIONES REGISTRADAS
        for (int i = aLstTransaction.size() - 1; i >= (aLstTransaction.size() - 5); i--) {
            aLstTransactionIniciales.add(aLstTransaction.get(i));
        }

        //COPIAMOS EN ESTE ARRAYLIST LA ÚLTIMA SESIÓN DEL USUARIO
        for (int i = aLstTransaction.size() - 1; i >= (aLstTransaction.size() - 1); i--) {
            aLstUltimaTransaction.add(aLstTransaction.get(i));
        }

        //MOSTRAREMOS AL INICIAR EL FRAGMENT EL ARRAY QUE CONTIENE LAS ÚLTIMAS 5 SESIONES
        mostrar5UltimasSesiones(view);
    }

    /**
     * Método para mostrar las últimas 5 sesiones de worpod realizadas por el usuario
     *
     * @param v instancia de la clase View
     */
    public void mostrar5UltimasSesiones(View v) {
        final Adaptador_LsV_Transaction_History aTransaction = new Adaptador_LsV_Transaction_History(v.getContext(), aLstTransactionIniciales);
        lsV_Transaction.setAdapter(aTransaction);
        lsV_Transaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                LsV_Transaction_History lsV_Transaction = (LsV_Transaction_History) aTransaction.getItem(i);
            }
        });
    }

    /**
     * Método para mostrar sólamente la última sesión de workpod realizada por el usuario
     *
     * @param v instancia de la clase View.
     */
    public void mostrarUltimaSesion(View v) {
        final Adaptador_LsV_Transaction_History aTransaction = new Adaptador_LsV_Transaction_History(v.getContext(), aLstUltimaTransaction);
        lsV_Transaction.setAdapter(aTransaction);
        lsV_Transaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                LsV_Transaction_History lsV_Transaction = (LsV_Transaction_History) aTransaction.getItem(i);

            }
        });
    }

    /**
     * Método para limpiar todas las sesiones de workpod realizadas por el usuario.
     *
     * @param v instancia de la clase View
     */
    public void limpiarSesiones(View v) {
        //LIMPIAMOS TODOS LOS ARRAYLIST
        aLstTransaction.clear();
        aLstUltimaTransaction.clear();
        aLstTransactionIniciales.clear();

        //MOSTRAMOS EL ARRAYLIST QUE CONTENDRÍA TODAS LAS SESIONES (QUE ESTARÁ VACÍO)
        final Adaptador_LsV_Transaction_History aTransaction = new Adaptador_LsV_Transaction_History(v.getContext(), aLstTransaction);
        lsV_Transaction.setAdapter(aTransaction);
        lsV_Transaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                LsV_Transaction_History lsV_Transaction = (LsV_Transaction_History) aTransaction.getItem(i);
            }
        });
    }
}