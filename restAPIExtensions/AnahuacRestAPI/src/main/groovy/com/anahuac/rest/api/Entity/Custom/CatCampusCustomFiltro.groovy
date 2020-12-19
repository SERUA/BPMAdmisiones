package com.anahuac.rest.api.Entity.Custom

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class CatCampusCustomFiltro {
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private Long persistenceVersion;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String fechaCreacion;
	@XmlElement
	private Boolean isEliminado;
	@XmlElement
	private String usuarioBanner;
	@XmlElement
	private String fechaImplementacion;
	@XmlElement
	private String clave;
	@XmlElement
	private Boolean isEnabled;
	@XmlElement
	private long orden;
	@XmlElement
	private String grupoBonita;
	@XmlElement
	private String id;
	@XmlElement
	private String urlDatosVeridicos;
	@XmlElement
	private String urlAutorDatos;
	@XmlElement
	private String urlAvisoPrivacidad;
	
	
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
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
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
	public String getFechaImplementacion() {
		return fechaImplementacion;
	}
	public void setFechaImplementacion(String fechaImplementacion) {
		this.fechaImplementacion = fechaImplementacion;
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
	public Long getOrden() {
		return orden;
	}
	public void setOrden(Long orden) {
		this.orden = orden;
	}
	public String getGrupoBonita() {
		return grupoBonita;
	}
	public void setGrupoBonita(String grupoBonita) {
		this.grupoBonita = grupoBonita;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrlDatosVeridicos() {
		return urlDatosVeridicos;
	}
	public void setUrlDatosVeridicos(String urlDatosVeridicos) {
		this.urlDatosVeridicos = urlDatosVeridicos;
	}
	public String getUrlAutorDatos() {
		return urlAutorDatos;
	}
	public void setUrlAutorDatos(String urlAutorDatos) {
		this.urlAutorDatos = urlAutorDatos;
	}
	public String getUrlAvisoPrivacidad() {
		return urlAvisoPrivacidad;
	}
	public void setUrlAvisoPrivacidad(String urlAvisoPrivacidad) {
		this.urlAvisoPrivacidad = urlAvisoPrivacidad;
	}
	
	@Override
	public String toString() {
		return "CatCampusCustom [persistenceId=" + persistenceId + ", persistenceVersion=" + persistenceVersion
				+ ", descripcion=" + descripcion + ", fechaCreacion=" + fechaCreacion + ", isEliminado=" + isEliminado
				+ ", usuarioBanner=" + usuarioBanner + ", fechaImplementacion=" + fechaImplementacion + ", clave="
				+ clave + ", isEnabled=" + isEnabled + ", orden =" + orden + " , grupoBonita =" + grupoBonita + ", id ="
				 + id + " , urlDatosVeridicos =" + urlDatosVeridicos + " , urlAutorDatos =" + urlAutorDatos + " , urlAvisoPrivacidad =" + urlAvisoPrivacidad + "]";
	}
	
	
}
