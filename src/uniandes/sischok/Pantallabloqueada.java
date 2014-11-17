package uniandes.sischok;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
/**
 * Activity de la pantalla bloqueada
 * @author LinaMariaDuarteSalinas
 *
 */
@SuppressWarnings("deprecation")
public class Pantallabloqueada extends Activity
{

	public int width;
	public int height;
	public String tamano;
	boolean orden;
	boolean finish; 
	boolean a,g,i,t,v,x,y;
	Button buttonA;
	Button buttonG;
	Button buttonV;
	Button buttonI;
	Button buttonX;
	Button buttonY;
	Button buttonT;
	CountDownTimer timerGeneral; 

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pantalla_bloqueada);

		final float scale = getBaseContext().getResources().getDisplayMetrics().density;
		//		int pixels = (int) (dps * scale + 0.5f);
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) 
		{
			// on a large screen device ...
			tamano = "Large";
			width = (int) (435 * scale + 0.5f);
			height = (int) (498 * scale + 0.5f);
		}
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) 
		{
			tamano = "normal";
			//290 -50 para que no se salga de borde
			width = (int) (240 * scale + 0.5f);
			//332-50 para que no se salga del bordee
			height = (int) (282 * scale + 0.5f);
		}

		buttonA = (Button)findViewById(R.id.bA);
		modificarBoton( buttonA);
		buttonG = (Button)findViewById(R.id.bG);
		modificarBoton( buttonG);
		buttonV = (Button)findViewById(R.id.bV);
		modificarBoton( buttonV);
		buttonI = (Button)findViewById(R.id.bI);
		modificarBoton( buttonI);
		buttonX = (Button)findViewById(R.id.bX);
		modificarBoton( buttonX);
		buttonY = (Button)findViewById(R.id.bY);
		modificarBoton( buttonY);
		buttonT = (Button)findViewById(R.id.bT);
		modificarBoton( buttonT);

		orden=false;
		finish= false;
		a = false;
		g = false;
		i = false;
		t = false;
		v = false;
		x = false;
		y = false;
		timerGeneral = new CountDownTimer(3000, 1000) 
		{
			@Override
			public void onFinish() 
			{
				//				*function containing the second statement* done
				finish=true;
				//si el tiempo acabo
				AlertDialog.Builder builder = new AlertDialog.Builder(Pantallabloqueada.this);
				builder.setMessage("El tiempo se ha agotado.")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int id) 
					{
						//Agregar el incidente
						//   		            	lstLatLngs.add(point);
						//					insertarIncidente(point);
						//   		            	gMap.addMarker(new MarkerOptions().position(point));
						//TODO oncreate en este punto para refrescar el activity
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				Intent intent = new Intent(Pantallabloqueada.this, VistaEbrio.class);
				startActivity(intent);
			}
			@Override
			public void onTick(long millisLeft) 
			{
				// not ticking
			}
		}.start();
	}

	private void modificarBoton(Button buttonParam) 
	{
		// TODO VALIDAR QUE NO PINTE UN BOTON SOBRE otro

		AbsoluteLayout.LayoutParams absParams = (AbsoluteLayout.LayoutParams)buttonParam.getLayoutParams();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		Random r = new Random();

		absParams.x =  r.nextInt(width ) ;
		absParams.y =  r.nextInt(height );
		buttonParam.setLayoutParams(absParams);
	}

	public boolean posicionValida(int numeroBoton)
	{
		boolean posicionValida = false;
		int[] location = new int[2];
		//segundo boton que pinta
		if(numeroBoton == 1)
		{
			buttonA.getLocationInWindow(location);

		}
		return posicionValida;

	}

	public void onClick(View vie)
	{
		timerGeneral.cancel();
		CountDownTimer timer = new CountDownTimer(4000, 1000) 
		{
			@Override
			public void onFinish() 
			{
				//				*function containing the second statement* done
				finish=true;
				//si el tiempo acabo
				AlertDialog.Builder builder = new AlertDialog.Builder(Pantallabloqueada.this);
				builder.setMessage("El tiempo se ha agotado. Por favor vuelva a intentarlo")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int id) 
					{
						//Agregar el incidente
						//   		            	lstLatLngs.add(point);
						//					insertarIncidente(point);
						//   		            	gMap.addMarker(new MarkerOptions().position(point));
						//TODO oncreate en este punto para refrescar el activity
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				Intent intent = new Intent(Pantallabloqueada.this, VistaEbrio.class);
				startActivity(intent);
				onDestroy();
			}
			@Override
			public void onTick(long millisLeft) 
			{
				// not ticking
			}
		};

		if(vie.getId() == R.id.bA)
		{
			timer.start();
			buttonA.setVisibility(View.GONE);
			a=true;
		}
		if(vie.getId() == R.id.bG && a && !finish)
		{
			timer.cancel();
			timer.start();
			finish=false;
			buttonG.setVisibility(View.GONE);
			g=true;
		}
		if(vie.getId() == R.id.bG && !a )
		{
			mostrarDialogoOrdenIncorrecto();
		}
		if(vie.getId() == R.id.bI && a && g && !finish)
		{
			timer.cancel();
			timer.start();
			finish=false;
			buttonI.setVisibility(View.GONE);
			i=true;
		}
		if(vie.getId() == R.id.bI && (!a || !g))
		{
			mostrarDialogoOrdenIncorrecto();
		}
		if(vie.getId() == R.id.bT && a && g && i && !finish)
		{
			timer.cancel();
			timer.start();
			finish=false;
			buttonT.setVisibility(View.GONE);
			t=true;
		}
		if(vie.getId() == R.id.bT && (!a || !g ||!i))
		{
			mostrarDialogoOrdenIncorrecto();
		}
		if(vie.getId() == R.id.bV && a && g && i && t && !finish)
		{
			timer.cancel();
			timer.start();
			finish=false;
			buttonV.setVisibility(View.GONE);
			v=true;
		}
		if(vie.getId() == R.id.bV && (!a || !g || !i || !t))
		{
			mostrarDialogoOrdenIncorrecto();
		}
		if(vie.getId() == R.id.bX && a && g && i && t && v && !finish)
		{
			timer.cancel();
			timer.start();
			finish=false;
			buttonX.setVisibility(View.GONE);
			x=true;
		}
		if(vie.getId() == R.id.bX && (!a || !g || !i || !t || !v))
		{
			mostrarDialogoOrdenIncorrecto();
		}
		if(vie.getId() == R.id.bY && a && g && i && t && v && x &&!finish)
		{
			timer.cancel();
			timer.start();
			finish=false;
			buttonY.setVisibility(View.GONE);
			y=true;
			//LLEGAR ACA IMPLICA QUE EL USUARIO NO ESTA EBRIO
		}
		if(vie.getId() == R.id.bY && (!a || !g || !i || !t || !v || !x))
		{
			mostrarDialogoOrdenIncorrecto();
		}
	}
	public void mostrarDialogoOrdenIncorrecto()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(Pantallabloqueada.this);
		builder.setMessage("Orden incorrecto. Vuelva a intentarlo")
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id) 
			{
				//Agregar el incidente
				//   		            	lstLatLngs.add(point);
				//					insertarIncidente(point);
				//   		            	gMap.addMarker(new MarkerOptions().position(point));
				//TODO oncreate en este punto para refrescar el activity
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

		//vuelve a crear este activity si lo hace en el orden equivocado
		Intent intent = new Intent(Pantallabloqueada.this, Pantallabloqueada.class);
		startActivity(intent);
		onDestroy();
	}
	public void onDestroy() 
	{   
		super.onDestroy();
	}
}
