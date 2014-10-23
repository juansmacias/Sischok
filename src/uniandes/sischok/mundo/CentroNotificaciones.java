package uniandes.sischok.mundo;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class CentroNotificaciones extends IntentService
{

	public static Context context;
	
	public CentroNotificaciones() 
	{
		super("Centro notificaciones");
	}

	@Override
	protected void onHandleIntent(Intent intent) 
	{
//		super.onCreate();
		
//     	NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	//para que al notificar abra la app en la pagina principal
//    	Intent notificationIntent = new Intent (context, Inicio.class );
//    	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//    	            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    	// prepare intent which is triggered if the notification is selected
//    	PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
//    	Notification n  = new Notification.Builder(this)
//        .setContentTitle("Revisar Incidentes de la zona")
//        .setContentText("Evento: ")
//        .setSmallIcon(R.drawable.icono)
//        .setContentIntent(pIntent)
//        .setAutoCancel(true).build();
//   NO     .addAction(R.drawable.icon, "Call", pIntent)
//     NO   .addAction(R.drawable.icon, "More", pIntent)
//       NO .addAction(R.drawable.icon, "And more", pIntent).build();
//    		Notification n = builder.build();A
//    	notificationManager.notify(0, n);

    	//easy way
//    	Toast.makeText(getApplicationContext(), 
//                "Revisar Incidentes Sichock ", Toast.LENGTH_LONG).show();
  
	}
}
