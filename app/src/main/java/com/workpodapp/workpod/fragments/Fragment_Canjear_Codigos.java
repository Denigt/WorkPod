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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Canjear_Codigos extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    //XML
    private TextView tV_Titulo_Canjea_Codigos;
    private TextView tV_Descuentos;
    private TextView tV_No_Descuentos;
    private TextView tV_MGM_Minutos_Estandar;
    private TextView tV_MGM_Minutos_Titulo;
    private TextView tV_MGM_Minutos;
    private TextView tV_MGM_Amigos;
    private TextView tV_MGM_Amigos_Titulo;
    private TextView tV_MGM;

    private LinearLayout lLShareFriendDescuento;
    private LinearLayout lLMGM;
    private LinearLayout lLDatosMGM;

    private ImageView iV_Give_Five;
    private ImageView iV_Btn_Volver;
    private ImageView iV_Btn_Siguiente;

    private EditText eTCanjearCodigos;

    private Button btnCancelarDescuento;
    private Button btnGuardarDescuento;
    private Button btnShareFriendCodeDescuento;
    private Button btnShareMoreFriendCode;
    private Button ibtnCanjear;


    Usuario usuario;
    Cupon cupon;
    private int errorCode;

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
    List<Cupon> lstAmigos = new ArrayList<>();


    private boolean boolIVCancelar = false;

    public Fragment_Canjear_Codigos() {
        this.errorCode = 0;
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
        lLDatosMGM = view.findViewById(R.id.LLDatosMGM);
        lLMGM = view.findViewById(R.id.LLMGM);
        lLShareFriendDescuento = view.findViewById(R.id.LLShareFriendDescuento);

        tV_Titulo_Canjea_Codigos = view.findViewById(R.id.TV_Titulo_Canjea_Codigos);
        tV_Descuentos = view.findViewById(R.id.TV_Descuentos);
        tV_No_Descuentos = view.findViewById(R.id.TV_No_Descuentos);
        tV_MGM_Amigos = view.findViewById(R.id.TV_MGM_Amigos);
        tV_MGM_Amigos_Titulo = view.findViewById(R.id.TV_MGM_Amigos_Titulo);
        tV_MGM_Minutos = view.findViewById(R.id.TV_MGM_Minutos);
        tV_MGM_Minutos_Estandar = view.findViewById(R.id.TV_MGM_Minutos_Estandar);
        tV_MGM_Minutos_Titulo = view.findViewById(R.id.TV_MGM_Minutos_Titulo);
        tV_MGM=view.findViewById(R.id.TV_MGM);

        iV_Btn_Siguiente = view.findViewById(R.id.IV_Btn_Siguiente);
        iV_Btn_Volver = view.findViewById(R.id.IV_Btn_Volver);
        iV_Give_Five = view.findViewById(R.id.IV_Give_Five);

        btnCancelarDescuento = view.findViewById(R.id.BtnCancelarDescuento);
        btnGuardarDescuento = view.findViewById(R.id.BtnGuardarDescuento);
        btnShareFriendCodeDescuento = view.findViewById(R.id.BtnShareFriendCodeDescuento);
        btnShareMoreFriendCode = view.findViewById(R.id.BtnShareMoreFriendCode);

        eTCanjearCodigos = view.findViewById(R.id.ETCanjearCodigos);
        lsV_Codigo_Descuento = view.findViewById(R.id.Lsv_codigo_descuento);
        ibtnCanjear = view.findViewById(R.id.iBtnCanjear);

        //LISTENERS
        btnShareFriendCodeDescuento.setOnClickListener(this);
        btnShareMoreFriendCode.setOnClickListener(this);
        btnGuardarDescuento.setOnClickListener(this);
        btnCancelarDescuento.setOnClickListener(this);
        iV_Btn_Volver.setOnClickListener(this);
        iV_Btn_Siguiente.setOnClickListener(this);

        //ESCALAMOS ELEMENTOS
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / metrics.density;

        volcarCupones(view);

        //PONEMOS EL ICONO DEL NV EN MENU USUARIO
        WorkpodActivity.btnNV.getMenu().findItem(R.id.inv_menu_user).setChecked(true);

        //Inicializamos TV_MGM con el MGM del usuario
        tV_MGM.setText("MGM: "+InfoApp.USER.getCodamigo());

        escalarElementos(metrics);

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
            accesoMU(view);

        });
        dbCupon.start();
    }

    private void accesoMU(View view) {
        if (!lstCupones.isEmpty()) {
            lLMGM.setVisibility(View.VISIBLE);
            tV_Titulo_Canjea_Codigos.setVisibility(View.VISIBLE);
            eTCanjearCodigos.setVisibility(View.VISIBLE);
            btnCancelarDescuento.setVisibility(View.VISIBLE);
            btnGuardarDescuento.setVisibility(View.VISIBLE);
            tV_Descuentos.setVisibility(View.VISIBLE);
            lsV_Codigo_Descuento.setVisibility(View.VISIBLE);
            //Le pasamos a la lista de amigos, los cupones de Por Invitar a un Amigo
            lstAmigos.clear();
            for (Cupon cupon : lstCupones) {
                if (cupon.getCampana().getNombre().equalsIgnoreCase("Por Invitar a un Amigo"))
                    lstAmigos.add(cupon);
            }

            if (lstAmigos.isEmpty()) {
                iV_Btn_Siguiente.setVisibility(View.GONE);
            } else {
                //Inicializamos los minutos y los amigos conseguidos
                tV_MGM_Minutos.setText(Integer.toString(lstAmigos.size() * lstAmigos.get(0).getCampana().getDescuento()) + " minutos");
                tV_MGM_Amigos.setText(Integer.toString(lstAmigos.size()) + " amigos");
                if (lstAmigos.size() == 1)
                    tV_MGM_Amigos.setText(Integer.toString(lstAmigos.size()) + " amigo");
            }

        } else {
            lLMGM.setVisibility(View.VISIBLE);
            iV_Btn_Siguiente.setVisibility(View.GONE);
            tV_Titulo_Canjea_Codigos.setVisibility(View.VISIBLE);
            eTCanjearCodigos.setVisibility(View.VISIBLE);
            btnCancelarDescuento.setVisibility(View.VISIBLE);
            btnGuardarDescuento.setVisibility(View.VISIBLE);
            lLShareFriendDescuento.setVisibility(View.VISIBLE);
            tV_Descuentos.setVisibility(View.GONE);
            tV_No_Descuentos.setVisibility(View.VISIBLE);
            lsV_Codigo_Descuento.setVisibility(View.GONE);
        }

    }

    private void contruyendoLsV(View view) {
        //LIMPIAMOS EL LSV
        lstDescuentos.clear();
        int i = 0;

        aLsvDescuentos = new Adaptador_Lsv_Descuentos(view.getContext(), metrics, getActivity().getSupportFragmentManager(), lstCupones);
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
        if (v.getId() == R.id.BtnShareFriendCodeDescuento || v.getId() == R.id.BtnShareMoreFriendCode) {
            shareFriendCode();
        } else if (v.getId() == R.id.BtnGuardarDescuento) {
            onClickBtnGuardarDescuento(v);
        } else if (v.getId() == R.id.BtnCancelarDescuento) {
            onClickBtnCancelarDescuento();
        } else if (v.getId() == R.id.IV_Btn_Siguiente) {
            onClickIV_Btn_Siguiente();
        } else if (v.getId() == R.id.IV_Btn_Volver) {
            onClickIV_Btn_Volver();
        }
    }

    private void onClickIV_Btn_Volver() {
        lLMGM.setVisibility(View.VISIBLE);
        lLDatosMGM.setVisibility(View.GONE);
    }

    private void onClickIV_Btn_Siguiente() {
        lLMGM.setVisibility(View.GONE);
        lLDatosMGM.setVisibility(View.VISIBLE);
    }

    private void onClickBtnCancelarDescuento() {
        if (!eTCanjearCodigos.getText().toString().equals("")) {
            eTCanjearCodigos.setText("");
        } else {
            Toast.makeText(getActivity(), "No has escrito nada", Toast.LENGTH_LONG).show();
        }
    }

    private void onClickBtnGuardarDescuento(View view) {

        if (eTCanjearCodigos.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Ingrese el código de un cupón para guardarlo", Toast.LENGTH_LONG).show();
        } else if (eTCanjearCodigos.getText().toString().trim().equals(InfoApp.USER.getCodamigo())) {
            Toast.makeText(getActivity(), "No puedes meter tu código amigo", Toast.LENGTH_LONG).show();

        } else {
            //INICIALIZAMOS LA VARIABLE CUPÓN
            cupon = new Cupon();
            cupon.setCodigo(eTCanjearCodigos.getText().toString());
            cupon.setfInsertado(ZonedDateTime.now());
            cupon.setUsuario(usuario.getId());

            //REALIZAMOS EL INSERT
            Database<Cupon> insert = new Database<>(Database.INSERT, cupon);
            insert.postRun(() -> {
                //LIMPIAMOS EL ET
                eTCanjearCodigos.setText("");

            });
            insert.postRunOnUI(getActivity(), () -> {

                if (insert.getError().code > -1) {
                    //REFRESCAMOS EL FRAGMENT SIN QUE SE REPITA
                    Fragment_Canjear_Codigos fragment_canjear_codigos = new Fragment_Canjear_Codigos();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragment_canjear_codigos).commit();
                } else if (insert.getError().code > -2) {
                    Toast.makeText(getActivity(), insert.getError().message, Toast.LENGTH_LONG).show();
                } else if (insert.getError().code < -10) {
                    Toast.makeText(getActivity(), "Ya has ingresado ese cupón", Toast.LENGTH_LONG).show();
                }

            });
            insert.start();
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
                usuario.getCodamigo() + "\nConsulta condiciones en:\n https://github.com/Denigt/WorkPod/raw/master/Workpod_MW.apk");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
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
        String n = InfoApp.INSTALLATION;

        //LLENAMOS COLECCIONES
        lstBtn.add(new Scale_Buttons(btnCancelarDescuento, "wrap_content", "bold", 13, 16, 17));
        lstBtn.add(new Scale_Buttons(btnGuardarDescuento, "wrap_content", "bold", 13, 16, 17));
        lstBtn.add(new Scale_Buttons(btnShareFriendCodeDescuento, "wrap_content", "bold", 14, 17, 20));
        lstBtn.add(new Scale_Buttons(btnShareMoreFriendCode, "wrap_content", "bold", 14, 16, 20));

        lstTv.add(new Scale_TextView(tV_Titulo_Canjea_Codigos, "wrap_content", "bold", 18, 21, 26));
        lstTv.add(new Scale_TextView(tV_Descuentos, "match_parent", "bold", 16, 18, 22));
        lstTv.add(new Scale_TextView(tV_No_Descuentos, "match_parent", "bold", 22, 18, 22));
        lstTv.add(new Scale_TextView(tV_MGM_Minutos_Estandar, "wrap_content", "bold", 13, 16, 18));
        lstTv.add(new Scale_TextView(tV_MGM_Amigos, "wrap_content", "bold", 14, 19, 22));
        lstTv.add(new Scale_TextView(tV_MGM_Amigos_Titulo, "wrap_content", "bold", 14, 19, 22));
        lstTv.add(new Scale_TextView(tV_MGM_Minutos, "wrap_content", "bold", 14, 19, 22));
        lstTv.add(new Scale_TextView(tV_MGM_Minutos_Titulo, "wrap_content", "bold", 14, 19, 22));
        lstTv.add(new Scale_TextView(tV_MGM, "wrap_content", "bold", 15, 15, 15));

        lstIv.add(new Scale_Image_View(iV_Btn_Volver, 50, 50, 100, 100, 130, 130, "", ""));
        lstIv.add(new Scale_Image_View(iV_Btn_Siguiente, 50, 50, 80, 80, 120, 120, "", ""));
        lstIv.add(new Scale_Image_View(iV_Give_Five, 150, 100, 220, 150, 350, 200, "", ""));

        Method.scaleBtns(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);
        Method.scaleIv(metrics, lstIv);

        escaladoParticular(metrics, lstBtn);
    }

    private void escaladoParticular(DisplayMetrics metrics, List<Scale_Buttons> lstBtn) {
        if ((width <= (750 / metrics.density)) && (width > (550 / metrics.density))) {
            btnCancelarDescuento.setPadding(60, 0, 60, 0);
            btnGuardarDescuento.setPadding(60, 0, 60, 0);
            tV_Titulo_Canjea_Codigos.setText("Canjea tus códigos de descuento");
        } else if (width <= (550 / metrics.density)) {
            btnCancelarDescuento.setPadding(40, 0, 40, 0);
            btnGuardarDescuento.setPadding(40, 0, 40, 0);
            btnShareFriendCodeDescuento.setPadding(0, 0, 0, 0);
            eTCanjearCodigos.setTextSize(14);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 10, 10, 10);
            lLShareFriendDescuento.setLayoutParams(params);
            tV_Titulo_Canjea_Codigos.setText("Canjea tus códigos de descuento");
            btnShareMoreFriendCode.getLayoutParams().height = 50;
            btnShareFriendCodeDescuento.getLayoutParams().height = 50;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}