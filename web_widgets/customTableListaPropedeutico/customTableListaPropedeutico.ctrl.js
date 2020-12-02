function PbTableCtrl($scope, modalService) {

    this.isArray = Array.isArray;

    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }





    $scope.deleteData = function(row, index) {
        debugger;
        $scope.properties.eliminar.splice([index],1);
        console.log($scope.properties.eliminar + index);
        $scope.properties.datosEditar=[];
    }


    function openModal(modalid) {

        modalService.open(modalid);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }

}