package com.example.workpod.fragments;

//IMPRESCINDIBLE PARA QUE FUNCIONE EN APIS INFERIORES A LA 23

import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.data.Workpod;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

//PARA CREAR UN DIALOGRESULT DEBEMOS HEREDAR DE LA CLASE DIALOGFRAGMENT
public class Fragment_Dialog_Transaction_Session extends Fragment implements View.OnClickListener {

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
    private ImageView iVSalirDialogSession;
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


    //CONSTRUCTOR
    public Fragment_Dialog_Transaction_Session(String ubicacion, ZonedDateTime fechaEntrada, ZonedDateTime fechaSalida, String offers,
                                               Double precio) {
        this.ubication = ubicacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.ofertas = offers;
        this.precio = precio;
        this.workpod=workpod;

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
    public Fragment_Dialog_Transaction_Session() {
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
        iVDialogUbication=(ImageView)view.findViewById(R.id.IVDialogUbication);


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
        iVSalirDialogSession.setOnClickListener(this);
        iVDialogUbication.setOnClickListener(this);
        return view;

    }



    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ESPECIFICAMOS DONDE VAMOS A CREAR (INFLAR) EL DIALOGRESULT
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_transaction_session, null);
        builder.setView(v);



        //RETORNAMOS EL OBJETO BUILDER CON EL MÉTODO CREATE
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        //CIERRA EL CUADRO DE DIALOGO
      if(v.getId()==R.id.IVDialogUbication){
          //Toast.makeText(getActivity(),String.valueOf(workpod.getUbicacion().getPosicion()),Toast.LENGTH_SHORT).show();
         /*   Fragment_Maps fragmentMaps=new Fragment_Maps();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LLFragment,fragmentMaps).commit();
            //CAMBIAMOS EL ICONO SELECCIONADO
            WorkpodActivity.boolLoc = true;
            WorkpodActivity.btnNV.setSelectedItemId(R.id.inv_location);*/
            //CERRAMOS EL DIALOGO EMERGENTE
          //  dismiss();

        }
    }

    //MÉTODOS

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
        //AGREGAMOS EL TIEMPO DE SESIÓN DE TRABAJO EN UN WORKPOD DEL USUARIO
        tVDialogSessionTime.setText(String.valueOf(hour) + "h " + String.valueOf(min) + "min");

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
        precio = (Double.parseDouble(String.valueOf(hour)) * 60 + Double.parseDouble(String.valueOf(min))) * 0.2;
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
     * @param nAleatorio número aleatorio
     * @param letra contiene todo el alfabeto con mayusculas y minusculas separadas por comas
     * @param signo contiene todos los signos que vamos a usar separados por comas
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
        idSesionWorkpod=String.valueOf(nAleatorio) + matrizSignos[signoAleatorio1] + alfabeto[letraAleatoria1] +
                matrizSignos[signoAleatorio2] + alfabeto[letraAleatoria2];
        //MOSTRAMOS EL RESULTADO PARA VER SI SE HA FORMADO UN ID ALEATORIO CORRECTO
        //Toast.makeText(getActivity(),idSesionWorkpod,Toast.LENGTH_LONG).show();
    }

}