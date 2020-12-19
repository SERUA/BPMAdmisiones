package com.anahuac.rest.api.Entity.Custom

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class CatPeriodoCustom {
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private Long persistenceVersion;
	@XmlElement
	private String descripcion;
	@XmlElement
	private Date fechaCreacion;
	@XmlElement
	private Boolean isEliminado;
	@XmlElement
	private String usuarioBanner;
	@XmlElement
	private Date fechaImportacion;
	@XmlElement
	private String clave;
	@XmlElement
	private Boolean isEnabled;
	@XmlElement
	private String nombreCampus;
	@XmlElement
	private String persistenceId_string;
	@XmlElement
	private Boolean isCuatrimestral
	
	public Date getFechaImportacion() {
		return fechaImportacion;
	}
	public void setFechaImportacion(Date fechaImportacion) {
		this.fechaImportacion = fechaImportacion;
	}
	public Boolean getIsCuatrimestral() {
		return isCuatrimestral;
	}
	public void setIsCuatrimestral(Boolean isCuatrimestral) {
		this.isCuatrimestral = isCuatrimestral;
	}
	public String getNombreCampus() {
		return nombreCampus;
	}
	public void setNombreCampus(String nombreCampus) {
		this.nombreCampus = nombreCampus;
	}
	public String getPersistenceId_string() {
		return persistenceId_string;
	}
	public void setPersistenceId_string(String persistenceId_string) {
		this.persistenceId_string = persistenceId_string;
	}
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
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}
	public String getUsuarioBanner() {
		return usuarioBanner;
	}
	public void setUsuarioBanner(String usuarioBanner) {
		this.usuarioBanner = usuarioBanner;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public Boolean getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	
	
}
