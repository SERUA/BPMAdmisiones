function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      $scope.properties.value =  $scope.properties.valor;
    };

}
