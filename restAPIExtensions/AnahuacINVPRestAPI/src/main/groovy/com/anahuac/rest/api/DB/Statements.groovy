package com.anahuac.rest.api.DB

class Statements {
	
	public static final String GET_CAT_PREGUNTAS = "SELECT * FROM CATPREGUNTAS [WHERE] [ORDERBY] [LIMITOFFSET]"
	
	public static final String GET_CAT_PREGUNTAS_EXAMEN = "SELECT * FROM CATPREGUNTAS [WHERE]";
	
	public static final String GET_SESION_LOGIN = "SELECT DISTINCT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion,p.nombre as nombre_prueba,p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida, ap.username FROM aspirantespruebas AS ap INNER JOIN SESIONES AS ses ON ses.persistenceid = ap.sesiones_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON tipo.descripcion = 'Examen Psicométrico'AND tipo.persistenceid = p.cattipoprueba_pid WHERE p.aplicacion = TO_TIMESTAMP(?, 'YYYY-MM-DD') AND ap.username = ?";
	
	public static final String GET_SESION_LOGIN_HORA = "SELECT DISTINCT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion,p.nombre as nombre_prueba,p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida, ap.username FROM aspirantespruebas AS ap INNER JOIN SESIONES AS ses ON ses.persistenceid = ap.sesiones_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON tipo.descripcion = 'Examen Psicométrico' AND tipo.persistenceid = p.cattipoprueba_pid WHERE p.aplicacion = TO_TIMESTAMP(?, 'YYYY-MM-DD') AND ap.username = ? AND TO_TIMESTAMP(TO_CHAR(CURRENT_TIMESTAMP, 'HH24:MI'), 'HH24:MI') BETWEEN TO_TIMESTAMP(p.entrada, 'HH24:MI') AND TO_TIMESTAMP(?, 'HH24:MI')";
	
	public static final String GET_DATOS_SESION_LOGIN = "SELECT DISTINCT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion,p.nombre as nombre_prueba,p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida, ap.username FROM aspirantespruebas AS ap INNER JOIN SESIONES AS ses ON ses.persistenceid = ap.sesiones_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON tipo.descripcion = 'Examen Psicométrico' AND tipo.persistenceid = p.cattipoprueba_pid WHERE ap.username = ?";
	
	public static final String GET_USERS_BY_USERNAME = "SELECT tenantid, id, enabled, username, password, firstname, lastname, title, jobtitle, manageruserid, createdby, creationdate, lastupdate, iconid FROM user_ WHERE LOWER(username) LIKE LOWER(CONCAT('%',?,'%'))";
	
	public static final String UPDATE_IDIOMA_REGISTRO_BY_USERNAME = "INSERT INTO idiomainvpusuario (persistenceId , persistenceVersion, idioma, username, havesesion, usuariobloqueado, nombresesion, istemporal ) values (case when (SELECT max(persistenceId)+1 from idiomainvpusuario ) is null then 1 else (SELECT max(persistenceId)+1 from idiomainvpusuario) end,0,?, ?, ?, ?, ?, ?) RETURNING persistenceid;";
	
	public static final String UPDATE_TABLE_CATREGISTRO = "CREATE TABLE IdiomaINVPUsuario (persistenceId bigserial, persistenceVersion bigint DEFAULT 0, idioma CHARACTER VARYING(255), username CHARACTER VARYING(255), PRIMARY KEY (persistenceId) )";
	
	public static final String SELECT_PROPERTIES_BONITA = "SELECT A.tenantid, A.id, A.process_id, A.name, A.value FROM proc_parameter AS A INNER JOIN process_definition AS B ON B.processid = A.process_id AND B.activationstate='ENABLED' WHERE A.name IN ('usuario','password') ORDER BY B.version DESC Limit 2";
	
	public static final String INSERT_RESPUESTA_EXAMEN = "INSERT INTO respuestainvp (persistenceid,pregunta, respuesta,caseid,idusuario,username) VALUES ( case when (SELECT max(persistenceId)+1 from RespuestaINVP ) is null then 1 else (SELECT max(persistenceId)+1 from RespuestaINVP) end,?,?,?,?,?) ";
	
	public static final String GET_RESPUESTAS_BY_USUARIOCASO = "SELECT persistenceid, caseid, pregunta, respuesta, idusuario  FROM RESPUESTAINVP WHERE idusuario = ? AND caseid = ?;";
	
	public static final String GET_RESPUESTAS_BY_USUARIO = "SELECT persistenceid, caseid, pregunta, respuesta, idusuario  FROM RESPUESTAINVP WHERE idusuario = ?;";
	
	public static final String UPDATE_RESPUESTAS = "UPDATE respuestainvp SET respuesta = ?  WHERE pregunta = ? AND idusuario = ? AND caseid = ?";
	
