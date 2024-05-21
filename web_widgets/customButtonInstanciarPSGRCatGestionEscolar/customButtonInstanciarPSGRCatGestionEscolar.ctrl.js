function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;
    var validarPeriodoDisponible;
    var validarInscripcionagosto;
    var validarInscripcionenero;
    var validarInscripcionmayo;
    var validarInscripcionseptiembre;
    var validarPropedeuticos;
    var validarClave;
    this.action = function action() {
        if ($scope.properties.action === 'Remove from collection') {
            removeFromCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Add to collection') {
            addToCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Submit task') {
            debugger;
            if($scope.properties.dataToChange == "agregar"){
                accionCatalogoInsert($scope.properties.urlInsert)
            }else if($scope.properties.dataToChange == "editar"){
                $scope.properties.urlUpdate = $scope.properties.urlUpdate;
                accionCatalogo($scope.properties.urlUpdate) 
            } 
        } else if ($scope.properties.action === 'Start process') {
            startProcess();
        } else if ($scope.properties.action === 'Submit task') {
            submitTask();
        } else if ($scope.properties.action === 'Open modal') {
            closeModal($scope.properties.closeOnSuccess);
            openModal($scope.properties.modalId);
        } else if ($scope.properties.action === 'Close modal') {
            closeModal(true);
        } else if ($scope.properties.url) {
            doRequest($scope.properties.action, $scope.properties.url);
        }
    };

    function accionCatalogoInsert(urlInsert){
        debugger;
        vm.busy = true;
        $scope.properties.dataToSend.lstCatGestionEscolarInput[0].CAMPUS = $scope.properties.campusSelected;
        $scope.properties.dataToSend.lstCatGestionEscolarInput[0].grado = $scope.properties.gradoSelected;

        $http.post(urlInsert, $scope.properties.dataToSend).success(function(_response){
            swal("OK", "Guardado correctamente", "success");
            $scope.properties.dataToChange = "tabla"
        }).error(function(_response){
            swal("¡Algo ha fallado!", _response.error, "error");
        }).finally(function(){
            vm.busy = false;
        });
    }

    function accionCatalogo(urlUpdate){
        debugger;
        vm.busy = true;
        $http.post(urlUpdate, $scope.properties.dataToSend).success(function(_response){
            swal("OK", "Guardado correctamente", "success");
            doRequest($scope.properties.urlPost)
            $scope.properties.dataToChange = "tabla";
        }).error(function(_response){
            swal("¡Algo ha fallado!", _response.error, "error");
        }).finally(function(){
            vm.busy = false;
        });
    }

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }

    function removeFromCollection() {
        if ($scope.properties.collectionToModify) {
            if (!Array.isArray($scope.properties.collectionToModify)) {
                throw 'Collection property for widget button should be an array, but was ' + $scope.properties.collectionToModify;
            }
            var index = -1;
            if ($scope.properties.collectionPosition === 'First') {
                index = 0;
            } else if ($scope.properties.collectionPosition === 'Last') {
                index = $scope.properties.collectionToModify.length - 1;
            } else if ($scope.properties.collectionPosition === 'Item') {
                index = $scope.properties.collectionToModify.indexOf($scope.properties.removeItem);
            }

            // Only remove element for valid index
            if (index !== -1) {
                $scope.properties.collectionToModify.splice(index, 1);
            }
        }
    }

    function addToCollection() {
        if (!$scope.properties.collectionToModify) {
            $scope.properties.collectionToModify = [];
        }
        if (!Array.isArray($scope.properties.collectionToModify)) {
            throw 'Collection property for widget button should be an array, but was ' + $scope.properties.collectionToModify;
        }
        var item = angular.copy($scope.properties.valueToAdd);

        if ($scope.properties.collectionPosition === 'First') {
            $scope.properties.collectionToModify.unshift(item);
        } else {
            $scope.properties.collectionToModify.push(item);
        }
    }

    function startProcess() {

        if ($scope.properties.dataToChange2.campus) { // validacion editar
            if ($scope.properties.dataToChange2.nombre && $scope.properties.dataToChange2.tipoLicenciatura && $scope.properties.dataToChange2.tipoCentroEstudio) {
                $scope.properties.dataToChange2.urlImgLicenciatura = ($scope.properties.dataToChange2.urlImgLicenciatura != null && $scope.properties.dataToChange2.urlImgLicenciatura != undefined) ? $scope.properties.dataToChange2.urlImgLicenciatura : "";
                //if ($scope.properties.dataToChange2.nombre && $scope.properties.dataToChange2.descripcion) {
                $scope.properties.dataToChange2.propedeuticos = [];
                $scope.properties.dataToChange2.matematicas = false;
                $scope.properties.dataToChange2.caseId = "";
                $scope.properties.dataToChange2.isMedicina = ($scope.properties.dataToChange2.isMedicina == null ? false : $scope.properties.dataToChange2.isMedicina);
                if ($scope.properties.dataToChange2.propedeutico === false) {
                    //variable para controlar lo de tipo licenciatura
                    var validadosTipoLicenciatura = false;

                    $scope.properties.dataToChange2.propedeuticos = [];
                    if ($scope.properties.dataToChange2.periodoDisponible < 0) {
                        validarPeriodoDisponible = false
                        swal("¡Seleccione por lo menos un!", "Periodo disponible", "warning");
                    } else {
                        validarPeriodoDisponible = true;
                    }
                    //VALIDACIONES POR TIPO DE LICENCIATURA
                    if ($scope.properties.dataToChange2.tipoLicenciatura === "Semestral") {
                        if (parseInt($scope.properties.dataToChange2.inscripcionagosto) >= 0) {
                            validarInscripcionagosto = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de agosto", "warning");
                            validarInscripcionagosto = false;
                        }
                        if (parseInt($scope.properties.dataToChange2.inscripcionenero) >= 0) {
                            validarInscripcionenero = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de enero", "warning");
                            validarInscripcionenero = false;
                        }
                        if (validarInscripcionenero && validarInscripcionagosto) {
                            validadosTipoLicenciatura = true;
                        }
                    } else if ($scope.properties.dataToChange2.tipoLicenciatura === "Cuatrimestral") {
                        if (parseInt($scope.properties.dataToChange2.inscripcionSeptiembre) >= 0) {
                            validarInscripcionseptiembre = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de septiembre ", "warning");
                            validarInscripcionseptiembre = false;
                        }
                        if (parseInt($scope.properties.dataToChange2.inscripcionMayo) >= 0) {
                            validarInscripcionmayo = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de mayo ", "warning");
                            validarInscripcionmayo = false;
                        }
                        if (parseInt($scope.properties.dataToChange2.inscripcionenero) >= 0) {
                            validarInscripcionenero = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de enero", "warning");
                            validarInscripcionenero = false;
                        }
                        if (validarInscripcionenero && validarInscripcionmayo && validarInscripcionseptiembre) {
                            validadosTipoLicenciatura = true;
                        }
                    } else if ($scope.properties.dataToChange2.tipoLicenciatura == "Anual") {
                        if (parseInt($scope.properties.dataToChange2.inscripcionagosto) >= 0) {
                            validarInscripcionagosto = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de agosto", "warning");
                            validarInscripcionagosto = false;
                        }
                        if (validarInscripcionagosto) {
                            validadosTipoLicenciatura = true;
                        }
                    }
                    if (validarPeriodoDisponible === true && validadosTipoLicenciatura) {
                        if ($scope.properties.processId) {
                            var req = {
                                method: 'GET',
                                url: `/API/extension/AnahuacRestGet?url=getValidarClave&p=0&c=10&tabla=CATGESTIONESCOLAR&clave=${$scope.properties.dataToChange2.clave}&id=${$scope.properties.dataToChange2.persistenceId}`,
                            };
                            return $http(req).success(function(data, status) {
                                if (data.data[0] === true) {
                                    var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                                        doRequest("GET", $scope.properties.url).then(function() {
                                            $scope.properties.dataToChange = $scope.properties.dataToSet;
                                            $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                                        });
                                        localStorageService.delete($window.location.href);
                                    });
                                } else {
                                    swal("¡Aviso!", "La clave capturada ya existe, por favor ingrese una diferente.", "warning");
                                }

                            }).error(function(data, status) {})


                        } else {
                            $log.log('Impossible to retrieve the process definition id value from the URL');
                        }
                    }
                }
                if ($scope.properties.dataToChange2.propedeutico === true) {
                    //variable para controlar lo de tipo licenciatura
                    var validadosTipoLicenciatura = false;
                    /*if ($scope.properties.dataToChange2.periodoDisponible < 0) {
                        validarPeriodoDisponible = false
                        swal("¡Seleccione por lo menos un!", "Periodo disponible", "warning");
                    } else {
                        validarPeriodoDisponible = true;
                    }*/
                    validarPeriodoDisponible = true;
                    //VALIDACIONES POR TIPO DE LICENCIATURA
                    if ($scope.properties.dataToChange2.tipoLicenciatura === "Semestral") {
                        if (parseInt($scope.properties.dataToChange2.inscripcionagosto) >= 0) {
                            validarInscripcionagosto = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de agosto", "warning");
                            validarInscripcionagosto = false;
                        }
                        if (parseInt($scope.properties.dataToChange2.inscripcionenero) >= 0) {
                            validarInscripcionenero = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de enero", "warning");
                            validarInscripcionenero = false;
                        }
                        if (validarInscripcionenero && validarInscripcionagosto) {
                            validadosTipoLicenciatura = true;
                        }
                    } else if ($scope.properties.dataToChange2.tipoLicenciatura === "Cuatrimestral") {
                        if (parseInt($scope.properties.dataToChange2.inscripcionSeptiembre) >= 0) {
                            validarInscripcionseptiembre = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de septiembre ", "warning");
                            validarInscripcionseptiembre = false;
                        }
                        if (parseInt($scope.properties.dataToChange2.inscripcionMayo) >= 0) {
                            validarInscripcionmayo = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de mayo ", "warning");
                            validarInscripcionmayo = false;
                        }
                        if (parseInt($scope.properties.dataToChange2.inscripcionenero) >= 0) {
                            validarInscripcionenero = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de enero", "warning");
                            validarInscripcionenero = false;
                        }
                        if (validarInscripcionenero && validarInscripcionmayo && validarInscripcionseptiembre) {
                            validadosTipoLicenciatura = true;
                        }
                    } else if ($scope.properties.dataToChange2.tipoLicenciatura == "Anual") {
                        if (parseInt($scope.properties.dataToChange2.inscripcionagosto) >= 0) {
                            validarInscripcionagosto = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de agosto", "warning");
                            validarInscripcionagosto = false;
                        }
                        if (validarInscripcionagosto) {
                            validadosTipoLicenciatura = true;
                        }
                    }

                    /*if ($scope.properties.dataToChange2.propedeuticos <= 0) {
                        swal("¡Seleccione por lo menos un!", "Propedéutico disponible", "warning");
                        validarPropedeuticos = false;
                    } else {
                        validarPropedeuticos = true;
                    }*/
                    validarPropedeuticos = true;
                    if (validarPeriodoDisponible === true && validadosTipoLicenciatura && validarPropedeuticos === true) {
                        if ($scope.properties.processId) {
                            var req = {
                                method: 'GET',
                                url: `/API/extension/AnahuacRestGet?url=getValidarClave&p=0&c=10&tabla=CATGESTIONESCOLAR&clave=${$scope.properties.dataToChange2.clave}&id=${$scope.properties.dataToChange2.persistenceId}`,
                            };
                            return $http(req).success(function(data, status) {
                                if (data.data[0] === true) {
                                    var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                                        doRequest("GET", $scope.properties.url).then(function() {
                                            $scope.properties.dataToChange = $scope.properties.dataToSet;
                                            $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                                        });
                                        localStorageService.delete($window.location.href);
                                    });
                                } else {
                                    swal("¡Aviso!", "La clave capturada ya existe, por favor ingrese una diferente.", "warning");
                                }

                            }).error(function(data, status) {})



                        } else {
                            $log.log('Impossible to retrieve the process definition id value from the URL');
                        }
                    }
                }
            } else {
                if (!$scope.properties.dataToChange2.tipoLicenciatura) {
                    swal("¡Aviso!", "Faltó capturar información en: Tipo licenciatura", "warning");
                }
                if (!$scope.properties.dataToChange2.tipoCentroEstudio) {
                    swal("¡Aviso!", "Faltó capturar información en: Tipo de centro de estudio", "warning");
                }
                if (!$scope.properties.dataToChange2.enlace) {
                    swal("¡Aviso!", "Faltó capturar información en: Enlace de sitio web", "warning");
                }
                if (!$scope.properties.dataToChange2.nombre) {
                    swal("¡Aviso!", "Faltó capturar información en: Nombre licenciatura", "warning");
                }
                if (!$scope.properties.dataToChange2.clave) {
                    swal("¡Aviso!", "Faltó capturar informacion en: Clave", "warning");
                }
                /*if(duplicados($scope.properties.dataToChange2.clave,2,false,$scope.properties.dataToChange2.persistenceId)){
                	 swal("¡Aviso!", "La Clave se encuantra duplicada", "warning");
                }*/
            }
        } else { // validacion guardar
            //if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].nombre && $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].descripcion) 
            if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].nombre && $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].tipoLicenciatura && $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].tipoCentroEstudio) {
                $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].urlImgLicenciatura = ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].urlImgLicenciatura != null && $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].urlImgLicenciatura != undefined) ? $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].urlImgLicenciatura : "";
                $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].propedeuticos = [];
                $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].matematicas = false;
                $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].caseId = "";
                $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].isMedicina = ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].isMedicina == null ? false : $scope.properties.dataToChange2.lstCatGestionEscolarInput[0].isMedicina);
                if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].propedeutico === false) {
                    //if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].periodoDisponible <= 0) {
                    //variable para controlar lo de tipo licenciatura
                    var validadosTipoLicenciatura = false;
                    if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].periodoDisponible < 0) {
                        validarPeriodoDisponible = false
                        swal("¡Seleccione por lo menos un!", "Periodo disponible", "warning");
                    } else {
                        validarPeriodoDisponible = true;
                    }
                    //VALIDACIONES POR TIPO DE LICENCIATURA
                    if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].tipoLicenciatura === "Semestral") {
                        if (parseInt($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionagosto) >= 0) {
                            validarInscripcionagosto = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de agosto", "warning");
                            validarInscripcionagosto = false;
                        }
                        if (parseInt($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionenero) >= 0) {
                            validarInscripcionenero = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de enero", "warning");
                            validarInscripcionenero = false;
                        }
                        if (validarInscripcionenero && validarInscripcionagosto) {
                            validadosTipoLicenciatura = true;
                        }
                    } else if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].tipoLicenciatura === "Cuatrimestral") {
                        if (parseInt($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionSeptiembre) >= 0) {
                            validarInscripcionseptiembre = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de septiembre ", "warning");
                            validarInscripcionseptiembre = false;
                        }
                        if (parseInt($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionMayo) >= 0) {
                            validarInscripcionmayo = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de mayo ", "warning");
                            validarInscripcionmayo = false;
                        }
                        if (parseInt($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionenero) >= 0) {
                            validarInscripcionenero = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de enero", "warning");
                            validarInscripcionenero = false;
                        }
                        if (validarInscripcionenero && validarInscripcionmayo && validarInscripcionseptiembre) {
                            validadosTipoLicenciatura = true;
                        }
                    } else if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].tipoLicenciatura == "Anual") {
                        if (parseInt($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionagosto) >= 0) {
                            validarInscripcionagosto = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de agosto", "warning");
                            validarInscripcionagosto = false;
                        }
                        if (validarInscripcionagosto) {
                            validadosTipoLicenciatura = true;
                        }
                    }
                    if (validarPeriodoDisponible === true && validadosTipoLicenciatura) {
                        if ($scope.properties.processId) {
                            var req = {
                                method: 'GET',
                                url: `/API/extension/AnahuacRestGet?url=getValidarClave&p=0&c=10&tabla=CATGESTIONESCOLAR&clave=${$scope.properties.dataToChange2.lstCatGestionEscolarInput[0].clave}&id=`,
                            };
                            return $http(req).success(function(data, status) {
                                if (data.data[0] === true) {
                                    var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                                        doRequest("GET", $scope.properties.url).then(function() {
                                            $scope.properties.dataToChange = $scope.properties.dataToSet;
                                            $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                                        });
                                        localStorageService.delete($window.location.href);
                                    });
                                } else {
                                    swal("¡Aviso!", "La clave capturada ya existe, por favor ingrese una diferente.", "warning");
                                }

                            }).error(function(data, status) {})




                        } else {
                            $log.log('Impossible to retrieve the process definition id value from the URL');
                        }
                    }
                }
                if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].propedeutico === true) {
                    //if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].periodoDisponible <= 0) {
                    //variable para controlar lo de tipo licenciatura
                    var validadosTipoLicenciatura = false;
                    /*if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].periodoDisponible < 0) {
                        validarPeriodoDisponible = false
                        swal("¡Seleccione por lo menos un!", "Periodo disponible", "warning");
                    } else {
                        validarPeriodoDisponible = true;
                    }*/
                    validarPeriodoDisponible = true;
                    //VALIDACIONES POR TIPO DE LICENCIATURA
                    if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].tipoLicenciatura === "Semestral") {
                        if (parseInt($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionagosto) >= 0) {
                            validarInscripcionagosto = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de agosto", "warning");
                            validarInscripcionagosto = false;
                        }
                        if (parseInt($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionenero) >= 0) {
                            validarInscripcionenero = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de enero", "warning");
                            validarInscripcionenero = false;
                        }
                        if (validarInscripcionenero && validarInscripcionagosto) {
                            validadosTipoLicenciatura = true;
                        }
                    } else if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].tipoLicenciatura === "Cuatrimestral") {
                        if (parseInt($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionSeptiembre) >= 0) {
                            validarInscripcionseptiembre = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de septiembre ", "warning");
                            validarInscripcionseptiembre = false;
                        }
                        if (parseInt($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionMayo) >= 0) {
                            validarInscripcionmayo = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de mayo ", "warning");
                            validarInscripcionmayo = false;
                        }
                        if (parseInt($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionenero) >= 0) {
                            validarInscripcionenero = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de enero", "warning");
                            validarInscripcionenero = false;
                        }
                        if (validarInscripcionenero && validarInscripcionmayo && validarInscripcionseptiembre) {
                            validadosTipoLicenciatura = true;
                        }
                    } else if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].tipoLicenciatura == "Anual") {
                        if (parseInt($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].inscripcionagosto) >= 0) {
                            validarInscripcionagosto = true;
                        } else {
                            swal("¡Aviso!", "Faltó capturar información en: Inscripción de agosto", "warning");
                            validarInscripcionagosto = false;
                        }
                        if (validarInscripcionagosto) {
                            validadosTipoLicenciatura = true;
                        }
                    }
                    /*if ($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].propedeuticos <= 0) {
                        swal("¡Seleccione por lo menos un!", "Propedéutico disponible", "warning");
                        validarPropedeuticos = false;
                    } else {
                        validarPropedeuticos = true;
                    }*/
                    validarPropedeuticos = true;
                    if (validarPeriodoDisponible === true && validadosTipoLicenciatura && validarPropedeuticos === true) {
                        if ($scope.properties.processId) {
                            var req = {
                                method: 'GET',
                                url: `/API/extension/AnahuacRestGet?url=getValidarClave&p=0&c=10&tabla=CATGESTIONESCOLAR&clave=${$scope.properties.dataToChange2.lstCatGestionEscolarInput[0].clave}&id=`,
                            };

                            return $http(req).success(function(data, status) {
                                if (data.data[0] === true) {
                                    var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                                        doRequest("GET", $scope.properties.url).then(function() {
                                            $scope.properties.dataToChange = $scope.properties.dataToSet;
                                            $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                                        });
                                        localStorageService.delete($window.location.href);
                                    });
                                } else {
                                    swal("¡Aviso!", "La clave capturada ya existe, por favor ingrese una diferente.", "warning");
                                }

                            }).error(function(data, status) {})


                        } else {
                            $log.log('Impossible to retrieve the process definition id value from the URL');
                        }
                    }
                }
            } else {
                if (!$scope.properties.dataToChange2.lstCatGestionEscolarInput[0].tipoLicenciatura) {
                    swal("¡Aviso!", "Faltó capturar información en: Tipo licenciatura", "warning");
                }
                if (!$scope.properties.dataToChange2.lstCatGestionEscolarInput[0].tipoCentroEstudio) {
                    swal("¡Aviso!", "Faltó capturar información en: Tipo de centro de estudio", "warning");
                }
                if (!$scope.properties.dataToChange2.lstCatGestionEscolarInput[0].enlace) {
                    swal("¡Aviso!", "Faltó capturar información en: Enlace de sitio web", "warning");
                }
                if (!$scope.properties.dataToChange2.lstCatGestionEscolarInput[0].nombre) {
                    swal("¡Aviso!", "Faltó capturar información en: Nombre licenciatura", "warning");
                }
                if (!$scope.properties.dataToChange2.lstCatGestionEscolarInput[0].clave) {
                    swal("¡Aviso!", "Faltó capturar información en: clave", "warning");
                }
                /*if(duplicados($scope.properties.dataToChange2.lstCatGestionEscolarInput[0].clave,2,true)){
                	 swal("¡Aviso!", "La Clave se encuantra duplicada", "warning");
                }*/
            }
        }
    }

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params) {
        ddebugger;
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req).success(function(data, status) {
                $scope.properties.dataFromSuccess = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
                notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status });
                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                closeModal($scope.properties.closeOnSuccess);
            })
            .error(function(data, status) {
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                vm.busy = false;
            });
    }

    function redirectIfNeeded() {
        var iframeId = $window.frameElement ? $window.frameElement.id : null;
        //Redirect only if we are not in the portal or a living app
        if (!iframeId || iframeId && iframeId.indexOf('bonitaframe') !== 0) {
            $window.location.assign($scope.properties.targetUrlOnSuccess);
        }
    }

    function notifyParentFrame(additionalProperties) {
        if ($window.parent !== $window.self) {
            var dataToSend = angular.extend({}, $scope.properties, additionalProperties);
            $window.parent.postMessage(JSON.stringify(dataToSend), '*');
        }
    }

    function getUserParam() {
        var userId = getUrlParam('user');
        if (userId) {
            return { 'user': userId };
        }
        return {};
    }

    /**
     * Extract the param value from a URL query
     * e.g. if param = "id", it extracts the id value in the following cases:
     *  1. http://localhost/bonita/portal/resource/process/ProcName/1.0/content/?id=8880000
     *  2. http://localhost/bonita/portal/resource/process/ProcName/1.0/content/?param=value&id=8880000&locale=en
     *  3. http://localhost/bonita/portal/resource/process/ProcName/1.0/content/?param=value&id=8880000&locale=en#hash=value
     * @returns {id}
     */
    function getUrlParam(param) {
        var paramValue = $location.absUrl().match('[//?&]' + param + '=([^&#]*)($|[&#])');
        if (paramValue) {
            return paramValue[1];
        }
        return '';
    }

    function submitTask() {
        var id;
        id = getUrlParam('id');
        if (id) {
            var params = $scope.properties.userId;
            params.assign = $scope.properties.assign;
            doRequest('POST', '../API/bpm/userTask/' + getUrlParam('id') + '/execution', params).then(function() {
                localStorageService.delete($window.location.href);
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }

    function ValidateEmail(mail) {
        let res = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        return res.test(mail);
    }

    function validateURL(textval) {
        var urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
        return urlregex.test(textval);
    }

    /*function duplicados(datos,eleccion,tipo,id){
    	var i = 0, iguales = false, count = 0;
    	switch (eleccion){
    		case 1:
    		    var datos
    			for (i = 0; i < $scope.properties.lstContenido.length; i++) {
    				if(tipo){
    					if( $scope.properties.lstContenido[i].id !=undefined && $scope.properties.lstContenido[i].id != null && $scope.properties.lstContenido[i].id.toString().toLocaleLowerCase().normalize() === datos.toString().toLocaleLowerCase().normalize()){
    						count++;
    					}
    				}else{
    					
    					if($scope.properties.lstContenido[i].persistenceId != id && $scope.properties.lstContenido[i].id !=undefined && $scope.properties.lstContenido[i].id != null && $scope.properties.lstContenido[i].id.toString().toLocaleLowerCase().normalize() === datos.toString().toLocaleLowerCase().normalize()){
    						count++;
    					}
    				}
    			}
    			iguales =  (count>0 ?true:false);
    		break;
    		case 2:
    			for (i = 0; i < $scope.properties.lstContenido.length; i++) {
    				if(tipo){
    					if( $scope.properties.lstContenido[i].clave !=undefined && $scope.properties.lstContenido[i].clave != null && $scope.properties.lstContenido[i].clave.toString().toLocaleLowerCase().normalize() === datos.toString().toLocaleLowerCase().normalize()){
    						count++;
    					}
    				}else{
    					
    					if($scope.properties.lstContenido[i].persistenceId != id && $scope.properties.lstContenido[i].clave !=undefined && $scope.properties.lstContenido[i].clave != null &&$scope.properties.lstContenido[i].clave.toString().toLocaleLowerCase().normalize() === datos.toString().toLocaleLowerCase().normalize()){
    						count++;
    					}
    				}
    			}
    			iguales =  (count>0 ?true:false);
    		break;
    		case 3:
    			for (i = 0; i < $scope.properties.lstContenido.length; i++) {
    				if(tipo){
    					if($scope.properties.lstContenido[i].orden !=undefined && $scope.properties.lstContenido[i].orden != null && $scope.properties.lstContenido[i].orden.toString().normalize() === datos.toString().normalize()){
    						count++;
    					}
    				}else{
    					if($scope.properties.lstContenido[i].persistenceId != id && $scope.properties.lstContenido[i].orden !=undefined && $scope.properties.lstContenido[i].orden != null && $scope.properties.lstContenido[i].orden.toString().normalize() === datos.toString().normalize()){
    						count++;
    					}
    				}
    			}
    			iguales =  (count>0 ?true:false);
    		break;
    	}
    	
    	return iguales;
    }*/



    function doRequestValidar(clave, id) {
        vm.busy = true;
        var req = {
            method: 'GET',
            url: `/API/extension/AnahuacRestGet?url=getValidarClave&p=0&c=10&tabla=CATGESTIONESCOLAR&clave=${clave}&id=${id}`,
        };

        return $http(req).success(function(data, status) {

                validarClave = data.data[0];
            })
            .error(function(data, status) {})
            .finally(function() {
                vm.busy = false;
            });
    }
}