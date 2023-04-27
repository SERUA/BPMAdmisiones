package com.anahuac.rest.api.DB

class Statements {
	
	public static final String GET_CAT_PREGUNTAS = "SELECT * FROM CATPREGUNTAS [WHERE] [ORDERBY] [LIMITOFFSET]"
	
	public static final String GET_CAT_PREGUNTAS_EXAMEN = "SELECT * FROM CATPREGUNTAS [WHERE]";
	
//	public static final String GET_SESION_LOGIN = "SELECT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion, p.nombre as nombre_prueba, p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida,  asps.username, CONCAT(p.aplicacion, ' ', p.entrada)::timestamp AS entradahora, now() fechaahoy, CONCAT(p.aplicacion, ' ', p.entrada)::timestamp <= now() AS valida, CONCAT(p.aplicacion, ' ', p.entrada)::timestamp <= now() AS sesion_iniciada, CONCAT(p.aplicacion, ' ', p.salida)::timestamp <= now() AS sesion_finalizada, CONCAT(temp.fechainiciosesion, ' ', temp.horainiciosesion)::timestamp <= now() AS sesion_iniciada_temp, CONCAT(temp.fechainiciosesion, ' ', temp.horafinsesion)::timestamp <= now() AS sesion_finalizada_temp,   CONCAT(temp.fechainiciosesion, ' ', temp.horainiciosesion)::timestamp AS fechainicio_temp FROM SESIONES AS ses INNER JOIN SesionAspirante AS asps ON asps.sesiones_pid = ses.persistenceid  INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON  tipo.persistenceid = p.cattipoprueba_pid  AND tipo.descripcion = 'Examen Psicométrico' LEFT JOIN InfoAspiranteTemporal AS temp ON temp.username = asps.username WHERE asps.username = ?;";
	public static final String GET_SESION_LOGIN = "SELECT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion, p.nombre as nombre_prueba, p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida,  asps.username, CONCAT(p.aplicacion, ' ', p.entrada)::timestamp AS entradahora, now() fechaahoy, CONCAT(p.aplicacion, ' ', p.entrada)::timestamp <= now() AS valida, CONCAT(p.aplicacion, ' ', p.entrada)::timestamp <= now() AS sesion_iniciada, CONCAT(p.aplicacion, ' ', p.salida)::timestamp <= now() AS sesion_finalizada, (CASE WHEN temp.fechainiciosesion IS NOT NULL THEN CONCAT(temp.fechainiciosesion, ' ', temp.horainiciosesion)::timestamp <= now() WHEN temp.fechainiciosesion IS NULL THEN null END) AS sesion_iniciada_temp, (CASE WHEN temp.fechainiciosesion IS NOT NULL THEN  CONCAT(temp.fechainiciosesion, ' ', temp.horafinsesion)::timestamp <= now() WHEN temp.fechainiciosesion IS NULL THEN null END) AS sesion_finalizada_temp,  (CASE WHEN temp.fechainiciosesion IS NOT NULL THEN CONCAT(temp.fechainiciosesion, ' ', temp.horainiciosesion)::timestamp WHEN temp.fechainiciosesion IS NULL THEN null END)  AS fechainicio_temp FROM SESIONES AS ses INNER JOIN SesionAspirante AS asps ON asps.sesiones_pid = ses.persistenceid  INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON  tipo.persistenceid = p.cattipoprueba_pid  AND tipo.descripcion = 'Examen Psicométrico' LEFT JOIN InfoAspiranteTemporal AS temp ON temp.username = asps.username  WHERE asps.username = ?;";
	
	public static final String GET_SESION_LOGIN_HORA = "SELECT DISTINCT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion,p.nombre as nombre_prueba,p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida, ap.username FROM aspirantespruebas AS ap INNER JOIN SESIONES AS ses ON ses.persistenceid = ap.sesiones_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON tipo.descripcion = 'Examen Psicométrico' AND tipo.persistenceid = p.cattipoprueba_pid WHERE p.aplicacion = TO_TIMESTAMP(?, 'YYYY-MM-DD') AND ap.username = ? AND TO_TIMESTAMP(TO_CHAR(CURRENT_TIMESTAMP, 'HH24:MI'), 'HH24:MI') BETWEEN TO_TIMESTAMP(p.entrada, 'HH24:MI') AND TO_TIMESTAMP(?, 'HH24:MI')";
	
//	public static final String GET_DATOS_SESION_LOGIN = "SELECT DISTINCT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion,p.nombre as nombre_prueba, p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida, ap.username, temp.*  FROM aspirantespruebas AS ap INNER JOIN SESIONES AS ses ON ses.persistenceid = ap.sesiones_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON tipo.descripcion = 'Examen Psicométrico' AND tipo.persistenceid = p.cattipoprueba_pid LEFT JOIN InfoAspiranteTemporal temp ON temp.username = ap.username  WHERE ap.username = ?  ORDER BY aplicacion DESC  LIMIT 1";
	public static final String GET_DATOS_SESION_LOGIN = "SELECT DISTINCT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion,p.nombre as nombre_prueba, p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida, ap.username, temp.fechainiciosesion AS aplicacion_temp,  temp.horainiciosesion AS entrada_temp, temp.horafinsesion AS salida_temp, temp.fechainiciosesion AS aplicacion_temp, ses_temp.nombre AS nombre_temp, ses_temp.persistenceid AS idsesion_temp   FROM CatRegistro AS creg LEFT JOIN aspirantespruebas AS ap ON creg.correoelectronico = ap.username LEFT JOIN SESIONES AS ses ON ses.persistenceid = ap.sesiones_pid LEFT JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid LEFT JOIN cattipoprueba AS tipo ON tipo.descripcion = 'Examen Psicométrico' AND tipo.persistenceid = p.cattipoprueba_pid LEFT JOIN InfoAspiranteTemporal temp ON temp.username = creg.correoelectronico LEFT JOIN SESIONES AS ses_temp ON ses_temp.persistenceid = temp.idprueba  WHERE creg.correoelectronico = ? ORDER BY aplicacion DESC  LIMIT 1";
	
