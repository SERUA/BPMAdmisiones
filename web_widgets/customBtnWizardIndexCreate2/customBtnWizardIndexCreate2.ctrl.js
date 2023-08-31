function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI, $filter) {
    var cont = 0;
    $scope.action = function() {
        const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        $scope.properties.disabled = true;
        var localS = localStorage;
        console.log("boton de siguiente");
        console.log($scope.properties.collageBoard);
        if ($scope.properties.selectedIndex === 0) {
            console.log("validar 0");
            if ($scope.properties.catSolicitudDeAdmision.catCampus.persistenceId_string === "") {
                swal($filter('translate')("¡Campus!"), $filter('translate')("Debes seleccionar un campus donde cursarás sus estudios"), "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                swal($filter('translate')("¡Licenciatura!"), $filter('translate')("Debes seleccionar una licenciatura"), "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                if ($scope.properties.catSolicitudDeAdmision.catPropedeutico === null) {
                    swal($filter('translate')("¡Curso propedéutico!"), $filter('translate')("Favor de seleccionar un curso propedéutico"), "warning");
                } else {
                    if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                        swal($filter('translate')("¡Período!"), $filter('translate')("Debes seleccionar un período donde cursarás sus estudios"), "warning");
                    }
                    /*else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                                           swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un lugar donde realizarás tu examen"), "warning");
                                       } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                                           if ($scope.properties.lugarexamen === "En un estado") {
                                               if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null) {
                                                   swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un estado donde realizarás el examen"), "warning");
                                               } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                                   swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar una ciudad donde realizarás el examen"), "warning");
                                               } else if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                   $scope.properties.selectedIndex--;
                                               } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                   $scope.properties.selectedIndex++;
                                                   //$scope.assignTask();
                                               }
                                           } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                                               if ($scope.properties.catSolicitudDeAdmision.catPaisExamen === null) {
                                                   swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un país donde realizarás el examen"), "warning");
                                               } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                                   swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar una ciudad donde realizarás el examen"), "warning");
                                               } else {
                                                   if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                       $scope.properties.selectedIndex--;
                                                   } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                       $scope.properties.selectedIndex++;
                                                       //$scope.assignTask();
                                                   }
                                               }
                                           } else {
                                               //$scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                                               //$scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                                               if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                   $scope.properties.selectedIndex--;
                                               } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                   $scope.properties.selectedIndex++;
                                                   //$scope.assignTask();
                                               }
                                           }

                                       } else {
                                           swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un lugar donde realizarás el examen"), "warning");
                                       }*/
                }
            } else if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                swal($filter('translate')("¡Período!"), $filter('translate')("Debes seleccionar un período donde cursarás sus estudios"), "warning");
            }
            /* else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                            swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un lugar donde realizarás tu examen"), "warning");
                        } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                            if ($scope.properties.lugarexamen === "En un estado") {
                                if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null) {
                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un estado donde realizarás el examen"), "warning");
                                } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar una ciudad donde realizarás el examen"), "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                    }
                                }
                            } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                                if ($scope.properties.catSolicitudDeAdmision.catPaisExamen === null) {
                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un país donde realizarás el examen"), "warning");
                                } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar una ciudad donde realizarás el examen"), "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                    }
                                }
                            } else {
                                //$scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                                //$scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.selectedIndex++;
                                    //$scope.assignTask();
                                }
                            }

                        } else {
                            swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un lugar donde realizarás el examen"), "warning");
                        }*/
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
                //$scope.assignTask();
            }
        } else if ($scope.properties.selectedIndex === 1) {

            console.log("validar 1");
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }*/
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.catSolicitudDeAdmision.primerNombre === "") {
                swal($filter('translate')("¡Nombre!"), $filter('translate')("Debes ingresar tu primer nombre"), "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.apellidoPaterno === "") {
                swal($filter('translate')("¡Apellido paterno!"), $filter('translate')("Debes ingresar tu apellido paterno"), "warning");
            }
            /* else if ($scope.properties.catSolicitudDeAdmision.apellidoMaterno === "") {
                            swal("¡Apellido materno!", "Debes ingresar tu apellido materno", "warning");
                        }*/
            else if ($scope.properties.catSolicitudDeAdmision.correoElectronico === "") {
                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes ingresar tu correo electrónico"), "warning");
            } else if (!re.test(String($scope.properties.catSolicitudDeAdmision.correoElectronico.trim()).toLowerCase())) {
                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                swal($filter('translate')("¡Licenciatura!"), $filter('translate')("Debes seleccionar una licenciatura"), "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                if ($scope.properties.catSolicitudDeAdmision.catPropedeutico === null) {
                    swal($filter('translate')("¡Examen propedéutico!"), $filter('translate')("Favor de seleccionar un examen propedéutico"), "warning");
                } else {
                    /*if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                        swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un lugar donde realizarás tu examen"), "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.avisoPrivacidad === false) {
                        swal($filter('translate')("¡Aviso de privacidad!"), $filter('translate')("Debes aceptar el aviso de privacidad"), "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                        if ($scope.properties.lugarexamen === "En un estado") {
                            if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un estado y una ciudad donde realizarás el examen"), "warning");
                            } else {



                                openModal($scope.properties.modalid);
                            }
                        } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                            if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un país y una ciudad donde realizarás el examen"), "warning");
                            } else {
                                openModal($scope.properties.modalid);
                            }
                        } else {
                            openModal($scope.properties.modalid);
                        }
                    }*/
                    openModal($scope.properties.modalid);
                }
            }
            /* else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                            swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un lugar donde realizarás tu examen"), "warning");
                        } else if ($scope.properties.catSolicitudDeAdmision.avisoPrivacidad === false) {
                            swal($filter('translate')("¡Aviso de privacidad!"), $filter('translate')("Debes aceptar el aviso de privacidad"), "warning");
                        } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                            if ($scope.properties.lugarexamen === "En un estado") {
                                if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un estado y una ciudad donde realizarás el examen"), "warning");
                                } else {
                                    openModal($scope.properties.modalid);
                                }
                            } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                                if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un país y una ciudad donde realizarás el examen"), "warning");
                                } else {
                                    openModal($scope.properties.modalid);
                                }
                            } else {
                                openModal($scope.properties.modalid);
                            }
                        }*/
            else {
                openModal($scope.properties.modalid);
            }
        } else if ($scope.properties.selectedIndex === 2) {
            $scope.faltacampo = false;
            console.log("validar 2 validacion urls");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.fechaNacimiento === undefined) {
                swal($filter('translate')("¡Fecha de nacimiento!"), $filter('translate')("Debes agregar tu fecha de nacimiento"), "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catNacionalidad === null) {
                swal($filter('translate')("¡Nacionalidad!"), $filter('translate')("Debes seleccionar tu nacionalidad"), "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catReligion === null) {
                swal($filter('translate')("¡Religión!"), $filter('translate')("Debes seleccionar tu religión"), "warning");
                $scope.faltacampo = true;
                /* } else if ($scope.properties.catSolicitudDeAdmision.curp === "" && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                     swal("¡CURP!", "Debes agregar tu CURP", "warning");
                     $scope.faltacampo = true;
                 } else if ($scope.properties.catSolicitudDeAdmision.curp.length < 18 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                     swal("¡CURP", "Tu CURP Debes tener 18 caracteres", "warning");
                     $scope.faltacampo = true;*/
            } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular === undefined) {
                swal($filter('translate')("¡Teléfono celular!"), $filter('translate')("Debes agregar tu número celular"), "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular.length !== 10 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                swal($filter('translate')("¡Teléfono celular!"), $filter('translate')("Tu número de teléfono celular debe tener 10 dígitos"), "warning");
                $scope.faltacampo = true;
            }
            /* else if($scope.properties.catSolicitudDeAdmision.telefonoCelular.length !== 14 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana"){
                            swal("¡Teléfono celular", "Tu número de teléfono celular Debes ser de 14 dígitos", "warning");
                            $scope.faltacampo = true;
                        }*/
            else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular === "") {
                swal($filter('translate')("¡Teléfono celular!"), $filter('translate')("Debes agregar tu número celular"), "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catEstadoCivil === null) {
                swal($filter('translate')("¡Estado civil!"), $filter('translate')("Debes seleccionar tu estado civil"), "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catSexo.persistenceId_string === "") {
                swal($filter('translate')("¡Sexo!"), $filter('translate')("Debes seleccionar tu sexo"), "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus === null) {
                swal($filter('translate')("¡Presentaste solicitud en otro campus!"), $filter('translate')("Debes seleccionar si has realizado la solicitud en otro campus"), "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion === "Si") {
                if ($scope.properties.catSolicitudDeAdmision.catCampusPresentadoSolicitud.length === 0) {
                    swal($filter('translate')("¡Campus presentado!"), $filter('translate')("Debes seleccionar el/los campus donde has presentado tu solicitud"), "warning");
                    $scope.faltacampo = true;
                }
            }
            if (!$scope.faltacampo) {
                if ($scope.properties.catSolicitudDeAdmision.calle === "") {
                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle de tu domicilio"), "warning");
                } else if (($scope.properties.catSolicitudDeAdmision.codigoPostal === "" || $scope.properties.catSolicitudDeAdmision.codigoPostal === null) && $scope.properties.catSolicitudDeAdmision.catPais.descripcion === "México") {
                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catPais === null) {
                    swal($filter('translate')("¡País!"), $filter('translate')("Debes seleccionar el país"), "warning");
                }
                /*else if ($scope.properties.catSolicitudDeAdmision.catEstado === null) {
                                   swal("¡Estado!", "Debes seleccionar el estado", "warning");
                               } */
                else if ($scope.properties.catSolicitudDeAdmision.ciudad === "") {
                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar una ciudad"), "warning");
                }
                /*else if ($scope.properties.catSolicitudDeAdmision.calle2 === "") {
                                   swal("¡Entre calles!", "Debes agregar entre que calles se encuentra tu domicilio", "warning");
                               }*/
                else if ($scope.properties.catSolicitudDeAdmision.numExterior === "") {
                    swal($filter('translate')("¡Número!"), $filter('translate')("Debes agregar el número de tu domicilio"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.colonia === "") {
                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.telefono === "") {
                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos === null) {
                    swal($filter('translate')("¡Preparatoria!"), $filter('translate')("Debes seleccionar una preparatoria, en caso de no encontrar la tuya selecciona la opción otro"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion === "Otro") {
                    if ($scope.properties.datosPreparatoria.nombreBachillerato === "" || $scope.properties.datosPreparatoria.nombreBachillerato === null || $scope.properties.datosPreparatoria.nombreBachillerato === undefined) {
                        swal($filter('translate')("¡Preparatoria!"), $filter('translate')("Debes agregar el nombre de tu preparatoria"), "warning");
                    } else if ($scope.properties.datosPreparatoria.paisBachillerato === undefined || $scope.properties.datosPreparatoria.paisBachillerato === "" || $scope.properties.datosPreparatoria.paisBachillerato === null) {
                        swal($filter('translate')("¡País de tu preparatoria!"), $filter('translate')("Debes agregar el país de tu preparatoria"), "warning");
                    } else if ($scope.properties.datosPreparatoria.estadoBachillerato === undefined || $scope.properties.datosPreparatoria.estadoBachillerato === "" || $scope.properties.datosPreparatoria.estadoBachillerato === null) {
                        swal($filter('translate')("¡Estado de tu preparatoria!"), $filter('translate')("Debes agregar el estado de tu preparatoria"), "warning");
                    } else if ($scope.properties.datosPreparatoria.ciudadBachillerato === undefined || $scope.properties.datosPreparatoria.ciudadBachillerato === "" || $scope.properties.datosPreparatoria.ciudadBachillerato === null) {
                        swal($filter('translate')("¡Ciudad de tu preparatoria!"), $filter('translate')("Debes agregar la ciudad de tu preparatoria"), "warning");
                    } else if (isNaN($scope.properties.catSolicitudDeAdmision.promedioGeneral) && $scope.properties.datosPreparatoria.paisBachillerato === 'México') {
                        swal($filter('translate')("¡Promedio!"), $filter('translate')("Debes agregar el promedio que obtuviste en tu preparatoria"), "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "" || $scope.properties.catSolicitudDeAdmision.promedioGeneral === null) {
                        swal($filter('translate')("¡Promedio!"), $filter('translate')("Debes agregar el promedio que obtuviste en tu preparatoria"), "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.catSolicitudDeAdmision.bachillerato = $scope.properties.datosPreparatoria.nombreBachillerato;
                            $scope.properties.catSolicitudDeAdmision.paisBachillerato = $scope.properties.datosPreparatoria.paisBachillerato;
                            $scope.properties.catSolicitudDeAdmision.estadoBachillerato = $scope.properties.datosPreparatoria.estadoBachillerato;
                            $scope.properties.catSolicitudDeAdmision.ciudadBachillerato = $scope.properties.datosPreparatoria.ciudadBachillerato;


                            $scope.fallo = false;
                            $scope.properties.catSolicitudDeAdmision.promedioGeneral = $scope.properties.catSolicitudDeAdmision.promedioGeneral + "";
                            if ($scope.properties.catSolicitudDeAdmision.urlConstancia === null || $scope.properties.catSolicitudDeAdmision.urlConstancia === "") {
                                if ($scope.properties.kardexarchivo.length > 0) {
                                    if (JSON.stringify($scope.properties.kardex) === "{}") {
                                        $scope.fallo = true;
                                        swal($filter('translate')("¡Constancia de estudios!"), $filter('translate')("Debes agregar tu constancia de calificaciones con promedio"), "warning");
                                    } else {
                                        var auxData = null;
                                        if ($scope.properties.kardexarchivo[0].newValue === undefined) {
                                            auxData = $scope.properties.kardexarchivo[0];
                                        } else {
                                            auxData = angular.copy($scope.properties.kardexarchivo[0].newValue);
                                        }
                                        auxData.filename = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.filename === '' ? null : $scope.properties.kardex.filename);
                                        auxData.tempPath = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.tempPath === '' ? null : $scope.properties.kardex.tempPath);
                                        auxData.contentType = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.contentType === '' ? null : $scope.properties.kardex.contentType);
                                        if (auxData.id !== undefined) {
                                            /*$scope.properties.kardexarchivo[0] = {
                                                "id": angular.copy(auxData.id),
                                                "newValue": angular.copy(auxData)
                                            };*/
                                            $scope.properties.kardexarchivo[0]["newValue"] = angular.copy(auxData);
                                        } else {
                                            if ($scope.properties.kardexarchivo[0].newValue.filename !== $scope.properties.kardex.filename) {
                                                $scope.properties.kardexarchivo[0] = ({
                                                    "newValue": angular.copy(auxData)
                                                });
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.kardex !== undefined) {
                                        $scope.properties.kardexarchivo = [];
                                        $scope.properties.kardexarchivo.push({
                                            "newValue": angular.copy($scope.properties.kardex)
                                        });
                                    } else {
                                        $scope.fallo = true;
                                        swal($filter('translate')("¡Constancia de estudios!"), $filter('translate')("Debes agregar tu constancia de calificaciones con promedio"), "warning");
                                    }
                                }
                            } else {
                                if ($scope.properties.kardexarchivo.length > 0) {
                                    var auxData = null;
                                    if ($scope.properties.kardexarchivo[0].newValue === undefined) {
                                        auxData = $scope.properties.kardexarchivo[0];
                                    } else {
                                        auxData = angular.copy($scope.properties.kardexarchivo[0].newValue);
                                    }
                                    auxData.filename = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.filename === '' ? null : $scope.properties.kardex.filename);
                                    auxData.tempPath = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.tempPath === '' ? null : $scope.properties.kardex.tempPath);
                                    auxData.contentType = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.contentType === '' ? null : $scope.properties.kardex.contentType);
                                    if (auxData.id !== undefined) {
                                        /*$scope.properties.kardexarchivo[0] = {
                                            "id": angular.copy(auxData.id),
                                            "newValue": angular.copy(auxData)
                                        };*/
                                        $scope.properties.kardexarchivo[0]["newValue"] = angular.copy(auxData);
                                        $scope.properties.catSolicitudDeAdmision.urlConstancia = "";
                                    } else {
                                        if ($scope.properties.kardexarchivo[0].newValue.filename !== $scope.properties.kardex.filename) {
                                            $scope.properties.kardexarchivo[0] = ({
                                                "newValue": angular.copy(auxData)
                                            });
                                            $scope.properties.catSolicitudDeAdmision.urlConstancia = "";
                                        }
                                    }

                                } else {
                                    if ($scope.properties.kardex !== undefined) {
                                        $scope.properties.kardexarchivo = [];
                                        $scope.properties.kardexarchivo.push({
                                            "newValue": angular.copy($scope.properties.kardex)
                                        });
                                        $scope.properties.catSolicitudDeAdmision.urlConstancia = "";
                                    } else {
                                        $scope.properties.kardexarchivo = [];
                                    }
                                }
                            }


                            if (!$scope.fallo) {
                                if ($scope.properties.catSolicitudDeAdmision.admisionAnahuac) {
                                    if ($scope.properties.catSolicitudDeAdmision.urlCartaAA === null || $scope.properties.catSolicitudDeAdmision.urlCartaAA === "") {
                                        if (JSON.stringify($scope.properties.cartaAA) === "{}") {
                                            $scope.fallo = true;
                                            swal($filter('translate')("¡Carta Admisión Anáhuac!"), $filter('translate')("Agrega tu carta que valida tu Admisión Anáhuac."), "warning");
                                        } else {
                                            if ($scope.properties.cartaAAarchivo.length > 0) {
                                                var auxData = null;
                                                if ($scope.properties.cartaAAarchivo[0].newValue === undefined) {
                                                    auxData = $scope.properties.cartaAAarchivo[0];
                                                } else {
                                                    auxData = angular.copy($scope.properties.cartaAAarchivo[0].newValue);
                                                }
                                                auxData.filename = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.filename === '' ? null : $scope.properties.cartaAA.filename);
                                                auxData.tempPath = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.tempPath === '' ? null : $scope.properties.cartaAA.tempPath);
                                                auxData.contentType = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.contentType === '' ? null : $scope.properties.cartaAA.contentType);
                                                if (auxData.id !== undefined) {
                                                    $scope.properties.cartaAAarchivo[0]["newValue"] = angular.copy(auxData);
                                                    /* = ({
                                                                                                                "id": angular.copy(auxData.id),
                                                                                                                "newValue": angular.copy(auxData)
                                                                                                            });*/
                                                } else {
                                                    if ($scope.properties.cartaAAarchivo[0].newValue.filename !== $scope.properties.cartaAA.filename) {
                                                        $scope.properties.cartaAAarchivo[0] = ({
                                                            "newValue": angular.copy(auxData)
                                                        });
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.cartaAA !== undefined && $scope.properties.cartaAA !== "") {
                                                    $scope.properties.cartaAAarchivo = [];
                                                    $scope.properties.cartaAAarchivo.push({
                                                        "newValue": angular.copy($scope.properties.cartaAA)
                                                    });
                                                } else {
                                                    $scope.fallo = true;
                                                    swal($filter('translate')("¡Carta Admisión Anáhuac!"), $filter('translate')("Agrega tu carta que valida tu Admisión Anáhuac."), "warning");
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.cartaAAarchivo.length > 0) {
                                            var auxData = null;
                                            if ($scope.properties.cartaAAarchivo[0].newValue === undefined) {
                                                auxData = $scope.properties.cartaAAarchivo[0];
                                            } else {
                                                auxData = angular.copy($scope.properties.cartaAAarchivo[0].newValue);
                                            }
                                            auxData.filename = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.filename === '' ? null : $scope.properties.cartaAA.filename);
                                            auxData.tempPath = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.tempPath === '' ? null : $scope.properties.cartaAA.tempPath);
                                            auxData.contentType = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.contentType === '' ? null : $scope.properties.cartaAA.contentType);
                                            if (auxData.id !== undefined) {
                                                $scope.properties.cartaAAarchivo[0]["newValue"] = angular.copy(auxData);
                                                $scope.properties.catSolicitudDeAdmision.urlCartaAA = "";
                                                /* = ({
                                                                                                            "id": angular.copy(auxData.id),
                                                                                                            "newValue": angular.copy(auxData)
                                                                                                        });*/
                                            } else {
                                                if ($scope.properties.cartaAAarchivo[0].newValue.filename !== $scope.properties.cartaAA.filename) {
                                                    $scope.properties.cartaAAarchivo[0] = ({
                                                        "newValue": angular.copy(auxData)
                                                    });
                                                    $scope.properties.catSolicitudDeAdmision.urlCartaAA = "";
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.cartaAA !== undefined && $scope.properties.cartaAA !== "") {
                                                $scope.properties.cartaAAarchivo = [];
                                                $scope.properties.cartaAAarchivo.push({
                                                    "newValue": angular.copy($scope.properties.cartaAA)
                                                });
                                                $scope.properties.catSolicitudDeAdmision.urlCartaAA = "";
                                            } else {
                                                $scope.properties.cartaAAarchivo = [];
                                            }
                                        }

                                    }
                                }
                            }

                            if (!$scope.fallo) {

                                if ($scope.properties.catSolicitudDeAdmision.tienePAA) {
                                    if ($scope.properties.catSolicitudDeAdmision.resultadoPAA === 0 || $scope.properties.catSolicitudDeAdmision.resultadoPAA === "" || $scope.properties.catSolicitudDeAdmision.resultadoPAA === null || $scope.properties.catSolicitudDeAdmision.resultadoPAA === undefined) {
                                        $scope.fallo = true;
                                        swal($filter('translate')("¡Resultado (PAA) del Examen College Board!"), $filter('translate')("Tu puntuación debe ser mayor a cero"), "warning");
                                    } else if ($scope.properties.catSolicitudDeAdmision.resultadoPAA > 1) {
                                        if ($scope.properties.catSolicitudDeAdmision.urlResultadoPAA === null || $scope.properties.catSolicitudDeAdmision.urlResultadoPAA === "") {
                                            if ($scope.properties.collageBoardarchivo.length > 0) {
                                                if (JSON.stringify($scope.properties.collageBoard) === "{}") {
                                                    $scope.fallo = true;
                                                    swal($filter('translate')("¡Constancia College Board!"), $filter('translate')("Debes agregar la constancia del resultado PAA como viene emitida por el College Board."), "warning");
                                                } else {
                                                    var auxData = null;
                                                    if ($scope.properties.collageBoardarchivo[0].newValue === undefined) {
                                                        auxData = $scope.properties.collageBoardarchivo[0];
                                                    } else {
                                                        auxData = angular.copy($scope.properties.collageBoardarchivo[0].newValue);
                                                    }
                                                    auxData.filename = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.filename === '' ? null : $scope.properties.collageBoard.filename);
                                                    auxData.tempPath = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.tempPath === '' ? null : $scope.properties.collageBoard.tempPath);
                                                    auxData.contentType = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.contentType === '' ? null : $scope.properties.collageBoard.contentType);
                                                    if (auxData.id !== undefined) {
                                                        $scope.properties.collageBoardarchivo[0]["newValue"] = angular.copy(auxData);
                                                        /*= ({
                                                            "id": angular.copy(auxData.id),
                                                            "newValue": angular.copy(auxData)
                                                        });*/
                                                    } else {
                                                        if ($scope.properties.collageBoardarchivo[0].newValue.filename !== $scope.properties.collageBoard.filename) {
                                                            $scope.properties.collageBoardarchivo[0] = ({
                                                                "newValue": angular.copy(auxData)
                                                            });
                                                        }
                                                    }
                                                }

                                            } else {
                                                if ($scope.properties.collageBoard !== undefined && $scope.properties.collageBoard !== "") {
                                                    $scope.properties.collageBoardarchivo = [];
                                                    $scope.properties.collageBoardarchivo.push({
                                                        "newValue": angular.copy($scope.properties.collageBoard)
                                                    });
                                                } else {
                                                    $scope.fallo = true;
                                                    swal($filter('translate')("¡Constancia College Board!"), $filter('translate')("Debes agregar la constancia del resultado PAA como viene emitida por el College Board."), "warning");
                                                }

                                            }
                                        } else {
                                            if ($scope.properties.collageBoardarchivo.length > 0) {
                                                var auxData = null;
                                                if ($scope.properties.collageBoardarchivo[0].newValue === undefined) {
                                                    auxData = $scope.properties.collageBoardarchivo[0];
                                                } else {
                                                    auxData = angular.copy($scope.properties.collageBoardarchivo[0].newValue);
                                                }
                                                auxData.filename = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.filename === '' ? null : $scope.properties.collageBoard.filename);
                                                auxData.tempPath = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.tempPath === '' ? null : $scope.properties.collageBoard.tempPath);
                                                auxData.contentType = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.contentType === '' ? null : $scope.properties.collageBoard.contentType);
                                                if (auxData.id !== undefined) {
                                                    $scope.properties.collageBoardarchivo[0]["newValue"] = angular.copy(auxData);
                                                    $scope.properties.catSolicitudDeAdmision.urlResultadoPAA = "";
                                                    /*= ({
                                                        "id": angular.copy(auxData.id),
                                                        "newValue": angular.copy(auxData)
                                                    });*/
                                                } else {
                                                    if ($scope.properties.collageBoardarchivo[0].newValue.filename !== $scope.properties.collageBoard.filename) {
                                                        $scope.properties.collageBoardarchivo[0] = ({
                                                            "newValue": angular.copy(auxData)
                                                        });
                                                        $scope.properties.catSolicitudDeAdmision.urlResultadoPAA = "";
                                                    }
                                                }


                                            } else {
                                                if ($scope.properties.collageBoard !== undefined && $scope.properties.collageBoard !== "") {
                                                    $scope.properties.collageBoardarchivo = [];
                                                    $scope.properties.collageBoardarchivo.push({
                                                        "newValue": angular.copy($scope.properties.collageBoard)
                                                    });
                                                    $scope.properties.catSolicitudDeAdmision.urlResultadoPAA = "";
                                                } else {
                                                    $scope.properties.collageBoardarchivo = [];
                                                }

                                            }

                                        }

                                    } //Aqui termina el if de resultado PAA MAYOR A 1
                                } else { //Este es el else de tiene PAA
                                    $scope.properties.collageBoardarchivo = [];
                                    if ($scope.properties.catSolicitudDeAdmision.tieneDescuento === true) {
                                        if ($scope.properties.catSolicitudDeAdmision.urlDescuentos === null || $scope.properties.catSolicitudDeAdmision.urlDescuentos === "") {
                                            if ($scope.properties.descuentoarchivo.length > 0) {
                                                if (JSON.stringify($scope.properties.descuento) === "{}") {
                                                    $scope.fallo = true;
                                                    swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                                } else {
                                                    var auxData = null;
                                                    if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                                                        auxData = $scope.properties.descuentoarchivo[0];
                                                    } else {
                                                        auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                                                    }
                                                    auxData.filename = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.filename === '' ? null : $scope.properties.descuento.filename);
                                                    auxData.tempPath = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.tempPath === '' ? null : $scope.properties.descuento.tempPath);
                                                    auxData.contentType = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.contentType === '' ? null : $scope.properties.descuento.contentType);
                                                    if (auxData.id !== undefined) {
                                                        $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);
                                                        /* = ({
                                                                                                                    "id": angular.copy(auxData.id),
                                                                                                                    "newValue": angular.copy(auxData)
                                                                                                                });*/
                                                    } else {
                                                        if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                                                            $scope.properties.descuentoarchivo[0] = ({
                                                                "newValue": angular.copy(auxData)
                                                            });
                                                        }
                                                    }
                                                }
                                            } else { //ELSE DEL DESCUENTO ARCHIVO MAYOR A 0
                                                if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                                    $scope.properties.descuentoarchivo = [];
                                                    $scope.properties.descuentoarchivo.push({
                                                        "newValue": angular.copy($scope.properties.descuento)
                                                    });
                                                } else {
                                                    $scope.fallo = true;
                                                    swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.descuentoarchivo.length > 0) {
                                                var auxData = null;
                                                if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                                                    auxData = $scope.properties.descuentoarchivo[0];
                                                } else {
                                                    auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                                                }
                                                auxData.filename = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.filename === '' ? null : $scope.properties.descuento.filename);
                                                auxData.tempPath = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.tempPath === '' ? null : $scope.properties.descuento.tempPath);
                                                auxData.contentType = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.contentType === '' ? null : $scope.properties.descuento.contentType);
                                                if (auxData.id !== undefined) {
                                                    $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);
                                                    $scope.properties.catSolicitudDeAdmision.urlDescuentos = "";
                                                    /* = ({
                                                                                                                "id": angular.copy(auxData.id),
                                                                                                                "newValue": angular.copy(auxData)
                                                                                                            });*/
                                                } else {
                                                    if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                                                        $scope.properties.descuentoarchivo[0] = ({
                                                            "newValue": angular.copy(auxData)
                                                        });
                                                        $scope.properties.catSolicitudDeAdmision.urlDescuentos = "";
                                                    }
                                                }

                                            } else { //ELSE DEL DESCUENTO ARCHIVO MAYOR A 0
                                                if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                                    $scope.properties.descuentoarchivo = [];
                                                    $scope.properties.descuentoarchivo.push({
                                                        "newValue": angular.copy($scope.properties.descuento)
                                                    });
                                                    $scope.properties.catSolicitudDeAdmision.urlDescuentos = "";
                                                } else {
                                                    $scope.properties.descuentoarchivo = [];
                                                }
                                            }
                                        }


                                    } else { //ELSE DEL TIENE DESCUENTO TRUE
                                        $scope.properties.descuentoarchivo = [];
                                        if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                            $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                        } else {
                                            $scope.properties.idExtranjero = "";
                                        }
                                        $scope.properties.pasoInformacionPersonal = true;
                                        //$scope.properties.selectedIndex++;
                                        $scope.assignTask();
                                        $scope.fallo = true;
                                    }
                                }
                            }
                            if (!$scope.fallo) {
                                if ($scope.properties.catSolicitudDeAdmision.tieneDescuento === true) {
                                    if ($scope.properties.catSolicitudDeAdmision.urlDescuentos === null || $scope.properties.catSolicitudDeAdmision.urlDescuentos === "") {
                                        if ($scope.properties.descuentoarchivo.length > 0) {
                                            if (JSON.stringify($scope.properties.descuento) === "{}") {
                                                $scope.fallo = true;
                                                swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                            } else {
                                                var auxData = null;
                                                if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                                                    auxData = $scope.properties.descuentoarchivo[0];
                                                } else {
                                                    auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                                                }
                                                auxData.filename = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.filename === '' ? null : $scope.properties.descuento.filename);
                                                auxData.tempPath = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.tempPath === '' ? null : $scope.properties.descuento.tempPath);
                                                auxData.contentType = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.contentType === '' ? null : $scope.properties.descuento.contentType);
                                                if (auxData.id !== undefined) {
                                                    $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);
                                                    /* = ({
                                                                                                            "id": angular.copy(auxData.id),
                                                                                                            "newValue": angular.copy(auxData)
                                                                                                        });*/
                                                } else {
                                                    if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                                                        $scope.properties.descuentoarchivo[0] = ({
                                                            "newValue": angular.copy(auxData)
                                                        });
                                                    }
                                                }
                                                // if ($scope.properties.idExtranjero !== undefined) {
                                                //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                // }
                                                if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                } else {
                                                    $scope.properties.idExtranjero = "";
                                                }
                                                $scope.properties.pasoInformacionPersonal = true;
                                                $scope.assignTask();
                                                //$scope.properties.selectedIndex++;
                                            }
                                        } else { //Else descuetoarchivo mayor a 0
                                            if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                                $scope.properties.descuentoarchivo.push({
                                                    "newValue": angular.copy($scope.properties.descuento)
                                                });
                                                // if ($scope.properties.idExtranjero !== undefined) {
                                                //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                // }
                                                if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                } else {
                                                    $scope.properties.idExtranjero = "";
                                                }
                                                $scope.properties.pasoInformacionPersonal = true;
                                                $scope.assignTask();
                                                //$scope.properties.selectedIndex++;
                                            } else {
                                                $scope.fallo = true;
                                                swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.descuentoarchivo.length > 0) {
                                            var auxData = null;
                                            if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                                                auxData = $scope.properties.descuentoarchivo[0];
                                            } else {
                                                auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                                            }
                                            auxData.filename = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.filename === '' ? null : $scope.properties.descuento.filename);
                                            auxData.tempPath = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.tempPath === '' ? null : $scope.properties.descuento.tempPath);
                                            auxData.contentType = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.contentType === '' ? null : $scope.properties.descuento.contentType);
                                            if (auxData.id !== undefined) {
                                                $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);
                                                $scope.properties.catSolicitudDeAdmision.urlDescuentos = "";
                                                /* = ({
                                                                                                        "id": angular.copy(auxData.id),
                                                                                                        "newValue": angular.copy(auxData)
                                                                                                    });*/
                                            } else {
                                                if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                                                    $scope.properties.descuentoarchivo[0] = ({
                                                        "newValue": angular.copy(auxData)
                                                    });
                                                    $scope.properties.catSolicitudDeAdmision.urlDescuentos = "";
                                                }
                                            }
                                            // if ($scope.properties.idExtranjero !== undefined) {
                                            //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            // }
                                            if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            } else {
                                                $scope.properties.idExtranjero = "";
                                            }
                                            $scope.properties.pasoInformacionPersonal = true;
                                            $scope.assignTask();
                                            //$scope.properties.selectedIndex++;

                                        } else { //Else descuetoarchivo mayor a 0
                                            if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                                $scope.properties.descuentoarchivo.push({
                                                    "newValue": angular.copy($scope.properties.descuento)
                                                });
                                                $scope.properties.catSolicitudDeAdmision.urlDescuentos = "";
                                                // if ($scope.properties.idExtranjero !== undefined) {
                                                //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                // }
                                                if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                } else {
                                                    $scope.properties.idExtranjero = "";
                                                }
                                                $scope.properties.pasoInformacionPersonal = true;
                                                $scope.assignTask();
                                                //$scope.properties.selectedIndex++;
                                            } else {
                                                $scope.properties.descuentoarchivo = [];
                                                if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                } else {
                                                    $scope.properties.idExtranjero = "";
                                                }
                                                $scope.properties.pasoInformacionPersonal = true;
                                                $scope.assignTask();
                                            }
                                        }



                                    }

                                } else {
                                    $scope.properties.descuentoarchivo = [];
                                    // if ($scope.properties.idExtranjero !== undefined) {
                                    //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                    // }
                                    if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                        $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                    } else {
                                        $scope.properties.idExtranjero = "";
                                    }
                                    $scope.properties.pasoInformacionPersonal = true;
                                    $scope.assignTask();
                                    //$scope.properties.selectedIndex++;
                                }
                            }

                        }
                    }
                } else if (isNaN($scope.properties.catSolicitudDeAdmision.promedioGeneral) && $scope.properties.datosPreparatoria.paisBachillerato === 'México') {
                    swal($filter('translate')("¡Promedio!"), $filter('translate')("Debes agregar el promedio que obtuviste en tu preparatoria"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "" || $scope.properties.catSolicitudDeAdmision.promedioGeneral === null) {
                    swal($filter('translate')("¡Promedio!"), $filter('translate')("Debes agregar el promedio que obtuviste en tu preparatoria"), "warning");
                } else {

                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.fallo = false;
                        if ($scope.properties.catSolicitudDeAdmision.urlConstancia === null || $scope.properties.catSolicitudDeAdmision.urlConstancia === "") {
                            if ($scope.properties.kardexarchivo.length > 0) {
                                if (JSON.stringify($scope.properties.kardex) === "{}") {
                                    $scope.fallo = true;
                                    swal($filter('translate')("¡Constancia de estudios!"), $filter('translate')("Debes agregar tu constancia de calificaciones con promedio"), "warning");
                                } else {
                                    var auxData = null;
                                    if ($scope.properties.kardexarchivo[0].newValue === undefined) {
                                        auxData = $scope.properties.kardexarchivo[0];
                                    } else {
                                        auxData = angular.copy($scope.properties.kardexarchivo[0].newValue);
                                    }
                                    auxData.filename = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.filename === '' ? null : $scope.properties.kardex.filename);
                                    auxData.tempPath = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.tempPath === '' ? null : $scope.properties.kardex.tempPath);
                                    auxData.contentType = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.contentType === '' ? null : $scope.properties.kardex.contentType);
                                    if (auxData.id !== undefined) {
                                        $scope.properties.kardexarchivo[0]["newValue"] = angular.copy(auxData);
                                        /* = {
                                                                                    "id": angular.copy(auxData.id),
                                                                                    "newValue": angular.copy(auxData)
                                                                                };*/
                                    } else {
                                        if ($scope.properties.kardexarchivo[0].newValue.filename !== $scope.properties.kardex.filename) {
                                            $scope.properties.kardexarchivo[0] = ({
                                                "newValue": angular.copy(auxData)
                                            });
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.kardex !== undefined) {
                                    $scope.properties.kardexarchivo = [];
                                    $scope.properties.kardexarchivo.push({
                                        "newValue": angular.copy($scope.properties.kardex)
                                    });
                                } else {
                                    $scope.fallo = true;
                                    swal($filter('translate')("¡Constancia de estudios!"), $filter('translate')("Debes agregar tu constancia de calificaciones con promedio"), "warning");
                                }
                            }
                        } else {
                            if ($scope.properties.kardexarchivo.length > 0) {
                                var auxData = null;
                                if ($scope.properties.kardexarchivo[0].newValue === undefined) {
                                    auxData = $scope.properties.kardexarchivo[0];
                                } else {
                                    auxData = angular.copy($scope.properties.kardexarchivo[0].newValue);
                                }
                                auxData.filename = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.filename === '' ? null : $scope.properties.kardex.filename);
                                auxData.tempPath = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.tempPath === '' ? null : $scope.properties.kardex.tempPath);
                                auxData.contentType = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.contentType === '' ? null : $scope.properties.kardex.contentType);
                                if (auxData.id !== undefined) {
                                    $scope.properties.kardexarchivo[0]["newValue"] = angular.copy(auxData);
                                    /* = {
                                                                                "id": angular.copy(auxData.id),
                                                                                "newValue": angular.copy(auxData)
                                                                            };*/
                                    $scope.properties.catSolicitudDeAdmision.urlConstancia = "";
                                } else {
                                    if ($scope.properties.kardexarchivo[0].newValue.filename !== $scope.properties.kardex.filename) {
                                        $scope.properties.kardexarchivo[0] = ({
                                            "newValue": angular.copy(auxData)
                                        });
                                        $scope.properties.catSolicitudDeAdmision.urlConstancia = "";
                                    }
                                }
                            } else {
                                if ($scope.properties.kardex !== undefined) {
                                    $scope.properties.kardexarchivo = [];
                                    $scope.properties.kardexarchivo.push({
                                        "newValue": angular.copy($scope.properties.kardex)
                                    });
                                    $scope.properties.catSolicitudDeAdmision.urlConstancia = "";
                                } else {
                                    $scope.properties.kardexarchivo = [];
                                }
                            }
                        }


                        if (!$scope.fallo) {
                            if ($scope.properties.catSolicitudDeAdmision.admisionAnahuac) {
                                if ($scope.properties.catSolicitudDeAdmision.urlCartaAA === null || $scope.properties.catSolicitudDeAdmision.urlCartaAA === "") {
                                    if (JSON.stringify($scope.properties.cartaAA) === "{}") {
                                        $scope.fallo = true;
                                        swal($filter('translate')("¡Carta Admisión Anáhuac!"), $filter('translate')("Agrega tu carta que valida tu Admisión Anáhuac."), "warning");
                                    } else {
                                        if ($scope.properties.cartaAAarchivo.length > 0) {
                                            var auxData = null;
                                            if ($scope.properties.cartaAAarchivo[0].newValue === undefined) {
                                                auxData = $scope.properties.cartaAAarchivo[0];
                                            } else {
                                                auxData = angular.copy($scope.properties.cartaAAarchivo[0].newValue);
                                            }
                                            auxData.filename = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.filename === '' ? null : $scope.properties.cartaAA.filename);
                                            auxData.tempPath = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.tempPath === '' ? null : $scope.properties.cartaAA.tempPath);
                                            auxData.contentType = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.contentType === '' ? null : $scope.properties.cartaAA.contentType);
                                            if (auxData.id !== undefined) {
                                                $scope.properties.cartaAAarchivo[0]["newValue"] = angular.copy(auxData);
                                                /* = ({
                                                                                                    "id": angular.copy(auxData.id),
                                                                                                    "newValue": angular.copy(auxData)
                                                                                                });*/
                                            } else {
                                                if ($scope.properties.cartaAAarchivo[0].newValue.filename !== $scope.properties.cartaAA.filename) {
                                                    $scope.properties.cartaAAarchivo[0] = ({
                                                        "newValue": angular.copy(auxData)
                                                    });
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.cartaAA !== undefined && $scope.properties.cartaAA !== "") {
                                                $scope.properties.cartaAAarchivo = [];
                                                $scope.properties.cartaAAarchivo.push({
                                                    "newValue": angular.copy($scope.properties.cartaAA)
                                                });
                                            } else {
                                                $scope.fallo = true;
                                                swal($filter('translate')("¡Carta Admisión Anáhuac!"), $filter('translate')("Agrega tu carta que valida tu Admisión Anáhuac."), "warning");
                                            }
                                        }
                                    } // 
                                } else {
                                    if ($scope.properties.cartaAAarchivo.length > 0) {
                                        var auxData = null;
                                        if ($scope.properties.cartaAAarchivo[0].newValue === undefined) {
                                            auxData = $scope.properties.cartaAAarchivo[0];
                                        } else {
                                            auxData = angular.copy($scope.properties.cartaAAarchivo[0].newValue);
                                        }
                                        auxData.filename = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.filename === '' ? null : $scope.properties.cartaAA.filename);
                                        auxData.tempPath = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.tempPath === '' ? null : $scope.properties.cartaAA.tempPath);
                                        auxData.contentType = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.contentType === '' ? null : $scope.properties.cartaAA.contentType);
                                        if (auxData.id !== undefined) {
                                            $scope.properties.cartaAAarchivo[0]["newValue"] = angular.copy(auxData);
                                            $scope.properties.catSolicitudDeAdmision.urlCartaAA = "";
                                            /* = ({
                                                                                                "id": angular.copy(auxData.id),
                                                                                                "newValue": angular.copy(auxData)
                                                                                            });*/
                                        } else {
                                            if ($scope.properties.cartaAAarchivo[0].newValue.filename !== $scope.properties.cartaAA.filename) {
                                                $scope.properties.cartaAAarchivo[0] = ({
                                                    "newValue": angular.copy(auxData)
                                                });
                                                $scope.properties.catSolicitudDeAdmision.urlCartaAA = "";
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.cartaAA !== undefined && $scope.properties.cartaAA !== "") {
                                            $scope.properties.cartaAAarchivo = [];
                                            $scope.properties.cartaAAarchivo.push({
                                                "newValue": angular.copy($scope.properties.cartaAA)
                                            });
                                            $scope.properties.catSolicitudDeAdmision.urlCartaAA = "";
                                        } else {
                                            $scope.properties.cartaAAarchivo = [];
                                        }
                                    }
                                }

                            }
                        }

                        if (!$scope.fallo) {

                            if ($scope.properties.catSolicitudDeAdmision.tienePAA) {
                                if ($scope.properties.catSolicitudDeAdmision.resultadoPAA === 0 || $scope.properties.catSolicitudDeAdmision.resultadoPAA === "" || $scope.properties.catSolicitudDeAdmision.resultadoPAA === null || $scope.properties.catSolicitudDeAdmision.resultadoPAA === undefined) {
                                    $scope.fallo = true;
                                    swal($filter('translate')("¡Resultado (PAA) del Examen College Board!"), $filter('translate')("Tu puntuación debe ser mayor a cero"), "warning");
                                } else if ($scope.properties.catSolicitudDeAdmision.resultadoPAA > 1) {
                                    if ($scope.properties.catSolicitudDeAdmision.urlResultadoPAA === null || $scope.properties.catSolicitudDeAdmision.urlResultadoPAA === "") {
                                        if (JSON.stringify($scope.properties.collageBoard) === "{}") {
                                            $scope.fallo = true;
                                            swal($filter('translate')("¡Constancia College Board!"), $filter('translate')("Debes agregar la constancia del resultado PAA como viene emitida por el College Board."), "warning");
                                        } else {
                                            if ($scope.properties.collageBoardarchivo.length > 0) {
                                                var auxData = null;
                                                if ($scope.properties.collageBoardarchivo[0].newValue === undefined) {
                                                    auxData = $scope.properties.collageBoardarchivo[0];
                                                } else {
                                                    auxData = angular.copy($scope.properties.collageBoardarchivo[0].newValue);
                                                }
                                                auxData.filename = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.filename === '' ? null : $scope.properties.collageBoard.filename);
                                                auxData.tempPath = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.tempPath === '' ? null : $scope.properties.collageBoard.tempPath);
                                                auxData.contentType = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.contentType === '' ? null : $scope.properties.collageBoard.contentType);
                                                if (auxData.id !== undefined) {
                                                    $scope.properties.collageBoardarchivo[0]["newValue"] = angular.copy(auxData);
                                                    /* = ({
                                                                                                        "id": angular.copy(auxData.id),
                                                                                                        "newValue": angular.copy(auxData)
                                                                                                    });*/
                                                } else {
                                                    if ($scope.properties.collageBoardarchivo[0].newValue.filename !== $scope.properties.collageBoard.filename) {
                                                        $scope.properties.collageBoardarchivo[0] = ({
                                                            "newValue": angular.copy(auxData)
                                                        });
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.collageBoard !== undefined && $scope.properties.collageBoard !== "") {
                                                    $scope.properties.collageBoardarchivo = [];
                                                    $scope.properties.collageBoardarchivo.push({
                                                        "newValue": angular.copy($scope.properties.collageBoard)
                                                    });
                                                } else {
                                                    $scope.fallo = true;
                                                    swal($filter('translate')("¡Constancia College Board!"), $filter('translate')("Debes agregar la constancia del resultado PAA como viene emitida por el College Board."), "warning");
                                                }

                                            }
                                        }
                                    } else {
                                        if ($scope.properties.collageBoardarchivo.length > 0) {
                                            var auxData = null;
                                            if ($scope.properties.collageBoardarchivo[0].newValue === undefined) {
                                                auxData = $scope.properties.collageBoardarchivo[0];
                                            } else {
                                                auxData = angular.copy($scope.properties.collageBoardarchivo[0].newValue);
                                            }
                                            auxData.filename = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.filename === '' ? null : $scope.properties.collageBoard.filename);
                                            auxData.tempPath = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.tempPath === '' ? null : $scope.properties.collageBoard.tempPath);
                                            auxData.contentType = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.contentType === '' ? null : $scope.properties.collageBoard.contentType);
                                            if (auxData.id !== undefined) {
                                                $scope.properties.collageBoardarchivo[0]["newValue"] = angular.copy(auxData);
                                                $scope.properties.catSolicitudDeAdmision.urlResultadoPAA = "";
                                                /* = ({
                                                                                                    "id": angular.copy(auxData.id),
                                                                                                    "newValue": angular.copy(auxData)
                                                                                                });*/
                                            } else {
                                                if ($scope.properties.collageBoardarchivo[0].newValue.filename !== $scope.properties.collageBoard.filename) {
                                                    $scope.properties.collageBoardarchivo[0] = ({
                                                        "newValue": angular.copy(auxData)
                                                    });
                                                    $scope.properties.catSolicitudDeAdmision.urlResultadoPAA = "";
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.collageBoard !== undefined && $scope.properties.collageBoard !== "") {
                                                $scope.properties.collageBoardarchivo = [];
                                                $scope.properties.collageBoardarchivo.push({
                                                    "newValue": angular.copy($scope.properties.collageBoard)
                                                });
                                                $scope.properties.catSolicitudDeAdmision.urlResultadoPAA = "";
                                            } else {
                                                $scope.properties.collageBoardarchivo = [];
                                            }

                                        }
                                    }


                                }
                            } else {
                                $scope.properties.collageBoardarchivo = [];
                                if ($scope.properties.catSolicitudDeAdmision.tieneDescuento === true) {
                                    if ($scope.properties.catSolicitudDeAdmision.urlDescuentos === null || $scope.properties.catSolicitudDeAdmision.urlDescuentos === "") {
                                        if ($scope.properties.descuentoarchivo.length > 0) {
                                            if (JSON.stringify($scope.properties.descuento) === "{}") {
                                                $scope.fallo = true;
                                                swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                            } else {
                                                var auxData = null;
                                                if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                                                    auxData = $scope.properties.descuentoarchivo[0];
                                                } else {
                                                    auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                                                }
                                                auxData.filename = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.filename === '' ? null : $scope.properties.descuento.filename);
                                                auxData.tempPath = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.tempPath === '' ? null : $scope.properties.descuento.tempPath);
                                                auxData.contentType = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.contentType === '' ? null : $scope.properties.descuento.contentType);
                                                if (auxData.id !== undefined) {
                                                    $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);
                                                    /* = ({
                                                                                                        "id": angular.copy(auxData.id),
                                                                                                        "newValue": angular.copy(auxData)
                                                                                                    });*/
                                                } else {
                                                    if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                                                        $scope.properties.descuentoarchivo[0] = ({
                                                            "newValue": angular.copy(auxData)
                                                        });
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                                $scope.properties.descuentoarchivo = [];
                                                $scope.properties.descuentoarchivo.push({
                                                    "newValue": angular.copy($scope.properties.descuento)
                                                });
                                            } else {
                                                $scope.fallo = true;
                                                swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.descuentoarchivo.length > 0) {
                                            var auxData = null;
                                            if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                                                auxData = $scope.properties.descuentoarchivo[0];
                                            } else {
                                                auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                                            }
                                            auxData.filename = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.filename === '' ? null : $scope.properties.descuento.filename);
                                            auxData.tempPath = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.tempPath === '' ? null : $scope.properties.descuento.tempPath);
                                            auxData.contentType = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.contentType === '' ? null : $scope.properties.descuento.contentType);
                                            if (auxData.id !== undefined) {
                                                $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);
                                                $scope.properties.catSolicitudDeAdmision.urlDescuentos = "";
                                                /* = ({
                                                                                                    "id": angular.copy(auxData.id),
                                                                                                    "newValue": angular.copy(auxData)
                                                                                                });*/
                                            } else {
                                                if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                                                    $scope.properties.descuentoarchivo[0] = ({
                                                        "newValue": angular.copy(auxData)
                                                    });
                                                    $scope.properties.catSolicitudDeAdmision.urlDescuentos = "";
                                                }
                                            }

                                        } else {
                                            if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                                $scope.properties.descuentoarchivo = [];
                                                $scope.properties.descuentoarchivo.push({
                                                    "newValue": angular.copy($scope.properties.descuento)
                                                });
                                                $scope.properties.catSolicitudDeAdmision.urlDescuentos = "";
                                            } else {
                                                $scope.properties.descuentoarchivo = [];
                                            }
                                        }

                                    }

                                } else {
                                    $scope.properties.descuentoarchivo = [];
                                    // if ($scope.properties.idExtranjero !== undefined) {
                                    //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                    // }
                                    if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                        $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                    } else {
                                        $scope.properties.idExtranjero = "";
                                    }
                                    $scope.properties.pasoInformacionPersonal = true;
                                    //$scope.properties.selectedIndex++;
                                    $scope.assignTask();
                                    $scope.fallo = true;
                                }
                            }
                        }

                        if (!$scope.fallo) {
                            if ($scope.properties.catSolicitudDeAdmision.tieneDescuento === true) {
                                if ($scope.properties.catSolicitudDeAdmision.urlDescuentos === null || $scope.properties.catSolicitudDeAdmision.urlDescuentos === "") {
                                    if ($scope.properties.descuentoarchivo.length > 0) {
                                        if (JSON.stringify($scope.properties.descuento) === "{}") {
                                            $scope.fallo = true;
                                            swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                        } else {
                                            var auxData = null;
                                            if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                                                auxData = $scope.properties.descuentoarchivo[0];
                                            } else {
                                                auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                                            }
                                            auxData.filename = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.filename === '' ? null : $scope.properties.descuento.filename);
                                            auxData.tempPath = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.tempPath === '' ? null : $scope.properties.descuento.tempPath);
                                            auxData.contentType = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.contentType === '' ? null : $scope.properties.descuento.contentType);
                                            if (auxData.id !== undefined) {
                                                $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);
                                                /* = ({
                                                                                                "id": angular.copy(auxData.id),
                                                                                                "newValue": angular.copy(auxData)
                                                                                            });*/
                                            } else {
                                                if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                                                    $scope.properties.descuentoarchivo[0] = ({
                                                        "newValue": angular.copy(auxData)
                                                    });
                                                }
                                            }
                                            // if ($scope.properties.idExtranjero !== undefined) {
                                            //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            // }
                                            if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            } else {
                                                $scope.properties.idExtranjero = "";
                                            }
                                            $scope.properties.pasoInformacionPersonal = true;
                                            //$scope.properties.selectedIndex++;
                                            $scope.assignTask();
                                        }
                                    } else {
                                        if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                            $scope.properties.descuentoarchivo.push({
                                                "newValue": angular.copy($scope.properties.descuento)
                                            });
                                            // if ($scope.properties.idExtranjero !== undefined) {
                                            //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            // }
                                            if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            } else {
                                                $scope.properties.idExtranjero = "";
                                            }
                                            $scope.properties.pasoInformacionPersonal = true;
                                            //$scope.properties.selectedIndex++;
                                            $scope.assignTask();
                                        } else {
                                            $scope.fallo = true;
                                            swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                        }
                                    }
                                } else {
                                    if ($scope.properties.descuentoarchivo.length > 0) {
                                        var auxData = null;
                                        if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                                            auxData = $scope.properties.descuentoarchivo[0];
                                        } else {
                                            auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                                        }
                                        auxData.filename = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.filename === '' ? null : $scope.properties.descuento.filename);
                                        auxData.tempPath = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.tempPath === '' ? null : $scope.properties.descuento.tempPath);
                                        auxData.contentType = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.contentType === '' ? null : $scope.properties.descuento.contentType);
                                        if (auxData.id !== undefined) {
                                            $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);
                                            $scope.properties.catSolicitudDeAdmision.urlDescuentos = "";
                                            /* = ({
                                                                                            "id": angular.copy(auxData.id),
                                                                                            "newValue": angular.copy(auxData)
                                                                                        });*/
                                        } else {
                                            if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                                                $scope.properties.descuentoarchivo[0] = ({
                                                    "newValue": angular.copy(auxData)
                                                });
                                                $scope.properties.catSolicitudDeAdmision.urlDescuentos = "";
                                            }
                                        }
                                        // if ($scope.properties.idExtranjero !== undefined) {
                                        //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                        // }
                                        if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                            $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                        } else {
                                            $scope.properties.idExtranjero = "";
                                        }
                                        $scope.properties.pasoInformacionPersonal = true;
                                        //$scope.properties.selectedIndex++;
                                        $scope.assignTask();

                                    } else {
                                        if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                            $scope.properties.descuentoarchivo.push({
                                                "newValue": angular.copy($scope.properties.descuento)
                                            });
                                            $scope.properties.catSolicitudDeAdmision.urlDescuentos = "";
                                            // if ($scope.properties.idExtranjero !== undefined) {
                                            //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            // }
                                            if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            } else {
                                                $scope.properties.idExtranjero = "";
                                            }
                                            $scope.properties.pasoInformacionPersonal = true;
                                            //$scope.properties.selectedIndex++;
                                            $scope.assignTask();
                                        } else {
                                            $scope.properties.descuentoarchivo = [];
                                            if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            } else {
                                                $scope.properties.idExtranjero = "";
                                            }
                                            $scope.properties.pasoInformacionPersonal = true;
                                            //$scope.properties.selectedIndex++;
                                            $scope.assignTask();
                                        }
                                    }
                                }

                            } else {
                                $scope.properties.descuentoarchivo = [];
                                // if ($scope.properties.idExtranjero !== undefined) {
                                //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                // }
                                if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                } else {
                                    $scope.properties.idExtranjero = "";
                                }
                                $scope.properties.pasoInformacionPersonal = true;
                                //$scope.properties.selectedIndex++;
                                $scope.assignTask();
                            }
                        }
                    }
                }
            }

        } else if ($scope.properties.selectedIndex === 3) {
            console.log("validar 3");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.tutorInput.length === 0) {
                swal($filter('translate')("¡Tutor!"), $filter('translate')("Debes agregar al menos un tutor"), "warning");
            } else if ($scope.properties.padreInput.desconozcoDatosPadres) {
                //validar madre
                if ($scope.properties.madreInput.desconozcoDatosPadres) {
                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                            $scope.properties.pasoInformacionFamiliar = true;
                            //$scope.properties.selectedIndex++;
                            $scope.assignTask();
                        }
                    }
                } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                    swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a la madre"), "warning");
                } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                    swal($filter('translate')("¡Nombre de la madre!"), $filter('translate')("Debes agregar nombre de la madre"), "warning");
                } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                    swal($filter('translate')("¡Apellidos de la madre!"), $filter('translate')("Debes agregar los apellidos de la madre"), "warning");
                } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                    swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si la madre vive"), "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                        swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la universidad Anáhuac"), "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                            swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                        } else {
                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                    swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                    swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                }
                                /*else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                   swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                               } */
                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                } else {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            //$scope.properties.selectedIndex++;
                                            $scope.assignTask();
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                }
                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                }*/
                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                } else {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            //$scope.properties.selectedIndex++;
                                            $scope.assignTask();
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                            swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                            }
                            /*else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                           swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                       } */
                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                            } else {
                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        //$scope.properties.selectedIndex++;
                                        $scope.assignTask();
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                            }
                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                        } */
                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                            } else {
                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        //$scope.properties.selectedIndex++;
                                        $scope.assignTask();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                        $scope.properties.pasoInformacionFamiliar = true;
                        //$scope.properties.selectedIndex++;
                        $scope.assignTask();
                    }
                }




            } else if ($scope.properties.padreInput.catTitulo === 0 || $scope.properties.padreInput.catTitulo === null) {
                swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar al padre"), "warning");
            } else if ($scope.properties.padreInput.nombre === "" || $scope.properties.padreInput.nombre === undefined) {
                swal($filter('translate')("¡Nombre del padre!"), $filter('translate')("Debes agregar nombre del padre"), "warning");
            } else if ($scope.properties.padreInput.apellidos === "" || $scope.properties.padreInput.apellidos === undefined) {
                swal($filter('translate')("¡Apellidos del padre!"), $filter('translate')("Debes agregar los apellidos del padre"), "warning");
            } else if ($scope.properties.padreInput.vive === 0 || $scope.properties.padreInput.vive === null) {
                swal($filter('translate')("¡Padre vive!"), $filter('translate')("Debes seleccionar si el padre vive"), "warning");
            } else if ($scope.properties.datosPadres.padrevive) {
                if ($scope.properties.padreInput.catEgresoAnahuac === 0 || $scope.properties.padreInput.catEgresoAnahuac === null) {
                    swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu padre egresó de la universidad Anáhuac"), "warning");
                } else if ($scope.properties.datosPadres.padreegresoanahuac) {
                    if ($scope.properties.padreInput.catCampusEgreso === 0 || $scope.properties.padreInput.catCampusEgreso === null) {
                        swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu padre"), "warning");
                    } else {
                        if ($scope.properties.padreInput.catTrabaja === 0 || $scope.properties.padreInput.catTrabaja === null) {
                            swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu padre trabaja"), "warning");
                        } else if ($scope.properties.datosPadres.padretrabaja) {
                            if ($scope.properties.padreInput.empresaTrabaja === "" || $scope.properties.padreInput.empresaTrabaja === undefined) {
                                swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu padre trabaja"), "warning");
                            } else if ($scope.properties.padreInput.puesto === "" || $scope.properties.padreInput.puesto === undefined) {
                                swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo del padre"), "warning");
                            } else if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico del padre"), "warning");
                            } else if (!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                            } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad del padre"), "warning");
                            } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio del padre"), "warning");
                            }
                            /* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                                        } */
                            else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                            } else if (($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) && $scope.properties.padreInput.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio del padre"), "warning");
                            } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio del padre"), "warning");
                            } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                            } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio del padre"), "warning");
                            } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono del padre"), "warning");
                            } else {
                                ///Validar madre 1
                                if ($scope.properties.madreInput.desconozcoDatosPadres) {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            //$scope.properties.selectedIndex++;
                                            $scope.assignTask();
                                        }
                                    }
                                } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                                    swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a la madre"), "warning");
                                } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                                    swal($filter('translate')("¡Nombre de la madre!"), $filter('translate')("Debes agregar nombre de la madre"), "warning");
                                } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                                    swal($filter('translate')("¡Apellidos de la madre!"), $filter('translate')("Debes agregar los apellidos de la madre"), "warning");
                                } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                                    swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si la madre vive"), "warning");
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                        swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la universidad Anáhuac"), "warning");
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                            swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                                        } else {
                                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                    swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                    swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                }
                                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                } */
                                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            //$scope.properties.selectedIndex++;
                                                            $scope.assignTask();
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                }
                                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                }*/
                                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            //$scope.properties.selectedIndex++;
                                                            $scope.assignTask();
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                            swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                            }
                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                        }*/
                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        //$scope.properties.selectedIndex++;
                                                        $scope.assignTask();
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                            }
                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                        } */
                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        //$scope.properties.selectedIndex++;
                                                        $scope.assignTask();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        //$scope.properties.selectedIndex++;
                                        $scope.assignTask();
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico del padre"), "warning");
                            } else if (!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                            } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad del padre"), "warning");
                            } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio del padre"), "warning");
                            }
                            /* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                                        }*/
                            else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                            } else if (($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) && $scope.properties.padreInput.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio del padre"), "warning");
                            } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio del padre"), "warning");
                            } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                            } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio del padre"), "warning");
                            } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono del padre"), "warning");
                            } else {
                                //VALIDAR MADRE 2
                                if ($scope.properties.madreInput.desconozcoDatosPadres) {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            //$scope.properties.selectedIndex++;
                                            $scope.assignTask();
                                        }
                                    }
                                } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                                    swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a la madre"), "warning");
                                } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                                    swal($filter('translate')("¡Nombre de la madre!"), $filter('translate')("Debes agregar nombre de la madre"), "warning");
                                } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                                    swal($filter('translate')("¡Apellidos de la madre!"), $filter('translate')("Debes agregar los apellidos de la madre"), "warning");
                                } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                                    swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si la madre vive"), "warning");
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                        swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la universidad Anáhuac"), "warning");
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                            swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                                        } else {
                                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                    swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                    swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                }
                                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                } */
                                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            //$scope.properties.selectedIndex++;
                                                            $scope.assignTask();
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                }
                                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                } */
                                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            //$scope.properties.selectedIndex++;
                                                            $scope.assignTask();
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                            swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                            }
                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                        }*/
                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        //$scope.properties.selectedIndex++;
                                                        $scope.assignTask();
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                            }
                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                        }*/
                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        //$scope.properties.selectedIndex++;
                                                        $scope.assignTask();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        //$scope.properties.selectedIndex++;
                                        $scope.assignTask();
                                    }
                                }
                            }
                        }
                    }
                } else if ($scope.properties.padreInput.catTrabaja === 0 || $scope.properties.padreInput.catTrabaja === null) {
                    swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu padre trabaja"), "warning");
                } else if ($scope.properties.datosPadres.padretrabaja) {
                    if ($scope.properties.padreInput.empresaTrabaja === "" || $scope.properties.padreInput.empresaTrabaja === undefined) {
                        swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu padre trabaja"), "warning");
                    } else if ($scope.properties.padreInput.puesto === "" || $scope.properties.padreInput.puesto === undefined) {
                        swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo del padre"), "warning");
                    } else if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico del padre"), "warning");
                    } else if (!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())) {
                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                    } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                        swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad del padre"), "warning");
                    } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                        swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio del padre"), "warning");
                    }
                    /* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                            swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                        }*/
                    else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                        swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                    } else if (($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) && $scope.properties.padreInput.catPais.descripcion === "México") {
                        swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio del padre"), "warning");
                    } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                        swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio del padre"), "warning");
                    } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                        swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                    } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                        swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio del padre"), "warning");
                    } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                        swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono del padre"), "warning");
                    } else {
                        ///Validar madre  3
                        if ($scope.properties.madreInput.desconozcoDatosPadres) {
                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                    $scope.properties.pasoInformacionFamiliar = true;
                                    //$scope.properties.selectedIndex++;
                                    $scope.assignTask();
                                }
                            }
                        } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                            swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a la madre"), "warning");
                        } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                            swal($filter('translate')("¡Nombre de la madre!"), $filter('translate')("Debes agregar nombre de la madre"), "warning");
                        } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                            swal($filter('translate')("¡Apellidos de la madre!"), $filter('translate')("Debes agregar los apellidos de la madre"), "warning");
                        } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                            swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si la madre vive"), "warning");
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la universidad Anáhuac"), "warning");
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                    swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                                } else {
                                    if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                        swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                            swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                        } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                            swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                        }
                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                } */
                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    //$scope.properties.selectedIndex++;
                                                    $scope.assignTask();
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                        }
                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                }*/
                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    //$scope.properties.selectedIndex++;
                                                    $scope.assignTask();
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                    swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                        swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                    } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                        swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                    } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                        swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                        swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                    }
                                    /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                        }*/
                                    else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                        swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                    } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                        swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                        swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                        swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                        swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                        swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                            swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                $scope.properties.pasoInformacionFamiliar = true;
                                                //$scope.properties.selectedIndex++;
                                                $scope.assignTask();
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                    } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                        swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                        swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                    }
                                    /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                        }*/
                                    else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                        swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                    } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                        swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                        swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                        swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                        swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                        swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                            swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                $scope.properties.pasoInformacionFamiliar = true;
                                                //$scope.properties.selectedIndex++;
                                                $scope.assignTask();
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                $scope.properties.pasoInformacionFamiliar = true;
                                //$scope.properties.selectedIndex++;
                                $scope.assignTask();
                            }
                        }
                    }
                } else {
                    if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico del padre"), "warning");
                    } else if (!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())) {
                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                    } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                        swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad del padre"), "warning");
                    } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                        swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio del padre"), "warning");
                    }
                    /* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                            swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                        }*/
                    else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                        swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                    } else if (($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) && $scope.properties.padreInput.catPais.descripcion === "México") {
                        swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio del padre"), "warning");
                    } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                        swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio del padre"), "warning");
                    } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                        swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                    } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                        swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio del padre"), "warning");
                    } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                        swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono del padre"), "warning");
                    } else {
                        //VALIDAR MADRE 4
                        if ($scope.properties.madreInput.desconozcoDatosPadres) {
                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                    $scope.properties.pasoInformacionFamiliar = true;
                                    //$scope.properties.selectedIndex++;
                                    $scope.assignTask();
                                }
                            }
                        } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                            swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a la madre"), "warning");
                        } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                            swal($filter('translate')("¡Nombre de la madre!"), $filter('translate')("Debes agregar nombre de la madre"), "warning");
                        } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                            swal($filter('translate')("¡Apellidos de la madre!"), $filter('translate')("Debes agregar los apellidos de la madre"), "warning");
                        } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                            swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si la madre vive"), "warning");
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la universidad Anáhuac"), "warning");
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                    swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                                } else {
                                    if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                        swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                            swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                        } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                            swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                        }
                                        /*else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                   swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                               } */
                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    //$scope.properties.selectedIndex++;
                                                    $scope.assignTask();
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                        }
                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                }*/
                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    //$scope.properties.selectedIndex++;
                                                    $scope.assignTask();
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                    swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                        swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                    } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                        swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                    } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                        swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                        swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                    }
                                    /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                        }*/
                                    else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                        swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                    } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                        swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                        swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                        swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                        swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                        swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                            swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                $scope.properties.pasoInformacionFamiliar = true;
                                                //$scope.properties.selectedIndex++;
                                                $scope.assignTask();
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                    } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                        swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                        swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                    }
                                    /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                        }*/
                                    else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                        swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                    } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                        swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                        swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                        swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                        swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                        swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                            swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                $scope.properties.pasoInformacionFamiliar = true;
                                                //$scope.properties.selectedIndex++;
                                                $scope.assignTask();
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                $scope.properties.pasoInformacionFamiliar = true;
                                //$scope.properties.selectedIndex++;
                                $scope.assignTask();
                            }
                        }
                    }
                }

            } else {
                console.log("esta validando el la linea 373")
                if ($scope.properties.madreInput.desconozcoDatosPadres) {
                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                            $scope.properties.pasoInformacionFamiliar = true;
                            //$scope.properties.selectedIndex++;
                            $scope.assignTask();
                        }
                    }
                } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                    swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a la madre"), "warning");
                } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                    swal($filter('translate')("¡Nombre de la madre!"), $filter('translate')("Debes agregar nombre de la madre"), "warning");
                } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                    swal($filter('translate')("¡Apellidos de la madre!"), $filter('translate')("Debes agregar los apellidos de la madre"), "warning");
                } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                    swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si la madre vive"), "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                        swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la universidad Anáhuac"), "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                            swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                        } else {
                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                    swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                    swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                }
                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                }*/
                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                } else {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            //$scope.properties.selectedIndex++;
                                            $scope.assignTask();
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                }
                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                }*/
                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                } else {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            //$scope.properties.selectedIndex++;
                                            $scope.assignTask();
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                            swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                            }
                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                        }*/
                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                            } else {
                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        //$scope.properties.selectedIndex++;
                                        $scope.assignTask();
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                            }
                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                        }*/
                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                            } else {
                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        //$scope.properties.selectedIndex++;
                                        $scope.assignTask();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                            $scope.properties.pasoInformacionFamiliar = true;
                            //$scope.properties.selectedIndex++;
                            $scope.assignTask();
                        }
                    }
                }
            }
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }*/



        } else if ($scope.properties.selectedIndex === 4) {
            console.log("validar 4");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {

                //$scope.properties.selectedIndex++;
                $scope.assignTask();
            }
        } else if ($scope.properties.selectedIndex === 5) {
            console.log("validar 4");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {

                //$scope.properties.selectedIndex++;
                $scope.assignTask();
            }
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
        var req = {
            method: "GET",
            url: "../API/system/session/unusedid",
        };
        return $http(req).success(function(data, status) {
                let url = "../API/bpm/userTask/" + $scope.properties.taskId;
                if ($scope.properties.catSolicitudDeAdmision.correoElectronico != data.user_name) {
                    swal("¡Error!", "Su sesion ha expirado", "warning");
                    setTimeout(function() { window.top.location.href = $scope.properties.urlDireccion }, 3000);
                } else {
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
                            function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
                                var cont = 0;
                                $scope.action = function() {
                                    const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                                    $scope.properties.disabled = true;
                                    var localS = localStorage;
                                    console.log("boton de siguiente");
                                    console.log($scope.properties.collageBoard);
                                    if ($scope.properties.selectedIndex === 0) {
                                        console.log("validar 0");
                                        if ($scope.properties.catSolicitudDeAdmision.catCampus.persistenceId_string === "") {
                                            swal($filter('translate')("¡Campus!"), $filter('translate')("Debes seleccionar un campus donde cursarás sus estudios"), "warning");
                                        } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                                            swal($filter('translate')("¡Licenciatura!"), $filter('translate')("Debes seleccionar una licenciatura"), "warning");
                                        } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                                            if ($scope.properties.catSolicitudDeAdmision.catPropedeutico === null) {
                                                swal($filter('translate')("¡Curso propedéutico!"), $filter('translate')("Favor de seleccionar un curso propedéutico"), "warning");
                                            } else {
                                                if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                                                    swal($filter('translate')("¡Período!"), $filter('translate')("Debes seleccionar un período donde cursarás sus estudios"), "warning");
                                                } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un lugar donde realizarás tu examen"), "warning");
                                                } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                                                    if ($scope.properties.lugarexamen === "En un estado") {
                                                        if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null) {
                                                            swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un estado donde realizarás el examen"), "warning");
                                                        } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                                            swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar una ciudad donde realizarás el examen"), "warning");
                                                        } else if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            $scope.properties.selectedIndex++;
                                                            //$scope.assignTask();
                                                        }
                                                    } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                                                        if ($scope.properties.catSolicitudDeAdmision.catPaisExamen === null) {
                                                            swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un país donde realizarás el examen"), "warning");
                                                        } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                                            swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar una ciudad donde realizarás el examen"), "warning");
                                                        } else {
                                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                $scope.properties.selectedIndex--;
                                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                                $scope.properties.selectedIndex++;
                                                                //$scope.assignTask();
                                                            }
                                                        }
                                                    } else {
                                                        //$scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                                                        //$scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            $scope.properties.selectedIndex++;
                                                            //$scope.assignTask();
                                                        }
                                                    }

                                                } else {
                                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un lugar donde realizarás el examen"), "warning");
                                                }
                                            }
                                        } else if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                                            swal($filter('translate')("¡Período!"), $filter('translate')("Debes seleccionar un período donde cursarás sus estudios"), "warning");
                                        } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                                            swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un lugar donde realizarás tu examen"), "warning");
                                        } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                                            if ($scope.properties.lugarexamen === "En un estado") {
                                                if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null) {
                                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un estado donde realizarás el examen"), "warning");
                                                } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar una ciudad donde realizarás el examen"), "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        $scope.properties.selectedIndex++;
                                                        //$scope.assignTask();
                                                    }
                                                }
                                            } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                                                if ($scope.properties.catSolicitudDeAdmision.catPaisExamen === null) {
                                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un país donde realizarás el examen"), "warning");
                                                } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar una ciudad donde realizarás el examen"), "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        $scope.properties.selectedIndex++;
                                                        //$scope.assignTask();
                                                    }
                                                }
                                            } else {
                                                //$scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                                                //$scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    $scope.properties.selectedIndex++;
                                                    //$scope.assignTask();
                                                }
                                            }

                                        } else {
                                            swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un lugar donde realizarás el examen"), "warning");
                                        }
                                    } else if ($scope.properties.selectedIndex === 1) {

                                        console.log("validar 1");
                                        /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            $scope.properties.selectedIndex++;
                                        }*/
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.catSolicitudDeAdmision.primerNombre === "") {
                                            swal($filter('translate')("¡Nombre!"), $filter('translate')("Debes ingresar tu primer nombre"), "warning");
                                        } else if ($scope.properties.catSolicitudDeAdmision.apellidoPaterno === "") {
                                            swal($filter('translate')("¡Apellido paterno!"), $filter('translate')("Debes ingresar tu apellido paterno"), "warning");
                                        } else if ($scope.properties.catSolicitudDeAdmision.apellidoMaterno === "") {
                                            swal("¡Apellido materno!", "Debes ingresar tu apellido materno", "warning");
                                        } else if ($scope.properties.catSolicitudDeAdmision.correoElectronico === "") {
                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes ingresar tu correo electrónico"), "warning");
                                        } else if (!re.test(String($scope.properties.catSolicitudDeAdmision.correoElectronico.trim()).toLowerCase())) {
                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                        } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                                            swal($filter('translate')("¡Licenciatura!"), $filter('translate')("Debes seleccionar una licenciatura"), "warning");
                                        } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                                            if ($scope.properties.catSolicitudDeAdmision.catPropedeutico === null) {
                                                swal($filter('translate')("¡Examen propedéutico!"), $filter('translate')("Favor de seleccionar un examen propedéutico"), "warning");
                                            } else {
                                                if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un lugar donde realizarás tu examen"), "warning");
                                                } else if ($scope.properties.catSolicitudDeAdmision.avisoPrivacidad === false) {
                                                    swal($filter('translate')("¡Aviso de privacidad!"), $filter('translate')("Debes aceptar el aviso de privacidad"), "warning");
                                                } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                                                    if ($scope.properties.lugarexamen === "En un estado") {
                                                        if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                                            swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un estado y una ciudad donde realizarás el examen"), "warning");
                                                        } else {
                                                            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                $scope.properties.selectedIndex--;
                                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                                $scope.properties.selectedIndex++;
                                                            }*/


                                                            openModal($scope.properties.modalid);
                                                        }
                                                    } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                                                        if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                                            swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un país y una ciudad donde realizarás el examen"), "warning");
                                                        } else {
                                                            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                $scope.properties.selectedIndex--;
                                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                                $scope.properties.selectedIndex++;
                                                            }*/


                                                            openModal($scope.properties.modalid);
                                                        }
                                                    } else {
                                                        //$scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                                                        //$scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                                                        /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            $scope.properties.selectedIndex++;
                                                        }*/


                                                        openModal($scope.properties.modalid);
                                                    }
                                                }
                                            }
                                        } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                                            swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un lugar donde realizarás tu examen"), "warning");
                                        } else if ($scope.properties.catSolicitudDeAdmision.avisoPrivacidad === false) {
                                            swal($filter('translate')("¡Aviso de privacidad!"), $filter('translate')("Debes aceptar el aviso de privacidad"), "warning");
                                        } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                                            if ($scope.properties.lugarexamen === "En un estado") {
                                                if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un estado y una ciudad donde realizarás el examen"), "warning");
                                                } else {
                                                    /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        $scope.properties.selectedIndex++;
                                                    }*/


                                                    openModal($scope.properties.modalid);
                                                }
                                            } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                                                if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                                    swal($filter('translate')("¡Lugar de examen!"), $filter('translate')("Debes seleccionar un país y una ciudad donde realizarás el examen"), "warning");
                                                } else {
                                                    /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        $scope.properties.selectedIndex++;
                                                    }*/


                                                    openModal($scope.properties.modalid);
                                                }
                                            } else {
                                                // $scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                                                //$scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                                                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    $scope.properties.selectedIndex++;
                                                }*/


                                                openModal($scope.properties.modalid);
                                            }
                                        }
                                    } else if ($scope.properties.selectedIndex === 2) {
                                        $scope.faltacampo = false;
                                        console.log("validar 2");
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                            $scope.faltacampo = true;
                                        } else if ($scope.properties.catSolicitudDeAdmision.fechaNacimiento === undefined) {
                                            swal($filter('translate')("¡Fecha de nacimiento!"), $filter('translate')("Debes agregar tu fecha de nacimiento"), "warning");
                                            $scope.faltacampo = true;
                                        } else if ($scope.properties.catSolicitudDeAdmision.catNacionalidad === null) {
                                            swal($filter('translate')("¡Nacionalidad!"), $filter('translate')("Debes seleccionar tu nacionalidad"), "warning");
                                            $scope.faltacampo = true;
                                        } else if ($scope.properties.catSolicitudDeAdmision.catReligion === null) {
                                            swal($filter('translate')("¡Religión!"), $filter('translate')("Debes seleccionar tu religión"), "warning");
                                            $scope.faltacampo = true;
                                            /* } else if ($scope.properties.catSolicitudDeAdmision.curp === "" && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                                                 swal("¡CURP!", "Debes agregar tu CURP", "warning");
                                                 $scope.faltacampo = true;
                                             } else if ($scope.properties.catSolicitudDeAdmision.curp.length < 18 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                                                 swal("¡CURP", "Tu CURP Debes tener 18 caracteres", "warning");
                                                 $scope.faltacampo = true;*/
                                        } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular === undefined) {
                                            swal($filter('translate')("¡Teléfono celular!"), $filter('translate')("Debes agregar tu número celular"), "warning");
                                            $scope.faltacampo = true;
                                        } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular.length !== 10 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                                            swal($filter('translate')("¡Teléfono celular!"), $filter('translate')("Tu número de teléfono celular debe tener 10 dígitos"), "warning");
                                            $scope.faltacampo = true;
                                        }
                                        /* else if($scope.properties.catSolicitudDeAdmision.telefonoCelular.length !== 14 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana"){
                                                        swal("¡Teléfono celular", "Tu número de teléfono celular Debes ser de 14 dígitos", "warning");
                                                        $scope.faltacampo = true;
                                                    }*/
                                        else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular === "") {
                                            swal($filter('translate')("¡Teléfono celular!"), $filter('translate')("Debes agregar tu número celular"), "warning");
                                            $scope.faltacampo = true;
                                        } else if ($scope.properties.catSolicitudDeAdmision.catEstadoCivil === null) {
                                            swal($filter('translate')("¡Estado civil!"), $filter('translate')("Debes seleccionar tu estado civil"), "warning");
                                            $scope.faltacampo = true;
                                        } else if ($scope.properties.catSolicitudDeAdmision.catSexo.persistenceId_string === "") {
                                            swal($filter('translate')("¡Sexo!"), $filter('translate')("Debes seleccionar tu sexo"), "warning");
                                            $scope.faltacampo = true;
                                        } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus === null) {
                                            swal($filter('translate')("¡Presentaste examen en otro campus!"), $filter('translate')("Debes seleccionar si has realizado la solicitud en otro campus"), "warning");
                                            $scope.faltacampo = true;
                                        } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion === "Si") {
                                            if ($scope.properties.catSolicitudDeAdmision.catCampusPresentadoSolicitud.length === 0) {
                                                swal($filter('translate')("¡Campus presentado!"), $filter('translate')("Debes seleccionar el/los campus donde has presentado tu solicitud"), "warning");
                                                $scope.faltacampo = true;
                                            }
                                        }
                                        if (!$scope.faltacampo) {
                                            if ($scope.properties.catSolicitudDeAdmision.calle === "") {
                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle de tu domicilio"), "warning");
                                            } else if (($scope.properties.catSolicitudDeAdmision.codigoPostal === "" || $scope.properties.catSolicitudDeAdmision.codigoPostal === null) && $scope.properties.catSolicitudDeAdmision.catPais.descripcion === "México") {
                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal"), "warning");
                                            } else if ($scope.properties.catSolicitudDeAdmision.catPais === null) {
                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes seleccionar el país"), "warning");
                                            }
                                            /*else if ($scope.properties.catSolicitudDeAdmision.catEstado === null) {
                                                               swal("¡Estado!", "Debes seleccionar el estado", "warning");
                                                           } */
                                            else if ($scope.properties.catSolicitudDeAdmision.ciudad === "") {
                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar una ciudad"), "warning");
                                            }
                                            /*else if ($scope.properties.catSolicitudDeAdmision.calle2 === "") {
                                                               swal("¡Entre calles!", "Debes agregar entre que calles se encuentra tu domicilio", "warning");
                                                           }*/
                                            else if ($scope.properties.catSolicitudDeAdmision.numExterior === "") {
                                                swal($filter('translate')("¡Número!"), $filter('translate')("Debes agregar el número de tu domicilio"), "warning");
                                            } else if ($scope.properties.catSolicitudDeAdmision.colonia === "") {
                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia"), "warning");
                                            } else if ($scope.properties.catSolicitudDeAdmision.telefono === "") {
                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono"), "warning");
                                            } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos === null) {
                                                swal("¡Preparatoria!", "Debes seleccionar una preparatoria en caso de no encontrar la tuya seleccionar la opción otro", "warning");
                                            } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion === "Otro") {
                                                if ($scope.properties.datosPreparatoria.nombreBachillerato === "") {
                                                    swal($filter('translate')("¡Preparatoria!"), $filter('translate')("Debes agregar el nombre de tu preparatoria"), "warning");
                                                } else if ($scope.properties.datosPreparatoria.paisBachillerato === undefined || $scope.properties.datosPreparatoria.paisBachillerato === "") {
                                                    swal($filter('translate')("¡País de tu preparatoria!"), $filter('translate')("Debes agregar el país de tu preparatoria"), "warning");
                                                } else if ($scope.properties.datosPreparatoria.estadoBachillerato === undefined || $scope.properties.datosPreparatoria.estadoBachillerato === "") {
                                                    swal($filter('translate')("¡Estado de tu preparatoria!"), $filter('translate')("Debes agregar el estado de tu preparatoria"), "warning");
                                                } else if ($scope.properties.datosPreparatoria.ciudadBachillerato === undefined || $scope.properties.datosPreparatoria.ciudadBachillerato === "") {
                                                    swal($filter('translate')("¡Ciudad de tu preparatoria!"), $filter('translate')("Debes agregar la ciudad de tu preparatoria"), "warning");
                                                } else if (isNaN($scope.properties.catSolicitudDeAdmision.promedioGeneral) && $scope.properties.datosPreparatoria.paisBachillerato === 'México') {
                                                    swal($filter('translate')("¡Promedio!"), $filter('translate')("Debes agregar el promedio que obtuviste en tu preparatoria"), "warning");
                                                } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "" || $scope.properties.catSolicitudDeAdmision.promedioGeneral === null) {
                                                    swal($filter('translate')("¡Promedio!"), $filter('translate')("Debes agregar el promedio que obtuviste en tu preparatoria"), "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        $scope.properties.catSolicitudDeAdmision.bachillerato = $scope.properties.datosPreparatoria.nombreBachillerato;
                                                        $scope.properties.catSolicitudDeAdmision.paisBachillerato = $scope.properties.datosPreparatoria.paisBachillerato;
                                                        $scope.properties.catSolicitudDeAdmision.estadoBachillerato = $scope.properties.datosPreparatoria.estadoBachillerato;
                                                        $scope.properties.catSolicitudDeAdmision.ciudadBachillerato = $scope.properties.datosPreparatoria.ciudadBachillerato;


                                                        $scope.fallo = false;
                                                        $scope.properties.catSolicitudDeAdmision.promedioGeneral = $scope.properties.catSolicitudDeAdmision.promedioGeneral + "";
                                                        if ($scope.properties.kardexarchivo.length > 0) {
                                                            if (JSON.stringify($scope.properties.kardex) === "{}") {
                                                                $scope.fallo = true;
                                                                swal($filter('translate')("¡Constancia de estudios!"), $filter('translate')("Debes agregar tu constancia de calificaciones con promedio"), "warning");
                                                            } else {
                                                                var auxData = null;
                                                                if ($scope.properties.kardexarchivo[0].newValue === undefined) {
                                                                    auxData = $scope.properties.kardexarchivo[0];
                                                                } else {
                                                                    auxData = angular.copy($scope.properties.kardexarchivo[0].newValue);
                                                                }
                                                                auxData.filename = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.filename === '' ? null : $scope.properties.kardex.filename);
                                                                auxData.tempPath = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.tempPath === '' ? null : $scope.properties.kardex.tempPath);
                                                                auxData.contentType = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.contentType === '' ? null : $scope.properties.kardex.contentType);
                                                                if (auxData.id !== undefined) {
                                                                    /*$scope.properties.kardexarchivo[0] = {
                                                                        "id": angular.copy(auxData.id),
                                                                        "newValue": angular.copy(auxData)
                                                                    };*/
                                                                    $scope.properties.kardexarchivo[0]["newValue"] = angular.copy(auxData);
                                                                } else {
                                                                    if ($scope.properties.kardexarchivo[0].newValue.filename !== $scope.properties.kardex.filename) {
                                                                        $scope.properties.kardexarchivo[0] = ({
                                                                            "newValue": angular.copy(auxData)
                                                                        });
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            if ($scope.properties.kardex !== undefined) {
                                                                $scope.properties.kardexarchivo = [];
                                                                $scope.properties.kardexarchivo.push({
                                                                    "newValue": angular.copy($scope.properties.kardex)
                                                                });
                                                            } else {
                                                                $scope.fallo = true;
                                                                swal($filter('translate')("¡Constancia de estudios!"), $filter('translate')("Debes agregar tu constancia de calificaciones con promedio"), "warning");
                                                            }
                                                        }

                                                        if (!$scope.fallo) {
                                                            if ($scope.properties.catSolicitudDeAdmision.admisionAnahuac) {
                                                                if (JSON.stringify($scope.properties.cartaAA) === "{}") {
                                                                    $scope.fallo = true;
                                                                    swal($filter('translate')("¡Carta Admisión Anáhuac!"), $filter('translate')("Agrega tu carta que valida tu Admisión Anáhuac."), "warning");
                                                                } else {
                                                                    if ($scope.properties.cartaAAarchivo.length > 0) {
                                                                        var auxData = null;
                                                                        if ($scope.properties.cartaAAarchivo[0].newValue === undefined) {
                                                                            auxData = $scope.properties.cartaAAarchivo[0];
                                                                        } else {
                                                                            auxData = angular.copy($scope.properties.cartaAAarchivo[0].newValue);
                                                                        }
                                                                        auxData.filename = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.filename === '' ? null : $scope.properties.cartaAA.filename);
                                                                        auxData.tempPath = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.tempPath === '' ? null : $scope.properties.cartaAA.tempPath);
                                                                        auxData.contentType = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.contentType === '' ? null : $scope.properties.cartaAA.contentType);
                                                                        if (auxData.id !== undefined) {
                                                                            $scope.properties.cartaAAarchivo[0]["newValue"] = angular.copy(auxData);
                                                                            /* = ({
                                                                                                                                    "id": angular.copy(auxData.id),
                                                                                                                                    "newValue": angular.copy(auxData)
                                                                                                                                });*/
                                                                        } else {
                                                                            if ($scope.properties.cartaAAarchivo[0].newValue.filename !== $scope.properties.cartaAA.filename) {
                                                                                $scope.properties.cartaAAarchivo[0] = ({
                                                                                    "newValue": angular.copy(auxData)
                                                                                });
                                                                            }
                                                                        }
                                                                    } else {
                                                                        if ($scope.properties.cartaAA !== undefined && $scope.properties.cartaAA !== "") {
                                                                            $scope.properties.cartaAAarchivo = [];
                                                                            $scope.properties.cartaAAarchivo.push({
                                                                                "newValue": angular.copy($scope.properties.cartaAA)
                                                                            });
                                                                        } else {
                                                                            $scope.fallo = true;
                                                                            swal($filter('translate')("¡Carta Admisión Anáhuac!"), $filter('translate')("Agrega tu carta que valida tu Admisión Anáhuac."), "warning");
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if (!$scope.fallo) {

                                                            if ($scope.properties.catSolicitudDeAdmision.tienePAA) {
                                                                if ($scope.properties.catSolicitudDeAdmision.resultadoPAA === 0 || $scope.properties.catSolicitudDeAdmision.resultadoPAA === "" || $scope.properties.catSolicitudDeAdmision.resultadoPAA === null || $scope.properties.catSolicitudDeAdmision.resultadoPAA === undefined) {
                                                                    $scope.fallo = true;
                                                                    swal($filter('translate')("¡Resultado (PAA) del Examen College Board!"), $filter('translate')("Tu puntuación debe ser mayor a cero"), "warning");
                                                                } else if ($scope.properties.catSolicitudDeAdmision.resultadoPAA > 1) {
                                                                    if ($scope.properties.collageBoardarchivo.length > 0) {
                                                                        if (JSON.stringify($scope.properties.collageBoard) === "{}") {
                                                                            $scope.fallo = true;
                                                                            swal($filter('translate')("¡Constancia College Board!"), $filter('translate')("Debes agregar la constancia del resultado PAA como viene emitida por el College Board."), "warning");
                                                                        } else {
                                                                            var auxData = null;
                                                                            if ($scope.properties.collageBoardarchivo[0].newValue === undefined) {
                                                                                auxData = $scope.properties.collageBoardarchivo[0];
                                                                            } else {
                                                                                auxData = angular.copy($scope.properties.collageBoardarchivo[0].newValue);
                                                                            }
                                                                            auxData.filename = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.filename === '' ? null : $scope.properties.collageBoard.filename);
                                                                            auxData.tempPath = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.tempPath === '' ? null : $scope.properties.collageBoard.tempPath);
                                                                            auxData.contentType = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.contentType === '' ? null : $scope.properties.collageBoard.contentType);
                                                                            if (auxData.id !== undefined) {
                                                                                $scope.properties.collageBoardarchivo[0]["newValue"] = angular.copy(auxData);
                                                                                /*= ({
                                                                "id": angular.copy(auxData.id),
                                                                "newValue": angular.copy(auxData)
                                                            });*/
                                                                            } else {
                                                                                if ($scope.properties.collageBoardarchivo[0].newValue.filename !== $scope.properties.collageBoard.filename) {
                                                                                    $scope.properties.collageBoardarchivo[0] = ({
                                                                                        "newValue": angular.copy(auxData)
                                                                                    });
                                                                                }
                                                                            }
                                                                        }

                                                                    } else {
                                                                        if ($scope.properties.collageBoard !== undefined && $scope.properties.collageBoard !== "") {
                                                                            $scope.properties.collageBoardarchivo = [];
                                                                            $scope.properties.collageBoardarchivo.push({
                                                                                "newValue": angular.copy($scope.properties.collageBoard)
                                                                            });
                                                                        } else {
                                                                            $scope.fallo = true;
                                                                            swal($filter('translate')("¡Constancia College Board!"), $filter('translate')("Debes agregar la constancia del resultado PAA como viene emitida por el College Board."), "warning");
                                                                        }

                                                                    }
                                                                }
                                                            } else {
                                                                $scope.properties.collageBoardarchivo = [];
                                                                if ($scope.properties.catSolicitudDeAdmision.tieneDescuento === true) {
                                                                    if ($scope.properties.descuentoarchivo.length > 0) {
                                                                        if (JSON.stringify($scope.properties.descuento) === "{}") {
                                                                            $scope.fallo = true;
                                                                            swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                                                        } else {
                                                                            var auxData = null;
                                                                            if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                                                                                auxData = $scope.properties.descuentoarchivo[0];
                                                                            } else {
                                                                                auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                                                                            }
                                                                            auxData.filename = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.filename === '' ? null : $scope.properties.descuento.filename);
                                                                            auxData.tempPath = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.tempPath === '' ? null : $scope.properties.descuento.tempPath);
                                                                            auxData.contentType = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.contentType === '' ? null : $scope.properties.descuento.contentType);
                                                                            if (auxData.id !== undefined) {
                                                                                $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);
                                                                                /* = ({
                                                                                                                                        "id": angular.copy(auxData.id),
                                                                                                                                        "newValue": angular.copy(auxData)
                                                                                                                                    });*/
                                                                            } else {
                                                                                if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                                                                                    $scope.properties.descuentoarchivo[0] = ({
                                                                                        "newValue": angular.copy(auxData)
                                                                                    });
                                                                                }
                                                                            }
                                                                        }
                                                                    } else {
                                                                        if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                                                            $scope.properties.descuentoarchivo = [];
                                                                            $scope.properties.descuentoarchivo.push({
                                                                                "newValue": angular.copy($scope.properties.descuento)
                                                                            });
                                                                        } else {
                                                                            $scope.fallo = true;
                                                                            swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                                                        }
                                                                    }
                                                                } else {
                                                                    $scope.properties.descuentoarchivo = [];
                                                                    // if ($scope.properties.idExtranjero !== undefined) {
                                                                    //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                    // }
                                                                    if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                                        $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                    } else {
                                                                        $scope.properties.idExtranjero = "";
                                                                    }
                                                                    $scope.properties.pasoInformacionPersonal = true;
                                                                    //$scope.properties.selectedIndex++;
                                                                    $scope.assignTask();
                                                                    $scope.fallo = true;
                                                                }
                                                            }
                                                        }
                                                        if (!$scope.fallo) {
                                                            if ($scope.properties.catSolicitudDeAdmision.tieneDescuento === true) {
                                                                if ($scope.properties.descuentoarchivo.length > 0) {
                                                                    if (JSON.stringify($scope.properties.descuento) === "{}") {
                                                                        $scope.fallo = true;
                                                                        swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                                                    } else {
                                                                        var auxData = null;
                                                                        if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                                                                            auxData = $scope.properties.descuentoarchivo[0];
                                                                        } else {
                                                                            auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                                                                        }
                                                                        auxData.filename = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.filename === '' ? null : $scope.properties.descuento.filename);
                                                                        auxData.tempPath = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.tempPath === '' ? null : $scope.properties.descuento.tempPath);
                                                                        auxData.contentType = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.contentType === '' ? null : $scope.properties.descuento.contentType);
                                                                        if (auxData.id !== undefined) {
                                                                            $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);
                                                                            /* = ({
                                                                                                                                "id": angular.copy(auxData.id),
                                                                                                                                "newValue": angular.copy(auxData)
                                                                                                                            });*/
                                                                        } else {
                                                                            if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                                                                                $scope.properties.descuentoarchivo[0] = ({
                                                                                    "newValue": angular.copy(auxData)
                                                                                });
                                                                            }
                                                                        }
                                                                        // if ($scope.properties.idExtranjero !== undefined) {
                                                                        //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                        // }
                                                                        if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                                            $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                        } else {
                                                                            $scope.properties.idExtranjero = "";
                                                                        }
                                                                        $scope.properties.pasoInformacionPersonal = true;
                                                                        $scope.assignTask();
                                                                        //$scope.properties.selectedIndex++;
                                                                    }
                                                                } else {
                                                                    if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                                                        $scope.properties.descuentoarchivo.push({
                                                                            "newValue": angular.copy($scope.properties.descuento)
                                                                        });
                                                                        // if ($scope.properties.idExtranjero !== undefined) {
                                                                        //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                        // }
                                                                        if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                                            $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                        } else {
                                                                            $scope.properties.idExtranjero = "";
                                                                        }
                                                                        $scope.properties.pasoInformacionPersonal = true;
                                                                        $scope.assignTask();
                                                                        //$scope.properties.selectedIndex++;
                                                                    } else {
                                                                        $scope.fallo = true;
                                                                        swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                                                    }
                                                                }
                                                            } else {
                                                                $scope.properties.descuentoarchivo = [];
                                                                // if ($scope.properties.idExtranjero !== undefined) {
                                                                //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                // }
                                                                if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                } else {
                                                                    $scope.properties.idExtranjero = "";
                                                                }
                                                                $scope.properties.pasoInformacionPersonal = true;
                                                                $scope.assignTask();
                                                                //$scope.properties.selectedIndex++;
                                                            }
                                                        }

                                                    }
                                                }
                                            } else if (isNaN($scope.properties.catSolicitudDeAdmision.promedioGeneral) && $scope.properties.datosPreparatoria.paisBachillerato === 'México') {
                                                swal($filter('translate')("¡Promedio!"), $filter('translate')("Debes agregar el promedio que obtuviste en tu preparatoria"), "warning");
                                            } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "" || $scope.properties.catSolicitudDeAdmision.promedioGeneral === null) {
                                                swal($filter('translate')("¡Promedio!"), $filter('translate')("Debes agregar el promedio que obtuviste en tu preparatoria"), "warning");
                                            } else {

                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    $scope.fallo = false;
                                                    if ($scope.properties.kardexarchivo.length > 0) {
                                                        if (JSON.stringify($scope.properties.kardex) === "{}") {
                                                            $scope.fallo = true;
                                                            swal($filter('translate')("¡Constancia de estudios!"), $filter('translate')("Debes agregar tu constancia de calificaciones con promedio"), "warning");
                                                        } else {
                                                            var auxData = null;
                                                            if ($scope.properties.kardexarchivo[0].newValue === undefined) {
                                                                auxData = $scope.properties.kardexarchivo[0];
                                                            } else {
                                                                auxData = angular.copy($scope.properties.kardexarchivo[0].newValue);
                                                            }
                                                            auxData.filename = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.filename === '' ? null : $scope.properties.kardex.filename);
                                                            auxData.tempPath = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.tempPath === '' ? null : $scope.properties.kardex.tempPath);
                                                            auxData.contentType = $scope.properties.kardex === undefined ? null : ($scope.properties.kardex.contentType === '' ? null : $scope.properties.kardex.contentType);
                                                            if (auxData.id !== undefined) {
                                                                $scope.properties.kardexarchivo[0]["newValue"] = angular.copy(auxData);
                                                                /* = {
                                                                                                        "id": angular.copy(auxData.id),
                                                                                                        "newValue": angular.copy(auxData)
                                                                                                    };*/
                                                            } else {
                                                                if ($scope.properties.kardexarchivo[0].newValue.filename !== $scope.properties.kardex.filename) {
                                                                    $scope.properties.kardexarchivo[0] = ({
                                                                        "newValue": angular.copy(auxData)
                                                                    });
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if ($scope.properties.kardex !== undefined) {
                                                            $scope.properties.kardexarchivo = [];
                                                            $scope.properties.kardexarchivo.push({
                                                                "newValue": angular.copy($scope.properties.kardex)
                                                            });
                                                        } else {
                                                            $scope.fallo = true;
                                                            swal($filter('translate')("¡Constancia de estudios!"), $filter('translate')("Debes agregar tu constancia de calificaciones con promedio"), "warning");
                                                        }
                                                    }

                                                    if (!$scope.fallo) {
                                                        if ($scope.properties.catSolicitudDeAdmision.admisionAnahuac) {
                                                            if (JSON.stringify($scope.properties.cartaAA) === "{}") {
                                                                $scope.fallo = true;
                                                                swal($filter('translate')("¡Carta Admisión Anáhuac!"), $filter('translate')("Agrega tu carta que valida tu Admisión Anáhuac."), "warning");
                                                            } else {
                                                                if ($scope.properties.cartaAAarchivo.length > 0) {
                                                                    var auxData = null;
                                                                    if ($scope.properties.cartaAAarchivo[0].newValue === undefined) {
                                                                        auxData = $scope.properties.cartaAAarchivo[0];
                                                                    } else {
                                                                        auxData = angular.copy($scope.properties.cartaAAarchivo[0].newValue);
                                                                    }
                                                                    auxData.filename = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.filename === '' ? null : $scope.properties.cartaAA.filename);
                                                                    auxData.tempPath = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.tempPath === '' ? null : $scope.properties.cartaAA.tempPath);
                                                                    auxData.contentType = $scope.properties.cartaAA === undefined ? null : ($scope.properties.cartaAA.contentType === '' ? null : $scope.properties.cartaAA.contentType);
                                                                    if (auxData.id !== undefined) {
                                                                        $scope.properties.cartaAAarchivo[0]["newValue"] = angular.copy(auxData);
                                                                        /* = ({
                                                                                                                            "id": angular.copy(auxData.id),
                                                                                                                            "newValue": angular.copy(auxData)
                                                                                                                        });*/
                                                                    } else {
                                                                        if ($scope.properties.cartaAAarchivo[0].newValue.filename !== $scope.properties.cartaAA.filename) {
                                                                            $scope.properties.cartaAAarchivo[0] = ({
                                                                                "newValue": angular.copy(auxData)
                                                                            });
                                                                        }
                                                                    }
                                                                } else {
                                                                    if ($scope.properties.cartaAA !== undefined && $scope.properties.cartaAA !== "") {
                                                                        $scope.properties.cartaAAarchivo = [];
                                                                        $scope.properties.cartaAAarchivo.push({
                                                                            "newValue": angular.copy($scope.properties.cartaAA)
                                                                        });
                                                                    } else {
                                                                        $scope.fallo = true;
                                                                        swal($filter('translate')("¡Carta Admisión Anáhuac!"), $filter('translate')("Agrega tu carta que valida tu Admisión Anáhuac."), "warning");
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                    if (!$scope.fallo) {

                                                        if ($scope.properties.catSolicitudDeAdmision.tienePAA) {
                                                            if ($scope.properties.catSolicitudDeAdmision.resultadoPAA === 0 || $scope.properties.catSolicitudDeAdmision.resultadoPAA === "" || $scope.properties.catSolicitudDeAdmision.resultadoPAA === null || $scope.properties.catSolicitudDeAdmision.resultadoPAA === undefined) {
                                                                $scope.fallo = true;
                                                                swal($filter('translate')("¡Resultado (PAA) del Examen College Board!"), $filter('translate')("Tu puntuación debe ser mayor a cero"), "warning");
                                                            } else if ($scope.properties.catSolicitudDeAdmision.resultadoPAA > 1) {
                                                                if (JSON.stringify($scope.properties.collageBoard) === "{}") {
                                                                    $scope.fallo = true;
                                                                    swal($filter('translate')("¡Constancia College Board!"), $filter('translate')("Debes agregar la constancia del resultado PAA como viene emitida por el College Board."), "warning");
                                                                } else {
                                                                    if ($scope.properties.collageBoardarchivo.length > 0) {
                                                                        var auxData = null;
                                                                        if ($scope.properties.collageBoardarchivo[0].newValue === undefined) {
                                                                            auxData = $scope.properties.collageBoardarchivo[0];
                                                                        } else {
                                                                            auxData = angular.copy($scope.properties.collageBoardarchivo[0].newValue);
                                                                        }
                                                                        auxData.filename = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.filename === '' ? null : $scope.properties.collageBoard.filename);
                                                                        auxData.tempPath = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.tempPath === '' ? null : $scope.properties.collageBoard.tempPath);
                                                                        auxData.contentType = $scope.properties.collageBoard === undefined ? null : ($scope.properties.collageBoard.contentType === '' ? null : $scope.properties.collageBoard.contentType);
                                                                        if (auxData.id !== undefined) {
                                                                            $scope.properties.collageBoardarchivo[0]["newValue"] = angular.copy(auxData);
                                                                            /* = ({
                                                                                                                                "id": angular.copy(auxData.id),
                                                                                                                                "newValue": angular.copy(auxData)
                                                                                                                            });*/
                                                                        } else {
                                                                            if ($scope.properties.collageBoardarchivo[0].newValue.filename !== $scope.properties.collageBoard.filename) {
                                                                                $scope.properties.collageBoardarchivo[0] = ({
                                                                                    "newValue": angular.copy(auxData)
                                                                                });
                                                                            }
                                                                        }
                                                                    } else {
                                                                        if ($scope.properties.collageBoard !== undefined && $scope.properties.collageBoard !== "") {
                                                                            $scope.properties.collageBoardarchivo = [];
                                                                            $scope.properties.collageBoardarchivo.push({
                                                                                "newValue": angular.copy($scope.properties.collageBoard)
                                                                            });
                                                                        } else {
                                                                            $scope.fallo = true;
                                                                            swal($filter('translate')("¡Constancia College Board!"), $filter('translate')("Debes agregar la constancia del resultado PAA como viene emitida por el College Board."), "warning");
                                                                        }

                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            $scope.properties.collageBoardarchivo = [];
                                                            if ($scope.properties.catSolicitudDeAdmision.tieneDescuento === true) {
                                                                if ($scope.properties.descuentoarchivo.length > 0) {
                                                                    if (JSON.stringify($scope.properties.descuento) === "{}") {
                                                                        $scope.fallo = true;
                                                                        swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                                                    } else {
                                                                        var auxData = null;
                                                                        if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                                                                            auxData = $scope.properties.descuentoarchivo[0];
                                                                        } else {
                                                                            auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                                                                        }
                                                                        auxData.filename = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.filename === '' ? null : $scope.properties.descuento.filename);
                                                                        auxData.tempPath = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.tempPath === '' ? null : $scope.properties.descuento.tempPath);
                                                                        auxData.contentType = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.contentType === '' ? null : $scope.properties.descuento.contentType);
                                                                        if (auxData.id !== undefined) {
                                                                            $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);
                                                                            /* = ({
                                                                                                                                "id": angular.copy(auxData.id),
                                                                                                                                "newValue": angular.copy(auxData)
                                                                                                                            });*/
                                                                        } else {
                                                                            if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                                                                                $scope.properties.descuentoarchivo[0] = ({
                                                                                    "newValue": angular.copy(auxData)
                                                                                });
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                                                        $scope.properties.descuentoarchivo = [];
                                                                        $scope.properties.descuentoarchivo.push({
                                                                            "newValue": angular.copy($scope.properties.descuento)
                                                                        });
                                                                    } else {
                                                                        $scope.fallo = true;
                                                                        swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                                                    }
                                                                }
                                                            } else {
                                                                $scope.properties.descuentoarchivo = [];
                                                                // if ($scope.properties.idExtranjero !== undefined) {
                                                                //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                // }
                                                                if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                } else {
                                                                    $scope.properties.idExtranjero = "";
                                                                }
                                                                $scope.properties.pasoInformacionPersonal = true;
                                                                //$scope.properties.selectedIndex++;
                                                                $scope.assignTask();
                                                                $scope.fallo = true;
                                                            }
                                                        }
                                                    }

                                                    if (!$scope.fallo) {
                                                        if ($scope.properties.catSolicitudDeAdmision.tieneDescuento === true) {
                                                            if ($scope.properties.descuentoarchivo.length > 0) {
                                                                if (JSON.stringify($scope.properties.descuento) === "{}") {
                                                                    $scope.fallo = true;
                                                                    swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                                                } else {
                                                                    var auxData = null;
                                                                    if ($scope.properties.descuentoarchivo[0].newValue === undefined) {
                                                                        auxData = $scope.properties.descuentoarchivo[0];
                                                                    } else {
                                                                        auxData = angular.copy($scope.properties.descuentoarchivo[0].newValue);
                                                                    }
                                                                    auxData.filename = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.filename === '' ? null : $scope.properties.descuento.filename);
                                                                    auxData.tempPath = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.tempPath === '' ? null : $scope.properties.descuento.tempPath);
                                                                    auxData.contentType = $scope.properties.descuento === undefined ? null : ($scope.properties.descuento.contentType === '' ? null : $scope.properties.descuento.contentType);
                                                                    if (auxData.id !== undefined) {
                                                                        $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);
                                                                        /* = ({
                                                                                                                        "id": angular.copy(auxData.id),
                                                                                                                        "newValue": angular.copy(auxData)
                                                                                                                    });*/
                                                                    } else {
                                                                        if ($scope.properties.descuentoarchivo[0].newValue.filename !== $scope.properties.descuento.filename) {
                                                                            $scope.properties.descuentoarchivo[0] = ({
                                                                                "newValue": angular.copy(auxData)
                                                                            });
                                                                        }
                                                                    }
                                                                    // if ($scope.properties.idExtranjero !== undefined) {
                                                                    //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                    // }
                                                                    if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                                        $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                    } else {
                                                                        $scope.properties.idExtranjero = "";
                                                                    }
                                                                    $scope.properties.pasoInformacionPersonal = true;
                                                                    //$scope.properties.selectedIndex++;
                                                                    $scope.assignTask();
                                                                }
                                                            } else {
                                                                if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                                                    $scope.properties.descuentoarchivo.push({
                                                                        "newValue": angular.copy($scope.properties.descuento)
                                                                    });
                                                                    // if ($scope.properties.idExtranjero !== undefined) {
                                                                    //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                    // }
                                                                    if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                                        $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                                    } else {
                                                                        $scope.properties.idExtranjero = "";
                                                                    }
                                                                    $scope.properties.pasoInformacionPersonal = true;
                                                                    //$scope.properties.selectedIndex++;
                                                                    $scope.assignTask();
                                                                } else {
                                                                    $scope.fallo = true;
                                                                    swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                                                }
                                                            }
                                                        } else {
                                                            $scope.properties.descuentoarchivo = [];
                                                            // if ($scope.properties.idExtranjero !== undefined) {
                                                            //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                            // }
                                                            if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                            } else {
                                                                $scope.properties.idExtranjero = "";
                                                            }
                                                            $scope.properties.pasoInformacionPersonal = true;
                                                            //$scope.properties.selectedIndex++;
                                                            $scope.assignTask();
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else if ($scope.properties.selectedIndex === 3) {
                                        console.log("validar 3");
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.tutorInput.length === 0) {
                                            swal($filter('translate')("¡Tutor!"), $filter('translate')("Debes agregar al menos un tutor"), "warning");
                                        } else if ($scope.properties.padreInput.desconozcoDatosPadres) {
                                            //validar madre
                                            if ($scope.properties.madreInput.desconozcoDatosPadres) {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        //$scope.properties.selectedIndex++;
                                                        $scope.assignTask();
                                                    }
                                                }
                                            } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                                                swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a la madre"), "warning");
                                            } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                                                swal($filter('translate')("¡Nombre de la madre!"), $filter('translate')("Debes agregar nombre de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                                                swal($filter('translate')("¡Apellidos de la madre!"), $filter('translate')("Debes agregar los apellidos de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                                                swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si la madre vive"), "warning");
                                            } else if ($scope.properties.datosPadres.madrevive) {
                                                if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                                    swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la universidad Anáhuac"), "warning");
                                                } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                                    if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                                        swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                                                    } else {
                                                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                            swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                                swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                                swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                            }
                                                            /*else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                               swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                           } */
                                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                            } else {
                                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                } else {
                                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                        $scope.properties.selectedIndex--;
                                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                                        //$scope.properties.selectedIndex++;
                                                                        $scope.assignTask();
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                            }
                                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                            }*/
                                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                            } else {
                                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                } else {
                                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                        $scope.properties.selectedIndex--;
                                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                                        //$scope.properties.selectedIndex++;
                                                                        $scope.assignTask();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                } else {
                                                    if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                        swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                                        if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                            swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                        } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                            swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                        }
                                                        /*else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                       swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                   } */
                                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                        } else {
                                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                            } else {
                                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                    $scope.properties.selectedIndex--;
                                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                                    //$scope.properties.selectedIndex++;
                                                                    $scope.assignTask();
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                        }
                                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                    } */
                                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                        } else {
                                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                            } else {
                                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                    $scope.properties.selectedIndex--;
                                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                                    //$scope.properties.selectedIndex++;
                                                                    $scope.assignTask();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    //$scope.properties.selectedIndex++;
                                                    $scope.assignTask();
                                                }
                                            }




                                        } else if ($scope.properties.padreInput.catTitulo === 0 || $scope.properties.padreInput.catTitulo === null) {
                                            swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar al padre"), "warning");
                                        } else if ($scope.properties.padreInput.nombre === "" || $scope.properties.padreInput.nombre === undefined) {
                                            swal($filter('translate')("¡Nombre del padre!"), $filter('translate')("Debes agregar nombre del padre"), "warning");
                                        } else if ($scope.properties.padreInput.apellidos === "" || $scope.properties.padreInput.apellidos === undefined) {
                                            swal($filter('translate')("¡Apellidos del padre!"), $filter('translate')("Debes agregar los apellidos del padre"), "warning");
                                        } else if ($scope.properties.padreInput.vive === 0 || $scope.properties.padreInput.vive === null) {
                                            swal($filter('translate')("¡Padre vive!"), $filter('translate')("Debes seleccionar si el padre vive"), "warning");
                                        } else if ($scope.properties.datosPadres.padrevive) {
                                            if ($scope.properties.padreInput.catEgresoAnahuac === 0 || $scope.properties.padreInput.catEgresoAnahuac === null) {
                                                swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu padre egresó de la universidad Anáhuac"), "warning");
                                            } else if ($scope.properties.datosPadres.padreegresoanahuac) {
                                                if ($scope.properties.padreInput.catCampusEgreso === 0 || $scope.properties.padreInput.catCampusEgreso === null) {
                                                    swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu padre"), "warning");
                                                } else {
                                                    if ($scope.properties.padreInput.catTrabaja === 0 || $scope.properties.padreInput.catTrabaja === null) {
                                                        swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu padre trabaja"), "warning");
                                                    } else if ($scope.properties.datosPadres.padretrabaja) {
                                                        if ($scope.properties.padreInput.empresaTrabaja === "" || $scope.properties.padreInput.empresaTrabaja === undefined) {
                                                            swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu padre trabaja"), "warning");
                                                        } else if ($scope.properties.padreInput.puesto === "" || $scope.properties.padreInput.puesto === undefined) {
                                                            swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo del padre"), "warning");
                                                        } else if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico del padre"), "warning");
                                                        } else if (!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())) {
                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                        } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad del padre"), "warning");
                                                        } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio del padre"), "warning");
                                                        }
                                                        /* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                                                                    } */
                                                        else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                                                        } else if (($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) && $scope.properties.padreInput.catPais.descripcion === "México") {
                                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio del padre"), "warning");
                                                        } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio del padre"), "warning");
                                                        } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                                                        } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio del padre"), "warning");
                                                        } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono del padre"), "warning");
                                                        } else {
                                                            ///Validar madre 1
                                                            if ($scope.properties.madreInput.desconozcoDatosPadres) {
                                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                } else {
                                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                        $scope.properties.selectedIndex--;
                                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                                        //$scope.properties.selectedIndex++;
                                                                        $scope.assignTask();
                                                                    }
                                                                }
                                                            } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                                                                swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                                                                swal($filter('translate')("¡Nombre de la madre!"), $filter('translate')("Debes agregar nombre de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                                                                swal($filter('translate')("¡Apellidos de la madre!"), $filter('translate')("Debes agregar los apellidos de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                                                                swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si la madre vive"), "warning");
                                                            } else if ($scope.properties.datosPadres.madrevive) {
                                                                if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                                                    swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la universidad Anáhuac"), "warning");
                                                                } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                                                    if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                                                        swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                                                                    } else {
                                                                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                                            swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                                                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                                                swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                                                swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                            }
                                                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                                                swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                                            } */
                                                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                            } else {
                                                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                                } else {
                                                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                                        $scope.properties.selectedIndex--;
                                                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                                                        //$scope.properties.selectedIndex++;
                                                                                        $scope.assignTask();
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else {
                                                                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                            }
                                                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                                                swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                                            }*/
                                                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                            } else {
                                                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                                } else {
                                                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                                        $scope.properties.selectedIndex--;
                                                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                                                        //$scope.properties.selectedIndex++;
                                                                                        $scope.assignTask();
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                } else {
                                                                    if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                                        swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                                                        if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                                            swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                                        } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                                            swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                        }
                                                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                                    }*/
                                                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                        } else {
                                                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                            } else {
                                                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                                    $scope.properties.selectedIndex--;
                                                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                                                    //$scope.properties.selectedIndex++;
                                                                                    $scope.assignTask();
                                                                                }
                                                                            }
                                                                        }
                                                                    } else {
                                                                        if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                        }
                                                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                                    } */
                                                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                        } else {
                                                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                            } else {
                                                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                                    $scope.properties.selectedIndex--;
                                                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                                                    //$scope.properties.selectedIndex++;
                                                                                    $scope.assignTask();
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                    $scope.properties.selectedIndex--;
                                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                                    //$scope.properties.selectedIndex++;
                                                                    $scope.assignTask();
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico del padre"), "warning");
                                                        } else if (!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())) {
                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                        } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad del padre"), "warning");
                                                        } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio del padre"), "warning");
                                                        }
                                                        /* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                                                                    }*/
                                                        else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                                                        } else if (($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) && $scope.properties.padreInput.catPais.descripcion === "México") {
                                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio del padre"), "warning");
                                                        } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio del padre"), "warning");
                                                        } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                                                        } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio del padre"), "warning");
                                                        } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono del padre"), "warning");
                                                        } else {
                                                            //VALIDAR MADRE 2
                                                            if ($scope.properties.madreInput.desconozcoDatosPadres) {
                                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                } else {
                                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                        $scope.properties.selectedIndex--;
                                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                                        //$scope.properties.selectedIndex++;
                                                                        $scope.assignTask();
                                                                    }
                                                                }
                                                            } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                                                                swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                                                                swal($filter('translate')("¡Nombre de la madre!"), $filter('translate')("Debes agregar nombre de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                                                                swal($filter('translate')("¡Apellidos de la madre!"), $filter('translate')("Debes agregar los apellidos de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                                                                swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si la madre vive"), "warning");
                                                            } else if ($scope.properties.datosPadres.madrevive) {
                                                                if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                                                    swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la universidad Anáhuac"), "warning");
                                                                } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                                                    if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                                                        swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                                                                    } else {
                                                                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                                            swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                                                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                                                swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                                                swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                            }
                                                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                                                swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                                            } */
                                                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                            } else {
                                                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                                } else {
                                                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                                        $scope.properties.selectedIndex--;
                                                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                                                        //$scope.properties.selectedIndex++;
                                                                                        $scope.assignTask();
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else {
                                                                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                            }
                                                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                                                swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                                            } */
                                                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                            } else {
                                                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                                } else {
                                                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                                        $scope.properties.selectedIndex--;
                                                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                                                        //$scope.properties.selectedIndex++;
                                                                                        $scope.assignTask();
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                } else {
                                                                    if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                                        swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                                                        if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                                            swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                                        } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                                            swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                        }
                                                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                                    }*/
                                                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                        } else {
                                                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                            } else {
                                                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                                    $scope.properties.selectedIndex--;
                                                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                                                    //$scope.properties.selectedIndex++;
                                                                                    $scope.assignTask();
                                                                                }
                                                                            }
                                                                        }
                                                                    } else {
                                                                        if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                        }
                                                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                                    }*/
                                                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                        } else {
                                                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                            } else {
                                                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                                    $scope.properties.selectedIndex--;
                                                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                                                    //$scope.properties.selectedIndex++;
                                                                                    $scope.assignTask();
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                    $scope.properties.selectedIndex--;
                                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                                    //$scope.properties.selectedIndex++;
                                                                    $scope.assignTask();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else if ($scope.properties.padreInput.catTrabaja === 0 || $scope.properties.padreInput.catTrabaja === null) {
                                                swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu padre trabaja"), "warning");
                                            } else if ($scope.properties.datosPadres.padretrabaja) {
                                                if ($scope.properties.padreInput.empresaTrabaja === "" || $scope.properties.padreInput.empresaTrabaja === undefined) {
                                                    swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu padre trabaja"), "warning");
                                                } else if ($scope.properties.padreInput.puesto === "" || $scope.properties.padreInput.puesto === undefined) {
                                                    swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo del padre"), "warning");
                                                } else if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico del padre"), "warning");
                                                } else if (!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad del padre"), "warning");
                                                } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio del padre"), "warning");
                                                }
                                                /* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                                                    }*/
                                                else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                                                } else if (($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) && $scope.properties.padreInput.catPais.descripcion === "México") {
                                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio del padre"), "warning");
                                                } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio del padre"), "warning");
                                                } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                                                } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio del padre"), "warning");
                                                } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono del padre"), "warning");
                                                } else {
                                                    ///Validar madre  3
                                                    if ($scope.properties.madreInput.desconozcoDatosPadres) {
                                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                            swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                        } else {
                                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                $scope.properties.selectedIndex--;
                                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                $scope.properties.pasoInformacionFamiliar = true;
                                                                //$scope.properties.selectedIndex++;
                                                                $scope.assignTask();
                                                            }
                                                        }
                                                    } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                                                        swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a la madre"), "warning");
                                                    } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                                                        swal($filter('translate')("¡Nombre de la madre!"), $filter('translate')("Debes agregar nombre de la madre"), "warning");
                                                    } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                                                        swal($filter('translate')("¡Apellidos de la madre!"), $filter('translate')("Debes agregar los apellidos de la madre"), "warning");
                                                    } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                                                        swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si la madre vive"), "warning");
                                                    } else if ($scope.properties.datosPadres.madrevive) {
                                                        if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                                            swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la universidad Anáhuac"), "warning");
                                                        } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                                            if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                                                swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                                                            } else {
                                                                if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                                    swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                                                    if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                                        swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                                    } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                                        swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                    } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                        swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                        swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                    }
                                                                    /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                                swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                            } */
                                                                    else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                        swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                    } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                        swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                        swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                        swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                        swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                        swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                    } else {
                                                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                            swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                        } else {
                                                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                                $scope.properties.selectedIndex--;
                                                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                                $scope.properties.pasoInformacionFamiliar = true;
                                                                                //$scope.properties.selectedIndex++;
                                                                                $scope.assignTask();
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                    } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                        swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                        swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                    }
                                                                    /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                                swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                            }*/
                                                                    else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                        swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                    } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                        swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                        swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                        swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                        swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                        swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                    } else {
                                                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                            swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                        } else {
                                                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                                $scope.properties.selectedIndex--;
                                                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                                $scope.properties.pasoInformacionFamiliar = true;
                                                                                //$scope.properties.selectedIndex++;
                                                                                $scope.assignTask();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                        } else {
                                                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                                swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                                    swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                                    swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                }
                                                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                    }*/
                                                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                } else {
                                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                    } else {
                                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                            $scope.properties.selectedIndex--;
                                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                                            //$scope.properties.selectedIndex++;
                                                                            $scope.assignTask();
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                }
                                                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                    }*/
                                                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                } else {
                                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                    } else {
                                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                            $scope.properties.selectedIndex--;
                                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                                            //$scope.properties.selectedIndex++;
                                                                            $scope.assignTask();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            //$scope.properties.selectedIndex++;
                                                            $scope.assignTask();
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico del padre"), "warning");
                                                } else if (!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad del padre"), "warning");
                                                } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio del padre"), "warning");
                                                }
                                                /* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                                                    }*/
                                                else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                                                } else if (($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) && $scope.properties.padreInput.catPais.descripcion === "México") {
                                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio del padre"), "warning");
                                                } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio del padre"), "warning");
                                                } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio del padre"), "warning");
                                                } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio del padre"), "warning");
                                                } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono del padre"), "warning");
                                                } else {
                                                    //VALIDAR MADRE 4
                                                    if ($scope.properties.madreInput.desconozcoDatosPadres) {
                                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                            swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                        } else {
                                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                $scope.properties.selectedIndex--;
                                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                $scope.properties.pasoInformacionFamiliar = true;
                                                                //$scope.properties.selectedIndex++;
                                                                $scope.assignTask();
                                                            }
                                                        }
                                                    } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                                                        swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a la madre"), "warning");
                                                    } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                                                        swal($filter('translate')("¡Nombre de la madre!"), $filter('translate')("Debes agregar nombre de la madre"), "warning");
                                                    } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                                                        swal($filter('translate')("¡Apellidos de la madre!"), $filter('translate')("Debes agregar los apellidos de la madre"), "warning");
                                                    } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                                                        swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si la madre vive"), "warning");
                                                    } else if ($scope.properties.datosPadres.madrevive) {
                                                        if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                                            swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la universidad Anáhuac"), "warning");
                                                        } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                                            if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                                                swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                                                            } else {
                                                                if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                                    swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                                                    if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                                        swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                                    } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                                        swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                    } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                        swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                        swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                    }
                                                                    /*else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                               swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                           } */
                                                                    else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                        swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                    } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                        swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                        swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                        swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                        swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                        swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                    } else {
                                                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                            swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                        } else {
                                                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                                $scope.properties.selectedIndex--;
                                                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                                $scope.properties.pasoInformacionFamiliar = true;
                                                                                //$scope.properties.selectedIndex++;
                                                                                $scope.assignTask();
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                    } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                        swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                        swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                        swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                    }
                                                                    /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                                swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                            }*/
                                                                    else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                        swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                    } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                        swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                        swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                        swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                        swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                        swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                    } else {
                                                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                            swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                        } else {
                                                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                                $scope.properties.selectedIndex--;
                                                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                                $scope.properties.pasoInformacionFamiliar = true;
                                                                                //$scope.properties.selectedIndex++;
                                                                                $scope.assignTask();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                        } else {
                                                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                                swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                                    swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                                    swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                }
                                                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                    }*/
                                                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                } else {
                                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                    } else {
                                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                            $scope.properties.selectedIndex--;
                                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                                            //$scope.properties.selectedIndex++;
                                                                            $scope.assignTask();
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Tu correo electrónico no es válido"), "warning");
                                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                                }
                                                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                    }*/
                                                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                                } else {
                                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                        swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                    } else {
                                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                            $scope.properties.selectedIndex--;
                                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                                            //$scope.properties.selectedIndex++;
                                                                            $scope.assignTask();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            //$scope.properties.selectedIndex++;
                                                            $scope.assignTask();
                                                        }
                                                    }
                                                }
                                            }

                                        } else {
                                            console.log("esta validando el la linea 373")
                                            if ($scope.properties.madreInput.desconozcoDatosPadres) {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        //$scope.properties.selectedIndex++;
                                                        $scope.assignTask();
                                                    }
                                                }
                                            } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                                                swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a la madre"), "warning");
                                            } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                                                swal($filter('translate')("¡Nombre de la madre!"), $filter('translate')("Debes agregar nombre de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                                                swal($filter('translate')("¡Apellidos de la madre!"), $filter('translate')("Debes agregar los apellidos de la madre"), "warning");
                                            } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                                                swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si la madre vive"), "warning");
                                            } else if ($scope.properties.datosPadres.madrevive) {
                                                if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                                    swal($filter('translate')("¡Egreso Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la universidad Anáhuac"), "warning");
                                                } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                                    if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                                        swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                                                    } else {
                                                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                            swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                                swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                                swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                            }
                                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                            }*/
                                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                            } else {
                                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                } else {
                                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                        $scope.properties.selectedIndex--;
                                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                                        //$scope.properties.selectedIndex++;
                                                                        $scope.assignTask();
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                            }
                                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                            }*/
                                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                            } else {
                                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                                } else {
                                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                        $scope.properties.selectedIndex--;
                                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                                        //$scope.properties.selectedIndex++;
                                                                        $scope.assignTask();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                } else {
                                                    if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                        swal($filter('translate')("¡Trabaja!"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                                        if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                            swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                                        } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                            swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                        }
                                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                    }*/
                                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                        } else {
                                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                            } else {
                                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                    $scope.properties.selectedIndex--;
                                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                                    //$scope.properties.selectedIndex++;
                                                                    $scope.assignTask();
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de la madre"), "warning");
                                                        }
                                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                        swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                    }*/
                                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México") {
                                                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la calle del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de la madre"), "warning");
                                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de la madre"), "warning");
                                                        } else {
                                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                                swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                            } else {
                                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                                    $scope.properties.selectedIndex--;
                                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                                    //$scope.properties.selectedIndex++;
                                                                    $scope.assignTask();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        //$scope.properties.selectedIndex++;
                                                        $scope.assignTask();
                                                    }
                                                }
                                            }
                                        }
                                        /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            $scope.properties.selectedIndex++;
                                        }*/



                                    } else if ($scope.properties.selectedIndex === 4) {
                                        console.log("validar 4");
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {

                                            //$scope.properties.selectedIndex++;
                                            $scope.assignTask();
                                        }
                                    } else if ($scope.properties.selectedIndex === 5) {
                                        console.log("validar 4");
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {

                                            //$scope.properties.selectedIndex++;
                                            $scope.assignTask();
                                        }
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
                                    if ($scope.properties.catSolicitudDeAdmision.correoElectronico != $scope.properties.userName) {
                                        swal("¡Error!", "Su sesion ha expirado", "warning");
                                        setTimeout(function() { window.top.location.href = $scope.properties.urlDireccion }, 3000);
                                    } else {
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
                                    $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndex = $scope.properties.selectedIndex + 1;
                                    if ($scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos !== null) {
                                        if ($scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string !== undefined && $scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string !== null) {
                                            $scope.properties.Bachilleratopersistenceid = angular.copy($scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string);
                                            //$scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos.persistenceId_string = $scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string;
                                        }
                                    }

                                    if ($scope.properties.Bachilleratopersistenceid !== undefined && $scope.properties.Bachilleratopersistenceid !== null && $scope.properties.Bachilleratopersistenceid !== "") {
                                        if ($scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos === null) {
                                            $scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos = {};
                                            $scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos["persistenceId_string"] = $scope.properties.Bachilleratopersistenceid
                                        } else {
                                            $scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos.persistenceId_string = $scope.properties.Bachilleratopersistenceid;
                                        }
                                    }
                                    console.log($scope.properties.dataToSend);
                                    var req = {
                                        method: method,
                                        url: url,
                                        data: angular.copy($scope.properties.dataToSend),
                                        params: params
                                    };

                                    return $http(req)
                                        .success(function(data, status) {
                                            /* $scope.properties.dataFromSuccess = data;
                                             $scope.properties.responseStatusCode = status;
                                             $scope.properties.dataFromError = undefined;
                                             notifyParentFrame({
                                                 message: 'success',
                                                 status: status,
                                                 dataFromSuccess: data,
                                                 dataFromError: undefined,
                                                 responseStatusCode: status
                                             });*/
                                            /*if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                                                redirectIfNeeded();
                                            }
                                            closeModal($scope.properties.closeOnSuccess);*/
                                            getTask();
                                            //topFunction();
                                            //$scope.properties.selectedIndex++;
                                        })
                                        .error(function(data, status) {
                                            console.log("Error al avanzar tarea")
                                            console.log(data);
                                            console.log(status);
                                            /*$scope.properties.dataFromError = data;
                                            $scope.properties.responseStatusCode = status;
                                            $scope.properties.dataFromSuccess = undefined;
                                            notifyParentFrame({
                                                message: 'error',
                                                status: status,
                                                dataFromError: data,
                                                dataFromSuccess: undefined,
                                                responseStatusCode: status
                                            });*/
                                        })
                                        .finally(function() {
                                            //vm.busy = false;
                                        });
                                }

                                function getTask() {

                                    var req = {
                                        method: 'GET',
                                        url: $scope.properties.urlCurrentTask
                                    };

                                    return $http(req)
                                        .success(function(data, status) {
                                            console.log("SUCCSES")
                                            console.log(data);
                                            if (data.length > 0) {
                                                if (data[0].id === $scope.properties.taskId) {
                                                    if (cont === 100) {
                                                        location.reload();
                                                    } else {
                                                        getTask();
                                                        cont++;
                                                    }
                                                } else {
                                                    $scope.properties.currentTask = data;
                                                    topFunction();
                                                    $scope.properties.disabled = false;

                                                    $scope.properties.selectedIndex++;
                                                }
                                            } else {
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

                            swal("¡Error!", data.message, "error");
                        })
                        .finally(function() {

                        });
                }
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
        $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndex = $scope.properties.selectedIndex + 1;
        if ($scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos !== null) {
            if ($scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string !== undefined && $scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string !== null) {
                $scope.properties.Bachilleratopersistenceid = angular.copy($scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string);
                //$scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos.persistenceId_string = $scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string;
            }
        }

        if ($scope.properties.Bachilleratopersistenceid !== undefined && $scope.properties.Bachilleratopersistenceid !== null && $scope.properties.Bachilleratopersistenceid !== "") {
            if ($scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos === null) {
                $scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos = {};
                $scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos["persistenceId_string"] = $scope.properties.Bachilleratopersistenceid
            } else {
                $scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos.persistenceId_string = $scope.properties.Bachilleratopersistenceid;
            }
        }
        console.log($scope.properties.dataToSend);
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                /* $scope.properties.dataFromSuccess = data;
                 $scope.properties.responseStatusCode = status;
                 $scope.properties.dataFromError = undefined;
                 notifyParentFrame({
                     message: 'success',
                     status: status,
                     dataFromSuccess: data,
                     dataFromError: undefined,
                     responseStatusCode: status
                 });*/
                /*if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                closeModal($scope.properties.closeOnSuccess);*/
                getTask();
                //topFunction();
                //$scope.properties.selectedIndex++;
            })
            .error(function(data, status) {
                getTaskAgain();
            })
            .finally(function() {
                //vm.busy = false;
            });
    }

    function getTask() {

        var req = {
            method: 'GET',
            url: $scope.properties.urlCurrentTask
        };

        return $http(req)
            .success(function(data, status) {
                console.log("SUCCSES")
                console.log(data);
                if (data.length > 0) {
                    if (data[0].id === $scope.properties.taskId) {
                        if (cont === 100) {
                            location.reload();
                        } else {
                            getTask();
                            cont++;
                        }
                    } else {
                        $scope.properties.currentTask = data;
                        topFunction();
                        $scope.properties.disabled = false;

                        $scope.properties.selectedIndex++;
                    }
                } else {
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
        function getTaskAgain() {

        var req = {
            method: 'GET',
            url: $scope.properties.urlCurrentTask
        };

        return $http(req)
            .success(function(data, status) {
                doRequest('POST', '../API/bpm/userTask/' + data[0].id  + '/execution', null).then(function() {
                    localStorageService.delete($window.location.href);
                });
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