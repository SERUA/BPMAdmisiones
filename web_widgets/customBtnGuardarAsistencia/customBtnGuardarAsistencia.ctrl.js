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
                if($scope.properties.seleccion === true){
                    swal("¡Asistencia capturada correctamente!","","success")
                }else{
                    swal("¡Asistencia cancelada correctamente!","","success")    
                }
                doRequestCaseValue($scope.properties.seleccion);
                 $scope.properties.regresarTabla = "tabla";
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            });
            
    };
    
     function doRequestCaseValue (asistencia){
        var caseId = $scope.properties.datosUsuario.aspirantes[0].caseid;
        var variableNombre = "asistencia"+( $scope.properties.datosUsuario.tipoprueba_PID == 1?"Entrevista": $scope.properties.datosUsuario.tipo_prueba == "Examen Psicométrico" ? "Psicometrico" : "CollegeBoard") 
        var req = {
            method: "PUT",
            url: `/API/bpm/caseVariable/${caseId}/${variableNombre}`,
            data: `{ "type": "java.lang.Boolean","value": "${asistencia}"}`
        };
        return $http(req)
            .success(function (data, status) {
                
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            });
    }

}
