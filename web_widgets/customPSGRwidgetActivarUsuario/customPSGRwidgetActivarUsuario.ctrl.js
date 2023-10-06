function($scope, $http){
    
    $scope.$watch("properties.username", function(){
        if(!$scope.properties.username){
            habilitarUsuario();
        }
    });
    
    function habilitarUsuario(){
        let url ="../API/extension/posgradosRestGet?url=habilitarUsuario&usernameAspirante=" + $scope.properties.username;
        
        $http.get(url).success(function(_data){
            swal("Ok", "Usuario activado exitosamente.", "success").then(() => {
                let href = window.location.protocol + "//" + window.location.host +"/apps/login/posgrados/";
                window.top.location = href;
            });
        }).error(function(_data){
            debugger;
            swal("Algo ha fallado", "El usuario ya ha sido activado anteriormente o no existe.", "error");
        });
    }
}