function PbTableCtrl($scope) {

  this.isArray = Array.isArray;

  this.isClickable = function () {
    return $scope.properties.isBound('selectedRow');
  };

  this.selectRow = function (row) {
    if (this.isClickable()) {
        $scope.properties.selectedRow = row;
        $scope.properties.formOutput.estado  = row.estado;
        $scope.properties.formOutput.municipio  = row.municipio;
        $scope.properties.formOutput.ciudad = row.municipio;
        $scope.properties.formOutput.colonia = row.asentamiento;
        $scope.properties.formOutput.cp = row.codigoPostal;
    }
  };

  this.isSelected = function(row) {
    return angular.equals(row, $scope.properties.selectedRow);
  }
}
