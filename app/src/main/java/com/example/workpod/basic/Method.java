package com.example.workpod.basic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import kotlin.jvm.Throws;

/**
 * Incorpora metodos estaticos para funciones basicas
 */
public abstract class Method {
    // Simbolos admitidos como tales en la creacion de la contrasena
    public static final char[] PASS_SYMBOLS = {'\\', '|', '!', '@', '"', '·', '#', '~', '$', '%',
            '€', '/', '(', ')', '=', '[', ']', '?', '¿', '¡', '*', '{', '}', '-', '_', ':', '.', ';',
            '-', '+', 'º', 'ª', '<', '>'};
    //Longitud minima de la contrasena
    public static final int PASS_MINLENGHT = 10;
    //Patron para comprobar el email
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$";

    /**
     * Muestra el mensaje en la zona inferior de la pantalla
     *
     * @param context Contexto de la actividad en la que se debe mostrar
     * @param msg     Mensaje a mostrar
     */
    public static void showError(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Comprueba la validez de la contrasena
     *
     * @param password Contrasena a validar
     * @return true si la contrasena es valida
     * false si la contrasena es erroneo
     */
    public static boolean checkPassword(String password) {
        boolean retorno = false;

        if (password.length() >= PASS_MINLENGHT) {
            for (char c : PASS_SYMBOLS)
                if (password.contains(String.valueOf(c)))
                    retorno = true;
        }
        return retorno;
    }

    /**
     * Comprueba la validez del email
     *
     * @param email Email a validar
     * @return true si el email es valida
     * false si el email es erroneo
     */
    public static boolean checkEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    /**
     * Obtiene un ZonedDateTime a partir de una String
     *
     * @param fecha Fecha en formato yyyy-mm-dd hh:mm:ss:nn
     * @param zona  Zona horaria del ZonedDateTime
     * @return ZonedDateTime con fecha indicada
     */
    public static ZonedDateTime stringToDate(String fecha, ZoneId zona) {
        int[] dia = new int[3];
        int[] hora = new int[4];

        fecha = fecha.trim();
        if (fecha.matches("\\d{2,4}[-/]\\d\\d[-/]\\d\\d$")) {
            String[] aux = fecha.split("[-/]");
            for (int i = 0; i < aux.length; i++)
                dia[i] = Integer.parseInt(aux[i]);

            for (int i = 0; i < hora.length; i++)
                hora[i] = 0;

        } else if (fecha.matches("\\d{2,4}[-/]\\d\\d[-/]\\d\\d \\d\\d:\\d\\d:\\d\\d(:\\d\\d)?$")) {
            String[] aux = fecha.split(" ");
            String[] auxF = aux[0].split("[-/]");
            String[] auxH = aux[1].split(":");

            for (int i = 0; i < auxF.length; i++)
                dia[i] = Integer.parseInt(auxF[i]);

            for (int i = 0; i < auxH.length; i++)
                hora[i] = Integer.parseInt(auxH[i]);

            if (auxH.length < 4)
                hora[3] = 0;
        } else return ZonedDateTime.of(0, 0, 0, 0, 0, 0, 0, zona);

        return ZonedDateTime.of(dia[0], dia[1], dia[2], hora[0], hora[1], hora[2], hora[3], zona);
    }

    /**
     *  Esclaremos los btns del XML teniendo en cuenta la densidad de pixeles del móvil para que el widht y el height que se cojan no sean los
     *  absolutos, sino los reales.
     * @param metrics objeto de la clase metrics, servirá para coger los parámetros del dispositivo en el que se encuentre la app.
     * @param mBtnPequenos colección clave valor cuya clave son los btn y los valores su tamaño para dispositivos pequeños
     * @param mBtnGrandes colección clave valor cuya clave son los tv y los valores su tamaño para dispositivos medianos y grandes
     */
    public static void scaleButtons(DisplayMetrics metrics, Map<Button, Integer> mBtnPequenos, Map<Button, Integer> mBtnGrandes) {
        //LA CLASE DISPLAYMETRICS NOS PERMITIRÁ COGER LOS PARÁMETROS FÍSICOS DE MÓVILES Y EMULADORES

        //COGEMOS SU ANCHO Y ALTO ABSOLUTO Y LO TRANSFORMAMOS EN REAL
        float width = metrics.widthPixels / metrics.density; // ancho absoluto en pixels
        float height = metrics.heightPixels / metrics.density; // alto absoluto en pixels

        //LOS MOVILES GRANDESCOGERÁN EL VALOR DEL XML
        //MOVILES MEDIANOS

        if (mBtnGrandes != null) {
            if ((width <= (1200 / metrics.density)) && (width > (550 / metrics.density))) {
                for (Map.Entry<Button, Integer> buttons : mBtnGrandes.entrySet()) {
                    buttons.getKey().setTextSize(buttons.getValue());
                    buttons.getKey().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    buttons.getKey().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                }
            }
        }
        //MOVILES PEQUEÑOS
        if (mBtnPequenos != null) {
            if (width <= (550 / metrics.density)) {
                //CAMBIAMOS TAMAÑO TEXTO
                for (Map.Entry<Button, Integer> buttons : mBtnPequenos.entrySet()) {
                    buttons.getKey().setTextSize(buttons.getValue());
                    buttons.getKey().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    buttons.getKey().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                }
            }
        }

    }

    /**
     * escalaremos los tv del XML teniendo en cuenta la densidad de pixeles del móvil para que el widht y el height que se cojan no sean los
     * absolutos, sino los reales.
     * @param metrics objeto de la clase metrics, servirá para coger los parámetros del dispositivo en el que se encuentre la app.
     * @param mTvPequenos colección clave valor cuya clave son los tv y los valores su tamaño para dispositivos pequeños
     * @param mTvGrandes colección clave valor cuya clave son los tv y los valores su tamaño para dispositivos medianos y grandes
     */
    public static void scaleTxt(DisplayMetrics metrics, Map<TextView, Integer> mTvPequenos, Map<TextView, Integer> mTvGrandes) {
        //LA CLASE DISPLAYMETRICS NOS PERMITIRÁ COGER LOS PARÁMETROS FÍSICOS DE MÓVILES Y EMULADORES

        //COGEMOS SU ANCHO Y ALTO ABSOLUTO Y LO TRANSFORMAMOS EN REAL
        float width = metrics.widthPixels / metrics.density; // ancho absoluto en pixels
        float height = metrics.heightPixels / metrics.density; // alto absoluto en pixels

        //LOS MOVILES GRANDESCOGERÁN EL VALOR DEL XML
        //MOVILES MEDIANOS

        if (mTvGrandes != null) {
            if ((width <= (1200 / metrics.density)) && (width > (550 / metrics.density))) {
                for (Map.Entry<TextView, Integer> tv : mTvGrandes.entrySet()) {
                    tv.getKey().setTextSize(tv.getValue());
                    tv.getKey().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    tv.getKey().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                }
            }
        }
        //MOVILES PEQUEÑOS
        if (mTvPequenos != null) {
            if (width <= (550 / metrics.density)) {
                //CAMBIAMOS TAMAÑO TEXTO
                for (Map.Entry<TextView, Integer> tv : mTvPequenos.entrySet()) {
                    tv.getKey().setTextSize(tv.getValue());
                    tv.getKey().setTypeface(null,Typeface.BOLD);
                    tv.getKey().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    tv.getKey().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                }
            }
        }

    }

    /**
     * Obtiene una String en formato yyyy-mm-dd hh:mm:ss a partir de un ZonedDateTime
     *
     * @param fecha Fecha
     * @param zona  Zona horaria a la que se transformara la fecha
     * @return String con fecha indicada
     */
    public static String dateToString(ZonedDateTime fecha, ZoneId zona) {
        fecha = fecha.withZoneSameInstant(zona);
        return String.format("%04d-%02d-%02d %02d:%02d:%02d",
                fecha.getYear(), fecha.getDayOfMonth(), fecha.getMonthValue(), fecha.getHour(), fecha.getMinute(), fecha.getSecond());
    }

    /**
     * Resta a la fecha1 la fecha2 y devuelve el resultado en segundos
     * Si las fechas son anteriores a 1970 el metodo no es funcional
     *
     * @param fecha1 Fecha
     * @param fecha2 Fecha a restar
     * @return long con segundos de diferencia entre las dos fechas
     * Si la fecha2 es mayor a fecha1 se devuelve un valor negativo
     */
    public static long subsDate(ZonedDateTime fecha1, ZonedDateTime fecha2) {
        long seg1 = fecha1.toEpochSecond();
        long seg2 = fecha2.toEpochSecond();
        return seg1 - seg2;
    }
}
