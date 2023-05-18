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
            blockUI.start();
            vm.bloqueo = true;
            console.log("Alerta");

            $scope.properties.dataToSend.catRegistroInput.nombreusuario = $scope.properties.strRegistro.CorreoElectronico;
            $scope.properties.dataToSend.catRegistroInput.primernombre = $scope.properties.strRegistro.PrimerNombre;
            $scope.properties.dataToSend.catRegistroInput.segundonombre = $scope.properties.strRegistro.SegundoNombre;
            $scope.properties.dataToSend.catRegistroInput.apellidopaterno = $scope.properties.strRegistro.ApellidoPaterno;
            $scope.properties.dataToSend.catRegistroInput.apellidomaterno = $scope.properties.strRegistro.ApellidoMaterno;
            $scope.properties.dataToSend.catRegistroInput.correoelectronico = $scope.properties.strRegistro.CorreoElectronico;
            $scope.properties.dataToSend.catRegistroInput.password = $scope.properties.strRegistro.Password;
            $scope.properties.dataToSend.catRegistroInput.isEliminado = false;
            $scope.properties.dataToSend.catRegistroInput.ayuda = $scope.properties.strRegistro.Ayuda;

            $scope.strBonita = {
                "nombreusuario": $scope.properties.strRegistro.CorreoElectronico,
                "nombre": $scope.properties.strRegistro.PrimerNombre + " " + $scope.properties.strRegistro.SegundoNombre,
                "apellido": $scope.properties.strRegistro.ApellidoPaterno + " " + $scope.properties.strRegistro.ApellidoMaterno,
                "password": $scope.properties.strRegistro.Password,
                "campus": $scope.properties.dataToSend.catSolicitudDeAdmisionInput.catCampus.grupoBonita,
                "numeroContacto":$scope.properties.strRegistro.numeroContacto
            }

            if (!$scope.properties.strRegistro.Validado || $scope.properties.strRegistro.error) {
                swal("¡" + $scope.properties.campoError + "!", $scope.properties.erroMessage, "warning");
                blockUI.stop();
                vm.bloqueo = false;
            } else {
                var req = {
                    method: "GET",
                    url: "../API/extension/AnahuacRestGet?url=getDatosUsername&p=0&c=10&username=" + $scope.properties.strRegistro.CorreoElectronico
                };
                return $http(req).success(function(data, status) {
                        $scope.properties.getUserBonita = data.data;
                        if ($scope.properties.getUserBonita !== undefined) {
                            if ($scope.properties.getUserBonita.length > 0) {
                                swal("Error", "Este correo electrónico ya está registrado.", "error");
                                blockUI.stop();
                                vm.bloqueo = false;
                            } else {
                                startProcess();
                            }
                        }
                    }).error(function(data, status) {
                        $scope.properties.dataFromError = data;
                        $scope.properties.responseStatusCode = status;
                        $scope.properties.dataFromSuccess = undefined;
                    })
                    .finally(function() {
                        blockUI.stop();
                       
                    });
            }
        } else if ($scope.properties.action === 'Submit task') {
            submitTask();
        } else if ($scope.properties.action === 'Open modal') {
            closeModal($scope.properties.closeOnSuccess);
            openModal($scope.properties.modalId);
        } else if ($scope.properties.action === 'Close modal') {
            closeModal(true);
        } else if ($scope.properties.url) {
            doRequest($scope.properties.action, $scope.properties.url);
        }
    };

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if (shouldClose) {
            modalService.close();
        }
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
        var url = '../API/extension/Registro?url=instantiation&p=0&c=10';
        var prom = doRequest('POST', url, getUserParam()).then(function() {
            localStorageService.delete($window.location.href);
        });

        /*INTEGRAACIÓN*/
        //var prom = doRequest('POST', '../API/bpm/process/4768112034171842957/instantiation', getUserParam()).then(function() {
        //localStorageService.delete($window.location.href);
        // });

        /*PRE PRODUCCIÓN*/
        //var prom = doRequest('POST', '../API/bpm/process/8130793842527796733/instantiation', getUserParam()).then(function() {
        //localStorageService.delete($window.location.href);
        //});

    }

    $scope.showLoading = function() {
        $("#loading").modal("show");
    }

    $scope.hideLoading = function() {
        $("#loading").modal("hide");
    }

    $scope.registrarBonita = function() {
        //$scope.showLoading();
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=RegistrarUsuario&p=0&c=10",
            data: angular.copy($scope.strBonita)
        };

        return $http(req).success(function(data, status) {
                /*if($scope.properties.strRegistro.Ayuda === true){
                    $scope.necesitaAyuda();
                } else {*/
                if (data.success) {
                    console.log("Modificación de información en registrar bonita");
                    $scope.properties.navigationVar = "formSuccess";
                    blockUI.stop();
                } else {
                    swal("Error", JSON.stringify(data.error), "error");
                    blockUI.stop();
                    
                }
                vm.bloqueo = false;
                //}
            })
            .error(function(data, status) {
                if (data.error.includes("A user with name")) {
                    swal("Error", "Este correo electrónico ya está registrado.", "error");
                    blockUI.stop();
                } else {
                    swal("Error", "Ha ocurrido un error inesperado. Inténtalo de nuevo mas tarde.", "error");
                    blockUI.stop();
                }
                // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                $scope.properties.disabled = false;
                blockUI.stop();
                vm.bloqueo = false;
                //$scope.hideLoading();
            });
    }

    $scope.necesitaAyuda = function() {
        var req = {
            method: "POST",
            url: $scope.properties.urlAyuda,
            data: angular.copy($scope.properties.strBonita)
        };

        return $http(req).success(function(data, status) {
                $scope.properties.navigationVar = "formSuccess";
            })
            .error(function(data, status) {
                // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params) {
        //$scope.showLoading();

        $scope.properties.disabled = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req).success(function(data, status) {
                $scope.properties.dataFromSuccess = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;

                if ($scope.properties.action === 'Start process') {
                    $scope.registrarBonita();

                }

                notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status });

                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                closeModal($scope.properties.closeOnSuccess);
            })
            .error(function(data, status) {
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                blockUI.stop();
                //$scope.hideLoading();
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
            return { 'user': userId };
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
}