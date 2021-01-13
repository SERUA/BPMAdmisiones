function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;
    var validarPeriodoDisponible;
    var validarInscripcionagosto;
    var validarInscripcionenero;
    var validarPropedeuticos;
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
            doRequest($scope.properties.action, $scope.properties.url);
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
        if ($scope.properties.dataToChange2.campus) { // validacion editar
            // if ($scope.properties.dataToChange2.nombre && $scope.properties.dataToChange2.descripcion && $scope.properties.dataToChange2.enlace) {
            if ($scope.properties.dataToChange2.nombre && $scope.properties.dataToChange2.descripcion) {
                if ($scope.properties.dataToChange2.propedeutico === false) {
                    $scope.properties.dataToChange2.propedeuticos = [];
                    if ($scope.properties.dataToChange2.periodoDisponible <= 0) {
                        validarPeriodoDisponible = false
                        swal("¡Seleccione por lo menos un!", "Periodo disponible", "warning");
                    } else {
                        validarPeriodoDisponible = true;
                    }
                    if ($scope.properties.dataToChange2.inscripcionagosto) {
                        validarInscripcionagosto = true;
                    } else {
                        swal("¡Aviso!", "Faltó capturar información en: Inscripcion Agosto 2021", "warning");
                        validarInscripcionagosto = false;
                    }
                    if ($scope.properties.dataToChange2.inscripcionenero) {
                        validarInscripcionenero = true;
                    } else {
                        swal("¡Aviso!", "Faltó capturar información en: Inscripción Enero 2021", "warning");
                        validarInscripcionenero = false;
                    }
                    if (validarPeriodoDisponible === true && validarInscripcionagosto === true && validarInscripcionenero === true) {
                        if ($scope.properties.processId) {
                            var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function () {
                                doRequest("GET", $scope.properties.url).then(function () {
                                    $scope.properties.dataToChange = $scope.properties.dataToSet;
                                    $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                                });
                                localStorageService.delete($window.location.href);
                            });

                        } else {
                            $log.log('Impossible to retrieve the process definition id value from the URL');
                        }
                    }
                }
                if ($scope.properties.dataToChange2.propedeutico === true) {
                    if ($scope.properties.dataToChange2.periodoDisponible <= 0) {
                        validarPeriodoDisponible = false
                        swal("¡Seleccione por lo menos un!", "Periodo disponible", "warning");
                    } else {
                        validarPeriodoDisponible = true;
                    }
                    if ($scope.properties.dataToChange2.inscripcionagosto) {
                        validarInscripcionagosto = true;
                    } else {
                        swal("¡Aviso!", "Faltó capturar información en: Inscripcion Agosto 2021", "warning");
                        validarInscripcionagosto = false;
                    }
                    if ($scope.properties.dataToChange2.inscripcionenero) {
                        validarInscripcionenero = true;
                    } else {
                        swal("¡Aviso!", "Faltó capturar información en: Inscripción Enero 2021", "warning");
                        validarInscripcionenero = false;
                    }
                    if ($scope.properties.dataToChange2.propedeuticos <= 0) {
                        swal("¡Seleccione por lo menos un!", "Propedéutico disponible", "warning");
                        validarPropedeuticos = false;
                    } else {
                        validarPropedeuticos = true;
                    }
                    if (validarPeriodoDisponible === true && validarInscripcionagosto === true && validarInscripcionenero === true && validarPropedeuticos === true) {
                        if ($scope.properties.processId) {
                            var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function () {
                                doRequest("GET", $scope.properties.url).then(function () {
                                    $scope.properties.dataToChange = $scope.properties.dataToSet;
                                    $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                                });
                                localStorageService.delete($window.location.href);
                            });

                        } else {
                            $log.log('Impossible to retrieve the process definition id value from the URL');
                        }
                    }
                }
            } else {
                // if (!$scope.properties.dataToChange2.enlace) {
                //     swal("¡Aviso!", "Faltó capturar información en: Enlace de sitio web", "warning");
                // }
                if (!$scope.properties.dataToChange2.descripcion) {
                    swal("¡Aviso!", "Faltó capturar información en: Descripción de la Carrera", "warning");
                }
                if (!$scope.properties.dataToChange2.nombre) {
                    swal("¡Aviso!", "Faltó capturar información en: Nombre licenciatura", "warning");
                }
                if(!$scope.properties.dataToChange2.clave){
                    swal("¡Aviso!","Faltó capturar informacion en: Clave","warning");
                }
            }
        } else {// validacion guardar
            if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].nombre && $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].descripcion) {
            // if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].nombre && $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].descripcion && $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].enlace) {
                if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].propedeutico === false) {
                    if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].periodoDisponible <= 0) {
                        validarPeriodoDisponible = false
                        swal("¡Seleccione por lo menos un!", "Periodo disponible", "warning");
                    } else {
                        validarPeriodoDisponible = true;
                    }
                    if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionagosto) {
                        validarInscripcionagosto = true;
                    } else {
                        swal("¡Aviso!", "Faltó capturar información en: Inscripcion Agosto 2021", "warning");
                        validarInscripcionagosto = false;
                    }
                    if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionenero) {
                        validarInscripcionenero = true;
                    } else {
                        swal("¡Aviso!", "Faltó capturar información en: Inscripción Enero 2021", "warning");
                        validarInscripcionenero = false;
                    }
                    if (validarPeriodoDisponible === true && validarInscripcionagosto === true && validarInscripcionenero === true) {
                        if ($scope.properties.processId) {
                            var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function () {
                                doRequest("GET", $scope.properties.url).then(function () {
                                    $scope.properties.dataToChange = $scope.properties.dataToSet;
                                    $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                                });
                                localStorageService.delete($window.location.href);
                            });

                        } else {
                            $log.log('Impossible to retrieve the process definition id value from the URL');
                        }
                    }
                }
                if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].propedeutico === true) {
                    if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].periodoDisponible <= 0) {
                        validarPeriodoDisponible = false
                        swal("¡Seleccione por lo menos un!", "Periodo disponible", "warning");
                    } else {
                        validarPeriodoDisponible = true;
                    }
                    if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionagosto) {
                        validarInscripcionagosto = true;
                    } else {
                        swal("¡Aviso!", "Faltó capturar información en: Inscripcion Agosto 2021", "warning");
                        validarInscripcionagosto = false;
                    }
                    if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionenero) {
                        validarInscripcionenero = true;
                    } else {
                        swal("¡Aviso!", "Faltó capturar información en: Inscripción Enero 2021", "warning");
                        validarInscripcionenero = false;
                    }
                    if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].propedeuticos <= 0) {
                        swal("¡Seleccione por lo menos un!", "Propedéutico disponible", "warning");
                        validarPropedeuticos = false;
                    } else {
                        validarPropedeuticos = true;
                    }
                    if (validarPeriodoDisponible === true && validarInscripcionagosto === true && validarInscripcionenero === true && validarPropedeuticos === true) {
                        if ($scope.properties.processId) {
                            var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function () {
                                doRequest("GET", $scope.properties.url).then(function () {
                                    $scope.properties.dataToChange = $scope.properties.dataToSet;
                                    $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                                });
                                localStorageService.delete($window.location.href);
                            });

                        } else {
                            $log.log('Impossible to retrieve the process definition id value from the URL');
                        }
                    }
                }
            } else {
                // if (!$scope.properties.dataToChange2.lstCatGestionEscolarInput[0].enlace) {
                //     swal("¡Aviso!", "Faltó capturar información en: Enlace de sitio web", "warning");
                // }
                if (!$scope.properties.dataToChange2.lstCatGestionEscolarInput[0].descripcion) {
                    swal("¡Aviso!", "Faltó capturar información en: Descripción de la Carrera", "warning");
                }
                if (!$scope.properties.dataToChange2.lstCatGestionEscolarInput[0].nombre) {
                    swal("¡Aviso!", "Faltó capturar información en: Nombre licenciatura", "warning");
                }
                if (!$scope.properties.dataToChange2.lstCatGestionEscolarInput[0].clave) {
                    swal("¡Aviso!", "Faltó capturar información en: clave", "warning");
                }
            }
        }
    }

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params) {
        vm.busy = true;
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
            var params = $scope.properties.userId;
            params.assign = $scope.properties.assign;
            doRequest('POST', '../API/bpm/userTask/' + getUrlParam('id') + '/execution', params).then(function() {
                localStorageService.delete($window.location.href);
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }
}