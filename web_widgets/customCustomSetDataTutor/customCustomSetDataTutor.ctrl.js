function ($scope, $http) {
    
    $scope.$watch("properties.reload", function(){
        if($scope.properties.reload === undefined){
           $scope.properties.datosTutor = [];
        }else{
           $scope.properties.datosTutor = $scope.properties.reload; 
        }
    });
}