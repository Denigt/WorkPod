package com.workpodapp.workpod.fragments;

//IMPRESCINDIBLE PARA QUE FUNCIONE EN APIS INFERIORES A LA 23

import androidx.annotation.NonNull;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.workpodapp.workpod.R;
import com.workpodapp.workpod.WorkpodActivity;
import com.workpodapp.workpod.basic.Database;
import com.workpodapp.workpod.basic.InfoApp;
import com.workpodapp.workpod.basic.Method;
import com.workpodapp.workpod.basic.Shared;
import com.workpodapp.workpod.data.Sesion;
import com.workpodapp.workpod.data.Ubicacion;
import com.workpodapp.workpod.data.Workpod;
import com.workpodapp.workpod.scale.Scale_TextView;
import com.google.android.gms.maps.model.LatLng;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

//PARA CREAR UN DIALOGRESULT DEBEMOS HEREDAR DE LA CLASE DIALOGFRAGMENT
public class Fragment_Transaction_Session extends Fragment implements View.OnClickListener {

    //CONSTANTES
    private static final String SIN_OFERTAS = "Sin ofertas";

    //XML
    private TextView tVDialogUbication;
    private TextView tVDialogDateHour;
    private TextView tVDialogOffers;
    private TextView tVDialogSessionTime;
    private TextView tVDialogPrice;
    private ImageView iVDialogUbication;

    //VARIABLES CON LAS QUE COGEREMOS LOS DATOS DEL CONSTRUCTOR
    private String ubication;
    private ZonedDateTime fechaEntrada;
    private ZonedDateTime fechaSalida;
    private String ofertas;
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
    private List<Workpod>lstWorkpods=new ArrayList<>();
    public static Boolean sesionHistorico=false;
    private boolean refrescardB=false;
    public static boolean desactivarBtnReservar=false;

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
    private boolean killHilos = false;

    //COLECCIONES
    List<Scale_TextView> lstTv;

