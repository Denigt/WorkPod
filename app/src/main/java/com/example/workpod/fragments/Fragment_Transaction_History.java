package com.example.workpod.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

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

import com.example.workpod.R;
import com.example.workpod.adapters.Adaptador_Spinner;
import com.example.workpod.otherclass.LsV_Transaction_History;
import com.example.workpod.otherclass.Spinner_Years_Transaction_History;

import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
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
    List<String> monthList;//ALMACENAMOS EL MES (TITULOS DEL ELSV)
    HashMap<String, List<LsV_Transaction_History>> itemList;//ALMACENAMOS LA SESIÓN (ITEMS DEL ELSV)

    //LIST
    List<LsV_Transaction_History> lstTransaction = new ArrayList<>();//LAS SESIONES DE WORKPOD


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__transaction__history, container, false);
        //INICIALIZAMOS LOS ELEMENTOS DEL XML
        eLsV = (ExpandableListView) view.findViewById(R.id.eLsV2);
        spinnerYears = (Spinner) view.findViewById(R.id.SpinnerYears2);

        //VOLCAMOS LAS SESIONES DE LA BD. POR AHORA TRABAJAMOS EN LOCAL
        lstSesiones();

        //SPINNER
        montandoSpinner(view, lstTransaction, i, lstYears, lstSpinner);

        //PARA ACTIVAR EL MENU EMERGENTE
        setHasOptionsMenu(true);

        return view;
    }


    //MÉTODOS

    /**
     * EN ESTE MÉTODO CREAMOS UNA LISTA CON TODAS LAS SESIONES DEL USUARIO ALMACENADAS EN LA BD
     * POR AHORA ES LOCAL PERO EN UNOS DÍAS SERÁ UN SELECTALL DE LA BD
     *
     * @return
     */
    public List<LsV_Transaction_History> lstSesiones() {

        //2021
        lstTransaction.add(new LsV_Transaction_History(0, "C/Pablo Neruda,2", ZonedDateTime.of(2021, Month.JULY.getValue(), 01, 9, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.JULY.getValue(), 01, 14, 30, 22, 0, ZoneId.systemDefault()), Double.parseDouble("28"), "40"));
        lstTransaction.add(new LsV_Transaction_History(1, "C/Anaxágoras ,84", ZonedDateTime.of(2021, Month.JULY.getValue(), 23, 15, 45, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.JULY.getValue(), 23, 19, 07, 02, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(2, "C/Rafael Alberti,7", ZonedDateTime.of(2021, Month.JULY.getValue(), 29, 10, 05, 22, 0, ZoneId.systemDefault())
                , ZonedDateTime.of(2021, Month.JULY.getValue(), 29, 12, 27, 33, 0, ZoneId.systemDefault()), Double.parseDouble("25"), "40"));
        lstTransaction.add(new LsV_Transaction_History(3, "C/Arquímides,18", ZonedDateTime.of(2021, Month.AUGUST.getValue(), 9, 16, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.AUGUST.getValue(), 9, 19, 23, 22, 0, ZoneId.systemDefault()), Double.parseDouble("17"), "Sin ofertas"));
        lstTransaction.add(new LsV_Transaction_History(4, "C/Alejandro Magno,33", ZonedDateTime.of(2021, Month.AUGUST.getValue(), 15, 23, 45, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.AUGUST.getValue(), 16, 03, 01, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(5, "C/Aristóteles ,66", ZonedDateTime.of(2021, Month.SEPTEMBER.getValue(), 07, 17, 05, 52, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.SEPTEMBER.getValue(), 07, 19, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(6, "C/Albert Einstein,27", ZonedDateTime.of(2021, Month.SEPTEMBER.getValue(), 13, 7, 05, 22, 0, ZoneId.systemDefault())
                , ZonedDateTime.of(2021, Month.SEPTEMBER.getValue(), 13, 9, 15, 22, 0, ZoneId.systemDefault()), Double.parseDouble("14"), "40"));
        lstTransaction.add(new LsV_Transaction_History(7, "C/Mozart,15", ZonedDateTime.of(2021, Month.SEPTEMBER.getValue(), 17, 18, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.SEPTEMBER.getValue(), 17, 19, 12, 22, 0, ZoneId.systemDefault()), Double.parseDouble("1.20"), "40"));
        lstTransaction.add(new LsV_Transaction_History(8, "C/Chopin,2", ZonedDateTime.of(2021, Month.OCTOBER.getValue(), 15, 14, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.OCTOBER.getValue(), 15, 19, 28, 22, 0, ZoneId.systemDefault()), Double.parseDouble("13"), "40"));
        lstTransaction.add(new LsV_Transaction_History(9, "C/Chaikoski,47", ZonedDateTime.of(2021, Month.OCTOBER.getValue(), 21, 20, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.OCTOBER.getValue(), 21, 22, 41, 22, 0, ZoneId.systemDefault()), Double.parseDouble("8"), "40"));
        lstTransaction.add(new LsV_Transaction_History(10, "C/Pitágoras,73", ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 18, 19, 25, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 19, 8, 15, 12, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(11, "C/Sófocles,48", ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 21, 19, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 21, 19, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(12, "C/Antonio Machado,98", ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 24, 11, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 24, 13, 59, 22, 0, ZoneId.systemDefault()), Double.parseDouble("2.95"), "40"));
        lstTransaction.add(new LsV_Transaction_History(13, "C/Stan Lee,23", ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 30, 19, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.NOVEMBER.getValue(), 30, 19, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(14, "Avnd Sócrates,41", ZonedDateTime.of(2021, Month.DECEMBER.getValue(), 05, 12, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.DECEMBER.getValue(), 05, 19, 18, 22, 0, ZoneId.systemDefault()), Double.parseDouble("9.63"), "40"));
        lstTransaction.add(new LsV_Transaction_History(15, "C/Platón,85", ZonedDateTime.of(2021, Month.DECEMBER.getValue(), 14, 19, 55, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2021, Month.DECEMBER.getValue(), 14, 21, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));

        //2022
        lstTransaction.add(new LsV_Transaction_History(16, "C/Gabriel García Marquez,2", ZonedDateTime.of(2022, Month.JANUARY.getValue(), 01, 9, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.JANUARY.getValue(), 01, 14, 30, 22, 0, ZoneId.systemDefault()), Double.parseDouble("28"), "40"));
        lstTransaction.add(new LsV_Transaction_History(17, "C/Anaxágoras ,84", ZonedDateTime.of(2022, Month.JANUARY.getValue(), 23, 15, 45, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.JANUARY.getValue(), 23, 19, 07, 02, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(18, "C/Rafael Alberti,7", ZonedDateTime.of(2022, Month.FEBRUARY.getValue(), 27, 10, 05, 22, 0, ZoneId.systemDefault())
                , ZonedDateTime.of(2022, Month.FEBRUARY.getValue(), 27, 12, 27, 33, 0, ZoneId.systemDefault()), Double.parseDouble("25"), "40"));
        lstTransaction.add(new LsV_Transaction_History(19, "C/Arquímides,18", ZonedDateTime.of(2022, Month.MARCH.getValue(), 9, 16, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.MARCH.getValue(), 9, 19, 23, 22, 0, ZoneId.systemDefault()), Double.parseDouble("17"), "Sin ofertas"));
        lstTransaction.add(new LsV_Transaction_History(20, "C/Alejandro Magno,33", ZonedDateTime.of(2022, Month.FEBRUARY.getValue(), 15, 23, 45, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.FEBRUARY.getValue(), 16, 03, 01, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(21, "C/Aristóteles ,66", ZonedDateTime.of(2022, Month.MARCH.getValue(), 07, 17, 05, 52, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.MARCH.getValue(), 07, 19, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(22, "C/Albert Einstein,27", ZonedDateTime.of(2022, Month.APRIL.getValue(), 13, 7, 05, 22, 0, ZoneId.systemDefault())
                , ZonedDateTime.of(2022, Month.APRIL.getValue(), 13, 9, 15, 22, 0, ZoneId.systemDefault()), Double.parseDouble("14"), "40"));
        lstTransaction.add(new LsV_Transaction_History(23, "C/Mozart,15", ZonedDateTime.of(2022, Month.MAY.getValue(), 17, 18, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.MAY.getValue(), 17, 19, 12, 22, 0, ZoneId.systemDefault()), Double.parseDouble("1.20"), "40"));
        lstTransaction.add(new LsV_Transaction_History(24, "C/Chopin,2", ZonedDateTime.of(2022, Month.APRIL.getValue(), 15, 14, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.APRIL.getValue(), 15, 19, 28, 22, 0, ZoneId.systemDefault()), Double.parseDouble("13"), "40"));
        lstTransaction.add(new LsV_Transaction_History(25, "C/Chaikoski,47", ZonedDateTime.of(2022, Month.MAY.getValue(), 21, 20, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.MAY.getValue(), 21, 22, 41, 22, 0, ZoneId.systemDefault()), Double.parseDouble("8"), "40"));
        lstTransaction.add(new LsV_Transaction_History(26, "C/Pitágoras,73", ZonedDateTime.of(2022, Month.JUNE.getValue(), 18, 19, 25, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.JUNE.getValue(), 19, 8, 15, 12, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(27, "C/Sófocles,48", ZonedDateTime.of(2022, Month.JUNE.getValue(), 21, 19, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.JUNE.getValue(), 21, 19, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(28, "C/Antonio Machado,98", ZonedDateTime.of(2022, Month.JUNE.getValue(), 24, 11, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.JUNE.getValue(), 24, 13, 59, 22, 0, ZoneId.systemDefault()), Double.parseDouble("2.95"), "40"));
        lstTransaction.add(new LsV_Transaction_History(29, "C/Stan Lee,23", ZonedDateTime.of(2022, Month.APRIL.getValue(), 30, 19, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.APRIL.getValue(), 30, 19, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(30, "Avnd Sócrates,41", ZonedDateTime.of(2022, Month.MAY.getValue(), 05, 12, 05, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.MAY.getValue(), 05, 19, 18, 22, 0, ZoneId.systemDefault()), Double.parseDouble("9.63"), "40"));
        lstTransaction.add(new LsV_Transaction_History(31, "C/Platón,85", ZonedDateTime.of(2022, Month.MARCH.getValue(), 14, 19, 55, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.MARCH.getValue(), 14, 21, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        lstTransaction.add(new LsV_Transaction_History(15, "C/Platón,85", ZonedDateTime.of(2022, Month.DECEMBER.getValue(), 14, 19, 55, 22, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2022, Month.DECEMBER.getValue(), 14, 21, 45, 22, 0, ZoneId.systemDefault()), Double.parseDouble("22"), "40"));
        return lstTransaction;
    }

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

    public void montandoSpinner(View view, List<LsV_Transaction_History> lstTransaction, int i, List<String> lstYears, List<Spinner_Years_Transaction_History> lstSpinner) {
        //RECORREMOS LAS SESIONES
        for (LsV_Transaction_History transaction : lstTransaction) {
            //SI LA LISTA DE AÑOS NO CONTIENE UN AÑO EN EL QUE SE HA REALIZADO UNA SESIÓN, QUE LO AÑADA A LA LISTA
            if (!lstYears.contains(String.valueOf(transaction.getFechaEntrada().getYear()))) {
                //AÑADIMOS EL AÑOS A LA LSTYEAR
                lstYears.add(String.valueOf(transaction.getFechaEntrada().getYear()));
                i++;
                //AÑADIMOS EL CÓDIGO Y EL AÑO AL SPINNER
                lstSpinner.add(new Spinner_Years_Transaction_History((i + 1), String.valueOf(transaction.getFechaEntrada().getYear())));
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
                for (LsV_Transaction_History transaction : lstTransaction) {
                    //SI EL AÑO DEL SPINNER APUNTA AL AÑO EN EL QUE SE HA REALIZADO LA SESIÓN
                    if (spinner.getTitulo().equals(String.valueOf(transaction.getFechaEntrada().getYear()))) {
                        //LE PASAMOS DICHO AÑO AL ELSV
                        construyendoELsV(transaction.getFechaEntrada().getYear());
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
     * @param year      AÑO SELECCIONADO EN EL SPINNER

     */
    private void construyendoELsV(int year) {
        //INICIALIZAMOS LA LISTA DE MESES
        monthList = new ArrayList<String>();
        //INICIALIZAMOS LA LISTA DE SESIONES
        itemList = new HashMap<String, List<LsV_Transaction_History>>();
        //METEMOS EN LOS TITULOS DEL ELSV LOS MESES
        for (LsV_Transaction_History transaction : lstTransaction) {
            //SI EL AÑO DE LA SESIÓN APUNTA AL AÑO QUE HEMOS SELECCIONADO EN EL SPINNER
            if (String.valueOf(transaction.getFechaEntrada().getYear()).equals(String.valueOf(year))) {
                //SI LA LISTA DE MESES NO CONTIENE YA ESE MES (EN EL IDIOMA EN EL QUE ESTEMOS TRABAJANDO) LO AÑADIMOS A LA LIST DE MESES
                if (!monthList.contains(transaction.getFechaEntrada().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase())) {
                    //AÑADIMOS EL MES A LA LISTA DE MESES
                    monthList.add(transaction.getFechaEntrada().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
                }
            }

        }


        // RECORRO LA LISTA DE MESES
        for (String meses : monthList) {
            //CREAMOS AQUÍ LA LISTA DONDE IRÁN LAS SESIONE PARA QUE SE APLIQUEN LOS FILTROS
            List<LsV_Transaction_History> lstSesion = new ArrayList<>();
            //RECORRO LA LISTA DE SESIONES
            for (LsV_Transaction_History transaction : lstTransaction) {
                //SI EL MES A PUNTA AL MES DE LA SESIÓN (EN EL IDIOMA EN EL QUE ESTEMOS TRABAJANDO)
                if (meses.equals(transaction.getFechaEntrada().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase())) {
                    //SI EL AÑO DE LA SESIÓN APUNTA AL AÑO QUE HEMOS SELECCIONADO EN EL SPINNER
                    if (String.valueOf(transaction.getFechaEntrada().getYear()).equals(String.valueOf(year))) {
                        //AÑADIMOS A LA LISTA LA SESIÓN
                        lstSesion.add(new LsV_Transaction_History(transaction.getCodigo(), transaction.getUbicacion(), transaction.getFechaEntrada(), transaction.getFechaSalida(), transaction.getPrecio(), transaction.getOferta()));
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
        private HashMap<String, List<LsV_Transaction_History>> items_ELsV;

        //VARIABLES PARA EL CÁLCULO DEL TIEMPO DE SESIÓN DEL USUARIO EN WORKPOD
        private int hour;
        private int min;
        private int seg;

        //CONSTRUCTOR CON TODOS LOS PARÁMETROS
        public Adapter_ELsV(Context context, List<String> chapterList, HashMap<String, List<LsV_Transaction_History>> items_ELsV) {
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
            final LsV_Transaction_History lsVTransactionHistory = (LsV_Transaction_History) getChild(groupPosition, childPosition);
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
            tVUbicacion.setText(lsVTransactionHistory.getUbicacion());

            //LE PASAMOS LA INFORMACIÓN DE LAS SESIONES
            //PARA ELLO UTILIZAMOS EL OBJETO DE LA CLASE LSV AL QUE LE HEMOS ASOCIADO LA DEVOLUCIÓN DE LOS ITEMS

            //APLICAMOS EL MÉTODO DE CALCULAR EL TIEMPO DE SESIÓN DEL USUARIO
            calcularTiempoSesion(lsVTransactionHistory.getFechaEntrada(), lsVTransactionHistory.getFechaSalida(), hour, min
                    , seg);
            tVFecha.setText(String.valueOf(lsVTransactionHistory.getFechaEntrada().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
            tVTiempo.setText(hour + "h:" + min + "min");

            //CREAMOS LA CONDICIÓN DE QUE AL TOCAR EN UN ITEM DEL ELSV SE ABRA EL CUADRO DE DIALOGO CON TODA LA INFORMACIÓN DE LA SESIÓN

            //CREO UN OBJETO LAYOUT PARA QUE AL TOCAR EL ITEM DEL ELSV SE ME ABRA EL DIALOGO EMERGENTE
            LinearLayout layout = view.findViewById(R.id.LLSesion);
            //CON EL EVENTO setOnClickListener SE ME ABRIRÁ EL DIÁLOGO EMERGENTE AL TOCAR EL ITEM
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //CREAMOS UN OBJETO DEL FRAGMENT QUE HACE DE DIALOGO EMERGENTE Y EN SU CONSTRUCTOR A TRAVÉS DEL OBJETO LSV LE PASAMOS LOS
                    //VALORES DE LA SESIÓN SELECCIONADA
                    Fragment_Dialog_Transaction_Session fragmentDialogTransactionSession = new Fragment_Dialog_Transaction_Session(lsVTransactionHistory.getUbicacion(), lsVTransactionHistory.getFechaEntrada(),
                            lsVTransactionHistory.getFechaSalida(), lsVTransactionHistory.getOferta(), lsVTransactionHistory.getPrecio());
                    fragmentDialogTransactionSession.show(myContext.getSupportFragmentManager(), "DialogToCall");
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