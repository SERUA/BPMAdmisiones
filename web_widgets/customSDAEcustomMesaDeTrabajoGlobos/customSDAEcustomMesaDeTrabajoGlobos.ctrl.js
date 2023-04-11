function($scope, $http, blockUI) {
    // $scope.isBecas = false;
    // $scope.isPsicologoSup = false;
    // $scope.isPsicologo = false;
    // $scope.isPaseDeLista = false;
    $scope.isPreautorizacion = false;
    $scope.isBecas = false;
    $scope.isComiteBecas = false;
    $scope.isAreaArtistica = false;
    $scope.isAreDeportiva = false;
    $scope.isFinanciamiento = false;
    $scope.isComiteFinanzas = false;

    $scope.isTiCampus = false;
    $scope.isTiSerua = false;
    $scope.isSerua = false;
    
    $scope.hasRole = function(){
        let output = false;
        
        if($scope.isPreautorizacion){
            output = true;
        } else if($scope.isBecas){
            output = true;
        } else if($scope.isComiteBecas){
            output = true;
        } else if($scope.isAreaArtistica){
            output = true;
        } else if($scope.isAreDeportiva){
            output = true;
        } else if($scope.isFinanciamiento){
            output = true;
        } else if($scope.isComiteFinanzas){
            output = true;
        } else if($scope.isTiCampus){
            output = true;
        } else if($scope.isTiSerua){
            output = true;
        } else if($scope.isSerua){
            output = true;
        }
        
        return output;
    }


    $scope.redirect = function (_param, filtro) {

        if (_param === "progreso") {
            if ($scope.isTiSerua || $scope.isSerua || $scope.isPreautorizacion ) {
                window.top.location.href = "/bonita/apps/administrativo/SDAEBandejaEnProgreso/";
            }
        } else if (_param === "preauto") {

            if ($scope.isTiSerua || $scope.isSerua || $scope.isPreautorizacion) {
                window.top.location.href = "/bonita/apps/administrativo/bandejaPreAutorizacion/";
            }
        } else if (_param === "comitebecas") {
            if ($scope.isBecas || $scope.isTiSerua || $scope.isSerua) {
                window.top.location.href = "/bonita/apps/administrativo/bandejaBecas/";
            }
        } else if (_param === "cierre") {
            if ($scope.isBecas || $scope.isTiSerua || $scope.isSerua || $scope.isPreautorizacion) {
                window.top.location.href = "/bonita/apps/administrativo/archivoSolicitudesCompletadas/";
            }
        } else if (_param === "archivadas") {
            if ($scope.isBecas || $scope.isTiSerua || $scope.isSerua || $scope.isPreautorizacion) {
                window.top.location.href = "/bonita/apps/administrativo/bandejaArchivadas/";
            }
        } else if (_param === "esperapago") {
            if ($scope.isBecas || $scope.isTiSerua || $scope.isSerua) {
                window.top.location.href = "/bonita/apps/administrativo/EsperaDePago/";
            }
        } else if (_param === "artistica") {
            if ($scope.isTiSerua || $scope.isSerua || $scope.isAreaArtistica) {
                window.top.location.href = "/bonita/apps/administrativo/bandejaAreaArtistica/";
            }
        } else if (_param === "deportiva") {
            if ($scope.isTiSerua || $scope.isSerua || $scope.isAreDeportiva) {
                window.top.location.href = "/bonita/apps/administrativo/bandejaAreaDeportiva/";
            }
        } else if (_param === "iniciadasaval") {
            if ($scope.isFinanciamiento || $scope.isTiSerua || $scope.isSerua) {
                window.top.location.href = "/bonita/apps/administrativo/solicitudes_financiamiento_progreso/";
            }
        } else if (_param === "financiamiento") {
            if ($scope.isFinanciamiento || $scope.isTiSerua || $scope.isSerua) {
                window.top.location.href = "/bonita/apps/administrativo/bandejaFinanzas/";
            }
        } else if (_param === "archivofinan") {
            if ($scope.isComiteFinanzas || $scope.isFinanciamiento || $scope.isSerua || $scope.isTiSerua ) {
                window.top.location.href = "/bonita/apps/administrativo/bandejaArchivadasFinanciamiento/";
            }
        } else if (_param === "comitefinan") {
            if ($scope.isComiteFinanzas ||$scope.isTiSerua || $scope.isSerua) {
                window.top.location.href = "/bonita/apps/administrativo/bandejaComiteFinanzas/";
            }
        } else if (_param === "cierrefinan") {
            if ($scope.isComiteFinanzas || $scope.isFinanciamiento || $scope.isTiSerua || $scope.isSerua) {
                window.top.location.href = "/bonita/apps/administrativo/archivoSolicitudesCompletadasFin/";
            }
        } 
    };


    $scope.lstMembership = [];
    $scope.$watch("properties.userId", function (newValue, oldValue) {
        if (newValue !== undefined) {
            var req = {
                method: "GET",
                url: `/API/identity/membership?p=0&c=100&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
            };

            return $http(req)
                .success(function (data, status) {
                    $scope.lstMembership = data;
                    for (var i = 0; i < $scope.lstMembership.length; i++) {
                        if ($scope.lstMembership[i].role_id.displayName === "PreAutorizacion") {
                            $scope.isPreautorizacion = true;
                            console.log("PreAutorizacion " + $scope.isPreautorizacion);
                        }  else if ($scope.lstMembership[i].role_id.displayName === "Becas") {
                            $scope.isBecas = true;
                            console.log("Becas " + $scope.isBecas);
                        } else if ($scope.lstMembership[i].role_id.displayName === "Area Artistica") {
                            $scope.isAreaArtistica = true;
                            console.log("Area artística " + $scope.isAreaArtistica);
                        } else if ($scope.lstMembership[i].role_id.displayName === "Area Deportiva") {
                            $scope.isAreDeportiva = true;
                            console.log("Area deportiva " + $scope.isAreDeportiva);
                        } else if ($scope.lstMembership[i].role_id.displayName === "Finanzas") {
                            $scope.isFinanciamiento = true;
                            console.log("Finanzas " + $scope.isFinanciamiento);
                        } else if ($scope.lstMembership[i].role_id.displayName === "Comite de Finanzas") {
                            $scope.isComiteFinanzas = true;
                            console.log("Finanzas " + $scope.isComiteFinanzas);
                        } else if ($scope.lstMembership[i].role_id.displayName === "TI CAMPUS") {
                            $scope.isTiCampus = true;
                            console.log("isTiCampu " + $scope.isTiCampus);
                        } else if ($scope.lstMembership[i].role_id.displayName === "TI SERUA") {
                            $scope.isTiSerua = true;
                            console.log("isTiSerua " + $scope.isTiSerua);
                        } else if ($scope.lstMembership[i].role_id.displayName === "SERUA") {
                            $scope.isSerua = true;
                            console.log("isSerua " + $scope.isSerua);
                        }
                    }

                })
                .error(function (data, status) {
                    console.error(data);
                })
                .finally(function () { });
        }
    });


    function doRequest(method, url, params, dataToSend, callback) {
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req).success(function (data, status) {
            callback(data)
        })
            .error(function (data, status) {
                console.error(data)
            })
            .finally(function () {

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
    $scope.initializeDatosProceso = function () {
        doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
            "estatusSolicitud": "Nuevas solicitudes",
            "tarea": "Validar Información",
            "lstFiltro": [],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 10,
            "offset": 0
        }, function (datos) {
            $scope.nuevasSolicitudes = datos.totalRegistros;
        })
    }
    $scope.initializeDatosProceso();
}