    //CONSTRUCTOR
    public Fragment_Transaction_Session(Sesion sesion) {
        this.ubication = sesion.getDireccion().toLongString();
        this.fechaEntrada = sesion.getEntrada();
        this.fechaSalida = sesion.getSalida();
        this.ofertas = String.valueOf(sesion.getDescuento());
        this.precio = sesion.getPrecio();
        this.workpod = sesion.getWorkpod();

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
        // iVSalirDialogSession = (ImageView) view.findViewById(R.id.IVSalirDialogSession);
        tVDialogUbication = (TextView) view.findViewById(R.id.TVDialogUbication);
        tVDialogDateHour = (TextView) view.findViewById(R.id.TVDialogDateHour);
        tVDialogOffers = (TextView) view.findViewById(R.id.TVDialogOffers);
        tVDialogPrice = (TextView) view.findViewById(R.id.TVDialogPrice);
        tVDialogSessionTime = (TextView) view.findViewById(R.id.TVDialogSessionTime);
        iVDialogUbication = (ImageView) view.findViewById(R.id.IVDialogUbication);



        // Iniciar el hilo para solicitar la ubicacion
        locationService = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            locationService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, new UbicacionListener());
        } catch (SecurityException e) {
        }
        //AÑADIMOS AL XML LOS DATOS DE LA SESIÓN DE WORPOD DEL USUARIO
        tVDialogUbication.setText(ubication);
        tVDialogDateHour.setText("Fecha: " + fechaEntrada.format(DateTimeFormatter.ofPattern("dd-MM-yy")) + "\nEntrada:" + fechaEntrada.format(DateTimeFormatter.ofPattern("HH:mm "))
                + "\nSalida: " + fechaSalida.format(DateTimeFormatter.ofPattern("HH:mm")));//HH 0 a 24 h con hh de 0 a 12h

        //MÉTODO PARA CALCULAR LAS 2 FECHAS Y AGREGARLAS AL TV DEL XML
        calcularTiempoSesion(fecha1, fecha2, hour, min, seg);

        //MÉTODO PARA CALCULAR EL PRECIO DE LA SESIÓN DE WORKPOD
        calcularPrecio(precioFinal, precio, hour, min, ofertas);

        //MÉTODO PARA CREAR EL ID DE LA SESIÓN DE WORKPOD
        crearIDSesion(nAleatorio, letra, signo, letraAleatoria1, letraAleatoria2, signoAleatorio1, signoAleatorio2);

        //ESTABLECEMOS EVENTOS PARA LOS CONTROLES
        iVDialogUbication.setOnClickListener(this);

        //ESCALAMOS ELEMENTOS
        escalarElementos();

        return view;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.IVDialogUbication) {
            onClickIVDialogUbication();
        }
    }


    //MÉTODOS

    /**
     * Si el usuario accede al workpod en el que está haciendo la sesión le retornará a su sesión
     * Si el usuario accede a un workpod con el que no está realizando una sesión en este momento, le llevará al Fragment_Dialog_Workpod
     * donde podrá ver el estado del workpod e interactuar con él.
     */
    private void onClickIVDialogUbication() {
        try {
            //LE INDICAMOS QUE DESACTIVE EL BTN DE RESERVAR
            desactivarBtnReservar=true;
            //REFRESCAMOS LOS WORKPODS
            Database<Ubicacion> dbUbicacion = new Database<>(Database.SELECTALL, new Ubicacion());
            dbUbicacion.postRun(() -> {
                if (!dbUbicacion.getError().get()){
                    lstUbicacion.removeAll(lstUbicacion);
                    lstUbicacion.addAll(dbUbicacion.getLstSelect());
                    //CONTROLAMOS QUE NO SE ABRA EL FRAGMENT_DIALOG_WORKPOD HASTA QUE EL HILO POSTRUN HAYA MUERTO
                    refrescardB=true;
                }

            });
            dbUbicacion.start();
            //CONTROLAMOS QUE NO SE ABRA EL FRAGMENT_DIALOG_WORKPOD HASTA QUE EL HILO POSTRUN HAYA MUERTO
            while(!refrescardB){
                Thread.sleep(10);
            }

            refrescardB=false;

            //SI EL USUARIO ACCEDE AL WORKPOD EN EL QUE ESTÁ REALIZANDO LA SESIÓN
            if (InfoApp.USER.getReserva().getEstado().equalsIgnoreCase("en uso") && InfoApp.SESION != null
                    && InfoApp.USER.getReserva().getWorkpod() == workpod.getId()) {
                WorkpodActivity.boolfolder=false;
                sesionHistorico=true;
                getActivity().onBackPressed();

            }// SI EL USUARIO ACCEDE A UN WORKPOD EN EL QUE NO ESTÁ REALIZANDO LA SESIÓN
            else {
                //RECORREMOS LOS WORKPODS QUE HAY EN CADA UBICACIÓN (NO SE PUEDE HACER CON FOREACH)
                for (int i=0;i<lstUbicacion.size();i++) {
                    //SI EN UNA UBICACIÓN HAY MÁS DE UNA CABINA
                    if (lstUbicacion.get(i).getWorkpods().size() > 1) {
                        lstWorkpods=lstUbicacion.get(i).getWorkpods();
                        for (int j = 0; j < lstWorkpods.size(); j++) {
                            if (workpod.getId() == lstWorkpods.get(j).getId()) {
                                workpod = lstWorkpods.get(j);
                                Fragment_Dialog_Workpod fragmentDialogWorkpod = new Fragment_Dialog_Workpod(workpod, workpod.getUbicacion(), posicion);
                                fragmentDialogWorkpod.show(getActivity().getSupportFragmentManager(), "UN SOLO WORPOD EN ESA UBICACIÓN");

                                //EVITAMOS QUE EL FRAGMENT_DIALOG_WORKPOD SE ABRA VARIAS VECES
                                break;
                            }
                        }
                    }//SI EN UNA UBICACIÓN SOLO HAY UN WORKPOD
                    else {
                        if (workpod.getId() == lstUbicacion.get(i).getWorkpods().get(0).getId()) {
                            workpod.getReserva().setEstado(lstUbicacion.get(i).getWorkpods().get(0).getReserva().getEstado());
                            Fragment_Dialog_Workpod fragmentDialogWorkpod = new Fragment_Dialog_Workpod(workpod, workpod.getUbicacion(), posicion);
                            fragmentDialogWorkpod.show(getActivity().getSupportFragmentManager(), "UN SOLO WORPOD EN ESA UBICACIÓN");
                            //EVITAMOS QUE EL FRAGMENT_DIALOG_WORKPOD SE ABRA VARIAS VECES

                            break;
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            //CONTROLAMOS QUE SI USER.GETRESERVA() APUNTA A NULO, EL USUARIO PUEDA ABRIR EL WORKPOD QUE QUIERA CONSULTAR
            //LE INDICAMOS QUE DESACTIVE EL BTN DE RESERVAR
            desactivarBtnReservar=true;
            //RECORREMOS LOS WORKPODS QUE HAY EN CADA UBICACIÓN (NO SE PUEDE HACER CON FOREACH)
            for (int i = 0; i < lstUbicacion.size(); i++) {
                //SI EN UNA UBICACIÓN HAY MÁS DE UNA CABINA
                if (lstUbicacion.get(i).getWorkpods().size() > 1) {
                    lstWorkpods = lstUbicacion.get(i).getWorkpods();
                    for (int j = 0; j < lstWorkpods.size(); j++) {
                        if (workpod.getId() == lstWorkpods.get(j).getId()) {
                            workpod = lstWorkpods.get(j);
                            Fragment_Dialog_Workpod fragmentDialogWorkpod = new Fragment_Dialog_Workpod(workpod, workpod.getUbicacion(), posicion);
                            fragmentDialogWorkpod.show(getActivity().getSupportFragmentManager(), "UN SOLO WORPOD EN ESA UBICACIÓN");
                            //EVITAMOS QUE EL FRAGMENT_DIALOG_WORKPOD SE ABRA VARIAS VECES
                            break;
                        }
                    }
                }//SI EN UNA UBICACIÓN SOLO HAY UN WORKPOD
                else {
                    if (workpod.getId() == lstUbicacion.get(i).getWorkpods().get(0).getId()) {
                        workpod = lstUbicacion.get(i).getWorkpods().get(0);
                        Fragment_Dialog_Workpod fragmentDialogWorkpod = new Fragment_Dialog_Workpod(workpod, workpod.getUbicacion(), posicion);
                        fragmentDialogWorkpod.show(getActivity().getSupportFragmentManager(), "UN SOLO WORPOD EN ESA UBICACIÓN");
                        //EVITAMOS QUE EL FRAGMENT_DIALOG_WORKPOD SE ABRA VARIAS VECES
                        break;
                    }
                }
            }
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
            tVDialogSessionTime.setText(min + "min " + seg + "seg");
        } else if (hour == 0 && min == 0) {
            tVDialogSessionTime.setText(seg + "seg");
        } else {
            tVDialogSessionTime.setText(hour + "h:" + min + "min");
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
    private void calcularPrecio(Double precioFinal, Double precio, int hour, int min, String ofertas) {
        //SI HAY OFERTAS
        if (!ofertas.equals(SIN_OFERTAS)) {
            precioFinal = precio - (precio * (Double.parseDouble(ofertas)) / 100);
            tVDialogOffers.setText(ofertas + "%");
            tVDialogPrice.setText(String.format("%.2f", precioFinal) + "€");
        } else {
            //SI NO HAY OFERTAS
            tVDialogPrice.setText(precio.toString() + "€");
            tVDialogOffers.setText(ofertas);
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
        this.lstTv = new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstTv.add(new Scale_TextView(tVDialogUbication, "match_parent", "bold", 20, 20, 20));
        lstTv.add(new Scale_TextView(tVDialogDateHour, "match_parent", "bold", 20, 20, 20));
        lstTv.add(new Scale_TextView(tVDialogSessionTime, "match_parent", "bold", 20, 20, 20));
        lstTv.add(new Scale_TextView(tVDialogOffers, "match_parent", "bold", 20, 20, 20));
        lstTv.add(new Scale_TextView(tVDialogPrice, "match_parent", "bold", 20, 20, 20));

        Method.scaleTv(metrics, lstTv);
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
            try{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        posicion.resource = null;
                        Toast.makeText(getContext(), "Habilite el GPS", Toast.LENGTH_LONG).show();
                    }
                });
            }catch(NullPointerException e){
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