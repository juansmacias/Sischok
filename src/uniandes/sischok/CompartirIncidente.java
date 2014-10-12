package uniandes.sischok;

import org.json.JSONObject;

import uniandes.sischok.mundo.CentroIncidentes;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class CompartirIncidente extends Activity {


	/**
	 * Constante que es usado para determinar cuando se entra a escoger un contacto
	 */
	private final static int ESCOGER_CONTACTO = 1;
	
	private String incidenteActual;
	private MatrixCursor matrixCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startActivityForResult(new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI),ESCOGER_CONTACTO);
		incidenteActual = this.getIntent().getStringExtra("incidente");
		setContentView(R.layout.activity_compartir_incidente);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compartir_incidente, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressWarnings({ "deprecation"})
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		String[] columns = new String[] { "_id",ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
		matrixCursor= new MatrixCursor(columns);
		startManagingCursor(matrixCursor);
		if(resultCode == RESULT_OK){
			if(reqCode == ESCOGER_CONTACTO){
				Uri uriContacto = data.getData();
				if(uriContacto != null ){						
					try {
						String[] cols = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
						Cursor cursor =  managedQuery(uriContacto, cols, null, null, null);
						cursor.moveToFirst();
						String nombreContacto = cursor.getString(0);
						
						Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
						String[] columnas = {ContactsContract.CommonDataKinds.Phone.NUMBER};
						String seleccion = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "='" + nombreContacto + "'";
						Cursor c = managedQuery(phoneUri,columnas,seleccion,null, null );
						String numeroTelefonico ="";
						if(c.moveToFirst()){
							numeroTelefonico = c.getString(0);
						}
						matrixCursor.addRow(new Object[] { 0,nombreContacto, numeroTelefonico});


					} catch (Exception e) {
						 Log.i("Error",e.getMessage());
					}
				}
			}
		}
			ListView comparteCon = (ListView) findViewById(R.id.compartirconlistView);
			ContactosCursorAdapter mAdapter = new ContactosCursorAdapter(this, matrixCursor, 0);
			comparteCon.setAdapter(mAdapter);
	}

	/**
	 * Envia un mensaje de texto al contacto seleccionado con los items de la lista de compras
	 */
	@SuppressWarnings("deprecation")
	public void enviar(View view)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();

		if(matrixCursor.getCount()>0 ){
			matrixCursor.moveToFirst();
			String numeroTelefonico =matrixCursor.getString(2); 
			if(!numeroTelefonico.equals(""))
			{
				SharedPreferences sharedPreference = getSharedPreferences(CentroIncidentes.nombrePreferencias, Context.MODE_PRIVATE);
			try{
				JSONObject incidente = new JSONObject(incidenteActual);
				String mensaje = "Tu amigo usuario de Sischok " + sharedPreference.getString(CentroIncidentes.prefNombre, "AdministradorSischok") + " compartio un incidente contigo: /n"; 
				mensaje += "Titulo-"+incidente.getString("titulo")+"/n";
				mensaje += "Zona-"+incidente.getString("zona")+"/n";
				mensaje += "Nivel de Gravedad-"+incidente.getString("gravedad")+"/n";
				mensaje += "Cod-"+incidente.getString("id");
				SmsManager manejador = SmsManager.getDefault();
				try {
					manejador.sendTextMessage(numeroTelefonico, null, mensaje, null, null);
					Log.i("Compartir", "Se envi— exitosamente el mensaje ");
				} catch (Exception e) {
					Log.i("Compartir", "No se envio el mensaje, excepcion: "+e.getMessage());
				}
				alertDialog.setTitle("Compartir Incidente");
				alertDialog.setMessage("El mensaje se ha enviado a " + matrixCursor.getString(1));
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	        		startActivity(new Intent(CompartirIncidente.this,Inicio.class));
	            	}
				});

			}catch(Exception e){
				alertDialog.setTitle("Compartir Incidente");
				alertDialog.setMessage("Hubo un error creando el mensaje, intentelo de nuevo");
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	        		startActivity(new Intent(CompartirIncidente.this,Inicio.class));
	            	}
				});
			}
		}else{
			alertDialog.setTitle("Compartir Incidente");
			alertDialog.setMessage("Tiene que seleccionar un contacto de su lista.");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
        		startActivity(new Intent(CompartirIncidente.this,Inicio.class));
            	}
			});
		}
		}		
	    alertDialog.show();
	}
	
	public void cancelar(View view)
	{
		finishActivity(0);
	}
}
