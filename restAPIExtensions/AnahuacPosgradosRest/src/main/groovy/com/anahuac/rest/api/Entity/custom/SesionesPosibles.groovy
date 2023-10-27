package com.anahuac.rest.api.Entity.custom

class SesionesPosibles {
	private Long persistenceId;
	private String persistenceId_string;
	private String fecha_entrevista;
	private String nombre;
	private String descripcion_entrevista;
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
}
