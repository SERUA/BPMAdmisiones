package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.search.Order
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Transferencias
import com.anahuac.rest.api.Entity.db.CatBitacoraCorreo
import com.anahuac.rest.api.Utilities.FileDownload
import com.anahuac.rest.api.Utilities.LoadParametros
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class TransferenciasDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferenciasDAO.class);
    Connection con;
    Statement stm;
    ResultSet rs;
    PreparedStatement pstm;

    public Result getUsuariosTransferencia(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
		
        String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = ""
        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
        List < DetalleSolicitud > lstDetalleSolicitud = new ArrayList < DetalleSolicitud > ();

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

            assert object instanceof Map;
            where += " WHERE sda.iseliminado=false "
            where += " AND (sda.ESTATUSSOLICITUD <> 'estatus1' AND sda.ESTATUSSOLICITUD <> 'estatus2' AND sda.ESTATUSSOLICITUD <> 'estatus3') AND (sda.ESTATUSSOLICITUD != 'Solicitud vencida') AND (sda.ESTATUSSOLICITUD != 'Periodo vencido') AND (sda.ESTATUSSOLICITUD != 'Solicitud caduca') AND (sda.ESTATUSSOLICITUD not like '%Solicitud vencida en:%') AND (sda.ESTATUSSOLICITUD not like '%Período vencido en:%')"

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
			
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs= pstm.executeQuery();
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			
            errorlog += "object.lstFiltro" + object.lstFiltro
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            //closeCon = validarConexion();
            String consulta = Statements.GET_SOLICITUDES_TRANSFERENCIA
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                errorlog += ", columna " + filtro.get("columna")
                switch (filtro.get("columna")) {

					case "IDBANNER,NOMBRE,EMAIL":
					errorlog+="IDBANNER,NOMBRE,EMAIL"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(da.idbanner) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				break;
				case "CAMPUS,PROGRAMA,PERÍODO":
					errorlog+="CAMPUS,PROGRAMA,PERÍODO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				
				break;
				case "PREPARATORIA,PROCEDENCIA,PROMEDIO":
					errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					/*
					where +="  OR LOWER(sda.estadoextranjero) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					*/
					where +="  OR LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				break;
				case "ESTATUS":
					errorlog+="ESTATUS,TIPO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				break;
				case "RESIDENCIA,TIPO ALUMNO,TIPO ADMISION":
					errorlog+="ESTATUS,TIPO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(tipoalumno.descripcion) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +="  OR LOWER(tipoadmision.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(residencia.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				break;
                    case "NOMBRE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) ";
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
                        where += " LOWER(sda.correoelectronico) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CURP":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.curp) ";
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
                        where += " LOWER(campus.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PREPARATORIA":
                        bachillerato += " AND LOWER(prepa.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            bachillerato += "=LOWER('[valor]')"
                        } else {
                            bachillerato += "LIKE LOWER('%[valor]%')"
                        }
                        bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROGRAMA":
                        programa += " AND LOWER(gestionescolar.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            programa += "=LOWER('[valor]')"
                        } else {
                            programa += "LIKE LOWER('%[valor]%')"
                        }
                        programa = programa.replace("[valor]", filtro.get("valor"))
                        break;
                    case "INGRESO":
                        ingreso += " AND LOWER(periodo.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            ingreso += "=LOWER('[valor]')"
                        } else {
                            ingreso += "LIKE LOWER('%[valor]%')"
                        }
                        ingreso = ingreso.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROCEDENCIA":
                        estado += " AND LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            estado += "=LOWER('[valor]')"
                        } else {
                            estado += "LIKE LOWER('%[valor]%')"
                        }
                        estado = estado.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROMEDIO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.PROMEDIOGENERAL) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTATUS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.ESTATUSSOLICITUD) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TELEFONO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.telefonocelular) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TIPO":
                        tipoalumno += " AND LOWER(residencia.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "IDBANNER":
                        tipoalumno += " AND LOWER(da.idbanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "LISTAROJA":
                        tipoalumno += " AND LOWER(da.observacionesListaRoja) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "RECHAZO":
                        tipoalumno += " AND LOWER(da.observacionesRechazo) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    default:
                        break;
                }
            }
            switch (object.orderby) {
                case "NOMBRE":
                    orderby += "sda.apellidopaterno";
                    break;
                case "EMAIL":
                    orderby += "sda.correoelectronico";
                    break;
                case "CURP":
                    orderby += "sda.curp";
                    break;
                case "CAMPUS":
                    orderby += "campusEstudio.descripcion"
                    break;
                case "PREPARATORIA":
                    orderby += "prepa.DESCRIPCION"
                    break;
                case "PROGRAMA":
                    orderby += "gestionescolar.DESCRIPCION"
                    break;
                case "INGRESO":
                    orderby += "periodo.DESCRIPCION"
                    break;
                case "PROCEDENCIA":
                    orderby += "procedencia";
                    break;
                case "PROMEDIO":
                    orderby += "sda.PROMEDIOGENERAL";
                    break;
                case "ESTATUS":
                    orderby += "sda.ESTATUSSOLICITUD";
                    break;
                case "TIPO":
                    orderby += "da.TIPOALUMNO";
                    break;
                case "TELEFONO":
                    orderby += "sda.telefonocelular";
                    break;
                case "IDBANNER":
                    orderby += "da.idbanner";
                    break;
                case "LISTAROJA":
                    orderby += "da.observacionesListaRoja";
                    break;
                case "RECHAZO":
                    orderby += "da.observacionesRechazo";
                    break;
                default:
                    orderby += "sda.persistenceid"
                    break;
            }
            orderby += " " + object.orientation;
            consulta = consulta.replace("[CAMPUS]", campus)
            consulta = consulta.replace("[PROGRAMA]", programa)
            consulta = consulta.replace("[INGRESO]", ingreso)
            consulta = consulta.replace("[ESTADO]", estado)
            consulta = consulta.replace("[BACHILLERATO]", bachillerato)
            consulta = consulta.replace("[TIPOALUMNO]", tipoalumno)
            where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno
            consulta = consulta.replace("[WHERE]", where);
			
            String consultaCount = Statements.GET_COUNT_SOLICITUDES_TRANSFERENCIA;
            consultaCount = consultaCount.replace("[CAMPUS]", campus)
            consultaCount = consultaCount.replace("[PROGRAMA]", programa)
            consultaCount = consultaCount.replace("[INGRESO]", ingreso)
            consultaCount = consultaCount.replace("[ESTADO]", estado)
            consultaCount = consultaCount.replace("[BACHILLERATO]", bachillerato)
            consultaCount = consultaCount.replace("[TIPOALUMNO]", tipoalumno)
            consultaCount = consultaCount.replace("[WHERE]", where)
            consultaCount = consultaCount.replace("[LIMITOFFSET]", "");
            consultaCount = consultaCount.replace("[ORDERBY]", "");
            pstm = con.prepareStatement(consultaCount);
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog += " ///////*/*/*/*/*/ la consulta es: " + consulta
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            rs = pstm.executeQuery()
            rows = new ArrayList < Map < String, Object >> ();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Map < String, Object > columns = new LinkedHashMap < String, Object > ();

                for (int i = 1; i <= columnCount; i++) {
                    columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
                    if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
                        String encoded = "";
                        try {
                            String urlFoto = rs.getString("urlfoto");
							if(urlFoto != null && !urlFoto.isEmpty()) {
								columns.put("fotografiab64", base64Imagen((rs.getString("urlfoto") + SSA)) );
								//columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
							}else {
								List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
								for(Document doc : doc1) {
									encoded = "../API/formsDocumentImage?document="+doc.getId();
									columns.put("fotografiab64", encoded);
								}
							}

                        } catch (Exception e) {
                            columns.put("fotografiab64", "");
                            errorlog += "" + e.getMessage();
                        }
                    }
                }

                rows.add(columns);
            }
            resultado.setSuccess(true)

            
            resultado.setData(rows)

        } catch (Exception e) {
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result transferirAspirante(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        String errorLog = "";
        Boolean closeCon = false;
		Boolean isCommit = false;
        try {
            Transferencias transferencia = new Transferencias();
            ProcessAPI processAPI = context.getApiClient().getProcessAPI()
            Boolean avanzartarea = false;
            String username = "";
            String password = "";
			
			closeCon = validarConexion();
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            assert object instanceof Map;

            org.bonitasoft.engine.api.APIClient apiClient = new APIClient() //context.getApiClient();
            apiClient.login(username, password)

            SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
            searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, object.caseid);
            searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
            final SearchOptions searchOptions = searchBuilder.done();
            SearchResult < HumanTaskInstance > SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
            List < HumanTaskInstance > lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();

            if (!object.campusAnterior.equals(object.campusNuevo)) {
                for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
					if (objHumanTaskInstance.getName().equals("Pase de lista Prueba 1") || objHumanTaskInstance.getName().equals("Pase de lista Prueba 2") || objHumanTaskInstance.getName().equals("Pase de lista Prueba 3")) {
                        Map < String, Serializable > inputs = new HashMap < String, Serializable > ();
                        if (objHumanTaskInstance.getName().equals("Carga y consulta de resultados")) {
                            inputs.put("isTransferencia", true);
                        } else if (objHumanTaskInstance.getName().equals("Resultado final de comité")) {
                            inputs.put("isTransferencia", true);
                        }

                        processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
                        processAPI.executeUserTask(objHumanTaskInstance.getId(), inputs);
                    }
                }
            }
			
			pstm = con.prepareStatement(Statements.SELECT_PAGO_CAMPUS_TRASNFERENCIA)
			pstm.setString(1, object.catCampus.grupoBonita)
			rs= pstm.executeQuery()
			String pagoid = "";
			if(rs.next()) {
				pagoid = rs.getString("persistenceid")
			}
			
            con.setAutoCommit(false)
			isCommit = true;
            pstm = con.prepareStatement(Statements.UPDATE_DATOS_TRASNFERENCIA)
            pstm.setLong(1, object.campus);
            pstm.setLong(2, object.licenciatura);
            if (object.propedeutico == null) {
                pstm.setNull(3, java.sql.Types.BIGINT);
            } else {
                pstm.setLong(3, object.propedeutico);
            }
            pstm.setLong(4, object.periodo);
            pstm.setLong(5, object.campusestudio);
			pstm.setString(6, object.estatus)
            pstm.setLong(7, Long.valueOf(object.caseid));
            pstm.executeUpdate();
			
			if(pagoid.length() > 0){
				String statement = Statements.UPDATE_DATOS_TRASNFERENCIA_PAGO
				
				if(object?.estatus?.toString().contains("pago") ) {
					statement = Statements.UPDATE_DATOS_TRASNFERENCIA_PAGO2
				}
				
				pstm = con.prepareStatement(statement)
				pstm.setLong(1, Long.valueOf(pagoid));
				pstm.setString(2, object.caseid);
				pstm.executeUpdate();
				
			}

            con.commit();
            errorLog += " DESPUES del update "
            errorLog += " insertare en la bitacora "
            transferencia.setAspirante(object.aspirante);
            transferencia.setCorreoAspirante(object.correoaspirante);
            transferencia.setValorOriginal(object.valororginal.toString());
            transferencia.setValorCambio(object.valorcambio.toString());
            transferencia.setUsuarioCreacion(object.usuario);
            transferencia.setCampusAnterior(object.campusAnterior);
            transferencia.setCampusNuevo(object.campusNuevo);
			transferencia.setCaseid(Long.valueOf(object.caseid));
			transferencia.setEstatus(object.estatus);
			transferencia.setLicenciatura(object.licenciaturatext);
			transferencia.setPeriodo(object.periodotext);
			transferencia.setIdbanner(object.idbanner);
           
            Result resultadoinsert = insertCatBitacoraTransferencia(transferencia);
            errorLog += " el error en el insert es: " + resultadoinsert.getError_info();
            if (resultadoinsert.isSuccess()) {
                errorLog += " inserte en la bitacora " + resultadoinsert.isSuccess().toString();
            } else {
                errorLog += " NO inserte en la bitacora "
                resultadoinsert.isSuccess().toString();
            }
			
			Result resultadoSesion = new SesionesDAO().eliminarSesionAspirante(object.correoaspirante, context)
			errorLog += " el error en el eliminar es : " + resultadoSesion.getError();
			if (resultadoSesion.isSuccess()) {
				errorLog += " se elimino al aspirante de la sesion" + resultadoinsert.isSuccess().toString();
			} else {
				errorLog += " no elimino al aspirante de la sesion "+ resultadoSesion.isSuccess().toString();
			}
			Result rHdao= new Result()
			rHdao = new HubspotDAO().createOrUpdateTransferirAspirante(object.valorcambio, object.valororginal, object.correoaspirante, context)
            resultado.setSuccess(true)
            resultado.setError_info(errorLog+ " || rHdao.getError_info()");
        } catch (Exception ex) {
            
            resultado.setSuccess(false);
            resultado.setError(ex.getMessage());
			if(isCommit) {
				con.rollback();
			}
            
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }

        return resultado;
    }

    public Boolean validarConexion() {
        Boolean retorno = false
        if (con == null || con.isClosed()) {
            con = new DBConnect().getConnection();
            retorno = true
        }
		return retorno
    }

    public Result insertCatBitacoraTransferencia(Transferencias transferencia) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String errorlog = "";
        List < Transferencias > data = new ArrayList < Transferencias > ()
        try {
            errorlog += " antes de revisar la conexion "
            closeCon = validarConexion();
            errorlog += " antes del preparent " + " CONEXION " + closeCon
            pstm = con.prepareStatement(Statements.INSERT_BITACORA_TRANSFERENCIA)
            pstm.setString(1, transferencia.getAspirante())
            pstm.setString(2, transferencia.getCorreoAspirante())
            pstm.setString(3, transferencia.getValorOriginal())
            pstm.setString(4, transferencia.getValorCambio())
            pstm.setString(5, transferencia.getUsuarioCreacion())
            pstm.setString(6, transferencia.getCampusAnterior())
            pstm.setString(7, transferencia.getCampusNuevo())
			pstm.setString(8, transferencia.getLicenciatura())
			pstm.setString(9, transferencia.getPeriodo())
			pstm.setString(10, transferencia.getEstatus())
			pstm.setLong(11, transferencia.getCaseid())
			pstm.setString(12, transferencia.getIdbanner())
            pstm.executeUpdate();

            resultado.setSuccess(true);
            resultado.setData(data)
            
        } catch (Exception e) {
            errorlog += " falle " + e.getMessage()
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result selectBitacoraTransferencias(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = " ", orderby = "ORDER BY ", errorlog = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Long userLogged = 0L;
        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
        String campus = "";
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            Transferencias row = new Transferencias();
            List < Transferencias > rows = new ArrayList < Transferencias > ();

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

            //			if(lstGrupo.size()>0) {
            //				where+=" OR campusNuevo IN ("
            //			}
            //			for(Integer i=0; i<lstGrupo.size(); i++) {
            //				String campusMiembro=lstGrupo.get(i);
            //				where+="'"+campusMiembro+"'"
            //				if(i==(lstGrupo.size()-1)) {
            //					where+=")) "
            //				}
            //				else {
            //					where+=", "
            //				}
            //			}

			if (object.orden == ">=") {
				if (lstGrupo.size() > 0) {
					where += " WHERE (BT.campusAnterior IN ("
				}
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += "'" + campusMiembro + "'"
					if (i == (lstGrupo.size() - 1)) {
						where += ")) "
					} else {
						where += ", "
					}
				}
			} else {
				if (lstGrupo.size() > 0) {
					where += " WHERE (BT.campusNuevo IN ("
				}
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += "'" + campusMiembro + "'"
					if (i == (lstGrupo.size() - 1)) {
						where += ")) "
					} else {
						where += ", "
					}
				}
			}
			
			
            closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs= pstm.executeQuery();
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                switch (filtro.get("columna")) {
					case "ID ASPIRANTE,NOMBRE":
						errorlog+="ID ASPIRANTE,NOMBRE"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(BT.aspirante) like lower('%[valor]%') ";
						
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(BT.idbanner) like lower('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						errorlog+=" EL WHERE ES " + where;
					break;
					case "CARRERA,PERIODO":
						errorlog+="CARRERA,PERIODO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(BT.licenciatura) like lower('%[valor]%') ";
						
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(BT.periodo) like lower('%[valor]%') )";
						
						where = where.replace("[valor]", filtro.get("valor"))
						
					break;
					case "ESTATUS":
					errorlog += " ESTATUS "
						if (where.contains("WHERE")) {
							where += " AND ";
						} else {
							where += " WHERE ";
						}
						where += " LOWER(BT.estatus) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "VPD ORIGEN":
					errorlog += " VPD ORIGEN "
						if (where.contains("WHERE")) {
							where += " AND ";
						} else {
							where += " WHERE ";
						}
						where += " LOWER(BT.CAMPUSANTERIOR) ";
						
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "VPD DESTINO":
					errorlog += " VPD DESTINO "
						if (where.contains("WHERE")) {
							where += " AND ";
						} else {
							where += " WHERE ";
						}
						where += " LOWER(BT.CAMPUSNUEVO) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "AUTOR TRANSFERENCIA,FECHA MOVIMIENTO":
						errorlog+="AUTOR TRANSFERENCIA,FECHA MOVIMIENTO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(BT.USUARIOCREACION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where +=" OR to_char(TO_TIMESTAMP(BT.FECHACREACION, 'YYYY-MM-DD HH24:MI:SS'), 'DD-MM-YYYY HH24:MI:SS') like lower('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						
					break;
					case "CAMPUS":
					errorlog += " CAMPUS " + object.orden;
					if (object.orden == "<") {
						if (where.contains("WHERE")) {
							campus += " AND ";
						} else {
							campus += " WHERE ";
						}
						campus += " LOWER(BT.campusnuevo) ";
						if (filtro.get("operador").equals("Igual a")) {
							campus += "=LOWER('[valor]')"
						} else {
							campus += "LIKE LOWER('%[valor]%')"
						}
						campus = campus.replace("[valor]", filtro.get("valor"))
					} else {
						if (where.contains("WHERE")) {
							campus += " AND ";
						} else {
							campus += " WHERE ";
						}
						campus += "LOWER(BT.campusanterior) ";
						if (filtro.get("operador").equals("Igual a")) {
							campus += "=LOWER('[valor]')"
						} else {
							campus += "LIKE LOWER('%[valor]%')"
						}
						campus = campus.replace("[valor]", filtro.get("valor"))
					}
					break;
                    default:
					errorlog += " Default and orden " + object.orden;
                        if (object.orden == ">=") {
                            if (lstGrupo.size() > 0) {
                                where += " WHERE (BT.campusAnterior IN ("
                            }
                            for (Integer i = 0; i < lstGrupo.size(); i++) {
                                String campusMiembro = lstGrupo.get(i);
                                where += "'" + campusMiembro + "'"
                                if (i == (lstGrupo.size() - 1)) {
                                    where += ")) "
                                } else {
                                    where += ", "
                                }
                            }
                        } else {
                            if (lstGrupo.size() > 0) {
                                where += " WHERE (BT.campusNuevo IN ("
                            }
                            for (Integer i = 0; i < lstGrupo.size(); i++) {
                                String campusMiembro = lstGrupo.get(i);
                                where += "'" + campusMiembro + "'"
                                if (i == (lstGrupo.size() - 1)) {
                                    where += ")) "
                                } else {
                                    where += ", "
                                }
                            }
                        }
                        break;
                }
            }

            switch (object.orderby) {
				case "IDASPIRANTE":
					orderby += "BT.idbanner";
				break;
				case "NOMBRE":
					orderby += "BT.aspirante";
				break;
				case "CARRERA":
					orderby += "BT.licenciatura";
				break;
				case "PERIODO":
					orderby += "BT.periodo";
				break;
				case "ESTATUS":
					orderby += "BT.estatus";
				break;
				case "VPDORIGEN":
					orderby += "BT.campusanterior";
				break;
				case "VPDDESTINO":
					orderby += "BT.campusnuevo";
				break;
				case "USUARIO":
					orderby += "BT.usuariocreacion";
				break;
				case "FECHA":
					orderby += "BT.fechacreacion";
				break;
            }

            if (orderby.equals("ORDER BY ")) {
                orderby += "BT.PERSISTENCEID";
            }

            orderby += " " + object.orientation;
			
            String consulta = Statements.GET_BITACORA_TRANSFERENCIA;
			errorlog += " WHERE " + where
			errorlog += " campus " + campus
            consulta = consulta.replace("[WHERE]", where);
            consulta = consulta.replace("[campus]", campus);
			errorlog += " GET_BITACORA_TRANSFERENCIA " + consulta;
            String consultaCount = Statements.GET_COUNT_BITACORA_TRANSFERENCIA;
            consultaCount = consultaCount.replace("[WHERE]", where);
            consultaCount = consultaCount.replace("[campus]", campus);

            pstm = con.prepareStatement(consultaCount.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery();

            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"));
            }

            consulta = consulta.replace("[ORDERBY]", orderby);
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
            errorlog += " consulta " + consulta;
            pstm = con.prepareStatement(consulta);
            pstm.setInt(1, object.limit);
            pstm.setInt(2, object.offset);
            rs = pstm.executeQuery();

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                row = new Transferencias();
                row.setAspirante(rs.getString("aspirante"));
                row.setCorreoAspirante(rs.getString("correoaspirante"));
				
				
				SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			
				String sDate1=rs.getString("fechacreacion");
				Date date1=formatter1.parse(sDate1);
				String fechastring = f.format(date1);
				
                //row.setFechaCreacion(rs.getString("fechacreacion"));
				row.setFechaCreacion(fechastring);
                row.setUsuarioCreacion(rs.getString("usuariocreacion"));
                row.setValorCambio(rs.getString("valorcambio"));
                row.setValorOriginal(rs.getString("valororiginal"));
                row.setCampusAnterior(rs.getString("campusanterior"));
                row.setCampusNuevo(rs.getString("campusnuevo"));
				row.setLicenciatura(rs.getString("licenciatura"));
				row.setPeriodo(rs.getString("periodo"));
				row.setEstatus(rs.getString("estatus"))
				row.setIdbanner(rs.getString("idbanner"));
				
				String urlFoto = rs.getString("urlfoto");
				String encoded = "";
				errorlog += " Antes de la foto "
				if(urlFoto != null && !urlFoto.isEmpty()) {
					errorlog += " foto azure "
					//encoded = rs.getString("urlfoto") +SSA;
					encoded = base64Imagen((rs.getString("urlfoto") + SSA));
					row.setImg(encoded);
				}else {
					errorlog += " foto bdm "
					try {
						List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString("caseid")), "fotoPasaporte", 0, 10)
						for(Document doc : doc1) {
							encoded = "../API/formsDocumentImage?document="+doc.getId();
							row.setImg(encoded);
						}
					} catch (Exception e) {
							row.setImg("");
							errorlog += "" + e.getMessage();
						}
				}
				
				
                rows.add(row);
            }
            resultado.setSuccess(true);
            
            resultado.setData(rows);

        } catch (Exception e) {
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            errorlog += " ERROR "
            e.getMessage();
            
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result reagendarExamen(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        String errorLog = "";
        Boolean closeCon = false;
        try {
			
            ProcessAPI processAPI = context.getApiClient().getProcessAPI();
            Boolean avanzartarea = false;
            String username = "";
            String password = "";
            
			closeCon = validarConexion();
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            assert object instanceof Map;

            org.bonitasoft.engine.api.APIClient apiClient = new APIClient() //context.getApiClient();
            apiClient.login(username, password)
            errorLog = errorLog + " | object.caseid: " + object.caseid;
            context.getApiClient().getProcessAPI().updateProcessDataInstance("isReagendar", Long.valueOf(object.caseid.toString()), true);

            SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
            searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, object.caseid);
            searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
            final SearchOptions searchOptions = searchBuilder.done();
            SearchResult < HumanTaskInstance > SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
            List < HumanTaskInstance > lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();

            for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
                errorLog = errorLog + " | objHumanTaskInstance.getName(): " + objHumanTaskInstance.getName()
                if (objHumanTaskInstance.getName().equals("Pase de lista Prueba 1") || objHumanTaskInstance.getName().equals("Pase de lista Prueba 2") || objHumanTaskInstance.getName().equals("Pase de lista Prueba 3") || objHumanTaskInstance.getName().equals("Generar credencial")) {
                    Map < String, Serializable > inputs = new HashMap < String, Serializable > ();
                    processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
                    processAPI.executeUserTask(objHumanTaskInstance.getId(), inputs);
                    errorLog = errorLog + " | FINAL"
                }
            }
						
			String usuarioReagendar = "";
			pstm = con.prepareStatement(Statements.GET_CORREO_BY_CASEID)
			pstm.setLong(1, Long.valueOf(object.caseid.toString()))
			rs = pstm.executeQuery()
			while(rs.next()) {
				usuarioReagendar = (rs.getString("correoelectronico"))
			}
			
			if(object.isProceso == false) {
				Result resultadoSesion = new SesionesDAO().eliminarSesionAspirante(usuarioReagendar, context)
				errorLog += " el error en el eliminar es : " + resultadoSesion.getError();
				if (resultadoSesion.isSuccess()) {
					errorLog += " se elimino al aspirante de la sesion" + resultadoSesion.isSuccess().toString();
				} else {
					errorLog += " no elimino al aspirante de la sesion "+ resultadoSesion.isSuccess().toString();
				}
				
			}else {
				/*con.setAutoCommit(false)
				 
				 List<Long> pruebas = new ArrayList<Long>()
				 pstm = con.prepareStatement(Statements.GET_PRUEBAS_ASPIRANTE)
				 pstm.setString(1,  usuarioReagendar)
				 rs = pstm.executeQuery()
				 while(rs.next()) {
					 pruebas.add(rs.getLong("prueba_pid"))
				 }
				 //errorLog = errorLog + " | Pruebas "+ pruebas
				 for(Long pa:pruebas) {
					 pstm = con.prepareStatement(Statements.GET_ASISTENCIA_PRUEBA_FALTA)
					 pstm.setString(1, usuarioReagendar)
					 pstm.setLong(2, pa)
					 rs = pstm.executeQuery()
					 //errorLog = errorLog + " | rs "+rs
					 if(!rs.next()) {
						 //errorLog = errorLog + " | insert de pruebas "
						 pstm = con.prepareStatement(Statements.INSERT_PASEDELISTA, Statement.RETURN_GENERATED_KEYS)
						 pstm.setLong(1, pa);
						 pstm.setString(2, usuarioReagendar);
						 pstm.setBoolean(3,false);
						 pstm.setString(4,"");
						 
						 pstm.executeUpdate();
					 }
				 }
				 
				 con.commit();*/
			}
            resultado.setSuccess(true)
            
        } catch (Exception ex) {
            
            resultado.setSuccess(false);
            resultado.setError(ex.getMessage());
            //con.rollback();
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }

        return resultado;
    }
	
	
	public Result GuardarFaltas(String email) {
		Result resultado = new Result();
		String errorLog = "";
		Boolean closeCon = false;
		try {
			
			closeCon = validarConexion()
			con.setAutoCommit(false)
			String usuarioReagendar = "";
			
			usuarioReagendar = email;
			
			List<Long> pruebas = new ArrayList<Long>()
			pstm = con.prepareStatement(Statements.GET_PRUEBAS_ASPIRANTE)
			pstm.setString(1,  usuarioReagendar)
			rs = pstm.executeQuery()
			while(rs.next()) {
				pruebas.add(rs.getLong("prueba_pid"))
			}
			//errorLog = errorLog + " | Pruebas "+ pruebas
			for(Long pa:pruebas) {
				pstm = con.prepareStatement(Statements.GET_ASISTENCIA_PRUEBA_FALTA)
				pstm.setString(1, usuarioReagendar)
				pstm.setLong(2, pa)
				rs = pstm.executeQuery()
				//errorLog = errorLog + " | rs "+rs
				if(!rs.next()) {
					//errorLog = errorLog + " | insert de pruebas "
					pstm = con.prepareStatement(Statements.INSERT_PASEDELISTA, Statement.RETURN_GENERATED_KEYS)
					pstm.setLong(1, pa);
					pstm.setString(2, usuarioReagendar);
					pstm.setBoolean(3,false);
					pstm.setString(4,"");
					
					pstm.executeUpdate();
				}
			}
			
			con.commit();
			
		}catch (Exception ex) {
            
            resultado.setSuccess(false);
            resultado.setError(ex.getMessage());
            con.rollback();
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
		
		return resultado;
		
	}
	
	public Result ejecutarGenerarCredencial(String email, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
		Boolean closeCon = false;
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		try {
			
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(email, 0, 1);
			
			ProcessAPI processAPI = context.getApiClient().getProcessAPI();
			Map < String, Serializable > inputs = new HashMap < String, Serializable > ();
			
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, lstSolicitudDeAdmision.get(0).getCaseId());
			searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
			SearchOptions searchOptions = searchBuilder.done();
			
			SearchResult < HumanTaskInstance > SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
			List < HumanTaskInstance > lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			Boolean isSeleccionCita = false,generoCredencial = false;;
			for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
				if (objHumanTaskInstance.getName().equals("Generar credencial")) {
					errorLog+="credencial"
					generoCredencial = true;
					processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
					processAPI.executeUserTask(objHumanTaskInstance.getId(),inputs);
				}
				
			}
			
			resultado.setSuccess(true)
		} catch (Exception ex) {
			resultado.setSuccess(false);
			resultado.setError(ex.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}

		return resultado;
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
	
	
	
}