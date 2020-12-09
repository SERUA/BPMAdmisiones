function ($scope, $http) {
    $scope.$watch("properties.reload", function(){
        if($scope.properties.reload !== undefined){
            if($scope.properties.documentoDescuento.length > 0){
                $scope.properties.tieneDescuento = true;
            }else{
                $scope.properties.tieneDescuento = false;
            }
            if($scope.properties.documentoPAA.length > 0 && !$scope.properties.pertenceRed){
                $scope.properties.tienePAA = true;
            }else{
                $scope.properties.tienePAA = false;
            }
        }
    });
}