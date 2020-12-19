package com.anahuac.rest.api.DB

class StatementsBachillerato {
	public static final String GET = "select persistenceid as persistenceid_string, ciudad, estado,descripcion, pais, pertenecered from catbachilleratos where iseliminado=false and isenabled=true";
	public static final String INSERT = "INSERT INTO CATBACHILLERATOS (PERSISTENCEID,CIUDAD,CLAVE,DESCRIPCION,ESTADO,FECHACREACION,ISELIMINADO,ISENABLED,PAIS,PERSISTENCEVERSION,PERTENECERED,USUARIOBANNER,FECHAIMPORTACION) VALUES ((SELECT CASE WHEN MAX(PERSISTENCEID) is null then 1 else MAX(PERSISTENCEID)+1 end AS id FROM CATBACHILLERATOS),?,?,?,?,NOW(),false,true,?,0,?,?,NOW())"
	public static final String UPDATE = "UPDATE CATBACHILLERATOS SET CIUDAD=?, CLAVE=?, DESCRIPCION=?, ESTADO=?, PAIS=?, USUARIOBANNER=?, ISELIMINADO=?, ISENABLED=? WHERE PERSISTENCEID=?"
	public static final String GET_DESCUENTOS_CIUDAD_BACHILLERATO="SELECT c.* FROM CatDescuentos c WHERE CURRENT_TIMESTAMP BETWEEN  TO_DATE(c.inicioVigencia,'YYYY-MM-DD HH24:MI:SS') AND TO_DATE(c.finVigencia,'YYYY-MM-DD HH24:MI:SS') AND c.campus = ? AND (c.bachillerato = ? OR c.ciudad = ?) ORDER BY c.descuento DESC";
	public static final String GET_DESCUENTOS_CAMPANA="SELECT c.* FROM CatDescuentos c WHERE c.tipo = 'Campaña'AND CURRENT_TIMESTAMP BETWEEN  TO_DATE(c.inicioVigencia,'YYYY-MM-DD HH24:MI:SS') AND TO_DATE(c.finVigencia,'YYYY-MM-DD HH24:MI:SS') AND c.campus = ? ORDER BY c.persistenceId ASC"
	
	public static final String INSERT_CATBITACORACOMENTARIOS="INSERT INTO CATBITACORACOMENTARIOS (persistenceid, comentario, fechacreacion, iseliminado, modulo, persistenceversion, usuario, usuariocomentario ) VALUES (CASE WHEN (SELECT MAX (persistenceid)+1 FROM CATBITACORACOMENTARIOS) is null then 1 else (SELECT MAX (persistenceid)+1 FROM CATBITACORACOMENTARIOS) END, ?, ?, ?,?, 0, ?, ? )"
	
	public static final String GET_CATBITACORACOMENTARIOS="SELECT bitacora.* FROM CATBITACORACOMENTARIOS bitacora LEFT JOIN SOLICITUDDEADMISION  sda ON sda.CORREOELECTRONICO=bitacora.USUARIOCOMENTARIO LEFT JOIN CATCAMPUS campus on sda.CATCAMPUS_PID=campus.persistenceid [WHERE] [ORDERBY] [LIMITOFFSET]";
}
