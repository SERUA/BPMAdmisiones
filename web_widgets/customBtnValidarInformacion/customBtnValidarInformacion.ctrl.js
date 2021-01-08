function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;
    $scope.confirmacion = function() {
        Swal.fire({
            title: `¿Está seguro que desea ${($scope.properties.accion == "Solicitar cambios") ? "solicitar cambios al aspirante" : ($scope.properties.accion == "Rechazar solicitud") ? "rechazar la solicitud del aspirante" : ($scope.properties.accion == "Validar candidato" ? "validar la solicitud del aspirante" : "enviar la solicitud del aspirante a lista roja")}?`,
            text: "La tarea avanzará",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: '#5cb85c',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Continuar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                submitTask();
                $scope.$apply();
            }
        })
        $scope.$apply();
    }
    $scope.action = function() {
        if ($scope.properties.action === 'Remove from collection') {
            removeFromCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Add to collection') {
            addToCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Start process') {
            startProcess();
        } else if ($scope.properties.action === 'Submit task') {
            debugger;
            try {
                if ($scope.properties.dataToSend.detalleSolicitudInput.catDescuentos === undefined || Object.keys($scope.properties.dataToSend.detalleSolicitudInput.catDescuentos).length === 0) {
                    $scope.properties.dataToSend.detalleSolicitudInput.catDescuentos = null;
                }
            } catch (error) {
                $scope.properties.dataToSend.detalleSolicitudInput.catDescuentos = null;
            }
            if ($scope.properties.dataToSend.detalleSolicitudInput.descuento === undefined) {
                $scope.properties.dataToSend.detalleSolicitudInput.descuento = null;
            }
            if ($scope.properties.dataToSend.detalleSolicitudInput.observacionesDescuento === undefined) {
                $scope.properties.dataToSend.detalleSolicitudInput.observacionesDescuento = null;
            }
            if ($scope.properties.dataToSend.detalleSolicitudInput.observacionesCambio === undefined) {
                $scope.properties.dataToSend.detalleSolicitudInput.observacionesCambio = null;
            }
            if ($scope.properties.dataToSend.detalleSolicitudInput.observacionesRechazo === undefined) {
                $scope.properties.dataToSend.detalleSolicitudInput.observacionesRechazo = null;
            }
            if ($scope.properties.dataToSend.detalleSolicitudInput.observacionesListaRoja === undefined) {
                $scope.properties.dataToSend.detalleSolicitudInput.observacionesListaRoja = null;
            }
            if ($scope.properties.dataToSend.detalleSolicitudInput.catPagoDeExamenDeAdmision === undefined) {
                $scope.properties.dataToSend.detalleSolicitudInput.catPagoDeExamenDeAdmision = null;
            }
            if ($scope.properties.dataToSend.detalleSolicitudInput.idBanner === undefined) {
                $scope.properties.dataToSend.detalleSolicitudInput.idBanner = "";
            }
            if ($scope.properties.dataToSend.detalleSolicitudInput.tipoAlumno === undefined) {
                $scope.properties.dataToSend.detalleSolicitudInput.tipoAlumno = null;
            }
            if ($scope.properties.dataToSend.detalleSolicitudInput.revisado === undefined) {
                $scope.properties.dataToSend.detalleSolicitudInput.revisado = null;
            }
            if ($scope.properties.dataToSend.detalleSolicitudInput.cbCoincide === undefined) {
                $scope.properties.dataToSend.detalleSolicitudInput.cbCoincide = null
            }
            if ($scope.properties.dataToSend.detalleSolicitudInput.admisionAnahuac === undefined) {
                $scope.properties.dataToSend.detalleSolicitudInput.admisionAnahuac = null
            }
            /* descuento: $data.detalleSolicitud.descuento !== undefined ? $data.detalleSolicitud.descuento : null,
       observacionesDescuento: $data.detalleSolicitud.observacionesDescuento !== undefined ? $data.detalleSolicitud.observacionesDescuento : null,
       observacionesCambio: $data.detalleSolicitud.observacionesCambio !== undefined ? $data.detalleSolicitud.observacionesCambio : null,
       observacionesRechazo: $data.detalleSolicitud.observacionesRechazo !== undefined ? $data.detalleSolicitud.observacionesRechazo : null,
       observacionesListaRoja: $data.detalleSolicitud.observacionesListaRoja !== undefined ? $data.detalleSolicitud.observacionesListaRoja : null*/
            if ($scope.properties.accion === "Solicitar cambios") {
                $scope.properties.dataToSend.detalleSolicitudInput.catDescuentos = null;
                $scope.properties.dataToSend.detalleSolicitudInput.descuento = 0;
                $scope.properties.dataToSend.conIsInformacionValidada = false;
                $scope.properties.dataToSend.detalleSolicitudInput.promedioCoincide = null;
                $scope.properties.dataToSend.conIs100Descuento = false;
                $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                $scope.properties.dataToSend.conIsListaRoja = false;
                $scope.properties.dataToSend.conIsRechazada = false;
                if ($scope.properties.dataToSend.detalleSolicitudInput.observacionesCambio.trim().replace("<br>", "") == "" || $scope.properties.dataToSend.detalleSolicitudInput.observacionesCambio == null || $scope.properties.dataToSend.detalleSolicitudInput.observacionesCambio == undefined) {
                    swal.fire("¡Validar mensaje!", "Debe agregar mensaje para aspirante", "warning");
                } else {
                    $scope.confirmacion();
                }
                /*if ($scope.properties.dataToSend.detalleSolicitudInput.isCurpValidado === false && $scope.properties.isMexicano) {
                    swal.fire("¡Validar CURP!", "¡Debe validar la CURP del aspirante!", "warning");
                } else if ($scope.properties.dataToSend.detalleSolicitudInput.promedioCoincide === false) {
                    swal.fire("¡Validar promedio!", "¡Debe validar que el promedio del aspirante coinsida!", "warning");
                } else if ($scope.properties.dataToSend.detalleSolicitudInput.revisado === false) {
                    swal.fire("¡Validar duplicados!", "¡Debe seleccionar si reviso los duplicados!", "warning");
                } else if ($scope.properties.dataToSend.detalleSolicitudInput.tipoAlumno === null) {
                    swal.fire("¡Tipo de alumno!", "¡Debe seleccionar el tipo de alumno!", "warning");
                } else if ($scope.properties.dataToSend.detalleSolicitudInput.idBanner === null) {
                    swal.fire("idBanner!", "Debe agregar el idBanner!", "warning");
                } else if ($scope.properties.dataToSend.detalleSolicitudInput.observacionesCambio.trim() === "") {
                    swal.fire("Comentario!", "Debe agregar un comentario!", "warning");
                } else if ($scope.properties.tipoAdmision!== undefined && $scope.properties.tipoAdmision !== null) {
                    if ($scope.properties.tipoAdmision === "Individual") {
                        if ($scope.properties.objCampanaSelected === null) {
                            swal.fire("Campaña!", "Debe seleccionar una campaña!", "warning");
                        } else if ($scope.properties.dataToSend.detalleSolicitudInput.descuento === null) {
                            swal.fire("Descuento!", "Debe agregar un descuento!", "warning");
                        } else {
                            $scope.properties.dataToSend.conIsInformacionValidada = false;
                            $scope.properties.dataToSend.conIs100Descuento = false;
                            $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                            $scope.properties.dataToSend.conIsListaRoja = false;
                            $scope.properties.dataToSend.conIsRechazada = false;
                            $scope.confirmacion();
                        }
                    }
        
                } else {
                    $scope.properties.dataToSend.conIsInformacionValidada = false;
                    $scope.properties.dataToSend.conIs100Descuento = false;
                    $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                    $scope.properties.dataToSend.conIsListaRoja = false;
                    $scope.properties.dataToSend.conIsRechazada = false;
                    $scope.confirmacion();
                }*/
            } else if ($scope.properties.accion === "Rechazar solicitud") {
                $scope.properties.dataToSend.detalleSolicitudInput.catDescuentos = null;
                $scope.properties.dataToSend.detalleSolicitudInput.descuento = 0;
                $scope.properties.dataToSend.detalleSolicitudInput.promedioCoincide = null;
                $scope.properties.dataToSend.conIsInformacionValidada = true;
                $scope.properties.dataToSend.conIsListaRoja = false;
                $scope.properties.dataToSend.conIs100Descuento = false;
                $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                $scope.properties.dataToSend.conIsRechazada = true;
                if ($scope.properties.dataToSend.detalleSolicitudInput.observacionesRechazo.trim().replace("<br>", "") == "" || $scope.properties.dataToSend.detalleSolicitudInput.observacionesRechazo == null || $scope.properties.dataToSend.detalleSolicitudInput.observacionesRechazo == undefined) {
                    swal.fire("¡Validar mensaje!", "Debe agregar mensaje para aspirante", "warning");
                } else {
                    $scope.confirmacion();
                }
                /*if ($scope.properties.dataToSend.detalleSolicitudInput.isCurpValidado === false && $scope.properties.isMexicano) {
                    swal.fire("¡Validar CURP!", "¡Debe validar la CURP del aspirante!", "warning");
                } else if ($scope.properties.dataToSend.detalleSolicitudInput.promedioCoincide === false) {
                    swal.fire("¡Validar promedio!", "¡Debe validar que el promedio del aspirante coinsida!", "warning");
                } else if ($scope.properties.dataToSend.detalleSolicitudInput.revisado === false) {
                    swal.fire("¡Validar duplicados!", "¡Debe seleccionar si reviso los duplicados!", "warning");
                } else if ($scope.properties.dataToSend.detalleSolicitudInput.tipoAlumno === null) {
                    swal.fire("¡Tipo de alumno!", "¡Debe seleccionar el tipo de alumno!", "warning");
                } else if ($scope.properties.dataToSend.detalleSolicitudInput.idBanner === null) {
                    swal.fire("idBanner!", "Debe agregar el idBanner!", "warning");
                } else if ($scope.properties.dataToSend.detalleSolicitudInput.observacionesRechazo.trim() === "") {
                    swal.fire("Comentario!", "Debe agregar un comentario!", "warning");
                } else if ($scope.properties.tipoAdmision!== undefined && $scope.properties.tipoAdmision !== null) {
                    if ($scope.properties.tipoAdmision === "Individual") {
                        if ($scope.properties.objCampanaSelected === null) {
                            swal.fire("Campaña!", "Debe seleccionar una campaña!", "warning");
                        } else if ($scope.properties.dataToSend.detalleSolicitudInput.descuento === null) {
                            swal.fire("Descuento!", "Debe agregar un descuento!", "warning");
                        } else {
                            $scope.properties.dataToSend.conIsInformacionValidada = true;
                            $scope.properties.dataToSend.conIsListaRoja = false;
                            $scope.properties.dataToSend.conIs100Descuento = false;
                            $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                            $scope.properties.dataToSend.conIsRechazada = true;
                            $scope.confirmacion();
                        }
                    }
        
                } else {
                    $scope.properties.dataToSend.conIsInformacionValidada = true;
                    $scope.properties.dataToSend.conIsListaRoja = false;
                    $scope.properties.dataToSend.conIs100Descuento = false;
                    $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                    $scope.properties.dataToSend.conIsRechazada = true;
                    $scope.confirmacion();
                }*/
            } else if ($scope.properties.accion === "Validar candidato") {
                doRequestCallBack("GET", "/bonita/API/extension/AnahuacRestGet?url=getIdbanner&idbanner=" + $scope.properties.dataToSend.detalleSolicitudInput.idBanner, {}, {}, function(datos) {
                    if ($scope.properties.dataToSend.detalleSolicitudInput.isCurpValidado === false && $scope.properties.isMexicano) {
                        swal.fire("¡Validar CURP!", "¡Debe validar la CURP del aspirante!", "warning");
                    } else if ($scope.properties.dataToSend.detalleSolicitudInput.promedioCoincide === false) {
                        swal.fire("¡Validar promedio!", "¡Debe validar que el promedio del aspirante coinsida!", "warning");
                    } else if ($scope.properties.dataToSend.detalleSolicitudInput.revisado === false) {
                        swal.fire("¡Validar duplicados!", "¡Debe seleccionar si reviso los duplicados!", "warning");
                    } else if ($scope.properties.dataToSend.detalleSolicitudInput.catTipoAlumno === null) {
                        swal.fire("¡Tipo de alumno!", "¡Debe seleccionar el tipo de alumno!", "warning");
                    } else if ($scope.properties.dataToSend.detalleSolicitudInput.catResidencia === null) {
                        swal.fire("¡Residencia!", "¡Debe seleccionar la residencia del alumno!", "warning");
                    } else if($scope.properties.dataToSend.detalleSolicitudInput.catTipoAdmision === null){
                        swal.fire("¡Tipo de admisión!", "¡Debe seleccionar el tipo de admisión del alumno!", "warning");
                    } else if ($scope.properties.dataToSend.detalleSolicitudInput.idBanner === null || $scope.properties.dataToSend.detalleSolicitudInput.idBanner.length < 8) {
                        swal.fire("¡Id Banner!", ($scope.properties.dataToSend.detalleSolicitudInput.idBanner.length < 8) ? "Favor de capturar 8 dígitos para Id Banner" : "Debe agregar el Id Banner!", "warning");
                    } else if (datos.length > 0) {
                        swal.fire("¡Id Banner!", `Ya existe Id Banner ${$scope.properties.dataToSend.detalleSolicitudInput.idBanner}`, "warning");
                    } else if($scope.properties.dataToSend.detalleSolicitudInput.catTipoAdmision.clave === "AA"){
                        if($scope.properties.dataToSend.detalleSolicitudInput.admisionAnahuac){
                            $scope.properties.dataToSend.conIsInformacionValidada = true;
                            $scope.properties.dataToSend.conIsListaRoja = false;
                            $scope.properties.dataToSend.conIs100Descuento = false;
                            $scope.properties.dataToSend.conIsAdmisionAnahuac = true;
                            $scope.properties.dataToSend.conIsRechazada = false;
                            $scope.confirmacion();
                        }else{
                            swal.fire("¡Tipo de admisión!", "¡Debe validar la carta de la Admisión Anáhuac!", "warning");
                        }
                        
                    } else {
                        $scope.properties.dataToSend.conIsInformacionValidada = true;
                        $scope.properties.dataToSend.conIsListaRoja = false;
                        $scope.properties.dataToSend.conIs100Descuento = false;
                        $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                        $scope.properties.dataToSend.conIsRechazada = false;
                        $scope.confirmacion();
                    }
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    /*if ($scope.properties.dataToSend.detalleSolicitudInput.idBanner === null || $scope.properties.dataToSend.detalleSolicitudInput.idBanner.length < 8) {
                        swal.fire("¡Id Banner!", ($scope.properties.dataToSend.detalleSolicitudInput.idBanner.length < 8) ? "Favor de capturar 8 dígitos para Id Banner" : "Debe agregar el Id Banner!", "warning");
                    } else if (datos.length > 0) {
                        swal.fire("¡Id Banner!", `Ya existe Id Banner ${$scope.properties.dataToSend.detalleSolicitudInput.idBanner}`, "warning");
                    } else {
                        $scope.properties.dataToSend.detalleSolicitudInput.catPagoDeExamenDeAdmision = $scope.properties.getPagoExamen[0];
                        console.log($scope.properties.tipoAdmision);
                        console.log($scope.properties.objCampanaSelected);
                        if ($scope.properties.dataToSend.detalleSolicitudInput.catTipoAdmision.clave === "AA") {
                            if ($scope.properties.dataToSend.detalleSolicitudInput.isCurpValidado === false && $scope.properties.isMexicano) {
                                swal.fire("¡Validar CURP!", "¡Debe validar la CURP del aspirante!", "warning");
                            } else if ($scope.properties.dataToSend.detalleSolicitudInput.promedioCoincide === false) {
                                swal.fire("¡Validar promedio!", "¡Debe validar que el promedio del aspirante coinsida!", "warning");
                            } else if ($scope.properties.dataToSend.detalleSolicitudInput.revisado === false) {
                                swal.fire("¡Validar duplicados!", "¡Debe seleccionar si reviso los duplicados!", "warning");
                            } else if ($scope.properties.dataToSend.detalleSolicitudInput.catTipoAlumno.persistenceId_string === "") {
                                swal.fire("¡Tipo de alumno!", "¡Debe seleccionar el tipo de alumno!", "warning");
                            } else if ($scope.properties.dataToSend.detalleSolicitudInput.catResidencia.persistenceId_string === "") {
                                swal.fire("¡Residencia!", "¡Debe seleccionar la residencia del alumno!", "warning");
                            } else if ($scope.properties.dataToSend.detalleSolicitudInput.idBanner === null || $scope.properties.dataToSend.detalleSolicitudInput.idBanner.length < 8) {
                                swal.fire("¡Id Banner!", ($scope.properties.dataToSend.detalleSolicitudInput.idBanner.length < 8) ? "Favor de capturar 8 dígitos para Id Banner" : "¡Debe agregar el Id Banner!", "warning");
                            } else {
                                if ($scope.properties.dataToSend.detalleSolicitudInput.catTipoAlumno.persistenceId_string !== "") {
                                    $scope.properties.dataToSend.conIsInformacionValidada = true;
                                    $scope.properties.dataToSend.conIsListaRoja = false;
                                    $scope.properties.dataToSend.conIs100Descuento = false;
                                    $scope.properties.dataToSend.conIsAdmisionAnahuac = true;
                                    $scope.properties.dataToSend.conIsRechazada = false;
                                    $scope.confirmacion();
                                } else {
                                    swal.fire("¡Tipo de alumno!", "¡Debe seleccionar el tipo de alumno!", "warning");
                                }


                            }
                        } else if($scope.properties.dataToSend.detalleSolicitudInput.catTipoAdmision.persistenceId_string === ""){
                            swal.fire("¡Tipo de admisión!", "¡Debe seleccionar el tipo de admisión del alumno!", "warning");
                        }else {
                            if ($scope.properties.dataToSend.detalleSolicitudInput.isCurpValidado === false && $scope.properties.isMexicano) {
                                swal.fire("¡Validar CURP!", "¡Debe validar la CURP del aspirante!", "warning");
                            } else if ($scope.properties.dataToSend.detalleSolicitudInput.promedioCoincide === false) {
                                swal.fire("¡Validar promedio!", "¡Debe validar que el promedio del aspirante coinsida!", "warning");
                            } else if ($scope.properties.dataToSend.detalleSolicitudInput.revisado === false) {
                                swal.fire("¡Validar duplicados!", "¡Debe seleccionar si reviso los duplicados!", "warning");
                            } else if ($scope.properties.dataToSend.detalleSolicitudInput.catTipoAlumno.persistenceId_string === "") {
                                swal.fire("¡Tipo de alumno!", "¡Debe seleccionar el tipo de alumno!", "warning");
                            } else if ($scope.properties.dataToSend.detalleSolicitudInput.idBanner === null || $scope.properties.dataToSend.detalleSolicitudInput.idBanner.length < 8) {
                                swal.fire("¡Id Banner!", ($scope.properties.dataToSend.detalleSolicitudInput.idBanner.length < 8) ? "Favor de capturar 8 dígitos para idBanner" : "¡Debe agregar el idBanner!", "warning");
                            } else if ($scope.properties.tipoAdmision !== undefined && $scope.properties.tipoAdmision !== null) {
                                /*if ($scope.properties.tipoAdmision === "Individual") {
                                    if ($scope.properties.objCampanaSelected === null) {
                                        swal.fire("¡Campaña!", "¡Debe seleccionar una campaña!", "warning");
                                    } else if ($scope.properties.dataToSend.detalleSolicitudInput.descuento === null) {
                                        swal.fire("¡Descuento!", "¡Debe agregar un descuento!", "warning");
                                    } else {
                                        if ($scope.properties.dataToSend.detalleSolicitudInput.catTipoAlumno.persistenceId_string !== "") {
                                            $scope.properties.dataToSend.detalleSolicitudInput.tipoAlumno = $scope.properties.tipoAlumno;
                                            $scope.properties.dataToSend.conIsInformacionValidada = true;
                                            $scope.properties.dataToSend.conIsListaRoja = false;
                                            $scope.properties.dataToSend.conIs100Descuento = false;
                                            $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                                            $scope.properties.dataToSend.conIsRechazada = false;
                                            $scope.confirmacion();
                                        } else {
                                            swal.fire("¡Tipo de alumno!", "¡Debe seleccionar el tipo de alumno!", "warning");
                                        }

                                    }
                                } else {
                                    if ($scope.properties.dataToSend.detalleSolicitudInput.catTipoAlumno.persistenceId_string !== "") {
                                        $scope.properties.dataToSend.detalleSolicitudInput.tipoAlumno = $scope.properties.tipoAlumno;
                                        $scope.properties.dataToSend.conIsInformacionValidada = true;
                                        $scope.properties.dataToSend.conIsListaRoja = false;
                                        $scope.properties.dataToSend.conIs100Descuento = false;
                                        $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                                        $scope.properties.dataToSend.conIsRechazada = false;
                                        $scope.confirmacion();
                                    } else {
                                        swal.fire("¡Tipo de alumno!", "¡Debe seleccionar el tipo de alumno!", "warning");
                                    }

                                }
                                if ($scope.properties.dataToSend.detalleSolicitudInput.catTipoAlumno.persistenceId_string !== "") {
                                    //$scope.properties.dataToSend.detalleSolicitudInput.tipoAlumno = $scope.properties.tipoAlumno;
                                    $scope.properties.dataToSend.conIsInformacionValidada = true;
                                    $scope.properties.dataToSend.conIsListaRoja = false;
                                    $scope.properties.dataToSend.conIs100Descuento = false;
                                    $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                                    $scope.properties.dataToSend.conIsRechazada = false;
                                    $scope.confirmacion();
                                } else {
                                    swal.fire("¡Tipo de alumno!", "¡Debe seleccionar el tipo de alumno!", "warning");
                                }
                            } else {
                                if ($scope.properties.dataToSend.detalleSolicitudInput.catTipoAlumno.persistenceId_string !== "") {
                                    //$scope.properties.dataToSend.detalleSolicitudInput.tipoAlumno = $scope.properties.tipoAlumno;
                                    $scope.properties.dataToSend.conIsInformacionValidada = true;
                                    $scope.properties.dataToSend.conIsListaRoja = false;
                                    $scope.properties.dataToSend.conIs100Descuento = false;
                                    $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                                    $scope.properties.dataToSend.conIsRechazada = false;
                                    $scope.confirmacion();
                                } else {
                                    swal.fire("¡Tipo de alumno!", "¡Debe seleccionar el tipo de alumno!", "warning");
                                }

                            }
                        }
                    }*/
                })
            } else if ($scope.properties.accion === "Lista roja") {
                $scope.properties.dataToSend.detalleSolicitudInput.catDescuentos = null;
                $scope.properties.dataToSend.detalleSolicitudInput.descuento = 0;
                $scope.properties.dataToSend.detalleSolicitudInput.promedioCoincide = null;
                $scope.properties.dataToSend.conIsInformacionValidada = true;
                $scope.properties.dataToSend.conIsListaRoja = true;
                $scope.properties.dataToSend.conIs100Descuento = false;
                $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                $scope.properties.dataToSend.conIsRechazada = false;
                if ($scope.properties.dataToSend.detalleSolicitudInput.observacionesListaRoja.trim().replace("<br>", "") == "" || $scope.properties.dataToSend.detalleSolicitudInput.observacionesListaRoja == null || $scope.properties.dataToSend.detalleSolicitudInput.observacionesListaRoja == undefined) {
                    swal.fire("¡Validar mensaje!", "Debe agregar mensaje para aspirante", "warning");
                } else {
                    $scope.confirmacion();
                }

                /* if ($scope.properties.dataToSend.detalleSolicitudInput.isCurpValidado === false && $scope.properties.isMexicano) {
                     swal.fire("¡Validar CURP!", "¡Debe validar la CURP del aspirante!", "warning");
                 } else if ($scope.properties.dataToSend.detalleSolicitudInput.promedioCoincide === false) {
                     swal.fire("¡Validar promedio!", "¡Debe validar que el promedio del aspirante coinsida!", "warning");
                 } else if ($scope.properties.dataToSend.detalleSolicitudInput.revisado === false) {
                     swal.fire("¡Validar duplicados!", "¡Debe seleccionar si reviso los duplicados!", "warning");
                 } else if ($scope.properties.dataToSend.detalleSolicitudInput.tipoAlumno === null) {
                     swal.fire("¡Tipo de alumno!", "¡Debe seleccionar el tipo de alumno!", "warning");
                 } else if ($scope.properties.dataToSend.detalleSolicitudInput.idBanner === null) {
                     swal.fire("idBanner!", "Debe agregar el idBanner!", "warning");
                 } else if ($scope.properties.dataToSend.detalleSolicitudInput.observacionesListaRoja.trim() === "") {
                     swal.fire("Comentario!", "Debe agregar un comentario!", "warning");
                 } else if ($scope.properties.tipoAdmision!== undefined && $scope.properties.tipoAdmision !== null) {
                     if ($scope.properties.tipoAdmision === "Individual") {
                         if ($scope.properties.objCampanaSelected === null) {
                             swal.fire("Campaña!", "Debe seleccionar una campaña!", "warning");
                         } else if ($scope.properties.dataToSend.detalleSolicitudInput.descuento === null) {
                             swal.fire("Descuento!", "Debe agregar un descuento!", "warning");
                         } else {
                             $scope.properties.dataToSend.conIsInformacionValidada = true;
                             $scope.properties.dataToSend.conIsListaRoja = true;
                             $scope.properties.dataToSend.conIs100Descuento = false;
                             $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                             $scope.properties.dataToSend.conIsRechazada = false;
                             $scope.confirmacion();
                         }
                     }
        
                 } else {
                     $scope.properties.dataToSend.conIsInformacionValidada = true;
                     $scope.properties.dataToSend.conIsListaRoja = true;
                     $scope.properties.dataToSend.conIs100Descuento = false;
                     $scope.properties.dataToSend.conIsAdmisionAnahuac = false;
                     $scope.properties.dataToSend.conIsRechazada = false;
                     $scope.confirmacion();
                 }*/
            }

        } else if ($scope.properties.action === 'Open modal') {
            closeModal($scope.properties.closeOnSuccess);
            openModal($scope.properties.modalId);
        } else if ($scope.properties.action === 'Close modal') {
            closeModal(true);
        } else if ($scope.properties.url) {
            doRequest($scope.properties.action, $scope.properties.url);
        }
    };

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
        var id = getUrlParam('id');
        if (id) {
            var prom = doRequest('POST', '../API/bpm/process/' + id + '/instantiation', getUserParam()).then(function() {
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
    }

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.dataFromSuccess = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
                notifyParentFrame({
                    message: 'success',
                    status: status,
                    dataFromSuccess: data,
                    dataFromError: undefined,
                    responseStatusCode: status
                });
                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                closeModal($scope.properties.closeOnSuccess);
            })
            .error(function(data, status) {
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({
                    message: 'error',
                    status: status,
                    dataFromError: data,
                    dataFromSuccess: undefined,
                    responseStatusCode: status
                });
            })
            .finally(function() {
                vm.busy = false;
            });
    }

    function doRequestCallBack(method, url, params, payload, callback) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy(payload),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data)
            })
            .error(function(data, status) {
                console.error(data);
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
            return {
                'user': userId
            };
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
            var params = getUserParam();
            params.assign = $scope.properties.assign;
            doRequest('POST', '../API/bpm/userTask/' + getUrlParam('id') + '/execution', params).then(function() {

                localStorageService.delete($window.location.href);
                $scope.$apply();
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }

}