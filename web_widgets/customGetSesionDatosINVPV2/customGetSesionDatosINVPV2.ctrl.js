function ($scope, $http) {
    $scope.$watch("properties.username", function(){
        if($scope.properties.username){
            let url = $scope.properties.url + $scope.properties.username; 
            
            $http.get(url).success(function(_success){
                $scope.properties.sesionInfoV2 = angular.copy(_success);
            }).error(function(_err){
                $scope.properties.sesionInfoV2 = angular.copy(_err);
            });
        }
    });
}