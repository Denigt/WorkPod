package com.workpodapp.workpod.fragments;

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

import com.workpodapp.workpod.R;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Usuario;
import com.workpodapp.workpod.scale.Scale_Image_View;

import java.util.ArrayList;
import java.util.List;

public class Fragment_invita_Amigo extends Fragment implements View.OnClickListener {

    //XML
    private ConstraintLayout cLJoinFriends;
    private ConstraintLayout cLFreeMinutes;
    private LinearLayout lLPrincipalAmigosUnidos;
    private LinearLayout lLAmigosUnidos;
    private LinearLayout lLFreeMin;
    private LinearLayout lLShareFriend;
    private Button btnJoinFriends;
    private Button btnFreeMin;
    private Button btnShareFriendCode;
    private TextView tVInvitaAmigo;
    private TextView tVTituloInvitaAmigo;
    private TextView tVMinGratisDisponibles;
    private TextView tVTituloMinGratisDisponibles;
    private TextView tVTituloMinGratisTotales;
    private TextView tVMinGratisTotales;
    private TextView tVMinGratisGastados;
    private ImageView iVInvitaAmigo;
    private ImageView iVSinAmigos;
    private TextView tVSinAmigos;
    private ImageView iVFlecha_Amigos_Unidos;
    private ImageView iVFlecha_Minutos_Gratis;
    private float widht = 0;

    //COLECCIONES
    List<Scale_Image_View> lstIv;

    //ESCALADO
    DisplayMetrics metrics;

    //BBDD
    List<String> lstAmigos;
    Usuario usuario;
    private int nFriends;

    //OTRAS VARIABLES
    private boolean amigosUnidos; //TRUE desplegado FALSE contraido
    private boolean minutosGratis; //TRUE desplegado FALSE contraido


    public Fragment_invita_Amigo() {
        this.amigosUnidos = false;
        this.lstAmigos = new ArrayList<>();
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
        lLAmigosUnidos = view.findViewById(R.id.LLAmigosUnidos);
        lLFreeMin = view.findViewById(R.id.LLFreeMin);
        lLShareFriend = view.findViewById(R.id.LLShareFriend);
        tVInvitaAmigo = view.findViewById(R.id.TVInvitaAmigo);
        tVTituloInvitaAmigo = view.findViewById(R.id.TVTituloInvitaAmigo);
        tVTituloMinGratisDisponibles = view.findViewById(R.id.TVTituloMinGratisDisponibles);
        tVMinGratisDisponibles = view.findViewById(R.id.TVMinGratisDisponibles);
        tVMinGratisTotales = view.findViewById(R.id.TVMinGratisTotales);
        tVMinGratisGastados = view.findViewById(R.id.TVMinGratisGastados);
        tVTituloMinGratisTotales = view.findViewById(R.id.TVTituloMinGratisTotales);
        iVInvitaAmigo = view.findViewById(R.id.IVInvitaAmigo);
        iVFlecha_Amigos_Unidos = view.findViewById(R.id.IVFlecha_Amigos_Unidos);
        iVFlecha_Minutos_Gratis = view.findViewById(R.id.IVFlecha_Minutos_Gratis);

        //RESPONDER A LOS EVENTOS
        btnShareFriendCode.setOnClickListener(this);
        btnFreeMin.setOnClickListener(this);
        btnJoinFriends.setOnClickListener(this);
        cLFreeMinutes.setOnClickListener(this);
        cLJoinFriends.setOnClickListener(this);
        lLShareFriend.setOnClickListener(this);

        //ESCALAMOS ELEMENTOS
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        escalarElementos(metrics);

        //RELLENAMOS LISTA DE AMIGOS UNIDOS POR EL USUARIO (PROVISIONAL)
        lstAmigos.add("Juan Luis");
        lstAmigos.add("Álvaro Pardo");
        lstAmigos.add("Alexis Nicolas");
        lstAmigos.add("Pablo García");
        lstAmigos.add("Raúl Parra");

        usuario = InfoApp.USER;

        //INICIALIZAMOS EL Nº DE AMIGOS UNIDOS A LA APP POR EL USUARIO
        nFriends = usuario.getnAmigos();

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
        } else if (v.getId() == R.id.BtnShareFriendCode || v.getId() == R.id.LLShareFriend) {
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
    private <T extends View> void escalarElementos(DisplayMetrics metrics) {
        //INICIALIZAMOS COLECCIONES
        List<T> lstView = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstView.add((T) btnShareFriendCode);
        lstView.add((T) btnJoinFriends);
        lstView.add((T) btnFreeMin);

        lstView.add((T) tVTituloInvitaAmigo);
        lstView.add((T) tVTituloMinGratisTotales);
        lstView.add((T) tVTituloMinGratisDisponibles);
        lstView.add((T) tVMinGratisGastados);
        lstView.add((T) tVMinGratisDisponibles);
        lstView.add((T) tVMinGratisTotales);
        lstView.add((T) tVInvitaAmigo);

        lstView.add((T) iVInvitaAmigo);
        lstView.add((T) iVFlecha_Amigos_Unidos);

        Method.scaleViews(metrics, lstView);
    }

    /**
     * A través de un intent, podrá enviar el usuario su código amigo a cualquier app q tenga instalada en su móvil que sirva para
     * enviar datos como redes sociales, emails, mensajes...
     */
    private void compartirCodigo() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hola\n ¡Te regalo 15 minutos gratis en la sesión de Workpod que quieras " +
                "canjearlos! Para conseguirlo: descárgate la app de Workpod, introduce una tarjeta de pago y canjea mi código:" +
                usuario.getCodamigo() + "\nConsulta condiciones en:\n https://dev.workpod.app/web/invita_amigo.html");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void contraerMinutosGratis() {
        lLFreeMin.setVisibility(View.GONE);
        iVFlecha_Minutos_Gratis.setImageResource(R.drawable.fill_icon_desplegar_minutos_obtenidos);
        minutosGratis = false;
    }

