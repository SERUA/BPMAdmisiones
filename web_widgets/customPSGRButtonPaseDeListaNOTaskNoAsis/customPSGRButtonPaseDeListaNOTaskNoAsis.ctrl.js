function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {

        // Verificar incumplimiento
        if ($scope.properties.showNonComplianceMessage) {
            let title = "Titulo";
            let text = "Sin mensaje";
            let icon = null;
            try {
                if ($scope.properties.nonComplianceMessage.title) title = $scope.properties.nonComplianceMessage.title;
                if ($scope.properties.nonComplianceMessage.text) text = $scope.properties.nonComplianceMessage.text;
                icon = $scope.properties.nonComplianceMessage.icon && ['success', 'info', 'warning', 'error'].includes($scope.properties.nonComplianceMessage.icon) ? $scope.properties.nonComplianceMessage.icon : null;
            }
            catch (e) { }

            Swal.fire({
                title: title,
                text: text,
                icon: icon,
                confirmButtonText: "Aceptar",
            });
            return;
        }

        pasarLista();
    };

    function addCaseComment() {
        debugger;
        const dataToSend = {
            content: JSON.stringify($scope.properties.caseUserCommentToSend),
            processInstanceId: $scope.properties.caseId,
        };
        doRequest("POST", "/API/bpm/comment/", null, dataToSend,
            function (data, status) { debugger; },
            function (data, status) { console.log("No se agregó el comentario de caso.") }
        );
    }

    // Ejecución de tarea Pase de lista (Reagendar o Asistió)
    function submitTask() {
        var id;
        id = $scope.properties.taskId;
        if (id) {
            var params = getUserParam();
            params.assign = $scope.properties.assign;

            doRequest('POST', '../API/bpm/userTask/' + id + '/execution', params, $scope.properties.dataToSend,
                // Success callback
                (data, status) => {
                    $scope.properties.dataFromSuccess = data;
                    $scope.properties.responseStatusCode = status;
                    $scope.properties.dataFromError = undefined;
                    $scope.properties.taskCompleted = true;
                    notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status });
                    if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                        redirectIfNeeded();
                    }
                    closeModal($scope.properties.closeOnSuccess);
                    if ($scope.properties.caseUserCommentToSend && $scope.properties.caseUserCommentToSend.mensaje !== null) {
                        addCaseComment();
                    }
                    if ($scope.properties.dataToSend && $scope.properties.dataToSend.isReagendarInput) {
                        $scope.properties.taskResultMessages.push("La solicitud fue mandada a reagendar.")
                    }


                    // Error callback
                }, (data, status) => {
                    $scope.properties.dataFromError = data;
                    $scope.properties.responseStatusCode = status;
                    $scope.properties.dataFromSuccess = undefined;
                    $scope.properties.taskCompleted = false;
                    notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                })
                .then(function () {
                    localStorageService.delete($window.location.href);
                });
        }
        else {
            $log.log('Impossible to retrieve the task id value from the URL');
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
        var id = $scope.properties.taskId;
        if (id) {
            var prom = doRequest('POST', '../API/bpm/process/' + id + '/instantiation', getUserParam()).then(function () {
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
    }

    function doRequest(method, url, params, dataToSend, successCallback, errorCallback) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .success(successCallback)
            .error(errorCallback)
            .finally(function () {
                vm.busy = false;
            });
    }

    function pasarLista() {
        vm.busy = true;
        let url = $scope.properties.urlPasarLista.replace("[ASISTIO]", false).replace("[CASEID]", $scope.properties.caseId);

        var req = {
            method: 'POST',
            url: url
        };

        return $http(req)
        .success(()=>{
            // actualizarEstatus();
            debugger;
            $scope.enviarReagendar();

        })
        .error((err)=>{
            swal("¡Algo ha fallado!", err.error, "error");
        })
        .finally(function () {
            vm.busy = false;
        });
    }

    $scope.enviarReagendar = function(){
        let url = "../API/extension/posgradosRest?url=reagendarAspirante&caseid=" + $scope.properties.caseId;
        let dataToSend = {
            "caseid": $scope.properties.caseId
        };

        $http.post(url, dataToSend).success(function(_data){
            console.log("Enviado a reagendar");
            $scope.properties.dataFromSuccess = _data;
            $scope.properties.dataFromError = undefined;
            $scope.properties.taskCompleted = true;

            if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                redirectIfNeeded();
            }

            closeModal($scope.properties.closeOnSuccess);
            if ($scope.properties.caseUserCommentToSend && $scope.properties.caseUserCommentToSend.mensaje !== null) {
                addCaseComment();
            }

            vm.busy = false;
            closeModal($scope.properties.closeOnSuccess);
        }).error(function(){
            swal("¡Algo ha fallado!", "Ha ocurrido un error inesperado, intente de nuevo mas tarde", "error")
        }).finally(function(){
            
        });
    }

    function actualizarEstatus() {
        vm.busy = true;
        let url = $scope.properties.urlActualizarEstatus.replace("[CASEID]", $scope.properties.caseId).replace("[ESTATUS]", "solicitud_lista_para_dictamen"); 

        var req = {
            method: 'POST',
            url: url
        };

        return $http(req)
        .success((data)=>{
            debugger;
            console.log("Estatus actualizado");
            $scope.properties.dataFromSuccess = data;
            $scope.properties.dataFromError = undefined;
            $scope.properties.taskCompleted = true;

            if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                redirectIfNeeded();
            }

            closeModal($scope.properties.closeOnSuccess);
            if ($scope.properties.caseUserCommentToSend && $scope.properties.caseUserCommentToSend.mensaje !== null) {
                addCaseComment();
            }

            vm.busy = false;
            closeModal($scope.properties.closeOnSuccess);
        })
        .error(()=>{
            console.log("Estatus no actualizado");
        })
        .finally(function () {
            localStorageService.delete($window.location.href);
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

    function getUrlParam(param) {
        var paramValue = $location.absUrl().match('[//?&]' + param + '=([^&#]*)($|[&#])');
        if (paramValue) {
            return paramValue[1];
        }
        return '';
    }
}