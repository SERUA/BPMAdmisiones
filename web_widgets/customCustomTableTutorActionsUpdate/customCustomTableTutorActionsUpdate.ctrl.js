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
        $scope.properties.datomodificar = angular.copy(row);
        $scope.properties.ver = true;
        $scope.properties.datosEditar = row;
        openModal($scope.properties.modalid);
    }

    this.editdatos = function(row) {
        $scope.properties.datomodificar = angular.copy(row);
        console.log($scope.properties.datomodificar);
        $scope.properties.ocultar = true;
        $scope.properties.ver = false;
        $scope.properties.datosEditar = row;
        $scope.properties.jsonRow = row;
        openModal($scope.properties.modalid);
    }


    $scope.deleteData = function(row, index) {
        if ($scope.properties.eliminar[index].catParentezco.descripcion === "Padre") {
            $scope.properties.padre.catTitulo = null;
            $scope.properties.padre.nombre = "";
            $scope.properties.padre.apellidos = "";
            $scope.properties.padre.correoElectronico = "";
            $scope.properties.padre.catEscolaridad = null;
            $scope.properties.padre.catEgresoAnahuac = null;
            $scope.properties.padre.catCampusEgreso = null;
            $scope.properties.padre.catTrabaja = null;
            $scope.properties.padre.empresaTrabaja = "";
            $scope.properties.padre.giroEmpresa = "";
            $scope.properties.padre.puesto = "";
            $scope.properties.padre.isTutor = false;
            $scope.properties.padre.calle = "";
            $scope.properties.padre.catPais = null;
            $scope.properties.padre.numeroExterior = "";
            $scope.properties.padre.numeroInterior = "";
            $scope.properties.padre.catEstado = null;
            $scope.properties.padre.ciudad = "";
            $scope.properties.padre.colonia = "";
            $scope.properties.padre.telefono = "";
            $scope.properties.padre.codigoPostal = "";
            $scope.properties.padre.viveContigo = false;
            $scope.properties.padre.vive = null;

        } else if ($scope.properties.eliminar[index].catParentezco.descripcion === "Madre") {
            $scope.properties.madre.vive = null;
            $scope.properties.madre.catTitulo = null;
            $scope.properties.madre.nombre = "";
            $scope.properties.madre.apellidos = "";
            $scope.properties.madre.correoElectronico = "";
            $scope.properties.madre.catEscolaridad = null;
            $scope.properties.madre.catEgresoAnahuac = null;
            $scope.properties.madre.catCampusEgreso = null;
            $scope.properties.madre.catTrabaja = null;
            $scope.properties.madre.empresaTrabaja = "";
            $scope.properties.madre.giroEmpresa = "";
            $scope.properties.madre.puesto = "";
            $scope.properties.madre.isTutor = false;
            $scope.properties.madre.calle = "";
            $scope.properties.madre.catPais = null;
            $scope.properties.madre.numeroExterior = "";
            $scope.properties.madre.numeroInterior = "";
            $scope.properties.madre.catEstado = null;
            $scope.properties.madre.ciudad = "";
            $scope.properties.madre.colonia = "";
            $scope.properties.madre.telefono = "";
            $scope.properties.madre.codigoPostal = "";
            $scope.properties.madre.viveContigo = false;
        }
        $scope.properties.eliminar.splice(index, 1);
        console.log($scope.properties.eliminar + index);
    }


    function openModal(modalid) {

        modalService.open(modalid);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }

}