    private void desplegarMinutosGratis() {
        lLFreeMin.setVisibility(View.VISIBLE);
        tVMinGratisDisponibles.setText(String.valueOf(usuario.getMinGratis()) + " min");
        //CALCULAMOS LOS MIN TOTALES OBTENIDOS, NO ES NECESARIO CREAR UN CAMPO EN LA BD, SI ESTÁ ESTANDARIZADO A 20 MIN POR AMIGO CON MULTIPLICAR
        //EL Nº DE AMIGOS X 20 YA ESTARÍA
        tVMinGratisTotales.setText(String.valueOf(nFriends * 20) + " min");
        //CALCULAMOS LA DIFERENCIA ENTRE LOS MINUTOS OBTENIDOS Y LOS MINUTS QUE AL USUARIO LE QUEDAN DISPONIBLES
        tVMinGratisGastados.setText("Has gastado " + String.valueOf((nFriends * 20 - usuario.getMinGratis())) + " min");
        iVFlecha_Minutos_Gratis.setImageResource(R.drawable.fill_icon_desvanecer_minutos_obtenidos);
        minutosGratis = true;
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
    private void desplegarAmigosUnidos() {
        lLPrincipalAmigosUnidos.setVisibility(View.VISIBLE);
        iVFlecha_Amigos_Unidos.setImageResource(R.drawable.fill_icon_desvanecer_amigos_invitados);
        amigosUnidos = true;
        if (nFriends != 0) {
            //LIMPIAMOS EL LAYOUT PARA Q NO APAREZCA EL ICONO DE NO AMIGOS
            lLAmigosUnidos.removeAllViews();
            lstIv.removeAll(lstIv);
            LinearLayout lLAmigo = new LinearLayout(getActivity());
            lLAmigo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            lLAmigo.setOrientation(LinearLayout.VERTICAL);
            lLAmigo.setGravity(Gravity.CENTER);
            ImageView iVAmigo = new ImageView(getActivity());
            TextView tVAmigo = new TextView(getActivity());
            //ESCALAMOS LOS ICONOS EN FUNCIÓN DEL TAMAÑO Y LA DENSIDAD MÉTRICA DE LA PANTALLA
           float width = metrics.widthPixels / metrics.density;
            if (width > (1200 / metrics.density)) {
                iVAmigo.setLayoutParams(new LinearLayout.LayoutParams(420, 420));
                tVAmigo.setTextSize(20);
            } else if ((width <= (1200 / metrics.density)) && (width > (750 / metrics.density))) {
                iVAmigo.setLayoutParams(new LinearLayout.LayoutParams(320, 320));
                tVAmigo.setTextSize(20);
            } else if ((width <= (750 / metrics.density)) && (width > (550 / metrics.density))) {
                iVAmigo.setLayoutParams(new LinearLayout.LayoutParams(215, 215));
                tVAmigo.setTextSize(20);
            } else if (width <= (550 / metrics.density)) {
                iVAmigo.setLayoutParams(new LinearLayout.LayoutParams(140, 140));
                tVAmigo.setTextSize(15);
            }
            iVAmigo.setImageResource(R.drawable.fill_icon_user_orange);
            tVAmigo.setGravity(Gravity.CENTER);
            tVAmigo.setText("Tienes " + nFriends + " amigos en Workpod");
            tVAmigo.setTextColor(Color.parseColor("#C5A475"));
            tVAmigo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 15, 0, 20);
            tVAmigo.setLayoutParams(params);
            lLAmigo.addView(iVAmigo);
            lLAmigo.addView(tVAmigo);
            lLAmigosUnidos.addView(lLAmigo);

        }
    }
}