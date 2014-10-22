package uniandes.sischok;

import org.apache.http.entity.StringEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class BackendRestClient {

	  private static final String BASE_URL = "http://backend-sischok.herokuapp.com/";

	  private static AsyncHttpClient client = new AsyncHttpClient();

	  public static void get(String url, AsyncHttpResponseHandler responseHandler) {
	      client.get(getAbsoluteUrl(url), null, responseHandler);
	  }

	  public static void post(String url, Context context, String params, AsyncHttpResponseHandler responseHandler) throws Exception {
		  StringEntity entity =new StringEntity(params);
		  entity.setContentType("application/json");
	      client.post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
	  }

	  private static String getAbsoluteUrl(String relativeUrl) {
	      return BASE_URL + relativeUrl;
	  }
}
