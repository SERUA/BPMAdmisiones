function PbTableCtrl($scope) {

  this.isArray = Array.isArray;

  this.isClickable = function () {
    return $scope.properties.isBound('selectedRow');
  };

  this.selectRow = function (row) {
    if (this.isClickable()) {
        $scope.properties.selectedRow = row;
        $scope.properties.formOutput.solicitudApoyoEducativoInput.estado  = row.estado;
        $scope.properties.formOutput.solicitudApoyoEducativoInput.delegacionCiudad = row.municipio;
        $scope.properties.formOutput.solicitudApoyoEducativoInput.colonia = row.asentamiento;
        $scope.properties.formOutput.solicitudApoyoEducativoInput.codigoPostal = row.codigoPostal;
    }
  };

  this.isSelected = function(row) {
    return angular.equals(row, $scope.properties.selectedRow);
  }
}
