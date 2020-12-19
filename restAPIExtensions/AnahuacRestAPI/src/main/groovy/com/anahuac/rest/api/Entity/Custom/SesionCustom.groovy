package com.anahuac.rest.api.Entity.Custom

import com.anahuac.catalogos.CatBachilleratos
import com.anahuac.catalogos.CatEstados
import com.anahuac.rest.api.Entity.db.Sesion

class SesionCustom extends Sesion{
	private CatEstados estado;
	private com.anahuac.catalogos.CatPais pais;
	private CatBachilleratos preparatoria;
	private List<PruebaCustom> pruebas;
	public CatEstados getEstado() {
		return estado;
	}
	public void setEstado(CatEstados estado) {
		this.estado = estado;
	}
	public com.anahuac.catalogos.CatPais getPais() {
		return pais;
	}
	public void setPais(com.anahuac.catalogos.CatPais pais) {
		this.pais = pais;
	}
	public CatBachilleratos getPreparatoria() {
		return preparatoria;
	}
	public void setPreparatoria(CatBachilleratos preparatoria) {
		this.preparatoria = preparatoria;
	}
	public List<PruebaCustom> getPruebas() {
		return pruebas;
	}
	public void setPruebas(List<PruebaCustom> pruebas) {
		this.pruebas = pruebas;
	}
	
}

