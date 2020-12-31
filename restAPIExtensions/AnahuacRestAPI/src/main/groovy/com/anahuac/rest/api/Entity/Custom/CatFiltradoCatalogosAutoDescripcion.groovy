package com.anahuac.rest.api.Entity.Custom

import javax.xml.bind.annotation.XmlElement

class CatFiltradoCatalogosAutoDescripcion {


	private Long persistenceId;

	private Long persistenceVersion;
	
	private String clave;
	
	private Long orden;
	
	private String descripcion;
	
	private String fechaCreacion;
	
	private String fechaImplementacion;
	
	private String usuarioCreacion;
	
	private String usuarioBanner;
	
	private Boolean isEliminado;
	
	private Boolean isEnabled;
	
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public Long getPersistenceVersion() {
		return persistenceVersion;
	}
	public void setPersistenceVersion(Long persistenceVersion) {
		this.persistenceVersion = persistenceVersion;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public Long getOrden() {
		return orden;
	}
	public void setOrden(Long orden) {
		this.orden = orden;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	
	public String getUsuarioBanner() {
		return usuarioBanner;
	}
	public void setUsuarioBanner(String usuarioBanner) {
		this.usuarioBanner = usuarioBanner;
	}
	
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}
	
	public String getFechaImplementacion() {
		return fechaImplementacion;
	}
	public void setFechaImplementacion(String fechaImplementacion) {
		this.fechaImplementacion = fechaImplementacion;
	}
	public Boolean getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
}
