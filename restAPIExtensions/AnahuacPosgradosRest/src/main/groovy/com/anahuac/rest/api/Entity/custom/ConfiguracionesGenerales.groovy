package com.anahuac.rest.api.Entity.custom

class ConfiguracionesGenerales {
	private Long persistenceId;
	private String valor;
	private String clave;
	private String descripcion;
	private Long id_campus;
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getId_campus() {
		return id_campus;
	}
	public void setId_campus(Long id_campus) {
		this.id_campus = id_campus;
	}
}
