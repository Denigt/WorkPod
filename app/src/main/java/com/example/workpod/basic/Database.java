package com.example.workpod.basic;

import java.util.List;

public class Database {

    /**
     * Almacena los nombres de las tablas de las bases de datos
     */
    public final static String[] TABLAS = {};
    /**
     * Indica el tipo de base de datos que se esta utilizando
     *  0 MySQL
     *  1 My SQL Azure
     * -1 SQLite (SOLO PRUEBAS)
     */
    private int tipoDB;

    /**
     * Realiza un select de una tabla retornando todos los datos de la misma
     * @param tabla Tabla a la que se le realiza la consulta
     * @return Lista con los valores retornados
     */
    public static List select(String tabla){
        List retorno = null;
        // realizacion del select aqui
        return retorno;
    }

    /**
     * Realiza un select de una tabla retornando los datos que cumplan la condicion
     * @param tabla Tabla a la que se le realiza la consulta
     * @param where Condicion de la consulta
     * @return Lista con los valores retornados
     */
    public static List select(String tabla, String where){
        List retorno = null;
        // realizacion del select aqui
        return retorno;
    }

    /**
     * Inserta el objeto en la tabla que le corresponda
     * @param obj Objeto a insertar
     * @return true si se ha podido ingresar el objeto
     */
    public static boolean insert(Object obj){
        boolean retorno = false;
        // realizacion del insert aqui
        return retorno;
    }

    /**
     * Actualiza el objeto en la tabla que le corresponda
     * @param obj Objeto a actualizar
     * @return true si se ha podido actualizar el objeto
     */
    public static boolean update(Object obj){
        boolean retorno = false;
        // realizacion del update aqui
        return retorno;
    }

    /**
     * Busca y elimina el objeto en la tabla que le corresponda
     * @param obj Objeto a borrar
     * @return true si se ha podido eliminar el objeto
     */
    public static boolean delete(Object obj){
        boolean retorno = false;
        // realizacion del delete aqui
        return retorno;
    }

    /**
     * Elimina el los registros de las tablas que cumplan la condicion
     * @param tabla Tabla a la que se le realiza el delete
     * @param where Condicion del delete
     * @return true si se han podido eliminar los registros
     */
    public static boolean delete(String tabla, String where){
        boolean retorno = false;
        // realizacion del delete aqui
        return retorno;
    }
}
