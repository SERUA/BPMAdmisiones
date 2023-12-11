function PbTableCtrl($scope) {

  this.isArray = Array.isArray;

  this.isClickable = function () {
    return $scope.properties.isBound('selectedRow');
  };

  this.selectRow = function (row) {
    if (this.isClickable()) {
      $scope.properties.selectedRow = row;
    }
  };

  this.isSelected = function(row) {
    return angular.equals(row, $scope.properties.selectedRow);
  }
  
  $scope.verSolicitud = function(rowData) {
    const url = "/bonita/portal/resource/app/posgrados/"+$scope.properties.abrirPagina+"/content/?app=posgrados&caseId=" + rowData.caseid;
    window.open(url, '_blank');  
  }
}
