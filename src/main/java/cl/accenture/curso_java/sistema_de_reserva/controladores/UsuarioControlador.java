package cl.accenture.curso_java.sistema_de_reserva.controladores;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import cl.accenture.curso_java.sistema_de_reserva.dao.UsuarioDAO;
import cl.accenture.curso_java.sistema_de_reserva.modelo.SinConexionException;
import cl.accenture.curso_java.sistema_de_reserva.modelo.Usuario;

@ManagedBean
@RequestScoped
public class UsuarioControlador implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5485314226149147415L;

	private String nombreUsuario;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String correo;
	private String password;
	private String mensajeNuevoUsuario;
	private String mensaje;
	private String preferencia;
	private String errorEdad;
	private String errorEmail;

	private int celular;
	private int edad;
	private int estado;

	private boolean errorNuevo;

	private List<String> preferencias;
	private List<String> preferenciasSeleccionadas;
	private List<Usuario> usuarios;

	public UsuarioControlador() {

		limpiar();

	}

	public String guardar() {

		Usuario usuario = new Usuario();
		usuario.setNombreUsuario(this.nombreUsuario);
		usuario.setNombre(this.nombre);
		usuario.setApellidoPaterno(this.apellidoPaterno);
		usuario.setApellidoMaterno(this.apellidoMaterno);
		usuario.setCorreo(this.correo);
		usuario.setCelular(this.celular);
		usuario.setEdad(this.edad);
		usuario.setPassword(this.password);
		usuario.setPreferencia(this.preferencia);

		try {
			
			if (validarEdad()) {
				this.errorEdad = "Debes ser mayor de edad";
			}
			else if(!validarUsuario()) {
				UsuarioDAO.insertarUsuario(usuario);
				return "inicioSesionFinal?faces-redirect=true";
			}else {
				this.mensaje = "El usuario ya esta en uso";
			}
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SinConexionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	// validar edad

	public boolean validarEdad() {

		if (this.edad < 18) {
			this.mensaje = "Debes ser mayor de edad";
			return true;
		} else {
			this.mensaje = "";
			return false;
		}

	}

	public boolean validarUsuario() {
		
		try {
			this.errorNuevo = UsuarioDAO.nombreUsuario_existe(this.nombreUsuario);
			if (errorNuevo) {
				this.mensaje = "Ya existe";
				return true;
			} else {
				this.mensaje = "Perfecto";
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SinConexionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}
	
	//validar email

	public void limpiar() {
		this.nombreUsuario = "";
		this.nombre = "";
		this.apellidoPaterno = "";
		this.apellidoMaterno = "";
		this.correo = "";
		this.celular = 0;
		this.edad = 0;
		this.preferencias = new ArrayList<String>();
		this.mensaje = "";

	}

	public List<String> getPreferenciasSeleccionadas() {
		return preferenciasSeleccionadas;
	}

	public void setPreferenciasSeleccionadas(List<String> preferenciasSeleccionadas) {
		this.preferenciasSeleccionadas = preferenciasSeleccionadas;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public int getCelular() {
		return celular;
	}

	public void setCelular(int celular) {
		this.celular = celular;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public int isEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public List<String> getPreferencias() {
		return preferencias;
	}

	public void setPreferencias(List<String> preferencias) {
		this.preferencias = preferencias;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isErrorNuevo() {
		return errorNuevo;
	}

	public void setErrorNuevo(boolean errorNuevo) {
		this.errorNuevo = errorNuevo;
	}

	public String getMensajeNuevoUsuario() {
		return mensajeNuevoUsuario;
	}

	public void setMensajeNuevoUsuario(String mensajeNuevoUsuario) {
		this.mensajeNuevoUsuario = mensajeNuevoUsuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getEstado() {
		return estado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getPreferencia() {
		return preferencia;
	}

	public void setPreferencia(String preferencia) {
		this.preferencia = preferencia;
	}

	public String getErrorEdad() {
		return errorEdad;
	}

	public void setErrorEdad(String errorEdad) {
		this.errorEdad = errorEdad;
	}

}
