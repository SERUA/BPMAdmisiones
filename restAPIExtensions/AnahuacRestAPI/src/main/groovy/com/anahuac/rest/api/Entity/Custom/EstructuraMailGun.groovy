package com.anahuac.rest.api.Entity.Custom

class EstructuraMailGun {
	
	private String to;
	private String cc;
	private String subject;
	private String body;
	private Long caseid;
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Long getCaseid() {
		return caseid;
	}
	public void setCaseid(Long caseid) {
		this.caseid = caseid;
	}


	
}