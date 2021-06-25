function WidgetlivingApplicationMenuController($scope, $http, $window, $location, $timeout, modalService) {
    var ctrl = this;
    
    ctrl.appStarted = false;
    ctrl.disabledByTask = function(name){
        let taskName = $scope.properties.currentTaskName;
        
        if(name === "Solicitud"){
            return false;
        } else if (!$scope.properties.isCaseStarted){
            return true;
        } 
        // else if (!$scope.properties.isCaseFinished){
        //     // alert("$scope.properties.isCaseFinished");
        //     return false;
        // } 
  
  //       Generar credencial
  //       Pase de lista college board
  
  
        else if (name === "Pago" 
            && (
                taskName === "Pago de examen" ||
                taskName === "Esperar pago" ||
                taskName === "Autodescripción" ||
                taskName === "Seleccionar cita" || 
                taskName === "Generar credencial" || 
                taskName === "Pase de lista Prueba 1" || 
                taskName === "Pase de lista Prueba 2" || 
                taskName === "Pase de lista Prueba 3" || 
                taskName === "Carga y consulta de resultados" || 
                taskName === "Resultado final de comité" ||
                taskName === "Reactivar usuario rechazado"
            )
        ){
            return false;
        } else if (name === "Autodescripción" 
            && (
                taskName === "Autodescripción" ||
                taskName === "Seleccionar cita" || 
                taskName === "Generar credencial" || 
                taskName === "Pase de lista Prueba 1" || 
                taskName === "Pase de lista Prueba 2" || 
                taskName === "Pase de lista Prueba 3" || 
                taskName === "Carga y consulta de resultados" || 
                taskName === "Resultado final de comité" ||
                taskName === "Reactivar usuario rechazado"
            )
        ){
            return false;
        } else if (name === "Cita Exámenes" 
            && (
                taskName === "Seleccionar cita" || 
                taskName === "Generar credencial" || 
                taskName === "Pase de lista Prueba 1" || 
                taskName === "Pase de lista Prueba 2" || 
                taskName === "Pase de lista Prueba 3" || 
                taskName === "Carga y consulta de resultados" || 
                taskName === "Resultado final de comité" ||
                taskName === "Reactivar usuario rechazado"
             )
        ){
            return false;
        } else if (name === "Credencial" 
            && (
                taskName === "Pase de lista Prueba 1" || 
                taskName === "Pase de lista Prueba 2" || 
                taskName === "Pase de lista Prueba 3" || 
                taskName === "Generar credencial" || 
                taskName === "Carga y consulta de resultados" || 
                taskName === "Resultado final de comité" ||
                taskName === "Reactivar usuario rechazado"
            )
        ){
            return false;
        } else if (name === "Resultado" 
            && (
                taskName === "Carga y consulta de resultados" || 
                taskName === "Resultado final de comité" ||
                taskName === "Reactivar usuario rechazado"
            )
        ){
            return false;
        } else {
            return true;
        }
    }
    
    ctrl.validateUrl = function(appToken){
        let outputToken = appToken;
        
        if(appToken === "nueva_solicitud" && $scope.properties.currentTaskName === "Llenar solicitud"){
            outputToken = "nueva_solicitud";
        } else if(appToken === "nueva_solicitud" && $scope.properties.currentTaskName !== "Modificar información"){
            outputToken = "solicitud_iniciada";
        } else if(appToken === "nueva_solicitud" && $scope.properties.currentTaskName === "Modificar información"){
            // outputToken = "modificar_solicitud";
            outputToken = "modificacion_iniciada";
        } else if (appToken === "generar_credencial" && $scope.properties.currentTaskName === "Generar credencial"){
            outputToken = "confirmacion_credencial";
        }
        // else if(appToken === "nueva_solicitud" && $scope.properties.currentTaskName === "Pago de examen"){
        //     outputToken = "verSolicitudAdmision";
        // } 
        else {
            outputToken = appToken;
        } 
  
        return outputToken;
    }
     
     
    ctrl.goTo = function(_token){
        // if(_token === "verSolicitudAdmision"){
        //     window.location.href = "../" + token;
        // } else {
        //     window.location.href = "../" + token + "?id=" + $scope.properties.taskId + "&displayConfirmation=false";
        // }
        
        window.location.href = "../" + token;
    }
    
    ctrl.filterChildren = function (parentId) {
        return (ctrl.applicationMenuList||[]).filter(function(menu){
            return menu.parentMenuId === '' + parentId;
        });
    };
    
    ctrl.isParentMenu = function(menu) {
        return menu.parentMenuId==-1 && menu.applicationPageId==-1;
    };
     
     
    ctrl.displayPage = function(token) {
        if(ctrl.pageToken === "autodescripcion" && $scope.properties.currentTaskName === "Autodescripción"){
            $scope.tokenNuevo = token;
            $scope.properties.accionModal = "salir";
            modalService.open($scope.properties.idModalConfirmacionAD);
        } else {
            ctrl.redirectToPage(token);
        }
    };

    ctrl.redirectToPage = function(token){
        var previousToken = ctrl.pageToken;
        var previousPath = window.location.pathname;
            
        ctrl.pageToken = token;
        var urlPath = "";
        if(previousToken === "autodescripcion" || previousToken === "pago_de_examen" || previousToken === "confirmacion_credencial" || previousToken === "verSesiones"){
            previousPath.substring(0, previousPath.length-previousToken.length-2) + token + '/' + ($window.location.search === undefined || $window.location.search === "undefined" ? "" : $window.location.search);
        } else {
            previousPath.substring(0, previousPath.length-previousToken.length-1) + token + '/' + ($window.location.search === undefined || $window.location.search === "undefined" ? "" : $window.location.search);
        }
            
        var stateObject = { title: "" + token + "", url: "" +  urlPath  + ""};
        if (typeof ($window.history.pushState) != "undefined") {
            $window.history.pushState(stateObject, stateObject.title, stateObject.url );
        } else {
            alert("Browser does not support HTML5.");
        }

        $scope.properties.currentToken = token;

        //make sure the user is still logged in before refreshing the iframe
        verifySession().then(setTargetedUrl, refreshPage);
            
        return false;
    }
    
    ctrl.setParentPageActive = function(menu){
        ctrl.parentPageId = menu.id;
    }
     
    ctrl.openCurrentSessionModal = function() {
        modalService.open($scope.properties.currentSessionModalId);
    };
     
    ctrl.openAppSelectionModal = function() {
         modalService.open($scope.properties.appSelectionModalId);
    };
     
    ctrl.logoutAndUpdateSuccessfulLogoutVariable = function() {
        return $http.get($scope.properties.logoutURL)
        .success(function(data) { 
            $scope.properties.successfulLogoutResponse = data;
        });
    };
    
    //handle the browser back button
    $window.addEventListener('popstate', function(e) {
        parseCurrentURL();
        //make sure the user is still logged in before refreshing the iframe
        setTargetedUrl();
        refreshPage();
    });
  
    function parseCurrentURL() {
        var pathArray = $window.location.pathname.split( '/' );
        ctrl.applicationToken = pathArray[pathArray.length-3];
        ctrl.pageToken = pathArray[pathArray.length-2];
    }
    
    function setApplicationMenuList(application) {
        return $http.get('../API/living/application-menu/?c=100&f=applicationId%3D'+application.id+'&d=applicationPageId&o=menuIndex+ASC')
        .success(function(data) { 
            ctrl.applicationMenuList = data;
            ctrl.applicationMenuList[0].applicationPageId.token = ctrl.validateUrl(ctrl.applicationMenuList[0].applicationPageId.token);
            ctrl.applicationMenuList[4].applicationPageId.token = ctrl.validateUrl(ctrl.applicationMenuList[4].applicationPageId.token);
        });
    }
  
    function searchSeparator() {
        return $window.location.search ? "&" : "?";
    }
  
    function setTargetedUrl() {
        // angular hack to force the variable bound to refresh
        // so we change it's value to undefined and then delay to the correct value
        $scope.properties.targetUrl = undefined;
        $timeout(function(){
            $scope.properties.targetUrl = "../../../portal/resource/app/" + ctrl.applicationToken + "/" + ctrl.pageToken + "/content/" + $window.location.search + searchSeparator() + "app=" + ctrl.applicationToken;
            console.log($scope.properties.targetUrl);
        }, 0);
    }
     
    function refreshPage() {
        $window.location.reload();
    }
  
    function verifySession() {
        var userIdentity = '../API/identity/user/' +  $scope.properties.userId;
        return $http.get(userIdentity);
    }
     
    function setApplication(){
        var application = $scope.properties.application;
        ctrl.applicationToken = application.token;
        if ($scope.properties.currentTaskName === "Pago de examen" || $scope.properties.currentTaskName === "Esperar pago"){
            ctrl.pageToken = "pago_de_examen";
        } else if ($scope.properties.currentTaskName === "Autodescripción"){

        	if(parseFloat($scope.properties.processData.version)>parseFloat("1.5")) {
				ctrl.pageToken = "autodescripcionV2";
			} else {
            	ctrl.pageToken = "autodescripcion";
			}
        } else if ($scope.properties.currentTaskName === "Seleccionar cita"){
            ctrl.pageToken = "verSesiones";
        } else if ($scope.properties.currentTaskName === "Generar credencial"){
            let array = window.location.href.split("/");
            let appName = array[array.length - 2];
            debugger;
            if(appName === "generar_credencial"){
                ctrl.pageToken = "generar_credencial";
            } else {
                ctrl.pageToken = "confirmacion_credencial";
            }
        }  else if (
            $scope.properties.currentTaskName === "Pase de lista Prueba 1" || 
            $scope.properties.currentTaskName === "Pase de lista Prueba 2" || 
            $scope.properties.currentTaskName === "Pase de lista Prueba 3" 
            ){
            ctrl.pageToken = "generar_credencial";
        }  else if (
            $scope.properties.currentTaskName === "Carga y consulta de resultados" || 
            $scope.properties.currentTaskName === "Resultado final de comité" ||
            $scope.properties.currentTaskName === "Reactivar usuario rechazado"
        ){
            ctrl.pageToken = "Resultado";
        } else {
            ctrl.pageToken = $scope.properties.pageToken;
        }
        
        $scope.properties.currentToken = ctrl.pageToken;

        ctrl.applicationName = $scope.properties.application.displayName;
        setApplicationMenuList(application);
        setTargetedUrl();
    }
    
    $scope.$watch("properties.currentTaskName", function(){
        // if($scope.properties.isCaseStarted !== undefined && $scope.properties.currentTaskName !== undefined && !ctrl.appStarted){
        //     ctrl.appStarted = true;
        //     setApplication();    
        // }
        if(
            ($scope.properties.isCaseStarted && $scope.properties.currentTaskName !== "") || 
            (!$scope.properties.isCaseStarted && $scope.properties.currentTaskName === "")
        ){
            ctrl.appStarted = true;
            setApplication();    
        } 
    });
    
    $scope.$watch("properties.isCaseStarted", function(){
        if($scope.properties.isCaseStarted !== undefined && $scope.properties.currentTaskName !== undefined && !ctrl.appStarted){
            if(
                ($scope.properties.isCaseStarted && $scope.properties.currentTaskName !== "") || 
                (!$scope.properties.isCaseStarted && $scope.properties.currentTaskName === "")
            ){
                ctrl.appStarted = true;
                setApplication();    
            } 
        }
    });

    $scope.$watch("properties.cambiarPagina", function(){
        if($scope.properties.cambiarPagina === true){
            $scope.properties.cambiarPagina = false;
            modalService.close();
            ctrl.redirectToPage($scope.tokenNuevo);
        }
    });
}