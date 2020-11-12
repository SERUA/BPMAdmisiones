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
        openModal($scope.properties.modalid);
    }


    $scope.deleteData = function(row, index) {
        if ($scope.properties.eliminar[index].catParentezco.descripcion === "Padre") {
            $scope.properties.formInput.padreInput.catTitulo = null;
            $scope.properties.formInput.padreInput.nombre = "";
            $scope.properties.formInput.padreInput.apellidos = "";
            $scope.properties.formInput.padreInput.correoElectronico = "";
            $scope.properties.formInput.padreInput.catEscolaridad = null;
            $scope.properties.formInput.padreInput.catEgresoAnahuac = null;
            $scope.properties.formInput.padreInput.catCampusEgreso = null;
            $scope.properties.formInput.padreInput.catTrabaja = null;
            $scope.properties.formInput.padreInput.empresaTrabaja = "";
            $scope.properties.formInput.padreInput.giroEmpresa = "";
            $scope.properties.formInput.padreInput.puesto = "";
            $scope.properties.formInput.padreInput.isTutor = false;
            $scope.properties.formInput.padreInput.calle = "";
            $scope.properties.formInput.padreInput.catPais = null;
            $scope.properties.formInput.padreInput.numeroExterior = "";
            $scope.properties.formInput.padreInput.numeroInterior = "";
            $scope.properties.formInput.padreInput.catEstado = null;
            $scope.properties.formInput.padreInput.ciudad = "";
            $scope.properties.formInput.padreInput.colonia = "";
            $scope.properties.formInput.padreInput.telefono = "";
            $scope.properties.formInput.padreInput.codigoPostal = "";
            $scope.properties.formInput.padreInput.viveContigo = false;
            $scope.properties.formInput.padreInput.vive = null;

        } else if ($scope.properties.eliminar[index].catParentezco.descripcion === "Madre") {
            $scope.properties.formInput.madreInput.vive = null;
            $scope.properties.formInput.madreInput.catTitulo = null;
            $scope.properties.formInput.madreInput.nombre = "";
            $scope.properties.formInput.madreInput.apellidos = "";
            $scope.properties.formInput.madreInput.correoElectronico = "";
            $scope.properties.formInput.madreInput.catEscolaridad = null;
            $scope.properties.formInput.madreInput.catEgresoAnahuac = null;
            $scope.properties.formInput.madreInput.catCampusEgreso = null;
            $scope.properties.formInput.madreInput.catTrabaja = null;
            $scope.properties.formInput.madreInput.empresaTrabaja = "";
            $scope.properties.formInput.madreInput.giroEmpresa = "";
            $scope.properties.formInput.madreInput.puesto = "";
            $scope.properties.formInput.madreInput.isTutor = false;
            $scope.properties.formInput.madreInput.calle = "";
            $scope.properties.formInput.madreInput.catPais = null;
            $scope.properties.formInput.madreInput.numeroExterior = "";
            $scope.properties.formInput.madreInput.numeroInterior = "";
            $scope.properties.formInput.madreInput.catEstado = null;
            $scope.properties.formInput.madreInput.ciudad = "";
            $scope.properties.formInput.madreInput.colonia = "";
            $scope.properties.formInput.madreInput.telefono = "";
            $scope.properties.formInput.madreInput.codigoPostal = "";
            $scope.properties.formInput.madreInput.viveContigo = false;
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