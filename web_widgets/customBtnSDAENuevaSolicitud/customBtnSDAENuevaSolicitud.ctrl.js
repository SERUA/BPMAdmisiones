function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        let url = "../API/extension/AnahuacBecasRestGET?url=getPRomedioMinimoByCampus&p=0&c=10&idCampus=" + $scope.properties.idCampus;
        $http.get(url).success((result)=>{
            debugger;
            let promedioMinimo = parseFloat(result.data[0].promedioMinimo);
            let promedioGeneral = parseFloat($scope.properties.promedioGeneral);
            if(promedioMinimo > promedioGeneral){
                showSwal("Atención", "No puedes solicitar apoyo educativo por que tu promedio es inferior al promedio mínimo marcado por el Campus.", "warning");
            } else if ($scope.properties.aceptado === false){
                showSwal("Atención", "No puedes solicitar un apoyo educativo por que tu soicitud de admisión fué rechazada", "warning");
            } else {
                var url = "/portal/resource/app/aspiranteSDAE/nueva_solicitud_SDAE/content/?tipoMoneda=" + $scope.properties.tipoMoneda
                window.location.replace(url);
            }
        }).error(()=>{
            
        });
        // let promedioMinimo = parseInt($scope.properties.promedioMinimo);
        
        
        //  var url = "/portal/resource/app/aspiranteSDAE/nueva_solicitud_SDAE/content/?tipoMoneda=" + $scope.properties.tipoMoneda
        //     window.location.replace(url);
    };
    
    function showSwal(_title, _message, _type){
        swal(_title, _message, _type)
    }

    function doRequest(method, url, params) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req).success(function (data, status) {
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
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () {
            vm.busy = false;
        });
    }
}