	public static final String GET_USERS_BY_USERNAME = "SELECT tenantid, id, enabled, username, password, firstname, lastname, title, jobtitle, manageruserid, createdby, creationdate, lastupdate, iconid FROM user_ WHERE LOWER(username) LIKE LOWER(CONCAT('%',?,'%'))";
	
	public static final String UPDATE_IDIOMA_REGISTRO_BY_USERNAME = "INSERT INTO idiomainvpusuario (persistenceId , persistenceVersion, idioma, username, havesesion, usuariobloqueado, nombresesion, istemporal ) values (case when (SELECT max(persistenceId)+1 from idiomainvpusuario ) is null then 1 else (SELECT max(persistenceId)+1 from idiomainvpusuario) end,0,?, ?, ?, ?, ?, ?) RETURNING persistenceid;";
	
	public static final String UPDATE_TABLE_CATREGISTRO = "CREATE TABLE IdiomaINVPUsuario (persistenceId bigserial, persistenceVersion bigint DEFAULT 0, idioma CHARACTER VARYING(255), username CHARACTER VARYING(255), PRIMARY KEY (persistenceId) )";
	
	public static final String SELECT_PROPERTIES_BONITA = "SELECT A.tenantid, A.id, A.process_id, A.name, A.value FROM proc_parameter AS A INNER JOIN process_definition AS B ON B.processid = A.process_id AND B.activationstate='ENABLED' WHERE A.name IN ('usuario','password') ORDER BY B.version DESC Limit 2";
	
	public static final String INSERT_RESPUESTA_EXAMEN = "INSERT INTO respuestainvp (persistenceid,pregunta, respuesta,caseid,idusuario,username) VALUES ( case when (SELECT max(persistenceId)+1 from RespuestaINVP ) is null then 1 else (SELECT max(persistenceId)+1 from RespuestaINVP) end,?,?,?,?,?) ";
	
	public static final String GET_RESPUESTAS_BY_USUARIOCASO = "SELECT persistenceid, caseid, pregunta, respuesta, idusuario  FROM RESPUESTAINVP WHERE idusuario = ? AND caseid = ?;";
	
	public static final String GET_RESPUESTAS_BY_USUARIO = "SELECT persistenceid, caseid, pregunta, respuesta, idusuario  FROM RESPUESTAINVP WHERE idusuario = ?;";
	
	public static final String UPDATE_RESPUESTAS = "UPDATE respuestainvp SET respuesta = ?  WHERE pregunta = ? AND idusuario = ? AND caseid = ?";
	
	public static final String GET_IDIOMA_USUARIO = "SELECT persistenceId , persistenceVersion, idioma, username, havesesion, usuariobloqueado, nombresesion FROM idiomainvpusuario WHERE username = ?";
	
	public static final String GET_CAT_CAMPUS_PID_BY_CORREO = "SELECT catcampus_pid FROM solicituddeadmision WHERE correoelectronico = ?";
	
	public static final String UPDATE_SESION_USUARIO = "UPDATE idiomainvpusuario SET havesesion = ? WHERE username = ?";
	
	public static final String BLOQUEO_USUARIO = "UPDATE idiomainvpusuario SET usuariobloqueado = ? WHERE username = ?";
	
	public static final String GET_SESION_USUARIO = "SELECT idio.havesesion, idio.istemporal, invp.examenreiniciado,  CONCAT(temp.fechainiciosesion, ' ', temp.horainiciosesion)::timestamp <= now() AS sesion_iniciada_temp, CONCAT(temp.fechainiciosesion, ' ', temp.horafinsesion)::timestamp <= now() AS sesion_finalizada_temp,   CONCAT(temp.fechainiciosesion, ' ', temp.horainiciosesion)::timestamp AS fechainicio_temp FROM idiomainvpusuario AS idio LEFT JOIN InstanciaINVP AS invp ON invp.username = idio.username LEFT JOIN InfoAspiranteTemporal AS temp ON temp.username = idio.username  WHERE idio.username  = ?";
	
	public static final String INSERT_TERMINADO_EXAMEN = "INSERT INTO invpexamenterminado (persistenceId, persistenceVersion, username, terminado, fechainicio, fechafin) VALUES ( case when (SELECT max(persistenceId)+1 from invpexamenterminado ) is null then 1 else (SELECT max(persistenceId)+1 from invpexamenterminado) end, 0, ?, ?, NOW(), NULL);";
	
	public static final String UPDATE_TERMINADO_EXAMEN = "UPDATE invpexamenterminado SET terminado = ?, fechafin = NOW() WHERE username = ?";
	
	public static final String GET_TERMINADO_EXAMEN = "SELECT terminado FROM invpexamenterminado WHERE username = ?";
	
	public static final String GET_COUNT_PREGUNTASCONTESTADAS_BY_USERNAME = "SELECT COUNT(*) AS totalpreguntas FROM respuestainvp WHERE username = ? AND pregunta <> 0;";
	
	public static final String INSERT_BLOQUEO_USUARIO = "INSERT INTO idiomainvpusuario (persistenceId , persistenceVersion, havesesion, username ) values (case when (SELECT max(persistenceId)+1 from idiomainvpusuario ) is null then 1 else (SELECT max(persistenceId)+1 from idiomainvpusuario) end,0,?,?) RETURNING persistenceid;";
	
