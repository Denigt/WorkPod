package com.example.workpod.basic;

import com.example.workpod.data.*;

/**
 * Clase para acceder a datos compartidos entre distintas actividades
 * QUEDA PROHIBIDO INTRODUCIR INFORMACION CONFIDENCIAL EN ESTA CLASE (Tarjetas, contrasenas, ubicacion de usuarios, datos de facturacion...)
 */
public abstract class InfoApp {
    /**
     * Usuario que ha iniciado sesion
     * null si no se ha iniciado sesion
     */
    public static Usuario USER;

    /**
     * Reserva que ha realizado el usuario
     * null si no ha realizado ninguna reserva
     */
    public static Reserva RESERVA;

    public static Sesion sesion;

    /**
     * Workpod abierto por el usuario
     * null si no ha abierto ningun Workpod
     */
    public static Workpod WORKPOD;

    static {
        USER = null;
        RESERVA = null;
        WORKPOD = null;
    }

}
