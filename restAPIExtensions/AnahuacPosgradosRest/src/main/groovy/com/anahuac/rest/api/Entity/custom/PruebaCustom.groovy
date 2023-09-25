package com.anahuac.rest.api.Entity.custom

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatEstados
import com.anahuac.catalogos.CatPais
import com.anahuac.rest.api.Entity.db.CatTipoPrueba

class PruebaCustom {
	private List<ResponsableCustom> psicologos;
	private CatCampus campus;
	private CatPais pais;
	private CatEstados estado;
	private CatTipoPrueba tipo;
	private Boolean cambioDuracion;
	private String asistencia;
	private String responsables;
	
	public Boolean getCambioDuracion() {
		return cambioDuracion;
	}
	public void setCambioDuracion(Boolean cambioDuracion) {
		this.cambioDuracion = cambioDuracion;
	}
	public CatTipoPrueba getTipo() {
		return tipo;
	}
	public void setTipo(CatTipoPrueba tipo) {
		this.tipo = tipo;
	}
	public List<ResponsableCustom> getPsicologos() {
		return psicologos;
	}
	public void setPsicologos(List<ResponsableCustom> psicologos) {
		this.psicologos = psicologos;
	}
	public CatCampus getCampus() {
		return campus;
	}
	public void setCampus(CatCampus campus) {
		this.campus = campus;
	}
	public CatPais getPais() {
		return pais;
	}
	public void setPais(CatPais pais) {
		this.pais = pais;
	}
	public CatEstados getEstado() {
		return estado;
	}
	public void setEstado(CatEstados estado) {
		this.estado = estado;
	}
	
	
	
	public String getResponsables() {
		return responsables;
	}
	public void setResponsables(String responsables) {
		this.responsables = responsables;
	}
	public String getAsistencia() {
		return asistencia;
	}
	public void setAsistencia(String asistencia) {
		this.asistencia = asistencia;
	}
	@Override
	public boolean equals(Object arg0) {
		Boolean part1=arg0 != null;
		Boolean part2=arg0 instanceof PruebaCustom;
		Boolean part3=((PruebaCustom) arg0).getPersistenceId().equals(this.getPersistenceId());
		return (part1 && part2 && part3);
	}
	
}
