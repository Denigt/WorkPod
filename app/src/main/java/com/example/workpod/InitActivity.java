package com.example.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

public class InitActivity extends AppCompatActivity implements View.OnClickListener {

    // CONTROLES DEL XML
    private Button btnAcceder;
    private Button btnConectar;
    private Button btnRegistrar;
    private ImageSwitcher imgSwitcher;
    private TextSwitcher txtSwitcher;
    private ArrayList<ImageButton> btnSwither = new ArrayList<ImageButton>();

    // RECURSOS DEL LOS SWITCHER
    private final int[] images = {R.drawable.empty_icon_user, R.drawable.fill_icon_historial, R.drawable.fill_icon_friends};
    private final String[] texts = {"Telefono", "Historial", "Amigos"};
    private int lastPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        // BUSCAR LOS CONTROLES DEL XML
        btnAcceder = findViewById(R.id.btnAcceder);
        btnConectar = findViewById(R.id.btnConectar);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        imgSwitcher = findViewById(R.id.imgSwitcher);
        txtSwitcher = findViewById(R.id.txtSwitcher);
        btnSwither.add(findViewById(R.id.btnSwitcher0));
        btnSwither.add(findViewById(R.id.btnSwitcher1));
        btnSwither.add(findViewById(R.id.btnSwitcher2));
        //btnSwither.add(findViewById(R.id.btnSwitcher3));

        // ESTABLECER EVENTOS PARA LOS CONTROLES
        btnAcceder.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        btnAccederOnClick(v);
        btnConectarOnClick(v);
        btnRegistrarOnClick(v);
        switcherOnClick(v);
        btnSwitcherOnClick(v);
    }

    // CLICK DE LOS BOTONES
    private void btnAccederOnClick(View v){
        if(v.getId() == btnAcceder.getId()){
            // PROVISIONAL PARA NO CAMBIAR EL MANIFIEST
            Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
            startActivity(activity);
        }
    }

    private void btnConectarOnClick(View v){
        if(v.getId() == btnConectar.getId()){

        }
    }

    /**
     * Inicia una actividad para registrarse al pulsar "Registrarse"
     * @param v Vista clicada
     */
    private void btnRegistrarOnClick(View v){
        if(v.getId() == btnRegistrar.getId()){
            Intent activity = new Intent(getApplicationContext(), SigninActivity.class);
            // Indicar la pantalla de registro
            activity.putExtra("screen", 0);
            startActivity(activity,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }

    /**
     * Pasa al siguiente elemento de los Switchers
     * @param v Vista clicada
     */
    private void switcherOnClick(View v){
        if(v.getId() == imgSwitcher.getId() || v.getId() == txtSwitcher.getId()){
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
    private void chgButtons(int btnToChange){
        if (btnToChange < btnSwither.size()){
            for (int i = 0; i < btnSwither.size(); i++){
                if (i != btnToChange)
                    btnSwither.get(i).setImageResource(R.drawable.fill_icon_circle);
                else
                    btnSwither.get(i).setImageResource(R.drawable.empty_icon_circle);
            }
        }
    }
}