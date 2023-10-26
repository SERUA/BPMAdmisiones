function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
    $scope.action = function() {
        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
            $scope.properties.selectedIndex--;
        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex)) {
            if ($scope.properties.isValidStep) {
                $scope.properties.selectedIndex++;
                blockUI.start();
                submitTask();
            } else {
                swal($scope.properties.messageTitle, $scope.properties.errorMessage, "warning");
            }
        }
    }

    function submitTask() {
        var id;
        id = $scope.properties.taskId;
        if (id) {
            doRequest('POST', '../API/bpm/userTask/' + id + '/execution').then(function() {
                localStorageService.delete($window.location.href);
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }

    function doRequest(method, url) {
        let dataToSend = angular.copy($scope.properties.formOutput);
        // dataToSend.solicitudApoyoEducativoInput.pageIndex = $scope.properties.selectedIndex;
        dataToSend.isSolicitudCompletadaInput = true;

        var req = {
            method: method,
            url: url,
            data: dataToSend
        };

        return $http(req)
            .success(function(data, status) {
                window.top.location.href = window.location.protocol + "//" + window.location.hostname +"/portal/resource/app/pg_aspirante/solicitud_finalizada/content/?app=pg_aspirante";
            })
            .error(function(data, status) {
                console.log("task failed")
            });
    }

    function getCurrentTask() {
        let contador = 0;
        let limite = 99;

        let url = "/API/bpm/humanTask?c=50&f=state=ready&f=assigned_id=" + $scope.properties.userId + "&f=name=Llenar solicitud de posgrado&p=0"
        // let url = "../API/bpm/humanTask?p=0&c=10&f=caseId=" + $scope.properties.caseId + "&fstate=ready";

        var req = {
            method: "GET",
            url: url
        };

        return $http(req)
            .success(function(data, status) {
                if (data.length === 0) {
                    console.log("retry, no task found");
                    getCurrentTask();
                } else if (data[0].id === $scope.properties.taskId) {
                    console.log("retry, same id");
                    getCurrentTask();
                } else {
                    $scope.properties.taskId = data[0].id;
                    console.log("Nueva tarea", $scope.properties.taskId);
                    blockUI.stop();
                }
            })
            .error(function(data, status) {
                getCurrentTask();
                if (contador <= limite) {
                    contador++;
                    getCurrentTask();
                } else {
                    blockUI.stop();
                }
            });
    }
}