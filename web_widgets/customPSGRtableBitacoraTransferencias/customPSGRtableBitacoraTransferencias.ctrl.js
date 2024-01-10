function PbTableCtrl($scope, $http) {

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
    
    $scope.dataToSend = {
        limit: 20,
        offset: 0,
        lstFiltro: [
            
        ],
        esDestino: false
    }
    
    function selectBitacoraTransferencias(){
        let url = "../API/extension/posgradosRest?url=selectBitacoraTransferencias";
        $http.post(url, $scope.dataToSend).success(function(succsess){
            $scope.properties.content = succsess.data;
        }).error(function(err){
            swal("¡Atención!", err.error, "error");
        })
    }
    
    // selectBitacoraTransferencias();
    
    $scope.getCampusByGrupo = function(campus) {
        var retorno = "";
        for (var i = 0; i < $scope.properties.lstCampus.length; i++) {
            if (campus == $scope.properties.lstCampus[i].grupo_bonita) {
                retorno = $scope.properties.lstCampus[i].descripcion
                if ($scope.lstCampusByUser.length == 2) {
                    $scope.properties.campusSeleccionado = $scope.properties.lstCampus[i].grupo_bonita
                }
            } else if (campus == "Todos los campus") {
                retorno = campus
            }
        }
        return retorno;
    }
  
    $scope.lstMembership = [];
    $scope.$watch("properties.userId", function(newValue, oldValue) {
        if (newValue !== undefined) {
            var req = {
                method: "GET",
                url: `/API/identity/membership?p=0&c=100&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
            };
  
            return $http(req)
                .success(function(data, status) {
                    $scope.lstMembership = data;
                    $scope.campusByUser();
                })
                .error(function(data, status) {
                    console.error(data);
                })
                .finally(function() {});
        }
    });
  
    $scope.lstCampusByUser = [];
    $scope.campusByUser = function() {
        var resultado = [];
        // resultado.push("Todos los campus")
        for (var x in $scope.lstMembership) {
            if ($scope.lstMembership[x].group_id.name.indexOf("CAMPUS") != -1) {
                let i = 0;
                resultado.forEach(value => {
                    if (value == $scope.lstMembership[x].group_id.name) {
                        i++;
                    }
                });
                if (i === 0) {
                    resultado.push($scope.lstMembership[x].group_id.name);
                }
            }
        }
        
        $scope.lstCampusByUser = resultado;
    }
    
    $scope.filtroCampus = "";

    $scope.addFilter = function() {
        if ($scope.filtroCampus != "Todos los campus") {
            $scope.licenciaturas = [];
            $scope.periodos = [];
            $scope.filtroPeriodo = "";
            $scope.filtroLicenciatura = "";
            $scope.mostrarFiltros = true;
            
            for (var i = 0; i < $scope.properties.lstCampus.length; i++) {
                if ($scope.properties.lstCampus[i].descripcion === $scope.filtroCampus) {
                    $scope.persistenceid = $scope.properties.lstCampus[i].persistenceId;
                    break;
                }
            }
            
            var filter = {
                "columna": "CAMPUS",
                "operador": "Igual a",
                "valor": $scope.filtroCampus,
                "persistenceid": $scope.persistenceid
            };

            if ($scope.dataToSend.lstFiltro.length > 0) {
                var encontrado = false;
                for (let index = 0; index < $scope.dataToSend.lstFiltro.length; index++) {
                    const element = $scope.dataToSend.lstFiltro[index];
                    if (element.columna == "CAMPUS") {
                        $scope.dataToSend.lstFiltro[index].columna = filter.columna;
                        $scope.dataToSend.lstFiltro[index].operador = filter.operador;
                        $scope.dataToSend.lstFiltro[index].valor = $scope.filtroCampus;
                        for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                            if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                                $scope.properties.campusSeleccionado = $scope.lstCampus[index2].descripcion;
                            }
                        }
                        encontrado = true
                    }
                }
  
                if (!encontrado) {
                    $scope.dataToSend.lstFiltro.push(filter);
                    for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                        if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                            $scope.properties.campusSeleccionado = $scope.lstCampus[index2].descripcion;
                        }
                    }
                }
            } else {
                $scope.dataToSend.lstFiltro.push(filter);
                for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                    if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                        $scope.properties.campusSeleccionado = $scope.lstCampus[index2].descripcion;
                    }
                }
            }

        } else {
            $scope.mostrarFiltros = false;
            if ($scope.dataToSend.lstFiltro.length > 0) {
                var encontrado = false;
                for (let index = 0; index < $scope.dataToSend.lstFiltro.length; index++) {
                    const element = $scope.dataToSend.lstFiltro[index];
                    if (element.columna == "CAMPUS") {
                        $scope.dataToSend.lstFiltro.splice(index, 1);
                        $scope.properties.campusSeleccionado = null;
                    }
                }
            } else {
                $scope.properties.campusSeleccionado = null;
            }
        }
    }
  
    $scope.getCatCampus = function() {
        var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.posgrados.catalog.PSGRCatCampus?q=getCat&p=0&c=9999&f=eliminado=false"
        };
  
        return $http(req)
            .success(function(data, status) {
                $scope.lstCampus = [];
                for (var index in data) {
                    $scope.lstCampus.push({
                        "descripcion": data[index].descripcion,
                        "valor": data[index].grupoBonita
                    })
                }
            })
            .error(function(data, status) {
                console.error(data);
            });
    }
    
    $scope.getCatCampus();
    
    // $scope.$watch("properties.campusSeleccionado", function(newValue, oldValue) {
    //     if (newValue !== undefined) {
    //         selectBitacoraTransferencias();
    //     }
    // });

    $scope.$watch("filtroCampus", function(newValue, oldValue) {
        if (newValue) {
        	selectBitacoraTransferencias();
        }
    });
}
