package com.anahuac.rest.api.Entity.custom

class SesionesCustom {
	private Long idSesion;
	private String nombreSesion;
	private String uni;
	private String sesion;
	private String fecha;
	private String horaInicio;
	private String horaTermino;
	private String estatus; 
	private String aspirantes;
	private String idioma;
	private String aspirantesNoRegistrados;
	
	public Long getIdSesion() {
		return idSesion;
	}
	public void setIdSesion(Long idSesion) {
		this.idSesion = idSesion;
	}
	public String getNombreSesion() {
		return nombreSesion;
	}
	public void setNombreSesion(String nombreSesion) {
		this.nombreSesion = nombreSesion;
	}
	public String getUni() {
		return uni;
	}
	public void setUni(String uni) {
		this.uni = uni;
	}
	public String getSesion() {
		return sesion;
	}
	public void setSesion(String sesion) {
		this.sesion = sesion;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}
	public String getHoraTermino() {
		return horaTermino;
	}
	public void setHoraTermino(String horaTermino) {
		this.horaTermino = horaTermino;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getAspirantes() {
		return aspirantes;
	}
	public void setAspirantes(String aspirantes) {
		this.aspirantes = aspirantes;
	}
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	public String getAspirantesNoRegistrados() {
		return aspirantesNoRegistrados;
	}
	public void setAspirantesNoRegistrados(String aspirantesNoRegistrados) {
		this.aspirantesNoRegistrados = aspirantesNoRegistrados;
	}
}
