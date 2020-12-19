package com.anahuac.rest.api.Entity.Custom

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatEstados
import com.anahuac.catalogos.CatPais
import com.anahuac.rest.api.Entity.db.CatTipoPrueba
import com.anahuac.rest.api.Entity.db.Prueba
import com.anahuac.rest.api.Entity.db.CatPsicologo

class PruebaCustom extends Prueba {
	private List<CatPsicologo> psicologos;
	private CatCampus campus;
	private CatPais pais;
	private CatEstados estado;
	private CatTipoPrueba tipo;
	
	public CatTipoPrueba getTipo() {
		return tipo;
	}
	public void setTipo(CatTipoPrueba tipo) {
		this.tipo = tipo;
	}
	public List<CatPsicologo> getPsicologos() {
		return psicologos;
	}
	public void setPsicologos(List<CatPsicologo> psicologos) {
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
}
