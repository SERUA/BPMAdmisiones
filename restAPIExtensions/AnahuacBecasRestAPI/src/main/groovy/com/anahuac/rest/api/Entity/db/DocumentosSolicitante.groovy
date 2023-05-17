package com.anahuac.rest.api.Entity.db

class DocumentosSolicitante {
	private CatManejoDocumentos catManejoDocumentos;
	private String urlDocumento;
	private Long caseId;
	private Long catManejoDocumentos_id;
	
	public CatManejoDocumentos getCatManejoDocumentos() {
		return catManejoDocumentos;
	}
	public void setCatManejoDocumentos(CatManejoDocumentos catManejoDocumentos) {
		this.catManejoDocumentos = catManejoDocumentos;
	}
	public String getUrlDocumento() {
		return urlDocumento;
	}
	public void setUrlDocumento(String urlDocumento) {
		this.urlDocumento = urlDocumento;
	}
	public Long getCaseId() {
		return caseId;
	}
	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}
	public Long getCatManejoDocumentos_id() {
		return catManejoDocumentos_id;
	}
	public void setCatManejoDocumentos_id(Long catManejoDocumentos_id) {
		this.catManejoDocumentos_id = catManejoDocumentos_id;
	}
}
