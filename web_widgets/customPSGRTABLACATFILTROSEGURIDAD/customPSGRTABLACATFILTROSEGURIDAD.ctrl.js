function PbTableCtrl($scope, $http) {

  this.isArray = Array.isArray;

  this.isClickable = function () {
    return $scope.properties.isBound('selectedRow');
  };

  this.selectRow = function (row) {
    if (this.isClickable()) {
      $scope.properties.selectedRow = row;
      $scope.properties.navigationVar = "editar";
    }
  };

  this.isSelected = function(row) {
    return angular.equals(row, $scope.properties.selectedRow);
  }
  $scope.$watch("properties.urlGet", function(){
      debugger;
        if($scope.properties.urlGet){
            $scope.getCatalogo();
        }
    });

    $scope.$watch("properties.reload", function(){
        if($scope.properties.reload === true){
            $scope.properties.reload = false;
            $scope.getCatalogo();
        }
    });
    
    $scope.getCatalogo = function (){
        $http.post($scope.properties.urlGet, {}).success(function(_response){
            $scope.properties.content = _response;
        }).error(function(_response){
            swal("¡Algo ha fallado!", _response.error, "error");
        });
    }
}