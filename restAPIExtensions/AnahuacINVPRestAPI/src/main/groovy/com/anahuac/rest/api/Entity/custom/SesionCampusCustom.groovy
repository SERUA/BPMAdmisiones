package com.anahuac.rest.api.Entity.custom

class SesionCampusCustom {
	private Long idprueba;
	private String entrada;
	private String salida;
	private String aplicacion;
	private String nombresesion;
	private Integer toleranciaminutos;
	private Integer toleranciasalidaminutos;
	private String estatus;
	private String campus;
	private String grupobonita;
	
	public Long getIdprueba() {
		return idprueba;
	}
	public void setIdprueba(Long idprueba) {
		this.idprueba = idprueba;
	}
	public String getEntrada() {
		return entrada;
	}
	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}
	public String getSalida() {
		return salida;
	}
	public void setSalida(String salida) {
		this.salida = salida;
	}
	public String getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	public String getNombresesion() {
		return nombresesion;
	}
	public void setNombresesion(String nombresesion) {
		this.nombresesion = nombresesion;
	}
	public Integer getToleranciaminutos() {
		return toleranciaminutos;
	}
	public void setToleranciaminutos(Integer toleranciaminutos) {
		this.toleranciaminutos = toleranciaminutos;
	}
	public Integer getToleranciasalidaminutos() {
		return toleranciasalidaminutos;
	}
	public void setToleranciasalidaminutos(Integer toleranciasalidaminutos) {
		this.toleranciasalidaminutos = toleranciasalidaminutos;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getGrupobonita() {
		return grupobonita;
	}
	public void setGrupobonita(String grupobonita) {
		this.grupobonita = grupobonita;
	}
}
