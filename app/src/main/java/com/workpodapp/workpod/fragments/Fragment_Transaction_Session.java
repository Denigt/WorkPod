package com.workpodapp.workpod.fragments;

//IMPRESCINDIBLE PARA QUE FUNCIONE EN APIS INFERIORES A LA 23

import androidx.annotation.NonNull;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.workpodapp.workpod.R;
import com.workpodapp.workpod.WorkpodActivity;
import com.workpodapp.workpod.basic.Database;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.basic.Shared;
import com.workpodapp.workpod.data.Sesion;
import com.workpodapp.workpod.data.Ubicacion;
import com.workpodapp.workpod.data.Workpod;
import com.google.android.gms.maps.model.LatLng;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

//PARA CREAR UN DIALOGRESULT DEBEMOS HEREDAR DE LA CLASE DIALOGFRAGMENT
public class Fragment_Transaction_Session extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    //CONSTANTES
    private static final String SIN_OFERTAS = "Sin ofertas";
    private static final int PARSEO_EURO_CENTIMOS = 100;

    //XML
    private LinearLayout lLFechaSesion;
    private LinearLayout lLFechaEntrada;
    private LinearLayout lLFechaSalida;

    private TextView tVFechaSesion;
    private TextView tVSessionPrice;
    private TextView tVDesgloseSession;
    private TextView tVTotalSession;
    private TextView tVPrecioTotalSession;
    private TextView tVDescuentoSession;
    private TextView tVImporteDescuentoSession;
    private TextView tVTimeSession;
    private TextView tVFechaEntrada;
    private TextView tVFechaEntradaDato;
    private TextView tVFechaSalida;
    private TextView tVFechaSalidaDato;
    private TextView tVDireccionSesion;
    private TextView tVMapa;

    private ImageView iVTimeSession;
    private ImageView iVPreviusSession;
    private ImageView iVNextSession;

    //VARIABLES CON LAS QUE COGEREMOS LOS DATOS DEL CONSTRUCTOR
    private String ubication;
    private ZonedDateTime fechaEntrada;
    private ZonedDateTime fechaSalida;
    private Double ofertas;
    private Double precio;
    private Double precioFinal;
    Workpod workpod;

    //VARIABLES PARA EL CÁLCULO DEL TIEMPO DE SESIÓN DEL USUARIO EN WORKPOD
    private int hour;
    private int min;
    private int seg;
    private Date fecha1;
    private Date fecha2;

    //VARIABLES PARA CREAR EL ID DE LA SESIÓN
    private int nAleatorio;
    private String letra;
    int letraAleatoria1;
    int letraAleatoria2;
    private String signo;
    int signoAleatorio1;
    int signoAleatorio2;
    private String[] alfabeto;
    private String[] matrizSignos;
    private List<Ubicacion> lstUbicacion = new ArrayList<>();
    private List<Workpod> lstWorkpods = new ArrayList<>();
    private boolean refrescardB = false;
    public static boolean desactivarBtnReservar = false;

    private boolean horas = false;
    private List<Sesion> lstSesiones;
    private Sesion sesionActual;

    // VARIABLES NECESARIAS PARA LA GEOLOCALIZACION
    private boolean havePermission = false;
    /**
     * Administrador de la geolocalizacion, maneja tambien el dibujado de la posicion del usuario
     */
    private LocationManager locationService;
    /**
     * Posicion del usuario
     * Cuidado al leerlo, puede ser null
     */
    private Shared<LatLng> posicion = new Shared<>();
    private Shared<LatLng> posicionWorkpod = new Shared<>();
    private boolean killHilos = false;

    // VARIABLES PARA LA GESTION DEL MAPA Y LA LOCALIZACION
    /**
     * Rango en el que el boton de centrar tiene el efecto de acercar el mapa
     */
    private final double errorMedida = 0.01;
    /**
     * Zoom por defecto en el mapa
     */
    private final int defaultZoom = 12;
    /**
     * El mapa
     */
    private GoogleMap mMap;

    // TAMANO DE LOS ICONOS Y MARCADORES DE MAPA
    private int TAM_ICON = 100;
    private int TAM_MARKERS = 110;

    private boolean esperaCargarMapa;

    //CONSTRUCTOR
    public Fragment_Transaction_Session(Sesion sesion, List<Sesion> lstSesiones) {
        this.ubication = sesion.getDireccion().toLongString();
        this.fechaEntrada = sesion.getEntrada();
        this.fechaSalida = sesion.getSalida();
        this.ofertas = sesion.getDescuento();
        this.precio = sesion.getPrecio();
        this.workpod = sesion.getWorkpod();
        this.lstSesiones = lstSesiones;
        this.sesionActual = sesion;
        this.esperaCargarMapa=false;

        //INICIALIZAMOS EL RESTO DE ELEMENTOS
        precioFinal = 0.0;
        hour = 0;
        min = 0;
        seg = 0;
        nAleatorio = 0;
        letra = "a";
        letraAleatoria1 = 0;
        letraAleatoria2 = 0;
        signo = "#";
        signoAleatorio1 = 0;
        signoAleatorio2 = 0;
        alfabeto = new String[52];
        matrizSignos = new String[9];
    }


    //CONSTRUCTOR POR DEFECTO
    public Fragment_Transaction_Session() {
    }

    //SOBREESCRITURAS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_session, container, false);
        //PARA QUE FUNCIONEN LOS ELEMENTOS DEL XML (BTN, IV...) EN UN DIALOG, LAS INSTANCIAS HAN DE ESTAR
        // EN EL MÉTODO onCreateDialog

        lLFechaEntrada = view.findViewById(R.id.LLFechaEntrada);
        lLFechaSalida = view.findViewById(R.id.LLFechaSalida);
        lLFechaSesion = view.findViewById(R.id.LLFechaSesion);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapSession);
        mapFragment.getMapAsync(this);

        tVFechaSesion = view.findViewById(R.id.TVFechaSession);
        tVSessionPrice = view.findViewById(R.id.TVSessionPrice);
        tVDesgloseSession = view.findViewById(R.id.TVDesgloseSession);
        tVTotalSession = view.findViewById(R.id.TVTotalSession);
        tVPrecioTotalSession = view.findViewById(R.id.TVPrecioTotalSession);
        tVDescuentoSession = view.findViewById(R.id.TVDescuentoSession);
        tVImporteDescuentoSession = view.findViewById(R.id.TVImporteDescuentoSession);
        tVTimeSession = view.findViewById(R.id.TVTimeSession);
        tVFechaEntrada = view.findViewById(R.id.TVFechaEntrada);
        tVFechaEntradaDato = view.findViewById(R.id.TVFechaEntradaDato);
        tVFechaSalida = view.findViewById(R.id.TVFechaSalida);
        tVFechaSalidaDato = view.findViewById(R.id.TVFechaSalidaDato);
        tVDireccionSesion = view.findViewById(R.id.TVDireccionSesion);
        tVMapa = view.findViewById(R.id.TVMapa);

        iVPreviusSession = view.findViewById(R.id.IVPreviusSession);
        iVNextSession = view.findViewById(R.id.IVNextSession);
        iVTimeSession = view.findViewById(R.id.IVTimeSession);

        InfoFragment.actual = InfoFragment.TRANSACTION_SESSION;

        // Iniciar el hilo para solicitar la ubicacion
        locationService = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            locationService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, new UbicacionListener());
        } catch (SecurityException e) {
        }
        //AÑADIMOS AL XML LOS DATOS DE LA SESIÓN DE WORPOD DEL USUARIO
        tVDireccionSesion.setText(ubication);
        tVFechaSesion.setText(fechaEntrada.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        tVFechaEntradaDato.setText(fechaEntrada.format(DateTimeFormatter.ofPattern("HH:mm")));
        tVFechaSalidaDato.setText(fechaSalida.format(DateTimeFormatter.ofPattern("HH:mm")));
        //MÉTODO PARA CALCULAR LAS 2 FECHAS Y AGREGARLAS AL TV DEL XML
        calcularTiempoSesion(fecha1, fecha2, hour, min, seg);

        //MÉTODO PARA CALCULAR EL PRECIO DE LA SESIÓN DE WORKPOD
        calcularPrecio(precioFinal, precio, hour, min, ofertas);

        //MÉTODO PARA CREAR EL ID DE LA SESIÓN DE WORKPOD
        crearIDSesion(nAleatorio, letra, signo, letraAleatoria1, letraAleatoria2, signoAleatorio1, signoAleatorio2);

        //ESTABLECEMOS EVENTOS PARA LOS CONTROLES
        iVTimeSession.setOnClickListener(this);
        iVPreviusSession.setOnClickListener(this);
        iVNextSession.setOnClickListener(this);
        lLFechaSesion.setOnClickListener(this);
        tVMapa.setOnClickListener(this);
        //ESCALAMOS ELEMENTOS
        escalarElementos();

        return view;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.TVMapa && esperaCargarMapa && !InfoFragment.DIALOGO_DESPLEGADO) {
            onClickLLMap();
        } else if (v.getId() == R.id.LLFechaSesion || v.getId() == R.id.IVTimeSession) {
            onClickIVTimeSession();
        } else if (v.getId() == R.id.IVNextSession) {
            onClickNextSession();
        } else if (v.getId() == R.id.IVPreviusSession) {
            onClickPreviusSession();
        }
    }

    private void onClickPreviusSession() {
        for (Sesion sesiones : lstSesiones) {
            if (sesionActual.equals(sesiones) && !sesiones.equals(lstSesiones.get(lstSesiones.size() - 1))) {
                Fragment_Transaction_Session fragment_transaction_session = new Fragment_Transaction_Session(lstSesiones.get(lstSesiones.indexOf(sesiones) + 1), lstSesiones);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragment_transaction_session).commit();
            }
        }
    }

    private void onClickNextSession() {
        for (Sesion sesiones : lstSesiones) {
            if (sesionActual.equals(sesiones) && !sesiones.equals(lstSesiones.get(0))) {
                Fragment_Transaction_Session fragment_transaction_session = new Fragment_Transaction_Session(lstSesiones.get(lstSesiones.indexOf(sesiones) - 1), lstSesiones);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragment_transaction_session).commit();
            }
        }
    }

    private void onClickIVTimeSession() {
        if (horas) {
            horas = false;
            lLFechaSalida.setVisibility(View.GONE);
            lLFechaEntrada.setVisibility(View.GONE);
        } else {
            horas = true;
            lLFechaSalida.setVisibility(View.VISIBLE);
            lLFechaEntrada.setVisibility(View.VISIBLE);
        }

    }


    //MÉTODOS

    /**
     * Si el usuario accede al workpod en el que está haciendo la sesión le retornará a su sesión
     * Si el usuario accede a un workpod con el que no está realizando una sesión en este momento, le llevará al Fragment_Dialog_Workpod
     * donde podrá ver el estado del workpod e interactuar con él.
     */
    private void onClickLLMap() {
        try {
            //LE INDICAMOS QUE DESACTIVE EL BTN DE RESERVAR
            desactivarBtnReservar = true;
            //REFRESCAMOS LOS WORKPODS
            Database<Ubicacion> dbUbicacion = new Database<>(Database.SELECTALL, new Ubicacion());
            dbUbicacion.postRun(() -> {
                if (!dbUbicacion.getError().get()) {
                    lstUbicacion.removeAll(lstUbicacion);
                    lstUbicacion.addAll(dbUbicacion.getLstSelect());
                    //CONTROLAMOS QUE NO SE ABRA EL FRAGMENT_DIALOG_WORKPOD HASTA QUE EL HILO POSTRUN HAYA MUERTO
                    refrescardB = true;
                }

            });
            dbUbicacion.start();
            //CONTROLAMOS QUE NO SE ABRA EL FRAGMENT_DIALOG_WORKPOD HASTA QUE EL HILO POSTRUN HAYA MUERTO
            while (!refrescardB) {
                Thread.sleep(10);
            }

            refrescardB = false;

            //SI EL USUARIO ACCEDE AL WORKPOD EN EL QUE ESTÁ REALIZANDO LA SESIÓN
            if (InfoApp.USER.getReserva() != null && InfoApp.USER.getReserva().getEstado().equalsIgnoreCase("en uso") && InfoApp.SESION != null
                    && InfoApp.USER.getReserva().getWorkpod() == workpod.getId()) {
                Fragment_sesion fragmentSesion = new Fragment_sesion(InfoApp.SESION);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment, fragmentSesion).commit();
                WorkpodActivity.btnNV.getMenu().findItem(R.id.inv_location).setChecked(true);
            }// SI EL USUARIO ACCEDE A UN WORKPOD EN EL QUE NO ESTÁ REALIZANDO LA SESIÓN
            else {
                //CONTROLAMOS QUE EL FRAGMENT_DIALOG_WORKPOD SOLO SE ABRA UNA VEZ
                if(!InfoFragment.DIALOGO_DESPLEGADO){
                    InfoFragment.DIALOGO_DESPLEGADO=true;
                    Fragment_Dialog_Workpod fragmentDialogWorkpod = new Fragment_Dialog_Workpod(workpod, workpod.getUbicacion(), posicion);
                    fragmentDialogWorkpod.show(getActivity().getSupportFragmentManager(), "UN SOLO WORPOD EN ESA UBICACIÓN");
                }

            }
        } catch (NullPointerException e) {
           e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para calcular la diferencia entre 2 fechas.
     * La clase ZoneDateTime no posee el método getTime() el cual permite calcular los milisegundos entre dos fechas,
     * es el método más fácil para calcular el tiempo entre 2 fechas. Para ello, parseamos nuestras fechas a Date y
     * aplicalamos los calculos respectivos para transformar las diferencias de milisegundos entre 2 fechas en horas, minutos y segundos
     *
     * @param fecha1 fecha de entrada al workpod
     * @param fecha2 fecha de salida del workpod
     * @param hour   horas que ha durado la sesión de workpod
     * @param min    minutos que ha durado la sesión de workpod
     * @param seg    segundos que ha durado la sesión de workpod
     */
    private void calcularTiempoSesion(Date fecha1, Date fecha2, int hour, int min, int seg) {
        fecha1 = Date.from(fechaEntrada.toInstant());
        fecha2 = Date.from(fechaSalida.toInstant());
        long dif = fecha2.getTime() - fecha1.getTime();
        seg = (int) (dif / 1000) % 60;
        min = (int) ((dif / (1000 * 60)) % 60);
        hour = (int) ((dif / (1000 * 60 * 60)) % 24);
        //LE PASAMOS LOS VALORES CALCULADOS A LAS VARIABLES QUE USAMOS FUERA DEL METODO
        this.hour = hour;
        this.min = min;
        this.seg = seg;
        //AGREGAMOS EL TIEMPO DE SESIÓN DE TRABAJO EN UN WORKPOD DEL USUARIO
        //ESTÉTICA DEL TIEMPO DE SESIÓN
        if (hour == 0 && min != 0) {
            tVTimeSession.setText(min + "min " + seg + "seg");
        } else if (hour == 0 && min == 0) {
            tVTimeSession.setText(seg + "seg");
        } else {
            tVTimeSession.setText(hour + "h:" + min + "min");
        }

    }

    /**
     * Método para calcular el precio de una sesión de workpod. El precio base son 0,20€ el minuto y además
     * hay que aplicarles los descuentos cangeados a dicha sesión del workpod
     *
     * @param precio      es el precio resultado de calcular los min que ha estado el usuario usando el workpod x 0,2
     * @param hour        las horas que ha estado el usuario usando el worpod
     * @param min         los minutos que ha estado el usuario usando el workpod
     * @param ofertas     el porcentaje de descuento de la oferta
     * @param precioFinal es el precio de los minutos que ha estado el usuario usando el workpod aplicándole el descuento
     */
    private void calcularPrecio(Double precioFinal, Double precio, int hour, int min, double ofertas) {
        //SI HAY OFERTAS
        if (ofertas > 0) {
            tVImporteDescuentoSession.setText(String.format("%.2f", (ofertas)) + "€");
            tVPrecioTotalSession.setText(String.format("%.2f", (precio)) + "€");
            tVSessionPrice.setText(String.format("%.2f", (precio - ofertas)) + "€");
        } else {
            //SI NO HAY OFERTAS
            tVPrecioTotalSession.setText(String.format("%.2f", (precio)) + "€");
            tVImporteDescuentoSession.setText(SIN_OFERTAS);
            tVSessionPrice.setText(String.format("%.2f", (precio)) + "€");
        }

        if (precio < 1) {
            tVPrecioTotalSession.setText(String.valueOf(Math.round(precio * PARSEO_EURO_CENTIMOS)) + " cent");
            tVSessionPrice.setText(String.valueOf(Math.round(precio * PARSEO_EURO_CENTIMOS)) + " cent");
        }
    }

    /**
     * Método que sirve para crear una clave compuesta de 6 dígitos cuyos 2 primeros dígitos es un nº aleatorio del 10 al 99, los otros 4 están compuestos
     * por 2 letras y 2 signos aleatorios
     *
     * @param nAleatorio      número aleatorio
     * @param letra           contiene todo el alfabeto con mayusculas y minusculas separadas por comas
     * @param signo           contiene todos los signos que vamos a usar separados por comas
     * @param letraAleatoria1 nº aleatorio que será la posición de la matriz donde esxtraigamos la letra
     * @param letraAleatoria2 nº aleatorio que será la posición de la matriz donde esxtraigamos la letra
     * @param signoAleatorio1 nº aleatorio que será la posición de la matriz donde esxtraigamos el signo
     * @param signoAleatorio2 nº aleatorio que será la posición de la matriz donde esxtraigamos el signo
     */
    private void crearIDSesion(int nAleatorio, String letra, String signo, int letraAleatoria1, int letraAleatoria2, int signoAleatorio1, int signoAleatorio2) {

        letra = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
        signo = "@,#,$,&,+,*,^,?,¿";

        //AGREGAMOS A LA MATRIZ UNA LETRA EN CADA POSICIÓN
        for (int i = 0; i <= alfabeto.length - 1; i++) {
            String[] datos = letra.split(",");
            alfabeto[i] = datos[i];
        }

        //AGREGAMOS A LA MATRIZ UN SIGNO EN CADA POSICIÓN
        for (int i = 0; i <= matrizSignos.length - 1; i++) {
            String[] datos = signo.split(",");
            matrizSignos[i] = datos[i];
        }

        //GENERAMOS 2 Nº ALEATORIO ENTRE 0 Y 26 (TAMAÑO DE LA MATRIZ)
        letraAleatoria1 = new Random().nextInt(52);
        letraAleatoria2 = new Random().nextInt(52);

        //GENERAMOS 2 Nº ALEATORIO ENTRE 0 Y 8 (TAMAÑO DE LA MATRIZ)
        signoAleatorio1 = new Random().nextInt(9);
        signoAleatorio2 = new Random().nextInt(9);

        //GENERAMOS 2 Nº ALEATORIO ENTRE 10 Y 99
        nAleatorio = new Random().nextInt(90) + 10;

        //GUARDAMOS EL ID EN UNA VARIABLE
        String idSesionWorkpod;
        idSesionWorkpod = String.valueOf(nAleatorio) + matrizSignos[signoAleatorio1] + alfabeto[letraAleatoria1] +
                matrizSignos[signoAleatorio2] + alfabeto[letraAleatoria2];
        //MOSTRAMOS EL RESULTADO PARA VER SI SE HA FORMADO UN ID ALEATORIO CORRECTO
        //Toast.makeText(getActivity(),idSesionWorkpod,Toast.LENGTH_LONG).show();
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
        //INICIALIZAMOS COLECCIONES
        List<View> lstView = new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstView.add(tVFechaSesion);
        lstView.add(tVSessionPrice);
        lstView.add(tVDesgloseSession);
        lstView.add(tVTotalSession);
        lstView.add(tVPrecioTotalSession);
        lstView.add(tVDescuentoSession);
        lstView.add(tVImporteDescuentoSession);
        lstView.add(tVTimeSession);
        lstView.add(tVFechaEntrada);
        lstView.add(tVFechaEntradaDato);
        lstView.add(tVFechaSalida);
        lstView.add(tVFechaSalidaDato);
        lstView.add(tVDireccionSesion);

        lstView.add(iVNextSession);
        lstView.add(iVPreviusSession);
        lstView.add(iVTimeSession);

        Method.scaleViews(metrics, lstView);
        escaladoParticular(metrics);

    }

    private void escaladoParticular(DisplayMetrics metrics) {
        float height = metrics.heightPixels / metrics.density;
        iVTimeSession.getLayoutParams().height = Integer.valueOf((int) Math.round(iVTimeSession.getLayoutParams().height * (height / Method.heightEmulator)));
        iVNextSession.getLayoutParams().height = Integer.valueOf((int) Math.round(iVNextSession.getLayoutParams().height * (height / Method.heightEmulator)));
        iVPreviusSession.getLayoutParams().height = Integer.valueOf((int) Math.round(iVPreviusSession.getLayoutParams().height * (height / Method.heightEmulator)));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        locationService = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //REFRESCAMOS LOS WORKPODS
        Database<Ubicacion> dbUbicacion = new Database<>(Database.SELECTALL, new Ubicacion());
        dbUbicacion.postRun(() -> {
            if (!dbUbicacion.getError().get()) {
                lstUbicacion.removeAll(lstUbicacion);
                lstUbicacion.addAll(dbUbicacion.getLstSelect());
                //CONTROLAMOS QUE NO SE ABRA EL FRAGMENT_DIALOG_WORKPOD HASTA QUE EL HILO POSTRUN HAYA MUERTO
                refrescardB = true;
            }

        });
        dbUbicacion.postRunOnUI(getActivity(), () -> {
            try {
                locationService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, new UbicacionListener());
                if (lstUbicacion.size() > 0) {
                    /*for (Ubicacion ubicacion : lstUbicacion) {
                        if(sesionActual.getWorkpod().getUbicacion().equals(ubicacion.getId())){
                            for (Workpod workpod : ubicacion.getWorkpods()) {
                                if (sesionActual.getWorkpod().equals(workpod)) {
                                    posicionWorkpod.resource = new LatLng(ubicacion.getLat(), ubicacion.getLon());
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicionWorkpod.resource, defaultZoom));
                                }
                            }
                        }


                    }*/
                    for (int i = 0; i < lstUbicacion.size(); i++) {
                        //SI EN UNA UBICACIÓN HAY MÁS DE UNA CABINA
                        if (lstUbicacion.get(i).getWorkpods().size() > 1) {
                            lstWorkpods = lstUbicacion.get(i).getWorkpods();
                            for (int j = 0; j < lstWorkpods.size(); j++) {
                                if (workpod.getId() == lstWorkpods.get(j).getId()) {
                                    workpod = lstWorkpods.get(j);
                                    workpod.setReserva(lstWorkpods.get(j).getReserva());
                                    posicionWorkpod.resource = new LatLng(lstUbicacion.get(i).getLat(), lstUbicacion.get(i).getLon());
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicionWorkpod.resource, defaultZoom));
                                    esperaCargarMapa=true;
                                }
                            }
                        } else {
                            if (workpod.getId() == lstUbicacion.get(i).getWorkpods().get(0).getId()) {
                                //    workpod.getReserva().setEstado(lstUbicacion.get(i).getWorkpods().get(0).getReserva().getEstado());
                                workpod = lstUbicacion.get(i).getWorkpods().get(0);
                                posicionWorkpod.resource = new LatLng(lstUbicacion.get(i).getLat(), lstUbicacion.get(i).getLon());
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicionWorkpod.resource, defaultZoom));
                                esperaCargarMapa=true;
                            }
                        }
                    }
                }

            } catch (SecurityException e) {
            }
            dibujaWorkpods();
        });
        dbUbicacion.start();

    }

    /**
     * Dibuja el marcador del workpod en el mapa
     */
    private void dibujaWorkpods() {
        try {
            Marker markPosicion;

            LatLng posicion = new LatLng(sesionActual.getWorkpod().getUbicacion().getLat(), sesionActual.getWorkpod().getUbicacion().getLon());
            markPosicion = mMap.addMarker(new MarkerOptions().position(posicion).zIndex(1));
            markPosicion.setTag(sesionActual.getWorkpod().getUbicacion());
            for (Ubicacion ubicacion:lstUbicacion) {
                boolean allReservados = ubicacion.allResevados();
                markPosicion.setIcon(VectortoBitmap(requireContext(), allReservados ? R.drawable.markers_cluster_red : R.drawable.markers_cluster, TAM_MARKERS, TAM_MARKERS, String.valueOf("W"), 60, allReservados ? R.color.red : R.color.blue));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Transforma un vectorAsset en un bitmapDrawable y escribe texto centrado sobre el
     *
     * @param context   Contexto de la app
     * @param drawable  Imagen vectorial
     * @param width     Ancho de la imagen en px
     * @param height    Alto de la imagen en px
     * @param text      Texto a escribir
     * @param textTam   Tamano del texto
     * @param textColor Color del texto (id en los Resources de la app)
     * @return Bitmap a partir del vectorAsset
     */
    private static BitmapDescriptor VectortoBitmap(Context context, int drawable, int width, int height, String text, int textTam, int textColor) {
        // Declarar pincel para hacer el texto
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        Paint pen = new Paint();
        pen.setTextSize(textTam);
        pen.setTextAlign(Paint.Align.CENTER);
        pen.setTypeface(font);
        pen.setColor(context.getResources().getColor(textColor));

        // Obtener el Drawable
        Drawable vectorImg = ContextCompat.getDrawable(context, drawable);
        // Establecer tamanos
        vectorImg.setBounds(0, 0, width, height);
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Dibujar en el bitmat
        Canvas canvas = new Canvas(bm);
        vectorImg.draw(canvas);
        canvas.drawText(text, canvas.getWidth() / 2, (canvas.getHeight() / 2) + (textTam / 4), pen);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    // CLASE QUE FUNCIONARA COMO EL LISTENER DE LA UBICACION

    /**
     * Esta clase funciona como LocationListener, es decir se ejecuta en segundo plano solicitando y
     * actualizando la posicion del usuario
     */
    public class UbicacionListener implements LocationListener {

        public UbicacionListener() {
            // SE COMPRUEBA SI SE TIENEN PERMISOS Y EN CASO DE TENERLOS SE SOLICITA LA ULTIMA UBICACION CONOCIDA PARA INICIAR EL MAPA EN ELLA
            new Thread(() -> {
                Location aux = null;
                while ((!havePermission || aux == null) && !killHilos) {
                    try {
                        aux = locationService.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        havePermission = true;
                    } catch (SecurityException e) {
                        havePermission = false;
                    }
                }
                if (!killHilos) {
                    posicion.resource = new LatLng(aux.getLatitude(), aux.getLongitude());
                }
            }).start();
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        posicion.resource = null;
                        Toast.makeText(getContext(), "Habilite el GPS", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            posicion.resource = new LatLng(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    @Override
    /**
     * Ademas de funcionar como metodo onDestroy ordena a todos los hilos que se detengan y al ubicationListener que deje de solicitar la posicion
     */
    public void onDestroy() {
        killHilos = true;
        locationService.removeUpdates(new UbicacionListener());
        super.onDestroy();
    }
}