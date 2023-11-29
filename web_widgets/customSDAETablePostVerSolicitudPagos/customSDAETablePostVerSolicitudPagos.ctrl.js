function PbTableCtrl($scope, $http, $window, blockUI) {

    this.isArray = Array.isArray;
    $scope.comentariosRechazar = "";
    $scope.comentariosAprobar = "";
    
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

        return $http(req).success(function (data, status) {
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

    $scope.verSolicitud = function (rowData) {
        var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
        };

        return $http(req).success(function (data, status) {
            debugger;
            rowData.taskId = data[0].id;
            rowData.taskName = data[0].name;
            rowData.processId = data[0].processId;
            $scope.preProcesoAsignarTarea(rowData);
        })
        .error(function (data, status) {
            console.error(data);
        })
        .finally(function () { });
    }

    $scope.preAsignarTarea = function (rowData) {
        var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
        };

        return $http(req).success(function (data, status) {
            rowData.taskId = data[0].id;
            rowData.taskName = data[0].name;
            rowData.processId = data[0].processId;
            $scope.preProcesoAsignarTarea(rowData);
        })
        .error(function (data, status) {
            console.error(data);
        })
        .finally(function () {

        });
    }

    $scope.preProcesoAsignarTarea = function (rowData) {

        var req = {
            method: "GET",
            url: `/API/bpm/process/${rowData.processId}?d=deployedBy&n=openCases&n=failedCases`
        };

        return $http(req).success(function (data, status) {
            rowData.processName = data.name;
            rowData.processVersion = data.version;
            $scope.asignarTarea(rowData);
        })
        .error(function (data, status) {
            console.error(data);
        })
        .finally(function () {

        });
    }

    $scope.asignarTarea = function (rowData) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + rowData.taskId,
            data: angular.copy({ "assigned_id": "" })
        };

        return $http(req).success(function (data, status) {
            redireccionarTarea(rowData);
        })
        .error(function (data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () {

        });
    }

    function abrirSolicitud(rowData) {

        var url = "/bonita/portal/resource/app/sdae/" + $scope.properties.abrirPagina + "/content/?app=sdae&caseId=" + rowData.caseid;
        window.open(url, '_blank');
    }

    function redireccionarTarea(rowData) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + rowData.taskId,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };

        return $http(req).success(function (data, status) {
            var url = "/bonita/portal/resource/app/sdae/" + $scope.properties.abrirPagina + "/content/?app=sdae&id=" + rowData.taskId + "&caseId=" + rowData.caseid;
            window.open(url, '_blank');
        })
        .error(function (data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () {

        });
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
        if (row.catCampus.grupoBonita == undefined) {
            for (var i = 0; i < $scope.lstCampus.length; i++) {
                if ($scope.lstCampus[i].descripcion == row.catCampus.descripcion) {
                    row.catCampus.grupoBonita = $scope.lstCampus[i].valor;
                }
            }
        }

        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10",
            data: angular.copy({
                "campus": row.catCampus.grupoBonita,
                "correo": row.correoElectronico,
                "codigo": "recordatorio",
                "isEnviar": true,
                "mensaje": mensaje
            })
        };

        return $http(req).success(function (data, status) {
            $scope.envelopeCancel();
        })
        .error(function (data, status) {
            console.error(data)
        })
        .finally(function () { });
    }
    $scope.lstCampus = [];

    $(function () {
        doRequest("POST", $scope.properties.urlPost);
    })

    $scope.$watch("properties.dataToSend", function (newValue, oldValue) {
        if (newValue !== undefined) {
            if ($scope.properties.campusSeleccionado !== undefined) {
                doRequest("POST", $scope.properties.urlPost);
            }
        }
        console.log($scope.properties.dataToSend);
    });

    $scope.$watch("properties.campusSeleccionado", function (newValue, oldValue) {
        if (newValue !== undefined) {
            if ($scope.properties.campusSeleccionado !== undefined) {
                doRequest("POST", $scope.properties.urlPost);
            }
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
    $scope.filterKeyPress = function (columna, press) {
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

    $scope.loadPaginado = function () {
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

    $scope.$watch("properties.userId", function (newValue, oldValue) {
        if (newValue !== undefined) {
            var req = {
                method: "GET",
                url: `/API/identity/membership?p=0&c=100&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
            };

            return $http(req)
            .success(function (data, status) {
                $scope.lstMembership = data;
                $scope.campusByUser();
            })
            .error(function (data, status) {
                console.error(data);
            })
            .finally(function () { });
        }
    });

    $scope.lstCampusByUser = [];

    $scope.campusByUser = function () {
        var resultado = [];
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
        
        $scope.lstCampusByUser = resultado;
    }

    $scope.filtroCampus = "";

    $scope.addFilter = function () {
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
                        encontrado = true;
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
            } else {
                $scope.properties.campusSeleccionado = null;
            }
        }
    }

    $scope.sizing = function () {
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

    $scope.getCatCampus = function () {
        var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.catalogos.CatCampus?q=find&p=0&c=100"
        };

        return $http(req)
        .success(function (data, status) {
            $scope.lstCampus = [];
            for (var index in data) {
                $scope.lstCampus.push({
                    "descripcion": data[index].descripcion,
                    "valor": data[index].grupoBonita
                })
            }
        })
        .error(function (data, status) {
            console.error(data);
        });
    }
    $scope.isPeriodoVencido = function (periodofin) {
        var fecha = new Date(periodofin.slice(0, 10))
        return fecha < new Date();
    }

    $scope.abrirSolicitud = function (row) {
        var url = "/bonita/portal/resource/app/sdae/" + $scope.properties.abrirPagina + "/content/?app=sdae&caseId=" + row.caseid;
        window.open(url, '_blank');
    }


    $scope.getCatCampus();

    function doRequest2(method, url, params, dataToSend, callback) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: dataToSend,
            params: params
        };

        return $http(req).success(function (data, status) {
            callback(data);
        })
        .error(function (data, status) {
            console.error(data);

        })
        .finally(function () {
            vm.busy = false;
        });
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
    
    function downloadFile(_document) {
        const linkSource = "data:application/pdf; base64,"+ _document;
        const downloadLink = document.createElement("a");
        let fileName = "solicitud_de_apoyo_educativo.pdf";
        downloadLink.href = linkSource;
        downloadLink.download = fileName;
        downloadLink.click();
    }
    
    $scope.downloadFile = function(_email, _caseId){
        let url = "../API/extension/DocAPI?pdf=pdfSolicitudApoyo&p=0&c=1&email=" + _email + "&caseid=" + _caseId;
        $http.post(url, {}).success(function(success){
            downloadFile(success.data[0]); 
        }).error(function(err){
           swal("Error", "No se ha podido generar el archivo, intentelo de nuevo mas tarde.", "error");
        });
    }
    
    $scope.showModal = function(_idModal, _row){
        $scope.properties.selectedRow = _row;
        $("#" + _idModal).modal("show");
    }
    
    $scope.hideModal = function(_idModal, _row){
        $scope.comentariosAprobar = "";
        $scope.comentariosRechazar = "";
        $("#" + _idModal).modal("hide");
    }


    $scope.getInfoTarea = function (_aprobar) {
        let valido = true;

        if(_aprobar && !$scope.comentariosAprobar){
            valido = false;
        } else if(!_aprobar && !$scope.comentariosRechazar){
            valido = false;
        }

        if(valido){
            let rowData = angular.copy($scope.properties.selectedRow);

            var req = {
                method: "GET",
                url: "../API/bpm/humanTask?c=20&p=0&f=state=ready&d=rootContainerId&d=assigned_id&f=caseId=" + rowData.caseid 
            };
    
            return $http(req).success(function (data, status) {
                $scope.asignarTarea(data[0].id, _aprobar, data[0].name);
            }).error(function (data, status) {
                console.error(data);
            }).finally(function () {
    
            });
        } else {
            swal("¡Atención!","Debes agregar comentarios para realizar esta acción", "warning")
        }
        
    }

    $scope.asignarTarea = function (_taskid, _aprobar, _tarea) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + _taskid,
            data: angular.copy({ "assigned_id": "" })
        };

        return $http(req).success(function (data, status) {
            redireccionarTarea(_taskid, _aprobar, _tarea);
        }).error(function (data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        }).finally(function () {

        });
    }

    function redireccionarTarea(_taskid, _aprobar, _tarea) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + _taskid,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };

        return $http(req).success(function (data, status) {
            var contrato = {};

            if (_tarea === 'Pago de estudio socio-económico') {
                contrato = {
                    "solicitudApoyoEducativoInput":{
                        "ordenPagoConekta":"",
                        "comentariosPago": $scope.comentariosAprobar,
                        "motivoRechazoPago": $scope.comentariosRechazar
                    },
                    "isPagoValidadoInput": _aprobar,
                    "isPagoConTarjetainput": true,
                    "isPagoRegresarInput": false,
                    "isPagoRechazadoInput": !_aprobar
                };
            } else if (_tarea !== 'Pago de estudio socio-económico') {
                contrato = {
                    "isPagoValidadoInput": false,
                    "isPagoRechazadoInput": !_aprobar
                };
            } 

            executeTask(_taskid, contrato);
        }).error(function (data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        }).finally(function () {

        });
    }

    function executeTask(_taskid, _contract) {
        let url = "../API/bpm/userTask/" + _taskid + "/execution";

        return $http.post(url, _contract).success(function (data, status) {    
            setTimeout(() => {
                $scope.hideModal();
                swal("Ok", "Usuario avanzado con éxito", "succcess");
                doRequest("POST", $scope.properties.urlPost);
            }, 3000);
        }).error(function (data, status) {
            swal("¡Atención!","No se ha podido ejecutar la tarea. Intente de nuevo mas tarde.", "warning")
        }).finally(function () {
            blockUI.stop();
        });
    }
}