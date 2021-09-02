package com.workpodapp.workpod.basic;

/**
 * Clase usada para almacenar recursos compartidos por distitnos hilos o interfaces
 * @param <T> Clase del recurso compartido
 */
public class Shared <T>{
    public T resource;

    public Shared(){
        resource = null;
    }

    public Shared(T resourceToShare){
        resource = resourceToShare;
    }
}
