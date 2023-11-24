function($scope, $http, blockUI, $window) {
    window.mobileCheck = function () {
        let check = false;
        (function (a) { if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) check = true; })(navigator.userAgent || navigator.vendor || window.opera);
        return check;
    };
    
    $scope.lstDuracion = [15, 30, 60];
    $scope.responsables = [];
    
    $scope.guardarSesion = function(_sesion){
        let url = !_sesion.persistenceId ? "/API/extension/posgradosRest?url=insertSesion" : "/API/extension/posgradosRest?url=updateSesion";

        let dataToSend = angular.copy($scope.sesion);
        dataToSend["responsables"] = angular.copy($scope.responsables);
        dataToSend["horarios"] = angular.copy($scope.horarios);
        
        $http.post(url, dataToSend).success(function(_data){
            $scope.sesion = {
                "nombre": "", 
                "descripcion_entrevista": "",
                "fecha_entrevista": "",
                "duracion_entrevista_minutos": "",
                "campus": "", 
                "horarios": [],
                "responsables": []
            }
            // generarHoras();
            $scope.navVar = "calendario";
            swal("Ok", "Sesión guardada correctamente.", "success");
            loadFechas();
        }).error(function(data){
            swal("Algo ha fallado", data.error, "warning");
        });
    }
    
    $scope.navVar = "calendario";
    
    $scope.initSesion = function(){
        $scope.navVar = "sesion";
        
        $scope.sesion = {
            "nombre": "", 
            "descripcion_entrevista": "",
            "fecha_entrevista": "",
            "duracion_entrevista_minutos": "",
            "campus": $scope.idcampus
        }
    }
    
    function fechaAStringConFormato(fecha) {
        var dia = fecha.getDate();
        var mes = fecha.getMonth() + 1;
        var ano = fecha.getFullYear();
        dia = dia < 10 ? '0' + dia : dia;
        mes = mes < 10 ? '0' + mes : mes;
      
        return dia + '/' + mes + '/' + ano;
    }

    var vm = this;
    $scope.resultado;
    $scope.sesion = [];
    $scope.aplicaciones = [];
    $scope.jsonEntrevista = {};
    $scope.fechaSelected = "";

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
    })

    function buscarEntrevista(_id) {
        let encontrado = false;
        for (let evento of eventos) {
            if (evento.id === parseInt(_id)) {
                
                for(let sesion of $scope.sesiones){
                    if(evento.id === sesion.persistenceId){
                        $scope.sesion = sesion;
                        break;
                    }
                }

                let url = "../API/extension/posgradosRestGet?url=getHorariosByIdSesion&idSesion=" + _id;

                $http.get(url).success(function(_data){
                    $scope.entrevistas = angular.copy(_data);
                    $scope.$apply();
                });
                
                
                $scope.navVar = "sesion";
                encontrado = true;
                generarHoras(30);
                break;
            }
        }

        if(!encontrado){
            swal("Evento no encontrado", "", "warning");
        }
    }

    var eventos = [];

    function loadFechas(){
        let url = "../API/extension/posgradosRestGet?url=getSesionesV2&idcampus=" + $scope.idcampus;
        
        $http.get(url).success(function(_data){
            if(_data){
                $scope.sesiones = _data;
                eventos = construirEventos(_data);
                scheduler.clearAll();
                scheduler.parse(eventos, "json");
            }
        }).error(function(){

        });
    }
    
    $scope.getInfoResponsable = function(_usuario){
        $scope.usuario = _usuario;
        $("#modalhorarios").modal("show");
    }

    function construirEventos(_sesiones){
        let eventos = [];
        for(let sesion of _sesiones){
            var evento = {
                "end_date": sesion.fecha_entrevista_back + " 21:00",
                "id": sesion.persistenceId,
                "color": "#ff5900",
                "text": sesion.nombre,
                "start_date": sesion.fecha_entrevista_back + " 7:00"
            };

            eventos.push(evento);
        }

        return eventos;
    }

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

    $scope.setShowCalendar = function () {
        $scope.properties.hideCalendario = false;
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

    $scope.$watch("properties.idUsuario", function(newValue, oldValue) {
        if (newValue !== undefined) {
            var req = {
                method: "GET",
                url: `/API/identity/membership?p=0&c=100&f=user_id%3d${$scope.properties.idUsuario}&d=role_id&d=group_id`
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

    $scope.filtroCampus = "";

    $scope.addFilter = function() {
        $scope.idcampus = $scope.filtroCampus;

        for(let item of $scope.lstCampus){
            if(item.descripcion === $scope.filtroCampus){
                $scope.idcampus = item.persistenceid;
                break;
            }
        }

        loadFechas();
    }
  
    $scope.getCatCampus = function() {
        var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.posgrados.catalog.PSGRCatCampus?q=getCat&p=0&c=100&f=eliminado=false"
        };
  
        return $http(req)
        .success(function(data, status) {
            $scope.lstCampus = [];
            for (var index in data) {
                $scope.lstCampus.push({
                    "descripcion": data[index].descripcion,
                    "valor": data[index].grupo_bonita,
                    "persistenceid":  data[index].persistenceId
                })
            }
        })
        .error(function(data, status) {
            console.error(data);
        });
    }

    $scope.getCatCampus();
    
    $scope.generarHoras = function(_duracion){
        generarHoras(_duracion);
    }
    
    function generarHoras(_duracion) {
        const horasList = [];
        $scope.horarios = [];
        const formatoHora = new Intl.DateTimeFormat('en-US', {
            hour: 'numeric',
            minute: 'numeric',
            hour12: false
        });
    
        let hora = new Date();
        hora.setHours(9, 0, 0, 0); 
    
        while (hora.getHours() <= 19) {
            let horario = {"hora_inicio":"", "hora_fin":""}
            horario["hora_inicio"] = formatoHora.format(hora);
            hora.setMinutes(hora.getMinutes() + _duracion);
            horario["hora_fin"] = formatoHora.format(hora);
            horario["cita_entrevista"] = {
                "persistenceId": $scope.sesion.persistenceId
            }, 
            horario["responsables"] = [];
            
            $scope.horarios.push(horario);
        }
    }
    
    $scope.$watch("properties.roles", function(){
        if($scope.properties.roles){
            getUsuariosRol();
        } 
    });
    
    function getUsuariosRol(){
        let url = "../API/identity/user?c=50&p=0&f=enabled=true";
        
        for(let rol of $scope.properties.roles){
            url += ("&f=role_id=" + rol.id);
        }
        
        $http.get(url).success(function(_data){
            $scope.usuariosResponsables = angular.copy(_data);
        }).error(function(){
            swal("Error al obtener la lista de usuarios", "", "error");
        })
    }
    
    $scope.agregarResponsable = function(){
        let _usuario = null;
        let encontrado = false;

        for(let usuarioResp of $scope.usuariosResponsables){
            if($scope.nuevoResponsable === usuarioResp.id){
                _usuario = angular.copy(usuarioResp);
            }
        }

        for(let usuario of $scope.responsables){
            if(usuario.responsable_id === _usuario.id){
                encontrado = true;
            }
        }

        if(!encontrado){
            let nuevoUsuario = {
                "responsable_id": _usuario.id,
                "horario": null, 
                "cita_entrevista": {
                    "persistenceId": $scope.sesion.persistenceId
                },
                "ocupado": false,
                "disponible_resp": true,
                "nombre": _usuario.firstname + " " + _usuario.lastname
            }

            $scope.responsables.push(nuevoUsuario);

            for(let horario of $scope.horarios){
                if(horario.persistenceId){
                    nuevoUsuario["horario"] = {
                        "persistenceId": horario.persistenceId
                    }
                }
                
                horario["responsables"].push(angular.copy(nuevoUsuario));
            }
        } else {
            $scope.nuevoResponsable = null;
        }
    }

    $scope.deleteResponsable = function(_usuario){
        debugger;
        for(let horario of $scope.horarios){
            for(let responsable of horario.responsables){
                if(_usuario.id === responsable.responsable_id){
                    horario.responsables.splice(horario.responsables.indexOf(responsable), 1);
                }
            }
        }
        
        for(let responsable of $scope.responsables){
            if(responsable.responsable_id === _usuario.id){
                $scope.responsables.splice($scope.responsables.indexOf(responsable));
            }
        }
    }

    $scope.setDisponible = function(_disponible, _index, _horarioIndex){
        $scope.horarios[_horarioIndex].responsables[_index].disponible_resp = _disponible;
    }

    $scope.validate = function(){
        let valid = true;
        let mensaje = "";
        let titulo = "¡Atención!";

        if(!$scope.sesion.nombre){ 
            mensaje = "Nombre de la sesión no debe ir vacío";
            valid = false;
        } else if(!$scope.sesion.descripcion_entrevista){ 
            mensaje = "Descripción de la sesión no debe ir vacío";
            valid = false;
        }  else if(!$scope.sesion.fecha_entrevista){ 
            mensaje = "Fecha de la sesión no debe ir vacío";
            valid = false;
        } else if(!$scope.sesion.duracion_entrevista_minutos){ 
            mensaje = "Duración de las entrevistas en minutos no debe ir vacío";
            valid = false;
        } else if(!$scope.responsables){ 
            mensaje = "Debes agregar al menos un responsable a la sesión";
            valid = false;
        }

        if(valid){
            $scope.guardarSesion($scope.sesion);
        } else {
            swal(titulo, mensaje, "error");
        }
    }
}