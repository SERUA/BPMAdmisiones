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
 
    $scope.removeItem = function(_row){
        $scope.selectedRow = _row;
        // swal("Atención", "¿Estás seguro que deseas borrar el registro?", "warning")
         swal({
            "title": "Confirmación",
            "text": "¿Estás seguro que deseas borrar el registro?",
            icon: "warning",
            buttons: [
                'Cancelar',
                'Eliminar'
            ],
            dangerMode: true,
        }).then(function (isConfirm) {
            if (isConfirm) {
                $scope.properties.content.splice($scope.properties.content.indexOf(_row), 1);
                $scope.$apply();
            }
        })
    }
  
}
