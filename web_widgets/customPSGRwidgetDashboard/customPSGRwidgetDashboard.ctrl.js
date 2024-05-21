function($scope, $http, blockUI) {
    $scope.redirect = function(_param, _valido) {
        if(_valido){
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
            "estatusSolicitud": "'aspirante_registrado','aspirante_validado','solicitud_iniciada','modificaciones_solicitadas'",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.enProceso = datos.totalRegistros;
        });

        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud": "'solicitud_completada','modificaciones_realizadas','solicitud_reactivada', 'solicitud_pase_lista_esperando_validacion'",
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

        doRequest("POST", "/bonita/API/extension/posgradosRest?url=selectSolicitudesAdmision&p=0&c=100", {}, {
            "estatusSolicitud" : "'solicitud_aprobada_admin', 'solicitud_completada','modificaciones_realizadas'",
            "lstFiltro": [],
            "type": "aspirantes_rechazados",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function(datos) {
            $scope.paseLista = datos.totalRegistros;
        });
        
    }
    
    $scope.initializeDatosProceso();
    
    $scope.lstMembership = [];
    
    $scope.$watch("properties.userId", function (newValue, oldValue) {
        if (newValue !== undefined) {
            var req = {
                method: "GET",
                url: `/API/identity/membership?p=0&c=1000&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
            };

            return $http(req)
            .success(function (data, status) {
                
                $scope.lstMembership = data;
                leerRoles()
                // $scope.campusByUser();
                
            })
            .error(function (data, status) {
                console.error(data);
            })
            .finally(function () { });
        }
    });
    
    $scope.isSerua = false;
    $scope.isAdministrador = false;
    $scope.isSeruaPSG = false;
    $scope.isComite = false;
    $scope.isTicampus = false;
    $scope.isChat = false;
    $scope.isAdmisiones = false;
    
    function leerRoles(){
        for(let membership of $scope.lstMembership){
            if(membership.role_id.name === "ADMINISTRADOR" ){
                $scope.isAdministrador = true;
            }
            if(membership.role_id.name === "SERUA" || membership.role_id.name === "SERUA PSG" || membership.role_id.name === "TI SERUA"){
                $scope.isSeruaPSG = true;
            }
            if(membership.role_id.name === "Comite PSG" ){
                $scope.isComite = true;
            }
            if(membership.role_id.name === "Admisiones PSG" ){
                $scope.isAdmisiones = true;
            }
            if(membership.role_id.name === "TI CAMPUS" || membership.role_id.name === "TI campus PSG"){
                $scope.isTicampus = true;
            }
            if(membership.role_id.name === "Chat PSG"){
                $scope.isChat = true;
            }
        }
    }

    $scope.isActivadoMenu = function(_opcion){
        let valido = false;
        if(_opcion === "iniciadas"){
            if($scope.isSerua || $scope.isAdministrador || $scope.isSeruaPSG || $scope.isChat || $scope.isAdmisiones) {
                valido = true;
            }
        } else if(_opcion === "nuevas"){
            if($scope.isSerua || $scope.isAdministrador || $scope.isSeruaPSG || $scope.isAdmisiones) {
                valido = true;
            }
        } else if(_opcion === "pase_lista"){
            if($scope.isSerua || $scope.isAdministrador || $scope.isSeruaPSG || $scope.isComite || $scope.isAdmisiones) {
                valido = true;
            }
        } else if(_opcion === "archivadas"){
            if($scope.isSerua || $scope.isAdministrador || $scope.isSeruaPSG || $scope.isAdmisiones) {
                valido = true;
            }
        } else if(_opcion === "estadisticas"){
            if($scope.isSerua || $scope.isAdministrador || $scope.isSeruaPSG || $scope.isTicampus) {
                valido = true;
            }   
        } else if(_opcion === "dictamen"){
            if($scope.isSerua || $scope.isAdministrador || $scope.isSeruaPSG || $scope.isComite) {
                valido = true;
            }
        } else if(_opcion === "admitidos"){
            if($scope.isSerua || $scope.isAdministrador || $scope.isSeruaPSG || $scope.isComite) {
                valido = true;
            }
        } else if(_opcion === "no_admitidos"){
            if($scope.isSerua || $scope.isAdministrador || $scope.isSeruaPSG || $scope.isComite) {
                valido = true;
            }
        } else if(_opcion === "transferencias"){
            if($scope.isSerua || $scope.isAdministrador || $scope.isSeruaPSG || $scope.isAdmisiones) {
                valido = true;
            }
        }

        return valido;
    }
}