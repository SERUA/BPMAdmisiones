/**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function ($scope,$http) {
    
      $scope.$watch('properties.solicitudDeAdmision', function(value) {
        if (angular.isDefined(value) && value !== null) {
             if ($scope.properties.isCorrectTask) {
                console.log("Cambio de tarea")
                $scope.asignarTarea();
                $scope.properties.isCorrectTask = false;
            }
        }
  });
  $scope.validarPeriodo=function(periodofin){
      console.log("isPeriodoVencido?")
      var fecha = new Date(periodofin.slice(0,10));
      if (fecha<new Date()){
          swal("¡Periodo vencido!", "Recuerda que el periodo de ingreso que seleccionaste ha vencido y debes actualizarlo, contacta a tu asesor o a través del chat", "warning");
      }
  }
  
  $scope.asignarTarea = function() {
        var req = {
            method: "PUT",
            url: "/bonita/API/extension/RegistroPut?url=changeTaskId&taskId=" + $scope.properties.taskId,
            data: angular.copy({ "assigned_id": "" })
        };

        return $http(req)
            .success(function(data, status) {
                $scope.submitTask();
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }
    
     $scope.submitTask = function() {

        var req = {
            method: "POST",
            url: "/bonita/API/bpm/userTask/" + $scope.properties.taskId + "/execution?assign=false",
            data: angular.copy("")
        };

        return $http(req)
            .success(function(data, status) {
                // $scope.getConsulta();
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }
  
  
}