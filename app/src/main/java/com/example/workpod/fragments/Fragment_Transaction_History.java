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

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workpod.R;
import com.example.workpod.ValoracionWorkpod;
import com.example.workpod.WorkpodActivity;
import com.example.workpod.adapters.Adaptador_Spinner;
import com.example.workpod.basic.Database;
import com.example.workpod.basic.InfoApp;
import com.example.workpod.basic.Method;
import com.example.workpod.data.Sesion;
import com.example.workpod.otherclass.Spinner_Years_Transaction_History;
import com.example.workpod.scale.Scale_TextView;

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
public class Fragment_Transaction_History extends Fragment implements View.OnClickListener {

    //XML
    //SPINNER Y SUS ELEMENTOS
    private Spinner spinnerYears;
    private int i = 0;
    private List<String> lstYears = new ArrayList<>();
    List<Spinner_Years_Transaction_History> lstSpinner = new ArrayList<>();

    //COLECCIONES
    List<Scale_TextView> lstTv;
    List<Sesion> lstSesiones = new ArrayList<>();//LAS SESIONES DE WORKPOD

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
    HashMap<String, List<Sesion>> itemList;//ALMACENAMOS LA SESI??N (ITEMS DEL ELSV)

    //XML
    private TextView tVfgmTransHistMisSesiones;
    private TextView tVfgmTransHistSelectAnio;
    private LinearLayout lLSinSesiones;
    private LinearLayout lLSeleccioneAnio;
    private ImageView iVLocationInTransactionHistory;
    private TextView tVNoSesion;

    //CONSTRUCTOR POR DEFECTO
    public Fragment_Transaction_History() {
        // Required empty public constructor
    }

    //SOBREESCRITURAS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //NECESARIA PARA QUE AL ABRIR EL DIALOGO CON LA INFORMACI??N DE
    //LA SESI??N NO APUNTE A NULO
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
        tVfgmTransHistMisSesiones = view.findViewById(R.id.tVfgmTransHistMisSesiones);
        tVfgmTransHistSelectAnio = view.findViewById(R.id.tVfgmTransHistSelectAnio);
        tVNoSesion = view.findViewById(R.id.TVNoSesion);
        lLSinSesiones = view.findViewById(R.id.LLSinSesiones);
        lLSeleccioneAnio = view.findViewById(R.id.LLSeleccioneAnio);
        iVLocationInTransactionHistory = view.findViewById(R.id.IVLocationInTransactionHistory);

        //CONEXI??N CON LA BD, VOLCADO DE LAS SESIONES EN LSTSESIONES
        conectarseBDSesion(view, getActivity());

        //PARA ACTIVAR EL MENU EMERGENTE
        setHasOptionsMenu(true);

        //ESCALAMOS ELEMENTOS
        escalarElementos();

        //REMARCAR EL ICONO DEL NV (SOLO PARA CUANDO EL USUARIO EST?? EN UNA SESI??N)
        if (InfoApp.USER.getReserva() != null) {
            if (InfoApp.USER.getReserva().getEstado().equalsIgnoreCase("En Uso") && (!ValoracionWorkpod.boolReservaFinalizada)) {
                WorkpodActivity.btnNV.getMenu().findItem(R.id.inv_folder).setChecked(true);
            }
        }

        // ESTABLECER EVENTOS PARA LOS CONTROLES
        iVLocationInTransactionHistory.setOnClickListener(this);

