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
    
    $scope.$watch("properties.reload", function(){
        if(($scope.properties.reload === undefined || $scope.properties.reload.length === 0) && $scope.properties.campusSeleccionado !== undefined){
            $("#loading").modal("show");
        }else{
            $("#loading").modal("hide");
        }
    });
}