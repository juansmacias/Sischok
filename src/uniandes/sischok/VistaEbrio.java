package uniandes.sischok;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class VistaEbrio extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
//		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vista_ebrio);
		
//		HomeKeyLocker HomeKeyLoader = new HomeKeyLocker();
	}
	
	public void pantallaBloqueada()
	{
		Intent intent = new Intent(VistaEbrio.this,Pantallabloqueada.class);
		startActivity(intent);	
	}
	
	@Override
	public void onAttachedToWindow() {
	    this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	    super.onAttachedToWindow();
	}
//	@Override    
//	public void onWindowFocusChanged(boolean hasFocus) {
//	    this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);    
//	    super.onWindowFocusChanged(hasFocus);
//	}

}
