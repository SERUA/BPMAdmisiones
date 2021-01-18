package com.anahuac.rest.api.DB

class Statements {
	public static final String DELETE_CATGESTIONESCOLAR="UPDATE CATGESTIONESCOLAR SET ISELIMINADO=true WHERE persistenceid=?";
	
	public static final String INSERT_CATLICENCIATURA="INSERT INTO CATLICENCIATURA (CLAVE,DESCRIPCION,FECHACREACION,ISELIMINADO,PERSISTENCEVERSION,USUARIOCREACION,PERSISTENCEID) VALUES (?,?,?,?,?,?,?);"
	
	public static final String GET_DETALLESOLICITUD="select IdBanner,ObservacionesRechazo,ObservacionesListaRoja,ObservacionesCambio,ordenpago  from detallesolicitud where caseid=(select concat(caseid,'') from SOLICITUDDEADMISION where correoelectronico=?  ORDER BY persistenceid desc limit 1)"
	
	public static final String GET_CAT_NOTIFICACIONES_CAMPUS_PROCESO_CODIGO="SELECT * from catnotificaciones where caseid=(SELECT caseid FROM procesocaso where campus = ? and proceso='CatNotificaciones') and codigo=?"
	
	public static final String GET_FIRMA_PERSISTENCEID="SELECT * FROM CATNOTIFICACIONESFIRMA where persistenceId=?"
	
	public static final String GET_TASK_USERID_NAME="WITH filter (userid,taskname ) AS (VALUES (?,? ) ) SELECT DISTINCT pi.tenantid, pi.id, pi.name, pi.startedby, fi.name AS taskname FROM process_instance pi INNER JOIN public.flownode_instance fi ON pi.id = fi.rootcontainerid AND fi.name=(SELECT taskname FROM filter) AND (fi.assigneeid=0 OR fi.assigneeid=(SELECT userid FROM filter)) AND fi.statename = 'ready'LEFT JOIN public.actor actor ON actor.id=fi.actorid LEFT JOIN public.actormember amember ON amember.actorid=actor.id LEFT JOIN public.role rol ON rol.id=amember.roleid LEFT JOIN public.group_ grupo ON grupo.id=amember.groupid LEFT JOIN public.user_ usuario ON usuario.id=amember.userid INNER JOIN public.user_membership um ON (um.roleid=rol.id OR  grupo.id=um.groupid) AND (amember.groupid>0 OR  amember.roleid>0) AND um.userid= (SELECT userid FROM filter)"
	
	public static final String INSERT_CAT_NOTIFICACIONES_FIRMA="INSERT INTO CATNOTIFICACIONESFIRMA (PERSISTENCEID, CARGO, CORREO, GRUPO, NOMBRECOMPLETO, PERSISTENCEVERSION, SHOWCARGO, SHOWCORREO, SHOWGRUPO, SHOWTELEFONO, SHOWTITULO, TELEFONO, TITULO, CAMPUS, FACEBOOK, TWITTER, APELLIDO, BANNER, isEliminar) VALUES ((SELECT count(persistenceid)+1 from CATNOTIFICACIONESFIRMA ), ?, ?, ?, ?, 0, ?, ?, ?, ?, ?, ?, ?, ? ,?,?,?,?,false) RETURNING persistenceid;";
	
	public static final String UPDATE_CAT_NOTIFICACIONES_FIRMA="UPDATE CATNOTIFICACIONESFIRMA SET  CARGO = ?, CORREO = ?, GRUPO = ?, NOMBRECOMPLETO = ?,  SHOWCARGO = ?, SHOWCORREO = ?, SHOWGRUPO = ?, SHOWTELEFONO = ?, SHOWTITULO = ?, TELEFONO = ?, TITULO = ?, CAMPUS = ?, FACEBOOK = ?, TWITTER = ?, APELLIDO = ?, BANNER = ? WHERE PERSISTENCEID = ?;"
	
	public static final String DELETE_CAT_NOTIFICACIONES_FIRMA="UPDATE CATNOTIFICACIONESFIRMA SET  ISELIMINAR = true WHERE PERSISTENCEID = ?;"
	
	public static final String GET_CAT_NOTIFICACION_FIRMA="SELECT PERSISTENCEID, CARGO, CORREO, GRUPO, NOMBRECOMPLETO, PERSISTENCEVERSION, SHOWCARGO, SHOWCORREO, SHOWGRUPO, SHOWTELEFONO, SHOWTITULO, TELEFONO, TITULO, CAMPUS, FACEBOOK, TWITTER, APELLIDO, BANNER FROM CATNOTIFICACIONESFIRMA [WHERE] [ORDERBY] [LIMITOFFSET];"
	
	public static final String GET_CAMPUS_ID_FROM_CLAVE="SELECT persistenceid as campus_id FROM CATCAMPUS where grupoBonita=? limit 1 "
	
