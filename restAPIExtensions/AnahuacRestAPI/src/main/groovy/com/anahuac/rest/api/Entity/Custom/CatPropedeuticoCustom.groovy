package com.anahuac.rest.api.Entity.Custom

import java.time.LocalDateTime

import com.anahuac.catalogos.CatCampus

class CatPropedeuticoCustom {
	private Long persistenceId;
	private Long persistenceVersion;
	private String clave;
	private String descripcion;
	private String fechaCreacion;
	private String usuarioCreacion;
	private Boolean isEliminado;
	private String fechaEjecucion;
	private String fechaInicio;
	private String fechaFinal;
	private String nombreCampus;
	private CatCampus campus;
	
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
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public String getFechaFinal() {
		return fechaFinal;
	}
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}	
	public String getNombreCampus() {
		return nombreCampus;
	}
	public void setNombreCampus(String nombreCampus) {
		this.nombreCampus = nombreCampus;
	}
	public CatCampus getCampus() {
		return campus;
	}
	public void setCampus(CatCampus campus) {
		this.campus = campus;
	}
}