	public static final String GET_ID_BANNER_BY_CORREO = "SELECT ds.idbanner, sda.telefono, sda.telefonocelular from solicituddeadmision sda INNER JOIN detallesolicitud ds on ds.caseid::INTEGER = sda.caseid WHERE sda.correoelectronico = ? ;";
	
	public static final String GET_ID_USER_BY_USERNAME = "SELECT id FROM public.user_ where username = ?";
	
	public static final String GET_CELULAR_BY_USERNAME = "SELECT telefonocelular from solicituddeadmision WHERE correoelectronico = ?";
	
	public static final String GET_SESONES_TODAS = "SELECT DISTINCT(ses.persistenceid) AS id_sesion, ses.descripcion AS descripcion_sesion, ses.nombre AS nombre_sesion, ses.fecha_inicio AS fecha_inicio_sesion, cca.descripcion AS campus, cca.grupobonita, ctp.descripcion AS tipo_prueba,  p.aplicacion AS fecha_prueba, p.entrada AS entrada_prueba, p.salida AS salida_prueba, p.cupo AS cupo_prueba, p.registrados AS registrados_prueba,  p.lugar AS lugar_prueba, CASE WHEN (CONCAT(p.aplicacion, ' ', p.entrada)::Timestamp > now() OR ses.fecha_inicio is null ) THEN 'Por aplicar' WHEN ( CONCAT(p.aplicacion, ' ', p.entrada)::Timestamp < now() AND CONCAT(p.aplicacion, ' ', p.salida)::Timestamp > now() ) THEN 'En curso' WHEN ( CONCAT(p.aplicacion, ' ', p.salida)::Timestamp < now()) THEN 'Concluidas' END estatus, (SELECT COUNT(persistenceid) FROM InfoAspiranteTemporal WHERE idprueba = ses.persistenceid) AS no_registrados_prueba  FROM SESIONES AS ses  INNER JOIN CATCAMPUS AS cca ON cca.persistenceid = ses.campus_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS ctp ON ctp.descripcion = 'Examen Psicométrico' AND ctp.persistenceid = p.cattipoprueba_pid  LEFT JOIN ResponsableDisponible AS res ON res.prueba_pid = p.persistenceid   [WHERE] [ORDERBY] LIMIT ? OFFSET ?;";
	
	public static final String GET_COUNT_SESONES_TODAS = "SELECT COUNT(*) AS total_registros FROM (SELECT DISTINCT(ses.persistenceid)  FROM SESIONES AS ses  INNER JOIN CATCAMPUS AS cca ON cca.persistenceid = ses.campus_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS ctp ON ctp.descripcion = 'Examen Psicométrico' AND ctp.persistenceid = p.cattipoprueba_pid LEFT JOIN ResponsableDisponible AS res ON res.prueba_pid = p.persistenceid [WHERE]) AS query";
	
	public static final String GET_ASPIRANTES_SESIONES = "SELECT DISTINCT(creg.caseid), invp.caseid AS caseidinvp, invp.estatus AS estatusinvp, invp.examenReiniciado, creg.caseid AS idbpm, creg.primernombre, creg.segundonombre, creg.apellidopaterno, creg.apellidomaterno, creg.correoelectronico, ccam.descripcion  AS campus, sdad.telefono, sdad.telefonocelular, (SELECT COUNT(persistenceid) AS total_preguntas FROM CatPreguntas WHERE iseliminado = false) AS total_preguntas, (SELECT COUNT(persistenceid) AS total_respuestas FROM RespuestaINVP WHERE username = creg.correoelectronico) AS total_respuestas, prue.nombre AS nombre_prueba, ctpr.descripcion AS tipo_prueba, prue.descripcion AS descripcion_prueba, extr.fechainicio, extr.fechafin, extr.terminado, dets.idbanner,  idio.idioma, idio.usuariobloqueado, (SELECT COUNT(persistenceid) FROM AspirantesBloqueados WHERE username = creg.correoelectronico) > 0 AS usuariobloqueadob,  CASE WHEN extr.fechainicio IS NULL OR  extr.fechafin IS NULL THEN NULL WHEN extr.fechainicio IS NOT NULL AND  extr.fechafin IS NOT NULL THEN  extr.fechafin::timestamp - extr.fechainicio::timestamp END tiempo, temp.fechainiciosesion AS fechainiciosesion_temp,   temp.horafinsesion AS horafinsesion_temp, temp.horainiciosesion AS horainiciosesion_temp, temp.toleranciaentradasesion AS toleranciaentradasesion_temp, temp.toleranciasalidasesion AS toleranciasalidasesion_temp, idio.istemporal , ses_temp.nombre AS nombre__temp, ses_temp.descripcion AS descripcion_temp FROM CatRegistro  AS creg LEFT JOIN SesionAspirante AS seas ON  seas.username = creg.correoelectronico  LEFT JOIN Pruebas AS prue ON seas.sesiones_pid = prue.sesion_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid INNER JOIN SolicitudDeAdmision  AS sdad ON sdad.caseid = creg.caseid LEFT JOIN INVPExamenTerminado AS extr ON extr.username = creg.correoelectronico INNER JOIN CatCampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid  LEFT JOIN DetalleSolicitud AS dets ON dets.caseid::BIGINT = sdad.caseid LEFT JOIN IdiomaINVPUsuario AS idio ON idio.username = sdad.correoelectronico LEFT JOIN InstanciaINVP AS invp ON invp.username =  creg.correoelectronico   LEFT JOIN InfoAspiranteTemporal AS temp ON temp.username = creg.correoelectronico LEFT JOIN Sesiones AS ses_temp ON temp.idprueba = ses_temp.persistenceid    [WHERE]  [ORDERBY]   LIMIT ? OFFSET ?";
	
