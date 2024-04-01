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
