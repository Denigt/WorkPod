package com.example.workpod.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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

import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Fragment_Transaction_History extends Fragment implements View.OnClickListener {

    //ARRAYLIST CON TODAS LAS SESIONES DEL USUARIO
    private ArrayList<LsV_Transaction_History> aLstTransaction = new ArrayList<LsV_Transaction_History>();

    private SimpleDateFormat fecha;

    //XML
    private ListView lsV_Transaction;
    private ImageView iVCalendar;

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
        iVCalendar=(ImageView)view.findViewById(R.id.IVCalendar);

        construyendo_LsV(view);
        iVCalendar.setOnClickListener(this);

        //PARA ACTIVAR EL MENU EMERGENTE
        setHasOptionsMenu(true);
        return view;
    }

    //SOBREESCRITURAS
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.IVCalendar) {
            Fragment_Dialog_Calendar fragment_dialog_calendar = new Fragment_Dialog_Calendar();
            fragment_dialog_calendar.show(getActivity().getSupportFragmentManager(), "DialogGoToCalendar");
        }
    }

    //MÉTODOS

    /**
     * Método para construir el ListView donde se encontrarán las sesiones del usuario con las cabinas workpod
     *
     * @param view instancia de la clase View
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void construyendo_LsV(View view) {

        aLstTransaction.add(new LsV_Transaction_History(0, "C/Pablo Neruda,2", ZonedDateTime.of(2021, Month.JULY.getValue(), 01, 9, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.JULY.getValue(), 01, 14, 30, 22, 0, ZoneId.systemDefault()), Double.parseDouble("28"), "40", "1852 5423 2008 ****"));
        aLstTransaction.add(new LsV_Transaction_History(1, "C/Anaxágoras ,84", ZonedDateTime.of(2021, Month.JULY.getValue(), 23, 15, 45, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.JULY.getValue(), 23, 19, 07, 02, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40", "1852 5423 2008 ****"));
        aLstTransaction.add(new LsV_Transaction_History(2, "C/Rafael Alberti,7", ZonedDateTime.of(2021, Month.JULY.getValue(), 29, 10, 05, 22, 0, ZoneId.systemDefault())
                , ZonedDateTime.of(2021, Month.JULY.getValue(), 29, 12, 27, 33, 0, ZoneId.systemDefault()), Double.parseDouble("25"), "40", "1852 5423 2008 ****"));
        aLstTransaction.add(new LsV_Transaction_History(3, "C/Arquímides,18", ZonedDateTime.of(2021, Month.AUGUST.getValue(), 9, 16, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.AUGUST.getValue(), 9, 19, 23, 22, 0, ZoneId.systemDefault()), Double.parseDouble("17"), "Sin ofertas", "1852 5423 2008 ****"));
        aLstTransaction.add(new LsV_Transaction_History(4, "C/Alejandro Magno,33", ZonedDateTime.of(2021, Month.AUGUST.getValue(), 15, 23, 45, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.AUGUST.getValue(), 16, 03, 01, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40", "1852 5423 2008 ****"));
        aLstTransaction.add(new LsV_Transaction_History(5, "C/Aristóteles ,66", ZonedDateTime.of(2021, Month.SEPTEMBER.getValue(), 07, 17, 05, 52, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.SEPTEMBER.getValue(), 07, 19, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40", "1852 5423 2852 ****"));
        aLstTransaction.add(new LsV_Transaction_History(6, "C/Albert Einstein,27", ZonedDateTime.of(2021, Month.SEPTEMBER.getValue(), 13, 7, 05, 22, 0, ZoneId.systemDefault())
                , ZonedDateTime.of(2021, Month.SEPTEMBER.getValue(), 13, 9, 15, 22, 0, ZoneId.systemDefault()), Double.parseDouble("14"), "40", "1852 5423 2008 ****"));
        aLstTransaction.add(new LsV_Transaction_History(7, "C/Mozart,15", ZonedDateTime.of(2021, Month.SEPTEMBER.getValue(), 17, 18, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.SEPTEMBER.getValue(), 17, 19, 12, 22, 0, ZoneId.systemDefault()), Double.parseDouble("1.20"), "40", "1852 5423 2852 ****"));
        aLstTransaction.add(new LsV_Transaction_History(8, "C/Chopin,2", ZonedDateTime.of(2021, Month.OCTOBER.getValue(), 15, 14, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.OCTOBER.getValue(), 15, 19, 28, 22, 0, ZoneId.systemDefault()), Double.parseDouble("13"), "40", "1852 5423 2008 ****"));
        aLstTransaction.add(new LsV_Transaction_History(9, "C/Chaikoski,47", ZonedDateTime.of(2021, Month.OCTOBER.getValue(), 21, 20, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.OCTOBER.getValue(), 21, 22, 41, 22, 0, ZoneId.systemDefault()), Double.parseDouble("8"), "40", "1852 5423 2852 ****"));
        aLstTransaction.add(new LsV_Transaction_History(10, "C/Pitágoras,73", ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 18, 19, 25, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 19, 8, 15, 12, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40", "1852 5423 2008 ****"));
        aLstTransaction.add(new LsV_Transaction_History(11, "C/Sófocles,48", ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 21, 19, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 21, 19, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40", "1852 5423 2852 ****"));
        aLstTransaction.add(new LsV_Transaction_History(12, "C/Antonio Machado,98", ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 24, 11, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 24, 13, 59, 22, 0, ZoneId.systemDefault()), Double.parseDouble("2.95"), "40", "1852 5423 2008 ****"));
        aLstTransaction.add(new LsV_Transaction_History(13, "C/Stan Lee,23", ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 30, 19, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 30, 19, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40", "1852 5423 2852 ****"));
        aLstTransaction.add(new LsV_Transaction_History(14, "Avnd Sócrates,41", ZonedDateTime.of(2021, Month.DECEMBER.getValue(), 05, 12, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.DECEMBER.getValue(), 05, 19, 18, 22, 0, ZoneId.systemDefault()), Double.parseDouble("9.63"), "40", "1852 5423 2008 ****"));
        aLstTransaction.add(new LsV_Transaction_History(15, "C/Platón,85", ZonedDateTime.of(2021, Month.DECEMBER.getValue(), 14, 19, 55, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.DECEMBER.getValue(), 14, 21, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40", "1852 5423 2852 ****"));

        final Adaptador_LsV_Transaction_History aTransaction = new Adaptador_LsV_Transaction_History(view.getContext(), aLstTransaction);
        lsV_Transaction.setAdapter(aTransaction);
        lsV_Transaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                LsV_Transaction_History lsV_Transaction = (LsV_Transaction_History) aTransaction.getItem(i);
                for (int j = 0; j < aLstTransaction.size(); j++) {
                    if (lsV_Transaction.getCodigo() == j) {
                        Fragment_Dialog_Transaction_Session fragmentDialogTransactionSession = new Fragment_Dialog_Transaction_Session(lsV_Transaction.getUbicacion(), lsV_Transaction.getFechaEntrada(),
                                lsV_Transaction.getFechaSalida(), lsV_Transaction.getOferta(), lsV_Transaction.getPrecio(), lsV_Transaction.getTarjeta());
                        fragmentDialogTransactionSession.show(getActivity().getSupportFragmentManager(), "DialogToCall");
                        break;
                    }
                }
            }
        });
    }
}