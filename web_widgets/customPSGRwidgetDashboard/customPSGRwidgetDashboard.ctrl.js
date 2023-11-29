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
        if (_param === "progreso") {
            window.top.location.href = "/bonita/apps/administrativo/solicitudesEnProgreso/";
        } else if (_param === "nuevas") {
            window.top.location.href = "/bonita/apps/administrativo/nuevasSolicitudes/";
        } else if (_param === "aspirantesProceso") {
            window.top.location.href = "/bonita/apps/administrativo/aspirantesEnProceso/?estatusSelected=" + filtro;
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

        return $http(req)
            .success(function(data, status) {
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
            "tarea": "Validar Información",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.enProceso = datos.totalRegistros;
        })
        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud": "'solicitud_completada','modificaciones_realizadas','solicitud_reactivada'",
            "tarea": "Validar Información",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.nuevasSolicitudes = datos.totalRegistros;
        })
        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud": "Solicitud en espera de pago",
            "tarea": "Validar Información",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.esperandoPago = datos.totalRegistros;
        })
        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud": "Solicitud con pago aceptado",
            "tarea": "Validar Información",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.autodescripcion = datos.totalRegistros;
        })
        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud": "Solicitud en progreso",
            "tarea": "Validar Información",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.solicitudProgreso = datos.totalRegistros;
        })
        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud": "Autodescripción en proceso",
            "tarea": "Validar Información",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.AutodescripcionEnProceso = datos.totalRegistros;
        });
        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud": "Elección de pruebas no calendarizado",
            "tarea": "Validar Información",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.EleccionPruebasNoCalendarizado = datos.totalRegistros;
        });
        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud": "No se ha impreso credencial",
            "tarea": "Validar Información",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.NoImpresoCredencial = datos.totalRegistros;
        });
        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud": "Ya se imprimió su credencial",
            "tarea": "Validar Información",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.YaSeImprimioSuCredencial = datos.totalRegistros;
        });
        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "tarea": "Lista roja",
            "estatusSolicitud" : "Solicitud lista roja",
            "lstFiltro": [],
            "type": "lista_roja",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.listaRoja = datos.totalRegistros;
        })
        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "tarea": "Solicitud rechazada",
            "estatusSolicitud" : "Solicitud rechazada",
            "lstFiltro": [],
            "type": "aspirantes_rechazados",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.rechazados = datos.totalRegistros;
        })
    }
    $scope.initializeDatosProceso();
}