	public static final String GET_IDBANNER_BY_IDBANNER="SELECT idbanner FROM DETALLESOLICITUD where idbanner=?;"
	
	public static final String GET_ASPIRANTES_EN_PROCESO=     "SELECT sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia FROM SOLICITUDDEADMISION sda LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID [CAMPUS] LEFT JOIN catcampus campusEstudio ON campusEstudio.persistenceid=sda.catcampusestudio_pid LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID [PROGRAMA] LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID [INGRESO] LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID [ESTADO] LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID [BACHILLERATO] LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER  LEFT JOIN CatTipoAdmision  as TA on TA.PERSISTENCEID = da.CATTIPOADMISION_PID LEFT JOIN CatResidencia  as R on R.PERSISTENCEID = da.CATRESIDENCIA_PID LEFT JOIN CatTipoAlumno  as TAL on TAL.PERSISTENCEID = da.CATTIPOALUMNO_PID LEFT JOIN CatCampus catcampus ON catcampus .persistenceid=sda.CATCAMPUS_PID [TIPOALUMNO] [WHERE] [ORDERBY] [LIMITOFFSET]";
	
	public static final String GET_ASPIRANTES_EN_PROCESO_FECHAS="SELECT sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, catcampus.descripcion as transferencia, STRING_AGG( tipo.descripcion || '  ' || p.aplicacion || '   ' || case when tipo.persistenceid=1 then rd.horario  else p.entrada ||' - '||p.salida end, ',' ORDER BY tipo.descripcion , p.aplicacion, horario ) fechasExamenes FROM SOLICITUDDEADMISION sda LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID [CAMPUS]  LEFT JOIN catcampus campusEstudio ON campusEstudio.persistenceid=sda.catcampusestudio_pid LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID   [PROGRAMA] LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID [INGRESO] LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID  [ESTADO] LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID  [BACHILLERATO] LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER LEFT JOIN CatTipoAdmision  as TA on TA.PERSISTENCEID = da.CATTIPOADMISION_PID LEFT JOIN CatResidencia  as R on R.PERSISTENCEID = da.CATRESIDENCIA_PID LEFT JOIN CatTipoAlumno  as TAL on TAL.PERSISTENCEID = da.CATTIPOALUMNO_PID LEFT JOIN SolicitudDeAdmision as SA on SA.PERSISTENCEID = sda.CATCAMPUS_PID LEFT JOIN CatCampus catcampus ON catcampus .persistenceid=sda.CATCAMPUS_PID left join sesionaspirante saw  on  saw. username= sda.correoelectronico LEFT JOIN sesiones s on s.persistenceid= saw.sesiones_pid left join pruebas p on p.sesion_pid=saw.sesiones_pid left join cattipoprueba tipo on tipo.persistenceid=p.cattipoprueba_pid left join responsabledisponible rd on rd.persistenceid=saw.responsabledisponible_pid [WHERE] GROUP BY sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion , campus.descripcion , gestionescolar.DESCRIPCION , periodo.DESCRIPCION , estado.DESCRIPCION , sda.bachillerato, prepa.DESCRIPCION , sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion  , R.descripcion, catcampus.descripcion [TIPOALUMNO]  [ORDERBY] [LIMITOFFSET]"

	public static final String GET_ASPIRANTES_EN_PROCESO_FECHAS2= " SELECT COUNT(sda.persistenceid) as registros FROM SOLICITUDDEADMISION sda LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID [CAMPUS]  LEFT JOIN catcampus campusEstudio ON campusEstudio.persistenceid=sda.catcampusestudio_pid LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID   [PROGRAMA] LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID [INGRESO] LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID  [ESTADO] LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID  [BACHILLERATO] LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER LEFT JOIN CatTipoAdmision  as TA on TA.PERSISTENCEID = da.CATTIPOADMISION_PID LEFT JOIN CatResidencia  as R on R.PERSISTENCEID = da.CATRESIDENCIA_PID LEFT JOIN CatTipoAlumno  as TAL on TAL.PERSISTENCEID = da.CATTIPOALUMNO_PID LEFT JOIN SolicitudDeAdmision as SA on SA.PERSISTENCEID = sda.CATCAMPUS_PID LEFT JOIN CatCampus catcampus ON catcampus .persistenceid=sda.CATCAMPUS_PID left join sesionaspirante saw  on  saw. username= sda.correoelectronico LEFT JOIN sesiones s on s.persistenceid= saw.sesiones_pid left join pruebas p on p.sesion_pid=saw.sesiones_pid left join cattipoprueba tipo on tipo.persistenceid=p.cattipoprueba_pid left join responsabledisponible rd on rd.persistenceid=saw.responsabledisponible_pid [TIPOALUMNO] [WHERE] "
		
