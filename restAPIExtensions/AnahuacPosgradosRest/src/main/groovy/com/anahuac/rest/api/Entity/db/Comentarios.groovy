package com.anahuac.rest.api.Entity.db

class Comentarios {
	private String comentario;
	private String usuario;
	private	String fechaCreacion;
	private	String modulo
	private	Boolean isEliminado;
	private String usuarioComentario;
	private Long caseId;
	
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getModulo() {
		return modulo;
	}
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}
	public String getUsuarioComentario() {
		return usuarioComentario;
	}
	public void setUsuarioComentario(String usuarioComentario) {
		this.usuarioComentario = usuarioComentario;
	}
	public Long getCaseId() {
		return caseId;
	}
	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}
}
