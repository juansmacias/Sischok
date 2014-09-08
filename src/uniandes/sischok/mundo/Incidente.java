package uniandes.sischok.mundo;

import java.util.Date;

public class Incidente {
	
	//--------Atributos---------
	
	/**
	 * Titulo el incidente
	 */
	private String titulo;

	/**
	 * Descripcion del incidente
	 */
	private String descripcion;
	
	/**
	 * Zona donde ocurrio el incidente. Zonas delimitadas por mapa de bogota.
	 */
	private int zona;
	
	/**
	 * Usuario que creo el incidente
	 */
	private String usuarioCreacion;

	/**
	 * Gravedad del incidente. Base del 1 al 5.
	 */
	private int gravedad;
	
	/**
	 * Fecha de Creación del incidente
	 */
	private Date fechaCreacion;
	
//----------Constructor---------------
	
	/**
	 * Constructor de la clase. 
	 * @param strTitulo
	 * @param strDescripcion
	 * @param strUsuario
	 * @param intZona
	 * @param intGravedad
	 */
	public Incidente(String strTitulo,String strDescripcion, String strUsuario, int intZona,int intGravedad)
	{
		titulo = strTitulo;
		descripcion = strDescripcion;
		zona = intZona;
		usuarioCreacion = strUsuario;
		gravedad =intGravedad;
		fechaCreacion = new Date();
	}

	//------------Metodos---------------------------------
	
	public String darTitulo() {
		return titulo;
	}

	public void cambiarTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String darDescripcion() {
		return descripcion;
	}

	public void cambiarDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int darZona() {
		return zona;
	}

	public void cambiarZona(int zona) {
		this.zona = zona;
	}

	public String darUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void cambiarUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public int darGravedad() {
		return gravedad;
	}

	public void cambiarGravedad(int gravedad) {
		this.gravedad = gravedad;
	}
	
	public Date darFechaCreacion() {
		return fechaCreacion;
	}
	public void cambiarFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
