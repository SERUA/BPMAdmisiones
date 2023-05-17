function ($scope, modalService, $http) {
    
    $scope.showModalLogout = function(){
        openModal($scope.properties.idModalLogout);
    }
    
    function openModal(modalId) {
        modalService.open(modalId);
    }
    
    $scope.action = function(){
        if($scope.properties.pageToken === "autodescripcion"){
            $scope.properties.accionModal = "logout";
            modalService.open($scope.properties.idModalConfirmacionAD);
        } else {
            $scope.logout();
        }
    };
    
    $scope.logout = function(){
        let url = "	/bonita/logoutservice?redirect=false";
        var req = {
            method: "POST",
            url: url,
        };
  
        return $http(req).success(function(data){
            let href = window.location.protocol + "//" + window.location.host + $scope.properties.urlToRedirect;
            window.top.location = href;
        }).error(function(error){
            
        });
    }
    $.sessionTimeout({
        title: "Tu sesión ha expirado",
        message: "Su sesión está apunto de cerrarse.",
        keepAliveUrl: "/bonita/API/system/session/unusedid",
        redirUrl: (window.location.href.includes("aspiranteSDAE"))? " /bonita/apps/login/apoyoeducativo/" : "/bonita/apps/login/administrativo/",
        logoutUrl: (window.location.href.includes("aspiranteSDAE")) ? "/bonita/apps/login/apoyoeducativo/" : "/bonita/apps/login/administrativo/",
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
        if($scope.properties.cerrarSesion === true){
            $scope.properties.cerrarSesion = false;
            modalService.close();
            $scope.logout();
        }
    })
}