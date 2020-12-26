package com.anahuac.rest.api.Entity.db

class ResponsableDisponible {
	private Long persisteneceId;
	private Long persistenceVersion;
	private String horario;
	private Long responsableId;
	private Boolean disponible;
	public Boolean getDisponible() {
		return disponible;
	}
	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
	}
	public Long getPersisteneceId() {
		return persisteneceId;
	}
	public void setPersisteneceId(Long persisteneceId) {
		this.persisteneceId = persisteneceId;
	}
	public Long getPersistenceVersion() {
		return persistenceVersion;
	}
	public void setPersistenceVersion(Long persistenceVersion) {
		this.persistenceVersion = persistenceVersion;
	}
	public String getHorario() {
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}
	public Long getResponsableId() {
		return responsableId;
	}
	public void setResponsableId(Long responsableId) {
		this.responsableId = responsableId;
	}
	
	@Override
	public boolean equals(Object arg0) {
		Boolean part1=arg0 != null;
		Boolean part2=arg0 instanceof ResponsableDisponible;
		Boolean part3=((ResponsableDisponible) arg0).getPersisteneceId().equals(this.getPersisteneceId());
		return (part1 && part2 && part3);
	}
}
