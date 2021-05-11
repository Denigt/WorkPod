package com.example.workpod.basic;

import android.content.Context;
import android.widget.Toast;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import kotlin.jvm.Throws;

/**
 * Incorpora metodos estaticos para funciones basicas
 */
public abstract class Method {
    // Simbolos admitidos como tales en la creacion de la contrasena
    public static final char[] PASS_SYMBOLS = {'\\', '|', '!', '@', '"', '·', '#' , '~', '$', '%',
            '€', '/', '(', ')', '=', '[', ']', '?', '¿', '¡', '*', '{', '}', '-', '_', ':', '.', ';',
            '-', '+', 'º', 'ª', '<', '>'};
    //Longitud minima de la contrasena
    public static final int PASS_MINLENGHT = 10;
    //Patron para comprobar el email
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$";

    /**
     * Muestra el mensaje en la zona inferior de la pantalla
     * @param context Contexto de la actividad en la que se debe mostrar
     * @param msg Mensaje a mostrar
     */
    public static void showError(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Comprueba la validez de la contrasena
     * @param password Contrasena a validar
     * @return true si la contrasena es valida
     *         false si la contrasena es erroneo
     */
    public static boolean checkPassword(String password){
        boolean retorno = false;

        if(password.length() >= PASS_MINLENGHT){
            for(char c : PASS_SYMBOLS)
                if(password.contains(String.valueOf(c)))
                    retorno = true;
        }
        return retorno;
    }

    /**
     * Comprueba la validez del email
     * @param email Email a validar
     * @return true si el email es valida
     *         false si el email es erroneo
     */
    public static boolean checkEmail(String email){
        return email.matches(EMAIL_PATTERN);
    }

    public static ZonedDateTime stringToDate(String fecha, ZoneId zona){
        int[] dia = new int[3];
        int[] hora = new int[4];

        fecha = fecha.trim();
        if (fecha.matches("\\d{2,4}[-/]\\d\\d[-/]\\d\\d$")) {
            String[] aux = fecha.split("[-/]");
            for(int i = 0; i < aux.length; i++)
                dia[i] = Integer.parseInt(aux[i]);

            for(int i = 0; i < hora.length; i++)
                hora[i] = 0;

        }else if (fecha.matches("\\d\\d[-/]\\d\\d[-/]\\d{2,4} \\d\\d:\\d\\d:\\d\\d(:\\d\\d)?$")) {
            String[] aux = fecha.split(" ");
            String[] auxF = aux[0].split("[-/]");
            String[] auxH = aux[1].split(":");

            for(int i = 0; i < auxF.length; i++)
                dia[i] = Integer.parseInt(aux[i]);

            for(int i = 0; i < auxH.length; i++)
                hora[i] = Integer.parseInt(aux[i]);

            if(auxH.length < 4)
                hora[3] = 0;
        }else ZonedDateTime.of(0,0,0,0,0,0,0, zona);

        return ZonedDateTime.of(dia[0], dia[1], dia[2], hora[0], hora[1], hora[2], hora[3], zona);
    }
}
