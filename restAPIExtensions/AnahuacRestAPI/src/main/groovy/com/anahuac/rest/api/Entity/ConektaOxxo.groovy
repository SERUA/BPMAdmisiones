package com.anahuac.rest.api.Entity

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class ConektaOxxo {
	@XmlElement
	private String id;
	@XmlElement
	private String total;
	@XmlElement
	private String orden;
	@XmlElement
	private String referencia;
	
	public ConektaOxxo(String id,String total, String orden, String referencia) {
		this.setReferencia(referencia);
		this.setId(id);
		this.setOrden(orden);
		this.setTotal(total);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
}
