function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;
  
  
    $scope.myFunc = function() {
        var item = {  "persistenceId_string": "", "nombreCompleto":"", "titulo":"", "grupo":"", "cargo":"", "telefono":"", "correo":"", "showTitulo":true, "showGrupo":true, "showCargo":true, "showTelefono":true,  "showCorreo":true }
        
        $scope.properties.dataWhereAssig.posicion = $scope.properties.dataWhereAssig.lstCatComentariosFirmaInput.length;
        $scope.properties.dataWhereAssig.lstCatComentariosFirmaInput[$scope.properties.dataWhereAssig.posicion]= item;
        
        closeModal();
        openModal($scope.properties.nombreModal);
    };

  function openModal(modalId) {
    modalService.open(modalId);
  }

  function closeModal(shouldClose) {
    if(shouldClose)
      modalService.close();
  }


}
