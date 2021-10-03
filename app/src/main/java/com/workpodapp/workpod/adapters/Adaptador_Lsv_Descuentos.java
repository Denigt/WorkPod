package com.workpodapp.workpod.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.workpodapp.workpod.R;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Cupon;
import com.workpodapp.workpod.fragments.Fragment_Canjear_Codigos;
import com.workpodapp.workpod.fragments.Fragment_Dialog_More_Information;
import com.workpodapp.workpod.otherclass.LsV_Descuentos;
import com.workpodapp.workpod.scale.Scale_Buttons;
import com.workpodapp.workpod.scale.Scale_TextView;

import java.util.ArrayList;
import java.util.List;

public class Adaptador_Lsv_Descuentos extends BaseAdapter {

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
        this.context = context;
        this.metrics = metrics;
        this.manager = supportFragmentManager;
        this.lstCupones = lstCupones;
        this.pago = false;
    }

    /**
     * Constructor a usar si se necesita cambiar entre el boton de +info y canjear
     * @param context Contexto de la aplicacion, se obtiene del fragment o activity
     * @param supportFragmentManager Controlador de fragments para poder abrir fragments y dialogos desde el listView
     * @param lstCupones Lista de cupones
     * @param pago True si se quiere boton para canjear, false para boton de +info
     * @param metrics Ajustar tamano de los elementos del listView
     */
    public Adaptador_Lsv_Descuentos(Context context, FragmentManager supportFragmentManager, List<Cupon> lstCupones, boolean pago, DisplayMetrics metrics) {
        this.context = context;
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
                }
            });
        }else {
            ibtnCanjear.setText("Canjear");

            if (lstCupones.get(i).isCanjeado() && lstCupones.get(i).getfCanjeado() == null) {
                ibtnCanjear.setText("Canjeado");
                ((LinearLayout) ibtnCanjear.getParent()).setBackground(context.getDrawable(R.drawable.rounded_back_button_green));
            }
            ibtnCanjear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (R.id.iBtnCanjear == v.getId()) {
                        boolean aplicado = false;
                        for (Cupon cupon : lstCupones) {
                            if (cupon.isCanjeado() && cupon.getfCanjeado() == null) {
                                aplicado = true;
                                if (cupon.equals(lstCupones.get(i))) {
                                    lstCupones.get(i).setCanjeado(false);
                                    cupon = null;
                                    ((Button) v).setText("Canjear");
                                    ((LinearLayout) v.getParent()).setBackground(context.getDrawable(R.drawable.rounded_back_button));
                                }
                                break;
                            }
                        }
                        if (!aplicado) {
                            lstCupones.get(i).setCanjeado(true);
                            cupon = lstCupones.get(i);
                            ((Button) v).setText("Canjeado");
                            ((LinearLayout) v.getParent()).setBackground(context.getDrawable(R.drawable.rounded_back_button_green));
                        }
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
    private void escalarElementos(DisplayMetrics metrics) {
        //INICIALIZAMOS COLECCIONES
        this.lstBtn = new ArrayList<>();
        this.lstTv = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstBtn.add(new Scale_Buttons(ibtnCanjear, "wrap_content", "bold", 12, 15, 17));

        lstTv.add(new Scale_TextView(tVminGratis, "wrap_content", "bold", 13, 16, 20));
        lstTv.add(new Scale_TextView(tVnombreDescuento, "wrap_content", "bold", 16, 20, 25));

        Method.scaleBtns(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);

        escaladoParticular(metrics);

    }

    private void escaladoParticular(DisplayMetrics metrics) {
        if ((width <= (750 / metrics.density)) && (width > (550 / metrics.density))) {
            lLDescuento.getLayoutParams().width = 350;
        } else if (width <= (550 / metrics.density)) {
            lLDescuento.getLayoutParams().width = 220;
        }
    }

    public Cupon getCupon() {
        return cupon;
    }
}