	public static final String GET_ASPIRANTES_SESIONES_TEMP = "SELECT DISTINCT(creg.caseid),creg.persistenceid, invp.caseid AS caseidinvp, idio.isTemporal, invp.estatus AS estatusinvp, invp.examenReiniciado, creg.caseid AS idbpm, creg.primernombre, creg.segundonombre, creg.apellidopaterno, creg.apellidomaterno, creg.correoelectronico, ccam.descripcion  AS campus, sdad.telefono, sdad.telefonocelular, (SELECT COUNT(persistenceid) AS total_preguntas FROM CatPreguntas WHERE iseliminado = false) AS total_preguntas, (SELECT COUNT(persistenceid) AS total_respuestas FROM RespuestaINVP WHERE username = creg.correoelectronico AND pregunta  <> 0) AS total_respuestas, prue.nombre AS nombre_prueba, ctpr.descripcion AS tipo_prueba, prue.descripcion AS descripcion_prueba, extr.fechainicio, extr.fechafin, extr.terminado, dets.idbanner, idio.idioma, idio.usuariobloqueado, (SELECT COUNT(persistenceid) FROM AspirantesBloqueados WHERE username = creg.correoelectronico) > 0 AS usuariobloqueadob, temp.fechainiciosesion AS temp_fechainiciosesion, temp.horainiciosesion AS temp_horainiciosesion, temp.horafinsesion tes_horafinsesion, temp.toleranciaentradasesion AS temp_toleranciaentrada, temp.toleranciasalidasesion AS temp_toleranciasalida, sestemp.nombre AS temp_Sesion     FROM CatRegistro  AS creg LEFT JOIN AspirantesPruebas AS aspr ON aspr.caseid = creg.caseid LEFT JOIN Pruebas AS prue ON prue.persistenceid = aspr.prueba_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid INNER JOIN SolicitudDeAdmision  AS sdad ON sdad.caseid = creg.caseid LEFT JOIN INVPExamenTerminado AS extr ON extr.username = creg.correoelectronico INNER JOIN CatCampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid  LEFT JOIN DetalleSolicitud AS dets ON dets.caseid::BIGINT = sdad.caseid LEFT JOIN IdiomaINVPUsuario AS idio ON idio.username = sdad.correoelectronico LEFT JOIN InstanciaINVP AS invp ON invp.username =  creg.correoelectronico LEFT JOIN InfoAspiranteTemporal AS temp ON creg.correoelectronico = temp.username LEFT JOIN SESIONES AS sestemp ON sestemp.persistenceid = temp.idprueba    [WHERE]  [ORDERBY]   LIMIT ? OFFSET ?";
	
	public static final String GET_ASPIRANTES_SESIONES_TEMP_COUNT = "SELECT COUNT(creg.persistenceid) AS total_registros FROM CatRegistro  AS creg LEFT JOIN AspirantesPruebas AS aspr ON aspr.caseid = creg.caseid LEFT JOIN Pruebas AS prue ON prue.persistenceid = aspr.prueba_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid INNER JOIN SolicitudDeAdmision  AS sdad ON sdad.caseid = creg.caseid LEFT JOIN INVPExamenTerminado AS extr ON extr.username = creg.correoelectronico INNER JOIN CatCampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid  LEFT JOIN DetalleSolicitud AS dets ON dets.caseid::BIGINT = sdad.caseid LEFT JOIN IdiomaINVPUsuario AS idio ON idio.username = sdad.correoelectronico LEFT JOIN InstanciaINVP AS invp ON invp.username =  creg.correoelectronico [WHERE]";
	
	public static final String GET_ASPIRANTES_SESIONES_COUNT = "SELECT COUNT(creg.caseid) AS total_registros FROM CatRegistro  AS creg LEFT JOIN AspirantesPruebas AS aspr ON aspr.caseid = creg.caseid LEFT JOIN Pruebas AS prue ON prue.persistenceid = aspr.prueba_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid LEFT JOIN SolicitudDeAdmision  AS sdad ON sdad.caseid = creg.caseid LEFT JOIN INVPExamenTerminado AS extr ON extr.username = creg.correoelectronico LEFT JOIN CatCampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid   LEFT JOIN DetalleSolicitud AS dets ON dets.caseid::BIGINT = sdad.caseid   LEFT JOIN InfoAspiranteTemporal AS temp ON temp.username = creg.correoelectronico LEFT JOIN Sesiones AS ses_temp ON temp.idprueba = ses_temp.persistenceid  [WHERE] ";
	
