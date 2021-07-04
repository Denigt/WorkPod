package com.example.workpod;

import com.example.workpod.basic.*;
import com.example.workpod.data.Usuario;
import com.example.workpod.scale.Scale_TextView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {
    // VARIABLES DE SIGNIN
    int pantalla;
    String nombre;
    String apellido;
    String dni;
    String email;
    String contrasena;

    private final String[] TIPOS_DOCUMENTO = {"NIF", "NIE"};

    // CONTROLES DEL XML
    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtDNI;
    private EditText txtEmail;
    private EditText txtContrasena;
    private EditText txtRContrasena;

    private TextView tVActSigninTitulo;
    private TextView tVActSigninPregNombre;
    private TextView tVActSigninNombre;
    private TextView tVActSigninApellidos;
    private TextView tVActSigninPregNIFNIE;
    private TextView tVActSignin2Titulo;
    private TextView tVActSignin2PregEmail;
    private TextView tVActSignin2Email;
    private TextView tVActSignin2PregContrasena;
    private TextView tVActSignin2Contrasena;
    private TextView tVActSignin2InstruccionesContrasena;
    private TextView tVActSignin2RepetirContrasena;

    private ImageButton btnSiguiente;
    private ImageButton btnVolver;
    private CheckBox btnShowContrasena;
    private Spinner spnDNI;

    private String correo;
    private String password;
    private Session sesion;

    //COLECCIONES
    List<Scale_TextView> lstTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pantalla = 0;
        initActivity();
    }

    @Override
    protected void onPause() {
        saveActivity();
        super.onPause();
    }

    @Override
    protected void onResume() {
        initActivity();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (pantalla == 0) {
            super.onBackPressed();
        } else {
            saveActivity();
            pantalla = 0;
            initActivity();
        }
    }

    // LISTENERS
    @Override
    public void onClick(View v) {
        btnSiguienteOnClick(v);
        btnVolverOnClick(v);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        editTextOnFocus(v, hasFocus);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        btnShowContrasenaOnChecked(buttonView, isChecked);
    }

    // OBTENER CHECK
    private void btnShowContrasenaOnChecked(View v, boolean isChecked) {
        if (v.getId() == btnShowContrasena.getId() && isChecked) {
            // MOSTRAR CONTRASENA
            txtContrasena.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            txtRContrasena.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else if (v.getId() == btnShowContrasena.getId()) {
            // OCULTAR CONTRASENA
            txtContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            txtRContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    // OBTENCION DEL FOCO

    /**
     * Retorna un EditText a sus colores por defecto si tiene el foco
     *
     * @param v        Control al que cambiar el color
     * @param hasFocus Indica si el control tiene el foco
     */
    private void editTextOnFocus(View v, boolean hasFocus) {
        if (v.getClass().getName().toLowerCase().contains("edittext") && hasFocus) {
            v.setBackgroundTintList(getResources().getColorStateList(R.color.editext));
        }
    }

    // CLICK DE LOS BOTONES

    /**
     * Si es la primera pantalla para registrarse abre la segunda
     * Si es la segunda pantalla, combrueba que los datos sean correctos, crea el usuario y abre
     * la aplicacion
     *
     * @param v Vista clicada
     */
    private void btnSiguienteOnClick(View v) {
        if (v.getId() == btnSiguiente.getId()) {
            // VALIDAR DATOS DE LA PRIMERA PANTALLA
            if (pantalla == 0) {
                boolean error = false;

                saveActivity();
                if (nombre.equals(null) || nombre.equals("")) {
                    Method.showError(this, "Introduzca su nombre");
                    txtNombre.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                    error = true;
                }
                if (apellido.equals(null) || apellido.equals("")) {
                    Method.showError(this, "Introduzca su apellido");
                    txtApellido.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                    error = true;
                }
                if (dni.equals(null) || dni.equals("")) {
                    Method.showError(this, "Introduzca su documento de identificación");
                    txtDNI.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                    error = true;
                }
                if (!error) {
                    pantalla = 1;
                    initActivity();
                }
            }
            // VALIDAR DATOS DE LA SEGUNDA PANTALLA
            else {
                boolean error = false;
                saveActivity();

                if (email.equals(null) || email.equals("")) {
                    Method.showError(this, "Introduzca su email");
                    txtEmail.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                    error = true;
                } else if (!Method.checkEmail(email)) {
                    Method.showError(this, "Introduzca una dirección de email válida");
                    txtEmail.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                    error = true;
                }
                if (contrasena.equals(null) || contrasena.equals("")) {
                    Method.showError(this, "Introduzca una contraseña");
                    txtContrasena.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                    error = true;
                } else if (!Method.checkPassword(contrasena)) {
                    Method.showError(this, "La contraseña ha de tener 10 caracteres y un símbolo");
                    txtContrasena.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                    error = true;
                } else if (!contrasena.equals(txtRContrasena.getText().toString())) {
                    Method.showError(this, "Las contraseñas no coinciden");
                    txtRContrasena.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                    error = true;
                }
                if (!error) {
                    // COMPROBAR QUE NO EXISTA UN USUARIO CON EL MISMO EMAIL
                    Database<Usuario> consulta = new Database<>(Database.SELECTID, new Usuario(email, contrasena));
                    consulta.postRunOnUI(this, () -> {
                        if (consulta.getError().code == -1) {
                            // SI NO EXISTE EL USUARIO INSERTAR EN LA BASE DE DATOS
                            Database<Usuario> insert = new Database<>(Database.INSERT, new Usuario(email, nombre, apellido, dni, contrasena, 0, null, null, null));
                            insert.postRun(() -> {
                                Database<Usuario> select = new Database<>(Database.SELECTID, new Usuario(email, contrasena));
                                select.postRun(() -> {
                                    InfoApp.USER = new Usuario();
                                    if (select.getError().code > -1) {
                                        InfoApp.USER.set(select.getDato());

                                        // ENVIAR CORREO DE VERIFICACION
                                        Database<Usuario> verificacion = new Database<>(Database.VERIFICACION, InfoApp.USER);
                                        verificacion.start();
                                    }else InfoApp.USER = null;
                                });
                                select.start();
                            });
                            insert.postRunOnUI(this, () -> {
                                if (insert.getError().code > -1) {
                                    // SI NO HA HABIDO NINGUN PROBLEMA PASAR A LA SIGUIENTE ACTIVIDAD HABIENDO INICIADO SESION
                                    Intent activity = new Intent(getApplicationContext(), WorkpodActivity.class);
                                    startActivity(activity);
                                    finish();
                                } else
                                    Toast.makeText(this, "No se ha podido crear el usuario", Toast.LENGTH_LONG).show();
                            });
                            insert.start();

                        } else
                            Toast.makeText(this, "Ya existe un usuario con el mismo Email", Toast.LENGTH_LONG).show();
                    });
                    consulta.start();
                }
            }
        }
    }

    /**
     * Cierra la ventana de Login
     *
     * @param v Vista clicada
     */
    private void btnVolverOnClick(View v) {
        if (v.getId() == btnVolver.getId()) {
            if (pantalla == 0) {
                finish();
            } else {
                saveActivity();
                pantalla = 0;
                initActivity();
            }
        }
    }

    // OTROS METODOS

    /**
     * Inicializa la actividad:
     * Elige el layout de la actividad
     * Establece los valores de los controles de la actividad
     * Anade los Listeners a los controles generados
     */
    private void initActivity() {
        // ESTABLECER LAYOUT DE LA ACTIVIDAD
        if (pantalla == 0)
            setContentView(R.layout.activity_signin);
        else
            setContentView(R.layout.activity_signin2);

        // BUSCAR LOS CONTROLES DEL XML
        if (pantalla == 0) {
            txtNombre = findViewById(R.id.txtNombre);
            txtNombre.setText(nombre);
            txtApellido = findViewById(R.id.txtApellido);
            txtApellido.setText(apellido);
            txtDNI = findViewById(R.id.txtDNI);
            txtDNI.setText(dni);
            spnDNI = findViewById(R.id.spnDNI);
            tVActSigninApellidos = findViewById(R.id.tVActSigninApellidos);
            tVActSigninNombre = findViewById(R.id.tVActSigninNombre);
            tVActSigninPregNIFNIE = findViewById(R.id.tVActSigninPregNIFNIE);
            tVActSigninPregNombre = findViewById(R.id.tVActSigninPregNombre);
            tVActSigninTitulo = findViewById(R.id.tVActSigninTitulo);

            // LLENAR EL SPINNER DEL TIPO DE DOCUMENTO
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner_basic, TIPOS_DOCUMENTO);
            spnDNI.setAdapter(adapter);

            //ESCALAMOS ELEMENTOS
            escalarElementosSignin1();

        } else {
            txtEmail = findViewById(R.id.txtEmail);
            txtEmail.setText(email);
            txtContrasena = findViewById(R.id.txtContrasena);
            txtContrasena.setText(contrasena);
            txtRContrasena = findViewById(R.id.txtRContrasena);
            tVActSignin2Contrasena = findViewById(R.id.tVActSignin2Contrasena);
            tVActSignin2Email = findViewById(R.id.tVActSignin2Email);
            tVActSignin2PregEmail = findViewById(R.id.tVActSignin2PregEmail);
            tVActSignin2InstruccionesContrasena = findViewById(R.id.tVActSignin2InstruccionesContrasena);
            tVActSignin2PregContrasena = findViewById(R.id.tVActSignin2PregContrasena);
            tVActSignin2RepetirContrasena = findViewById(R.id.tVActSignin2RepetirContrasena);
            tVActSignin2Titulo = findViewById(R.id.tVActSignin2Titulo);
            btnShowContrasena = findViewById(R.id.btnShowContrasena);

            //ESCALAMOS ELEMENTOS
            escalarElementosSignin2();
        }
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnVolver = findViewById(R.id.btnVolver);

        // ESTABLECER EVENTOS PARA LOS CONTROLES
        if (pantalla == 0) {
            txtNombre.setOnFocusChangeListener(this);
            txtApellido.setOnFocusChangeListener(this);
            txtDNI.setOnFocusChangeListener(this);
        } else {
            txtEmail.setOnFocusChangeListener(this);
            txtContrasena.setOnFocusChangeListener(this);
            txtRContrasena.setOnFocusChangeListener(this);
            btnShowContrasena.setOnCheckedChangeListener(this);
        }
        btnSiguiente.setOnClickListener(this);
        btnVolver.setOnClickListener(this);
    }

    /**
     * Guarda la actividad:
     * Almacena en las variables de la actividad los valores de todos los campos de la misma
     */
    private void saveActivity() {
        if (pantalla == 0) {
            nombre = txtNombre.getText().toString().trim();
            apellido = txtApellido.getText().toString().trim();
            dni = txtDNI.getText().toString().trim();
        } else {
            email = txtEmail.getText().toString().trim().toLowerCase();
            contrasena = txtContrasena.getText().toString();
        }
    }

    /**
     * Este método sirve de ante sala para el método de la clase Methods donde escalamos los elementos del xml de Signin.
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
    private void escalarElementosSignin1() {
        //INICIALIZAMOS COLECCIONES
        this.lstTv = new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstTv.add(new Scale_TextView(tVActSigninApellidos, "Match_Parent", "bold", 18,18, 18));
        lstTv.add(new Scale_TextView(tVActSigninNombre, "Match_Parent", "bold", 18,18, 18));
        lstTv.add(new Scale_TextView(tVActSigninPregNIFNIE, "Match_Parent", "bold", 24,24, 24));
        lstTv.add(new Scale_TextView(tVActSigninPregNombre, "Match_Parent", "bold", 24,24, 24));
        lstTv.add(new Scale_TextView(tVActSigninTitulo, "Match_Parent", "bold", 34,34, 34));

        Method.scaleTv(metrics, lstTv);
    }

    /**
     * Este método sirve de ante sala para el método de la clase Methods donde escalamos los elementos del xml de Signin2.
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
    private void escalarElementosSignin2() {
        //INICIALIZAMOS COLECCIONES
        this.lstTv = new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstTv.add(new Scale_TextView(tVActSignin2Contrasena, "Match_Parent", "bold", 18, 18,18));
        lstTv.add(new Scale_TextView(tVActSignin2Email, "Match_Parent", "bold", 18, 18,18));
        lstTv.add(new Scale_TextView(tVActSignin2InstruccionesContrasena, "Match_Parent", "bold", 16, 16,16));
        lstTv.add(new Scale_TextView(tVActSignin2PregContrasena, "Match_Parent", "bold", 24, 24,24));
        lstTv.add(new Scale_TextView(tVActSignin2PregEmail, "Match_Parent", "bold", 24,24, 24));
        lstTv.add(new Scale_TextView(tVActSignin2RepetirContrasena, "Match_Parent", "bold", 18, 18,18));
        lstTv.add(new Scale_TextView(tVActSignin2Titulo, "Match_Parent", "bold", 34,34, 34));

        Method.scaleTv(metrics, lstTv);
    }

    /**
     * Método para enviar un token de verificación al usuario que se acaba de registrar
     * Si el correo emisor es gmail o hotmail, las instrucciones a utilizar varían ligeramente.
     */
    private void correoVerificacion(){
        //CORREO EMPRESA
        correo = "workpodtfg@gmail.com";
        password = "workpod2021";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //PROPIEDADES DEL SERVIDOR DE CORREO ELECTRONICO
        Properties properties = new Properties();
        //INDICAMOS EL NOMBRE DEL SERVIDOR DEL CORREO ELECTRÓNICO QUE VAMOS A USAR COMO EMISOR
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        //SOCKET PARA RECIBIR RESPUESTA DEL SERVIDOR
        properties.put("mail.smtp.socketFactory.port", "465");//EL PUERTO DEL SERVIDOR DE GMAIL ES 465 (NO ES UNO AL AZAR)
        //ESPECIFICAMOS QUE EL PROTOCOLO DE SEGURIDAD PARA EL ENVÍO DE INFORMACIÓN SERÁ SSL
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //PERMITE AL SERVIDOR LA AUTENTIFICACIÓN DEL CLIENTE
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        try {
            //NECESITAMOS AUTENTIFICACIÓN PARA LA CONEXIÓN DE RED
            sesion = Session.getDefaultInstance(properties, new Authenticator() {
                /**
                 * Autentificará la contraseña del correo emisor
                 * @return
                 */
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correo, password);
                }
            });
            //SI EL OBJETO SESIÓN APUNTA A NULO ES QUE LA AUTENTIFICACIÓN HA FALLADO
            if (sesion != null) {
                //CREAMOS UN OBJETO DE LA CLASE MESSAGE, LA CUAL NOS PERMITIRÁ MANDAR EL EMAIL
                Message message = new MimeMessage(sesion);
                //CORREO EMISOR
                message.setFrom(new InternetAddress(correo));
                //ASUNTO DEL CORREO
                message.setSubject("Token de verificación de Workpod");
                //CON TO ESPECIFICAMOS LOS DESTINATARIOS DEL MENSAJE
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                //CONTENIDO DEL MENSAJE Y ESPECIFICACIÓN DE LA CODIFICACIÓN UNICODE
                message.setContent("Tu token de verificación de Workpod es 1234AB#", "text/html; charset=utf-8");
                //ENVIAMOS CORREO UTILIZANDO EL MÉTODO SEND DE LA CLASE TRANSPORT
                Transport.send(message);

                //ECO
                Toast.makeText(this, "Se ha enviado un token de verificación al correo", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}