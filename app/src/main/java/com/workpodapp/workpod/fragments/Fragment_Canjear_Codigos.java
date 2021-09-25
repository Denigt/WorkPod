package com.workpodapp.workpod.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.workpodapp.workpod.R;
import com.workpodapp.workpod.ValoracionWorkpod;
import com.workpodapp.workpod.WorkpodActivity;
import com.workpodapp.workpod.adapters.Adaptador_Lsv_Descuentos;
import com.workpodapp.workpod.basic.Database;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Cupon;
import com.workpodapp.workpod.data.Usuario;
import com.workpodapp.workpod.otherclass.LsV_Descuentos;
import com.workpodapp.workpod.scale.Scale_Buttons;
import com.workpodapp.workpod.scale.Scale_Image_View;
import com.workpodapp.workpod.scale.Scale_TextView;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Canjear_Codigos extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    //XML
    private TextView tV_Titulo_Canjea_Codigos;
    private TextView tV_Descuentos_Sesion;
    private TextView tV_Descuentos_Menu;
    private TextView tV_Descuentos;
    private TextView tV_No_Descuentos;
    private LinearLayout lLShareFriendDescuento;
    private LinearLayout lLDescuentoMenu;
    private LinearLayout lLDescuentoSesion;
    private EditText eTCanjearCodigos;
    private Button btnCancelarDescuento;
    private Button btnGuardarDescuento;
    private Button btnShareFriendCodeDescuento;
    Button ibtnCanjear;
    private ImageView iV_Btn_Cancelar_Descuento;

    Usuario usuario;

    //PARAMETROS PARA LA GESTIÓN DEL LSV
    private ListView lsV_Codigo_Descuento;
    private List<LsV_Descuentos> lstDescuentos = new ArrayList<>();
    private Adaptador_Lsv_Descuentos aLsvDescuentos;
    //ESCALADO
    DisplayMetrics metrics;
    float width;
    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView> lstTv;
    List<Scale_Image_View> lstIv;
    List<Cupon> lstCupones = new ArrayList<>();
    //VARIABLE CONTROLAR IR A CANJEAR_CODIGOS DESDE MENU DE USUARIO
    public static boolean canjearCodigosMU;
    private boolean boolIVCancelar = false;

    public Fragment_Canjear_Codigos() {
        // Required empty public constructor
    }

    public static Fragment_Canjear_Codigos newInstance(String param1, String param2) {
        Fragment_Canjear_Codigos fragment = new Fragment_Canjear_Codigos();
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
        View view = inflater.inflate(R.layout.fragment_canjear_codigos, container, false);
        //INSTANCIAMOS ELEMENTOS DEL XML
        lLDescuentoMenu = view.findViewById(R.id.LLDescuentoMenu);
        lLDescuentoSesion = view.findViewById(R.id.LLDescuentoSesion);
        lLShareFriendDescuento = view.findViewById(R.id.LLShareFriendDescuento);
        tV_Titulo_Canjea_Codigos = view.findViewById(R.id.TV_Titulo_Canjea_Codigos);
        tV_Descuentos_Menu = view.findViewById(R.id.TV_Descuentos_Menu);
        tV_Descuentos = view.findViewById(R.id.TV_Descuentos);
        tV_Descuentos_Sesion = view.findViewById(R.id.TV_Descuentos_Sesion);
        tV_No_Descuentos = view.findViewById(R.id.TV_No_Descuentos);
        iV_Btn_Cancelar_Descuento = view.findViewById(R.id.IV_Btn_Cancelar_Descuento);
        btnCancelarDescuento = view.findViewById(R.id.BtnCancelarDescuento);
        btnGuardarDescuento = view.findViewById(R.id.BtnGuardarDescuento);
        btnShareFriendCodeDescuento = view.findViewById(R.id.BtnShareFriendCodeDescuento);
        eTCanjearCodigos = view.findViewById(R.id.ETCanjearCodigos);
        lsV_Codigo_Descuento = view.findViewById(R.id.Lsv_codigo_descuento);
        ibtnCanjear=view.findViewById(R.id.iBtnCanjear);
//medir rendimiento de las query  block change q las cosas no han sido alteradas con el paso del tiempo. Criptomonedas pone en jaque al sistema financiero mundial. Si un bitcoin cuesta x cuando no hayas sido previsor, stoplost parar inversion
        //LISTENERS
        btnShareFriendCodeDescuento.setOnClickListener(this);
        btnGuardarDescuento.setOnClickListener(this);
        btnCancelarDescuento.setOnClickListener(this);
        iV_Btn_Cancelar_Descuento.setOnClickListener(this);

        //ESCALAMOS ELEMENTOS
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / metrics.density;
        escalarElementos(metrics);
        volcarCupones(view);


        return view;
    }

    private void volcarCupones(View view) {
        usuario = InfoApp.USER;
        Database<Cupon> dbCupon = new Database<>(Database.SELECTUSER, new Cupon());
        dbCupon.postRun(() -> {
            for (Cupon cupon : dbCupon.getLstSelect()) {
                if (!lstCupones.contains(cupon)) {
                    lstCupones.add(cupon);
                }
            }
        });
        dbCupon.postRunOnUI(getActivity(), () -> {
            contruyendoLsV(view);
            if (canjearCodigosMU) {
                accesoMU(view);
            } else {
                accesoSesion();
                //PERMITE QUE AL DARLE ATRÁS HABIENDO ABIERTO ESTE FRAGMENT TRAS FINALIZAR LA SESIÓN, TE LLEVE A VALORACIÓN DE WORKPOD
                WorkpodActivity.boolValoracion = true;
            }
        });
        dbCupon.start();
    }

    /**
     * Se harán visibles los elementos del XML que han de estar activos cuando el usuario accede a este fragment al finalizar una sesión
     */
    private void accesoSesion() {
        tV_Titulo_Canjea_Codigos.setVisibility(View.VISIBLE);
        iV_Btn_Cancelar_Descuento.setVisibility(View.VISIBLE);
        lLDescuentoSesion.setVisibility(View.VISIBLE);
        eTCanjearCodigos.setVisibility(View.VISIBLE);
        btnCancelarDescuento.setVisibility(View.VISIBLE);
        btnGuardarDescuento.setVisibility(View.VISIBLE);
        tV_Descuentos.setVisibility(View.VISIBLE);
        lsV_Codigo_Descuento.setVisibility(View.VISIBLE);
    }

    private void accesoMU(View view) {
        if (!lstCupones.isEmpty()) {
            //  canjearCodigosMU = false;
            tV_Titulo_Canjea_Codigos.setVisibility(View.VISIBLE);
            lLDescuentoMenu.setVisibility(View.VISIBLE);
            eTCanjearCodigos.setVisibility(View.VISIBLE);
            btnCancelarDescuento.setVisibility(View.VISIBLE);
            btnGuardarDescuento.setVisibility(View.VISIBLE);
            tV_Descuentos.setVisibility(View.VISIBLE);
            lsV_Codigo_Descuento.setVisibility(View.VISIBLE);
            lLDescuentoSesion.setVisibility(View.GONE);
            iV_Btn_Cancelar_Descuento.setVisibility(View.GONE);
        } else {
            tV_Titulo_Canjea_Codigos.setVisibility(View.VISIBLE);
            lLDescuentoMenu.setVisibility(View.VISIBLE);
            eTCanjearCodigos.setVisibility(View.VISIBLE);
            btnCancelarDescuento.setVisibility(View.VISIBLE);
            btnGuardarDescuento.setVisibility(View.VISIBLE);
            lLShareFriendDescuento.setVisibility(View.VISIBLE);
            lLDescuentoSesion.setVisibility(View.GONE);
            tV_Descuentos.setVisibility(View.GONE);
            tV_No_Descuentos.setVisibility(View.VISIBLE);
            iV_Btn_Cancelar_Descuento.setVisibility(View.GONE);
            lsV_Codigo_Descuento.setVisibility(View.GONE);
        }

    }

    private void contruyendoLsV(View view) {
        int i = 0;
        for (Cupon cupones : lstCupones) {
            lstDescuentos.add(new LsV_Descuentos(i, cupones.getCampana().getNombre().toString(),
                    (Integer.toString(cupones.getCampana().getDescuento())) + " minutos gratis"));
            i++;
        }
        aLsvDescuentos = new Adaptador_Lsv_Descuentos(view.getContext(), lstDescuentos, metrics, getActivity().getSupportFragmentManager(),lstCupones);
        lsV_Codigo_Descuento.setAdapter(aLsvDescuentos);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        LsV_Descuentos lsV_descuentos = (LsV_Descuentos) aLsvDescuentos.getItem(i);
        if (lsV_descuentos.getId() == 0) {
            Toast.makeText(getActivity(), "23", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.IV_Btn_Cancelar_Descuento) {
            salirFragmentDescuento();
        } else if (v.getId() == R.id.BtnShareFriendCodeDescuento) {
            shareFriendCode();
        } else if (v.getId() == R.id.BtnGuardarDescuento) {
            onClickBtnGuardarDescuento();
        } else if (v.getId() == R.id.BtnCancelarDescuento) {
            onClickBtnCancelarDescuento();
        }
    }

    private void onClickBtnCancelarDescuento() {
        if(!eTCanjearCodigos.getText().toString().equals("")){
           eTCanjearCodigos.setText("");
        }else{
            Toast.makeText(getActivity(),"No has escrito nada",Toast.LENGTH_LONG).show();
        }
    }

    private void onClickBtnGuardarDescuento() {

        if(eTCanjearCodigos.getText().toString().equals("")){
            Toast.makeText(getActivity(),"Ingrese el código de un cupón para guardarlo",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * A través de un intent, podrá enviar el usuario su código amigo a cualquier app q tenga instalada en su móvil que sirva para
     * enviar datos como redes sociales, emails, mensajes...
     */
    private void shareFriendCode() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hola\n ¡Te regalo 15 minutos gratis en la sesión de Workpod que quieras " +
                "canjearlos! Para conseguirlo: descárgate la app de Workpod, introduce una tarjeta de pago y canjea mi código:" +
                usuario.getCodamigo() + "\nConsulta condiciones en:\n https://dev.workpod.app/web/invita_amigo.html");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    /**
     * Cuando le des al IV de cancelar (la X roja) se irá al fragment de valoración workpod
     * Este IV solo aparece si se abre este fragment al finalizar l sesión
     */
    private void salirFragmentDescuento() {
        boolIVCancelar = true;
        Intent activity = new Intent(getActivity(), ValoracionWorkpod.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(activity);
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
        this.lstIv = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstBtn.add(new Scale_Buttons(btnCancelarDescuento, "wrap_content", "bold", 14, 16, 18));
        lstBtn.add(new Scale_Buttons(btnGuardarDescuento, "wrap_content", "bold", 14, 16, 18));
        lstBtn.add(new Scale_Buttons(btnShareFriendCodeDescuento, "wrap_content", "bold", 20, 20, 23));

        lstTv.add(new Scale_TextView(tV_Titulo_Canjea_Codigos, "wrap_content", "bold", 20, 22, 26));
        lstTv.add(new Scale_TextView(tV_Descuentos_Sesion, "wrap_content", "bold", 13, 15, 17));
        lstTv.add(new Scale_TextView(tV_Descuentos_Menu, "wrap_content", "bold", 13, 15, 17));
        lstTv.add(new Scale_TextView(tV_Descuentos, "match_parent", "bold", 16, 18, 22));
        lstTv.add(new Scale_TextView(tV_No_Descuentos, "match_parent", "bold", 22, 18, 22));

        lstIv.add(new Scale_Image_View(iV_Btn_Cancelar_Descuento, 0, 60, 0, 80, 0, 130, "wrap_content", ""));

        Method.scaleBtns(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);
        Method.scaleIv(metrics, lstIv);

        escaladoParticular(metrics, lstBtn);
    }

    private void escaladoParticular(DisplayMetrics metrics, List<Scale_Buttons> lstBtn) {
        if ((width <= (750 / metrics.density)) && (width > (550 / metrics.density))) {
            btnCancelarDescuento.setPadding(60, 0, 60, 0);
            btnGuardarDescuento.setPadding(60, 0, 60, 0);
        } else if (width <= (550 / metrics.density)) {
            btnCancelarDescuento.setPadding(40, 0, 40, 0);
            btnGuardarDescuento.setPadding(40, 0, 40, 0);
            eTCanjearCodigos.setTextSize(16);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}