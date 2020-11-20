function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

  this.action = function action() {
     $scope.properties.mostrarPagoDeshabilitado = false; 
     $scope.properties.mostrarPagoExamen = false; 
     $scope.properties.mostrarOpcionesPagoExamenAdmision = false;
     $scope.properties.mostrarPantallaEditar = false;
  }
}
