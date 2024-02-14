function PbTableCtrl($scope, $http, $window, blockUI) {

    this.isArray = Array.isArray;
    $scope.action = "";
  
    $scope.lstCampus = [];
    $scope.posgrados = [];
    $scope.programas = [];
    $scope.periodos = [];
    
    // Campus mostrados (resultado del match entre la lista del catalogo de campus y los campus disponibles para el usuarios)
    $scope.campusDisponibles = [];

    // Valor del input select
    $scope.selectedCampus = "";
    $scope.selectedPosgrado = "";
    $scope.selectedPrograma = "";
    $scope.selectedPeriodo = "";

    // Valor seleccionado
    $scope.properties.campusSeleccionado = null;
    $scope.properties.posgradoSeleccionado = null;
    $scope.properties.programaSeleccionado = null;
    $scope.properties.periodoSeleccionado = null;
  
    // Los periodos se cargan una vez y se muestran como opciones todos los periodos no eliminados
    getPeriodos();
    
    getCatCampus();

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
        $scope.properties.filtroExcel = $scope.properties.dataToSend;
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

    // Scope functions

    $scope.campusChanged = function() {

        if ($scope.selectedCampus === "" || $scope.selectedCampus === "Todos los campus") {
            filterListRemove("CAMPUS");

            $scope.mostrarFiltros = false;
            $scope.properties.campusSeleccionado = $scope.selectedCampus;
            $scope.posgrados = [];
        } 
        else {
            // Nuevo valor del filtro campus
            const campusFilter = {
                "columna": "CAMPUS",
                "operador": "Igual a",
                "valor": $scope.selectedCampus
            };

            // Agregar o actualizar el filtro campus
            filterListAdd("CAMPUS", campusFilter);
            
            $scope.mostrarFiltros = true;
            $scope.properties.campusSeleccionado = $scope.selectedCampus;
            const campus = $scope.lstCampus.find(item => item.descripcion === $scope.selectedCampus)
            getPosgrados(campus.persistenceId);
        } 
    }

    $scope.posgradoChanged = function() {
        if ($scope.selectedPosgrado === "") {
            filterListRemove("POSGRADO");

            $scope.properties.posgradoSeleccionado = $scope.selectedPosgrado;
            $scope.programas = [];
        } 
        else {
            // Nuevo valor del filtro posgrado
            const posgradoFilter = {
                "columna": "POSGRADO",
                "operador": "Igual a",
                "valor": $scope.selectedPosgrado
            };

            // Agregar o actualizar el filtro posgrado
            filterListAdd("POSGRADO", posgradoFilter);

            $scope.properties.posgradoSeleccionado = $scope.selectedPosgrado;
            const campus = $scope.lstCampus.find(item => item.descripcion === $scope.selectedCampus)
            const posgrado = $scope.posgrados.find(item => item.descripcion === $scope.selectedPosgrado)
            getProgramas(campus.persistenceId , posgrado.persistenceId);
        } 
    }

    $scope.programaChanged = function() {
        
        if ($scope.selectedPrograma === "") {
            filterListRemove("PROGRAMA");

            $scope.properties.programaSeleccionado = $scope.selectedPrograma;
            $scope.periodos = [];
        } 
        else {
            // Nuevo valor del filtro programa
            const programaFilter = {
                "columna": "PROGRAMA",
                "operador": "Igual a",
                "valor": $scope.selectedPrograma
            };

            // Agregar o actualizar el filtro campus
            filterListAdd("PROGRAMA", programaFilter);

            $scope.properties.programaSeleccionado = $scope.selectedPrograma;
        } 
    }

    $scope.periodoChanged = function() {
        
        if ($scope.selectedPeriodo === "") {
            filterListRemove("PERIODO");

            $scope.properties.periodoSeleccionado = $scope.selectedPeriodo;
        } 
        else {
            // Nuevo valor del filtro periodo
            const periodoFilter = {
                "columna": "PERIODO",
                "operador": "Igual a",
                "valor": $scope.selectedPeriodo
            };

            // Agregar o actualizar el filtro periodo
            filterListAdd("PERIODO", periodoFilter);

            $scope.properties.periodoSeleccionado = $scope.selectedPeriodo;
        } 
    }
    
    $scope.descargarSolicitud = function(row) {
        $scope.action = "descargarSolicitud";
          var req = {
            method: "POST",
            url: `../API/extension/DocAPI?pdf=pdfFileSolicitudPosgrado&p=0&c=1&caseid=${row.caseid}`
        };
  
        return $http(req).success(function(data, status) {
                console.log("Ejecutado correctamente.")
                console.log(data)
                const documentoB64 = data.data[0];
                downloadFile(documentoB64, row.caseid);
            })
            .error(function(data, status) {
                console.log("Fallo algo en la descarga.")
                console.log(data);
            })
            .finally(function() {});
    }
    
    function downloadFile(documentoB64, caseid) {
        var base64Data = documentoB64;

        // Crear un objeto Blob a partir de la cadena base64
        var binaryData = atob(base64Data);
        var arrayBuffer = new ArrayBuffer(binaryData.length);
        var uint8Array = new Uint8Array(arrayBuffer);

        for (var i = 0; i < binaryData.length; i++) {
        uint8Array[i] = binaryData.charCodeAt(i);
        }

        var blob = new Blob([uint8Array], { type: 'application/pdf' });

        // Crear un enlace (link) y simular un clic para descargar el archivo
        var a = document.createElement('a');
        a.href = URL.createObjectURL(blob);
        a.download = "Solicitud de admisión a posgrados " + caseid + ".pdf";
        a.click();
        /*const linkSource = documentoB64;
        const downloadLink = document.createElement("a");
        const fileName = "Solicitud de admisión a posgrados " + caseid + ".pdf";
    
        downloadLink.href = linkSource;
        downloadLink.download = fileName;
        downloadLink.click();*/
    }

    // Utils

    function getCatCampus() {
        var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.posgrados.catalog.PSGRCatCampus?q=getCat&f=eliminado=false&p=0&c=1000"
        };
  
        return $http(req)
            .success(function(data, status) {
                $scope.lstCampus = data;
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    function getPosgrados(campus_pid) {
        var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.posgrados.catalog.PSGRCatPosgrado?q=getCat&p=0&c=9999&f=is_eliminado=false&f=campus="  + campus_pid,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };
  
        return $http(req).success(function(data, status) {
            $scope.posgrados = data;
            // window.open(url, '_blank');
        })
        .error(function(data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {

        });
    }

    function getProgramas(campus_pid, posgrado_pid) {
        var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.posgrados.catalog.PSGRCatGestionEscolar?q=getCat&p=0&c=9999&f=is_eliminado=false&f=campus="+campus_pid+"&f=posgrado="+posgrado_pid,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };
  
        return $http(req).success(function(data, status) {
            $scope.programas = data;
            // window.open(url, '_blank');
        })
        .error(function(data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {

        });
    }

    function getPeriodos() {
        var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.posgrados.catalog.PSGRCatPeriodo?q=getCat&p=0&c=999&f=is_eliminado=false",
            //url: "/API/extension/AnahuacRestGet?url=getCatPeriodoActivo&p=0&c=10&tipo=Semestral",
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };
  
        return $http(req).success(function(data, status) {
            $scope.periodos  = data;
            // window.open(url, '_blank');
        })
        .error(function(data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {

        });
    }

    function filterListAdd(columna, filter) {
        // Obteniendo la lista de filtros
        const filterList = $scope.properties.dataToSend.lstFiltro;

        // Agregar o actualizar el filtro campus
        const foundFilter = filterList.find(filtro => filtro.columna === columna)
        if (foundFilter) {
            foundFilter.columna = filter.columna;
            foundFilter.operador = filter.operador;
            foundFilter.valor = filter.valor;
        } 
        else {
            filterList.push(filter);
        }
    }

    function filterListRemove(columna) {
        // Obteniendo la lista de filtros
        const filterList = $scope.properties.dataToSend.lstFiltro;

        const index = filterList.findIndex(filter => filter.columna === columna);
        if (index !== -1) filterList.splice(index, 1);
    }

    function lstFiltrosChanged() {
        if ($scope.properties.dataToSend.lstFiltro) {
            const filterList = $scope.properties.dataToSend.lstFiltro;
            
            // Eliminar filtros
            if ($scope.selectedCampus && $scope.selectedCampus !== "Todos los campus" && !filterList.find(item => item.columna === "CAMPUS")) {
                // Limpiar filtro campus
                $scope.selectedCampus = "";
                $scope.campusSeleccionado = null;

                filterListRemove("POSGRADOS");
                $scope.posgrados = [];
            }
            else if ($scope.selectedPosgrado && !filterList.find(item => item.columna === "POSGRADO")) {
                // Limpiar filtro posgrado
                $scope.selectedPosgrado = "";
                $scope.posgradoSeleccionado = null;
                
                filterListRemove("PROGRAMA");
                $scope.programas = [];
            } 
            else if ($scope.selectedPrograma && !filterList.find(item => item.columna === "PROGRAMA")) {
                // Limpiar filtro programa
                $scope.selectedPrograma = "";
                $scope.programaSeleccionado = null;
            }
            
            if ($scope.selectedPeriodo && !filterList.find(item => item.columna === "PERIODO")) {
                $scope.selectedPeriodo = "";
                $scope.periodoSeleccionado = null;
            }
        }
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
            $scope.selectedCampus = campusDisponibles[0].descripcion;
            $scope.campusChanged();
        }
    }

    // Watchers

    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        
        // Actualizar lista de filtros
        lstFiltrosChanged();

        if (newValue !== undefined) {
            if ($scope.properties.campusSeleccionado !== undefined) {
                doRequest("POST", $scope.properties.urlPost);
            }
        }
    });

    $scope.$watch("lstCampus", function(newValue, oldValue) {
        updateCampusDisponibles();
    });

    $scope.$watch("lstCampusByUser", function(newValue, oldValue) {
        updateCampusDisponibles();
    });

    /* --------------------------------------------------------- */

    // Se usa para ejecutar la tarea Solicitud dictamen
    function executeTarea(method, url, params) {
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };
  
        return $http(req).success(function(data, status) {
                
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                blockUI.stop();
            });
    }
  
    $scope.verSolicitud = function(rowData) {
        $scope.action = "verSolicitud";
          var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
        };
  
        return $http(req).success(function(data, status) {
                debugger;
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
    
    // ABRIR MODAL-REACTIVAR
    $scope.abrirModalReactivarSolicitud = function (rowData) {
        //$scope.isTareaPreAutorizacion = false;
        //$scope.avanzarSolicitud = false;
        //$scope.avanzarPreAutorizacion = true;
        //$scope.avanzarFinanciamiento = false;
        //$scope.avanzarArchivar = false;
        $scope.caseIdTarea = rowData.caseid;
        $('#modalReactivarSolicitud').modal('show');
    }

    // Con modal de confirmacion. ?
    $scope.avanzarTareaPreaAutorizacion = function () {
        var rowData = {
            caseid: $scope.caseIdTarea
        };

        var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${$scope.caseIdTarea}&f=isFailed%3dfalse`
        };

        return $http(req).success(function (data, status) {
            rowData.taskId = data[0].id;
            rowData.taskName = data[0].name;
            rowData.processId = data[0].processId;
            $scope.preProcesoAsignarTarea(rowData)
        }).error(function (data, status) {
            console.error(data);
        }).finally(function () {

        });
    }
    // Este se usa si no hay modal de confirmacion. ?
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
    // REACTIVAR SOLICITUD
    $scope.reactivarSolicitud = function () {
        $scope.action = "reactivarSolicitud";
        var rowData = {
            caseid: $scope.caseIdTarea
        };

        var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${$scope.caseIdTarea}&f=isFailed%3dfalse`
        };

        return $http(req).success(function (data, status) {
            rowData.taskId = data[0].id;
            rowData.taskName = data[0].name;
            rowData.processId = data[0].processId;
            $scope.preProcesoAsignarTarea(rowData)
        }).error(function (data, status) {
            console.error(data);
        }).finally(function () {

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
            
            switch($scope.action){
                case "verSolicitud":
                    var url = "/bonita/portal/resource/app/sdae/"+$scope.properties.abrirPagina+"/content/?app=sdae&id=" + rowData.taskId + "&caseId=" + rowData.caseid;
                    window.open(url, '_blank');
                    break;
                case "reactivarSolicitud":
                    executeTarea('POST', '../API/bpm/userTask/' + rowData.taskId + '/execution', {}).then(function () {
                        console.log("tarea realizada");
                        // Recargar pagina
                        location.reload(true);
                    });
                    break;
            }
                
            
                
                /*
                var url = "/bonita/portal/resource/taskInstance/[NOMBREPROCESO]/[VERSIONPROCESO]/[NOMBRETAREA]/content/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[NOMBREPROCESO]", rowData.processName);
                url = url.replace("[VERSIONPROCESO]", rowData.processVersion);
                url = url.replace("[NOMBRETAREA]", rowData.taskName);
                url = url.replace("[TASKID]", rowData.taskId);
                $window.location.assign(url);*/
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
  
            });
    }
    
    //-------------------------
  
    

    function abrirSolicitud(rowData) {
        
        var url = "/bonita/portal/resource/app/sdae/"+$scope.properties.abrirPagina+"/content/?app=sdae&caseId=" + rowData.caseid;
        window.open(url, '_blank');
    }
    
    $scope.abrirBitacora = function(rowData) {
        debugger;
        var url = "/portal/resource/app/administrativo/SDAEBitacora/content/?caseId=" + rowData.caseid;
        window.open(url, '_blank');
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

    /*$scope.addFilter = function() {

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
            } else {
                $scope.properties.campusSeleccionado = null;
            }
  
        }
  
    }*/

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

    /*
    function getPeriodos(_campus) {
        var req = {
            method: "GET",
            url: "/API/extension/AnahuacRestGet?url=getCatPeriodoActivo&p=0&c=10&tipo=Semestral",
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };
  
        return $http(req).success(function(data, status) {
            $scope.periodos  = data.data;
            // window.open(url, '_blank');
        })
        .error(function(data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {

        });
    }*/


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
  
    /*
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
    }*/
    
    $scope.isPeriodoVencido = function(periodofin) {
        var fecha = new Date(periodofin.slice(0, 10))
        return fecha < new Date();
    }
    
    $scope.abrirSolicitud = function(row) {
        let indexSectionParam = ""
        if ($scope.properties.isDirectToDictamen) {
            indexSectionParam = "&indexSection=last"
        }
        var url = "/bonita/portal/resource/app/posgrados/"+$scope.properties.abrirPagina+"/content/?app=posgrados&caseId=" + row.caseid + indexSectionParam;
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
}