function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;
  
  this.action = function action() {
    $scope.properties.mostrarPagoDeshabilitado = false; //$scope.properties.valorDeshabilitado;
    $scope.properties.mostrarPagoExamen = true; //$scope.properties.valor;
     $scope.properties.pagoHabilitadoODeshabilitado = true; // pago habilitado
     console.log($scope.properties.pagoHabilitadoODeshabilitado);
  };
  this.actionPago = function action() {
    $scope.properties.mostrarPagoExamen = false //$scope.properties.valor;
    $scope.properties.mostrarPagoDeshabilitado = true;  //$scope.properties.valorDeshabilitado;
    $scope.properties.pagoHabilitadoODeshabilitado = false; // pago deshabilitado
    console.log($scope.properties.pagoHabilitadoODeshabilitado);
  };
}