	public static final String GET_ASPIRANTES_SESIONES_TODOS = "SELECT DISTINCT(creg.caseid), invp.resultadoenviado, invp.caseid AS caseidinvp, invp.estatus AS estatusinvp, invp.examenReiniciado, creg.caseid AS idbpm, creg.primernombre, creg.segundonombre, creg.apellidopaterno, creg.apellidomaterno, creg.correoelectronico, ccam.descripcion  AS campus, sdad.telefono, sdad.telefonocelular, (SELECT COUNT(persistenceid) AS total_preguntas FROM CatPreguntas WHERE iseliminado = false) AS total_preguntas, (SELECT COUNT(persistenceid) AS total_respuestas FROM RespuestaINVP WHERE username = creg.correoelectronico AND pregunta  <> 0) AS total_respuestas, prue.nombre AS nombre_prueba, ctpr.descripcion AS tipo_prueba, prue.descripcion AS descripcion_prueba, extr.fechainicio, extr.fechafin, extr.terminado, dets.idbanner,  idio.idioma, idio.usuariobloqueado, (SELECT COUNT(persistenceid) FROM AspirantesBloqueados WHERE username = creg.correoelectronico) > 0 AS usuariobloqueadob,  CASE WHEN extr.fechainicio IS NULL OR  extr.fechafin IS NULL THEN NULL WHEN extr.fechainicio IS NOT NULL AND  extr.fechafin IS NOT NULL THEN  extr.fechafin::timestamp - extr.fechainicio::timestamp END tiempo, temp.fechainiciosesion AS fechainiciosesion_temp,   temp.horafinsesion AS horafinsesion_temp, temp.horainiciosesion AS horainiciosesion_temp, temp.toleranciaentradasesion AS toleranciaentradasesion_temp, temp.toleranciasalidasesion AS toleranciasalidasesion_temp, idio.istemporal , ses_temp.nombre AS nombre__temp, ses_temp.descripcion AS descripcion_temp FROM CatRegistro  AS creg LEFT JOIN SesionAspirante AS seas ON  seas.username = creg.correoelectronico  LEFT JOIN Pruebas AS prue ON seas.sesiones_pid = prue.sesion_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid INNER JOIN SolicitudDeAdmision  AS sdad ON sdad.caseid = creg.caseid LEFT JOIN INVPExamenTerminado AS extr ON extr.username = creg.correoelectronico INNER JOIN CatCampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid  LEFT JOIN DetalleSolicitud AS dets ON dets.caseid::BIGINT = sdad.caseid LEFT JOIN IdiomaINVPUsuario AS idio ON idio.username = sdad.correoelectronico LEFT JOIN InstanciaINVP AS invp ON invp.username =  creg.correoelectronico   LEFT JOIN InfoAspiranteTemporal AS temp ON temp.username = creg.correoelectronico LEFT JOIN Sesiones AS ses_temp ON temp.idprueba = ses_temp.persistenceid   [WHERE]  [ORDERBY]   LIMIT ? OFFSET ?";
	
	public static final String GET_ASPIRANTES_SESIONES_COUNT_TODOS = "SELECT Count(creg.caseid) AS total_registros FROM   catregistro AS creg LEFT JOIN sesionaspirante AS seas ON seas.username = creg.correoelectronico LEFT JOIN pruebas AS prue ON seas.sesiones_pid = prue.sesion_pid LEFT JOIN cattipoprueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid left join solicituddeadmision AS sdad ON sdad.caseid = creg.caseid left join invpexamenterminado AS extr ON extr.username = creg.correoelectronico left join catcampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid left join detallesolicitud AS dets ON dets.caseid :: bigint = sdad.caseid LEFT JOIN idiomainvpusuario AS idio ON idio.username = sdad.correoelectronico left join infoaspirantetemporal AS temp ON temp.username = creg.correoelectronico left join sesiones AS ses_temp ON temp.idprueba = ses_temp.persistenceid  [WHERE] ";
	
	public static final String UPDATE_IDIOMA_USUARIO = "UPDATE idiomainvpusuario SET idioma = ? WHERE username = ?";
	
	public static final String GET_IDIOMA_EXISTE_USUARIO = "SELECT COUNT(PERSISTENCEID) > 0 AS existe FROM IdiomaINVPUsuario WHERE USERNAME = ?";
	
	public static final String UPDATE_EXISTING_IDIOMA = "UPDATE IdiomaINVPUsuario SET idioma = ? WHERE username IN (SELECT DISTINCT(creg.correoelectronico)  FROM CatRegistro  AS creg LEFT JOIN AspirantesPruebas AS aspr ON aspr.caseid = creg.caseid LEFT JOIN Pruebas AS prue ON prue.persistenceid = aspr.prueba_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid  LEFT JOIN InstanciaINVP AS invp ON invp.username =  creg correoelectronico  WHERE  prue.sesion_pid = ?)";
	
	public static final String GET_USUARIOS_BLOQUEADOS = "SELECT * FROM AspirantesBloqueados [WHERE] LIMIT ? OFFSET ?";
	
	public static final String INSERT_CONFIGURACION_INVP = "INSERT INTO ConfiguracionesINVP (persistenceid, persistenceversion, toleranciaminutos, toleranciaSalidaMinutos, idprueba) VALUES (case when (SELECT max(persistenceId)+1 from ConfiguracionesINVP) is null then 1 else (SELECT max(persistenceId)+1 from ConfiguracionesINVP) end, 0, ?, ?, ?)";
	
	public static final String UPDATE_CONFIGURACION_INVP = "UPDATE ConfiguracionesINVP SET toleranciaminutos = ?, toleranciaSalidaMinutos = ? WHERE idprueba = ?;";
	
	public static final String GET_EXISTE_CONFIGURACION_INVP = "SELECT COUNT(persistenceid) > 0 AS existe FROM ConfiguracionesINVP WHERE idprueba = ?;";
	
	public static final String GET_CONFIGURACION_INVP = "SELECT toleranciaminutos, toleranciasalidaminutos, idprueba FROM ConfiguracionesINVP WHERE idprueba = ?;";
	
