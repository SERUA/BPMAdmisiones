package com.anahuac.rest.api.Entity.custom

class Responsables {
	private Boolean disponible_resp;
	private String nombre;
	private Boolean ocupado;
	private String responsable_id;
	
	public Boolean getDisponible_resp() {
		return disponible_resp;
	}
	public void setDisponible_resp(Boolean disponible_resp) {
		this.disponible_resp = disponible_resp;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Boolean getOcupado() {
		return ocupado;
	}
	public void setOcupado(Boolean ocupado) {
		this.ocupado = ocupado;
	}
	public String getResponsable_id() {
		return responsable_id;
	}
	public void setResponsable_id(String responsable_id) {
		this.responsable_id = responsable_id;
	}
}
