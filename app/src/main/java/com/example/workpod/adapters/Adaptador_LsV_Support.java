package com.example.workpod.adapters;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.basic.Method;
import com.example.workpod.otherclass.LsV_Support;
import com.example.workpod.scale.Scale_TextView;

import java.util.ArrayList;
import java.util.List;

public class Adaptador_LsV_Support extends BaseAdapter {

    Context context;
    ArrayList<LsV_Support> aLstSupport = new ArrayList<>();
    public TextView tVIcono;

    //INSTANCIA DE LA CLASE DISPLAYMETRICS
    private DisplayMetrics metrics=new DisplayMetrics();

    //COLECCIONES
    private List<Scale_TextView> lstTv;

    public Adaptador_LsV_Support() {
    }

    public Adaptador_LsV_Support(Context context, ArrayList<LsV_Support> aLstSupport, DisplayMetrics metrics) {
        this.context = context;
        this.aLstSupport = aLstSupport;
        this.metrics=metrics;
    }

    @Override
    public int getCount() {
        return aLstSupport.size();
    }

    @Override
    public Object getItem(int i) {
        return aLstSupport.get(i);
    }

    @Override
    public long getItemId(int i) {
        return aLstSupport.get(i).getCodigo();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_support, null);
        ImageView iVIcono;
        tVIcono = (TextView) view.findViewById(R.id.iTVSupport);
        iVIcono = (ImageView) view.findViewById(R.id.iIVSupport);

        if (aLstSupport.get(i).getCodigo() == 1) {
            tVIcono.setText(Html.fromHtml("<a href=\"mailto:workpodtfg@gmail.com\">workpodtfg@gmail.com </a>"));
            tVIcono.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            tVIcono.setText(aLstSupport.get(i).getTexto());
        }
        iVIcono.setImageResource(aLstSupport.get(i).getIcono());

        //ESCALAMOS ELEMENTOS
        escalarElementos();

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
     */
    private void escalarElementos() {
        //INICIALIZAMOS COLECCIONES
        this.lstTv = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstTv.add(new Scale_TextView(tVIcono, "match_parent", "normal",18 , 25));
        Method.scaleTv(metrics, lstTv);
    }
}