	public static final String GET_TOLERANCIA_BY_USERNAME = "SELECT CONCAT(prbs.aplicacion, ' ', prbs.entrada)::Timestamp + ((CASE WHEN toleranciaminutos IS NULL THEN 0 WHEN toleranciaminutos IS NOT NULL THEN toleranciaminutos END ) * interval '1 minute') <= now() AS puedeentrar, CONCAT(prbs.aplicacion, ' ', prbs.entrada)::Timestamp + ((CASE WHEN toleranciaminutos IS NULL THEN 0 WHEN toleranciaminutos IS NOT NULL THEN toleranciaminutos END ) * interval '1 minute') > now() AS tienetolerancia FROM ASPIRANTESPRUEBAS AS prue INNER JOIN CATTIPOPRUEBA AS tipo ON tipo.persistenceid = prue.cattipoprueba_pid AND tipo.descripcion = 'Examen Psicométrico' LEFT JOIN ConfiguracionesINVP AS conf ON conf.idprueba = sesiones_pid  INNER JOIN PRUEBAS AS prbs ON prbs.persistenceid = prue.prueba_pid  WHERE prue.username =  ? ORDER BY sesiones_pid DESC  LIMIT 1;";
	
	public static final String GET_TOLERANCIATEMP_BY_USERNAME = "SELECT temp.toleranciaEntradaSesion = 0, CONCAT(temp.fechaInicioSesion, ' ', temp.horaInicioSesion)::Timestamp + ((CASE WHEN  temp.toleranciaEntradaSesion IS NULL THEN 0 WHEN  temp.toleranciaEntradaSesion IS NOT NULL THEN  temp.toleranciaEntradaSesion END ) * interval '1 minute') > now() AS tienetolerancia FROM InfoAspiranteTemporal AS temp  WHERE temp.username = ?  LIMIT 1;";
	
	public static final String GET_EXAMEN_TERMINADO = "SELECT terminado FROM INVPExamenTerminado WHERE username = ?";
	
	public static final String GET_EXAMEN_TERMINADO_IDIOMA = "SELECT terminado FROM INVPExamenTerminado  WHERE username = ?";
	
	public static final String GET_USUARIO_TEMPORAL = "SELECT istemporal FROM IdiomaINVPUsuario WHERE username = ?";
	
	public static final String GET_SESIONES_BY_CAMPUS = "SELECT * FROM (SELECT sesi.persistenceid AS idprueba, prue.entrada, prue.salida, prue.aplicacion, sesi.nombre AS nombresesion,  conf.toleranciaminutos, conf.toleranciasalidaminutos, CASE WHEN (CONCAT(prue.aplicacion, ' ', prue.entrada)::Timestamp > now() OR sesi.fecha_inicio is null ) THEN 'Por aplicar' WHEN ( CONCAT(prue.aplicacion, ' ', prue.entrada)::Timestamp < now() AND CONCAT(prue.aplicacion, ' ', prue.salida)::Timestamp > now() ) THEN 'En curso' WHEN ( CONCAT(prue.aplicacion, ' ', prue.salida)::Timestamp < now()) THEN 'Concluidas' END estatus , ccam.descripcion AS campus,   ccam.grupobonita FROM pruebas AS prue INNER JOIN  Sesiones AS sesi ON sesi.persistenceid = prue.sesion_pid INNER JOIN cattipoprueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid AND ctpr.descripcion = 'Examen Psicométrico' INNER JOIN CatCampus AS ccam ON ccam.persistenceid = sesi.campus_pid LEFT JOIN ConfiguracionesINVP AS conf ON conf.idprueba = prue.sesion_pid ) AS mainquery  WHERE campus = ? AND (estatus = 'En curso'  OR estatus = 'Por aplicar') AND CONCAT(aplicacion, ' ', entrada)::DATE =  now()::DATE;";
	
	public static final String GET_EXISTE_NUEVA_CONF_USUARIO = "SELECT COUNT(PERSISTENCEID) > 0 AS existe FROM InfoAspiranteTemporal WHERE username = ?;";
	
	public static final String GET_NUEVA_CONF_USUARIO = "SELECT * FROM InfoAspiranteTemporal WHERE username = ?;";
	
	public static final String INSERT_NUEVA_CONFIG_USUARIO = "INSERT INTO InfoAspiranteTemporal (persistenceid, persistenceversion, username, fechaInicioSesion, horaInicioSesion, horaFinSesion, toleranciaEntradaSesion, toleranciaSalidaSesion, idprueba ) VALUES (case when (SELECT max(persistenceId)+1 from InfoAspiranteTemporal) is null then 1 else (SELECT max(persistenceId)+1 from InfoAspiranteTemporal) end, 0, ?, ?, ?, ?, ?, ?, ?);";
	
	public static final String UPDATE_NUEVA_CONFIG_USUARIO = "UPDATE  InfoAspiranteTemporal SET fechaInicioSesion = ?, horaInicioSesion = ?, horaFinSesion = ?, toleranciaEntradaSesion = ?, toleranciaSalidaSesion = ?, idprueba = ? WHERE username = ?";
	
	public static final String UPDATE_TOLERANCIAS_USUARIO = "UPDATE  InfoAspiranteTemporal SET toleranciaEntradaSesion = ?, toleranciaSalidaSesion = ?, idprueba = ? WHERE username = ?";
	
	public static final String GET_SESION_LOGIN_TEMPORAL = "SELECT temp.fechainiciosesion, temp.horafinsesion, temp.horainiciosesion, temp.toleranciaentradasesion, temp.toleranciasalidasesion, temp.idprueba, temp.username, sesi.nombre AS nombresesion, sesi.descripcion AS descripcionsesion FROM InfoAspiranteTemporal AS temp LEFT JOIN Sesiones AS sesi ON sesi.persistenceid = temp.idprueba WHERE temp.username = ?  AND temp.fechainiciosesion::timestamp = TO_TIMESTAMP(?, 'YYYY-MM-DD');";
	
