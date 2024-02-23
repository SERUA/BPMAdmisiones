function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
    $scope.action = function() {
        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
            $scope.properties.selectedIndex--;
        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
            if ($scope.properties.isValidStep) {
                
                if($scope.properties.isModificar !== true){

                    // Agendar cita (cambiar de estado a horario)
                    if ($scope.properties.agendar) {
                        agendar(() => {
                            // Ejecutar tarea despues de agendar
                            blockUI.start();
                            submitTask();
                            $scope.properties.selectedIndex++;
                        });
                    }
                    else {
                        // Ejecutar tarea
                        blockUI.start();
                        submitTask();
                        $scope.properties.selectedIndex++;
                    }
                }
                else {
                    $scope.properties.selectedIndex++;
                }
            } else {

                swal($scope.properties.messageTitle, $scope.properties.errorMessage, "warning");
            }
        }
    }
    
    function agendar(success) {
        swal({
            "title": "Agendar para el día " + $scope.properties.fechaEntrevista + " de " + $scope.properties.horarioEntrevista,
            "text": "Si confirmas la operación la cita de entrevista será agendada a tu nombre para el día y horario establecido.",
            "icon": "warning",
            buttons: [
                'Cancelar',
                'Confirmar'
            ],
        }).then(function (isConfirm) {
            if (isConfirm) {
                
                if (!$scope.properties.citaHorarioPersistenceId) return;
                
                const dataToSend = {
                    "persistenceId": $scope.properties.citaHorarioPersistenceId,
                    "agendado": true,
                }
                
                var req = {
                    method: "POST",
                    url: "../API/extension/posgradosRest?url=updateAgendadoHorario",
                    data: dataToSend
                };
                
                $http(req)
                .success(success)
                .error((err) => {
                    swal("No fue posible agendar", err && err.error ? err.error : "", "error");
                })
            }
        })
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

        var req = {
            method: method,
            url: url,
            // data: angular.copy($scope.properties.formOutput)
            data: dataToSend
        };

        return $http(req)
            .success(function(data, status) {
                getCurrentTask();
                console.log("Task done")
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