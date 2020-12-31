function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';
  
    var vm = this;
    
    
    $scope.redirect = function(){
        var url = "/apps/administrativo/"+$scope.properties.Direccion;
        window.top.location.href = url;
    }
}