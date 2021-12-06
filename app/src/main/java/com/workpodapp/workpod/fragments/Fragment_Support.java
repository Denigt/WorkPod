package com.workpodapp.workpod.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.workpodapp.workpod.R;
import com.workpodapp.workpod.WebActivity;
import com.workpodapp.workpod.WorkpodActivity;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Support#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Support extends Fragment implements View.OnClickListener {

    //XML
    private TextView tVTituloConsultas;
    private TextView tVFAQ;
    private TextView tVTyC;
    private TextView tVTituloContactos;
    private TextView tVMail;
    private TextView tVCall;

    private ImageView iVFAQ;
    private ImageView iVTyC;
    private ImageView iVMail;
    private ImageView iVCall;

    private LinearLayout lLFAQ;
    private LinearLayout lLTyC;
    private LinearLayout lLMail;
    private LinearLayout lLCall;


    //ESCALADO
    DisplayMetrics metrics = new DisplayMetrics();

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

        //Bindeamos java con xml
        tVCall = view.findViewById(R.id.TVCall);
        tVMail = view.findViewById(R.id.TVMail);
        tVFAQ = view.findViewById(R.id.TVFAQ);
        tVTyC = view.findViewById(R.id.TVTyC);
        tVTituloConsultas = view.findViewById(R.id.TVTituloConsultas);
        tVTituloContactos = view.findViewById(R.id.TVTituloContactos);
        iVCall = view.findViewById(R.id.IVCall);
        iVMail = view.findViewById(R.id.IVMail);
        iVFAQ = view.findViewById(R.id.IVFAQ);
        iVTyC = view.findViewById(R.id.IVTyC);
        lLCall = view.findViewById(R.id.LLCall);
        lLMail = view.findViewById(R.id.LLMail);
        lLFAQ = view.findViewById(R.id.LLFAQ);
        lLTyC = view.findViewById(R.id.LLTyC);

        lLTyC.setOnClickListener(this);
        lLFAQ.setOnClickListener(this);
        lLMail.setOnClickListener(this);
        lLCall.setOnClickListener(this);

        //INICIALIZAMOS EL OBJETO DISPLAYMETRICS CON LOS PARÁMETROS DE NUESTRO DISPOSITIVO
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //ESCALAMOS ELEMENTOS
        escalarElementos();

        if (InfoApp.USER == null) {
            WorkpodActivity.btnNV.getMenu().findItem(R.id.inv_support).setChecked(true);
            InfoFragment.actual = InfoFragment.MENU;
        } else {
            //PONEMOS EL ICONO DEL NV EN SUPPORT
            WorkpodActivity.btnNV.getMenu().findItem(R.id.inv_support).setChecked(true);

            //Como al soporte se puede acceder desde la sesión, hay que volver a asignar el InfroFragment.actual a soporte
            //para volver al menú al darle atrás
            InfoFragment.actual = InfoFragment.SOPORTE;
        }

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

        //LLENAMOS COLECCIONES
        List<View> lstViews = new ArrayList<>();
        lstViews.add(iVCall);
        lstViews.add(tVCall);
        lstViews.add(iVFAQ);
        lstViews.add(iVMail);
        lstViews.add(iVTyC);

        lstViews.add(tVFAQ);
        lstViews.add(tVMail);
        lstViews.add(tVTituloConsultas);
        lstViews.add(tVTituloContactos);
        lstViews.add(tVTyC);

        Method.scaleViews(metrics, lstViews);
        escaladoParticular(metrics);
    }

    private void escaladoParticular(DisplayMetrics metrics) {
        float height = metrics.heightPixels / metrics.density;
        iVCall.getLayoutParams().height = Integer.valueOf((int) Math.round(iVCall.getLayoutParams().height * (height / Method.heightEmulator)));
        iVFAQ.getLayoutParams().height = Integer.valueOf((int) Math.round(iVFAQ.getLayoutParams().height * (height / Method.heightEmulator)));
        iVMail.getLayoutParams().height = Integer.valueOf((int) Math.round(iVMail.getLayoutParams().height * (height / Method.heightEmulator)));
        iVTyC.getLayoutParams().height = Integer.valueOf((int) Math.round(iVTyC.getLayoutParams().height * (height / Method.heightEmulator)));

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.LLTyC) {
            onClickLLTyC();
        } else if (v.getId() == R.id.LLFAQ) {
            float width = metrics.widthPixels;

            Toast.makeText(getActivity(), String.valueOf(width), Toast.LENGTH_LONG).show();
        } else if (v.getId() == R.id.LLMail) {
            onClickLLMail();

        } else if (v.getId() == R.id.LLCall) {
            onCLickLLCall();

        }
    }

    private void onCLickLLCall() {
        String dial = "tel:618950208";//PONER OBLIGATORIAMENTE tel
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));//ESTO SERÁ LA LLAMADA
        /*Fragment_Dialog_Call fragmentDialogCall = new Fragment_Dialog_Call();
        fragmentDialogCall.show(getActivity().getSupportFragmentManager(), "DialogToCall");*/
    }

    private void onClickLLMail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "info@uworkpod.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Android APP - ");
        startActivity(emailIntent);
    }

    private void onClickLLTyC() {
        Intent terminos = new Intent(getActivity(), WebActivity.class);
        terminos.putExtra("web", "https://www.workpod.app/tyc/");
        startActivity(terminos);
    }
}