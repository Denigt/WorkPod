package com.workpodapp.workpod.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.workpodapp.workpod.R;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Cupon;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Dialog_More_Information extends DialogFragment implements View.OnClickListener {

    //XML
    private ImageView iVSalirDialogMoreInformation;
    private TextView tV_Titulo_Cupon;
    private TextView tV_Descripcion_Campana;
    private TextView tV_Condiciones_Uso;
    private TextView tV_Titulo_Fecha_Validez;
    private TextView tV_Titulo_Minutos_Minimos;
    private TextView tV_Fecha_Validez;
    private TextView tV_Minutos_Minimos;

    //ESCALADO
    DisplayMetrics metrics;

    Cupon cupon;

    public Fragment_Dialog_More_Information() {

    }

    public Fragment_Dialog_More_Information(Cupon cupon) {
        this.cupon = cupon;
    }

    public static Fragment_Dialog_More_Information newInstance(String param1, String param2) {
        Fragment_Dialog_More_Information fragment = new Fragment_Dialog_More_Information();
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

        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.fragment_dialog_more_information, container, false);
        return view;
    }

    //ESTA SOBREESCRITURA PERMITE CREAR UN DIALOGRESULT
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return createDialog();
    }

    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ESPECIFICAMOS DONDE VAMOS A CREAR (INFLAR) EL DIALOGRESULT
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_more_information, null);
        builder.setView(view);

        //INSTANCIAMOS ELEMENTOS DEL XML
        tV_Condiciones_Uso = view.findViewById(R.id.TV_Condiciones_Uso);
        tV_Descripcion_Campana = view.findViewById(R.id.TV_Descripcion_Campana);
        tV_Fecha_Validez = view.findViewById(R.id.TV_Fecha_Validez);
        tV_Minutos_Minimos = view.findViewById(R.id.TV_Minutos_Minimos);
        tV_Titulo_Cupon = view.findViewById(R.id.TV_Titulo_Cupon);
        tV_Titulo_Minutos_Minimos = view.findViewById(R.id.TV_Titulo_Minutos_Minimos);
        tV_Titulo_Fecha_Validez = view.findViewById(R.id.TV_Titulo_Fecha_Validez);
        iVSalirDialogMoreInformation = view.findViewById(R.id.IVSalirDialogMoreInformation);
        //  tV_Minutos_Minimos.setText("Minutos mínimos para canjear este cupón: " + Html.fromHtml("<font color='#FF58B1E3'>14</font>"));

        //INICIALIZAMOS ELEMENTOS XML
        tV_Minutos_Minimos.setText(Integer.toString(cupon.getCampana().getConsumoMin()) + " minutos");
        tV_Titulo_Cupon.setText(cupon.getCampana().getNombre());
        if (!cupon.getCampana().getFinCanjeo().equals(ZonedDateTime.of(2000, 01, 01, 0, 0, 0, 0, ZonedDateTime.now().getZone()))) {
            tV_Fecha_Validez.setText(Integer.toString(cupon.getCampana().getFinCanjeo().getDayOfMonth()) + "/" +
                    Integer.toString(cupon.getCampana().getFinCanjeo().getMonth().getValue()) + "/" +
                    Integer.toString(cupon.getCampana().getFinCanjeo().getYear()));
        } else {
            tV_Titulo_Fecha_Validez.setVisibility(View.GONE);
            tV_Fecha_Validez.setText("No tiene fecha límite");
        }
        tV_Descripcion_Campana.setText(cupon.getCampana().getDescripcion());

        //LISTENERS
        iVSalirDialogMoreInformation.setOnClickListener(this);

        //ESCALAMOS ELEMENTOS
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        escalarElementos(metrics);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == iVSalirDialogMoreInformation.getId()) {
            dismiss();
        }
    }

    //MÉTODOS

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
     *
     * @param metrics
     */
    private <T extends View> void escalarElementos(DisplayMetrics metrics) {
        //INICIALIZAMOS COLECCIONES
        List<T> lstView = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstView.add((T) iVSalirDialogMoreInformation);
        lstView.add((T) tV_Titulo_Cupon);
        lstView.add((T) tV_Descripcion_Campana);
        lstView.add((T) tV_Condiciones_Uso);
        lstView.add((T) tV_Titulo_Fecha_Validez);
        lstView.add((T) tV_Fecha_Validez);
        lstView.add((T) tV_Titulo_Minutos_Minimos);
        lstView.add((T) tV_Minutos_Minimos);

        Method.scaleViews(metrics, lstView);

    }
}