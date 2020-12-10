package com.anahuac.rest.api.DB

class Statements {
	public static final String DELETE_CATGESTIONESCOLAR="UPDATE CATGESTIONESCOLAR SET ISELIMINADO=true WHERE persistenceid=?";
	
	public static final String INSERT_CATLICENCIATURA="INSERT INTO CATLICENCIATURA (CLAVE,DESCRIPCION,FECHACREACION,ISELIMINADO,PERSISTENCEVERSION,USUARIOCREACION,PERSISTENCEID) VALUES (?,?,?,?,?,?,?);"
	
	public static final String GET_DETALLESOLICITUD="select IdBanner,ObservacionesRechazo,ObservacionesListaRoja,ObservacionesCambio,ordenpago  from detallesolicitud where caseid=(select concat(caseid,'') from SOLICITUDDEADMISION where correoelectronico=? limit 1)"
	
	public static final String GET_TASK_USERID_NAME="WITH filter (userid,taskname ) AS (VALUES (?,? ) ) SELECT DISTINCT pi.tenantid, pi.id, pi.name, pi.startedby, fi.name AS taskname FROM process_instance pi INNER JOIN public.flownode_instance fi ON pi.id = fi.rootcontainerid AND fi.name=(SELECT taskname FROM filter) AND (fi.assigneeid=0 OR fi.assigneeid=(SELECT userid FROM filter)) AND fi.statename = 'ready'LEFT JOIN public.actor actor ON actor.id=fi.actorid LEFT JOIN public.actormember amember ON amember.actorid=actor.id LEFT JOIN public.role rol ON rol.id=amember.roleid LEFT JOIN public.group_ grupo ON grupo.id=amember.groupid LEFT JOIN public.user_ usuario ON usuario.id=amember.userid INNER JOIN public.user_membership um ON (um.roleid=rol.id OR  grupo.id=um.groupid) AND (amember.groupid>0 OR  amember.roleid>0) AND um.userid= (SELECT userid FROM filter)"
	
	public static final String INSERT_CAT_NOTIFICACIONES_FIRMA="INSERT INTO CATNOTIFICACIONESFIRMA (PERSISTENCEID, CARGO, CORREO, GRUPO, NOMBRECOMPLETO, PERSISTENCEVERSION, SHOWCARGO, SHOWCORREO, SHOWGRUPO, SHOWTELEFONO, SHOWTITULO, TELEFONO, TITULO) VALUES ((SELECT count(persistenceid)+1 from CATNOTIFICACIONESFIRMA ), ?, ?, ?, ?, 0, ?, ?, ?, ?, ?, ?, ?) RETURNING persistenceid;";
	
	public static final String UPDATE_CAT_NOTIFICACIONES_FIRMA="UPDATE CATNOTIFICACIONESFIRMA SET  CARGO = ?, CORREO = ?, GRUPO = ?, NOMBRECOMPLETO = ?,  SHOWCARGO = ?, SHOWCORREO = ?, SHOWGRUPO = ?, SHOWTELEFONO = ?, SHOWTITULO = ?, TELEFONO = ?, TITULO = ? WHERE PERSISTENCEID = ?;"
	
	public static final String GET_CAT_NOTIFICACION_FIRMA="SELECT PERSISTENCEID, CARGO, CORREO, GRUPO, NOMBRECOMPLETO, PERSISTENCEVERSION, SHOWCARGO, SHOWCORREO, SHOWGRUPO, SHOWTELEFONO, SHOWTITULO, TELEFONO, TITULO FROM CATNOTIFICACIONESFIRMA [WHERE] [ORDERBY] [LIMITOFFSET];"
	
	public static final String GET_CAMPUS_ID_FROM_CLAVE="SELECT persistenceid as campus_id FROM CATCAMPUS where grupoBonita=? limit 1 "
	
	public static final String GET_IDBANNER_BY_IDBANNER="SELECT idbanner FROM DETALLESOLICITUD where idbanner=?;"
	
	public static final String GET_ASPIRANTES_EN_PROCESO="SELECT sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campus.descripcion         AS campus, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION        AS ingreso, estado.DESCRIPCION         AS estado, prepa.DESCRIPCION          AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita FROM SOLICITUDDEADMISION sda LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID [CAMPUS] LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID [PROGRAMA] LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID [INGRESO] LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID [ESTADO] LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID [BACHILLERATO] LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER [TIPOALUMNO] [WHERE] [ORDERBY] [LIMITOFFSET]";
	
	public static final String GET_SOLICITUDES_EN_PROCESO="SELECT sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campus.descripcion         AS campus, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION        AS ingreso, estado.DESCRIPCION         AS estado, prepa.DESCRIPCION          AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita FROM SOLICITUDDEADMISION sda LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID [CAMPUS] LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID [PROGRAMA] LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID [INGRESO] LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID [ESTADO] LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID [BACHILLERATO] LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER [TIPOALUMNO] [WHERE] [ORDERBY] [LIMITOFFSET]";
	
	/*************DANIEL CERVANTES***********************/
	public static final String GET_CATCAMPUS="SELECT * FROM CATCAMPUS [WHERE] [ORDERBY] [LIMITOFFSET]"
	/*************DANIEL CERVANTES FIN*******************/
	/*************JUAN ESQUER***********************/
	public static final String GET_CATTITULO="SELECT * FROM CATTITULO  [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATESTADOCIVIL ="SELECT * FROM CATESTADOCIVIL  [WHERE] [ORDERBY] [LIMITOFFSET]"
	/*************JUAN ESQUER FIN*******************/
	/***********************JESUS OSUNA********************************/
	public static final String GET_CATESCOLARIDAD = "SELECT * FROM CATESCOLARIDAD [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATSEXO = "SELECT * FROM CATSEXO [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATPARENTESCO = "SELECT * FROM CATPARENTESCO [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATDESCUENTOS = "SELECT c.*, b.descripcion as bachilleratos FROM CATDESCUENTOS c left join CATBACHILLERATOS b on b.PERSISTENCEID = c.CATBACHILLERATOS_PID [BACHILLERATO]  [WHERE] [ORDERBY] [LIMITOFFSET]"
	/***********************JESUS OSUNA FIN********************************/
	/***********************ERIC ROSAS*******************************/
	public static final String GET_CATPERIODO = "SELECT * FROM CATPERIODO [WHERE] [ORDERBY] [LIMITOFFSET]"
	
	/***********************ERIC ROSAS FIN********************************/
}
