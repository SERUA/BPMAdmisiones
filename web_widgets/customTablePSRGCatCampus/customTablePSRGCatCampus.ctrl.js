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
    this.selectRowEditar = function(row) {
        debugger;
        $scope.properties.selectedRow = row;
        $scope.properties.isSelected = 'editar';
    };
    this.selectRowDelete = function(row) {
        swal("¡Aviso!", "¿Está seguro que desea eliminar?", "warning", {
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
                        $scope.properties.selectedRow["isEliminado"] = true;
                        $scope.properties.selectedRow["pais"].persistenceId_string = $scope.properties.selectedRow["pais"].persistenceId;
                        $scope.properties.selectedRow["estado"] = $scope.properties.selectedRow["estados"]
                        $scope.properties.selectedRow["estado"].persistenceId_string = $scope.properties.selectedRow["estado"].persistenceId;
                        $scope.$apply();
                        this.handleTrashClick(row);
                        break;
                    default:

                }
            });
        /*
        
        $scope.properties.isSelected = 'editar';*/
    };

    this.handleTrashClick = function (row) {
        debugger;
        var persistenceid = row.persistenceId; // Obtener el persistenceid del row
        $scope.deleteCatalogo({ persistenceid: persistenceid }) // Enviar persistenceid como objeto JSON
            .then(function () {
                // Actualizar la matriz properties.content después de eliminar el registro
                var index = $scope.properties.lstContenido.indexOf(row);
                if (index !== -1) {
                    $scope.properties.lstContenido.splice(index, 1);
                }
            });
    };
    
    $scope.deleteCatalogo = function (dataToDelete) {
        debugger;
        // Realiza la solicitud HTTP para eliminar el registro utilizando la matriz JSON
        $scope.busy = true;
    
        return $http({
            method: 'POST',
            url: $scope.properties.urlDelete,
            data: dataToDelete,
            headers: { 'Content-Type': 'application/json;charset=utf-8' }
        }).then(function (response) {
            // Procesa la respuesta de eliminación si es necesario
            swal("OK", "Registro eliminado correctamente", "success");
            // Actualiza la vista o realiza otras acciones necesarias después de la eliminación
        }).catch(function (error) {
            swal("¡Algo ha fallado!", error.data.error, "error");
        }).finally(function () {
            $scope.busy = false;
        });
    };

    $scope.deleteContent = function(objContent) {
        debugger;
        console.log(objContent);
        var index = $scope.dataToSend.lstFiltro.indexOf(objContent);
        console.log(index);
        if(index != -1){
            $scope.dataToSend.lstFiltro.splice(index, 1);
        }
    }

    function startProcess() {
        if ($scope.properties.processId) {
            var prom = doRequests('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
    }
    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    function doRequests(method, url, params) {
        debugger;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSendFiltro),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.lstContenido = data.data;
                swal("¡Eliminado correctamente!", "", "success");
                $scope.value = data.totalRegistros;
                $scope.loadPaginado();
                doRequest("POST", $scope.properties.urlPost);
                console.log(data.data)
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
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
    ///API/bpm/process/4774666324165829920?d=deployedBy&n=openCases&n=failedCases
    $scope.preAsignarTarea = function(rowData) {

        var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
        };

        return $http(req)
            .success(function(data, status) {
                rowData.taskId = data[0].id;
                rowData.taskName = data[0].name;
                rowData.processId = data[0].processId;
                //rowData.taskName=
                $scope.preProcesoAsignarTarea(rowData);
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {});
    }
    $scope.preProcesoAsignarTarea = function(rowData) {

        var req = {
            method: "GET",
            url: `/API/bpm/process/${rowData.processId}?d=deployedBy&n=openCases&n=failedCases`
        };

        return $http(req)
            .success(function(data, status) {
                rowData.processName = data.name;
                rowData.processVersion = data.version;
                $scope.asignarTarea(rowData);
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {});
    }
    $scope.asignarTarea = function(rowData) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + rowData.taskId,
            data: angular.copy({ "assigned_id": "" })
        };

        return $http(req)
            .success(function(data, status) {
                redireccionarTarea(rowData);
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    function redireccionarTarea(rowData) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + rowData.taskId,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };

        return $http(req)
            .success(function(data, status) {
                var url = "/bonita/portal/resource/taskInstance/[NOMBREPROCESO]/[VERSIONPROCESO]/[NOMBRETAREA]/content/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[NOMBREPROCESO]", rowData.processName);
                url = url.replace("[VERSIONPROCESO]", rowData.processVersion);
                url = url.replace("[NOMBRETAREA]", rowData.taskName);
                url = url.replace("[TASKID]", rowData.taskId);
                $window.location.assign(url);
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
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

        return $http(req)
            .success(function(data, status) {

                $scope.envelopeCancel();
            })
            .error(function(data, status) {
                console.error(data)
            })
            .finally(function() {});
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
    $(function() {
        debugger;
        doRequest("POST", $scope.properties.urlPost);
    })


    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        debugger;
        if (newValue !== undefined) {
            doRequest("POST", $scope.properties.urlPost);
        }
        console.log($scope.properties.dataToSend);
    });
    $scope.setOrderBy = function(order) {
        debugger;
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
        debugger;
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
        for (var i = 0; i < $scope.lstCampus.length; i++) {
            if (campus == $scope.lstCampus[i].valor) {
                retorno = $scope.lstCampus[i].descripcion
            }

        }
        return retorno;
    }
    $scope.lstMembership = [];
    $scope.$watch("properties.userId", function(newValue, oldValue) {
        if (newValue !== undefined) {
            var req = {
                method: "GET",
                url: `/API/identity/membership?p=0&c=10&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
            };

            return $http(req)
                .success(function(data, status) {
                    $scope.lstMembership = data;
                })
                .error(function(data, status) {
                    console.error(data);
                })
                .finally(function() {});
        }
    });
    $scope.filtroCampus = ""
    $scope.addFilter = function() {
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
    
    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        debugger;
        if (newValue !== undefined) {
            doRequestEstado("POST", $scope.properties.urlEstado)
                .then(function(response) {
                    // Aquí puedes trabajar con la respuesta en caso de éxito
                    console.log("Datos recibidos:", response.data);
                })
                .catch(function(error) {
                    // Aquí puedes manejar los errores
                    console.error("Error en la solicitud:", error);
                });
        }
        console.log($scope.properties.dataToSend);
    });
    
    function doRequestEstado(method, url) {
        debugger;
        return $http({
            method: method,
            url: url,
            data: $scope.properties.dataToSend
        })
        .then(function(response) {
            console.log("Datos recibidos:", response.data);
            $scope.properties.lstEstados = response.data;
            console.log("Datos Estados:", $scope.properties.lstEstados);
            return response;
        })
        .catch(function(error) {
            console.error("Error en la solicitud HTTP:", error);
            throw error; // Propagar el error para su posterior manejo si es necesario
        });
    }

    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        debugger;
        if (newValue !== undefined) {
            doRequestPais("POST", $scope.properties.urlPais)
                .then(function(response) {
                    // Aquí puedes trabajar con la respuesta en caso de éxito
                    console.log("Datos recibidos:", response.data);
                })
                .catch(function(error) {
                    // Aquí puedes manejar los errores
                    console.error("Error en la solicitud:", error);
                });
        }
        console.log($scope.properties.dataToSend);
    });
    
    function doRequestPais(method, url) {
        debugger;
        return $http({
            method: method,
            url: url,
            data: $scope.properties.dataToSend
        })
        .then(function(response) {
            console.log("Datos recibidos:", response.data);
            $scope.properties.lstPais = response.data;
            console.log("Datos Estados:", $scope.properties.lstPais);
            return response;
        })
        .catch(function(error) {
            console.error("Error en la solicitud HTTP:", error);
            throw error; // Propagar el error para su posterior manejo si es necesario
        });
    }
}