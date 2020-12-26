package com.anahuac.rest.api.Entity.Custom

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class CatEstadoCivilCustom {
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private Long persistenceVersion;
	@XmlElement
	private String clave;
	@XmlElement
	private String descripcion;
	@XmlElement
	//private Date fechaCreacion;
	private String fechaCreacion
	@XmlElement
	//private Date fechaImportacion;
	private String fechaImportacion;
	@XmlElement
	private String usuarioBanner;
	@XmlElement
	private Boolean isEliminado;
	
	/*public Date getFechaImportacion() {
		return fechaImportacion;
	}
	public void setFechaImportacion(Date fechaImportacion) {
		this.fechaImportacion = fechaImportacion;
	}*/
	public String getFechaImportacion() {
	 return fechaImportacion;
	}
	public void setFechaImportacion(String fechaImportacion) {
	 this.fechaImportacion = fechaImportacion;
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
	/*public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}*/
	
	public String getFechaCreacion() {
	 return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
	 this.fechaCreacion = fechaCreacion;
	}
	
	public String getusuarioBanner() {
		return usuarioBanner;
	}
	public void setusuarioBanner(String usuarioBanner) {
		this.usuarioBanner = usuarioBanner;
	}
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}
	@Override
	public String toString() {
		return "CatLicenciaturaCustom [persistenceId=" + persistenceId + ", persistenceVersion=" + persistenceVersion
				+ ", clave=" + clave + ", descripcion=" + descripcion + ", fechaCreacion=" + fechaCreacion
				+ ", usuarioBanner=" + usuarioBanner + ", isEliminado=" + isEliminado + "]";
	}
	
}
