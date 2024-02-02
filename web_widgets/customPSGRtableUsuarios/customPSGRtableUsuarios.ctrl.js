function PbTableCtrl($scope, blockUI, $http) {
    var vm = this;
    this.isArray = Array.isArray;
    
    $scope.limpiarFiltros = function(){
        if($scope.filtroToSend){
            for(let filtro of $scope.filtroToSend.lstFiltro){
                if(filtro.columna !== "CAMPUS"){
                    $scope.filtroToSend.lstFiltro.splice($scope.filtroToSend.lstFiltro.indexOf(filtro), 1);
                }
            }
        } 
        
        
        for(let filtro of $scope.properties.headers){
            $scope.dynamicInput[filtro] = "";
        }
    }
    
    $scope.deleteContent = function(objContent) {
        var index = $scope.filtroToSend.lstFiltro.indexOf(objContent);
        if(index != -1){
            $scope.filtroToSend.lstFiltro.splice(index, 1);
        }
    }
    
    $scope.pantalla = 'principal';
    $scope.filtroToSend = {
        "estatusSolicitud": "Solicitud en progreso",
        "tarea": "Llenar solicitud",
        "lstFiltro": [],
        "type": "solicitudes_progreso",
        "usuario": 0,
        "orderby": "",
        "orientation": "DESC",
        "limit": 20,
        "offset": 0
    }
    $scope.usuario = {
        "enabled": "true",
        "firstname": "",
        "lastname": "",
        "password": "",
        "password_confirm": "",
        "userName": ""
    }
    $scope.dynamicInput = {};
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

    function doRequest(method, url, params, dataToSend, extra, callback) {
        vm.busy = true;
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data, extra)
            })
            .error(function(data, status) {
                console.error(data);

                if (data.message.includes("No se puede crear el usuario")) {
                    Swal.fire("¡Error!", data.message, "error");
                }

            })
            .finally(function() {
                vm.busy = false;
                blockUI.stop();
            });
    }
    $scope.setOrderBy = function(order) {
        if ($scope.filtroToSend.orderby == order) {
            $scope.filtroToSend.orientation = ($scope.filtroToSend.orientation == "ASC") ? "DESC" : "ASC";
        } else {
            $scope.filtroToSend.orderby = order;
            $scope.filtroToSend.orientation = "ASC";
        }
        $scope.loadCatalog();
    }
    $scope.loadCatalog = function() {
        doRequest("GET", `/bonita/API/extension/posgradosRestGet?url=getUsuarios&p=${$scope.pagina}&c=${$scope.cont}&jsonData=${encodeURIComponent(JSON.stringify($scope.filtroToSend))}`, null, null, null, function(datos, extra) {
            $scope.content = datos.data;
            $scope.value = datos.totalRegistros;
            $scope.loadPaginado();
            $scope.roles = [];

            doRequest("GET", "/bonita/API/identity/role?p=0&c=100&o=displayName%20ASC", null, null, null, function(datos, extra) {
                for(let dato of datos ){
                    if(dato.name === "Admisiones PSG" || dato.name === "Chat PSG" || dato.name === "TI campus PSG" || dato.name === "Comite PSG" || dato.name === "SERUA PSG" ){
                        $scope.roles.push(dato);
                    }
                }
                
                doRequest("GET", "/API/identity/membership?p=0&c=100&f=user_id%3d" + $scope.properties.userid + "&d=role_id&d=group_id", null, null, null, function(datos, extra) {

                    var rolesMine = datos;
                    var isSerua = false;
                    for (let index0 = 0; index0 < rolesMine.length; index0++) {
                        if (rolesMine[index0].role_id.name == 'TI SERUA' || rolesMine[index0].role_id.name == 'ADMINISTRADOR') {
                            isSerua = true;
                        }
                    }
                    if (!isSerua) {
                        var eliminado = false;
                        for (let index = 0; index < $scope.roles.length; index++) {
                            if (eliminado) {
                                index = 0;
                                eliminado = false;
                            }
                            const element = $scope.roles[index];
                            if (element.name != 'PASE DE LISTA' && element.name != 'PSICOLOGO' && element.name != 'PSICOLOGO SUPERVISOR' && element.name != 'ADMISIONES') {
                                $scope.roles.splice(index, 1);
                                eliminado = true;
                                index = 0;
                            }
                        }
                    }

                })
            })

        })

    }
    $scope.onOffUser = function(row) {
        //bonita/API/identity/user/537
        Swal.fire({
            title: `¿Estás seguro que desea ${row.enabled?" desactivar ":" activar "} usuario?`,
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: '#FF5900',
            cancelButtonColor: '#231F20',
            confirmButtonText: 'Continuar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                doRequest("PUT", "/bonita/API/identity/user/" + row.id, {}, { "enabled": !row.enabled + "" }, null, function(datos, extra) {
                    row.enabled = !row.enabled;


                })
            } else {
                row.enabled = row.enabled;
            }
        })
    }
    $scope.filtro = {};
    $scope.group_id = 0;
    $scope.$watch('properties.campusSelected', function(value) {
        if (angular.isDefined(value) && value !== null && angular.isDefined(value.group_id)) {
            ///bonita/API/identity/group/18?n=number_of_users
            $scope.pagina = 0;
            $scope.group_id = value.group_id;
            if ($scope.group_id !== null) {
                var filter = {
                    "columna": "CAMPUS",
                    "operador": "Igual a",
                    "valor": $scope.group_id
                }
                if ($scope.filtroToSend.lstFiltro.length > 0) {
                    var encontrado = false;
                    for (let index = 0; index < $scope.filtroToSend.lstFiltro.length; index++) {
                        const element = $scope.filtroToSend.lstFiltro[index];
                        if (element.columna == "CAMPUS") {
                            $scope.filtroToSend.lstFiltro[index].columna = filter.columna;
                            $scope.filtroToSend.lstFiltro[index].operador = filter.operador;
                            $scope.filtroToSend.lstFiltro[index].valor = $scope.group_id;
                            encontrado = true
                        }
                        if (!encontrado) {
                            $scope.filtroToSend.lstFiltro.push(filter);
                        }

                    }
                } else {
                    $scope.filtroToSend.lstFiltro.push(filter);
                }
            } else {

                if ($scope.filtroToSend.lstFiltro.length > 0) {
                    var encontrado = false;
                    for (let index = 0; index < $scope.filtroToSend.lstFiltro.length; index++) {
                        const element = $scope.filtroToSend.lstFiltro[index];
                        if (element.columna == "CAMPUS") {
                            $scope.filtroToSend.lstFiltro.splice(index, 1);

                        }
                    }
                }

            }

            $scope.loadCatalog();

        }

    });
    /*****PAGINADO */
    $scope.pagina = 0;
    $scope.cont = 20;
    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 10;
    $scope.valorTotal = 10;

    $scope.loadPaginado = function() {
        $scope.valorTotal = Math.ceil($scope.value / $scope.cont);
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
        if ($scope.valorSeleccionado > Math.ceil($scope.value / $scope.cont)) {
            $scope.valorSeleccionado = Math.ceil($scope.value / $scope.cont);
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
                $scope.pagina = ($scope.lstPaginado[i].numero - 1);
                $scope.filtroToSend.offset = (($scope.lstPaginado[i].numero - 1) * $scope.filtroToSend.limit)
            }
        }
        $scope.loadCatalog();
    }
    $scope.sizing = function() {
        $scope.lstPaginado = [];
        $scope.valorSeleccionado = 1;
        $scope.iniciarP = 1;
        $scope.finalP = 10;
        try {
            $scope.filtroToSend.limit = parseInt($scope.cont);
        } catch (exception) {

        }

        $scope.loadCatalog();
    }
    $scope.roles = [];
    $scope.role_id = 0;
    $scope.membership = [];
    /*****PAGINADO */
    $scope.agregarUsuario = function() {
        if ($scope.membership.length > 0) {
            Swal.fire({
                title: `¿Estás seguro que desea crear usuario?`,
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: '#FF5900',
                cancelButtonColor: '#231F20',
                confirmButtonText: 'Continuar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    doRequest("POST", "/bonita/API/identity/user/", {}, $scope.usuario, null, function(datos, extra) {
                        $scope.usuario = datos;
                        $scope.agregarMembreciaUsuario();


                    })
                }
            })
        } else {
            Swal.fire("", 'Agregar por lo menos un rol', "info")
        }


    }
    $scope.agregarMembreciaUsuario = function() {
        if ($scope.membership.length > 0) {
            if ($scope.membership[0].hasOwnProperty("assigned_by_user_id")) {
                $scope.membership.splice(0, 1);
                $scope.agregarMembreciaUsuario();
            } else {
                doRequest("POST", "/bonita/API/identity/membership", {}, {...$scope.membership[0], "user_id": $scope.usuario.id }, null, function(datos, extra) {
                    $scope.membership.splice(0, 1);
                    $scope.agregarMembreciaUsuario();
                })
            }

        } else {
            Swal.fire(($scope.editar) ? "Guardado" : "Agregado", "Usuario " + ($scope.editar) ? "Guardado" : "Agregado" + " correctamente", "success");
            $scope.pantalla = 'principal';
            $scope.loadCatalog();
        }

    }
    $scope.cambiarPantalla = function(pantalla) {
        if (!$scope.editar && pantalla == 'usuario') {
            $scope.usuario = {
                "enabled": "true",
                "firstname": "",
                "lastname": "",
                "password": "",
                "password_confirm": "",
                "userName": ""
            }
            $scope.membership = [];
        }
        $scope.pantalla = pantalla;
    }

    $scope.agregarRol = function() {
        for (let index = 0; index < $scope.roles.length; index++) {
            const element = $scope.roles[index];
            if (element.id == $scope.role_id) {
                $scope.membership.push(angular.copy({...element, "role_id": element.id, "group_id": $scope.group_id, "user_id": $scope.usuario.id }))
                $scope.roles.splice(index, 1);
            }
        }
        $scope.rol_id = 0;
    }
    $scope.eliminarRol = function(row) {
        for (let index = 0; index < $scope.membership.length; index++) {
            const element = $scope.membership[index];
            if (element.role_id = row.role_id) {
                $scope.roles.push(angular.copy(element))
                $scope.membership.splice(index, 1);
            }
        }
    }
    $scope.validateEmail = function(email) {
        var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
    }
    $scope.validatePassword = function(password) {
        var re = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W)/s
        var size = password.length
        return re.test(password) && size >= 8 && size <= 16
    }
    $scope.validatePasswordLC = function(password) {
        var re = /(?=.*[a-z])/s
        return re.test(password)
    }
    $scope.validatePasswordUC = function(password) {
        var re = /(?=.*[A-Z])/s
        return re.test(password)
    }
    $scope.validatePasswordNumber = function(password) {
        var re = /(?=.*\d)/s
        return re.test(password)
    }
    $scope.validatePasswordEspecial = function(password) {
        var re = /(?=.*\W)/s
        return re.test(password)
    }
    $scope.validatePasswordSize = function(password) {
        var size = password.length
        return size >= 8 && size <= 16
    }
    $scope.allLetter = function(inputtxt) {
        //var letters = /^[a-zA-Z\s]*$/;
        var letters = /^[a-zA-ZÀ-ÿ\u00f1\u00d1]+(\s*[a-zA-ZÀ-ÿ\u00f1\u00d1]*)*[a-zA-ZÀ-ÿ\u00f1\u00d1]+$/;
        return letters.test(inputtxt);
    }
    $scope.capitalLetter = function(txt) {
        var re = /^[A-ZÀ-ÿ\u00f1\u00d1]+(\s*[a-zA-ZÀ-ÿ\u00f1\u00d1]*)*[a-zA-ZÀ-ÿ\u00f1\u00d1]\w*$/g
        return re.test(txt);
    }
    $scope.validarTodo = function() {
        return ($scope.validateEmail($scope.usuario.userName) && ($scope.validatePassword($scope.usuario.password) && ($scope.usuario.password == $scope.usuario.password_confirm) || $scope.editar) &&
            $scope.allLetter($scope.usuario.firstname) && $scope.allLetter($scope.usuario.lastname) && $scope.capitalLetter($scope.usuario.firstname) && $scope.capitalLetter($scope.usuario.lastname))
    }
    $scope.editar = false;
    $scope.editarUsuario = function(row) {
        doRequest("GET", `/bonita/API/identity/membership?p=0&c=10&f=user_id%3d${row.id}&d=role_id&d=group_id`, null, null, null, function(datos, extra) {
            $scope.membership = datos;
            for (let index = 0; index < $scope.membership.length; index++) {
                const element = $scope.membership[index];
                $scope.membership[index].name = element.role_id.name;
                $scope.membership[index].displayName = element.role_id.displayName;
                $scope.membership[index].role_id = element.role_id.id;
                $scope.membership[index].group_id = $scope.group_id;
                for (let i = 0; i < $scope.roles.length; i++) {
                    const element2 = $scope.roles[i];
                    if (element2.id == $scope.membership[index].role_id) {
                        $scope.roles.splice(i, 1);
                    }
                }

            }
            $scope.usuario = row;
            $scope.editar = true;
            $scope.cambiarPantalla('usuario')
        })
    }
    $scope.actualizarUsuario = function() {
        Swal.fire({
            title: `¿Estás seguro que desea actualizar datos de usuario?`,
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: '#FF5900',
            cancelButtonColor: '#231F20',
            confirmButtonText: 'Continuar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                delete $scope.usuario.icon;
                var dataSend = {...$scope.usuario };
                dataSend.enabled = $scope.usuario.enabled + "";
                doRequest("PUT", `/bonita/API/identity/user/${$scope.usuario.id}`, {}, dataSend, null, function(datos, extra) {
                    $scope.agregarMembreciaUsuario();


                })
            }
        })

    }
    $scope.eliminarMembresia = function(row) {
        Swal.fire({
            title: `¿Estás seguro que desea eliminar rol de usuario?`,
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: '#FF5900',
            cancelButtonColor: '#231F20',
            confirmButtonText: 'Continuar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                doRequest("DELETE", `/bonita/API/identity/membership/${$scope.usuario.id}/${$scope.group_id}/${row.role_id}`, {}, null, null, function(datos, extra) {
                    $scope.eliminarRol(row)
                })
            }
        })

    }
    $scope.filterKeyPress = function(columna, press) {
        var aplicado = true;
        for (let index = 0; index < $scope.filtroToSend.lstFiltro.length; index++) {
            const element = $scope.filtroToSend.lstFiltro[index];
            if (element.columna == columna) {
                $scope.filtroToSend.lstFiltro[index].valor = press;
                $scope.filtroToSend.lstFiltro[index].operador = "Que contenga";
                aplicado = false;
            }

        }
        if (aplicado) {
            var obj = { "columna": columna, "operador": "Que contenga", "valor": press }
            $scope.filtroToSend.lstFiltro.push(obj);
        }

        $scope.loadCatalog();
    }
    var hidden = document.getElementsByClassName("oculto");
    hidden[0].classList.add("hidden")
}