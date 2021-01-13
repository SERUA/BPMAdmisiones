function ($scope, $http, blockUI) {
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
}