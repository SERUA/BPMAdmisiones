function WidgetlivingApplicationMenuController($scope, $http, $window, $location, $timeout, modalService) {
    var ctrl = this;
    var vm = this;
    $scope.processVersion=0;
    ctrl.appStarted = false;
    ctrl.disabledByTask = function(name) {
        let taskName = $scope.properties.currentTaskName;

        if (name === "Solicitud") {
            return false;
        } else if (!$scope.properties.isCaseStarted) {
            return true;
        } else if (name === "Pago" &&
            (
                taskName === "Pago de examen" ||
                taskName === "Esperar pago" ||
                taskName === "Autodescripción" ||
                taskName === "Seleccionar cita" ||
                taskName === "Generar credencial" ||
                taskName === "Pase de lista Prueba 1" ||
                taskName === "Pase de lista Prueba 2" ||
                taskName === "Pase de lista Prueba 3" ||
                taskName === "Carga y consulta de resultados" ||
                taskName === "Resultado final de comité" ||
                taskName === "Reactivar usuario rechazado"
            )
        ) {
            return false;
        } else if (name === "Autodescripción" &&
            (
                taskName === "Autodescripción" ||
                taskName === "Seleccionar cita" ||
                taskName === "Generar credencial" ||
                taskName === "Pase de lista Prueba 1" ||
                taskName === "Pase de lista Prueba 2" ||
                taskName === "Pase de lista Prueba 3" ||
                taskName === "Carga y consulta de resultados" ||
                taskName === "Resultado final de comité" ||
                taskName === "Reactivar usuario rechazado"
            )
        ) {
            return false;
        } else if (name === "Cita Exámenes" &&
            (
                taskName === "Seleccionar cita" ||
                taskName === "Generar credencial" ||
                taskName === "Pase de lista Prueba 1" ||
                taskName === "Pase de lista Prueba 2" ||
                taskName === "Pase de lista Prueba 3" ||
                taskName === "Carga y consulta de resultados" ||
                taskName === "Resultado final de comité" ||
                taskName === "Reactivar usuario rechazado"
            )
        ) {
            return false;
        } else if (name === "Credencial" &&
            (
                taskName === "Pase de lista Prueba 1" ||
                taskName === "Pase de lista Prueba 2" ||
                taskName === "Pase de lista Prueba 3" ||
                taskName === "Generar credencial" ||
                taskName === "Carga y consulta de resultados" ||
                taskName === "Resultado final de comité" ||
                taskName === "Reactivar usuario rechazado"
            )
        ) {
            return false;
        } else if (name === "Resultado" &&
            (
                taskName === "Carga y consulta de resultados" ||
                taskName === "Resultado final de comité" ||
                taskName === "Reactivar usuario rechazado"
            )
        ) {
            return false;
        } else {
            return true;
        }
    }

    ctrl.validateUrl = function(appToken) {
        let outputToken = appToken;

        if (appToken === "nueva_solicitud_SDAE" && $scope.properties.currentTaskName === "Llenado solicitud de apoyo académico") {
            outputToken = "nueva_solicitud_SDAE";
        } else if (appToken === "nueva_solicitud_SDAE" && $scope.properties.currentTaskName === "Revisión y control de solicitud de apoyo educativo") {
            outputToken = "solicitud_completada";
        } else if (appToken === "nueva_solicitud" && $scope.properties.currentTaskName === "Modificar información") {
            // outputToken = "modificar_solicitud";
            outputToken = "solicitud_completada";
        } else {
            outputToken = appToken;
        }

        return outputToken;
    }


    ctrl.goTo = function(_token) {
        // if(_token === "verSolicitudAdmision"){
        //     window.location.href = "../" + token;
        // } else {
        //     window.location.href = "../" + token + "?id=" + $scope.properties.taskId + "&displayConfirmation=false";
        // }

        window.location.href = "../" + token;
    }

    ctrl.filterChildren = function(parentId) {
        return (ctrl.applicationMenuList || []).filter(function(menu) {
            return menu.parentMenuId === '' + parentId;
        });
    };

    ctrl.isParentMenu = function(menu) {
        return menu.parentMenuId == -1 && menu.applicationPageId == -1;
    };


    ctrl.displayPage = function(token) {
        if (ctrl.pageToken === "autodescripcion" && $scope.properties.currentTaskName === "Autodescripción") {
            $scope.tokenNuevo = token;
            $scope.properties.accionModal = "salir";
            modalService.open($scope.properties.idModalConfirmacionAD);
        } else {
            ctrl.redirectToPage(token);
        }
    };

    ctrl.redirectToPage = function(token) {
        var previousToken = ctrl.pageToken;
        var previousPath = window.location.pathname;
        if(token=="autodescripcion"){
            token=($scope.processVersion<1.53)?"autodescripcion":"autodescripcionV2";
            if(token=="autodescripcionV2"){
                var taskName = $scope.properties.currentTaskName;
                if(taskName === "Seleccionar cita" ||
                taskName === "Generar credencial" ||
                taskName === "Pase de lista Prueba 1" ||
                taskName === "Pase de lista Prueba 2" ||
                taskName === "Pase de lista Prueba 3" ||
                taskName === "Carga y consulta de resultados" ||
                taskName === "Resultado final de comité" ||
                taskName === "Reactivar usuario rechazado"){
                    token="autodescripcionV2vista";
                }
            }
        }
        
        ctrl.pageToken = token;
        var urlPath = "";
        if (previousToken === "autodescripcion" || previousToken === "autodescripcionV2" || previousToken === "pago_de_examen" || previousToken === "confirmacion_credencial" || previousToken === "verSesiones") {
            previousPath.substring(0, previousPath.length - previousToken.length - 2) + token + '/' + ($window.location.search === undefined || $window.location.search === "undefined" ? "" : $window.location.search);
        } else {
            previousPath.substring(0, previousPath.length - previousToken.length - 1) + token + '/' + ($window.location.search === undefined || $window.location.search === "undefined" ? "" : $window.location.search);
        }

        var stateObject = { title: "" + token + "", url: "" + urlPath + "" };
        if (typeof($window.history.pushState) != "undefined") {
            $window.history.pushState(stateObject, stateObject.title, stateObject.url);
        } else {
            alert("Browser does not support HTML5.");
        }

        $scope.properties.currentToken = token;

        //make sure the user is still logged in before refreshing the iframe
        verifySession().then(setTargetedUrl, refreshPage);

        return false;
    }

    ctrl.setParentPageActive = function(menu) {
        ctrl.parentPageId = menu.id;
    }

    ctrl.openCurrentSessionModal = function() {
        modalService.open($scope.properties.currentSessionModalId);
    };

    ctrl.openAppSelectionModal = function() {
        modalService.open($scope.properties.appSelectionModalId);
    };

    ctrl.logoutAndUpdateSuccessfulLogoutVariable = function() {
        return $http.get($scope.properties.logoutURL)
            .success(function(data) {
                $scope.properties.successfulLogoutResponse = data;
            });
    };

    //handle the browser back button
    $window.addEventListener('popstate', function(e) {
        parseCurrentURL();
        //make sure the user is still logged in before refreshing the iframe
        setTargetedUrl();
        refreshPage();
    });

    function parseCurrentURL() {
        var pathArray = $window.location.pathname.split('/');
        ctrl.applicationToken = pathArray[pathArray.length - 3];
        ctrl.pageToken = pathArray[pathArray.length - 2];
    }

    function setApplicationMenuList(application) {
        return $http.get('../API/living/application-menu/?c=100&f=applicationId%3D' + application.id + '&d=applicationPageId&o=menuIndex+ASC')
            .success(function(data) {
                ctrl.applicationMenuList = data;
                debugger;
                ctrl.applicationMenuList[0].applicationPageId.token = ctrl.validateUrl(ctrl.applicationMenuList[0].applicationPageId.token);
                // ctrl.applicationMenuList[4].applicationPageId.token = ctrl.validateUrl(ctrl.applicationMenuList[4].applicationPageId.token);
            });
    }

    function searchSeparator() {
        return $window.location.search ? "&" : "?";
    }

    function setTargetedUrl() {
        // angular hack to force the variable bound to refresh
        // so we change it's value to undefined and then delay to the correct value
        $scope.properties.targetUrl = undefined;
        $timeout(function() {
            $scope.properties.targetUrl = "../../../portal/resource/app/" + ctrl.applicationToken + "/" + ctrl.pageToken + "/content/" + $window.location.search + searchSeparator() + "app=" + ctrl.applicationToken;
            console.log($scope.properties.targetUrl);
        }, 0);
    }

    function refreshPage() {
        $window.location.reload();
    }

    function verifySession() {
        var userIdentity = '../API/identity/user/' + $scope.properties.userId;
        return $http.get(userIdentity);
    }
    
    function setApplication() {
        var application = $scope.properties.application;
        ctrl.applicationToken = application.token;
        // if ($scope.properties.currentTaskName === "Pago de examen" || $scope.properties.currentTaskName === "Esperar pago") {
        //     ctrl.pageToken = "pago_de_examen";
        // } else if ($scope.properties.currentTaskName === "Autodescripción") {
        //     ctrl.pageToken = ($scope.processVersion<1.53)?"autodescripcion":"autodescripcionV2";
        // } else if ($scope.properties.currentTaskName === "Seleccionar cita") {
        //     ctrl.pageToken = "verSesiones";
        // } else if ($scope.properties.currentTaskName === "Generar credencial") {
        //     let array = window.location.href.split("/");
        //     let appName = array[array.length - 2];
        //     if (appName === "generar_credencial") {
        //         ctrl.pageToken = "generar_credencial";
        //     } else {
        //         ctrl.pageToken = "confirmacion_credencial";
        //     }
        // } else if (
        //     $scope.properties.currentTaskName === "Pase de lista Prueba 1" ||
        //     $scope.properties.currentTaskName === "Pase de lista Prueba 2" ||
        //     $scope.properties.currentTaskName === "Pase de lista Prueba 3"
        // ) {
        //     ctrl.pageToken = "generar_credencial";
        // } else if (
        //     $scope.properties.currentTaskName === "Carga y consulta de resultados" ||
        //     $scope.properties.currentTaskName === "Resultado final de comité" ||
        //     $scope.properties.currentTaskName === "Reactivar usuario rechazado"
        // ) {
        //     ctrl.pageToken = "Resultado";
        // } else {
        //     ctrl.pageToken = $scope.properties.pageToken;
        // }



        //ctrl.pageToken = "bandeja";
        
        let array = window.location.href.split("/");
        let appName = array[array.length - 2];
        
        if (appName === "bandeja") {
            ctrl.pageToken = "bandeja";
        }else if (appName === "catTipoApoyo") {
            ctrl.pageToken = "catTipoApoyo";
        }else if (appName === "preAutorizacion") {
            ctrl.pageToken = "preAutorizacion";
        }else if (appName === "catControlDocumental") {
            ctrl.pageToken = "catControlDocumental";
        }else if (appName === "bandejaSolicitudesRechazadas") {
            ctrl.pageToken = "bandejaSolicitudesRechazadas";
        }else if (appName === "solicitudRechazada") {
            ctrl.pageToken = "solicitudRechazada";
        }else if (appName === "bandejaAreaDeportiva") {
            ctrl.pageToken = "bandejaAreaDeportiva";
        }else if (appName === "bandejaAreaArtistica") {
            ctrl.pageToken = "bandejaAreaArtistica";
        }else if (appName === "revisionAreaDeportiva") {
            ctrl.pageToken = "revisionAreaDeportiva";
        }else if (appName === "revisionAreaArtistica") {
            ctrl.pageToken = "revisionAreaArtistica";
        }else if (appName === "bandejaBecas") {
            ctrl.pageToken = "bandejaBecas";
        }else if (appName === "BandejaFinanzas") {
            ctrl.pageToken = "BandejaFinanzas";
        }else if (appName === "BandejaComiteFinanzas") {
            ctrl.pageToken = "BandejaComiteFinanzas";
        }
        
        
        
        /*if ($scope.properties.currentTaskName === "Llenado solicitud de apoyo académico") {
            ctrl.pageToken = "nueva_solicitud_SDAE";
        } else{
            ctrl.pageToken = "";
        }*/

        $scope.properties.currentToken = ctrl.pageToken;

        ctrl.applicationName = $scope.properties.application.displayName;
        setApplicationMenuList(application);
        setTargetedUrl();
    }

    $scope.$watch("properties.currentTaskName", function() {
        // if($scope.properties.isCaseStarted !== undefined && $scope.properties.currentTaskName !== undefined && !ctrl.appStarted){
        //     ctrl.appStarted = true;
        //     setApplication();    
        // }
        console.log("currentTask " + $scope.properties.currentTaskName)
        doRequest("GET", "../API/identity/user/" + $scope.properties.userId, null, null, function(data) {

            doRequest("GET", "../API/bdm/businessData/com.anahuac.catalogos.CatRegistro?q=findByCorreoelectronico&f=correoelectronico=" + data.userName + "&p=0&c=500", null, null, function(datos1) {
                doRequest("GET", "../API/bpm/humanTask?p=0&c=10&f=caseId=" + datos1[0].caseId + "&fstate=ready&d=processId", null, null, function(data0) {
                    if (data0.length > 0) {
                        $scope.processVersion=data0[0].processId.version;
                        if (
                            ($scope.properties.isCaseStarted && $scope.properties.currentTaskName !== "") ||
                            (!$scope.properties.isCaseStarted && $scope.properties.currentTaskName === "")
                        ) {
                            ctrl.appStarted = true;
                            setApplication();
                        }
                    } else {
                        doRequest("GET", "../API/bpm/archivedActivity?f=caseId=" + datos1[0].caseId + "&f=state=aborted", null, null, function(datos2) {
                            for (let index = 0; index < datos2.length; index++) {
                                const element = datos2[index].displayName;
                                if (true) {
                                    $scope.properties.currentTaskName = "nueva_solicitud";
                                    break;
                                }

                            }
                            if (
                                ($scope.properties.isCaseStarted && $scope.properties.currentTaskName !== "") ||
                                (!$scope.properties.isCaseStarted && $scope.properties.currentTaskName === "")
                            ) {
                                ctrl.appStarted = true;
                                setApplication();
                            }
                        })
                    }

                })
            })
        })

    });

    $scope.$watch("properties.isCaseStarted", function() {
        if ($scope.properties.isCaseStarted !== undefined && $scope.properties.currentTaskName !== undefined && !ctrl.appStarted) {
            if (
                ($scope.properties.isCaseStarted && $scope.properties.currentTaskName !== "") ||
                (!$scope.properties.isCaseStarted && $scope.properties.currentTaskName === "")
            ) {
                ctrl.appStarted = true;
                setApplication();
            }
        }
    });

    $scope.$watch("properties.cambiarPagina", function() {
        if ($scope.properties.cambiarPagina === true) {
            $scope.properties.cambiarPagina = false;
            modalService.close();
            ctrl.redirectToPage($scope.tokenNuevo);
        }
    });
    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     * ../API/bdm/businessData/com.anahuac.catalogos.CatRegistro?q=findByCorreoelectronico&f=correoelectronico={{users.user_name}}&p=0&c=500
     */
    function doRequest(method, url, params, dataToSend, callback) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data);
            })
            .error(function(data, status) {
                console.error(data)
            })
            .finally(function() {
                vm.busy = false;
            });
    }
}