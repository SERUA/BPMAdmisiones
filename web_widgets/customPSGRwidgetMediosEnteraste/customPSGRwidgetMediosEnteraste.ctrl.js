function PSGRwidgetMediosEnteraste($scope) {
    
    $scope.$watch("properties.content", function(){
        if($scope.properties.content){
            $scope.show = true;
            console.log("$scope.properties.content: " + $scope.properties.content); 
        } 
    });
    
}