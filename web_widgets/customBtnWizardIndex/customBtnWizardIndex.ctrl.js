function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    $scope.action = function() {
        const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        var localS = localStorage;
        console.log("boton de siguiente");
        console.log($scope.properties.collageBoard);
        if ($scope.properties.selectedIndex === 0) {
            console.log("validar 0");
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }*/
            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catCampus.persistenceId_string === "") {
                swal("¡Campus!", "Debes seleccionar un campus donde cursarás tus estudios", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catGestionEscolar === null) {
                swal("¡Licenciatura!", "Debes seleccionar una licenciatura", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catGestionEscolar.propedeutico) {
                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPropedeutico === null) {
                    swal("¡Examen propedéutico!", "Favor de seleccionar un examen propedéutico", "warning");
                } else {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPeriodo === null) {
                        swal("¡Período!", "Debes seleccionar un período donde cursarás tus estudios", "warning");
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás tu examen", "warning");
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen.persistenceId_string !== "") {
                        if ($scope.properties.lugarexamen === "En un estado") {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoExamen === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar un estado donde realizarás el examen", "warning");
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar una ciudad donde realizarás el examen", "warning");
                            } else if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.fotopasaporte = undefined;
                                $scope.properties.actanacimiento = undefined;
                                $scope.properties.kardex = undefined;
                                $scope.properties.descuento = undefined;
                                $scope.properties.collageBoard = undefined;
                                $scope.properties.cartaAA = undefined;
                                
                                $scope.properties.selectedIndex--;
                                console.log(1)
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {

                                $scope.properties.formInput.catSolicitudDeAdmisionInput.catPaisExamen = null;
                                $scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamenPais = null;
                                $scope.properties.selectedIndex++;
                            }
                        } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPaisExamen === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar un país donde realizarás el examen", "warning");
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamenPais === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar una ciudad donde realizarás el examen", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.fotopasaporte = undefined;
                                    $scope.properties.actanacimiento = undefined;
                                    $scope.properties.kardex = undefined;
                                    $scope.properties.descuento = undefined;
                                    $scope.properties.collageBoard = undefined;
                                    $scope.properties.cartaAA = undefined;
                                    $scope.properties.selectedIndex--;
                                    console.log(2)
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else {
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.catPaisExamen = null;
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoExamen = null;
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.fotopasaporte = undefined;
                                $scope.properties.actanacimiento = undefined;
                                $scope.properties.kardex = undefined;
                                $scope.properties.descuento = undefined;
                                $scope.properties.collageBoard = undefined;
                                $scope.properties.cartaAA = undefined;
                                $scope.properties.selectedIndex--;
                                console.log(3)
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {

                                $scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoExamen = null;
                                $scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen = null;
                                $scope.properties.selectedIndex++;
                            }
                        }

                    } else {
                        swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás el examen", "warning");
                    }
                }
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPeriodo === null) {
                swal("¡Período!", "Debes seleccionar un período donde cursarás tus estudios", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen === null) {
                swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás tu examen", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoExamen === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar un estado donde realizarás el examen", "warning");
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar una ciudad donde realizarás el examen", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.fotopasaporte = undefined;
                            $scope.properties.actanacimiento = undefined;
                            $scope.properties.kardex = undefined;
                            $scope.properties.descuento = undefined;
                            $scope.properties.collageBoard = undefined;
                            $scope.properties.cartaAA = undefined;
                            $scope.properties.selectedIndex--;
                            console.log(4)
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {

                            $scope.properties.formInput.catSolicitudDeAdmisionInput.catPaisExamen = null;
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamenPais = null;
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPaisExamen === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar un país donde realizarás el examen", "warning");
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamenPais === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar una ciudad donde realizarás el examen", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.fotopasaporte = undefined;
                            $scope.properties.actanacimiento = undefined;
                            $scope.properties.kardex = undefined;
                            $scope.properties.descuento = undefined;
                            $scope.properties.collageBoard = undefined;
                            $scope.properties.cartaAA = undefined;
                            $scope.properties.selectedIndex--;
                            console.log(5)
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoExamen = null;
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen = null;

                            $scope.properties.selectedIndex++;
                        }
                    }
                } else {
                    $scope.properties.formInput.catSolicitudDeAdmisionInput.catPaisExamen = null;
                    $scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoExamen = null;
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.fotopasaporte = undefined;
                        $scope.properties.actanacimiento = undefined;
                        $scope.properties.kardex = undefined;
                        $scope.properties.descuento = undefined;
                        $scope.properties.collageBoard = undefined;
                        $scope.properties.cartaAA = undefined;
                        $scope.properties.selectedIndex--;
                        console.log(6)
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                        $scope.properties.selectedIndex++;
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
                $scope.properties.selectedIndex++;
            }*/
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.fotopasaporte = undefined;
                $scope.properties.actanacimiento = undefined;
                $scope.properties.kardex = undefined;
                $scope.properties.descuento = undefined;
                $scope.properties.collageBoard = undefined;
                $scope.properties.cartaAA = undefined;
                $scope.properties.selectedIndex--;
                console.log(7)
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.primerNombre === "") {
                swal("¡Nombre!", "Debes ingresar su primer nombre", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.apellidoPaterno === "") {
                swal("¡Apellido paterno!", "Debes ingresar su apellido paterno", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.apellidoMaterno === "") {
                swal("¡Apellido materno!", "Debes ingresar su apellido materno", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.correoElectronico === "") {
                swal("¡Correo electrónico!", "Debes ingresar su correo electrónico", "warning");
            } else if (!re.test(String($scope.properties.formInput.catSolicitudDeAdmisionInput.correoElectronico.trim()).toLowerCase())) {
                swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catGestionEscolar === null) {
                swal("¡Licenciatura!", "Debes seleccionar una licenciatura", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catGestionEscolar.propedeutico) {
                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPropedeutico === null) {
                    swal("¡Examen propedéutico!", "Favor de seleccionar un examen propedéutico", "warning");
                } else {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás tu examen", "warning");
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.avisoPrivacidad === false) {
                        swal("¡Aviso de privacidad!", "Debes aceptar el aviso de privacidad", "warning");
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen.persistenceId_string !== "") {
                        if ($scope.properties.lugarexamen === "En un estado") {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar un estado y una ciudad donde realizarás el examen", "warning");
                            } else {
                                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.selectedIndex++;
                                }*/


                                openModal($scope.properties.modalid);
                            }
                        } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamenPais === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar un país y una ciudad donde realizarás el examen", "warning");
                            } else {
                                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.selectedIndex++;
                                }*/


                                openModal($scope.properties.modalid);
                            }
                        } else {
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.catPaisExamen = null;
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoExamen = null;
                            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                $scope.properties.selectedIndex++;
                            }*/


                            openModal($scope.properties.modalid);
                        }
                    }
                }
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen === null) {
                swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás tu examen", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.avisoPrivacidad === false) {
                swal("¡Aviso de privacidad!", "Debes aceptar el aviso de privacidad", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar un estado y una ciudad donde realizarás el examen", "warning");
                    } else {
                        /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }*/


                        openModal($scope.properties.modalid);
                    }
                } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamenPais === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar un país y una ciudad donde realizarás el examen", "warning");
                    } else {
                        /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }*/


                        openModal($scope.properties.modalid);
                    }
                } else {
                    $scope.properties.formInput.catSolicitudDeAdmisionInput.catPaisExamen = null;
                    $scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoExamen = null;
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
                $scope.properties.fotopasaporte = undefined;
                $scope.properties.actanacimiento = undefined;
                $scope.properties.kardex = undefined;
                $scope.properties.descuento = undefined;
                $scope.properties.collageBoard = undefined;
                $scope.properties.cartaAA = undefined;
                $scope.properties.selectedIndex--;
                console.log(8)
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.fechaNacimiento === undefined) {
                swal("¡Fecha de nacimiento!", "Debes agregar su fecha de nacimiento", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catNacionalidad === null) {
                swal("¡Nacionalidad!", "Debes seleccionar su nacionalidad", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catReligion === null) {
                swal("¡Religión!", "Debes seleccionar su religión", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.curp === "" && $scope.properties.formInput.catSolicitudDeAdmisionInput.catNacionalidad.descripcion === "Mexicana") {
                swal("¡CURP!", "Debes agregar su CURP", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.curp.length < 18 && $scope.properties.formInput.catSolicitudDeAdmisionInput.catNacionalidad.descripcion === "Mexicana") {
                swal("¡CURP", "Su CURP debe tener 18 caracteres", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.telefonoCelular === undefined) {
                swal("¡Teléfono celular!", "Debes agregar su número celular", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.telefonoCelular.length !== 10 && $scope.properties.formInput.catSolicitudDeAdmisionInput.catNacionalidad.descripcion === "Mexicana") {
                swal("¡Teléfono celular", "Su número de teléfono celular debe ser de 10 dígitos", "warning");
                $scope.faltacampo = true;
            }
            /* else if($scope.properties.formInput.catSolicitudDeAdmisionInput.telefonoCelular.length !== 14 && $scope.properties.formInput.catSolicitudDeAdmisionInput.catNacionalidad.descripcion !== "Mexicana"){
                            swal("¡Teléfono celular", "Su número de teléfono celular debe ser de 14 dígitos", "warning");
                            $scope.faltacampo = true;
                        }*/
            else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.telefonoCelular === "") {
                swal("¡Teléfono celular!", "Debes agregar su número celular", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoCivil === null) {
                swal("¡Estado civil!", "Debes seleccionar su estado civil", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catSexo.persistenceId_string === "") {
                swal("¡Sexo!", "Debes seleccionar su sexo", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPresentasteEnOtroCampus === null) {
                swal("¡Presento examen en otro campus!", "Debes seleccionar si ha realizado la solicitud en otro campus", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPresentasteEnOtroCampus.descripcion === "Sí") {
                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catCampusPresentadoSolicitud.length === 0) {
                    swal("¡Campus presentado!", "Debes seleccionar el/los campus donde ha presentado su solicitud", "warning");
                    $scope.faltacampo = true;
                }
            }
            if (!$scope.faltacampo) {
                if ($scope.properties.fotopasaporte === undefined || JSON.stringify($scope.properties.fotopasaporte) == '{}') {
                    swal("¡Fotografía!", "Debes agregar una fotografía", "warning");
                } else if ($scope.properties.actanacimiento === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                    swal("¡Acta de nacimiento!", "Debes agregar su acta de nacimiento", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.calle === "") {
                    swal("¡Calle!", "Debes agregar la calle de su domicilio", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.codigoPostal === "") {
                    swal("¡Código postal!", "Debes agregar el código postal", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPais === null) {
                    swal("¡País!", "Debes seleccionar el país", "warning");
                }
                /*else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catEstado === null) {
                                   swal("¡Estado!", "Debes seleccionar el estado", "warning");
                               } */
                else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudad === "") {
                    swal("¡Ciudad!", "Debes agregar una ciudad", "warning");
                }
                /*else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.calle2 === "") {
                                   swal("¡Entre calles!", "Debes agregar entre que calles se encuentra su domicilio", "warning");
                               }*/
                else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.numExterior === "") {
                    swal("¡Número!", "Debes agregar el número de su domicilio", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.colonia === "") {
                    swal("¡Colonia!", "Debes agregar la colonia", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.telefono === "") {
                    swal("¡Teléfono!", "Debes el agregar el teléfono", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catBachilleratos === null) {
                    swal("¡Preparatoria!", "Debes seleccionar una preparatoria en caso de no encontrar la suya seleccionar la opción otro", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catBachilleratos.descripcion === "Otro") {
                    if ($scope.properties.datosPreparatoria.nombreBachillerato === "") {
                        swal("¡Preparatoria!", "Debes agregar el nombre de su preparatoria", "warning");
                    } else if ($scope.properties.datosPreparatoria.paisBachillerato === undefined) {
                        swal("¡País preparatoria!", "Debes agregar el país de su preparatoria", "warning");
                    } else if ($scope.properties.datosPreparatoria.estadoBachillerato === undefined) {
                        swal("¡Estado preparatoria!", "Debes agregar el estado de su preparatoria", "warning");
                    } else if ($scope.properties.datosPreparatoria.ciudadBachillerato === undefined) {
                        swal("¡Ciudad preparatoria!", "Debes agregar la ciudad de su preparatoria", "warning");
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.promedioGeneral === "") {
                        swal("¡Promedio!", "Debes agregar el promedio que obtuvo en su preparatoria", "warning");
                    } else if ($scope.properties.kardex === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                        swal("¡Contancia de calificaciones!", "Debes agregar la constancia de calificaciones de su preparatoria", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.fotopasaporte = undefined;
                            $scope.properties.actanacimiento = undefined;
                            $scope.properties.kardex = undefined;
                            $scope.properties.descuento = undefined;
                            $scope.properties.collageBoard = undefined;
                            $scope.properties.cartaAA = undefined;
                            $scope.properties.selectedIndex--;
                            console.log(9)
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.promedioGeneral = $scope.properties.formInput.catSolicitudDeAdmisionInput.promedioGeneral + "";
                            //$scope.properties.formInput.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                            //$scope.properties.formInput.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.bachillerato = $scope.properties.datosPreparatoria.nombreBachillerato;
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.estadoBachillerato = $scope.properties.datosPreparatoria.estadoBachillerato;
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.paisBachillerato = $scope.properties.datosPreparatoria.paisBachillerato;
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadBachillerato = $scope.properties.datosPreparatoria.ciudadBachillerato;


                            $scope.properties.formInput.constanciaDocumentInput.push($scope.properties.kardex);

                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catBachilleratos.perteneceRed) {
                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.resultadoPAA === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.resultadoPAA === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.resultadoPAA === null || $scope.properties.formInput.catSolicitudDeAdmisionInput.resultadoPAA === undefined) {
                                    swal("¡Resultado (PAA) del Examen Collage Board", "La calificación debe ser mayor a cero en caso de no haber realizado esta prueba marcarlo con el número 1", "warning");
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.resultadoPAA > 1) {
                                    if ($scope.properties.collageBoard !== undefined) {
                                        $scope.properties.formInput.resultadoCBDocumentInput = [];
                                        $scope.properties.formInput.resultadoCBDocumentInput.push($scope.properties.collageBoard);
                                        if ($scope.properties.tieneDescuento === true) {
                                            if ($scope.properties.descuento !== undefined) {
                                                $scope.properties.formInput.descuentoDocumentInput = [];
                                                $scope.properties.formInput.descuentoDocumentInput.push($scope.properties.descuento);








                                                if ($scope.properties.idExtranjero !== undefined) {
                                                    $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                                }
                                                $scope.properties.pasoInformacionPersonal = true;
                                                $scope.properties.selectedIndex++;
                                            } else {
                                                swal("¡Documento de descuento!", "Debes agregar el documento que acredita tu descuento", "warning");
                                            }
                                        } else {






                                            if ($scope.properties.idExtranjero !== undefined) {
                                                $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                            }
                                            $scope.properties.pasoInformacionPersonal = true;
                                            $scope.properties.selectedIndex++;
                                        }

                                    } else {
                                        swal("¡Constancia Collage Board!", "Debes agregar la constancia del resultado PAA como viene emitida por el Collage Board. En caso de no haber realizado esta prueba marcarlo con el número 1", "warning");
                                    }
                                }
                            } else {
                                if ($scope.properties.tieneDescuento === true) {
                                    if ($scope.properties.collageBoard !== undefined) {
                                        $scope.properties.formInput.resultadoCBDocumentInput = [];
                                        $scope.properties.formInput.resultadoCBDocumentInput.push($scope.properties.collageBoard);
                                    }
                                    if ($scope.properties.descuento !== undefined) {
                                        $scope.properties.formInput.descuentoDocumentInput = [];
                                        $scope.properties.formInput.descuentoDocumentInput.push($scope.properties.descuento);








                                        if ($scope.properties.idExtranjero !== undefined) {
                                            $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                        }
                                        $scope.properties.pasoInformacionPersonal = true;
                                        $scope.properties.selectedIndex++;
                                    } else {
                                        swal("¡Documento de descuento!", "Debes agregar el documento que acredita tu descuento", "warning");
                                    }
                                } else {
                                    debugger;
                                    if ($scope.properties.collageBoard !== undefined) {
                                        $scope.properties.formInput.resultadoCBDocumentInput = [];
                                        $scope.properties.formInput.resultadoCBDocumentInput.push($scope.properties.collageBoard);
                                    }






                                    if ($scope.properties.idExtranjero !== undefined) {
                                        $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                    }
                                    $scope.properties.pasoInformacionPersonal = true;
                                    $scope.properties.selectedIndex++;
                                }
                            }

                        }
                    }
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.promedioGeneral === "") {
                    swal("¡Promedio!", "Debes agregar el promedio que obtuvo en su preparatoria", "warning");
                } else if ($scope.properties.kardex === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                    swal("¡Contancia de calificaciones!", "Debes agregar la constancia de calificaciones de su preparatoria", "warning");
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.fotopasaporte = undefined;
                        $scope.properties.actanacimiento = undefined;
                        $scope.properties.kardex = undefined;
                        $scope.properties.descuento = undefined;
                        $scope.properties.collageBoard = undefined;
                        $scope.properties.cartaAA = undefined;
                        $scope.properties.selectedIndex--;
                        console.log(10)
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        //$scope.properties.formInput.catSolicitudDeAdmisionInput.promedioGeneral = $scope.properties.formInput.catSolicitudDeAdmisionInput.promedioGeneral + "";
                        //$scope.properties.formInput.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                        //$scope.properties.formInput.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                        $scope.properties.formInput.constanciaDocumentInput.push($scope.properties.kardex);

                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catBachilleratos.perteneceRed) {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.resultadoPAA === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.resultadoPAA === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.resultadoPAA === null || $scope.properties.formInput.catSolicitudDeAdmisionInput.resultadoPAA === undefined) {
                                swal("¡Resultado (PAA) del Examen Collage Board", "La calificación debe ser mayor a cero en caso de no haber realizado esta prueba marcarlo con el número 1", "warning");
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.resultadoPAA > 1) {
                                if ($scope.properties.collageBoard !== undefined) {
                                    $scope.properties.formInput.resultadoCBDocumentInput = [];
                                    $scope.properties.formInput.resultadoCBDocumentInput.push($scope.properties.collageBoard);
                                    if ($scope.properties.tieneDescuento === true) {
                                        if ($scope.properties.descuento !== undefined) {
                                            $scope.properties.formInput.descuentoDocumentInput = [];
                                            $scope.properties.formInput.descuentoDocumentInput.push($scope.properties.descuento);








                                            if ($scope.properties.idExtranjero !== undefined) {
                                                $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                            }
                                            $scope.properties.pasoInformacionPersonal = true;
                                            $scope.properties.selectedIndex++;
                                        } else {
                                            swal("¡Documento de descuento!", "Debes agregar el documento que acredita tu descuento", "warning");
                                        }
                                    } else {






                                        if ($scope.properties.idExtranjero !== undefined) {
                                            $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                        }
                                        $scope.properties.pasoInformacionPersonal = true;
                                        $scope.properties.selectedIndex++;
                                    }

                                } else {
                                    swal("¡Constancia Collage Board!", "Debes agregar la constancia del resultado PAA como viene emitida por el Collage Board. En caso de no haber realizado esta prueba marcarlo con el número 1", "warning");
                                }
                            } else if ($scope.properties.tieneDescuento === true) {
                                if ($scope.properties.collageBoard !== undefined) {
                                    $scope.properties.formInput.resultadoCBDocumentInput = [];
                                    $scope.properties.formInput.resultadoCBDocumentInput.push($scope.properties.collageBoard);
                                }
                                if ($scope.properties.descuento !== undefined) {
                                    $scope.properties.formInput.descuentoDocumentInput = [];
                                    $scope.properties.formInput.descuentoDocumentInput.push($scope.properties.descuento);








                                    if ($scope.properties.idExtranjero !== undefined) {
                                        $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                    }
                                    $scope.properties.pasoInformacionPersonal = true;
                                    $scope.properties.selectedIndex++;
                                } else {
                                    swal("¡Documento de descuento!", "Debes agregar el documento que acredita tu descuento", "warning");
                                }
                            } else {






                                if ($scope.properties.idExtranjero !== undefined) {
                                    $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                }
                                $scope.properties.pasoInformacionPersonal = true;
                                $scope.properties.selectedIndex++;
                            }
                        } else {
                            debugger;
                            if ($scope.properties.collageBoard !== undefined) {
                                $scope.properties.formInput.resultadoCBDocumentInput = [];
                                $scope.properties.formInput.resultadoCBDocumentInput.push($scope.properties.collageBoard);
                            }
                            if ($scope.properties.tieneDescuento === true) {
                                if ($scope.properties.descuento !== undefined) {
                                    $scope.properties.formInput.descuentoDocumentInput = [];
                                    $scope.properties.formInput.descuentoDocumentInput.push($scope.properties.descuento);








                                    if ($scope.properties.idExtranjero !== undefined) {
                                        $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                    }
                                    $scope.properties.pasoInformacionPersonal = true;
                                    $scope.properties.selectedIndex++;
                                } else {
                                    swal("¡Documento de descuento!", "Debes agregar el documento que acredita tu descuento", "warning");
                                }
                            } else {
                                debugger;
                                if ($scope.properties.collageBoard !== undefined) {
                                    $scope.properties.formInput.resultadoCBDocumentInput = [];
                                    $scope.properties.formInput.resultadoCBDocumentInput.push($scope.properties.collageBoard);
                                }






                                if ($scope.properties.idExtranjero !== undefined) {
                                    $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                }
                                $scope.properties.pasoInformacionPersonal = true;
                                $scope.properties.selectedIndex++;
                            }
                        }

                    }
                }
            }
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }
            //$scope.properties.formInput.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                //$scope.properties.formInput.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                $scope.properties.formInput.constanciaDocumentInput.push($scope.properties.kardex);*/
            /* if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                 $scope.properties.selectedIndex--;
             } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                  //$scope.properties.formInput.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                     //$scope.properties.formInput.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                     $scope.properties.formInput.constanciaDocumentInput.push($scope.properties.kardex);
                 $scope.properties.selectedIndex++;
             }*/
        } else if ($scope.properties.selectedIndex === 3) {
            console.log("validar 3");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.fotopasaporte = undefined;
                $scope.properties.actanacimiento = undefined;
                $scope.properties.kardex = undefined;
                $scope.properties.descuento = undefined;
                $scope.properties.collageBoard = undefined;
                $scope.properties.cartaAA = undefined;
                $scope.properties.selectedIndex--;
                console.log(11)
            } else if ($scope.properties.formInput.tutorInput.length === 0) {
                swal("¡Tutor!", "Debes agregar al menos un tutor", "warning");
            } else if ($scope.properties.formInput.padreInput.desconozcoDatosPadres) {
                //validar madre
                if ($scope.properties.formInput.madreInput.desconozcoDatosPadres) {
                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.fotopasaporte = undefined;
                            $scope.properties.actanacimiento = undefined;
                            $scope.properties.kardex = undefined;
                            $scope.properties.descuento = undefined;
                            $scope.properties.collageBoard = undefined;
                            $scope.properties.cartaAA = undefined;
                            $scope.properties.selectedIndex--;
                            console.log(12)
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                            $scope.properties.pasoInformacionFamiliar = true;
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else if ($scope.properties.formInput.madreInput.catTitulo === 0 || $scope.properties.formInput.madreInput.catTitulo === null) {
                    swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                } else if ($scope.properties.formInput.madreInput.nombre === "" || $scope.properties.formInput.madreInput.nombre === undefined) {
                    swal("¡Nombre de la madre!", "Debes agregar nombre de la madre", "warning");
                } else if ($scope.properties.formInput.madreInput.apellidos === "" || $scope.properties.formInput.madreInput.apellidos === undefined) {
                    swal("¡Apellidos de la madre!", "Debes agregar los apellidos de la madre", "warning");
                } else if ($scope.properties.formInput.madreInput.vive === 0 || $scope.properties.formInput.madreInput.vive === null) {
                    swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.formInput.madreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.madreInput.catEgresoAnahuac === null) {
                        swal("¡Egreso Anáhuac!", "Debes seleccionar si su madre egresó de la universidad Anáhuac", "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.formInput.madreInput.catCampusEgreso === 0 || $scope.properties.formInput.madreInput.catCampusEgreso === null) {
                            swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó su madre", "warning");
                        } else {
                            if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                swal("¡Trabaja!", "Debes seleccionar si su madre trabaja", "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                    swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su madre trabaja", "warning");
                                } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                    swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                    swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                }
                                /*else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                   swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                               } */
                                else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                } else {
                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.fotopasaporte = undefined;
                                            $scope.properties.actanacimiento = undefined;
                                            $scope.properties.kardex = undefined;
                                            $scope.properties.descuento = undefined;
                                            $scope.properties.collageBoard = undefined;
                                            $scope.properties.cartaAA = undefined;
                                            $scope.properties.selectedIndex--;
                                            console.log(13)
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                    swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                }
                                /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                }*/
                                else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                } else {
                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.fotopasaporte = undefined;
                                            $scope.properties.actanacimiento = undefined;
                                            $scope.properties.kardex = undefined;
                                            $scope.properties.descuento = undefined;
                                            $scope.properties.collageBoard = undefined;
                                            $scope.properties.cartaAA = undefined;
                                            $scope.properties.selectedIndex--;
                                            console.log(14)
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                            swal("¡Trabaja!", "Debes seleccionar si su madre trabaja", "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su madre trabaja", "warning");
                            } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                            } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                            }
                            /*else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                           swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                       } */
                            else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                            } else {
                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.fotopasaporte = undefined;
                                        $scope.properties.actanacimiento = undefined;
                                        $scope.properties.kardex = undefined;
                                        $scope.properties.descuento = undefined;
                                        $scope.properties.collageBoard = undefined;
                                        $scope.properties.cartaAA = undefined;
                                        $scope.properties.selectedIndex--;
                                        console.log(15)
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                            } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                            }
                            /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                        } */
                            else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                            } else {
                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.fotopasaporte = undefined;
                                        $scope.properties.actanacimiento = undefined;
                                        $scope.properties.kardex = undefined;
                                        $scope.properties.descuento = undefined;
                                        $scope.properties.collageBoard = undefined;
                                        $scope.properties.cartaAA = undefined;
                                        $scope.properties.selectedIndex--;
                                        console.log(16)
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.fotopasaporte = undefined;
                        $scope.properties.actanacimiento = undefined;
                        $scope.properties.kardex = undefined;
                        $scope.properties.descuento = undefined;
                        $scope.properties.collageBoard = undefined;
                        $scope.properties.cartaAA = undefined;
                        $scope.properties.selectedIndex--;
                        console.log(17)
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                        $scope.properties.pasoInformacionFamiliar = true;
                        $scope.properties.selectedIndex++;
                    }
                }




            } else if ($scope.properties.formInput.padreInput.catTitulo === 0 || $scope.properties.formInput.padreInput.catTitulo === null) {
                swal("¡Título!", "Debes seleccionar el título para identificar al padre", "warning");
            } else if ($scope.properties.formInput.padreInput.nombre === "" || $scope.properties.formInput.padreInput.nombre === undefined) {
                swal("¡Nombre del padre!", "Debes agregar nombre del padre", "warning");
            } else if ($scope.properties.formInput.padreInput.apellidos === "" || $scope.properties.formInput.padreInput.apellidos === undefined) {
                swal("¡Apellidos del padre!", "Debes agregar los apellidos del padre", "warning");
            } else if ($scope.properties.formInput.padreInput.vive === 0 || $scope.properties.formInput.padreInput.vive === null) {
                swal("¡Padre vive!", "Debes seleccionar si el padre vive", "warning");
            } else if ($scope.properties.datosPadres.padrevive) {
                if ($scope.properties.formInput.padreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.padreInput.catEgresoAnahuac === null) {
                    swal("¡Egreso Anáhuac!", "Debes seleccionar si su padre egreso de la universidad Anáhuac", "warning");
                } else if ($scope.properties.datosPadres.padreegresoanahuac) {
                    if ($scope.properties.formInput.padreInput.catCampusEgreso === 0 || $scope.properties.formInput.padreInput.catCampusEgreso === null) {
                        swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó su padre", "warning");
                    } else {
                        if ($scope.properties.formInput.padreInput.catTrabaja === 0 || $scope.properties.formInput.padreInput.catTrabaja === null) {
                            swal("¡Trabaja!", "Debes seleccionar si su padre trabaja", "warning");
                        } else if ($scope.properties.datosPadres.padretrabaja) {
                            if ($scope.properties.formInput.padreInput.empresaTrabaja === "" || $scope.properties.formInput.padreInput.empresaTrabaja === undefined) {
                                swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su padre trabaja", "warning");
                            } else if ($scope.properties.formInput.padreInput.puesto === "" || $scope.properties.formInput.padreInput.puesto === undefined) {
                                swal("¡Puesto!", "Debes agregar el puesto de trabajo del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.correoElectronico === "" || $scope.properties.formInput.padreInput.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico del padre", "warning");
                            } else if (!re.test(String($scope.properties.formInput.padreInput.correoElectronico.trim()).toLowerCase())) {
                                swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                            } else if ($scope.properties.formInput.padreInput.catEscolaridad === 0 || $scope.properties.formInput.padreInput.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.catPais === 0 || $scope.properties.formInput.padreInput.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio del padre", "warning");
                            }
                            /* else if ($scope.properties.formInput.padreInput.catEstado === 0 || $scope.properties.formInput.padreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                                        } */
                            else if ($scope.properties.formInput.padreInput.calle === "" || $scope.properties.formInput.padreInput.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.codigoPostal === "" || $scope.properties.formInput.padreInput.codigoPostal === undefined) {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.numeroExterior === "" || $scope.properties.formInput.padreInput.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.ciudad === "" || $scope.properties.formInput.padreInput.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la calle del domicilio del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.colonia === "" || $scope.properties.formInput.padreInput.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.telefono === "" || $scope.properties.formInput.padreInput.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono del padre", "warning");
                            } else {
                                ///Validar madre 1
                                if ($scope.properties.formInput.madreInput.desconozcoDatosPadres) {
                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.fotopasaporte = undefined;
                                            $scope.properties.actanacimiento = undefined;
                                            $scope.properties.kardex = undefined;
                                            $scope.properties.descuento = undefined;
                                            $scope.properties.collageBoard = undefined;
                                            $scope.properties.cartaAA = undefined;
                                            $scope.properties.selectedIndex--;
                                            console.log(18)
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                } else if ($scope.properties.formInput.madreInput.catTitulo === 0 || $scope.properties.formInput.madreInput.catTitulo === null) {
                                    swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.nombre === "" || $scope.properties.formInput.madreInput.nombre === undefined) {
                                    swal("¡Nombre de la madre!", "Debes agregar nombre de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.apellidos === "" || $scope.properties.formInput.madreInput.apellidos === undefined) {
                                    swal("¡Apellidos de la madre!", "Debes agregar los apellidos de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.vive === 0 || $scope.properties.formInput.madreInput.vive === null) {
                                    swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.formInput.madreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.madreInput.catEgresoAnahuac === null) {
                                        swal("¡Egreso Anáhuac!", "Debes seleccionar si su madre egresó de la universidad Anáhuac", "warning");
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.formInput.madreInput.catCampusEgreso === 0 || $scope.properties.formInput.madreInput.catCampusEgreso === null) {
                                            swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó su madre", "warning");
                                        } else {
                                            if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                                swal("¡Trabaja!", "Debes seleccionar si su madre trabaja", "warning");
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                                    swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su madre trabaja", "warning");
                                                } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                                    swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                                } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                                }
                                                /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                } */
                                                else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                                } else {
                                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.fotopasaporte = undefined;
                                                            $scope.properties.actanacimiento = undefined;
                                                            $scope.properties.kardex = undefined;
                                                            $scope.properties.descuento = undefined;
                                                            $scope.properties.collageBoard = undefined;
                                                            $scope.properties.cartaAA = undefined;
                                                            $scope.properties.selectedIndex--;
                                                            console.log(19)
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                                } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                                }
                                                /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                }*/
                                                else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                                } else {
                                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.fotopasaporte = undefined;
                                                            $scope.properties.actanacimiento = undefined;
                                                            $scope.properties.kardex = undefined;
                                                            $scope.properties.descuento = undefined;
                                                            $scope.properties.collageBoard = undefined;
                                                            $scope.properties.cartaAA = undefined;
                                                            $scope.properties.selectedIndex--;
                                                            console.log(20)
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                            swal("¡Trabaja!", "Debes seleccionar si su madre trabaja", "warning");
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                                swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su madre trabaja", "warning");
                                            } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                                swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                            } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                            }
                                            /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                        }*/
                                            else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                            } else {
                                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.fotopasaporte = undefined;
                                                        $scope.properties.actanacimiento = undefined;
                                                        $scope.properties.kardex = undefined;
                                                        $scope.properties.descuento = undefined;
                                                        $scope.properties.collageBoard = undefined;
                                                        $scope.properties.cartaAA = undefined;
                                                        $scope.properties.selectedIndex--;
                                                        console.log(21)
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                            } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                            }
                                            /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                        } */
                                            else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                            } else {
                                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.fotopasaporte = undefined;
                                                        $scope.properties.actanacimiento = undefined;
                                                        $scope.properties.kardex = undefined;
                                                        $scope.properties.descuento = undefined;
                                                        $scope.properties.collageBoard = undefined;
                                                        $scope.properties.cartaAA = undefined;
                                                        $scope.properties.selectedIndex--;
                                                        console.log(22)
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.fotopasaporte = undefined;
                                        $scope.properties.actanacimiento = undefined;
                                        $scope.properties.kardex = undefined;
                                        $scope.properties.descuento = undefined;
                                        $scope.properties.collageBoard = undefined;
                                        $scope.properties.cartaAA = undefined;
                                        $scope.properties.selectedIndex--;
                                        console.log(22)
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.formInput.padreInput.correoElectronico === "" || $scope.properties.formInput.padreInput.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico del padre", "warning");
                            } else if (!re.test(String($scope.properties.formInput.padreInput.correoElectronico.trim()).toLowerCase())) {
                                swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                            } else if ($scope.properties.formInput.padreInput.catEscolaridad === 0 || $scope.properties.formInput.padreInput.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.catPais === 0 || $scope.properties.formInput.padreInput.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio del padre", "warning");
                            }
                            /* else if ($scope.properties.formInput.padreInput.catEstado === 0 || $scope.properties.formInput.padreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                                        }*/
                            else if ($scope.properties.formInput.padreInput.calle === "" || $scope.properties.formInput.padreInput.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.codigoPostal === "" || $scope.properties.formInput.padreInput.codigoPostal === undefined) {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.numeroExterior === "" || $scope.properties.formInput.padreInput.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.ciudad === "" || $scope.properties.formInput.padreInput.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la calle del domicilio del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.colonia === "" || $scope.properties.formInput.padreInput.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio del padre", "warning");
                            } else if ($scope.properties.formInput.padreInput.telefono === "" || $scope.properties.formInput.padreInput.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono del padre", "warning");
                            } else {
                                //VALIDAR MADRE 2
                                if ($scope.properties.formInput.madreInput.desconozcoDatosPadres) {
                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.fotopasaporte = undefined;
                                            $scope.properties.actanacimiento = undefined;
                                            $scope.properties.kardex = undefined;
                                            $scope.properties.descuento = undefined;
                                            $scope.properties.collageBoard = undefined;
                                            $scope.properties.cartaAA = undefined;
                                            $scope.properties.selectedIndex--;
                                            console.log(22)
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                } else if ($scope.properties.formInput.madreInput.catTitulo === 0 || $scope.properties.formInput.madreInput.catTitulo === null) {
                                    swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.nombre === "" || $scope.properties.formInput.madreInput.nombre === undefined) {
                                    swal("¡Nombre de la madre!", "Debes agregar nombre de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.apellidos === "" || $scope.properties.formInput.madreInput.apellidos === undefined) {
                                    swal("¡Apellidos de la madre!", "Debes agregar los apellidos de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.vive === 0 || $scope.properties.formInput.madreInput.vive === null) {
                                    swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.formInput.madreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.madreInput.catEgresoAnahuac === null) {
                                        swal("¡Egreso Anáhuac!", "Debes seleccionar si su madre egresó de la universidad Anáhuac", "warning");
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.formInput.madreInput.catCampusEgreso === 0 || $scope.properties.formInput.madreInput.catCampusEgreso === null) {
                                            swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó su madre", "warning");
                                        } else {
                                            if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                                swal("¡Trabaja!", "Debes seleccionar si su madre trabaja", "warning");
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                                    swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su madre trabaja", "warning");
                                                } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                                    swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                                } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                                }
                                                /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                } */
                                                else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                                } else {
                                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.fotopasaporte = undefined;
                                                            $scope.properties.actanacimiento = undefined;
                                                            $scope.properties.kardex = undefined;
                                                            $scope.properties.descuento = undefined;
                                                            $scope.properties.collageBoard = undefined;
                                                            $scope.properties.cartaAA = undefined;
                                                            $scope.properties.selectedIndex--;
                                                            console.log(23)
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                                } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                    swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                                }
                                                /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                                } */
                                                else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                                } else {
                                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.fotopasaporte = undefined;
                                                            $scope.properties.actanacimiento = undefined;
                                                            $scope.properties.kardex = undefined;
                                                            $scope.properties.descuento = undefined;
                                                            $scope.properties.collageBoard = undefined;
                                                            $scope.properties.cartaAA = undefined;
                                                            $scope.properties.selectedIndex--;
                                                            console.log(24)
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                            $scope.properties.pasoInformacionFamiliar = true;
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                            swal("¡Trabaja!", "Debes seleccionar si su madre trabaja", "warning");
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                                swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su madre trabaja", "warning");
                                            } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                                swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                            } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                            }
                                            /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                        }*/
                                            else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                            } else {
                                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.fotopasaporte = undefined;
                                                        $scope.properties.actanacimiento = undefined;
                                                        $scope.properties.kardex = undefined;
                                                        $scope.properties.descuento = undefined;
                                                        $scope.properties.collageBoard = undefined;
                                                        $scope.properties.cartaAA = undefined;
                                                        $scope.properties.selectedIndex--;
                                                        console.log(25)
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                            } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                                swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                            }
                                            /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                        }*/
                                            else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                            } else {
                                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.fotopasaporte = undefined;
                                                        $scope.properties.actanacimiento = undefined;
                                                        $scope.properties.kardex = undefined;
                                                        $scope.properties.descuento = undefined;
                                                        $scope.properties.collageBoard = undefined;
                                                        $scope.properties.cartaAA = undefined;
                                                        $scope.properties.selectedIndex--;
                                                        console.log(27)
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                        $scope.properties.pasoInformacionFamiliar = true;
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.fotopasaporte = undefined;
                                        $scope.properties.actanacimiento = undefined;
                                        $scope.properties.kardex = undefined;
                                        $scope.properties.descuento = undefined;
                                        $scope.properties.collageBoard = undefined;
                                        $scope.properties.cartaAA = undefined;
                                        $scope.properties.selectedIndex--;
                                        console.log(28)
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        }
                    }
                } else if ($scope.properties.formInput.padreInput.catTrabaja === 0 || $scope.properties.formInput.padreInput.catTrabaja === null) {
                    swal("¡Trabaja!", "Debes seleccionar si su padre trabaja", "warning");
                } else if ($scope.properties.datosPadres.padretrabaja) {
                    if ($scope.properties.formInput.padreInput.empresaTrabaja === "" || $scope.properties.formInput.padreInput.empresaTrabaja === undefined) {
                        swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su padre trabaja", "warning");
                    } else if ($scope.properties.formInput.padreInput.puesto === "" || $scope.properties.formInput.padreInput.puesto === undefined) {
                        swal("¡Puesto!", "Debes agregar el puesto de trabajo del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.correoElectronico === "" || $scope.properties.formInput.padreInput.correoElectronico === undefined) {
                        swal("¡Correo electrónico!", "Debes agregar el correo electrónico del padre", "warning");
                    } else if (!re.test(String($scope.properties.formInput.padreInput.correoElectronico.trim()).toLowerCase())) {
                        swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                    } else if ($scope.properties.formInput.padreInput.catEscolaridad === 0 || $scope.properties.formInput.padreInput.catEscolaridad === null) {
                        swal("¡Escolaridad!", "Debes seleccionar la escolaridad del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.catPais === 0 || $scope.properties.formInput.padreInput.catPais === null) {
                        swal("¡País!", "Debes agregar el país del domicilio del padre", "warning");
                    }
                    /* else if ($scope.properties.formInput.padreInput.catEstado === 0 || $scope.properties.formInput.padreInput.catEstado === null) {
                                            swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                        }*/
                    else if ($scope.properties.formInput.padreInput.calle === "" || $scope.properties.formInput.padreInput.calle === undefined) {
                        swal("¡Calle!", "Debes agregar la calle del domicilio del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.codigoPostal === "" || $scope.properties.formInput.padreInput.codigoPostal === undefined) {
                        swal("¡Código postal!", "Debes agregar el código postal del domicilio del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.numeroExterior === "" || $scope.properties.formInput.padreInput.numeroExterior === undefined) {
                        swal("¡Número exterior!", "Debes agregar el número exterior del domicilio del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.ciudad === "" || $scope.properties.formInput.padreInput.ciudad === undefined) {
                        swal("¡Ciudad!", "Debes agregar la calle del domicilio del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.colonia === "" || $scope.properties.formInput.padreInput.colonia === undefined) {
                        swal("¡Colonia!", "Debes agregar la colonia del domicilio del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.telefono === "" || $scope.properties.formInput.padreInput.telefono === undefined) {
                        swal("¡Teléfono!", "Debes agregar el teléfono del padre", "warning");
                    } else {
                        ///Validar madre  3
                        if ($scope.properties.formInput.madreInput.desconozcoDatosPadres) {
                            if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.fotopasaporte = undefined;
                                    $scope.properties.actanacimiento = undefined;
                                    $scope.properties.kardex = undefined;
                                    $scope.properties.descuento = undefined;
                                    $scope.properties.collageBoard = undefined;
                                    $scope.properties.cartaAA = undefined;
                                    $scope.properties.selectedIndex--;
                                    console.log(29)
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                    $scope.properties.pasoInformacionFamiliar = true;
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else if ($scope.properties.formInput.madreInput.catTitulo === 0 || $scope.properties.formInput.madreInput.catTitulo === null) {
                            swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                        } else if ($scope.properties.formInput.madreInput.nombre === "" || $scope.properties.formInput.madreInput.nombre === undefined) {
                            swal("¡Nombre de la madre!", "Debes agregar nombre de la madre", "warning");
                        } else if ($scope.properties.formInput.madreInput.apellidos === "" || $scope.properties.formInput.madreInput.apellidos === undefined) {
                            swal("¡Apellidos de la madre!", "Debes agregar los apellidos de la madre", "warning");
                        } else if ($scope.properties.formInput.madreInput.vive === 0 || $scope.properties.formInput.madreInput.vive === null) {
                            swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.formInput.madreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.madreInput.catEgresoAnahuac === null) {
                                swal("¡Egreso Anáhuac!", "Debes seleccionar si su madre egresó de la universidad Anáhuac", "warning");
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.formInput.madreInput.catCampusEgreso === 0 || $scope.properties.formInput.madreInput.catCampusEgreso === null) {
                                    swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó su madre", "warning");
                                } else {
                                    if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                        swal("¡Trabaja!", "Debes seleccionar si su madre trabaja", "warning");
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                            swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su madre trabaja", "warning");
                                        } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                            swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                            swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                        } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                            swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                            swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                            swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                        }
                                        /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                } */
                                        else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                            swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                            swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                            swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                            swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                            swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                            swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                        } else {
                                            if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.fotopasaporte = undefined;
                                                    $scope.properties.actanacimiento = undefined;
                                                    $scope.properties.kardex = undefined;
                                                    $scope.properties.descuento = undefined;
                                                    $scope.properties.collageBoard = undefined;
                                                    $scope.properties.cartaAA = undefined;
                                                    $scope.properties.selectedIndex--;
                                                    console.log(30)
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                            swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                        } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                            swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                            swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                            swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                        }
                                        /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                }*/
                                        else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                            swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                            swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                            swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                            swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                            swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                            swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                        } else {
                                            if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.fotopasaporte = undefined;
                                                    $scope.properties.actanacimiento = undefined;
                                                    $scope.properties.kardex = undefined;
                                                    $scope.properties.descuento = undefined;
                                                    $scope.properties.collageBoard = undefined;
                                                    $scope.properties.cartaAA = undefined;
                                                    $scope.properties.selectedIndex--;
                                                    console.log(31)
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                    swal("¡Trabaja!", "Debes seleccionar si su madre trabaja", "warning");
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                        swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su madre trabaja", "warning");
                                    } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                        swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                        swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                    } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                        swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                        swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                        swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                    }
                                    /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                        }*/
                                    else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                        swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                        swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                        swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                        swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                        swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                        swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                    } else {
                                        if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                            swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.fotopasaporte = undefined;
                                                $scope.properties.actanacimiento = undefined;
                                                $scope.properties.kardex = undefined;
                                                $scope.properties.descuento = undefined;
                                                $scope.properties.collageBoard = undefined;
                                                $scope.properties.cartaAA = undefined;
                                                $scope.properties.selectedIndex--;
                                                console.log(32)
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                $scope.properties.pasoInformacionFamiliar = true;
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                        swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                    } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                        swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                        swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                        swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                    }
                                    /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                        }*/
                                    else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                        swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                        swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                        swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                        swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                        swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                        swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                    } else {
                                        if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                            swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.fotopasaporte = undefined;
                                                $scope.properties.actanacimiento = undefined;
                                                $scope.properties.kardex = undefined;
                                                $scope.properties.descuento = undefined;
                                                $scope.properties.collageBoard = undefined;
                                                $scope.properties.cartaAA = undefined;
                                                $scope.properties.selectedIndex--;
                                                console.log(33)
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                $scope.properties.pasoInformacionFamiliar = true;
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.fotopasaporte = undefined;
                                $scope.properties.actanacimiento = undefined;
                                $scope.properties.kardex = undefined;
                                $scope.properties.descuento = undefined;
                                $scope.properties.collageBoard = undefined;
                                $scope.properties.cartaAA = undefined;
                                $scope.properties.selectedIndex--;
                                console.log(34)
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                $scope.properties.pasoInformacionFamiliar = true;
                                $scope.properties.selectedIndex++;
                            }
                        }
                    }
                } else {
                    if ($scope.properties.formInput.padreInput.correoElectronico === "" || $scope.properties.formInput.padreInput.correoElectronico === undefined) {
                        swal("¡Correo electrónico!", "Debes agregar el correo electrónico del padre", "warning");
                    } else if (!re.test(String($scope.properties.formInput.padreInput.correoElectronico.trim()).toLowerCase())) {
                        swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                    } else if ($scope.properties.formInput.padreInput.catEscolaridad === 0 || $scope.properties.formInput.padreInput.catEscolaridad === null) {
                        swal("¡Escolaridad!", "Debes seleccionar la escolaridad del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.catPais === 0 || $scope.properties.formInput.padreInput.catPais === null) {
                        swal("¡País!", "Debes agregar el país del domicilio del padre", "warning");
                    }
                    /* else if ($scope.properties.formInput.padreInput.catEstado === 0 || $scope.properties.formInput.padreInput.catEstado === null) {
                                            swal("¡Estado!", "Debes agregar el estado del domicilio del padre", "warning");
                                        }*/
                    else if ($scope.properties.formInput.padreInput.calle === "" || $scope.properties.formInput.padreInput.calle === undefined) {
                        swal("¡Calle!", "Debes agregar la calle del domicilio del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.codigoPostal === "" || $scope.properties.formInput.padreInput.codigoPostal === undefined) {
                        swal("¡Código postal!", "Debes agregar el código postal del domicilio del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.numeroExterior === "" || $scope.properties.formInput.padreInput.numeroExterior === undefined) {
                        swal("¡Número exterior!", "Debes agregar el número exterior del domicilio del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.ciudad === "" || $scope.properties.formInput.padreInput.ciudad === undefined) {
                        swal("¡Ciudad!", "Debes agregar la calle del domicilio del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.colonia === "" || $scope.properties.formInput.padreInput.colonia === undefined) {
                        swal("¡Colonia!", "Debes agregar la colonia del domicilio del padre", "warning");
                    } else if ($scope.properties.formInput.padreInput.telefono === "" || $scope.properties.formInput.padreInput.telefono === undefined) {
                        swal("¡Teléfono!", "Debes agregar el teléfono del padre", "warning");
                    } else {
                        //VALIDAR MADRE 4
                        if ($scope.properties.formInput.madreInput.desconozcoDatosPadres) {
                            if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.fotopasaporte = undefined;
                                    $scope.properties.actanacimiento = undefined;
                                    $scope.properties.kardex = undefined;
                                    $scope.properties.descuento = undefined;
                                    $scope.properties.collageBoard = undefined;
                                    $scope.properties.cartaAA = undefined;
                                    $scope.properties.selectedIndex--;
                                    console.log(35)
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                    $scope.properties.pasoInformacionFamiliar = true;
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else if ($scope.properties.formInput.madreInput.catTitulo === 0 || $scope.properties.formInput.madreInput.catTitulo === null) {
                            swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                        } else if ($scope.properties.formInput.madreInput.nombre === "" || $scope.properties.formInput.madreInput.nombre === undefined) {
                            swal("¡Nombre de la madre!", "Debes agregar nombre de la madre", "warning");
                        } else if ($scope.properties.formInput.madreInput.apellidos === "" || $scope.properties.formInput.madreInput.apellidos === undefined) {
                            swal("¡Apellidos de la madre!", "Debes agregar los apellidos de la madre", "warning");
                        } else if ($scope.properties.formInput.madreInput.vive === 0 || $scope.properties.formInput.madreInput.vive === null) {
                            swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.formInput.madreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.madreInput.catEgresoAnahuac === null) {
                                swal("¡Egreso Anáhuac!", "Debes seleccionar si su madre egresó de la universidad Anáhuac", "warning");
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.formInput.madreInput.catCampusEgreso === 0 || $scope.properties.formInput.madreInput.catCampusEgreso === null) {
                                    swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó su madre", "warning");
                                } else {
                                    if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                        swal("¡Trabaja!", "Debes seleccionar si su madre trabaja", "warning");
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                            swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su madre trabaja", "warning");
                                        } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                            swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                            swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                        } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                            swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                            swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                            swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                        }
                                        /*else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                                   swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                               } */
                                        else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                            swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                            swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                            swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                            swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                            swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                            swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                        } else {
                                            if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.fotopasaporte = undefined;
                                                    $scope.properties.actanacimiento = undefined;
                                                    $scope.properties.kardex = undefined;
                                                    $scope.properties.descuento = undefined;
                                                    $scope.properties.collageBoard = undefined;
                                                    $scope.properties.cartaAA = undefined;
                                                    $scope.properties.selectedIndex--;
                                                    console.log(37)
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                            swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                        } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                            swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                            swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                            swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                        }
                                        /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                                }*/
                                        else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                            swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                            swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                            swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                            swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                            swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                        } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                            swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                        } else {
                                            if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.fotopasaporte = undefined;
                                                    $scope.properties.actanacimiento = undefined;
                                                    $scope.properties.kardex = undefined;
                                                    $scope.properties.descuento = undefined;
                                                    $scope.properties.collageBoard = undefined;
                                                    $scope.properties.cartaAA = undefined;
                                                    $scope.properties.selectedIndex--;
                                                    console.log(38)
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                    $scope.properties.pasoInformacionFamiliar = true;
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                    swal("¡Trabaja!", "Debes seleccionar si su madre trabaja", "warning");
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                        swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su madre trabaja", "warning");
                                    } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                        swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                        swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                    } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                        swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                        swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                        swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                    }
                                    /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                        }*/
                                    else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                        swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                        swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                        swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                        swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                        swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                        swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                    } else {
                                        if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                            swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.fotopasaporte = undefined;
                                                $scope.properties.actanacimiento = undefined;
                                                $scope.properties.kardex = undefined;
                                                $scope.properties.descuento = undefined;
                                                $scope.properties.collageBoard = undefined;
                                                $scope.properties.cartaAA = undefined;
                                                $scope.properties.selectedIndex--;
                                                console.log(39)
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                $scope.properties.pasoInformacionFamiliar = true;
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                        swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                    } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                                        swal("¡Correo electrónico!", "Su correo electrónico no es válido", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                        swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                        swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                    }
                                    /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                        }*/
                                    else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                        swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                        swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                        swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                        swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                        swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                    } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                        swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                    } else {
                                        if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                            swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.fotopasaporte = undefined;
                                                $scope.properties.actanacimiento = undefined;
                                                $scope.properties.kardex = undefined;
                                                $scope.properties.descuento = undefined;
                                                $scope.properties.collageBoard = undefined;
                                                $scope.properties.cartaAA = undefined;
                                                $scope.properties.selectedIndex--;
                                                console.log(40)
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                                $scope.properties.pasoInformacionFamiliar = true;
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.fotopasaporte = undefined;
                                $scope.properties.actanacimiento = undefined;
                                $scope.properties.kardex = undefined;
                                $scope.properties.descuento = undefined;
                                $scope.properties.collageBoard = undefined;
                                $scope.properties.cartaAA = undefined;
                                $scope.properties.selectedIndex--;
                                console.log(41)
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                $scope.properties.pasoInformacionFamiliar = true;
                                $scope.properties.selectedIndex++;
                            }
                        }
                    }
                }

            } else {
                console.log("esta validando el la linea 373")
                if ($scope.properties.formInput.madreInput.catTitulo === 0 || $scope.properties.formInput.madreInput.catTitulo === null) {
                    swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                } else if ($scope.properties.formInput.madreInput.nombre === "" || $scope.properties.formInput.madreInput.nombre === undefined) {
                    swal("¡Nombre de la madre!", "Debes agregar nombre de la madre", "warning");
                } else if ($scope.properties.formInput.madreInput.apellidos === "" || $scope.properties.formInput.madreInput.apellidos === undefined) {
                    swal("¡Apellidos de la madre!", "Debes agregar los apellidos de la madre", "warning");
                } else if ($scope.properties.formInput.madreInput.vive === 0 || $scope.properties.formInput.madreInput.vive === null) {
                    swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.formInput.madreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.madreInput.catEgresoAnahuac === null) {
                        swal("¡Egreso Anáhuac!", "Debes seleccionar si su madre egresó de la universidad Anáhuac", "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.formInput.madreInput.catCampusEgreso === 0 || $scope.properties.formInput.madreInput.catCampusEgreso === null) {
                            swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó su madre", "warning");
                        } else {
                            if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                swal("¡Trabaja!", "Debes seleccionar si su madre trabaja", "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                    swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su madre trabaja", "warning");
                                } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                    swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                }
                                /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                }*/
                                else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                } else {
                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.fotopasaporte = undefined;
                                            $scope.properties.actanacimiento = undefined;
                                            $scope.properties.kardex = undefined;
                                            $scope.properties.descuento = undefined;
                                            $scope.properties.collageBoard = undefined;
                                            $scope.properties.cartaAA = undefined;
                                            $scope.properties.selectedIndex--;
                                            console.log(42)
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                    swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                                }
                                /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                                    swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                                }*/
                                else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                    swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                    swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                    swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                                } else {
                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.fotopasaporte = undefined;
                                            $scope.properties.actanacimiento = undefined;
                                            $scope.properties.kardex = undefined;
                                            $scope.properties.descuento = undefined;
                                            $scope.properties.collageBoard = undefined;
                                            $scope.properties.cartaAA = undefined;
                                            $scope.properties.selectedIndex--;
                                            console.log(43)
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                            $scope.properties.pasoInformacionFamiliar = true;
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                            swal("¡Trabaja!", "Debes seleccionar si su madre trabaja", "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                swal("¡Empresa!", "Debes agregar el nombre de la empresa donde su madre trabaja", "warning");
                            } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                swal("¡Puesto!", "Debes agregar el puesto de trabajo de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                            }
                            /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                        }*/
                            else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                            } else {
                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.fotopasaporte = undefined;
                                        $scope.properties.actanacimiento = undefined;
                                        $scope.properties.kardex = undefined;
                                        $scope.properties.descuento = undefined;
                                        $scope.properties.collageBoard = undefined;
                                        $scope.properties.cartaAA = undefined;
                                        $scope.properties.selectedIndex--;
                                        console.log(44)
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio de la madre", "warning");
                            }
                            /* else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                            swal("¡Estado!", "Debes agregar el estado del domicilio de la madre", "warning");
                                                        }*/
                            else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la calle del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de la madre", "warning");
                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono de la madre", "warning");
                            } else {
                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.fotopasaporte = undefined;
                                        $scope.properties.actanacimiento = undefined;
                                        $scope.properties.kardex = undefined;
                                        $scope.properties.descuento = undefined;
                                        $scope.properties.collageBoard = undefined;
                                        $scope.properties.cartaAA = undefined;
                                        $scope.properties.selectedIndex--;
                                        console.log(45)
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                                        $scope.properties.pasoInformacionFamiliar = true;
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                        swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.fotopasaporte = undefined;
                            $scope.properties.actanacimiento = undefined;
                            $scope.properties.kardex = undefined;
                            $scope.properties.descuento = undefined;
                            $scope.properties.collageBoard = undefined;
                            $scope.properties.cartaAA = undefined;
                            $scope.properties.selectedIndex--;
                            console.log(46)
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {


                            $scope.properties.pasoInformacionFamiliar = true;
                            $scope.properties.selectedIndex++;
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
                $scope.properties.fotopasaporte = undefined;
                $scope.properties.actanacimiento = undefined;
                $scope.properties.kardex = undefined;
                $scope.properties.descuento = undefined;
                $scope.properties.collageBoard = undefined;
                $scope.properties.cartaAA = undefined;
                $scope.properties.selectedIndex--;
                console.log(47)
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {

                $scope.properties.selectedIndex++;
            }
        } else if ($scope.properties.selectedIndex === 5) {
            console.log("validar 4");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.fotopasaporte = undefined;
                $scope.properties.actanacimiento = undefined;
                $scope.properties.kardex = undefined;
                $scope.properties.descuento = undefined;
                $scope.properties.collageBoard = undefined;
                $scope.properties.cartaAA = undefined;
                $scope.properties.selectedIndex--;
                console.log(48)
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {

                $scope.properties.selectedIndex++;
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
}