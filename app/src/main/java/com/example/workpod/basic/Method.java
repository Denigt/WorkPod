package com.example.workpod.basic;

import android.content.Context;
import android.widget.Toast;

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
}
