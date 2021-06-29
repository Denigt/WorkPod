package com.example.workpod.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.workpod.R;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.basic.Method;
import com.example.workpod.data.Workpod;
import com.example.workpod.scale.Scale_Buttons;
import com.example.workpod.scale.Scale_TextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Adaptador_LsV_Workpod extends BaseAdapter {
    //DECLARAMOS VARIABLES
    Context context;
    List<Workpod> lstWorkpods = new ArrayList<>();
    private DisplayMetrics metrics;

    //VARIABLES ITEM
    TextView txtNombre;
    TextView txtEstado;
    TextView txtNumPersonas;
    TextView txtUso;
    TextView txtLimpieza;
    private int idWorkpodUsuario;

    //COLECCIONES
    private List<Scale_TextView> lstTv;

    //CONSTRUCTOR POR DEFECTO
    public Adaptador_LsV_Workpod() {
    }

    //CONSTRUCTOR CON TODOS LOS PARAMETROS

    public Adaptador_LsV_Workpod(Context context, List<Workpod> lstWorkpods, DisplayMetrics metrics) {
        this.context = context;
        this.lstWorkpods = lstWorkpods;
        this.metrics = metrics;
        this.idWorkpodUsuario = 0;

    }

    //SOBREESCRITURAS
    @Override
    public int getCount() {
        return lstWorkpods.size();
    }

    @Override
    public Object getItem(int i) {
        return lstWorkpods.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstWorkpods.get(i).getId();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        //INFLAMOS EL LAYOUT DE LOS ITEMS DEL LSV
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_lsv_workpods, null);
        //DECLARAMOS VARIABLES
        LinearLayout lLEstadoLsV = (LinearLayout) view.findViewById(R.id.LLEstadoLsV);
        txtNombre = (TextView) view.findViewById(R.id.txtNombre);
        txtEstado = (TextView) view.findViewById(R.id.txtEstado);
        txtNumPersonas = (TextView) view.findViewById(R.id.txtNumPersonas);
        txtUso = (TextView) view.findViewById(R.id.txtUso);
        txtLimpieza = (TextView) view.findViewById(R.id.txtLimpieza);
        ImageView iV_Icon_Historial = (ImageView) view.findViewById(R.id.IV_Icon_Historial);
        ImageView iV_Icon_Limpieza = (ImageView) view.findViewById(R.id.IV_Icon_Limpieza);

        //CONTROL DEL ESTADO
        try {
            if (lstWorkpods.get(i).isMantenimiento()) {
                txtEstado.setText("Mantenimiento");
                //APIS SUPERIORES A LA 21
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    txtEstado.setBackgroundTintList(context.getResources().getColorStateList(R.color.orange));
                //API 21
                else*/
                txtEstado.setBackground(context.getDrawable(R.drawable.rounded_back_button_orange));
            } else if ((lstWorkpods.get(i).getReserva() == null)) {
                txtEstado.setText("Disponible");
              /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    txtEstado.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
                else*/
                txtEstado.setBackground(context.getDrawable(R.drawable.rounded_back_button));
            } else {
                txtEstado.setText("Reservado");
                //OBTENEMOS EL ID DEL WORKPOD QUE HA RESERVADO EL USUARIO
                if (InfoApp.USER != null && InfoApp.USER.getReserva() != null)
                    idWorkpodUsuario = InfoApp.USER.getReserva().getWorkpod();
                //SI EL USUARIO NO HA RESERVADO NINGÚN WORKPOD DEL CLUSTER, LE APARECERÁ LA PALABRA RESERVADO EN ROJO
                txtEstado.setBackground(context.getDrawable(R.drawable.rounded_back_button_red));
                //SI EL USUARIO HA RESERVADO ALGUN WORKPOD, LE SALDRÁ LA PALABRA RESERVADO EN VERDE
                if (idWorkpodUsuario == lstWorkpods.get(i).getId())
                    txtEstado.setBackground(context.getDrawable(R.drawable.rounded_back_button_green));

            }
        } catch (NullPointerException e) {

        }
        txtNumPersonas.setText(String.valueOf(lstWorkpods.get(i).getNumUsuarios()));
        txtNombre.setText(lstWorkpods.get(i).getNombre());

        //CONTROLAMOS QUE NO CASQUE LA APP SI UNA FECHA APUNTA A NULL
        if (lstWorkpods.get(i).getLimpieza() != null) {
            txtLimpieza.setText(String.valueOf(lstWorkpods.get(i).getLimpieza().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"))));
        } else {
            txtLimpieza.setText("");
            iV_Icon_Limpieza.setVisibility(View.GONE);
        }
        if (lstWorkpods.get(i).getUltimoUso() != null) {

            txtUso.setText(String.valueOf(lstWorkpods.get(i).getUltimoUso().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"))));
        } else {
            txtUso.setText("");
            iV_Icon_Historial.setVisibility(View.GONE);
        }

        //CAMBIO COLOR AL ICONO
        iV_Icon_Historial.setImageTintList(ColorStateList.valueOf(Color.parseColor("#58B1E3")));
        iV_Icon_Limpieza.setImageTintList(ColorStateList.valueOf(Color.parseColor("#58B1E3")));

        //ESCALAR ELEMENTOS
        escalarElementos();
        return view;
    }

    /**
     * Este método sirve de ante sala para el método de la clase Methods donde escalamos los elementos del xml.
     * En este método inicializamos las colecciones donde guardamos los elementos del xml que vamos a escalar y
     * donde especificamos el width que queremos (match_parent, wrap_content o ""(si no ponemos nada significa que
     * el elemento tiene unos dp definidos que queremos que se conserven tanto en dispositivos grandes como en pequeños.
     * También especificamos en la List el estilo de letra (bold, italic, normal) y el tamaño de la fuente del texto tanto
     * para dispositivos pequeños como para dispositivos grandes).
     * <p>
     * Como el método scale de la clase Methods no es un activity o un fragment no podemos inicializar nuestro objeto de la clase
     * DisplayMetrics con los parámetros reales de nuestro móvil, es por ello que lo inicializamos en este método.
     * <p>
     * En resumen, en este método inicializamos el metrics y las colecciones y se lo pasamos al método de la clase Methods
     */
    private void escalarElementos() {
        //INICIALIZAMOS COLECCIONES
        this.lstTv = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstTv.add(new Scale_TextView(txtEstado, "match_parent", "bold", 18, 18));
        lstTv.add(new Scale_TextView(txtLimpieza, "wrap_content", "bold", 16, 16));
        lstTv.add(new Scale_TextView(txtNombre, "match_parent", "bold", 35, 40));
        lstTv.add(new Scale_TextView(txtNumPersonas, "", "normal", 15, 15));
        Method.scaleTv(metrics, lstTv);
    }

}