	public static final String GET_SOLICITUDES_EN_PROCESO=    "SELECT sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita FROM SOLICITUDDEADMISION sda LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID [CAMPUS] LEFT JOIN catcampus campusEstudio ON campusEstudio.persistenceid=sda.catcampusestudio_pid LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID [PROGRAMA] LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID [INGRESO] LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID [ESTADO] LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID [BACHILLERATO] LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER [TIPOALUMNO] [WHERE] [ORDERBY] [LIMITOFFSET]";

	/*************DANIEL CERVANTES***********************/
	//public static final String GET_CATCAMPUS="SELECT * FROM CATCAMPUS [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATCAMPUS="SELECT  c.*, p.descripcion as pais ,e.descripcion as estado FROM CATCAMPUS c left join CATPAIS p ON c.PAIS_PID  = p.PERSISTENCEID  left join CATESTADOS e ON  e.PERSISTENCEID  = c.ESTADO_PID  [WHERE] [ORDERBY] [LIMITOFFSET]"
	//public static final String GET_CATCAMPUS="SELECT * FROM CATCAMPUS c left join CATPAIS p ON c.PAIS_PID  = p.PERSISTENCEID  left join CATESTADOS e ON  e.PERSISTENCEID  = c.ESTADO_PID  [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATPAIS="SELECT * FROM CATPAIS [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATESTADOS="SELECT * FROM CATESTADOS [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATBACHILLERATO="SELECT * FROM CATBACHILLERATOS  [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATCIUDAD="SELECT c.*, b.descripcion as campus FROM CATCIUDAD c left join CATCAMPUS b on b.PERSISTENCEID = c.CAMPUS_PID  [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATFILTRADOCATALOGOSAUTODESCRIPCION = "SELECT * FROM [CATALOGOAD] [WHERE] [ORDERBY] [LIMITOFFSET]"
	/*************DANIEL CERVANTES FIN*******************/
	/*************JUAN ESQUER***********************/
	public static final String GET_CATTITULO="SELECT * FROM CATTITULO  [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATESTADOCIVIL ="SELECT * FROM CATESTADOCIVIL  [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATTIPOPRUEBA = "SELECT * FROM CATTIPOPRUEBA [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATPSICOLOGO = "SELECT * FROM CATPSICOLOGO  [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_USER_BONITA = "SELECT u.id, u.firstname, u.lastname, g.name as grupo, r.name as rol FROM user_ u INNER JOIN user_membership um ON um.userid=u.id INNER JOIN role r ON r.id=um.roleid [ROLE] INNER JOIN group_ g ON g.id=um.groupid [GROUP] [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATGESTIONESCOLAR = "SELECT GE.*, campus.descripcion as nombreCampus FROM CATGESTIONESCOLAR as GE  LEFT JOIN catcampus campus ON campus.grupoBonita = GE.campus [CAMPUS]  [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String INSERT_SESION="INSERT INTO SESIONES ( persistenceid,persistenceversion,NOMBRE, DESCRIPCION, FECHA_INICIO, ISMEDICINA, PREPARATORIA_PID, ESTADO_PID, PAIS_PID, BORRADOR, CAMPUS_PID, TIPO,CIUDAD_PID) VALUES ( case when (SELECT max(persistenceId)+1 from SESIONES ) is null then 1 else (SELECT max(persistenceId)+1 from SESIONES) end,0,?, ?, ?, ?, ?, ?, ?, ?,?,?,?);";
	public static final String UPDATE_SESION="UPDATE SESIONES set NOMBRE=?, DESCRIPCION=?, FECHA_INICIO=?, ISMEDICINA=?, PREPARATORIA_PID=?, ESTADO_PID=?, PAIS_PID=?, BORRADOR=?, CAMPUS_PID=?, TIPO=?, CIUDAD_PID=? WHERE persistenceId=?"
	public static final String INSERT_PRUEBA="INSERT INTO PRUEBAS ( persistenceid,NOMBRE, APLICACION, ENTRADA, SALIDA, REGISTRADOS, ULTIMO_DIA_INSCRIPCION, CUPO, LUGAR, CAMPUS_PID, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, CODIGO_POSTAL, MUNICIPIO, PAIS_PID, ESTADO_PID, ISELIMINADO, SESION_PID, DURACION, DESCRIPCION, cattipoprueba_pid) VALUES ( case when (SELECT max(persistenceId)+1 from PRUEBAS ) is null then 1 else (SELECT max(persistenceId)+1 from PRUEBAS) end,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?);"
	public static final String UPDATE_PRUEBA="UPDATE PRUEBAS SET NOMBRE=?, APLICACION=?, ENTRADA=?, SALIDA=?, REGISTRADOS=?, ULTIMO_DIA_INSCRIPCION=?, CUPO=?, LUGAR=?, CAMPUS_PID=?, CALLE=?, NUMERO_INT=?, NUMERO_EXT=?, COLONIA=?, CODIGO_POSTAL=?, MUNICIPIO=?, PAIS_PID=?, ESTADO_PID=?, ISELIMINADO=?, SESION_PID=?, DURACION=?, DESCRIPCION=?, cattipoprueba_pid=? WHERE PERSISTENCEID=?"
	public static final String INSERT_RESPONSABLEDISPONIBLE="INSERT INTO RESPONSABLEDISPONIBLE ( persistenceid,persistenceversion,HORARIO, DISPONIBLE, RESPONSABLEID, PRUEBA_PID, licenciaturas) VALUES ( case when (SELECT max(persistenceId)+1 from RESPONSABLEDISPONIBLE ) is null then 1 else (SELECT max(persistenceId)+1 from RESPONSABLEDISPONIBLE) end,0,?, ?, ?, ?, ?);"
	public static final String UPDATE_RESPONSABLEDISPONIBLE="UPDATE RESPONSABLEDISPONIBLE SET HORARIO=?, DISPONIBLE=?, RESPONSABLEID=?, PRUEBA_PID=?, licenciaturas=?, iseliminado=? WHERE PERSISTENCEID=?";
	public static final String DELETE_IF_CAMBIO_DURACION="DELETE FROM RESPONSABLEDISPONIBLE WHERE PRUEBA_PID=?"
	public static final String GET_SESIONES_CALENDARIO="SELECT s.persistenceid as id, case when p.entrada is null then  concat(to_char( s.FECHA_INICIO, 'MM/DD/YYYY'),' 00:00' ) else concat(to_char( s.FECHA_INICIO, 'MM/DD/YYYY'),' ',p.entrada ) end as start_date, case when p.entrada is null then  concat(to_char( s.FECHA_INICIO, 'MM/DD/YYYY'),' 23:00' ) else concat(to_char( s.FECHA_INICIO, 'MM/DD/YYYY'),' ',p.salida) end as end_date, case when s.BORRADOR then concat(s.nombre, ' <i class=\"small material-icons\">do_not_disturb</i>' ) else concat(s.nombre, ' <i class=\"small material-icons\">language</i>' ) end as text, case when s.fecha_inicio<now() then '#969696'else '#ff5900' end as color FROM SESIONES s LEFT JOIN PRUEBAS p on p.SESION_PID =s.persistenceid and (SELECT fa.persistenceid FROM pruebas fa WHERE fa.sesion_pid= s.persistenceid LIMIT 1 )=p.persistenceid [WHERE]"
	public static final String GET_SESION_ID="SELECT s.* FROM SESIONES s WHERE persistenceId=?"
	public static final String GET_PRUEBAS_SESION_PID="SELECT p.*, r.horario, r.RESPONSABLEID , r.DISPONIBLE, c.descripcion as tipo, r.persistenceId as rid, r.ocupado, r.licenciaturas  FROM PRUEBAS p LEFT JOIN RESPONSABLEDISPONIBLE  r on r.PRUEBA_PID =p.PERSISTENCEID AND r.iseliminado=false LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid where p.SESION_PID =? AND p.iseliminado=false ORDER BY r.horario"
	public static final String GET_REGISTRADOS_CUPO="SELECT p.registrados, p.cupo from pruebas p where p.sesion_pid=? and cattipoprueba_pid!=1"
	public static final String UPDATE_REGISTRADOS="UPDATE PRUEBAS SET REGISTRADOS=(SELECT registrados+1 FROM pruebas WHERE sesion_pid=? and cattipoprueba_pid!=1 LIMIT 1) where  sesion_pid=? and cattipoprueba_pid!=1"
	public static final String INSERT_sesionaspirante="INSERT INTO sesionaspirante (persistenceid,persistenceversion,username,sesiones_pid,responsabledisponible_pid) VALUES ( case when (SELECT max(persistenceId)+1 from sesionaspirante ) is null then 1 else (SELECT max(persistenceId)+1 from sesionaspirante) end,0,?,?,?)"
	public static final String UPDATE_OCUPADO_RESPONSABLE_DISPONIBLE="update RESPONSABLEDISPONIBLE set OCUPADO=true where persistenceid=?"
	public static final String GET_OCUPADO_RESPONSABLE_DISPONIBLE="SELECT persistenceId FROM RESPONSABLEDISPONIBLE where persistenceid=? and ocupado=true"
	public static final String GET_DATOS_SESION_USERNAME="select p.aplicacion, tipo.descripcion, p.lugar, case when tipo.persistenceid=1 then rd.horario  else concat(p.entrada,' - ',p.salida) end as horario, s.nombre as snombre, s.descripcion as sdescripcion, p.nombre as pnombre, p.descripcion as pdescripcion, p.municipio, p.colonia, p.codigo_postal from sesionaspirante sa LEFT JOIN sesiones s on s.persistenceid= sa.sesiones_pid left join pruebas p on p.sesion_pid=sa.sesiones_pid left join cattipoprueba tipo on tipo.persistenceid=p.cattipoprueba_pid left join responsabledisponible rd on rd.persistenceid=sa.responsabledisponible_pid  where sa.username=?"
	public static final String UPDATE_REGISTRADOS_PRUEBAS="UPDATE PRUEBAS set registrados=(SELECT registrados+1 FROM pruebas where persistenceid=(SELECT prueba_pid FROM RESPONSABLEDISPONIBLE WHERE persistenceid=?) ) where persistenceid=(SELECT prueba_pid FROM RESPONSABLEDISPONIBLE WHERE persistenceid=?)"
	public static final String GET_PRUEBAS_SESION_PID_ASPIRANTE="SELECT p.*, r.horario, r.RESPONSABLEID , r.DISPONIBLE, c.descripcion as tipo, r.persistenceId as rid, r.ocupado, r.licenciaturas  FROM PRUEBAS p LEFT JOIN RESPONSABLEDISPONIBLE  r on r.PRUEBA_PID =p.PERSISTENCEID AND r.iseliminado=false LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid where p.SESION_PID =? AND p.iseliminado=false AND p.cattipoprueba_pid!=1 ORDER BY r.horario"
	public static final String GET_PRUEBAS_SESION_ENTREVISTA_ASPIRANTE="SELECT p.*, r.horario, r.RESPONSABLEID , r.DISPONIBLE, c.descripcion   AS tipo, r.persistenceId AS rid, r.ocupado, r.licenciaturas, disponibles.disponibles FROM PRUEBAS p LEFT JOIN RESPONSABLEDISPONIBLE r ON r.PRUEBA_PID =p.PERSISTENCEID AND r.iseliminado=false LEFT JOIN cattipoprueba c ON c.PERSISTENCEID =p.cattipoprueba_pid LEFT JOIN (SELECT p.persistenceid  AS prueba_pid, COUNT(r.ocupado) AS disponibles FROM PRUEBAS p LEFT JOIN RESPONSABLEDISPONIBLE r ON r.PRUEBA_PID =p.PERSISTENCEID AND r.iseliminado=false WHERE p.SESION_PID =? AND p.iseliminado=false AND p.cattipoprueba_pid=1 AND r.disponible=true AND ocupado=false AND  (r.licenciaturas ='' or r.licenciaturas is null or r.licenciaturas like '%' || (SELECT g.descripcion from solicituddeadmision sda LEFT JOIN CATGESTIONESCOLAR g ON g.persistenceid=sda.CATGESTIONESCOLAR_PID where sda.correoelectronico=? limit 1) ||'%') GROUP BY p.persistenceid ORDER BY p.registrados ASC) disponibles ON disponibles.prueba_pid=p.persistenceid WHERE p.SESION_PID =? AND  (r.licenciaturas ='' or r.licenciaturas is null or r.licenciaturas like '%' || (SELECT g.descripcion from solicituddeadmision sda LEFT JOIN CATGESTIONESCOLAR g ON g.persistenceid=sda.CATGESTIONESCOLAR_PID where sda.correoelectronico=? limit 1) ||'%') AND p.iseliminado=false AND p.cattipoprueba_pid=1 AND r.disponible=true AND ocupado=false ORDER BY p.registrados ASC, disponibles.disponibles DESC";
	public static final String GET_HORARIOS_PRUEBAS_ENTREVISTA_ASPIRANTE="SELECT r.horario, r.RESPONSABLEID , r.DISPONIBLE, r.persistenceId AS rid, r.ocupado, r.licenciaturas FROM RESPONSABLEDISPONIBLE r WHERE r.PRUEBA_PID = ? AND r.iseliminado=false ORDER BY r.horario ASC"
	public static final String INSERT_CATBITACORACORREOS="INSERT INTO CATBITACORACORREOS  (persistenceid, persistenceversion,codigo,de,estatus,fechacreacion,mensaje,para,campus) values (case when (SELECT max(persistenceId)+1 from CATBITACORACORREOS ) is null then 1 else (SELECT max(persistenceId)+1 from CATBITACORACORREOS ) end,0,?,?,?,now(),?,?,?)"
	/*************JUAN ESQUER FIN*******************/
	/***********************JESUS OSUNA********************************/
	public static final String GET_CATESCOLARIDAD = "SELECT * FROM CATESCOLARIDAD [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATSEXO = "SELECT * FROM CATSEXO [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATPARENTESCO = "SELECT * FROM CATPARENTESCO [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATDESCUENTOS = "SELECT c.*, b.descripcion as bachilleratos FROM CATDESCUENTOS c left join CATBACHILLERATOS b on b.PERSISTENCEID = c.CATBACHILLERATOS_PID [BACHILLERATO] LEFT JOIN CATCAMPUS a on a.grupobonita=c.campus [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATNACIONALIDAD = "SELECT * FROM CATNACIONALIDAD [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_CATGENERICO = "SELECT * FROM [CATALOGO] [WHERE] [ORDERBY] [LIMITOFFSET]"
	
	public static final String GET_TIPOPRUEBA = "SELECT c.persistenceid as tipoprueba_pid FROM PRUEBAS AS P LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid WHERE P.persistenceid = ?";
	public static final String GET_SESIONESASPIRANTE = "SELECT SA.*,RD.responsableid,RD.prueba_pid, P.aplicacion, P.nombre as nombre_prueba,P.Lugar as lugar_prueba, c.descripcion as tipo_prueba, case when C.persistenceid=1 then rd.horario  else concat(p.entrada,' - ',p.salida) end as horario, c.persistenceid as tipoprueba_pid, PL.asistencia FROM responsabledisponible as RD left join PRUEBAS  as P on P.persistenceid = RD.prueba_pid LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid LEFT JOIN SESIONES as S on S.persistenceid = P.sesion_pid LEFT JOIN sesionaspirante as SA on SA.sesiones_pid = S.persistenceid LEFT JOIN PaseLista as PL on PL.USERNAME = SA.USERNAME  AND PL.prueba_pid = P.persistenceid LEFT JOIN SOLICITUDDEADMISION sda ON sda.correoelectronico = SA.USERNAME LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID LEFT JOIN catSexo as sx on sx.persistenceid = sda.CATSEXO_PID LEFT JOIN CATRESIDENCIA as R on R.persistenceid = da.catresidencia_pid WHERE SA.sesiones_pid = ? AND P.persistenceid = ? AND rd.responsableid = ?  AND CAST( P.aplicacion AS DATE) >= CAST([FECHA] AS DATE) [ENTREVISTA]  [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_SESIONESCALENDARIZADAS = "SELECT DISTINCT(P.persistenceid)  as pruebas_id,   P.nombre, P.aplicacion, S.tipo as residencia, S.persistenceid as sesiones_id, P.lugar, P.registrados as alumnos_generales, S.nombre as nombre_sesion, c.descripcion as tipo_prueba, P.cupo, P.entrada,P.salida FROM PRUEBAS as P  LEFT JOIN SESIONES as S on P.sesion_pid = S.persistenceid LEFT JOIN RESPONSABLEDISPONIBLE as RD on RD.prueba_pid = P.persistenceid LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid WHERE P.ISELIMINADO = FALSE AND RD.responsableid = ? AND CAST(P.aplicacion AS DATE) >= CAST([FECHA] AS DATE) [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String GET_ASPIRANTESDELASESION = "SELECT sda.caseId, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.telefonocelular, sda.correoelectronico, campus.descripcion AS campus, gestionescolar.DESCRIPCION AS licenciatura,  prepa.DESCRIPCION AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita, le.descripcion as lugarexamen, sx.descripcion as sexo, CPO.descripcion as periodo, R.descripcion as residencia FROM SOLICITUDDEADMISION sda LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER LEFT JOIN catLugarExamen as le on le.persistenceid = sda.CATLUGAREXAMEN_PID LEFT JOIN catSexo as sx on sx.persistenceid = sda.CATSEXO_PID LEFT JOIN catPeriodo as CPO on CPO.persistenceid = sda.CATPERIODO_PID LEFT JOIN CATRESIDENCIA as R on R.persistenceid = da.catresidencia_pid   WHERE sda.correoelectronico = ?";
	
	public static final String INSERT_PASEDELISTA = "INSERT INTO PaseLista (persistenceid,prueba_pid,username,asistencia,fecha,usuarioPaseLista) VALUES ( case when (SELECT max(persistenceId)+1 from PaseLista ) is null then 1 else (SELECT max(persistenceId)+1 from PaseLista) end,?,?,?,?,?) "
	public static final String UPDATE_PASEDELISTA = "update PaseLista set asistencia = ?, fecha = ?, usuarioPaseLista = ? where prueba_pid = ? and username = ?" 
	
	public static final String GET_SESIONESCALENDARIZADASPASADAS = "SELECT P.nombre, P.aplicacion, S.tipo as residencia, P.persistenceid as pruebas_id, S.persistenceid as sesiones_id, P.lugar, P.registrados as alumnos_generales, S.nombre as nombre_sesion, c.descripcion as tipo_prueba, P.cupo, P.entrada,P.salida, campus.descripcion AS campus FROM PRUEBAS as P LEFT JOIN SESIONES as S on P.sesion_pid = S.persistenceid LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid LEFT JOIN catcampus campus ON campus.persistenceid=s.campus_pid [CAMPUS] WHERE P.ISELIMINADO = FALSE AND CAST(P.aplicacion AS DATE) [ORDEN] CAST([FECHA] AS DATE)  [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String GET_SESIONESASPIRANTEPASADAS = "SELECT SA.*,RD.responsableid,RD.prueba_pid, P.aplicacion, P.nombre as nombre_prueba,P.Lugar as lugar_prueba, c.descripcion as tipo_prueba, case when C.persistenceid=1 then rd.horario  else concat(p.entrada,' - ',p.salida) end as horario, c.persistenceid as tipoprueba_pid, PL.asistencia FROM responsabledisponible as RD left join PRUEBAS  as P on P.persistenceid = RD.prueba_pid LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid LEFT JOIN SESIONES as S on S.persistenceid = P.sesion_pid LEFT JOIN sesionaspirante as SA on SA.sesiones_pid = S.persistenceid LEFT JOIN PaseLista as PL on PL.USERNAME = SA.USERNAME  AND PL.prueba_pid = P.persistenceid LEFT JOIN SOLICITUDDEADMISION sda ON sda.correoelectronico = SA.USERNAME LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID LEFT JOIN catSexo as sx on sx.persistenceid = sda.CATSEXO_PID LEFT JOIN CATRESIDENCIA as R on R.persistenceid = da.catresidencia_pid WHERE SA.sesiones_pid = ? AND P.persistenceid = ?   [ENTREVISTA]  [WHERE] [ORDERBY] [LIMITOFFSET] "
	public static final String GET_SESIONESCALENDARIZADASREPORTE = "SELECT DISTINCT(P.persistenceid)  as pruebas_id,   P.nombre, P.aplicacion, S.tipo as residencia, S.persistenceid as sesiones_id, P.lugar, P.registrados as alumnos_generales, S.nombre as nombre_sesion, c.descripcion as tipo_prueba, P.cupo, P.entrada,P.salida FROM PRUEBAS as P  LEFT JOIN SESIONES as S on P.sesion_pid = S.persistenceid LEFT JOIN RESPONSABLEDISPONIBLE as RD on RD.prueba_pid = P.persistenceid LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid WHERE P.ISELIMINADO = FALSE AND RD.responsableid = ? AND CAST(P.aplicacion AS DATE) < CAST([FECHA] AS DATE) [WHERE] [ORDERBY] [LIMITOFFSET]";
	/***********************JESUS OSUNA FIN********************************/
	/*************JOSÉ GARCÍA***********************/
	public static final String GET_CATPROPEDEUTICO ="SELECT p.*, c.descripcion AS campus FROM CATPROPEDEUTICO p INNER JOIN CATCAMPUS c ON p.CAMPUS_PID = c.PERSISTENCEID  [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String INSERT_BITACORA_PAGO = "INSERT INTO CATBITACORAMETODOPAGO(persistenceid, estatus, eventos, fechaMovimiento, iseliminado, mediopago, monto, notransaccion, observaciones, usuarioaspirante, persistenceversion, caseid, campus, nombrepago) VALUES (case when (SELECT max(persistenceId)+1 from CATBITACORAMETODOPAGO ) is null then 1 else (SELECT max(persistenceId)+1 from CATBITACORAMETODOPAGO) end, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ?)";
	public static final String GET_BITACORA_PAGO =" SELECT *, estatus,  fechaMovimiento, medioPago, monto, noTransaccion, observaciones, usuarioAspirante, caseId, nombrePago FROM CATBITACORAMETODOPAGO [WHERE] GROUP BY persistenceId, estatus,  fechaMovimiento, medioPago, monto, noTransaccion, observaciones, usuarioAspirante, caseId [ORDERBY] [LIMITOFFSET]";
	public static final String GET_COUNT_BITACORA_PAGO = "SELECT COUNT(PERSISTENCEID) as registros FROM CATBITACORAMETODOPAGO [WHERE]";
	/*************JOSÉ GARCÍA FIN***********************/
	/***********************ERIC ROSAS*******************************/
	public static final String GET_CATPERIODO = "SELECT P.* FROM CATPERIODO as P LEFT JOIN catcampus campus ON campus.persistenceid=P.campus_pid [CAMPUS] [WHERE] [ORDERBY] [LIMITOFFSET]"
	/***********************ERIC ROSAS FIN********************************/
	/***********************ARTURO ZAMORANO*******************************/
	public static final String GET_LAST_FECHA_EXAMEN = "SELECT TO_CHAR(E.aplicacion, 'YYYY-MM-DD') AS fechaFinal, E.salida, A.username, F.descripcion FROM SESIONASPIRANTE AS A LEFT JOIN SESIONES AS D ON A.sesiones_pid = D.persistenceid LEFT JOIN PRUEBAS AS E ON E.sesion_pid = D.persistenceid LEFT JOIN CATTIPOPRUEBA AS F ON E.cattipoprueba_pid = F.persistenceid WHERE A.username = ?";
	/***********************ARTURO ZAMORANO FIN*******************************/
	/***********************MARIO ICEDO*******************************/
	public static final String GET_SOLICITUDES_TRANSFERENCIA="SELECT sda.persistenceid, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campussede, campus.descripcion AS campus, gestionescolar.nombre AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, residencia.descripcion AS tipoalumno, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, le.descripcion AS lugarexamen, ciudadestado.descripcion AS ciudadestado, ciudadpais.descripcion AS ciudadpais, estadoexamen.descripcion AS estadoexamen, pais.descripcion AS paisexamen, prope.descripcion AS propedeutico FROM SOLICITUDDEADMISION sda LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID [CAMPUS] LEFT JOIN catcampus campusEstudio ON campusEstudio.persistenceid=sda.catcampusestudio_pid LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID [PROGRAMA] LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID [INGRESO] LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID [ESTADO] LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID [BACHILLERATO] LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER LEFT JOIN catlugarexamen le ON le.persistenceid = sda.catlugarexamen_pid LEFT JOIN CATESTADOs estadoexamen ON estadoexamen.persistenceid = sda.catestadoexamen_pid LEFT JOIN catpais pais ON pais.persistenceid = sda.catpaisexamen_pid LEFT JOIN catciudad ciudadestado ON ciudadestado.persistenceid = sda.ciudadexamen_pid LEFT JOIN catciudad ciudadpais ON ciudadpais.persistenceid = sda.ciudadexamenpais_pid LEFT JOIN catpropedeutico prope ON prope.persistenceid = sda.catpropedeutico_pid LEFT JOIN catresidencia residencia ON residencia.persistenceid = da.catresidencia_pid [TIPOALUMNO] [WHERE] GROUP BY sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion, campus.descripcion, gestionescolar.nombre, periodo.DESCRIPCION, estado.DESCRIPCION, prepa.DESCRIPCION, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, residencia.descripcion, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, le.descripcion, ciudadestado.descripcion, ciudadpais.descripcion, estadoexamen.descripcion, pais.descripcion, sda.persistenceid, prope.descripcion [ORDERBY] [LIMITOFFSET]";
	public static final String GET_COUNT_SOLICITUDES_TRASNFERENCIA="SELECT COUNT(sda.persistenceid) as registros FROM SOLICITUDDEADMISION sda LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID [CAMPUS] LEFT JOIN catcampus campusEstudio ON campusEstudio.persistenceid=sda.catcampusestudio_pid LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID [PROGRAMA] LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID [INGRESO] LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID [ESTADO] LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID [BACHILLERATO] LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER LEFT JOIN catlugarexamen le ON le.persistenceid = sda.catlugarexamen_pid LEFT JOIN CATESTADOs estadoexamen ON estadoexamen.persistenceid = sda.catestadoexamen_pid LEFT JOIN catpais pais ON pais.persistenceid = sda.catpaisexamen_pid LEFT JOIN catciudad ciudadestado ON ciudadestado.persistenceid = sda.ciudadexamen_pid LEFT JOIN catciudad ciudadpais ON ciudadpais.persistenceid = sda.ciudadexamenpais_pid [TIPOALUMNO] [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String UPDATE_DATOS_TRASNFERENCIA="UPDATE SOLICITUDDEADMISION SET catcampus_pid = ?, catgestionescolar_pid = ?, catpropedeutico_pid = ?, catperiodo_pid = ?, catlugarexamen_pid = ?, catestadoexamen_pid = ?, ciudadexamen_pid = ?, catpaisexamen_pid = ?, ciudadexamenpais_pid = ?, catcampusestudio_pid = ? WHERE caseid = ?";
	public static final String INSERT_BITACORA_TRANSFERENCIA="INSERT INTO CATBITACORATRANSFERENCIAS (persistenceid, persistenceversion, aspirante, correoAspirante, valorOriginal, valorCambio, fechaCreacion, usuarioCreacion, campusAnterior, campusNuevo) VALUES (case when (SELECT max(persistenceId)+1 from CATBITACORATRANSFERENCIAS) is null then 1 else (SELECT max(persistenceId)+1 from CATBITACORATRANSFERENCIAS) end,0,?,?,?,?,now(),?,?,?)";
	public static final String GET_BITACORA_TRANSFERENCIA="SELECT *, aspirante, correoaspirante, fechacreacion,usuariocreacion, valorcambio,valororiginal FROM CATBITACORATRANSFERENCIAS [WHERE] GROUP BY persistenceId, persistenceversion, aspirante, correoaspirante,fechacreacion,usuariocreacion,valorcambio,valororiginal [ORDERBY] [LIMITOFFSET]";
	public static final String GET_COUNT_BITACORA_TRANSFERENCIA="SELECT COUNT(PERSISTENCEID) as registros FROM CATBITACORATRANSFERENCIAS [WHERE]";
	/***********************MARIO ICEDO FIN*******************************/
}
