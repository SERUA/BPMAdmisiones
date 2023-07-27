function PbButtonCtrl($scope, modalService) {
    $scope.changeVariable = function(){
        localStorage.setItem('terminado', "true");
        $scope.properties.navigationVar = $scope.properties.newValue;
    }
}
