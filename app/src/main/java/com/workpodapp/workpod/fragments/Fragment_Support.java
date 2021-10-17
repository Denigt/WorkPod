package com.workpodapp.workpod.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.workpodapp.workpod.R;
import com.workpodapp.workpod.ValoracionWorkpod;
import com.workpodapp.workpod.WorkpodActivity;
import com.workpodapp.workpod.adapters.Adaptador_LsV_Support;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.otherclass.LsV_Support;
import com.workpodapp.workpod.scale.Scale_TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Support#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Support extends Fragment {
    private ListView lsV_Support;
    private TextView tVFgmSupportTitulo;
    ArrayList<LsV_Support> aLstSupport = new ArrayList<>();
    DisplayMetrics metrics = new DisplayMetrics();

    //COLECCIONES;
    List<Scale_TextView> lstTv;

    public Fragment_Support() {
        // Required empty public constructor
    }


    public static Fragment_Support newInstance(String param1, String param2) {
        Fragment_Support fragment = new Fragment_Support();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        lsV_Support = (ListView) view.findViewById(R.id.LsV_Support);
        tVFgmSupportTitulo = view.findViewById(R.id.tVFgmSupportTitulo);

        //INICIALIZAMOS EL OBJETO DISPLAYMETRICS CON LOS PARÁMETROS DE NUESTRO DISPOSITIVO
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //ARMAMOS EL LSV
        construyendo_LsV(view);

        //ESCALAMOS ELEMENTOS
        escalarElementos();

        //PONEMOS EL ICONO DEL NV EN MENU USUARIO
        if(InfoApp.USER!=null)
            WorkpodActivity.btnNV.getMenu().findItem(R.id.inv_menu_user).setChecked(true);

        //Como al soporte se puede acceder desde la sesión, hay que volver a asignar el InfroFragment.actual a soporte
        //para volver al menú al darle atrás
        InfoFragment.actual = InfoFragment.SOPORTE;

        return view;
    }


    //METODOS

    /**
     * Método para construir el ListView donde se encontrará el email, el tlfn y la dirección de la compañía
     *
     * @param view instancia de la clase View
     */
    public void construyendo_LsV(View view) {

        aLstSupport.add(new LsV_Support(1, R.drawable.fill_icon_gmail, "workpodtfg@gmail.com"));
        aLstSupport.add(new LsV_Support(2, R.drawable.fill_icon_llamar, "Contacta por Teléfono "));
        final Adaptador_LsV_Support aSuport = new Adaptador_LsV_Support(view.getContext(), aLstSupport, metrics);
        lsV_Support.setAdapter(aSuport);
        lsV_Support.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                LsV_Support lsV_support = (LsV_Support) aSuport.getItem(i);
              /*  if (lsV_support.getCodigo() == 0) {
                    Intent terminos = new Intent(requireContext(), WebActivity.class);
                    terminos.putExtra("web", "https://dev.workpod.app/web/terminos_condiciones.html");
                    startActivity(terminos);
                }*/
                if (lsV_support.getCodigo() == 2) {
                    Fragment_Dialog_Call fragmentDialogCall = new Fragment_Dialog_Call();
                    fragmentDialogCall.show(getActivity().getSupportFragmentManager(), "DialogToCall");
                }
            }
        });
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
        lstTv.add(new Scale_TextView(tVFgmSupportTitulo, "", "normal", 30, 30, 30));

        Method.scaleTv(metrics, lstTv);
    }

    @Override
    public void onDestroy() {
            super.onDestroy();
    }
}