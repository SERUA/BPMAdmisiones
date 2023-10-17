function($scope, $http, blockUI, $window) {
    window.mobileCheck = function() {
        let check = false;
        (function(a) { if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) check = true; })(navigator.userAgent || navigator.vendor || window.opera);
        return check;
    };
    $scope.contadorVerificarTask = 0;
    var vm = this;
    $scope.resultado;
    $scope.sesion = [];
    $scope.aplicaciones = [];
    $scope.jsonEntrevista = {};
    $scope.jsonPsicometrico = {};
    $scope.jsonCollage = {};
    scheduler.init('scheduler_here', new Date(), window.mobileCheck() ? "day" : "month");
    scheduler.config.drag_resize = false;
    scheduler.config.drag_create = false;
    scheduler.config.drag_highlight = false;
    scheduler.config.drag_lightbox = false;
    scheduler.config.drag_move = false;
    scheduler.config.drag_resize = false;
    scheduler.config.edit_on_create = false;
    scheduler.config.dblclick_create = false;
    scheduler.config.select = false;
    $scope.fechaSelected = "";
    $scope.asistenciaCollegeBoard = false;
    $scope.asistenciaPsicometrico = false;
    $scope.asistenciaEntrevista = false;

    document.getElementById("dhx_minical_icon").innerHTML = "<i class='glyphicon glyphicon-calendar'></i>"
        /*
            document.getElementsByClassName("dhx_cal_next_button")[0].innerHTML = "<i class='glyphicon glyphicon-arrow-right'></i>";
            document.getElementsByClassName("dhx_cal_prev_button")[0].innerHTML = "<i class='glyphicon glyphicon-arrow-left'></i>";*/

    //Mapeo de la lista de entrevistas
    //scheduler.parse($scope.properties.jsonEntrevistas, "json");

    /*Evento que se ejecuta al dar click!*/
    $scope.sesionid = 0;
    scheduler.attachEvent("onClick", function(id, e) {
        $scope.sesionid = id;
        $scope.getSesionById($scope.sesionid);
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
                if (data.error == "No hay cupo") {
                    Swal.fire(
                        "Sesión sin cupo",
                        "Lamentamos informarle que la sesión que está intentando ingresar se ha quedado sin cupo",
                        'info'
                    )
                    $scope.sesionid = 0;
                    $scope.properties.hideCalendario = false;
                    $scope.getLstSesion();
                }
                if (data.error == "Se encuentra ocupado") {
                    Swal.fire(
                        "Horario ocupado",
                        "Lamentamos informarle que el horario de la entrevista ha seleccionado sido ocupado",
                        'info'
                    )
                    $scope.getSesionById($scope.sesionid);
                }
                if (data.error == "Los psicólogos disponibles para esta sesión son especializados para otra licenciatura") {
                    Swal.fire(
                        "Psicólogo para diferente licenciatura",
                        "Los psicólogos disponibles para esta sesión son especializados para otra licenciatura",
                        'info'
                    )
                    $scope.sesionid = 0;
                    $scope.properties.hideCalendario = false;
                    $scope.getLstSesion();
                }
            })
            .finally(function() {
                vm.busy = false;
                blockUI.stop();
            });
    }
    $scope.$watch('properties.isCorrectTask', function(value) {
        if (angular.isDefined(value) && value !== null) {
            if (!value) {
                $scope.getDatosSesionUser();
                $scope.loadAsistencias();
            }
        }
    });

    $scope.$watch('properties.usuario', function(value) {
        if (angular.isDefined(value) && value !== null) {
            if (!$scope.properties.isCorrectTask) {
                $scope.getDatosSesionUser();
                $scope.loadAsistencias();
            }
        }
    });

    $scope.getDatosSesionUser = function() {
        doRequest("GET", "/bonita/API/extension/AnahuacRestGet?url=getDatosSesionUsername&p=0&c=10&username=" + $scope.properties.usuario[0].correoElectronico, null, null, null, function(data) {

            for (let index = 0; index < data.data.length; index++) {
                const element = data.data[index];
                $scope.sesion.nombre = element.snombre;
                $scope.sesion.descripcion = element.sdescripcion;
                if (element.descripcion == "Examen de aptitudes y conocimientos") {
                    $scope.jsonCollage.nombre = element.pnombre;
                    $scope.jsonCollage.descripcion = element.pdescripcion;
                    $scope.jsonCollage.aplicacion = element.aplicacion;
                    $scope.jsonCollage.horario = element.horario;
                    $scope.jsonCollage.lugar = element.lugar;
                    $scope.jsonCollage.online = element.online;
                    $scope.jsonCollage.municipio = element.municipio;
                    $scope.jsonCollage.colonia = element.colonia;
                    $scope.jsonCollage.codigo_postal = element.codigo_postal;
                    $scope.jsonCollage.calle = element.calle;
                    $scope.jsonCollage.numero_int = element.numero_int;
                }
                if (element.descripcion == "Examen Psicométrico") {
                    $scope.jsonPsicometrico.nombre = element.pnombre;
                    $scope.jsonPsicometrico.descripcion = element.pdescripcion;
                    $scope.jsonPsicometrico.aplicacion = element.aplicacion;
                    $scope.jsonPsicometrico.horario = element.horario;
                    $scope.jsonPsicometrico.lugar = element.lugar;
                    $scope.jsonPsicometrico.online = element.online;
                    $scope.jsonPsicometrico.municipio = element.municipio;
                    $scope.jsonPsicometrico.colonia = element.colonia;
                    $scope.jsonPsicometrico.codigo_postal = element.codigo_postal;
                    $scope.jsonPsicometrico.calle = element.calle;
                    $scope.jsonPsicometrico.numero_int = element.numero_int;
                }
                if (element.descripcion == "Entrevista") {
                    $scope.jsonEntrevista.nombre = element.pnombre;
                    $scope.jsonEntrevista.descripcion = element.pdescripcion;
                    $scope.jsonEntrevista.aplicacion = element.aplicacion;
                    $scope.datosSelected.horario = element.horario;
                    $scope.jsonEntrevista.lugar = element.lugar;
                    $scope.jsonEntrevista.online = element.online;
                    $scope.jsonEntrevista.municipio = element.municipio;
                    $scope.jsonEntrevista.colonia = element.colonia;
                    $scope.jsonEntrevista.codigo_postal = element.codigo_postal;
                    $scope.jsonEntrevista.calle = element.calle;
                    $scope.jsonEntrevista.numero_int = element.numero_int;
                    $scope.entrevistaSelected = true;

                }
            }
        })
    }
    $scope.$watch('properties.value', function(value) {
        if (angular.isDefined(value) && value !== null) {
            var items = $scope.properties.availableValues;
            if (Array.isArray(items)) {
                var foundItem = ctrl.findSelectedItem(items);
                ctrl.setSelectedValue(foundItem);
            }
        }
    });


    $scope.getSesionById = function(id) {
        doRequest("GET", "/bonita/API/extension/AnahuacRestGet?url=getSesionAspirante&p=0&c=10&sessionid=" + id, null, $scope.dataToSend, null, function(datos, extra) {
            $scope.sesion = datos.data[0]
            $scope.sesion_aspirante.sesiones_pid = $scope.sesion.persistenceId;
            $scope.jsonEntrevista = { "psicologos": [{ "lstFechasDisponibles": [] }] }
            for (var i = 0; i < $scope.sesion.pruebas.length; i++) {
                if ($scope.sesion.pruebas[i].tipo.descripcion == 'Entrevista') {
                    $scope.jsonEntrevista = { "psicologos": [{ "lstFechasDisponibles": [] }] }
                } else if ($scope.sesion.pruebas[i].tipo.descripcion == 'Examen Psicométrico') {
                    $scope.jsonPsicometrico = $scope.sesion.pruebas[i];
                } else if ($scope.sesion.pruebas[i].tipo.descripcion == 'Examen de aptitudes y conocimientos') {
                    $scope.jsonCollage = $scope.sesion.pruebas[i];
                }
            }
        })
    }
    $scope.getPruebasFechas = function() {
        doRequest("GET", "/bonita/API/extension/AnahuacRestGet?url=getPruebasFechas&p=0&c=10&sessionid=" + $scope.sesion.persistenceId + "&aspirante=" + $scope.properties.usuario[0].correoElectronico, null, {}, null, function(datos, extra) {
            $scope.aplicaciones = datos.data
            $scope.modalConfirmar();

        })
    }
    $scope.selectFecha = function(ap) {
        $scope.fechaSelected = ap.aplicacion.split("-")[2] + "-" + ap.aplicacion.split("-")[1] + "-" + ap.aplicacion.split("-")[0];
        doRequest("GET", "/bonita/API/extension/AnahuacRestGet?url=getHorarios&p=0&c=10&prueba_pid=" + ap.persistenceId + "&sessionid=" + $scope.sesion.persistenceId + "&correoAspirante=" + $scope.properties.usuario[0].correoElectronico, null, {}, null, function(datos, extra) {
            $scope.jsonEntrevista.nombre = datos.data[0].nombre;
            $scope.jsonEntrevista.descripcion = datos.data[0].descripcion;
            $scope.jsonEntrevista.aplicacion = datos.data[0].aplicacion;
            $scope.jsonEntrevista.lugar = datos.data[0].lugar;
            $scope.jsonEntrevista.online = datos.data[0].online;
            $scope.jsonEntrevista.municipio = datos.data[0].municipio;
            $scope.jsonEntrevista.colonia = datos.data[0].colonia;
            $scope.jsonEntrevista.codigo_postal = datos.data[0].codigo_postal;
            $scope.jsonEntrevista.calle = datos.data[0].calle;
            $scope.jsonEntrevista.numero_int = datos.data[0].numero_int;
            $scope.jsonEntrevista.psicologos = datos.data[0].psicologos;
            for (let index = 1; index < datos.data[0].psicologos.length; index++) {
                const element = datos.data[0].psicologos[index];
                for (let index2 = 0; index2 < element.lstFechasDisponibles.length; index2++) {
                    const element2 = element.lstFechasDisponibles[index2];
                    $scope.jsonEntrevista.psicologos[0].lstFechasDisponibles.push(element2)
                }


            }
        })
    }
    $scope.getLstSesion = function() {
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
            "valor": $scope.properties.detalleSolicitud[0].catResidencia.clave
        }
        $scope.dataToSend.lstFiltro.push(angular.copy(filtro))
        if ($scope.properties.usuario[0].catEstado != null) {
            filtro = {
                "columna": "ESTADO",
                "operador": "Igual a",
                "valor": $scope.properties.usuario[0].catEstado.persistenceId
            }
            $scope.dataToSend.lstFiltro.push(angular.copy(filtro))
        }

        filtro = {
            "columna": "PAIS",
            "operador": "Igual a",
            "valor": $scope.properties.usuario[0].catPais.persistenceId
        }
        $scope.dataToSend.lstFiltro.push(angular.copy(filtro))

        filtro = {
            "columna": "CIUDAD",
            "operador": "Igual a",
            "valor": ($scope.properties.usuario[0].ciudadExamen == null) ? 0 : $scope.properties.usuario[0].ciudadExamen.persistenceId
        }
        $scope.dataToSend.lstFiltro.push(angular.copy(filtro))
        filtro = {
            "columna": "PERIODO",
            "operador": "Igual a",
            "valor": $scope.properties.usuario[0].catPeriodo.persistenceId
        }
        $scope.dataToSend.lstFiltro.push(angular.copy(filtro))

        filtro = {
            "columna": "EMAIL",
            "operador": "Igual a",
            "valor": $scope.properties.usuario[0].correoElectronico
        }
        $scope.dataToSend.lstFiltro.push(angular.copy(filtro))
        doRequest("POST", "/bonita/API/extension/AnahuacRest?url=getSesionesCalendarioAspirante&p=0&c=10&fecha=" + fechaReporte + "&isMedicina=" + $scope.properties.isMedicina, null, $scope.dataToSend, null, function(datos, extra) {
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

            for (let index = 0; index < value[0].links.length; index++) {
                const element = value[0].links[index];
                if (element.rel == "catResidencia") {
                    doRequest("GET", element.href, null, null, null, function(datos, extra) {
                        $scope.properties.detalleSolicitud[0].catResidencia = datos;
                        $scope.getLstSesion();
                    })
                }

            }
            if ($scope.properties.isMedicina !== null) {
                $scope.getLstSesion();
            }
        }
    });
    $scope.$watch('properties.isMedicina', function(value) {
        if (angular.isDefined(value) && value !== null && $scope.properties.detalleSolicitud !== null) {
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

    $scope.closeModal = function() {
        $("#modalConfirmar").modal('hide')
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
        if ($scope.sesion_aspirante.responsabledisponible_pid == 0 && !$scope.asistenciaEntrevista) {
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
                title: `¿Estás seguro que deseas guardar la sesión ${$scope.sesion.nombre + " "} seleccionada?`,
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Continuar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    $scope.sesion_aspirante.username = $scope.properties.usuario[0].correoElectronico
                    doRequest("POST", "/bonita/API/extension/AnahuacRest?url=insertSesionAspirante&p=0&c=10", null, $scope.sesion_aspirante, null, function(datos, extra) {
                        doRequest("GET", `/API/extension/RegistroRest?url=humanTask&caseid=${$scope.properties.usuario[0].caseId}&f=isFailed%3dfalse`, null, null, null, function(datos, extra) {
                            var taskId = 0;
                            for (let index = 0; index < datos.length; index++) {
                                const element = datos[index];
                                if (element.name == "Seleccionar cita") {
                                    taskId = element.id;
                                    break;
                                }
                            }
                            try {
                                console.log("isPeriodoVencido?")
                                var fecha = new Date($scope.properties.solicitudDeAdmision.catPeriodo.fechaFin.slice(0, 10));
                                if (fecha < new Date()) {
                                    Swal.fire("¡Periodo vencido!", "Recuerda que el periodo de ingreso que seleccionaste ha vencido y debes actualizarlo, contacta a tu asesor o a través del chat", "warning")
                                        .then((value) => {
                                            doRequest("PUT", `../API/extension/RegistroPut?url=changeTaskId&taskId=${taskId}`, null, {}, null, function(datos, extra) {
                                                doRequest("POST", `/API/bpm/userTask/${taskId}/execution`, null, {}, null, function(datos, extra) {

                                                    Swal.fire({
                                                            icon: 'success',
                                                            title: 'Correcto',
                                                            text: `Sesión ${$scope.sesion.nombre+" "} guardada correctamente`,
                                                        })
                                                        ///bonita/portal/resource/app/aspirante/solicitud_iniciada/content/?app=aspirante
                                                        //$window.location.assign("/bonita/portal/resource/app/aspirante/confirmacion_credencial/content/?app=aspirante");
                                                        //window.top.location.href = '/bonita/apps/aspirante/nueva_solicitud/';
                                                    $scope.VerificarTask();
                                                })
                                            });

                                        });
                                } else {
                                    doRequest("PUT", `../API/extension/RegistroPut?url=changeTaskId&taskId=${taskId}`, null, {}, null, function(datos, extra) {
                                        doRequest("POST", `/API/bpm/userTask/${taskId}/execution`, null, {}, null, function(datos, extra) {

                                            Swal.fire({
                                                    icon: 'success',
                                                    title: 'Correcto',
                                                    text: `Sesión ${$scope.sesion.nombre+" "} guardada correctamente`,
                                                })
                                                ///bonita/portal/resource/app/aspirante/solicitud_iniciada/content/?app=aspirante
                                                //$window.location.assign("/bonita/portal/resource/app/aspirante/confirmacion_credencial/content/?app=aspirante");
                                                //window.top.location.href = '/bonita/apps/aspirante/nueva_solicitud/';
                                            $scope.VerificarTask();
                                        })
                                    });

                                }
                            } catch (e) {
                                doRequest("PUT", `../API/extension/RegistroPut?url=changeTaskId&taskId=${taskId}`, null, {}, null, function(datos, extra) {
                                    doRequest("POST", `/API/bpm/userTask/${taskId}/execution`, null, {}, null, function(datos, extra) {

                                        Swal.fire({
                                                icon: 'success',
                                                title: 'Correcto',
                                                text: `Sesión ${$scope.sesion.nombre+" "} guardada correctamente`,
                                            })
                                            ///bonita/portal/resource/app/aspirante/solicitud_iniciada/content/?app=aspirante
                                            //$window.location.assign("/bonita/portal/resource/app/aspirante/confirmacion_credencial/content/?app=aspirante");
                                            //window.top.location.href = '/bonita/apps/aspirante/nueva_solicitud/';
                                        $scope.VerificarTask();
                                    })
                                });

                            }

                        })
                    })

                } else {

                }
            })
        }

    }

    $scope.$watch('properties.usuario', function(value) {
        if ($scope.properties.usuario != undefined) {
            console.log($scope.properties.usuario);
            $scope.loadAsistencias();
        }
    });


    $scope.loadAsistencias = function() {
        $scope.asistenciaCollegeBoard = false;
        $scope.asistenciaPsicometrico = false;
        $scope.asistenciaEntrevista = false;
        $scope.loadAsistenciaCollegeBoard();
    }

    $scope.loadAsistenciaCollegeBoard = function() {
        doRequest("GET", "../API/extension/RegistroRest?url=caseVariable&caseid=" + $scope.properties.usuario[0].caseId + "&name=asistenciaCollegeBoard", null, null, null, function(datos, extra) {
            $scope.asistenciaCollegeBoard = (datos.value === "true");
            $scope.loadAsistenciaPsicometrico();
        })
    }
    $scope.loadAsistenciaPsicometrico = function() {
        doRequest("GET", "../API/extension/RegistroRest?url=caseVariable&caseid=" + $scope.properties.usuario[0].caseId + "&name=asistenciaPsicometrico", null, null, null, function(datos, extra) {
            $scope.asistenciaPsicometrico = (datos.value === "true");
            $scope.loadAsistenciaEntrevista();
        })
    }
    $scope.loadAsistenciaEntrevista = function() {
        doRequest("GET", "../API/extension/RegistroRest?url=caseVariable&caseid=" + $scope.properties.usuario[0].caseId + "&name=asistenciaEntrevista", null, null, null, function(datos, extra) {
            $scope.asistenciaEntrevista = (datos.value === "true");
        })
    }
    $scope.VerificarTask = function() {
        doRequest("GET", "/API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid=" + $scope.properties.usuario[0].caseId + "&fstate=ready", null, null, null, function(datos, extra) {

            if ($scope.contadorVerificarTask <= 100) {
                var isSeleccionar = false;
                for (let index = 0; index < datos.length; index++) {
                    const element = datos[index];
                    if (element.name == "Seleccionar cita") {
                        isSeleccionar = true;
                    }
                }
                if (datos !== undefined && datos.length > 0 && !isSeleccionar) {
                    let ipBonita = window.location.protocol + "//" + window.location.host + "";
                    let url = "";
                    url = ipBonita + "/bonita/apps/aspirante/nueva_solicitud/";
                    top.location.href = url;
                } else {
                    blockUI.start();
                    setTimeout(function() {
                        blockUI.stop();
                        $scope.VerificarTask();
                    }, 1000);

                    $scope.contadorVerificarTask = $scope.contadorVerificarTask + 1;
                }

            }
        })
    }
}