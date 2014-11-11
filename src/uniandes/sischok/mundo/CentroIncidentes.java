package uniandes.sischok.mundo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.greenrobot.dao.query.QueryBuilder;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import uniandes.sischok.CrearIncidenteBarrios;
import uniandes.sischok.R;
import uniandes.sischok.mundo.DaoMaster.DevOpenHelper;
import uniandes.sischok.mundo.Incidente;
import uniandes.sischok.mundo.IncidenteDao.Properties;

public class CentroIncidentes{

	//-------------------Atributos-------------------------------
	
	public static final String nombrePreferencias = "Preferencias";
	public static final String prefPrimeraVez = "PrimeraVez";
	public static final String prefNombre = "nombre";
	public static final String prefNombreDefault = "Administrador Sischok";
	public static final String prefEdad = "edad";
	public static final String prefBorracho = "borracho";
	public static final String prefFechaActualizacion = "FechaActualizacion";
	public static final String nomdb = "sischok-db";
	public static final String strarchivoIncidentesBasicos = "IncidentesBasicos";
	private static CentroIncidentes instancia;	
	@SuppressWarnings("unused")
	private ArrayList<Incidente> incidentesLocales;
	private IncidenteDao incidenteDao;
	private Context contexto;
	private Date fechaActualizacion;
	
	
	
	//-------------------Constructor-----------------------------
	
	public CentroIncidentes(Context contexto)
	{
		incidentesLocales = new ArrayList<Incidente>();
		DevOpenHelper helperNuevo = new DaoMaster.DevOpenHelper(contexto, nomdb, null);
		 SQLiteDatabase db = helperNuevo.getWritableDatabase();
		 DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		incidenteDao = daoSession.getIncidenteDao();
		this.contexto = contexto; 
	}
	
	//-------------------Metodos---------------------------------
	
	public static CentroIncidentes darInstancia(Context contexto)
	{
		if(instancia==null)
		{
			instancia = new CentroIncidentes( contexto);
		}
		return instancia;
	}

	@SuppressLint("SimpleDateFormat") 
	public void iniciarBasedeDatos()
	{
		try {
			String jsonIncidentes = "";
		    StringBuilder builder = new StringBuilder();
		    InputStream instream = contexto.getResources().openRawResource(R.raw.incidentesbasicos);
		    BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
	        while ((jsonIncidentes = reader.readLine()) != null) {
	          builder.append(jsonIncidentes);
	        }
	        jsonIncidentes = builder.toString();
	        reader.close();
	        JSONObject jObjIncidentes = new JSONObject(jsonIncidentes);
	        JSONArray jAryIncidentes = jObjIncidentes.getJSONArray("Incidentes");
	        for (int i = 0; i < jAryIncidentes.length(); i++) {
	        	JSONObject jIncidente = (JSONObject)jAryIncidentes.get(i);
	        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        	Date fechap = sdf.parse(jIncidente.getString("fechaCreacion"));
	        	Incidente inciactual = new Incidente(null, "", jIncidente.getString("titulo"), jIncidente.getString("descripcion"), Integer.parseInt(jIncidente.getString("zona")), Integer.parseInt(jIncidente.getString("gravedad")), Double.valueOf(jIncidente.getString("latitud")),Double.valueOf(jIncidente.getString("longitud")), fechap, jIncidente.getString("usuarioCreacion"));
	        	incidenteDao.insert(inciactual);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Incidente> darUltimos5Incidentes()
	{
		QueryBuilder<Incidente> qb = incidenteDao.queryBuilder();
		qb.limit(5);
		qb.orderDesc(Properties.FechaCreacion);
		return qb.list();
		
	}
	
	public List<Incidente> darUltimosIncidentesEnServidor(JSONArray JsonO)
	{
		ArrayList<Incidente> objUltimoIncidnete = new ArrayList<Incidente>();
			try{
		        for (int i = 0; i < JsonO.length(); i++) {
		        	JSONObject jIncidente = (JSONObject)JsonO.get(i);
		        	Incidente actualNuevo = Incidente.toIncidenteDeServidor(jIncidente);
		        	long idNuevo = crearIncidente(actualNuevo);
		        	objUltimoIncidnete.add(darIncidentePorId(idNuevo));
		        }
		        if(JsonO.length()>0){
			         SharedPreferences sharedpreferences = contexto.getSharedPreferences(CentroIncidentes.nombrePreferencias, Context.MODE_PRIVATE);
				        fechaActualizacion = new Date();
				        Editor editor = sharedpreferences.edit();
				        editor.putLong(prefFechaActualizacion, fechaActualizacion.getTime());
				        editor.commit();
		        }

			} catch (Exception e) {
				e.printStackTrace();
			}
		return objUltimoIncidnete;
	}
	
	public Incidente darIncidentePorId(long id)
	{
		QueryBuilder<Incidente> qb = incidenteDao.queryBuilder();
		qb.limit(1);
		qb.where(Properties.Id.eq(id));
		return qb.unique();
	}
	
	public Boolean registarIncidentes (String strTitulo,String strDescripcion, String strUsuario, int intZona,int intGravedad)
	{
//		incidentesLocales.add(new Incidente(strTitulo, strDescripcion, strUsuario, intZona, intGravedad ));		
		return true;
	}

	
	
	/**
	 * Metodo para Consultar los incidentes Por Zonas
	 * @param zonas Codigos de zonas que se quiere consultar en formato json.
	 * @return Se retorna un arreglo de Incidentes en formato json
	 */
	public List<Incidente> consultarIncidentesPorZonas(String zonas)
	{
		
		String[] zonasp = zonas.split("-");
		List<Integer> zonasInt = new ArrayList<Integer>();
		for (int i = 0; i < zonasp.length; i++) {
			zonasInt.add(Integer.decode(zonasp[0]));
		}
		QueryBuilder<Incidente> qb = incidenteDao.queryBuilder();
		qb.where(Properties.Zona.in(zonasInt));
		return qb.list();
	}
	
	public long crearIncidente (Incidente incidente)
	{
		return incidenteDao.insert(incidente);
	}
	
	@SuppressWarnings("unused")
	private Incidente darIncidentesPorZona(int zona)
	{
		return null;
	}
	
	public Date darfechaActualizacion()
	{
		return fechaActualizacion;
	}
	
	public void setFechaActualizacion(Date date)
	{
		fechaActualizacion = date;
	}
	
}
