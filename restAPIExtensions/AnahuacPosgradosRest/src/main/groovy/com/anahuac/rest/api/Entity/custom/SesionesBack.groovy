package com.anahuac.rest.api.Entity.custom

class SesionesBack {
	private Long persistenceId;
	private String persistenceId_string;
	private String fecha_entrevista;
	private String nombre;
	private String descripcion_entrevista;
	private Long campus;
	private Integer duracion_entrevista_minutos;
	private fecha_entrevista_back;
	
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public String getPersistenceId_string() {
		return persistenceId_string;
	}
	public void setPersistenceId_string(String persistenceId_string) {
		this.persistenceId_string = persistenceId_string;
	}
	public String getFecha_entrevista() {
		return fecha_entrevista;
	}
	public void setFecha_entrevista(String fecha_entrevista) {
		this.fecha_entrevista = fecha_entrevista;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion_entrevista() {
		return descripcion_entrevista;
	}
	public void setDescripcion_entrevista(String descripcion_entrevista) {
		this.descripcion_entrevista = descripcion_entrevista;
	}
	public Integer getDuracion_entrevista_minutos() {
		return duracion_entrevista_minutos;
	}
	public void setDuracion_entrevista_minutos(Integer duracion_entrevista_minutos) {
		this.duracion_entrevista_minutos = duracion_entrevista_minutos;
	}
	public Long getCampus() {
		return campus;
	}
	public void setCampus(Long campus) {
		this.campus = campus;
	}
	public java.lang.Object getFecha_entrevista_back() {
		return fecha_entrevista_back;
	}
	public void setFecha_entrevista_back(java.lang.Object fecha_entrevista_back) {
		this.fecha_entrevista_back = fecha_entrevista_back;
	}
}
