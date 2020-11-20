function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

  this.action = function action() {
    $scope.properties.mostrarPantallaAgregarPagoExamenAdmision = true;
     $scope.properties.mostrarPantallaEditar = false;
    var dateDay = new Date().getDay()+ 1;
    var dateMonth = new Date().getMonth() + 1;
    var dateYear = new Date().getFullYear();
    var dateTime = new Date(); 
    $scope.properties.newData =[
        {
            "persistenceId_string" : "",
            "clave" : "",
            "descripcion" : "",
            "usuarioCreacion" : $scope.properties.userData,
            "isEliminado" : false,
            "fechaCreacion": dateYear + "-" + dateMonth + "-" + dateDay + "-" + dateTime.getHours() + "T:" + dateTime.getMinutes() + ":" + dateTime.getSeconds(),
            "montoAspiranteLocal": "",
            "montoAspitanteForaneo": "",
            "montoAspiranteLocalDolares": "",
            "montoAspiranteForaneoDolares": "",
            "instruccionesDePago": "",
            "textoDescriptivoPagoDeshabilitado": "",
            "deshabilitarPagoDeExamenDeAdmision": false,
            "campus":$scope.properties.campus
            
        }
    ];
      
  }
}
