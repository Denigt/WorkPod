package com.example.workpod.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.basic.Method;
import com.example.workpod.data.Usuario;
import com.example.workpod.scale.Scale_Buttons;
import com.example.workpod.scale.Scale_Image_View;
import com.example.workpod.scale.Scale_TextView;

import java.util.ArrayList;
import java.util.List;

public class Fragment_invita_Amigo extends Fragment implements View.OnClickListener {

    //XML
    private ConstraintLayout cLJoinFriends;
    private ConstraintLayout cLFreeMinutes;
    private LinearLayout lLPrincipalAmigosUnidos;
    private LinearLayout lLAmigosUnidos;
    private LinearLayout lLFreeMin;
    private Button btnJoinFriends;
    private Button btnFreeMin;
    private Button btnShareFriendCode;
    private TextView tVInvitaAmigo;
    private TextView tVTituloInvitaAmigo;
    private TextView tVMinGratis;
    private ImageView iVInvitaAmigo;
    private ImageView iVFlecha_Amigos_Unidos;
    private ImageView iVFlecha_Minutos_Gratis;

    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView> lstTv;
    List<Scale_Image_View> lstIv;

    //ESCALADO
    DisplayMetrics metrics;
    float width;

    //BBDD
    List<Usuario>lstUsuario;
    Usuario usuario;

    //OTRAS VARIABLES
    private boolean amigosUnidos; //TRUE desplegado FALSE contraido
    private boolean minutosGratis; //TRUE desplegado FALSE contraido


    public Fragment_invita_Amigo() {
        this.amigosUnidos = false;
        this.lstUsuario=new ArrayList<>();
    }

    public static Fragment_invita_Amigo newInstance(String param1, String param2) {
        Fragment_invita_Amigo fragment = new Fragment_invita_Amigo();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invita_amigo, container, false);
        //INSTANCIAMOS LOS ELEMENTOS DEL XML
        btnShareFriendCode = view.findViewById(R.id.BtnShareFriendCode);
        btnFreeMin = view.findViewById(R.id.BtnFreeMin);
        btnJoinFriends = view.findViewById(R.id.BtnJoinFriends);
        cLFreeMinutes = view.findViewById(R.id.cLFreeMin);
        cLJoinFriends = view.findViewById(R.id.cLJoinFriend);
        lLPrincipalAmigosUnidos = view.findViewById(R.id.LLPrincipalAmigosUnidos);
        lLAmigosUnidos= view.findViewById(R.id.LLAmigosUnidos);
        lLFreeMin = view.findViewById(R.id.LLFreeMin);
        tVInvitaAmigo = view.findViewById(R.id.TVInvitaAmigo);
        tVTituloInvitaAmigo = view.findViewById(R.id.TVTituloInvitaAmigo);
        tVMinGratis=view.findViewById(R.id.TVMinGratis);
        iVInvitaAmigo = view.findViewById(R.id.IVInvitaAmigo);
        iVFlecha_Amigos_Unidos = view.findViewById(R.id.IVFlecha_Amigos_Unidos);
        iVFlecha_Minutos_Gratis = view.findViewById(R.id.IVFlecha_Minutos_Gratis);

        //RESPONDER A LOS EVENTOS
        btnShareFriendCode.setOnClickListener(this);
        btnFreeMin.setOnClickListener(this);
        btnJoinFriends.setOnClickListener(this);
        cLFreeMinutes.setOnClickListener(this);
        cLJoinFriends.setOnClickListener(this);

        //ESCALAMOS ELEMENTOS
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / metrics.density;
        escalarElementos(metrics);

