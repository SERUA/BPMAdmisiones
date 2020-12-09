function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    $scope.action = function() {
        if ($scope.properties.selectedIndex === 0) {
        } else if ($scope.properties.selectedIndex === 1) {
            console.log("Modal continuar");
            if ($scope.properties.catSolicitudDeAdmision.catCampus.persistenceId_string === "") {
                swal("¡Campus!", "Debe seleccionar un campus donde cursará sus estudios", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                swal("¡Licenciatura!", "Debe seleccionar una licenciatura", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                swal("¡Periodo!", "Debe seleccionar un periodo donde cursará sus estudios", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                swal("¡Lugar de examen!", "Debe seleccionar un lugar donde cursará sus estudios", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null || $scope.properties.catSolicitudDeAdmision.ciudadExamen === "") {
                        swal("¡Lugar de examen!", "Debe seleccionar un estado y una ciudad donde realizará el examen", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            //closeModal($scope.properties.modalid);
                            //$scope.properties.selectedIndex++;
                            $scope.assignTask();
                        }
                    }
                } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                    if ($scope.properties.catSolicitudDeAdmision.catPaisExamen === null || $scope.properties.catSolicitudDeAdmision.ciudadExamen === "") {
                        swal("¡Lugar de examen!", "Debe seleccionar un país y una ciudad donde realizará el examen", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            /*closeModal($scope.properties.modalid);
                            $scope.properties.selectedIndex++;*/
                            $scope.assignTask();
                        }
                    }
                } else {
                    //$scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                    //$scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        /*closeModal($scope.properties.modalid);
                        $scope.properties.selectedIndex++;*/
                        $scope.assignTask();
                    }
                }

            } else {
                swal("¡Lugar de examen!", "Debe seleccionar un lugar donde realizara el examen", "warning");
            }


        } else if ($scope.properties.selectedIndex === 2) {
            
        } else if ($scope.properties.selectedIndex === 3) {

        } else if ($scope.properties.selectedIndex === 4) {
            
        }

    }
    
    function openModal(modalid) {

        modalService.open(modalid);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }
    
    $scope.assignTask = function() {
        //$scope.showModal();
        let url = "../API/bpm/userTask/" + $scope.properties.taskId;

        var req = {
            method: "PUT",
            url: url,
            data: {
                "assigned_id": $scope.properties.userId
            }
        };

        return $http(req).success(function(data, status) {
                //$scope.executeTask();
                submitTask();
            })
            .error(function(data, status) {
                $scope.hideModal();
                swal("¡Error", data.message, "error");
            })
            .finally(function() {

            });
    }
	
	function submitTask() {
        var id;
        //id = getUrlParam('id');
        id = $scope.properties.taskId;
        if (id) {
            var params = getUserParam();
            //params.assign = $scope.properties.assign;
            doRequest('POST', '../API/bpm/userTask/' + id + '/execution', params).then(function() {
                localStorageService.delete($window.location.href);
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }
	
    function getUserParam() {
        var userId = getUrlParam('user');
        if (userId) {
            return {
                'user': userId
            };
        }
        return {};
    }
	
    function getUrlParam(param) {
        var paramValue = $location.absUrl().match('[//?&]' + param + '=([^&#]*)($|[&#])');
        if (paramValue) {
            return paramValue[1];
        }
        return '';
    }
	
	function doRequest(method, url, params) {
        //vm.busy = true;
        $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndex = $scope.properties.selectedIndex+1;
        console.log($scope.properties.dataToSend);
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                getTask();
                closeModal($scope.properties.modalid);
                $scope.properties.selectedIndex++;
            })
            .error(function(data, status) {
                console.log("Error al avanzar tarea")
                console.log(data);
                console.log(status);
            })
            .finally(function() {
                //vm.busy = false;
            });
    }

    function getTask(){
        setTimeout(function(){ 
            var req = {
            method: 'GET',
            url: $scope.properties.urlCurrentTask
        };

        return $http(req)
            .success(function(data, status) {
                console.log("SUCCSES")
                console.log(data);
                $scope.properties.currentTask = data;
            })
            .error(function(data, status) {
                console.log("Error al avanzar tarea")
                console.log(data);
                console.log(status);
            })
            .finally(function() {
                //vm.busy = false;
            });
            
        }, 1000);
        
    }
}