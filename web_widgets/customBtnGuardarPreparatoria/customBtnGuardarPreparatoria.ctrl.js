function PbButtonCtrl($scope, $http, modalService) {
    'use strict';
    var vm = this;

    this.isArray = Array.isArray;
    $scope.loading = false;


    $scope.sendData = function() {
        if ($scope.loading == false) {
            $("#loading").modal("show");
            $scope.loading = true;
            if ($scope.properties.isModificacion === false) {

                $scope.properties.nuevosValores.forEach(element => {
                        $scope.properties.contenido.push(element);
                    })
                    //console.log("$scope.properties.contenido")
                    //console.log($scope.properties.contenido)
                $scope.properties.nuevosValores = [{ "clave": "", "descripcion": "", "pais": "", "estado": "", "ciudad": "", "isEliminado": false, "isEnabled": true, "perteneceRed": "", "persistenceId_string": "" }];
            } else {
                $scope.properties.contenido[$scope.properties.index].clave = $scope.properties.nuevosValores[0].clave;
                $scope.properties.contenido[$scope.properties.index].descripcion = $scope.properties.nuevosValores[0].descripcion;
                $scope.properties.contenido[$scope.properties.index].fechaCreacion = $scope.properties.nuevosValores[0].fechaCreacion;
                $scope.properties.contenido[$scope.properties.index].usuarioBanner = $scope.properties.nuevosValores[0].usuarioBanner;
                $scope.properties.contenido[$scope.properties.index].pais = $scope.properties.nuevosValores[0].pais;
                $scope.properties.contenido[$scope.properties.index].estado = $scope.properties.nuevosValores[0].estado;
                $scope.properties.contenido[$scope.properties.index].ciudad = $scope.properties.nuevosValores[0].ciudad;
                $scope.properties.contenido[$scope.properties.index].fechaImportacion = $scope.properties.nuevosValores[0].fechaImportacion;
                $scope.properties.contenido[$scope.properties.index].perteneceRed = $scope.properties.nuevosValores[0].perteneceRed;
                $scope.properties.nuevosValores = [{ "clave": "", "descripcion": "", "pais": "", "estado": "", "ciudad": "", "isEliminado": false, "isEnabled": true, "perteneceRed": "", "persistenceId_string": "" }];
                //console.log($scope.properties.contenido[$scope.properties.index]);
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
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }
}