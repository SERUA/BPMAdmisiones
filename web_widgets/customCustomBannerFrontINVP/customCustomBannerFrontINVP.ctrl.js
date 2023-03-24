function ($scope, modalService, $http) {
    
    $scope.showModalLogout = function(){
        openModal($scope.properties.idModalLogout);
    }
    
    function openModal(modalId) {
        modalService.open(modalId);
    }
    
    $scope.action = function(){
        if($scope.properties.pageToken === "examen"){
            $scope.properties.accionModal = "logout";
            modalService.open($scope.properties.idModalLogout);
        } else {
            $scope.logout();
        }
    };
    
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
        
        if($scope.properties.pageToken === "examen" || $scope.properties.pageToken === "presentar"){  
            updateterminado();
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
            getTaskInfo();
        })
        .error(function(data, status) {
           
        })
        .finally(function() {

        });
    }

    function getTaskInfo(){
        var req = {
            method: "GET",
            url: "../API/bpm/task?c=1&p=0&f=assigned_id=" + $scope.properties.userData.user_id
        };

        return $http(req)
        .success(function(data, status) {
            executeTask(data[0].id);
        })
        .error(function(data, status) {
           
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
           window.top.location.href = "/bonita/apps/aspiranteinvp/termino/";
        })
        .error(function(data, status) {
           
        })
        .finally(function() {

        });
    }
}