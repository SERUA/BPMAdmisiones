package com.anahuac.rest.api.Entity.db

import javax.xml.bind.annotation.XmlElement

class PSGRCatSexo {
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
	private String usuario_creacion;
	@XmlElement
	private Boolean is_eliminado;
	
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
	public String getUsuarioCreacion() {
		return usuario_creacion;
	}
	public void setUsuarioCreacion(String usuario_creacion) {
		this.usuario_creacion = usuario_creacion;
	}
	public Boolean getIsEliminado() {
		return is_eliminado;
	}
	public void setIsEliminado(Boolean is_eliminado) {
		this.is_eliminado = is_eliminado;
	}
}