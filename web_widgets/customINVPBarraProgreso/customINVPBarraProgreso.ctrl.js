function ($scope) {
    $scope.porcentaje = 0;
  $scope.$watch("properties.lstContestadas", function() {
        if ($scope.properties.lstContestadas !== null && $scope.properties.lstContestadas !== undefined) {
            $scope.porcentaje = ($scope.properties.lstContestadas*100)/$scope.properties.totalpreguntas;
            
            
        }
    });
}