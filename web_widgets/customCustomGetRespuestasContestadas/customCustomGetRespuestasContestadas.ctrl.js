function ($scope, $http) {
    
    $scope.getCommentList = function(){
        debugger;
        let data = {
            "caseid":$scope.properties.commentList,
            "idusuario":Number($scope.properties.reload)
        }
        let url = "../API/extension/AnahuacINVPRestAPI?url=getCatRespuestasExamenbyUsuariocaso&p=0&c=10";
        var req = {
            method: "POST",
            url: url,
            data:data
        };
        
        return $http(req).success(function(data, status) {
            debugger;
            let respuesta = [];
            /*let valores = {
                "pregunta":0,
                "respuesta":null
            }*/
            for(let i=0;i<data.data.length;i++){
                let valores = {
                    "pregunta":0,
                    "respuesta":null
                    }
                valores.pregunta = data.data[i].pregunta;
                valores.respuesta = data.data[i].respuesta;
                respuesta.push(valores);
                 $scope.properties.campusSeleccionado.push(data.data[i].pregunta);
                 console.log(respuesta)
                 $scope.properties.preguntasContestadas.push(valores);
                 if(valores.pregunta === 1){
                     $scope.properties.respuesta = valores.respuesta;
                 }
            }
            
            if($scope.properties.preguntasContestadas.length > 0){
                $scope.properties.cargo = true;
            }
            //$scope.properties.campusSeleccionado = data.data[0].caseid;
            
        })
        .error(function(data, status) {
             swal("Error.", data.message, "error");
           // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {
            
        });
    }
    
   // $scope.getCommentList();
    
    $scope.$watch("properties.commentList", function(){
        if($scope.properties.reload !== undefined && $scope.properties.commentList !== undefined){
             $scope.getCommentList();
        }
        /*if(($scope.properties.reload === undefined || $scope.properties.reload.length === 0) && $scope.properties.campusSeleccionado !== undefined){
            $("#loading").modal("show");
        }else{
            $("#loading").modal("hide");
        }*/
    });
}