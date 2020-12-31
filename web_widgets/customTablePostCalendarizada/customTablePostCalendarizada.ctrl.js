function PbTableCtrl($scope, $http, $window,blockUI) {

    this.isArray = Array.isArray;


    $scope.redirecc = function(row){
        var url = "/apps/administrativo/PaseDeLista?idsesion="+row.sesion.persistenceId+"&idprueba="+row.prueba.persistenceId;
        window.top.location.href = url;
    }
    
    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function (row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    this.isSelected = function (row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    function doRequest(method, url, params) {
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                $scope.properties.lstContenido = data.data;
                $scope.value = data.totalRegistros;
                $scope.loadPaginado();
                console.log(data.data)
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {
                
                blockUI.stop();
            });
    }

    $scope.asignarTarea = function (rowData) {

        var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
        };

        return $http(req)
            .success(function (data, status) {
                debugger
                var url = "/apps/administrativo/PaseDeLista";
                window.top.location.href = url;
            })
            .error(function (data, status) {
                console.error(data);
            })
            .finally(function () { });
    }
    $scope.isenvelope = false;
    $scope.selectedrow = {};
    $scope.mensaje = "";
    $scope.envelope = function (row) {
        $scope.isenvelope = true;
        $scope.mensaje = "";
        $scope.selectedrow = row;
    }
    $scope.envelopeCancel = function () {
        $scope.isenvelope = false;
        $scope.selectedrow = {};
    }
    $scope.sendMail = function (row, mensaje) {
        if (row.grupobonita == undefined) {
            for (var i = 0; i < $scope.lstCampus.length; i++) {
                if ($scope.lstCampus[i].descripcion == row.campus) {
                    row.grupobonita = $scope.lstCampus[i].valor;
                }
            }
        }
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10",
            data: angular.copy({
                "campus": row.grupobonita,
                "correo": row.correoelectronico,
                "codigo": "recordatorio",
                "isEnviar": true,
                "mensaje": mensaje
            })
        };

        return $http(req)
            .success(function (data, status) {

                $scope.envelopeCancel();
            })
            .error(function (data, status) {
                console.error(data)
            })
            .finally(function () { });
    }
    $scope.lstCampus = [{
        "descripcion": "Anáhuac Cancún",
        "valor": "CAMPUS-CANCUN"
    },
    {
        "descripcion": "Anáhuac Mayab",
        "valor": "CAMPUS-MAYAB"
    },
    {
        "descripcion": "Anáhuac México Norte",
        "valor": "CAMPUS-MNORTE"
    },
    {
        "descripcion": "Anáhuac México Sur",
        "valor": "CAMPUS-MSUR"
    },
    {
        "descripcion": "Anáhuac Oaxaca",
        "valor": "CAMPUS-OAXACA"
    },
    {
        "descripcion": "Anáhuac Puebla",
        "valor": "CAMPUS-PUEBLA"
    },
    {
        "descripcion": "Anáhuac Querétaro",
        "valor": "CAMPUS-QUERETARO"
    },
    {
        "descripcion": "Anáhuac Xalapa",
        "valor": "CAMPUS-XALAPA"
    },
    {
        "descripcion": "Juan Pablo II",
        "valor": "CAMPUS-JP2"
    },
    {
        "descripcion": "Anáhuac Cordoba",
        "valor": "CAMPUS-CORDOBA"
    }
    ];
    $(function () {
        doRequest("POST", $scope.properties.urlPost);
    })


    $scope.$watch("properties.dataToSend", function (newValue, oldValue) {
        if (newValue !== undefined) {
            doRequest("POST", $scope.properties.urlPost);
        }
        console.log($scope.properties.dataToSend);
    });
    $scope.setOrderBy = function (order) {
        if ($scope.properties.dataToSend.orderby == order) {
            $scope.properties.dataToSend.orientation = ($scope.properties.dataToSend.orientation == "ASC") ? "DESC" : "ASC";
        } else {
            $scope.properties.dataToSend.orderby = order;
            $scope.properties.dataToSend.orientation = "ASC";
        }
        doRequest("POST", $scope.properties.urlPost);
    }

    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 10;
    $scope.valorTotal = 10;

    $scope.loadPaginado = function () {
        $scope.valorTotal = Math.ceil($scope.value / $scope.properties.dataToSend.limit);
        $scope.lstPaginado = []
        if ($scope.valorSeleccionado <= 5) {
            $scope.iniciarP = 1;
            $scope.finalP = $scope.valorTotal > 10 ? 10 : $scope.valorTotal;
        }
        else {
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

    $scope.siguiente = function () {
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

    $scope.anterior = function () {
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

    $scope.seleccionarPagina = function (valorSeleccionado) {
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
    $scope.getCampusByGrupo = function (campus) {
        var retorno = "";
        for (var i = 0; i < $scope.lstCampus.length; i++) {
            if (campus == $scope.lstCampus[i].valor) {
                retorno = $scope.lstCampus[i].descripcion
            }
            
        }
        return retorno;
    }
    $scope.lstMembership = [];
    $scope.$watch("properties.userId", function (newValue, oldValue) {
        if (newValue !== undefined) {
            var req = {
                method: "GET",
                url: `/API/identity/membership?p=0&c=10&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
            };

            return $http(req)
                .success(function (data, status) {
                    $scope.lstMembership = data;
                })
                .error(function (data, status) {
                    console.error(data);
                })
                .finally(function () { });
        }
    });
    $scope.filtroCampus = ""
    $scope.addFilter = function () {
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
                    encontrado = true
                }
                if (!encontrado) {
                    $scope.properties.dataToSend.lstFiltro.push(filter);
                }

            }
        } else {
            $scope.properties.dataToSend.lstFiltro.push(filter);
        }
    }
    $scope.sizing=function(){
        $scope.lstPaginado = [];
        $scope.valorSeleccionado = 1;
        $scope.iniciarP = 1;
        $scope.finalP = 10;
        try{
            $scope.properties.dataToSend.limit=parseInt($scope.properties.dataToSend.limit);
        }catch(exception){
            
        }
        
        doRequest("POST", $scope.properties.urlPost);
    }
}