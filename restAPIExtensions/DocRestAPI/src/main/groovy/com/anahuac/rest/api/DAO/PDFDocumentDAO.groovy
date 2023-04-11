package com.anahuac.rest.api.DAO
import static org.bonitasoft.engine.bpm.process.ProcessInstanceCriterion.DEFAULT

import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.time.LocalDate
import java.time.Period

import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource

import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.process.ProcessInstanceCriterion
import org.bonitasoft.engine.identity.User

import com.anahuac.SDAE.model.SolicitudApoyoEducativo
import com.anahuac.SDAE.model.SolicitudApoyoEducativoDAO
import com.anahuac.SDAE.model.SolicitudFinanciamiento
import com.anahuac.SDAE.model.SolicitudFinanciamientoDAO
import com.anahuac.model.PadresTutor
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utils.FileDownload
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper
import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document as DocumentItext
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.VerticalPositionMark

class PDFDocumentDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public Boolean validarConexion() {
		  Boolean retorno=false
		  if (con == null || con.isClosed()) {
				con = new DBConnect().getConnection();
				retorno=true
		  }
		  return retorno
	}
	public Result PdfFileCatalogo(String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		InputStream targetStream;
		Boolean streamOpen = false;
		String log = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			Result dataResult = new Result();
			List<List < Object >> lstParams;
			
			object.intento = object.intento == null?0:object.intento;
			
			List<?> info = getInfoReportes(object.email, object.intento).getData();
			if(info.size() < 1) {
				throw new Exception("400 Bad Request Usuario no encontrado");
			}
			//log += isNullOrBlanck(info.get(0)?.idbanner.toString())
			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			columns.put("idbanner", isNullOrBlanck(info.get(0)?.idbanner.toString()));
			columns.put("nombreAspirante", isNullOrBlanck(info.get(0)?.nombre.toString()));
			columns.put("fechaNacimiento", isNullOrBlanck(info.get(0)?.fechanacimiento.toString()));
			columns.put("fotoPerfil", isNullOrBlanck(info.get(0)?.urlfoto.toString()));
			columns.put("preparatoria", isNullOrBlanck(info.get(0)?.preparatoria.toString()));
			columns.put("ciudad", isNullOrBlanck(info.get(0)?.ciudad.toString()));
			columns.put("pais", isNullOrBlanck(info.get(0)?.pais.toString()));
			columns.put("carreraEstudiar", isNullOrBlanck(info.get(0)?.carrera.toString()));
			columns.put("edad", isNullOrBlanck(info.get(0)?.edad.toString()));
			columns.put("promedio", isNullOrBlanck(info.get(0)?.promedio.toString()));
			columns.put("fecha", isNullOrBlanck(info.get(0)?.fechafinalizacion.toString()));
			columns.put("paav", isNullOrBlanck(info.get(0)?.paav.toString()));
			columns.put("paan", isNullOrBlanck(info.get(0)?.paan.toString()));
			columns.put("para", isNullOrBlanck(info.get(0)?.para.toString()));
			columns.put("paat", isNullOrBlanck( sumStrings(isNullOrBlanck(info.get(0)?.para.toString()),isNullOrBlanck(info.get(0)?.paan.toString()),isNullOrBlanck(info.get(0)?.paav.toString()) ) ));
			columns.put("invp", isNullOrBlanck(info.get(0)?.invp.toString()));
			columns.put("tipoAdmision", isNullOrBlanck(info.get(0)?.tipoadmision.toString()));
			columns.put("periodoIngreso", isNullOrBlanck(info.get(0)?.periodo.toString()));
			columns.put("entrevisto", isNullOrBlanck(info.get(0)?.quienrealizoentrevista.toString()));
			columns.put("integro", isNullOrBlanck(info.get(0)?.quienintegro.toString()));
			String caseid = info.get(0)?.caseid.toString()
			log = 1;
			info = getInfoRelativos(info.get(0)?.caseid.toString()).getData();
			Boolean[] familiares = [false,false,false]
			info.each{
				if(it?.parentesco.toString().equals("Padre") && !familiares[0]) {
					if(it?.desconozcodatospadres.toString().equals("t")  ) {
						columns.put("nombrePadre", "Se desconoce");
						columns.put("ocupacionPadre", "Se desconoce");
						columns.put("empresaPadre", "Se desconoce");
						columns.put("universidadPadre", "Se desconoce");
					} else if (it?.trabaja.toString().equals("No")) {
						columns.put("nombrePadre", isNullOrBlanck(it?.nombre.toString()));
						columns.put("ocupacionPadre", it?.jubilado.toString().equals("t") ?isNullOrBlanck("Jubilado") : isNullOrBlanck("No trabaja"));
						columns.put("empresaPadre", isNullOrBlanck("No trabaja"));
						columns.put("universidadPadre", isNullOrBlanck(it?.categresoanahuac_pid.toString())== "N/A"?"No":"Si" );
					} else {
						columns.put("nombrePadre", isNullOrBlanck(it?.nombre.toString()));
						columns.put("ocupacionPadre", isNullOrBlanck(it?.puesto.toString())+"  ${ (isNullOrBlanck(it?.vive.toString()).equals('Sí') || !isNullOrBlanck(it?.puesto.toString()).equals('N/A') )? (it?.jubilado.toString().equals('t')?'(jubilado)':'' ) :''} ");
						columns.put("empresaPadre", isNullOrBlanck(it?.empresatrabaja.toString()));
						columns.put("universidadPadre", isNullOrBlanck(it?.categresoanahuac_pid.toString())== "N/A"?"No":"Si" );
					}
					familiares[0] = true;
				}
				
				if(it?.parentesco.toString().equals("Madre") && !familiares[1]) {
					if(it?.desconozcodatospadres.toString().equals("t")) {
						columns.put("nombreMadre", "Se desconoce");
						columns.put("ocupacionMadre", "Se desconoce");
						columns.put("empresaMadre", "Se desconoce");
						columns.put("universidadMadre", "Se desconoce");
					} else if (it?.trabaja.toString().equals("No")) {
						columns.put("nombreMadre", isNullOrBlanck(it?.nombre.toString()));
						columns.put("ocupacionMadre", it?.jubilado.toString().equals("t") ?isNullOrBlanck("Jubilado") : isNullOrBlanck("No trabaja"));
						columns.put("empresaMadre", isNullOrBlanck("No trabaja"));
						columns.put("universidadMadre", isNullOrBlanck(it?.categresoanahuac_pid.toString())== "N/A"?"No":"Si" );
					} else {
						columns.put("nombreMadre", isNullOrBlanck(it?.nombre.toString()));
						columns.put("ocupacionMadre", isNullOrBlanck(it?.puesto.toString())+ " ${ (isNullOrBlanck(it?.vive.toString()).equals('Sí') || !isNullOrBlanck(it?.puesto.toString()).equals('N/A') ) ? (it?.jubilado.toString()=='t'?'(jubilado)':'' ) :''} ");
						columns.put("empresaMadre", isNullOrBlanck(it?.empresatrabaja.toString()));
						columns.put("universidadMadre", isNullOrBlanck(it?.categresoanahuac_pid.toString())== "N/A"?"No":"Si" );
					}
					
					familiares[1] = true;
				}
				
				if(it?.istutor.toString().equals("t")  && !familiares[2]) {
					
					columns.put("nombreTutor", isNullOrBlanck(it?.nombre.toString()));
					
					if(it?.trabaja.toString().equals("No")) {
						columns.put("ocupacionTutor", it?.jubilado.toString().equals("t") ?isNullOrBlanck("Jubilado") : isNullOrBlanck("No trabaja"));
						columns.put("empresaTutor", isNullOrBlanck("No trabaja"));
					}else {
						columns.put("ocupacionTutor", isNullOrBlanck(it?.puesto.toString())+"  ${ (it?.jubilado.toString()=='t'?'(jubilado)':'')  }");
						columns.put("empresaTutor", isNullOrBlanck(it?.empresatrabaja.toString()));
					}
					
					columns.put("universidadTutor", isNullOrBlanck(it?.categresoanahuac_pid.toString())== "N/A"?"No":"Si" );
					familiares[2] = true;
				}
				
				
			}
			log = 2;
			info = getInfoFuentesInfluyeron(caseid,object.intento)?.getData();
			columns.put("fuentesInfluyeron",  isNullOrBlanck(info?.get(0)?.fuentes.toString()) );
			
			info = getInfoRasgos(caseid,object.intento)?.getData();
			info.each{ 
				switch(it?.rasgo) {
					case "Salud aparente":
					columns.put("saludAparente", it.calificacion)
					break;
					case "Limpieza Personal":
					columns.put("limpiezaPersonal", it.calificacion)
					break;
					case "Impresión Física":
					columns.put("impresionFisica", it.calificacion)
					break;
					case "Manera de Relacionarse":
					columns.put("maneraRelacionarse", it.calificacion)
					break;
					case "Forma Expresarse":
					columns.put("formaExpresarse", it.calificacion)
					break;
					case "Educación":
					columns.put("educacion", it.calificacion)
					break;
					case "Cooperación":
					columns.put("cooperacion", it.calificacion)
					break;
					case "Sinceridad":
					columns.put("sinceridad", it.calificacion)
					break;
					case "Seguridad":
					columns.put("seguridad", it.calificacion)
					break;
					case "Serenidad":
					columns.put("serenidad", it.calificacion)
					break;
					case "Estabilidad Escolar":
					columns.put("EstabilidadEscolar", it.calificacion)
					break;
					case "Responsabilidad":
					columns.put("responsabilidad", it.calificacion)
					break;
					case "Motivación":
					columns.put("motivacion", it.calificacion)
					break;
					case "Hábitos de Estudio":
					columns.put("habitoEstudio", it.calificacion)
					break;
					case "Liderazgo":
					columns.put("liderazgo", it.calificacion)
					break;
					case "Iniciativa":
					columns.put("iniciativa", it.calificacion)
					break;
					case "Relación con la Autoridad":
					columns.put("relacionAutoridad", it.calificacion)
					break;
					case "Autocrítica":
					columns.put("autocritica", it.calificacion)
					break;
					case "Metas Realistas":
					columns.put("metaRealizada", it.calificacion)
					break;
					
				}
			}
			log = 3;
			info = getInfoSaludSSeccion(caseid,object.intento)?.getData();
			columns.put("salud",  isNullOrBlanck(info?.get(0)?.salud.toString()) );
			columns.put("conclusion",  isNullOrBlanck(info?.get(0)?.conclusiones_recomendaciones.toString()) );
			columns.put("interpretacion",  isNullOrBlanck(info?.get(0)?.interpretacion.toString()) );
			columns.put("pca",  isNullOrBlanck("No") );
			columns.put("pdp",  isNullOrBlanck("No") );
			columns.put("sse",  isNullOrBlanck("No") );
			columns.put("pdu",  isNullOrBlanck("No") );
			info?.each{
				switch(it?.cursos_recomendados) {
					case "PDU":
					columns.put("pdu",  isNullOrBlanck("Si") );
					break;
				case "SSE":
					columns.put("sse",  isNullOrBlanck("Si") );
					break;
				case "PDP":
					columns.put("pdp",  isNullOrBlanck("Si") );
					break;
				case "PCA":
					columns.put("pca",  isNullOrBlanck("Si") );
					break;
				default:
					break;
				}
			}
			log = 4;
			info = getInfoSaludPSeccion(caseid)?.getData();
			columns.put("vivesSituacionDiscapacidad",  isNullOrBlanck(info?.get(0)?.cat_situacion_discapacidad_descripcion.toString()) );
			columns.put("tipoDiscapacidad",  isNullOrBlanck(info?.get(0)?.tipo_discapacidad?.toString()) );
			columns.put("SituacionTipo",  isNullOrBlanck(info?.get(0)?.situaciontipo?.toString()) );
			columns.put("situacionDiscapacidad",  isNullOrBlanck(info?.get(0)?.problemassaludatencioncontinua?.toString()) );
			columns.put("personaSaludableDescripcion",  isNullOrBlanck(info?.get(0)?.cat_persona_saludable_descripcion.toString()) );
			columns.put("terapiaDescripcion",  isNullOrBlanck(info?.get(0)?.cat_terapia_descripcion.toString()) );
			columns.put("tipoTerapia",  isNullOrBlanck(info?.get(0)?.tipo_terapia.toString()) );
			log = 5;
			
			info =  getInfoCapacidadAdaptacion(caseid,object.intento)?.getData();
			columns.put("ajusteMedioFamiliar",  isNullOrBlanck(info?.get(0)?.ajustemediofamiliar.toString()) );
			columns.put("califajustemediofamiliar",  isNullOrBlanck(info?.get(0)?.califajustemediofamiliar.toString()) );
			columns.put("ajusteEscolarPrevio",  isNullOrBlanck(info?.get(0)?.ajusteescolarprevio.toString()) );
			columns.put("califajusteescolarprevio",  isNullOrBlanck(info?.get(0)?.califajusteescolarprevio.toString()) );
			columns.put("ajusteMedioSocial",  isNullOrBlanck(info?.get(0)?.ajustemediosocial.toString()) );
			columns.put("califajustemediosocial",  isNullOrBlanck(info?.get(0)?.califajustemediosocial.toString()) );
			columns.put("ajusteEfectivo",  isNullOrBlanck(info?.get(0)?.ajusteefectivo.toString()) );
			columns.put("califajusteafectivo",  isNullOrBlanck(info?.get(0)?.califajusteafectivo.toString()) );
			columns.put("ajusteReligioso",  isNullOrBlanck(info?.get(0)?.ajustereligioso.toString()) );
			columns.put("califajustereligioso",  isNullOrBlanck(info?.get(0)?.califajustereligioso.toString()) );
			columns.put("ajusteExistencial",  isNullOrBlanck(info?.get(0)?.ajusteexistencial.toString()) );
			columns.put("califajusteexistencial",  isNullOrBlanck(info?.get(0)?.califajusteexistencial.toString()) );
			log = 6;
			info = bitacoraPsicometrico(object.email,context)?.getData();
			log = 7;
			//log +=info
			String comentarios = "";
			info?.each { 
				comentarios += (comentarios.length() > 1?"<br>":"")+ it?.comentario
			}
			columns.put("comentarios",  isNullOrBlanck(comentarios) );
			
			Properties prop = new Properties();
			String propFileName = "configuration.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			String plantilla = prop.getProperty("jasperBase64")
			//columns.put("fotoFondo", prop.getProperty("fondo"));
			inputStream.close();
			
			byte [] file = Base64.getDecoder().decode(plantilla)
			targetStream = new ByteArrayInputStream(file);
			streamOpen = true;
			JasperReport jasperReport = JasperCompileManager.compileReport(targetStream)
			
			JRDataSource dataSource = new JREmptyDataSource();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, columns, dataSource);
			byte[] encode = Base64.getEncoder().encode(JasperExportManager.exportReportToPdf(jasperPrint));
			String result = new String(encode);
			
			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add(result)
			lstResultado.add(log)
			
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			resultado.setError_info(log)
			
		} catch (Exception e) {
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
			resultado.setError_info(log)
        }finally {
			if(streamOpen) {				
				targetStream.close();
			}
		}
		
		return resultado;
	}
	
	private String sumStrings(String val1="",val2="",val3="") {
		return "${((val1 == "N/A"?0:val1 as Integer) + (val2 == "N/A"?0:val2 as Integer) + (val3 == "N/A"?0:val3 as Integer))}"
	}
	
	private String isNullOrBlanck(String text) {
		if(text == null || text.equals(null) || text.equals("null") || text.equals("") || text.length() == 0) {
			return "N/A"
		}
		
	    return text;
	}
	
	private String encodeFileToBase64Binary(String fileName) throws IOException {

		File file = new File(fileName);
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encoder.encode(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}
	
	private static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		is.close();
		return bytes;
	}

	
	
	public Result getInfoReportes(String usuario,Long intento) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			if (rs.next()) {
				SSA = rs.getString("valor")
			}
			
			pstm = con.prepareStatement(Statements.INFO_REPORTE);
			pstm.setString(1, usuario)
			pstm.setLong(2, intento)
			rs = pstm.executeQuery();
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("urlfoto")) {
						String urlFoto = rs.getString("urlfoto");
						columns.put("urlfoto",  urlFoto+SSA );
					}else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getInfoRelativos(String caseid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT distinct on (pt.catparentezco_pid) pt.catparentezco_pid,cp.descripcion as parentesco, pt.empresatrabaja,puesto,pt.vive_pid, cv.descripcion AS vive, pt.desconozcoDatosPadres , cc.descripcion as campusAnahuac, pt.categresoanahuac_pid as egresoAnahuac, CONCAT(pt.nombre,' ',pt.apellidos) AS nombre, pt.isTutor, tpr.jubilado, ctr.descripcion AS trabaja FROM PadresTutor AS pt LEFT JOIN catParentesco as cp ON cp.persistenceid = pt.catparentezco_pid LEFT JOIN catCampus AS CC ON cc.persistenceid = catcampusegreso_pid LEFT JOIN CatEgresadoUniversidadAnahuac AS eua ON eua.persistenceid =  pt.categresoanahuac_pid LEFT JOIN catVive AS cv ON cv.persistenceid = pt.vive_pid LEFT JOIN TestPsicometricoRelativos AS tpr on tpr.caseid = pt.caseid::varchar AND tpr.catparentezco_pid = pt.catparentezco_pid LEFT JOIN CatPadreTrabaja AS ctr on ctr.persistenceid = pt.cattrabaja_pid where  pt.caseid ="+caseid)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getInfoRelativosHermanos(String caseid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT CONCAT(nombres,' ',apellidos) AS nombre, isestudia,istrabaja,escuelaestudia,empresatrabaja from hermano where caseid = "+caseid)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getInfoFuentesInfluyeron(String caseid, Long intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean autov1 = false;
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement("SELECT idlc.descripcion as fuentes  FROM CatinformacionDeLaCarrera AS idlc LEFT JOIN AUTODESCRIPCIO_INFORMACIONCAR AS ai ON idlc.persistenceid = ai.catinformaciondelacarrera_pid LEFT JOIN Autodescripcion AS auto ON auto.persistenceid = ai.autodescripcion_pid  where caseid = "+caseid)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					columns.put("autodescripcion", true);
					autov1 = true;
				}

				rows.add(columns);
			}
			
			if(!autov1) {
				//pstm = con.prepareStatement("SELECT fuentesInfluyeronDesicion as fuentes FROM autodescripcionv2 where caseid = "+caseid)
				pstm = con.prepareStatement("SELECT fuentesInfluyeronDesicion as fuentes FROM TestPsicometrico where caseid = '"+caseid+"' AND countRechazo = "+intentos)
				rs = pstm.executeQuery()
				rows = new ArrayList < Map < String, Object >> ();
				metaData = rs.getMetaData();
				columnCount = metaData.getColumnCount();
				while (rs.next()) {
					Map < String, Object > columns = new LinkedHashMap < String, Object > ();
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						columns.put("autodescripcionv2", true);
					}
	
					rows.add(columns);
				}
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getInfoRasgos(String caseid, Long intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT rc.descripcion AS calificacion, cro.descripcion AS rasgo FROM TestPsicometricoRasgos AS tpr LEFT JOIN CatRasgosCalif AS rc ON rc.persistenceid = tpr.calificacion_pid LEFT JOIN CatRasgosObservados AS cro ON cro.persistenceid = tpr.rasgo_pid where tpr.caseid = ?  AND tpr.countRechazo = ?")
			pstm.setString(1, caseid);
			pstm.setLong(2, intentos);
			rs = pstm.executeQuery();
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getInfoCapacidadAdaptacion(String caseid, Long intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT ajusteMedioFamiliar,califAjusteMedioFamiliar,ajusteEscolarPrevio,califAjusteEscolarPrevio,ajusteMedioSocial,califAjusteMedioSocial,ajusteEfectivo,califAjusteAfectivo,ajusteReligioso,califAjusteReligioso,ajusteExistencial,califAjusteExistencial FROM TestPsicometrico where caseid = ? AND countRechazo = ?")
			pstm.setString(1, caseid);
			pstm.setLong(2, intentos);
			rs = pstm.executeQuery();
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					columns.put("autodescripcionv2", true);
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getInfoSaludPSeccion(String caseid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean querySuccess = false;
		String strError = "";
		
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.SELECT_SITUACION_SALUD);
			pstm.setLong(1, Long.parseLong(caseid));
			rs = pstm.executeQuery();

			strError += "SELECT_SITUACION_SALUD: Success | ";
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				
				rows.add(columns);
			}
			
			
			strError += "Se obtuvieron los resultados | ";
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			resultado.setError_info(errorLog+" - Error: "+strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	public Result getInfoSaludSSeccion(String caseid, Long intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean querySuccess = false;
		String strError = "";
		
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			List<Map<String, Object>> lstCursos = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.SELECT_RECOMENDACIONES_CONCLUSIONES);
			pstm.setString(1, caseid);
			pstm.setLong(2, intentos);
			rs = pstm.executeQuery();
				
			strError += "SELECT_RECOMENDACIONES_CONCLUSIONES: Success | ";
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while (rs.next()) {
				columns = new LinkedHashMap < String, Object > ();
				
				for (int i = 1; i <= columnCount; i++) {
						columns.put("salud", rs.getString("salud"));
						columns.put("conclusiones_recomendaciones", rs.getString("conclusiones_recomendaciones"));
						columns.put("interpretacion", rs.getString("interpretacion"));
						columns.put("cursos_recomendados", rs.getString("cursos_recomendados"));
				}
				rows.add(columns);
			}


			
			strError += "Se obtuvieron los resultados | ";
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			resultado.setError_info(errorLog+" - Error: "+strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	public Boolean deleteJasper() {
		boolean fileSuccessfullyDeleted = false;
		try {
			fileSuccessfullyDeleted =  new File("Psicometrico_report.jrxml").delete()
		}catch(Exception e) {
			fileSuccessfullyDeleted = false; 
		}
		
		return fileSuccessfullyDeleted
	}
	
	public String base64Imagen(String url)  throws Exception {
		String b64 = "";
		if(url.toLowerCase().contains(".jpeg")) {
				b64 = ( "data:image/jpeg;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".png")) {
				b64 = ( "data:image/png;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".jpg")) {
				b64 = ( "data:image/jpg;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".jfif")) {
				b64 = ( "data:image/jfif;base64, "+(new FileDownload().b64Url(url)));
			}
		return  b64
	}
	
	public Result bitacoraPsicometrico(String usuarioComentario, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			pstm = con.prepareStatement("SELECT comentario,fechaCreacion,isEliminado,modulo,persistenceId,persistenceVersion,usuario,usuarioComentario FROM CatBitacoraComentarios where usuarioComentario = ? ORDER BY persistenceId DESC")
			pstm.setString(1, usuarioComentario)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("usuario")) {
						User usr;
						String responsables = rs.getString(i);
						String nombres= "";
						if(!responsables.equals("null") && responsables != null) {
							usr = context.getApiClient().getIdentityAPI().getUserByUserName(responsables);
							nombres=usr.getFirstName()+" "+usr.getLastName();
						}
						columns.put(metaData.getColumnLabel(i), nombres);
					}else {
						columns.put(metaData.getColumnLabel(i), rs.getString(i));
					}
					
					
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result pdfDatosAval(String email, RestAPIContext context) {
		Result resultado = new Result();
		InputStream targetStream;
		Boolean streamOpen = false;
		String errorLog = "";
		
		try {
			errorLog += "Entr al metodo ";
			def jsonSlurper = new JsonSlurper();
			Result dataResult = new Result();
			List<List < Object >> lstParams;
			Result infoAval = getInfoAval(email, context);
			List<?> info = infoAval.getData();
			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			errorLog += " | Ontuvo info del aval ";
			errorLog += " | " + infoAval.getError_info();
			
			if(info.size() < 1) {
				throw new Exception("400 Bad Request Usuario no encontrado");
			} else {
				columns = (Map < String, Object >) info.get(0);
			}
			
			String comentarios = "";
			Properties prop = new Properties();
			String propFileName = "configuration.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			
			String plantilla = prop.getProperty("jasperDatosAvalB64");
			inputStream.close();
			byte [] file = Base64.getDecoder().decode(plantilla);
			targetStream = new ByteArrayInputStream(file);
			streamOpen = true;
			JasperReport jasperReport = JasperCompileManager.compileReport(targetStream);
			JRDataSource dataSource = new JREmptyDataSource();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, columns, dataSource);
			byte[] encode = Base64.getEncoder().encode(JasperExportManager.exportReportToPdf(jasperPrint));
			String result = new String(encode);
			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add(result)
			lstResultado.add(errorLog)
						
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			resultado.setError_info(errorLog);
			
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			errorLog += e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		}finally {
			if(streamOpen) {
				targetStream.close();
			}
		}
		
		return resultado;
	}
	
	public Result getInfoAval(String email, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Long caseidapoyo = 0L;
		Map < String, Object > columns = new LinkedHashMap < String, Object > ();
		
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			rows = new ArrayList < Map < String, Object >> ();
			String SSA = "";
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				SSA = rs.getString("valor")
			}
			
			pstm = con.prepareStatement(Statements.GET_DATOS_AVAL);
			pstm.setString(1, email);
			rs = pstm.executeQuery();
			
			//Agregando datos del aval 
			while (rs.next()) {
				errorLog += " | Datos del aval encontrados " ;
				columns = new LinkedHashMap < String, Object > ();
				String urlFoto = rs.getString("fotosol");
				errorLog += " |" + urlFoto + SSA;
				columns.put("urlFoto",  urlFoto + SSA);
				columns.put("nombre", rs.getString("primernombre") + " " + rs.getString("segundonombre"));
				columns.put("idBanner", rs.getString("idbanner"));
				columns.put("carrera", rs.getString("carrera"));
				columns.put("periodo", rs.getString("periodo"));
				columns.put("apellidoPaterno", rs.getString("apellidopaterno"));
				columns.put("apellidoMaterno", rs.getString("apellidomaterno") != null ? rs.getString("apellidomaterno") : "");
				columns.put("porcFinSolicitado", rs.getString("porcentajefinanciamientosolicitado") + "%");
				columns.put("porcFinPrea", rs.getString("porcentajefinanciamientopreautorizado"));
				columns.put("nombresAval", rs.getString("avalnombres"));
				columns.put("apellidoPaternoAval", rs.getString("avalapellido_p"));
				columns.put("apellidoMaternoAval", rs.getString("avalapellido_m"));
				columns.put("curpAval", rs.getString("avalcurp"));
				columns.put("rfcAval", rs.getString("rfc"));
				columns.put("parentescoAval", "");
				columns.put("telefonoAval", rs.getString("avaltelefono"));
				columns.put("emailPersonalAval", rs.getString("avalemail"));
				columns.put("ingresoMensual", rs.getString("ingresomensual"));
				columns.put("provenienteDe", rs.getString("provenientede"));
				columns.put("otroIngreso", rs.getString("otroingreso"));
				columns.put("ingresoMensualTotal", rs.getString("ingresomensualtotal"));
				columns.put("egresoMensualTotal", rs.getString("egresomensualtotal"));
				columns.put("saldoMensual", rs.getString("saldomensual"));
				columns.put("paisDomicilioAval", rs.getString("avaldomiciliopais"));
				columns.put("cpDomicilioAval", rs.getString("avaldomiciliocp"));
				columns.put("estadoDomicilioAval", rs.getString("avaldomicilioestado"));
				columns.put("ciudadDomicilioAval", rs.getString("avaldomiciliociudad"));
				columns.put("coloniaDomicilioAval", rs.getString("avaldomiciliocolonia"));
				columns.put("calleNumeroDomicilioAval", rs.getString("avaldomiciliocalleynumero"));
				columns.put("empresaTrabajaAval", rs.getString("avaltrabajoempresa"));
				columns.put("empresaPuestoAval", rs.getString("avaltrabajopuesto"));
				columns.put("empresaTelefonoAval", rs.getString("avaltrabajotelefono"));
				columns.put("empresaTelefonoExtAval", rs.getString("avaltrabajotelefonoext"));
				columns.put("empresaFechaIngresoAval", buildDate(rs.getString("avaltrabajofechaingreso")));
				columns.put("empresaEmailAval", rs.getString("avaltrabajoemail"));
				columns.put("paisDomicilioEmpresa", rs.getString("avaltrabajopais"));
				columns.put("cpDomicilioEmpresa", rs.getString("avaltrabajocp"));
				columns.put("estadoDomicilioEmpresa", rs.getString("avaltrabajoestado"));
				columns.put("ciudadDomicilioEmpresa", rs.getString("avaltrabajociudad"));
                columns.put("coloniaDomicilioEmpresa", rs.getString("avaltrabajocolonia"));
				columns.put("calleNumeroDomicilioEmpresa", rs.getString("avaltrabajocalleynumero"));
				columns.put("paisDomicilioInmueble", rs.getString("inmueblepais"));
				columns.put("cpDomicilioInmueble", rs.getString("inmueblecp"));
				columns.put("estadoDomicilioInmueble", rs.getString("inmuebleestado"));
				columns.put("ciudadDomicilioInmueble", rs.getString("inmuebleciudad"));
				columns.put("coloniaDomicilioInmueble", rs.getString("inmueblecolonia"));
				columns.put("noInteriorDomicilioInmueble", rs.getString("inmueblenointerior"));
				columns.put("calleDomicilioInmueble", rs.getString("inmueblecalle") );
				columns.put("calleNumeroDomicilioInmueble", rs.getString("inmueblecalle") + " #" +  rs.getString("inmueblenoexterior"));
				columns.put("noExteriorDomicilioInmueble", rs.getString("inmueblenoexterior"));
				columns.put("notario", rs.getString("notario"));
				columns.put("noNotaria", rs.getString("numeronotaria"));
				columns.put("noEscritura", rs.getString("numeroescritura"));
				columns.put("fechaNotaria", buildDate(rs.getString("fechanotario")));
				columns.put("volumen", rs.getString("volumennotario"));
				columns.put("libro", rs.getString("libronotario"));
				columns.put("lugarDeEscrituracion", rs.getString("lugardeescrituracionnotario"));
				columns.put("folio", rs.getString("foliopropiedad"));
				columns.put("tomo", rs.getString("tomopropiedad"));
				columns.put("libroRegistro", rs.getString("libropropiedad"));
				columns.put("tomoPropiedad", rs.getString("tomopropiedad"));
				columns.put("fechaRegistro", buildDate(rs.getString("fechapropiedad")));
				columns.put("empresaExtensionAval", "N/A");
				
				//Domicilio solicitante 
				columns.put("nuevoIngreso", true);
				columns.put("avanzado", false);
				columns.put("calleNumeroSol", rs.getString("callesol") + " #" + rs.getString("numerosol"));
				columns.put("coloniaSol", rs.getString("coloniasol"));
				columns.put("cpSol", rs.getString("cpsol"));
				columns.put("ciudadSol", rs.getString("ciudadsol"));
				columns.put("estadoSol", rs.getString("estadosol"));
				columns.put("paisSol", rs.getString("paissol"));
				columns.put("telefonoSol", rs.getString("telefonosol"));
				columns.put("celularSol", rs.getString("celularsol"));
				columns.put("emailSol", rs.getString("correosol"));
				
				caseidapoyo = rs.getLong("caseidapoyo");
//				rows.add(columns);
			}
			
			/*Referencias personales del solcitante*/
			pstm = con.prepareStatement(Statements.GET_REFERENCIAS_PERSONALES);
			pstm.setLong(1, caseidapoyo);
			pstm.setBoolean(2, true);
			rs = pstm.executeQuery();
			
			Map < String, Object > referenciaPersonalSolicitante = new LinkedHashMap < String, Object > ();
			List<Map < String, Object >> lstReferenciasPersonalesSolicitante = new ArrayList<Map < String, Object > >();
			while (rs.next()) {
				referenciaPersonalSolicitante = new LinkedHashMap < String, Object > ();
				referenciaPersonalSolicitante.put("nombre", rs.getString("nombre"));
				referenciaPersonalSolicitante.put("parentesco", rs.getString("parentesco"));
				referenciaPersonalSolicitante.put("telefono", rs.getString("telefono"));
				referenciaPersonalSolicitante.put("email", rs.getString("correopersonal"));
				lstReferenciasPersonalesSolicitante.add(referenciaPersonalSolicitante);
			}
			JRBeanCollectionDataSource referenciasPersonalesSolicitante = new JRBeanCollectionDataSource(lstReferenciasPersonalesSolicitante);
			columns.put("referenciasPersonalesSolicitante", referenciasPersonalesSolicitante);
			
			/*Referencias personales del aval*/
			pstm = con.prepareStatement(Statements.GET_REFERENCIAS_PERSONALES);
			pstm.setLong(1, caseidapoyo);
			pstm.setBoolean(2, false);
			rs = pstm.executeQuery();
			
			Map < String, Object > referenciaPersonalAval = new LinkedHashMap < String, Object > ();
			List<Map < String, Object >> lstReferenciasPersonalesAval = new ArrayList<Map < String, Object > >();
			while (rs.next()) {
				referenciaPersonalAval = new LinkedHashMap < String, Object > ();
				referenciaPersonalAval.put("nombre", rs.getString("nombre"));
				referenciaPersonalAval.put("parentesco", rs.getString("parentesco"));
				referenciaPersonalAval.put("telefono", rs.getString("telefono"));
				referenciaPersonalAval.put("email", rs.getString("correopersonal"));
				lstReferenciasPersonalesAval.add(referenciaPersonalAval);
			}
			JRBeanCollectionDataSource referenciasPersonalesAval = new JRBeanCollectionDataSource(lstReferenciasPersonalesAval);
			columns.put("referenciasPersonalesAval", referenciasPersonalesAval);
			
			/*Referencias bancarias del aval*/
			pstm = con.prepareStatement(Statements.GET_REFERENCIAS_BANCARIAS);
			pstm.setLong(1, caseidapoyo);
			pstm.setBoolean(2, true);
			rs = pstm.executeQuery();
			
			Map < String, Object > referenciaBancariaAval = new LinkedHashMap < String, Object > ();
			List<Map < String, Object >> lstReferenciasBancariasAval = new ArrayList<Map < String, Object > >();
			while (rs.next()) {
				referenciaBancariaAval = new LinkedHashMap < String, Object > ();
				referenciaBancariaAval.put("banco", rs.getString("banco"));
				referenciaBancariaAval.put("tipoCuenta", rs.getString("tipocuenta"));
				referenciaBancariaAval.put("numeroCuenta", rs.getString("numerocuenta"));
				referenciaBancariaAval.put("saldoPromedio", rs.getString("saldopromedio"));
				lstReferenciasBancariasAval.add(referenciaBancariaAval);
			}
			JRBeanCollectionDataSource referenciasBancarias = new JRBeanCollectionDataSource(lstReferenciasBancariasAval);
			columns.put("referenciasBancarias", referenciasBancarias);
			
			/*Referencias bancarias del aval*/
			pstm = con.prepareStatement(Statements.GET_REFERENCIAS_BANCARIAS);
			pstm.setLong(1, caseidapoyo);
			pstm.setBoolean(2, false);
			rs = pstm.executeQuery();
			
			Map < String, Object > referenciaCreditoAval = new LinkedHashMap < String, Object > ();
			List<Map < String, Object >> lstReferenciasCreditoAval = new ArrayList<Map < String, Object > >();
			while (rs.next()) {
				referenciaCreditoAval = new LinkedHashMap < String, Object > ();
				referenciaCreditoAval.put("banco", rs.getString("banco"));
				referenciaCreditoAval.put("tipoCuenta", rs.getString("tipocuenta"));
				referenciaCreditoAval.put("numeroCuenta", rs.getString("numerocuenta"));
				referenciaCreditoAval.put("saldoPromedio", rs.getString("saldopromedio"));
				lstReferenciasCreditoAval.add(referenciaCreditoAval);
			}
			JRBeanCollectionDataSource referenciasCredito = new JRBeanCollectionDataSource(lstReferenciasCreditoAval);
			columns.put("referenciasCredito", referenciasCredito);
			
			rows.add(columns);//Agregando columnas al final 
			
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError_info(errorLog);
		}catch (Exception e) {
			errorLog += e.getMessage();
			resultado.setError_info(errorLog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	private String buildDate(String input) {
		String output = "";
		if( input != null) {
			String datein = input.split("T")[0];
			String[] dateparts = datein.split("-");
			output = dateparts[2] + "/" + dateparts[1] + "/" + dateparts[0];
		}
		return output;
	}
	
	private static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
		if ((birthDate != null) && (currentDate != null)) {
			return Period.between(birthDate, currentDate).getYears();
		} else {
			return 0;
		}
	}
	
	private Integer getAge (String input) {
		String[] dateparts = input.split("/");
		Date now = new Date();
		return calculateAge( 
			LocalDate.of(
				Integer.valueOf(dateparts[2]), 
				Integer.valueOf(dateparts[1]), 
				Integer.valueOf(dateparts[0])
			),  
			LocalDate.of(
				now.getYear() + 1900, 
				now.getMonth() + 1, 
				now.getDate()
			)
		);
	}
	
	public Result pdfSolicitudApoyo(String email, String caseid, RestAPIContext context) {
		Result resultado = new Result();
		InputStream targetStream;
		Boolean streamOpen = false;
		String errorLog = "";
		
		try {
			errorLog += "Entr al metodo ";
			def jsonSlurper = new JsonSlurper();
			Result dataResult = new Result();
			List<List < Object >> lstParams;
			Result solicitud = getSolicitudApoyo(email, caseid, context);
			List<?> info = solicitud.getData();
			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			errorLog += " | Ontuvo info del aval ";
			errorLog += " | " + solicitud.getError_info();
			
            if(info != null){
                if(info.size() < 1) {
                    throw new Exception("400 Bad Request Usuario no encontrado");
                } else {
                    columns = (Map < String, Object >) info.get(0);
                }
            } else {
                throw new Exception("400 Bad Request Usuario no encontrado");
            }
			
			String comentarios = "";
			Properties prop = new Properties();
			String propFileName = "configuration.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			
			String plantilla = prop.getProperty("jasperDatosSolicitud");
			inputStream.close();
			byte [] file = Base64.getDecoder().decode(plantilla);
			targetStream = new ByteArrayInputStream(file);
			streamOpen = true;
			JasperReport jasperReport = JasperCompileManager.compileReport(targetStream);
			JRDataSource dataSource = new JREmptyDataSource();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, columns, dataSource);
			byte[] encode = Base64.getEncoder().encode(JasperExportManager.exportReportToPdf(jasperPrint));
			String result = new String(encode);
			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add(result)
			lstResultado.add(errorLog)
						
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			resultado.setError_info(errorLog);
			
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			errorLog += e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		}finally {
			if(streamOpen) {
				targetStream.close();
			}
		}
		
		return resultado;
	}
	
	public Result getSolicitudApoyo(String email, String caseId, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Long caseidSolicitud = 0L;
		String idbanner = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			rows = new ArrayList < Map < String, Object >> ();
			String SSA = "";
			
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				SSA = rs.getString("valor");
			}
			
			pstm = con.prepareStatement(Statements.GET_SOLICITUD_ADMISION_INFO);
			pstm.setString(1, email);
			rs = pstm.executeQuery();
			Map < String, Object > columns = new HashMap < String, Object > ();
			
			while (rs.next()) {
				String urlFoto = rs.getString("urlfoto");
				String nombre = rs.getString("primernombre");
				String preparatoria = rs.getString("preparatoria")// ? rs.getString("preparatoria") : rs.getString("otraprepa");
				nombre += rs.getString("segundonombre") ? " " +  rs.getString("segundonombre") : "";
				nombre +=  " " + rs.getString("apellidopaterno");
				nombre += rs.getString("apellidomaterno") ? " " + rs.getString("apellidomaterno") : "";
				String fechaNac = buildDate(rs.getString("fechanacimiento"));
				errorLog += " | fechaNac: " + fechaNac;
				columns.put("urlFoto",  urlFoto + SSA);
				columns.put("promedioPreparatoria", rs.getString("promediogeneral"));
				columns.put("nombreAsp", nombre);
				columns.put("sexoAsp", rs.getString("sexo"));
				columns.put("tipoIngresoAsp", rs.getString("tipoingreso"));
				columns.put("fechaNacAsp", fechaNac);
				columns.put("idAsp", rs.getString("idbanner"));
				columns.put("correoAsp", email);
				columns.put("carrera", rs.getString("carrera"));
				columns.put("periodo", rs.getString("periodo"));
				columns.put("edadAsp", getAge(fechaNac).toString());
				columns.put("preparatoria", preparatoria);
				columns.put("paisPreparatoria", rs.getString("paisprepa"));//PENDING
				columns.put("ciudadPreparatoria", rs.getString("estadoprepa"));//PENDING
				columns.put("estadoPreparatoria", rs.getString("ciudadprepa"));//PENDING
				columns.put("calleAsp", rs.getString("calle"));
				columns.put("numExtAsp", rs.getString("idbanner"));
				idbanner = rs.getString("idbanner");
				columns.put("cpAsp", rs.getString("cp"));
				columns.put("paisAsp", rs.getString("pais"));
				columns.put("telefonoAsp", rs.getString("telefono"));
				columns.put("trabajasAsp", rs.getString("trabajas"));
				columns.put("coloniaAsp", rs.getString("colonia"));
				columns.put("ciudadAsp", rs.getString("ciudad"));
				columns.put("estadoAsp", rs.getString("estado"));
				columns.put("celularAsp", rs.getString("telefonocelular"));
				columns.put("estadoCivilAsp", rs.getString("estadocivil"));
				caseidSolicitud = rs.getLong("caseid");
			}
			
			errorLog += " | " + columns.toString();
			
			pstm = con.prepareStatement(Statements.GET_PADRES_TUTOR_BY_CASEID);
			pstm.setLong(1, caseidSolicitud);
			rs = pstm.executeQuery();
			
			Boolean tutor = false;
			Boolean padre = false;
			Boolean madre = false;
			Boolean desconozcoPadre = false;
			Boolean desconozcoMadre = false;
			
			while (rs.next()) {
				if(rs.getBoolean("istutor") && !tutor) {
					columns.put("nombreTutor", rs.getString("nombre") + " " + rs.getString("apellidos"));
					columns.put("emailTutor", rs.getString("correoelectronico"));
					columns.put("ocupacionTutor", rs.getString("titulo"));
					columns.put("telefonoCasaTutor", rs.getString("telefono"));
					columns.put("parentescoTutor", rs.getString("parentesco"));
					columns.put("empresaTutor", rs.getString("empresatrabaja") ?  rs.getString("empresatrabaja") : "N/A");
					columns.put("puestoTutor", rs.getString("puesto") ? rs.getString("puesto") : "N/A");
					tutor = true;
				} else if(rs.getString("parentesco").toLowerCase().equals("padre") && !padre) {
					desconozcoPadre = rs.getBoolean("desconozcodatospadres");
					if(rs.getBoolean("desconozcodatospadres")) {
						columns.put("nombrePadre", "Se desconoce");
						columns.put("correoElectronicoPadre", "Se desconoce");
						columns.put("ocupacionPadre", "Se desconoce");
						columns.put("telefonoCasaPadre", "Se desconoce");
						columns.put("vivePadre", "Se desconoce");
						columns.put("empresaPadre", "Se desconoce");
						columns.put("puestoPadre", "Se desconoce");
					} else {
						columns.put("nombrePadre", rs.getString("nombre") + " " + rs.getString("apellidos"));
						columns.put("correoElectronicoPadre", rs.getString("correoelectronico"));
						columns.put("ocupacionPadre", rs.getString("titulo"));
						columns.put("telefonoCasaPadre", rs.getString("telefono"));
						columns.put("vivePadre", rs.getString("vive"));
						columns.put("empresaPadre", rs.getString("empresatrabaja") ? rs.getString("empresatrabaja") : "N/A");
						columns.put("puestoPadre", rs.getString("puesto") ? rs.getString("puesto") : "N/A");
					}
					padre = true;
				} else if(rs.getString("parentesco").toLowerCase().equals("madre") && !madre) {
					desconozcoMadre = rs.getBoolean("desconozcodatospadres");
					if(rs.getBoolean("desconozcodatospadres")) {
						columns.put("nombreMadre", "Se desconoce");
						columns.put("correoElectronicoMadre", "Se desconoce");
						columns.put("ocupacionMadre", "Se desconoce");
						columns.put("telefonoCasaMadre", "Se desconoce");
						columns.put("viveMadre", "Se desconoce");
						columns.put("empresaMadre", "Se desconoce");
						columns.put("puestoMadre", "Se desconoce");
					} else {
						columns.put("nombreMadre", rs.getString("nombre") + " " + rs.getString("apellidos"));
						columns.put("correoElectronicoMadre", rs.getString("correoelectronico"));
						columns.put("ocupacionMadre", rs.getString("titulo"));
						columns.put("telefonoCasaMadre", rs.getString("telefono"));
						columns.put("viveMadre", rs.getString("vive"));
						columns.put("empresaMadre", rs.getString("empresatrabaja") ? rs.getString("empresatrabaja") : "N/A");
						columns.put("puestoMadre", rs.getString("puesto") ? rs.getString("puesto") : "N/A");
					}
					madre = true;
				} 
				
				if(tutor && padre && madre) {
					break;
				}
			}
			
			pstm = con.prepareStatement(Statements.GET_SOLICITUD_APOYO_INFO);
			pstm.setLong(1, Long.valueOf(caseId));
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				columns.put("terrenoM2Casa", rs.getString("terrenom2casa"));
				columns.put("casaDondeVives", rs.getString("casadondevives"));
				columns.put("valorAproxCasa", formatCurrency(rs.getString("valoraproxcasa")));
				columns.put("construccionM2Casa", rs.getString("contruccionm2casa"));
				columns.put("ingresoPadre", formatCurrency(rs.getString("ingresopadre")));
				columns.put("ingresoMadre", formatCurrency(rs.getString("ingresomadre")));
				columns.put("ingresoHermano", formatCurrency(rs.getString("ingresohermano")));
				columns.put("ingresoTio", formatCurrency(rs.getString("ingresotio")));
				columns.put("ingresoAbuelo", formatCurrency(rs.getString("ingresoabuelo")));
				columns.put("ingresoAspirante", formatCurrency(rs.getString("ingresoaspirante")));
				columns.put("ingresoOtro", formatCurrency(rs.getString("ingresootro")));
				columns.put("egresoRenta", formatCurrency(rs.getString("egresorenta")));
				columns.put("egresoServicios", formatCurrency(rs.getString("egresoservicios")));
				columns.put("egresoEducacion", formatCurrency(rs.getString("egresoeducacion")));
				columns.put("egresoGastosMedicos", formatCurrency(rs.getString("egresogastosmedicos")));
				columns.put("egresoAlimentacion", formatCurrency(rs.getString("egresoalimentacion")));
				columns.put("egresoVestido", formatCurrency(rs.getString("egresovestido")));
				columns.put("egresoSeguros", formatCurrency(rs.getString("egresoseguro")));
				columns.put("egresoDiversion", formatCurrency(rs.getString("egresodiversion")));
				columns.put("egresoAhorro", formatCurrency(rs.getString("egresoahorro")));
				columns.put("egresoCreditos", formatCurrency(rs.getString("egresocreditos")));
				columns.put("egresoOtro", formatCurrency(rs.getString("egresootros")));
				columns.put("totalIngresos", formatCurrency(rs.getString("ingresototal")));
				columns.put("totalEgresos", formatCurrency(rs.getString("egresototal")));
				columns.put("sexoTutor", rs.getString("sexoTutor"));
				columns.put("provieneIngresosTutor", rs.getString("provieneningresos"));
				columns.put("telefonoCelTutor", rs.getString("telefonocelulartutor") != null && !rs.getString("telefonocelulartutor").equals("") ? rs.getString("telefonocelulartutor") : "N/A");
				columns.put("telefonoOficinaTutor", rs.getString("telefonoOficinaTutor") != null && !rs.getString("telefonoOficinaTutor").equals("") ? rs.getString("telefonoOficinaTutor") : "N/A");
				columns.put("ingresoMensualTutor", formatCurrency(rs.getString("ingresomensualnetotutor")));
				columns.put("telefonoOficinaPadre", desconozcoPadre ? "Se desconoce" : rs.getString("telefonooficinapadre") != null ? rs.getString("telefonooficinapadre") : "N/A");
				columns.put("telefonoCelularPadre", desconozcoPadre ? "Se desconoce" : rs.getString("telefonocasapadre"));
				columns.put("ingresoMensualPadre", desconozcoPadre ? "Se desconoce" : formatCurrency(rs.getString("ingresopadre")));
				columns.put("telefonoOficinaMadre", desconozcoMadre ? "Se desconoce" : rs.getString("telefonooficinamadre") != null ? rs.getString("telefonooficinamadre") : "N/A");
				columns.put("telefonoCelularMadre", desconozcoMadre ? "Se desconoce" : rs.getString("telefonocasamadre") != null ? rs.getString("telefonocasamadre") : "N/A");
				columns.put("ingresoMensualMadre", desconozcoMadre ? "Se desconoce" : formatCurrency(rs.getString("ingresomadre")));
				columns.put("colegiaturaAsp", formatCurrency(rs.getString("colegiatura")));
				columns.put("porcentajeBeca", rs.getString("porcentajebecaautorizacion") ? rs.getString("porcentajebecaautorizacion") : "");
				columns.put("porcentajeFinan", rs.getString("porcentajecreditoautorizacion") ? rs.getString("porcentajecreditoautorizacion") : "");
				String fechaAutoriza = buildDate(rs.getString("fechaautorizacion"));
				columns.put("fecha", fechaAutoriza);
				columns.put("autoriza", rs.getString("usuarioautoriza"));
				columns.put("comentarioComite", rs.getString("cambiosSolicitudAutorizacionText"));
			}
			
			pstm = con.prepareStatement(Statements.GET_BIENES_RAICES_BY_CASEID);
			pstm.setLong(1, Long.valueOf(caseId));
			rs = pstm.executeQuery();
			
			Map < String, Object > bienRaiz = new LinkedHashMap < String, Object > ();
			List<Map < String, Object >> lstBienesRaices = new ArrayList<Map < String, Object > >();
			Boolean mostrarBienesRaices = false;
			while (rs.next()) {
				mostrarBienesRaices = true;
				bienRaiz = new LinkedHashMap < String, Object > ();
				bienRaiz.put("descripcion", rs.getString("descripcion"));
				bienRaiz.put("direccionbanco", rs.getString("direccionbanco"));
				bienRaiz.put("valor", formatCurrency(rs.getString("valor")));
				bienRaiz.put("tipo", rs.getString("tipo"));
				lstBienesRaices.add(bienRaiz);
			}
			JRBeanCollectionDataSource bienesRaices = new JRBeanCollectionDataSource(lstBienesRaices);
			columns.put("bienesRaices", bienesRaices);
			columns.put("mostrarBienesRaices", mostrarBienesRaices);
			
			pstm = con.prepareStatement(Statements.GET_HERMANOS_BY_CASEID);
			pstm.setLong(1, Long.valueOf(caseId));
			rs = pstm.executeQuery();
			
			Map < String, Object > hermano = new LinkedHashMap < String, Object > ();
			List<Map < String, Object >> lstHermanos = new ArrayList<Map < String, Object > >();
			Boolean mostrarHermanos = false;
			while (rs.next()) {
				mostrarHermanos = true;
				hermano = new LinkedHashMap < String, Object > ();
				hermano.put("nombres", rs.getString("nombres"));
				hermano.put("apellidos", rs.getString("apellidos"));
				hermano.put("edad", rs.getString("edad"));
				hermano.put("isestudia", rs.getBoolean("isestudia") ? "Sí": "No");
				hermano.put("colegiaturamensual", rs.getString("colegiaturamensual"));
				hermano.put("institucion", rs.getString("institucion"));
				hermano.put("istienebeca", rs.getBoolean("istienebeca") ? "Sí": "No");
				hermano.put("porcentajebecaasignado", rs.getString("porcentajebecaasignado"));
				hermano.put("istrabaja", rs.getBoolean("istrabaja") ? "Sí": "No");
				hermano.put("empresa", rs.getString("empresa"));
				hermano.put("ingresomensual", formatCurrency(rs.getString("ingresomensual")));
				lstHermanos.add(hermano);
			}
			JRBeanCollectionDataSource hermanos = new JRBeanCollectionDataSource(lstHermanos);
			columns.put("hermanos", hermanos);
			columns.put("mostrarHermanos", mostrarHermanos);
			
			pstm = con.prepareStatement(Statements.GET_AUTOS_BY_CASEID);
			pstm.setLong(1, Long.valueOf(caseId));
			rs = pstm.executeQuery();
			
			Map < String, Object > auto = new LinkedHashMap < String, Object > ();
			List<Map < String, Object >> lstAutos = new ArrayList<Map < String, Object > >();
			Boolean mostrarAutos = false;
			while (rs.next()) {
				mostrarAutos = true;
				auto = new LinkedHashMap < String, Object > ();
				auto.put("marca", rs.getString("marca"));
				auto.put("modelo", rs.getString("modelo"));
				auto.put("ano", rs.getString("ano"));
				auto.put("situacion", rs.getString("situacion"));
				lstAutos.add(auto);
			}
			JRBeanCollectionDataSource autos = new JRBeanCollectionDataSource(lstAutos);
			columns.put("autos", autos);
			columns.put("mostrarAutos", mostrarAutos);
			
			pstm = con.prepareStatement(Statements.GET_IMAGENES_BY_CASEID);
			pstm.setLong(1, Long.valueOf(caseId));
			rs = pstm.executeQuery();
			
			Map < String, Object > imagen = new LinkedHashMap < String, Object > ();
			List<Map < String, Object >> lstImagenes = new ArrayList<Map < String, Object > >();
			Boolean mostrarImagenes = false;
			while (rs.next()) {
				mostrarImagenes = true;
				//Primera entrada al while es para la primera imagen (se muestran en doscolumnas)
				imagen = new LinkedHashMap < String, Object > ();
				imagen.put("urlimagen", rs.getString("urlimagen") + SSA);
				imagen.put("descripcion", rs.getString("descripcion"));
				//If es para la segunda columna (El objeto contiene las dos imagenes y los dos titulos)
				if(rs.next()) {
					imagen.put("urlimagen2", rs.getString("urlimagen") + SSA);
					imagen.put("descripcion2", rs.getString("descripcion"));
				}
				
				lstImagenes.add(imagen);
			}
			JRBeanCollectionDataSource imagenes = new JRBeanCollectionDataSource(lstImagenes);
			columns.put("imagenes", imagenes);
			columns.put("mostrarImagenes", mostrarImagenes);
			
			pstm = con.prepareStatement(Statements.GET_PAA_BY_IDBANNER_SIN_PERSISTENCE);
			pstm.setString(1, idbanner);
			rs = pstm.executeQuery();
			
			//Puntuaciones
			if(rs.next()) {
				columns.put("v", rs.getString("paav"));
				columns.put("n", rs.getString("paan"));
				columns.put("rj", rs.getString("para"));
				columns.put("total", rs.getString("total"));
			} else {
				columns.put("v", "P/A");
				columns.put("n", "P/A");
				columns.put("rj", "P/A");
				columns.put("total", "P/A");
			}
			
			resultado.setSuccess(true);
			rows.add(columns);
			resultado.setData(rows);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			errorLog += e.getMessage();
			resultado.setError_info(errorLog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	private String formatCurrency(String input) {
		if(input.equals("")) {
			return "";
		} else {
			return "\$ " + input + ".00";
		}
	}
}