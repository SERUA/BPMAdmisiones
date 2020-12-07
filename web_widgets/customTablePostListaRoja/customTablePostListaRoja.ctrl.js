function PbTableCtrl($scope, $http, $window) {
    var vm = this;
    this.isArray = Array.isArray;

    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function (row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    this.isSelected = function (row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    function doRequest(method, url, params) {
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                $scope.properties.lstContenido = data.data;
                console.log(data.data)
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () { });
    }

    $scope.asignarTarea = function (rowData) {
       var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseId}&f=isFailed%3dfalse`
        };

        return $http(req)
            .success(function(data, status) {
                debuggerconsole.log("RowData");
        console.log(rowData);
       var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseId}&f=isFailed%3dfalse`
        };

        return $http(req)
            .success(function(data, status) {
                debugger
                var url="https://anahuac-preproduction.bonitacloud.com/apps/administrativo/verSolicitudAdmision/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[TASKID]", data[0].id);
                window.top.location.href=url;
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {});
                var url="https://anahuac-preproduction.bonitacloud.com/apps/administrativo/verSolicitudAdmision/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[TASKID]", data[0].id);
                window.top.location.href=url;
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {});
    }

    function redireccionarTarea(rowData) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + rowData.taskId,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };

        return $http(req)
            .success(function (data, status) {
                var url = "/bonita/portal/resource/taskInstance/[NOMBREPROCESO]/[VERSIONPROCESO]/[NOMBRETAREA]/content/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[NOMBREPROCESO]", rowData.processName);
                url = url.replace("[VERSIONPROCESO]", rowData.processVersion);
                url = url.replace("[NOMBRETAREA]", rowData.taskName);
                url = url.replace("[TASKID]", rowData.taskId);
                $window.location.assign(url);
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () { });
    }

    $(function () {
        doRequest("POST", $scope.properties.urlPost);
    })


    $scope.$watch("properties.dataToSend", function (newValue, oldValue) {
        if (newValue !== undefined) {
            doRequest("POST", $scope.properties.urlPost);
        }
        console.log($scope.properties.dataToSend);
    });
    $scope.reciclar = function (rowData) {
        swal({
            title: `¿Está seguro que desea reactivar la solicitud del aspirante ${rowData.primerNombre + " " + rowData.segundoNombre + " " + rowData.apellidoPaterno + " " + rowData.apellidoMaterno}?`,
            text: "La solicitud tendrá que ser validada",
            icon: "warning",
            buttons: true,
            dangerMode: true,
            buttons: ["Cancelar", "Continuar"]
        })
            .then((willDelete) => {
                if (willDelete) {
                    doRequestCallback("PUT", `/bonita/API/bpm/humanTask/${rowData.taskId}`,{},{ "assigned_id": $scope.properties.userId },function(success,rowData){
                        doRequestCallback("POST", `/bonita/API/bpm/userTask/${rowData.taskId}/execution?assign=false`,{},{},function(success,rowData){
                            window.top.location.href=`/bonita/apps/administrativo/AppListaRoja/`
                            
                        },rowData); 
                    },rowData);
                    
                } else {

                }
            });
        
    }
    /**
   * Execute a get/post request to an URL
   * It also bind custom data from success|error to a data
   * @return {void}
   */
    function doRequestCallback(method, url, params, payload, callback,extra) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy(payload),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                callback(data,extra);
            })
            .error(function (data, status) {
                console.error(data);
            })
            .finally(function () {
                vm.busy = false;
            });
    }

}