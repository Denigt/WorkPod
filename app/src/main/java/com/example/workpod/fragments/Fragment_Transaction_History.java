package com.example.workpod.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workpod.R;
import com.example.workpod.WorkpodActivity;
import com.example.workpod.adapters.Adaptador_Spinner;
import com.example.workpod.basic.Database;
import com.example.workpod.data.Sesion;
import com.example.workpod.otherclass.Spinner_Years_Transaction_History;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//EN ESTA CLASE VAMOS A MONTAR EL ELSV Y SU ADAPTADOR
public class Fragment_Transaction_History extends Fragment {

    //XML
    //SPINNER Y SUS ELEMENTOS
    private Spinner spinnerYears;
    private int i = 0;
    private List<String> lstYears = new ArrayList<>();
    List<Spinner_Years_Transaction_History> lstSpinner = new ArrayList<>();

    //ELSV Y SUS ELEMENTOS
    ExpandableListView eLsV;

    //PARA QUE FUNCIONE EL ELSV EN UN FRAGMENT HAY QUE CREAR UN SEGUNDO CONTEXTO
    private FragmentActivity myContext;

    //ELEMENTOS DEL ELSV
    //ADAPTADORES
    Adaptador_Spinner adaptador_spinner;
    Adapter_ELsV adapter_eLsV;

    //COLECCIONES DEL ELSV
    ArrayList<String> monthList;//ALMACENAMOS EL MES (TITULOS DEL ELSV)
    HashMap<String, List<Sesion>> itemList;//ALMACENAMOS LA SESIÓN (ITEMS DEL ELSV)

    //LIST
    List<Sesion> lstSesiones = new ArrayList<>();//LAS SESIONES DE WORKPOD


    //CONSTRUCTOR POR DEFECTO
    public Fragment_Transaction_History() {
        // Required empty public constructor
    }