        //CREAMOS PROGRAMATICAMENTE EL USUARIO
       /*
        LinearLayout lLAmigo=new LinearLayout(getActivity());
        lLAmigo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        lLAmigo.setOrientation(LinearLayout.VERTICAL);
        ImageView iVAmigo=new ImageView(getActivity());
        iVAmigo.setLayoutParams(new LinearLayout.LayoutParams(250,250));
        iVAmigo.setImageResource(R.drawable.fill_icon_user_gray);
        iVAmigo.setForegroundGravity(Gravity.CENTER);
        TextView tVAmigo=new TextView(getActivity());
        tVAmigo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        tVAmigo.setForegroundGravity(Gravity.CENTER);
        tVAmigo.setText("Ningún amigo");
        tVAmigo.setTextColor(Color.parseColor("#C1BFBD"));
        tVAmigo.setTextSize(15);
        tVAmigo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        lLAmigo.addView(iVAmigo);
        lLAmigo.addView(tVAmigo);
        lLAmigosUnidos.addView(lLAmigo);*/

        usuario= InfoApp.USER;

        return view;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cLJoinFriend || v.getId() == R.id.BtnJoinFriends || v.getId() == R.id.IVFlecha_Amigos_Unidos) {
            if (!amigosUnidos) {
                desplegarAmigosUnidos();
            } else {
                contraerAmigos();
            }
        } else if (v.getId() == R.id.cLFreeMin || v.getId() == R.id.BtnFreeMin || v.getId() == R.id.IVFlecha_Minutos_Gratis) {
            if (!minutosGratis) {
                desplegarMinutosGratis();
            } else {
                contraerMinutosGratis();
            }
        }else if(v.getId()==R.id.BtnShareFriendCode){
            compartirCodigo();

        }

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
        lstBtn.add(new Scale_Buttons(btnShareFriendCode, "wrap_content", "bold", 20, 23, 23));
        lstBtn.add(new Scale_Buttons(btnJoinFriends, "wrap_content", "bold", 21, 23, 23));
        lstBtn.add(new Scale_Buttons(btnFreeMin, "wrap_content", "bold", 21, 23, 23));

        lstTv.add(new Scale_TextView(tVTituloInvitaAmigo, "wrap_content", "bold", 35, 35, 35));
        lstTv.add(new Scale_TextView(tVInvitaAmigo, "n", "bold", 15, 17, 18, 280, 80,
                420, 150, 650, 200));

        lstIv.add(new Scale_Image_View(iVInvitaAmigo, 100, 100, 180, 150, 280, 250, "", ""));
        lstIv.add(new Scale_Image_View(iVFlecha_Amigos_Unidos, 40, 42, 70, 72, 88, 90, "", ""));

        Method.scaleBtns(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);
        Method.scaleIv(metrics, lstIv);
    }

    /**
     * A través de un intent, podrá enviar el usuario su código amigo a cualquier app q tenga instalada en su móvil que sirva para
     * enviar datos como redes sociales, emails, mensajes...
     */
    private void compartirCodigo() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "El código de tu amigo "+usuario.getNombre()+" para unirte a workpod es "+usuario.getCodamigo());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void contraerMinutosGratis() {
        lLFreeMin.setVisibility(View.GONE);
        iVFlecha_Minutos_Gratis.setImageResource(R.drawable.fill_icon_desplegar_minutos_obtenidos);
        minutosGratis=false;
    }

    private void desplegarMinutosGratis() {
        lLFreeMin.setVisibility(View.VISIBLE);
        tVMinGratis.setText(String.valueOf(usuario.getMinGratis())+" min");
        iVFlecha_Minutos_Gratis.setImageResource(R.drawable.fill_icon_desvanecer_minutos_obtenidos);
        minutosGratis=true;
    }

    private void contraerAmigos() {
        lLPrincipalAmigosUnidos.setVisibility(View.GONE);
        iVFlecha_Amigos_Unidos.setImageResource(R.drawable.fill_icon_desplegar_amigos_invitados);
        amigosUnidos = false;
    }

    /**
     * Al pulsar el btn o el layout de Amigos Unidos por ti, se verán los amigos que se han unido a workpod
     * a través de tu código amigo o en su defecto, te pondrá que no se ha unido ningún amigo.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void desplegarAmigosUnidos() {
        lLPrincipalAmigosUnidos.setVisibility(View.VISIBLE);
        iVFlecha_Amigos_Unidos.setImageResource(R.drawable.fill_icon_desvanecer_amigos_invitados);
        amigosUnidos = true;
    }
}