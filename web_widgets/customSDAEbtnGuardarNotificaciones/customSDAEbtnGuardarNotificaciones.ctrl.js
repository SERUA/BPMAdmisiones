function PbButtonCtrl($scope, $http, modalService, $window) {
    'use strict';
    var vm = this;

    this.isArray = Array.isArray;
    $scope.loading = false;
    $scope.lstContenidoRespaldo = [];

    $scope.generateObjContrato = function() {
        debugger;
        console.log($scope.properties.informacionEnviar)
        var dataToSend = {...$scope.properties.selectedData };
        doRequest("POST", "/API/extension/AnahuacRest?url=updateCatNotificacionesSDAE&p=0&c=10", null, dataToSend, function(response) {
            $window.location.reload();
            swal("Ok", "Guardado", "success").then(function(){
                // $scope.properties.selectedIndex = 0 ;
                debugger;
                $window.location.reload();
            });
            // $window.location.assign("/portal/resource/app/administrativo/SDAEcartas/content/?app=administrativo");
        });
        // if (angular.isDefined($scope.properties.taskId)) {
        //     $scope.asignarTarea();
        // } else {
        //     if (angular.isDefined($scope.properties.selectedData)) {
        //         var dataToSend = {...$scope.properties.selectedData }
        //         doRequest("POST", "/API/extension/AnahuacRest?url=updateCatNotificaciones&p=0&c=10", null, dataToSend, function(response) {
        //             debugger;
        //             $window.location.assign("/portal/resource/app/administrativo/SDAEcartas/content/?app=administrativo");
        //         })
        //     } else {
        //         console.error("los datos a enviar no están definidos");
        //     }
        // }
    }

    $scope.sendData = function() {
        debugger;
        if ($scope.loading == false) {
            $("#loading").modal("show");
            $scope.loading = true;
            if ($scope.properties.isModificacion === false) {
                $scope.properties.nuevosValores.forEach(element => {
                    $scope.properties.contenido.push(element);
                })
                $scope.properties.nuevosValores = [];
            } else {
                $scope.properties.contenido[$scope.properties.index].clave = $scope.properties.nuevosValores[0].clave;
                $scope.properties.contenido[$scope.properties.index].descripcion = $scope.properties.nuevosValores[0].descripcion;
                $scope.properties.contenido[$scope.properties.index].fechaCreacion = $scope.properties.nuevosValores[0].fechaCreacion;
                $scope.properties.contenido[$scope.properties.index].usuarioCreacion = $scope.properties.nuevosValores[0].usuarioCreacion;
                $scope.properties.nuevosValores = [];
            }
            if ($scope.properties.taskId == undefined) {
                doRequest("GET", `../API/bpm/task?p=0&c=10&f=caseId=${caseId}&f=isFailed%3dfalse`, null, null, function(value) {
                    vm.busy = false;
                    $scope.taskId = value[0].id;
                    $scope.properties.taskId = value[0].id;
                    redireccionarTarea();
                })
            } else {
                if (angular.isDefined($scope.properties.taskId)) {
                    $scope.asignarTarea();
                } else {
                    if (angular.isDefined($scope.properties.selectedData)) {
                        var dataToSend = {...$scope.properties.selectedData }
                        doRequest("POST", "/API/extension/AnahuacRest?url=updateCatNotificacionesSDAE&p=0&c=10", null, dataToSend, function(response) {
                            $window.location.assign("/portal/resource/app/administrativo/SDAECartas/content/?app=administrativo");
                        })
                    } else {
                        console.error("los datos a enviar no están definidos");
                    }
                }
            }

        } else {
            console.log("click doble");
        }
    }

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal() {
        modalService.close();
    }

    $scope.asignarTarea = function() {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + $scope.properties.taskId,
            data: angular.copy({ "assigned_id": "" })
        };

        return $http(req)
            .success(function(data, status) {
                redireccionarTarea();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    function redireccionarTarea() {
        var req = {
            method: "PUT",
            url: `/bonita/API/bpm/humanTask/${$scope.properties.taskId}`,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };

        return $http(req)
            .success(function(data, status) {
                $scope.submitTask();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }
    
    $scope.validar = function() {
        var error = false;
        var texto = "";
        var info = "";
        $scope.properties.selectedData.clave = $scope.properties.selectedData.codigo;
        if ($scope.properties.selectedData.asunto == "") {
            error = true;
            texto = "Faltó capturar información: Asunto";
            info = "¡Aviso!"
        } else if ($scope.properties.selectedData.contenidoCorreo == "" || $scope.properties.selectedData.contenidoCorreo == "<p></p>") {
            error = true;
            texto = "Faltó capturar información: Contenido de correo";
            info = "¡Aviso!"
        } else if ($scope.properties.selectedData.clave == "") {
            error = true;
            texto = "Faltó capturar información: Clave";
            info = "¡Aviso!"
        }
        var count=0;
        for (let index = 0; index < $scope.properties.contenido.length; index++) {
            const element = $scope.properties.contenido[index];
            if ($scope.properties.selectedData.codigo == element.codigo) {
                count++;
                if(count>1){
                    error = true;
                    texto = "No puede haber dos claves iguales";
                    info = "¡Aviso!"
                }
                
            }
        }
        if (error) {
            swal(info, texto, "warning");
        }

        return error;
    }
    
    $scope.submitTask = function() {
        debugger;
        if (!$scope.validar()) {
            if ($scope.properties.informacionEnviar == null || $scope.properties.informacionEnviar == {} || $scope.properties.informacionEnviar == undefined) {
                $scope.getDataForSubmit();
            } else {
                var req = {
                    method: "POST",
                    url: "/bonita/API/bpm/userTask/" + $scope.properties.taskId + "/execution?assign=false",
                    data: angular.copy($scope.properties.informacionEnviar)
                };

                return $http(req)
                    .success(function(data, status) {
                        //console.log("$scope.properties.informacionEnviar");
                        //console.log($scope.properties.informacionEnviar);
                        $scope.getConsulta();
                    })
                    .error(function(data, status) {
                        $("#loading").modal("hide");
                        $scope.loading = false;
                        // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                    })
                    .finally(function() {});
            }
        }

    }

    $scope.getConsulta = function() {
        var req = {
            method: "GET",
            url: $scope.properties.urlConsulta + "&f=codigo=" + $scope.properties.selectedData.codigo
        };

        return $http(req)
            .success(function(data, status) {
                //console.log("data");
                //console.log(data);
                $scope.properties.contenido = data;
                $scope.getObjTaskInformation();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    $scope.getObjTaskInformation = function() {
        var req = {
            method: "GET",
            url: $scope.properties.urlTaskInformation
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.objTaskInformation = data;
                $scope.loading = false;
                closeModal();
                $("#loading").modal("hide");
                $scope.properties.nuevosValores = angular.copy($scope.properties.dataToSet);
                $scope.properties.variableDestino = angular.copy($scope.properties.variableAcopiar);
                $scope.properties.ocultarTable = false;
                $scope.properties.isModificacion = false;
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    function uuidv4() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random() * 16 | 0,
                v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params, dataToSend, value) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .then(function(data, status) {
                value(data.data, null)
                vm.busy = false;
                //$scope.lstUsuarios=data.data;
            }, function myError(response) {
                vm.busy = false;
                console.error("[ERROR] " + url)
            });

    }
    $scope.getDataForSubmit = function() {
        //../API/bdm/businessData/com.anahuac.model.ProcesoCaso?q=getCaseId&f=campus={{campusSelected}}&f=proceso={{proceso}}&p=0&c=1
        var caseId = 0;
        var lstCatNotificacionesInput = [];
        var lstCatImageNotificacion = [];
        var imageHeaderDocumentInput = [];
        var imageFooterDocumentInput = []
        var docGuiaEstudioDocumentInput = [];
        var docEtapaProcesoDocumentInput = [];
        var lstCatComentariosFirmaInput = [];
        var taskId = 0;
        var context = {};
        doRequest("GET", `../API/bdm/businessData/com.anahuac.model.ProcesoCaso?q=getCaseId&f=campus=${$scope.properties.campusSelected}&f=proceso=${$scope.properties.proceso}&p=0&c=1`, null, null, function(value) {
            vm.busy = false;
            caseId = value[0].caseId;
            doRequest("GET", `../API/bdm/businessData/com.anahuac.catalogos.CatNotificaciones?q=findByCaseId&p=0&c=9999&f=caseId=${caseId}`, null, null, function(value) {
                vm.busy = false;
                debugger;
                lstCatNotificacionesInput = value;
                if ($scope.properties.selectedData.persistenceId_string == "") {
                    lstCatNotificacionesInput.push($scope.properties.selectedData);
                } else {


                    for (let index = 0; index < lstCatNotificacionesInput.length; index++) {

                        if ($scope.properties.selectedData.persistenceId_string == lstCatNotificacionesInput[index].persistenceId_string) {
                            lstCatNotificacionesInput[index] = $scope.properties.selectedData
                        }
                    }
                }
                doRequest("GET", `../API/bpm/task?p=0&c=10&f=caseId=${caseId}&f=isFailed%3dfalse`, null, null, function(value) {
                    vm.busy = false;
                    taskId = value[0].id;
                    doRequest("GET", `../API/bpm/userTask/${taskId}/context`, null, null, function(value) {
                        vm.busy = false;
                        context = value;
                        imageHeaderDocumentInput = context.imageHeader_ref.map(doc => ({
                            id: doc.id ? doc.id.toString() : null,
                            filename: doc && doc.filename ? doc.filename : null,
                            tempPath: doc && doc.tempPath ? doc.tempPath : null,
                            contentType: doc && doc.contentType ? doc.contentType : null
                        }));
                        imageFooterDocumentInput = context.imageFooter_ref.map(doc => ({
                            id: doc.id ? doc.id.toString() : null,
                            filename: doc && doc.filename ? doc.filename : null,
                            tempPath: doc && doc.tempPath ? doc.tempPath : null,
                            contentType: doc && doc.contentType ? doc.contentType : null
                        }));
                        docGuiaEstudioDocumentInput = context.docGuiaEstudio_ref.map(doc => ({
                            id: doc.id ? doc.id.toString() : null,
                            filename: doc && doc.filename ? doc.filename : null,
                            tempPath: doc && doc.tempPath ? doc.tempPath : null,
                            contentType: doc && doc.contentType ? doc.contentType : null
                        }));
                        docEtapaProcesoDocumentInput = context.docEtapaProceso_ref.map(doc => ({
                            id: doc.id ? doc.id.toString() : null,
                            filename: doc && doc.filename ? doc.filename : null,
                            tempPath: doc && doc.tempPath ? doc.tempPath : null,
                            contentType: doc && doc.contentType ? doc.contentType : null
                        }))
                        lstCatComentariosFirmaInput = $scope.properties.strPersonaFirma.lstCatComentariosFirmaInput;

                        if (context.lstCatImageNotificacion_ref.storageIds.length > 0 && ($scope.properties.lstCatImageNotificacion == null || $scope.properties.lstCatImageNotificacion == undefined)) {
                            doRequest("GET", `../${context.lstCatImageNotificacion_ref.link}`, null, null, function(value) {
                                vm.busy = false;
                                lstCatImageNotificacion = value;
                                var generated = {
                                    "lstCatNotificacionesInput": lstCatNotificacionesInput,
                                    "lstCatImageNotificacionInput": lstCatImageNotificacion,
                                    "imageHeaderDocumentInput": imageHeaderDocumentInput,
                                    "imageFooterDocumentInput": imageFooterDocumentInput,
                                    "docGuiaEstudioDocumentInput": docGuiaEstudioDocumentInput,
                                    "docEtapaProcesoDocumentInput": docEtapaProcesoDocumentInput,
                                    "docEtapaProcesoDocumentInput": docEtapaProcesoDocumentInput,
                                    "lstCatComentariosFirmaInput": lstCatComentariosFirmaInput

                                }
                                var hasDocument = false;
                                for (let index = 0; index < generated.docGuiaEstudioDocumentInput.length; index++) {
                                    if (generated.docGuiaEstudioDocumentInput[index].id != null || generated.docGuiaEstudioDocumentInput[index].filename != null) {
                                        hasDocument = true;
                                    }

                                }
                                if (!hasDocument) {
                                    generated.docGuiaEstudioDocumentInput = [];
                                }
                                console.log(generated);
                                doRequest("POST", `/bonita/API/bpm/userTask/${$scope.properties.taskId}/execution?assign=false`, null, generated, function(value) {
                                    vm.busy = false;
                                    $scope.getConsulta();
                                })
                            })
                        } else {
                            var generated = {
                                "lstCatNotificacionesInput": lstCatNotificacionesInput,
                                "lstCatImageNotificacionInput": [],
                                "imageHeaderDocumentInput": imageHeaderDocumentInput,
                                "imageFooterDocumentInput": imageFooterDocumentInput,
                                "docGuiaEstudioDocumentInput": docGuiaEstudioDocumentInput,
                                "docEtapaProcesoDocumentInput": docEtapaProcesoDocumentInput,
                                "lstCatComentariosFirmaInput": lstCatComentariosFirmaInput

                            }
                            generated.lstCatImageNotificacionInput = ($scope.properties.lstCatImageNotificacion == null || $scope.properties.lstCatImageNotificacion == undefined) ? [] : $scope.properties.lstCatImageNotificacion.map(it => ({
                                persistenceId_string: it.persistenceId_string !== undefined ? it.persistenceId_string : null,
                                descripcion: it.descripcion !== undefined ? it.descripcion : null,
                                texto: it.texto !== undefined ? it.texto : null,
                                caseId: it.caseId !== undefined ? it.caseId : null,
                                titulo: it.titulo !== undefined ? it.titulo : null,
                                codigo: it.codigo !== undefined ? it.codigo : null
                            }));
                            generated.docEtapaProcesoDocumentInput = ($scope.properties.docEtapaProcesoDocument == null || $scope.properties.docEtapaProcesoDocument == undefined) ? docEtapaProcesoDocumentInput : $scope.properties.docEtapaProcesoDocument.map(doc => ({
                                id: doc.id ? doc.id.toString() : null,
                                filename: doc && doc.filename ? doc.filename : null,
                                tempPath: doc && doc.tempPath ? doc.tempPath : null,
                                contentType: doc && doc.contentType ? doc.contentType : null
                            }));
                            generated.docGuiaEstudioDocumentInput = ($scope.properties.docGuiaEstudioDocument == null || $scope.properties.docGuiaEstudioDocument == undefined) ? docGuiaEstudioDocumentInput : $scope.properties.docGuiaEstudioDocument.map(doc => ({
                                id: doc.id ? doc.id.toString() : null,
                                filename: doc && doc.filename ? doc.filename : $scope.properties.docGuiaEstudio && $scope.properties.docGuiaEstudio.filename ? $scope.properties.docGuiaEstudio.filename : null,
                                tempPath: doc && doc.tempPath ? doc.tempPath : $scope.properties.docGuiaEstudio && $scope.properties.docGuiaEstudio.tempPath ? $scope.properties.docGuiaEstudio.tempPath : null,
                                contentType: doc && doc.contentType ? doc.contentType : $scope.properties.docGuiaEstudio && $scope.properties.docGuiaEstudio.contentType ? $scope.properties.docGuiaEstudio.contentType : null
                            }));
                            generated.lstCatComentariosFirmaInput = ($scope.properties.lstCatComentariosFirma == null || $scope.properties.lstCatComentariosFirma == undefined) ? lstCatComentariosFirmaInput : $scope.properties.lstCatComentariosFirma;
                            for (let index = 0; index < generated.lstCatNotificacionesInput.length; index++) {
                                if (generated.lstCatNotificacionesInput[index].codigo == $scope.properties.selectedData.codigo) {
                                    //generated.lstCatNotificacionesInput[index].docGuiaEstudio = $scope.properties.docGuiaEstudio.filename;
                                    generated.lstCatNotificacionesInput[index].docGuiaEstudio = $scope.properties.selectedData.docGuiaEstudio;
                                }
                            }
                            var hasDocument = false;
                            for (let index = 0; index < generated.docGuiaEstudioDocumentInput.length; index++) {
                                if (generated.docGuiaEstudioDocumentInput[index].id != null || generated.docGuiaEstudioDocumentInput[index].filename != null) {
                                    hasDocument = true;
                                }

                            }
                            if (!hasDocument) {
                                generated.docGuiaEstudioDocumentInput = [];
                            }
                            console.log(generated);
                            debugger;
                            doRequest("POST", `/bonita/API/bpm/userTask/${$scope.properties.taskId}/execution?assign=false`, null, generated, function(value) {
                                vm.busy = false;
                                //$scope.getConsulta();
                                $window.location.assign("/portal/resource/app/administrativo/SDAECartas/content/?app=administrativo");
                            })

                        }
                    })
                })
            })
        })
    }
}