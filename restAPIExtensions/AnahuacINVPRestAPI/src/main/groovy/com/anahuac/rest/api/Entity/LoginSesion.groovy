package com.anahuac.rest.api.Entity

import javax.xml.bind.annotation.XmlElement

class LoginSesion {
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String username;
	@XmlElement
	private String aplicacion;
	@XmlElement
	private String entrada;
	@XmlElement
	private String salida;
	@XmlElement
	private String nombre_prueba;
	@XmlElement
	private String nombre_sesion;
	@XmlElement
	private Long id_prueba;
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
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
	public String getNombre_prueba() {
		return nombre_prueba;
	}
	public void setNombre_prueba(String nombre_prueba) {
		this.nombre_prueba = nombre_prueba;
	}
	public String getNombre_sesion() {
		return nombre_sesion;
	}
	public void setNombre_sesion(String nombre_sesion) {
		this.nombre_sesion = nombre_sesion;
	}
	public Long getId_prueba() {
		return id_prueba;
	}
	public void setId_prueba(Long id_prueba) {
		this.id_prueba = id_prueba;
	}


}
