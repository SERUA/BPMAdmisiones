package com.anahuac.rest.api.Entity

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class Result {
	@XmlElement
	private String error_info;
	@XmlElement
	private String error;
	@XmlElement
	private Integer totalRegistros;
	@XmlElement
	private boolean success;
	@XmlElement
	private List<?> data;
	@XmlElement
	private List<?> additional_data;
	@XmlElement
	private String info;
	@XmlElement
	private int limit;
	@XmlElement
	private int offset;
	@XmlElement
	private int finalizados;
	@XmlElement
	private int proceso;
	@XmlElement
	private int iniciar;
	
	public int getFinalizados() {
		return finalizados;
	}

	public void setFinalizados(int finalizados) {
		this.finalizados = finalizados;
	}

	@XmlElement
	public int getProceso() {
		return proceso;
	}

	public void setProceso(int proceso) {
		this.proceso = proceso;
	}

	@XmlElement
	public int getIniciar() {
		return iniciar;
	}

	public void setIniciar(int iniciar) {
		this.iniciar = iniciar;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getLimit() {
		return limit;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getError_info() {
		return error_info;
	}
	public void setError_info(String error_info) {
		this.error_info = error_info;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Integer getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public List<?> getData() {
		return data;
	}
	public void setData(List<?> data) {
		this.data = data;
	}
	public List<?> getAdditional_data() {
		return additional_data;
	}
	public void setAdditional_data(List<?> additional_data) {
		this.additional_data = additional_data;
	}
	
	@Override
	public String toString() {
		return "Result [error_info=" + error_info + ", error=" + error + ", totalRegistros=" + totalRegistros
				+ ", success=" + success + ", data=" + data + ", additional_data=" + additional_data + "]";
	}
	
	
}
