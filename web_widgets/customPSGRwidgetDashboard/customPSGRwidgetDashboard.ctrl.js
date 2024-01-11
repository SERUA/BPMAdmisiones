/**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function($scope, $http, blockUI) {
    
    $scope.redirect = function(_param, filtro) {
        if (_param === "en_proceso") {
            window.top.location.href = "/bonita/apps/posgrados/pg_solicitudes_iniciadas/";
        } else if (_param === "nuevas_solicitudes") {
            window.top.location.href = "/bonita/apps/posgrados/pg_solicitudes_nuevas";
        } else if (_param === "citas") {
            window.top.location.href = "/bonita/apps/posgrados/pg_citas";
        } else if (_param === "pase_lista") {
            window.top.location.href = "/bonita/apps/posgrados/pg_solicitudes_pase_lista";
        } else if (_param === "solicitudes_archivadas") {
            window.top.location.href = "/bonita/apps/posgrados/pg_solicitudes_rechazadas";
        } else if (_param === "estadisticas") {
            window.top.location.href = "/bonita/apps/posgrados/";
        } else if (_param === "dictamen") {
            window.top.location.href = "/bonita/apps/posgrados/pg_solicitudes_dictamen";
        } else if (_param === "admitidos") {
            window.top.location.href = "/bonita/apps/posgrados/pg_solicitudes_dictamen";
        } else if (_param === "no_admitidos") {
            window.top.location.href = "/bonita/apps/posgrados/pg_solicitudes_dictamen";
        } else if (_param === "transferencias") {
            window.top.location.href = "/bonita/apps/posgrados/pg_transferencias";
        }
    };

    function doRequest(method, url, params, dataToSend, callback) {
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req).success(function(data, status) {
            callback(data)
        })
        .error(function(data, status) {
            console.error(data)
        })
        .finally(function() {

            blockUI.stop();
        });
    }

    $scope.enProceso = 0;
    $scope.nuevasSolicitudes = 0;
    $scope.esperandoPago = 0;
    $scope.autodescripcion = 0;
    $scope.AutodescripcionEnProceso = 0;
    $scope.EleccionPruebasNoCalendarizado = 0;
    $scope.NoImpresoCredencial = 0;
    $scope.YaSeImprimioSuCredencial = 0;

    $scope.initializeDatosProceso = function() {
        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud": "'solicitud_completada','modificaciones_realizadas','solicitud_reactivada'",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.nuevasSolicitudes = datos.totalRegistros;
        });

        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud": "'solicitud_aprobada_admin'",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.citasAgendadas = datos.totalRegistros;
        });

        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud" : "'solicitud_rechazada_admin', 'solicitud_archivada_area_academica', 'solicitud_archivada_reagendacion', 'solicitud_archivada_dictamen'",
            "lstFiltro": [],
            "type": "aspirantes_rechazados",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.rechazados = datos.totalRegistros;
        });

        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud" : "'solicitud_admitida'",
            "lstFiltro": [],
            "type": "aspirantes_rechazados",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.admitidos = datos.totalRegistros;
        });

        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud" : "'solicitud_no_admitida'",
            "lstFiltro": [],
            "type": "aspirantes_rechazados",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.noAdmitidos = datos.totalRegistros;
        });

        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud" : "'solicitud_lista_para_dictamen'",
            "lstFiltro": [],
            "type": "aspirantes_rechazados",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.dictamen = datos.totalRegistros;
        });
    }
    $scope.initializeDatosProceso();
}