function PbButtonCtrl($scope, $http) {
    'use strict';
    var vm = this;
    var validacion = "";

    this.isArray = Array.isArray;
    $scope.loading = false;

    
    $scope.sendData = function() {
        if ($scope.loading == false) {
            if($scope.properties.isModificacion === false){
                if($scope.properties.pagoExamenDeshabilitadoOHabilitado === true){
                    if($scope.properties.nuevosValores[0].campus && $scope.properties.nuevosValores[0].conekta && $scope.properties.nuevosValores[0].mailgun && $scope.properties.nuevosValores[0].crispChat && $scope.properties.nuevosValores[0].mailgunDominio && $scope.properties.nuevosValores[0].mailgunCorreo){
                        $("#loading").modal("show");
                        debugger
                        $scope.loading = true;
                        $scope.properties.nuevosValores.forEach(element =>{
                        $scope.properties.contenido.push(element);
                        
                    })
                    $scope.properties.mostrarPantallaEditar = false;
                    $scope.properties.mostrarPantallaAgregarApiKey = false;
                    $scope.properties.nuevosValores = [];
                    $scope.properties.objContrato = $scope.properties.contenido;
                    $("#loading").modal("show");
                    $scope.loading = true;
                    $scope.asignarTarea()
                }else{
                    if(!$scope.properties.nuevosValores[0].campus){
                        this.validacion = "Campus";
                        $("#modalValidacion").modal("show");
                        //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                    if(!$scope.properties.nuevosValores[0].conekta){
                        
                        this.validacion = "Conekta";
                        $("#modalValidacion").modal("show");
                         //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                    if(!$scope.properties.nuevosValores[0].mailgun){
                        
                        this.validacion = "Mailgun";
                        $("#modalValidacion").modal("show");
                        //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                    if(!$scope.properties.nuevosValores[0].crispChat){
                        
                        this.validacion = "Crisp chat";
                        $("#modalValidacion").modal("show");
                        //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                }
            } 
        }else{
            debugger
            $("#loading").modal("show");
            $scope.loading = true;
            console.log($scope.properties.nuevosValores);
            console.log( $scope.properties.contenido[$scope.properties.index]);
            $scope.properties.contenido[$scope.properties.index].campus = $scope.properties.nuevosValores[0].campus;
            $scope.properties.contenido[$scope.properties.index].conekta = $scope.properties.nuevosValores[0].conekta;
            $scope.properties.contenido[$scope.properties.index].mailgun = $scope.properties.nuevosValores[0].mailgun;
            $scope.properties.contenido[$scope.properties.index].crispChat = $scope.properties.nuevosValores[0].crispChat;

            $scope.properties.mostrarPantallaEditar = false;
            $scope.properties.mostrarPantallaAgregarApiKey = false;
            $scope.properties.objContrato = $scope.properties.contenido;
            $scope.properties.nuevosValores = [];
            $scope.asignarTarea()
        }
        } else {
            console.log("click doble");
        }
    }

    $scope.asignarTarea = function() {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + $scope.properties.taskId,
            data: angular.copy({ "assigned_id": "" })
        };
        return $http(req)
            .success(function(data, status) {
                redireccionarTarea();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
            })
            .finally(function() {});
    }

    function redireccionarTarea() {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + $scope.properties.taskId,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };
        console.log($scope.properties.userId);
        return $http(req)
            .success(function(data, status) {
                $scope.submitTask();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    $scope.submitTask = function() {
        
        var req = {
            method: "POST",
            url: "/bonita/API/bpm/userTask/" + $scope.properties.taskId + "/execution?assign=false",
            data: angular.copy($scope.properties.dataToSend)
        };

        return $http(req)
            .success(function(data, status) {
                console.log("$scope.properties.dataToSend");
                console.log($scope.properties.dataToSend);
                $scope.getConsulta();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
               // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    $scope.getConsulta = function() {
        var req = {
            method: "GET",
            url: $scope.properties.urlConsulta
        };

        return $http(req)
            .success(function(data, status) {
                //console.log("data");
                //console.log(data);
                $scope.properties.contenido = data;
                $scope.getObjTaskInformation();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    $scope.getObjTaskInformation = function() {
        var req = {
            method: "GET",
            url: $scope.properties.urlTaskInformation
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.objTaskInformation = data;
                $scope.loading = false;
                $("#loading").modal("hide");
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
               // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }
}
