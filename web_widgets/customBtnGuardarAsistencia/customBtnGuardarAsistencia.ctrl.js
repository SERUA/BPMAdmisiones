function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      $scope.properties.value =  $scope.properties.seleccion;
      $scope.properties.regresarTabla = "tabla";
      if($scope.properties.value === true){
      swal("¡Asistencia capturada correctamente!","","success")
      }else{
      swal("¡Asistencia cancelada correctamente!","","success")    
      }
    };

}
