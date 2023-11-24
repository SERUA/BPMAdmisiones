function PbTableCtrl($scope, $http, $window, blockUI) {

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
  
    function doRequest(method, url, params) {
        if ($scope.seleccion == true) {
            blockUI.start();
            var req = {
                method: method,
                url: url,
                data: angular.copy($scope.properties.dataToSend),
                params: params
            };
        
            return $http(req)
                .success(function(data, status) {
                    $scope.properties.lstContenido = data.data;
                    $scope.value = data.totalRegistros;
                    $scope.loadPaginado();
                    console.log(data.data)
                })
                .error(function(data, status) {
                    notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                })
                .finally(function() {
                    blockUI.stop();
                });
        }
    }
  
  
    $scope.lstCampus = [];
    $(function() {
        doRequest("POST", $scope.properties.urlPost);
    })
  
  
    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        if (newValue !== undefined) {
            doRequest("POST", $scope.properties.urlPost);
        }
        console.log($scope.properties.dataToSend);
  
    });
    $scope.setOrderBy = function(order) {
        if ($scope.properties.dataToSend.orderby == order) {
            $scope.properties.dataToSend.orientation = ($scope.properties.dataToSend.orientation == "ASC") ? "DESC" : "ASC";
        } else {
            $scope.properties.dataToSend.orderby = order;
            $scope.properties.dataToSend.orientation = "ASC";
        }
        doRequest("POST", $scope.properties.urlPost);
    }
  
    $scope.filterKeyPress = function(columna, press) {
        var aplicado = true;
        for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
            const element = $scope.properties.dataToSend.lstFiltro[index];
            if (element.columna == columna) {
                $scope.properties.dataToSend.lstFiltro[index].valor = press;
                $scope.properties.dataToSend.lstFiltro[index].operador = "Que contengan";
                aplicado = false;
            }
  
        }
        if (aplicado) {
            var obj = { "columna": columna, "operador": "Que contengan", "valor": press }
            $scope.properties.dataToSend.lstFiltro.push(obj);
        }
  
        doRequest("POST", $scope.properties.urlPost);
    }
  
  
    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 10;
    $scope.valorTotal = 10;
  
    $scope.loadPaginado = function() {
        $scope.valorTotal = Math.ceil($scope.value / $scope.properties.dataToSend.limit);
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
        if ($scope.valorSeleccionado > Math.ceil($scope.value / $scope.properties.dataToSend.limit)) {
            $scope.valorSeleccionado = Math.ceil($scope.value / $scope.properties.dataToSend.limit);
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
                $scope.properties.dataToSend.offset = (($scope.lstPaginado[i].numero - 1) * $scope.properties.dataToSend.limit)
            }
        }
  
        doRequest("POST", $scope.properties.urlPost);
    }
    $scope.getCampusByGrupo = function(campus) {
        var retorno = "";
        for (var i = 0; i < $scope.properties.lstCampus.length; i++) {
            if (campus == $scope.properties.lstCampus[i].grupoBonita) {
                retorno = $scope.properties.lstCampus[i].descripcion
                if ($scope.lstCampusByUser.length == 2) {
                    $scope.properties.campusSeleccionado = $scope.properties.lstCampus[i].grupoBonita
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
        // var isSerua = true;
        resultado.push("Todos los campus")
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
        // if(isSerua){
        //     resultado.push("Todos los campus")
        // }
        $scope.lstCampusByUser = resultado;
    }
    $scope.filtroCampus = ""
    $scope.addFilter = function() {
        $scope.seleccion = true;
        if ($scope.filtroCampus != "Todos los campus") {
            var filter = {
                "columna": "CAMPUS",
                "operador": "Igual a",
                "valor": $scope.filtroCampus
            }
            if ($scope.properties.dataToSend.lstFiltro.length > 0) {
                var encontrado = false;
                for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
                    const element = $scope.properties.dataToSend.lstFiltro[index];
                    if (element.columna == "CAMPUS") {
                        $scope.properties.dataToSend.lstFiltro[index].columna = filter.columna;
                        $scope.properties.dataToSend.lstFiltro[index].operador = filter.operador;
                        $scope.properties.dataToSend.lstFiltro[index].valor = $scope.filtroCampus;
                        for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                            if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                                $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;
                            }
                        }
                        encontrado = true
                    }
                }
  
                if (!encontrado) {
                    $scope.properties.dataToSend.lstFiltro.push(filter);
                    for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                        if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                            $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;
                        }
                    }
                }
            } else {
                $scope.properties.dataToSend.lstFiltro.push(filter);
                for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                    if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                        $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;
                    }
                }
            }
        } else {
  
            if ($scope.properties.dataToSend.lstFiltro.length > 0) {
                var encontrado = false;
                for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
                    const element = $scope.properties.dataToSend.lstFiltro[index];
                    if (element.columna == "CAMPUS") {
                        $scope.properties.dataToSend.lstFiltro.splice(index, 1);
                        $scope.properties.campusSeleccionado = null;
                    }
                }
            }
            else if ($scope.properties.dataToSend.lstFiltro.length == 0){
                doRequest("POST", $scope.properties.urlPost);
            }
        }
    }
    $scope.sizing = function() {
        $scope.lstPaginado = [];
        $scope.valorSeleccionado = 1;
        $scope.iniciarP = 1;
        $scope.finalP = 10;
        try {
            $scope.properties.dataToSend.limit = parseInt($scope.properties.dataToSend.limit);
        } catch (exception) {
  
        }
  
        doRequest("POST", $scope.properties.urlPost);
    }
  
    $scope.getCatCampus = function() {
        var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.catalogos.CatCampus?q=find&p=0&c=100"
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
  
    $scope.cargaManual = function(row) {
        if ($scope.isPeriodoVencido(row.periodofin)) {
            swal("¡Periodo vencido!", "El periodo del aspirante ha vencido, se debe actualizar para poder continuar con el proceso", "warning").then((value) => {
  
                $scope.properties.datosAspirante = {
                    "IDBANNER": "",
                    "FECHAEXAMEN": "",
                    "IdSesion": "",
                    "CIT1": "",
                    "CIT2": "",
                    "HI1": "",
                    "HI2": "",
                    "HI3": "",
                    "HI4": "",
                    "HI5": "",
                    "HI6": "",
                    "LA1": "",
                    "LA2": "",
                    "LA3": "",
                    "LA4": "",
                    "LEO1": "",
                    "LEO3": "",
                    "LEO4": "",
                    "LEO5": "",
                    "PAAN": "",
                    "PAAV": "",
                    "PARA": "",
                    "PG1": "",
                    "PG2": "",
                    "PG3": "",
                    "PG4": "",
                    "PG5": "",
                    "PV1": "",
                    "PV4": "",
                    "TOTAL": "",
                    "tipoExamen": "",
                    "MLEX": "",
                    "CLEX": "",
                    "HLEX": "",
                    "INVP": ""
                };
                $scope.properties.datosAspirante.IDBANNER = row.idbanner;
                $scope.properties.datosAspirante.IdSesion = row.id;
                $scope.properties.tabla = "fragmento";
                $scope.properties.view = false;
                /*var req = {
                    method: "GET",
                    url: `/API/extension/AnahuacRestGet?url=getAspirantePAA&p=0&c=10&idbanner=${row.idbanner}`
                };
                return $http(req)
                    .success(function (data, status) {
                        cargaDeDatos($scope.properties.datosAspirante,data.data[0])
                    })
                    .error(function (data, status) {
                        console.error(data);
                    })
                    .finally(function () { });*/
  
                    $scope.$apply();
            });
        } else {
            $scope.properties.datosAspirante = {
                "IDBANNER": "",
                "FECHAEXAMEN": "",
                "IdSesion": "",
                "CIT1": "",
                "CIT2": "",
                "HI1": "",
                "HI2": "",
                "HI3": "",
                "HI4": "",
                "HI5": "",
                "HI6": "",
                "LA1": "",
                "LA2": "",
                "LA3": "",
                "LA4": "",
                "LEO1": "",
                "LEO3": "",
                "LEO4": "",
                "LEO5": "",
                "PAAN": "",
                "PAAV": "",
                "PARA": "",
                "PG1": "",
                "PG2": "",
                "PG3": "",
                "PG4": "",
                "PG5": "",
                "PV1": "",
                "PV4": "",
                "TOTAL": "",
                "tipoExamen": "",
                "MLEX": "",
                "CLEX": "",
                "HLEX": "",
                "INVP": ""
            };
            $scope.properties.datosAspirante.IDBANNER = row.idbanner;
            $scope.properties.datosAspirante.IdSesion = row.id;
            $scope.properties.tabla = "fragmento";
            $scope.properties.view = false;
            /*var req = {
                method: "GET",
                url: `/API/extension/AnahuacRestGet?url=getAspirantePAA&p=0&c=10&idbanner=${row.idbanner}`
            };
            return $http(req)
                .success(function (data, status) {
                    cargaDeDatos($scope.properties.datosAspirante,data.data[0])
                })
                .error(function (data, status) {
                    console.error(data);
                })
                .finally(function () { });*/
  
        }
    }
  
    function cargaDeDatos(json, data) {
        var datos = data;
        if (datos !== null && datos !== undefined) {
            let columna = datos;
            for (var key in columna) {
                if (key != "total" && key != "fechaexamen" && key != "tipoexamen" && key != "lexiumpara" && key != "lexiumpaav" && key != "lexiumpaan" && key != "lexiumtotal") {
                    json[key.toUpperCase()] = data[key]
                } else if (key === "total") {
                    json.Total = data[key]
                } else if (key == "fechaexamen") {
                    json.fechaExamen = data[key]
                } else if (key == "tipoexamen") {
                    json.tipoExamen = data[key];
                } else if (key == "lexiumpara") {
                    json.LEXIUM_PARA = data[key]
                } else if (key == "lexiumpaav") {
                    json.LEXIUM_PAAV = data[key]
                } else if (key == "lexiumpaan") {
                    json.LEXIUM_PAAN = data[key]
                } else if (key == "lexiumtotal") {
                    json.LEXIUM_Total = data[key]
                }
            }
  
        }
    }
  
    $scope.isPeriodoVencido = function(periodofin) {
        var fecha = new Date(periodofin.slice(0, 10))
        return fecha < new Date();
    }
  
  }