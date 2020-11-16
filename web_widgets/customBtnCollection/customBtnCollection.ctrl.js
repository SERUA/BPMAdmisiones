function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
        console.log("paso");
      var contact = JSON.parse($scope.properties.strJson);
      $scope.properties.dataReset.push(contact);
    };
    
    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal() {
        modalService.close();
    }
    


}