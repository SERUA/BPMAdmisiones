function PbTableCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
    this.isArray = Array.isArray;
    $scope.dynamicInput = {};
    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        $scope.properties.selectedRow = row;
        $scope.properties.isSelected = 'editar';
    };
    $scope.displayed = function(header) {
        var retorno = "";

        if (header == 'PERSISTENCEID') {
            retorno = 'Clave';
        } else if (header == 'FECHACREACION') {
            retorno = 'Fecha creación';
        } else {
            retorno = header;
        }

        return retorno;
    }

    this.selectRowDelete = function(row) {
        swal("¿Está seguro que desea eliminar?", {
                buttons: {
                    cancel: "No",
                    catch: {
                        text: "Si",
                        value: "Si",
                    }
                },
            })
            .then((value) => {
                switch (value) {
                    case "Si":
                        
                        $scope.properties.selectedRow = row;
                        row.isEliminado = true
                        $scope.properties.selectedRow["todelete"] = false;

                        $scope.$apply();
                        startProcess();
                        break;
                    default:

                }
            });
        /*
        
        $scope.properties.isSelected = 'editar';*/
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    function startProcess() {
        if ($scope.properties.processId) {
            var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
    }

    function doRequest(method, url, params, dataToSend, callback) {
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data)

            })
            .error(function(data, status) {

            }).finally(function() {
                blockUI.stop();
            });
    }
    $scope.content = [];

    function doRequestGet() {

        var notfound = true;
        $scope.filtro = {
            "columna": "CAMPUS",
            "operador": "Igual a",
            "valor": $scope.properties.campusSelected.grupoBonita
        }
        var dataToSend = angular.copy($scope.properties.filtroToSend);
        for (let index = 0; index < dataToSend.lstFiltro.length; index++) {
            const element = dataToSend.lstFiltro[index];
            if (element.columna == "CAMPUS") {
                dataToSend.lstFiltro[index].valor = value.grupoBonita
                notfound = false;
            }

        }
        if (notfound) {
            dataToSend.lstFiltro.push($scope.filtro);
        }

        blockUI.start();
        var req = {
            method: "GET",
            url: `${$scope.properties.urlGet}&jsonData=${encodeURIComponent(JSON.stringify(dataToSend))}`
        };

        return $http(req)
            .success(function(data, status) {
                $scope.content = data.data;
                $scope.value = data.totalRegistros;
                $scope.loadPaginado();
            })
            .error(function(data, status) {

            }).finally(function() {
                blockUI.stop();
            });
    }
    $scope.$watch("properties.filtroToSend", function(newValue, oldValue) {
        if (newValue !== undefined) {
            //doRequestGet();
        }
        console.log($scope.properties.filtroToSend);
    });
    $scope.setOrderBy = function(order) {
        if ($scope.properties.filtroToSend.orderby == order) {
            $scope.properties.filtroToSend.orientation = ($scope.properties.filtroToSend.orientation == "ASC") ? "DESC" : "ASC";
        } else {
            $scope.properties.filtroToSend.orderby = order;
            $scope.properties.filtroToSend.orientation = "ASC";
        }
        doRequestGet();
    }
    $scope.filterKeyPress = function(columna, press) {
        var aplicado = true;
        for (let index = 0; index < $scope.properties.filtroToSend.lstFiltro.length; index++) {
            const element = $scope.properties.filtroToSend.lstFiltro[index];
            if (element.columna == columna) {
                $scope.properties.filtroToSend.lstFiltro[index].valor = press;
                $scope.properties.filtroToSend.lstFiltro[index].operador = "Que contenga";
                aplicado = false;
            }

        }
        if (aplicado) {
            var obj = { "columna": columna, "operador": "Que contenga", "valor": press }
            $scope.properties.filtroToSend.lstFiltro.push(obj);
        }

        doRequestGet();
    }
    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 10;
    $scope.valorTotal = 10;

    $scope.loadPaginado = function() {
        $scope.valorTotal = Math.ceil($scope.value / $scope.properties.filtroToSend.limit);
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
        if ($scope.valorSeleccionado > Math.ceil($scope.value / $scope.properties.filtroToSend.limit)) {
            $scope.valorSeleccionado = Math.ceil($scope.value / $scope.properties.filtroToSend.limit);
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
                $scope.properties.filtroToSend.offset = (($scope.lstPaginado[i].numero - 1) * $scope.properties.filtroToSend.limit)
            }
        }

        doRequestGet();
    }
    $scope.filtroCampus = ""
    $scope.addFilter = function() {
        var filter = {
            "columna": "CAMPUS",
            "operador": "Igual a",
            "valor": $scope.filtroCampus
        }
        if ($scope.properties.filtroToSend.lstFiltro.length > 0) {
            var encontrado = false;
            for (let index = 0; index < $scope.properties.filtroToSend.lstFiltro.length; index++) {
                const element = $scope.properties.filtroToSend.lstFiltro[index];
                if (element.columna == "CAMPUS") {
                    $scope.properties.filtroToSend.lstFiltro[index].columna = filter.columna;
                    $scope.properties.filtroToSend.lstFiltro[index].operador = filter.operador;
                    $scope.properties.filtroToSend.lstFiltro[index].valor = $scope.filtroCampus;
                    encontrado = true
                }
                if (!encontrado) {
                    $scope.properties.filtroToSend.lstFiltro.push(filter);
                }

            }
        } else {
            $scope.properties.filtroToSend.lstFiltro.push(filter);
        }
    }
    $scope.sizing = function() {
        $scope.lstPaginado = [];
        $scope.valorSeleccionado = 1;
        $scope.iniciarP = 1;
        $scope.finalP = 10;
        try {
            $scope.properties.filtroToSend.limit = parseInt($scope.properties.filtroToSend.limit);
        } catch (exception) {

        }

        doRequestGet();
    }
    $scope.previewModal = function(data) {
        var dataToSend = {
            "campus": data.campus,
            "correo": data.para,
            "codigo": data.codigo,
            "mensaje": data.mensaje,
            "isEnviar": false
        }
        
        debugger;
        let url = "/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10";

        if(dataToSend.codigo.toLowerCase().includes("sdae")){
            url  = "/bonita/API/extension/AnahuacRest?url=generateHtmlSDAE&p=0&c=10";
        }

        doRequest("POST", url, {}, dataToSend, function(data) {
            $scope.firma = angular.copy(data.data)
            var value = data.data;
            var element = document.getElementById("firma");
            if (dataToSend.codigo == 'cambios' || dataToSend.codigo == 'rechazada' || dataToSend.codigo == 'listaroja') {
                doRequest("GET", "/API/bdm/businessData/com.anahuac.catalogos.CatRegistro?q=findByCorreoelectronico&p=0&c=999&f=correoelectronico=" + dataToSend.correo, null, null, function(response) {
                    doRequest("GET", "/API/bdm/businessData/com.anahuac.model.DetalleSolicitud?q=findByCaseId&p=0&c=999&f=caseId=" + response[0].caseId, null, null, function(response) {
                        value = value[0];
                        value = value.replace("[COMENTARIOS-CAMBIO]", response[0].observacionesCambio)
                        value = value.replace("[RECHAZO-COMENTARIOS]", response[0].observacionesRechazo)
                        value = value.replace("[LISTAROJA-COMENTARIOS]", response[0].observacionesListaRoja)
                        element.innerHTML = value
                        $(`#previewFirma`).modal('show')
                    })
                })
            } else {
                element.innerHTML = value
                $(`#previewFirma`).modal('show')
            }
        })

    }
    $scope.$watch('properties.campusSelected', function(value) {
        if (angular.isDefined(value) && value !== null && angular.isDefined($scope.properties.filtroToSend)) {
            /*var notfound = true;
            $scope.filtro = {
                "columna": "CAMPUS",
                "operador": "Igual a",
                "valor": value.grupoBonita
            }
            for (let index = 0; index < $scope.properties.filtroToSend.lstFiltro.length; index++) {
                const element = $scope.properties.filtroToSend.lstFiltro[index];
                if (element.columna == "CAMPUS") {
                    $scope.properties.filtroToSend.lstFiltro[index].valor = value.grupoBonita
                    notfound = false;
                }

            }
            if (notfound) {
                $scope.properties.filtroToSend.lstFiltro.push($scope.filtro);
            }*/


            doRequestGet();
        }
    });

    $scope.sendMail = function(data) {
        Swal.fire({
            title: `¿Está seguro que desea reenviar email?`,
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: '#FF5900',
            cancelButtonColor: '#231F20',
            confirmButtonText: 'Continuar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                var dataToSend = {
                    "campus": data.campus,
                    "correo": data.para,
                    "codigo": data.codigo,
                    "isEnviar": true
                };

                let url = "/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10";
                
                if(dataToSend.codigo.toLowerCase().includes("sdae")){
                    url = "/bonita/API/extension/AnahuacRest?url=generateHtmlSDAE&p=0&c=10";
                }

                doRequest("POST", url, {}, dataToSend, function(data) {
                    Swal.fire("Enviado", "Correo enviado correctamente", "success");
                });

                // if(){
                //     doRequest("POST", "/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10", {}, dataToSend, function(data) {
                //         Swal.fire("Enviado", "Correo enviado correctamente", "success");
                //     })
                // } else {
                //     doRequest("POST", "/bonita/API/extension/AnahuacRest?url=generateHtmlSDAE&p=0&c=10", {}, dataToSend, function(data) {
                //         Swal.fire("Enviado", "Correo enviado correctamente", "success");
                //     })
                // }

            } else {

            }
        })


    }
}