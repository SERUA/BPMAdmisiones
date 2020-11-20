function PbTableCtrl($scope, $http, modalService) {

    this.isArray = Array.isArray;
    $scope.loading = false;

    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        if (this.isClickable()) {
            //$scope.properties.selectedRow = row;
            console.log($scope.properties.selectedRow);
        }
    };
    
    
    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }


   $scope.modificarData = function(row, index) {
            closeModal();
            openModal("modalInputs");
            $scope.properties.selectedToModificate = [{"clave" : "","descripcion" : "","usuarioCreacion" : "","isEliminado" : false,"fechaCreacion": "","montoAspiranteLocal": "","montoAspitanteForaneo": "","montoAspiranteLocalDolares": "","montoAspiranteForaneoDolares": "","instruccionesDePago": "","textoDescriptivoPagoDeshabilitado": "","deshabilitarPagoDeExamenDeAdmision": "", "campus": ""}];
            $scope.properties.selectedToModificate[0].clave = $scope.properties.contenido[index].clave;
 			$scope.properties.selectedToModificate[0].descripcion = $scope.properties.contenido[index].descripcion;
 			$scope.properties.selectedToModificate[0].fechaCreacion = $scope.properties.contenido[index].fechaCreacion;
 			$scope.properties.selectedToModificate[0].isEliminado = $scope.properties.contenido[index].isEliminado;
 			$scope.properties.selectedToModificate[0].usuarioCreacion = $scope.properties.contenido[index].usuarioCreacion;
 			$scope.properties.selectedToModificate[0].montoAspiranteLocal = $scope.properties.contenido[index].montoAspiranteLocal;
 			$scope.properties.selectedToModificate[0].montoAspitanteForaneo = $scope.properties.contenido[index].montoAspitanteForaneo;
 			$scope.properties.selectedToModificate[0].montoAspiranteLocalDolares = $scope.properties.contenido[index].montoAspiranteLocalDolares;
 			$scope.properties.selectedToModificate[0].montoAspiranteForaneoDolares = $scope.properties.contenido[index].montoAspiranteForaneoDolares;
 			$scope.properties.selectedToModificate[0].instruccionesDePago = $scope.properties.contenido[index].instruccionesDePago;
 			$scope.properties.selectedToModificate[0].textoDescriptivoPagoDeshabilitado = $scope.properties.contenido[index].textoDescriptivoPagoDeshabilitado;
 			$scope.properties.selectedToModificate[0].deshabilitarPagoDeExamenDeAdmision = $scope.properties.contenido[index].deshabilitarPagoDeExamenDeAdmision;
             $scope.properties.selectedToModificate[0].campus = $scope.properties.contenido[index].campus;
            $scope.properties.isModificacion = true;
            $scope.properties.index = index;
            $scope.properties.mostrarPantallaEditar = true
    }
    
    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal() {
        modalService.close();
    }
  
    $scope.deleteData = function(row, index) {

        if ($scope.loading == false) {
            $("#loading").modal("show");
            $scope.loading = true;
            $scope.properties.contenido[index].isEliminado = true;
             $scope.properties.objContrato = $scope.properties.contenido;
            console.log($scope.properties.contenido);
            $scope.asignarTarea()
        } else {
            console.log("click doble");
        }
    }

    $scope.sendData = function(row, index) {

        if ($scope.loading == false) {
            $("#loading").modal("show");
            console.log(index)
            $scope.loading = true;
            $scope.properties.contenido[index].isEnabled = !$scope.properties.contenido[index].isEnabled
            console.log(row);
            $scope.asignarTarea()
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
               // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    function redireccionarTarea() {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + $scope.properties.taskId,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };

        return $http(req)
            .success(function(data, status) {
                $scope.submitTask();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    $scope.submitTask = function() {
        console.log($scope.properties.dataToSend)
        var req = {
            method: "POST",
            url: "/bonita/API/bpm/userTask/" + $scope.properties.taskId + "/execution?assign=false",
            data: angular.copy($scope.properties.dataToSend)
        };

        return $http(req)
            .success(function(data, status) {
                $scope.getConsulta();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
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
                $scope.properties.contenido = [];
                $scope.properties.contenido = data;
                $scope.getObjTaskInformation();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
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
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }
}