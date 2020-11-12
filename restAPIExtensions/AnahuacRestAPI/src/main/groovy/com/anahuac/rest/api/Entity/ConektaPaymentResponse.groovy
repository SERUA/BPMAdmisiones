package com.anahuac.rest.api.Entity

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class ConektaPaymentResponse {
	@XmlElement
	private String id;
	@XmlElement
	private String total;
	@XmlElement
	private String orden;
	@XmlElement
	private String codigo;
	@XmlElement
	private String card_info;
	
	public ConektaPaymentResponse(String id,String total, String orden, String codigo, String card_info) {
		this.setCard_info(card_info);
		this.setCodigo(codigo);
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
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getCard_info() {
		return card_info;
	}
	public void setCard_info(String card_info) {
		this.card_info = card_info;
	}
}
