function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;
    var validStatus = [
        "Autodescripción concluida",
        "Ya se imprimió su credencial",
        "Elección de pruebas calendarizado",
        "Resultado final del comité",
        "Carga y consulta de resultados"
    ];

    var caseid = 0;
    $scope.caseid = 0;

    this.action = function action() {
        let url = "../API/extension/AnahuacBecasRestGET?url=getPRomedioMinimoByCampus&p=0&c=0&idCampus=" + $scope.properties.idCampus;
        $http.get(url).success((result)=>{
            let promedioMinimo = parseFloat(result.data[0].promedioMinimo);
            let promedioGeneral = parseFloat($scope.properties.promedioGeneral);
            if(promedioMinimo > promedioGeneral){
                swal("Atención", "No puedes solicitar apoyo educativo por que tu promedio es inferior al promedio mínimo marcado por el Campus.", "warning");
            } else if ($scope.properties.aceptado === false){
                swal("Atención", "No puedes solicitar un apoyo educativo por que tu soicitud de admisión fué rechazada", "warning");
            } else if (!validarEstatus($scope.properties.estatusSolicitud)){
                swal("Atención", "Aun no se cuenta con la información suficiente para iniciar la solicitud de apoyo educativo, es necesario que concluyas el llenado de la autodescripción en tu proceso de admisión.", "warning");
            } else {
                if ($scope.properties.aceptoAvisoPrivacidad === true) {
                    startProcess();
                } else {
                    swal("¡Aviso!", "Para continuar debe aceptar el aviso de privacidad.", "warning");
                }
            }
        }).error(()=>{
            
        });
    };

    function validarEstatus(_input){
        let output = false;
        
        for(let estatus of validStatus){
            if(_input === estatus){
                output = true;
                break;
            }
        }

        return output;
    }

    function startProcess() {
        //var id = getUrlParam('id');
        if ($scope.properties.idProceso) {
            var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.idProceso + '/instantiation', getUserParam()).then(function () {
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
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

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if (shouldClose) { modalService.close(); }
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
            data: angular.copy($scope.properties.processStartContract),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                caseid = data.caseId;
                $scope.caseid = data.caseId;
                $scope.properties.dataFromSuccess = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
                notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status });
                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                closeModal($scope.properties.closeOnSuccess);
            })
            .error(function (data, status) {
                vm.busy = false;
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {
                setTimeout(function(){
                    insertBitacora();
                }, 2000);
            });
    }

    function insertBitacora(){
        let url = $scope.properties.urlBitacora;
        let dataToSend = angular.copy($scope.properties.objetoBitacora);
        dataToSend.caseid = $scope.caseid;
        $http.post(url, dataToSend).success(function(){
            
        }).error(function(){
            
        }).finally(function(){
            window.location.replace($scope.properties.urlSolicitud);
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
}