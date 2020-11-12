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
            if ($scope.properties.catSolicitudDeAdmision.catCampus.persistenceId_string === "") {
                swal("Campus!", "Debe seleccionar un campus donde cursara tus estudios!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                swal("Licenciatura!", "Debe seleccionar una licenciatura!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                if ($scope.properties.catSolicitudDeAdmision.catPropedeutico === null) {
                    swal("Examen propedéutico!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
                }else if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                    swal("Periodo!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                    swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                    if ($scope.properties.lugarexamen === "En un estado") {
                        if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                            swal("Lugar de examen!", "Debe seleccionar un estado y una ciudad donde realizara el examen!", "warning");
                        } else {
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                $scope.properties.selectedIndex++;
                            }
                        }
                    } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                        if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                            swal("Lugar de examen!", "Debe seleccionar un pais y una ciudad donde realizara el examen!", "warning");
                        } else {
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                $scope.properties.selectedIndex++;
                            }
                        }
                    } else {
                        $scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                        $scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }
                    }

                } else {
                    swal("Lugar de examen!", "Debe seleccionar un lugar donde realizara el examen!", "warning");
                }
            }  else if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                swal("Periodo!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                        swal("Lugar de examen!", "Debe seleccionar un estado y una ciudad donde realizara el examen!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                    if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                        swal("Lugar de examen!", "Debe seleccionar un pais y una ciudad donde realizara el examen!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else {
                    $scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                    $scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
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
            if ($scope.properties.catSolicitudDeAdmision.primerNombre === "") {
                swal("Nombre!", "Debe ingresar su primer nombre!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.apellidoPaterno === "") {
                swal("Apellido paterno!", "Debe ingresar su apellido paterno!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.apellidoMaterno === "") {
                swal("Apellido materno!", "Debe ingresar su apellido materno!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.correoElectronico === "") {
                swal("Correo electronico!", "Debe ingresar su correo electronico!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                swal("Licenciatura!", "Debe seleccionar una licenciatura!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                if ($scope.properties.catSolicitudDeAdmision.catPropedeutico === null) {
                    swal("Examen propedéutico!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
                }else if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                    swal("Periodo!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                    swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                    if ($scope.properties.lugarexamen === "En un estado") {
                        if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                            swal("Lugar de examen!", "Debe seleccionar un estado y una ciudad donde realizara el examen!", "warning");
                        } else if ($scope.properties.catSolicitudDeAdmision.avisoPrivacidad === false) {
                            swal("Aviso de privacidad!", "Debe aceptar el aviso de privacidad!", "warning");
                        } else {
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                $scope.properties.selectedIndex++;
                            }
                        }
                    } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                        if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                            swal("Lugar de examen!", "Debe seleccionar un pais y una ciudad donde realizara el examen!", "warning");
                        } else if ($scope.properties.catSolicitudDeAdmision.avisoPrivacidad === false) {
                            swal("Aviso de privacidad!", "Debe aceptar el aviso de privacidad!", "warning");
                        } else {
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                $scope.properties.selectedIndex++;
                            }
                        }
                    } else {
                        $scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                        $scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }
                    }

                } else {
                    swal("Lugar de examen!", "Debe seleccionar un lugar donde realizara el examen!", "warning");
                }
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string === "") {
                swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.avisoPrivacidad === false) {
                swal("Aviso de privacidad!", "Debe aceptar el aviso de privacidad!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string === "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
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
                    if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
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
                    $scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                    $scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                    /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.selectedIndex++;
                    }*/
                    openModal($scope.properties.modalid);
                }
            } else {
                    $scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                    $scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                    /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.selectedIndex++;
                    }*/
                    openModal($scope.properties.modalid);
                }
        } else if ($scope.properties.selectedIndex === 2) {
            $scope.faltacampo = false;
            console.log("validar 2");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            }
            if ($scope.properties.catSolicitudDeAdmision.fechaNacimiento === undefined) {
                swal("Fecha de nacimiento!", "Debe agergar su fecha de nacimiento!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catNacionalidad === null) {
                swal("Nacionalidad!", "Debe seleccionar su nacionalidad!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catReligion === null) {
                swal("Religión!", "Debe seleccionar su religón!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.curp === "") {
                swal("CURP!", "Debe agregar su CURP!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catSexo.persistenceId_string === "") {
                swal("Sexo!", "Debe seleccionar su sexo!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus === null) {
                swal("Presento examen en otro campus!", "Debe seleccionar si ha realizado la solicitud en otro campus!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion === "Si") {
                if ($scope.properties.catSolicitudDeAdmision.catCampusPresentadoSolicitud.length === 0) {
                    swal("Campus presentado!", "Debe seleccionar el/los campus donde ha presentado su solicitud!", "warning");
                    $scope.faltacampo = true;
                }
            }
            if (!$scope.faltacampo) {
                if ($scope.properties.catSolicitudDeAdmision.calle === "") {
                    swal("Calle!", "Debe agregar la calle!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.codigoPostal === "") {
                    swal("Código postal!", "Debe agregar el código postal!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catPais === null) {
                    swal("País!", "Debe seleccionar el país!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catEstado === null) {
                    swal("Estado!", "Debe seleccionar el estado!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.ciudad === "") {
                    swal("Ciudad!", "Debe agregar una ciudad!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.calle2 === "") {
                    swal("Entre calles!", "Debe agregar entre que calles se encuentra su domicilio!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.numExterior === "") {
                    swal("Número!", "Debe agregar el número de su domicilio!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.colonia === "") {
                    swal("Colonia!", "Debe agregar la colonia!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.telefono === "") {
                    swal("Teléfono!", "Debe el agregar el teléfono!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.otroTelefonoContacto === "") {
                    swal("Otro teléfono de contacto!", "Debe agregar otro teléfono de contacto!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos === null) {
                    swal("Preparatoria!", "Debe seleccionar una preparatoria en caso de no encontrar la suya seleccionar la opción otro!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion === "Otro") {
                    if ($scope.properties.datosPreparatoria.nombreBachillerato === "") {
                        swal("Preparatoria!", "Debe agregar el nombre de su preparatoria!", "warning");
                    } else if ($scope.properties.datosPreparatoria.paisBachillerato === undefined) {
                        swal("País preparatoria!", "Debe agregar el país de su preparatoria!", "warning");
                    } else if ($scope.properties.datosPreparatoria.estadoBachillerato === undefined) {
                        swal("Estado preparatoria!", "Debe agregar el estado de su preparatoria!", "warning");
                    } else if ($scope.properties.datosPreparatoria.ciudadBachillerato === undefined) {
                        swal("Ciudad preparatoria!", "Debe agregar la ciudad de su preparatoria!", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "") {
                        swal("Promedio!", "Debe agregar el promedio que obtuvo en su preparatoria!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            if($scope.properties.fotopasaporte !== undefined){
                                $scope.properties.fotopasaportearchivo["newValue"] = $scope.properties.fotopasaporte;
                                console.log($scope.properties.fotopasaportearchivo);
                            }
                            if($scope.properties.actanacimiento !== undefined){
                                $scope.properties.actanacimientoarchivo["newValue"] = $scope.properties.actanacimiento;
                            }
                            if($scope.properties.kardex !== undefined){
                                $scope.properties.kardexarchivo["newValue"] = $scope.properties.kardex;
                            }
                            if($scope.properties.tieneDescuento === true){
                                if($scope.properties.descuentoarchivo.length === 0){
                                    if($scope.properties.descuento !== undefined){
                                        $scope.properties.descuentoarchivo.push($scope.properties.descuento);
                                        $scope.properties.selectedIndex++;
                                    }else{
                                        swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                                    }
                                    
                                }else{
                                    if($scope.properties.descuento !== undefined){
                                        $scope.properties.descuentoarchivo["newValue"] = $scope.properties.descuento;
                                        $scope.properties.selectedIndex++;
                                    }
                                    else{
                                        //swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                                        $scope.properties.selectedIndex++;
                                    }
                                    
                                }
                            }
                            else{
                                $scope.properties.selectedIndex++;
                            }
                            //$scope.properties.selectedIndex++;
                        }
                    }
                } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "") {
                    swal("Promedio!", "Debe agregar el promedio que obtuvo en su preparatoria!", "warning");
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        if($scope.properties.fotopasaporte !== undefined){
                                $scope.properties.fotopasaportearchivo["newValue"] = $scope.properties.fotopasaporte;
                                console.log($scope.properties.fotopasaportearchivo);
                            }
                            if($scope.properties.actanacimiento !== undefined){
                                $scope.properties.actanacimientoarchivo["newValue"] = $scope.properties.actanacimiento;
                            }
                            if($scope.properties.kardex !== undefined){
                                $scope.properties.kardexarchivo["newValue"] = $scope.properties.kardex;
                            }
                            if($scope.properties.tieneDescuento === true){
                                if($scope.properties.descuentoarchivo.length === 0){
                                    if($scope.properties.descuento !== undefined){
                                        $scope.properties.descuentoarchivo.push($scope.properties.descuento);
                                        $scope.properties.selectedIndex++;
                                    }else{
                                        swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                                    }
                                    
                                }else{
                                    if($scope.properties.descuento !== undefined){
                                        $scope.properties.descuentoarchivo["newValue"] = $scope.properties.descuento;
                                        $scope.properties.selectedIndex++;
                                    }
                                    else{
                                        //swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                                        $scope.properties.selectedIndex++;
                                    }
                                    
                                }
                            }
                            else{
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
            if ($scope.properties.tutorbdm.length === 0) {
                swal("Tutor!", "Debe agregar al menos un tutor!", "warning");
            } else if ($scope.properties.padre.desconozcoDatosPadres) {
                //validar madre
                if ($scope.properties.madre.desconozcoDatosPadres) {
                    if ($scope.properties.contactoEmergencia.length === 0) {
                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else if ($scope.properties.madre.catTitulo === 0 || $scope.properties.madre.catTitulo === null) {
                    swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                } else if ($scope.properties.madre.nombre === "" || $scope.properties.madre.nombre === undefined) {
                    swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                } else if ($scope.properties.madre.apellidos === "" || $scope.properties.madre.apellidos === undefined) {
                    swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                } else if ($scope.properties.madre.vive === 0 || $scope.properties.madre.vive === null) {
                    swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.madre.catEgresoAnahuac === 0 || $scope.properties.madre.catEgresoAnahuac === null) {
                        swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.madre.catCampusEgreso === 0 || $scope.properties.madre.catCampusEgreso === null) {
                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                        } else {
                            if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                                swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                    swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                    swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                } else {
                                    if ($scope.properties.contactoEmergencia.length === 0) {
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
                                if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                } else {
                                    if ($scope.properties.contactoEmergencia.length === 0) {
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
                        if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                            swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                            } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                            } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                            } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                            } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                            } else {
                                if ($scope.properties.contactoEmergencia.length === 0) {
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
                            if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                            } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                            } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                            } else {
                                if ($scope.properties.contactoEmergencia.length === 0) {
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




            } else if ($scope.properties.padre.catTitulo === 0 || $scope.properties.padre.catTitulo === null) {
                swal("Título!", "Debe seleccionar el título para identificar al padre!", "warning");
            } else if ($scope.properties.padre.nombre === "" || $scope.properties.padre.nombre === undefined) {
                swal("Nombre del padre!", "Debe agregar nombre del padre!", "warning");
            } else if ($scope.properties.padre.apellidos === "" || $scope.properties.padre.apellidos === undefined) {
                swal("Apellidos del padre!", "Debe agregar los apellidos del padre!", "warning");
            } else if ($scope.properties.padre.vive === 0 || $scope.properties.padre.vive === null) {
                swal("Padre vive!", "Debe seleccionar si el padre vive!", "warning");
            } else if ($scope.properties.datosPadres.padrevive) {
                if ($scope.properties.padre.catEgresoAnahuac === 0 || $scope.properties.padre.catEgresoAnahuac === null) {
                    swal("Egreso Anahuac!", "Debe seleccionar si su padre egreso de la universidad Anahuac!", "warning");
                } else if ($scope.properties.datosPadres.padreegresoanahuac) {
                    if ($scope.properties.padre.catCampusEgreso === 0 || $scope.properties.padre.catCampusEgreso === null) {
                        swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su padre!", "warning");
                    } else {
                        if ($scope.properties.padre.catTrabaja === 0 || $scope.properties.padre.catTrabaja === null) {
                            swal("Trabaja!", "Debe seleccionar si su padre trabaja!", "warning");
                        } else if ($scope.properties.datosPadres.padretrabaja) {
                            if ($scope.properties.padre.empresaTrabaja === "" || $scope.properties.padre.empresaTrabaja === undefined) {
                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su padre trabaja!", "warning");
                            } else if ($scope.properties.padre.puesto === "" || $scope.properties.padre.puesto === undefined) {
                                swal("Puesto!", "Debe agregar el puesto de trabajo del padre!", "warning");
                            } else if ($scope.properties.padre.correoElectronico === "" || $scope.properties.padre.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                            } else if ($scope.properties.padre.catEscolaridad === 0 || $scope.properties.padre.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad del padre!", "warning");
                            } else if ($scope.properties.padre.catPais === 0 || $scope.properties.padre.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.catEstado === 0 || $scope.properties.padre.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.calle === "" || $scope.properties.padre.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.codigoPostal === "" || $scope.properties.padre.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.numeroExterior === "" || $scope.properties.padre.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.ciudad === "" || $scope.properties.padre.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.colonia === "" || $scope.properties.padre.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.telefono === "" || $scope.properties.padre.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono del padre!", "warning");
                            } else {
                                ///Validar madre 1
                                if ($scope.properties.madre.desconozcoDatosPadres) {
                                    if ($scope.properties.contactoEmergencia.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                } else if ($scope.properties.madre.catTitulo === 0 || $scope.properties.madre.catTitulo === null) {
                                    swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                                } else if ($scope.properties.madre.nombre === "" || $scope.properties.madre.nombre === undefined) {
                                    swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                                } else if ($scope.properties.madre.apellidos === "" || $scope.properties.madre.apellidos === undefined) {
                                    swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                                } else if ($scope.properties.madre.vive === 0 || $scope.properties.madre.vive === null) {
                                    swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.madre.catEgresoAnahuac === 0 || $scope.properties.madre.catEgresoAnahuac === null) {
                                        swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.madre.catCampusEgreso === 0 || $scope.properties.madre.catCampusEgreso === null) {
                                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                                        } else {
                                            if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                                                swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                                    swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                                } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                                    swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                                } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                                } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                                } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergencia.length === 0) {
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
                                                if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                                } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                                } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergencia.length === 0) {
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
                                        if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                                            swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                            } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                                swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                            } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                            } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                            } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergencia.length === 0) {
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
                                            if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                            } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                            } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergencia.length === 0) {
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
                            if ($scope.properties.padre.correoElectronico === "" || $scope.properties.padre.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                            } else if ($scope.properties.padre.catEscolaridad === 0 || $scope.properties.padre.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad del padre!", "warning");
                            } else if ($scope.properties.padre.catPais === 0 || $scope.properties.padre.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.catEstado === 0 || $scope.properties.padre.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.calle === "" || $scope.properties.padre.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.codigoPostal === "" || $scope.properties.padre.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.numeroExterior === "" || $scope.properties.padre.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.ciudad === "" || $scope.properties.padre.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.colonia === "" || $scope.properties.padre.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio del padre!", "warning");
                            } else if ($scope.properties.padre.telefono === "" || $scope.properties.padre.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono del padre!", "warning");
                            } else {
                                //VALIDAR MADRE 2
                                if ($scope.properties.madre.desconozcoDatosPadres) {
                                    if ($scope.properties.contactoEmergencia.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                } else if ($scope.properties.madre.catTitulo === 0 || $scope.properties.madre.catTitulo === null) {
                                    swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                                } else if ($scope.properties.madre.nombre === "" || $scope.properties.madre.nombre === undefined) {
                                    swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                                } else if ($scope.properties.madre.apellidos === "" || $scope.properties.madre.apellidos === undefined) {
                                    swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                                } else if ($scope.properties.madre.vive === 0 || $scope.properties.madre.vive === null) {
                                    swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.madre.catEgresoAnahuac === 0 || $scope.properties.madre.catEgresoAnahuac === null) {
                                        swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.madre.catCampusEgreso === 0 || $scope.properties.madre.catCampusEgreso === null) {
                                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                                        } else {
                                            if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                                                swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                                    swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                                } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                                    swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                                } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                                } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                                } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergencia.length === 0) {
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
                                                if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                                } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                                } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergencia.length === 0) {
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
                                        if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                                            swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                            } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                                swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                            } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                            } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                            } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergencia.length === 0) {
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
                                            if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                            } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                            } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergencia.length === 0) {
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
                } else if ($scope.properties.padre.catTrabaja === 0 || $scope.properties.padre.catTrabaja === null) {
                    swal("Trabaja!", "Debe seleccionar si su padre trabaja!", "warning");
                } else if ($scope.properties.datosPadres.padretrabaja) {
                    if ($scope.properties.padre.empresaTrabaja === "" || $scope.properties.padre.empresaTrabaja === undefined) {
                        swal("Empresa!", "Debe agregar el nombre de la empresa donde su padre trabaja!", "warning");
                    } else if ($scope.properties.padre.puesto === "" || $scope.properties.padre.puesto === undefined) {
                        swal("Puesto!", "Debe agregar el puesto de trabajo del padre!", "warning");
                    } else if ($scope.properties.padre.correoElectronico === "" || $scope.properties.padre.correoElectronico === undefined) {
                        swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                    } else if ($scope.properties.padre.catEscolaridad === 0 || $scope.properties.padre.catEscolaridad === null) {
                        swal("Escolaridad!", "Debe seleccionar la escolaridad del padre!", "warning");
                    } else if ($scope.properties.padre.catPais === 0 || $scope.properties.padre.catPais === null) {
                        swal("País!", "Debe agregar el país del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.catEstado === 0 || $scope.properties.padre.catEstado === null) {
                        swal("Estado!", "Debe agregar el estado del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.calle === "" || $scope.properties.padre.calle === undefined) {
                        swal("Calle!", "Debe agregar la calle del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.codigoPostal === "" || $scope.properties.padre.codigoPostal === undefined) {
                        swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.numeroExterior === "" || $scope.properties.padre.numeroExterior === undefined) {
                        swal("Número exterior!", "Debe agregar el número exterior del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.ciudad === "" || $scope.properties.padre.ciudad === undefined) {
                        swal("Ciudad!", "Debe agregar la calle del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.colonia === "" || $scope.properties.padre.colonia === undefined) {
                        swal("Colonia!", "Debe agregar la colonia del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.telefono === "" || $scope.properties.padre.telefono === undefined) {
                        swal("Teléfono!", "Debe agregar el teléfono del padre!", "warning");
                    } else {
                        ///Validar madre  3
                        if ($scope.properties.madre.desconozcoDatosPadres) {
                            if ($scope.properties.contactoEmergencia.length === 0) {
                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else if ($scope.properties.madre.catTitulo === 0 || $scope.properties.madre.catTitulo === null) {
                            swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                        } else if ($scope.properties.madre.nombre === "" || $scope.properties.madre.nombre === undefined) {
                            swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                        } else if ($scope.properties.madre.apellidos === "" || $scope.properties.madre.apellidos === undefined) {
                            swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                        } else if ($scope.properties.madre.vive === 0 || $scope.properties.madre.vive === null) {
                            swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.madre.catEgresoAnahuac === 0 || $scope.properties.madre.catEgresoAnahuac === null) {
                                swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.madre.catCampusEgreso === 0 || $scope.properties.madre.catCampusEgreso === null) {
                                    swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                                } else {
                                    if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                                        swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                            swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                        } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                            swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                        } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                            swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                        } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                            swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                        } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                            swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                            swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                            swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                            swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                            swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                            swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                            swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                            swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergencia.length === 0) {
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
                                        if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                            swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                        } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                            swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                        } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                            swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                            swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                            swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                            swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                            swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                            swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                            swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                            swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergencia.length === 0) {
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
                                if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                                    swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                        swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                    } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                        swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                    } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                        swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                    } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                        swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                    } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                        swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                        swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                        swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                        swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                        swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                        swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                        swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                        swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergencia.length === 0) {
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
                                    if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                        swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                    } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                        swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                    } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                        swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                        swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                        swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                        swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                        swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                        swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                        swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                        swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergencia.length === 0) {
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
                    if ($scope.properties.padre.correoElectronico === "" || $scope.properties.padre.correoElectronico === undefined) {
                        swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                    } else if ($scope.properties.padre.catEscolaridad === 0 || $scope.properties.padre.catEscolaridad === null) {
                        swal("Escolaridad!", "Debe seleccionar la escolaridad del padre!", "warning");
                    } else if ($scope.properties.padre.catPais === 0 || $scope.properties.padre.catPais === null) {
                        swal("País!", "Debe agregar el país del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.catEstado === 0 || $scope.properties.padre.catEstado === null) {
                        swal("Estado!", "Debe agregar el estado del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.calle === "" || $scope.properties.padre.calle === undefined) {
                        swal("Calle!", "Debe agregar la calle del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.codigoPostal === "" || $scope.properties.padre.codigoPostal === undefined) {
                        swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.numeroExterior === "" || $scope.properties.padre.numeroExterior === undefined) {
                        swal("Número exterior!", "Debe agregar el número exterior del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.ciudad === "" || $scope.properties.padre.ciudad === undefined) {
                        swal("Ciudad!", "Debe agregar la calle del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.colonia === "" || $scope.properties.padre.colonia === undefined) {
                        swal("Colonia!", "Debe agregar la colonia del domicilio del padre!", "warning");
                    } else if ($scope.properties.padre.telefono === "" || $scope.properties.padre.telefono === undefined) {
                        swal("Teléfono!", "Debe agregar el teléfono del padre!", "warning");
                    } else {
                        //VALIDAR MADRE 4
                        if ($scope.properties.madre.desconozcoDatosPadres) {
                            if ($scope.properties.contactoEmergencia.length === 0) {
                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else if ($scope.properties.madre.catTitulo === 0 || $scope.properties.madre.catTitulo === null) {
                            swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                        } else if ($scope.properties.madre.nombre === "" || $scope.properties.madre.nombre === undefined) {
                            swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                        } else if ($scope.properties.madre.apellidos === "" || $scope.properties.madre.apellidos === undefined) {
                            swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                        } else if ($scope.properties.madre.vive === 0 || $scope.properties.madre.vive === null) {
                            swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.madre.catEgresoAnahuac === 0 || $scope.properties.madre.catEgresoAnahuac === null) {
                                swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.madre.catCampusEgreso === 0 || $scope.properties.madre.catCampusEgreso === null) {
                                    swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                                } else {
                                    if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                                        swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                            swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                        } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                            swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                        } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                            swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                        } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                            swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                        } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                            swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                            swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                            swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                            swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                            swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                            swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                            swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                            swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergencia.length === 0) {
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
                                        if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                            swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                        } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                            swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                        } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                            swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                            swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                            swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                            swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                            swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                            swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                            swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                            swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergencia.length === 0) {
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
                                if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                                    swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                        swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                    } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                        swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                    } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                        swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                    } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                        swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                    } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                        swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                        swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                        swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                        swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                        swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                        swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                        swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                        swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergencia.length === 0) {
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
                                    if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                        swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                    } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                        swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                    } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                        swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                        swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                        swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                        swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                        swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                        swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                        swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                        swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergencia.length === 0) {
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
                if ($scope.properties.madre.catTitulo === 0 || $scope.properties.madre.catTitulo === null) {
                    swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                } else if ($scope.properties.madre.nombre === "" || $scope.properties.madre.nombre === undefined) {
                    swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                } else if ($scope.properties.madre.apellidos === "" || $scope.properties.madre.apellidos === undefined) {
                    swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                } else if ($scope.properties.madre.vive === 0 || $scope.properties.madre.vive === null) {
                    swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.madre.catEgresoAnahuac === 0 || $scope.properties.madre.catEgresoAnahuac === null) {
                        swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.madre.catCampusEgreso === 0 || $scope.properties.madre.catCampusEgreso === null) {
                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                        } else {
                            if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                                swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                    swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                    swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                } else {
                                    if ($scope.properties.contactoEmergencia.length === 0) {
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
                                if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                } else {
                                    if ($scope.properties.contactoEmergencia.length === 0) {
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
                        if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                            swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                            } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                            } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                            } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                            } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                            } else {
                                if ($scope.properties.contactoEmergencia.length === 0) {
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
                            if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                            } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                            } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                            } else {
                                if ($scope.properties.contactoEmergencia.length === 0) {
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
                    if ($scope.properties.contactoEmergencia.length === 0) {
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