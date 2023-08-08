function PbButtonCtrl($scope, $http, modalService) {
    'use strict';

    var vm = this;

    this.action = function action() {
        vm.busy = true;
        debugger;
        let url = angular.copy($scope.properties.urlEliminarCarta);
        let dataToSend = angular.copy($scope.properties.cartaEliminar);
        $http.post(url, dataToSend).success(function(success){
          
        }).error(function(error){
          
        }).finally(function(){
            vm.busy = false; 
            window.location.reload();
        });
    }
}
