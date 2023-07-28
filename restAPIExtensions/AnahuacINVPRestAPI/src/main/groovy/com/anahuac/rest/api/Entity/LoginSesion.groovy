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
	@XmlElement
	private String aplicacion_temp;
	@XmlElement
	private String entrada_temp;
	@XmlElement
	private String salida_temp;
	@XmlElement
	private String nombre_temp;
	@XmlElement
	private Long idsesion_temp;
	
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
	public String getAplicacion_temp() {
		return aplicacion_temp;
	}
	public void setAplicacion_temp(String aplicacion_temp) {
		this.aplicacion_temp = aplicacion_temp;
	}
	public String getEntrada_temp() {
		return entrada_temp;
	}
	public void setEntrada_temp(String entrada_temp) {
		this.entrada_temp = entrada_temp;
	}
	public String getSalida_temp() {
		return salida_temp;
	}
	public void setSalida_temp(String salida_temp) {
		this.salida_temp = salida_temp;
	}
	public String getNombre_temp() {
		return nombre_temp;
	}
	public void setNombre_temp(String nombre_temp) {
		this.nombre_temp = nombre_temp;
	}
	public Long getIdsesion_temp() {
		return idsesion_temp;
	}
	public void setIdsesion_temp(Long idsesion_temp) {
		this.idsesion_temp = idsesion_temp;
	}
}
