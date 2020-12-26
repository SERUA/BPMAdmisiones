package com.anahuac.rest.api.DB

class Statements {
	public static final String DELETE_CATGESTIONESCOLAR="UPDATE CATGESTIONESCOLAR SET ISELIMINADO=true WHERE persistenceid=?";
	
	public static final String INSERT_CATLICENCIATURA="INSERT INTO CATLICENCIATURA (CLAVE,DESCRIPCION,FECHACREACION,ISELIMINADO,PERSISTENCEVERSION,USUARIOCREACION,PERSISTENCEID) VALUES (?,?,?,?,?,?,?);"
	
	public static final String GET_DETALLESOLICITUD="select IdBanner,ObservacionesRechazo,ObservacionesListaRoja,ObservacionesCambio,ordenpago  from detallesolicitud where caseid=(select concat(caseid,'') from SOLICITUDDEADMISION where correoelectronico=?  ORDER BY persistenceid desc limit 1)"
	
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
	public static final String GET_CATPAIS="SELECT * FROM CATPAIS [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATESTADOS="SELECT * FROM CATESTADOS [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATBACHILLERATO="SELECT * FROM CATBACHILLERATOS  [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATCIUDAD="SELECT c.*, b.descripcion as campus FROM CATCIUDAD c left join CATCAMPUS b on b.PERSISTENCEID = c.CAMPUS_PID  [WHERE] [ORDERBY] [LIMITOFFSET]"
	/*************DANIEL CERVANTES FIN*******************/
	/*************JUAN ESQUER***********************/
	public static final String GET_CATTITULO="SELECT * FROM CATTITULO  [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATESTADOCIVIL ="SELECT * FROM CATESTADOCIVIL  [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATTIPOPRUEBA = "SELECT * FROM CATTIPOPRUEBA [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATPSICOLOGO = "SELECT * FROM CATPSICOLOGO  [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_USER_BONITA = "SELECT u.id, u.firstname, u.lastname, g.name as grupo, r.name as rol FROM user_ u INNER JOIN user_membership um ON um.userid=u.id INNER JOIN role r ON r.id=um.roleid [ROLE] INNER JOIN group_ g ON g.id=um.groupid [GROUP] [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATGESTIONESCOLAR = "SELECT * FROM CATGESTIONESCOLAR  [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String INSERT_SESION="INSERT INTO SESIONES ( NOMBRE, DESCRIPCION, FECHA_INICIO, ISMEDICINA, PREPARATORIA_PID, ESTADO_PID, PAIS_PID, BORRADOR, CAMPUS_PID, TIPO) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?,?,?);";
	public static final String INSERT_PRUEBA="INSERT INTO PRUEBAS ( NOMBRE, APLICACION, ENTRADA, SALIDA, REGISTRADOS, ULTIMO_DIA_INSCRIPCION, CUPO, LUGAR, CAMPUS_PID, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, CODIGO_POSTAL, MUNICIPIO, PAIS_PID, ESTADO_PID, ISELIMINADO, SESION_PID, DURACION, DESCRIPCION, cattipoprueba_pid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?);"
	public static final String INSERT_RESPONSABLEDISPONIBLE="INSERT INTO RESPONSABLEDISPONIBLE ( HORARIO, DISPONIBLE, RESPONSABLEID, PRUEBA_PID) VALUES (?, ?, ?, ?);"
	public static final String GET_SESIONES_CALENDARIO="SELECT s.persistenceid as id, case when p.entrada is null then  concat(to_char( s.FECHA_INICIO, 'MM/DD/YYYY'),' 00:00' ) else concat(to_char( s.FECHA_INICIO, 'MM/DD/YYYY'),' ',p.entrada ) end as start_date, case when p.entrada is null then  concat(to_char( s.FECHA_INICIO, 'MM/DD/YYYY'),' 23:00' ) else concat(to_char( s.FECHA_INICIO, 'MM/DD/YYYY'),' ',p.salida) end as end_date, case when s.BORRADOR then concat(s.nombre, ' <i class=\"small material-icons\">do_not_disturb</i>' ) else concat(s.nombre, ' <i class=\"small material-icons\">language</i>' ) end as text, case when s.fecha_inicio<now() then '#969696'else '#ff5900' end as color FROM SESIONES s LEFT JOIN PRUEBAS p on p.SESION_PID =s.persistenceid and (SELECT fa.persistenceid FROM pruebas fa WHERE fa.sesion_pid= s.persistenceid LIMIT 1 )=p.persistenceid [WHERE]"
	public static final String GET_SESION_ID="SELECT s.* FROM SESIONES s WHERE persistenceId=?"
	public static final String GET_PRUEBAS_SESION_PID="SELECT p.*, r.horario, r.RESPONSABLEID , r.DISPONIBLE, c.descripcion as tipo  FROM PRUEBAS p LEFT JOIN RESPONSABLEDISPONIBLE  r on r.PRUEBA_PID =p.PERSISTENCEID LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid where p.SESION_PID =?"
	/*************JUAN ESQUER FIN*******************/
	/***********************JESUS OSUNA********************************/
	public static final String GET_CATESCOLARIDAD = "SELECT * FROM CATESCOLARIDAD [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATSEXO = "SELECT * FROM CATSEXO [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATPARENTESCO = "SELECT * FROM CATPARENTESCO [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATDESCUENTOS = "SELECT c.*, b.descripcion as bachilleratos FROM CATDESCUENTOS c left join CATBACHILLERATOS b on b.PERSISTENCEID = c.CATBACHILLERATOS_PID [BACHILLERATO] LEFT JOIN CATCAMPUS a on a.grupobonita=c.campus [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATNACIONALIDAD = "SELECT * FROM CATNACIONALIDAD [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATGENERICO = "SELECT * FROM [CATALOGO] [WHERE] [ORDERBY] [LIMITOFFSET]"
	
	public static final String GET_SESIONESASPIRANTE = "SELECT SA.*,S.fecha_inicio  from SESIONASPIRANTE as SA  LEFT JOIN SESIONES as S on SA.sesion_pid = S.persistenceid WHERE SA.ISELIMINADO = FALSE AND SA.session_pid = ? "
	public static final String GET_SESIONESCALENDARIZADAS = "SELECT P.nombre, S.fecha_inicio, S.tipo, P.persistenceid as pruebas_id, S.persistenceid as sesiones_id, P.lugar, P.registrados as alumnos_generales FROM PRUEBAS as P  LEFT JOIN SESIONES as S on P.sesion_pid = S.persistenceid LEFT JOIN RESPONSABLEDISPONIBLE as RD on RD.prueba_pid = P.persistenceid WHERE P.ISELIMINADO = FALSE AND RD.responsableid = ? [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String GET_ASPIRANTESDELASESION = "SELECT sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, campus.descripcion AS campus, gestionescolar.DESCRIPCION AS licenciatura,  prepa.DESCRIPCION AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita FROM SOLICITUDDEADMISION sda LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER  WHERE sda.correoelectronico = ?";
	/***********************JESUS OSUNA FIN********************************/
	/*************JOSÉ GARCÍA***********************/
	public static final String GET_CATPROPEDEUTICO ="SELECT p.*, c.descripcion AS campus FROM CATPROPEDEUTICO p INNER JOIN CATCAMPUS c ON p.CAMPUS_PID = c.PERSISTENCEID  [WHERE] [ORDERBY] [LIMITOFFSET]";
	/*************JOSÉ GARCÍA FIN***********************/
	/***********************ERIC ROSAS*******************************/
	public static final String GET_CATPERIODO = "SELECT * FROM CATPERIODO [WHERE] [ORDERBY] [LIMITOFFSET]"
	/***********************ERIC ROSAS FIN********************************/
}
