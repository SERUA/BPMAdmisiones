package com.anahuac.rest.api.Entity.db

class PSGRCatTipoEmpresa {
	private Long persistenceid;
	private String clave;
	private Long orden;
	private String descripcion;
	private String fecha_creacion;
	private String usuario_creacion;

	public Long getPersistenceid() {
		return persistenceid;
	}

	public void setPersistenceid(Long persistenceid) {
		this.persistenceid = persistenceid;
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
}