	public static final String  GET_IDIOMA_USUARIO = "SELECT persistenceId , persistenceVersion, idioma, username, havesesion, usuariobloqueado, nombresesion FROM idiomainvpusuario WHERE username = ?";
	
	public static final String GET_CAT_CAMPUS_PID_BY_CORREO = "SELECT catcampus_pid FROM solicituddeadmision WHERE correoelectronico = ?";
	
	public static final String UPDATE_SESION_USUARIO = "UPDATE idiomainvpusuario SET havesesion = ? WHERE username = ?";
	
	public static final String BLOQUEO_USUARIO = "UPDATE idiomainvpusuario SET usuariobloqueado = ? WHERE username = ?";
	
	public static final String GET_SESION_USUARIO = "SELECT havesesion, istemporal FROM idiomainvpusuario WHERE username = ?";
	
	public static final String INSERT_TERMINADO_EXAMEN = "INSERT INTO invpexamenterminado (persistenceId, persistenceVersion, username, terminado, fechainicio, fechafin) VALUES ( case when (SELECT max(persistenceId)+1 from invpexamenterminado ) is null then 1 else (SELECT max(persistenceId)+1 from invpexamenterminado) end, 0, ?, ?, NOW(), NULL);";
	
	public static final String UPDATE_TERMINADO_EXAMEN = "UPDATE invpexamenterminado SET terminado = ?, fechafin = NOW() WHERE username = ?";
	
	public static final String GET_TERMINADO_EXAMEN = "SELECT terminado FROM invpexamenterminado WHERE username = ?";
	
	public static final String GET_COUNT_PREGUNTASCONTESTADAS_BY_USERNAME = "SELECT COUNT(*) AS totalpreguntas FROM respuestainvp WHERE username = ?;";
	
	public static final String INSERT_BLOQUEO_USUARIO = "INSERT INTO idiomainvpusuario (persistenceId , persistenceVersion, havesesion, username ) values (case when (SELECT max(persistenceId)+1 from idiomainvpusuario ) is null then 1 else (SELECT max(persistenceId)+1 from idiomainvpusuario) end,0,?,?) RETURNING persistenceid;";
	
	public static final String GET_ID_BANNER_BY_CORREO = "SELECT ds.idbanner, sda.telefono, sda.telefonocelular from solicituddeadmision sda INNER JOIN detallesolicitud ds on ds.caseid::INTEGER = sda.caseid WHERE sda.correoelectronico = ? ;";
	
	public static final String GET_ID_USER_BY_USERNAME = "SELECT id FROM public.user_ where username = ?";
	
	public static final String GET_CELULAR_BY_USERNAME = "SELECT telefonocelular from solicituddeadmision WHERE correoelectronico = ?";
	
	public static final String GET_SESONES_TODAS = "SELECT DISTINCT(ses.persistenceid) AS id_sesion, ses.descripcion AS descripcion_sesion, ses.nombre AS nombre_sesion, ses.fecha_inicio AS fecha_inicio_sesion, cca.descripcion AS campus, cca.grupobonita, ctp.descripcion AS tipo_prueba,  p.aplicacion AS fecha_prueba, p.entrada AS entrada_prueba, p.salida AS salida_prueba, p.cupo AS cupo_prueba, p.registrados AS registrados_prueba,  p.lugar AS lugar_prueba, CASE WHEN (CONCAT(p.aplicacion, ' ', p.entrada)::Timestamp > now() OR ses.fecha_inicio is null ) THEN 'Por aplicar' WHEN ( CONCAT(p.aplicacion, ' ', p.entrada)::Timestamp < now() AND CONCAT(p.aplicacion, ' ', p.salida)::Timestamp > now() ) THEN 'En curso' WHEN ( CONCAT(p.aplicacion, ' ', p.salida)::Timestamp < now()) THEN 'Concluidas' END estatus FROM SESIONES AS ses  INNER JOIN CATCAMPUS AS cca ON cca.persistenceid = ses.campus_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS ctp ON ctp.descripcion = 'Examen Psicométrico' AND ctp.persistenceid = p.cattipoprueba_pid  LEFT JOIN ResponsableDisponible AS res ON res.prueba_pid = p.persistenceid [WHERE] [ORDERBY] LIMIT ? OFFSET ?;";
	
	public static final String GET_COUNT_SESONES_TODAS = "SELECT COUNT(*) AS total_registros FROM (SELECT DISTINCT(ses.persistenceid)  FROM SESIONES AS ses  INNER JOIN CATCAMPUS AS cca ON cca.persistenceid = ses.campus_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS ctp ON ctp.descripcion = 'Examen Psicométrico' AND ctp.persistenceid = p.cattipoprueba_pid LEFT JOIN ResponsableDisponible AS res ON res.prueba_pid = p.persistenceid [WHERE]) AS query";
	
