function($scope, $http) {

    $scope.getCommentList = function() {
        debugger;
        let data = {
            "username" : $scope.properties.reload
        }

        var req = {
            method: 'POST',
            url: '../API/extension/AnahuacINVPRestAPI?url=getDatosSesionLogin&p=0&c=10',
            data: data
        };
      
        return $http(req).success(function(data, status) {
            if(data.data.length > 0){
                var today = new Date();
                var yyyy = today.getFullYear();
                let mm = today.getMonth() + 1; // Months start at 0!
                let dd = today.getDate();
                
                if (dd < 10) dd = '0' + dd;
                if (mm < 10) mm = '0' + mm;
                
                var formattedDate = yyyy+'-'+mm+'-'+dd;

                for(var x=0;x<data.data.length;x++){
                    if(data.data[x].aplicacion === formattedDate){
                        $scope.properties.campusSeleccionado = data.data[x].nombre_sesion;         
                    }
                }
               
            }
        })
        .error(function(data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            
        })
        .finally(function() {
            $scope.hideLoading();
            vm.busy = false;
        });
            
    }

    // $scope.getCommentList();

    $scope.$watch("properties.reload", function() {
        if ($scope.properties.reload !== undefined) {
            $scope.getCommentList();
        }
        /*if(($scope.properties.reload === undefined || $scope.properties.reload.length === 0) && $scope.properties.campusSeleccionado !== undefined){
            $("#loading").modal("show");
        }else{
            $("#loading").modal("hide");
        }*/
    });
}