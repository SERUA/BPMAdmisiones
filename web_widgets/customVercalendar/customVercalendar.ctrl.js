function($scope, $http, blockUI, $window) {
    var vm = this;
    $scope.resultado;
    $scope.sesion = [];
    $scope.jsonEntrevista = {};
    $scope.jsonPsicometrico = {};
    $scope.jsonCollage = {};
    scheduler.init('scheduler_here', new Date(), "month");
    scheduler.config.drag_resize = false;
    scheduler.config.drag_create = false;
    scheduler.config.drag_highlight = false;
    scheduler.config.drag_lightbox = false;
    scheduler.config.drag_move = false;
    scheduler.config.drag_resize = false;
    scheduler.config.edit_on_create = false;
    scheduler.config.dblclick_create = false;
    scheduler.config.select = false;

    document.getElementById("dhx_minical_icon").innerHTML = "<i class='glyphicon glyphicon-calendar'></i>"
        /*
            document.getElementsByClassName("dhx_cal_next_button")[0].innerHTML = "<i class='glyphicon glyphicon-arrow-right'></i>";
            document.getElementsByClassName("dhx_cal_prev_button")[0].innerHTML = "<i class='glyphicon glyphicon-arrow-left'></i>";*/

    //Mapeo de la lista de entrevistas
    //scheduler.parse($scope.properties.jsonEntrevistas, "json");

    /*Evento que se ejecuta al dar click!*/
    scheduler.attachEvent("onClick", function(id, e) {
        debugger;

        $scope.getSesionById(id);
        $scope.properties.hideCalendario = true;
        $scope.$apply();
    })
    $scope.entrevistaSelected = false;
    $scope.sesion_aspirante = {
            "persistenceId": 0,
            "persistenceVersion": 0,
            "responsabledisponible_pid": 0,
            "sesiones_pid": 0,
            "username": ""
        }
        /**
         * Execute a get/post request to an URL
         * It also bind custom data from success|error to a data
         * @return {void}
         */
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
            })
            .finally(function() {
                vm.busy = false;
                blockUI.stop();
            });
    }



    $scope.getSesionById = function(id) {
        doRequest("GET", "/bonita/API/extension/AnahuacRestGet?url=getSesion&p=0&c=10&sessionid=" + id, null, $scope.dataToSend, null, function(datos, extra) {
            $scope.sesion = datos.data[0]
            $scope.sesion_aspirante.sesiones_pid = $scope.sesion.persistenceId;
            debugger
            for (var i = 0; i < $scope.sesion.pruebas.length; i++) {
                if ($scope.sesion.pruebas[i].tipo.descripcion == 'Entrevista') {
                    $scope.jsonEntrevista = $scope.sesion.pruebas[i];
                } else if ($scope.sesion.pruebas[i].tipo.descripcion == 'Examen Psicométrico') {
                    $scope.jsonPsicometrico = $scope.sesion.pruebas[i];
                } else if ($scope.sesion.pruebas[i].tipo.descripcion == 'College Board') {
                    $scope.jsonCollage = $scope.sesion.pruebas[i];
                }
            }
        })
    }
    $scope.getLstSesion = function() {
        debugger
        var filtro = {
            "columna": "CAMPUS",
            "operador": "Igual a",
            "valor": $scope.properties.usuario[0].catCampus.persistenceId
        }
        $scope.dataToSend = {
            "lstFiltro": []
        }
        $scope.dataToSend.lstFiltro.push(angular.copy(filtro))
        filtro = {
            "columna": "BACHILLERATO",
            "operador": "Igual a",
            "valor": $scope.properties.usuario[0].catBachilleratos.persistenceId
        }
        $scope.dataToSend.lstFiltro.push(angular.copy(filtro))
        filtro = {
            "columna": "RESIDENCIA",
            "operador": "Parecido",
            "valor": $scope.properties.detalleSolicitud[0].tipoAlumno
        }
        $scope.dataToSend.lstFiltro.push(angular.copy(filtro))
            /*filtro = {
                "columna": "Estado",
                "operador": "Igual a",
                "valor": $scope.properties.detalleSolicitud[0].tipoAlumno
            }
            $scope.dataToSend.lstFiltro.push(angular.copy(filtro))*/
        doRequest("POST", "/bonita/API/extension/AnahuacRest?url=getSesionesCalendario&p=0&c=10&fecha=" + fechaReporte, null, $scope.dataToSend, null, function(datos, extra) {
            scheduler.clearAll();
            scheduler.parse(datos.data, "json");
        })
    }

    $scope.horaInicio = new Date();
    $scope.horaFin = new Date();
    $scope.fechaCalendario = "";
    $scope.resultadoMostrarAudiencia = {};
    $scope.resultadoMostrarAudiencia.idSala = 0;
    $scope.resultadoMostrarAudiencia.idTipo_Audiencia = 0;
    $scope.resultadoMostrarAudiencia.text = "";
    $scope.resultadoMostrarAudiencia.id = 0;
    $scope.eliminado = "false";

    $scope.show_minical = function() {
        if (scheduler.isCalendarVisible()) {
            scheduler.destroyCalendar();
        } else {
            scheduler.renderCalendar({
                position: "dhx_minical_icon",
                date: scheduler._date,
                navigation: true,
                handler: function(date, calendar) {
                    scheduler.setCurrentView(date);
                    scheduler.destroyCalendar()
                }
            });
        }
    }


    var selectorFecha = $("div.dhx_cal_date").text();
    var fechaReporte = convertidorFechaCalendario(selectorFecha);
    //Fución que se ejecuta cada que se cambia el DOM  en la clase dhx_cal_date
    $('div.dhx_cal_date').bind("DOMSubtreeModified", function() {
        selectorFecha = $("div.dhx_cal_date").text();
        fechaReporte = convertidorFechaCalendario(selectorFecha);
        console.log(fechaReporte);
        $scope.getLstSesion();
    });

    $scope.$watch('properties.detalleSolicitud', function(value) {
        if (angular.isDefined(value) && value !== null) {
            $scope.getLstSesion();
        }
    });

    function convertidorFechaCalendario(selectorFecha) {
        var fecha = "";
        var year = selectorFecha.substring(selectorFecha.length - 4);
        if (selectorFecha.includes("Ene")) {
            fecha = year + "-01-01";
        }
        if (selectorFecha.includes("Feb")) {
            fecha = year + "-02-01";
        }
        if (selectorFecha.includes("Mar")) {
            fecha = year + "-03-01";
        }
        if (selectorFecha.includes("Abr")) {
            fecha = year + "-04-01";
        }
        if (selectorFecha.includes("May")) {
            fecha = year + "-05-01";
        }
        if (selectorFecha.includes("Jun")) {
            fecha = year + "-06-01";
        }
        if (selectorFecha.includes("Jul")) {
            fecha = year + "-07-01";
        }
        if (selectorFecha.includes("Ago")) {
            fecha = year + "-08-01";
        }
        if (selectorFecha.includes("Sep")) {
            fecha = year + "-09-01";
        }
        if (selectorFecha.includes("Oct")) {
            fecha = year + "-10-01";
        }
        if (selectorFecha.includes("Nov")) {
            fecha = year + "-11-01";
        }
        if (selectorFecha.includes("Dic")) {
            fecha = year + "-12-01";
        }

        return fecha;
    }

    $scope.modalConfirmar = function() {
        $("#modalConfirmar").modal('show')
    }

    $scope.modalEntrevista = function() {
        $("#modalEntrevista").modal('show')
    }

    $scope.setShowCalendar = function() {
        $scope.properties.hideCalendario = false;
    }
    $scope.validarSesion = function() {
        var error = false;
        var sweet = { "titulo": "", "texto": "" }
        if ($scope.sesion_aspirante.responsabledisponible_pid == 0) {
            error = true;
            sweet.titulo = "Incorrecto";
            sweet.texto = 'Favor de seleccionar psicólogo para entrevista'
        }
        if (error) {
            Swal.fire(
                sweet.titulo,
                sweet.texto,
                'info'
            )
        }
        return error;
    }
    $scope.datosSelected = {};
    $scope.selectEntrevista = function(disponible) {
        if (disponible.disponible && !disponible.ocupado) {
            $scope.entrevistaSelected = true;
            $scope.datosSelected = angular.copy(disponible);
            $scope.sesion_aspirante.responsabledisponible_pid = disponible.persistenceId;
            $scope.sesion_aspirante.username = $scope.properties.usuario[0].correoElectronico;
            $("#modalConfirmar").modal("hide")
        } else {
            Swal.fire(
                "",
                "Favor de seleccionar horario disponible",
                'info'
            )
        }

    }
    $scope.insertSesionAspirante = function() {
        if (!$scope.validarSesion()) {
            Swal.fire({
                title: `¿Está seguro que desea gurardar la sesión ${$scope.sesion.nombre + " "} seleccionada?`,
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Continuar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    doRequest("POST", "/bonita/API/extension/AnahuacRest?url=insertSesionAspirante&p=0&c=10", null, $scope.sesion_aspirante, null, function(datos, extra) {
                        doRequest("GET",`/API/bpm/task?p=0&c=10&f=caseId%3d${$scope.properties.usuario[0].caseId}&f=isFailed%3dfalse`,null,null,null,function(datos,extra){
                            var taskId=0;
                            for (let index = 0; index < datos.length; index++) {
                                const element = datos[index];
                                if(element.name=="Seleccionar cita"){
                                    taskId=element.id;
                                    break;
                                }
                            }
                            
                            doRequest("POST", `/API/bpm/userTask/${taskId}/execution?assign=true`, null, {}, null, function(datos, extra) {

                                Swal.fire({
                                    icon: 'success',
                                    title: 'Correcto',
                                    text: `Sesion ${$scope.sesion.nombre} guardada correctamente`,
                                })
                                ///bonita/portal/resource/app/aspirante/solicitud_iniciada/content/?app=aspirante
                                $window.location.assign("/bonita/portal/resource/app/aspirante/solicitud_iniciada/content/?app=aspirante");
                            })
                        })
                    })

                } else {

                }
            })
        }

    }
}