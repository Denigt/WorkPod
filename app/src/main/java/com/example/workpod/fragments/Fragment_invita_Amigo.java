package com.example.workpod.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.basic.Method;
import com.example.workpod.scale.Scale_Buttons;
import com.example.workpod.scale.Scale_Image_View;
import com.example.workpod.scale.Scale_TextView;

import java.util.ArrayList;
import java.util.List;

public class Fragment_invita_Amigo extends Fragment implements View.OnClickListener {

    //XML
    private ConstraintLayout lLJoinFriends;
    private ConstraintLayout lLFreeMinutes;
    private Button btnJoinFriends;
    private Button btnFreeMin;
    private Button btnShareFriendCode;
    private TextView tVInvitaAmigo;
    private TextView tVTituloInvitaAmigo;
    private ImageView iVInvitaAmigo;
    private ImageView iVFlecha_Amigos_Unidos;

    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView> lstTv;
    List<Scale_Image_View> lstIv;

    //ESCALADO
    DisplayMetrics metrics;
    float width;


    public Fragment_invita_Amigo() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invita_amigo, container, false);
        //INSTANCIAMOS LOS ELEMENTOS DEL XML
        btnShareFriendCode = view.findViewById(R.id.BtnShareFriendCode);
        btnFreeMin = view.findViewById(R.id.BtnFreeMin);
        btnJoinFriends = view.findViewById(R.id.BtnJoinFriends);
        lLFreeMinutes = view.findViewById(R.id.LLFreeMin);
        lLJoinFriends = view.findViewById(R.id.LLJoinFriend);
        tVInvitaAmigo=view.findViewById(R.id.TVInvitaAmigo);
        tVTituloInvitaAmigo=view.findViewById(R.id.TVTituloInvitaAmigo);
        iVInvitaAmigo=view.findViewById(R.id.IVInvitaAmigo);
        iVFlecha_Amigos_Unidos=view.findViewById(R.id.IVFlecha_Amigos_Unidos);

        //RESPONDER A LOS EVENTOS
        btnShareFriendCode.setOnClickListener(this);
        btnFreeMin.setOnClickListener(this);
        btnJoinFriends.setOnClickListener(this);
        lLFreeMinutes.setOnClickListener(this);
        lLJoinFriends.setOnClickListener(this);

        //ESCALAMOS ELEMENTOS
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / metrics.density;
        escalarElementos(metrics);

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
        lstTv.add(new Scale_TextView(tVInvitaAmigo, "n", "bold", 15, 17, 20, 280, 80,
                420, 150, 620, 200));

        lstIv.add(new Scale_Image_View(iVInvitaAmigo, 100, 100, 180, 150, 280, 250, "", ""));
        lstIv.add(new Scale_Image_View(iVFlecha_Amigos_Unidos, 40, 42, 70, 72, 88, 90, "", ""));

        Method.scaleBtns(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);
        Method.scaleIv(metrics, lstIv);
    }

    @Override
    public void onClick(View v) {


    }
}