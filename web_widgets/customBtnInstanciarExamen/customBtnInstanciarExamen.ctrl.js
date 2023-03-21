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
            // startProcess();
            checkTolerancia($scope.properties.userData.user_name);
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

    function checkTolerancia(_username){
        let url = "../API/extension/AnahuacINVPRestGet?url=checkToleranciaFront&p=0&c=10&&username=" + _username;

        $http.get(url).success(function(_success){
            if(_success[0] === true){
                startProcess();
                // getTerminoPrueba(_username);
            } else {
                if($scope.properties.idioma === "ESP"){
                    swal("Error", "Se ha excedido el tiempo de tolerancia de entrada a tu examen", "error");
                } else {
                    swal("Error", "The entry tolerance time for your exam has been exceeded", "error");
                }
            }
        }).error(function(_error){
            if(_error.error === "no_sesion_asignada"){
                if($scope.properties.idioma === "ESP"){
                    swal("Error", "Aún no tienes una sesión asignada. Contacta con tu aplicador.", "error");
                } else {
                    swal("Error", "You don't have a session assigned yet. Contact your advisor.", "error");
                }
            } else {
                if($scope.properties.idioma === "ESP"){
                    swal("Error", "Se ha excedido el tiempo de tolerancia de entrada a tu examen", "error");
                } else {
                    swal("Error", "The entry tolerance time for your exam has been exceeded", "error");
                }
            }
        });
    }

    // function getTerminoPrueba(_username){

    //     let url = "../API/extension/AnahuacINVPRestGet?url=getTerminoPrueba&p=0&c=100&username=" + _username 

    //     $http.get(url).success(function(data){
    //         $scope.properties.dataToSend.terminarExamenInput = data[0];
    //         startProcess();
    //     }).error(function(data){
    //         swal("Error", "Error al obtener la información, intente de nuevo ams tarde ", "error");
    //     });

    // }

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
        var prom = doRequest('POST', '../API/extension/AnahuacINVPRestAPI?url=instantiation&p=0&c=10', getUserParam()).then(function() {
                //$scope.properties.inicioexamen = true;

                //localStorageService.delete($window.location.href);
            });
        //../API/extension/AnahuacINVPRestAPI?url=insertRespuesta&p=0&c=10
        //var id = getUrlParam('id');
        /*var id = $scope.properties.ProcessId;
        if (id) {
            var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.ProcessId + '/instantiation', getUserParam()).then(function() {
                debugger;
                $scope.properties.inicioexamen = true;

                //localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }*/
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
        .success(function(data, status) {
            $scope.properties.dataFromSuccess = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromError = undefined;
            insertCase(data.caseId);
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
        //var userId = getUrlParam('user');
        var userId = $scope.properties.userData.user_id;
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

    function insertCase(caseid) {
        vm.busy = true;
        var data = {
            "pregunta": 0,
            "caseid": Number(caseid),
            "respuesta": false,
            "idusuario": $scope.properties.userData.user_id
        }

        var req = {
            method: "POST",
            url: "../API/extension/AnahuacINVPRestAPI?url=insertRespuesta&p=0&c=10",
            data: data
        };

        return $http(req)
        .success(function(data, status) {
            insertterminado();
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

     function insertterminado() {
        vm.busy = true;

        var data = {
            "terminado": false,
            "username": $scope.properties.userData.user_name
        }

        var req = {
            method: "POST",
            url: "../API/extension/AnahuacINVPRestAPI?url=insertterminado&p=0&c=10",
            data: data
        };

        return $http(req)
        .success(function(data, status) {
            insertidiomausuario();
        })
        .error(function(data, status) {
            insertidiomausuario();
        })
        .finally(function() {
            vm.busy = false;
        });
    }

    function insertidiomausuario() {
        vm.busy = true;
        var data = {
            "idioma": $scope.properties.idioma,
            "nombreusuario": $scope.properties.userData.user_name
        }

        var req = {
            method: "POST",
            url: "../API/extension/AnahuacINVPRestAPI?url=insertUpdateIidiomaUsuario&p=0&c=10",
            data: data
        };

        return $http(req)
        .success(function(data, status) {
            $scope.properties.dataFromSuccess = true;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromError = undefined;
            window.top.location.href = $scope.properties.targetUrlOnSuccess;
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

}