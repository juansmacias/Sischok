package uniandes.sischok.mundo;

import java.text.SimpleDateFormat;
import java.util.Date;
import uniandes.sischok.R;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

public class CentroEventos extends IntentService
{
		
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
		// TODO Auto-generated method stub lo que hace cuando el servicio start
		//run query
		Cursor cur = null;
		ContentResolver cr = getContentResolver();
		String uriString = "content://com.android.calendar/events"; 
		Uri uri = Uri.parse(uriString); 
		
		String currentDate = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new Date());
		
		String selection = "((" + CalendarContract.Events.DTSTART + " = ?))";
		String[] selectionArgs = new String[] {currentDate}; 
//		Uri uri = CalendarContract.Events.CONTENT_URI; version >14
		cur = cr.query(uri, new String[]{"_id", "title", "description", "dtstart", "dtend", "eventLocation"}, selection, selectionArgs, null);
		cur.moveToFirst();
		//arreglo de los eventos de cada dia
        String[] CalTitle = new String[cur.getCount()];

        // recorre los eventos del dia para notificar unicamente los que van a suceder en la proxima hora y 29 min
        for (int i = 0; i < CalTitle.length; i++) 
        {
            CalTitle[i] = cur.getString(1);
            Date sDate = new Date(cur.getLong(3));
            //6000 milisegundos en un minuto
            Date limitDate = new Date(cur.getLong(3)+(59*60000));
            if(sDate.after(limitDate))
            { 
            	//Notifica
            	NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            	// prepare intent which is triggered if the notification is selected
//            	intent = new Intent(this, NotificationReceiver.class);
            	PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
            	
//            	Notification.Builder builder  = new Notification.Builder(this) A
            	Notification n  = new Notification.Builder(this)
                .setContentTitle("Revisar zonas peligrosas ")
                .setContentText("Evento: "+ cur.getString(3))
                .setSmallIcon(R.drawable.icono)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();
//                .addAction(R.drawable.icon, "Call", pIntent)
//                .addAction(R.drawable.icon, "More", pIntent)
//                .addAction(R.drawable.icon, "And more", pIntent).build();
            	
//            		Notification n = builder.build();A
            	
            	notificationManager.notify(0, n); 
            } 
            cur.moveToNext();
        }
        stopSelf();
	}
	
	   @Override
	    public void onDestroy() 
	   {
	        // I want to restart this service again in one hour
	        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
	        alarm.set
	        (
	            alarm.RTC_WAKEUP,
	            System.currentTimeMillis() + (1000 * 60 * 60),
	            PendingIntent.getService(this, 0, new Intent(this, CentroEventos.class), 0)
	        );
	   }
}
