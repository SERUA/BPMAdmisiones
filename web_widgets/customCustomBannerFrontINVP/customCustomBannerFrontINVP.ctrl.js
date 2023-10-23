function ($scope, modalService, $http) {
    
    $scope.showModalLogout = function(){
        openModal($scope.properties.idModalLogout);
    }
    
    function openModal(modalId) {
        modalService.open(modalId);
    }
    
    // $scope.action = function(){
    //     if($scope.properties.pageToken === "examen"){
    //         $scope.properties.accionModal = "logout";
    //         modalService.open($scope.properties.idModalLogout);
    //     } else {
    //         $scope.logout();
    //     }
    // };
    
    $scope.action = function(){
        if($scope.properties.pageToken === "examen"){
            let url = "../API/extension/AnahuacINVPRestGet?url=getInfoRespuestas&p=0&c=10&username=" + $scope.properties.userData.user_name
                
            return $http({
                method: "GET",
                url: url
            }).success(function (data, status) {
                $scope.properties.datosRespuestas = data.data[0];
                $scope.properties.accionModal = "logout";
                modalService.open($scope.properties.idModalLogout); 
            })
            .error(function (data, status) {
                swal("Error", "No se pudo obtener la información d las respuestas.", "error")
            });
        } else {
            $scope.logout();
        }
    }
    
    function getIdiomaUsuario(){
        var req = {
            method: "GET",
            url: "../API/extension/AnahuacINVPRestGet?url=getIdiomaUsuario&p=0&c=10&username=" + $scope.properties.userData.user_name
        };

        return $http(req).success(function(data, status) {
            if(data.data.length === 0){
                $scope.properties.idioma = "ESP";
            } else if(data.data[0].idioma === null){
                $scope.properties.idioma = "ESP";
            } else{
                $scope.properties.idioma = data.data[0].idioma;
            }
        }).error(function(data, status) {
            console.log(data);
        });
    }

    $scope.$watch("properties.userData", function(){
        if($scope.properties.userData){
            getIdiomaUsuario();
        }
    });
    
    $scope.logout = function(){
        let url = "	/bonita/logoutservice?redirect=false";
        var req = {
            method: "POST",
            url: url,
        };
  
        return $http(req).success(function(data){
            if($scope.properties.pageToken === "termino"){
                window.top.location.href = "/bonita/apps/login/admisiones/";
            }else{
                let href = window.location.protocol + "//" + window.location.host + $scope.properties.urlToRedirect;
                window.top.location = href;
            }

        }).error(function(error){
            
        });
    }

    $.sessionTimeout({
        title: "Tu sesión ha expirado",
        message: "Su sesión está apunto de cerrarse.",
        keepAliveUrl: "/bonita/API/system/session/unusedid",
        redirUrl: (window.location.href.includes("aspirante"))?"/bonita/apps/login/admisiones/":"/bonita/apps/login/administrativo/",
        logoutUrl: (window.location.href.includes("aspirante"))?"/bonita/apps/login/admisiones/":"/bonita/apps/login/administrativo/",
        warnAfter: 7.08e+6,
        redirAfter: 7.14e+6,
        ignoreUserActivity: !0,
        countdownMessage: "Redireccionando en {timer} segundos.",
        ajaxType: "GET",
        keepAliveButton: "Mantenerse conectado",
        logoutButton: "Desconectarse",
        countdownBar: !0
    })
    
    $scope.$watch("properties.cerrarSesion", function(){
        debugger;
        if($scope.properties.pageToken === "examen" || $scope.properties.pageToken === "presentar"){  
            getTaskInfo();
        }else if($scope.properties.pageToken === "termino"){
            $scope.properties.cerrarSesion = false;
            modalService.close();
            $scope.logout();
        }else if($scope.properties.cerrarSesion === true){
            $scope.properties.cerrarSesion = false;
            modalService.close();
            $scope.logout();
        }
    })
    
    $scope.msj = "";
    $scope.quitar = false;
    
    $scope.$watch("properties.pageToken", function(){
        // $scope.properties.idioma = localStorage.getItem("idioma");MARIO
        if($scope.properties.pageToken === "presentar"){
            $scope.msj = "Terminar / Finish";
            $scope.quitar = true;
        }else if($scope.properties.pageToken === "examen"){
            if($scope.properties.idioma === "ESP"){
                $scope.msj = "Terminar";    
            }else if($scope.properties.idioma === "ENG"){
                $scope.msj = "Finish";  
            }
            $scope.quitar = false;
        }else if($scope.properties.pageToken === "termino"){
            $scope.msj = "Ir a Admisiones / Go to Admisions";
            $scope.quitar = true;
        }
    })

    $scope.$watch("properties.idioma", function(){
        if($scope.properties.pageToken === "presentar"){
            $scope.msj = "Terminar / Finish";
            $scope.quitar = true;
        }else if($scope.properties.pageToken === "examen"){
            if($scope.properties.idioma === "ESP"){
                $scope.msj = "Terminar";    
            }else if($scope.properties.idioma === "ENG"){
                $scope.msj = "Finish";  
            }
            $scope.quitar = false;
        }else if($scope.properties.pageToken === "termino"){
            $scope.msj = "Ir a Admisiones / Go to Admisions";
            $scope.quitar = true;
        }
    })
    
    function updateterminado() {
        var data = {
            "terminado": true,
            "username": $scope.properties.userData.user_name
        }

        var req = {
            method: "POST",
            url: "../API/extension/AnahuacINVPRestAPI?url=updateterminado&p=0&c=10",
            data: data
        };

        return $http(req)
        .success(function(data, status) {
            window.top.location.href = "/bonita/apps/aspiranteinvp/termino/";
        })
        .error(function(data, status) {
           swal("Error", "No se pudo actualizar el estatus a terminado. Contacte a su aplicador.", "error")
        })
        .finally(function() {

        });
    }

    function getTaskInfo(){
        var req = {
            method: "GET",
            // url: "../API/bpm/task?c=100&p=0&f=assigned_id=" + $scope.properties.userData.user_id + "&f=name=Examen%20INVP"
            url: "../API/bpm/humanTask?p=0&c=10&f=assigned_id=" + $scope.properties.userData.user_id + "&f=name=Examen%20INVP"
        };

        return $http(req)
        .success(function(data, status) {
            let taskid = ""
            for(let reg of data){
                if(reg.assigned_id === $scope.properties.userData.user_id){
                    taskid = reg.id;
                    break;
                }
            }

            executeTask(taskid);
        })
        .error(function(data, status) {
            swal("Error", "Ocurrio un problema al obtener los datos de la tarea", "error");
        })
        .finally(function() {

        });
    }

    function executeTask(_taskid) {
        let dataToSend = {
            "isTerminado": true,
            "instanciaINVPInput": {
                "mensajeTermino": "",
            },
            "terminadoFInput": false,
        }

        var req = {
            method: "POST",
            url: "../API/bpm/userTask/" + _taskid + "/execution",
            data: dataToSend
        };

        return $http(req)
        .success(function(data, status) {
            updateterminado()
        })
        .error(function(data, status) {
            if(status == 405){
                window.location.reload();
            } else {
                swal("Error", "Error al ejecutar la tarea: " + status, "error");
            }
        })
        .finally(function() {

        });
    }
}