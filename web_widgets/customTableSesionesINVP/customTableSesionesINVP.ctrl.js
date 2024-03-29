function PbTableCtrl($scope, $http, $window, blockUI) {

    this.isArray = Array.isArray;
    $scope.idiomaTodos = "";
    $scope.seleccionarTodosVar = false;
    $scope.tabShown = "seguimiento";
    $scope.preguntas = true;
    
    $scope.switchTab = function(_tab){
        $scope.tabShown = _tab;
        $scope.preguntas = _tab === 'seguimiento';
        
        getAspirantesSesion($scope.selectedSesion.idSesion);
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

    $scope.setNavVar = function (_value) {
        $scope.properties.navigationVar = _value;
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


    $scope.lstPaginadoAsp = [];
    $scope.valorSeleccionadoAsp = 1;
    $scope.iniciarPAsp = 1;
    $scope.finalPAsp = 10;
    $scope.valorTotalAsp = 10;

    $scope.loadPaginadoAsp = function () {
        $scope.valorTotalAsp = Math.ceil($scope.valueAsp / $scope.dataToSend.limit);
        $scope.lstPaginadoAsp = []
        if ($scope.valorSeleccionadoAsp <= 5) {
            $scope.iniciarPAsp = 1;
            $scope.finalPAsp = $scope.valorTotalAsp > 10 ? 10 : $scope.valorTotalAsp;
        } else {
            $scope.iniciarPAsp = $scope.valorSeleccionadoAsp - 5;
            $scope.finalPAsp = $scope.valorTotalAsp > ($scope.valorSeleccionadoAsp + 4) ? ($scope.valorSeleccionadoAsp + 4) : $scope.valorTotalAsp;
        }
        for (var i = $scope.iniciarPAsp; i <= $scope.finalPAsp; i++) {

            var obj = {
                "numero": i,
                "inicio": ((i * 10) - 9),
                "fin": (i * 10),
                "seleccionado": (i == $scope.valorSeleccionadoAsp)
            };
            $scope.lstPaginadoAsp.push(obj);
        }
    }

    $scope.siguienteAsp = function () {
        var objSelected = {};
        for (var i in $scope.lstPaginadoAsp) {
            if ($scope.lstPaginadoAsp[i].seleccionado) {
                objSelected = $scope.lstPaginadoAsp[i];
                $scope.valorSeleccionadoAsp = $scope.lstPaginadoAsp[i].numero;
            }
        }
        $scope.valorSeleccionadoAsp = $scope.valorSeleccionadoAsp + 1;
        if ($scope.valorSeleccionadoAsp > Math.ceil($scope.valueAsp / $scope.dataToSend.limit)) {
            $scope.valorSeleccionadoAsp = Math.ceil($scope.valueAsp / $scope.dataToSend.limit);
        }
        $scope.seleccionarPagina($scope.valorSeleccionadoAsp);
    }

    $scope.anteriorAsp = function () {
        var objSelected = {};
        for (var i in $scope.lstPaginadoAsp) {
            if ($scope.lstPaginadoAsp[i].seleccionado) {
                objSelected = $scope.lstPaginadoAsp[i];
                $scope.valorSeleccionadoAsp = $scope.lstPaginadoAsp[i].numero;
            }
        }
        $scope.valorSeleccionadoAsp = $scope.valorSeleccionadoAsp - 1;
        if ($scope.valorSeleccionadoAsp == 0) {
            $scope.valorSeleccionadoAsp = 1;
        }
        $scope.seleccionarPagina($scope.valorSeleccionadoAsp);
    }

    $scope.seleccionarPaginaAsp = function (valorSeleccionadoAsp) {
        var objSelected = {};
        for (var i in lstPaginadoAsp) {
            if (lstPaginadoAsp[i].numero == valorSeleccionadoAsp) {
                $scope.inicio = (lstPaginadoAsp[i].numero - 1);
                $scope.fin = lstPaginadoAsp[i].fin;
                $scope.valorSeleccionadoAsp = lstPaginadoAsp[i].numero;
                $scope.dataToSend.offset = ((lstPaginadoAsp[i].numero - 1) * $scope.dataToSend.limit)
            }
        }

        doRequest("POST", $scope.properties.urlPost);
    }

    function getAspirantesSesion(_idsesion){
        // let url = "../API/extension/AnahuacINVPRestAPI?url=getAspirantesTodos&p=0&c=10";
        let url = "../API/extension/AnahuacINVPRestAPI?url=getAspirantesTodos&p=0&c=10&preguntas=";
        url += ($scope.preguntas + "" );
        
        $scope.dataToSend = angular.copy($scope.properties.dataToSendAsp);

        if($scope.properties.dataToSendAsp.lstFiltro.length > 0){
            let encontrado = false;
            for(let dato of $scope.properties.dataToSendAsp.lstFiltro){
                if(dato.columna === "id_sesion"){
                    encontrado = true;
                    let index = $scope.properties.dataToSendAsp.lstFiltro.indexOf(dato);
                    $scope.properties.dataToSendAsp.lstFiltro[index].valor = _idsesion + "";
                    break;
                }
            }

            if(encontrado === false){
                $scope.properties.dataToSendAsp.lstFiltro.push({
                    "columna": "id_sesion",
                    "operador": "Igual a",
                    "valor": _idsesion + ""
                });
            }
        } else {
            $scope.properties.dataToSendAsp.lstFiltro = [{
                "columna": "id_sesion",
                "operador": "Igual a",
                "valor": _idsesion + ""
            }];
        }
        
        $http.post(url, $scope.properties.dataToSendAsp).success(function(_data){
            $scope.aspirantes = _data.data;
            $scope.valueAsp = _data.totalRegistros;
            $scope.loadPaginadoAsp();
            // $("#modalAspirantesSesion").modal("show");
            $scope.properties.navigationVar = "aspirantesEnSesion";
        }).error(function(_err){
            swal("Error", _err.mensajeError, "error");
        })
    }

    $scope.verSesion = function (_sesion) {
        $scope.selectedSesion = angular.copy(_sesion);
        console.log($scope.selectedSesion);
        getAspirantesSesion(_sesion.idSesion);
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

    $scope.setOrderByAsp = function (order) {
        if ($scope.properties.dataToSendAsp.orderby == order) {
            $scope.properties.dataToSendAsp.orientation = ($scope.properties.dataToSendAsp.orientation == "ASC") ? "DESC" : "ASC";
        } else {
            $scope.properties.dataToSendAsp.orderby = order;
            $scope.properties.dataToSendAsp.orientation = "ASC";
        }
        
        getAspirantesSesion($scope.selectedSesion.idSesion);
    }

    $scope.filterKeyPressAspirantes = function(columna, press) {
        var aplicado = true;
        
        for (let index = 0; index < $scope.properties.dataToSendAsp.lstFiltro.length; index++) {
            const element = $scope.properties.dataToSendAsp.lstFiltro[index];
            if (element.columna == columna) {
                $scope.properties.dataToSendAsp.lstFiltro[index].valor = press;
                $scope.properties.dataToSendAsp.lstFiltro[index].operador = "Que contengan";
                aplicado = false;
            }
        }
        if (aplicado) {
            var obj = { "columna": columna, "operador": "Que contengan", "valor": press }
            $scope.properties.dataToSendAsp.lstFiltro.push(obj);
        }
  
        getAspirantesSesion($scope.selectedSesion.idSesion);
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

            return $http(req).success(function (data, status) {
                $scope.lstMembership = data;
                $scope.campusByUser();
            }).error(function (data, status) {
                console.error(data);
            }).finally(function () { 

            });
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

    $scope.filtroCampus = ""

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

        return $http(req).success(function (data, status) {
            $scope.lstCampus = [];
            for (var index in data) {
                $scope.lstCampus.push({
                    "descripcion": data[index].descripcion,
                    "valor": data[index].grupoBonita
                })
            }
        }).error(function (data, status) {
            console.error(data);
        });
    }

    $scope.isPeriodoVencido = function (periodofin) {
        var fecha = new Date(periodofin.slice(0, 10))
        return fecha < new Date();
    }


    $scope.getCatCampus();

    $scope.setSelectedAspirante = function (_aspirante, _modal) {
        $scope.selectedAspirante = angular.copy(_aspirante);
        if (_modal === "bloquear") {
            mostrarModal("modalBloquear");
        } else if (_modal === "reactivar") {
            $scope.aplicacion = "";
            $scope.entrada = "";
            $scope.salida = "";
            $scope.configUsuario = {
                "username": _aspirante.correoElectronico,
                "aplicacion": "",
                "entrada": "",
                "salida": "",
                "toleranciaminutos": 0,
                "toleranciasalidaminutos": 0
            }
            mostrarModal("modalReactivar");
        } else if (_modal === "reagen") {
            $scope.aplicacion = "";
            $scope.entrada = "";
            $scope.salida = "";
            $scope.configUsuario = {
                "username": _aspirante.correoElectronico,
                "aplicacion": "",
                "entrada": "",
                "salida": "",
                "toleranciaminutos": 0,
                "toleranciasalidaminutos": 0
            }
            mostrarModal("modalReagen");
        } else if (_modal === "ver") {
            $scope.configUsuario = {
                "username": _aspirante.correoElectronico,
                "toleranciaminutos": parseInt($scope.selectedAspirante.temptoleranciaentrada),
                "toleranciasalidaminutos": parseInt($scope.selectedAspirante.temptoleranciaSalida),
                "caseid": $scope.selectedAspirante.caseidINVP
            }
            mostrarModal("modalVerReag");
        } else {
            mostrarModal("modalTerminar");
        }

    }

    $scope.mostrarModalTodos = function (_idModal) {
        mostrarModal(_idModal)
    }

    function mostrarModal(_idModal) {
        $("#" + _idModal).modal("show");
    }

    function ocultarModal(_idModal) {
        $("#" + _idModal).modal("hide");
    }

    $scope.terminarAspirante = function () {
        let url = "../API/extension/AnahuacINVPRestAPI?url=bloquearAspirante&p=0&c=10&username="
            + $scope.selectedAspirante.correoElectronico + "&bloquear=" + $scope.selectedAspirante.bloqueado + "&terminar=" + !$scope.selectedAspirante.terminado;

        $http.post(url).success(function (_data) {
            getUserInfo($scope.selectedAspirante.correoElectronico, $scope.selectedAspirante.caseidINVP);
        }).error(function (_error) {

        });
    }

    $scope.reagendarAspiranteNoIniciado = function () {

        if (validarConfig()) {
            if ($scope.selectedAspirante.isTemporal) {
                $scope.configUsuario.idprueba = $scope.selectedSesion.idSesion
            }

            let url = "../API/extension/AnahuacINVPRestAPI?url=insertUpdateUsuarioNuevaConfig&p=0&c=10";

            $http.post(url, $scope.configUsuario).success(function (_data) {
                let url = "../API/extension/AnahuacINVPRestAPI?url=bloquearAspirante&p=0&c=10&username=" + $scope.selectedAspirante.correoElectronico + "&bloquear=false&terminar=false";

                $http.post(url).success(function (_data) {
                    ocultarModal("modalReagen");
                    swal("Ok", "Usuario re agendado", "success");
                    getAspirantesSesion($scope.selectedSesion.idSesion);
                }).error(function (_error) {

                });
            }).error(function (_error) {

            });
        }
    }

    $scope.bloquearAspiranteDef = function () {
        let servicio = "bloquearAspiranteDef";

        if ($scope.selectedAspirante.usuarioBloqueado) {
            servicio = "desbloquearAspiranteDef"
        }

        let url = "../API/extension/AnahuacINVPRestGet?url=" + servicio + "&p=0&c=10&username=" + $scope.selectedAspirante.correoElectronico;

        $http.get(url).success(function (_data) {
            let mensaje = "Usuario " + ($scope.selectedAspirante.usuarioBloqueado ? "desbloqueado" : "bloqueado");
            ocultarModal("modalBloquear");
            swal("Ok", mensaje, "success");
            getAspirantesSesion($scope.selectedSesion.idSesion);
        }).error(function (_error) {

        });
    }

    $scope.idDoFor = "";

    function getUserInfo(_username, _caseId) {
        let url = "../API/identity/user?c=10&p=0&f=userName=" + _username;

        $http.get(url).success(function (_data) {
            $scope.idDoFor = _data[0].id;

            getTaskInfo(_caseId);
        }).error(function (_error) {
            swal("Algo ha fallado", "Por favor intente de nuevo mas tarde", "error");
        });
    }

    function getTaskInfo(_caseid) {
        let url = "../API/bpm/task?c=10&p=0&f=caseId=" + _caseid;

        $http.get(url).success(function (_data) {
            if (_data[0].name === "Examen INVP") {
                executeTaskExamen(_data[0].id, $scope.idDoFor);
            } else if (_data[0].name === "Finalizar examen") {
                executeTaskReactivar(_data[0].id);
            }
        }).error(function (_error) {
            swal("Algo ha fallado", "Por favor intente de nuevo mas tarde", "error");
        });
    }

    function executeTaskExamen(_taskId, _userId) {
        let url = "../API/bpm/userTask/" + _taskId + "/execution?user=" + _userId;

        let dataToSend = {
            "isTerminado": true,
            "terminadoFInput": true,
            "instanciaINVPInput": {
                "mensajeTermino": ""
            }
        }

        $http.post(url, dataToSend).success(function (_data) {
            ocultarModal("modalTerminar");
            swal("Ok", "Usuario terminado", "success");
            getAspirantesSesion($scope.selectedSesion.idSesion);
        }).error(function (_error) {
            swal("Algo ha fallado", "Por favor intente de nuevo mas tarde", "error");
        });
    }

    function executeTaskReactivar(_taskId) {
        let url = "../API/bpm/userTask/" + _taskId + "/execution?assign=true";

        let dataToSend = {
            "repetirExamenInput": true
        }

        $http.post(url, dataToSend).success(function (_data) {
            ocultarModal("modalReactivar");
            swal("Ok", "El usuario ha sido reactivado", "success");
            getAspirantesSesion($scope.selectedSesion.idSesion);
        }).error(function (_error) {
            swal("Algo ha fallado", "Por favor intente de nuevo mas tarde", "error");
        });
    }

    $scope.insertUpdateIidiomaUsuarioTodos = function () {
        $scope.dataToSend = angular.copy($scope.properties.dataToSendAsp);
        $scope.dataToSend.lstFiltro = [{
            "columna": "id_sesion",
            "valor": $scope.selectedSesion.idSesion + ""
        }];

        let url = "../API/extension/AnahuacINVPRestAPI?url=updateIdiomaTodos&p=0&c=10&idioma=" + $scope.idiomaTodos;

        $http.post(url, $scope.dataToSend).success(function (_data) {
            ocultarModal("modalIdiomaTodos");
            swal("Ok", "Idioma actualizado para todos los aspirantes", "success");
            getAspirantesSesion($scope.selectedSesion.idSesion);
        }).error(function (_error) {

        });
    }

    function showModalConfig() {
        $("#modalConfiguraciones").modal("show");
    }

    $scope.getConfiguracionINVP = function (_row) {
        $scope.sesionConfiguracion = {
            "idprueba": _row.idSesion,
            "toleranciaminutos": 0,
            "toleranciasalidaminutos": 0
        };

        let url = "../API/extension/AnahuacINVPRestGet?url=getConfiguracionSesion&p=0&c=10&idprueba=" + _row.idSesion;

        $http.get(url).success(function (_data) {
            if (_data[0]) {
                $scope.sesionConfiguracion.toleranciaminutos = _data[0].toleranciaMinutos;
                $scope.sesionConfiguracion.toleranciasalidaminutos = _data[0].toleranciaSalidaMinutos;
            }

            showModalConfig();
        }).error(function (_error) {
            swal("Algo ha fallado", "Por favor intente de nuevo mas tarde", "error");
        });
    }

    $scope.insertUpdateConfiguracionSesion = function () {
        if (validateConfig()) {
            $scope.dataToSend = angular.copy($scope.sesionConfiguracion);
            let url = "../API/extension/AnahuacINVPRestAPI?url=insertUpdateConfiguracionSesion&p=0&c=10";

            $http.post(url, $scope.dataToSend).success(function (_data) {
                ocultarModal("modalConfiguraciones");
                swal("Ok", "La configuración se ha guardado correctamente", "success");
                getAspirantesSesion($scope.selectedSesion.idSesion);
            }).error(function (_error) {
                swal("Algo ha fallado", "Por favor intente de nuevo mas tarde", "error");
            });
        }
    }

    function validateConfig() {
        let output = true;
        let mensajeError = "";

        if ($scope.sesionConfiguracion.toleranciaminutos < 0 || $scope.sesionConfiguracion.toleranciaminutos === "" || $scope.sesionConfiguracion.toleranciaminutos === null) {
            output = false;
            mensajeError = "Campo 'Tolerancia (minutos)' debe contener un valor mayor o igual a cero";
        } else if ($scope.sesionConfiguracion.toleranciaminutos < 0 || $scope.sesionConfiguracion.toleranciaminutos === "" || $scope.sesionConfiguracion.toleranciaminutos === null) {
            output = false;
            mensajeError = "Campo 'Tolerancia (minutos)' debe contener un valor mayor o igual a cero";
        }

        if (!output) {
            swal("¡Atención!", mensajeError, "warning");
        }

        return output;
    }

    $scope.cambiarIdioma = function (_aspirante, _modal) {
        $scope.selectedAspirante = angular.copy(_aspirante);
        mostrarModal("modalIdioma");
    }

    $scope.insertUpdateIidiomaUsuario = function () {
        let dataToSend = {
            "nombreusuario": $scope.selectedAspirante.correoElectronico,
            "idioma": $scope.selectedAspirante.idioma === "ESP" ? "ENG" : "ESP"
        }

        let url = "../API/extension/AnahuacINVPRestAPI?url=insertUpdateIidiomaUsuario&p=0&c=10";

        $http.post(url, dataToSend).success(function (_data) {
            ocultarModal("modalIdioma");
            swal("Ok", "Idioma actualizado", "success");
            getAspirantesSesion($scope.selectedSesion.idSesion);
        }).error(function (_error) {

        });
    }

    $scope.configUsuario = {
        "aplicacion": "",
        "entrada": "",
        "salida": "",
        "toleranciaminutos": 0,
        "toleranciasalidaminutos": 0
    }

    $scope.aplicacion = "";
    $scope.entrada = "";
    $scope.salida = "";

    $scope.insertUpdateUsuarioNuevaConfig = function (_action) {
        if (validarConfig()) {
            if (_action === "temp") {
                $scope.configUsuario.idprueba = $scope.selectedSesion.idSesion
            }

            let url = "../API/extension/AnahuacINVPRestAPI?url=insertUpdateUsuarioNuevaConfig&p=0&c=10";

            $http.post(url, $scope.configUsuario).success(function (_data) {
                ocultarModal("modalReagen");
                if ($scope.selectedSesion.estatus === "Concluida" && $scope.selectedAspirante.estatusINVP === "Por iniciar") {
                    swal("Ok", "Usuario reagendado", "success");
                    $scope.refreshAspirantes();
                } else {
                    $scope.terminarAspirante();
                }
            }).error(function (_error) {
                swal("Algo ha fallado", "Por favor intente de nuevo mas tarde", "error");
            });
        }
    }

    function validarConfig() {
        let output = true;
        let mensajeError = "";

        if (!$scope.configUsuario.aplicacion) {
            mensajeError = "Campo 'Fecha' no debe ir vacío";
            output = false;
        } 
        // else if (!$scope.configUsuario.entrada) {
        //     mensajeError = "Campo 'Hora inicio' no debe ir vacío";
        //     output = false;
        // } else if (!$scope.configUsuario.salida) {
        //     mensajeError = "Campo 'Hora fin' no debe ir vacío";
        //     output = false;
        // } else if ($scope.configUsuario.toleranciaminutos === null || $scope.configUsuario.toleranciaminutos === undefined || $scope.configUsuario.toleranciaminutos < 0) {
        //     mensajeError = "Campo 'Tolerancia entrada: (minutos)' no debe ir vacío y debe tener un valor mínimo de 0.";
        //     output = false;
        // } else if ($scope.configUsuario.toleranciasalidaminutos === null || $scope.configUsuario.toleranciasalidaminutos === undefined || $scope.configUsuario.toleranciasalidaminutos < 0) {
        //     mensajeError = "Campo 'Tolerancia salida (minutos):' no debe ir vacío y debe tener un valor mínimo de 0.";
        //     output = false;
        // }

        if (output == false) {
            swal("¡Atención!", mensajeError, "warning");
        }

        return output;
    }

    $scope.setValue = function (_value, _type) {
        let fecha = new Date(_value);

        if (_type === "aplicacion") {
            fecha = (fecha.getFullYear()) + "-"
                + ((fecha.getMonth() + 1) < 10 ? "0" + (fecha.getMonth() + 1) : (fecha.getMonth() + 1)) + "-"
                + (fecha.getDate() < 10 ? "0" + fecha.getDate() : fecha.getDate());
        } else {
            fecha = (fecha.getHours() < 10 ? "0" + fecha.getHours() : fecha.getHours()) + ":"
                + (fecha.getMinutes() < 10 ? "0" + fecha.getMinutes() : fecha.getMinutes()) + ":00";
        }

        $scope.configUsuario[_type] = fecha
    }

    $scope.fechaMenorATresDias = function (_fecha) {
        var hoy = new Date();
        var fechaIngresada = new Date(_fecha);
        var diferenciaEnMilisegundos = fechaIngresada - hoy;
        var dias = Math.floor(diferenciaEnMilisegundos / (1000 * 60 * 60 * 24));

        return (dias < 3);
    }

    $scope.insertUpdateUsuarioTolerancias = function (_action) {
        if (validarConfigTol()) {
            if (_action === "temp") {
                $scope.configUsuario.idprueba = $scope.selectedSesion.idSesion
            }

            let url = "../API/extension/AnahuacINVPRestAPI?url=insertUpdateUsuarioTolerancias&p=0&c=10";

            $http.post(url, $scope.configUsuario).success(function (_data) {
                
                if($scope.configUsuario.toleranciasalidaminutos > 0){
                    actualizarTolSalida($scope.configUsuario.caseid, $scope.configUsuario.toleranciasalidaminutos);
                } else {
                    ocultarModal("modalVerReag");
                    swal("Ok", "Tolerancia actualizada", "success");
                    getAspirantesSesion($scope.selectedSesion.idSesion);
                }
                
                // ocultarModal("modalVerReag");
                // swal("Ok", "Tolerancia actualizada", "success");
                // getAspirantesSesion($scope.selectedSesion.idSesion);
            }).error(function (_error) {
                swal("Algo ha fallado", "Por favor intente de nuevo mas tarde", "error");
            });
        }
    }

    function validarConfigTol() {
        let output = true;
        let mensajeError = "";

        if ($scope.configUsuario.toleranciaminutos === null || $scope.configUsuario.toleranciaminutos === undefined || $scope.configUsuario.toleranciaminutos < 0) {
            mensajeError = "Campo 'Tolerancia entrada: (minutos)' no debe ir vacío y debe tener un valor mínimo de 0.";
            output = false;
        } else if ($scope.configUsuario.toleranciasalidaminutos === null || $scope.configUsuario.toleranciasalidaminutos === undefined || $scope.configUsuario.toleranciasalidaminutos < 0) {
            mensajeError = "Campo 'Tolerancia salida (minutos):' no debe ir vacío y debe tener un valor mínimo de 0.";
            output = false;
        }

        if (output == false) {
            swal("¡Atención!", mensajeError, "warning");
        }

        return output;
    }

    $scope.refreshAspirantes = function () {
        $scope.verSesion($scope.selectedSesion);
        // getAspirantesSesion($scope.selectedSesion.idSesion);
    }
    
    // $scope.refreshAspirantes = function () {
    //     // $scope.verSesion($scope.selectedSesion);
    //     doRequestRefresh();
    // }

    function doRequestRefresh(method, url, params) {
        blockUI.start();
        
        var req = {
            method: method,
            url: url,
            data: angular.copy({columna: "No.", operador: "Que contengan", valor: $scope.selectedSesion.idSesion}),
            params: params
        };
  
        return $http(req)
        .success(function(data, status) {
            $scope.selectedSesion = data[0];
            getAspirantesSesion($scope.selectedSesion.idSesion);
        })
        .error(function(data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {
            blockUI.stop();
        });
    }
    
    $scope.refreshSesiones = function(){
        doRequest("POST", $scope.properties.urlPost);
    }

    $scope.enviarResultados = function (username, idbanner) {
        if (idbanner == null) {
            swal("¡Atención!", "El aspirante tiene que tener un id banner para poder enviar su resultado", "warning");
        } else {
            let dataToSend = {
                "username": username,
                "sesion": $scope.selectedSesion.idSesion,
                "idbanner": idbanner
            }

            let url = "../API/extension/AnahuacINVPRestAPI?url=PostRespuestaSesion&p=0&c=10";

            $http.post(url, dataToSend).success(function (_data) {
                swal("Ok", "Se cargaron los datos correctamente", "success");
            }).error(function (_error) {
                swal("Algo ha fallado", _error.error_info, "error");
            });
        }

    }

    $scope.enviarTodos = function (aspirantes) {
        var dataToSend = [];
        aspirantes.forEach((element) => {
            dataToSend.push({
                "username": element.username,
                "sesion": $scope.selectedSesion.idSesion,
                "idbanner": element.idbanner
            })
        });

        let url = "../API/extension/AnahuacINVPRestAPI?url=PostMasivoRespuestaSesion&p=0&c=10";

        $http.post(url, dataToSend).success(function (_data) {
            if (_data.success) {
                swal("Ok", "Los resultados seleccionados fueron enviados correctamente: " + _data.info, "success");
            } else {
                swal("Warning", _data.info, "warning");
            }
        }).error(function (_error) {
            swal("Algo ha fallado", _error.info, "warning");
        });
    }

    $scope.enviarSeleccionados = function () {
        var dataToSend = [];
        $scope.aspirantesEnvio.forEach((element) => {
            if (element.seleccionado) {
                dataToSend.push({
                    "username": element.username,
                    "sesion": $scope.selectedSesion.idSesion,
                    "idbanner": element.idbanner,
                    "caseidINVP" : element.caseidINVP != null ?  element.caseidINVP: element.caseidINVP_temp
                });
            }
        });

        let url = "../API/extension/AnahuacINVPRestAPI?url=PostMasivoRespuestaSesion&p=0&c=10";

        $http.post(url, dataToSend).success(function (_data) {
            if (_data.success) {
                $scope.refreshAspirantes();
                swal("Ok", "Los resultados seleccionados fueron enviados correctamente" , "success");
                $scope.showModalEnviarSeleccionados(false);
            } else {
                swal("Algo ha fallado", _data.info, "error");
            }
        }).error(function (_error) {
            swal("Algo ha fallado", _error.info, "error");
        });
    }


    $scope.generarListaEnvios = function () {
        cargarLista();
        $scope.showModalEnviarSeleccionados(true);
    }

    function cargarLista(){
        $scope.aspirantesEnvio = [];
        $scope.aspirantes.forEach((element) => {
            
            if (element.idsesion === $scope.selectedSesion.idSesion && (element.estatusINVP === "Prueba terminada" || element.estatusINVP === "Prueba terminada por administrador" ) && !element.resultadoEnviado && element.idBanner!==null && element.sesionAsignada===true ) {
            // if ((element.estatusINVP === "Prueba terminada" || element.estatusINVP === "Prueba terminada por administrador") && !element.resultadoEnviado && element.idBanner !== null && element.sesionAsignada === true) {
                $scope.aspirantesEnvio.push({
                    "username": element.correoElectronico.replace(' (rechazado)', ''),
                    "sesion": $scope.selectedSesion.idSesion,
                    "idbanner": element.idBanner,
                    "nombre": element.nombre,
                    "idBpm": element.idBpm,
                    "sesionAsignada": element.sesionAsignada,
                    "caseidINVP": element.caseidINVP, 
                    "caseidINVP_temp": element.caseidINVP_temp
                });
            }
        });

        console.log("$scope.aspirantesEnvio", $scope.aspirantesEnvio);
    }

    $scope.showModalEnviarSeleccionados = function (_show) {
        if (_show) {
            $("#modalEnviarSeleccionados").modal("show");
        } else {
            $("#modalEnviarSeleccionados").modal("hide");
        }
    }

    $scope.seleccionarTodos = function () {
        $scope.aspirantesEnvio.forEach((element) => {
            element.seleccionado = $scope.seleccionarTodosVar;
        });
    }
    
    $scope.validarEstatus = function(_enviado, _estatus){
        let output = ""; 

        if((_estatus === "Prueba terminada por administrador" || _estatus === "Prueba terminada") && _enviado === true){
            output = "Enviado"
        } else {
            output = _estatus;
        }

        return  output;
    }

    $scope.terminarAspiranteTodos = function(){
        let url = "../API/extension/AnahuacINVPRestAPI?url=terminarTodos&p=0&c=10&idsesion=" + $scope.selectedSesion.idSesion;
        $http.post(url).success(function(_data){
            ocultarModal("modalTerminarTodos");
            swal("Ok", "La sesión ha terminado para todos los usuarios.", "success");
            getAspirantesSesion($scope.selectedSesion.idSesion);
        }).error(function(_error){
            swal("Algo ha fallado", "Ha ocurrido un error al terminar la sesión para todos los aspirantes en proceso. Intente de nuevo mas tarde", "error");
        });
    }

    $scope.deleteContent = function(objContent) {
        console.log(objContent);
        var index = $scope.properties.dataToSendAsp.lstFiltro.indexOf(objContent);

        if(index != -1){
            $scope.properties.dataToSendAsp.lstFiltro.splice(index, 1);
            getAspirantesSesion($scope.selectedSesion.idSesion);
        }
    }
    
    function actualizarTolSalida(_caseid, _minutos){
        let url = "../API/bpm/timerEventTrigger?caseId=" + _caseid + "&p=0&c=10&";

        $http.get(url).success(function(_data){
            let newMS = _data[0].executionDate + (60000 * _minutos);
            updateTimer(_data[0].id_string, newMS);
        }).error(function(_error){
            ocultarModal("modalVerReag");
            swal("Ok", "Tolerancia actualizada", "success");
            getAspirantesSesion($scope.selectedSesion.idSesion);
        });
    }

    function updateTimer(_timerId, _newValue){
        let url = "../API/bpm/timerEventTrigger/" + _timerId;
        let newTimer = { "executionDate" : _newValue };
        
        $http.put(url, newTimer).success(function(_data){
            ocultarModal("modalVerReag");
            swal("Ok", "Tolerancia actualizada", "success");
            getAspirantesSesion($scope.selectedSesion.idSesion);
        }).error(function(_error){
            swal("Algo ha fallado", "Por favor intente de nuevo mas tarde", "error");
        });
    }
}