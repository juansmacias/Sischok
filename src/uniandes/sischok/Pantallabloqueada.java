package uniandes.sischok;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pantalla_bloqueada);
		
		Button buttonA = (Button)findViewById(R.id.bA);
		modificarBoton( buttonA);
		Button buttonG = (Button)findViewById(R.id.bG);
		modificarBoton( buttonG);
		Button buttonV = (Button)findViewById(R.id.bV);
		modificarBoton( buttonV);
		Button buttonI = (Button)findViewById(R.id.bI);
		modificarBoton( buttonI);
		Button buttonX = (Button)findViewById(R.id.bX);
		modificarBoton( buttonX);
		Button buttonY = (Button)findViewById(R.id.bY);
		modificarBoton( buttonY);
		Button buttonT = (Button)findViewById(R.id.bT);
		modificarBoton( buttonT);
//		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		buttonA.setLayoutParams(params1);
		

	}

	private void modificarBoton(Button buttonParam) 
	{
		// TODO VALIDAR QUE NO PINTE UN BOTON SOBRE otro y que no se salga del margen 
		AbsoluteLayout.LayoutParams absParams = (AbsoluteLayout.LayoutParams)buttonParam.getLayoutParams();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		int height = displaymetrics.heightPixels;

		Random r = new Random();

		absParams.x =  r.nextInt(width ) ;
		absParams.y =  r.nextInt(height );
		buttonParam.setLayoutParams(absParams);
	}
	public void onDestroy() 
	{   
	    super.onDestroy();
	}

	//TODO metodo que analice como espichan los botones de letras y si esta ebrio o no.
}
