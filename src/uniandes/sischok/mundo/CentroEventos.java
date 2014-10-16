package uniandes.sischok.mundo;

import java.util.Date;

import uniandes.sischok.Inicio;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.Toast;

public class CentroEventos extends IntentService
{
	
	public static Context context;
		
	public CentroEventos() 
	{
		super("Centro eventos");
	}

	//-------------------Metodos---------------------------------
	
	/**
	 * Metodo que revisa cada 12 horas eventos en el calendario. 6am y 6pm
	 * @return Retorna un objeto json con la informacion del evento que se encuentra en el calendario.
	 */
	public static String darProximosEventos()
	{
		return "objeto json";
	}

	@Override
	protected void onHandleIntent(Intent intent) 
	{
		CentroEventos.context = getApplicationContext();
		
		// TODO Auto-generated method stub lo que hace cuando el servicio start
		//run query
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		
    	Long timeNow = new Date().getTime();
		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(eventsUriBuilder, timeNow);
		ContentUris.appendId(eventsUriBuilder, (timeNow +(60000 * 120)));
		Uri eventsUri = eventsUriBuilder.build();
		cur = cr.query(eventsUri, new String[]{"_id", "title", "description", "dtstart", "dtend", "eventLocation"}, null, null, CalendarContract.Instances.DTSTART + " ASC");
		
		cur.moveToFirst();
		
		//arreglo de los eventos de cada dia
        String[] CalTitle = new String[cur.getCount()];
    	//yayyy si saleeeeee
    	
        // recorre los eventos resultados de la consulta para notificar unicamente los que van a suceder en la proxima hora y 59 min
        for (int i = 0; i < CalTitle.length; i++) 
        {
            CalTitle[i] = cur.getString(1);
            
            	NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            	//para que al notificar abra la app en la pagina principal
            	Intent notificationIntent = new Intent (context, Inicio.class );
            	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
            	            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            	// prepare intent which is triggered if the notification is selected
            	PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
            	Notification n  = new Notification.Builder(this)
                .setContentTitle("Revisar zonas peligrosas ")
                .setContentText("Evento: "+ cur.getString(1))
//                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();
//           NO     .addAction(R.drawable.icon, "Call", pIntent)
//             NO   .addAction(R.drawable.icon, "More", pIntent)
//               NO .addAction(R.drawable.icon, "And more", pIntent).build();
//            		Notification n = builder.build();A
            	notificationManager.notify(0, n);

            	//easy way
            	Toast.makeText(getApplicationContext(), 
                        "Revisar Sichock: "+CalTitle[i], Toast.LENGTH_LONG).show();
            cur.moveToNext();
        }
        stopSelf();
	}
	
	   @Override
	    public void onDestroy() 
	   {
	        // I want to restart this service again in one hour and 59 min
	        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
	        alarm.set
	        (
	            alarm.RTC_WAKEUP,
	            System.currentTimeMillis() + (1000 * 60 * 60),
	            PendingIntent.getService(this, 0, new Intent(this, CentroEventos.class), 0)
	        );
	   }
}
