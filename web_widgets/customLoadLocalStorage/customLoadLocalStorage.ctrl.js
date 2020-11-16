/**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function ($scope) {
    
    
    function loadLocalStorage(){
        debugger;
        var localS = localStorage;
        if(localS.getItem("catSolicitudDeAdmisionInput") !== null){
            $scope.properties.formInput = JSON.parse(localS.getItem("catSolicitudDeAdmisionInput"));  
            $scope.properties.selectedIndex = parseInt(localS.getItem("selectedIndex"));
            if(localS.getItem("actanacimiento") !== null){
                $scope.properties.actanacimiento = JSON.parse(localS.getItem("actanacimiento")); 
            }
            if(localS.getItem("fotopasaporte") !== null){
                $scope.properties.fotopasaporte = JSON.parse(localS.getItem("fotopasaporte")); 
            }
            if(localS.getItem("kardex") !== null){
                $scope.properties.kardex = JSON.parse(localS.getItem("kardex")); 
            }
            if(localS.getItem("descuento") !== null){
                $scope.properties.descuento = JSON.parse(localS.getItem("descuento")); 
            }
            if(localS.getItem("isPadretutor") !== null){
                $scope.properties.isPadretutor = localS.getItem("isPadretutor"); 
            }
            if(localS.getItem("isMadretutor") !== null){
                $scope.properties.isMadretutor = localS.getItem("isMadretutor"); 
            }
            if(localS.getItem("preparatoriaSeleccionada") !== null){
                $scope.properties.preparatoriaSeleccionada = localS.getItem("preparatoriaSeleccionada"); 
            }
            for(var x=0;x<$scope.properties.catCampus.length;x++){
                if($scope.properties.catCampus[x].persistenceId === $scope.properties.formInput.catSolicitudDeAdmisionInput.catCampus.persistenceId){
                    $scope.properties.campusSelected = $scope.properties.formInput.catSolicitudDeAdmisionInput.catCampus.persistenceId;
                }
            }
            if($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen.descripcion === "En el mismo campus en donde realizaré mi licenciatura"){
               $scope.properties.testOptionsRadio = 1;
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen.descripcion === "En un estado"){
                $scope.properties.testOptionsRadio = 2;
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen.descripcion === "En el extranjero (solo si vives fuera de México)"){
                $scope.properties.testOptionsRadio = 3;
            }
        }
    }
    
    $scope.$watch("properties.catCampus", function(){
        if($scope.properties.catCampus !== undefined){
           loadLocalStorage();
        }
    });
}