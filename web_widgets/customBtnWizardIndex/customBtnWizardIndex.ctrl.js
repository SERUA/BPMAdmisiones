function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    $scope.action = function() {
        console.log("boton de siguiente");
        if ($scope.properties.selectedIndex === 0) {
            console.log("validar 0");
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }*/
            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catCampus.persistenceId_string === "") {
                swal("Campus!", "Debe seleccionar un campus donde cursara tus estudios!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catGestionEscolar === null) {
                swal("Licenciatura!", "Debe seleccionar una licenciatura!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catGestionEscolar.propedeutico) {
                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPropedeutico === null) {
                    swal("Examen propedéutico!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
                } else {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPeriodo === null) {
                        swal("Periodo!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen === null) {
                        swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen.persistenceId_string !== "") {
                        if ($scope.properties.lugarexamen === "En un estado") {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen === null) {
                                swal("Lugar de examen!", "Debe seleccionar una ciudad donde realizara el examen!", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamenPais === null) {
                                swal("Lugar de examen!", "Debe seleccionar una ciudad donde realizara el examen!", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else {
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.catPaisExamen = null;
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoExamen = null;
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                $scope.properties.selectedIndex++;
                            }
                        }

                    } else {
                        swal("Lugar de examen!", "Debe seleccionar un lugar donde realizara el examen!", "warning");
                    }
                }
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPeriodo === null) {
                swal("Periodo!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen === null) {
                swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen === null) {
                        swal("Lugar de examen!", "Debe seleccionar una ciudad donde realizara el examen!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamenPais === null) {
                        swal("Lugar de examen!", "Debe seleccionar una ciudad donde realizara el examen!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else {
                    $scope.properties.formInput.catSolicitudDeAdmisionInput.catPaisExamen = null;
                    $scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoExamen = null;
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.selectedIndex++;
                    }
                }

            } else {
                swal("Lugar de examen!", "Debe seleccionar un lugar donde realizara el examen!", "warning");
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
            }
            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.primerNombre === "") {
                swal("Nombre!", "Debe ingresar su primer nombre!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.apellidoPaterno === "") {
                swal("Apellido paterno!", "Debe ingresar su apellido paterno!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.apellidoMaterno === "") {
                swal("Apellido materno!", "Debe ingresar su apellido materno!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.correoElectronico === "") {
                swal("Correo electronico!", "Debe ingresar su correo electronico!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catGestionEscolar === null) {
                swal("Licenciatura!", "Debe seleccionar una licenciatura!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catGestionEscolar.propedeutico) {
                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPropedeutico === null) {
                    swal("Examen propedéutico!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
                }else{
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen === null) {
                swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.avisoPrivacidad === false) {
                swal("Aviso de privacidad!", "Debe aceptar el aviso de privacidad!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen === null) {
                        swal("Lugar de examen!", "Debe seleccionar un estado y una ciudad donde realizara el examen!", "warning");
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
                        swal("Lugar de examen!", "Debe seleccionar un pais y una ciudad donde realizara el examen!", "warning");
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
                swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.avisoPrivacidad === false) {
                swal("Aviso de privacidad!", "Debe aceptar el aviso de privacidad!", "warning");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen === null) {
                        swal("Lugar de examen!", "Debe seleccionar un estado y una ciudad donde realizara el examen!", "warning");
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
                        swal("Lugar de examen!", "Debe seleccionar un pais y una ciudad donde realizara el examen!", "warning");
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
                $scope.properties.selectedIndex--;
            }
            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.fechaNacimiento === undefined) {
                swal("Fecha de nacimiento!", "Debe agergar su fecha de nacimiento!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catNacionalidad === null) {
                swal("Nacionalidad!", "Debe seleccionar su nacionalidad!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catReligion === null) {
                swal("Religión!", "Debe seleccionar su religón!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.curp === "") {
                swal("CURP!", "Debe agregar su CURP!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoCivil === null){
                swal("Estado civil!", "Debe seleccionar su estado civil!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catSexo.persistenceId_string === "") {
                swal("Sexo!", "Debe seleccionar su sexo!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPresentasteEnOtroCampus === null) {
                swal("Presento examen en otro campus!", "Debe seleccionar si ha realizado la solicitud en otro campus!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPresentasteEnOtroCampus.descripcion === "Si") {
                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catCampusPresentadoSolicitud.length === 0) {
                    swal("Campus presentado!", "Debe seleccionar el/los campus donde ha presentado su solicitud!", "warning");
                    $scope.faltacampo = true;
                }
            }
            if (!$scope.faltacampo) {
                if ($scope.properties.fotopasaporte === undefined || JSON.stringify($scope.properties.fotopasaporte) == '{}') {
                    swal("Fotografía!", "Debe agregar una fotografía!", "warning");
                } else if ($scope.properties.actanacimiento === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                    swal("Acta de nacimiento!", "Debe agregar su acta de nacimiento!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.calle === "") {
                    swal("Calle!", "Debe agregar la calle!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.codigoPostal === "") {
                    swal("Código postal!", "Debe agregar el código postal!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPais === null) {
                    swal("País!", "Debe seleccionar el país!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catEstado === null) {
                    swal("Estado!", "Debe seleccionar el estado!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudad === "") {
                    swal("Ciudad!", "Debe agregar una ciudad!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.calle2 === "") {
                    swal("Entre calles!", "Debe agregar entre que calles se encuentra su domicilio!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.numExterior === "") {
                    swal("Número!", "Debe agregar el número de su domicilio!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.colonia === "") {
                    swal("Colonia!", "Debe agregar la colonia!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.telefono === "") {
                    swal("Teléfono!", "Debe el agregar el teléfono!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.otroTelefonoContacto === "") {
                    swal("Otro teléfono de contacto!", "Debe agregar otro teléfono de contacto!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catBachilleratos === null) {
                    swal("Preparatoria!", "Debe seleccionar una preparatoria en caso de no encontrar la suya seleccionar la opción otro!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catBachilleratos.descripcion === "Otro") {
                    if ($scope.properties.datosPreparatoria.nombreBachillerato === "") {
                        swal("Preparatoria!", "Debe agregar el nombre de su preparatoria!", "warning");
                    } else if ($scope.properties.datosPreparatoria.paisBachillerato === undefined) {
                        swal("País preparatoria!", "Debe agregar el país de su preparatoria!", "warning");
                    } else if ($scope.properties.datosPreparatoria.estadoBachillerato === undefined) {
                        swal("Estado preparatoria!", "Debe agregar el estado de su preparatoria!", "warning");
                    } else if ($scope.properties.datosPreparatoria.ciudadBachillerato === undefined) {
                        swal("Ciudad preparatoria!", "Debe agregar la ciudad de su preparatoria!", "warning");
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.promedioGeneral === "") {
                        swal("Promedio!", "Debe agregar el promedio que obtuvo en su preparatoria!", "warning");
                    } else if ($scope.properties.kardex === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                        swal("Contancia de calificaciones!", "Debe agregar la constancia de calificaciones de su preparatoria!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.promedioGeneral = $scope.properties.formInput.catSolicitudDeAdmisionInput.promedioGeneral + "";
                            $scope.properties.formInput.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                            $scope.properties.formInput.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                            $scope.properties.formInput.constanciaDocumentInput.push($scope.properties.kardex);
                            if ($scope.properties.tieneDescuento === true) {
                                if ($scope.properties.descuento !== undefined) {
                                    $scope.properties.formInput.descuentoDocumentInput.push($scope.properties.descuento);
                                    $scope.properties.selectedIndex++;
                                } else {
                                    swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                                }
                            } else {
                                $scope.properties.selectedIndex++;
                            }
                        }
                    }
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.promedioGeneral === "") {
                    swal("Promedio!", "Debe agregar el promedio que obtuvo en su preparatoria!", "warning");
                } else if ($scope.properties.kardex === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                    swal("Contancia de calificaciones!", "Debe agregar la constancia de calificaciones de su preparatoria!", "warning");
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.formInput.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                        $scope.properties.formInput.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                        $scope.properties.formInput.constanciaDocumentInput.push($scope.properties.kardex);
                        if ($scope.properties.tieneDescuento === true) {
                            if ($scope.properties.descuento !== undefined) {
                                $scope.properties.formInput.descuentoDocumentInput.push($scope.properties.descuento);
                                $scope.properties.selectedIndex++;
                            } else {
                                swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                            }
                        } else {
                            $scope.properties.selectedIndex++;
                        }
                    }
                }
            }
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }
            $scope.properties.formInput.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                $scope.properties.formInput.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                $scope.properties.formInput.constanciaDocumentInput.push($scope.properties.kardex);*/
            /* if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                 $scope.properties.selectedIndex--;
             } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                  $scope.properties.formInput.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                     $scope.properties.formInput.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                     $scope.properties.formInput.constanciaDocumentInput.push($scope.properties.kardex);
                 $scope.properties.selectedIndex++;
             }*/
        } else if ($scope.properties.selectedIndex === 3) {
            console.log("validar 3");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            }
            if ($scope.properties.formInput.tutorInput.length === 0) {
                swal("Tutor!", "Debe agregar al menos un tutor!", "warning");
            } else if ($scope.properties.formInput.padreInput.desconozcoDatosPadres) {
                //validar madre
                if ($scope.properties.formInput.madreInput.desconozcoDatosPadres) {
                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else if ($scope.properties.formInput.madreInput.catTitulo === 0 || $scope.properties.formInput.madreInput.catTitulo === null) {
                    swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                } else if ($scope.properties.formInput.madreInput.nombre === "" || $scope.properties.formInput.madreInput.nombre === undefined) {
                    swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                } else if ($scope.properties.formInput.madreInput.apellidos === "" || $scope.properties.formInput.madreInput.apellidos === undefined) {
                    swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                } else if ($scope.properties.formInput.madreInput.vive === 0 || $scope.properties.formInput.madreInput.vive === null) {
                    swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.formInput.madreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.madreInput.catEgresoAnahuac === null) {
                        swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.formInput.madreInput.catCampusEgreso === 0 || $scope.properties.formInput.madreInput.catCampusEgreso === null) {
                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                        } else {
                            if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                    swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                    swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                } else {
                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                } else {
                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                            swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                            } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                            } else {
                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                            } else {
                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.selectedIndex++;
                    }
                }




            } else if ($scope.properties.formInput.padreInput.catTitulo === 0 || $scope.properties.formInput.padreInput.catTitulo === null) {
                swal("Título!", "Debe seleccionar el título para identificar al padre!", "warning");
            } else if ($scope.properties.formInput.padreInput.nombre === "" || $scope.properties.formInput.padreInput.nombre === undefined) {
                swal("Nombre del padre!", "Debe agregar nombre del padre!", "warning");
            } else if ($scope.properties.formInput.padreInput.apellidos === "" || $scope.properties.formInput.padreInput.apellidos === undefined) {
                swal("Apellidos del padre!", "Debe agregar los apellidos del padre!", "warning");
            } else if ($scope.properties.formInput.padreInput.vive === 0 || $scope.properties.formInput.padreInput.vive === null) {
                swal("Padre vive!", "Debe seleccionar si el padre vive!", "warning");
            } else if ($scope.properties.datosPadres.padrevive) {
                if ($scope.properties.formInput.padreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.padreInput.catEgresoAnahuac === null) {
                    swal("Egreso Anahuac!", "Debe seleccionar si su padre egreso de la universidad Anahuac!", "warning");
                } else if ($scope.properties.datosPadres.padreegresoanahuac) {
                    if ($scope.properties.formInput.padreInput.catCampusEgreso === 0 || $scope.properties.formInput.padreInput.catCampusEgreso === null) {
                        swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su padre!", "warning");
                    } else {
                        if ($scope.properties.formInput.padreInput.catTrabaja === 0 || $scope.properties.formInput.padreInput.catTrabaja === null) {
                            swal("Trabaja!", "Debe seleccionar si su padre trabaja!", "warning");
                        } else if ($scope.properties.datosPadres.padretrabaja) {
                            if ($scope.properties.formInput.padreInput.empresaTrabaja === "" || $scope.properties.formInput.padreInput.empresaTrabaja === undefined) {
                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su padre trabaja!", "warning");
                            } else if ($scope.properties.formInput.padreInput.puesto === "" || $scope.properties.formInput.padreInput.puesto === undefined) {
                                swal("Puesto!", "Debe agregar el puesto de trabajo del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.correoElectronico === "" || $scope.properties.formInput.padreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.catEscolaridad === 0 || $scope.properties.formInput.padreInput.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.catPais === 0 || $scope.properties.formInput.padreInput.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.catEstado === 0 || $scope.properties.formInput.padreInput.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.calle === "" || $scope.properties.formInput.padreInput.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.codigoPostal === "" || $scope.properties.formInput.padreInput.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.numeroExterior === "" || $scope.properties.formInput.padreInput.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.ciudad === "" || $scope.properties.formInput.padreInput.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.colonia === "" || $scope.properties.formInput.padreInput.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.telefono === "" || $scope.properties.formInput.padreInput.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono del padre!", "warning");
                            } else {
                                ///Validar madre 1
                                if ($scope.properties.formInput.madreInput.desconozcoDatosPadres) {
                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                } else if ($scope.properties.formInput.madreInput.catTitulo === 0 || $scope.properties.formInput.madreInput.catTitulo === null) {
                                    swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.nombre === "" || $scope.properties.formInput.madreInput.nombre === undefined) {
                                    swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.apellidos === "" || $scope.properties.formInput.madreInput.apellidos === undefined) {
                                    swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.vive === 0 || $scope.properties.formInput.madreInput.vive === null) {
                                    swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.formInput.madreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.madreInput.catEgresoAnahuac === null) {
                                        swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.formInput.madreInput.catCampusEgreso === 0 || $scope.properties.formInput.madreInput.catCampusEgreso === null) {
                                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                                        } else {
                                            if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                                swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                                    swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                                    swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                                } else {
                                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                                } else {
                                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                            swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                                swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                            } else {
                                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                            } else {
                                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.formInput.padreInput.correoElectronico === "" || $scope.properties.formInput.padreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.catEscolaridad === 0 || $scope.properties.formInput.padreInput.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.catPais === 0 || $scope.properties.formInput.padreInput.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.catEstado === 0 || $scope.properties.formInput.padreInput.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.calle === "" || $scope.properties.formInput.padreInput.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.codigoPostal === "" || $scope.properties.formInput.padreInput.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.numeroExterior === "" || $scope.properties.formInput.padreInput.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.ciudad === "" || $scope.properties.formInput.padreInput.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.colonia === "" || $scope.properties.formInput.padreInput.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.telefono === "" || $scope.properties.formInput.padreInput.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono del padre!", "warning");
                            } else {
                                //VALIDAR MADRE 2
                                if ($scope.properties.formInput.madreInput.desconozcoDatosPadres) {
                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                } else if ($scope.properties.formInput.madreInput.catTitulo === 0 || $scope.properties.formInput.madreInput.catTitulo === null) {
                                    swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.nombre === "" || $scope.properties.formInput.madreInput.nombre === undefined) {
                                    swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.apellidos === "" || $scope.properties.formInput.madreInput.apellidos === undefined) {
                                    swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.vive === 0 || $scope.properties.formInput.madreInput.vive === null) {
                                    swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.formInput.madreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.madreInput.catEgresoAnahuac === null) {
                                        swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.formInput.madreInput.catCampusEgreso === 0 || $scope.properties.formInput.madreInput.catCampusEgreso === null) {
                                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                                        } else {
                                            if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                                swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                                    swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                                    swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                                } else {
                                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                                } else {
                                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                            swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                                swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                            } else {
                                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                            } else {
                                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        }
                    }
                } else if ($scope.properties.formInput.padreInput.catTrabaja === 0 || $scope.properties.formInput.padreInput.catTrabaja === null) {
                    swal("Trabaja!", "Debe seleccionar si su padre trabaja!", "warning");
                } else if ($scope.properties.datosPadres.padretrabaja) {
                    if ($scope.properties.formInput.padreInput.empresaTrabaja === "" || $scope.properties.formInput.padreInput.empresaTrabaja === undefined) {
                        swal("Empresa!", "Debe agregar el nombre de la empresa donde su padre trabaja!", "warning");
                    } else if ($scope.properties.formInput.padreInput.puesto === "" || $scope.properties.formInput.padreInput.puesto === undefined) {
                        swal("Puesto!", "Debe agregar el puesto de trabajo del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.correoElectronico === "" || $scope.properties.formInput.padreInput.correoElectronico === undefined) {
                        swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.catEscolaridad === 0 || $scope.properties.formInput.padreInput.catEscolaridad === null) {
                        swal("Escolaridad!", "Debe seleccionar la escolaridad del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.catPais === 0 || $scope.properties.formInput.padreInput.catPais === null) {
                        swal("País!", "Debe agregar el país del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.catEstado === 0 || $scope.properties.formInput.padreInput.catEstado === null) {
                        swal("Estado!", "Debe agregar el estado del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.calle === "" || $scope.properties.formInput.padreInput.calle === undefined) {
                        swal("Calle!", "Debe agregar la calle del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.codigoPostal === "" || $scope.properties.formInput.padreInput.codigoPostal === undefined) {
                        swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.numeroExterior === "" || $scope.properties.formInput.padreInput.numeroExterior === undefined) {
                        swal("Número exterior!", "Debe agregar el número exterior del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.ciudad === "" || $scope.properties.formInput.padreInput.ciudad === undefined) {
                        swal("Ciudad!", "Debe agregar la calle del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.colonia === "" || $scope.properties.formInput.padreInput.colonia === undefined) {
                        swal("Colonia!", "Debe agregar la colonia del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.telefono === "" || $scope.properties.formInput.padreInput.telefono === undefined) {
                        swal("Teléfono!", "Debe agregar el teléfono del padre!", "warning");
                    } else {
                        ///Validar madre  3
                        if ($scope.properties.formInput.madreInput.desconozcoDatosPadres) {
                            if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else if ($scope.properties.formInput.madreInput.catTitulo === 0 || $scope.properties.formInput.madreInput.catTitulo === null) {
                            swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                        } else if ($scope.properties.formInput.madreInput.nombre === "" || $scope.properties.formInput.madreInput.nombre === undefined) {
                            swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                        } else if ($scope.properties.formInput.madreInput.apellidos === "" || $scope.properties.formInput.madreInput.apellidos === undefined) {
                            swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                        } else if ($scope.properties.formInput.madreInput.vive === 0 || $scope.properties.formInput.madreInput.vive === null) {
                            swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.formInput.madreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.madreInput.catEgresoAnahuac === null) {
                                swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.formInput.madreInput.catCampusEgreso === 0 || $scope.properties.formInput.madreInput.catCampusEgreso === null) {
                                    swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                                } else {
                                    if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                        swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                            swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                            swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                            swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                            swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                            swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                            swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                            swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                            swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                            swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                            swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                            swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                            swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                        } else {
                                            if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                            swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                            swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                            swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                            swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                            swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                            swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                            swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                            swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                            swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                            swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                        } else {
                                            if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                    swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                        swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                        swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                        swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                        swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                        swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                        swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                        swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                        swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                        swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                        swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                        swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                        swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                    } else {
                                        if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                            swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                        swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                        swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                        swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                        swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                        swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                        swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                        swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                        swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                        swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                        swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                    } else {
                                        if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                            swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                $scope.properties.selectedIndex++;
                            }
                        }
                    }
                } else {
                    if ($scope.properties.formInput.padreInput.correoElectronico === "" || $scope.properties.formInput.padreInput.correoElectronico === undefined) {
                        swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.catEscolaridad === 0 || $scope.properties.formInput.padreInput.catEscolaridad === null) {
                        swal("Escolaridad!", "Debe seleccionar la escolaridad del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.catPais === 0 || $scope.properties.formInput.padreInput.catPais === null) {
                        swal("País!", "Debe agregar el país del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.catEstado === 0 || $scope.properties.formInput.padreInput.catEstado === null) {
                        swal("Estado!", "Debe agregar el estado del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.calle === "" || $scope.properties.formInput.padreInput.calle === undefined) {
                        swal("Calle!", "Debe agregar la calle del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.codigoPostal === "" || $scope.properties.formInput.padreInput.codigoPostal === undefined) {
                        swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.numeroExterior === "" || $scope.properties.formInput.padreInput.numeroExterior === undefined) {
                        swal("Número exterior!", "Debe agregar el número exterior del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.ciudad === "" || $scope.properties.formInput.padreInput.ciudad === undefined) {
                        swal("Ciudad!", "Debe agregar la calle del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.colonia === "" || $scope.properties.formInput.padreInput.colonia === undefined) {
                        swal("Colonia!", "Debe agregar la colonia del domicilio del padre!", "warning");
                    } else if ($scope.properties.formInput.padreInput.telefono === "" || $scope.properties.formInput.padreInput.telefono === undefined) {
                        swal("Teléfono!", "Debe agregar el teléfono del padre!", "warning");
                    } else {
                        //VALIDAR MADRE 4
                        if ($scope.properties.formInput.madreInput.desconozcoDatosPadres) {
                            if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else if ($scope.properties.formInput.madreInput.catTitulo === 0 || $scope.properties.formInput.madreInput.catTitulo === null) {
                            swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                        } else if ($scope.properties.formInput.madreInput.nombre === "" || $scope.properties.formInput.madreInput.nombre === undefined) {
                            swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                        } else if ($scope.properties.formInput.madreInput.apellidos === "" || $scope.properties.formInput.madreInput.apellidos === undefined) {
                            swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                        } else if ($scope.properties.formInput.madreInput.vive === 0 || $scope.properties.formInput.madreInput.vive === null) {
                            swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.formInput.madreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.madreInput.catEgresoAnahuac === null) {
                                swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.formInput.madreInput.catCampusEgreso === 0 || $scope.properties.formInput.madreInput.catCampusEgreso === null) {
                                    swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                                } else {
                                    if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                        swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                            swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                            swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                            swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                            swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                            swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                            swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                            swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                            swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                            swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                            swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                            swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                            swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                        } else {
                                            if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                            swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                            swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                            swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                            swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                            swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                            swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                            swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                            swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                            swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                            swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                        } else {
                                            if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                    swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                        swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                        swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                        swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                        swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                        swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                        swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                        swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                        swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                        swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                        swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                        swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                        swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                    } else {
                                        if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                            swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                        swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                        swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                        swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                        swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                        swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                        swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                        swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                        swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                        swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                        swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                    } else {
                                        if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                            swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                $scope.properties.selectedIndex++;
                            }
                        }
                    }
                }

            } else {
                console.log("esta validando el la linea 373")
                if ($scope.properties.formInput.madreInput.catTitulo === 0 || $scope.properties.formInput.madreInput.catTitulo === null) {
                    swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                } else if ($scope.properties.formInput.madreInput.nombre === "" || $scope.properties.formInput.madreInput.nombre === undefined) {
                    swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                } else if ($scope.properties.formInput.madreInput.apellidos === "" || $scope.properties.formInput.madreInput.apellidos === undefined) {
                    swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                } else if ($scope.properties.formInput.madreInput.vive === 0 || $scope.properties.formInput.madreInput.vive === null) {
                    swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.formInput.madreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.madreInput.catEgresoAnahuac === null) {
                        swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.formInput.madreInput.catCampusEgreso === 0 || $scope.properties.formInput.madreInput.catCampusEgreso === null) {
                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                        } else {
                            if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                                swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                    swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                    swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                } else {
                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                } else {
                                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.formInput.madreInput.catTrabaja === 0 || $scope.properties.formInput.madreInput.catTrabaja === null) {
                            swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.formInput.madreInput.empresaTrabaja === "" || $scope.properties.formInput.madreInput.empresaTrabaja === undefined) {
                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                            } else if ($scope.properties.formInput.madreInput.puesto === "" || $scope.properties.formInput.madreInput.puesto === undefined) {
                                swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                            } else {
                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.catEscolaridad === 0 || $scope.properties.formInput.madreInput.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.catPais === 0 || $scope.properties.formInput.madreInput.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.catEstado === 0 || $scope.properties.formInput.madreInput.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.calle === "" || $scope.properties.formInput.madreInput.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.codigoPostal === "" || $scope.properties.formInput.madreInput.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.numeroExterior === "" || $scope.properties.formInput.madreInput.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.ciudad === "" || $scope.properties.formInput.madreInput.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.colonia === "" || $scope.properties.formInput.madreInput.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                            } else if ($scope.properties.formInput.madreInput.telefono === "" || $scope.properties.formInput.madreInput.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                            } else {
                                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
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
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }
        } else if ($scope.properties.selectedIndex === 5) {
            console.log("validar 4");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
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