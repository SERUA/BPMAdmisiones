function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    $scope.action = function() {
        const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        var localS = localStorage;
        console.log("boton de siguiente");
        console.log($scope.properties.collageBoard);
        if ($scope.properties.selectedIndex === 0) {
            console.log("validar 0");
            if ($scope.properties.catSolicitudDeAdmision.catCampus.persistenceId_string === "") {
                swal("¡Campus!", "Debes seleccionar un campus donde cursará sus estudios", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                swal("¡Licenciatura!", "Debes seleccionar una licenciatura", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                if ($scope.properties.catSolicitudDeAdmision.catPropedeutico === null) {
                    swal("¡Examen propedéutico!", "Favor de seleccionar un examen propedéutico", "warning");
                } else {
                    if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                        swal("¡Período!", "Debes seleccionar un período donde cursará sus estudios", "warning");
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
                                topFunction()
                                $scope.properties.selectedIndex++;
                                ////$scope.assignTask();
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
                                    topFunction()
                                    $scope.properties.selectedIndex++;
                                    ////$scope.assignTask();
                                }
                            }
                        } else {
                            //$scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                            //$scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                topFunction()
                                $scope.properties.selectedIndex++;
                                ////$scope.assignTask();
                            }
                        }

                    } else {
                        swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás el examen", "warning");
                    }
                }
            } else if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                swal("¡Período!", "Debes seleccionar un período donde cursará sus estudios", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás tu examen", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar un estado donde realizarás el examen", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar una ciudad donde realizarás el examen", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            topFunction()
                            $scope.properties.selectedIndex++;
                            ////$scope.assignTask();
                        }
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
                            topFunction()
                            $scope.properties.selectedIndex++;
                            ////$scope.assignTask();
                        }
                    }
                } else {
                    //$scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                    //$scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        topFunction()
                        $scope.properties.selectedIndex++;
                        ////$scope.assignTask();
                    }
                }

            } else {
                swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás el examen", "warning");
            }
        } else if ($scope.properties.selectedIndex === 1) {

            console.log("validar 1");
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                topFunction()
                $scope.properties.selectedIndex++;
            }*/
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.catSolicitudDeAdmision.primerNombre === "") {
                swal("¡Nombre!", "Debes ingresar tu primer nombre", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.apellidoPaterno === "") {
                swal("¡Apellido paterno!", "Debes ingresar tu apellido paterno", "warning");
            }/* else if ($scope.properties.catSolicitudDeAdmision.apellidoMaterno === "") {
                swal("¡Apellido materno!", "Debes ingresar tu apellido materno", "warning");
            }*/ else if ($scope.properties.catSolicitudDeAdmision.correoElectronico === "") {
                swal("¡Correo electrónico!", "Debes ingresar tu correo electrónico", "warning");
            } else if (!re.test(String($scope.properties.catSolicitudDeAdmision.correoElectronico.trim()).toLowerCase())) {
                swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                swal("¡Licenciatura!", "Debes seleccionar una licenciatura", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                if ($scope.properties.catSolicitudDeAdmision.catPropedeutico === null) {
                    swal("Curso propedéutico!", "Por favor selecciona un curso propedéutico", "warning");
                } else {
                    if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás tu examen", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.avisoPrivacidad === false) {
                        swal("¡Aviso de privacidad!", "Debes aceptar el aviso de privacidad", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                        if ($scope.properties.lugarexamen === "En un estado") {
                            if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar un estado y una ciudad donde realizarás el examen", "warning");
                            } else {
                                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    topFunction()
                                    $scope.properties.selectedIndex++;
                                }*/


                                openModal($scope.properties.modalid);
                            }
                        } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                            if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar un país y una ciudad donde realizarás el examen", "warning");
                            } else {
                                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    topFunction()
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
                                topFunction()
                                $scope.properties.selectedIndex++;
                            }*/


                            openModal($scope.properties.modalid);
                        }
                    }
                }
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás tu examen", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.avisoPrivacidad === false) {
                swal("¡Aviso de privacidad!", "Debes aceptar el aviso de privacidad", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar un estado y una ciudad donde realizarás el examen", "warning");
                    } else {
                        /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            topFunction()
                            $scope.properties.selectedIndex++;
                        }*/


                        openModal($scope.properties.modalid);
                    }
                } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                    if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar un país y una ciudad donde realizarás el examen", "warning");
                    } else {
                        /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            topFunction()
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
                        topFunction()
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
                swal("¡Fecha de nacimiento!", "Debes agregar tu fecha de nacimiento", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catNacionalidad === null) {
                swal("¡Nacionalidad!", "Debes seleccionar tu nacionalidad", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catReligion === null) {
                swal("¡Religión!", "Debes seleccionar tu religión", "warning");
                $scope.faltacampo = true;
                /* } else if ($scope.properties.catSolicitudDeAdmision.curp === "" && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                     swal("¡CURP!", "Debes agregar tu CURP", "warning");
                     $scope.faltacampo = true;
                 } else if ($scope.properties.catSolicitudDeAdmision.curp.length < 18 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                     swal("¡CURP", "Tu CURP Debes tener 18 caracteres", "warning");
                     $scope.faltacampo = true;*/
            } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular === undefined) {
                swal("¡Teléfono celular!", "Debes agregar tu número celular", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular.length !== 10 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                swal("¡Teléfono celular", "Tu número de teléfono celular Debes ser de 10 dígitos", "warning");
                $scope.faltacampo = true;
            }
            /* else if($scope.properties.catSolicitudDeAdmision.telefonoCelular.length !== 14 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana"){
                            swal("¡Teléfono celular", "Tu número de teléfono celular Debes ser de 14 dígitos", "warning");
                            $scope.faltacampo = true;
                        }*/
            else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular === "") {
                swal("¡Teléfono celular!", "Debes agregar tu número celular", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catEstadoCivil === null) {
                swal("¡Estado civil!", "Debes seleccionar tu estado civil", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catSexo.persistenceId_string === "") {
                swal("¡Sexo!", "Debes seleccionar tu sexo", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus === null) {
                swal("¡Presento examen en otro campus!", "Debes seleccionar si ha realizado la solicitud en otro campus", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion === "Si") {
                if ($scope.properties.catSolicitudDeAdmision.catCampusPresentadoSolicitud.length === 0) {
                    swal("¡Campus presentado!", "Debes seleccionar el/los campus donde ha presentado tu solicitud", "warning");
                    $scope.faltacampo = true;
                }
            }
            if (!$scope.faltacampo) {
                if ($scope.properties.catSolicitudDeAdmision.calle === "") {
                    swal("¡Calle!", "Debes agregar la calle de tu domicilio", "warning");
                } else if (($scope.properties.catSolicitudDeAdmision.codigoPostal === "" || $scope.properties.catSolicitudDeAdmision.codigoPostal === null) && $scope.properties.catSolicitudDeAdmision.catPais.descripcion === "México") {
                    swal("¡Código postal!", "Debes agregar el código postal", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catPais === null) {
                    swal("¡País!", "Debes seleccionar el país", "warning");
                }
                /*else if ($scope.properties.catSolicitudDeAdmision.catEstado === null) {
                                   swal("¡Estado!", "Debes seleccionar el estado", "warning");
                               } */
                else if ($scope.properties.catSolicitudDeAdmision.ciudad === "") {
                    swal("¡Ciudad!", "Debes agregar una ciudad", "warning");
                }
                /*else if ($scope.properties.catSolicitudDeAdmision.calle2 === "") {
                                   swal("¡Entre calles!", "Debes agregar entre que calles se encuentra tu domicilio", "warning");
                               }*/
                else if ($scope.properties.catSolicitudDeAdmision.numExterior === "") {
                    swal("¡Número!", "Debes agregar el número de tu domicilio", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.colonia === "") {
                    swal("¡Colonia!", "Debes agregar la colonia", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.telefono === "") {
                    swal("¡Teléfono!", "Debes el agregar el teléfono", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos === null) {
                    swal("¡Preparatoria!", "Debes seleccionar una preparatoria, en caso de no encontrar la tuya selecciona la opción otro", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion === "Otro") {
                    if ($scope.properties.datosPreparatoria.nombreBachillerato === "") {
                        swal("¡Preparatoria!", "Debes agregar el nombre de tu preparatoria", "warning");
                    } else if ($scope.properties.datosPreparatoria.paisBachillerato === undefined || $scope.properties.datosPreparatoria.paisBachillerato === "") {
                        swal("¡País de tu preparatoria!", "Debes agregar el país de tu preparatoria", "warning");
                    } else if ($scope.properties.datosPreparatoria.estadoBachillerato === undefined || $scope.properties.datosPreparatoria.estadoBachillerato === "") {
                        swal("¡Estado de tu preparatoria!", "Debes agregar el estado de tu preparatoria", "warning");
                    } else if ($scope.properties.datosPreparatoria.ciudadBachillerato === undefined || $scope.properties.datosPreparatoria.ciudadBachillerato === "") {
                        swal("¡Ciudad de tu preparatoria!", "Debes agregar la ciudad de tu preparatoria", "warning");
                    } else if (isNaN($scope.properties.catSolicitudDeAdmision.promedioGeneral)) {
                        swal("¡Promedio!", "Debes agregar el promedio que obtuvo en tu preparatoria", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "" || $scope.properties.catSolicitudDeAdmision.promedioGeneral === null) {
                        swal("¡Promedio!", "Debes agregar el promedio que obtuvo en tu preparatoria", "warning");
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
                                if(JSON.stringify($scope.properties.kardex) === "{}"){
                                    $scope.fallo = true;
                                    swal("¡Constancia de estudios!", "Debes agregar tu constancia de calificaciones con promedio", "warning");
                                }else{
                                    if ($scope.properties.kardex !== undefined) {
                                        $scope.properties.kardexarchivo[0]["newValue"] = $scope.properties.kardex;
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
                                    swal("¡Constancia de estudios!", "Debes agregar tu constancia de calificaciones con promedio", "warning");
                                }
                            }

                            if (!$scope.fallo) {
                                if($scope.properties.catSolicitudDeAdmision.admisionAnahuac){
                                    if(JSON.stringify($scope.properties.cartaAA) === "{}"){
                                        $scope.fallo = true;
                                        swal("¡Carta Admisión Anáhuac!", "Agrega tu carta que valida tu Admisión Anáhuac. ", "warning");
                                    }else{
                                        if($scope.properties.cartaAAarchivo.length > 0){
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
                                                $scope.properties.cartaAAarchivo[0]["newValue"] = angular.copy(auxData);/* = ({
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
                                        }else{
                                            if ($scope.properties.cartaAA !== undefined && $scope.properties.cartaAA !== "") {
                                                $scope.properties.cartaAAarchivo = [];
                                                $scope.properties.cartaAAarchivo.push({
                                                    "newValue": angular.copy($scope.properties.cartaAA)
                                                });
                                            } else {
                                                $scope.fallo = true;
                                                swal("¡Carta Admisión Anáhuac!", "Agrega tu carta que valida tu Admisión Anáhuac. ", "warning");
                                            }
                                        }
                                    }  
                                }
                            } 

                            if (!$scope.fallo) {
                                if ($scope.properties.catSolicitudDeAdmision.tienePAA) {
                                    if ($scope.properties.catSolicitudDeAdmision.resultadoPAA === 0 || $scope.properties.catSolicitudDeAdmision.resultadoPAA === "" || $scope.properties.catSolicitudDeAdmision.resultadoPAA === null || $scope.properties.catSolicitudDeAdmision.resultadoPAA === undefined) {
                                        $scope.fallo = true;
                                        swal("¡Resultado (PAA) del Examen Collage Board!", "Tu puntuacion debe ser mayor a cero", "warning");
                                    } else if ($scope.properties.catSolicitudDeAdmision.resultadoPAA > 1) {
                                        if ($scope.properties.collageBoardarchivo.length > 0) {
                                            if(JSON.stringify($scope.properties.collageBoard) === "{}"){
                                                $scope.fallo = true;
                                                swal("¡Constancia Collage Board!", "Debes agregar la constancia del resultado PAA como viene emitida por el Collage Board. En caso de no haber realizado esta prueba marcarlo con el número 1", "warning");
                                            }else{
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
                                                swal("¡Constancia Collage Board!", "Debes agregar la constancia del resultado PAA como viene emitida por el Collage Board. En caso de no haber realizado esta prueba marcarlo con el número 1", "warning");
                                            }

                                        }
                                    }
                                    /*if ($scope.properties.catSolicitudDeAdmision.resultadoPAA === 0 || $scope.properties.catSolicitudDeAdmision.resultadoPAA === "" || $scope.properties.catSolicitudDeAdmision.resultadoPAA === null || $scope.properties.catSolicitudDeAdmision.resultadoPAA === undefined) {
                                        $scope.fallo = true;
                                        swal("¡Resultado (PAA) del Examen Collage Board!", "Tu puntuacion debe ser mayor a cero", "warning");
                                    } else if ($scope.properties.catSolicitudDeAdmision.resultadoPAA > 1) {
                                        if ($scope.properties.collageBoardarchivo.length > 0) {
                                            if ($scope.properties.collageBoard !== undefined) {
                                                $scope.properties.collageBoardarchivo[0]["newValue"] = $scope.properties.collageBoard;
                                            }
                                            
                                        } else {
                                            if ($scope.properties.collageBoard !== undefined && $scope.properties.collageBoard !== "") {
                                                $scope.properties.collageBoardarchivo = [];
                                                $scope.properties.collageBoardarchivo.push({
                                                    "newValue": angular.copy($scope.properties.collageBoard)
                                                });
                                            } else {
                                                $scope.fallo = true;
                                                swal("¡Constancia Collage Board!", "Debes agregar la constancia del resultado PAA como viene emitida por el Collage Board. En caso de no haber realizado esta prueba marcarlo con el número 1", "warning");
                                            }

                                        }
                                    }*/
                                } else {
                                    $scope.properties.collageBoardarchivo = [];
                                    if ($scope.properties.catSolicitudDeAdmision.tieneDescuento === true) {
                                        if ($scope.properties.descuentoarchivo.length > 0) {
                                            if ($scope.properties.descuento !== undefined) {
                                                $scope.properties.descuentoarchivo[0]["newValue"] = $scope.properties.descuento;
                                            }
                                            $scope.properties.pasoInformacionPersonal = true;
                                            topFunction()
                                            $scope.properties.selectedIndex++;
                                            //$scope.assignTask();
                                            $scope.fallo = true;
                                        } else {
                                            if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                                $scope.properties.descuentoarchivo = [];
                                                $scope.properties.descuentoarchivo.push({
                                                    "newValue": angular.copy($scope.properties.descuento)
                                                });
                                            } else {
                                                $scope.fallo = true;
                                                swal("¡Documento de descuento!", "Debes agregar el documento que acredita tu descuento", "warning");
                                            }
                                        }
                                    } else {
                                        $scope.properties.descuentoarchivo = [];
                                        if ($scope.properties.idExtranjero !== undefined) {
                                            $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                        }
                                        $scope.properties.pasoInformacionPersonal = true;
                                        topFunction()
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                        $scope.fallo = true;
                                    }
                                }
                            }
                            if (!$scope.fallo) {
                                if ($scope.properties.catSolicitudDeAdmision.tieneDescuento === true) {
                                    if ($scope.properties.descuentoarchivo.length > 0) {
                                        if ($scope.properties.descuento !== undefined) {
                                            $scope.properties.descuentoarchivo[0]["newValue"] = $scope.properties.descuento;
                                        }
                                        if ($scope.properties.idExtranjero !== undefined) {
                                            $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                        }
                                        $scope.properties.pasoInformacionPersonal = true;
                                        //$scope.assignTask();
                                        topFunction()
                                        $scope.properties.selectedIndex++;
                                    } else {
                                        if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                            $scope.properties.descuentoarchivo.push({
                                                "newValue": angular.copy($scope.properties.descuento)
                                            });
                                            if ($scope.properties.idExtranjero !== undefined) {
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            }
                                            $scope.properties.pasoInformacionPersonal = true;
                                            //$scope.assignTask();
                                            topFunction()
                                            $scope.properties.selectedIndex++;
                                        } else {
                                            $scope.fallo = true;
                                            swal("¡Documento de descuento!", "Debes agregar el documento que acredita tu descuento", "warning");
                                        }
                                    }
                                } else {
                                    $scope.properties.descuentoarchivo = [];
                                    if ($scope.properties.idExtranjero !== undefined) {
                                        $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                    }
                                    $scope.properties.pasoInformacionPersonal = true;
                                    //$scope.assignTask();
                                    topFunction()
                                    $scope.properties.selectedIndex++;
                                }
                            }

                        }
                    }
                } else if (isNaN($scope.properties.catSolicitudDeAdmision.promedioGeneral)) {
                    swal("¡Promedio!", "Debes agregar el promedio que obtuvo en tu preparatoria", "warning");
                }  else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "" || $scope.properties.catSolicitudDeAdmision.promedioGeneral === null) {
                    swal("¡Promedio!", "Debes agregar el promedio que obtuvo en tu preparatoria", "warning");
                } else {
                    debugger;
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.fallo = false;
                        if ($scope.properties.kardexarchivo.length > 0) {
                            if(JSON.stringify($scope.properties.kardex) === "{}"){
                                $scope.fallo = true;
                                swal("¡Constancia de estudios!", "Debes agregar tu constancia de calificaciones con promedio", "warning");
                            }else{
                                if ($scope.properties.kardex !== undefined) {
                                    $scope.properties.kardexarchivo[0]["newValue"] = $scope.properties.kardex;
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
                                swal("¡Constancia de estudios!", "Debes agregar tu constancia de calificaciones con promedio", "warning");
                            }
                        }
                        if (!$scope.fallo) {
                            if($scope.properties.catSolicitudDeAdmision.admisionAnahuac){
                                if(JSON.stringify($scope.properties.cartaAA) === "{}"){
                                    $scope.fallo = true;
                                    swal("¡Carta Admisión Anáhuac!", "Agrega tu carta que valida tu Admisión Anáhuac. ", "warning");
                                }else{
                                    if($scope.properties.cartaAAarchivo.length > 0){
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
                                            $scope.properties.cartaAAarchivo[0]["newValue"] = angular.copy(auxData);/* = ({
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
                                    }else{
                                        if ($scope.properties.cartaAA !== undefined && $scope.properties.cartaAA !== "") {
                                            $scope.properties.cartaAAarchivo = [];
                                            $scope.properties.cartaAAarchivo.push({
                                                "newValue": angular.copy($scope.properties.cartaAA)
                                            });
                                        } else {
                                            $scope.fallo = true;
                                            swal("¡Carta Admisión Anáhuac!", "Agrega tu carta que valida tu Admisión Anáhuac. ", "warning");
                                        }
                                    }
                                }  
                            }
                        } 

                        if (!$scope.fallo) {
                            if ($scope.properties.catSolicitudDeAdmision.tienePAA) {
                                if ($scope.properties.catSolicitudDeAdmision.resultadoPAA === 0 || $scope.properties.catSolicitudDeAdmision.resultadoPAA === "" || $scope.properties.catSolicitudDeAdmision.resultadoPAA === null || $scope.properties.catSolicitudDeAdmision.resultadoPAA === undefined) {
                                    $scope.fallo = true;
                                    swal("¡Resultado (PAA) del Examen Collage Board!", "Tu puntuacion debe ser mayor a cero ", "warning");
                                } else if ($scope.properties.catSolicitudDeAdmision.resultadoPAA > 1) {
                                    if(JSON.stringify($scope.properties.collageBoard) === "{}"){
                                        $scope.fallo = true;
                                        swal("¡Constancia Collage Board!", "Debes agregar la constancia del resultado PAA como viene emitida por el Collage Board. ", "warning");
                                    }else{
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
                                                $scope.properties.collageBoardarchivo[0]["newValue"] = angular.copy(auxData);/* = ({
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
                                                swal("¡Constancia Collage Board!", "Debes agregar la constancia del resultado PAA como viene emitida por el Collage Board. ", "warning");
                                            }

                                        }
                                    }
                                }
                            } else {
                                $scope.properties.collageBoardarchivo = [];
                                if ($scope.properties.catSolicitudDeAdmision.tieneDescuento === true) {
                                    if ($scope.properties.descuentoarchivo.length > 0) {
                                        if(JSON.stringify($scope.properties.descuento) === "{}"){
                                            $scope.fallo = true;
                                            swal("¡Documento de descuento!", "Debes agregar el documento que acredita tu descuento", "warning");
                                        }else{
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
                                                $scope.properties.descuentoarchivo[0]["newValue"] = angular.copy(auxData);/* = ({
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
                                            swal("¡Documento de descuento!", "Debes agregar el documento que acredita tu descuento", "warning");
                                        }
                                    }
                                } else {
                                    $scope.properties.descuentoarchivo = [];
                                    if ($scope.properties.idExtranjero !== undefined) {
                                        $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                    }
                                    $scope.properties.pasoInformacionPersonal = true;
                                    topFunction()
                                    $scope.properties.selectedIndex++;
                                    //$scope.assignTask();
                                    $scope.fallo = true;
                                }
                            }
                        }

                        if (!$scope.fallo) {
                            if ($scope.properties.catSolicitudDeAdmision.tieneDescuento === true) {
                                if ($scope.properties.descuentoarchivo.length > 0) {
                                    if ($scope.properties.descuento !== undefined) {
                                        $scope.properties.descuentoarchivo[0]["newValue"] = $scope.properties.descuento;
                                    }
                                    if ($scope.properties.idExtranjero !== undefined) {
                                        $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                    }
                                    $scope.properties.pasoInformacionPersonal = true;
                                    topFunction()
                                    $scope.properties.selectedIndex++;
                                } else {
                                    if ($scope.properties.descuento !== undefined && $scope.properties.descuento !== "") {
                                        $scope.properties.descuentoarchivo.push({
                                            "newValue": angular.copy($scope.properties.descuento)
                                        });
                                        if ($scope.properties.idExtranjero !== undefined) {
                                            $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                        }
                                        $scope.properties.pasoInformacionPersonal = true;
                                        topFunction()
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                    } else {
                                        $scope.fallo = true;
                                        swal("¡Documento de descuento!", "Debes agregar el documento que acredita tu descuento", "warning");
                                    }
                                }
                            } else {
                                $scope.properties.descuentoarchivo = [];
                                if ($scope.properties.idExtranjero !== undefined) {
                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                }
                                $scope.properties.pasoInformacionPersonal = true;
                                topFunction()
                                $scope.properties.selectedIndex++;
                                //$scope.assignTask();
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
                swal("¡Tutor!", "Debes agregar al menos un tutor", "warning");
            } else if ($scope.properties.padreInput.desconozcoDatosPadres) {
                //validar madre
                if ($scope.properties.madreInput.desconozcoDatosPadres) {
                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                            $scope.properties.pasoInformacionFamiliar = true;
                            topFunction()
                            $scope.properties.selectedIndex++;
                            //$scope.assignTask();
                        }
                    }
                } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                    swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                    swal("¡Nombre de la madre!", "Debes agregar nombre de la madre", "warning");
                } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                    swal("¡Apellidos de la madre!", "Debes agregar los apellidos de la madre", "warning");
                } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                    swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                        swal("¡Egreso Anáhuac!", "Debes seleccionar si tu madre egresó de la universidad Anáhuac", "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                            swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó tu madre", "warning");
                        } else {
                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                swal("¡Trabaja!", "Debes seleccionar si tu madre trabaja", "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                    swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                    swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                    swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                }
                                /*else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                   swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                               } */
                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                } else {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            topFunction()
                                            $scope.properties.selectedIndex++;
                                            //$scope.assignTask();
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                    swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                }
                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                }*/
                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                } else {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            topFunction()
                                            $scope.properties.selectedIndex++;
                                            //$scope.assignTask();
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                            swal("¡Trabaja!", "Debes seleccionar si tu madre trabaja", "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                            }
                            /*else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                           swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                       } */
                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                            } else {
                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        topFunction()
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                            }
                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                        } */
                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                            } else {
                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        topFunction()
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
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
                        topFunction()
                        $scope.properties.selectedIndex++;
                        //$scope.assignTask();
                    }
                }




            } else if ($scope.properties.padreInput.catTitulo === 0 || $scope.properties.padreInput.catTitulo === null) {
                swal("¡Título!", "Debes seleccionar el título para identificar al padre", "warning");
            } else if ($scope.properties.padreInput.nombre === "" || $scope.properties.padreInput.nombre === undefined) {
                swal("¡Nombre del padre!", "Debes agregar nombre del padre", "warning");
            } else if ($scope.properties.padreInput.apellidos === "" || $scope.properties.padreInput.apellidos === undefined) {
                swal("¡Apellidos del padre!", "Debes agregar los apellidos del padre", "warning");
            } else if ($scope.properties.padreInput.vive === 0 || $scope.properties.padreInput.vive === null) {
                swal("¡Padre vive!", "Debes seleccionar si el padre vive", "warning");
            } else if ($scope.properties.datosPadres.padrevive) {
                if ($scope.properties.padreInput.catEgresoAnahuac === 0 || $scope.properties.padreInput.catEgresoAnahuac === null) {
                    swal("¡Egreso Anáhuac!", "Debes seleccionar si tu padre egreso de la universidad Anáhuac", "warning");
                } else if ($scope.properties.datosPadres.padreegresoanahuac) {
                    if ($scope.properties.padreInput.catCampusEgreso === 0 || $scope.properties.padreInput.catCampusEgreso === null) {
                        swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó tu padre", "warning");
                    } else {
                        if ($scope.properties.padreInput.catTrabaja === 0 || $scope.properties.padreInput.catTrabaja === null) {
                            swal("¡Trabaja!", "Debes seleccionar si tu padre trabaja", "warning");
                        } else if ($scope.properties.datosPadres.padretrabaja) {
                            if ($scope.properties.padreInput.empresaTrabaja === "" || $scope.properties.padreInput.empresaTrabaja === undefined) {
                                swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu padre trabaja", "warning");
                            } else if ($scope.properties.padreInput.puesto === "" || $scope.properties.padreInput.puesto === undefined) {
                                swal("¡Puesto!", "Debes agregar el puesto de trabajo del padre", "warning");
                            } else if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico del padre", "warning");
                            } else if (!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())) {
                                swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                            } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad del padre", "warning");
                            } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio del padre", "warning");
                            }
                            /* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                                        } */
                            else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio del padre", "warning");
                            } else if (($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) && $scope.properties.padreInput.catPais.descripcion === "México" ) {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio del padre", "warning");
                            } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio del padre", "warning");
                            } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la calle del domicilio del padre", "warning");
                            } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio del padre", "warning");
                            } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono del padre", "warning");
                            } else {
                                ///Validar madre 1
                                if ($scope.properties.madreInput.desconozcoDatosPadres) {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            topFunction()
                                            $scope.properties.selectedIndex++;
                                            //$scope.assignTask();
                                        }
                                    }
                                } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                                    swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                                } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                                    swal("¡Nombre de la madre!", "Debes agregar nombre de la madre", "warning");
                                } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                                    swal("¡Apellidos de la madre!", "Debes agregar los apellidos de la madre", "warning");
                                } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                                    swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                        swal("¡Egreso Anáhuac!", "Debes seleccionar si tu madre egresó de la universidad Anáhuac", "warning");
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                            swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó tu madre", "warning");
                                        } else {
                                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                swal("¡Trabaja!", "Debes seleccionar si tu madre trabaja", "warning");
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                    swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                    swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                                }
                                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                } */
                                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            topFunction()
                                                            $scope.properties.selectedIndex++;
                                                            //$scope.assignTask();
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                                }
                                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                }*/
                                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            topFunction()
                                                            $scope.properties.selectedIndex++;
                                                            //$scope.assignTask();
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                            swal("¡Trabaja!", "Debes seleccionar si tu madre trabaja", "warning");
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                            }
                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                        }*/
                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        topFunction()
                                                        $scope.properties.selectedIndex++;
                                                        //$scope.assignTask();
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                            }
                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                        } */
                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        topFunction()
                                                        $scope.properties.selectedIndex++;
                                                        //$scope.assignTask();
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
                                        topFunction()
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico del padre", "warning");
                            } else if (!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())) {
                                swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                            } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad del padre", "warning");
                            } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio del padre", "warning");
                            }
                            /* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                                        }*/
                            else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio del padre", "warning");
                            } else if (($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) && $scope.properties.padreInput.catPais.descripcion === "México" ) {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio del padre", "warning");
                            } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio del padre", "warning");
                            } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la calle del domicilio del padre", "warning");
                            } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio del padre", "warning");
                            } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono del padre", "warning");
                            } else {
                                //VALIDAR MADRE 2
                                if ($scope.properties.madreInput.desconozcoDatosPadres) {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            topFunction()
                                            $scope.properties.selectedIndex++;
                                            //$scope.assignTask();
                                        }
                                    }
                                } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                                    swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                                } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                                    swal("¡Nombre de la madre!", "Debes agregar nombre de la madre", "warning");
                                } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                                    swal("¡Apellidos de la madre!", "Debes agregar los apellidos de la madre", "warning");
                                } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                                    swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                        swal("¡Egreso Anáhuac!", "Debes seleccionar si tu madre egresó de la universidad Anáhuac", "warning");
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                            swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó tu madre", "warning");
                                        } else {
                                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                swal("¡Trabaja!", "Debes seleccionar si tu madre trabaja", "warning");
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                    swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                    swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                                }
                                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                } */
                                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            topFunction()
                                                            $scope.properties.selectedIndex++;
                                                            //$scope.assignTask();
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                                } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                                }
                                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                } */
                                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            topFunction()
                                                            $scope.properties.selectedIndex++;
                                                            //$scope.assignTask();
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                            swal("¡Trabaja!", "Debes seleccionar si tu madre trabaja", "warning");
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                            }
                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                        }*/
                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        topFunction()
                                                        $scope.properties.selectedIndex++;
                                                        //$scope.assignTask();
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                            } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                            }
                                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                        }*/
                                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        topFunction()
                                                        $scope.properties.selectedIndex++;
                                                        //$scope.assignTask();
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
                                        topFunction()
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                    }
                                }
                            }
                        }
                    }
                } else if ($scope.properties.padreInput.catTrabaja === 0 || $scope.properties.padreInput.catTrabaja === null) {
                    swal("¡Trabaja!", "Debes seleccionar si tu padre trabaja", "warning");
                } else if ($scope.properties.datosPadres.padretrabaja) {
                    if ($scope.properties.padreInput.empresaTrabaja === "" || $scope.properties.padreInput.empresaTrabaja === undefined) {
                        swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu padre trabaja", "warning");
                    } else if ($scope.properties.padreInput.puesto === "" || $scope.properties.padreInput.puesto === undefined) {
                        swal("¡Puesto!", "Debes agregar el puesto de trabajo del padre", "warning");
                    } else if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                        swal("¡Correo electrónico!", "Debes agregar el correo electrónico del padre", "warning");
                    } else if (!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())) {
                        swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                    } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                        swal("¡Escolaridad!", "Debes seleccionar la escolaridad del padre", "warning");
                    } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                        swal("¡País!", "Debes agregar el país del domicilio del padre", "warning");
                    }
                    /* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                            swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                        }*/
                    else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                        swal("¡Calle!", "Debes agregar la calle del domicilio del padre", "warning");
                    } else if (($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) && $scope.properties.padreInput.catPais.descripcion === "México" ) {
                        swal("¡Código postal!", "Debes agregar el código postal del domicilio del padre", "warning");
                    } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                        swal("¡Número exterior!", "Debes agregar el número exterior del domicilio del padre", "warning");
                    } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                        swal("¡Ciudad!", "Debes agregar la calle del domicilio del padre", "warning");
                    } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                        swal("¡Colonia!", "Debes agregar la colonia del domicilio del padre", "warning");
                    } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                        swal("¡Teléfono!", "Debes agregar el teléfono del padre", "warning");
                    } else {
                        ///Validar madre  3
                        if ($scope.properties.madreInput.desconozcoDatosPadres) {
                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                    $scope.properties.pasoInformacionFamiliar = true;
                                    topFunction()
                                    $scope.properties.selectedIndex++;
                                    //$scope.assignTask();
                                }
                            }
                        } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                            swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                        } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                            swal("¡Nombre de la madre!", "Debes agregar nombre de la madre", "warning");
                        } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                            swal("¡Apellidos de la madre!", "Debes agregar los apellidos de la madre", "warning");
                        } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                            swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                swal("¡Egreso Anáhuac!", "Debes seleccionar si tu madre egresó de la universidad Anáhuac", "warning");
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                    swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó tu madre", "warning");
                                } else {
                                    if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                        swal("¡Trabaja!", "Debes seleccionar si tu madre trabaja", "warning");
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                            swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                                        } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                            swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                        } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                            swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                            swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                            swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                            swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                        }
                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                } */
                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                            swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                            swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                            swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                            swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                            swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                            swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    topFunction()
                                                    $scope.properties.selectedIndex++;
                                                    //$scope.assignTask();
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                            swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                            swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                            swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                            swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                        }
                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                }*/
                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                            swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                            swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                            swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                            swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                            swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                            swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    topFunction()
                                                    $scope.properties.selectedIndex++;
                                                    //$scope.assignTask();
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                    swal("¡Trabaja!", "Debes seleccionar si tu madre trabaja", "warning");
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                        swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                                    } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                        swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                    } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                        swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                    } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                        swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                        swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                        swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                    }
                                    /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                        }*/
                                    else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                        swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                        swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                        swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                        swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                        swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                        swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                            swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                $scope.properties.pasoInformacionFamiliar = true;
                                                topFunction()
                                                $scope.properties.selectedIndex++;
                                                //$scope.assignTask();
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                        swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                    } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                        swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                        swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                        swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                    }
                                    /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                        }*/
                                    else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                        swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                        swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                        swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                        swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                        swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                        swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                            swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                $scope.properties.pasoInformacionFamiliar = true;
                                                topFunction()
                                                $scope.properties.selectedIndex++;
                                                //$scope.assignTask();
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
                                topFunction()
                                $scope.properties.selectedIndex++;
                                //$scope.assignTask();
                            }
                        }
                    }
                } else {
                    if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                        swal("¡Correo electrónico!", "Debes agregar el correo electrónico del padre", "warning");
                    } else if (!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())) {
                        swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                    } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                        swal("¡Escolaridad!", "Debes seleccionar la escolaridad del padre", "warning");
                    } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                        swal("¡País!", "Debes agregar el país del domicilio del padre", "warning");
                    }
                    /* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                            swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                        }*/
                    else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                        swal("¡Calle!", "Debes agregar la calle del domicilio del padre", "warning");
                    } else if (($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) && $scope.properties.padreInput.catPais.descripcion === "México" ) {
                        swal("¡Código postal!", "Debes agregar el código postal del domicilio del padre", "warning");
                    } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                        swal("¡Número exterior!", "Debes agregar el número exterior del domicilio del padre", "warning");
                    } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                        swal("¡Ciudad!", "Debes agregar la calle del domicilio del padre", "warning");
                    } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                        swal("¡Colonia!", "Debes agregar la colonia del domicilio del padre", "warning");
                    } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                        swal("¡Teléfono!", "Debes agregar el teléfono del padre", "warning");
                    } else {
                        //VALIDAR MADRE 4
                        if ($scope.properties.madreInput.desconozcoDatosPadres) {
                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                    $scope.properties.pasoInformacionFamiliar = true;
                                    topFunction()
                                    $scope.properties.selectedIndex++;
                                    //$scope.assignTask();
                                }
                            }
                        } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                            swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                        } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                            swal("¡Nombre de la madre!", "Debes agregar nombre de la madre", "warning");
                        } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                            swal("¡Apellidos de la madre!", "Debes agregar los apellidos de la madre", "warning");
                        } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                            swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                swal("¡Egreso Anáhuac!", "Debes seleccionar si tu madre egresó de la universidad Anáhuac", "warning");
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                    swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó tu madre", "warning");
                                } else {
                                    if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                        swal("¡Trabaja!", "Debes seleccionar si tu madre trabaja", "warning");
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                            swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                                        } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                            swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                        } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                            swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                            swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                            swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                            swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                        }
                                        /*else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                   swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                               } */
                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                            swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                            swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                            swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                            swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                            swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                            swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    topFunction()
                                                    $scope.properties.selectedIndex++;
                                                    //$scope.assignTask();
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                            swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                        } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                            swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                            swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                            swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                        }
                                        /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                }*/
                                        else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                            swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                            swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                            swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                            swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                            swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                            swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    topFunction()
                                                    $scope.properties.selectedIndex++;
                                                    //$scope.assignTask();
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                    swal("¡Trabaja!", "Debes seleccionar si tu madre trabaja", "warning");
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                        swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                                    } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                        swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                    } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                        swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                    } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                        swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                        swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                        swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                    }
                                    /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                        }*/
                                    else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                        swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                        swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                        swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                        swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                        swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                        swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                            swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                $scope.properties.pasoInformacionFamiliar = true;
                                                topFunction()
                                                $scope.properties.selectedIndex++;
                                                //$scope.assignTask();
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                        swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                    } else if (!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())) {
                                        swal("¡Correo electrónico!", "Tu correo electrónico no es válido", "warning");
                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                        swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                        swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                    }
                                    /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                        }*/
                                    else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                        swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                        swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                        swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                        swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                        swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                        swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                            swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                $scope.properties.pasoInformacionFamiliar = true;
                                                topFunction()
                                                $scope.properties.selectedIndex++;
                                                //$scope.assignTask();
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
                                topFunction()
                                $scope.properties.selectedIndex++;
                                //$scope.assignTask();
                            }
                        }
                    }
                }

            } else {
                console.log("esta validando el la linea 373")
                if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                    swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                    swal("¡Nombre de la madre!", "Debes agregar nombre de la madre", "warning");
                } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                    swal("¡Apellidos de la madre!", "Debes agregar los apellidos de la madre", "warning");
                } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                    swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                        swal("¡Egreso Anáhuac!", "Debes seleccionar si tu madre egresó de la universidad Anáhuac", "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                            swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó tu madre", "warning");
                        } else {
                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                swal("¡Trabaja!", "Debes seleccionar si tu madre trabaja", "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                    swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                    swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                }
                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                }*/
                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                } else {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            topFunction()
                                            $scope.properties.selectedIndex++;
                                            //$scope.assignTask();
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                }
                                /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                }*/
                                else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                } else {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            topFunction()
                                            $scope.properties.selectedIndex++;
                                            //$scope.assignTask();
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                            swal("¡Trabaja!", "Debes seleccionar si tu madre trabaja", "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                            }
                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                        }*/
                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                            } else {
                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        topFunction()
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                            }
                            /* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                        }*/
                            else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if (($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) && $scope.properties.madreInput.catPais.descripcion === "México" ) {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                            } else {
                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        topFunction()
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                            $scope.properties.pasoInformacionFamiliar = true;
                            topFunction()
                            $scope.properties.selectedIndex++;
                            //$scope.assignTask();
                        }
                    }
                }
            }
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                topFunction()
                $scope.properties.selectedIndex++;
            }*/



        } else if ($scope.properties.selectedIndex === 4) {
            console.log("validar 4");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {

                topFunction()
                $scope.properties.selectedIndex++;
                //$scope.assignTask();
            }
        } else if ($scope.properties.selectedIndex === 5) {
            console.log("validar 4");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {

                topFunction()
                $scope.properties.selectedIndex++;
                //$scope.assignTask();
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
                swal("Error", data.message, "error");
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
        if($scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string !== undefined){
            $scope.properties.Bachilleratopersistenceid = angular.copy($scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string);
            $scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos.persistenceId_string = $scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string;
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
                topFunction();
                topFunction()
                $scope.properties.selectedIndex++;
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

    function getTask(){
        setTimeout(function(){ 
            var req = {
            method: 'GET',
            url: $scope.properties.urlCurrentTask
        };

        return $http(req)
            .success(function(data, status) {
                console.log("TuCCSES")
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

    function topFunction() {
        document.body.scrollTop = 0;
        document.documentElement.scrollTop = 0;
      }
}
