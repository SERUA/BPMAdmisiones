function ($scope, $http) {
    
    /*$scope.getCommentList = function(){
        let url = "../API/bdm/businessData/com.anahuac.bitacora.CatBitacoraComentarios?q=find&c=10000&p=0";
        var req = {
            method: "GET",
            url: url
        };
        
        return $http(req).success(function(data, status) {
            $scope.properties.commentList = data;
        })
        .error(function(data, status) {
             swal("Error.", data.message, "error");
           // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {
            
        });
    }
    */
   // $scope.getCommentList();
   $scope.llenoestado = false;
    $scope.llenovalororiginal = false;
    $scope.ciudadExamenSelected;
    $scope.estadoExamenSelected;
    
    /*var unregister = $scope.$watch("properties.catSolicitudDeAdmision.catEstadoExamen", function(){
        debugger;
        if($scope.properties.catSolicitudDeAdmision.catEstadoExamen !== null && $scope.properties.catSolicitudDeAdmision.catEstadoExamen !== undefined){
            if(!$scope.llenoestado){
                $scope.estadoExamenSelected = $scope.properties.catSolicitudDeAdmision.catEstadoExamen;
                $scope.properties.estadoSeleccionado = $scope.properties.catSolicitudDeAdmision.catEstadoExamen.descripcion;
                $scope.llenoestado = true;
                unregister();
            }
        }
    });*/
    
    var unregister = $scope.$watch("properties.catSolicitudDeAdmision.ciudadExamen", function(){
        if($scope.properties.catSolicitudDeAdmision !== null && $scope.properties.catSolicitudDeAdmision !== undefined){
            if($scope.properties.catSolicitudDeAdmision.ciudadExamen !== null && $scope.properties.catSolicitudDeAdmision.ciudadExamen !== undefined){
                if(!$scope.llenovalororiginal){
                    $scope.ciudadExamenSelected = $scope.properties.catSolicitudDeAdmision.ciudadExamen;
                    $scope.llenovalororiginal = true;
                    $scope.estadoExamenSelected = $scope.properties.catSolicitudDeAdmision.catEstadoExamen;
                    if($scope.properties.catSolicitudDeAdmision.catEstadoExamen !== null){
                        $scope.properties.estadoSeleccionado = $scope.properties.catSolicitudDeAdmision.catEstadoExamen.descripcion;
                    }else{
                        $scope.properties.estadoSeleccionado = "";
                    }
                    unregister();
                }
            }   
        }
    });
    
     $scope.$watch("properties.getCiudadesByEstado", function(){
         if($scope.properties.getCiudadesByEstado !== null && $scope.properties.getCiudadesByEstado !== undefined){
             if($scope.properties.getCiudadesByEstado.length > 0){
                if($scope.llenovalororiginal){
                    $scope.properties.catSolicitudDeAdmision.ciudadExamen = $scope.ciudadExamenSelected;
                
                }
            }
         }
    });
}