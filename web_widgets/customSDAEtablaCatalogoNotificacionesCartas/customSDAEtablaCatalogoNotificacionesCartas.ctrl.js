function PbTableCtrl($scope, $http, modalService, blockUI) {

    this.isArray = Array.isArray;
    $scope.loading = false;

    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function (row) {
        if (this.isClickable()) {
            //$scope.properties.selectedRow = row;
            console.log($scope.properties.selectedRow);
        }
    };


    this.isSelected = function (row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }


    $scope.modificarData = function (row, index) {
        debugger;
        $scope.properties.selectedToModificate = row;
        $scope.properties.selectedDataPID = row.persistenceId ? row.persistenceId + "" : "";
        $scope.properties.selectedToModificate.persistenceId_string =  row.persistenceId ? row.persistenceId + "" : "";
        $scope.properties.selectedIndex = index;
        localStorage.setItem("index", index);
    }


    $scope.deleteData = function (row, index) {
        if ($scope.loading === false) {
            $("#loading").modal("show");
            $scope.loading = true;
            $scope.properties.contenido[index].isEliminado = true;
            console.log(row);
            $scope.asignarTarea()
        } else {
            console.log("click doble");
        }
    }

    $scope.sendData = function (row, index) {
        if ($scope.loading === false) {
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

    $scope.asignarTarea = function () {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + $scope.properties.taskId,
            data: angular.copy({ "assigned_id": "" })
        };

        return $http(req)
            .success(function (data, status) {
                redireccionarTarea();
            })
            .error(function (data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () { });
    }

    function redireccionarTarea() {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + $scope.properties.taskId,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };

        return $http(req)
            .success(function (data, status) {
                $scope.submitTask();
            })
            .error(function (data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () { });
    }

    $scope.submitTask = function () {
        console.log($scope.properties.dataToSend)
        var req = {
            method: "POST",
            url: "/bonita/API/bpm/userTask/" + $scope.properties.taskId + "/execution?assign=false",
            data: angular.copy($scope.properties.dataToSend)
        };

        return $http(req).success(function (data, status) {
            $scope.getConsulta();
        })
        .error(function (data, status) {
            $("#loading").modal("hide");
            $scope.loading = false;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () { 
            
        });
    }

    $scope.getConsulta = function () {
        var req = {
            method: "GET",
            url: $scope.properties.urlConsulta
        };

        return $http(req).success(function (data, status) {
            $scope.properties.contenido = [];
            $scope.properties.contenido = data;
            $scope.getObjTaskInformation();
        })
        .error(function (data, status) {
            $("#loading").modal("hide");
            $scope.loading = false;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () { 

        });
    }

    $scope.getObjTaskInformation = function () {
        var req = {
            method: "GET",
            url: $scope.properties.urlTaskInformation
        };

        return $http(req).success(function (data, status) {
            $scope.properties.objTaskInformation = data;
            $scope.loading = false;
            $("#loading").modal("hide");
        })
        .error(function (data, status) {
            $("#loading").modal("hide");
            $scope.loading = false;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () { 

        });
    }

    $scope.preview = function (codigo) {
        blockUI.start()
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=generateHtmlSDAE&p=0&c=10",
            data: angular.copy({
                "campus": $scope.properties.campusSelected,
                "correo": "braul5497@gmail.com",
                "codigo": codigo,
                "isEnviar": false
            })
        };

        return $http(req).success(function (data, status) {
            $scope.properties.previewHtml = data.data[0]
            modalService.open("previewerid");
            $scope.$apply();
        })
        .error(function (data, status) {
            $("#loading").modal("hide");
            console.error(data)
        })
        .finally(function () {
            blockUI.stop()
        });
    }
}