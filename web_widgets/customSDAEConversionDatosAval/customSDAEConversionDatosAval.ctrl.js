function ($scope) {
    $scope.$watch("properties.objetoSalida", function(){
        if($scope.properties.objetoSalida){
            $scope.properties.objetoSalida["ingresoMensual"] = parseInt($scope.properties.objetoSalida["ingresoMensual"]);
            $scope.properties.objetoSalida["otroIngreso"] = parseInt($scope.properties.objetoSalida["otroIngreso"]);
            $scope.properties.objetoSalida["egresoMensualTotal"] = parseInt($scope.properties.objetoSalida["egresoMensualTotal"]);
            $scope.properties.objetoSalida["provenienteDe"] = parseInt($scope.properties.objetoSalida["provenienteDe"]);
        } 
    });
    
    $scope.$watch("properties.avalReferenciasBancariasModel", function(){
        if($scope.properties.avalReferenciasBancariasModel){
            for(let i = 0; i < $scope.properties.avalReferenciasBancariasModel.length; i++){
                $scope.properties.avalReferenciasBancariasModel[i]["saldoPromedio"] = parseInt($scope.properties.avalReferenciasBancariasModel[i]["saldoPromedio"]);   
            }
        } 
    });
    
    $scope.$watch("properties.avalReferenciaCreditoModel", function(){
        if($scope.properties.avalReferenciaCreditoModel){
            for(let i = 0; i < $scope.properties.avalReferenciaCreditoModel.length; i++){
                $scope.properties.avalReferenciaCreditoModel[i]["saldoPromedio"] = parseInt($scope.properties.avalReferenciaCreditoModel[i]["saldoPromedio"]);   
            }
        } 
    });
}