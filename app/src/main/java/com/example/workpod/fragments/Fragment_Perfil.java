package com.example.workpod.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workpod.ModPerfilActivity;
import com.example.workpod.R;
import com.example.workpod.adapters.Adaptador_LsV_Transaction_History;
import com.example.workpod.adapters.Adaptador_Lsv_dirfacturacion;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.basic.Method;
import com.example.workpod.otherclass.LsV_Transaction_History;
import com.example.workpod.scale.Scale_Buttons;
import com.example.workpod.scale.Scale_TextView;

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
    private Button btnShowInfo;
    private Button btnPassword;
    private TextView txtNombre;
    private TextView txtEmail;
    private TextView tVTituloDNI;
    private TextView txtDNI;
    private TextView tVTituloTlfn;
    private TextView txtTelefono;
    private TextView tVPerfil;
    private TextView tVDirFacturacion;
    private LinearLayout lytPrivate;
    private ExpandableListView elsvFacturacion;

    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView>lstTv;

    // VARIABLES QUE MANEJAN EL ESTADO DEL FRAGMENT
    private boolean showInfo = false;

    public Fragment_Perfil() {
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
        btnPassword=view.findViewById(R.id.btnPassword);

        txtNombre = view.findViewById(R.id.txtNombre);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtDNI = view.findViewById(R.id.txtDNI);
        txtTelefono = view.findViewById(R.id.txtTelefono);
        tVDirFacturacion=view.findViewById(R.id.tVDirFacturacion);
        tVPerfil=view.findViewById(R.id.tVPerfil);
        tVTituloDNI=view.findViewById(R.id.tVTituloDNI);
        tVTituloTlfn=view.findViewById(R.id.tVTituloTlfn);

        elsvFacturacion = view.findViewById(R.id.elsvFacturacion);
        lytPrivate = view.findViewById(R.id.lytPrivate);
        lytPrivate.setVisibility(View.GONE);

        // DIBUJAR FOREGROUND SI LA VERSION ES MENOR A LA 23
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            FrameLayout lyt = view.findViewById(R.id.lytForeground1);
            lyt.setForeground(getContext().getDrawable(R.drawable.rounded_border_button));

            lyt = view.findViewById(R.id.lytForeground2);
            lyt.setForeground(getContext().getDrawable(R.drawable.rounded_border_button));
        }
        // ASIGNACION DE LOS LISTENERS A LOS CONTROLES
        btnEdit.setOnClickListener(this);
        btnShowInfo.setOnClickListener(this);

        // INICIALIZAR DATOS DEL FRAGMENT SI HAY UN USUARIO REGISTRADO
        if (InfoApp.USER != null) {
            txtNombre.setText(InfoApp.USER.getNombre() + " " + InfoApp.USER.getApellidos());
            txtEmail.setText(InfoApp.USER.getEmail());
            txtDNI.setText(InfoApp.USER.getDni());

            Adaptador_Lsv_dirfacturacion adaptador = new Adaptador_Lsv_dirfacturacion(requireContext(), InfoApp.USER.getDirFacturacion());
            elsvFacturacion.setAdapter(adaptador);
            elsvFacturacion.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return false;
                }
            });

            //txtDNI.setText(InfoApp.USER.getTelefono());
        }

        //ESCALAR ELEMENTOS
        escalarElementos();

        return view;
    }

    // LISTENERS DEL FRAGMENT
    @Override
    public void onClick(View v) {
        btnEditOnClick(v);
        btnShowInfoOnClick(v);
    }

    // METODOS ONCLICK DE CADA CONTROL
    private void btnEditOnClick(View v) {
        if (v.getId() == btnEdit.getId()){
            showInfo = !showInfo;

            Intent activity = new Intent(getContext(), ModPerfilActivity.class);
            startActivity(activity);
        }
    }

    private void btnShowInfoOnClick(View v) {
        if (v.getId() == btnShowInfo.getId()){
            showInfo = !showInfo;

            if (showInfo) {
                lytPrivate.setVisibility(View.VISIBLE);
                btnShowInfo.setText("Ocultar información personal");
            }else {
                lytPrivate.setVisibility(View.GONE);
                btnShowInfo.setText("Mostrar información personal");
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
     *
     * Como el método scale de la clase Methods no es un activity o un fragment no podemos inicializar nuestro objeto de la clase
     * DisplayMetrics con los parámetros reales de nuestro móvil, es por ello que lo inicializamos en este método.
     *
     * En resumen, en este método inicializamos el metrics y las colecciones y se lo pasamos al método de la clase Methods
     *
     */
    private void escalarElementos() {
        //INICIALIZAMOS COLECCIONES
        this.lstBtn=new ArrayList<>();
        this.lstTv=new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstBtn.add(new Scale_Buttons(btnShowInfo,"match_parent","bold",14,14));
        lstBtn.add(new Scale_Buttons(btnPassword,"match_parent","bold",14,14));

        lstTv.add(new Scale_TextView(tVPerfil,"match_parent","bold",40,40));
        lstTv.add(new Scale_TextView(txtNombre,"match_parent","normal",15,15));
        lstTv.add(new Scale_TextView(txtEmail,"match_parent","bold",16,16));
        lstTv.add(new Scale_TextView(tVTituloDNI,"match_parent","bold",18,18));
        lstTv.add(new Scale_TextView(txtDNI,"match_parent","bold",16,16));
        lstTv.add(new Scale_TextView(tVTituloTlfn,"match_parent","bold",18,18));
        lstTv.add(new Scale_TextView(txtTelefono,"match_parent","bold",16,16));
        lstTv.add(new Scale_TextView(tVDirFacturacion,"match_parent","bold",18,18));

        Method.scaleButtons(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);
    }
}