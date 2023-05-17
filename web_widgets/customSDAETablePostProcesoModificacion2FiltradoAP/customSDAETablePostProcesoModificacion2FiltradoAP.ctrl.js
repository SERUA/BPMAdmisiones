function PbTableCtrl($scope, $http, $window, blockUI) {

    this.isArray = Array.isArray;
    var vm = this;

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

    $scope.asignarTarea = function(rowData) {
        if ($scope.isPeriodoVencido(rowData.periodofin)) {
            swal("¡Periodo vencido!", "El periodo del aspirante ha vencido, se debe actualizar para poder continuar con el proceso", "warning").then((value) => {

                var page = "verSolicitudAdmisionADV2";
                doRequest2("GET", "/API/bpm/archivedHumanTask?p=0&c=10&f=caseId=" + rowData.caseid + "&f=state=aborted&d=processId", null, null, function(dataAborted) {
                    if (dataAborted.length > 0) {
                        doRequest2("POST", "/bonita/API/extension/AnahuacRest?url=recoveryData&p=0&c=100", null, { "caseId": parseInt(rowData.caseid), "processDefinitionId": dataAborted[0].processId.id }, function(recoveryData) {
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

                                            ///API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid=30197&f=state=ready&d=processId

                                            var url = "/bonita/portal/resource/app/administrativo/[PAGE]/content/?id=[TASKID]&caseId=[CASEID]&displayConfirmation=false";
                                            if (data2.length > 0) {
                                                if (parseFloat(data2[0].processId.version) < 1.51) {
                                                    page = "verSolicitudAdmision";
                                                }
                                                url = url.replace("[PAGE]", page);
                                                url = url.replace("[TASKID]", data2[0].id);
                                            } else {
                                                url = url.replace("[TASKID]", "");
                                            }
                                            url = url.replace("[CASEID]", rowData.caseid);
                                            //window.top.location.href = url;
                                            window.open(url, '_blank');
                                            $scope.$apply();
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

                        })
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

                                        ///API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid=30197&f=state=ready&d=processId

                                        var url = "/bonita/portal/resource/app/administrativo/[PAGE]/content/?id=[TASKID]&caseId=[CASEID]&displayConfirmation=false";
                                        if (data2.length > 0) {
                                            if (parseFloat(data2[0].processId.version) < 1.51) {
                                                page = "verSolicitudAdmision";
                                            }
                                            url = url.replace("[PAGE]", page);
                                            url = url.replace("[TASKID]", data2[0].id);
                                        } else {
                                            url = url.replace("[TASKID]", "");
                                        }
                                        url = url.replace("[CASEID]", rowData.caseid);
                                        //window.top.location.href = url;
                                        window.open(url, '_blank');
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

                })


            });
        } else {
            var page = "verSolicitudAdmisionADV2";
            doRequest2("GET", "/API/bpm/archivedHumanTask?p=0&c=10&f=caseId=" + rowData.caseid + "&f=state=aborted&d=processId", null, null, function(dataAborted) {
                if (dataAborted.length > 0) {
                    doRequest2("POST", "/bonita/API/extension/AnahuacRest?url=recoveryData&p=0&c=100", null, { "caseId": parseInt(rowData.caseid), "processDefinitionId": dataAborted[0].processId.id }, function(recoveryData) {
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

                                        ///API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid=30197&f=state=ready&d=processId

                                        var url = "/bonita/portal/resource/app/administrativo/[PAGE]/content/?id=[TASKID]&caseId=[CASEID]&displayConfirmation=false";
                                        if (data2.length > 0) {
                                            if (parseFloat(data2[0].processId.version) < 1.51) {
                                                page = "verSolicitudAdmision";
                                            }
                                            url = url.replace("[PAGE]", page);
                                            url = url.replace("[TASKID]", data2[0].id);
                                        } else {
                                            url = url.replace("[TASKID]", "");
                                        }
                                        url = url.replace("[CASEID]", rowData.caseid);
                                        //window.top.location.href = url;
                                        window.open(url, '_blank');
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

                    })
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

                                    ///API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid=30197&f=state=ready&d=processId

                                    var url = "/bonita/portal/resource/app/administrativo/[PAGE]/content/?id=[TASKID]&caseId=[CASEID]&displayConfirmation=false";
                                    if (data2.length > 0) {
                                        if (parseFloat(data2[0].processId.version) < 1.51) {
                                            page = "verSolicitudAdmision";
                                        }
                                        url = url.replace("[PAGE]", page);
                                        url = url.replace("[TASKID]", data2[0].id);
                                    } else {
                                        url = url.replace("[TASKID]", "");
                                    }
                                    url = url.replace("[CASEID]", rowData.caseid);
                                    //window.top.location.href = url;
                                    window.open(url, '_blank');
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

            })

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

    $scope.Transferencia = function(row) {
        let sedeExamen;
        if (row.transferencia == row.campus) {
            sedeExamen = "MISMO CAMPUS"
        } else {
            sedeExamen = "TRANSFERENCIA"
        }
        return sedeExamen;
    }


    $scope.getCatCampus();

    $scope.viewDownloadSolicitud = function(rowData) {
            doRequest2("GET", "/API/bpm/archivedHumanTask?p=0&c=10&f=caseId=" + rowData.caseid + "&f=state=aborted&d=processId", null, null, function(dataAborted) {
                if (dataAborted.length > 0) {
                    doRequest2("POST", "/bonita/API/extension/AnahuacRest?url=recoveryData&p=0&c=100", null, { "caseId": parseInt(rowData.caseid), "processDefinitionId": dataAborted[0].processId.id }, function(recoveryData) {
                        var req = {
                            method: "GET",
                            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
                        };

                        return $http(req)
                            .success(function(data, status) {
                                var url = "/apps/administrativo/descargarSolicitud/?id=[TASKID]&caseId=[CASEID]&displayConfirmation=false";
                                if (data.length > 0) {
                                    url = url.replace("[TASKID]", data[0].id);
                                } else {
                                    url = url.replace("[TASKID]", "");
                                }
                                url = url.replace("[CASEID]", rowData.caseid);
                                window.open(url, '_blank');
                            })
                            .error(function(data, status) {
                                console.error(data);
                            })
                            .finally(function() {});
                    })
                } else {
                    var req = {
                        method: "GET",
                        url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
                    };

                    return $http(req)
                        .success(function(data, status) {
                            var url = "/apps/administrativo/descargarSolicitud/?id=[TASKID]&caseId=[CASEID]&displayConfirmation=false";
                            if (data.length > 0) {
                                url = url.replace("[TASKID]", data[0].id);
                            } else {
                                url = url.replace("[TASKID]", "");
                            }
                            url = url.replace("[CASEID]", rowData.caseid);
                            window.open(url, '_blank');
                        })
                        .error(function(data, status) {
                            console.error(data);
                        })
                        .finally(function() {});
                }

            })

        }
        /**
         * Execute a get/post request to an URL
         * It also bind custom data from success|error to a data
         * @return {void}
         */
    function doRequest2(method, url, params, dataToSend, callback) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: dataToSend,
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data);
            })
            .error(function(data, status) {
                console.error(data);

            })
            .finally(function() {
                vm.busy = false;
            });
    }
    $scope.isPeriodoVencido = function(periodofin) {
        var fecha = new Date(periodofin.slice(0, 10))
        return fecha < new Date();
    }
}