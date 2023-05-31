function ($scope, $http) {
    $scope.$watch("properties.lstReferenciasBancarias", ()=>{
        if($scope.properties.lstReferenciasBancarias && !$scope.referenciasBancarias){
            $scope.referenciasBancarias = true;
            debugger;
            for(let i = 0; i < $scope.properties.lstReferenciasBancarias.length; i++){
                $scope.properties.lstReferenciasBancarias[i].saldoPromedio = parseInt($scope.properties.lstReferenciasBancarias[i].saldoPromedio);
            }
        }
    });

    $scope.$watch("properties.lstReferenciasCredito", ()=>{
        if($scope.properties.lstReferenciasCredito && !$scope.referenciasCredito){
            $scope.referenciasCredito = true;
            debugger;
            for(let i = 0; i < $scope.properties.lstReferenciasCredito.length; i++){
                $scope.properties.lstReferenciasCredito[i].saldoPromedio = parseInt($scope.properties.lstReferenciasCredito[i].saldoPromedio);
            }
        }
    }); 

    $scope.$watch("properties.datosAval", ()=>{
        if($scope.properties.datosAval && !$scope.datosAval){
            $scope.datosAval = true;
            $scope.properties.datosAval.ingresoMensual = parseInt($scope.properties.datosAval.ingresoMensual);
            $scope.properties.datosAval.provenienteDe = parseInt($scope.properties.datosAval.provenienteDe);
            $scope.properties.datosAval.otroIngreso = parseInt($scope.properties.datosAval.otroIngreso);
            $scope.properties.datosAval.egresoMensualTotal = parseInt($scope.properties.datosAval.egresoMensualTotal);
        }
    }); 
    
    $scope.$watch("properties.infoNotarial", ()=>{
        if($scope.properties.infoNotarial && !$scope.infoNotarial){
            $scope.infoNotarial = true;
            delete $scope.properties.infoNotarial.caseId;
        }
    }); 
    
    delete json.edad;
}