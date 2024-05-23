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
        // getTaskInfo(row.caseid);
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
  
    // Método principal para obtener los registros de la tabla.
    function selectSolicitudes() {
        if (!$scope.properties.dataToSend || !$scope.properties.dataToSend.lstFiltro) return;

        const filters = $scope.properties.dataToSend.lstFiltro;
        
        // Validar filtros obligatorios
        if ($scope.filtroCampus !== "Todos los campus") {
            if (!filters.some(filter => filter.columna === "CAMPUS")){ limpiarRegistros(); return; }
            if (!filters.some(filter => filter.columna === "LICENCIATURA") && !filters.some(filter => filter.columna === "PERIODO")) { limpiarRegistros(); return; }
        }

        doRequest("POST", $scope.properties.urlPost);
    }

    function limpiarRegistros() {
        $scope.properties.lstContenido = [];
        $scope.sinCoincidencias = undefined;
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
            $scope.sinCoincidencias = data && data.data ? (data.data.length === 0 ? true : false) : undefined;
            
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

    // Quita de la lista de filtros el filtro especificado, si es que existe
    function filterListRemove(columna) {
        // Obteniendo la lista de filtros
        const filterList = $scope.properties.dataToSend.lstFiltro;

        const index = filterList.findIndex(filter => filter.columna === columna);
        if (index !== -1) filterList.splice(index, 1);
        
        // Al actualizar el dataToSend.lstFiltro se ejecuta el lstFiltrosChanged y actualiza las variables de cada filtro.
    }

    function lstFiltrosChanged() {
        if ($scope.properties.dataToSend.lstFiltro) {
            const filterList = $scope.properties.dataToSend.lstFiltro;
            // Eliminar filtros que fueron eliminados desde la lista de filtros
            if ($scope.filtroPeriodo && !filterList.find(item => item.columna === "PERIODO")) {
                $scope.filtroPeriodo = "";
            }
            if ($scope.filtroLicenciatura && !filterList.find(item => item.columna === "LICENCIATURA")) {
                $scope.filtroLicenciatura = "";
            }
        }
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
        selectSolicitudes();
    })
  
  
    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {

        // Actualizar lista de filtros
        lstFiltrosChanged();

        if (newValue !== undefined) {
            if ($scope.properties.campusSeleccionado !== undefined) {
                selectSolicitudes();
            }
        }
    });
  
    $scope.$watch("properties.campusSeleccionado", function(newValue, oldValue) {
        if (newValue !== undefined) {
            if ($scope.properties.campusSeleccionado !== undefined) {
                selectSolicitudes();
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
        selectSolicitudes();
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
  
        selectSolicitudes();
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
  
        selectSolicitudes();
    }
  
    $scope.getCampusByGrupo = function(campus) {
        var retorno = "";

        for (var i = 0; i < $scope.properties.lstCampus.length; i++) {
            if (campus == $scope.properties.lstCampus[i].grupoBonita) {
                retorno = $scope.properties.lstCampus[i].descripcion
                if ($scope.lstCampusByUser.length == 2) {
                    // Seleccionando valor si solo hay una opción
                    $scope.properties.campusSeleccionado = $scope.properties.lstCampus[i].grupoBonita

                    if (!$scope.filtroCampus) {
                        $scope.filtroCampus = retorno;
                        $scope.addFilter()
                    }    
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

    /*$scope.addFilter = function() {
        if ($scope.filtroCampus != "Todos los campus") {
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
  
    }*/

    $scope.addFilter = function() {
        if ($scope.filtroCampus != "Todos los campus") {
            $scope.licenciaturas = [];
            $scope.periodos = [];
            filterListRemove("PERIODO");
            filterListRemove("LICENCIATURA");
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
        const dataToSend = {
            "lstFiltro": [],
            "usuario": "",
            "orderby": "",
            "orientation": "ASC",
            "limit": 999,
            "offset": 0
        }

        var req = {
            method: "POST",
            url: "/API/extension/AnahuacRest?url=getCatPeriodo&p=0&c=999",
            data: angular.copy(dataToSend)
        };
  
        try {
            return $http(req).success(function(data, status) {
                // Filtro para no distinguir entre periodos de diferente tipo (semestral, anual, trimestral)
                const periodos = [];
                data.data.forEach((periodo) => {
                    if (!periodos.some(periodoGuardado => periodoGuardado.descripcion === periodo.descripcion))
                        periodos.push(periodo);
                })
    
                // Ordenar por clave
                periodos.sort((a, b) => b.clave?.localeCompare(a.clave));
    
                $scope.periodos = periodos;
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
    
            });
        }
        catch(e) {
            console.error("Algo falló al recuperar el catálogo de periodos.");
        }
        
    }


    $scope.addFilterLicenciatura = function() {
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
    
    $scope.addFilterBecaFina = function() {
        if ($scope.filtroBeca) {
            var filter = {
                "columna": "T-BECA",
                "operador": "Que contengan",
                "valor": $scope.filtroBeca
            };

            if ($scope.properties.dataToSend.lstFiltro.length > 0) {
                var encontrado = false;
                for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
                    const element = $scope.properties.dataToSend.lstFiltro[index];
                    if (element.columna == "T-BECA") {
                        $scope.properties.dataToSend.lstFiltro[index].columna = filter.columna;
                        $scope.properties.dataToSend.lstFiltro[index].operador = filter.operador;
                        $scope.properties.dataToSend.lstFiltro[index].valor = $scope.filtroBeca;
                        encontrado = true
                    }
                }
  
                if (!encontrado) {
                    $scope.properties.dataToSend.lstFiltro.push(filter);
                }
            } else {
                $scope.properties.dataToSend.lstFiltro.push(filter);
            }
        } else {
            var encontrado = false;
            for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
                const element = $scope.properties.dataToSend.lstFiltro[index];
                if (element.columna == "T-BECA") {
                    $scope.properties.dataToSend.lstFiltro.splice(index, 1);
                }
            }
        }

        if ($scope.filtroFinanciamiento) {
            var filter = {
                "columna": "T-FINAN",
                "operador": "Que contengan",
                "valor": $scope.filtroFinanciamiento
            };

            if ($scope.properties.dataToSend.lstFiltro.length > 0) {
                var encontrado = false;
                for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
                    const element = $scope.properties.dataToSend.lstFiltro[index];
                    if (element.columna == "T-FINAN") {
                        $scope.properties.dataToSend.lstFiltro[index].columna = filter.columna;
                        $scope.properties.dataToSend.lstFiltro[index].operador = filter.operador;
                        $scope.properties.dataToSend.lstFiltro[index].valor = $scope.filtroFinanciamiento;
                        encontrado = true
                    }
                }
  
                if (!encontrado) {
                    $scope.properties.dataToSend.lstFiltro.push(filter);
                }
            } else {
                $scope.properties.dataToSend.lstFiltro.push(filter);
            }
        } else {
            for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
                const element = $scope.properties.dataToSend.lstFiltro[index];
                if (element.columna == "T-FINAN") {
                    $scope.properties.dataToSend.lstFiltro.splice(index, 1);
                }
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
  
        selectSolicitudes();
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
        blockUI.start();
        const linkSource = "data:application/pdf; base64,"+ _document;
        const downloadLink = document.createElement("a");
    
        let fileName = "solicitud_de_apoyo_educativo.pdf";

        downloadLink.href = linkSource;
        downloadLink.download = fileName;
        downloadLink.click();
        blockUI.stop();
    }
    
    
    $scope.downloadFile = function(_email, _caseId){
        blockUI.start();
        let url = "../API/extension/DocAPI?pdf=pdfSolicitudApoyo&p=0&c=1&email=" + _email + "&caseid=" + _caseId;
        $http.post(url, {}).success(function(success){
            downloadFile(success.data[0]); 
        }).error(function(err){
           swal("Error", "No se ha podido generar el archivo, intentelo de nuevo mas tarde.", "error");
        }).finally(function(){
            blockUI.stop();
        });
    }

    function downloadFileF(_document) {
        blockUI.start();
        const linkSource = "data:application/pdf; base64,"+ _document;
        const downloadLink = document.createElement("a");
    
        let fileName = "Información del aval.pdf";

        downloadLink.href = linkSource;
        downloadLink.download = fileName;
        downloadLink.click();
        blockUI.stop();
    }

    $scope.downloadFileF = function(_email, _caseId){
        blockUI.start();
        let url = "../API/extension/DocAPI?pdf=pdfDatosAval&p=0&c=1&email=" + _email;
        $http.post(url, {}).success(function(success){
            downloadFileF(success.data[0]); 
        }).error(function(err){
            
        }).finally(function(){
            blockUI.stop();
        });
    }

    $scope.abrirSolicitud = function(_rowData){
        var url = "/bonita/portal/resource/app/sdae/verSolicitud/content/?app=sdae&caseId="+ _rowData.caseid;
        window.open(url, '_blank');
    }
    
    $scope.abrirSolicitudF = function(_rowData){
        var url = "/bonita/portal/resource/app/sdae/verFinanciamiento/content/?app=sdae&caseId="+ _rowData.caseid;
        window.open(url, '_blank');
    }
}