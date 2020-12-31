function PbButtonCtrl($scope, $http, $window) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
        var asis = angular.copy($scope.properties.dataToSend.asistencia)
        $scope.properties.dataToSend.asistencia = $scope.properties.seleccion;
         var req = {
            method: "POST",
            url: asis === null?"/bonita/API/extension/AnahuacRest?url=insertPaseLista&p=0&c=10":"/bonita/API/extension/AnahuacRest?url=updatePaseLista&p=0&c=10",
            data: angular.copy($scope.properties.dataToSend)
        };
        return $http(req)
            .success(function (data, status) {
                $scope.properties.regresarTabla = "tabla";
                if($scope.properties.seleccion === true){
                    swal("¡Asistencia capturada correctamente!","","success")
                }else{
                    swal("¡Asistencia cancelada correctamente!","","success")    
                }
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            });
            
    };

}
