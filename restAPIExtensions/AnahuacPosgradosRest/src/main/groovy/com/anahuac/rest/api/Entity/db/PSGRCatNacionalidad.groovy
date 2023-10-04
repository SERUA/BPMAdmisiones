package com.anahuac.rest.api.Entity.db

import javax.xml.bind.annotation.XmlElement

class PSGRCatNacionalidad {
    @XmlElement
	private Long persistenceId;
	@XmlElement
	private Long persistenceVersion;
	@XmlElement
	private String clave;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String fecha_creacion;
	@XmlElement
	private String fecha_importacion;
	@XmlElement
	private String usuario_banner;
	@XmlElement
	private Boolean is_eliminado;
	@XmlElement
	private Long orden;
	@XmlElement
	private String id;
	
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
	public String getFechaCreacion() {
		return fecha_creacion;
	}
	public void setFechaCreacion(String fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	public String getFechaImportacion() {
		return fecha_importacion;
	}
	public void setFechaImportacion(String fecha_importacion) {
		this.fecha_importacion = fecha_importacion;
	}
	public String getUsuarioBanner() {
		return usuario_banner;
	}
	public void setUsuarioBanner(String usuario_banner) {
		this.usuario_banner = usuario_banner;
	}
	public Boolean getIsEliminado() {
		return is_eliminado;
	}
	public void setIsEliminado(Boolean is_eliminado) {
		this.is_eliminado = is_eliminado;
	}
	public Long getOrden() {
		return orden;
	}
	public void setOrden(Long orden) {
		this.orden = orden;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}