	public static final String GET_ASPIRANTES_SESIONES = "SELECT DISTINCT(creg.caseid), invp.caseid AS caseidinvp, invp.estatus AS estatusinvp, invp.examenReiniciado, creg.caseid AS idbpm, creg.primernombre, creg.segundonombre, creg.apellidopaterno, creg.apellidomaterno, creg.correoelectronico, ccam.descripcion  AS campus, sdad.telefono, sdad.telefonocelular, (SELECT COUNT(persistenceid) AS total_preguntas FROM CatPreguntas WHERE iseliminado = false) AS total_preguntas, (SELECT COUNT(persistenceid) AS total_respuestas FROM RespuestaINVP WHERE username = creg.correoelectronico) AS total_respuestas, prue.nombre AS nombre_prueba, ctpr.descripcion AS tipo_prueba, prue.descripcion AS descripcion_prueba, extr.fechainicio, extr.fechafin, extr.terminado, dets.idbanner,  idio.idioma, idio.usuariobloqueado, (SELECT COUNT(persistenceid) FROM AspirantesBloqueados WHERE username = creg.correoelectronico) > 0 AS usuariobloqueadob,  CASE WHEN extr.fechainicio IS NULL OR  extr.fechafin IS NULL THEN NULL WHEN extr.fechainicio IS NOT NULL AND  extr.fechafin IS NOT NULL THEN  extr.fechafin::timestamp - extr.fechainicio::timestamp END tiempo  FROM CatRegistro  AS creg LEFT JOIN AspirantesPruebas AS aspr ON aspr.caseid = creg.caseid LEFT JOIN Pruebas AS prue ON prue.persistenceid = aspr.prueba_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid INNER JOIN SolicitudDeAdmision  AS sdad ON sdad.caseid = creg.caseid LEFT JOIN INVPExamenTerminado AS extr ON extr.username = creg.correoelectronico INNER JOIN CatCampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid  LEFT JOIN DetalleSolicitud AS dets ON dets.caseid::BIGINT = sdad.caseid LEFT JOIN IdiomaINVPUsuario AS idio ON idio.username = sdad.correoelectronico LEFT JOIN InstanciaINVP AS invp ON invp.username =  creg.correoelectronico [WHERE]  [ORDERBY]   LIMIT ? OFFSET ?";
	
	public static final String GET_ASPIRANTES_SESIONES_TEMP = "SELECT DISTINCT(creg.caseid),creg.persistenceid, invp.caseid AS caseidinvp, idio.isTemporal, invp.estatus AS estatusinvp, invp.examenReiniciado, creg.caseid AS idbpm, creg.primernombre, creg.segundonombre, creg.apellidopaterno, creg.apellidomaterno, creg.correoelectronico, ccam.descripcion  AS campus, sdad.telefono, sdad.telefonocelular, (SELECT COUNT(persistenceid) AS total_preguntas FROM CatPreguntas WHERE iseliminado = false) AS total_preguntas, (SELECT COUNT(persistenceid) AS total_respuestas FROM RespuestaINVP WHERE username = creg.correoelectronico) AS total_respuestas, prue.nombre AS nombre_prueba, ctpr.descripcion AS tipo_prueba, prue.descripcion AS descripcion_prueba, extr.fechainicio, extr.fechafin, extr.terminado, dets.idbanner, idio.idioma, idio.usuariobloqueado, (SELECT COUNT(persistenceid) FROM AspirantesBloqueados WHERE username = creg.correoelectronico) > 0 AS usuariobloqueadob   FROM CatRegistro  AS creg LEFT JOIN AspirantesPruebas AS aspr ON aspr.caseid = creg.caseid LEFT JOIN Pruebas AS prue ON prue.persistenceid = aspr.prueba_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid INNER JOIN SolicitudDeAdmision  AS sdad ON sdad.caseid = creg.caseid LEFT JOIN INVPExamenTerminado AS extr ON extr.username = creg.correoelectronico INNER JOIN CatCampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid  LEFT JOIN DetalleSolicitud AS dets ON dets.caseid::BIGINT = sdad.caseid LEFT JOIN IdiomaINVPUsuario AS idio ON idio.username = sdad.correoelectronico LEFT JOIN InstanciaINVP AS invp ON invp.username =  creg.correoelectronico  [WHERE]  [ORDERBY]   LIMIT ? OFFSET ?";
	
