package cl.accenture.curso_java.sistema_de_reserva.controladores;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import cl.accenture.curso_java.sistema_de_reserva.dao.ConfiguracionDAO;
import cl.accenture.curso_java.sistema_de_reserva.dao.DiaFeriadoDAO;
import cl.accenture.curso_java.sistema_de_reserva.dao.ReservaDAO;
import cl.accenture.curso_java.sistema_de_reserva.dao.SucursalDAO;
import cl.accenture.curso_java.sistema_de_reserva.modelo.Configuracion;
import cl.accenture.curso_java.sistema_de_reserva.modelo.Sucursal;
import cl.accenture.curso_java.sistema_de_reserva.modelo.Usuario;
import cl.accenture.curso_java.sistema_de_reserva.servicio.ServicioHorasDisponibles;

@ManagedBean
@SessionScoped
public class AgregarReservaControlador implements Serializable {

	/**
	 * @author Luis Torres
	 *
	 */
	private static final long serialVersionUID = 2545746997175419045L;

	private String mensaje;
	private String servicio;
	private String nombre;
	private String horaInicio;
	private String horaFin;
	private String bloque;
	private String hora;
	private String verFecha;
	private String sinHoras;

	private Date fechaReserva;

	private int idsucursal;

	private List<String> diasFeriados;
	private List<Sucursal> sucursales;
	private List<String> horas;
	private List<String> horasReservadas;
	private List<String> horasDisponibles;
	private List<Configuracion> configuraciones;

	public AgregarReservaControlador() {
		obtenerSucursal();
		obtenerFeriados();
		recargar();
		obtenerHorasDisponibles();

	}

	// Listar dias Feriados

	public void obtenerFeriados() {

		try {
			this.setDiasFeriados(DiaFeriadoDAO.obtenerFeriados());
		} catch (Exception e) {
			// TODO: handle exception
			this.mensaje = "Lo sentimos ocurrio un error en listar los dias feriados";
			this.setDiasFeriados(new ArrayList<String>());
		}

	}

	// lista de sucursales

	public void obtenerSucursal() {

		try {
			this.sucursales = SucursalDAO.obtenerSucursal();
		} catch (Exception e) {
			this.mensaje = "Lo sentimos, Ocurrio un error al obtener la Sucursal";
			this.sucursales = new ArrayList<Sucursal>();
		}
	}

	// Horas disponibles

	public void obtenerHorasDisponibles() {

		try {

			this.configuraciones = ConfiguracionDAO.obtenerConfiguraciones();

			this.bloque = this.configuraciones.get(0).getValor();
			this.horaFin = this.configuraciones.get(1).getValor();
			this.horaInicio = this.configuraciones.get(2).getValor();

			int bloqueF = Integer.parseInt(bloque);

			Calendar cal = Calendar.getInstance();
			cal.setTime(this.fechaReserva);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			this.fechaReserva = cal.getTime();

			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
			String fecha = formatoFecha.format(this.fechaReserva);
			this.horas = ServicioHorasDisponibles.calcularHorasDisponibles(this.horaInicio, this.horaFin, bloqueF);
			this.horasReservadas = ReservaDAO.obenerHorasReservadas(fecha);
			this.horasDisponibles = ServicioHorasDisponibles.obtenerHorasDisponibles(this.horasReservadas, this.horas);

			// Dias Feriados
			for (String feriado : this.diasFeriados) {

				if (fecha.equals(feriado)) {

					this.horasDisponibles = new ArrayList<String>();
					this.sinHoras = "No hay horas disponibles";

				}
			}

			// Cambiar la fecha para la vista
			SimpleDateFormat formatoFechaMes = new SimpleDateFormat("EEEEEEEEE dd 'de'   MMMMM 'de' yyyy");
			String fechaMostrar = formatoFechaMes.format(this.fechaReserva);
			this.verFecha = fechaMostrar;
			this.sinHoras = "";
			this.mensaje = "";

			// Sin horas Disponibles
			if (this.horasDisponibles.size() == 0) {

				this.sinHoras = "No hay horas disponibles";

			}

		} catch (Exception e) {
			e.printStackTrace();
			this.mensaje = "Lo sentimos, Ocurrio un error al obtener las Horas";
			this.configuraciones = new ArrayList<Configuracion>();
			this.horasDisponibles = new ArrayList<String>();
			this.horasReservadas = new ArrayList<String>();
		}
	}

	// agregar una reserva

	public void agregarReserva() {

		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = formatoFecha.format(this.fechaReserva);

		Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("usuario");

		try {
			ReservaDAO.agregarReserva(fecha, this.servicio, this.nombre, usuario, this.hora);
			recargar();
			obtenerHorasDisponibles();

			this.mensaje = "Reserva agregada con exito";

		} catch (Exception e) {
			this.mensaje = "Ocurrio un error al agregar la reserva";
			System.err.println(e);
		}

	}

	private void recargar() {

		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, 1);

			this.horasDisponibles = new ArrayList<String>();
			this.servicio = "";
			this.verFecha = "";
			this.fechaReserva = cal.getTime();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	public List<Sucursal> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<Sucursal> sucursales) {
		this.sucursales = sucursales;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public List<Configuracion> getConfiguraciones() {
		return configuraciones;
	}

	public void setConfiguraciones(List<Configuracion> configuraciones) {
		this.configuraciones = configuraciones;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	public String getBloque() {
		return bloque;
	}

	public void setBloque(String bloque) {
		this.bloque = bloque;
	}

	public List<String> getHorasDisponibles() {
		return horasDisponibles;
	}

	public void setHorasDisponibles(List<String> horasDisponibles) {
		this.horasDisponibles = horasDisponibles;
	}

	public List<String> getHorasReservadas() {
		return horasReservadas;
	}

	public void setHorasReservadas(List<String> horasReservadas) {
		this.horasReservadas = horasReservadas;
	}

	public List<String> getHoras() {
		return horas;
	}

	public void setHoras(List<String> horas) {
		this.horas = horas;
	}

	public int getIdsucursal() {
		return idsucursal;
	}

	public void setIdsucursal(int idsucursal) {
		this.idsucursal = idsucursal;
	}

	public String getVerFecha() {
		return verFecha;
	}

	public void setVerFecha(String verFecha) {
		this.verFecha = verFecha;
	}

	public List<String> getDiasFeriados() {
		return diasFeriados;
	}

	public void setDiasFeriados(List<String> diasFeriados) {
		this.diasFeriados = diasFeriados;
	}

	public String getSinHoras() {
		return sinHoras;
	}

	public void setSinHoras(String sinHoras) {
		this.sinHoras = sinHoras;
	}

}
