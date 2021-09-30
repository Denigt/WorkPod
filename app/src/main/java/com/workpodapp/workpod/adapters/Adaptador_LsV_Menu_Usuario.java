package com.workpodapp.workpod.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Cupon;
import com.workpodapp.workpod.otherclass.LsV_Menu_Usuario;
import com.workpodapp.workpod.R;
import com.workpodapp.workpod.scale.Scale_Buttons;
import com.workpodapp.workpod.scale.Scale_Image_View;
import com.workpodapp.workpod.scale.Scale_TextView;

import java.util.ArrayList;
import java.util.List;

public class Adaptador_LsV_Menu_Usuario extends BaseAdapter {
    Context context;
    ArrayList<LsV_Menu_Usuario>aLstMenuUsuario=new ArrayList<>();

    //XML
    private TextView tVIcono;
    private ImageView iVIcono;

    //ESCALADO
    DisplayMetrics metrics;
    float width;

    //COLECCIONES
    List<Scale_Image_View> lstIv;
    List<Scale_TextView> lstTv;

    public Adaptador_LsV_Menu_Usuario() {
    }

    public Adaptador_LsV_Menu_Usuario(Context context, ArrayList<LsV_Menu_Usuario> aLstMenuUsuario, DisplayMetrics metrics) {
        this.context = context;
        this.aLstMenuUsuario = aLstMenuUsuario;
        this.metrics=metrics;
    }

    @Override
    public int getCount() {
        return aLstMenuUsuario.size();
    }

    @Override
    public Object getItem(int i) {
        return aLstMenuUsuario.get(i);
    }

    @Override
    public long getItemId(int i) {
        return aLstMenuUsuario.get(i).getCodigo();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        view=inflater.inflate(R.layout.item_lsv_menu_usuario,null);

        tVIcono=(TextView)view.findViewById(R.id.iTV);
        iVIcono=(ImageView)view.findViewById(R.id.iIV);
        tVIcono.setText(aLstMenuUsuario.get(i).getTexto());
        iVIcono.setImageResource(aLstMenuUsuario.get(i).getIcono());

        //ESCALAMOS ELEMENTOS
        width = metrics.widthPixels / metrics.density;
        escalarElementos(metrics);

        return view;
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
        this.lstIv = new ArrayList<>();
        this.lstTv = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstIv.add(new Scale_Image_View(iVIcono, 50, 50, 80, 80, 120, 120, "", ""));
        lstTv.add(new Scale_TextView(tVIcono, "match_parent", "normal", 20, 24, 27));

        Method.scaleIv(metrics, lstIv);
        Method.scaleTv(metrics, lstTv);


    }
}
