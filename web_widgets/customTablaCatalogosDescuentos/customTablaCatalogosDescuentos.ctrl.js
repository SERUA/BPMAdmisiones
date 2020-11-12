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
            $scope.properties.selectedToModificate = angular.copy(row)
            $scope.properties.isModificacion = true;
            $scope.properties.ocultarTable = true;
            $scope.properties.index = index;
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
            //$scope.properties.contenido[index].isEliminado = true;
            //console.log(row);
            //$scope.asignarTarea()
            $scope.generateObjContrato(index);
        } else {
            console.log("click doble");
        }
    }

    $scope.generateObjContrato = function(index) {
        var obj = {};
        var resultado = [];
        var resultadoDoc = angular.copy($scope.properties.lstDocumento);
        $scope.lstCatDescuentoRespaldo = angular.copy($scope.properties.lstCatDescuento);
        $scope.lstCatDescuentoRespaldo[index].isEliminado = true;
        for (var index in $scope.lstCatDescuentoRespaldo) {
            if ($scope.lstCatDescuentoRespaldo[index].fileSelected !== undefined) {
                if ($scope.lstCatDescuentoRespaldo[index].fileSelected.filename !== undefined) {
                    $scope.lstCatDescuentoRespaldo[index].fileSelected.filename = uuidv4()+$scope.lstCatDescuentoRespaldo[index].fileSelected.filename.substring($scope.lstCatDescuentoRespaldo[index].fileSelected.filename.indexOf("."));
                    resultadoDoc.push(angular.copy($scope.lstCatDescuentoRespaldo[index].fileSelected));
                }
            }
            obj["persistenceId"] = $scope.lstCatDescuentoRespaldo[index].persistenceId;
            obj["persistenceId_string"] = $scope.lstCatDescuentoRespaldo[index].persistenceId_string;
            obj["persistenceVersion"] = $scope.lstCatDescuentoRespaldo[index].persistenceVersion;
            obj["persistenceVersion_string"] = $scope.lstCatDescuentoRespaldo[index].persistenceVersion_string;
            obj["catBachilleratos"] = $scope.lstCatDescuentoRespaldo[index].catBachilleratos;
            obj["ciudad"] = $scope.lstCatDescuentoRespaldo[index].ciudad;
            obj["campana"] = $scope.lstCatDescuentoRespaldo[index].campana;
            obj["documento"] = $scope.lstCatDescuentoRespaldo[index].fileSelected === undefined ? $scope.lstCatDescuentoRespaldo[index].documento : $scope.lstCatDescuentoRespaldo[index].fileSelected.filename === undefined ? $scope.lstCatDescuentoRespaldo[index].documento : $scope.lstCatDescuentoRespaldo[index].fileSelected.filename;
            obj["convenioDescuento"] = $scope.lstCatDescuentoRespaldo[index].convenioDescuento;
            obj["inicioVigencia"] = $scope.lstCatDescuentoRespaldo[index].inicioVigencia;
            obj["finVigencia"] = $scope.lstCatDescuentoRespaldo[index].finVigencia;
            obj["tipo"] = $scope.lstCatDescuentoRespaldo[index].tipo;
            obj["descuento"] = $scope.lstCatDescuentoRespaldo[index].descuento;
            obj["usuarioCreacion"] = $scope.lstCatDescuentoRespaldo[index].usuarioCreacion;
            obj["isEliminado"] = $scope.lstCatDescuentoRespaldo[index].isEliminado;
            obj["campus"] = $scope.lstCatDescuentoRespaldo[index].campus;
            obj["bachillerato"] = $scope.lstCatDescuentoRespaldo[index].bachillerato;
            resultado.push(angular.copy(obj));
        }
        $scope.properties.objContrato = angular.copy(resultado);
        $scope.properties.objContratoDocumento = angular.copy(resultadoDoc);
        $scope.asignarTarea();
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
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
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
        console.log(")))))))===========================dataToSend");
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
                console.log(")))))))===========================data");
                console.log(data);
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