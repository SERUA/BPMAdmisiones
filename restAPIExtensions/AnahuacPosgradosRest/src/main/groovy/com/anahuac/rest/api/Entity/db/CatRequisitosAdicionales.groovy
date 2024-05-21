package com.anahuac.rest.api.Entity.db

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class CatRequisitosAdicionales {
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
	@XmlElement
	private Long orden;
	@XmlElement
	private Boolean requiere_documento;
	@XmlElement
	private String tipo_de_archivo;
	@XmlElement
	private String nombre;
	
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
	public String getFecha_creacion() {
		return fecha_creacion;
	}
	public void setFecha_creacion(String fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	public String getUsuario_creacion() {
		return usuario_creacion;
	}
	public void setUsuario_creacion(String usuario_creacion) {
		this.usuario_creacion = usuario_creacion;
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
	public String getTipo_de_archivo() {
		return tipo_de_archivo;
	}
	public void setTipo_de_archivo(String tipo_de_archivo) {
		this.tipo_de_archivo = tipo_de_archivo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Boolean getRequiere_documento() {
		return requiere_documento;
	}
	public void setRequiere_documento(Boolean requiere_documento) {
		this.requiere_documento = requiere_documento;
	}
}
