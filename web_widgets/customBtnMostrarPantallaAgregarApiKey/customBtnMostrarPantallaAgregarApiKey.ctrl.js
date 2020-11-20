function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
  'use strict';
  var vm = this;
  this.action = function action() {
    $scope.properties.mostrarPantallaAgregarApiKey = true;
     $scope.properties.mostrarPantallaEditar = false;
    $scope.properties.newData =[
        {
            "persistenceId_string" : "",
            "campus" : "",
            "conekta" : "",
            "mailgun" : "",
            "crispChat" : "",
            "isEliminado" : false,
        }
    ];
  }
}
