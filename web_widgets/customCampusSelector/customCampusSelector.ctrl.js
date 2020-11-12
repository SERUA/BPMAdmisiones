function ($scope) {
    $scope.selected = "";
    $scope.setSelected = function(_campus){
        $scope.properties.campusSelected = _campus;
        $scope.selected = _campus;
    }
}