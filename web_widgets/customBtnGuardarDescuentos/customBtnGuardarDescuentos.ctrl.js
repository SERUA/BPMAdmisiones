function PbButtonCtrl($scope, $http, modalService) {
    'use strict';
    var vm = this;

    this.isArray = Array.isArray;
    $scope.loading = false;
    $scope.lstContenidoRespaldo = [];

    $scope.generateObjContrato = function() {
        var obj = {};
        var resultado = [];
        var resultadoDoc = angular.copy($scope.properties.lstDocumento);
        $scope.lstCatDescuentoRespaldo = angular.copy($scope.properties.lstCatDescuento);

        $scope.properties.nuevosValores.campus = $scope.properties.campus;
        $scope.properties.nuevosValores.usuarioCreacion = $scope.properties.usuario;
        console.log($scope.properties.nuevosValores);
        console.log($scope.properties.nuevosValores.catBachilleratos === null ? "" : ($scope.properties.nuevosValores.catBachilleratos.descripcion === undefined ? "" : $scope.properties.nuevosValores.catBachilleratos.descripcion));
        if($scope.properties.nuevosValores.tipo == "Preparatoria"){
            $scope.properties.nuevosValores.ciudad = "";
            $scope.properties.nuevosValores.campana = "";
        }
        if($scope.properties.nuevosValores.tipo == "Campaña"){
            $scope.properties.nuevosValores.catBachilleratos = null
            $scope.properties.nuevosValores.ciudad = "";
        }
        if($scope.properties.nuevosValores.tipo == "Ciudad"){
            $scope.properties.nuevosValores.catBachilleratos = null
            $scope.properties.nuevosValores.campana = "";
        }
        
        $scope.properties.nuevosValores.bachillerato = ($scope.properties.nuevosValores.catBachilleratos === null ? "" : ($scope.properties.nuevosValores.catBachilleratos.descripcion === undefined ? "" : $scope.properties.nuevosValores.catBachilleratos.descripcion));
        if($scope.properties.isModificacion){
            $scope.lstCatDescuentoRespaldo[$scope.properties.index] = angular.copy($scope.properties.nuevosValores);
        }
        else{
            $scope.lstCatDescuentoRespaldo.push($scope.properties.nuevosValores);   
        }
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

    $scope.sendData = function() {
        if ($scope.loading == false) {
            $("#loading").modal("show");
            $scope.loading = true;
            if ($scope.properties.isModificacion === false) {
                $scope.properties.nuevosValores.forEach(element => {
                    $scope.properties.contenido.push(element);
                })
                $scope.properties.nuevosValores = [];
            } else {
                $scope.properties.contenido[$scope.properties.index].clave = $scope.properties.nuevosValores[0].clave;
                $scope.properties.contenido[$scope.properties.index].descripcion = $scope.properties.nuevosValores[0].descripcion;
                $scope.properties.contenido[$scope.properties.index].fechaCreacion = $scope.properties.nuevosValores[0].fechaCreacion;
                $scope.properties.contenido[$scope.properties.index].usuarioCreacion = $scope.properties.nuevosValores[0].usuarioCreacion;
                $scope.properties.nuevosValores = [];
            }

            $scope.asignarTarea()
        } else {
            console.log("click doble");
        }
    }

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal() {
        modalService.close();
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
                //console.log("$scope.properties.dataToSend");
                //console.log($scope.properties.dataToSend);
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
                closeModal();
                $("#loading").modal("hide");
                $scope.properties.nuevosValores = angular.copy($scope.properties.dataToSet);
                $scope.properties.ocultarTable = false;
                $scope.properties.isModificacion = false;
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    function uuidv4() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random() * 16 | 0,
                v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
}