function ($scope, $http) {
    $scope.$watch("properties.catSolicitudDeAdmision.catGestionEscolar", function(){
        if($scope.properties.catSolicitudDeAdmision.catGestionEscolar !== undefined){
            if($scope.properties.gestionActual !== undefined){
                if($scope.properties.gestionActual.descripcion !== $scope.properties.catSolicitudDeAdmision.catGestionEscolar.descripcion){
                    if($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico){
                        $scope.properties.gestionActual = angular.copy($scope.properties.catSolicitudDeAdmision.catGestionEscolar);
                        $scope.properties.variableDestino = "vacio"
                    }
                }
            }else{
                $scope.properties.gestionActual = angular.copy($scope.properties.catSolicitudDeAdmision.catGestionEscolar);
            }
            
        }
        
    });
}