package com.workpodapp.workpod;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Esta es la clase que será capaz de recibir las notificaciones y mediante la cual podremos manejarlas a nuestro antojo
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification().getBody() != null) {
            Looper.prepare();
            Log.i("INFORMATION", "RECEIVED MESSAGE: " + remoteMessage.getNotification().getBody());
            String texto= remoteMessage.getData().get("url");
            String asunto= remoteMessage.getNotification().getTitle();
          //  Toast.makeText(getBaseContext(),asunto,Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(),remoteMessage.getNotification().getBody(),Toast.LENGTH_LONG).show();
            Looper.loop();
        }


    }
}
