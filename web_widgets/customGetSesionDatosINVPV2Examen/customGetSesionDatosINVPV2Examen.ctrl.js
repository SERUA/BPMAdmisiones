function ($scope, $http) {
    
    $scope.error = true;
    $scope.$watch("properties.username", function(){
        if($scope.properties.username){
            let url = $scope.properties.url + $scope.properties.username; 
            
            $http.get(url).success(function(_success){
                $scope.properties.sesionInfoV2 = angular.copy(_success);
            }).error(function(_err){
                $scope.properties.sesionInfoV2 = angular.copy(_err);
                
                if($scope.properties.sesionInfoV2.error === "no_existe_sesion" && ($scope.properties.objetoSesion.idsesion_temp || $scope.properties.objetoSesion.id_prueba)){
                    // updateterminado()
                }
            });
        }
    });
    
    
    $scope.$watch("properties.objetoSesion", function(){
        if($scope.properties.objetoSesion){
            if($scope.properties.sesionInfoV2.error === "no_existe_sesion" && ($scope.properties.objetoSesion.idsesion_temp || $scope.properties.objetoSesion.id_prueba)){
                // updateterminado()
            }
        }
    });
    
    function updateterminado() {
        debugger;
        var data = {
            "terminado": true,
            "username": $scope.properties.username
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
}