function PbButtonCtrl($scope, $http) {

    'use strict';

    var vm = this;

    this.action = function action() {
        if($scope.properties.navigationVar === "nuevo"){
            accionCatalogo($scope.properties.urlInsert);
        } else {
            accionCatalogo($scope.properties.urlUpdate);
        }        
    };

    function accionCatalogo(_url){
        debugger;
        vm.busy = true;
        $scope.properties.dataToSend.id_campus= $scope.properties.dataToFilter.persistenceid;
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