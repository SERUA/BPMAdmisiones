function($scope, $http, blockUI, $window) {
    window.mobileCheck = function () {
        let check = false;
        (function (a) { if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) check = true; })(navigator.userAgent || navigator.vendor || window.opera);
        return check;
    };

    $scope.seleccionarHorario = function (_horario){
        $scope.horario = _horario;
        let horario = _horario.hora_inicio + " - " + _horario.hora_fin
        
        swal({
            "title": "Confirmación",
            "text": "¿Estás seguro que deseas seeccionar tu cita para el día " + fechaAStringConFormato($scope.seleccionada.start_date) + " en el horario " + horario + "?",
            icon: "warning",
            buttons: [
                'Cancelar',
                'Aceptar'
            ],
        }).then(function (isConfirm) {
            if (isConfirm) {
                swal("Ok", "Entrevista asignada con éxito. ", "success").then(function () {
                    $scope.properties.cita = {
                        "cita_horario": angular.copy($scope.horario)
                    };
                    
                    $scope.$apply();
                    $("#modalConfirmar").modal("hide");
                })
            }
        })
    }

    function fechaAStringConFormato(fecha) {
        var dia = fecha.getDate();
        var mes = fecha.getMonth() + 1;
        var ano = fecha.getFullYear();
        dia = dia < 10 ? '0' + dia : dia;
        mes = mes < 10 ? '0' + mes : mes;
      
        return dia + '/' + mes + '/' + ano;
    }

    function generarEventosPorMes(mes, ano) {
        var eventos = [];
        var ultimoDia = new Date(ano, mes, 0).getDate();

        for (var dia = 1; dia <= ultimoDia; dia++) {

            var fechaInicio = mes + "/" + (dia < 10 ? "0" + dia : dia) + "/ " + ano + " 7:00";
            var fechaFin = mes + "/" + (dia < 10 ? "0" + dia : dia) + "/ " + ano + " 21:00";

            var fecha = mes + '/' + dia + '/' + ano;
            var fechaHoy = new Date();
            var fechaGenerada = new Date(fecha);

            if (fechaGenerada >= fechaHoy) {
                var evento = {
                    "end_date": fechaFin,
                    "id": dia,
                    "color": "#ff5900",
                    "text": `DÍA ${dia}`,
                    "start_date": fechaInicio
                };
                eventos.push(evento);
            }
        }

        return eventos;
    }

    $scope.contadorVerificarTask = 0;
    var vm = this;
    $scope.resultado;
    $scope.sesion = [];
    $scope.aplicaciones = [];
    $scope.jsonEntrevista = {};
    $scope.jsonPsicometrico = {};
    $scope.jsonCollage = {};
    $scope.fechaSelected = "";
    $scope.asistenciaCollegeBoard = false;
    $scope.asistenciaPsicometrico = false;
    $scope.asistenciaEntrevista = false;

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
    document.getElementById("dhx_minical_icon").innerHTML = "<i class='glyphicon glyphicon-calendar'></i>";

    $scope.sesionid = 0;

    scheduler.attachEvent("onClick", function (id, e) {
        buscarEntrevista(id);
        $scope.$apply();

        // $("#modalConfirmar").modal("show");
    })

    function buscarEntrevista(_id) {
        for (let evento of eventos) {
            if (evento.id === parseInt(_id)) {
                $scope.seleccionada = angular.copy(evento);
                let url = "../API/extension/posgradosRestGet?url=getHorariosByIdSesionV2&idSesion=" + _id;
                $http.get(url).success(function(_data){
                    $scope.entrevistas = angular.copy(_data);
                    $scope.$apply();
                    $("#modalConfirmar").modal("show");
                })
            }
        }
    }
    var eventos = [];

    function loadFechas(){
        let url = "../API/extension/posgradosRestGet?url=getSesionesV1";
        
        $http.get(url).success(function(_data){
            if(_data){
                eventos = construirEventos(_data);
                scheduler.clearAll();
                scheduler.parse(eventos, "json");
            }
        }).error(function(){

        });
    }

    loadFechas();

    function construirEventos(_sesiones){
        let eventos = []
        for(let sesion of _sesiones){
            var evento = {
                "end_date": sesion.fecha_entrevista + " 21:00",
                "id": sesion.persistenceId,
                "color": "#ff5900",
                "text": sesion.nombre,
                "start_date": sesion.fecha_entrevista + " 7:00"
            };

            eventos.push(evento);
        }

        return eventos;
    }

    // $scope.entrevistaSelected = false;
    // $scope.sesion_aspirante = {
    //     "persistenceId": 0,
    //     "persistenceVersion": 0,
    //     "responsabledisponible_pid": 0,
    //     "sesiones_pid": 0,
    //     "username": ""
    // }

    // $scope.horaInicio = new Date();
    // $scope.horaFin = new Date();
    // $scope.fechaCalendario = "";
    // $scope.resultadoMostrarAudiencia = {};
    // $scope.resultadoMostrarAudiencia.idSala = 0;
    // $scope.resultadoMostrarAudiencia.idTipo_Audiencia = 0;
    // $scope.resultadoMostrarAudiencia.text = "";
    // $scope.resultadoMostrarAudiencia.id = 0;
    // $scope.eliminado = "false";

    $scope.show_minical = function () {
        if (scheduler.isCalendarVisible()) {
            scheduler.destroyCalendar();
        } else {
            scheduler.renderCalendar({
                position: "dhx_minical_icon",
                date: scheduler._date,
                navigation: true,
                handler: function (date, calendar) {
                    scheduler.setCurrentView(date);
                    scheduler.destroyCalendar()
                }
            });
        }
    }
    
    var selectorFecha = $("div.dhx_cal_date").text();
    var fechaReporte = convertidorFechaCalendario(selectorFecha);

    $('div.dhx_cal_date').bind("DOMSubtreeModified", function () {
        selectorFecha = $("div.dhx_cal_date").text();
        fechaReporte = convertidorFechaCalendario(selectorFecha);
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

    // $scope.modalConfirmar = function () {
    //     $("#modalConfirmar").modal('show')
    // }

    // $scope.closeModal = function () {
    //     $("#modalConfirmar").modal('hide')
    // }

    // $scope.modalEntrevista = function () {
    //     $("#modalEntrevista").modal('show')
    // }

    $scope.setShowCalendar = function () {
        $scope.properties.hideCalendario = false;
    }
}