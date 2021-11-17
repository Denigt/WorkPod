package com.workpodapp.workpod.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.workpodapp.workpod.AddFacturacionActivity;
import com.workpodapp.workpod.ModPerfilActivity;
import com.workpodapp.workpod.R;
import com.workpodapp.workpod.WorkpodActivity;
import com.workpodapp.workpod.adapters.Adaptador_Lsv_dirfacturacion;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.scale.Scale_Buttons;
import com.workpodapp.workpod.scale.Scale_TextView;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Perfil extends Fragment implements View.OnClickListener {

    // PARAMETROS PARA LA INSTANCIACION DEL FRAGMENT, NO SE USA
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // CONTROLES DEL FRAGMENT
    private ImageButton btnEdit;
    private ImageButton iBUser;
    private Button btnShowInfo;
    private Button btnShowFacturacion;
    private Button btnPassword;
    private TextView txtNombre;
    private TextView tVTituloEmail;
    private TextView txtEmail;
    private TextView tVTituloDNI;
    private TextView txtDNI;
    private TextView tVTituloTlfn;
    private TextView txtTelefono;
    private TextView tVPerfil;
    private TextView tVDirFacturacion;
    private LinearLayout lytPrivate;
    private LinearLayout lytFacturacion;
    private FrameLayout lytShowInfo;
    private FrameLayout lytShowFacturacion;
    private ExpandableListView elsvFacturacion;
    private boolean inicio;

    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView> lstTv;

    // VARIABLES QUE MANEJAN EL ESTADO DEL FRAGMENT
    private boolean showInfo = false;

    public Fragment_Perfil() {
        this.inicio=true;
    }

    /**
     * Crea una instancia del Fragmento Perfil
     *
     * @param param1 Parametro 1
     * @param param2 Parametro 2
     * @return Un objeto Fragment_Perfil inicializado segun los parametros pasados
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Perfil newInstance(String param1, String param2) {
        Fragment_Perfil fragment = new Fragment_Perfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (InfoApp.USER != null) {
            txtNombre.setText(InfoApp.USER.getNombre() + " " + InfoApp.USER.getApellidos());
            txtEmail.setText(InfoApp.USER.getEmail());
            txtDNI.setText(InfoApp.USER.getDni());
            //txtDNI.setText(InfoApp.USER.getTelefono());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (InfoApp.USER != null) {
            txtNombre.setText(InfoApp.USER.getNombre() + " " + InfoApp.USER.getApellidos());
            txtEmail.setText(InfoApp.USER.getEmail());
            txtDNI.setText(InfoApp.USER.getDni());
            //txtDNI.setText(InfoApp.USER.getTelefono());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // BUSQUEDA E INICIALIZACION DE LOS CONTROLES DEL LAYOUT
        btnEdit = view.findViewById(R.id.btnEdit);
        btnShowInfo = view.findViewById(R.id.btnShowInfo);
        btnShowFacturacion = view.findViewById(R.id.btnShowFacturacion);
        btnPassword = view.findViewById(R.id.btnPassword);

        txtNombre = view.findViewById(R.id.txtNombre);
        tVTituloEmail=view.findViewById(R.id.tVTituloEmail);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtDNI = view.findViewById(R.id.txtDNI);
        txtTelefono = view.findViewById(R.id.txtTelefono);
        tVDirFacturacion = view.findViewById(R.id.tVDirFacturacion);
        tVPerfil = view.findViewById(R.id.tVPerfil);
        tVTituloDNI = view.findViewById(R.id.tVTituloDNI);
        tVTituloTlfn = view.findViewById(R.id.tVTituloTlfn);
        iBUser=view.findViewById(R.id.IBUser);

        elsvFacturacion = view.findViewById(R.id.elsvFacturacion);
        lytPrivate = view.findViewById(R.id.lytPrivate);
        lytPrivate.setVisibility(View.GONE);
        lytFacturacion = view.findViewById(R.id.lytFacturacion);
        lytFacturacion.setVisibility(View.GONE);

        lytShowInfo = view.findViewById(R.id.lytShowInfo);
        lytShowFacturacion = view.findViewById(R.id.lytShowFacturacion);

        // DIBUJAR FOREGROUND SI LA VERSION ES MENOR A LA 23
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LinearLayout lyt = view.findViewById(R.id.lytForeground1);
            lyt.setBackground(getContext().getDrawable(R.drawable.rounded_border_button));

           FrameLayout lyt2 = view.findViewById(R.id.lytForeground2);
            lyt2.setForeground(getContext().getDrawable(R.drawable.rounded_border_button));
        }
        // ASIGNACION DE LOS LISTENERS A LOS CONTROLES
        btnEdit.setOnClickListener(this);
        btnShowInfo.setOnClickListener(this);
        btnShowFacturacion.setOnClickListener(this);
        btnPassword.setOnClickListener(this);

        // INICIALIZAR DATOS DEL FRAGMENT SI HAY UN USUARIO REGISTRADO
        if (InfoApp.USER != null) {
            txtNombre.setText(InfoApp.USER.getNombre() + " " + InfoApp.USER.getApellidos());
            txtEmail.setText(InfoApp.USER.getEmail());
            txtDNI.setText(InfoApp.USER.getDni());

            if (InfoApp.USER.getDirFacturacion() != null) {
                Adaptador_Lsv_dirfacturacion adaptador = new Adaptador_Lsv_dirfacturacion(requireContext(), InfoApp.USER.getDirFacturacion());
                elsvFacturacion.setAdapter(adaptador);
                elsvFacturacion.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        return false;
                    }
                });
            } else {
                elsvFacturacion.setVisibility(View.GONE);
                tVDirFacturacion.setText("Sin direcciones de facturación");
            }

            //txtDNI.setText(InfoApp.USER.getTelefono());

        }

        //ESCALAR ELEMENTOS
        escalarElementos();

        //PONEMOS EL ICONO DEL NV EN MENU USUARIO
        WorkpodActivity.btnNV.getMenu().findItem(R.id.inv_menu_user).setChecked(true);

        //Ponemos por defecto que se muestren los datos del usuario
        btnShowInfoOnClick(view);
        return view;
    }

    // LISTENERS DEL FRAGMENT
    @Override
    public void onClick(View v) {
        btnEditOnClick(v);
        btnShowInfoOnClick(v);
        btnShowFacturacionOnClick(v);
        btnPasswordOnClick(v);
    }

    // METODOS ONCLICK DE CADA CONTROL
    private void btnEditOnClick(View v) {
        if (v.getId() == btnEdit.getId()) {
            showInfo = !showInfo;

            Intent activity = new Intent(getContext(), ModPerfilActivity.class);
            startActivity(activity);
        }
    }

    private void btnShowInfoOnClick(View v) {
        if (v.getId() == btnShowInfo.getId() || inicio) {
            //if (showInfo) {
            lytPrivate.setVisibility(View.VISIBLE);
            lytFacturacion.setVisibility(View.GONE);
            lytShowInfo.setBackground(getResources().getDrawable(R.drawable.subrayado));
            lytShowFacturacion.setBackground(null);
            btnPassword.setText("Modificar contraseña");
            /*}else {
                lytPrivate.setVisibility(View.GONE);
                btnShowInfo.setText("Mostrar información personal");
            }*/
        }
    }

    private void btnShowFacturacionOnClick(View v) {
        if (v.getId() == btnShowFacturacion.getId()) {
            lytFacturacion.setVisibility(View.VISIBLE);
            lytPrivate.setVisibility(View.GONE);
            lytShowFacturacion.setBackground(getResources().getDrawable(R.drawable.subrayado));
            lytShowInfo.setBackground(null);
            btnPassword.setText("Añadir dirección");
        }
    }

    private void btnPasswordOnClick(View v) {
        if (v.getId() == btnPassword.getId()) {
            if (lytFacturacion.getVisibility() == View.VISIBLE) {
                Intent activity = new Intent(getContext(), AddFacturacionActivity.class);
                startActivity(activity);
            }
        }
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
    private  void escalarElementos() {
        //INICIALIZAMOS COLECCIONES
        List<View> lstView = new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstView.add( btnShowInfo);
        lstView.add( btnPassword);

        lstView.add( tVPerfil);
        lstView.add( txtNombre);
        lstView.add( txtEmail);
        lstView.add( tVTituloDNI);
        lstView.add( txtDNI);
        lstView.add(tVTituloEmail);
        lstView.add( tVTituloTlfn);
        lstView.add( txtTelefono);
        lstView.add( tVDirFacturacion);
        lstView.add( btnShowInfo);
        lstView.add( btnShowFacturacion);
        lstView.add( lytShowInfo);
        lstView.add( lytShowFacturacion);
        lstView.add( iBUser);

        Method.scaleViews(metrics, lstView);
        //Fuerzo a que tengan el mismo tamaño, mis datos aparece siempre más pequeño
        btnShowInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnShowFacturacion.getTextSize());

        escaladoParticular(metrics);
    }

    private void escaladoParticular(DisplayMetrics metrics) {
        float height = metrics.heightPixels / metrics.density;
        iBUser.getLayoutParams().height = Integer.valueOf((int) Math.round(iBUser.getLayoutParams().height * (height / Method.heightEmulator)));
    }

}