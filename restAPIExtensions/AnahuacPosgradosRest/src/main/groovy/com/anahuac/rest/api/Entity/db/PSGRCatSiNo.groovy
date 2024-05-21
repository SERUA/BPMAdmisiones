package com.anahuac.rest.api.Entity.db

class PSGRCatSiNo {
	private Long persistenceid;
	private String clave;
	private String descripcion;
	private Boolean esSiNo;
	private Boolean esTalvez;
	private Boolean esOtro;
	
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
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Boolean getEsSiNo() {
		return esSiNo;
	}
	public void setEsSiNo(Boolean esSiNo) {
		this.esSiNo = esSiNo;
	}
	public Boolean getEsTalvez() {
		return esTalvez;
	}
	public void setEsTalvez(Boolean esTalvez) {
		this.esTalvez = esTalvez;
	}
	public Boolean getEsOtro() {
		return esOtro;
	}
	public void setEsOtro(Boolean esOtro) {
		this.esOtro = esOtro;
	}
}
