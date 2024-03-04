function($scope, $http, blockUI) {
    $scope.isPreautorizacion = false;
    $scope.isBecas = false;
    $scope.isComiteBecas = false;
    $scope.isAreaArtistica = false;
    $scope.isAreDeportiva = false;
    $scope.isFinanciamiento = false;
    $scope.isComiteFinanzas = false;
    $scope.isTiSerua = false;
    $scope.isSerua = false;
    $scope.isChat = false;
    $scope.isAdministrador = false;
    $scope.isReporteSDAE = false;

    $scope.countPreautorizacion = 0;
    $scope.countArtistica = 0;
    $scope.countDeportiva = 0;
    $scope.countComiteBecas = 0;
    $scope.countCierreBecas = 0;
    $scope.countPreAutoFina = 0;
    $scope.countComiteFinanzas = 0;
    $scope.countCierreFinanzas = 0;
    
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
        } else if($scope.isTiSerua){
            output = true;
        } else if($scope.isChat){
            output = true;
        } else if ($scope.isConfigCampusSDAE){
            output = true;
        } else if ($scope.isReporteSDAE){
            output = true;
        }
        
        return output;
    }


    $scope.redirect = function (_param, filtro) {

        if (_param === "vistageneral") {
            if ($scope.isTiSerua || $scope.isSerua || $scope.isPreautorizacion || $scope.isFinanciamiento || $scope.isChat) {
                window.top.location.href = "/bonita/apps/administrativo/SDAEBandejaMaestra/";
            }
        } else if (_param === "apoyosotorgados") {
            if ($scope.isTiSerua || $scope.isSerua || $scope.isPreautorizacion || $scope.isFinanciamiento || $scope.isReporteSDAE) {
                window.top.location.href = "/bonita/apps/administrativo/bandejaFinalizadas/";
            }
        } else if (_param === "progreso") {
            if ($scope.isTiSerua || $scope.isSerua || $scope.isPreautorizacion || $scope.isChat) {
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
            if ($scope.isBecas || $scope.isTiSerua || $scope.isSerua || $scope.isPreautorizacion) {
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
            if ($scope.isFinanciamiento || $scope.isTiSerua || $scope.isSerua || $scope.isChat) {
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

            return $http(req).success(function (data, status) {
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
                    } else if ($scope.lstMembership[i].role_id.displayName === "Crisp") {
                        $scope.isChat = true;
                        console.log("isSerua " + $scope.isChat);
                    } else if ($scope.lstMembership[i].role_id.displayName === "Config Campus SDAE"){
                        $scope.isConfigCampusSDAE = true;
                        console.log("isConfigCampusSDAE " + $scope.isConfigCampusSDAE);
                    } else if ($scope.lstMembership[i].role_id.displayName === "Administrador") {
                        $scope.isAdministrador = true;
                        console.log("isAdministrador " + $scope.isAdministrador);
                    } else if ($scope.lstMembership[i].role_id.displayName === "Reporte SDAE") {
                        $scope.isReporteSDAE = true;
                        console.log("isReporteSDAE " + $scope.isReporteSDAE);
                    }
                }
            }).error(function (data, status) {
                console.error(data);
            }).finally(function () { 

            });
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
        }).error(function (data, status) {
            console.error(data)
        }).finally(function () {
            blockUI.stop();
        });
    }

    // $scope.enProceso = 0;
    // $scope.nuevasSolicitudes = 0;
    // $scope.esperandoPago = 0;
    // $scope.autodescripcion = 0;
    // $scope.AutodescripcionEnProceso = 0;
    // $scope.EleccionPruebasNoCalendarizado = 0;
    // $scope.NoImpresoCredencial = 0;
    // $scope.YaSeImprimioSuCredencial = 0;
    // $scope.initializeDatosProceso = function () {
    //     doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
    //         "estatusSolicitud": "Nuevas solicitudes",
    //         "tarea": "Validar Información",
    //         "lstFiltro": [],
    //         "type": "aspirantes_proceso",
    //         "orderby": "",
    //         "orientation": "DESC",
    //         "limit": 10,
    //         "offset": 0
    //     }, function (datos) {
    //         console.log("getDatosCierreFinanzas" + _data.totalRegistros);
    //         $scope.nuevasSolicitudes = datos.totalRegistros;
    //     })
    // }
    // $scope.initializeDatosProceso();

    function getDatosPreauto() {
        let data = {
            "estatusSolicitud": "'Esperando Pre-Autorización', 'Correcciones realizadas', 'Evaluación artística rechaza', 'Evaluación deportiva rechaza'"
        };
        doRequest("POST", "../API/extension/AnahuacBecasRest?url=countSolicitudesDeApoyoByEstatus&p=0&c=10", {}, data, function (_data) {
            console.log("getDatosPreauto::" + _data.totalRegistros);
            $scope.countPreautorizacion = _data.totalRegistros;
            getDatosArtsistica();
        })
    }

    getDatosPreauto();

    function getDatosArtsistica() {
        let data = {
            "estatusSolicitud": "'Esperando revisión área artistica'"
        };
        doRequest("POST", "../API/extension/AnahuacBecasRest?url=countSolicitudesDeApoyoByEstatus&p=0&c=10", {}, data, function (_data) {
            console.log("getDatosArtsistica::" + _data.totalRegistros);
            $scope.countArtistica =  _data.totalRegistros;
            getDatosDeportiva()
        })
    }

    function getDatosDeportiva() {
        let data = {
            "estatusSolicitud": "'Esperando revisión área deportiva'"
        };
        doRequest("POST", "../API/extension/AnahuacBecasRest?url=countSolicitudesDeApoyoByEstatus&p=0&c=10", {}, data, function (_data) {
            console.log("getDatosDeportiva::" + _data.totalRegistros);
            $scope.countDeportiva =  _data.totalRegistros;
            getDatosComiteBecas();
        })
    }

    function getDatosComiteBecas() {
        let data = {
            "estatusSolicitud": "'En espera de autorización'"
        };
        doRequest("POST", "../API/extension/AnahuacBecasRest?url=countSolicitudesDeApoyoByEstatus&p=0&c=10", {}, data, function (_data) {
            console.log("getDatosComiteBecas::" + _data.totalRegistros);
            $scope.countComiteBecas =  _data.totalRegistros;
            getDatosCierreBecas();
        })
    }

    function getDatosCierreBecas() {
        let data = {
            "isCompletadas": true,
            "estatusSolicitud": "'Propuesta aceptada por aspirante', 'Propuesta financiamiento aceptada por aspirante', 'Solicitud de financiamiento autorizada', 'Propuesta de financiamiento aceptada por aspirante'"
        };
        doRequest("POST", "../API/extension/AnahuacBecasRest?url=countSolicitudesDeApoyoByEstatus&p=0&c=10", {}, data, function (_data) {
            console.log("getDatosCierreBecas::" + _data.totalRegistros);
            $scope.countCierreBecas =  _data.totalRegistros;
            getDatosPreautoFina();
        })
    }

    function getDatosPreautoFina() {
        let data = {
            "estatusSolicitud": "'Solicitud de financiamiento completada', 'Solicitud de financimiento reactivada',  'Modificaciones realizadas'"
        };
        doRequest("POST", "../API/extension/AnahuacBecasRest?url=countSolicitudesDeApoyoByEstatus&p=0&c=10", {}, data, function (_data) {
            console.log("getDatosPreautoFina::" + _data.totalRegistros);
            $scope.countPreAutoFina =  _data.totalRegistros;
            getDatosComiteFinanzas();
        })
    }

    function getDatosComiteFinanzas() {
        let data = {
            "estatusSolicitud": "'Solicitud de financiamiento en autorización', 'Solicitud validada por finanzas'"
        };
        doRequest("POST", "../API/extension/AnahuacBecasRest?url=countSolicitudesDeApoyoByEstatus&p=0&c=10", {}, data, function (_data) {
            console.log("getDatosComiteFinanzas::" + _data.totalRegistros);
            $scope.countComiteFinanzas =  _data.totalRegistros;
            getDatosCierreFinanzas();
        })
    }

    function getDatosCierreFinanzas() {
        let data = {
            "isCompletadas": true,
            "estatusSolicitud": "'Propuesta aceptada por aspirante', 'Propuesta financiamiento aceptada por aspirante', 'Solicitud de financiamiento autorizada', 'Propuesta de financiamiento aceptada por aspirante'"
        };
        
        doRequest("POST", "../API/extension/AnahuacBecasRest?url=countSolicitudesDeApoyoByEstatus&p=0&c=10", {}, data, function (_data) {
            console.log("getDatosCierreFinanzas" + _data.totalRegistros);
            $scope.countCierreFinanzas =  _data.totalRegistros;
        })
    }
}