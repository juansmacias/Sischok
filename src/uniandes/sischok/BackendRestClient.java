package uniandes.sischok;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

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
