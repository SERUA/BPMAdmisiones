function($scope, $http) {
    $scope.$watch("properties.dataValor", function() {
       if($scope.properties.dataValor !== undefined){
           if($scope.properties.valorValidacion === "vacio"){
               $scope.properties.valorValidacion = "lleno";
               $scope.properties.variableDestino = angular.copy($scope.properties.dataValor);
           }
       }
    });
}