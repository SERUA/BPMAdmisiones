function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        $scope.updateRequisitos();
    };

    $scope.updateRequisitos = function() {
        
        const isUpdate = $scope.properties.isUpdate;
        const isCleanList = $scope.properties.isCleanList;
        let text = "";
        let title = "";
        let buttonText = "";
        
        if (isUpdate) {
            if (isCleanList) {
                text = "¿Estás seguro que deseas eliminar los requisitos adicionales?";
                title = "Eliminar requisitos adicionales";
                buttonText = "Eliminar";
            } 
            else {
                text = "Si confirmas esta operación se actualizará la lista de requisitos adicionales y se le notificará al aspirante vía correo electrónico. La solicitud quedará en espera de que sean marcados como cumplidos.";
                title = "Actualizar";
                buttonText = "Actualizar";
            }
        }
        else {
            text = "Si confirmas esta operación se le solicitará al aspirante los requisitos adicionales seleccionados vía correo electrónico, y la solicitud quedará en espera de que sean marcados como cumplidos.";
            title = "Solicitar";
            buttonText = "Solicitar";
        }
        
        swal({
            title: title,
            text: text,
            icon: "info",
            buttons: [
                'Cancelar',
                buttonText
            ],
        })
        .then((isConfirmar) => {
            if (isConfirmar) {
                
                // Actualizar
                doRequestUpdate("POST", "/API/extension/posgradosRest?url=updateListaRequisitosAdicionalesAuxiliar", null, $scope.properties.dataToUpdate, 
                    function(datos, status) { 
                        closeModal(true);

                        if (!isCleanList) {
                            sendMail();
                        }

                        if (status) {
                            $scope.properties.updateResponseStatusCode = status;
                        }
                        else $scope.properties.updateResponseStatusCode = "200";
                        
                        $scope.$apply();
                    },
                    function(datos) { }
                );

                
            }
        });
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
        var id = getUrlParam('id');
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

    function insertBitacora() {
        let url = $scope.properties.urlBitacora;
        let dataToSend = angular.copy($scope.properties.objetoBitacora);

        $http.post(url, dataToSend).success(function () {
            
        }).error(function (err) {
            
        }).finally(function () {
            $window.close();
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
        id = $scope.properties.taskId;
        var params = getUserParam();
        params.assign = true;
        if (id) {
                doRequest('POST', '../API/bpm/userTask/' + id + '/execution', params).then(function() {
                    localStorageService.delete($window.location.href);
                });
            } else {
                $log.log('Impossible to retrieve the task id value from the URL');
            }
    }
    
    function doRequest(method, url, params) {
        let dataToSend = angular.copy($scope.properties.dataToSend);
        vm.busy = true;
        var req = {
          method: method,
          url: url,
          data: dataToSend,
          params: params
        };
    
        return $http(req)
          .success(function(data, status) {
            $scope.properties.dataFromSuccess = "success";
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromError = undefined;
            notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status});
            if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
              redirectIfNeeded();
            }
            
            // Cerrar modal
            closeModal($scope.properties.closeOnSuccess);
          })
          .error(function(data, status) {
            $scope.properties.dataFromError = "error";
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status});

          })
          .finally(function() {
            vm.busy = false;
          });
      }
     
    function doRequestUpdate(method, url, params, dataToSend, callbackSuccess, callbackError) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: dataToSend,
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callbackSuccess(data, status)
            })
            .error(function(data, status) {
                callbackError(data);
                console.error(data);
            })
            .finally(function() {
                vm.busy = false;
            });
    }

    function sendMail(){
        let url = angular.copy($scope.properties.urlMail);
        let dataToSend =angular.copy($scope.properties.dataToSendMail);

        $http.post(url, dataToSend).success(function(){
            console.log("Correo enviado")
        }).error(function(){
            swal("¡Algo ha fallado!", "No se ha podido enviar la notificción de requisitos adicionales, intente de nuevo mas tarde", "error");
        })
    }

}