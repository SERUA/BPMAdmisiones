package com.anahuac.rest.api.Entity.custom

class SesionesPosibles {
	private Long persistenceId;
	private String persistenceId_string;
	private String fecha_entrevista;
	private String nombre;
	private String descripcion_entrevista;
	private Long campus;
	private Integer duracion_entrevista_minutos;
	private String responsable_id;
	private List<Responsables> responsables;
	private Long programa_interes_pid;
	private Boolean is_presencial;
	
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public Long getProgramaInteres_pid() {
		return programa_interes_pid;
	}
	public void setProgramaInteres_pid(Long programa_interes_pid) {
		this.programa_interes_pid = programa_interes_pid;
	}
	public String getPersistenceId_string() {
		return persistenceId_string;
	}
	public void setPersistenceId_string(String persistenceId_string) {
		this.persistenceId_string = persistenceId_string;
	}
	public String getFecha_entrevista() {
		return fecha_entrevista;
	}
	public void setFecha_entrevista(String fecha_entrevista) {
		this.fecha_entrevista = fecha_entrevista;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion_entrevista() {
		return descripcion_entrevista;
	}
	public void setDescripcion_entrevista(String descripcion_entrevista) {
		this.descripcion_entrevista = descripcion_entrevista;
	}
	public Integer getDuracion_entrevista_minutos() {
		return duracion_entrevista_minutos;
	}
	public void setDuracion_entrevista_minutos(Integer duracion_entrevista_minutos) {
		this.duracion_entrevista_minutos = duracion_entrevista_minutos;
	}
	public Long getCampus() {
		return campus;
	}
	public void setCampus(Long campus) {
		this.campus = campus;
	}
	public String getResponsable_id() {
		return responsable_id;
	}
	public void setResponsable_id(String responsable_id) {
		this.responsable_id = responsable_id;
	}
	public List<Responsables> getResponsables() {
		return responsables;
	}
	public void setResponsables(List<Responsables> responsables) {
		this.responsables = responsables;
	}
	public Boolean getIs_presencial() {
		return is_presencial;
	}
	public void setIs_presencial(Boolean is_presencial) {
		this.is_presencial = is_presencial;
	}
}
