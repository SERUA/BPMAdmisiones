function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
    var cont = 0;
    $scope.action = function() {
        $scope.properties.disabled = true;
        if ($scope.properties.selectedIndex === 0) {
        } else if ($scope.properties.selectedIndex === 1) {
            console.log("Modal continuar");
            debugger;
            if ($scope.properties.catSolicitudDeAdmision.catCampus.persistenceId_string === "") {
                swal("¡Campus!", "Debes seleccionar un campus donde cursarás tus estudios", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                swal("¡Licenciatura!", "Debes seleccionar una licenciatura", "warning");
            }else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                if ($scope.properties.catSolicitudDeAdmision.catPropedeutico === null) {
                    swal("¡Curso propedéutico!", "Favor de seleccionar un curso propedéutico", "warning");
                } else {
                    if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                        swal("¡Período!", "Debes seleccionar un período donde cursarás sus estudios", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás tu examen", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                        if ($scope.properties.lugarexamen === "En un estado") {
                            if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar un estado donde realizarás el examen", "warning");
                            } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar una ciudad donde realizarás el examen", "warning");
                            } else if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                //$scope.properties.selectedIndex++;
                                $scope.assignTask();
                            }
                        } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                            if ($scope.properties.catSolicitudDeAdmision.catPaisExamen === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar un país donde realizarás el examen", "warning");
                            } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar una ciudad donde realizarás el examen", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    //$scope.properties.selectedIndex++;
                                    $scope.assignTask();
                                }
                            }
                        } else {
                            //$scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                            //$scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                //$scope.properties.selectedIndex++;
                                $scope.assignTask();
                            }
                        }

                    } else {
                        swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás el examen", "warning");
                    }
                }
            } else if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                swal("¡Periodo!", "Debes seleccionar un periodo donde cursarás tus estudios", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                swal("¡Lugar de examen!", "Debes seleccionar un lugar donde cursarás tus estudios", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null || $scope.properties.catSolicitudDeAdmision.ciudadExamen === "") {
                        swal("¡Lugar de examen!", "Debes seleccionar un estado y una ciudad donde realizarás el examen", "warning");
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
                        swal("¡Lugar de examen!", "Debes seleccionar un país y una ciudad donde realizarás el examen", "warning");
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
                swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás el examen", "warning");
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
        blockUI.start();
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
                swal("¡Error!", data.message, "error");
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
        console.log($scope.properties.dataToSend.catSolicitudDeAdmisionInput);
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                getTask();
                // closeModal($scope.properties.modalid);
                // $scope.properties.selectedIndex++;
            })
            .error(function(data, status) {
                blockUI.stop();
                swal("¡Error!", data.message, "error");
                console.log("Error al avanzar tarea")
                console.log(data);
                console.log(status);
            })
            .finally(function() {
                //vm.busy = false;
            });
    }

    // function getTask(){
    //     setTimeout(function(){ 
    //         var req = {
    //         method: 'GET',
    //         url: $scope.properties.urlCurrentTask
    //     };

    //     return $http(req)
    //         .success(function(data, status) {
    //             console.log("SUCCSES")
    //             console.log(data);
    //             $scope.properties.currentTask = data;
    //         })
    //         .error(function(data, status) {
    //             console.log("Error al avanzar tarea")
    //             console.log(data);
    //             console.log(status);
    //         })
    //         .finally(function() {
    //             //vm.busy = false;
    //         });
            
    //     }, 1000);
        
    // }

    function getTask() {
        debugger;
        var req = {
            method: 'GET',
            url: $scope.properties.urlCurrentTask
        };
    
        return $http(req)
            .success(function(data, status) {
                console.log("SUCCSES")
                console.log(data);
                if(data.length > 0){
                    if(data[0].id === $scope.properties.taskId){
                        if (cont === 100) {
                            location.reload();
                        } else {
                            getTask();
                            cont++;
                        }
                    }else{
                        $scope.properties.currentTask = data;
                        topFunction();
                        closeModal($scope.properties.modalid);
                        $scope.properties.disabled = false;
                        blockUI.stop();
                        $scope.properties.selectedIndex++;
                    }
                }else{
                    if (cont === 100) {
                        location.reload();
                    } else {
                        getTask();
                        cont++;
                    }
                }
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
    
    function topFunction() {
        document.body.scrollTop = 0;
        document.documentElement.scrollTop = 0;
    }
}