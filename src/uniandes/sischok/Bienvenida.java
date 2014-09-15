package uniandes.sischok;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class Bienvenida extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bienvenida);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bienvenida, menu);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public void iniciarApp (View view)
	{
			Intent intentResult = new Intent();
			String nombreU = ((EditText) findViewById(R.id.txtNombre)).getText().toString();
			String stredadU = ((EditText) findViewById(R.id.txtEdad)).getText().toString();
			if(!nombreU.equals("")&&!stredadU.equals(""))
			{
				int edadU = Integer.parseInt(stredadU);
				if(edadU>17)
				{
				intentResult.putExtra("nombre", nombreU);
				intentResult.putExtra("edad", edadU);
				setResult(1, intentResult);
				finish();
				}
				else
				{
					AlertDialog alertDialog = new AlertDialog.Builder(
							Bienvenida.this).create();

		    alertDialog.setTitle("Error");
		    alertDialog.setMessage("No cumple los requerimientos basicos");

		    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            }
		    });
		    alertDialog.show();
				}
			}
			else
			{
				AlertDialog alertDialog = new AlertDialog.Builder(
						Bienvenida.this).create();

	    alertDialog.setTitle("Error");
	    alertDialog.setMessage("No cumple los requerimientos basicos");

	    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            }
	    });
	    alertDialog.show();
			}
		}
}
