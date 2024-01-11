package com.anahuac.rest.api.Entity.custom

class Horarios {
	private Long persistenceid;
	private String hora_fin;
	private String hora_inicio;
	private List<Responsables> responsables;
	private Object cita_entrevista;
	
	public Long getPersistenceid() {
		return persistenceid;
	}
	public void setPersistenceid(Long persistenceid) {
		this.persistenceid = persistenceid;
	}
	public String getHora_fin() {
		return hora_fin;
	}
	public void setHora_fin(String hora_fin) {
		this.hora_fin = hora_fin;
	}
	public String getHora_inicio() {
		return hora_inicio;
	}
	public void setHora_inicio(String hora_inicio) {
		this.hora_inicio = hora_inicio;
	}
	public List<Responsables> getResponsables() {
		return responsables;
	}
	public void setResponsables(List<Responsables> responsables) {
		this.responsables = responsables;
	}
	public Object getCita_entrevista() {
		return cita_entrevista;
	}
	public void setCita_entrevista(Object cita_entrevista) {
		this.cita_entrevista = cita_entrevista;
	}
}