	public static final String GET_SESION_FECHA_SALIDA = "SELECT CONCAT(prue.aplicacion, ' ', prue.salida)::Timestamp AS aplicacion_salida FROM PRUEBAS AS prue INNER JOIN CATTIPOPRUEBA AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid WHERE prue.sesion_pid = ?  AND ctpr.descripcion = 'Examen Psicométrico' ;";
	
	public static final String GET_FECHA_TERMINO_BY_USERNAME = "SELECT horafin, horafin_temp  FROM (SELECT ctpr.descripcion AS tipo_prueba,  CASE WHEN prue.aplicacion IS NOT NULL THEN (CONCAT(prue.aplicacion, ' ', prue.salida)::timestamp + ((CASE WHEN  conf.toleranciasalidaminutos IS NULL THEN 0 WHEN  conf.toleranciasalidaminutos IS NOT NULL THEN  conf.toleranciasalidaminutos END ) * interval '1 minute') ) WHEN prue.aplicacion IS NULL THEN null END horafin, CASE WHEN temp.fechainiciosesion IS null then null WHEN temp.fechainiciosesion IS NOT NULL THEN CONCAT(temp.fechaInicioSesion, ' ', temp.horafinsesion)::Timestamp + ((CASE WHEN  temp.toleranciaSalidaSesion IS NULL THEN 0 WHEN  temp.toleranciaSalidaSesion IS NOT NULL THEN  temp.toleranciaSalidaSesion END ) * interval '1 minute') END  horafin_Temp  FROM CatRegistro AS creg LEFT JOIN AspirantesPruebas AS aspr ON aspr.username = creg.correoelectronico LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = aspr.cattipoprueba_pid  LEFT JOIN Pruebas AS prue ON prue.persistenceid = aspr.prueba_pid LEFT JOIN ConfiguracionesINVP AS conf ON conf.idprueba =  aspr.sesiones_pid LEFT JOIN InfoAspiranteTemporal AS temp ON temp.username =  creg.correoelectronico WHERE creg.correoelectronico = ? AND (ctpr.descripcion = 'Examen Psicométrico' OR temp.idprueba IS NOT NULL) ORDER BY  aspr.persistenceid DESC ) AS query ";
	
	public static final String GET_INVP_INSTANCIA = "SELECT * FROM instanciainvp WHERE username = ?;";
	
	public static final String UPDATE_INVP_TOLERANCIA_TEMPORALES_SESION = "UPDATE  InfoAspiranteTemporal SET toleranciaentradasesion = ?, toleranciasalidasesion = ? WHERE idprueba = ?";
	
	public static final String GET_USUARIOS_BY_IDSESION = "SELECT * FROM (SELECT DISTINCT(creg.caseid), invp.caseid AS caseidinvp, invp.estatus AS estatusinvp, creg.correoelectronico FROM CatRegistro  AS creg LEFT JOIN SesionAspirante AS seas ON  seas.username = creg.correoelectronico  LEFT JOIN Pruebas AS prue ON seas.sesiones_pid = prue.sesion_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid INNER JOIN SolicitudDeAdmision  AS sdad ON sdad.caseid = creg.caseid LEFT JOIN INVPExamenTerminado AS extr ON extr.username = creg.correoelectronico INNER JOIN CatCampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid  LEFT JOIN DetalleSolicitud AS dets ON dets.caseid::BIGINT = sdad.caseid LEFT JOIN IdiomaINVPUsuario AS idio ON idio.username = sdad.correoelectronico LEFT JOIN InstanciaINVP AS invp ON invp.username =  creg.correoelectronico   LEFT JOIN InfoAspiranteTemporal AS temp ON temp.username = creg.correoelectronico LEFT JOIN Sesiones AS ses_temp ON temp.idprueba = ses_temp.persistenceid WHERE seas.sesiones_pid = ? OR temp.idprueba = ? ) As query WHERE estatusinvp = 'Examen iniciado' OR  estatusinvp = 'Examen reactivado'";
	
	public static final String GET_SESION_TERMINADA_EXISTE = "SELECT COUNT(*) FROM SesionesFinalizadas WHERE idsesion = ?;"
	
	public static final String INSERT_SESION_TERMINADA = "INSERT INTO SesionesFinalizadas (persistenceid, persistenceversion, idsesion, finalizada) VALUES (case when (SELECT max(persistenceId)+1 from SesionesFinalizadas ) is null then 1 else (SELECT max(persistenceId)+1 from SesionesFinalizadas) end, 0, ?, true);"
	
	public static final String GET_SESION_FINALIZADA_BY_USERNAME = "SELECT * FROM CatRegistro AS creg INNER JOIN SesionAspirante AS seas ON seas.username = creg.correoelectronico LEFT JOIN SesionesFinalizadas AS sefi ON sefi.idsesion = seas.sesiones_pid  WHERE creg.correoelectronico = ?;";
	
	public static final String GET_RESULTADOS_INVP_BY_USERNAME = "SELECT string_agg(CASE WHEN respuesta IS NULL THEN '*' WHEN respuesta= true THEN 'C' WHEN respuesta = false THEN 'F' END,  '') AS resultados FROM ( SELECT cpre.orden, resp.respuesta  FROM  CatPReguntas AS cpre  LEFT JOIN RespuestaINVP AS resp ON resp.pregunta = cpre.orden  AND (resp.username = ? AND resp.pregunta <> 0) ORDER BY cpre.orden ASC ) results_query ";
	
