package com.example.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.workpod.basic.Database;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.basic.Method;
import com.example.workpod.data.Usuario;
import com.example.workpod.scale.Scale_Buttons;
import com.example.workpod.scale.Scale_TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InitActivity extends AppCompatActivity implements View.OnClickListener {

    // CONTROLES DEL XML
    private Button btnAcceder;
    private Button btnAccederSR;
    private Button btnConectar;
    private Button btnRegistrar;
    private ImageSwitcher imgSwitcher;
    private TextSwitcher txtSwitcher;
    private TextView tvTituloInitActivity;
    private ArrayList<ImageButton> btnSwither = new ArrayList<ImageButton>();

    //COLECCIONES
    List<Scale_Buttons> lstBtn;
    List<Scale_TextView>lstTv;

    // RECURSOS DEL LOS SWITCHER
    private final int[] images = {R.drawable.empty_icon_user_21, R.drawable.fill_icon_contact_us_cicle_21, R.drawable.fill_icon_friends};
    private final String[] texts = {"Telefono", "Historial", "Amigos"};
    private int lastPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        // BUSCAR LOS CONTROLES DEL XML
        btnAcceder = findViewById(R.id.btnAcceder);
        btnAccederSR = findViewById(R.id.btnAccederSR);
        btnConectar = findViewById(R.id.btnConectar);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        imgSwitcher = findViewById(R.id.imgSwitcher);
        txtSwitcher = findViewById(R.id.txtSwitcher);
        tvTituloInitActivity = findViewById(R.id.tvTituloInitActivity);
        btnSwither.add(findViewById(R.id.btnSwitcher0));
        btnSwither.add(findViewById(R.id.btnSwitcher1));
        btnSwither.add(findViewById(R.id.btnSwitcher2));

        //btnSwither.add(findViewById(R.id.btnSwitcher3));

        // ESTABLECER EVENTOS PARA LOS CONTROLES
        btnAcceder.setOnClickListener(this);
        btnAccederSR.setOnClickListener(this);
        btnConectar.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);
        imgSwitcher.setOnClickListener(this);
        txtSwitcher.setOnClickListener(this);
        for (ImageButton btn : btnSwither)
            btn.setOnClickListener(this);

        // PREPARAR IMAGE SWITCHER
        imgSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(InitActivity.this);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;
            }
        });
        imgSwitcher.setBackgroundResource(R.color.blue);
        imgSwitcher.setImageResource(images[lastPos]);
        imgSwitcher.setInAnimation(getApplicationContext(), R.anim.left_in);
        imgSwitcher.setOutAnimation(getApplicationContext(), R.anim.left_out);

        // PREPARAR TEXT SWITCHER
        txtSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView txtView = new TextView(InitActivity.this);
                txtView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                txtView.setGravity(Gravity.CENTER);
                return txtView;
            }
        });
        txtSwitcher.setText(texts[lastPos]);
        txtSwitcher.setInAnimation(getApplicationContext(), R.anim.left_in);
        txtSwitcher.setOutAnimation(getApplicationContext(), R.anim.left_out);

        // DIBUJAR FOREGROUND SI LA VERSION ES MENOR A LA 23
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            FrameLayout lyt = findViewById(R.id.lytForeground1);
            lyt.setForeground(getDrawable(R.drawable.rounded_border_button));

            lyt = findViewById(R.id.lytForeground2);
            lyt.setForeground(getDrawable(R.drawable.rounded_border_button));

            lyt = findViewById(R.id.lytForeground3);
            lyt.setForeground(getDrawable(R.drawable.rounded_border_button));
        }

        //ESCALAMOS ELEMENTOS
        escalarElementos();
    }

    @Override
    public void onClick(View v) {
        btnAccederOnClick(v);
        btnAccederSROnClick(v);
        btnConectarOnClick(v);
        btnRegistrarOnClick(v);
        switcherOnClick(v);
        btnSwitcherOnClick(v);
    }

    // CLICK DE LOS BOTONES
    private void btnAccederOnClick(View v) {
        if (v.getId() == btnAcceder.getId()) {
            // PROVISIONAL PARA NO CAMBIAR EL MANIFIEST
            Intent activity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(activity);
        }
    }


    /**
     * Lleva al usuario a la aplicacion principal como usuario no registrado
     * @param v Vista clicada
     */
    private void btnAccederSROnClick(View v) {
        if (v.getId() == btnAccederSR.getId()) {
            Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
            startActivity(activity);
        }
    }

    private void btnConectarOnClick(View v) {
        if (v.getId() == btnConectar.getId()) {

        }
    }

    /**
     * Inicia una actividad para registrarse al pulsar "Registrarse"
     *
     * @param v Vista clicada
     */
    private void btnRegistrarOnClick(View v) {
        if (v.getId() == btnRegistrar.getId()) {
            Intent activity = new Intent(getApplicationContext(), SigninActivity.class);
            // Indicar la pantalla de registro
            activity.putExtra("screen", 0);
            startActivity(activity,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }

    /**
     * Pasa al siguiente elemento de los Switchers
     *
     * @param v Vista clicada
     */
    private void switcherOnClick(View v) {
        if (v.getId() == imgSwitcher.getId() || v.getId() == txtSwitcher.getId()) {
            if (lastPos < (images.length - 1))
                ++lastPos;
            else lastPos = 0;

            chgButtons(lastPos);
            imgSwitcher.setImageResource(images[lastPos]);
            txtSwitcher.setText(texts[lastPos]);
        }
    }

    /**
     * Maneja las pulsaciones a los botones del switcher
     *
     * @param v Vista clicada
     */
    private void btnSwitcherOnClick(View v) {
        int containsId = -1;

        for (int i = 0; i < btnSwither.size(); i++) {
            // Se busca la ID del boton si se encuentra se marca ese boton y se desmarcan los demas
            if (btnSwither.get(i).getId() == v.getId() && containsId == -1) {
                containsId = i;
                btnSwither.get(i).setImageResource(R.drawable.empty_icon_circle);
            }
        }
        if (containsId != -1) {
            chgButtons(containsId);
            lastPos = containsId;

            imgSwitcher.setImageResource(images[lastPos]);
            txtSwitcher.setText(texts[lastPos]);
        }
    }

    // OTROS METODOS
    private void chgButtons(int btnToChange) {
        if (btnToChange < btnSwither.size()) {
            for (int i = 0; i < btnSwither.size(); i++) {
                if (i != btnToChange)
                    btnSwither.get(i).setImageResource(R.drawable.fill_icon_circle);
                else
                    btnSwither.get(i).setImageResource(R.drawable.empty_icon_circle);
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
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstBtn.add(new Scale_Buttons(btnConectar,"Match_Parent","bold",11,11,15));
        lstBtn.add(new Scale_Buttons(btnAcceder,"Match_Parent","bold",12,12,15));
        lstBtn.add(new Scale_Buttons(btnRegistrar,"Match_Parent","bold",25,25,25));

        lstTv.add(new Scale_TextView(tvTituloInitActivity,"Match_Parent","bold",29,29,29));

        Method.scaleBtns(metrics, lstBtn);
        Method.scaleTv(metrics, lstTv);
    }
}