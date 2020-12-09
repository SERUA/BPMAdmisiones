function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        if ($scope.properties.action === 'Remove from collection') {
            removeFromCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Add to collection') {
            addToCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Start process') {
            if (!$scope.properties.formInput.catSolicitudDeAdmisionInput.datosVeridicos) {
                swal("Aviso!", "Debe aceptar que los datos ingresados son veridicos!", "warning");
            } else if (!$scope.properties.formInput.catSolicitudDeAdmisionInput.aceptoAvisoPrivacidad) {
                swal("Aviso!", "Debe aceptar el aviso de privacidad!", "warning");
            } else if (!$scope.properties.formInput.catSolicitudDeAdmisionInput.confirmarAutorDatos) {
                swal("Aviso!", "Debe aceptar que confirma que es el auto de los datos de este formulario!", "warning");
            } else {
                startProcess();
            }
        } else if ($scope.properties.action === 'Submit task') {
            console.log("Enviara a guardar");
            debugger;
            if($scope.properties.fotopasaportearchivo.length > 0){
                var auxData = null;
                if ($scope.properties.fotopasaportearchivo[0].newValue === undefined) {
                    auxData = $scope.properties.fotopasaportearchivo[0];
                } else {
                    auxData = angular.copy($scope.properties.fotopasaportearchivo[0].newValue);
                }
                auxData.filename = $scope.properties.fotopasaporte === undefined?null:($scope.properties.fotopasaporte.filename === '' ? null : $scope.properties.fotopasaporte.filename)
                auxData.tempPath = $scope.properties.fotopasaporte === undefined?null:($scope.properties.fotopasaporte.tempPath === ''?null:$scope.properties.fotopasaporte.tempPath);
                auxData.contentType = $scope.properties.fotopasaporte === undefined?null:($scope.properties.fotopasaporte.contentType === ''?null:$scope.properties.fotopasaporte.contentType);
                if (auxData.id !== undefined) {
                    $scope.properties.fotopasaportearchivo[0] = {
                        "id": angular.copy(auxData.id),
                        "newValue": angular.copy(auxData)
                    };
                } else {
                    if ($scope.properties.fotopasaportearchivo[0].newValue.filename !== $scope.properties.fotopasaporte.filename) {
                        $scope.properties.fotopasaportearchivo[0] = ({
                            "newValue": angular.copy(auxData)
                        });
                    }
                }
            } else{
				if($scope.properties.fotopasaporte !== undefined){
					$scope.properties.fotopasaportearchivo = [];
					$scope.properties.fotopasaportearchivo.push({
						"newValue": angular.copy($scope.properties.fotopasaporte)
					});
				}
            }
            
            if($scope.properties.actanacimientoarchivo.length > 0){
                var auxData = null;
                if ($scope.properties.actanacimientoarchivo[0].newValue === undefined) {
                    auxData = $scope.properties.actanacimientoarchivo[0];
                } else {
                    auxData = angular.copy($scope.properties.actanacimientoarchivo[0].newValue);
                }
                auxData.filename = $scope.properties.actanacimiento === undefined?null:($scope.properties.actanacimiento.filename === ''?null:$scope.properties.actanacimiento.filename);
                auxData.tempPath = $scope.properties.actanacimiento === undefined?null:($scope.properties.actanacimiento.tempPath === ''?null:$scope.properties.actanacimiento.tempPath);
                auxData.contentType = $scope.properties.actanacimiento === undefined?null:($scope.properties.actanacimiento.contentType === ''?null:$scope.properties.actanacimiento.contentType);
                if (auxData.id !== undefined) {
                    $scope.properties.actanacimientoarchivo[0] = {
                        "id": angular.copy(auxData.id),
                        "newValue": angular.copy(auxData)
                    };
                } else {
                    if ($scope.properties.actanacimientoarchivo[0].newValue.filename !== $scope.properties.actanacimiento.filename) {
                        $scope.properties.actanacimientoarchivo[0] = ({
                            "newValue": angular.copy(auxData)
                        });
                    }
                }
            }else{
				if($scope.properties.actanacimiento !== undefined){
					$scope.properties.actanacimientoarchivo = [];
					$scope.properties.actanacimientoarchivo.push({
						"newValue": angular.copy($scope.properties.actanacimiento)
					});
				}
            }
            
            if($scope.properties.constanciaarchivo.length > 0){
                var auxData = null;
                if ($scope.properties.constanciaarchivo[0].newValue === undefined) {
                    auxData = $scope.properties.constanciaarchivo[0];
                } else {
                    auxData = angular.copy($scope.properties.constanciaarchivo[0].newValue);
                }
                auxData.filename = $scope.properties.constancia === undefined?null:($scope.properties.constancia.filename === ''?null:$scope.properties.constancia.filename);
                auxData.tempPath = $scope.properties.constancia === undefined?null:($scope.properties.constancia.tempPath === ''?null:$scope.properties.constancia.tempPath);
                auxData.contentType = $scope.properties.constancia === undefined?null:($scope.properties.constancia.contentType === ''?null:$scope.properties.constancia.contentType);
                if (auxData.id !== undefined) {
                    $scope.properties.constanciaarchivo[0] = {
                        "id": angular.copy(auxData.id),
                        "newValue": angular.copy(auxData)
                    };
                } else {
                    if ($scope.properties.constanciaarchivo[0].newValue.filename !== $scope.properties.constancia.filename) {
                        $scope.properties.constanciaarchivo[0] = ({
                            "newValue": angular.copy(auxData)
                        });
                    }
                }
            }else{
				if($scope.properties.constancia !== undefined){
					$scope.properties.constanciaarchivo = [];
					$scope.properties.constanciaarchivo.push({
						"newValue": angular.copy($scope.properties.constancia)
					});
				}
            }
            
            if($scope.properties.descuentoarchivo.length > 0){
                var auxData = null;
                if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                    auxData = $scope.properties.descuentoarchivo[0];
                } else {
                    auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                }
                auxData.filename = $scope.properties.descuento === undefined?null:($scope.properties.descuento.filename === ''?null:$scope.properties.descuento.filename);
                auxData.tempPath = $scope.properties.descuento === undefined?null:($scope.properties.descuento.tempPath === ''?null:$scope.properties.descuento.tempPath);
                auxData.contentType = $scope.properties.descuento === undefined?null:($scope.properties.descuento.contentType === ''?null:$scope.properties.descuento.contentType);
                if (auxData.id !== undefined) {
                    $scope.properties.descuentoarchivo[0] = {
                        "id": angular.copy(auxData.id),
                        "newValue": angular.copy(auxData)
                    };
                } else {
                    if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                        $scope.properties.descuentoarchivo[0] = ({
                            "newValue": angular.copy(auxData)
                        });
                    }
                }
            }else{
				if($scope.properties.descuento !== undefined && $scope.properties.descuento !=="" && $scope.properties.descuento !== null){
					$scope.properties.descuentoarchivo = [];
					$scope.properties.descuentoarchivo.push({
						"newValue": angular.copy($scope.properties.descuento)
					});
				}
            }
            
            if($scope.properties.collageBoardarchivo.length > 0){
                var auxData = null;
                if ($scope.properties.collageBoardarchivo[0].newValue === undefined) {
                    auxData = $scope.properties.collageBoardarchivo[0];
                } else {
                    auxData = angular.copy($scope.properties.collageBoardarchivo[0].newValue);
                }
                auxData.filename = $scope.properties.collageBoard === undefined?null:($scope.properties.collageBoard.filename === ''?null:$scope.properties.collageBoard.filename);
                auxData.tempPath = $scope.properties.collageBoard === undefined?null:($scope.properties.collageBoard.tempPath === ''?null:$scope.properties.collageBoard.tempPath);
                auxData.contentType = $scope.properties.collageBoard === undefined?null:($scope.properties.collageBoard.contentType === ''?null:$scope.properties.collageBoard.contentType);
                if (auxData.id !== undefined) {
                    $scope.properties.collageBoardarchivo[0] = {
                        "id": angular.copy(auxData.id),
                        "newValue": angular.copy(auxData)
                    };
                } else {
                    if ($scope.properties.collageBoardarchivo[0].newValue.filename !== $scope.properties.collageBoard.filename) {
                        $scope.properties.collageBoardarchivo[0] = ({
                            "newValue": angular.copy(auxData)
                        });
                    }
                }
            }else{
				if($scope.properties.collageBoard !== undefined){
					$scope.properties.collageBoardarchivo = [];
					$scope.properties.collageBoardarchivo.push({
						"newValue": angular.copy($scope.properties.collageBoard)
					});
				}
            }
            
            $scope.properties.dataToSend.isEnviarSolicitudCont = false;
            if($scope.properties.selectedIndex === undefined){
                $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndex = null;
            }else{
                $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndex = $scope.properties.selectedIndex;
            }
            if($scope.properties.selectedIndexPersonal === undefined){
                $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndexPersonal = null;
            }else{
                $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndexPersonal = $scope.properties.selectedIndexPersonal;
            }
            if($scope.properties.selectedIndexFamiliar === undefined){
                $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndexFamiliar = null;
            }else{
                $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndexFamiliar = $scope.properties.selectedIndexFamiliar;
            }
            if($scope.properties.selectedIndexRevision === undefined){
                $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndexRevision = null;
            }else{
                $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndexRevision = $scope.properties.selectedIndexRevision;
            }
            console.log($scope.properties.dataToSend);
            $scope.assignTask();

        } else if ($scope.properties.action === 'Open modal') {
            closeModal($scope.properties.closeOnSuccess);
            openModal($scope.properties.modalId);
        } else if ($scope.properties.action === 'Close modal') {
            closeModal(true);
        } else if ($scope.properties.url) {
            doRequest($scope.properties.action, $scope.properties.url);
        }
    };

    $scope.assignTask = function() {
        //$scope.showModal();
        let url = "../API/bpm/userTask/" + $scope.properties.taskId;

        var req = {
            method: "PUT",
            url: url,
            data: {
                "assigned_id": $scope.properties.userId
            }
        };

        return $http(req).success(function(data, status) {
                //$scope.executeTask();
                submitTask();
            })
            .error(function(data, status) {
                $scope.hideModal();
                swal("Error", data.message, "error");
            })
            .finally(function() {

            });
    }

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
        vm.busy = true;
        console.log($scope.properties.dataToSend);
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
                closeModal($scope.properties.closeOnSuccess);
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
        //id = getUrlParam('id');
        id = $scope.properties.taskId;
        if (id) {
            var params = getUserParam();
            params.assign = $scope.properties.assign;
            doRequest('POST', '../API/bpm/userTask/' + id + '/execution', params).then(function() {
                localStorageService.delete($window.location.href);
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }

}