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
                    if($scope.properties.nuevosValores[0].clave && $scope.properties.nuevosValores[0].descripcion && $scope.properties.nuevosValores[0].montoAspiranteLocal && $scope.properties.nuevosValores[0].montoAspitanteForaneo && $scope.properties.nuevosValores[0].montoAspiranteLocalDolares && $scope.properties.nuevosValores[0].montoAspiranteForaneoDolares && $scope.properties.nuevosValores[0].instruccionesDePago){
                        $("#loading").modal("show");
                        $scope.loading = true;
                        $scope.properties.nuevosValores.forEach(element =>{
                        $scope.properties.contenido.push(element);
                        
                    })
                    $scope.properties.mostrarPantallaEditar = false;
                    $scope.properties.cerrarOpcionesDePago = false;
                    $scope.properties.cerrarPantallaDePago = false;
                    $scope.properties.cerrarPantallaPagoDeshabilitado = false;
                    $scope.properties.nuevosValores = [];
                    $scope.properties.objContrato = $scope.properties.contenido;
                    $scope.properties.objProcesoCasoInput =[{
                        "campus" : $scope.properties.contenido[0].campus,
                        "proceso" : "CatPagoDeExamenDeAdmision"}];
                    $("#loading").modal("show");
                    $scope.loading = true;
                    $scope.asignarTarea()
                }else{
                    if(!$scope.properties.nuevosValores[0].clave){
                        this.validacion = "Clave";
                        $("#modalValidacion").modal("show");
                        //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                    if(!$scope.properties.nuevosValores[0].descripcion){
                        
                        this.validacion = "Descripción";
                        $("#modalValidacion").modal("show");
                         //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                    if(!$scope.properties.nuevosValores[0].montoAspiranteLocal){
                        
                        this.validacion = "Monto a pagar aspirante local";
                        $("#modalValidacion").modal("show");
                        //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                    if(!$scope.properties.nuevosValores[0].montoAspitanteForaneo){
                        
                        this.validacion = "Monto a pagar aspirante foráneo";
                        $("#modalValidacion").modal("show");
                        //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                    if(!$scope.properties.nuevosValores[0].montoAspiranteLocalDolares){
                        
                        this.validacion = "Monto a pagar aspirante local dólares";
                        $("#modalValidacion").modal("show");
                        //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                     if(!$scope.properties.nuevosValores[0].montoAspiranteForaneoDolares){
                        
                        this.validacion = "Monto a pagar aspirante foráneo dólares";
                        $("#modalValidacion").modal("show");
                        //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                    if(!$scope.properties.nuevosValores[0].instruccionesDePago){
                        
                        this.validacion = "Texto instrucciones de pago";
                        $("#modalValidacion").modal("show");
                         //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                }
            } 
             if($scope.properties.pagoExamenDeshabilitadoOHabilitado === false){
                 
               if($scope.properties.nuevosValores[0].clave && $scope.properties.nuevosValores[0].descripcion && $scope.properties.nuevosValores[0].deshabilitarPagoDeExamenDeAdmision && $scope.properties.nuevosValores[0].textoDescriptivoPagoDeshabilitado){
                    $("#loading").modal("show");
                        $scope.loading = true;
                        $scope.properties.nuevosValores.forEach(element =>{
                        $scope.properties.contenido.push(element);
                    })
                    $scope.properties.mostrarPantallaEditar = false;
                    $scope.properties.cerrarOpcionesDePago = false;
                    $scope.properties.cerrarPantallaDePago = false;
                    $scope.properties.cerrarPantallaPagoDeshabilitado = false;
                    $scope.properties.objContrato = $scope.properties.contenido;
                    $scope.properties.objProcesoCasoInput =[{
                        "campus" : $scope.properties.contenido[0].campus,
                        "proceso" : "CatPagoDeExamenDeAdmision"}];
                    
                    $scope.properties.nuevosValores = [];
                    $("#loading").modal("show");
                    $scope.loading = true;
                    $scope.asignarTarea()
               }else{
                    if(!$scope.properties.nuevosValores[0].clave){
                        this.validacion = "Clave";
                        $("#modalValidacion").modal("show");
                        //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                    if(!$scope.properties.nuevosValores[0].descripcion){
                        
                        this.validacion = "Descripción";
                        $("#modalValidacion").modal("show");
                         //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                     if(!$scope.properties.nuevosValores[0].deshabilitarPagoDeExamenDeAdmision){
                        
                        this.validacion = "Deshabilitar pago de Examen de admisión";
                        $("#modalValidacion").modal("show");
                         //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                    if(!$scope.properties.nuevosValores[0].deshabilitarPagoDeExamenDeAdmision){
                        
                        this.validacion = "Deshabilitar pago de Examen de admisión";
                        $("#modalValidacion").modal("show");
                         //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
                   if(!$scope.properties.nuevosValores[0].textoDescriptivoPagoDeshabilitado){
                        
                        this.validacion = "Texto descriptivo";
                        $("#modalValidacion").modal("show");
                         //SweetAlert.swal("Falto capturar informacion en:", this.validacion);
                    }
               }
             }
        }else{
            $("#loading").modal("show");
            $scope.loading = true;
            $scope.properties.contenido[$scope.properties.index].clave = $scope.properties.nuevosValores[0].clave;
            $scope.properties.contenido[$scope.properties.index].descripcion = $scope.properties.nuevosValores[0].descripcion;
            $scope.properties.contenido[$scope.properties.index].fechaCreacion = $scope.properties.nuevosValores[0].fechaCreacion;
            $scope.properties.contenido[$scope.properties.index].usuarioCreacion = $scope.properties.nuevosValores[0].usuarioCreacion;
            $scope.properties.contenido[$scope.properties.index].montoAspiranteLocal = $scope.properties.nuevosValores[0].montoAspiranteLocal;
            $scope.properties.contenido[$scope.properties.index].montoAspitanteForaneo = $scope.properties.nuevosValores[0].montoAspitanteForaneo;
            $scope.properties.contenido[$scope.properties.index].montoAspiranteLocalDolares = $scope.properties.nuevosValores[0].montoAspiranteLocalDolares;
            $scope.properties.contenido[$scope.properties.index].montoAspiranteForaneoDolares = $scope.properties.nuevosValores[0].montoAspiranteForaneoDolares;
            $scope.properties.contenido[$scope.properties.index].instruccionesDePago = $scope.properties.nuevosValores[0].instruccionesDePago;
            $scope.properties.contenido[$scope.properties.index].textoDescriptivoPagoDeshabilitado = $scope.properties.nuevosValores[0].textoDescriptivoPagoDeshabilitado;
            $scope.properties.contenido[$scope.properties.index].deshabilitarPagoDeExamenDeAdmision = $scope.properties.nuevosValores[0].deshabilitarPagoDeExamenDeAdmision;
        
            $scope.properties.mostrarPantallaEditar = false;
            $scope.properties.cerrarOpcionesDePago = false;
            $scope.properties.cerrarPantallaDePago = false;
            $scope.properties.cerrarPantallaPagoDeshabilitado = false;
            $scope.properties.objContrato = $scope.properties.contenido;
            $scope.properties.objProcesoCasoInput =[{
                "campus" : $scope.properties.contenido[0].campus,
                "proceso" : "CatPagoDeExamenDeAdmision"}];
                console.log($scope.properties.contenido)
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
