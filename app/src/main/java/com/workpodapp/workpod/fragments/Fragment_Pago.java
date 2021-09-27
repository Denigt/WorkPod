package com.workpodapp.workpod.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.workpodapp.workpod.R;
import com.workpodapp.workpod.ValoracionWorkpod;
import com.workpodapp.workpod.WorkpodActivity;
import com.workpodapp.workpod.basic.Database;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Reserva;
import com.workpodapp.workpod.data.Sesion;
import com.workpodapp.workpod.scale.Scale_Buttons;
import com.workpodapp.workpod.scale.Scale_TextView;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Pago extends Fragment implements View.OnClickListener {

    //XML
    private TextView txtCabina;
    private TextView txtTiempo;
    private TextView txtPrecio;
    private TextView txtTotal;
    private TextView txtDescuento;
    private Button btnPagar;
    private Button btnVolver;
    private ListView lsvCupones;

    //VARIABLES PARA ESCALADO
    private List<Scale_Buttons>lstBtn;
    private List<Scale_TextView>lstTv;
    DisplayMetrics metrics;
    float width;

    private Reserva reserva;
    private Sesion sesion;

    private boolean controlador;

    public Fragment_Pago() {
        this.sesion = InfoApp.SESION;
        this.reserva = InfoApp.RESERVA;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pago, container, false);

        // CALCULAR DATOS A MOSTRAR EN LA PANTALLA
        reserva.setEstado("FINALIZADA");
        sesion.setSalida(ZonedDateTime.now());
        double precio = (Method.subsDate(sesion.getSalida(), sesion.getEntrada())/60.) * sesion.getWorkpod().getPrecio();
        sesion.setPrecio(precio);
        sesion.setTiempo(Method.subsDate(sesion.getSalida(), sesion.getEntrada())/3600.);

        // INSTANCIAMOS ELEMENTOS DEL XML
        btnVolver = view.findViewById(R.id.btnVolver);
        btnPagar = view.findViewById(R.id.btnPagar);

        // EVENTOS DE LOS CONTROLES
        btnVolver.setOnClickListener(this);
        btnPagar.setOnClickListener(this);

        // ESCALAMOS ELEMENTOS
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / metrics.density;
        escalarElementos(metrics);
        return view;
    }

    //SOBRESECRITURAS
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnNo) {
            onClickBtnVolver();
        } else if (v.getId() == R.id.BtnSi) {
            onClickBtnPagar();
        }
    }

    /**
     * Al darle al btn Si, nos vamos a la parte del cuestionario.
     */
    private void onClickBtnPagar() {
        try{
            //PARAMOS EL HILO
            Fragment_sesion.cerrarWorkpod = true;

            Database<Reserva> updateReserva = new Database<>(Database.UPDATE, reserva);
            updateReserva.postRun( () -> {
                if (updateReserva.getError().code > -1) {
                    // SI NO HA HABIDO ERRORES ACTUALIZAR LA RESERVA EN LA APP
                    InfoApp.RESERVA.set(reserva);
                }
            });

            updateReserva.postRunOnUI(requireActivity(), () -> {
                if (updateReserva.getError().code > -1) {
                    // SI NO HA HABIDO ERRORES ACTUALIZAR LA SESION EN LA DB
                    Database<Sesion> updateSesion = new Database<>(Database.UPDATE, sesion);
                    updateSesion.postRunOnUI(getActivity(), () -> {
                        if (updateSesion.getError().code > -1){
                            InfoApp.SESION = sesion;
                            WorkpodActivity.boolLoc=false;
                            WorkpodActivity.boolSession=false;

                            Intent activity = new Intent(getActivity(), ValoracionWorkpod.class);
                            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(activity);
                        }
                    });
                    updateSesion.start();
                }
            });
            updateReserva.start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //MÉTODOS
    /**
     * Cerramos el fragment dialog al pulsar el btn No.
     */
    private void onClickBtnVolver() {

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
     *
     * @param metrics
     */
    private void escalarElementos(DisplayMetrics metrics) {
        //INICIALIZAMOS COLECCIONES
        this.lstBtn = new ArrayList<>();
        this.lstTv = new ArrayList<>();

        //LLENAMOS COLECCIONES

        Method.scaleBtns(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);
    }
}