package com.anahuac.rest.api.DB

class CatNotificacionesCampus {
public static final String GET_CATNOTIFICACIONESCAMPUS="SELECT cn.codigo, cn.tipo_correo, cn.descripcion, case when cnc.header is not null and cnc.header!='' then cnc.header else cn.angulo_imagen_header end as header, case when cnc.footer is not null and cnc.footer!='' then cnc.footer else cn.texto_footer end as footer, cnc.catnotificacionesfirma_pid, campus.persistenceid AS catcampus_pid, cnc.copia FROM PSGRCATNOTIFICACIONES cn LEFT JOIN PSGRprocesocaso pc ON pc.case_id=cn.case_id LEFT JOIN psgrcatcampus campus ON campus.grupo_bonita=pc.campus LEFT JOIN psgrcatnotificacionescampus cnc on cnc.codigo=cn.codigo and cnc.catcampus_pid=campus.persistenceid WHERE campus.grupo_bonita=?"
	public static final String GET_CATNOTIFICACIONESCAMPUSCC="SELECT cn.codigo, cn.tipo_correo, cn.descripcion, case when cnc.header is not null and cnc.header!='' then cnc.header else cn.angulo_imagen_header end as header, case when cnc.footer is not null and cnc.footer!='' then cnc.footer else cn.texto_footer end as footer, cnc.catnotificacionesfirma_pid, campus.persistenceid AS catcampus_pid, cnc.copia FROM PSGRCATNOTIFICACIONES cn LEFT JOIN psgrprocesocaso pc ON pc.case_id=cn.case_id LEFT JOIN psgrcatcampus campus ON campus.grupo_bonita=pc.campus LEFT JOIN psgrcatnotificacionescampus cnc on cnc.codigo=cn.codigo and cnc.catcampus_pid=campus.persistenceid WHERE cn.codigo=? and campus.grupo_bonita=?"
	public static final String INSERT_CATNOTIFICACIONESCAMPUS="INSERT INTO PSGRCATNOTIFICACIONESCAMPUS  (CATNOTIFICACIONESFIRMA_PID, CODIGO ,COPIA ,FOOTER ,HEADER ,CATCAMPUS_PID ,PERSISTENCEID ,PERSISTENCEVERSION  ) values (?,?,?,?,?,?,case when (SELECT max(persistenceId)+1 from CATNOTIFICACIONESCAMPUS  ) is null then 1 else (SELECT max(persistenceId)+1 from CATNOTIFICACIONESCAMPUS  ) end,0)"
	public static final String UPDATE_CATNOTIFICACIONESCAMPUS="UPDATE PSGRCATNOTIFICACIONESCAMPUS SET  CATNOTIFICACIONESFIRMA_PID=?, COPIA =?,FOOTER =?,HEADER=?  WHERE CODIGO=? AND CATCAMPUS_PID=?"
	public static final String GET_CATNOTIFICACIONESCAMPUS_CODIGO_CAMPUS="SELECT * FROM PSGRCATNOTIFICACIONESCAMPUS  WHERE CODIGO=? AND CATCAMPUS_PID=?";
	
	private String codigo;
	private String tipo_correo;
	private String descripcion;
	private String header;
	private String footer;
	private Long catnotificacionesfirma_pid;
	private Long catcampus_pid;
	private String copia;
	
	public String getCopia() {
		return copia;
	}
	public void setCopia(String copia) {
		this.copia = copia;
	}
	public String getCodigo() {
		return codigo;
	}
	public String getTipo_correo() {
		return tipo_correo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public String getHeader() {
		return header;
	}
	public String getFooter() {
		return footer;
	}
	public Long getCatnotificacionesfirma_pid() {
		return catnotificacionesfirma_pid;
	}
	public Long getCatcampus_pid() {
		return catcampus_pid;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setTipo_correo(String tipo_correo) {
		this.tipo_correo = tipo_correo;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public void setFooter(String footer) {
		this.footer = footer;
	}
	public void setCatnotificacionesfirma_pid(Long catnotificacionesfirma_pid) {
		this.catnotificacionesfirma_pid = catnotificacionesfirma_pid;
	}
	public void setCatcampus_pid(Long catcampus_pid) {
		this.catcampus_pid = catcampus_pid;
	} 
	
}
