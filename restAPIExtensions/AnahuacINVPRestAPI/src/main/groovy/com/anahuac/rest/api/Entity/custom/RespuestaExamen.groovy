package com.anahuac.rest.api.Entity.custom

class RespuestaExamen {
	private Long persistenceId;
	private Long persistenceVersion;
	private Long caseId;
	private Boolean contestada;
	private Long idUsuario;
	private String pregunta;
	private Boolean respuesta;
	private List<Object> lstPreguntas;
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public Long getPersistenceVersion() {
		return persistenceVersion;
	}
	public void setPersistenceVersion(Long persistenceVersion) {
		this.persistenceVersion = persistenceVersion;
	}
	public Long getCaseId() {
		return caseId;
	}
	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}
	public Boolean getContestada() {
		return contestada;
	}
	public void setContestada(Boolean contestada) {
		this.contestada = contestada;
	}
	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getPregunta() {
		return pregunta;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	public Boolean getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Boolean respuesta) {
		this.respuesta = respuesta;
	}
	public List<Object> getLstPreguntas() {
		return lstPreguntas;
	}
	public void setLstPreguntas(List<Object> lstPreguntas) {
		this.lstPreguntas = lstPreguntas;
	}

	
}
