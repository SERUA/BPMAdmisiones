function ($scope, $http) {
    $scope.$watch("properties.username", function(){
        if($scope.properties.username){
            let url = "../API/extension/AnahuacINVPRestGet?url=getSesionActivaV2&p=0&c=10&username=" + $scope.properties.username; 
            
            $http.get(url).success(function(_success){
                $scope.properties.sesionInfoV2 = angular.copy(_success);
            }).error(function(_err){
                $scope.properties.sesionInfoV2 = angular.copy(_err);
            });
        }
    });
}