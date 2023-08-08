function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

  this.action = function action() {
      debugger;
      
      $scope.properties.actualizar = true;
      
      if($scope.properties.editarSec1 == false){
        $scope.properties.editarSec1 = true;
        
        }else if($scope.properties.editarSec1 == true){
        $scope.properties.editarSec1 = false;
        }
        if($scope.properties.editarSec2 == false){
        $scope.properties.editarSec2 = true;
        
        }else if($scope.properties.editarSec2 == true){
        $scope.properties.editarSec2 = false;
        }
        if($scope.properties.editarSec3 == false){
        $scope.properties.editarSec3 = true;
        
        }else if($scope.properties.editarSec3 == true){
        $scope.properties.editarSec3 = false;
        }
        if($scope.properties.editarSec4 == false){
        $scope.properties.editarSec4 = true;
        
        }else if($scope.properties.editarSec4 == true){
        $scope.properties.editarSec4 = false;
        }
        if($scope.properties.editarSec5 == false){
        $scope.properties.editarSec5 = true;
        
        }else if($scope.properties.editarSec5 == true){
        $scope.properties.editarSec5 = false;
        }
        if($scope.properties.editarSec6 == false){
        $scope.properties.editarSec6 = true;
        
        }else if($scope.properties.editarSec6 == true){
        $scope.properties.editarSec6 = false;
        }
        if($scope.properties.editarSec7 == false){
        $scope.properties.editarSec7 = true;
        
        }else if($scope.properties.editarSec7 == true){
        $scope.properties.editarSec7 = false;
        }
  }

}