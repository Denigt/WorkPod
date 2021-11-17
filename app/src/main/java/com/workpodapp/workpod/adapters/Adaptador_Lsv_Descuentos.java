package com.workpodapp.workpod.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.workpodapp.workpod.R;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Cupon;
import com.workpodapp.workpod.fragments.Fragment_Dialog_More_Information;
import com.workpodapp.workpod.otherclass.LsV_Descuentos;
import com.workpodapp.workpod.scale.Scale_Buttons;
import com.workpodapp.workpod.scale.Scale_TextView;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Adaptador_Lsv_Descuentos extends BaseAdapter {
    private Activity activity;
    private Runnable onBtnClick = null;
    Context context;
    FragmentManager manager;

    //VARIABLES DEL ITEM
    LinearLayout lLDescuento;
    TextView tVnombreDescuento;
    TextView tVminGratis;
    Button ibtnCanjear;
    LinearLayout llBtnCanjear;

    Cupon cupon;

    // INDICA SI SE HA CREADO EL ADAPTADOR PARA REALIZAR PAGOS
    private boolean pago;

    //ESCALADO
    DisplayMetrics metrics;
    float width;
    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView> lstTv;
    List<Cupon> lstCupones;

    public Adaptador_Lsv_Descuentos(Context context, List<LsV_Descuentos> lstDescuentos, DisplayMetrics metrics) {
    }

    public Adaptador_Lsv_Descuentos(Context context, DisplayMetrics metrics,
                                    FragmentManager supportFragmentManager, List<Cupon> lstCupones) {
        this.activity = null;
        this.context = context;
        this.metrics = metrics;
        this.manager = supportFragmentManager;
        this.lstCupones = lstCupones;
        this.pago = false;
    }

    /**
     * Constructor a usar si se necesita cambiar entre el boton de +info y canjear
     *
     * @param context                Contexto de la aplicacion, se obtiene del fragment o activity
     * @param supportFragmentManager Controlador de fragments para poder abrir fragments y dialogos desde el listView
     * @param lstCupones             Lista de cupones
     * @param pago                   True si se quiere boton para canjear, false para boton de +info
     * @param metrics                Ajustar tamano de los elementos del listView
     */
    public Adaptador_Lsv_Descuentos(Context context, FragmentManager supportFragmentManager, List<Cupon> lstCupones, boolean pago, DisplayMetrics metrics) {
        this.activity = null;
        this.context = context;
        this.metrics = metrics;
        this.manager = supportFragmentManager;
        this.lstCupones = lstCupones;
        this.pago = pago;
    }

    /**
     * Constructor a usar si se necesita cambiar entre el boton de +info y canjear
     * Tambien sustituye el context por activity, lo que permite ejecutar el Runnable onBtnClick (El cual se ejecuta al pulsar el boton)
     *
     * @param activity               Actividad en ejecucion
     * @param supportFragmentManager Controlador de fragments para poder abrir fragments y dialogos desde el listView
     * @param lstCupones             Lista de cupones
     * @param pago                   True si se quiere boton para canjear, false para boton de +info
     * @param metrics                Ajustar tamano de los elementos del listView
     */
    public Adaptador_Lsv_Descuentos(Activity activity, FragmentManager supportFragmentManager, List<Cupon> lstCupones, boolean pago, DisplayMetrics metrics) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.metrics = metrics;
        this.manager = supportFragmentManager;
        this.lstCupones = lstCupones;
        this.pago = pago;
    }

    @Override
    public int getCount() {
        return lstCupones.size();
    }

    @Override
    public Object getItem(int i) {
        return lstCupones.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstCupones.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        //INFLAMOS EL LAYOUT DE LOS ITEMS DEL LSV
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_lsv_descuentos, null);
        lLDescuento = view.findViewById(R.id.iLLDescuento);
        tVnombreDescuento = view.findViewById(R.id.iTV_Nombre_Descuento);
        tVminGratis = view.findViewById(R.id.iTV_Min_Descuento);
        ibtnCanjear = view.findViewById(R.id.iBtnCanjear);
        llBtnCanjear = view.findViewById(R.id.llBntCanjear);
        tVnombreDescuento.setText(lstCupones.get(i).getCampana().getNombre());
        tVminGratis.setText(lstCupones.get(i).getCampana().getDescuento() + " minutos gratis");

        if (!pago) {
            ibtnCanjear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if(ibtnCanjear.getText().)
                    cupon = lstCupones.get(i);
                    showDialogMoreInformation(v);

                    if (activity != null && onBtnClick != null)
                        activity.runOnUiThread(onBtnClick);
                }
            });
        } else {
            ibtnCanjear.setText("Canjear");
            ibtnCanjear.setTag(lstCupones.get(i));

            if (lstCupones.get(i).isCanjeado() && lstCupones.get(i).getfCanjeado() == null) {
                ibtnCanjear.setText("Canjeado");
                ((LinearLayout) ibtnCanjear.getParent()).setBackground(context.getDrawable(R.drawable.rounded_back_button_green));
            }
            ibtnCanjear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (R.id.iBtnCanjear == v.getId()) {
                        boolean aplicado = false;
                        for (Cupon auxCupon : lstCupones) {
                            if (auxCupon.isCanjeado() && auxCupon.getfCanjeado() == null) {
                                aplicado = true;
                                if (auxCupon.equals(v.getTag())) {
                                    lstCupones.get(i).setCanjeado(false);
                                    cupon = null;
                                    ((Button) v).setText("Canjear");
                                    ((LinearLayout) v.getParent()).setBackground(context.getDrawable(R.drawable.rounded_back_button));
                                }
                                break;
                            }
                        }
                        if (!aplicado) {
                            if (((Cupon) v.getTag()).getfCaducidad() == null || Method.subsDate(ZonedDateTime.now(), ((Cupon) v.getTag()).getfCaducidad()) <= 0) {
                                ((Cupon) v.getTag()).setCanjeado(true);
                                cupon = (Cupon) v.getTag();
                                ;
                                ((Button) v).setText("Canjeado");
                                ((LinearLayout) v.getParent()).setBackground(context.getDrawable(R.drawable.rounded_back_button_green));
                            } else {
                                Toast.makeText(context, "El cupón está caducado", Toast.LENGTH_SHORT);
                            }
                        }

                        if (activity != null && onBtnClick != null)
                            activity.runOnUiThread(onBtnClick);
                    }
                }
            });
        }

        //ESCALAMOS ELEMENTOS
        width = metrics.widthPixels / metrics.density;
        escalarElementos(metrics);


        return view;
    }


    private void showDialogMoreInformation(View view) {
        Fragment_Dialog_More_Information dialog_more_information = new Fragment_Dialog_More_Information(cupon);
        dialog_more_information.show(manager, "Show Dialog More Information");
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
        lstView.add((T) ibtnCanjear);
        lstView.add((T) tVminGratis);
        lstView.add((T) tVnombreDescuento);
        lstView.add((T) lLDescuento);

        Method.scaleViews(metrics, lstView);
    }

    /**
     * Establece el runnable a ejecutar al pulsar el boton
     * El runnable se ejecuta en el hilo de la interfaz por lo que puede pausar la intefaz del programa,
     * impedir interacciones con la interfaz y afectar a la misma, cuidado con que codigo se mete en el
     *
     * @param onBtnClick
     */
    public void setOnBtnClick(Runnable onBtnClick) {
        this.onBtnClick = onBtnClick;
    }

    public Cupon getCupon() {
        return cupon;
    }
}
