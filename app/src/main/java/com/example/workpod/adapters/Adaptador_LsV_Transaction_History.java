package com.example.workpod.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.otherclass.LsV_Transaction_History;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Adaptador_LsV_Transaction_History extends BaseAdapter {

    //VARIABLES PARA CONSTRUIR EL LISTVIEW
    Context context;
    ArrayList<LsV_Transaction_History> aLstTransactions = new ArrayList<>();

    //VARIABLES PARA EL CÁLCULO DEL TIEMPO DE SESIÓN DEL USUARIO EN WORKPOD
    private int hour;
    private int min;
    private int seg;

    public Adaptador_LsV_Transaction_History() {
    }

    public Adaptador_LsV_Transaction_History(Context context, ArrayList<LsV_Transaction_History> aLstTransactions) {
        this.context = context;
        this.aLstTransactions = aLstTransactions;
        hour = 0;
        min = 0;
        seg = 0;
    }

    @Override
    public int getCount() {
        return aLstTransactions.size();
    }

    @Override
    public Object getItem(int i) {
        return aLstTransactions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return aLstTransactions.get(i).getCodigo();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_lsv_transaction_history, null);
        TextView tVUbicacion;
        TextView tVFecha;
        TextView tVTiempo;
        tVUbicacion = (TextView) view.findViewById(R.id.iTVUbicacion);
        tVFecha = (TextView) view.findViewById(R.id.iTVFecha);
        tVTiempo = (TextView) view.findViewById(R.id.iTVHora);
        
        //ATRIBUTOS QUE VAMOS A USAR EN EL LSV DEL HISTÓRICO DE TRANSACCIONES
        tVUbicacion.setText(aLstTransactions.get(i).getUbicacion());
        //APLICAMOS EL MÉTODO DE CALCULAR EL TIEMPO DE SESIÓN DEL USUARIO
        calcularTiempoSesion(aLstTransactions.get(i).getFechaEntrada(),aLstTransactions.get(i).getFechaSalida(),hour,min
        ,seg);
        tVFecha.setText(String.valueOf(aLstTransactions.get(i).getFechaEntrada().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        tVTiempo.setText(hour+"h:"+min+"min");

        return view;
    }

    //MÉTODOS

    /**
     * Método para calcular la diferencia entre 2 fechas.
     * La clase ZoneDateTime no posee el método getTime() el cual permite calcular los milisegundos entre dos fechas,
     * es el método más fácil para calcular el tiempo entre 2 fechas. Para ello, parseamos nuestras fechas a Date y
     * aplicalamos los calculos respectivos para transformar las diferencias de milisegundos entre 2 fechas en horas, minutos y segundos
     *
     * @param fechaEntrada fecha de entrada al workpod
     * @param fechaSalida fecha de salida del workpod
     * @param hour   horas que ha durado la sesión de workpod
     * @param min    minutos que ha durado la sesión de workpod
     * @param seg    segundos que ha durado la sesión de workpod
     */
    private void calcularTiempoSesion(ZonedDateTime fechaEntrada, ZonedDateTime fechaSalida, int hour, int min, int seg) {
        Date fecha1 = Date.from(fechaEntrada.toInstant());
        Date fecha2 = Date.from(fechaSalida.toInstant());
        long dif = fecha2.getTime() - fecha1.getTime();
        seg = (int) (dif / 1000) % 60;
        min = (int) ((dif / (1000 * 60)) % 60);
        hour = (int) ((dif / (1000 * 60 * 60)) % 24);
        //LE PASAMOS LOS VALORES CALCULADOS A LAS VARIABLES QUE USAMOS FUERA DEL METODO
        this.hour = hour;
        this.min = min;
        //AGREGAMOS EL TIEMPO DE SESIÓN DE TRABAJO EN UN WORKPOD DEL USUARIO

    }
}
