function PbTableCtrl($scope, $http, $window, blockUI, modalService) {

    this.isArray = Array.isArray;
    $scope.task = {};
    $scope.mostrarFiltros = false;
    $scope.filtroBeca = false;
    $scope.filtroFinanciamiento = false;
    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };
  
    this.selectRow = function(row) {
        $scope.properties.selectedRow = row;
        getTaskInfo(row.caseid);
    };
    
    function getTaskInfo(_caseId){
        let url = "../API/bpm/humanTask?c=100&p=0&f=parentCaseId=" + _caseId;
        $http.get(url).success(function(data){
            $scope.task = data[0];
            $scope.properties.taskId = data[0].id;
        }).error(function(err){
            console.log(err);
        });
    }
  
    this.isSelected = function(row) {
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
  
        return $http(req).success(function(data, status) {
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
  
    $scope.verSolicitud = function(_rowData, _action) {
        $scope.properties.selectedRow = _rowData;
        
        var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${_rowData.caseid}&f=isFailed%3dfalse`
        };
        
        return $http(req).success(function(data, status) {
            _rowData.taskId = data[0].id;
            _rowData.taskName = data[0].name;
            _rowData.processId = data[0].processId;
            
            if(_action === "rechazar" || _action === "aceptar" || _action === "modificar"){
                $scope.properties.navVar = _action;
                getTaskInfo(_rowData,caseId);
                // showModal($scope.properties.idModalRechazar);
            } else {
                let taskId = data[0].id;
                var url = "/bonita/portal/resource/app/sdae/preAutorizacion/content/?app=sdae&id=" + taskId + "&caseId=" + _rowData.caseid;
                window.open(url, '_blank'); 
            }
        })
        .error(function(data, status) {
            console.error(data);
        })
        .finally(function() {
            
        });
    }

    function showModal(_idModal){
        modalService.open(_idModal);
    }

  
    $scope.preAsignarTarea = function(rowData) {
        var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
        };
  
        return $http(req).success(function(data, status) {
            rowData.taskId = data[0].id;
            rowData.taskName = data[0].name;
            rowData.processId = data[0].processId;
            //rowData.taskName=
            $scope.preProcesoAsignarTarea(rowData);
        })
        .error(function(data, status) {
            console.error(data);
        })
        .finally(function() {

        });
    }
    $scope.preProcesoAsignarTarea = function(rowData) {
  
        var req = {
            method: "GET",
            url: `/API/bpm/process/${rowData.processId}?d=deployedBy&n=openCases&n=failedCases`
        };
  
        return $http(req).success(function(data, status) {
            rowData.processName = data.name;
            rowData.processVersion = data.version;
            $scope.asignarTarea(rowData);
        })
        .error(function(data, status) {
            console.error(data);
        })
        .finally(function() {

        });
    }
  
    $scope.asignarTarea = function(rowData) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + rowData.taskId,
            data: angular.copy({ "assigned_id": "" })
        };
  
        return $http(req).success(function(data, status) {
            redireccionarTarea(rowData);
        })
        .error(function(data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {

        });
    }
  
    function redireccionarTarea(rowData) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + rowData.taskId,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };
  
        return $http(req).success(function(data, status) {
            var url = "/bonita/portal/resource/app/sdae/"+$scope.properties.abrirPagina+"/content/?app=sdae&id=" + rowData.taskId + "&caseId=" + rowData.caseid;
            window.open(url, '_blank');
        })
        .error(function(data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {

        });
    }
  
    $scope.isenvelope = false;
    $scope.selectedrow = {};
    $scope.mensaje = "";
  
    $scope.envelope = function(row) {
        $scope.isenvelope = true;
        $scope.mensaje = "";
        $scope.selectedrow = row;
    }
  
    $scope.envelopeCancel = function() {
        $scope.isenvelope = false;
        $scope.selectedrow = {};
    }
  
    $scope.sendMail = function(row, mensaje) {
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
  
        return $http(req).success(function(data, status) {
  
                $scope.envelopeCancel();
            })
            .error(function(data, status) {
                console.error(data)
            })
            .finally(function() {});
    }

    $scope.lstCampus = [];
  
    $(function() {
        doRequest("POST", $scope.properties.urlPost);
    })
  
  
    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        if (newValue !== undefined) {
            if ($scope.properties.campusSeleccionado !== undefined) {
                doRequest("POST", $scope.properties.urlPost);
            }
        }
        console.log($scope.properties.dataToSend);
    });
  
    $scope.$watch("properties.campusSeleccionado", function(newValue, oldValue) {
        if (newValue !== undefined) {
            if ($scope.properties.campusSeleccionado !== undefined) {
                doRequest("POST", $scope.properties.urlPost);
            }
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
  
            return $http(req).success(function(data, status) {
                $scope.lstMembership = data;
                $scope.campusByUser();
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {

            });
        }
    });
  
    $scope.lstCampusByUser = [];

    $scope.campusByUser = function() {
        var resultado = [];
        resultado.push("Todos los campus");
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

            var filter = {
                "columna": "CAMPUS",
                "operador": "Igual a",
                "valor": $scope.filtroCampus
            };

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
                getLicenciasturas($scope.properties.campusSeleccionado);
                getPeriodos($scope.properties.campusSeleccionado);
            } else {
                $scope.properties.dataToSend.lstFiltro.push(filter);
                for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                    if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                        $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;
                    }
                }
                getLicenciasturas($scope.properties.campusSeleccionado);
                getPeriodos($scope.properties.campusSeleccionado);
            }

        } else {
            $scope.mostrarFiltros = false;
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

    function getLicenciasturas(_campus) {
        var req = {
            method: "GET",
            url: "/API/bdm/businessData/com.anahuac.catalogos.CatGestionEscolar?q=getCatGestionEscolarByCampus&p=0&c=9999&f=campus="  + _campus,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };
  
        return $http(req).success(function(data, status) {
            debugger;
            $scope.licenciaturas = data;
            $scope.periodos
            // window.open(url, '_blank');
        })
        .error(function(data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {

        });
    }

    function getPeriodos(_campus) {
        var req = {
            method: "GET",
            url: "/API/extension/AnahuacRestGet?url=getCatPeriodoActivo&p=0&c=10&tipo=Semestral",
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };
  
        return $http(req).success(function(data, status) {
            debugger;
            $scope.periodos  = data.data;
            // window.open(url, '_blank');
        })
        .error(function(data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {

        });
    }


    $scope.addFilterLicenciatura = function() {
        debugger;
        if ($scope.filtroLicenciatura) {
            var filter = {
                "columna": "LICENCIATURA",
                "operador": "Que contengan",
                "valor": $scope.filtroLicenciatura
            };

            if ($scope.properties.dataToSend.lstFiltro.length > 0) {
                var encontrado = false;
                for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
                    const element = $scope.properties.dataToSend.lstFiltro[index];
                    if (element.columna == "LICENCIATURA") {
                        $scope.properties.dataToSend.lstFiltro[index].columna = filter.columna;
                        $scope.properties.dataToSend.lstFiltro[index].operador = filter.operador;
                        $scope.properties.dataToSend.lstFiltro[index].valor = $scope.filtroLicenciatura;
                        encontrado = true
                    }
                }
  
                if (!encontrado) {
                    $scope.properties.dataToSend.lstFiltro.push(filter);
                }
            } else {
                $scope.properties.dataToSend.lstFiltro.push(filter);
            }

        }
    }

    $scope.addFilterPeriodo = function() {
        debugger;
        if ($scope.filtroPeriodo) {
            var filter = {
                "columna": "PERIODO",
                "operador": "Que contengan",
                "valor": $scope.filtroPeriodo
            };

            if ($scope.properties.dataToSend.lstFiltro.length > 0) {
                var encontrado = false;
                for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
                    const element = $scope.properties.dataToSend.lstFiltro[index];
                    if (element.columna == "PERIODO") {
                        $scope.properties.dataToSend.lstFiltro[index].columna = filter.columna;
                        $scope.properties.dataToSend.lstFiltro[index].operador = filter.operador;
                        $scope.properties.dataToSend.lstFiltro[index].valor = $scope.filtroPeriodo;
                        encontrado = true
                    }
                }
  
                if (!encontrado) {
                    $scope.properties.dataToSend.lstFiltro.push(filter);
                }
            } else {
                $scope.properties.dataToSend.lstFiltro.push(filter);
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
  
        return $http(req).success(function(data, status) {
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

    $scope.isPeriodoVencido = function(periodofin) {
        var fecha = new Date(periodofin.slice(0, 10))
        return fecha < new Date();
    }
  
    $scope.getCatCampus();
    
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
}