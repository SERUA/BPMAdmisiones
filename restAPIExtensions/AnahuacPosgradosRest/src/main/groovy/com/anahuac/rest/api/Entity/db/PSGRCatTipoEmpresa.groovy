package com.anahuac.rest.api.Entity.db

class PSGRCatTipoEmpresa {
	private Long persistenceid;
	private String clave;
	private Long orden;
	private String descripcion;

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
}
