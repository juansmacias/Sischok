package uniandes.sischok.mundo;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;



public class DetectarBorrachosService extends IntentService {

    public static final String AVISAR_ACTION =
        "BROADCAST_AVISO";
    
    private boolean ya;
	
	
	public DetectarBorrachosService() {
		super("detectarBorrachosService");
		ya= false;
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		if(!ya)
		{
			Intent avisarIntent = new Intent(AVISAR_ACTION);
			LocalBroadcastManager.getInstance(this).sendBroadcast(avisarIntent);
			ya =true;
		}

	}

}
