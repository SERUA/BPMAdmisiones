package com.anahuac.rest.api.Entity.Custom

class SesionesAspiranteCustom {
	
	private Long session_pid;
	private String username;
	private Boolean iseliminado;
	private Long persistenceid;
	private String fecha;
	private String nombre_prueba;
	private String lugar_prueba;
	private String tipo_prueba;
	private String entrada;
	private String salida;
	private String horario;
	private String tipoprueba_PID;
	private Boolean asistencia;
	
	
	public Boolean getAsistencia() {
		return asistencia;
	}
	public void setAsistencia(Boolean asistencia) {
		this.asistencia = asistencia;
	}
	public String getTipoprueba_PID() {
		return tipoprueba_PID;
	}
	public void setTipoprueba_PID(String tipoprueba_PID) {
		this.tipoprueba_PID = tipoprueba_PID;
	}
	public String getHorario() {
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}
	public String getNombre_prueba() {
		return nombre_prueba;
	}
	public void setNombre_prueba(String nombre_prueba) {
		this.nombre_prueba = nombre_prueba;
	}
	public String getLugar_prueba() {
		return lugar_prueba;
	}
	public void setLugar_prueba(String lugar_prueba) {
		this.lugar_prueba = lugar_prueba;
	}
	public String getTipo_prueba() {
		return tipo_prueba;
	}
	public void setTipo_prueba(String tipo_prueba) {
		this.tipo_prueba = tipo_prueba;
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
	private List<Map<String, Object>> Aspirantes;
	
	
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Long getSession_pid() {
		return session_pid;
	}
	public void setSession_pid(Long session_pid) {
		this.session_pid = session_pid;
	}
	public String getUsername() {
		return username;
	}
	public List<Map<String, Object>> getAspirantes() {
		return Aspirantes;
	}
	public void setAspirantes(List<Map<String, Object>> aspirantes) {
		Aspirantes = aspirantes;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Boolean getIseliminado() {
		return iseliminado;
	}
	public void setIseliminado(Boolean iseliminado) {
		this.iseliminado = iseliminado;
	}
	public Long getPersistenceid() {
		return persistenceid;
	}
	public void setPersistenceid(Long persistenceid) {
		this.persistenceid = persistenceid;
	}
	
}