	public static final String GET_SESION_LOGIN_LOGIN = "SELECT  CONCAT(p.aplicacion, ' ', p.entrada)::timestamp <= now() AS sesion_iniciada, CONCAT(p.aplicacion, ' ', p.entrada)::timestamp <= now() AS sesion_finalizada, ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion, p.nombre as nombre_prueba, p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida,  asps.username, CONCAT(p.aplicacion, ' ', p.entrada)::timestamp AS entradahora, now() fechaahoy  FROM SESIONES AS ses INNER JOIN SesionAspirante AS asps ON asps.sesiones_pid = ses.persistenceid  INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON  tipo.persistenceid = p.cattipoprueba_pid  AND tipo.descripcion = 'Examen Psicométrico'  WHERE  asps.username = ? ";
	
	public static final String GET_SESION_LOGIN_TEMPORAL_LOGIN = "SELECT CONCAT(temp.fechaInicioSesion, ' ', temp.horaInicioSesion)::Timestamp <= now() AS sesion_iniciada, CONCAT(temp.fechaInicioSesion, ' ', temp.horaFinSesion)::Timestamp <= now() AS sesion_finalizada,  temp.fechainiciosesion, temp.horafinsesion, temp.horainiciosesion, temp.toleranciaentradasesion, temp.toleranciasalidasesion, temp.idprueba, temp.username, sesi.nombre AS nombresesion, sesi.descripcion AS descripcionsesion FROM InfoAspiranteTemporal AS temp LEFT JOIN Sesiones AS sesi ON sesi.persistenceid = temp.idprueba WHERE temp.username = ?;";
	
	public static final String INSET_BITACORA_RESPUESTAS = "INSERT INTO CatBitacoraComentarios (comentario,usuarioComentario,usuario,modulo,fechaCreacion,isEliminado,persistenceid) VALUES(?,?,?,?,?,false,case when (SELECT max(persistenceId)+1 from CatBitacoraComentarios ) is null then 1 else (SELECT max(persistenceId)+1 from CatBitacoraComentarios) end ) ";
	
	public static final String INSERT_PASEDELISTA = "INSERT INTO PaseLista (persistenceid,prueba_pid,username,asistencia,fecha,usuarioPaseLista) VALUES ( case when (SELECT max(persistenceId)+1 from PaseLista ) is null then 1 else (SELECT max(persistenceId)+1 from PaseLista) end,?,?,?,CAST(NOW() as varchar),?) ";
	
	public static final String UPDATE_PASEDELISTA = "update PaseLista set asistencia = ?, fecha = CAST(NOW() as varchar), usuarioPaseLista = ? where prueba_pid = ? and username = ?"
	
	public static final String GET_RESPUESTAS_INVP = "SELECT persistenceid,  escala,  genero,  persistenceversion, pregunta, puntuacion, respuesta  FROM catrespuestasinvp where genero='ambos' OR genero=lower((SELECT cs.descripcion from solicituddeadmision sda inner join catsexo cs on cs.persistenceid=sda.catsexo_pid inner join detallesolicitud ds on ds.caseid::bigint=sda.caseid and sda.correoelectronico NOT LIKE '%(rechazado)%' where ds.idbanner=? )) order by pregunta asc";
	
	public static final String GET_CASEID_BY_IDBANNER = "SELECT ds.caseid FROM detallesolicitud as ds INNER JOIN solicitudDeAdmision as sda ON sda.caseid = ds.caseid::integer WHERE sda.correoelectronico NOT LIKE '%(rechazado)%' and  ds.idbanner = ? limit 1";
	
	public static final String GET_RESULTADO_INVP = "SELECT idbanner,escala,puntuacion FROM resultadoinvp where idbanner = ? and sesiones_pid = ?";
		
	public static final String INSERT_RESULTADO_INVP = "INSERT INTO resultadoinvp (idbanner,escala,puntuacion,sesiones_pid,persistenceid,persistenceversion,fecha_registro,caseId) values (?,?,?,?,case when (SELECT max(persistenceId)+1 from resultadoinvp ) is null then 1 else (SELECT max(persistenceId)+1 from resultadoinvp) end,0,?,?)";
	
	public static final String GET_PRUEBA_CASEID_USERNAME_BY_BANNER_AND_SESION	= "Select sda.caseid, ap.prueba_pid, ap.username FROM solicituddeadmision AS SDA INNER JOIN detallesolicitud AS DS ON DS.caseid = SDA.caseid::varchar AND DS.idbanner = ? INNER JOIN aspirantespruebas AS AP ON AP.username = SDA.correoelectronico AND AP.catTipoPrueba_pid = 2 and AP.sesiones_pid = ? "
	
	public static final String GET_USER_REGISTRADO_EN_PRUEBA = " SELECT persistenceid FROM paselista WHERE prueba_pid = ? and username = ?"
	
	public static final String INSERT_ASPIRANTESPRUEBAS = "INSERT INTO ASPIRANTESPRUEBAS (persistenceid,username,PRUEBA_PID,asistencia,countRechazo,sesiones_pid,cattipoprueba_pid,responsabledisponible_pid,acreditado,caseid) VALUES ( case when (SELECT max(persistenceId)+1 from ASPIRANTESPRUEBAS ) is null then 1 else (SELECT max(persistenceId)+1 from ASPIRANTESPRUEBAS) end,?,?,?,?,?,?,?,?,?)"
	
	public static final String UPDATE_ASPIRANTESPRUEBAS = "UPDATE ASPIRANTESPRUEBAS set  asistencia = ?    where prueba_pid = ? and username = ?";
	
	public static final String UPDATE_BITACORAASPIRANTESPRUEBAS = "UPDATE CatBitacoraSesiones set  asistencia = ?  where prueba_pid = ? and username = ?";
	
	public static final String UPDATE_RESULTADOENVIADO = "UPDATE InstanciaINVP SET resultadoenviado = true  WHERE USERNAME = ?;";
	
	
	
}
