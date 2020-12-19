function ($scope, $http, modalService) {
    
    $scope.formInput = { 
        "conIsPagoValidado" : false, 
        "isPagoTarjeta" : false ,
        "detalleSolicitudInput" : {
            "ordenPago":""
        }
    }
    
    $scope.showModal = function(){
        $("#loading").modal("show");
    };
    
    $scope.hideModal = function(){
        $("#loading").modal("hide");
    };
    
    $scope.newNavValue = "";
    
    $scope.assignTask = function () {
        $scope.showModal();
        let url = "../API/bpm/userTask/" + $scope.properties.taskId;
        
        var req = {
            method: "PUT",
            url: url,
            data:{
                "assigned_id": $scope.properties.userId
            }
        };

        return $http(req).success(function(data, status) {
            $scope.executeTask();
        })
        .error(function(data, status) {
            $scope.hideModal();
            swal("Error", data.message, "error");
        })
        .finally(function() {
            
        });
    }

    $scope.executeTask = function(){
        let ipBonita = window.location.protocol + "//" + window.location.host + "/bonita";
        let url = ipBonita + "/API/bpm/userTask/" + $scope.properties.taskId + "/execution";
        
        $http.post(url, $scope.formInput).success(function(data){
            $scope.hideModal();
            $scope.properties.navigationVar = "hideall"
            swal("Pago exitoso", "Redireccionando.", "success");
            if($scope.reloadPage){
                setTimeout(function(){
                    window.location.reload();
                }, 3000);   
            }
        }).error(function(error){
            console.log("Fallo la tarea" + JSON.stringify(error));
        })
    }
    
    $scope.$watch("properties.bankInformationSPEI", function(){
        if($scope.properties.bankInformationSPEI !== undefined){
            if($scope.properties.bankInformationSPEI.success){
                $scope.formInput.isPagoTarjeta = false;
                $scope.formInput.detalleSolicitudInput.ordenPago = $scope.properties.bankInformationSPEI.data[0].id;
                if($scope.properties.isCorrectTask){
                    // $scope.newNavValue = "paymentWaiting";
                    $scope.reloadPage = false;
                    $scope.assignTask();  
                }
            }
        }
    });
    
    $scope.$watch("properties.paymentInformationOXXO", function(){
        if($scope.properties.paymentInformationOXXO !== undefined){
            if($scope.properties.paymentInformationOXXO.success){
                $scope.formInput.isPagoTarjeta = false;
                $scope.formInput.detalleSolicitudInput.ordenPago = $scope.properties.paymentInformationOXXO.data[0].id;
                if($scope.properties.isCorrectTask){
                    // $scope.newNavValue = "paymentWaiting";
                    $scope.reloadPage = false;
                    $scope.assignTask();    
                }
            }
        }
    });
    
    $scope.$watch("properties.paymentInfoCard", function(){
        if($scope.properties.paymentInfoCard !== undefined){
            if($scope.properties.paymentInfoCard.success){
                $scope.formInput.isPagoTarjeta = true;
                $scope.formInput.detalleSolicitudInput.ordenPago = $scope.properties.paymentInfoCard.data[0].id;
                if($scope.properties.isCorrectTask){
                    // $scope.newNavValue = "cardPaid";
                    $scope.reloadPage = true;
                    $scope.assignTask();
                }
            }
        }
    });
    
    $scope.getOrderInformation = function(){
        let url = "/bonita/API/extension/AnahuacRest?url=getOrderDetails&p=0&c=10";
        $http.post(url, $scope.properties.orderObject).success(function(success){
            $scope.properties.orderOutput = success.data[0];
        }).error(function(error){
            swal("Error", JSOM.stringify(error), "error");
        }).finally(function(){
          
        })  
    };
    
    $scope.$watch("properties.orderObject", function(){
        if($scope.properties.orderObject !== undefined && $scope.properties.orderObject !== null){
            $scope.getOrderInformation();
        }
    });
}