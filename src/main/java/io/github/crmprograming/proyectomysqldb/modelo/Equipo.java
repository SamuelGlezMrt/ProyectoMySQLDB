package io.github.crmprograming.proyectomysqldb.modelo;

public class Equipo extends Registro {
	
	private int codEquipo;
	private String nomEquipo;
	private String codLiga;
	private String liga;
	private String localidad;
	private boolean internacional;
	
	public Equipo(int codEquipo, String nomEquipo, String codLiga, String liga, String localidad, boolean internacional) {
		this.codEquipo = codEquipo;
		this.nomEquipo = nomEquipo;
		this.codLiga = codLiga;
		this.liga = liga;
		this.localidad = localidad;
		this.internacional = internacional;
	}

	public int getCodEquipo() {
		return codEquipo;
	}

	public void setCodEquipo(int codEquipo) {
		this.codEquipo = codEquipo;
	}

	public String getNomEquipo() {
		return nomEquipo;
	}

	public void setNomEquipo(String nomEquipo) {
		this.nomEquipo = nomEquipo;
	}
	
	public String getCodLiga() {
		return codLiga;
	}

	public void setCodLiga(String codLiga) {
		this.codLiga = codLiga;
	}

	public String getLiga() {
		return liga;
	}

	public void setLiga(String liga) {
		this.liga = liga;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public boolean isInternacional() {
		return internacional;
	}

	public void setInternacional(boolean internacional) {
		this.internacional = internacional;
	}

	@Override
	public String toString() {
		return "Equipo [codEquipo=" + codEquipo + ", nomEquipo=" + nomEquipo + ", liga=" + liga + ", localidad="
				+ localidad + ", internacional=" + internacional + "]";
	}

}
