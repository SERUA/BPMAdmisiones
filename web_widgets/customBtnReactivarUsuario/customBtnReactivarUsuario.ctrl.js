function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {

    'use strict';

    var vm = this;
    this.bloqueo = false;

    this.action = function action() {
        if ($scope.properties.action === 'Remove from collection') {
            removeFromCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Add to collection') {
            addToCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Start process') {
            startProcess();
        } else if ($scope.properties.action === 'Submit task') {
            submitTask();
        } else if ($scope.properties.action === 'Open modal') {
            closeModal($scope.properties.closeOnSuccess);
            openModal($scope.properties.modalId);
        } else if ($scope.properties.action === 'Close modal') {
            closeModal(true);
        } else if ($scope.properties.url) {
            vm.bloqueo = true;
            
            var existecambio = false;
            var isTransferencia = false;
            if($scope.properties.valoresSolicitante.correoelectronico.includes("(rechazado)") ){
                Swal.fire("¡Aviso!", "Este es un intento ya rechazado, no se puede volver a reactivar", "warning");
                vm.bloqueo = false;
            }
            else if ($scope.properties.valoresSolicitante.catCampus === null) {
               Swal.fire("¡Aviso!", "Debes seleccionar un campus", "warning");
               vm.bloqueo = false;
            } else if ($scope.properties.valoresSolicitante.catLicenciatura === null) {
                Swal.fire("¡Aviso!", "Debes seleccionar una licenciatura", "warning");
                vm.bloqueo = false;
            } else if ($scope.properties.valoresSolicitante.periodo === null || $scope.properties.valoresSolicitante.periodo === undefined) {
                Swal.fire("¡Aviso!", "Debes seleccionar un período", "warning");
                vm.bloqueo = false;
            } else if ($scope.properties.valoresSolicitante.catLicenciatura.propedeutico && $scope.properties.valoresSolicitante.catPropedeutico === undefined) {
                Swal.fire("¡Aviso!", "Debes seleccionar un curso propedéutico", "warning");
                vm.bloqueo = false;
            } else if ($scope.properties.valoresSolicitante.catLugarExamen === null) {
                Swal.fire("¡Aviso!", "Debes seleccionar un lugar de examen", "warning");
                vm.bloqueo = false;
            } else if ($scope.properties.valoresSolicitante.catLugarExamen.descripcion === "En un estado" && $scope.properties.valoresSolicitante.catEstadoExamen === null) {
                Swal.fire("¡Aviso!", "Debes seleccionar un estado donde realizará  el examen", "warning");
                vm.bloqueo = false;
            } else if ($scope.properties.valoresSolicitante.catLugarExamen.descripcion === "En un estado" && $scope.properties.valoresSolicitante.ciudadExamen === null) {
                Swal.fire("¡Aviso!", "Debes seleccionar una ciudad donde se realizará  el examen", "warning");
                vm.bloqueo = false;
            } else if ($scope.properties.valoresSolicitante.catLugarExamen.descripcion === "En el extranjero (solo si vives fuera de México)" && $scope.properties.valoresSolicitante.catPaisExamen === null) {
                Swal.fire("¡Aviso!", "Debes seleccionar un pais donde se realizará  el examen", "warning");
                vm.bloqueo = false;
            } else if ($scope.properties.valoresSolicitante.catLugarExamen.descripcion === "En el extranjero (solo si vives fuera de México)" && $scope.properties.valoresSolicitante.ciudadExamenPais === null) {
                Swal.fire("¡Aviso!", "Debes seleccionar una ciudad donde se realizará  el examen", "warning");
                vm.bloqueo = false;
            } else if((parseInt($scope.properties.valoresSolicitante.countrechazos) + 1 ) >= 2){
                 Swal.fire("¡Aviso!", "No se le puede dar una tercera oportunidad al aspirante", "warning");
                 vm.bloqueo = false;
            } else {
                $scope.properties.JSONTransferencia.caseid = $scope.properties.valoresSolicitante.caseid;
                $scope.properties.JSONTransferencia.licenciatura = $scope.properties.valoresSolicitante.catLicenciatura.persistenceId;
                if (!$scope.properties.valoresSolicitante.catLicenciatura.propedeutico) {
                    $scope.properties.valoresSolicitante.catPropedeutico = null
                }
                if ($scope.properties.valoresSolicitante.catPropedeutico === null) {
                    $scope.properties.JSONTransferencia.propedeutico = $scope.properties.valoresSolicitante.catPropedeutico
                } else {
                    $scope.properties.JSONTransferencia.propedeutico = $scope.properties.valoresSolicitante.catPropedeutico.persistenceId;
                }
                $scope.properties.JSONTransferencia.periodo = $scope.properties.valoresSolicitante.periodo.persistenceId;
                $scope.properties.JSONTransferencia.campusestudio = $scope.properties.valoresSolicitante.catCampusEstudio.persistenceId;
                $scope.properties.JSONTransferencia.campus = $scope.properties.valoresSolicitante.catCampus.persistenceId;

                $scope.properties.JSONTransferencia.usuario = $scope.properties.usuario.user_name;
                var nombreaspirante = "";
                if ($scope.properties.valoresSolicitante.segundonombre === "") {
                    nombreaspirante = $scope.properties.valoresSolicitante.primernombre + " " + $scope.properties.valoresSolicitante.apellidopaterno + " " + $scope.properties.valoresSolicitante.apellidomaterno;
                } else {
                    nombreaspirante = $scope.properties.valoresSolicitante.primernombre + " " + $scope.properties.valoresSolicitante.segundonombre + " " + $scope.properties.valoresSolicitante.apellidopaterno + " " + $scope.properties.valoresSolicitante.apellidomaterno;
                }
                $scope.properties.JSONTransferencia.campusAnterior = $scope.properties.jsonOriginal.campus
                $scope.properties.JSONTransferencia.campusNuevo = $scope.properties.valoresSolicitante.catCampus.descripcion
                $scope.properties.JSONTransferencia.aspirante = nombreaspirante;
                $scope.properties.JSONTransferencia.correoaspirante = $scope.properties.valoresSolicitante.correoelectronico;
                $scope.properties.JSONTransferencia.licenciaturatext = $scope.properties.valoresSolicitante.catLicenciatura.nombre;
                $scope.properties.JSONTransferencia.periodotext = $scope.properties.valoresSolicitante.periodo.descripcion;
                $scope.properties.JSONTransferencia.estatus = $scope.properties.valoresSolicitante.estatussolicitud;
                $scope.properties.JSONTransferencia.idbanner = $scope.properties.valoresSolicitante.idbanner;
                $scope.properties.JSONTransferencia.catCampus = $scope.properties.valoresSolicitante.catCampus;
                if ($scope.properties.valoresSolicitante.countrechazos === null) {
                    $scope.properties.JSONTransferencia.countrechazos = 1;
                } else {
                    $scope.properties.JSONTransferencia.countrechazos = parseInt($scope.properties.valoresSolicitante.countrechazos) + 1;
                }

                console.log($scope.properties.JSONTransferencia);
                $scope.properties.estadoSeleccionado = undefined;
                doRequest($scope.properties.action, $scope.properties.url);
                
            }
        }
    };

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }

    function removeFromCollection() {
        if ($scope.properties.collectionToModify) {
            if (!Array.isArray($scope.properties.collectionToModify)) {
                throw 'Collection property for widget button should be an array, but was ' + $scope.properties.collectionToModify;
            }
            var index = -1;
            if ($scope.properties.collectionPosition === 'First') {
                index = 0;
            } else if ($scope.properties.collectionPosition === 'Last') {
                index = $scope.properties.collectionToModify.length - 1;
            } else if ($scope.properties.collectionPosition === 'Item') {
                index = $scope.properties.collectionToModify.indexOf($scope.properties.removeItem);
            }

            // Only remove element for valid index
            if (index !== -1) {
                $scope.properties.collectionToModify.splice(index, 1);
            }
        }
    }

    function addToCollection() {
        if (!$scope.properties.collectionToModify) {
            $scope.properties.collectionToModify = [];
        }
        if (!Array.isArray($scope.properties.collectionToModify)) {
            throw 'Collection property for widget button should be an array, but was ' + $scope.properties.collectionToModify;
        }
        var item = angular.copy($scope.properties.valueToAdd);

        if ($scope.properties.collectionPosition === 'First') {
            $scope.properties.collectionToModify.unshift(item);
        } else {
            $scope.properties.collectionToModify.push(item);
        }
    }

    function startProcess() {
        var id = getUrlParam('id');
        if (id) {
            var prom = doRequest('POST', '../API/bpm/process/' + id + '/instantiation', getUserParam()).then(function() {
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
    }

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params) {
        blockUI.start();
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.dataFromSuccess = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
                notifyParentFrame({
                    message: 'success',
                    status: status,
                    dataFromSuccess: data,
                    dataFromError: undefined,
                    responseStatusCode: status
                });
                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                //sendMail($scope.properties.dataToSend);
                getLstAspirantesTransferencia("POST", $scope.properties.urlPost);

            })
            .error(function(data, status) {
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({
                    message: 'error',
                    status: status,
                    dataFromError: data,
                    dataFromSuccess: undefined,
                    responseStatusCode: status
                });
            })
            .finally(function() {
                vm.busy = false;
                vm.bloqueo = false;
            });
    }

    function redirectIfNeeded() {
        var iframeId = $window.frameElement ? $window.frameElement.id : null;
        //Redirect only if we are not in the portal or a living app
        if (!iframeId || iframeId && iframeId.indexOf('bonitaframe') !== 0) {
            $window.location.assign($scope.properties.targetUrlOnSuccess);
        }
    }

    function notifyParentFrame(additionalProperties) {
        if ($window.parent !== $window.self) {
            var dataToSend = angular.extend({}, $scope.properties, additionalProperties);
            $window.parent.postMessage(JSON.stringify(dataToSend), '*');
        }
    }

    function getUserParam() {
        var userId = getUrlParam('user');
        if (userId) {
            return {
                'user': userId
            };
        }
        return {};
    }

    /**
     * Extract the param value from a URL query
     * e.g. if param = "id", it extracts the id value in the following cases:
     *  1. http://localhost/bonita/portal/resource/process/ProcName/1.0/content/?id=8880000
     *  2. http://localhost/bonita/portal/resource/process/ProcName/1.0/content/?param=value&id=8880000&locale=en
     *  3. http://localhost/bonita/portal/resource/process/ProcName/1.0/content/?param=value&id=8880000&locale=en#hash=value
     * @returns {id}
     */
    function getUrlParam(param) {
        var paramValue = $location.absUrl().match('[//?&]' + param + '=([^&#]*)($|[&#])');
        if (paramValue) {
            return paramValue[1];
        }
        return '';
    }

    function submitTask() {
        var id;
        id = getUrlParam('id');
        if (id) {
            var params = getUserParam();
            params.assign = $scope.properties.assign;
            doRequest('POST', '../API/bpm/userTask/' + getUrlParam('id') + '/execution', params).then(function() {
                localStorageService.delete($window.location.href);
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }

    function getLstAspirantesTransferencia(method, url, params) {
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.jsonenviar),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.lstContenido = data.data;
                swal("¡Aspirante transferido correctamente!", "Se ha transferido al aspirante correctamente", "success");
            })
            .error(function(data, status) {
                notifyParentFrame({
                    message: 'error',
                    status: status,
                    dataFromError: data,
                    dataFromSuccess: undefined,
                    responseStatusCode: status
                });
            })
            .finally(function() {
                blockUI.stop();
                closeModal($scope.properties.closeOnSuccess);
            });
    }

    //  $scope.sendMail=function(row) {
    function sendMail(row) {

        if (row.catCampus.grupoBonita == undefined) {
            for (var i = 0; i < $scope.lstCampus.length; i++) {
                if ($scope.lstCampus[i].descripcion == row.catCampus.descripcion) {
                    row.catCampus.grupoBonita = $scope.lstCampus[i].valor;
                }
            }
        }
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10",
            data: angular.copy({
                "campus": row.catCampus.grupoBonita,
                "correo": row.correoaspirante,
                "codigo": "transferencia",
                "isEnviar": true,
                "mensaje": ""
            })
        };
        //row.correoaspirante
        return $http(req)
            .success(function(data, status) {
                getLstAspirantesTransferencia("POST", $scope.properties.urlPost);
                //$scope.envelopeCancel();
            })
            .error(function(data, status) {
                console.error(data)
            })
            .finally(function() {});
    }

}