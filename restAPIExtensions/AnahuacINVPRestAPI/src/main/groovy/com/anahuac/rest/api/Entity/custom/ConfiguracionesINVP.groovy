package com.anahuac.rest.api.Entity.custom

class ConfiguracionesINVP {
	private Integer toleranciaMinutos;
	private Integer toleranciaSalidaMinutos;
	private Long idPrueba;
	
	public Integer getToleranciaMinutos() {
		return toleranciaMinutos;
	}
	public void setToleranciaMinutos(Integer toleranciaMinutos) {
		this.toleranciaMinutos = toleranciaMinutos;
	}
	public Long getIdPrueba() {
		return idPrueba;
	}
	public void setIdPrueba(Long idPrueba) {
		this.idPrueba = idPrueba;
	}
	public Integer getToleranciaSalidaMinutos() {
		return toleranciaSalidaMinutos;
	}
	public void setToleranciaSalidaMinutos(Integer toleranciaSalidaMinutos) {
		this.toleranciaSalidaMinutos = toleranciaSalidaMinutos;
	}
}
