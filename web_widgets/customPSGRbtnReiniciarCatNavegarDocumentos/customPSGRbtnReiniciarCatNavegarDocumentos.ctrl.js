function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    $scope.changeVariable = function(){
        debugger;
        var propiedadesActuales = Object.keys($scope.properties.objetoCat);

        // Recorre todas las propiedades
        for (var i = 0; i < propiedadesActuales.length; i++) {
            var propiedad = propiedadesActuales[i];
        
            // Verifica si la propiedad no es campus_pid ni posgrado_pid
            if (propiedad !== 'campus_pid' && propiedad !== 'posgrado_pid') {
                // Elimina la propiedad
                delete $scope.properties.objetoCat[propiedad];
            }
        }
        $scope.properties.navigationVar = $scope.properties.newValue;
    }
}
