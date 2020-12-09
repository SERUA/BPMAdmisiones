package com.anahuac.rest.api.Entity.Custom

import java.time.LocalDateTime


class CatEscolaridadCustom{

	private Long persistenceId;

	private Long persistenceVersion;

	private String clave;

	private String descripcion;

	private String fechaCreacion;

	private String usuarioCreacion;

	private Boolean isEliminado;

	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}

	public Long getPersistenceId() {
		return persistenceId;
	}

	public void setPersistenceVersion(Long persistenceVersion) {
		this.persistenceVersion = persistenceVersion;
	}

	public Long getPersistenceVersion() {
		return persistenceVersion;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getClave() {
		return clave;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}


	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}

	public Boolean isIsEliminado() {
		return isEliminado;
	}
}
