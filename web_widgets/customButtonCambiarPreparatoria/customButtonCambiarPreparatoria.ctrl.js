function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  $scope.myFunc = function() {

           
            
            let nombrePrepa= $scope.properties.NombrePrepaNueva;
            let nuevaPrepa="";
            let encontro=false;
            
             if(nombrePrepa==="Otro"){
                     Swal.fire("¡Aviso!","No debes modificar la preparatoria a 'Otro'","warning");
              }else{
                for (var i=0 ; i < $scope.properties.catBachillerato2.length ; i++){
                    if($scope.properties.catBachillerato2[i].descripcion ===nombrePrepa){
                        nuevaPrepa=$scope.properties.catBachillerato2[i].persistenceid;
                        encontro=true
                    break;
                    }
                }
                
            }
            if(encontro){
                
            var prepa = Number(nuevaPrepa);
            $scope.properties.prepaToSend.persistenceid = $scope.properties.idSolicitud;
            $scope.properties.prepaToSend.catbachilleratos_pid = prepa;
            
            // $scope.properties.prepaToSend.persistenceid = $scope.properties.idSolicitud.toString();
            // $scope.properties.prepaToSend.catbachilleratos_pid = nuevaPrepa.toString();
           //alert("solicitud no:"+$scope.properties.prepaToSend.persistenceid+" Prepa numero:"+$scope.properties.prepaToSend.catbachilleratos_pid)
            
        
            var req = {
                method: "POST",
                url: "/bonita/API/extension/AnahuacRest?url=updatePrepaSolicitudDeAdmision&p=0&c=10",
                data: angular.copy($scope.properties.prepaToSend)
            };
            return $http(req)
                .success(function (data, status) {
                    Swal.fire("¡Preparatoria modificada correctamente!","","success")
                    $scope.properties.isPrepaModificada=true;
                     $scope.properties.CheckCambiarPrepa=false;
                })
                .error(function (data, status) {
                    notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                });
                
                 
            }else{
                Swal.fire("¡Preparatoria no encontrada!","Debe ingresar una preparatoria existente en el catálogo.","warning")
                
            }
            
            
        
            
    };
}
