package com.workpodapp.workpod;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.workpodapp.workpod.adapters.Adaptador_Lsv_Descuentos;
import com.workpodapp.workpod.basic.Database;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Cupon;
import com.workpodapp.workpod.data.Reserva;
import com.workpodapp.workpod.data.Sesion;
import com.workpodapp.workpod.fragments.Fragment_sesion;
import com.workpodapp.workpod.otherclass.LsV_Descuentos;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagoActivity extends AppCompatActivity implements View.OnClickListener {

    // XML
    private TextView txtCabina;
    private TextView txtTiempo;
    private TextView txtPrecio;
    private TextView txtTotal;
    private TextView txtDescuento;
    private TextView txtTVFinal;
    private TextView txtTVDesglose;
    private TextView txtTVTotal;
    private TextView txtTVDescuento;
    private Button btnPagar;
    private Button btnVolver;
    private ListView lsvCupones;


    // VARIABLES PARA ESCALADO
    DisplayMetrics metrics;
    float width;

    // VARIABLES PARA EL CALCULO DEL COSTE DE LA SESION
    private List<LsV_Descuentos> itemsDescuentos = new ArrayList<>();
    private List<Cupon> lstCupones = new ArrayList<>();
    private Adaptador_Lsv_Descuentos adDescuentos;
    private Reserva reserva = new Reserva();
    private Sesion sesion = new Sesion();

    public PagoActivity() {
        this.sesion.set(InfoApp.SESION);
        this.reserva.set(InfoApp.USER.getReserva());

        Database<Cupon> consulta = new Database<Cupon>(Database.SELECTUSER, new Cupon());
        consulta.postRun(() -> {
            if (consulta.getError().code > -1) {
                lstCupones.addAll(consulta.getLstSelect());
            }
        });
        consulta.postRunOnUI(this, () -> {
            if (consulta.getError().code > -1 && lsvCupones != null) {
                adDescuentos = new Adaptador_Lsv_Descuentos(this, getSupportFragmentManager(), lstCupones, true, metrics);

                adDescuentos.setOnBtnClick(() -> {
                    Cupon cupon = adDescuentos.getCupon();

                    double importe = Method.round((Method.subsDate(sesion.getSalida(), sesion.getEntrada()) / 60.) * sesion.getWorkpod().getPrecio(), 2);
                    sesion.setPrecio(importe);
                    if (cupon == null)
                        sesion.setDescuento(0);
                    else
                        sesion.setDescuento(sesion.calculaDescuento(cupon.getCampana(), sesion.getWorkpod()));

                    txtPrecio.setText(String.format("%.2f€", sesion.calculaPrecio()));
                    txtTotal.setText(String.format("%.2f€", sesion.getPrecio()));
                    txtDescuento.setText(String.format("%.2f€", sesion.getDescuento()));
                });
                lsvCupones.setAdapter(adDescuentos);
            }
        });
        consulta.start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);

        // CALCULAR DATOS A MOSTRAR EN LA PANTALLA
        reserva.setEstado("FINALIZADA");
        sesion.setSalida(ZonedDateTime.now());
        double precio = Method.round((Method.subsDate(sesion.getSalida(), sesion.getEntrada()) / 60.) * sesion.getWorkpod().getPrecio(), 2);
        sesion.setPrecio(precio);
        sesion.setTiempo(Method.subsDate(sesion.getSalida(), sesion.getEntrada()) / 3600.);
        int[] tiempo = new int[3];
        tiempo[0] = (int) sesion.getTiempo(); // HORAS
        tiempo[1] = (int) ((sesion.getTiempo() - tiempo[0]) * 60); // MINUTOS
        tiempo[2] = (int) ((sesion.getTiempo() - (tiempo[0] + (tiempo[1] / 60.))) * 3600); // SEGUNDOS

        // INSTANCIAMOS ELEMENTOS DEL XML
        txtTVDescuento = findViewById(R.id.TVDescuento);
        txtTVDesglose = findViewById(R.id.TVDesglose);
        txtTVFinal = findViewById(R.id.TVFinal);
        txtTVTotal = findViewById(R.id.TVTotal);

        txtCabina = findViewById(R.id.TVCabina);
        txtCabina.setText(sesion.getWorkpod().getNombre());

        txtTiempo = findViewById(R.id.TVTiempo);
        txtTiempo.setText(String.format("%02d:%02d:%02d", tiempo[0], tiempo[1], tiempo[2]));

        txtPrecio = findViewById(R.id.TVPrecio);
        txtPrecio.setText(String.format("%.2f€", sesion.calculaPrecio()));

        txtTotal = findViewById(R.id.TVPrecioTotal);
        txtTotal.setText(String.format("%.2f€", sesion.getPrecio()));

        txtDescuento = findViewById(R.id.TVImporteDescuento);
        txtDescuento.setText(String.format("%.2f€", sesion.getDescuento()));

        lsvCupones = findViewById(R.id.lsvCupones);


        btnVolver = findViewById(R.id.btnVolver);
        btnPagar = findViewById(R.id.btnPagar);

        // EVENTOS DE LOS CONTROLES
        btnVolver.setOnClickListener(this);
        btnPagar.setOnClickListener(this);

        // ESCALAMOS ELEMENTOS
        escalarElementos();

        //SI LO PONES ANTES, METRICS APUNTA A NULO
        adDescuentos = new Adaptador_Lsv_Descuentos(this, getSupportFragmentManager(), lstCupones, true, metrics);

        adDescuentos.setOnBtnClick(() -> {
            Cupon cupon = adDescuentos.getCupon();

            double importe = Method.round((Method.subsDate(sesion.getSalida(), sesion.getEntrada()) / 60.) * sesion.getWorkpod().getPrecio(), 2);
            sesion.setPrecio(importe);
            sesion.setDescuento(sesion.calculaDescuento(cupon.getCampana(), sesion.getWorkpod()));

            txtPrecio.setText(String.format("%.2f€", sesion.calculaPrecio()));
            txtTotal.setText(String.format("%.2f€", sesion.getPrecio()));
            txtDescuento.setText(String.format("%.2f€", sesion.getDescuento()));
        });
        lsvCupones.setAdapter(adDescuentos);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnVolver.getId()) {
            onClickBtnVolver();
        } else if (v.getId() == btnPagar.getId()) {
            onClickBtnPagar();
        }
    }

    /**
     * Al darle al btn Si, nos vamos a la parte del cuestionario.
     */
    private void onClickBtnPagar() {
        try {
            //PARAMOS EL HILO
            Fragment_sesion.cerrarWorkpod = true;

            Database<Reserva> updateReserva = new Database<>(Database.UPDATE, reserva);
            updateReserva.postRun(() -> {
                if (updateReserva.getError().code > -1) {
                    // SI NO HA HABIDO ERRORES ACTUALIZAR LA RESERVA EN LA APP
                    InfoApp.USER.getReserva().set(reserva);
                }
            });

            updateReserva.postRunOnUI(this, () -> {
                if (updateReserva.getError().code > -1) {
                    // SI NO HA HABIDO ERRORES ACTUALIZAR LA SESION EN LA DB
                    Database<Sesion> updateSesion = new Database<>(Database.UPDATE, sesion);
                    Database<Cupon> updateCupon = new Database<>(Database.UPDATE, adDescuentos.getCupon());
                    updateSesion.postRunOnUI(updateReserva.getActivity(), () -> {
                        if (updateSesion.getError().code > -1) {
                            InfoApp.SESION = sesion;
                            WorkpodActivity.boolSession = false;

                           /* Intent activity = new Intent(updateSesion.getActivity(), ValoracionWorkpod.class);
                            activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(activity);*/

                            //Le pasamos al activity de PayPal lo q va a pagar el usuario
                            InfoApp.PRECIO_FINAL_SESION = this.sesion.calculaPrecio();
                            Intent activity = new Intent(getApplicationContext(), paypalActivity.class);
                            // activity.putExtra("PrecioSesion",String.valueOf(this.sesion.getPrecio()));
                            finishAffinity();
                            startActivity(activity);
                        }
                    });
                    if (adDescuentos.getCupon() != null)
                        updateCupon.start();
                    updateSesion.start();
                }
            });
            updateReserva.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //MÉTODOS

    /**
     * Cerramos el fragment dialog al pulsar el btn No.
     */
    private void onClickBtnVolver() {
        super.onBackPressed();
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
        metrics = new DisplayMetrics();
        //INICIALIZAMOS EL OBJETO DISPLAYMETRICS CON LOS PARÁMETROS DE NUESTRO DISPOSITIVO
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //INICIALIZAMOS COLECCIONES
        List<View> lstViews = new ArrayList<>();
        lstViews.add(txtCabina);
        lstViews.add(txtDescuento);
        lstViews.add(txtPrecio);
        lstViews.add(txtTiempo);
        lstViews.add(txtTotal);
        lstViews.add(btnPagar);
        lstViews.add(btnVolver);
        lstViews.add(txtTVTotal);
        lstViews.add(txtTVFinal);
        lstViews.add(txtTVDesglose);
        lstViews.add(txtTVDescuento);
        //LLENAMOS COLECCIONES
        Method.scaleViews(metrics, lstViews);

    }
}