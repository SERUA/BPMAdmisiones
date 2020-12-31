package com.anahuac.rest.api.Entity.Custom

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatEstados
import com.anahuac.catalogos.CatPais
import com.anahuac.rest.api.Entity.db.CatTipoPrueba
import com.anahuac.rest.api.Entity.db.Prueba
import com.anahuac.rest.api.Entity.db.Responsable

class PruebaCustom extends Prueba {
	private List<ResponsableCustom> psicologos;
	private CatCampus campus;
	private CatPais pais;
	private CatEstados estado;
	private CatTipoPrueba tipo;
	private Boolean cambioDuracion;
	
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
	
	@Override
	public boolean equals(Object arg0) {
		Boolean part1=arg0 != null;
		Boolean part2=arg0 instanceof PruebaCustom;
		Boolean part3=((PruebaCustom) arg0).getPersistenceId().equals(this.getPersistenceId());
		return (part1 && part2 && part3);
	}
	
}
