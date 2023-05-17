function PbTableCtrl($scope) {

    this.isArray = Array.isArray;

    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function (row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
            refreshEvent();
        }
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }
  
    $scope.removeElement = function(_row){
        $scope.properties.content.splice($scope.properties.content.indexOf(_row), 1);
    }
}