        return view;
    }


    //M??TODOS

    /**
     * Este m??todo servir?? para que si no est??s conectado a internet, no se realice la conexi??n
     * con la BD, Si no est??s conectado a internet, te salta el Toast, si lo est??s,se realiza la conexi??n
     *
     * @param context contexto de la app
     * @param view    instancia de la clase View
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void conectarseBDSesion(View view, Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //SI EL NETWORKINFO ES NULL O SI ISCONNECTED DEVUELVE FALSE ES QUE NO HAY INTERNET
        if (networkInfo == null || (networkInfo.isConnected() == false)) {
            Toast.makeText(getActivity(), "No est??s conectado a internet", Toast.LENGTH_LONG).show();
        } else {
            try {
                Database<Sesion> dbSesion = new Database<>(Database.SELECTUSER, new Sesion());
                dbSesion.postRun(() -> {
                    for (Sesion sesion : dbSesion.getLstSelect())
                        if (sesion.getEntrada() != null && sesion.getSalida() != null) {
                            if (!lstSesiones.contains(sesion))
                                lstSesiones.add(sesion);
                        }

                });
                dbSesion.postRunOnUI(getActivity(), () -> {
                    if (lstSesiones.size() != 0) {
                        //HACEMOS QUE APAREZCA LOS ELEMENTOS DE CUANDO EL USUARIO YA HA REALIZADO ALGUNA SESI??N
                        tVfgmTransHistMisSesiones.setVisibility(View.VISIBLE);
                        lLSeleccioneAnio.setVisibility(View.VISIBLE);
                        eLsV.setVisibility(View.VISIBLE);
                        //OCULTAMOS LOS ELEMENTOS QUE SALEN CUANDO NO TIENES NINGUNA SESI??N
                        iVLocationInTransactionHistory.setVisibility(View.GONE);
                        lLSinSesiones.setVisibility(View.GONE);
                        montandoSpinner(view, lstSesiones, i, lstYears, lstSpinner);
                    } else {
                        //OCULTAMOS EL SPINNER Y EL ELSV
                        lLSeleccioneAnio.setVisibility(View.GONE);
                        eLsV.setVisibility(View.GONE);
                        //HACEMOS QUE APAREZCA LOS ELEMENTOS QUE SALEN CUANDO NO TIENES NINGUNA SESI??N
                        tVfgmTransHistMisSesiones.setVisibility(View.VISIBLE);
                        iVLocationInTransactionHistory.setVisibility(View.VISIBLE);
                        lLSinSesiones.setVisibility(View.VISIBLE);
                    }
                });
                dbSesion.start();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * EN ESTE M??TODO, PRIMERO INTRODUCIMOS LOS A??OS EN LOS QUE SE HAN REALIZADO LAS SESIONES DE WORKPOD EN UNA LIST, LUEGO
     * CONSTRUIMOS EL SPINNER PASANDOLE SU ADAPTADOR Y CON EL M??TODO SETONITEMSELECTEDLISTENER LE DECIMOS QUE NOS MUESTRE
     * LAS SESIONES QUE SE HAYAN REALIZADO EN EL A??O SELECCIONADO
     *
     * @param view
     * @param i              COMO EL ADAPATDOR DEL SPINNER NECESITA UN ATRIBUTO QUE HAGA DE ID, ESE ATRIBUTO SER?? ESTA VARIABLE
     * @param lstTransaction LIST QUE CONTIENE LAS SESIONES DEL USUARIO
     * @param lstYears       LIST DONDE A??ADIREMOS LOS A??OS REGISTRADOS EN LAS SESIONES DEL USUARIO, LA UTILIZAREMOS PARA COMPARAR LOS A??OS
     *                       EN LOS QUE SE HA RELIZADO LAS SESIONES Y ASI EN LSTSPINNER NO HAYA DUPLICADOS
     * @param lstSpinner     SER?? LA LISTA DONDE MONTAREMOS EL SPINNER YA QUE EL ADAPTADOR DEL SPINNER NECESITA UN ID Y UN VALOR, Y DICHO VALOR
     *                       SER?? EL A??O EN EL QUE SE HA REALIZADO LAS SESIONES DE WORKPOD
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void montandoSpinner(View view, List<Sesion> lstTransaction, int i, List<String> lstYears, List<Spinner_Years_Transaction_History> lstSpinner) {

        //ORDENAMOS LA LIST POR FECHA DE ENERO A DICIEMBRE Y DEL 1 AL 31 (30 O 28)
        lstTransaction.sort(Comparator.comparing(Sesion::getEntrada));
        //LE DAMOS LA VUELTA A LA LIST PARA QUE SALGAN PRIMERO LAS SESIONES M??S RECIENTES
        Collections.reverse(lstTransaction);
        //RECORREMOS LAS SESIONES
        for (Sesion transaction : lstTransaction) {
            //SI LA LISTA DE A??OS NO CONTIENE UN A??O EN EL QUE SE HA REALIZADO UNA SESI??N, QUE LO A??ADA A LA LISTA
            if (!lstYears.contains(String.valueOf(transaction.getEntrada().getYear()))) {
                //A??ADIMOS EL A??OS A LA LSTYEAR
                lstYears.add(String.valueOf(transaction.getEntrada().getYear()));
                i++;
                //A??ADIMOS EL C??DIGO Y EL A??O AL SPINNER
                lstSpinner.add(new Spinner_Years_Transaction_History((i + 1), String.valueOf(transaction.getEntrada().getYear())));
            }
        }

        //UNA VEZ INTRODUCIDOS LOS A??OS EN LA LIST, INSTANCIAMOS EL ADAPATADOR DEL SPINNER
        adaptador_spinner = new Adaptador_Spinner(view.getContext(), lstSpinner);
        //LE PASAMOS EL ADAPTADOR AL SPINNER
        spinnerYears.setAdapter(adaptador_spinner);
        //EVENTO QUE NOS PERMITIR?? QUE AL SELECCIONAR UN A??O DEL SPINNER, SE MUESTRE SUS SESIONES
        spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                try {
                    //CONECTAMOS UN OBJETO DE LA CLASE SPINNER CON EL ADAPTADOR
                    Spinner_Years_Transaction_History spinner = (Spinner_Years_Transaction_History) adaptador_spinner.getItem(i);
                    //RECORREMOS LA LISTA DE SESIONES
                    for (Sesion transaction : lstTransaction) {
                        //SI EL A??O DEL SPINNER APUNTA AL A??O EN EL QUE SE HA REALIZADO LA SESI??N
                        if (spinner.getTitulo().equals(String.valueOf(transaction.getEntrada().getYear()))) {
                            //LE PASAMOS DICHO A??O AL ELSV
                            construyendoELsV(transaction.getEntrada().getYear());
                            //INICIALIZAMOS EL ADAPATADOR DEL ELSV
                            adapter_eLsV = new Adapter_ELsV(view.getContext(), monthList, itemList);
                            //LE PASAMOS EL ADAPTADOR AL ELSV
                            eLsV.setAdapter(adapter_eLsV);
                        }
                    }
                } catch (NullPointerException e) {
                    //CONTROLA QUE NO CASQUE EL ADAPTADOR CUANDO APUNTA A NULO (LN 251) ESTO OCURRE CUANDO ESTAMOS EN UNA SESI??N Y ACCEDEMOS
                    //AL HIST??RICO Y DESPU??S ABANDONAMOS LA APP
                    e.printStackTrace();
                }

            }

            //SOBREESCRITURA QUE SE USAR??A SI QUEREMOS QUE EL SPINNER HAGA ALGO CUANDO NO SE SELECCIONA NING??N ITEM
            //NO SE PUEDE QUITAR DICHA SOBREESCRITURA
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.IVLocationInTransactionHistory) {
            irAlMapa();
        }
    }

    /**
     * EN ESTE M??TODO LE VAMOS A PASAR EL A??O QUE SE SELECCIONA EN EL SPINNER AL ELSV, Y SE CONSTRUIR??
     * CON LAS SESIONES REALIZADAS EN DICHO A??O
     *
     * @param year A??O SELECCIONADO EN EL SPINNER
     */
    private void construyendoELsV(int year) {
        //INICIALIZAMOS LA LISTA DE MESES
        monthList = new ArrayList<String>();
        //INICIALIZAMOS LA LISTA DE SESIONES
        itemList = new HashMap<String, List<Sesion>>();
        //METEMOS EN LOS TITULOS DEL ELSV LOS MESES
        for (Sesion transaction : lstSesiones) {
            //SI EL A??O DE LA SESI??N APUNTA AL A??O QUE HEMOS SELECCIONADO EN EL SPINNER
            if (String.valueOf(transaction.getEntrada().getYear()).equals(String.valueOf(year))) {
                //SI LA LISTA DE MESES NO CONTIENE YA ESE MES (EN EL IDIOMA EN EL QUE ESTEMOS TRABAJANDO) LO A??ADIMOS A LA LIST DE MESES
                if (!monthList.contains(transaction.getEntrada().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase())) {
                    //A??ADIMOS EL MES A LA LISTA DE MESES
                    monthList.add(transaction.getEntrada().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
                }
            }

        }


        // RECORRO LA LISTA DE MESES
        for (String meses : monthList) {
            //CREAMOS AQU?? LA LISTA DONDE IR??N LAS SESIONE PARA QUE SE APLIQUEN LOS FILTROS
            List<Sesion> lstSesion = new ArrayList<>();
            //RECORRO LA LISTA DE SESIONES
            for (Sesion transaction : lstSesiones) {
                //SI EL MES A PUNTA AL MES DE LA SESI??N (EN EL IDIOMA EN EL QUE ESTEMOS TRABAJANDO)
                if (meses.equals(transaction.getEntrada().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase())) {
                    //SI EL A??O DE LA SESI??N APUNTA AL A??O QUE HEMOS SELECCIONADO EN EL SPINNER
                    if (String.valueOf(transaction.getEntrada().getYear()).equals(String.valueOf(year))) {
                        //A??ADIMOS A LA LISTA LA SESI??N
                        lstSesion.add(transaction);
                        //CONSTRUIMOS EL HASHMAP DONDE ESTAMOS CONTROLANDO QUE EN CADA MES (CLAVE) SE METE LA SESI??N HECHA EN DICHO MES (VALOR) Y EN EL A??O
                        //EN EL QUE ESTAMOS TRABAJANDO
                        itemList.put(meses, lstSesion);
                    }

                }
            }
        }

    }

    /**
     * Este m??todo sirve de ante sala para el m??todo de la clase Methods donde escalamos los elementos del xml.
     * En este m??todo inicializamos las colecciones donde guardamos los elementos del xml que vamos a escalar y
     * donde especificamos el width que queremos (match_parent, wrap_content o ""(si no ponemos nada significa que
     * el elemento tiene unos dp definidos que queremos que se conserven tanto en dispositivos grandes como en peque??os.
     * Tambi??n especificamos en la List el estilo de letra (bold, italic, normal) y el tama??o de la fuente del texto tanto
     * para dispositivos peque??os como para dispositivos grandes).
     * <p>
     * Como el m??todo scale de la clase Methods no es un activity o un fragment no podemos inicializar nuestro objeto de la clase
     * DisplayMetrics con los par??metros reales de nuestro m??vil, es por ello que lo inicializamos en este m??todo.
     * <p>
     * En resumen, en este m??todo inicializamos el metrics y las colecciones y se lo pasamos al m??todo de la clase Methods
     */
    private void escalarElementos() {
        //INICIALIZAMOS COLECCIONES
        this.lstTv = new ArrayList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LLENAMOS COLECCIONES
        lstTv.add(new Scale_TextView(tVfgmTransHistMisSesiones, "", "bold", 35, 35, 35));
        lstTv.add(new Scale_TextView(tVfgmTransHistSelectAnio, "wrap_content", "bold", 23, 23, 23));
        lstTv.add(new Scale_TextView(tVNoSesion, "match_parent", "bold", 15, 18, 22));

        Method.scaleTv(metrics, lstTv);
    }

    /**
     * Al pulsar en el IV de location te lleva al mapa o a tu sesi?? si est??s realizando una sesi??n
     */
    private void irAlMapa() {
        //CERRAMOS EL FRAGMENT Y VOLVEMOS AL MAPA
        getActivity().onBackPressed();
    }

    //CLASE DEL ADAPTADOR
    //EL ADAPTADOR DE UN ELSV ES MUY PARECIDO AL DE UN LSV, SOLO QUE HAY DOS TIPOS VALORES, EL DE LOS T??TULOS Y EL DE LOS DATOS QUE PERTENECEN
    //A CADA T??TULO, EL T??TULO SERA UNA LIST<STRING> Y LOS DATOS SER??N UN HASHMAP, DONDE LA KEY SER?? EL T??TULO Y LOS DATOS QUE PERTENECEN
    //A DICHO T??TULO SER?? UNA LIST QUE SER?? EL VALUE DEL HASHMAP
    //LAS SOBREESCRITURAS DEL ADAPTADOR DEL ELSV ES COMO LA DE UN LSV PERO POR DUPLICADO, UNO PARA LOS T??TULOS Y OTRO PARA LOS ITEMS
    public class Adapter_ELsV extends BaseExpandableListAdapter {
        //DECLARAMOS EL CONTEXTO Y LA LIST PARA LOS T??TULOS Y EL HASHMAP PARA LOS ITEMS
        private Context context;
        private List<String> monthList;
        private HashMap<String, List<Sesion>> items_ELsV;

        //VARIABLES PARA EL C??LCULO DEL TIEMPO DE SESI??N DEL USUARIO EN WORKPOD
        private int hour;
        private int min;
        private int seg;

        //CONSTRUCTOR CON TODOS LOS PAR??METROS
        public Adapter_ELsV(Context context, List<String> chapterList, HashMap<String, List<Sesion>> items_ELsV) {
            this.context = context;
            this.monthList = chapterList;
            this.items_ELsV = items_ELsV;
        }

        //TAMA??O DE LOS T??TULOS
        @Override
        public int getGroupCount() {
            return this.monthList.size();
        }

        //TAMA??O DE LA LIST DE ITEMS DE CADA CHARAPTER
        @Override
        public int getChildrenCount(int groupPosition) {
            return this.items_ELsV.get(this.monthList.get(groupPosition)).size();
        }

        //DEVUELVE LA POSICI??N DE LOS T??TULOS
        @Override
        public Object getGroup(int groupPosition) {
            return this.monthList.get(groupPosition);
        }

        //DEVUELVE LA POSICI??N DE LOS ITEMS
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.items_ELsV.get(this.monthList.get(groupPosition)).get(childPosition);
        }

        //DEVUELVE LA CLAVE PRIMARIA DE LOS T??TULOS
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

        //CONSTRUIMOS LA PARTE DE LOS T??TULOS DEL ELSV
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
            //DECLARAMOS UN STRING AL QUE ASOCIAMOS LO QUE DEVUELVA LOS T??TULOS (LOS MESES) EN CADA POSICI??N
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

            //LE PASAMOS LA INFORMACI??N DE LAS SESIONES
            //PARA ELLO UTILIZAMOS EL OBJETO DE LA CLASE LSV AL QUE LE HEMOS ASOCIADO LA DEVOLUCI??N DE LOS ITEMS

            //APLICAMOS EL M??TODO DE CALCULAR EL TIEMPO DE SESI??N DEL USUARIO
            calcularTiempoSesion(sesion.getEntrada(), sesion.getSalida(), hour, min
                    , seg);
            tVFecha.setText(String.valueOf(sesion.getEntrada().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
            //EST??TICA DEL TIEMPO DE SESI??N
            if (hour == 0 && min != 0) {
                tVTiempo.setText(min + "min");
            } else if (hour == 0 && min == 0) {
                tVTiempo.setText(seg + "seg");
            } else {
                tVTiempo.setText(hour + "h:" + min + "min");
            }
            tVUbicacion.setText(sesion.getDireccion().toString());
            //CREAMOS LA CONDICI??N DE QUE AL TOCAR EN UN ITEM DEL ELSV SE ABRA EL CUADRO DE DIALOGO CON TODA LA INFORMACI??N DE LA SESI??N

            //CREO UN OBJETO LAYOUT PARA QUE AL TOCAR EL ITEM DEL ELSV SE ME ABRA EL DIALOGO EMERGENTE
            LinearLayout layout = view.findViewById(R.id.LLSesion);
            //CON EL EVENTO setOnClickListener SE ME ABRIR?? EL DI??LOGO EMERGENTE AL TOCAR EL ITEM
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //CREAMOS UN OBJETO DEL FRAGMENT QUE HACE DE DIALOGO EMERGENTE Y EN SU CONSTRUCTOR A TRAV??S DEL OBJETO LSV LE PASAMOS LOS
                    //VALORES DE LA SESI??N SELECCIONADA
                    Fragment_Transaction_Session fragmentDialogTransactionSession = new Fragment_Transaction_Session(sesion);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.LLFragment, fragmentDialogTransactionSession).addToBackStack(null).commit();
                    //POENEMOS EL BOOLEANO QUE CONTROLA QUE UNA VEZ IDO AL FRAGMENT DE LA SESI??N SE VUELVA AL HIST??RICO A TRUE
                    WorkpodActivity.boolfolder = true;
                    WorkpodActivity.boolLoc = true;
                }
            });

            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
        //M??TODOS

        /**
         * M??todo para calcular la diferencia entre 2 fechas.
         * La clase ZoneDateTime no posee el m??todo getTime() el cual permite calcular los milisegundos entre dos fechas,
         * es el m??todo m??s f??cil para calcular el tiempo entre 2 fechas. Para ello, parseamos nuestras fechas a Date y
         * aplicalamos los calculos respectivos para transformar las diferencias de milisegundos entre 2 fechas en horas, minutos y segundos
         *
         * @param fechaEntrada fecha de entrada al workpod
         * @param fechaSalida  fecha de salida del workpod
         * @param hour         horas que ha durado la sesi??n de workpod
         * @param min          minutos que ha durado la sesi??n de workpod
         * @param seg          segundos que ha durado la sesi??n de workpod
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
            this.seg = seg;
            //AGREGAMOS EL TIEMPO DE SESI??N DE TRABAJO EN UN WORKPOD DEL USUARIO

        }
    }

    @Override
    public void onDestroy() {
        if (InfoApp.USER != null) {
            try {
                if (InfoApp.USER.getReserva().getEstado().equalsIgnoreCase("En Uso")) {
                    WorkpodActivity.boolfolder = false;
                    WorkpodActivity.boolSession = true;
                    WorkpodActivity.btnNV.getMenu().findItem(R.id.inv_location).setChecked(true);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}