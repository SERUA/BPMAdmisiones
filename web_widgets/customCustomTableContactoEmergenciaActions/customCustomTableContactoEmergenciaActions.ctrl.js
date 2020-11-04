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

    this.showdatos = function(row) {
        debugger;
        $scope.properties.datomodificar = angular.copy(row);
        $scope.properties.ver = true;
        $scope.properties.datosEditar = row;
        openModal($scope.properties.modalidContactoEmergencia);
        openModal($scope.properties.modalid);

    }

    this.editdatos = function(row) {
                debugger;
        console.log("entro al nuevo")
        $scope.properties.datomodificar = angular.copy(row);
        console.log($scope.properties.datomodificar);
        $scope.properties.ocultar = true;
        $scope.properties.ver = false;
        $scope.properties.datosEditar = row;
        openModal($scope.properties.modalid);
        
    }

  
    $scope.deleteData = function(row, index) {
        
            $scope.properties.eliminar.splice(index, 1);
            console.log($scope.properties.eliminar + index);
    }
    
    
    function openModal(modalid,modalidContactoEmergencia) {

        modalService.open(modalid);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }

}