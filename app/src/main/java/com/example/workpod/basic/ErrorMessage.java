package com.example.workpod.basic;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorMessage {
    public final int code;
    public final String message;

    public ErrorMessage(int codError, String messageError){
        this.code = codError;
        this.message =messageError;

        if (code < 0)
            Log.e("UNKNOW ERROR | ", message);
    }

    public ErrorMessage(JSONObject json){
        int auxCodError;
        String auxMessageError;
        try {
            auxCodError = json.getInt("estado");
        }catch(JSONException e){
            auxCodError = 0;
        }
        try {
            auxMessageError = json.getString("mensaje");
        }catch(JSONException e){
            auxMessageError = "Sin errores";
        }
        this.code = auxCodError;
        this.message = auxMessageError;

        if (code < 0)
            Log.e("DATABASE ERROR | ", message);
    }
}
