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
	private Date fechaImplementacion;
	@XmlElement
	private String clave;
	@XmlElement
	private String isEnable;
	@XmlElement
	private Boolean isCuatrimestral;
	
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
	public Date getFechaImplementacion() {
		return fechaImplementacion;
	}
	public void setFechaImplementacion(Date fechaImplementacion) {
		this.fechaImplementacion = fechaImplementacion;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	
	public Boolean getIsCuatrimestral() {
		return IsCuatrimestral;
	}
	public void setIsCuatrimestral(Boolean IsCuatrimestral) {
		this.IsCuatrimestral = IsCuatrimestral;
	}
	
	@Override
	public String toString() {
		return "CatCampusCustom [persistenceId=" + persistenceId + ", persistenceVersion=" + persistenceVersion
				+ ", descripcion=" + descripcion + ", fechaCreacion=" + fechaCreacion + ", isEliminado=" + isEliminado
				+ ", usuarioBanner=" + usuarioBanner + ", fechaImplementacion=" + fechaImplementacion + ", clave="
				+ clave + ", isEnable=" + isEnable + "]";
	}
	
	
}
