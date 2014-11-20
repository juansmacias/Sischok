package uniandes.sischok.mundo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uniandes.sischok.Inicio;
import uniandes.sischok.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

public class ServicioPusher extends IntentService {

	private Pusher pusher;

	public ServicioPusher() {
		super("ServicioPusher");
	
	}

	@Override
	protected void onHandleIntent(Intent intent) {	
		pusher = new Pusher("d4faf50f8ab3dbd72a89");

		pusher.connect(new ConnectionEventListener() {
			@Override
			public void onConnectionStateChange(ConnectionStateChange change) {
				Log.i("Servicio Pusher","State changed to " + change.getCurrentState() +
						" from " + change.getPreviousState());
			}

			@Override
			public void onError(String message, String code, Exception e) {
				Log.i("Servicio Pusher","There was a problem connecting!");
			}
		}, ConnectionState.ALL);

		// Subscribe to a channel
		Channel channel = pusher.subscribe("test_channel");

		channel.bind("evento", new ChannelEventListener() {

			@Override
			public void onEvent(String channel, String event, String data) {
				Log.i("Servicio Pusher","Received event with data: " + data);
			}

			@Override
			public void onSubscriptionSucceeded(String arg0) {
				Log.i("Servicio Pusher","Se subcribio bien");
				
			}
		});
		
		channel.bind("borrachos", new ChannelEventListener() {

			@Override
			public void onEvent(String channel, String event, String data) {
				Log.i("Servicio Pusher","Received event with data: " + data);
				try {
					JSONObject arrObjeto = new JSONObject(data);
					SharedPreferences sharedpreferences = getSharedPreferences(CentroIncidentes.nombrePreferencias, Context.MODE_PRIVATE);
					if(!sharedpreferences.getString(CentroIncidentes.prefNombre, CentroIncidentes.prefNombreDefault).equals(arrObjeto.get("usuario")))
					{
						NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		            	//para que al notificar abra la app en la pagina principal
		            	Intent notificationIntent = new Intent (ServicioPusher.this.getApplicationContext(), Inicio.class );
		            	notificationIntent.putExtra("latitud", arrObjeto.getDouble("latitud"));
		            	notificationIntent.putExtra("longitud", arrObjeto.getDouble("longitud"));		            	
		            	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		            	            | Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		            	// prepare intent which is triggered if the notification is selected
		            	PendingIntent pIntent = PendingIntent.getActivity(ServicioPusher.this.getApplicationContext(), 0, notificationIntent, 0);
		            	Notification n  = new Notification.Builder(ServicioPusher.this.getApplicationContext())
		                .setContentTitle("Peligro de borracho en la zona")
		                .setContentText("Se ha detectado un borracho en el sistema. Revise la pagina de Inicio de Sischok para ver su ubicaci—n.")
		                .setSmallIcon(R.drawable.icono)
		                .setContentIntent(pIntent)
		                .setAutoCancel(true).build();
//		            		Notification n = builder.build();A
		            	notificationManager.notify(0, n);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onSubscriptionSucceeded(String arg0) {
				Log.i("Servicio Pusher","Se subcribio bien");
				
			}
		});
		
		Log.i("Servicio Pusher","termino el on create");	
	}

}
