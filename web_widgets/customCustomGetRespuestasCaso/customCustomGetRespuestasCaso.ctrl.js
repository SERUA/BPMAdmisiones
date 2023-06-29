function ($scope, $http) {
    $scope.properties.showForm = false;
    
    $scope.getCasoByIdUser = function(){
        let data = {
            "idusuario": $scope.properties.reload,
            "username": $scope.properties.username
        }
        
        let url = "../API/extension/AnahuacINVPRestAPI?url=getCaseIdbyuserid&p=0&c=10";
        var req = {
            method: "POST",
            url: url,
            data:data
        };

        return $http(req).success(function(data, status) {
            if(data.additional_data){
                $scope.properties.caseId = data.additional_data[0];
            }
            // $scope.properties.caseId = data.data[0].caseid;
            getExamenTerminado($scope.properties.username);
        })
        .error(function(data, status) {
             swal("Error.", data.message, "error");
        })
        .finally(function() {
            
        });
    }
    
    function getExamenTerminado(_username){
        let url = "../API/extension/AnahuacINVPRestGet?url=getExamenTerminado&p=0&c=100&username=" + _username;
        $http.get(url).success(function(_success){
            if(_success[0].examenTerminado){
                window.top.location.href = "/bonita/apps/aspiranteinvp/termino";
            } else if (_success[0].examenIniciado && !$scope.properties.isExamen)  {
                window.top.location.href = "/bonita/apps/aspiranteinvp/examen";
            } else {
                $scope.properties.showForm = true;
            }
        }).error(function(_error){
            Swal.fire({
                title: '<strong>Atención</strong>',
                icon: 'error',
                html: _error, showCloseButton: false
            });
        });
    }

    $scope.$watch("properties.reload", function(){
        if($scope.properties.reload !== undefined){
            // $scope.getCasoByIdUser();
            if($scope.properties.username && $scope.properties.username !== "guest"){
                $scope.getCasoByIdUser(); 
            } else {
               window.top.location.href = "/apps/login/testinvp"; 
            }
        }
    });
}