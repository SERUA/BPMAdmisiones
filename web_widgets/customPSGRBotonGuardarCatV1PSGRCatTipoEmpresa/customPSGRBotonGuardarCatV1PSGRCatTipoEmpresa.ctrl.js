function PbButtonCtrl($scope, $http) {

    'use strict';

    var vm = this;

    this.action = function action() {
        $scope.properties.dataToSend;
        if($scope.properties.navigationVar === "nuevo"){
            accionCatalogo($scope.properties.urlInsert);
        } else {
            accionCatalogo($scope.properties.urlUpdate);
        }        
    };

    function accionCatalogo(_url){
        vm.busy = true;
        $http.post(_url, $scope.properties.dataToSend).success(function(_response){
            swal("OK", "Guardado correctamente", "success");
            $scope.properties.navigationVar = "tabla"
        }).error(function(_response){
            swal("¡Algo ha fallado!", _response.error, "error");
        }).finally(function(){
            vm.busy = false;
        });
    }
}