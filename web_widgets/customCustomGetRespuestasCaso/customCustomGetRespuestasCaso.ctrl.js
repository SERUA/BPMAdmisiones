function ($scope, $http) {
    
    $scope.getCommentList = function(){
        let data = {
            "idusuario":$scope.properties.reload
        }
        let url = "../API/extension/AnahuacINVPRestAPI?url=getCaseIdbyuserid&p=0&c=10";
        var req = {
            method: "POST",
            url: url,
            data:data
        };
        
        return $http(req).success(function(data, status) {
            $scope.properties.commentList = data.data[0].caseid;
            // var termino = localStorage.getItem("terminado");
            // if(termino === "true"){
            //     window.top.location.href = "/bonita/apps/login/testinvp/";
            // }
            getExamenTerminado($scope.properties.username);
        })
        .error(function(data, status) {
             swal("Error.", data.message, "error");
           // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {
            
        });
    }
    
    function getExamenTerminado(_username){
        debugger;
        let url = "../API/extension/AnahuacINVPRestGet?url=getExamenTerminado&p=0&c=100&username=" + _username;
        $http.get(url).success(function(_success){
            debugger;
            if(_success[0].examenTerminado){
                window.top.location.href = "/bonita/apps/aspiranteinvp/termino";
            } else if (_success[0].examenIniciado && !$scope.properties.isExamen)  {
                window.top.location.href = "/bonita/apps/aspiranteinvp/examen";
            } 
        }).error(function(_error){
            Swal.fire({
                title: '<strong>Atención</strong>',
                icon: 'error',
                html: _error, showCloseButton: false
            });
        });
    }
    
   // $scope.getCommentList();
    
    $scope.$watch("properties.reload", function(){
        if($scope.properties.reload !== undefined){
             $scope.getCommentList();
        }
    });
}