	public static final String GET_ASPIRANTES_SESIONES_TEMP_COUNT = "SELECT COUNT(creg.persistenceid) AS total_registros FROM CatRegistro  AS creg LEFT JOIN AspirantesPruebas AS aspr ON aspr.caseid = creg.caseid LEFT JOIN Pruebas AS prue ON prue.persistenceid = aspr.prueba_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid INNER JOIN SolicitudDeAdmision  AS sdad ON sdad.caseid = creg.caseid LEFT JOIN INVPExamenTerminado AS extr ON extr.username = creg.correoelectronico INNER JOIN CatCampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid  LEFT JOIN DetalleSolicitud AS dets ON dets.caseid::BIGINT = sdad.caseid LEFT JOIN IdiomaINVPUsuario AS idio ON idio.username = sdad.correoelectronico LEFT JOIN InstanciaINVP AS invp ON invp.username =  creg.correoelectronico [WHERE]";
	
	public static final String GET_ASPIRANTES_SESIONES_COUNT = "SELECT COUNT(creg.caseid) AS total_registros FROM CatRegistro  AS creg LEFT JOIN AspirantesPruebas AS aspr ON aspr.caseid = creg.caseid LEFT JOIN Pruebas AS prue ON prue.persistenceid = aspr.prueba_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid LEFT JOIN SolicitudDeAdmision  AS sdad ON sdad.caseid = creg.caseid LEFT JOIN INVPExamenTerminado AS extr ON extr.username = creg.correoelectronico LEFT JOIN CatCampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid   LEFT JOIN DetalleSolicitud AS dets ON dets.caseid::BIGINT = sdad.caseid   [WHERE] ";
	
	public static final String UPDATE_IDIOMA_USUARIO = "UPDATE idiomainvpusuario SET idioma = ? WHERE username = ?";
	
	public static final String GET_IDIOMA_EXISTE_USUARIO = "SELECT COUNT(PERSISTENCEID) > 0 AS existe FROM IdiomaINVPUsuario WHERE USERNAME = ?";
	
	public static final String UPDATE_EXISTING_IDIOMA = "UPDATE IdiomaINVPUsuario SET idioma = ? WHERE username IN (SELECT DISTINCT(creg.correoelectronico)  FROM CatRegistro  AS creg LEFT JOIN AspirantesPruebas AS aspr ON aspr.caseid = creg.caseid LEFT JOIN Pruebas AS prue ON prue.persistenceid = aspr.prueba_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid  LEFT JOIN InstanciaINVP AS invp ON invp.username =  creg correoelectronico  WHERE  prue.sesion_pid = ?)";
	
	public static final String GET_USUARIOS_BLOQUEADOS = "SELECT * FROM AspirantesBloqueados [WHERE] LIMIT ? OFFSET ?";
	
	public static final String INSERT_CONFIGURACION_INVP = "INSERT INTO ConfiguracionesINVP (persistenceid, persistenceversion, toleranciaminutos, toleranciaSalidaMinutos, idprueba) VALUES (case when (SELECT max(persistenceId)+1 from ConfiguracionesINVP) is null then 1 else (SELECT max(persistenceId)+1 from ConfiguracionesINVP) end, 0, ?, ?, ?)";
	
	public static final String UPDATE_CONFIGURACION_INVP = "UPDATE ConfiguracionesINVP SET toleranciaminutos = ?, toleranciaSalidaMinutos = ? WHERE idprueba = ?;";
	
	public static final String GET_EXISTE_CONFIGURACION_INVP = "SELECT COUNT(persistenceid) > 0 AS existe FROM ConfiguracionesINVP WHERE idprueba = ?;";
	
	public static final String GET_CONFIGURACION_INVP = "SELECT toleranciaminutos, toleranciasalidaminutos, idprueba FROM ConfiguracionesINVP WHERE idprueba = ?;";
	
	public static final String GET_TOLERANCIA_BY_USERNAME = "SELECT toleranciaminutos IS NULL, CONCAT(prbs.aplicacion, ' ', prbs.entrada)::Timestamp + ((CASE WHEN toleranciaminutos IS NULL THEN 5 WHEN toleranciaminutos IS NOT NULL THEN toleranciaminutos END ) * interval '1 minute') > now() AS tienetolerancia FROM ASPIRANTESPRUEBAS AS prue INNER JOIN CATTIPOPRUEBA AS tipo ON tipo.persistenceid = prue.cattipoprueba_pid AND tipo.descripcion = 'Examen Psicométrico' LEFT JOIN ConfiguracionesINVP AS conf ON conf.idprueba = sesiones_pid  INNER JOIN PRUEBAS AS prbs ON prbs.persistenceid = prue.prueba_pid  WHERE prue.username =  ? ORDER BY sesiones_pid DESC  LIMIT 1;";
	
	public static final String GET_EXAMEN_TERMINADO = "SELECT terminado FROM INVPExamenTerminado WHERE username = ?";
	
	public static final String GET_EXAMEN_TERMINADO_IDIOMA = "SELECT terminado FROM INVPExamenTerminado  WHERE username = ?";
	
	public static final String GET_USUARIO_TEMPORAL = "SELECT istemporal FROM IdiomaINVPUsuario WHERE username = ?";
}
