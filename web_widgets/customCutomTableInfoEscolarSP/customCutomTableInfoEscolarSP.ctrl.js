function PbTableCtrl($scope, modalService) {

    $scope.objInformacionEscolarInput = {
        "persistenceId_string": "",
        "grado": null,
        "tipo": null,
        "escuela": null,
        "otraEscuela": "",
        "pais": null,
        "estado": null,
        "ciudad": "",
        "anoInicio": "",
        "anoFin": "",
        "promedio": "",
        "estadoString": ""
    };
    this.isArray = Array.isArray;

    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row, index, action) {
        if (this.isClickable()) {
            row.index = index;
            row.escuelaString = row.escuela === null ? "" : row.escuela.descripcion;

            if (row.anoFin !== null && row.anoFin !== undefined && row.anoFin !== "") {
                row.anoFin = parseInt(row.anoFin);
            }

            if (row.anoInicio !== null && row.anoInicio !== undefined && row.anoInicio !== "") {
                row.anoInicio = parseInt(row.anoInicio);
            }

            // if(row.promedio !== null && row.promedio !== undefined && row.promedio !== ""){
            //     row.promedio = parseInt(row.promedio);
            // }
            // row.anoInicio = parseInt(row.anoInicio);

            // row.promedio = parseFloat(row.promedio);
            $scope.properties.selectedRow = angular.copy(row);
            
            // Trim al texto pais
            if ($scope.properties.selectedRow.escuela.pais) {
                $scope.properties.selectedRow.escuela.pais = $scope.properties.selectedRow.escuela.pais.toString().trim()
            }
            
            // Trim al texto estado
            if ($scope.properties.selectedRow.escuela.estado) {
                $scope.properties.selectedRow.escuela.estado = $scope.properties.selectedRow.escuela.estado.toString().trim()
            }

            if (action === "ver") {
                this.showModalEditar(false);
            } else if (action === "editar") {
                console.log($scope.properties.nombrePrepaDefault);
                if ($scope.properties.selectedRow.escuela.descripcion === $scope.properties.nombrePrepaDefault) {
                    if ($scope.properties.selectedRow.pais === undefined || $scope.properties.selectedRow.pais === null || $scope.properties.selectedRow.pais === "") {
                        for (var indexPais in $scope.properties.catPais) {
                            if ($scope.properties.catPais[indexPais].descripcion === $scope.properties.selectedRow.escuela.pais) {
                                $scope.properties.selectedRow.pais = $scope.properties.catPais[indexPais];
                            }
                        }
                    }
                    if ($scope.properties.selectedRow.escuela.pais === "México") {
                        if ($scope.properties.selectedRow.estado === undefined || $scope.properties.selectedRow.estado === null || $scope.properties.selectedRow.estado === "") {
                            for (var indexEstados in $scope.properties.catEstados) {
                                if ($scope.properties.catEstados[indexEstados].descripcion === $scope.properties.selectedRow.escuela.estado) {
                                    $scope.properties.selectedRow.estado = $scope.properties.catEstados[indexEstados];
                                }
                            }

                        }
                    } else {
                        if ($scope.properties.selectedRow.estadoString === undefined || $scope.properties.selectedRow.estadoString === null || $scope.properties.selectedRow.estadoString === "") {
                            $scope.properties.selectedRow.estadoString = $scope.properties.selectedRow.escuela.estado;
                        }
                    }
                    if ($scope.properties.selectedRow.ciudad === undefined || $scope.properties.selectedRow.ciudad === null || $scope.properties.selectedRow.ciudad === "") {
                        $scope.properties.selectedRow.ciudad = $scope.properties.selectedRow.escuela.ciudad;
                    }
                }

                this.showModalEditar(true);

            } else if (action === "eliminar") {
                swal({
                        title: "",
                        text: "¿Seguro que deseas continuar?",
                        icon: "warning",
                        buttons: true
                    })
                    .then((willDelete) => {
                        if (willDelete) {
                            $scope.properties.lstInformacionEscolarMod.splice(index, 1);
                            $scope.$apply();
                        }
                    });
            }
        }
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    this.showModalEliminar = function() {
        openModal($scope.properties.modalEliminar);
    }

    this.showModalEditar = function(_isEditar) {
        if (_isEditar) {
            $scope.properties.accionModal = "editar";
        } else {
            $scope.properties.accionModal = "ver";
        }

        openModal($scope.properties.modalEditar);
    }

    this.showModalAgregar = function() {
        $scope.properties.accionModal = "agregar";
        $scope.properties.selectedRow = angular.copy($scope.objInformacionEscolarInput);
        openModal($scope.properties.modalEditar);
    }

    function openModal(modal) {
        modalService.open(modal);
    }
}