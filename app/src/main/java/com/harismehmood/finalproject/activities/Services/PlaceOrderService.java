package com.harismehmood.finalproject.activities.Services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.harismehmood.finalproject.activities.common.shopping_cart;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class PlaceOrderService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //get the local time of the user and track the time of the order after 1 minutes generate a notification to the user

        //get location of the user and track the location of the user after 1 minutes generate a notification to the user

        //get local time of the sytem in seconds

        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                  Log.d("seconds remaining: ", String.valueOf(millisUntilFinished / 1000));
                  }

            @Override
            public void onFinish() {
                //generate a notification to the user
                    //generate notification
                    String title = "Hurry Up";
                    String message = "Its been one minute long. Your food is waiting in your cart. Place your order now!";
                    JSONObject json = null;


                    String playerId= OneSignal.getDeviceState().getUserId();
                    try {
// json=new JSONObject("{'contents':{'en':'vdvdvdvd'}, 'headings':{'en':'vdvdvdvd'}, 'include_player_ids':['f81724c8-1e00-4de4-9766-ecc40a62bb84']}");
                        json= new JSONObject("{'contents': {'en':'" +
                                message +
                                "'}, 'headings':{'en':'"
                                +title+
                                "'}, 'include_player_ids': ['" +
                                playerId + "']}");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    OneSignal.postNotification(json, new OneSignal.PostNotificationResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            Log.d("response-success",jsonObject.toString());
                        }
                        @Override
                        public void onFailure(JSONObject jsonObject) {
                            Log.d("response-failure",jsonObject.toString());
                        }
                    });
                }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
