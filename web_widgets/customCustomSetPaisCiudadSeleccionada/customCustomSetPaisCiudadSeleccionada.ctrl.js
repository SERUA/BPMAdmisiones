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
    
    var unregister = $scope.$watch("properties.catSolicitudDeAdmision.ciudadExamenPais", function(){
        if($scope.properties.catSolicitudDeAdmision !== null && $scope.properties.catSolicitudDeAdmision !== undefined){
            if($scope.properties.catSolicitudDeAdmision.ciudadExamenPais !== null && $scope.properties.catSolicitudDeAdmision.ciudadExamenPais !== undefined){
                if(!$scope.llenovalororiginal){
                    $scope.ciudadExamenSelected = $scope.properties.catSolicitudDeAdmision.ciudadExamenPais;
                    $scope.llenovalororiginal = true;
                    if($scope.properties.catSolicitudDeAdmision.ciudadExamenPais !== null){
                        $scope.properties.paisSeleccionado = $scope.properties.catSolicitudDeAdmision.catPaisExamen.descripcion;
                    }else{
                        $scope.properties.paisSeleccionado === "";
                    }
                    
                    unregister();
                }
            }   
        }
    });
    
    var unregister2 = $scope.$watch("properties.getCiudadesByPais", function(){
        if($scope.properties.getCiudadesByPais !== null && $scope.properties.getCiudadesByPais !== undefined){
            if($scope.properties.getCiudadesByPais.length > 0){
                if($scope.llenovalororiginal){
                    $scope.properties.catSolicitudDeAdmision.ciudadExamen = $scope.ciudadExamenSelected;
                    unregister2();
                }
            }   
        }
    });
}