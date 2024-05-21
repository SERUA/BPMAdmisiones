function PbTableCtrl($scope, $http, modalService) {

    this.isArray = Array.isArray;
    
     // Campus del catalogo
    $scope.lstCampus = [];

    // Campus mostrados (resultado del match entre la lista del catalogo de campus y los campus disponibles para el usuarios)
    $scope.campusDisponibles = [];

    // Valor del input select
    $scope.selectedCampus = "";

    // Valor seleccionado
    $scope.properties.campusSeleccionado = null;
    
    // Forzar a cargar los campus del catalogo
    getCatCampus();
    
    // Scope functions
    
    // Utils
    
    function getCatCampus() {
        var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.posgrados.catalog.PSGRCatCampus?q=getCat&p=0&c=9999&f=eliminado=false"
        };
  
        return $http(req)
            .success(function(data, status) {
                $scope.lstCampus = data;
            })
            .error(function(data, status) {
                console.error(data);
            });
    }
    
    function updateCampusDisponibles() {
        const campusDisponibles = [];

        if ($scope.lstCampus && $scope.lstCampus.length > 0 && $scope.lstCampusByUser && $scope.lstCampusByUser.length > 0) {

            for (let catCampus of $scope.lstCampus) {
                if ($scope.lstCampusByUser.find(grupo_bonita => catCampus.grupo_bonita === grupo_bonita)) {
                    campusDisponibles.push(catCampus);
                }
            }
        }
        
        $scope.campusDisponibles = campusDisponibles;
        
        // Cuando es campus unico seleccionarlo por defecto.
        if (campusDisponibles.length === 1) {
            $scope.filtroCampus = campusDisponibles[0].descripcion;
            $scope.addFilter();
        }
    }
    
    // Watchers

    $scope.$watch("lstCampus", function (newValue, oldValue) {
        updateCampusDisponibles();
    });

    $scope.$watch("lstCampusByUser", function (newValue, oldValue) {
        updateCampusDisponibles();
    });
    
    // --------------
    
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
    
    $scope.verTransferido = function(){
        modalService.open("modalVerTransferido");    
    }
    
    $scope.dataToSend = {
        limit: 20,
        offset: 0,
        lstFiltro: [],
        esDestino: true,
        orderby: "fecha",
        orientation: "DESC"
    }
    
    function selectBitacoraTransferencias(){
        let url = "../API/extension/posgradosRest?url=selectBitacoraTransferencias";
        $http.post(url, $scope.dataToSend).success(function(success){
            $scope.properties.content = success.data;
            $scope.value = success.totalRegistros;
            $scope.loadPaginado();
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
    
    //$scope.getCatCampus();

    $scope.$watch("filtroCampus", function(newValue, oldValue) {
        if (newValue) {
        	selectBitacoraTransferencias();
        }
    });

    $scope.setOrderBy = function(order) {
        if ($scope.dataToSend.orderby == order) {
            $scope.dataToSend.orientation = ($scope.dataToSend.orientation == "ASC") ? "DESC" : "ASC";
        } else {
            $scope.dataToSend.orderby = order;
            $scope.dataToSend.orientation = "ASC";
        }
        selectBitacoraTransferencias();
    }

    $scope.filterKeyPress = function(columna, press) {
        var aplicado = true;
  
        for (let index = 0; index < $scope.dataToSend.lstFiltro.length; index++) {
            const element = $scope.dataToSend.lstFiltro[index];
            if (element.columna == columna) {
                $scope.dataToSend.lstFiltro[index].valor = press;
                $scope.dataToSend.lstFiltro[index].operador = "Que contengan";
                aplicado = false;
            }
  
        }
        if (aplicado) {
            var obj = { "columna": columna, "operador": "Que contengan", "valor": press }
            $scope.dataToSend.lstFiltro.push(obj);
        }
  
        selectBitacoraTransferencias();
    }

    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 10;
    $scope.valorTotal = 10;
  
    $scope.loadPaginado = function() {
        $scope.valorTotal = Math.ceil($scope.value / $scope.dataToSend.limit);
        $scope.lstPaginado = []
        if ($scope.valorSeleccionado <= 5) {
            $scope.iniciarP = 1;
            $scope.finalP = $scope.valorTotal > 10 ? 10 : $scope.valorTotal;
        } else {
            $scope.iniciarP = $scope.valorSeleccionado - 5;
            $scope.finalP = $scope.valorTotal > ($scope.valorSeleccionado + 4) ? ($scope.valorSeleccionado + 4) : $scope.valorTotal;
        }
        for (var i = $scope.iniciarP; i <= $scope.finalP; i++) {
  
            var obj = {
                "numero": i,
                "inicio": ((i * 10) - 9),
                "fin": (i * 10),
                "seleccionado": (i == $scope.valorSeleccionado)
            };
            $scope.lstPaginado.push(obj);
        }
    }
  
    $scope.siguiente = function() {
        var objSelected = {};
        for (var i in $scope.lstPaginado) {
            if ($scope.lstPaginado[i].seleccionado) {
                objSelected = $scope.lstPaginado[i];
                $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
            }
        }
        $scope.valorSeleccionado = $scope.valorSeleccionado + 1;
        if ($scope.valorSeleccionado > Math.ceil($scope.value / $scope.dataToSend.limit)) {
            $scope.valorSeleccionado = Math.ceil($scope.value / $scope.dataToSend.limit);
        }
        $scope.seleccionarPagina($scope.valorSeleccionado);
    }
  
    $scope.anterior = function() {
        var objSelected = {};
        for (var i in $scope.lstPaginado) {
            if ($scope.lstPaginado[i].seleccionado) {
                objSelected = $scope.lstPaginado[i];
                $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
            }
        }
        $scope.valorSeleccionado = $scope.valorSeleccionado - 1;
        if ($scope.valorSeleccionado == 0) {
            $scope.valorSeleccionado = 1;
        }
        $scope.seleccionarPagina($scope.valorSeleccionado);
    }
  
    $scope.seleccionarPagina = function(valorSeleccionado) {
        var objSelected = {};
        for (var i in $scope.lstPaginado) {
            if ($scope.lstPaginado[i].numero == valorSeleccionado) {
                $scope.inicio = ($scope.lstPaginado[i].numero - 1);
                $scope.fin = $scope.lstPaginado[i].fin;
                $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
                $scope.dataToSend.offset = (($scope.lstPaginado[i].numero - 1) * $scope.dataToSend.limit)
            }
        }
  
        selectBitacoraTransferencias();
    }
    
    $scope.deleteContent = function(objContent) {
        var index = $scope.dataToSend.lstFiltros.indexOf(objContent);
        if(index != -1){
            $scope.dataToSend.lstFiltros.splice(index, 1);
        }
    }
    
    $scope.cambiarDestino = function(){
        selectBitacoraTransferencias();
    }
    
    $scope.sizing = function() {
        $scope.lstPaginado = [];
        $scope.valorSeleccionado = 1;
        $scope.iniciarP = 1;
        $scope.finalP = 10;
        
        try {
            $scope.dataToSend.limit = parseInt($scope.dataToSend.limit);
        } catch (exception) {
  
        }
  
        selectBitacoraTransferencias();
    }
}