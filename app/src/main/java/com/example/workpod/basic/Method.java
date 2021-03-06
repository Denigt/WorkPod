package com.example.workpod.basic;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.workpod.scale.Scale_Buttons;
import com.example.workpod.scale.Scale_Image_View;
import com.example.workpod.scale.Scale_TextView;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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
     * Esclaremos los btns del XML teniendo en cuenta la densidad de pixeles del móvil para que el widht y el height que se cojan no sean los
     * absolutos, sino los reales.
     *
     * @param metrics objeto de la clase metrics, servirá para coger los parámetros del dispositivo en el que se encuentre la app.
     */
    public static void scaleBtns(DisplayMetrics metrics, List<Scale_Buttons> lstBtns) {
        //LA CLASE DISPLAYMETRICS NOS PERMITIRÁ COGER LOS PARÁMETROS FÍSICOS DE MÓVILES Y EMULADORES
        //COGEMOS SU ANCHO Y ALTO ABSOLUTO Y LO TRANSFORMAMOS EN REAL
        float width = metrics.widthPixels / metrics.density; // ancho absoluto en pixels
        float height = metrics.heightPixels / metrics.density; // alto absoluto en pixels

        for (Scale_Buttons btn : lstBtns) {
            //DEFINIMOS TAMAÑO FUENTE
            if ((width <= (1200 / metrics.density)) && (width > (750 / metrics.density))) {
                btn.getButton().setTextSize(btn.getSizeBig());
                //DEFINIMOS ANCHO DEL BTN
                if (btn.getWidth().trim().equalsIgnoreCase("MATCH_PARENT") && btn.getWidhtBig() == 0) {
                    btn.getButton().getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                } else if (btn.getWidth().trim().equalsIgnoreCase("WRAP_CONTENT") && btn.getWidhtBig() == 0) {
                    btn.getButton().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else {
                    btn.getButton().getLayoutParams().width = btn.getWidhtBig();
                }
            } else if ((width <= (750 / metrics.density)) && (width > (550 / metrics.density))) {
                btn.getButton().setTextSize(btn.getSizeMiddle());
                //DEFINIMOS ANCHO DEL BTN
                if (btn.getWidth().trim().equalsIgnoreCase("MATCH_PARENT") && btn.getWidhtMiddle() == 0) {
                    btn.getButton().getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                } else if (btn.getWidth().trim().equalsIgnoreCase("WRAP_CONTENT") && btn.getWidhtMiddle() == 0) {
                    btn.getButton().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else {
                    btn.getButton().getLayoutParams().width = btn.getWidhtMiddle();
                }
            } else if (width <= (550 / metrics.density)) {
                btn.getButton().setTextSize(btn.getSizeLittle());
                //DEFINIMOS ANCHO DEL BTN
                if (btn.getWidth().trim().equalsIgnoreCase("MATCH_PARENT") && btn.getWidhtLittle() == 0) {
                    btn.getButton().getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                } else if (btn.getWidth().trim().equalsIgnoreCase("WRAP_CONTENT") && btn.getWidhtLittle() == 0) {
                    btn.getButton().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else {
                    btn.getButton().getLayoutParams().width = btn.getWidhtLittle();
                }
            }

            //APLICAMOS EL ESTILO CORRESPONDIENTE
            if (btn.getStyle().trim().equalsIgnoreCase("bold")) {
                btn.getButton().setTypeface(null, Typeface.BOLD);
            } else if (btn.getStyle().trim().equalsIgnoreCase("italic")) {
                btn.getButton().setTypeface(null, Typeface.ITALIC);
            } else if (btn.getStyle().trim().equalsIgnoreCase("normal")) {
                btn.getButton().setTypeface(null, Typeface.NORMAL);
            } else if (btn.getStyle().trim().equalsIgnoreCase("bold_italic")) {
                btn.getButton().setTypeface(null, Typeface.BOLD_ITALIC);
            }
        }
    }

    /**
     * escalaremos los tv del XML teniendo en cuenta la densidad de pixeles del móvil para que el widht y el height que se cojan no sean los
     * absolutos, sino los reales.
     *
     * @param metrics objeto de la clase metrics, servirá para coger los parámetros del dispositivo en el que se encuentre la app.
     */
    public static void scaleTv(DisplayMetrics metrics, List<Scale_TextView> lstTv) {
        //LA CLASE DISPLAYMETRICS NOS PERMITIRÁ COGER LOS PARÁMETROS FÍSICOS DE MÓVILES Y EMULADORES
        //COGEMOS SU ANCHO Y ALTO ABSOLUTO Y LO TRANSFORMAMOS EN REAL
        float width = metrics.widthPixels / metrics.density; // ancho absoluto en pixels
        float height = metrics.heightPixels / metrics.density; // alto absoluto en pixels

        for (Scale_TextView tv : lstTv) {
            //DEFINIMOS TAMAÑO FUENTE
            if ((width <= (1200 / metrics.density)) && (width > (750 / metrics.density))) {
                tv.getTextView().setTextSize(tv.getSizeBig());
                //DEFINIMOS ANCHO DEL TXT
                if (tv.getWidht().trim().equalsIgnoreCase("MATCH_PARENT") && tv.getWidhtBig() == 0) {
                    tv.getTextView().getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                } else if (tv.getWidht().trim().equalsIgnoreCase("WRAP_CONTENT") && tv.getWidhtBig() == 0) {
                    tv.getTextView().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else if (tv.getWidht().trim().equalsIgnoreCase("N")) {
                    tv.getTextView().getLayoutParams().width = tv.getWidhtBig();
                    tv.getTextView().getLayoutParams().height = tv.getHeightBig();
                }
            } else if ((width <= (750 / metrics.density)) && (width > (550 / metrics.density))) {
                tv.getTextView().setTextSize(tv.getSizeMiddle());
                //DEFINIMOS ANCHO DEL TXT
                if (tv.getWidht().trim().equalsIgnoreCase("MATCH_PARENT") && tv.getWidhtMiddle() == 0) {
                    tv.getTextView().getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                } else if (tv.getWidht().trim().equalsIgnoreCase("WRAP_CONTENT") && tv.getWidhtMiddle() == 0) {
                    tv.getTextView().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else if (tv.getWidht().trim().equalsIgnoreCase("N")) {
                    tv.getTextView().getLayoutParams().width = tv.getWidhtMiddle();
                    tv.getTextView().getLayoutParams().height = tv.getHeightMiddle();
                }
            } else if (width <= (550 / metrics.density)) {
                tv.getTextView().setTextSize(tv.getSizeLittle());
                //DEFINIMOS ANCHO DEL TXT
                if (tv.getWidht().trim().equalsIgnoreCase("MATCH_PARENT") && tv.getWidhtLittle() == 0) {
                    tv.getTextView().getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                } else if (tv.getWidht().trim().equalsIgnoreCase("WRAP_CONTENT") && tv.getWidhtLittle() == 0) {
                    tv.getTextView().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else if (tv.getWidht().trim().equalsIgnoreCase("N")) {
                    tv.getTextView().getLayoutParams().width = tv.getWidhtLittle();
                    tv.getTextView().getLayoutParams().height = tv.getHeightLittle();
                }
            }

            //APLICAMOS EL ESTILO CORRESPONDIENTE
            if (tv.getStyle().trim().equalsIgnoreCase("bold")) {
                tv.getTextView().setTypeface(null, Typeface.BOLD);
            } else if (tv.getStyle().trim().equalsIgnoreCase("italic")) {
                tv.getTextView().setTypeface(null, Typeface.ITALIC);
            } else if (tv.getStyle().trim().equalsIgnoreCase("normal")) {
                tv.getTextView().setTypeface(null, Typeface.NORMAL);
            } else if (tv.getStyle().trim().equalsIgnoreCase("bold_italic")) {
                tv.getTextView().setTypeface(null, Typeface.BOLD_ITALIC);
            }
        }

    }

    public static void scaleIv(DisplayMetrics metrics, List<Scale_Image_View> lstIv) {
        //LA CLASE DISPLAYMETRICS NOS PERMITIRÁ COGER LOS PARÁMETROS FÍSICOS DE MÓVILES Y EMULADORES
        //COGEMOS SU ANCHO Y ALTO ABSOLUTO Y LO TRANSFORMAMOS EN REAL
        float width = metrics.widthPixels / metrics.density; // ancho absoluto en pixels
        for (Scale_Image_View iV : lstIv) {
            if ((width <= (1200 / metrics.density)) && (width > (750 / metrics.density))) {
                //DEFINIMOS ANCHO DEL IV
                if (iV.getWidhtString().trim().equalsIgnoreCase("MATCH_PARENT") && iV.getWidhtBig() == 0) {
                    iV.getiV().getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                } else if (iV.getWidhtString().trim().equalsIgnoreCase("WRAP_CONTENT") && iV.getWidhtBig() == 0) {
                    iV.getiV().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else {
                    iV.getiV().getLayoutParams().width = iV.getWidhtBig();
                }
                //DEFINIMOS ALTURA DEL IV
                if (iV.getHeightString().trim().equalsIgnoreCase("MATCH_PARENT") && iV.getHeightBig() == 0) {
                    iV.getiV().getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
                } else if (iV.getHeightString().trim().equalsIgnoreCase("WRAP_CONTENT") && iV.getHeightBig() == 0) {
                    iV.getiV().getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else {
                    iV.getiV().getLayoutParams().height = iV.getHeightBig();
                }

            } else if ((width <= (750 / metrics.density)) && (width > (550 / metrics.density))) {
                //DEFINIMOS ANCHO DEL IV
                if (iV.getWidhtString().trim().equalsIgnoreCase("MATCH_PARENT") && iV.getWidhtMiddle() == 0) {
                    iV.getiV().getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                } else if (iV.getWidhtString().trim().equalsIgnoreCase("WRAP_CONTENT") && iV.getWidhtMiddle() == 0) {
                    iV.getiV().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else {
                    iV.getiV().getLayoutParams().width = iV.getWidhtMiddle();
                }
                //DEFINIMOS ALTURA DEL IV

                if (iV.getHeightString().trim().equalsIgnoreCase("MATCH_PARENT") && iV.getHeightMiddle() == 0) {
                    iV.getiV().getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
                } else if (iV.getHeightString().trim().equalsIgnoreCase("WRAP_CONTENT") && iV.getHeightMiddle() == 0) {
                    iV.getiV().getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else {
                    iV.getiV().getLayoutParams().height = iV.getHeightMiddle();
                }
            } else if (width <= (550 / metrics.density)) {
                //DEFINIMOS ANCHO DEL IV
                if (iV.getWidhtString().trim().equalsIgnoreCase("MATCH_PARENT") && iV.getWidhtLittle() == 0) {
                    iV.getiV().getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                } else if (iV.getWidhtString().trim().equalsIgnoreCase("WRAP_CONTENT") && iV.getWidhtLittle() == 0) {
                    iV.getiV().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else {
                    iV.getiV().getLayoutParams().width = iV.getWidhtLittle();
                }
                //DEFINIMOS ALTURA DEL IV
                if (iV.getHeightString().trim().equalsIgnoreCase("MATCH_PARENT") && iV.getHeightLittle() == 0) {
                    iV.getiV().getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                } else if (iV.getHeightString().trim().equalsIgnoreCase("WRAP_CONTENT") && iV.getHeightLittle() == 0) {
                    iV.getiV().getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else {
                    iV.getiV().getLayoutParams().height = iV.getHeightLittle();
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
                fecha.getYear(), fecha.getMonthValue(), fecha.getDayOfMonth(), fecha.getHour(), fecha.getMinute(), fecha.getSecond());
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

    /**
     * Cifra la cadena pasada como parametro en MD5
     * @param input Cadena a crifrar
     * @return Cadena cifrada
     */
    public static String md5(String input) {
        String hashtext = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("CIFRADO MD5", "No se ha encontrado el algoritmo de cifrado");
        }

        return hashtext;
    }

    /**
     * Encripta la entrada usando como clave la key pasada como parametro
     * @param input Texto a encriptar
     * @param key Clave a usar
     * @return Texto encriptado (En bytes)
     */
    public static byte[] encryptAES(String input, String key){
        try{
            // Creamos una clave de encriptado unica a traves de la pasada por el usuario
            SecretKey pass = new SecretKeySpec(md5(key).getBytes(),  0, 32, "AES");

            // Se obtiene un cifrador AES
            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

            // Se inicializa para encriptacion y se encripta el texto (Texto pasado a bytes)
            aes.init(Cipher.ENCRYPT_MODE, pass);
            byte[] encriptado = aes.doFinal(input.getBytes());

            // Se iniciliza el cifrador para desencriptar, la clave debe ser la misma con la que se encripto
            aes.init(Cipher.DECRYPT_MODE, pass);
            byte[] desencriptado = aes.doFinal(encriptado);
            return encriptado;
        }catch (NoSuchAlgorithmException e){
            Log.e("CIFRADO AES", "No se ha encontrado el algoritmo de cifrado");
        }catch (NoSuchPaddingException | BadPaddingException e) {
            Log.e("CIFRADO AES", "No tengo muy claro porque salta esta excepcion");
        }catch (InvalidKeyException e) {
            Log.e("CIFRADO AES", "La clave indicada no es valida");
        }catch (IllegalBlockSizeException e) {
            Log.e("CIFRADO AES", "Error con el tamano del texto a encriptar");
        }
        return null;
    }

    /**
     * Desencripta la entrada usando como clave la key pasada como parametro
     * @param input bytes a desencriptar
     * @param key Clave a usar
     * @return Texto desencriptado
     */
    public static String decryptAES(byte[] input, String key){
        try{
            // Creamos una clave de encriptado unica a traves de la pasada por el usuario
            SecretKey pass = new SecretKeySpec(md5(key).getBytes(),  0, 32, "AES");

            // Se obtiene un cifrador AES
            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

            // Se inicializa el cifrador para desencriptar, la clave debe ser la misma con la que se encripto
            aes.init(Cipher.DECRYPT_MODE, pass);
            byte[] desencriptado = aes.doFinal(input);

            return new String(desencriptado);
        }catch (NoSuchAlgorithmException e){
            Log.e("CIFRADO AES", "No se ha encontrado el algoritmo de cifrado");
        }catch (NoSuchPaddingException | BadPaddingException e) {
            Log.e("CIFRADO AES", "No tengo muy claro porque salta esta excepcion");
        }catch (InvalidKeyException e) {
            Log.e("CIFRADO AES", "La clave indicada no es valida");
        }catch (IllegalBlockSizeException e) {
            Log.e("CIFRADO AES", "Error con el tamano del texto a encriptar");
        }
        return null;
    }
}
