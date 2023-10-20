function PbTableCtrl($scope, modalService) {

    this.isArray = Array.isArray;

    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function (row) {
        // if (this.isClickable()) {
        //     $scope.properties.selectedRow = row;
        // }
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }
    
    $scope.removeItem = function(_row){
        $scope.selectedRow = _row;
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
    
    $scope.editar = function(_row, _index){
        $scope.properties.selectedRow = angular.copy(_row);
        $scope.properties.selectedRow["editar"] = true;
        $scope.properties.selectedRow["ver"] = false;
        $scope.properties.selectedRow["index"] = _index;
        openModal($scope.properties.modalId);
    }
    
    $scope.ver = function(_row){
        $scope.properties.selectedRow = angular.copy(_row);
        $scope.properties.selectedRow["editar"] = false;
        $scope.properties.selectedRow["ver"] = true;
        openModal($scope.properties.modalId);
    }
    
    function openModal(modalId) {
        modalService.open(modalId);
    }    
}
