function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        if(validarFormulario()){
            // startProcess();
            registro();
        }
    };

    function registro() {
        let dataToSend = angular.copy($scope.properties.dataToSend);
        dataToSend["processDefinitionId"] = angular.copy($scope.properties.processId);
        var prom = doRequestRegistro('POST', '../API/extension/posgradosRest?url=registro&p=0&c=10', dataToSend).then(function () {
            localStorageService.delete($window.location.href);
        });
    }

    function doRequestRegistro(_method, _url, _data) {
        vm.busy = true;

        var req = {
            method: _method,
            url: _url,
            data: _data
        };

        return $http(req)
            .success(function (dataResponse, status) {
                $scope.properties.navigationVar = "registro_completado";
            })
            .error(function (dataResponse, status) {
                
                const data = dataResponse.data;
                
                if (data && data.length !== 0 && data[0] === "AlreadyExistsException") {
                    $scope.properties.navigationVar = "registro_existente";
                    
                    /*swal({
                        title: "",
                        text: "",
                        icon: "",
                        button: "Aceptar",
                    })*/
                }
                else {
                    swal({
                        title: "Algo ha fallado",
                        text: "Ha ocurrido un error inesperado, intenta de nuevo mas tarde.",
                        icon: "error",
                        button: "Aceptar",
                    })
                }
            })
            .finally(function () {
                vm.busy = false;
            });
    }

    function validarFormulario(){
        let valid = true;
        let title = "¡Atención!", message = "";
        let data = angular.copy($scope.properties.dataToSend);

        if(!data.registroInput.nombre){
            valid = false;
            message = "El campo 'Nombre' no debe ir vacío.";
        } else if(!data.registroInput.apellido_paterno){
            valid = false;
            message = "El campo 'Apellido paterno' no debe ir vacío.";
        } else if(!data.registroInput.telefono_celular){
            valid = false;
            message = "El campo 'Teléfono celular' no debe ir vacío."; 
        } else if(data.registroInput.telefono_celular.toString().length < 10 || data.registroInput.telefono_celular.toString().length > 14){
            valid = false;
            message = "La logintud del campo 'Teléfono celular' debe ser de 10 a 14 caracteres.";
        } else if(!data.registroInput.correo_electronico){
            valid = false;
            message = "El campo 'Correo electrónico' no debe ir vacío.";
        } else if(!validarCorreo(data.registroInput.correo_electronico)){
            valid = false;
            message = "El campo 'Correo electrónico' tiene un formato inválido.";
        } else if(!data.registroInput.confirmar_correo_electronico){
            valid = false;
            message = "El campo 'Confirmar correo electrónico' no debe ir vacío.";
        } else if(data.registroInput.correo_electronico !== data.registroInput.confirmar_correo_electronico){
            valid = false;
            message = "Los correos no coinciden.";
        } else if(!data.registroInput.password){
            valid = false;
            message = "El campo 'Contraseña' no debe ir vacío.";
        } else if(!data.registroInput.confirmar_password){
            valid = false;
            message = "El campo 'Confirmar contraseña' no debe ir vacío.";
        } else if(data.registroInput.password !== data.registroInput.confirmar_password){
            valid = false;
            message = "Las contraseñas no coinciden.";
        } else if(!data.registroInput.campus){
            valid = false;
            message = "El campo 'Universidad a la que deseas ingresar' no debe ir vacío.";
        } else if(!data.registroInput.acepto_avisoprivacidad){
            valid = false;
            message = "Debes aceptar el aviso de privacidad.";
        } 

        if(valid === false){
            swal(title, message, "warning");
        }

        return valid;
    }

    function validarCorreo(correo) {
        var expresionRegularCorreo = /^(([^<>()\[\]\\.,;:\s@”]+(\.[^<>()\[\]\\.,;:\s@”]+)*)|(“.+”))@((\[[0–9]{1,3}\.[0–9]{1,3}\.[0–9]{1,3}\.[0–9]{1,3}])|(([a-zA-Z\-0–9]+\.)+[a-zA-Z]{2,}))$/;
        correo = correo.trim().toLowerCase();
        
        if (expresionRegularCorreo.test(correo)) {
            return true;
        } else {
            return false;
        }
    }

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if (shouldClose) { modalService.close(); }
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
        var id = angular.copy($scope.properties.processId);
        if (id) {
            var prom = doRequest('POST', '../API/bpm/process/' + id + '/instantiation', getUserParam()).then(function () {
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
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                $scope.properties.dataFromSuccess = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
                notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status });
                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                closeModal($scope.properties.closeOnSuccess);

                $scope.properties.navigationVar = "registro_completado";
            })
            .error(function (data, status) {
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {
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
            var params = getUserParam();
            params.assign = $scope.properties.assign;
            doRequest('POST', '../API/bpm/userTask/' + getUrlParam('id') + '/execution', params).then(function () {
                localStorageService.delete($window.location.href);
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }

}