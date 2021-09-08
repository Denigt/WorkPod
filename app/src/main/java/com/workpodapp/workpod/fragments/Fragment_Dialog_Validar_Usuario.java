package com.workpodapp.workpod.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.workpodapp.workpod.R;
import com.workpodapp.workpod.basic.Database;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.data.Usuario;
import com.workpodapp.workpod.scale.Scale_Buttons;
import com.workpodapp.workpod.scale.Scale_Image_View;
import com.workpodapp.workpod.scale.Scale_TextView;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Dialog_Validar_Usuario  extends DialogFragment implements View.OnClickListener {

    //XML
    private Button btnResendEmail;
    private ImageView iV_Btn_Cancelar;
    private TextView tV_No_Validado_Usuario;
    private TextView tV_Instrucciones_Enviar_Email;
    //ESCALADO
    DisplayMetrics metrics;
    float width;
    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView> lstTv;
    List<Scale_Image_View> lstIv;

    public Fragment_Dialog_Validar_Usuario() {
        // Required empty public constructor
    }

    public static Fragment_Dialog_Validar_Usuario newInstance(String param1, String param2) {
        Fragment_Dialog_Validar_Usuario fragment = new Fragment_Dialog_Validar_Usuario();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //SOBREESCRITURAS
    //ESTA SOBREESCRITURA PERMITE CREAR UN DIALOGRESULT
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return createDialog();
    }

    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ESPECIFICAMOS DONDE VAMOS A CREAR (INFLAR) EL DIALOGRESULT
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_validar_usuario, null);
        builder.setView(view);

        //INSTANCIAMOS ELEMENTOS DEL XML
        btnResendEmail=view.findViewById(R.id.BtnResendEmail);
        iV_Btn_Cancelar=view.findViewById(R.id.IV_Btn_Cancelar);
        tV_No_Validado_Usuario=view.findViewById(R.id.TV_No_Validado_Usuario);
        tV_Instrucciones_Enviar_Email=view.findViewById(R.id.TV_Instrucciones_Enviar_Email);
        //ESTABLECEMOS EVENTOS PARA LOS CONTROLES
        btnResendEmail.setOnClickListener(this);
        iV_Btn_Cancelar.setOnClickListener(this);
        //ESCALAMOS ELEMENTOS
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / metrics.density;
        escalarElementos(metrics);

        return builder.create();
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

        View view=inflater.inflate(R.layout.fragment_dialog_validar_usuario, container, false);
        //Ubicación del layout en la interfaz gráfica
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        //HACEMOS EL BACKGROUND TRANSPARENTE X Q SI NO, NO PODEMOS HACER QUE LAS ESQUINAS SEAN CURVAS, YA QUE SE VA EL BACKGROUND BLANCO DETRÁS
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return view;
    }

    private void escalarElementos(DisplayMetrics metrics) {
        //INICIALIZAMOS COLECCIONES
        this.lstBtn = new ArrayList<>();
        this.lstTv = new ArrayList<>();
        this.lstIv = new ArrayList<>();

        //LLENAMOS COLECCIONES
        lstBtn.add(new Scale_Buttons(btnResendEmail, "wrap_content", "bold", 18, 18, 18));
        lstTv.add(new Scale_TextView(tV_No_Validado_Usuario ,"match_parent", "bold", 30, 30, 30));
        lstTv.add(new Scale_TextView(tV_Instrucciones_Enviar_Email, "match_parent", "", 20, 20, 20));
        lstIv.add(new Scale_Image_View(iV_Btn_Cancelar, 0, 0, 0, 0, 0, 0, "wrap_content", "wrap_content"));

        Method.scaleBtns(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);
        Method.scaleIv(metrics, lstIv);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.BtnResendEmail){
            resendEmail();
        }else if(v.getId()==R.id.IV_Btn_Cancelar){
           dismiss();
        }
    }

    /**
     * Envía un correo de verificación al usuario que está usando la app
     */
    private void resendEmail() {
        // ENVIAR CORREO DE VERIFICACION
        Database<Usuario> verificacion = new Database<>(Database.VERIFICACION, InfoApp.USER);
        verificacion.start();
        //ECO DE CORREO ENVIADO
        Toast.makeText(getActivity(),"Correo verificación vuelto a enviar",Toast.LENGTH_LONG).show();
        //cerrar el dialog
          dismiss();
    }
}