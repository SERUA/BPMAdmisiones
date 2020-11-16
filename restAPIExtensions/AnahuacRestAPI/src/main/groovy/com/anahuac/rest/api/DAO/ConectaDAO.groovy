package com.anahuac.rest.api.DAO

import org.bonitasoft.engine.api.APIClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.Entity.Result

import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper

class ConectaDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListadoDAO.class);
	
	public Result ejecutarEsperarPago(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		List<DetalleSolicitud> lstResultado = new ArrayList<DetalleSolicitud>();
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			String id = object.data.object.id;
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			def objDetalleSolicitudDAO = context.getApiClient().getDAO(DetalleSolicitudDAO.class);
			List<DetalleSolicitud> detalleSolicitud = objDetalleSolicitudDAO.findByOrdenPago(id, 0, 1);
			apiClient.login("acuna.karol@correo.com", "bpm");
			String caseId = detalleSolicitud.get(0).caseId;
			def startedBy = apiClient.getProcessAPI().getProcessInstance(Integer.parseInt(caseId)).startedBy;
			apiClient.processAPI.executeFlowNode(startedBy, apiClient.processAPI.getHumanTaskInstances(Long.valueOf(caseId), "Esperar pago", 0, 1).get(0).getId());
			resultado.setSuccess(true);
		}catch(Exception ex) {
			LOGGER.error ex.getMessage()
			resultado.setSuccess(false)
			resultado.setError(ex.getMessage())
		}
		
		return resultado;
	}
}
