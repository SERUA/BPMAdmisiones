function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    $scope.changeVariable = function(){
        debugger;
        var campus_pid = $scope.properties.objetoCat.campus_pid;
        var posgrado_pid = $scope.properties.objetoCat.posgrado_pid;
        $scope.properties.objetoCat = {};
        $scope.properties.objetoCat.campus_pid = campus_pid;
        $scope.properties.objetoCat.posgrado_pid = posgrado_pid;
        $scope.properties.navigationVar = $scope.properties.newValue;
    }
}
