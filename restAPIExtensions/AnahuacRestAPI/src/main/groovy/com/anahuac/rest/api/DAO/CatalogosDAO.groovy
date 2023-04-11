package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.CatCodigoPostalCustomFiltro
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.AzureConfig
import com.anahuac.rest.api.Entity.Custom.CatBachilleratoCustomFiltro
import com.anahuac.rest.api.Entity.Custom.CatCampusCustom
import com.anahuac.rest.api.Entity.Custom.CatCampusCustomFiltro
import com.anahuac.rest.api.Entity.Custom.CatCiudadCustonFiltro
import com.anahuac.rest.api.Entity.Custom.CatPaisCustomFiltro
import com.anahuac.catalogos.CatBachilleratos
import com.anahuac.catalogos.CatEstados
import com.anahuac.catalogos.CatPais
import com.anahuac.catalogos.CatPropedeutico
import com.anahuac.catalogos.CatTitulo
import com.anahuac.rest.api.Entity.Custom.CatDescuentosCustom
import com.anahuac.rest.api.Entity.Custom.CatEscolaridadCustom
import com.anahuac.rest.api.Entity.Custom.CatEstadoCivilCustom
import com.anahuac.rest.api.Entity.Custom.CatEstadoCustomFiltro
import com.anahuac.rest.api.Entity.Custom.CatFiltradoCatalogosAutoDescripcion
import com.anahuac.rest.api.Entity.Custom.CatGenericoFiltro
import com.anahuac.rest.api.Entity.Custom.CatEstadoGFiltro
import com.anahuac.rest.api.Entity.Custom.CatEstadosCustom
import com.anahuac.rest.api.Entity.Custom.CatGestionEscolar
import com.anahuac.rest.api.Entity.Custom.CatNacionalidadCustomeFiltro
import com.anahuac.rest.api.Entity.Custom.CatParentescoCustom
import com.anahuac.rest.api.Entity.Custom.CatParentescoFiltro
import com.anahuac.rest.api.Entity.Custom.CatPropedeuticoCustom
import com.anahuac.rest.api.Entity.Custom.CatPropedeuticoFilterCustom
import com.anahuac.rest.api.Entity.Custom.CatPropedeuticoFinal
import com.anahuac.rest.api.Entity.Custom.CatPropedeuticoGralCustom
import com.anahuac.rest.api.Entity.Custom.CatPeriodoCustom
import com.anahuac.rest.api.Entity.Custom.CatPeriodoFinal
import com.anahuac.rest.api.Entity.Custom.CatSexoCustom
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper

import java.security.InvalidKeyException
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document as DocumentItext
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.itextpdf.text.Phrase

class CatalogosDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosDAO.class);
    Connection con;
    Statement stm;
    ResultSet rs;
    PreparedStatement pstm;
    
	public Boolean validarConexion() {
        Boolean retorno = false
        if (con == null || con.isClosed()) {
            con = new DBConnect().getConnection();
            retorno = true
        }
        return retorno
    }
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
		return retorno;
	}

    /************************DANIEL CERVANTES****************************/
    
    public Result getCatCampus(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "WHERE c.ISELIMINADO=false", orderby = "ORDER BY ", errorLog = "";
        String consulta = Statements.GET_CATCAMPUS
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);


            CatCampusCustomFiltro row = new CatCampusCustomFiltro();
            List < CatCampusCustomFiltro > rows = new ArrayList < CatCampusCustomFiltro > ();
            closeCon = validarConexion();
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                switch (filtro.get("columna")) {
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "URLIMAGEN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(URLIMAGEN) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "EMAIL":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(EMAIL) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA IMPORTACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHAIMPLEMENTACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "GRUPOBONITA":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(GRUPOBONITA) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ISELIMINADO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ISELIMINADO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ISENABLED":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("ISENABLED").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ORDEN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ORDEN) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "URLAUTORDATOS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(URLAUTORDATOS) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "URLAVISOPRIVACIDAD":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(URLAVISOPRIVACIDAD) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "URLDATOSVERIDICOS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(URLDATOSVERIDICOS) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO BANNER":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOBANNER) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CALLE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CALLE) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "COLONIA":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(COLONIA) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "NÚMERO INTERIOR":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(NUMEROINTERIOR) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "NÚMERO EXTERIOR":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(NUMEROEXTERIOR) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CÓDIGO POSTAL":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CODIGOPOSTAL) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "MUNICIPIO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(MUNICIPIO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PAÍS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PAIS) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTADO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ESTADO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break;
                case "DESCRIPCIÓN":
                    orderby += "descripción";
                    break;
                case "FECHA CREACIÓN":
                    orderby += "fechaCreacion";
                    break;
                case "FECHA IMPORTACIÓN":
                    orderby += "fechaImplementacion";
                    break;
                case "GRUPOBONITA":
                    orderby += "grupoBonita";
                    break;
                case "ID":
                    orderby += "id";
                    break;
                case "ISELIMINADO":
                    orderby += "isEliminado";
                    break;
                case "ISENABLED":
                    orderby += "isEnabled";
                    break;
                case "ORDEN":
                    orderby += "orden";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "URLAUTORDATOS":
                    orderby += "urlAutorDatos";
                    break;
                case "URLAVISOPRIVACIDAD":
                    orderby += "urlAvisoPrivacidad";
                    break;
                case "URLDATOSVERIDICOS":
                    orderby += "urlDatosVeridicos";
                    break;
                case "USUARIO BANNER":
                    orderby += "usuarioBanner";
                    break;
                case "ORDEN":
                    orderby += "ORDEN";
                    break;
                case "CALLE":
                    orderby += "CALLE";
                    break;
                case "COLONIA":
                    orderby += "COLONIA";
                    break;
                case "NÚMERO INTERIOR":
                    orderby += "NUMEROINTERIOR";
                    break;
                case "NÚMERO EXTERIOR":
                    orderby += "NUMEROEXTERIOR";
                    break;
                case "CÓDIGO POSTAL":
                    orderby += "CODIGOPOSTAL";
                    break;
                case "MUNICIPIO":
                    orderby += "MUNICIPIO";
                    break;
                case "URLIMAGEN":
                    orderby += "URLIMAGEN";
                    break;
                case "EMAIL":
                    orderby += "EMAIL";
                    break;
                case "PAÍS":
                    orderby += "PAIS";
                    break;
                case "ESTADO":
                    orderby += "ESTADO";
                    break;
                default:
                    orderby += "ORDEN"
                    break;
            }
            errorLog += ""
            orderby += " " + object.orientation;
            consulta = consulta.replace("[WHERE]", where);
            pstm = con.prepareStatement(consulta.replace("c.*, p.descripcion as pais, e.clave as cEstado, e.descripcion as dEstado", "COUNT(c.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")

            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            rs = pstm.executeQuery()
            errorLog += " " + consulta
            while (rs.next()) {
                row = new CatCampusCustomFiltro();
                row.setClave(rs.getString("clave"))
                row.setDescripcion(rs.getString("descripcion"));
                //row.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaCreacion")))
                row.setFechaCreacion(rs.getString("fechaCreacion"));
                //row.setFechaImplementacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaImplementacion")))
                row.setFechaImplementacion(rs.getString("fechaImplementacion"));
                row.setGrupoBonita(rs.getString("grupoBonita"));
                row.setId(rs.getString("id"));
                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setIsEnabled(rs.getBoolean("isEnabled"));
                row.setOrden(rs.getLong("orden"));
                row.setPersistenceId(rs.getLong("persistenceId"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setUrlAutorDatos(rs.getString("urlAutorDatos"));
                row.setUrlAvisoPrivacidad(rs.getString("urlAvisoPrivacidad"));
                row.setUrlDatosVeridicos(rs.getString("urlDatosVeridicos"));
                row.setUsuarioBanner(rs.getString("usuarioBanner"));
                row.setCalle(rs.getString("calle"));
                row.setColonia(rs.getString("colonia"));
                row.setNumeroExterior(rs.getString("numeroExterior"));
                row.setNumeroInterior(rs.getString("numeroInterior"));
                row.setCodigoPostal(rs.getString("codigoPostal"));
                row.setMunicipio(rs.getString("municipio"));
                row.setUrlImagen(rs.getString("urlImagen"));
                row.setEmail(rs.getString("email"))
                row.setPais_pid(rs.getString("pais_pid"))
                row.setEstado_pid(rs.getString("estado_pid"))
                errorLog += "pais"
                try {
                    row.setPais(new CatPais())
                    row.getPais().setDescripcion(rs.getString("pais"))
                    row.getPais().setPersistenceId(rs.getLong("PAIS_PID"))
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += e.getMessage()
                }
                try {
                    row.setEstados(new CatEstadosCustom())
                    row.getEstados().setDescripcion(rs.getString("destado"))
                    row.getEstados().setClave(rs.getString("cestado"))
                    row.getEstados().setPersistenceId(rs.getLong("estado_pid"))
                    //row.getEstado().setPais(rs.getString("pEstado"))
                    //row.getEstado().setCaseId(rs.getString("ciestado"))
                    //row.getEstado().setIsEliminado(rs.getBoolean("eestado"))
                    //row.getEstado().setOrden(rs.getInt("oestado"))
                    //row.getEstado().setPersistenceVersion(rs.getLong("pvestado"))
                    //row.getEstado().setPersistenceVersion_string(rs.getString("pvestado"))
                    //row.getEstado().setUsuarioCreacion(rs.getString("ucestado"))
                    //row.getEstado().setPersistenceId_string(rs.getString("piestado"))
                    //row.getEstado().setFechaCreacion(rs.getString("fcEstado"));
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += e.getMessage()
                }
                rows.add(row)
            }
            resultado.setSuccess(true)

            resultado.setData(rows)
            
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            resultado.setError_info(consulta)
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatPais(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String errorlog = "";
        String where = "WHERE ISELIMINADO=false", orderby = "ORDER BY "
        String consulta = Statements.GET_CATPAIS;
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            CatPaisCustomFiltro row = new CatPaisCustomFiltro();
            List < CatPaisCustomFiltro > rows = new ArrayList < CatPaisCustomFiltro > ();
            errorlog += " Antes de validar conexion "
            closeCon = validarConexion();
            errorlog += " Despues de validar conexion "
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                errorlog += " Filtros "
                switch (filtro.get("columna")) {
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ISELIMINADO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ISELIMINADO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ORDEN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ORDEN) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            errorlog += " Order by "
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break;
                case "DESCRIPCIÓN":
                    orderby += "descripcion";
                    break;
                case "FECHA CREACIÓN":
                    orderby += "fechaCreacion";
                    break;
                case "ISELIMINADO":
                    orderby += "isEliminado";
                    break;
                case "ORDEN":
                    orderby += "orden";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "USUARIO CREACIÓN":
                    orderby += "usuarioCreacion";
                    break;
                case "ORDEN":
                    orderby += "ORDEN";
                    break;
                default:
                    orderby += "persistenceid"
                    break;
            }
            orderby += " " + object.orientation;
            consulta = consulta.replace("[WHERE]", where);
            errorlog += " Consulta " + consulta
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")

            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            rs = pstm.executeQuery()

            while (rs.next()) {
                row = new CatPaisCustomFiltro();
                row.setClave(rs.getString("clave"))
                row.setDescripcion(rs.getString("descripcion"));
                //row.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaCreacion")))
                row.setFechaCreacion(rs.getString("fechaCreacion"));
                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setOrden(rs.getLong("orden"));
                row.setPersistenceId(rs.getLong("persistenceId"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setUsuarioCreacion(rs.getString("usuarioCreacion"));
                rows.add(row)
            }
            resultado.setSuccess(true)

            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            errorlog += " ERROR " + e.getMessage();
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatEstados(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "WHERE ISELIMINADO=false", orderby = "ORDER BY "
        String consulta = Statements.GET_CATESTADOS;
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);


            CatEstadoCustomFiltro row = new CatEstadoCustomFiltro();
            List < CatEstadoCustomFiltro > rows = new ArrayList < CatEstadoCustomFiltro > ();
            closeCon = validarConexion();
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                switch (filtro.get("columna")) {
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ISELIMINADO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ISELIMINADO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ORDEN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ORDEN) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PAÍS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PAIS) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break;
                case "DESCRIPCIÓN":
                    orderby += "descripcion";
                    break;
                case "FECHA CREACIÓN":
                    orderby += "fechaCreacion";
                    break;
                case "ISELIMINADO":
                    orderby += "isEliminado";
                    break;
                case "ORDEN":
                    orderby += "orden";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "USUARIO CREACIÓN":
                    orderby += "usuarioCreacion";
                    break;
                case "PAÍS":
                    orderby += "pais";
                    break;
                case "ORDEN":
                    orderby += "orden";
                    break;
                default:
                    orderby += "persistenceid"
                    break;
            }
            orderby += " " + object.orientation;
            consulta = consulta.replace("[WHERE]", where);
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")

            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            rs = pstm.executeQuery()

            while (rs.next()) {
                row = new CatEstadoCustomFiltro();
                row.setClave(rs.getString("clave"));
                row.setOrden(rs.getLong("orden"));
                row.setPais(rs.getString("pais"));
                row.setDescripcion(rs.getString("descripcion"));
                row.setFechaCreacion(rs.getString("fechaCreacion"));
                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setCaseId(rs.getString("caseId"));
                row.setPersistenceId(rs.getLong("persistenceId"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setUsuarioCreacion(rs.getString("usuarioCreacion"));
                rows.add(row)
            }
            resultado.setSuccess(true)

            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError_info(consulta)
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatEstados(String pais) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "WHERE ISELIMINADO=false", orderby = "ORDER BY "
        String consulta = Statements.GET_CATESTADOS;
        try {

            CatEstadosCustom row = new CatEstadosCustom();
            List < CatEstadosCustom > rows = new ArrayList < CatEstadosCustom > ();
            closeCon = validarConexion();

            consulta = consulta.replace("[WHERE]", " WHERE pais = '" + pais + "'");
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", "")
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")

            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, 40)
            pstm.setInt(2, 0)


            rs = pstm.executeQuery()

            while (rs.next()) {
                row = new CatEstadosCustom();
                row.setClave(rs.getString("clave"));
                row.setDescripcion(rs.getString("descripcion"));
                row.setPersistenceId(rs.getLong("persistenceId"));
                rows.add(row)
            }
            resultado.setSuccess(true)

            resultado.setData(rows)
            resultado.setError_info(consulta)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatBachillerato(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String error = "";
        String where = "WHERE ISELIMINADO=false", orderby = "ORDER BY "
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            String consulta = Statements.GET_CATBACHILLERATO;
            CatBachilleratoCustomFiltro row = new CatBachilleratoCustomFiltro();
            List < CatBachilleratoCustomFiltro > rows = new ArrayList < CatBachilleratoCustomFiltro > ();
            closeCon = validarConexion();
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                switch (filtro.get("columna")) {
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
					case "ID BANNER":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(id) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
                    case "DESCRIPCIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA DE CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " TO_CHAR(TO_TIMESTAMP(FECHACREACION, 'YYYY-MM-DD HH24:MI:SS'), 'DD-MM-YYYY HH24:MI:SS') ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
					case "FECHA DE IMPORTACIÓN":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += "  TO_CHAR(TO_TIMESTAMP(fechaimportacion, 'YYYY-MM-DD HH24:MI:SS'), 'DD-MM-YYYY HH24:MI:SS') ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
                    case "FECHA DE IMPLEMENTACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += "  TO_CHAR(TO_TIMESTAMP(FECHAIMPLEMENTACION, 'YYYY-MM-DD HH24:MI:SS'), 'DD-MM-YYYY HH24:MI:SS') ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ISELIMINADO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ISELIMINADO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CIUDAD":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CIUDAD) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PAÍS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PAIS) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTADO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ESTADO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
					case "MUNICIPIO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        //where += " LOWER(countyCode) ";
						where += " LOWER(municipio) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
					case "CÓDIGO POSTAL":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(postalCode) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
					case "CALLE LÍNEA 1":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(streetLine1) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
					case "CALLE LÍNEA 3":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(streetLine3) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO DE CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOBANNER) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
					case "CLAVE DEL PAÍS":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(nationCode) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CLAVE DEL ESTADO":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(stateCode) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
                    case "PERTENECE A LA RED":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        if (filtro.get("valor").equals("Si") || filtro.get("valor").equals("SI") || filtro.get("valor").equals("si") || filtro.get("valor").toString().toLowerCase().equals("sí") || filtro.get("valor").toString().toLowerCase().equals("true")) {
                            where += " PERTENECERED = true "
                        } else if (filtro.get("valor").equals("No") || filtro.get("valor").equals("NO") || filtro.get("valor").equals("no") || filtro.get("valor").toString().toLowerCase().equals("false")) {
                            where += " PERTENECERED = false "
                        }
                        /*where += " LOWER(PERTENECERED) ";
                        if (filtro.get("operador").equals("Igual a")) {
                        	where += "=LOWER('[valor]')"
                        } else {
                        	where += "LIKE LOWER('%[valor]%')"
                        }*/
                        //where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break;
                case "PERTENECE A LA RED":
                    orderby += "PERTENECERED";
                    break;
                case "DESCRIPCIÓN":
                    orderby += "descripcion";
                    break;
                case "FECHA CREACIÓN":
                    orderby += "fechaCreacion";
                    break;
                case "FECHA IMPLEMENTACIÓN":
                    orderby += "fechaImplementacion";
                    break;
                case "ISELIMINADO":
                    orderby += "isEliminado";
                    break;
                case "ORDEN":
                    orderby += "orden";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "USUARIO CREACIÓN":
                    orderby += "usuarioBanner";
                    break;
                case "PAÍS":
                    orderby += "pais";
                    break;
				case "MUNICIPIO":
                    orderby += "municipio";
                    break;
                case "ESTADO":
                    orderby += "estado";
                    break;
                case "CIUDAD":
                    orderby += "ciudad";
                    break;
                default:
                    orderby += "descripcion"
                    break;
            }
            orderby += " " + object.orientation;
            consulta = consulta.replace("[WHERE]", where);
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            error += consulta
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            rs = pstm.executeQuery()

            while (rs.next()) {
                row = new CatBachilleratoCustomFiltro();
                row.setCiudad(rs.getString("ciudad"));
                row.setClave(rs.getString("clave"));
                row.setDescripcion(rs.getString("descripcion"));
                row.setEstado(rs.getString("estado"));
                row.setFechaCreacion(rs.getString("fechaCreacion"));
                row.setFechaImportacion(rs.getString("fechaImportacion"));
                row.setPais(rs.getString("pais"));
                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setIsEnabled(rs.getBoolean("isEnabled"));
                row.setUsuarioBanner(rs.getString("usuarioBanner"));
                row.setPersistenceId(rs.getLong("persistenceId"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setPerteneceRed(rs.getBoolean("perteneceRed"));
                row.setId(rs.getString("id"));
				
				row.setUsuarioCreacion(rs.getString("usuariobanner"));
				
				row.setIdDireccion(rs.getString("idDireccion"));
				row.setCountyCode(rs.getString("countyCode"));
				row.setNationCode(rs.getString("nationCode"));
				row.setStateCode(rs.getString("stateCode"));
				row.setStreetLine1(rs.getString("streetLine1"));
				row.setStreetLine2(rs.getString("streetLine2"));
				row.setStreetLine3(rs.getString("streetLine3"));
				row.setTypeInd(rs.getString("typeInd"));
				row.setPostalCode(rs.getString("postalCode"));
				row.setMunicipio(rs.getString("municipio"))
				
                rows.add(row)
            }
            resultado.setSuccess(true)
            resultado.setError_info(error)
            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatFiltradoCalalogosAdMisiones(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", orderby = "ORDER BY ", errorLog = ""
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            String consulta = Statements.GET_CATFILTRADOCATALOGOSAUTODESCRIPCION
            CatFiltradoCatalogosAutoDescripcion row = new CatFiltradoCatalogosAutoDescripcion();
            List < CatFiltradoCatalogosAutoDescripcion > rows = new ArrayList < CatFiltradoCatalogosAutoDescripcion > ();
            closeCon = validarConexion();
            where += " WHERE isEliminado = false";
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {

                switch (filtro.get("columna")) {
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ORDEN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(orden) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHACREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIOCREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            switch (object.orderby) {
                case "CLAVE":

                    orderby += "clave::integer"
                    //orderby+="clave";

                    break;
                case "ORDEN":

                    orderby += "orden::integer"
                    //orderby+="orden";

                    break;
                case "DESCRIPCION":
                    orderby += "descripcion";
                    break;
                case "FECHACREACION":
                    orderby += "fechaCreacion";
                    break;
                case "ISELIMINADO":
                    orderby += "isEliminado";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "USUARIOCREACION":
                    orderby += "usuarioCreacion";
                    break;
                default:
                    orderby += "persistenceid"
                    break;
            }
            orderby += " " + object.orientation;
            consulta = consulta.replace("[WHERE]", where);
            consulta = consulta.replace("[CATALOGOAD]", object.catalogo)
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorLog += "consulta"
            errorLog += consulta
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)
            rs = pstm.executeQuery()
            errorLog += " entro set";
            while (rs.next()) {
                row = new CatFiltradoCatalogosAutoDescripcion();
                row.setClave(rs.getString("clave"))

                row.setDescripcion(rs.getString("descripcion"));
                //row.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaCreacion")));
                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setPersistenceId(rs.getLong("PERSISTENCEID"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                try {
                    row.setUsuarioCreacion(rs.getString("usuariocreacion"));
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += e.getMessage()
                }
                try {
                    row.setOrden(rs.getLong("orden"))
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += e.getMessage()
                }
                try {
                    row.setUsuarioBanner(rs.getString("usuarioBanner"));
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += e.getMessage()
                }
                try {
                    row.setFechaCreacion(rs.getString("fechaCreacion"));
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += e.getMessage()
                }
                try {
                    row.setFechaImplementacion(rs.getString("fechaImplementacion"));
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += e.getMessage()
                }
                try {
                    row.setFechaImplementacion(rs.getString("fechaImportacion"));
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += e.getMessage()
                }
                try {
                    row.setIsEnabled(rs.getString("isEnabled"));
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += e.getMessage()
                }
                rows.add(row)
            }
            errorLog += " salio set";
            resultado.setSuccess(true)
            resultado.setError(errorLog)
            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }
    /***********************DANIEL CERVANTES FIN************************/
    /***********************JUAN ESQUER******************************/

    public Result getAzureConfig() {
        Result result = new Result();
        String defaultEndpointsProtocol = ""
        String accountName = ""
        String accountKey = ""
        List < AzureConfig > data = new ArrayList < AzureConfig > ();
        AzureConfig ac = new AzureConfig();
        Boolean closeCon = false;
        try {


            closeCon = validarConexion();

            pstm = con.prepareStatement(AzureConfig.CONFIGURACIONES)
            rs = pstm.executeQuery()
            while (rs.next()) {
                switch (rs.getString("clave")) {
                    case "AzureAccountName":
                        ac.setAzureAccountName(rs.getString("valor"))
                        break;
                    case "AzureAccountKey":
                        ac.setAzureAccountKey(rs.getString("valor"))
                        break;
                    case "AzureDefaultEndpointsProtocol":
                        ac.setAzureDefaultEndpointsProtocol(rs.getString("valor"))
                        break;
                    case "BannerToken":
                        ac.setBannerToken(rs.getString("valor"))
                        break;
					case "filterPassword":
						ac.setAdminPassword(rs.getString("valor"))
						break;
					case "bannerMatchPerson":
					ac.setBannerMatchPerson(rs.getString("valor"))
					break;
		}
            }
            data.add(ac)
            result.setSuccess(true);
            result.setData(data)
        } catch (Exception exception) {
        	LOGGER.error "[ERROR] " + exception.getMessage();
            result.setSuccess(false)
            result.setError(exception.getMessage())
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return result;
    }

    public Result insertAzureConfig(AzureConfig ac) {
        Result result = new Result();
        String defaultEndpointsProtocol = ""
        String accountName = ""
        String accountKey = ""
        List < AzureConfig > data = new ArrayList < AzureConfig > ();
        Boolean closeCon = false;
        try {


            closeCon = validarConexion();

            pstm = con.prepareStatement(AzureConfig.GET_CONFIGURACIONES_CLAVE)
            pstm.setString(1, "AzureAccountName")
            rs = pstm.executeQuery()
            if (rs.next()) {
                pstm = con.prepareStatement(AzureConfig.UPDATE_CONFIGURACIONES)
                pstm.setString(1, ac.getAzureAccountName())
                pstm.setString(2, "AzureAccountName")
                pstm.executeUpdate()
            } else {
                pstm = con.prepareStatement(AzureConfig.INSERT_CONFIGURACIONES)
                pstm.setString(1, "AzureAccountName")
                pstm.setString(2, ac.getAzureAccountName())
                pstm.setString(3, "Nombre de la cuenta de Azure")
                pstm.executeUpdate()
            }

            pstm = con.prepareStatement(AzureConfig.GET_CONFIGURACIONES_CLAVE)
            pstm.setString(1, "AzureAccountKey")
            rs = pstm.executeQuery()
            if (rs.next()) {
                pstm = con.prepareStatement(AzureConfig.UPDATE_CONFIGURACIONES)
                pstm.setString(1, ac.getAzureAccountKey())
                pstm.setString(2, "AzureAccountKey")
                pstm.executeUpdate()
            } else {
                pstm = con.prepareStatement(AzureConfig.INSERT_CONFIGURACIONES)
                pstm.setString(1, "AzureAccountKey")
                pstm.setString(2, ac.getAzureAccountKey())
                pstm.setString(3, "Directiva de acceso de Azure")
                pstm.executeUpdate()
            }

            pstm = con.prepareStatement(AzureConfig.GET_CONFIGURACIONES_CLAVE)
            pstm.setString(1, "AzureDefaultEndpointsProtocol")
            rs = pstm.executeQuery()
            if (rs.next()) {
                pstm = con.prepareStatement(AzureConfig.UPDATE_CONFIGURACIONES)
                pstm.setString(1, ac.getAzureDefaultEndpointsProtocol())
                pstm.setString(2, "AzureDefaultEndpointsProtocol")
                pstm.executeUpdate()
            } else {
                pstm = con.prepareStatement(AzureConfig.INSERT_CONFIGURACIONES)
                pstm.setString(1, "AzureDefaultEndpointsProtocol")
                pstm.setString(2, ac.getAzureDefaultEndpointsProtocol())
                pstm.setString(3, "Azure Storage admite HTTP y HTTPS en una cadena de conexión, se recomienda encarecidamente HTTPS")
                pstm.executeUpdate()
            }

            pstm = con.prepareStatement(AzureConfig.GET_CONFIGURACIONES_CLAVE)
            pstm.setString(1, "BannerToken")
            rs = pstm.executeQuery()
            if (rs.next()) {
                pstm = con.prepareStatement(AzureConfig.UPDATE_CONFIGURACIONES)
                pstm.setString(1, ac.getBannerToken())
                pstm.setString(2, "BannerToken")
                pstm.executeUpdate()
            } else {
                pstm = con.prepareStatement(AzureConfig.INSERT_CONFIGURACIONES)
                pstm.setString(1, "BannerToken")
                pstm.setString(2, ac.getBannerToken())
                pstm.setString(3, "Token para hacer peticiones a banner")
                pstm.executeUpdate()
            }
			
			pstm = con.prepareStatement(AzureConfig.GET_CONFIGURACIONES_CLAVE)
			pstm.setString(1, "filterPassword")
			rs = pstm.executeQuery()
			if (rs.next()) {
				pstm = con.prepareStatement(AzureConfig.UPDATE_CONFIGURACIONES)
				pstm.setString(1, ac.getAdminPassword())
				pstm.setString(2, "filterPassword")
				pstm.executeUpdate()
			} else {
				pstm = con.prepareStatement(AzureConfig.INSERT_CONFIGURACIONES)
				pstm.setString(1, "filterPassword")
				pstm.setString(2, ac.getAdminPassword())
				pstm.setString(3, "Contraseña que se utiliza para poder iniciar sesión y ejecutar una tarea desde el filter llamado por el webhook de conekta")
				pstm.executeUpdate()
			}
			pstm = con.prepareStatement(AzureConfig.GET_CONFIGURACIONES_CLAVE)
			pstm.setString(1, "bannerMatchPerson")
			rs = pstm.executeQuery()
			if (rs.next()) {
				pstm = con.prepareStatement(AzureConfig.UPDATE_CONFIGURACIONES)
				pstm.setString(1, ac.getBannerMatchPerson())
				pstm.setString(2, "bannerMatchPerson")
				pstm.executeUpdate()
			} else {
				pstm = con.prepareStatement(AzureConfig.INSERT_CONFIGURACIONES)
				pstm.setString(1, "bannerMatchPerson")
				pstm.setString(2, ac.getBannerMatchPerson())
				pstm.setString(3, "APIKEY de banner para la autorización de Match person")
				pstm.executeUpdate()
			}
			
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
			closeCon=validarConexionBonita()
			pstm = con.prepareStatement("UPDATE proc_parameter set value =? where name='password'")
			pstm.setString(1,ac.getAdminPassword())
			pstm.executeUpdate()
			

            data.add(ac)
            result.setSuccess(true);
            result.setData(data)
        } catch (Exception exception) {
        	LOGGER.error "[ERROR] " + exception.getMessage();
            result.setSuccess(false)
            result.setError(exception.getMessage())
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return result;
    }

    public Result getCatTitulo(String jsonData) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "WHERE ISELIMINADO=false", orderby = "ORDER BY ", errorlog = ""
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            CatTitulo row = new CatTitulo()
            List < CatTitulo > rows = new ArrayList < CatTitulo > ();
            closeCon = validarConexion();


            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                switch (filtro.get("columna")) {
                    case "ORDEN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ORDEN) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CLAVE) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            orderby += object.orderby
            if (orderby.equals("ORDER BY ")) {
                orderby += "PERSISTENCEID";
            }
            orderby += " " + object.orientation;
            String consulta = Statements.GET_CATTITULO;
            consulta = consulta.replace("[WHERE]", where);
            errorlog += "consulta:"
            errorlog += consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "")
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorlog += "consulta:"
            errorlog += consulta
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)
            rs = pstm.executeQuery()
            while (rs.next()) {
                row = new CatTitulo()
                row.setClave(rs.getString("clave"))
                row.setDescripcion(rs.getString("descripcion"))
                row.setIsEliminado(rs.getBoolean("isEliminado"))
                row.setOrden(rs.getInt("orden"))
                row.setPersistenceId(rs.getLong("persistenceId"))
                row.setPersistenceVersion(rs.getLong("persistenceVersion"))
                row.setUsuarioCreacion(rs.getString("usuarioCreacion"))
                rows.add(row)
            }
            resultado.setSuccess(true)

            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + Exception.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getEstadoCivil(String jsonData) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "WHERE ISELIMINADO=false", orderby = "ORDER BY ", errorlog = ""
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            CatEstadoCivilCustom row = new CatEstadoCivilCustom()
            List < CatEstadoCivilCustom > rows = new ArrayList < CatEstadoCivilCustom > ();
            closeCon = validarConexion();


            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                switch (filtro.get("columna")) {
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CLAVE) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO BANNER":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(usuariobanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA IMPORTACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHAIMPORTACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break
                case "DESCRIPCIÓN":
                    orderby += "descripcion";
                    break
                case "USUARIO BANNER":
                    orderby += "usuariobanner";
                    break
                case "FECHA CREACIÓN":
                    orderby += "fechaCreacion";
                    break
                case "FECHA IMPORTACIÓN":
                    orderby += "fechaImportacion";
                    break
                default:
                    orderby += "PERSISTENCEID";
                    break;
            }
            orderby += " " + object.orientation;
            String consulta = Statements.GET_CATESTADOCIVIL;
            consulta = consulta.replace("[WHERE]", where);
            errorlog += "consulta:"
            errorlog += consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "")
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorlog += "consulta:"
            errorlog += consulta
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)
            rs = pstm.executeQuery()
            while (rs.next()) {
                row = new CatEstadoCivilCustom()
                row.setClave(rs.getString("clave"))
                row.setDescripcion(rs.getString("descripcion"))
                row.setIsEliminado(rs.getBoolean("isEliminado"))
                try {
                    row.setFechaCreacion(rs.getString("fechaCreacion"))
                    row.setFechaImportacion(rs.getString("fechaImportacion"))
                    //row.fechaCreacion(new java.util.Date(rs.getDate("fechacreacion")))
                    //row.fechaImportacion(new java.util.Date(rs.getDate("fechaimportacion")))
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorlog += e.getMessage()
                }

                row.setPersistenceId(rs.getLong("persistenceId"))
                row.setPersistenceVersion(rs.getLong("persistenceVersion"))
                row.setusuarioBanner(rs.getString("usuariobanner"))
                rows.add(row)
            }
            resultado.setSuccess(true)

            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }
	public Result getRespuestasINVP(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "WHERE ISELIMINADO=false", orderby = "ORDER BY ", errorlog = ""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatEstadoCivilCustom row = new CatEstadoCivilCustom()
			List < CatEstadoCivilCustom > rows = new ArrayList < CatEstadoCivilCustom > ();
			closeCon = validarConexion();


			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				switch (filtro.get("columna")) {
					case "Clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(CLAVE) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Escala":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Pregunta":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(usuariobanner) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Respuesta":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(FECHACREACION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Puntuación":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " puntuacion ";
						
						where += "=[valor]"
						
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "Genero":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(genero) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}
			switch (object.orderby) {
				case "CLAVE":
					orderby += "clave";
					break
				case "Escala":
					orderby += "escala";
					break
				case "Pregunta":
					orderby += "pregunta";
					break
				case "Puntuación":
					orderby += "puntuacion";
					break
				case "Genero":
					orderby += "genero";
					break
				default:
					orderby += "PERSISTENCEID";
					break;
			}
			orderby += " " + object.orientation;
			String consulta = "SELECT * FROM catrespuestasinvp"
			consulta = consulta.replace("[WHERE]", where);
			errorlog += "consulta:"
			errorlog += consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "")
			pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog += "consulta:"
			errorlog += consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)
			rs = pstm.executeQuery()
			while (rs.next()) {
				row = new CatEstadoCivilCustom()
				row.setClave(rs.getString("clave"))
				row.setDescripcion(rs.getString("descripcion"))
				row.setIsEliminado(rs.getBoolean("isEliminado"))
				try {
					row.setFechaCreacion(rs.getString("fechaCreacion"))
					row.setFechaImportacion(rs.getString("fechaImportacion"))
					//row.fechaCreacion(new java.util.Date(rs.getDate("fechacreacion")))
					//row.fechaImportacion(new java.util.Date(rs.getDate("fechaimportacion")))
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					errorlog += e.getMessage()
				}

				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				row.setusuarioBanner(rs.getString("usuariobanner"))
				rows.add(row)
			}
			resultado.setSuccess(true)

			resultado.setData(rows)

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
  
	/**********************JUAN ESQUER FIN******************************/
    /***********************JESUS OSUNA********************************/

    public Result getCatEscolaridad(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", orderby = "ORDER BY ", errorLog = ""
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            //assert object instanceof List;

            String consulta = Statements.GET_CATESCOLARIDAD
            CatEscolaridadCustom row = new CatEscolaridadCustom();
            List < CatEscolaridadCustom > rows = new ArrayList < CatEscolaridadCustom > ();
            closeCon = validarConexion();
            errorLog += "informacion 1"
            where += " WHERE isEliminado = false";
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {

                switch (filtro.get("columna")) {
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHACREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIOCREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break;
                case "DESCRIPCION":
                    orderby += "descripcion";
                    break;
                case "FECHACREACION":
                    orderby += "fechaCreacion";
                    break;
                case "ISELIMINADO":
                    orderby += "isEliminado";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "USUARIOCREACION":
                    orderby += "usuarioCreacion";
                    break;
                default:
                    orderby += "persistenceid"
                    break;
            }
            errorLog += "orderby"
            orderby += " " + object.orientation;
            consulta = consulta.replace("[WHERE]", where);
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorLog += "consulta"
            errorLog += consulta
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            errorLog += "fecha=="

            rs = pstm.executeQuery()
            while (rs.next()) {
                errorLog += "entro"
                Date creacion = sdf.parse(rs.getString("fechacreacion"))
                LocalDateTime localDateTime = creacion.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                row = new CatEscolaridadCustom();
                row.setClave(rs.getString("clave"))
                row.setDescripcion(rs.getString("descripcion"));
                row.setFechaCreacion(rs.getString("fechacreacion"));
                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setPersistenceId(rs.getLong("PERSISTENCEID"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setUsuarioCreacion(rs.getString("usuariocreacion"));

                rows.add(row)
            }
            errorLog += "llenado"

            resultado.setSuccess(true)
            resultado.setError(errorLog)
            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatSexo(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", orderby = "ORDER BY ", errorLog = ""
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            //assert object instanceof List;

            String consulta = Statements.GET_CATSEXO
            CatSexoCustom row = new CatSexoCustom();
            List < CatSexoCustom > rows = new ArrayList < CatSexoCustom > ();
            closeCon = validarConexion();
            where += " WHERE isEliminado = false";
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {

                switch (filtro.get("columna")) {
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHACREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIOCREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break;
                case "DESCRIPCION":
                    orderby += "descripcion";
                    break;
                case "FECHACREACION":
                    orderby += "fechaCreacion";
                    break;
                case "ISELIMINADO":
                    orderby += "isEliminado";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "USUARIOCREACION":
                    orderby += "usuarioCreacion";
                    break;
                default:
                    orderby += "persistenceid"
                    break;
            }
            errorLog += "orderby"
            orderby += " " + object.orientation;
            consulta = consulta.replace("[WHERE]", where);
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorLog += "consulta"
            errorLog += consulta
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            errorLog += "fecha=="

            rs = pstm.executeQuery()
            while (rs.next()) {

                row = new CatSexoCustom();
                row.setClave(rs.getString("clave"))
                row.setDescripcion(rs.getString("descripcion"));
                row.setFechaCreacion(rs.getString("fechacreacion"));
                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setPersistenceId(rs.getLong("PERSISTENCEID"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setUsuarioCreacion(rs.getString("usuariocreacion"));

                rows.add(row)
            }

            resultado.setSuccess(true)
            resultado.setError(errorLog)
            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatParentesco(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", orderby = "ORDER BY ", errorLog = ""
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            //assert object instanceof List;
            String consulta = Statements.GET_CATGENERICO
            CatGenericoFiltro row = new CatGenericoFiltro();
            List < CatGenericoFiltro > rows = new ArrayList < CatGenericoFiltro > ();
            closeCon = validarConexion();
            where += " WHERE isEliminado = false";
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {

                switch (filtro.get("columna")) {
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHACREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIOCREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break;
                case "DESCRIPCION":
                    orderby += "descripcion";
                    break;
                case "FECHACREACION":
                    orderby += "fechaCreacion";
                    break;
                case "ISELIMINADO":
                    orderby += "isEliminado";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "USUARIOCREACION":
                    orderby += "usuarioCreacion";
                    break;
                default:
                    orderby += "persistenceid"
                    break;
            }
            errorLog += "orderby"
            orderby += " " + object.orientation;
            consulta = consulta.replace("[WHERE]", where);
            consulta = consulta.replace("[CATALOGO]", object.catalogo)
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorLog += "consulta"
            errorLog += consulta
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            errorLog += " fecha=="

            rs = pstm.executeQuery()
            while (rs.next()) {

                row = new CatGenericoFiltro();
                row.setClave(rs.getString("clave"))
                row.setDescripcion(rs.getString("descripcion"));
                row.setFechaCreacion(rs.getString("fechacreacion"));
                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setPersistenceId(rs.getLong("PERSISTENCEID"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setUsuarioCreacion(rs.getString("usuariocreacion"));

                rows.add(row)
            }
            errorLog += " paso el listado";
            resultado.setSuccess(true)
            resultado.setError(errorLog)
            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatDescuentos(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", orderby = "ORDER BY ", errorLog = "", bachillerato = "", campus = ""

        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();

        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            //assert object instanceof List;
            objGrupoCampus = new HashMap < String, String > ();
            objGrupoCampus.put("descripcion", "Anáhuac Cancún");
            objGrupoCampus.put("valor", "CAMPUS-CANCUN");
            lstGrupoCampus.add(objGrupoCampus);

            objGrupoCampus = new HashMap < String, String > ();
            objGrupoCampus.put("descripcion", "Anáhuac Mayab");
            objGrupoCampus.put("valor", "CAMPUS-MAYAB");
            lstGrupoCampus.add(objGrupoCampus);

            objGrupoCampus = new HashMap < String, String > ();
            objGrupoCampus.put("descripcion", "Anáhuac México Norte");
            objGrupoCampus.put("valor", "CAMPUS-MNORTE");
            lstGrupoCampus.add(objGrupoCampus);

            objGrupoCampus = new HashMap < String, String > ();
            objGrupoCampus.put("descripcion", "Anáhuac México Sur");
            objGrupoCampus.put("valor", "CAMPUS-MSUR");
            lstGrupoCampus.add(objGrupoCampus);

            objGrupoCampus = new HashMap < String, String > ();
            objGrupoCampus.put("descripcion", "Anáhuac Oaxaca");
            objGrupoCampus.put("valor", "CAMPUS-OAXACA");
            lstGrupoCampus.add(objGrupoCampus);

            objGrupoCampus = new HashMap < String, String > ();
            objGrupoCampus.put("descripcion", "Anáhuac Puebla");
            objGrupoCampus.put("valor", "CAMPUS-PUEBLA");
            lstGrupoCampus.add(objGrupoCampus);

            objGrupoCampus = new HashMap < String, String > ();
            objGrupoCampus.put("descripcion", "Anáhuac Querétaro");
            objGrupoCampus.put("valor", "CAMPUS-QUERETARO");
            lstGrupoCampus.add(objGrupoCampus);

            objGrupoCampus = new HashMap < String, String > ();
            objGrupoCampus.put("descripcion", "Anáhuac Xalapa");
            objGrupoCampus.put("valor", "CAMPUS-XALAPA");
            lstGrupoCampus.add(objGrupoCampus);

            objGrupoCampus = new HashMap < String, String > ();
            objGrupoCampus.put("descripcion", "Anáhuac Querétaro");
            objGrupoCampus.put("valor", "CAMPUS-QUERETARO");
            lstGrupoCampus.add(objGrupoCampus);

            /*objGrupoCampus = new HashMap<String, String>();
            objGrupoCampus.put("descripcion","Juan Pablo II");
            objGrupoCampus.put("valor","CAMPUS-JP2");
            lstGrupoCampus.add(objGrupoCampus);
            */
            objGrupoCampus = new HashMap < String, String > ();
            objGrupoCampus.put("descripcion", "Juan Pablo II México");
            objGrupoCampus.put("valor", "CAMPUS-JP2");
            lstGrupoCampus.add(objGrupoCampus);


            objGrupoCampus = new HashMap < String, String > ();
            objGrupoCampus.put("descripcion", "Juan Pablo II Guadalajara");
            objGrupoCampus.put("valor", "CAMPUS-JP2G");
            lstGrupoCampus.add(objGrupoCampus);


            objGrupoCampus = new HashMap < String, String > ();
            objGrupoCampus.put("descripcion", "Anáhuac Cordoba");
            objGrupoCampus.put("valor", "CAMPUS-CORDOBA");
            lstGrupoCampus.add(objGrupoCampus);

            userLogged = context.getApiSession().getUserId();

            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (Map < String, String > rowGrupo: lstGrupoCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.get("valor"))) {
                        lstGrupo.add(rowGrupo.get("valor"));
                        break;
                    }
                }
            }

            where += " WHERE c.isEliminado = false";
            campus += " AND ("
            for (Integer i = 0; i < lstGrupo.size(); i++) {
                String campusMiembro = lstGrupo.get(i);
                campus += "c.campus='" + campusMiembro + "'"
                if (i == (lstGrupo.size() - 1)) {
                    campus += ") "
                } else {
                    campus += " OR "
                }
            }

            String consulta = Statements.GET_CATDESCUENTOS
            CatDescuentosCustom row = new CatDescuentosCustom();
            List < CatDescuentosCustom > rows = new ArrayList < CatDescuentosCustom > ();
            closeCon = validarConexion();

            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {

                switch (filtro.get("columna")) {
                    case "TIPO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(TIPO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CONVENI O DESCUENTO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CONVENIODESCUENTO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "INICIO VIGENCIA":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(INICIOVIGENCIA) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "FIN VIGENCIA":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FINVIGENCIA) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIOCREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "BACHILLERATO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(BACHILLERATO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "CAMPANA":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CAMPANA) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;


                    case "CIUDAD":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CIUDAD) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;


                    case "DESCUENTO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCUENTO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "CAMPUS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(a.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                }
            }
            switch (object.orderby) {
                case "TIPO":
                    orderby += "TIPO";
                    break;
                case "DESCUENTO":
                    orderby += "fechaCreacion";
                    break;
                case "ISELIMINADO":
                    orderby += "isEliminado";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "USUARIOCREACION":
                    orderby += "usuarioCreacion";
                    break;
                case "INICIOVIGENCIA":
                    orderby += "INICIOVIGENCIA";
                    break;
                case "FINVIGENCIA":
                    orderby += "FINVIGENCIA";
                    break;
                default:
                    orderby += "c.persistenceid"
                    break;
            }
            errorLog += "orderby"
            orderby += " " + object.orientation;
            consulta = consulta.replace("[BACHILLERATO]", bachillerato)
            where += " " + campus + " " + bachillerato
            consulta = consulta.replace("[WHERE]", where);
            errorLog += " " + consulta
            pstm = con.prepareStatement(consulta.replace("c.*, b.descripcion as bachilleratos", "COUNT(c.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorLog += "consulta"
            errorLog += consulta
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            errorLog += "fecha=="

            rs = pstm.executeQuery()
            while (rs.next()) {

                row = new CatDescuentosCustom();
                row.setTipo(rs.getString("TIPO"))
                row.setDescuento(rs.getInt("descuento"));
                row.setInicioVigencia(rs.getString("iniciovigencia"));
                row.setFinVigencia(rs.getString("finvigencia"));
                row.setCampana(rs.getString("campana"));
                row.setCiudad(rs.getString("ciudad"));
                try {
                    row.setCatBachilleratos(new CatBachilleratos())
                    row.getCatBachilleratos().setDescripcion(rs.getString("bachilleratos"))
                    row.getCatBachilleratos().setPersistenceId(rs.getLong("CATBACHILLERATOS_PID"))
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += e.getMessage()
                }

                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setPersistenceId(rs.getLong("PERSISTENCEID"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setUsuarioCreacion(rs.getString("usuariocreacion"))
                row.setCampus(rs.getString("campus"))
                row.setConvenioDescuento(rs.getString("convenioDescuento"))

                rows.add(row)
            }

            resultado.setSuccess(true)
            resultado.setError(errorLog)
            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatGestionEscolar(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", orderby = "ORDER BY ", errorLog = "", bachillerato = "", campus = ""

        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();

        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
            List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)

            userLogged = context.getApiSession().getUserId();

            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getDescripcion());
                        break;
                    }
                }
            }

            if (lstGrupo.size() > 0) {
                campus += " AND ("
            }
            for (Integer i = 0; i < lstGrupo.size(); i++) {
                String campusMiembro = lstGrupo.get(i);
                campus += "campus.descripcion='" + campusMiembro + "'"
                if (i == (lstGrupo.size() - 1)) {
                    campus += ") "
                } else {
                    campus += " OR "
                }
            }

            String consulta = Statements.GET_CATGESTIONESCOLAR
            CatGestionEscolar row = new CatGestionEscolar();
            List < CatDescuentosCustom > rows = new ArrayList < CatDescuentosCustom > ();
            closeCon = validarConexion();

            where = "WHERE GE.isEliminado = false and campus.isEliminado = false and GE.campus = '" + object.campus + "'"
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                def booleanos = filtro.get("valor");
                switch (filtro.get("columna")) {
                    case "NOMBRE LICENCIATURA":
                        where += " AND LOWER(GE.nombre) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CLAVE":
                        where += " AND LOWER(GE.CLAVE) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "LIGA":
                        where += " AND LOWER(GE.enlace) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCION DE CARRERA":
                        where += " AND LOWER(GE.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "INSCRIPCIÓN ENERO":
                        where += " AND LOWER(GE.inscripcionenero) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "INSCRIPCIÓN AGOSTO":
                        where += " AND LOWER(GE.inscripcionagosto) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        where += " AND LOWER(GE.PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TIPO LICENCIATURA":
                        where += " AND LOWER(GE.TIPOLICENCIATURA) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "INSCRIPCIÓN MAYO":
                        where += " AND LOWER(GE.inscripcionMayo) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "INSCRIPCIÓN SEPTIEMBRE":
                        where += " AND LOWER(GE.inscripcionSeptiembre) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "PROPEDÉUTICO":
                        where += " AND GE.propedeutico ";
                        where += " = [valor]"
                        errorLog += " Que valor tienen: " + booleanos.toString().equals("Si") + " "
                        where = where.replace("[valor]", (booleanos.toString().equals("Si") ? "true" : booleanos.toString().equals("Sí") ? "true" : "false"))
                        break;


                    case "PROGRAMA PARCIAL":
                        where += " AND GE.programaparcial ";
                        where += " = [valor]"
                        errorLog += " Que valor tienen: " + booleanos.toString().equals("Si") + " "
                        where = where.replace("[valor]", (booleanos.toString().equals("Si") ? "true" : booleanos.toString().equals("Sí") ? "true" : "false"))
                        break;
                        /* case "CAMPUS":
                         	campus +=" AND LOWER(campus.DESCRIPCION) ";
                        	 if(filtro.get("operador").equals("Igual a")) {
                        		 campus+="=LOWER('[valor]')"
                        	 }else {
                        		 campus+="LIKE LOWER('%[valor]%')"
                        	 }
                        	 campus = campus.replace("[valor]", filtro.get("valor"))
                        break;*/
                }
            }
            switch (object.orderby) {
                case "NOMBRE LICENCIATURA":
                    orderby += "GE.nombre";
                    break;
                case "LIGA":
                    orderby += "GE.enlace";
                    break;
                case "CLAVE":
                    orderby += "GE.clave";
                    break;
                case "DESCRIPCION DE CARRERA":
                    orderby += "GE.descripcion";
                    break;
                case "INSCRIPCIÓN ENERO":
                    orderby += "GE.inscripcionenero::Integer";
                    break;
                case "INSCRIPCIÓN AGOSTO":
                    orderby += "GE.inscripcionagosto::Integer";
                    break;
                case "INSCRIPCIÓN MAYO":
                    orderby += "GE.inscripcionmayo::Integer";
                    break;
                case "INSCRIPCIÓN SEPTIEMBRE":
                    orderby += "GE.inscripcionseptiembre::Integer";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "GE.PERSISTENCEVERSION";
                    break;
                case "CAMPUS":
                    orderby += "campus.descripcion";
                    break;
                case "TIPO LICENCIATURA":
                    orderby += "GE.tipolicenciatura";
                    break;
                case "PROPEDÉUTICO":
                    orderby += "GE.propedeutico";
                    break;
                case "PROGRAMA PARCIAL":
                    orderby += "GE.programaparcial";
                    break;
                default:
                    orderby += "GE.nombre"
                    break;
            }

            orderby += " " + object.orientation;
            //where+=" "+campus
            consulta = consulta.replace("[CAMPUS]", campus)
            consulta = consulta.replace("[WHERE]", where);

            pstm = con.prepareStatement(consulta.replace("GE.*, campus.descripcion as nombreCampus", "COUNT(GE.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorLog += " " + consulta
            errorLog += " consulta= "
            errorLog += consulta
            errorLog += " where = " + where
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            errorLog += "fecha=="

            rs = pstm.executeQuery()
            while (rs.next()) {

                row = new CatGestionEscolar()
                row.setCampus(rs.getString("campus"))
                //row.setCaseId(rs.getString("caseId"))
                row.setDescripcion(rs.getString("descripcion"))
                row.setEnlace(rs.getString("enlace"))
                try {
                    row.setFechaCreacion(new java.util.Date(rs.getDate("fechaCreacion").getTime()))
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += ", " + e.getMessage()
                }
                row.setClave(rs.getString("clave"));
                row.setInscripcionagosto(rs.getString("inscripcionagosto"))
                row.setInscripcionenero(rs.getString("inscripcionenero"))
                row.setIsEliminado(rs.getBoolean("isEliminado"))
                //row.setMatematicas(rs.getBoolean("matematicas"))
                row.setNombre(rs.getString("nombre"))
                row.setPersistenceId(rs.getLong("persistenceId"))
                row.setPersistenceVersion(rs.getLong("persistenceVersion"))
                row.setProgramaparcial(rs.getBoolean("programaparcial"))
                row.setPropedeutico(rs.getBoolean("propedeutico"))
                row.setUsuarioCreacion(rs.getString("usuarioCreacion"))
                row.setTipoLicenciatura(rs.getString("tipoLicenciatura"))
                row.setTipoCentroEstudio(rs.getString("tipoCentroEstudio"))
                row.setInscripcionMayo(rs.getString("inscripcionMayo"))
                row.setInscripcionSeptiembre(rs.getString("inscripcionSeptiembre"))
                row.setUrlImgLicenciatura(rs.getString("urlImgLicenciatura"))
                row.setIsMedicina(rs.getBoolean("isMedicina"))
                row.setIdioma(rs.getString("idioma"))

                rows.add(row)
            }

            resultado.setSuccess(true)
            resultado.setError(errorLog)
            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result activarDesactivarLugarExamen(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String errorLog = "";
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            closeCon = validarConexion();
            con.setAutoCommit(false)
            pstm = con.prepareStatement(Statements.UPDATE_LUGAREXAMEN)
            pstm.setBoolean(1, object.deshabilitado);

            pstm.executeUpdate();

            con.commit();
            resultado.setSuccess(true)
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatGenerico(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", orderby = "ORDER BY ", errorLog = ""
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            //assert object instanceof List;
            String consulta = Statements.GET_CATGENERICO
            CatGenericoFiltro row = new CatGenericoFiltro();
            List < CatGenericoFiltro > rows = new ArrayList < CatGenericoFiltro > ();
            closeCon = validarConexion();
            where += " WHERE isEliminado = false";
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {

                switch (filtro.get("columna")) {
                    case "ORDEN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        if (object.isOrdenAD || object.isBannerAD) {
                            where += " LOWER(clave) ";
                            if (filtro.get("operador").equals("Igual a")) {
                                where += "=LOWER('[valor]')"
                            } else {
                                where += "LIKE LOWER('%[valor]%')"
                            }
                        } else {
                            where += " CAST(orden as varchar ) ";
                            if (filtro.get("operador").equals("Igual a")) {
                                where += "=LOWER('[valor]')"
                            } else {
                                where += "LIKE LOWER('%[valor]%')"
                            }
                        }

                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA CREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO CREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "FECHA CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "USUARIO BANNER":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOBANNER) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "FECHA IMPORTACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHA IMPORTACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TOTC":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(totc) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
						
                    case "LETRA":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(letra) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
					
					case "ESCALA CÓDIGO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(letra) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
						
                    case "VALOR DE ESCALA":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(equivalente) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
					case "EQUIVALENTE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(equivalente) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
						
                    case "SEXO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " sexo [valor] ";
                        where = where.replace("[valor]", (filtro.get("valor").toString().equals("Masculino")?"is true":" is false OR sexo is NULL"))
                        break;

                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break;
                case "ORDEN":
                    if (object.isOrdenAD || object.isBannerAD) { orderby += "clave::integer" } else { orderby += "orden"; }
                    break;
                case "DESCRIPCION":
                    orderby += "descripcion";
                    break;
                case "FECHACREACION":
                    orderby += "fechaCreacion";
                    break;
                case "ISELIMINADO":
                    orderby += "isEliminado";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "USUARIOCREACION":
                    orderby += "usuarioCreacion";
                    break;
                case "DESCRIPCIÓN":
                    orderby += "descripcion";
                    break;
                case "USUARIO CREACIÓN":
                    orderby += "usuarioCreacion";
                    break;
                case "FECHA CREACIÓN":
                    orderby += "fechaCreacion";
                    break;
                case "USUARIO BANNER":
                    orderby += "usuarioBanner";
                    break;
                case "FECHA IMPORTACIÓN":
                    orderby += "fechaImportacion";
                    break;
                default:
                    if (object.isOrden) 
					{ orderby += "orden::integer" } 
					else if (object.isOrdenAD || object.isBannerAD) 
					{ orderby += "clave::integer" } 
					else if(object.catalogo.equals("CatEscalaINVP"))
					{ orderby += "Sexo"}
					else { orderby += "descripcion" }

                    break;
            }
            errorLog += "orderby"
            orderby += " " + object.orientation;
            consulta = consulta.replace("[WHERE]", where);
            consulta = consulta.replace("[CATALOGO]", object.catalogo)
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorLog += "consulta"
            errorLog += consulta
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            errorLog += " fecha=="

            rs = pstm.executeQuery()
            while (rs.next()) {

                row = new CatGenericoFiltro();
				if(object.catalogo.equals("CatEscalaINVP")){
					row.setTotc(rs.getString("totc"));
					row.setLetra(rs.getString("letra"));
					row.setEquivalente(rs.getString("equivalente"));
					row.setSexo(rs.getBoolean("sexo"));
					row.setFechaCreacion(rs.getString("fechacreacion"));
					row.setIsEliminado(rs.getBoolean("isEliminado"));
					row.setPersistenceId(rs.getLong("PERSISTENCEID"));
					row.setPersistenceVersion(rs.getString("persistenceVersion") == null ? null:rs.getLong("persistenceVersion"));
					row.setUsuarioCreacion(rs.getString("usuariocreacion"));
				}else{
					row.setClave(rs.getString("clave"))
					if (object.isOrden) { row.setOrden(rs.getString("orden")) }
					row.setDescripcion(rs.getString("descripcion"));
					row.setFechaCreacion(rs.getString("fechacreacion"));
					row.setIsEliminado(rs.getBoolean("isEliminado"));
					row.setPersistenceId(rs.getLong("PERSISTENCEID"));
					row.setPersistenceVersion(rs.getLong("persistenceVersion"));
					if (object.isBannerAD) {
						row.setFechaImportacion(rs.getString("fechaimportacion"));
						row.setUsuarioCreacion(rs.getString("usuariobanner"));
					} else {
						row.setUsuarioCreacion(rs.getString("usuariocreacion"));
					}
					if(object.isAdmitido) {
						row.setIsAdmitido(rs.getBoolean("isAdmitido"));
					}
				
				}
                


                rows.add(row)
            }
            errorLog += " paso el listado";
            resultado.setSuccess(true)
            resultado.setError(errorLog)
            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatParentescoA(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", orderby = "ORDER BY ", errorLog = ""
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            //assert object instanceof List;
            String consulta = Statements.GET_CATPARENTESCTO
            CatParentescoFiltro row = new CatParentescoFiltro();
            List < CatParentescoFiltro > rows = new ArrayList < CatParentescoFiltro > ();
            closeCon = validarConexion();
            where += " WHERE isEliminado = false";
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {

                switch (filtro.get("columna")) {

                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA CREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO CREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "FECHA CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "USUARIO CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break;
                case "ID":
                    orderby += "id";
                    break;
                case "DESCRIPCION":
                    orderby += "descripcion";
                    break;
                case "FECHACREACION":
                    orderby += "fechaCreacion";
                    break;
                case "ISELIMINADO":
                    orderby += "isEliminado";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "USUARIOCREACION":
                    orderby += "usuarioCreacion";
                    break;
                case "DESCRIPCIÓN":
                    orderby += "descripcion";
                    break;
                case "USUARIO CREACIÓN":
                    orderby += "usuarioCreacion";
                    break;
                case "FECHA CREACIÓN":
                    orderby += "fechaCreacion";
                    break;
                default:
                    if (object.isOrden) { orderby += "orden" } else { orderby += "persistenceid" }

                    break;
            }
            errorLog += "orderby"
            orderby += " " + object.orientation;
            consulta = consulta.replace("[WHERE]", where);
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorLog += "consulta"
            errorLog += consulta
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            errorLog += " fecha=="

            rs = pstm.executeQuery()
            while (rs.next()) {

                row = new CatParentescoFiltro();
                row.setClave(rs.getString("clave"))
                row.setId(rs.getString("id"))
                row.setDescripcion(rs.getString("descripcion"));
                row.setFechaCreacion(rs.getString("fechacreacion"));
                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setPersistenceId(rs.getLong("PERSISTENCEID"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setUsuarioCreacion(rs.getString("usuariocreacion"));

                rows.add(row)
            }
            errorLog += " paso el listado";
            resultado.setSuccess(true)
            resultado.setError(errorLog)
            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatEstadoG(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", orderby = "ORDER BY ", errorLog = ""
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            //assert object instanceof List;
            String consulta = Statements.GET_CATGENERICO
            CatEstadoGFiltro row = new CatEstadoGFiltro();
            List < CatEstadoGFiltro > rows = new ArrayList < CatEstadoGFiltro > ();
            closeCon = validarConexion();
            where += " WHERE isEliminado = false";
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {

                switch (filtro.get("columna")) {
                    case "ORDEN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " CAST(orden as varchar ) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA CREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO CREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "FECHA CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "USUARIO CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break;
                case "ORDEN":
                    orderby += "orden";
                    break;
                case "DESCRIPCION":
                    orderby += "descripcion";
                    break;
                case "FECHACREACION":
                    orderby += "fechaCreacion";
                    break;
                case "ISELIMINADO":
                    orderby += "isEliminado";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "USUARIOCREACION":
                    orderby += "usuarioCreacion";
                    break;
                case "DESCRIPCIÓN":
                    orderby += "descripcion";
                    break;
                case "USUARIO CREACIÓN":
                    orderby += "usuarioCreacion";
                    break;
                case "FECHA CREACIÓN":
                    orderby += "fechaCreacion";
                    break;
                case "PAÍS":
                    orderby += "pais";
                    break;

                default:
                    if (object.isOrden) { orderby += "orden" } else { orderby += "persistenceid" }

                    break;
            }
            errorLog += "orderby"
            orderby += " " + object.orientation;
            consulta = consulta.replace("[WHERE]", where);
            consulta = consulta.replace("[CATALOGO]", object.catalogo)
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorLog += "consulta"
            errorLog += consulta
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            errorLog += " fecha=="

            rs = pstm.executeQuery()
            while (rs.next()) {

                row = new CatEstadoGFiltro();
                row.setClave(rs.getString("clave"))
                if (object.isOrden) { row.setOrden(rs.getString("orden")) }
                row.setDescripcion(rs.getString("descripcion"));
                row.setFechaCreacion(rs.getString("fechacreacion"));
                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setPersistenceId(rs.getLong("PERSISTENCEID"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setUsuarioCreacion(rs.getString("usuariocreacion"));
                row.setPais(rs.getString("pais"));

                rows.add(row)
            }
            errorLog += " paso el listado";
            resultado.setSuccess(true)
            resultado.setError(errorLog)
            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public LocalDateTime stringParse(String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        Date creacion = sdf.parse(fecha)
        LocalDateTime localDateTime = creacion.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        return localDateTime;
    }

    public Result getCatNacionalidad(String jsonData) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "WHERE ISELIMINADO=false", orderby = "ORDER BY ", errorlog = ""
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            CatNacionalidadCustomeFiltro row = new CatNacionalidadCustomeFiltro()
            List < CatNacionalidadCustomeFiltro > rows = new ArrayList < CatNacionalidadCustomeFiltro > ();
            closeCon = validarConexion();


            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                switch (filtro.get("columna")) {
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CLAVE) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO BANNER":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(usuariobanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA IMPLEMENTACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHAIMPORTACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break
                case "DESCRIPCIÓN":
                    orderby += "descripcion";
                    break
                case "USUARIO BANNER":
                    orderby += "usuariobanner";
                    break
                case "FECHACREACION":
                    orderby += "fechacreacion";
                    break
                case "FECHAIMPORTACION":
                    orderby += "fechaImplementacion";
                    break
                default:
                    orderby += "ORDEN";
                    break;
            }
            orderby += " " + object.orientation;
            String consulta = Statements.GET_CATNACIONALIDAD;
            consulta = consulta.replace("[WHERE]", where);
            errorlog += "consulta:"
            errorlog += consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "")
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorlog += "consulta:"
            errorlog += consulta
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)
            rs = pstm.executeQuery()
            while (rs.next()) {
                row = new CatNacionalidadCustomeFiltro()
                row.setClave(rs.getString("clave"))
                row.setCaseId(rs.getString("CASEID"))
                row.setDescripcion(rs.getString("descripcion"))
                row.setIsEliminado(rs.getBoolean("isEliminado"))
                row.setIsEnabled(rs.getBoolean("isEnabled"))
                try {
                    row.setFechaCreacion(rs.getString("fechacreacion"))
                    row.setFechaImplementacion(rs.getString("fechaimplementacion"))
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorlog += e.getMessage()
                }

                row.setPersistenceId(rs.getLong("persistenceId"))
                row.setPersistenceVersion(rs.getLong("persistenceVersion"))
                row.setUsuarioBanner(rs.getString("usuariobanner"))
                rows.add(row)
            }
            resultado.setSuccess(true)

            resultado.setData(rows)
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getPeriodosActivos(Integer P, Integer C, String tipo, String id) {
        Result resultado = new Result();
        Boolean closeCon = false;
        List < Boolean > lstResultado = new ArrayList < Boolean > ();
        try {
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            CatPeriodoCustom row = new CatPeriodoCustom();
            closeCon = validarConexion();
            String consulta, errorLog = "";

            consulta = Statements.GET_PERIODOSACTIVOS;
            consulta = consulta.replace("[TIPO]", tipo);
            if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
                consulta = consulta.replace("[ID]", (" AND PERSISTENCEID <> " + id))
            } else {
                consulta = consulta.replace("[ID]", "")
            }

            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, (tipo.equals("ISANUAL") ? 1 : 3))
            rs = pstm.executeQuery()
            while (rs.next()) {
                row = new CatPeriodoCustom();
                row.setClave(rs.getString("clave"));
                row.setDescripcion(rs.getString("descripcion"))
                row.setFechaFin(rs.getString("fechaFin"));
                row.setFechaInicio(rs.getString("fechaInicio"));
                rows.add(row)
            }

            resultado.setSuccess(true)
            
            resultado.setData(rows)
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getPeriodosSiguientes(Integer P, Integer C, String tipo, String fecha, String id) {
        Result resultado = new Result();
        Boolean closeCon = false;
        List < Boolean > lstResultado = new ArrayList < Boolean > ();
        try {
            closeCon = validarConexion();
            String consulta, errorLog = "";

            consulta = Statements.GET_PERIODOSSIGUIENTES;
            consulta = consulta.replace("[TIPO]", tipo);
            consulta = consulta.replace("[FECHA]", "'" + fecha + "'")
            if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
                consulta = consulta.replace("[ID]", (" AND PERSISTENCEID <> " + id))
            } else {
                consulta = consulta.replace("[ID]", " ")
            }
            errorLog += " " + consulta
            pstm = con.prepareStatement(consulta)
            rs = pstm.executeQuery()
            while (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }

            resultado.setSuccess(true)
            
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }


    public Result getValidarClavePeriodo(Integer P, Integer C, String CLAVE, String tipo, String id) {
        Result resultado = new Result();
        Boolean closeCon = false;
        List < Boolean > lstResultado = new ArrayList < Boolean > ();
        try {
            closeCon = validarConexion();
            String consulta, errorLog = "";

            consulta = Statements.GET_VALIDARCLAVEPERIODO;
            consulta = consulta.replace("[TIPO]", tipo);
            if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
                consulta = consulta.replace("[ID]", (" AND PERSISTENCEID <> " + id))
            } else {
                consulta = consulta.replace("[ID]", " ")
            }
            errorLog += " " + consulta
            pstm = con.prepareStatement(consulta)
            pstm.setString(1, CLAVE);
            rs = pstm.executeQuery()
            while (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("TOTAL"))
            }

            resultado.setSuccess(true)
            
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }
	
	public Result getValidarEscalaEAC(Integer P, Integer C, String escala, String id) {
        Result resultado = new Result();
        Boolean closeCon = false;
        List < Boolean > lstResultado = new ArrayList < Boolean > ();
        try {
            closeCon = validarConexion();
            String consulta, errorLog = "";

            consulta = Statements.GET_VALIDARESCALAEAC;
            if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
                consulta = consulta.replace("[ID]", (" AND PERSISTENCEID <> " + id))
            } else {
                consulta = consulta.replace("[ID]", " ")
            }
			errorLog += " " + consulta
            pstm = con.prepareStatement(consulta)
            pstm.setString(1, escala);
            rs = pstm.executeQuery()
            while (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("TOTAL"))
            }

            resultado.setSuccess(true)
            
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }


    /***********************JESUS OSUNA FIN********************************/

    /***********************JOSÉ GARCÍA ******************************/

    public Result getCatPropedeutico(String jsonData) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "WHERE p.ISELIMINADO=false", orderby = "ORDER BY ", errorlog = ""
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            CatPropedeuticoCustom row = new CatPropedeuticoCustom();
            List < CatTitulo > rows = new ArrayList < CatTitulo > ();
            closeCon = validarConexion();

            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                switch (filtro.get("columna")) {
                    case "DESCRIPCION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CLAVE) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(c.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHACREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIOCREACION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHAINICIO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHAINICIO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHAFINAL":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHAFINAL) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "NOMBRECAMPUS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(NOMBRECAMPUS) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(campus) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            switch (object.orderby) {
                case "Clave":
                    orderby += "clave";
                    break;
                case "Descripción":
                    orderby += "DESCRIPCION";
                    break
                case "Usuario creación":
                    orderby += "USUARIOCREACION";
                    break
                case "Fecha creación":
                    orderby += "FECHACREACION";
                    break
                case "Fecha inicio":
                    orderby += "FECHAINICIO";
                    break
                case "Fecha final":
                    orderby += "FECHAFINAL";
                    break
                case "Nombre campus":
                    orderby += "NOMBRECAMPUS";
                    break
            }

            if (orderby.equals("ORDER BY ")) {
                orderby += "p.PERSISTENCEID";
            }

            orderby += " " + object.orientation;

            String consulta = Statements.GET_CATPROPEDEUTICO;
            consulta = consulta.replace("[WHERE]", where);
            errorlog += "consulta:"
            errorlog += consulta.replace("*", "COUNT(p.PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "")
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(p.PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorlog += "consulta:"
            errorlog += consulta
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)
            rs = pstm.executeQuery();
            while (rs.next()) {
                Date creacion = sdf.parse(rs.getString("fechacreacion"));
                Date inicio = sdf.parse(rs.getString("fechaInicio"));
                Date fin = sdf.parse(rs.getString("fechaFinal"));

                row = new CatPropedeuticoCustom()
                row.setClave(rs.getString("clave"));
                row.setDescripcion(rs.getString("descripcion"));
                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setPersistenceId(rs.getLong("persistenceId"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setUsuarioCreacion(rs.getString("usuarioCreacion"));
                row.setFechaCreacion(sdf.format(creacion));
                row.setFechaInicio(sdf.format(inicio));
                row.setFechaFinal(sdf.format(fin));
                row.setNombreCampus(rs.getString("nombreCampus"));
                CatCampus campus = new CatCampus();
                campus.setDescripcion(rs.getString("campus"))
                row.setCampus(campus);

                rows.add(row);
            }
            resultado.setSuccess(true)

            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getValidarClave(Integer parameterP, Integer parameterC, String tabla, String clave, String id) {
        Result resultado = new Result();
        Boolean closeCon = false;
        List < Boolean > lstResultado = new ArrayList < Boolean > ();

        try {
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            closeCon = validarConexion();
            String consulta;
            String errorLog = consulta + ";";
            if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
                consulta = Statements.GET_VALIDACION_CLAVE_EDIT;
                errorLog += "GET_VALIDACION_CLAVE_EDIT; ";
            } else {
                consulta = Statements.GET_VALIDACION_CLAVE;
                errorLog += "GET_VALIDACION_CLAVE; ";
            }

            errorLog += consulta + ";";
            
            consulta = consulta.replace("[TABLA]", tabla);
            errorLog += consulta + ";";

            pstm = con.prepareStatement(consulta);
            pstm.setString(1, clave.toLowerCase());
            if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
                pstm.setLong(2, Long.valueOf(id));
            }

            rs = pstm.executeQuery();

            while (rs.next()) {
                lstResultado.add(rs.getInt("total") < 1);
            }

            resultado.setSuccess(true);
            resultado.setData(lstResultado);
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }
	
	public Result getValidarEscalaINVP(String tabla, String totc,String letra, String Sexo, String id) {
        Result resultado = new Result();
        Boolean closeCon = false;
        List < Boolean > lstResultado = new ArrayList < Boolean > ();

        try {
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            closeCon = validarConexion();
            String consulta;
            String errorLog = consulta + ";";
            if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
                consulta = Statements.GET_VALIDACION_ESCALA_INVP_EDIT;
            } else {
                consulta = Statements.GET_VALIDACION_ESCALA_INVP;
            }

            errorLog += consulta + ";";
            
            consulta = consulta.replace("[TABLA]", tabla);
            errorLog += consulta + ";";

            pstm = con.prepareStatement(consulta);
            pstm.setString(1, totc.toLowerCase());
			pstm.setString(2, letra.toLowerCase());
			pstm.setBoolean(3, Sexo.toBoolean());
			
            if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
                pstm.setLong(4, Long.valueOf(id));
            }

            rs = pstm.executeQuery();

            while (rs.next()) {
                lstResultado.add(rs.getInt("total") < 1);
            }

            resultado.setSuccess(true);
            resultado.setData(lstResultado);
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }
	

    public Result getValidarOrden(Integer parameterP, Integer parameterC, String tabla, Integer orden, String id) {
        Result resultado = new Result();
        Boolean closeCon = false;
        List < Boolean > lstResultado = new ArrayList < Boolean > ();

        try {
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            closeCon = validarConexion();
            String consulta;

            if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
                consulta = Statements.GET_VALIDACION_ORDEN_EDIT;
            } else {
                consulta = Statements.GET_VALIDACION_ORDEN;
            }

            consulta = consulta.replace("[TABLA]", tabla);
            String errorLog = consulta;
            
            pstm = con.prepareStatement(consulta);
            pstm.setInt(1, orden);
            if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
                pstm.setLong(2, Long.valueOf(id));
            }

            rs = pstm.executeQuery();

            while (rs.next()) {
                lstResultado.add(rs.getInt("total") < 1);
            }

            resultado.setSuccess(true);
            resultado.setData(lstResultado);
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getValidarIdBanner(Integer parameterP, Integer parameterC, String tabla, String idBanner, String id) {
        Result resultado = new Result();
        Boolean closeCon = false;
        List < Boolean > lstResultado = new ArrayList < Boolean > ();

        try {
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            closeCon = validarConexion();
            String consulta;

            if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
                consulta = Statements.GET_VALIDACION_IDBANNER_EDIT;
            } else {
                consulta = Statements.GET_VALIDACION_IDBANNER;
            }

            consulta = consulta.replace("[TABLA]", tabla);

            String errorLog = consulta;
            
            pstm = con.prepareStatement(consulta);
            pstm.setString(1, idBanner);
            if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
                pstm.setLong(2, Long.valueOf(id));
            }

            rs = pstm.executeQuery();

            while (rs.next()) {
                lstResultado.add(rs.getInt("total") < 1);
            }

            resultado.setSuccess(true);
            resultado.setData(lstResultado);
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatNacionalidadNew(String jsonData) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "WHERE ISELIMINADO=false", orderby = "ORDER BY ", errorlog = ""

        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            CatNacionalidadCustomeFiltro row = new CatNacionalidadCustomeFiltro()
            List < CatNacionalidadCustomeFiltro > rows = new ArrayList < CatNacionalidadCustomeFiltro > ();
            closeCon = validarConexion();

            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                switch (filtro.get("columna")) {
                    case "ORDEN":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(COALESCE(orden, 0)::VARCHAR) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(CLAVE) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ID":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(ID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO BANNER":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(usuariobanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA IMPLEMENTACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(FECHAIMPORTACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.get("valor"));
                        break;
                }
            }

            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break;
                case "DESCRIPCIÓN":
                    orderby += "descripcion";
                    break;
                case "ID BANNER":
                    orderby += "id";
                    break;
                case "USUARIO BANNER":
                    orderby += "usuariobanner";
                    break;
                case "FECHACREACION":
                    orderby += "fechacreacion";
                    break;
                case "FECHAIMPORTACION":
                    orderby += "fechaImplementacion";
                    break;
                default:
                    orderby += "ORDEN";
                    break;
            }

            orderby += " " + object.orientation;
            String consulta = Statements.GET_CAT_NACIONALIDAD;
            consulta = consulta.replace("[WHERE]", where);
            errorlog += "consulta:";
            errorlog += consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "");
            pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""));
            rs = pstm.executeQuery();
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"));
            }
            consulta = consulta.replace("[ORDERBY]", orderby);
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
            errorlog += "consulta:";
            errorlog += consulta;
            pstm = con.prepareStatement(consulta);
            pstm.setInt(1, object.limit);
            pstm.setInt(2, object.offset);
            rs = pstm.executeQuery();

            while (rs.next()) {
                row = new CatNacionalidadCustomeFiltro()
                row.setClave(rs.getString("clave"));
                row.setCaseId(rs.getString("CASEID"));
                row.setOrden(rs.getLong("orden"));
                row.setDescripcion(rs.getString("descripcion"));
                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setIsEnabled(rs.getBoolean("isEnabled"));

                try {
                    row.setFechaCreacion(rs.getString("fechacreacion"))
                    row.setFechaImplementacion(rs.getString("fechaimplementacion"))
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorlog += e.getMessage()
                }
                row.setId(rs.getString("id"));
                row.setPersistenceId(rs.getLong("persistenceId"))
                row.setPersistenceVersion(rs.getLong("persistenceVersion"))
                row.setUsuarioBanner(rs.getString("usuariobanner"));
                rows.add(row)
            }
            resultado.setSuccess(true)

            resultado.setData(rows)
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }
    /***********************JOSÉ GARCÍA FIN ******************************/
    /***********************ERIC ROSAS ********************************/

    public Result getCatPeriodo(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;
        String where = "", orderby = "ORDER BY ", errorlog = "", role = "", campus = "", group = "";
        List < String > lstGrupo = new ArrayList < String > ();
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            //assert object instanceof List;

            String consulta = Statements.GET_CATPERIODO
            CatPeriodoCustom row = new CatPeriodoCustom();
            List < CatPeriodoCustom > rows = new ArrayList < CatPeriodoCustom > ();
            closeCon = validarConexion();


            /*def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
            List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
					
            userLogged = context.getApiSession().getUserId();
					
            List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for(UserMembership objUserMembership : lstUserMembership) {
            	for(CatCampus rowGrupo : lstCatCampus) {
            		if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
            			lstGrupo.add(rowGrupo.getDescripcion());
            			break;
            		}
            	}
            }
					
            if(lstGrupo.size()>0) {
            	campus+=" AND ("
            }
            for(Integer i=0; i<lstGrupo.size(); i++) {
            	String campusMiembro=lstGrupo.get(i);
            	campus+="campus.descripcion='"+campusMiembro+"'"
            	if(i==(lstGrupo.size()-1)) {
            		campus+=") "
            	}
            	else {
            		campus+=" OR "
            	}
            }*/
            //where+=" WHERE P.isEliminado = false AND nombreCampus = '"+object.campus+"'";
            where += " WHERE P.isEliminado = false";
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                def booleanos = filtro.get("valor");
                switch (filtro.get("columna")) {

                    case "SEMESTRAL":

                        where += " AND P.isSemestral ";
                        where += "= [valor] "
                        where = where.replace("[valor]", booleanos.toString().equals("Si") ? "true" : booleanos.toString().equals("Sí") ? "true" : "false")
                        break;

                    case "ANUAL":
                        where += " AND P.isAnual ";
                        where += " = [valor]"
                        errorlog += " Que valor tienen: " + booleanos.toString().equals("Si") + " "
                        where = where.replace("[valor]", (booleanos.toString().equals("Si") ? "true" : booleanos.toString().equals("Sí") ? "true" : "false"))
                        break;

                    case "ACTIVO":
                        where += " AND P.ACTIVO ";
                        where += " = [valor]"
                        errorlog += " Que valor tienen: " + booleanos.toString().equals("Si") + " "
                        where = where.replace("[valor]", (booleanos.toString().equals("Si") ? "true" : booleanos.toString().equals("Sí") ? "true" : "false"))
                        break;
                    case "CLAVE":
                        where += " AND LOWER(P.clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCIÓN":
                        where += " AND LOWER(P.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CUATRIMESTRAL":
                        where += " AND P.isCuatrimestral ";
                        where += "=[valor]"
                        where = where.replace("[valor]", (booleanos.toString().equals("Si") ? "true" : booleanos.toString().equals("Sí") ? "true" : "false"))
                        break;
                    case "FECHA CREACIÓN":
                        where += " AND LOWER(CAST(P.FECHACREACION as varchar)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "FECHA IMPORTACIÓN":
                        where += " AND LOWER(CAST(P.FECHAIMPORTACION as varchar)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        where += " AND LOWER(P.PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        where += " AND LOWER(P.PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO BANNER":
                        where += " AND LOWER(P.USUARIOBANNER) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "INICIO CAPTACIÓN":
                        where += " AND LOWER(CAST(P.FECHAINICIO as varchar)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "FIN CAPTACIÓN":
                        where += " AND LOWER(CAST(P.FECHAFIN as varchar)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                        /*case "CAMPUS":
                        	errorlog+="CAMPUS"
                        	campus +=" AND LOWER(campus.DESCRIPCION) ";
                        	where +=" AND LOWER(campus.DESCRIPCION)  "
                        	if(filtro.get("operador").equals("Igual a")) {
                        		campus+="=LOWER('[valor]')"
                        		where +="=LOWER('[valor]')"
                        	}else {
                        		campus+="LIKE LOWER('%[valor]%')"
                        		where+="LIKE LOWER('%[valor]%')"
                        	}
                        	campus = campus.replace("[valor]", filtro.get("valor"))
                        	where = where.replace("[valor]", filtro.get("valor"))
                        	break;*/
                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "P.clave";
                    break;
                case "DESCRIPCIÓN":
                    orderby += "P.descripcion";
                    break;
                case "CUATRIMESTRAL":
                    orderby += "P.isCuatrimestral";
                    break;
                case "SEMESTRAL":
                    orderby += "P.isSemestral";
                    break;
                case "ANUAL":
                    orderby += "P.isAnual";
                    break;
                case "FECHA CREACIÓN":
                    orderby += "P.fechaCreacion";
                    break;
                case "ISELIMINADO":
                    orderby += "P.isEliminado";
                    break;
                case "PERSISTENCEID":
                    orderby += "P.persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "P.persistenceVersion";
                    break;
                case "USUARIO BANNER":
                    orderby += "P.usuarioBanner";
                    break;
                case "FECHA IMPORTACIÓN":
                    orderby += "P.fechaImplementacion";
                    break;
                case "INICIO CAPTACIÓN":
                    orderby += "P.fechaInicio";
                    break;
                case "FIN CAPTACIÓN":
                    orderby += "P.fechaFin";
                    break;
                case "ACTIVO":
                    orderby += "P.ACTIVO DESC, CAST(FECHAFIN as DATE) >= NOW() DESC, P.FECHAFIN";
                    break;
                default:
                    orderby += " P.ACTIVO DESC, CAST(FECHAFIN as DATE) >= NOW() DESC, P.FECHAFIN"
                    break;
            }
            errorlog += "orderby"
            orderby += " " + object.orientation;
            //consulta=consulta.replace("[CAMPUS]", campus)
            //consulta=consulta.replace("[CAMPUS]", "")
            consulta = consulta.replace("[WHERE]", where);
            pstm = con.prepareStatement(consulta.replace("P.*", "COUNT(P.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorlog += "consulta = "
            errorlog += consulta
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            errorlog += "fecha=="

            rs = pstm.executeQuery()
            while (rs.next()) {

                row = new CatPeriodoCustom();
                row.setClave(rs.getString("clave"))
                row.setDescripcion(rs.getString("descripcion"));

                row.setIsEliminado(rs.getBoolean("isEliminado"));
                row.setPersistenceId(rs.getLong("PERSISTENCEID"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setUsuarioBanner(rs.getString("usuarioBanner"));
                row.setIsAnual(rs.getBoolean("isAnual"));
                row.setIsCuatrimestral(rs.getBoolean("isCuatrimestral"));
                row.setIsSemestral(rs.getBoolean("isSemestral"));
                row.setActivo(rs.getBoolean("activo"));
                row.setId(rs.getString("id"));

                /*try {
                	row.setCampus(new CatCampus())
                	row.getCampus().setPersistenceId(rs.getLong("CAMPUS_PID"))
                	}catch(Exception e) {
                		LOGGER.error "[ERROR] " + e.getMessage();
                	errorlog+=e.getMessage()
                }*/


                row.setIsEnabled(rs.getBoolean("isEnabled"))
                row.setPersistenceId_string(rs.getString("PERSISTENCEID"))
                row.setNombreCampus(rs.getString("NombreCampus"))


                try {
                    row.setFechaImportacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("FECHAIMPORTACION")));
                    //row.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechacreacion")))
                    //row.setFechaFin(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaFin")));
                    //row.setFechaInicio(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaInicio")));
                    row.setFechaCreacion(rs.getString("fechacreacion"))
                    row.setFechaFin(rs.getString("fechaFin"));
                    row.setFechaInicio(rs.getString("fechaInicio"));
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorlog += " cuatrimestral " + e.getMessage()
                }


                rows.add(row)
            }

            resultado.setSuccess(true)
            
            resultado.setData(rows)

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    /***********************ERIC ROSAS FIN********************************/
    /***********************ARTURO ZAMORANO*******************************/

    public Result getCatCiudad(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;

        String where = "WHERE C.ISELIMINADO=false";
        String orderby = "ORDER BY ";
        String consulta = Statements.GET_CATCIUDAD_CUSTOM;
        String errorLog = "";

        try {
            def jsonSlurper = new JsonSlurper();
            def objectoData = jsonSlurper.parseText(jsonData);
            errorLog = errorLog + " | " + jsonData;
            assert objectoData instanceof Map;
            if (objectoData.lstFiltro != null) {
                assert objectoData.lstFiltro instanceof List;
            }
            String objectoDataOrderby = objectoData.orderby;
            String objectoDataOrientation = objectoData.orientation;
            String objectoDataLimit = objectoData.limit;
            String objectoDataOffset = objectoData.offset;

            errorLog = errorLog + " | " + objectoDataOrderby;
            errorLog = errorLog + " | " + objectoDataOrientation;
            errorLog = errorLog + " | " + objectoDataLimit;
            errorLog = errorLog + " | " + objectoDataOffset;

            CatCiudadCustonFiltro row = new CatCiudadCustonFiltro();
            CatCampusCustom campus = new CatCampusCustom();
            List < CatCiudadCustonFiltro > rows = new ArrayList < CatCiudadCustonFiltro > ();
            closeCon = validarConexion();
            errorLog = errorLog + " | ";

            for (def filtro: objectoData.lstFiltro) {
                assert filtro instanceof Map;
                errorLog = errorLog + " | " + filtro.columna;
                switch (filtro.columna) {
                    case "Orden":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(COALESCE(C.orden, 0)::VARCHAR) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Clave":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(C.clave) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Descripción":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(C.descripcion) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "País":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(C.pais) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Estado":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(C.estado) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Fecha creación":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(C.fechacreacion) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Usuario creación":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(C.usuariocreacion) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Campus":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(B.descripcion) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                }
            }
            errorLog = errorLog + " | " + objectoDataOrderby;

            switch (objectoDataOrderby) {
                case "Orden":
                    orderby += "C.orden";
                    break;
                case "Clave":
                    orderby += "C.clave";
                    break;
                case "Descripción":
                    orderby += "C.descripcion";
                    break;
                case "País":
                    orderby += "C.pais";
                    break;
                case "Estado":
                    orderby += "C.estado";
                    break;
                case "Fecha creación":
                    orderby += "C.fechacreacion";
                    break;
                case "Usuario creación":
                    orderby += "C.usuariocreacion";
                    break;
                case "Campus":
                    orderby += "B.descripcion";
                    break;
                default:
                    orderby += "C.persistenceid"
                    break;
            }

            orderby += " " + objectoDataOrientation;
            //				orderby+=" ASC"
            errorLog = errorLog + " | " + orderby;

            consulta = consulta.replace("[WHERE]", where);

            errorLog = errorLog + " | consulta: " + consulta;
            errorLog = errorLog + " | consulta: " + consulta.replace("C.persistenceid, C.caseid, C.clave, C.descripcion, C.estado, C.fechacreacion, C.iseliminado, C.orden, C.pais, C.persistenceversion, C.region, C.usuariocreacion, C.campus_pid, C.isestado, B.persistenceid AS persistenceidB, B.clave AS claveB, B.descripcion AS descripcionB, B.fechacreacion AS fechacreacionB, B.fechaimplementacion AS fechaimplementacionB, B.id AS idB, B.iseliminado AS iseliminadoB, B.isenabled AS isenabledB, B.persistenceversion AS persistenceversionB, B.urlautordatos AS urlautordatosB, B.urlavisoprivacidad AS urlavisoprivacidadB, B.urldatosveridicos AS urldatosveridicosB, B.usuariobanner AS usuariobannerB, B.grupobonita AS grupobonitaB, B.orden AS ordenB, B.calle AS calleB, B.codigopostal AS codigopostalB, B.colonia AS coloniaB, B.municipio AS municipioB, B.numeroexterior AS numeroexteriorB, B.numerointerior AS numerointeriorB, B.estado_pid AS estado_pidB, B.pais_pid AS pais_pidB, B.email AS emailB, B.urlimagen AS urlimagenB", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "");
            pstm = con.prepareStatement(consulta.replace("C.persistenceid, C.caseid, C.clave, C.descripcion, C.estado, C.fechacreacion, C.iseliminado, C.orden, C.pais, C.persistenceversion, C.region, C.usuariocreacion, C.campus_pid, C.isestado, B.persistenceid AS persistenceidB, B.clave AS claveB, B.descripcion AS descripcionB, B.fechacreacion AS fechacreacionB, B.fechaimplementacion AS fechaimplementacionB, B.id AS idB, B.iseliminado AS iseliminadoB, B.isenabled AS isenabledB, B.persistenceversion AS persistenceversionB, B.urlautordatos AS urlautordatosB, B.urlavisoprivacidad AS urlavisoprivacidadB, B.urldatosveridicos AS urldatosveridicosB, B.usuariobanner AS usuariobannerB, B.grupobonita AS grupobonitaB, B.orden AS ordenB, B.calle AS calleB, B.codigopostal AS codigopostalB, B.colonia AS coloniaB, B.municipio AS municipioB, B.numeroexterior AS numeroexteriorB, B.numerointerior AS numerointeriorB, B.estado_pid AS estado_pidB, B.pais_pid AS pais_pidB, B.email AS emailB, B.urlimagen AS urlimagenB", "COUNT(C.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"));
            }
            consulta = consulta.replace("[ORDERBY]", orderby);
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
            errorLog = errorLog + " | consulta: " + consulta;
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, Integer.valueOf(objectoDataLimit));
            pstm.setInt(2, Integer.valueOf(objectoDataOffset));
            //pstm.setInt(1, 100);
            //pstm.setInt(2, 0);
            rs = pstm.executeQuery()
            //				
            while (rs.next()) {
                errorLog = errorLog + " | WHILE: ";
                row = new CatCiudadCustonFiltro();
                campus = new CatCampusCustom();

                row.setPersistenceId(rs.getLong("persistenceid") == null ? 0L : rs.getLong("persistenceid"));
                row.setPersistenceId_string(rs.getString("persistenceid") == null ? "0" : rs.getString("persistenceid"));
                row.setCaseId(rs.getString("caseid") == null ? "" : rs.getString("caseid"));
                row.setClave(rs.getString("clave") == null ? "" : rs.getString("clave"));
                row.setDescripcion(rs.getString("descripcion") == null ? "" : rs.getString("descripcion"));
                row.setEstado(rs.getString("estado") == null ? "" : rs.getString("estado"));
                row.setFechaCreacion(rs.getString("fechacreacion") == null ? "" : rs.getString("fechacreacion"));

                row.setOrden(rs.getLong("orden") == null ? 0L : rs.getLong("orden"));
                row.setPais(rs.getString("pais") == null ? "" : rs.getString("pais"));
                row.setPersistenceVersion(rs.getLong("persistenceversion") == null ? 0L : rs.getLong("persistenceversion"));
                row.setRegion(rs.getString("region") == null ? "" : rs.getString("region"));
                row.setUsuarioCreacion(rs.getString("usuariocreacion") == null ? "" : rs.getString("usuariocreacion"));
                row.setIsEstado(rs.getString("isestado") == null ? "" : rs.getString("isestado"));
                row.setIsEliminado(false);

                campus.setPersistenceId(rs.getLong("persistenceidB") == null ? 0L : rs.getLong("persistenceidB"));
                campus.setPersistenceId_string(rs.getString("persistenceidB") == null ? "0" : rs.getString("persistenceidB"));
                campus.setPersistenceVersion(rs.getLong("persistenceversionB") == null ? 0L : rs.getLong("persistenceversionB"));
                campus.setDescripcion(rs.getString("descripcionB") == null ? "" : rs.getString("descripcionB"));
                campus.setUsuarioBanner(rs.getString("usuariobannerB") == null ? "" : rs.getString("usuariobannerB"));
                campus.setClave(rs.getString("claveB") == null ? "" : rs.getString("claveB"));
                campus.setIsEnable(rs.getString("isenabledB") == null ? "" : rs.getString("isenabledB"));

                row.setCampus(campus);
                rows.add(row)
            }
            resultado.setSuccess(true)

            resultado.setData(rows)
            

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCodigoPostalRepetido(String jsonData, RestAPIContext context) {
        Result resultado = new Result();

        List < Boolean > lstResultado = new ArrayList < Boolean > ();

        Boolean closeCon = false;

        String errorLog = "";

        try {
            def jsonSlurper = new JsonSlurper();
            def objectoData = jsonSlurper.parseText(jsonData);
            errorLog = errorLog + " | " + jsonData;
            assert objectoData instanceof Map;

            String codigopostal = objectoData.codigopostal;
            String estado = objectoData.estado;
            String municipio = objectoData.municipio;
            String ciudad = objectoData.ciudad;
            String asentamiento = objectoData.asentamiento;
            String tipoasentamiento = objectoData.tipoasentamiento;

            errorLog = errorLog + " | codigopostal: " + codigopostal;
            errorLog = errorLog + " | estado: " + estado;
            errorLog = errorLog + " | municipio: " + municipio;
            errorLog = errorLog + " | ciudad: " + ciudad;
            errorLog = errorLog + " | asentamiento: " + asentamiento;
            errorLog = errorLog + " | tipoasentamiento: " + tipoasentamiento;

            closeCon = validarConexion();
            errorLog = errorLog + " | ";

            pstm = con.prepareStatement(Statements.GET_CODIGO_POSTAL_EXISTENTE);
            pstm.setString(1, codigopostal);
            pstm.setString(2, estado.toLowerCase());
            pstm.setString(3, municipio.toLowerCase());
            pstm.setString(4, ciudad.toLowerCase());
            pstm.setString(5, asentamiento.toLowerCase());
            pstm.setString(6, tipoasentamiento.toLowerCase());
            rs = pstm.executeQuery()
            if (rs.next()) {
                lstResultado.add(rs.getInt("resultado") > 0);
            }

            resultado.setSuccess(true)

            resultado.setData(lstResultado)
            

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatPropedeuticoGral(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;

        String where = "WHERE A.ISELIMINADO=false";
        String orderby = "ORDER BY ";
        String consulta = Statements.GET_CATPROPEDEUTICO_GRAL;
        String errorLog = "";

        try {
            def jsonSlurper = new JsonSlurper();
            def objectoData = jsonSlurper.parseText(jsonData);
            errorLog = errorLog + " | " + jsonData;
            assert objectoData instanceof Map;
            if (objectoData.lstFiltro != null) {
                assert objectoData.lstFiltro instanceof List;
            }
            String objectoDataOrderby = objectoData.orderby;
            String objectoDataOrientation = objectoData.orientation;
            String objectoDataLimit = objectoData.limit;
            String objectoDataOffset = objectoData.offset;

            errorLog = errorLog + " | " + objectoDataOrderby;
            errorLog = errorLog + " | " + objectoDataOrientation;
            errorLog = errorLog + " | " + objectoDataLimit;
            errorLog = errorLog + " | " + objectoDataOffset;

            CatPropedeuticoGralCustom row = new CatPropedeuticoGralCustom();
            CatCampusCustom campus = new CatCampusCustom();
            List < CatPropedeuticoGralCustom > rows = new ArrayList < CatPropedeuticoGralCustom > ();
            closeCon = validarConexion();
            errorLog = errorLog + " | ";

            for (def filtro: objectoData.lstFiltro) {
                assert filtro instanceof Map;
                errorLog = errorLog + " | " + filtro.columna;
                switch (filtro.columna) {
                    case "Clave":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(A.clave) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Descripción":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(A.descripcion) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Id":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(COALESCE(A.id, '0')::VARCHAR)  ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Campus":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(CONCAT(C.descripcion,' (',B.tipoperiodo,')')) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                }
            }
            errorLog = errorLog + " | " + objectoDataOrderby;

            switch (objectoDataOrderby) {
                case "Clave":
                    orderby += "A.clave";
                    break;
                case "Descripción":
                    orderby += "A.descripcion";
                    break;
                case "Id":
                    orderby += "A.id";
                    break;
            }

            orderby += " " + objectoDataOrientation;
            //				orderby+=" ASC"
            errorLog = errorLog + " | " + orderby;

            consulta = consulta.replace("[WHERE]", where);

            errorLog = errorLog + " | consulta: " + consulta;
            errorLog = errorLog + " | consulta: " + consulta.replace("A.persistenceid, A.clave, A.descripcion, A.id, A.persistenceversion, STRING_AGG(CONCAT(C.descripcion,' (',B.tipoperiodo,')'),', ') AS campus", "COUNT(A.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "").replace("GROUP BY A.persistenceid, A.clave, A.descripcion, A.id, A.persistenceversion", "");
            pstm = con.prepareStatement(consulta.replace("A.persistenceid, A.clave, A.descripcion, A.id, A.persistenceversion, STRING_AGG(CONCAT(C.descripcion,' (',B.tipoperiodo,')'),', ') AS campus", "COUNT(distinct A.persistenceid::VARCHAR) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "").replace("GROUP BY A.persistenceid, A.clave, A.descripcion, A.id, A.persistenceversion", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"));
            }
            consulta = consulta.replace("[ORDERBY]", orderby);
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
            errorLog = errorLog + " | consulta: " + consulta;
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, Integer.valueOf(objectoDataLimit));
            pstm.setInt(2, Integer.valueOf(objectoDataOffset));
            rs = pstm.executeQuery()

            while (rs.next()) {
                errorLog = errorLog + " | WHILE: ";
                row = new CatPropedeuticoGralCustom();
                row.setPersistenceId(rs.getLong("persistenceId"));
                row.setClave(rs.getString("clave"));
                row.setDescripcion(rs.getString("descripcion"));
                row.setId(rs.getString("id"));
                row.setPersistenceVersion(rs.getString("persistenceVersion"));
                row.setCampus(rs.getString("campus"));
                rows.add(row)
            }
            resultado.setSuccess(true)

            resultado.setData(rows)
            

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatPropedeuticoRelacionTipo(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;

        String where = "WHERE A.ISELIMINADO=false";
        String orderby = "ORDER BY ";
        String consulta = Statements.GET_CATPROPEDEUTICO_RELACION_TIPO;
        String errorLog = "";

        try {
            def jsonSlurper = new JsonSlurper();
            def objectoData = jsonSlurper.parseText(jsonData);
            errorLog = errorLog + " | " + jsonData;
            assert objectoData instanceof Map;
            if (objectoData.lstFiltro != null) {
                assert objectoData.lstFiltro instanceof List;
            }
            String objectoDataOrderby = objectoData.orderby;
            String objectoDataOrientation = objectoData.orientation;
            String objectoDataLimit = objectoData.limit;
            String objectoDataOffset = objectoData.offset;

            errorLog = errorLog + " | " + objectoDataOrderby;
            errorLog = errorLog + " | " + objectoDataOrientation;
            errorLog = errorLog + " | " + objectoDataLimit;
            errorLog = errorLog + " | " + objectoDataOffset;

            CatPropedeuticoFilterCustom row = new CatPropedeuticoFilterCustom();
            CatCampusCustom campus = new CatCampusCustom();
            List < CatPropedeuticoFilterCustom > rows = new ArrayList < CatPropedeuticoFilterCustom > ();
            closeCon = validarConexion();
            errorLog = errorLog + " | ";

            for (def filtro: objectoData.lstFiltro) {
                assert filtro instanceof Map;
                errorLog = errorLog + " | " + filtro.columna;
                switch (filtro.columna) {
                    case "Clave":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(C.clave) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Descripción":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(C.descripcion) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Campus":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(B.descripcion) ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Tipo Periodo":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(A.tipoperiodo)  ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Fecha inicio":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(D.fechainicio)  ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                    case "Fecha final":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(D.fechafinal)  ";
                        if (filtro.operador.equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.valor);
                        break;
                }
            }
            errorLog = errorLog + " | " + objectoDataOrderby;

            switch (objectoDataOrderby) {
                case "Clave":
                    orderby += "C.clave";
                    break;
                case "Descripción":
                    orderby += "C.descripcion";
                    break;
                case "Campus":
                    orderby += "B.descripcion";
                    break;
                case "Tipo Periodo":
                    orderby += "A.tipoperiodo";
                    break;
                case "Fecha inicio":
                    orderby += "D.fechainicio";
                    break;
                case "Fecha final":
                    orderby += "D.fechafinal";
                    break;
            }

            orderby += " " + objectoDataOrientation;
            //				orderby+=" ASC"
            errorLog = errorLog + " | " + orderby;

            consulta = consulta.replace("[WHERE]", where);

            errorLog = errorLog + " | consulta: " + consulta;
            errorLog = errorLog + " | consulta: " + consulta.replace("A.persistenceid, A.clave, A.descripcion, A.id, A.persistenceversion, STRING_AGG(CONCAT(C.descripcion,' (',B.tipoperiodo,')'),', ') AS campus", "COUNT(A.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "").replace("GROUP BY A.persistenceid, A.clave, A.descripcion, A.id, A.persistenceversion", "");
            pstm = con.prepareStatement(consulta.replace("C.clave, C.descripcion, D.usuariocreacion, A.persistenceid AS persistenceIdCatRecibirAtencionEspiritual, D.persistenceid, D.fechainicio, D.fechafinal, B.descripcion AS campus, A.tipoperiodo, D.idioma", "COUNT(distinct A.persistenceid::VARCHAR) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"));
            }
            consulta = consulta.replace("[ORDERBY]", orderby);
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
            errorLog = errorLog + " | consulta: " + consulta;
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, Integer.valueOf(objectoDataLimit));
            pstm.setInt(2, Integer.valueOf(objectoDataOffset));
            rs = pstm.executeQuery()

            while (rs.next()) {
                errorLog = errorLog + " | WHILE: ";
                row = new CatPropedeuticoFilterCustom();
                row.setClave(rs.getString("clave"));
                row.setDescripcion(rs.getString("descripcion"));
                row.setUsuarioCreacion(rs.getString("usuarioCreacion"));
                row.setPersistenceIdCatRecibirAtencionEspiritual(rs.getLong("persistenceIdCatRecibirAtencionEspiritual"));
                row.setPersistenceId(rs.getLong("persistenceId"));
                row.setFechaInicio(rs.getString("fechaInicio"));
                row.setFechaFinal(rs.getString("fechaFinal"));
                row.setCampus(rs.getString("campus"));
                row.setTipoPeriodo(rs.getString("tipoPeriodo"));
                row.setIdioma(rs.getString("idioma"))
                rows.add(row)
            }
            resultado.setSuccess(true)

            resultado.setData(rows)
            

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatPropedeuticoByPeriodo(String id, String grupobonita, String tipoperiodo, String idioma, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;

        String consulta = Statements.GET_CATPERIODO_BY_ID_NO_CAMPUS;
        String errorLog = "";
        String strFechaInicio = "";
        String strFechaFinal = "";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date fechaInicio = new Date();
        Date fechaFinal = new Date();

        CatPropedeuticoFinal objCatPropedeuticoFinal = new CatPropedeuticoFinal();

        List < CatPropedeuticoFinal > lstCatPropedeuticoFinal = new ArrayList < CatPropedeuticoFinal > ();
        try {

            closeCon = validarConexion();
            errorLog = errorLog + " | ";

            pstm = con.prepareStatement(consulta);
            pstm.setInt(1, Integer.valueOf(id));
            rs = pstm.executeQuery();
            if (rs.next()) {
                strFechaInicio = rs.getString("fechainicio");
                strFechaFinal = rs.getString("fechafin");
            }
            errorLog = errorLog + " | grupobonita: " + grupobonita
            errorLog = errorLog + " | tipoperiodo: " + tipoperiodo;
            errorLog = errorLog + " | strFechaInicio: " + strFechaInicio;
            errorLog = errorLog + " | strFechaFinal: " + strFechaFinal;

            if (strFechaInicio != null) {
                if (!strFechaInicio.equals("")) {
                    if (strFechaFinal != null) {
                        if (!strFechaFinal.equals("")) {
                            consulta = Statements.GET_CATPROPEDEUTICO_BY_FECHA;
                            strFechaFinal = strFechaFinal.substring(0, 11) + "23:59:59";
                            errorLog = errorLog + " | consulta: " + consulta
                            errorLog = errorLog + " | grupobonita: " + grupobonita
                            errorLog = errorLog + " | tipoperiodo: " + tipoperiodo;
                            errorLog = errorLog + " | strFechaInicio: " + strFechaInicio;
                            errorLog = errorLog + " | strFechaFinal: " + strFechaFinal;

                            if (idioma.equals(null) || idioma.equals(" ") || idioma.equals("") || idioma.equals("null")) {
                                idioma = "Español";
                            }
                            pstm = con.prepareStatement(consulta);
                            pstm.setString(1, grupobonita);
                            pstm.setString(2, tipoperiodo);
                            pstm.setString(3, strFechaFinal);
                            pstm.setString(4, idioma)
                            rs = pstm.executeQuery();
                            while (rs.next()) {
                                objCatPropedeuticoFinal = new CatPropedeuticoFinal();
                                objCatPropedeuticoFinal.setPersistenceId(rs.getLong("persistenceid"));
                                objCatPropedeuticoFinal.setPersistenceId_string(rs.getString("persistenceid"));
                                objCatPropedeuticoFinal.setPersistenceVersion(rs.getLong("persistenceversion"));
                                objCatPropedeuticoFinal.setPersistenceVersion_string(rs.getString("persistenceversion"));
                                objCatPropedeuticoFinal.setClave(rs.getString("clave"));
                                objCatPropedeuticoFinal.setDescripcion(rs.getString("descripcion"));
                                objCatPropedeuticoFinal.setFechaCreacion(rs.getString("fechacreacion"));
                                objCatPropedeuticoFinal.setUsuarioCreacion(rs.getString("usuariocreacion"));
                                objCatPropedeuticoFinal.setIsEliminado(rs.getBoolean("iseliminado"));
                                objCatPropedeuticoFinal.setFechaEjecucion(rs.getString("fechaejecucion"));
                                objCatPropedeuticoFinal.setFechaInicio(rs.getString("fechainicio"));
                                objCatPropedeuticoFinal.setFechaFinal(rs.getString("fechafinal"));
                                objCatPropedeuticoFinal.setNombreCampus(rs.getString("nombrecampus"));
                                objCatPropedeuticoFinal.setPersistenceIdCatRecibirAtencionEspiritual(rs.getLong("persistenceidcatrecibiratencionespiritual"));
                                objCatPropedeuticoFinal.setPersistenceIdCatRecibirAtencionEspiritual_string(rs.getString("persistenceidcatrecibiratencionespiritual"));
                                lstCatPropedeuticoFinal.add(objCatPropedeuticoFinal);
                            }
                            errorLog = errorLog + " | paso el while";
                        } else {
                            errorLog = errorLog + " | if(!strFechaFinal.equals()){";
                        }
                    } else {
                        errorLog = errorLog + " | if(strFechaFinal != null){";
                    }
                } else {
                    errorLog = errorLog + " | if(!strFechaInicio.equals()){";
                }
            } else {
                errorLog = errorLog + " | if(strFechaInicio != null){";
            }
            resultado.setData(lstCatPropedeuticoFinal);
            resultado.setSuccess(true);
            

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatPeriodoActivo(String tipo, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;

        String consulta = "";
        String errorLog = "";
        String tipoCast = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date fechaInicio = new Date();
        Date fechaFinal = new Date();

        CatPeriodoFinal objCatPeriodoFinal = new CatPeriodoFinal();

        List < CatPeriodoFinal > lstCatPeriodoFinal = new ArrayList < CatPeriodoFinal > ();
        try {

            closeCon = validarConexion();
            errorLog = errorLog + " | ";

            errorLog = errorLog + " | tipo: " + tipo
            if (tipo.equals("Semestral")) {
                tipoCast = "issemestral";
            }
            if (tipo.equals("Cuatrimestral")) {
                tipoCast = "iscuatrimestral";
            }
            if (tipo.equals("Anual")) {
                tipoCast = "isanual";
            }
            errorLog = errorLog + " | tipoCast: " + tipoCast

            consulta = Statements.GET_PERIODO_ACTIVO_FINAL.replace("[TIPO]", tipoCast);
            errorLog = errorLog + " | consulta: " + consulta

            pstm = con.prepareStatement(consulta);
            rs = pstm.executeQuery();
            while (rs.next()) {
                objCatPeriodoFinal = new CatPeriodoFinal();
                objCatPeriodoFinal.setPersistenceId(rs.getLong("persistenceId"));
                objCatPeriodoFinal.setPersistenceId_string(rs.getString("persistenceId"));
                objCatPeriodoFinal.setPersistenceVersion(rs.getLong("persistenceVersion"));
                objCatPeriodoFinal.setPersistenceVersion_string(rs.getString("persistenceVersion"));
                objCatPeriodoFinal.setDescripcion(rs.getString("descripcion"));
                objCatPeriodoFinal.setFechaCreacion(rs.getString("fechaCreacion"));
                objCatPeriodoFinal.setIsEliminado(rs.getBoolean("isEliminado"));
                objCatPeriodoFinal.setUsuarioBanner(rs.getString("usuarioBanner"));
                objCatPeriodoFinal.setFechaImportacion(rs.getString("fechaImportacion"));
                objCatPeriodoFinal.setClave(rs.getString("clave"));
                objCatPeriodoFinal.setIsEnabled(rs.getBoolean("isEnabled"));
                //objCatPeriodoFinal.setNombreCampus(rs.getString("nombreCampus"));
                objCatPeriodoFinal.setIsCuatrimestral(rs.getBoolean("isCuatrimestral"));
                objCatPeriodoFinal.setFechaInicio(rs.getString("fechaInicio"));
                objCatPeriodoFinal.setFechaFin(rs.getString("fechaFin"));
                objCatPeriodoFinal.setIsSemestral(rs.getBoolean("isSemestral"));
                objCatPeriodoFinal.setIsAnual(rs.getBoolean("isAnual"));
                objCatPeriodoFinal.setIsPropedeutico(rs.getString("isPropedeutico"));
                objCatPeriodoFinal.setId(rs.getString("id"));
                objCatPeriodoFinal.setActivo(rs.getBoolean("activo"));
                lstCatPeriodoFinal.add(objCatPeriodoFinal);
            }
            errorLog = errorLog + " | paso el while";

            resultado.setData(lstCatPeriodoFinal);
            resultado.setSuccess(true);
            

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }
	
	public Result getCatPeriodoActivoFechaEspecifica(String fecha, String tipo, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;

		String consulta = "";
		String errorLog = "";
		String tipoCast = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date fechaInicio = new Date();
		Date fechaFinal = new Date();

		CatPeriodoFinal objCatPeriodoFinal = new CatPeriodoFinal();

		List < CatPeriodoFinal > lstCatPeriodoFinal = new ArrayList < CatPeriodoFinal > ();
		try {

			closeCon = validarConexion();
			errorLog = errorLog + " | ";

			errorLog = errorLog + " | tipo: " + tipo
			if (tipo.equals("Semestral")) {
				tipoCast = "issemestral";
			}
			if (tipo.equals("Cuatrimestral")) {
				tipoCast = "iscuatrimestral";
			}
			if (tipo.equals("Anual")) {
				tipoCast = "isanual";
			}
			errorLog = errorLog + " | tipoCast: " + tipoCast

			consulta = Statements.GET_PERIODO_ACTIVO_FECHA_ESPECIFICA.replace("[TIPO]", tipoCast).replace("[FECHA]", fecha);
			errorLog = errorLog + " | consulta: " + consulta

			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
			while (rs.next()) {
				objCatPeriodoFinal = new CatPeriodoFinal();
				objCatPeriodoFinal.setPersistenceId(rs.getLong("persistenceId"));
				objCatPeriodoFinal.setPersistenceId_string(rs.getString("persistenceId"));
				objCatPeriodoFinal.setPersistenceVersion(rs.getLong("persistenceVersion"));
				objCatPeriodoFinal.setPersistenceVersion_string(rs.getString("persistenceVersion"));
				objCatPeriodoFinal.setDescripcion(rs.getString("descripcion"));
				objCatPeriodoFinal.setFechaCreacion(rs.getString("fechaCreacion"));
				objCatPeriodoFinal.setIsEliminado(rs.getBoolean("isEliminado"));
				objCatPeriodoFinal.setUsuarioBanner(rs.getString("usuarioBanner"));
				objCatPeriodoFinal.setFechaImportacion(rs.getString("fechaImportacion"));
				objCatPeriodoFinal.setClave(rs.getString("clave"));
				objCatPeriodoFinal.setIsEnabled(rs.getBoolean("isEnabled"));
				//objCatPeriodoFinal.setNombreCampus(rs.getString("nombreCampus"));
				objCatPeriodoFinal.setIsCuatrimestral(rs.getBoolean("isCuatrimestral"));
				objCatPeriodoFinal.setFechaInicio(rs.getString("fechaInicio"));
				objCatPeriodoFinal.setFechaFin(rs.getString("fechaFin"));
				objCatPeriodoFinal.setIsSemestral(rs.getBoolean("isSemestral"));
				objCatPeriodoFinal.setIsAnual(rs.getBoolean("isAnual"));
				objCatPeriodoFinal.setIsPropedeutico(rs.getString("isPropedeutico"));
				objCatPeriodoFinal.setId(rs.getString("id"));
				objCatPeriodoFinal.setActivo(rs.getBoolean("activo"));
				lstCatPeriodoFinal.add(objCatPeriodoFinal);
			}
			errorLog = errorLog + " | paso el while";

			resultado.setData(lstCatPeriodoFinal);
			resultado.setSuccess(true);
			

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}

    public Result getCatCiudadExcel(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
            String type = object.type;
            String orden = ""
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(type);
            CellStyle style = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            Date fecha = new Date();

            if (!type.equals("")) {

                dataResult = getCatCiudad(jsonData, context);

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos en " + type);
                }

                Row titleRow = sheet.createRow(++rowCount);
                Cell cellReporte = titleRow.createCell(1);
                cellReporte.setCellValue("Reporte:");
                cellReporte.setCellStyle(style);
                Cell cellTitle = titleRow.createCell(2);
                cellTitle.setCellValue("LISTADO DE " + type);
                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat dfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat dfSalida = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                String sDate = formatter.format(date);
                Cell cellFechaData = titleRow.createCell(5);
                cellFechaData.setCellValue(sDate);

                Row blank = sheet.createRow(++rowCount);
                Cell cellusuario = blank.createCell(4);
                cellusuario.setCellValue("Usuario:");
                cellusuario.setCellStyle(style);
                Cell cellusuarioData = blank.createCell(5);
                cellusuarioData.setCellValue(object.usuarioNombre);

                Row headersRow = sheet.createRow(++rowCount);

                int count = 0;
                if (object.isOrden) {
                    Cell header0 = headersRow.createCell(count);
                    header0.setCellValue("Orden");
                    header0.setCellStyle(style);
                    count++;
                }
                Cell header1 = headersRow.createCell(count);
                header1.setCellValue("Clave");
                header1.setCellStyle(style);
                count++;

                Cell header2 = headersRow.createCell(count);
                header2.setCellValue("Descripción");
                header2.setCellStyle(style);
                count++;

                Cell header3 = headersRow.createCell(count);
                header3.setCellValue("País");
                header3.setCellStyle(style);
                count++;

                Cell header4 = headersRow.createCell(count);
                header4.setCellValue("Estado");
                header4.setCellStyle(style);
                count++;

                Cell header5 = headersRow.createCell(count);
                header5.setCellValue("Fecha creación");
                header5.setCellStyle(style);
                count++;

                Cell header6 = headersRow.createCell(count);
                header6.setCellValue("Usuario creación");
                header6.setCellStyle(style);
                count++;

                headersRow.setRowStyle(style);

                CatCiudadCustonFiltro objCatCiudadCustonFiltro = new CatCiudadCustonFiltro();

                for (int i = 0; i < lstParams.size(); ++i) {
                    objCatCiudadCustonFiltro = new CatCiudadCustonFiltro();
                    objCatCiudadCustonFiltro = (CatCiudadCustonFiltro) lstParams.get(i);

                    Row row = sheet.createRow(++rowCount);
                    int count2 = 0;
                    if (object.isOrden) {
                        Cell cell1 = row.createCell(count2);
                        cell1.setCellValue(objCatCiudadCustonFiltro.getOrden());
                        count2++;
                    }

                    Cell cell2 = row.createCell(count2);
                    cell2.setCellValue(objCatCiudadCustonFiltro.getClave());
                    count2++;

                    Cell cell3 = row.createCell(count2);
                    cell3.setCellValue(objCatCiudadCustonFiltro.getDescripcion());
                    count2++;

                    Cell cell4 = row.createCell(count2);
                    cell4.setCellValue(objCatCiudadCustonFiltro.getPais());
                    count2++;

                    Cell cell5 = row.createCell(count2);
                    cell5.setCellValue(objCatCiudadCustonFiltro.getEstado());
                    count2++;

                    Cell cell6 = row.createCell(count2);
                    if (objCatCiudadCustonFiltro.getFechaCreacion() != null) {
                        fecha = dfEntrada.parse(objCatCiudadCustonFiltro.getFechaCreacion().toString());
                        cell6.setCellValue(dfSalida.format(fecha));
                    } else {
                        cell6.setCellValue("");
                    }
                    count2++;

                    Cell cell7 = row.createCell(count2);
                    cell7.setCellValue(objCatCiudadCustonFiltro.getEstado());
                }
            }

            for (int i = 0; i <= rowCount + 3; ++i) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream outputStream = new FileOutputStream("ReportGenericos.xls");
            workbook.write(outputStream);

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportGenericos.xls"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }

    public Result getCatCiudadPdf(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
            String type = object.type;

            String orden = "";
            def documento = "ReportGenericos.pdf"
            DocumentItext document = new DocumentItext();
            document.setPageSize(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(documento));
            float fontSize = 8.5 f;
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, fontSize, BaseColor.BLACK);
            String phraseToInput = "";
            Date fecha = new Date();
            if (!type.equals("")) {
                dataResult = getCatCiudad(jsonData, context);
                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos de " + type);
                }
                document.open();
                Paragraph preface = new Paragraph("LISTADO DE " + type);
                preface.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(preface);
                document.add(new Paragraph(" "));

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat dfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat dfSalida = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String sDate = formatter.format(date);

                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("FECHA: " + sDate);
                p.add(new Chunk(glue));
                p.add("USUARIO: " + object.usuarioNombre);
                document.add(p);

                document.add(new Paragraph(" "))

                PdfPTable table = new PdfPTable((object.isOrden ? 7 : 4));
                table.setWidthPercentage(100f);

                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);


                if (object.isOrden) {
                    header.setPhrase(new Phrase("Orden", normalFont));
                    table.addCell(header);
                }

                header.setPhrase(new Phrase("Clave", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("Descripción", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("País", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("Estado", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("Fecha creación", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("Usuario creación", normalFont));
                table.addCell(header);

                CatCiudadCustonFiltro objCatCiudadCustonFiltro = new CatCiudadCustonFiltro();

                for (int i = 0; i < lstParams.size(); ++i) {
                    objCatCiudadCustonFiltro = new CatCiudadCustonFiltro();
                    objCatCiudadCustonFiltro = (CatCiudadCustonFiltro) lstParams.get(i);

                    if (object.isOrden) {
                        table.addCell(new Phrase(objCatCiudadCustonFiltro.getOrden().toString(), normalFont));
                    }

                    table.addCell(new Phrase(objCatCiudadCustonFiltro.getClave(), normalFont));
                    table.addCell(new Phrase(objCatCiudadCustonFiltro.getDescripcion(), normalFont));
                    table.addCell(new Phrase(objCatCiudadCustonFiltro.getPais(), normalFont));
                    table.addCell(new Phrase(objCatCiudadCustonFiltro.getEstado(), normalFont));
                    if (objCatCiudadCustonFiltro.getFechaCreacion() != null) {
                        fecha = dfEntrada.parse(objCatCiudadCustonFiltro.getFechaCreacion().toString());
                        table.addCell(new Phrase(dfSalida.format(fecha), normalFont));
                    } else {
                        table.addCell(new Phrase("", normalFont));
                    }

                    table.addCell(new Phrase(objCatCiudadCustonFiltro.getEstado(), normalFont));
                }

                document.add(table);
                document.close();
            }

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportGenericos.pdf"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }

    public Result getPropedeuticosNoFecha(RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String errorlog = "", campus = "";
        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();

        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();
        try {
            closeCon = validarConexion();
            def jsonSlurper = new JsonSlurper();
            //def object = jsonSlurper.parseText(jsonData);
            def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)

            userLogged = context.getApiSession().getUserId();

            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getGrupoBonita());
                        break;
                    }
                }
            }

            for (Integer i = 0; i < lstGrupo.size(); i++) {
                String campusMiembro = lstGrupo.get(i);
                campus += "'" + campusMiembro + "'"
                if (i == (lstGrupo.size() - 1)) {
                    campus += ") "
                } else {
                    campus += " , "
                }
            }
            errorlog += campus

            String consulta = Statements.GET_PROPEDEUTICOS_NO_FECHA;
            consulta = consulta.replace("[CAMPUS]", campus);
            errorlog += consulta
            pstm = con.prepareStatement(consulta)
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
            

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            
            //resultado.setError_info(consulta)
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getCatPaisExcel(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
            String type = object.type;
            String orden = ""
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(type);
            CellStyle style = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);

            if (!type.equals("")) {

                dataResult = getCatPais(jsonData, context);
                //dataResult = getCatGenerico(jsonData, context);

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos en " + type);
                }

                Row titleRow = sheet.createRow(++rowCount);
                Cell cellReporte = titleRow.createCell(1);
                cellReporte.setCellValue("Reporte:");
                cellReporte.setCellStyle(style);
                Cell cellTitle = titleRow.createCell(2);
                cellTitle.setCellValue("LISTADO DE " + type);
                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                String sDate = formatter.format(date);
                Cell cellFechaData = titleRow.createCell(5);
                cellFechaData.setCellValue(sDate);

                Row blank = sheet.createRow(++rowCount);
                Cell cellusuario = blank.createCell(4);
                cellusuario.setCellValue("Usuario:");
                cellusuario.setCellStyle(style);
                Cell cellusuarioData = blank.createCell(5);
                cellusuarioData.setCellValue(object.usuarioNombre);

                Row headersRow = sheet.createRow(++rowCount);

                int count = 0;
                if (object.isOrden) {
                    Cell header0 = headersRow.createCell(count);
                    header0.setCellValue("Orden");
                    header0.setCellStyle(style);
                    count++;
                }
                Cell header1 = headersRow.createCell(count);
                header1.setCellValue("Clave");
                header1.setCellStyle(style);
                count++;

                Cell header2 = headersRow.createCell(count);
                header2.setCellValue("Descripción");
                header2.setCellStyle(style);
                count++;

                Cell header3 = headersRow.createCell(count);
                header3.setCellValue("Usuario creación");
                header3.setCellStyle(style);
                count++;

                Cell header4 = headersRow.createCell(count);
                header4.setCellValue("Fecha creación");
                header4.setCellStyle(style);
                count++;

                headersRow.setRowStyle(style);

                CatPaisCustomFiltro objCatPaisCustomFiltro = new CatPaisCustomFiltro();

                for (int i = 0; i < lstParams.size(); ++i) {
                    objCatPaisCustomFiltro = new CatPaisCustomFiltro();
                    objCatPaisCustomFiltro = (CatPaisCustomFiltro) lstParams.get(i);

                    Row row = sheet.createRow(++rowCount);
                    int count2 = 0;
                    if (object.isOrden) {
                        Cell cell1 = row.createCell(count2);
                        cell1.setCellValue(objCatPaisCustomFiltro.getOrden());
                        count2++;
                    }

                    Cell cell2 = row.createCell(count2);
                    cell2.setCellValue(objCatPaisCustomFiltro.getClave());
                    count2++;

                    Cell cell3 = row.createCell(count2);
                    cell3.setCellValue(objCatPaisCustomFiltro.getDescripcion());
                    count2++;

                    Cell cell4 = row.createCell(count2);
                    cell4.setCellValue(objCatPaisCustomFiltro.getUsuarioCreacion());
                    count2++;

                    Cell cell5 = row.createCell(count2);
                    cell5.setCellValue(objCatPaisCustomFiltro.getFechaCreacion().toString()[0..9]);
                    count2++;
                }
            }

            for (int i = 0; i <= rowCount + 3; ++i) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream outputStream = new FileOutputStream("ReportGenericos.xls");
            workbook.write(outputStream);

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportGenericos.xls"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);

        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }

    private String encodeFileToBase64Binary(String fileName)
    throws IOException {

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
        while (offset < bytes.length &&
            (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }
	
	public Result getEstadosPreparatorias() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Map<String, Object> lstEstadosPreparatorias = new LinkedHashMap<String, Object>();
		List<?> rows = new ArrayList<?>();
	
		try {
			closeCon = validarConexion();
	
			pstm = con.prepareStatement(Statements.GET_ESTADOS_PREPARATORIA);	
			rs = pstm.executeQuery();
			while (rs.next()) {
				lstEstadosPreparatorias = new LinkedHashMap<String, Object>();
				lstEstadosPreparatorias.put("estado", rs.getString("estado"));
	
				rows.add(lstEstadosPreparatorias);
			}
	
			
			resultado.setData(rows);
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setError_info(errorLog+" - error: "+e.getMessage());
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getCatDescuentosByInfoAspirante(String campus, String idbachillerato, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		
		try {
			String consulta = Statements.GET_CATDESCUENTOS_BY_CAMPUS_AND_BACHILLERATO;
			CatDescuentosCustom row = new CatDescuentosCustom();
			List < CatDescuentosCustom > rows = new ArrayList < CatDescuentosCustom > ();

			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, Long.valueOf(idbachillerato));
			pstm.setString(2, campus);

			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new CatDescuentosCustom();
				row.setCampus(campus);
				row.setDescuento(rs.getInt("descuento"));
				try {
					row.setCatBachilleratos(new CatBachilleratos())
					row.getCatBachilleratos().setDescripcion(rs.getString("bachillerato"))
					row.getCatBachilleratos().setPersistenceId(Long.valueOf(idbachillerato))
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					errorLog += e.getMessage()
				}

				rows.add(row)
			}

			resultado.setSuccess(true)
			resultado.setError(errorLog)
			resultado.setData(rows)

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
    /***********************ARTURO ZAMORANO0 FIN**************************/
}