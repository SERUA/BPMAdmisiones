function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      var contact = JSON.parse($scope.properties.strJson);
      $scope.properties.dataReset = [];
      $scope.properties.dataReset.push(contact);
      closeModal();
      openModal($scope.properties.modalName);
      $scope.properties.value =  $scope.properties.seleccion;
    };
    
    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal() {
        modalService.close();
    }
    


}
