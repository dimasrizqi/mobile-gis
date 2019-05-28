package id.gis.collection.ui.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import id.gis.collection.R;
import id.gis.collection.api.RestClient;
import id.gis.collection.model.SignUp.ResponseSignUp;
import id.gis.collection.utils.AppConstant;
import id.gis.collection.utils.IqbalUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrackingService extends Service {
    private static final String TAG = TrackingService.class.getSimpleName();

    public TrackingService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        requestLocation();
    }

    private void requestLocation() {
        LocationRequest request = new LocationRequest();
        request.setInterval(900000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED){
            client.requestLocationUpdates(request, new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if(IqbalUtils.readPreference(TrackingService.this, "online", "").equals("1")){
                    String idUser = IqbalUtils.readPreference(getApplicationContext(), "id", "");
                    Call<ResponseSignUp> call = RestClient.get().updatePosition(AppConstant.API_KEY, idUser, location.getLongitude(), location.getLatitude());
                    call.enqueue(new Callback<ResponseSignUp>() {
                        @Override
                        public void onResponse(Call<ResponseSignUp> call, Response<ResponseSignUp> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseSignUp> call, Throwable t) {

                        }
                    });
                }
                }
            }, null);
        }
    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            channelId = createChannel();
        }else{
            channelId = "";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.tracking_enabled_notif))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.tracking_enabled);
        startForeground(1, builder.build());
    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "snap map fake location ";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel("snap map channel", name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "snap map channel";
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
