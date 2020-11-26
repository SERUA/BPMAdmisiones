function ($scope) {
    $scope.$watch('properties.objEntrada', function () {
        if($scope.properties.objEntrada!= undefined){
            $scope.properties.objSalida = angular.copy($scope.properties.objEntrada);
            $scope.properties.objSalida.todelete = false;
            console.log("data")
        }
    });
}