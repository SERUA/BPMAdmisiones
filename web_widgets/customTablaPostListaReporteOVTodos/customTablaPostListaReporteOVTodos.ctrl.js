function PbTableCtrl($scope, $http, $window, blockUI) {

    this.isArray = Array.isArray;
    this.orden = true;

    $scope.urlPost3 = $scope.properties.urlPost;
    $scope.cambioOrden = function(orden) {
        if (orden) {
            $scope.urlPost3 = $scope.properties.urlPost1;
            //doRequest("POST", $scope.properties.urlPost);
        } else {
            $scope.urlPost3 = $scope.properties.urlPost2;
            //doRequest("POST", $scope.properties.urlPost);
        }
        //$scope.$apply();
        doRequest("POST", $scope.urlPost3);

    }


    $scope.redirecc = function(row) {

        let str = {
            "username": row.correoelectronico,
            "idbanner": row.idbanner
        };
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=postBitacoraSesiones&p=0&c=10",
            data: str,
        };
        return $http(req)
            .success(function(data, status) {
                if (data.data.length < 1) {
                    swal("¡El aspirante aún no ha seleccionado una sesión!", "", "info")
                } else {
                    var url = "/portal/resource/app/administrativo/BitacoraSesiones/content/?username=" + row.correoelectronico + "&nombre=" + `${row.apellidopaterno}\xa0${row.apellidomaterno}\xa0${row.primernombre}\xa0${row.segundonombre}` + "&idbanner=" + row.idbanner;
                    window.open(url, '_blank');
                }
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {

                blockUI.stop();
            });


    }

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
    $scope.guardarSesion = null;
    function doRequest(method, url, params) {
        debugger;
        $scope.guardarSesion = angular.copy($scope.properties.dataToSend);
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
                if($scope.properties.lstContenido.length < 1){
                   swal("No se encuentran coincidencias con el criterio de búsqueda o el aspirante aún no ha seleccionado cita.", "", "info"); 
                }
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {

                blockUI.stop();
            });
    }

    $scope.asignarTarea = function(rowData) {
        if ($scope.isPeriodoVencido(rowData.periodofin)) {
            swal("¡Periodo vencido!", "El periodo del aspirante ha vencido, se debe actualizar para poder continuar con el proceso", "warning").then((value) => {

                var req = {
                    method: "GET",
                    url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
                };

                return $http(req)
                    .success(function(data, status) {

                        blockUI.start();
                        var req2 = {
                            method: "GET",
                            url: `/API/bpm/${(data.length>0)?"humanTask":"archivedHumanTask"}?p=0&c=10&f=caseId=${rowData.caseid}&f=state=${(data.length>0)?"ready":"completed"}&d=processId`
                        };

                        $http(req2)
                            .success(function(data2, status) {

                                var url = "/apps/administrativo/appPsicometricoV3/?taskId=[TASKID]&id=[ID]&intento=[COUNTRECHAZO]&sesion=[SESION]";
                                if (data2.length > 0) {
                                    if (parseFloat(data2[0].processId.version) >= 1.51) {
                                        url = url.replace("[ID]", data2[0].caseId);
                                        url = url.replace("[TASKID]", data2[0].id);
                                        url = url.replace("[SESION]", rowData.idsesion);
                                        url = url.replace("[COUNTRECHAZO]", rowData.countrechazos == null ? (rowData.countrechazo == null ? "null" : rowData.countrechazo) : rowData.countrechazos);
                                        window.open(url, '_blank');
                                    }

                                } else {
                                    url = url.replace("[TASKID]", "");
                                }


                            })
                            .error(function(data, status) {
                                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                            })
                            .finally(function() {

                                blockUI.stop();
                            });
                    })
                    .error(function(data, status) {
                        console.error(data);
                    })
                    .finally(function() {});

            });
        } else {
            var req = {
                method: "GET",
                url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
            };

            return $http(req)
                .success(function(data, status) {

                    blockUI.start();
                    var req2 = {
                        method: "GET",
                        url: `/API/bpm/${(data.length>0)?"humanTask":"archivedHumanTask"}?p=0&c=10&f=caseId=${rowData.caseid}&f=state=${(data.length>0)?"ready":"completed"}&d=processId`
                    };

                    $http(req2)
                        .success(function(data2, status) {

                            var url = "/apps/administrativo/appPsicometricoV3/?taskId=[TASKID]&id=[ID]&intento=[COUNTRECHAZO]&sesion=[SESION]";
                            if (data2.length > 0) {
                                if (parseFloat(data2[0].processId.version) >= 1.51) {
                                    url = url.replace("[ID]", data2[0].caseId);
                                    url = url.replace("[TASKID]", data2[0].id);
                                    url = url.replace("[SESION]", rowData.idsesion);
                                    url = url.replace("[COUNTRECHAZO]", rowData.countrechazos == null ? (rowData.countrechazo == null ? "null" : rowData.countrechazo) : rowData.countrechazos);
                                    window.open(url, '_blank');
                                }

                            } else {
                                url = url.replace("[TASKID]", "");
                            }


                        })
                        .error(function(data, status) {
                            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                        })
                        .finally(function() {

                            blockUI.stop();
                        });
                })
                .error(function(data, status) {
                    console.error(data);
                })
                .finally(function() {});
        }
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
            .success(function(data, status) {

                $scope.envelopeCancel();
            })
            .error(function(data, status) {
                console.error(data)
            })
            .finally(function() {});
    }
    $scope.lstCampus = [];
   /* $(function() {
        doRequest("POST", $scope.properties.urlPost);
    })*/
    function limpiarFiltro(){
        $scope.properties.lstContenido = [];
        $scope.lstPaginado = [];
        $scope.iniciarP = 1;
        $scope.finalP = 10;
        $scope.value = 0;
    }

    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        if (newValue !== undefined) {
            if(newValue.campus !== undefined ){
                if(($scope.filtroCampus != "Todos los campus" && $scope.properties.dataToSend.lstFiltro.length > 1)||($scope.filtroCampus == "Todos los campus" && $scope.properties.dataToSend.lstFiltro.length > 0)){
                    doRequest("POST", $scope.urlPost3);
                }else{
                    limpiarFiltro()
                }
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
        if(($scope.filtroCampus != "Todos los campus" && $scope.properties.dataToSend.lstFiltro.length > 1)||($scope.filtroCampus == "Todos los campus" && $scope.properties.dataToSend.lstFiltro.length > 0)){
            doRequest("POST", $scope.urlPost3);
        }
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

        doRequest("POST", $scope.urlPost3);
    }

    $scope.filterKeyPressSuperior = function(columna, press) {
        $scope.limpiarFiltrosTabla();
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

        doRequest("POST", $scope.urlPost3);
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

        doRequest("POST", $scope.urlPost3);
    }
    $scope.getCampusByGrupo = function(campus) {
        var retorno = "";
        for (var i = 0; i < $scope.properties.lstCampus.length; i++) {
            if (campus == $scope.properties.lstCampus[i].grupoBonita) {
                retorno = $scope.properties.lstCampus[i].descripcion
                if ($scope.lstCampusByUser.length == 1) {
					debugger;
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
        //resultado.push("Todos los campus")
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

        doRequest("POST", $scope.urlPost3);
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

    $scope.isPeriodoVencido = function(periodofin) {
        var fecha = new Date(periodofin.slice(0, 10))
        return fecha < new Date();
    }
    
    $(function() {
        doRequestPeriodo();
    })

    function doRequestPeriodo() {
        blockUI.start();
        var req = {
            method: "GET",
            url: "/bonita/API/extension/AnahuacRestGet?url=getPeriodo&p=0&c=100",
        };

        return $http(req)
            .success(function(data, status) {
                console.log(data)
                $scope.periodoLista = data;
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {

                blockUI.stop();
            });
    }
    
    function doRequestCarrera() {
        blockUI.start();
        var req = {
            method: "GET",
            url: "/bonita/API/extension/AnahuacRestGet?url=getCarreraByCampus&p=0&c=100&campus="+$scope.properties.campusSeleccionado,
        };

        return $http(req)
            .success(function(data, status) {
                console.log(data.data)
                $scope.carreraLista = data.data;
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {

                blockUI.stop();
            });
    }

    $scope.$watch("properties.campusSeleccionado", function(newValue, oldValue) {
        if (newValue !== undefined) {
            doRequestCarrera();
        }
    });

    $scope.$watch("filtroCampus", function(newValue, oldValue) {
        if (newValue !== undefined) {
        	if(newValue == "Todos los campus"){
        		doRequestCarrera();
        	}
            
        }
    });


    $scope.peridoSelected = "";
    $scope.periodoLista = [];
    $scope.carreraSelected = "";
    $scope.carreraLista =[];

    $scope.filterSelectCarrera = function() {
		$scope.limpiarFiltrosTabla();
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
            doRequest("POST", $scope.urlPost3);
        }
    }

    $scope.filterSelectPeriodo = function() {
		$scope.limpiarFiltrosTabla();
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
            doRequest("POST", $scope.urlPost3);
        }
    }
    
   $scope.limpiarFiltros = function(){
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

    $scope.descargarExcel = function(row) {
        debugger;
        var url = '/bonita/API/extension/AnahuacRest?url=getExcelFileReporteOvBusquedaAvanzada&p=0&c=100';
    
        // Ajusta los valores según tus necesidades
        var method = 'POST';
        var filename = 'reporteov.xlsx';
    
        // Agrega el nombre de la hoja al objeto $scope.guardarSesion
        $scope.guardarSesion.type = 'ReporteSesiones'; // Puedes usar el nombre que prefieras
    
        var req = {
            method: method,
            url: url,
            data: $scope.guardarSesion, // Envía el contenido de $scope.guardarSesion en el cuerpo de la solicitud
            responseType: 'text',
        };
    
        return $http(req)
        .success(function(response, status) {
            debugger;
            const blob = b64toBlob(response.data[0]);
            const blobUrl = URL.createObjectURL(blob);
            
            var link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = "ReporteOV";

            document.body.appendChild(link);

            link.click();

            document.body.removeChild(link);
            
            //window.open(blobUrl, '_blank');
            // window.location = blobUrl;
        })
        .error(function(data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status});
        })
        .finally(function() {
            vm.busy = false;
        });
    };
    
    function b64toBlob(dataURI) {
        debugger;
        var byteString = atob(dataURI);
        var ab = new ArrayBuffer(byteString.length);
        var ia = new Uint8Array(ab);
        let contentType = "";
        
        contentType = "application/vnd.ms-excel";

        for (var i = 0; i < byteString.length; i++) {
            ia[i] = byteString.charCodeAt(i);
        }
        return new Blob([ab], { type: contentType });
    }
    
}