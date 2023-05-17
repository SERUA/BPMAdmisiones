function PbCheckboxCtrl($scope, $log, widgetNameFactory, modalService, $http) {

    $scope.$watch('properties.value', function(value) {
        if (value === 'true' || value === true) {
          $scope.properties.value = true;
        } else {
          $scope.properties.value = false;
        }
    });
  
    $scope.showModalCondiciones = function(){
        $scope.properties.objetoNuevaImagen = $scope.properties.objetoReinicioImagen;
        $scope.properties.reloadTableImagenes = true;
        modalService.open("imagenesSocioEconomico");
    }
    
    $scope.updateTipoApoyoVideocase = function(){
        let _data = angular.copy($scope.properties.tipoApoyoSeleccionado);
        _data.idCampus = $scope.properties.idCampus;
        
        var req = {
            method: "POST",
            url: $scope.properties.url,
            data: _data
        };

        return $http(req)
            .success(function(data, status) {
                  console.log("Actualizado ");
            })
            .error(function(data, status) {
                swal("Error", data.error, "error");
            })
            .finally(function() {
                console.log("Final ");
            });
    }
    
    if (!$scope.properties.isBound('value')) {
        $log.error('the pbCheckbox property named "value" need to be bound to a variable');
    }
}
