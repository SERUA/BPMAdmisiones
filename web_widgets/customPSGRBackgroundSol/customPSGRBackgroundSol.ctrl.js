function ($scope, $http) {
    $scope.logout = function(){
        let url = "	/bonita/logoutservice?redirect=false";
        var req = {
            method: "POST",
            url: url,
        };
  
        return $http(req).success(function(data){
            let href = window.location.protocol + "//" + window.location.host + "/apps/login/posgrados/";
            window.top.location.href = href;
        }).error(function(error){
            
        });
    }
    
    $scope.asklogout = function(){
         swal({
            "title": "Confirmación",
            "text": "¿Estás seguro que deseas cerrar sesión?",
            icon: "warning",
            buttons: [
                'Cancelar',
                'Si, cerrar sesión'
            ],
            dangerMode: true,
        }).then(function (isConfirm) {
            if (isConfirm) {
                $scope.logout();
            }
        })
    }
}