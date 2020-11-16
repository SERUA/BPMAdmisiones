function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      $scope.properties.dataReset.pop();
      closeModal();
    };
    
    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal() {
        modalService.close();
    }

}
