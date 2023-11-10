function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    $scope.changeVariable = function(){
        $scope.properties.navigationVar = $scope.properties.newValue;
    }
}
