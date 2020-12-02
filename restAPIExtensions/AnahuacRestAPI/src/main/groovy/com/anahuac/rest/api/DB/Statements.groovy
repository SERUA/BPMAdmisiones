package com.anahuac.rest.api.DB

class Statements {
	public static final String DELETE_CATGESTIONESCOLAR="UPDATE CATGESTIONESCOLAR SET ISELIMINADO=true WHERE persistenceid=?";
	
	public static final String INSERT_CATLICENCIATURA="INSERT INTO CATLICENCIATURA (CLAVE,DESCRIPCION,FECHACREACION,ISELIMINADO,PERSISTENCEVERSION,USUARIOCREACION,PERSISTENCEID) VALUES (?,?,?,?,?,?,?);"
	
	public static final String GET_DETALLESOLICITUD="select IdBanner,ObservacionesRechazo,ObservacionesListaRoja,ObservacionesCambio,ordenpago  from detallesolicitud where caseid=(select concat(caseid,'') from SOLICITUDDEADMISION where correoelectronico=? limit 1)"
	
	public static final String GET_TASK_USERID_NAME="WITH filter (userid,taskname ) AS (VALUES (?,? ) ) SELECT DISTINCT pi.tenantid, pi.id, pi.name, pi.startedby, fi.name AS taskname FROM process_instance pi INNER JOIN public.flownode_instance fi ON pi.id = fi.rootcontainerid AND fi.name=(SELECT taskname FROM filter) AND (fi.assigneeid=0 OR fi.assigneeid=(SELECT userid FROM filter)) AND fi.statename = 'ready'LEFT JOIN public.actor actor ON actor.id=fi.actorid LEFT JOIN public.actormember amember ON amember.actorid=actor.id LEFT JOIN public.role rol ON rol.id=amember.roleid LEFT JOIN public.group_ grupo ON grupo.id=amember.groupid LEFT JOIN public.user_ usuario ON usuario.id=amember.userid INNER JOIN public.user_membership um ON (um.roleid=rol.id OR  grupo.id=um.groupid) AND (amember.groupid>0 OR  amember.roleid>0) AND um.userid= (SELECT userid FROM filter)"
	
	public static final String INSERT_CAT_NOTIFICACIONES_FIRMA="INSERT INTO CATNOTIFICACIONESFIRMA (PERSISTENCEID, CARGO, CORREO, GRUPO, NOMBRECOMPLETO, PERSISTENCEVERSION, SHOWCARGO, SHOWCORREO, SHOWGRUPO, SHOWTELEFONO, SHOWTITULO, TELEFONO, TITULO) VALUES ((SELECT count(persistenceid)+1 from CATNOTIFICACIONESFIRMA ), ?, ?, ?, ?, 0, ?, ?, ?, ?, ?, ?, ?) RETURNING persistenceid;";
	
	public static final String UPDATE_CAT_NOTIFICACIONES_FIRMA="UPDATE CATNOTIFICACIONESFIRMA SET  CARGO = ?, CORREO = ?, GRUPO = ?, NOMBRECOMPLETO = ?,  SHOWCARGO = ?, SHOWCORREO = ?, SHOWGRUPO = ?, SHOWTELEFONO = ?, SHOWTITULO = ?, TELEFONO = ?, TITULO = ? WHERE PERSISTENCEID = ?;"
	
	public static final String GET_CAT_NOTIFICACION_FIRMA="SELECT PERSISTENCEID, CARGO, CORREO, GRUPO, NOMBRECOMPLETO, PERSISTENCEVERSION, SHOWCARGO, SHOWCORREO, SHOWGRUPO, SHOWTELEFONO, SHOWTITULO, TELEFONO, TITULO FROM CATNOTIFICACIONESFIRMA;"
	
	public static final String GET_CAMPUS_ID_FROM_CLAVE="SELECT persistenceid as campus_id FROM CATCAMPUS where grupoBonita=? limit 1 "
}
