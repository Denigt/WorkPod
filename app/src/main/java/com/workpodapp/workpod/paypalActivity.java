package com.workpodapp.workpod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.Order;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PayPalButton;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.fragments.Fragment_Dialog_Call;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class paypalActivity extends AppCompatActivity implements View.OnClickListener {
    //PAYPAL
    public static final String PAYPAL_CLIENT_ID = "ASa6o8cPU2CMHqXpOSz0YGP8J89btftABuXRNG6JxhBSfBP2iqlV3RJrd_JJJPT9Kq7rdU-qj_n93LzS";
    private String precio;
    PayPalButton payPalButton;

    //XML
    private TextView tVPayPalTitulo;
    private ImageView iVPayPal;
    private TextView tVInstrucciones;
    private TextView tVMasInstrucciones;
    private TextView tVSugerencias;
    private LinearLayout lLPayPalFAQ;
    private TextView tVPayPalFAQ;
    private ImageView iVPayPalFAQ;
    private LinearLayout lLPayPalCall;
    private TextView tVPayPalCall;
    private ImageView iVPayPalCall;
    private TextView tVTyCs;

    //Booleano para controlar q una vez hecho el pago no le de al usuario x acceder a las FAQ o llamar o TyC, x q la app tarda como 1s en pasar al siguiente activity
    private boolean pagado = false;

    public paypalActivity() {
        //this.precio=getIntent().getExtras().getString("PrecioSesion");
        this.precio = String.valueOf(InfoApp.PRECIO_FINAL_SESION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        //Bindeamos el XML con java
        lLPayPalFAQ = findViewById(R.id.LLPayPalFAQ);
        lLPayPalCall = findViewById(R.id.LLPayPalCall);
        tVInstrucciones = findViewById(R.id.TVInstrucciones);
        tVMasInstrucciones = findViewById(R.id.TVMasInstrucciones);
        tVPayPalFAQ = findViewById(R.id.TVPayPalFAQ);
        tVPayPalTitulo = findViewById(R.id.TVPayPalTitulo);
        tVPayPalCall = findViewById(R.id.TVPayPalCall);
        tVSugerencias = findViewById(R.id.TVSugerencias);
        tVTyCs = findViewById(R.id.TVTyCs);
        iVPayPal = findViewById(R.id.IVPayPal);
        iVPayPalFAQ = findViewById(R.id.IVPayPalFAQ);
        iVPayPalCall = findViewById(R.id.IVPayPalCall);

        payPalButton = findViewById(R.id.payPalButton);
        CheckoutConfig config = new CheckoutConfig(
                getApplication(),
                PAYPAL_CLIENT_ID,
                Environment.SANDBOX,
                String.format("%s://paypalpay", BuildConfig.APPLICATION_ID),
                CurrencyCode.EUR,
                UserAction.PAY_NOW,
                new SettingsConfig(
                        true,
                        false
                )
        );
        PayPalCheckout.setConfig(config);
        payPalButton.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        ArrayList purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.EUR)
                                                        .value(precio)
                                                        .build()
                                        )
                                        .build()
                        );
                        Order order = new Order(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result));
                                String resultado = String.valueOf(result);
                                if (resultado.contains("Success")) {
                                    pagado = true;
                                    Intent activity = new Intent(getApplication(), ValoracionWorkpod.class);
                                    activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    finishAffinity();
                                    startActivity(activity);
                                }
                            }
                        });
                    }
                }
        );
        escalarElementos();

        tVTyCs.setOnClickListener(this);
        lLPayPalFAQ.setOnClickListener(this);
        lLPayPalCall.setOnClickListener(this);
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
    private void escalarElementos() {
        DisplayMetrics metrics = new DisplayMetrics();
        //INICIALIZAMOS EL OBJETO DISPLAYMETRICS CON LOS PARÁMETROS DE NUESTRO DISPOSITIVO
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //INICIALIZAMOS COLECCIONES
        List<View> lstViews = new ArrayList<>();
        lstViews.add(lLPayPalFAQ);
        lstViews.add(lLPayPalCall);
        lstViews.add(tVInstrucciones);
        lstViews.add(tVMasInstrucciones);
        lstViews.add(tVPayPalFAQ);
        lstViews.add(tVPayPalTitulo);
        lstViews.add(tVPayPalCall);
        lstViews.add(tVSugerencias);
        lstViews.add(iVPayPal);
        lstViews.add(iVPayPalFAQ);
        lstViews.add(iVPayPalCall);
        lstViews.add(tVTyCs);
        //LLENAMOS COLECCIONES
        Method.scaleViews(metrics, lstViews);
        escaladoParticular(metrics);

    }

    private void escaladoParticular(DisplayMetrics metrics) {
        float height = metrics.heightPixels / metrics.density;
        iVPayPalFAQ.getLayoutParams().height = Integer.valueOf((int) Math.round(iVPayPalFAQ.getLayoutParams().height * (height / Method.heightEmulator)));
        iVPayPalCall.getLayoutParams().height = Integer.valueOf((int) Math.round(iVPayPalCall.getLayoutParams().height * (height / Method.heightEmulator)));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.LLPayPalCall && !pagado) {
            onClickLLPayPalCall();
        } else if (v.getId() == R.id.LLPayPalFAQ && !pagado) {

        } else if (v.getId() == R.id.TVTyCs && !pagado) {
            onClickTVTyCs();

        }
    }

    private void onClickTVTyCs() {
        Intent terminos = new Intent(this, WebActivity.class);
        terminos.putExtra("web", "https://www.workpod.app/tyc/");
        startActivity(terminos);
    }

    private void onClickLLPayPalCall() {
        Fragment_Dialog_Call fragmentDialogCall = new Fragment_Dialog_Call();
        fragmentDialogCall.show(getSupportFragmentManager(), "DialogToCall");
    }
}