    //SOBREESCRITURAS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //NECESARIA PARA QUE AL ABRIR EL DIALOGO CON LA INFORMACIÓN DE
    //LA SESIÓN NO APUNTE A NULO
    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    //EL METODO DE ORDENAR LA LIST DE SESIONES REQUIERE LA API 24, POR ELLO USAMOS REQUIRESAPI
    //PARA QUE SEA COMPATIBLE CON LA API 21
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__transaction__history, container, false);
        //INICIALIZAMOS LOS ELEMENTOS DEL XML
        eLsV = (ExpandableListView) view.findViewById(R.id.eLsV2);
        spinnerYears = (Spinner) view.findViewById(R.id.SpinnerYears2);

        //CONEXIÓN CON LA BD, VOLCADO DE LAS SESIONES EN LSTSESIONES
        conectarseBDSesion(view, getActivity());

        //PARA ACTIVAR EL MENU EMERGENTE
        setHasOptionsMenu(true);

        return view;
    }


    //MÉTODOS

    /**
     * Este método servirá para que si no estás conectado a internet, no se realice la conexión
     * con la BD, Si no estás conectado a internet, te salta el Toast, si lo estás,se realiza la conexión
     * @param context contexto de la app
     * @param view instancia de la clase View
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void conectarseBDSesion(View view, Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //SI EL NETWORKINFO ES NULL O SI ISCONNECTED DEVUELVE FALSE ES QUE NO HAY INTERNET
        if (networkInfo == null || (networkInfo.isConnected() == false)) {
            Toast.makeText(getActivity(), "No estás conectado a internet", Toast.LENGTH_LONG).show();
        } else {
            try {
                Database<Sesion> dbSesion = new Database<>(Database.SELECTALL, new Sesion());
                dbSesion.postRun(() -> {
                    for (Sesion sesion :dbSesion.getLstSelect())
                        if (sesion.getEntrada() != null && sesion.getSalida() != null)
                            lstSesiones.add(sesion);
                });
                dbSesion.postRunOnUI(getActivity(), () -> {
                    montandoSpinner(view, lstSesiones, i, lstYears, lstSpinner);
                });
                dbSesion.start();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * EN ESTE MÉTODO CREAMOS UNA LISTA CON TODAS LAS SESIONES DEL USUARIO ALMACENADAS EN LA BD
     * POR AHORA ES LOCAL PERO EN UNOS DÍAS SERÁ UN SELECTALL DE LA BD
     *
     * @return
     */

    /**
     * EN ESTE MÉTODO, PRIMERO INTRODUCIMOS LOS AÑOS EN LOS QUE SE HAN REALIZADO LAS SESIONES DE WORKPOD EN UNA LIST, LUEGO
     * CONSTRUIMOS EL SPINNER PASANDOLE SU ADAPTADOR Y CON EL MÉTODO SETONITEMSELECTEDLISTENER LE DECIMOS QUE NOS MUESTRE
     * LAS SESIONES QUE SE HAYAN REALIZADO EN EL AÑO SELECCIONADO
     *
     * @param view
     * @param i              COMO EL ADAPATDOR DEL SPINNER NECESITA UN ATRIBUTO QUE HAGA DE ID, ESE ATRIBUTO SERÁ ESTA VARIABLE
     * @param lstTransaction LIST QUE CONTIENE LAS SESIONES DEL USUARIO
     * @param lstYears       LIST DONDE AÑADIREMOS LOS AÑOS REGISTRADOS EN LAS SESIONES DEL USUARIO, LA UTILIZAREMOS PARA COMPARAR LOS AÑOS
     *                       EN LOS QUE SE HA RELIZADO LAS SESIONES Y ASI EN LSTSPINNER NO HAYA DUPLICADOS
     * @param lstSpinner     SERÁ LA LISTA DONDE MONTAREMOS EL SPINNER YA QUE EL ADAPTADOR DEL SPINNER NECESITA UN ID Y UN VALOR, Y DICHO VALOR
     *                       SERÁ EL AÑO EN EL QUE SE HA REALIZADO LAS SESIONES DE WORKPOD
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void montandoSpinner(View view, List<Sesion> lstTransaction, int i, List<String> lstYears, List<Spinner_Years_Transaction_History> lstSpinner) {
        //ORDENAMOS LA LIST POR FECHA DE ENERO A DICIEMBRE Y DEL 1 AL 31 (30 O 28)
        lstTransaction.sort(Comparator.comparing(Sesion::getEntrada));
        //LE DAMOS LA VUELTA A LA LIST PARA QUE SALGAN PRIMERO LAS SESIONES MÁS RECIENTES
        Collections.reverse(lstTransaction);
        //RECORREMOS LAS SESIONES
        for (Sesion transaction : lstTransaction) {
            //SI LA LISTA DE AÑOS NO CONTIENE UN AÑO EN EL QUE SE HA REALIZADO UNA SESIÓN, QUE LO AÑADA A LA LISTA
            if (!lstYears.contains(String.valueOf(transaction.getEntrada().getYear()))) {
                //AÑADIMOS EL AÑOS A LA LSTYEAR
                lstYears.add(String.valueOf(transaction.getEntrada().getYear()));
                i++;
                //AÑADIMOS EL CÓDIGO Y EL AÑO AL SPINNER
                lstSpinner.add(new Spinner_Years_Transaction_History((i + 1), String.valueOf(transaction.getEntrada().getYear())));
            }
        }

        //UNA VEZ INTRODUCIDOS LOS AÑOS EN LA LIST, INSTANCIAMOS EL ADAPATADOR DEL SPINNER
        adaptador_spinner = new Adaptador_Spinner(view.getContext(), lstSpinner);
        //LE PASAMOS EL ADAPTADOR AL SPINNER
        spinnerYears.setAdapter(adaptador_spinner);
        //EVENTO QUE NOS PERMITIRÁ QUE AL SELECCIONAR UN AÑO DEL SPINNER, SE MUESTRE SUS SESIONES
        spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                //CONECTAMOS UN OBJETO DE LA CLASE SPINNER CON EL ADAPTADOR
                Spinner_Years_Transaction_History spinner = (Spinner_Years_Transaction_History) adaptador_spinner.getItem(i);
                //RECORREMOS LA LISTA DE SESIONES
                for (Sesion transaction : lstTransaction) {
                    //SI EL AÑO DEL SPINNER APUNTA AL AÑO EN EL QUE SE HA REALIZADO LA SESIÓN
                    if (spinner.getTitulo().equals(String.valueOf(transaction.getEntrada().getYear()))) {
                        //LE PASAMOS DICHO AÑO AL ELSV
                        construyendoELsV(transaction.getEntrada().getYear());
                        //INICIALIZAMOS EL ADAPATADOR DEL ELSV
                        adapter_eLsV = new Adapter_ELsV(view.getContext(), monthList, itemList);
                        //LE PASAMOS EL ADAPTADOR AL ELSV
                        eLsV.setAdapter(adapter_eLsV);
                    }
                }

            }

            //SOBREESCRITURA QUE SE USARÍA SI QUEREMOS QUE EL SPINNER HAGA ALGO CUANDO NO SE SELECCIONA NINGÚN ITEM
            //NO SE PUEDE QUITAR DICHA SOBREESCRITURA
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * EN ESTE MÉTODO LE VAMOS A PASAR EL AÑO QUE SE SELECCIONA EN EL SPINNER AL ELSV, Y SE CONSTRUIRÁ
     * CON LAS SESIONES REALIZADAS EN DICHO AÑO
     *
     * @param year AÑO SELECCIONADO EN EL SPINNER
     */
    private void construyendoELsV(int year) {
        //INICIALIZAMOS LA LISTA DE MESES
        monthList = new ArrayList<String>();
        //INICIALIZAMOS LA LISTA DE SESIONES
        itemList = new HashMap<String, List<Sesion>>();
        //METEMOS EN LOS TITULOS DEL ELSV LOS MESES
        for (Sesion transaction : lstSesiones) {
            //SI EL AÑO DE LA SESIÓN APUNTA AL AÑO QUE HEMOS SELECCIONADO EN EL SPINNER
            if (String.valueOf(transaction.getEntrada().getYear()).equals(String.valueOf(year))) {
                //SI LA LISTA DE MESES NO CONTIENE YA ESE MES (EN EL IDIOMA EN EL QUE ESTEMOS TRABAJANDO) LO AÑADIMOS A LA LIST DE MESES
                if (!monthList.contains(transaction.getEntrada().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase())) {
                    //AÑADIMOS EL MES A LA LISTA DE MESES
                    monthList.add(transaction.getEntrada().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
                }
            }

        }


        // RECORRO LA LISTA DE MESES
        for (String meses : monthList) {
            //CREAMOS AQUÍ LA LISTA DONDE IRÁN LAS SESIONE PARA QUE SE APLIQUEN LOS FILTROS
            List<Sesion> lstSesion = new ArrayList<>();
            //RECORRO LA LISTA DE SESIONES
            for (Sesion transaction : lstSesiones) {
                //SI EL MES A PUNTA AL MES DE LA SESIÓN (EN EL IDIOMA EN EL QUE ESTEMOS TRABAJANDO)
                if (meses.equals(transaction.getEntrada().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase())) {
                    //SI EL AÑO DE LA SESIÓN APUNTA AL AÑO QUE HEMOS SELECCIONADO EN EL SPINNER
                    if (String.valueOf(transaction.getEntrada().getYear()).equals(String.valueOf(year))) {
                        //AÑADIMOS A LA LISTA LA SESIÓN
                        lstSesion.add(transaction);
                        //CONSTRUIMOS EL HASHMAP DONDE ESTAMOS CONTROLANDO QUE EN CADA MES (CLAVE) SE METE LA SESIÓN HECHA EN DICHO MES (VALOR) Y EN EL AÑO
                        //EN EL QUE ESTAMOS TRABAJANDO
                        itemList.put(meses, lstSesion);
                    }

                }
            }
        }
    }

    //CLASE DEL ADAPTADOR
    //EL ADAPTADOR DE UN ELSV ES MUY PARECIDO AL DE UN LSV, SOLO QUE HAY DOS TIPOS VALORES, EL DE LOS TÍTULOS Y EL DE LOS DATOS QUE PERTENECEN
    //A CADA TÍTULO, EL TÍTULO SERA UNA LIST<STRING> Y LOS DATOS SERÁN UN HASHMAP, DONDE LA KEY SERÁ EL TÍTULO Y LOS DATOS QUE PERTENECEN
    //A DICHO TÍTULO SERÁ UNA LIST QUE SERÁ EL VALUE DEL HASHMAP
    //LAS SOBREESCRITURAS DEL ADAPTADOR DEL ELSV ES COMO LA DE UN LSV PERO POR DUPLICADO, UNO PARA LOS TÍTULOS Y OTRO PARA LOS ITEMS
    public class Adapter_ELsV extends BaseExpandableListAdapter {
        //DECLARAMOS EL CONTEXTO Y LA LIST PARA LOS TÍTULOS Y EL HASHMAP PARA LOS ITEMS
        private Context context;
        private List<String> monthList;
        private HashMap<String, List<Sesion>> items_ELsV;

        //VARIABLES PARA EL CÁLCULO DEL TIEMPO DE SESIÓN DEL USUARIO EN WORKPOD
        private int hour;
        private int min;
        private int seg;

        //CONSTRUCTOR CON TODOS LOS PARÁMETROS
        public Adapter_ELsV(Context context, List<String> chapterList, HashMap<String, List<Sesion>> items_ELsV) {
            this.context = context;
            this.monthList = chapterList;
            this.items_ELsV = items_ELsV;
        }

        //TAMAÑO DE LOS TÍTULOS
        @Override
        public int getGroupCount() {
            return this.monthList.size();
        }

        //TAMAÑO DE LA LIST DE ITEMS DE CADA CHARAPTER
        @Override
        public int getChildrenCount(int groupPosition) {
            return this.items_ELsV.get(this.monthList.get(groupPosition)).size();
        }

        //DEVUELVE LA POSICIÓN DE LOS TÍTULOS
        @Override
        public Object getGroup(int groupPosition) {
            return this.monthList.get(groupPosition);
        }

        //DEVUELVE LA POSICIÓN DE LOS ITEMS
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.items_ELsV.get(this.monthList.get(groupPosition)).get(childPosition);
        }

        //DEVUELVE LA CLAVE PRIMARIA DE LOS TÍTULOS
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //DEVUELVE LA CLAVE PRIMARIA DE LOS ITEMS
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        //CONSTRUIMOS LA PARTE DE LOS TÍTULOS DEL ELSV
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
            //DECLARAMOS UN STRING AL QUE ASOCIAMOS LO QUE DEVUELVA LOS TÍTULOS (LOS MESES) EN CADA POSICIÓN
            String monthTitle = (String) getGroup(groupPosition);
            //EN EL CASO DE QUE SEA NULO, QUE NOS INFLE EL ELSV IGUALMENTE (POR DEFECTO, PARA EVITAR ERRORES)
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.month_list, null);
            }
            //COMO EL TITULO DE LOS MESES SOLO NECESITA UN TV LO DECLARAMOS Y LO INICIALIZAMOS CON EL TV DEL XML
            TextView tVMonthList = view.findViewById(R.id.TVMonthList);
            //LE PASAMOS EL VALOR DE LOS TITULOS DEL ELSV AL TV DEL XML
            tVMonthList.setText(monthTitle);

            return view;
        }

        //CONSTRUIMOS LA PARTE DE LOS ITEMS DEL ELSV
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
            //COMO EN LOS ITEMS QUEREMOS PONER VARIOS ATRIBUTOS DE LA CLASE LSV, CREAMOS UN OBJETO DE LA CLASE LSV
            //Y LO ASOCIAMOS CON LO QUE DEVUELVE LOS ITEMS DEL ELSV (ESPECIFICANDO EL TITULO Y EL ITEM CON SUS RESPECTIVAS POSICIONES)
            final Sesion sesion = (Sesion) getChild(groupPosition, childPosition);
            //EN EL CASO DE QUE SEA NULO, QUE NOS INFLE EL ELSV IGUALMENTE (POR DEFECTO, PARA EVITAR ERRORES)
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_lsv_transaction_history, null);
            }
            //DECLARAMOS TODOS LOS ELEMENTOS QUE VAMOS A USAR
            TextView tVItemsELsV = view.findViewById(R.id.TVItemsELsV);
            TextView tVUbicacion;
            TextView tVFecha;
            TextView tVTiempo;
            //LOS INICIALIZAMOS CON LOS ELEMENTOS DEL XML
            tVUbicacion = (TextView) view.findViewById(R.id.iTVUbicacion);
            tVFecha = (TextView) view.findViewById(R.id.iTVFecha);
            tVTiempo = (TextView) view.findViewById(R.id.iTVHora);
            // tVUbicacion.setText(sesion.getUbicacion());

            //LE PASAMOS LA INFORMACIÓN DE LAS SESIONES
            //PARA ELLO UTILIZAMOS EL OBJETO DE LA CLASE LSV AL QUE LE HEMOS ASOCIADO LA DEVOLUCIÓN DE LOS ITEMS

            //APLICAMOS EL MÉTODO DE CALCULAR EL TIEMPO DE SESIÓN DEL USUARIO
            calcularTiempoSesion(sesion.getEntrada(), sesion.getSalida(), hour, min
                    , seg);
            tVFecha.setText(String.valueOf(sesion.getEntrada().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
            tVTiempo.setText(hour + "h:" + min + "min");
            tVUbicacion.setText(sesion.getDireccion().toString());
            //CREAMOS LA CONDICIÓN DE QUE AL TOCAR EN UN ITEM DEL ELSV SE ABRA EL CUADRO DE DIALOGO CON TODA LA INFORMACIÓN DE LA SESIÓN

            //CREO UN OBJETO LAYOUT PARA QUE AL TOCAR EL ITEM DEL ELSV SE ME ABRA EL DIALOGO EMERGENTE
            LinearLayout layout = view.findViewById(R.id.LLSesion);
            //CON EL EVENTO setOnClickListener SE ME ABRIRÁ EL DIÁLOGO EMERGENTE AL TOCAR EL ITEM
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //CREAMOS UN OBJETO DEL FRAGMENT QUE HACE DE DIALOGO EMERGENTE Y EN SU CONSTRUCTOR A TRAVÉS DEL OBJETO LSV LE PASAMOS LOS
                    //VALORES DE LA SESIÓN SELECCIONADA
                    Fragment_Dialog_Transaction_Session fragmentDialogTransactionSession = new Fragment_Dialog_Transaction_Session(sesion.getDireccion().toString(), sesion.getEntrada(),
                            sesion.getSalida(), String.valueOf(sesion.getDescuento()), sesion.getPrecio());
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.LLFragment,fragmentDialogTransactionSession).addToBackStack(null).commit();
                    //POENEMOS EL BOOLEANO QUE CONTROLA QUE UNA VEZ IDO AL FRAGMENT DE LA SESIÓN SE VUELVA AL HISTÓRICO A TRUE
                    WorkpodActivity.boolfolder=true;
                    WorkpodActivity.boolLoc=true;
                }
            });

            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
        //MÉTODOS

        /**
         * Método para calcular la diferencia entre 2 fechas.
         * La clase ZoneDateTime no posee el método getTime() el cual permite calcular los milisegundos entre dos fechas,
         * es el método más fácil para calcular el tiempo entre 2 fechas. Para ello, parseamos nuestras fechas a Date y
         * aplicalamos los calculos respectivos para transformar las diferencias de milisegundos entre 2 fechas en horas, minutos y segundos
         *
         * @param fechaEntrada fecha de entrada al workpod
         * @param fechaSalida  fecha de salida del workpod
         * @param hour         horas que ha durado la sesión de workpod
         * @param min          minutos que ha durado la sesión de workpod
         * @param seg          segundos que ha durado la sesión de workpod
         */
        private void calcularTiempoSesion(ZonedDateTime fechaEntrada, ZonedDateTime fechaSalida, int hour, int min, int seg) {
            Date fecha1 = Date.from(fechaEntrada.toInstant());
            Date fecha2 = Date.from(fechaSalida.toInstant());
            long dif = fecha2.getTime() - fecha1.getTime();
            seg = (int) (dif / 1000) % 60;
            min = (int) ((dif / (1000 * 60)) % 60);
            hour = (int) ((dif / (1000 * 60 * 60)) % 24);
            //LE PASAMOS LOS VALORES CALCULADOS A LAS VARIABLES QUE USAMOS FUERA DEL METODO
            this.hour = hour;
            this.min = min;
            //AGREGAMOS EL TIEMPO DE SESIÓN DE TRABAJO EN UN WORKPOD DEL USUARIO

        }
    }
}