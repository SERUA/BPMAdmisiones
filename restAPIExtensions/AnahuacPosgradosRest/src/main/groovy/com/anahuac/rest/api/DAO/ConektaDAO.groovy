package com.anahuac.rest.api.DAO

import com.anahuac.rest.api.Entity.Result
import groovy.json.JsonSlurper
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import org.bonitasoft.engine.search.Order
import org.bonitasoft.web.extension.rest.RestAPIContext

import io.conekta.Charge
import io.conekta.Conekta
import io.conekta.Customer
import io.conekta.LineItems
import io.conekta.OxxoPayment
import io.conekta.PaymentMethod
import io.conekta.SpeiPayment

class ConektaDAO {
	public Result getOrderDetails(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List < Object > lstResultado = new ArrayList < Object > ();
		String order_id = "";
		String campus_id = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			order_id = object.order_id;
			campus_id = object.campus_id;
			
			Result resultApiKey = getApiKeyByCampus(context, campus_id);
			
			if(resultApiKey.success) {
				Conekta.setApiKey(resultApiKey.getData().get(0));
			} else {
				throw new Exception("Error inesperado");
			}
			
			Order order = Order.find(order_id);
			LineItems line_item = (LineItems) order.line_items.get(0);
			Charge charge = (Charge) order.charges.get(0);
			PaymentMethod payment_method = (PaymentMethod) charge.payment_method;
			double amount = order.amount / 100;
			String status = order.payment_status;
			DecimalFormat twoPlaces = new DecimalFormat("0.00");
			Map<String, String> mapResultado = new HashMap<String, String>();
			Date createdAt = new Date((long)1000 * order.created_at);
			SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
			formatDate.setTimeZone(TimeZone.getTimeZone("GMT-6"));
			SimpleDateFormat formatHours = new SimpleDateFormat("hh:mm");
			formatHours.setTimeZone(TimeZone.getTimeZone("GMT-6"));
			String dateString = formatDate.format(createdAt);
			String timeString = formatHours.format(createdAt);
			
			if(payment_method.type.equals("spei")) {
				SpeiPayment speiPayment = (SpeiPayment) charge.payment_method;
				mapResultado.put("speiBank", speiPayment.bank);
				mapResultado.put("CLABE", speiPayment.clabe);
				mapResultado.put("amount", "\$" + twoPlaces.format(amount).toString() + " " + order.currency);
				mapResultado.put("id", order.id);
				mapResultado.put("createdAtDate", dateString);
				mapResultado.put("createdAtTime", timeString);
				mapResultado.put("type", payment_method.type);
				mapResultado.put("authorizationCode", speiPayment.clabe);
				mapResultado.put("status", status);
			} else if(payment_method.type.equals("oxxo")) {
				OxxoPayment oxxoPayment = (OxxoPayment) charge.payment_method;
				
				mapResultado.put("referencia", oxxoPayment.reference);
				mapResultado.put("amount", "\$" + twoPlaces.format(amount).toString() + " " + order.currency);
				mapResultado.put("id", order.id);
				mapResultado.put("createdAtDate", dateString);
				mapResultado.put("createdAtTime", timeString);
				mapResultado.put("type", payment_method.type);
				mapResultado.put("authorizationCode", oxxoPayment.reference);
				mapResultado.put("status", status);
				mapResultado.put("barcodeUrl", oxxoPayment.barcode_url);
			} else {
				mapResultado.put("cardNumber", payment_method.getVal("last4"));
				mapResultado.put("amount", "\$" + twoPlaces.format(amount).toString() + " " + order.currency);
				mapResultado.put("id", order.id);
				mapResultado.put("authorizationCode", payment_method.getVal("auth_code"));
				mapResultado.put("createdAtDate", dateString);
				mapResultado.put("createdAtTime", timeString);
				mapResultado.put("name", order.customer_info.name);
				mapResultado.put("type", payment_method.type);
				mapResultado.put("cardBrand", payment_method.getVal("brand"));
				mapResultado.put("status", status);
//				lstResultado.add(mapResultado);
			}
			
			lstResultado.add(mapResultado);
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (io.conekta.ErrorList error) {
			LOGGER.error error.details.get(0).message
			resultado.setSuccess(false);
			resultado.setError(error.details.get(0).message);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
}
