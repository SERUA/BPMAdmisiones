function ($scope, $http, blockUI) {
    debugger;
    
    if($scope.properties.reload === undefined || $scope.properties.reload === "" || $scope.properties.reload === null){
        $scope.properties.showbuttons = true;
    }
    
    $scope.$watch("properties.showbuttons", function(){
        if(!$scope.properties.showbuttons){
            blockUI.start();
        }else{
            blockUI.stop();
        }
        
    });
    
   /* $scope.$watch("properties.reload", function(){
        if(($scope.properties.reload === undefined || $scope.properties.reload.length === 0) && $scope.properties.campusSeleccionado !== undefined){
            $("#loading").modal("show");
        }else{
            $("#loading").modal("hide");
        }
    });*/
}