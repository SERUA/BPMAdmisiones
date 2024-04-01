function PbTableCtrl($scope, $http, $window, blockUI, modalService) {
    
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

    $scope.openModal = function openModal() {
        modalService.open($scope.properties.modalId);
    }

    $scope.closeModal = function closeModal(shouldClose) {
        modalService.close();
    }
    
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
                if($scope.properties.lstContenido.length < 1){
                    swal({
                        text: "No se encuentran coincidencias con el criterio de búsqueda o el aspirante ya tiene resultado de admisión.",
                        icon: "info",
                        button: "Aceptar"
                    })
                    swal("No se encuentran coincidencias con el criterio de búsqueda o el aspirante ya tiene resultado de admisión.", "", "info"); 
                 }
            })
            .error(function(data, status) {
                // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                blockUI.stop();
            });
    }
    
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
  
    $scope.verSolicitud = function(rowData) {
          var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
        };
  
        return $http(req).success(function(data, status) {
                rowData.taskId = data[0].id;
                rowData.taskName = data[0].name;
                rowData.processId = data[0].processId;
                $scope.preProcesoAsignarTarea(rowData)
                
                //let taskId = data[0].id;
                //var url = "/bonita/portal/resource/app/sdae/preAutorizacion/content/?app=sdae&id=" + taskId + "&caseId=" + rowData.caseid;
                //window.open(url, '_blank');
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {});
  
      /*
      }else{
        var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
        };
  
        return $http(req).success(function(data, status) {
                let taskId = data[0].id;
                var url = "/bonita/portal/resource/app/aspirante/verSolicitudAdmision/content/?app=aspirante&id=" + rowData.caseid + "&displayConfirmation=false";
                //window.location.href = url;
                window.open(url, '_blank');
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {});
          }*/
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
                // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
  
            });
    }
    
    function abrirSolicitud(rowData) {
        
        var url = "/bonita/portal/resource/app/sdae/"+$scope.properties.abrirPagina+"/content/?app=sdae&caseId=" + rowData.caseid;
        window.open(url, '_blank');
    }
    
    $scope.abrirBitacora = function(rowData) {
        var url = "/portal/resource/app/administrativo/SDAEBitacora/content/?caseId=" + rowData.caseid;
        window.open(url, '_blank');
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
                
                /*
                var url = "/bonita/portal/resource/taskInstance/[NOMBREPROCESO]/[VERSIONPROCESO]/[NOMBRETAREA]/content/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[NOMBREPROCESO]", rowData.processName);
                url = url.replace("[VERSIONPROCESO]", rowData.processVersion);
                url = url.replace("[NOMBRETAREA]", rowData.taskName);
                url = url.replace("[TASKID]", rowData.taskId);
                $window.location.assign(url);*/
            })
            .error(function(data, status) {
                // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
  
            });
    }
  
    $(function() {
        doRequest("POST", $scope.properties.urlPost);
    })
  
    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        if (newValue !== undefined) {
            if ($scope.properties.campusSeleccionado !== undefined) {
                //doRequest("POST", $scope.properties.urlPost);
            }
        }
        console.log($scope.properties.dataToSend);
    });
  
    $scope.$watch("properties.campusSeleccionado", function(newValue, oldValue) {
        if (newValue !== undefined) {
            if ($scope.properties.campusSeleccionado !== undefined) {
                //doRequest("POST", $scope.properties.urlPost);
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
            if (campus == $scope.properties.lstCampus[i].grupo_bonita) {
                retorno = $scope.properties.lstCampus[i].descripcion
                if ($scope.lstCampusByUser.length == 2) {
                    $scope.properties.campusSeleccionado = $scope.properties.lstCampus[i].grupo_bonita
                }
            }
            //  else if (campus == "Todos los campus") {
            //     retorno = campus
            // }
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
                                $scope.properties.campusSeleccionado = $scope.lstCampus[index2].descripcion;
                            }
                        }
                        encontrado = true
                    }
                }
  
                if (!encontrado) {
                    $scope.properties.dataToSend.lstFiltro.push(filter);
                    for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                        if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                            $scope.properties.campusSeleccionado = $scope.lstCampus[index2].descripcion;
                        }
                    }
                }
            } else {
                $scope.properties.dataToSend.lstFiltro.push(filter);
                for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                    if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                        $scope.properties.campusSeleccionado = $scope.lstCampus[index2].descripcion;
                    }
                }
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
    
    $scope.isPeriodoVencido = function(periodofin) {
        var fecha = new Date(periodofin.slice(0, 10))
        return fecha < new Date();
    }
    
    $scope.abrirSolicitud = function(row) {
        var url = "/bonita/portal/resource/app/posgrados/"+$scope.properties.abrirPagina+"/content/?app=sdae&caseId=" + row.caseid;
        window.open(url, '_blank');
    }

    $scope.getTareaByEstatus = function(_estatus){
        let output = "";
        if(_estatus === "Esperando Pre-Autorización"){
            output = "Pre-Autorización";
        } else if(_estatus === "Correcciones realizadas"){
            output = "Pre-Autorización";
        } else if(_estatus === "Evaluación artística rechaza"){
            output = "Archivo";
        } else if(_estatus === "Evaluación deportiva rechaza"){
            output = "Archivo";
        } else if(_estatus === "Solicitud Rechazada"){
            output = "Archivo";
        } else if(_estatus === "En espera de resultado"){
            output = "Pre-Autorización";
        } else if(_estatus === "Esperando revisión área deportiva"){
            output = "Pre-Autorización";
        } else if(_estatus === "Esperando revisión área artistica"){
            output = "Pre-Autorización";
        } else if(_estatus === "En espera de autorización"){
            output = "Comité de becas";
        } else if(_estatus === "Solicitud de apoyo en progreso"){
            output = "Aspirante";
        } else if(_estatus === "Solicitud de Financiamiento en Proceso"){
            output = "Aspirante";
        } else if(_estatus === "Pre-autorización solicita modificaciones"){
            output = "Aspirante";
        } else if(_estatus === "Solicitud autorizada"){
            output = "Aspirante";
        } 

        return output;
    };

    $scope.abrirSolicitudF = function(_rowData){
        var url = "/bonita/portal/resource/app/sdae/verFinanciamiento/content/?app=sdae&caseId="+ _rowData.caseid;
        window.open(url, '_blank');
    }

    $scope.peridoSelected = "";
    $scope.periodoLista = [];
    $scope.carreraSelected = "";
    $scope.carreraLista =[];
    
    $scope.filterSelectPosgrado = function () {
        var aplicado = true;
        for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
            const element = $scope.properties.dataToSend.lstFiltro[index];
            if (element.columna == "POSGRADO") {
                if ($scope.peridoSelected == "") {
                    $scope.properties.dataToSend.lstFiltro.splice(index, index + 1);
                } else {
                    $scope.properties.dataToSend.lstFiltro[index].valor = $scope.posgradoSelected;
                    $scope.properties.dataToSend.lstFiltro[index].operador = "Que contengan";
                }
                aplicado = false;
            }

        }
        //if (aplicado) {
        var obj = { "columna": "POSGRADO", "operador": "Que contengan", "valor": $scope.posgradoSelected }
        $scope.properties.dataToSend.lstFiltro.push(obj);
        //}
        if (($scope.filtroCampus != "Todos los campus" && $scope.properties.dataToSend.lstFiltro.length > 0) || ($scope.filtroCampus == "Todos los campus" && $scope.properties.dataToSend.lstFiltro.length > 0)) {
            doRequest("POST", $scope.properties.urlPost);
            for (let posgrado of $scope.posgradosLista) {
                if (posgrado.descripcion === $scope.posgradoSelected) {
                    $scope.persistenceIdPosgrado = posgrado.persistenceId;
                    break;
                }
            }

            doRequestCarrera();
        }
    }
    
    $scope.filterSelectCarrera = function() {
		//$scope.limpiarFiltrosTabla();
        var aplicado = true;
        for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
            const element = $scope.properties.dataToSend.lstFiltro[index];
            if (element.columna == "CARRERA") {
                if($scope.peridoSelected == ""){
                    $scope.properties.dataToSend.lstFiltro.splice(index,index+1);
                }else{
                    $scope.properties.dataToSend.lstFiltro[index].valor = $scope.carreraSelected;
                    $scope.properties.dataToSend.lstFiltro[index].operador = "Que contengan";
                }
                aplicado = false;
            }

        }
        //if (aplicado) {
            var obj = { "columna": "CARRERA", "operador": "Que contengan", "valor": $scope.carreraSelected }
            $scope.properties.dataToSend.lstFiltro.push(obj);
        //}
        if(($scope.filtroCampus != "Todos los campus" && $scope.properties.dataToSend.lstFiltro.length > 0)||($scope.filtroCampus == "Todos los campus" && $scope.properties.dataToSend.lstFiltro.length > 0)){
            doRequest("POST", $scope.properties.urlPost);
        }
    }

    $scope.filterSelectPeriodo = function() {
		//$scope.limpiarFiltrosTabla();
        var aplicado = true;
        for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
            const element = $scope.properties.dataToSend.lstFiltro[index];
            if (element.columna == "PERIODO") {
                if($scope.peridoSelected == ""){
                    $scope.properties.dataToSend.lstFiltro.splice(index,index+1);
                }else{
                    $scope.properties.dataToSend.lstFiltro[index].valor = $scope.peridoSelected;
                    $scope.properties.dataToSend.lstFiltro[index].operador = "Que contengan";
                }
                aplicado = false;
            }

        }
        if (aplicado) {
            var obj = { "columna": "PERIODO", "operador": "Que contengan", "valor": $scope.peridoSelected }
            $scope.properties.dataToSend.lstFiltro.push(obj);
        }
        if(($scope.filtroCampus != "Todos los campus" && $scope.properties.dataToSend.lstFiltro.length > 1)||($scope.filtroCampus == "Todos los campus" && $scope.properties.dataToSend.lstFiltro.length > 0)){
            doRequest("POST", $scope.properties.urlPost);
        }
    }

    function doRequestPeriodo() {
        blockUI.start();
        var req = {
            method: "GET",
            url: "/API/bdm/businessData/com.anahuac.posgrados.catalog.PSGRCatPeriodo?q=getCat&p=0&c=9999&f=is_eliminado=false",
        };

        return $http(req)
            .success(function(data, status) {
                console.log(data)
                $scope.periodoLista = data;
            })
            .error(function(data, status) {
                // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {

                blockUI.stop();
            });
    }

    function doRequestCarrera() {
        blockUI.start();
        var req = {
            method: "GET",
            url: "/API/bdm/businessData/com.anahuac.posgrados.catalog.PSGRCatGestionEscolar?q=getCat&p=0&c=9999&f=is_eliminado=false&f=campus="+ $scope.persistenceid,
        };

        return $http(req)
            .success(function(data, status) {
                console.log(data.data)
                $scope.carreraLista = data;
            })
            .error(function(data, status) {
                // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {

                blockUI.stop();
            });
    }
    
    function doRequestPosgrado() {
        blockUI.start();
        var req = {
            method: "GET",
            url: "/API/bdm/businessData/com.anahuac.posgrados.catalog.PSGRCatPosgrado?q=getCat&p=0&c=9999&f=is_eliminado=false&f=campus=" + $scope.persistenceid,
        };

        return $http(req)
            .success(function (data, status) {
                $scope.posgradosLista = data;
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {

                blockUI.stop();
            });
    }

    $scope.$watch("properties.campusSeleccionado", function(newValue, oldValue) {
        if (newValue !== undefined) {
            // doRequestCarrera();
            doRequestPosgrado();
            doRequestPeriodo();
        }
    });

    $scope.$watch("filtroCampus", function(newValue, oldValue) {
        if (newValue !== undefined) {
        	if(newValue == "Todos los campus"){
        		doRequestCarrera();
        	}
            
        }
    });

    $scope.filterKeyPressSuperior = function(columna, press) {
        //$scope.limpiarFiltrosTabla();
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

    $scope.limpiarFiltros = function(){
        $scope.carreraSelected = null;
        $scope.peridoSelected = null;
        $scope.posgradoSelected = null;
        $scope.limpiarFiltrosTabla();
        $scope.properties.lstContenido = [];
        $scope.primerCheck = true;
        $scope.lstPaginado = [];
        $scope.valorSeleccionado = 1;
        $scope.iniciarP = 1;
        $scope.finalP = 10;
        $scope.value = 0;
		let index = null;
		$('#banner2').val('');
		$('#nombrealumno2').val('');
		$('#correoelectronico2').val('');
		$('#carrera2').val('');
		$('#periodo2').val('');
		$('#fechaentrevista2').val('');
        $('#id_sesion').val('');
        $('#sesion').val('');
        index = $scope.properties.dataToSend.lstFiltro.findIndex((json) => json.columna === "CAMPUS");
        if(index != null){
        	if(index==0){
        		$scope.properties.dataToSend.lstFiltro.splice(index+1,$scope.properties.dataToSend.lstFiltro.length);
        	}else{
        		$scope.properties.dataToSend.lstFiltro.splice(index+1,$scope.properties.dataToSend.lstFiltro.length);
        		$scope.properties.dataToSend.lstFiltro.splice(0,index);
        	}
        
        }else{
        	$scope.properties.dataToSend.lstFiltro.splice(0,$scope.properties.dataToSend.lstFiltro.length);
        }
    }

    $scope.limpiarFiltrosTabla = function(){
        $('#tablaFiltro1').val('');
		$('#tablaFiltro2').val('');
		$('#tablaFiltro3').val('');
		$('#tablaFiltro4').val('');
		$('#tablaFiltro5').val('');
		$('#tablaFiltro6').val('');
		$('#tablaFiltro7').val('');
		$('#tablaFiltro8').val('');
        if($scope.properties.lstContenido.length >= 1){
            try{
                $scope.dynamicInput['nombre'] = '';
                $scope.dynamicInput['banner'] = '';
                $scope.dynamicInput['programa'] = '';
                $scope.dynamicInput['preparatoria'] = '';
                $scope.dynamicInput['indicadores'] ='';
                $scope.dynamicInput['sesion'] = '';
                $scope.dynamicInput['estatus'] = '';
                $scope.dynamicInput['ultimamodificacion'] ='';
            }catch(error){

            }
            
        }
        
        $scope.properties.lstContenido = [];
        $scope.primerCheck = true;
        $scope.lstPaginado = [];
        $scope.valorSeleccionado = 1;
        $scope.iniciarP = 1;
        $scope.finalP = 10;
        $scope.value = 0;
		let index = null;
        const filtrosTabla = ['NOMBRE,EMAIL,CURP', 'CAMPUS,PROGRAMA,INGRESO', 'PROCEDENCIA,PREPARATORIA,PROMEDIO', 'INDICADORES', 'SESIÓN,ID SESIÓN,FECHA ENTREVISTA', 'ESTATUS', 'ULTIMA MODIFICACION']
        filtrosTabla.forEach((element) =>{
            index = $scope.properties.dataToSend.lstFiltro.findIndex((json) => json.columna === element);
            if(index != null){
                $scope.properties.dataToSend.lstFiltro.splice(index,index+1);
            }
        });
        
    }
}