function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    $scope.action = function() {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        var localS = localStorage;
        console.log("boton de siguiente");
        console.log($scope.properties.tabs);
        if ($scope.properties.tabs === "Informacion Personal") {
            if ($scope.properties.selectedIndex === 1) {
                console.log("validar 1");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                }
                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catPais === null) {
                    swal("País!", "Debe seleccionar el país!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.codigoPostal === "") {
                    swal("Código postal!", "Debe agregar el código postal!", "warning");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.calle === "") {
                    swal("Calle!", "Debe agregar la calle!", "warning");
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
                } else if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }
                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }*/

            } else if ($scope.properties.selectedIndex === 0) {
                $scope.faltacampo = false;
                console.log("validar 0");
                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }*/
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.fechaNacimiento === undefined) {
                    swal("Fecha de nacimiento!", "Debe agergar su fecha de nacimiento!", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catNacionalidad === null) {
                    swal("Nacionalidad!", "Debe seleccionar su nacionalidad!", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catReligion === null) {
                    swal("Religión!", "Debe seleccionar su religón!", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.curp === "" && $scope.properties.formInput.catSolicitudDeAdmisionInput.catNacionalidad.descripcion === "Mexicana") {
                    swal("CURP!", "Debe agregar su CURP!", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.curp.length < 18 && $scope.properties.formInput.catSolicitudDeAdmisionInput.catNacionalidad.descripcion === "Mexicana") {
                    swal("CURP!", "Su CURP debe tener 18 caracteres!", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.telefonoCelular === undefined) {
                    swal("Teléfono celular!", "Debe agregar su numero celular!", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.telefonoCelular.length !== 10 && $scope.properties.formInput.catSolicitudDeAdmisionInput.catNacionalidad.descripcion === "Mexicana") {
                    swal("Teléfono celular!", "Su teléfono celular debe ser de 10 digitos!", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.telefonoCelular.length !== 14 && $scope.properties.formInput.catSolicitudDeAdmisionInput.catNacionalidad.descripcion !== "Mexicana") {
                    swal("Teléfono celular!", "Su teléfono celular debe ser de 14 digitos!", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.telefonoCelular === "") {
                    swal("Teléfono celular!", "Debe agregar su numero celular!", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catEstadoCivil === null) {
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
                    } else if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.formInput.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                        $scope.properties.formInput.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                        localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                        //localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                        localS.setItem("actanacimiento", JSON.stringify($scope.properties.actanacimiento));
                        localS.setItem("fotopasaporte", JSON.stringify($scope.properties.fotopasaporte));
                        if ($scope.properties.idExtranjero !== undefined) {
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                        }
                        $scope.properties.selectedIndex++;
                    }
                }

            } else if ($scope.properties.selectedIndex === 2) {
			debugger;
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
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
                                    localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                                    localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                                    localS.setItem("actanacimiento", JSON.stringify($scope.properties.actanacimiento));
                                    localS.setItem("fotopasaporte", JSON.stringify($scope.properties.fotopasaporte));
                                    localS.setItem("kardex", JSON.stringify($scope.properties.kardex));
                                    localS.setItem("descuento", JSON.stringify($scope.properties.descuento));
                                    localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                    if ($scope.properties.idExtranjero !== undefined) {
                                        $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                    }
                                    $scope.properties.selectedIndex++;
                                } else {
                                    swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                                }
                            } else {
                                localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                                localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                                localS.setItem("actanacimiento", JSON.stringify($scope.properties.actanacimiento));
                                localS.setItem("fotopasaporte", JSON.stringify($scope.properties.fotopasaporte));
                                localS.setItem("kardex", JSON.stringify($scope.properties.kardex));
                                localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                if ($scope.properties.idExtranjero !== undefined) {
                                    $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                }
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
                        $scope.properties.formInput.constanciaDocumentInput.push($scope.properties.kardex);
                        if ($scope.properties.tieneDescuento === true) {
                            if ($scope.properties.descuento !== undefined) {
                                $scope.properties.formInput.descuentoDocumentInput.push($scope.properties.descuento);
                                localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                                localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                                localS.setItem("kardex", JSON.stringify($scope.properties.kardex));
                                localS.setItem("descuento", JSON.stringify($scope.properties.descuento));
                                localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                if ($scope.properties.idExtranjero !== undefined) {
                                    $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                                }
                                $scope.properties.selectedIndex++;
                            } else {
                                swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                            }
                        } else {
                            localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                            localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                            localS.setItem("kardex", JSON.stringify($scope.properties.kardex));
                            localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                            if ($scope.properties.idExtranjero !== undefined) {
                                $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                            }
                            $scope.properties.selectedIndex++;
                        }
                    }
                }
                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }*/

            }
        } else if ($scope.properties.tabs === "Informacion Familiar") {
            if ($scope.properties.selectedIndex === 0) {
                console.log("validar 0");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.formInput.tutorInput.length === 0) {
                    swal("Tutor!", "Debe agregar al menos un tutor!", "warning");
                }else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                    localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                    localS.setItem("isPadretutor", $scope.properties.isPadretutor);
                    localS.setItem("isMadretutor", $scope.properties.isMadretutor);
                    $scope.properties.selectedIndex++;
                }
            } else if ($scope.properties.selectedIndex === 1) {

                console.log("validar 1");
				if ($scope.properties.formInput.padreInput.desconozcoDatosPadres) {
    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
        $scope.properties.selectedIndex--;
    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
        localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
        localS.setItem("selectedIndex", $scope.properties.selectedIndex);
        $scope.properties.selectedIndex++;
    }
} else if ($scope.properties.formInput.padreInput.catTitulo === 0 || $scope.properties.formInput.padreInput.catTitulo === null) {
                    swal("Título!", "Debe seleccionar el título para identificar al padre!", "warning");
                } else if ($scope.properties.formInput.padreInput.nombre === "" || $scope.properties.formInput.padreInput.nombre === undefined) {
                    swal("Nombre del padre!", "Debe agregar nombre del padre!", "warning");
                } else if ($scope.properties.formInput.padreInput.apellidos === "" || $scope.properties.formInput.padreInput.apellidos === undefined) {
                    swal("Apellidos del padre!", "Debe agregar los apellidos del padre!", "warning");
                } else if ($scope.properties.formInput.padreInput.vive === 0 || $scope.properties.formInput.padreInput.vive === null) {
                    swal("padre vive!", "Debe seleccionar si la padre vive!", "warning");
                } else if ($scope.properties.datosPadres.padrevive) {
                    if ($scope.properties.formInput.padreInput.catEgresoAnahuac === 0 || $scope.properties.formInput.padreInput.catEgresoAnahuac === null) {
                        swal("Egreso Anahuac!", "Debe seleccionar si su padre egreso del universidad Anahuac!", "warning");
                    } else if ($scope.properties.datosPadres.padreegresoanahuac) {
                        if ($scope.properties.formInput.padreInput.catCampusEgreso === 0 || $scope.properties.formInput.padreInput.catCampusEgreso === null) {
                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su padre!", "warning");
                        } else {
                            if ($scope.properties.formInput.padreInput.catTrabaja === 0 || $scope.properties.formInput.padreInput.catTrabaja === null) {
                                swal("Trabaja!", "Debe seleccionar si su padre trabaja!", "warning");
                            } else if ($scope.properties.datosPadres.padretrabaja) {
                                if ($scope.properties.formInput.padreInput.empresaTrabaja === "" || $scope.properties.formInput.padreInput.empresaTrabaja === undefined) {
                                    swal("Empresa!", "Debe agregar el nombre del empresa donde su padre trabaja!", "warning");
                                } else if ($scope.properties.formInput.padreInput.puesto === "" || $scope.properties.formInput.padreInput.puesto === undefined) {
                                    swal("Puesto!", "Debe agregar el puesto de trabajo del padre!", "warning");
                                } else if ($scope.properties.formInput.padreInput.correoElectronico === "" || $scope.properties.formInput.padreInput.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                                } else if (!re.test(String($scope.properties.formInput.padreInput.correoElectronico.trim()).toLowerCase())) {
                                    swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
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
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                                        localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            } else {
                                if ($scope.properties.formInput.padreInput.correoElectronico === "" || $scope.properties.formInput.padreInput.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                                } else if (!re.test(String($scope.properties.formInput.padreInput.correoElectronico.trim()).toLowerCase())) {
                                    swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
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
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                                        localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.formInput.padreInput.catTrabaja === 0 || $scope.properties.formInput.padreInput.catTrabaja === null) {
                            swal("Trabaja!", "Debe seleccionar si su padre trabaja!", "warning");
                        } else if ($scope.properties.datosPadres.padretrabaja) {
                            if ($scope.properties.formInput.padreInput.empresaTrabaja === "" || $scope.properties.formInput.padreInput.empresaTrabaja === undefined) {
                                swal("Empresa!", "Debe agregar el nombre del empresa donde su padre trabaja!", "warning");
                            } else if ($scope.properties.formInput.padreInput.puesto === "" || $scope.properties.formInput.padreInput.puesto === undefined) {
                                swal("Puesto!", "Debe agregar el puesto de trabajo del padre!", "warning");
                            } else if ($scope.properties.formInput.padreInput.correoElectronico === "" || $scope.properties.formInput.padreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                            } else if (!re.test(String($scope.properties.formInput.padreInput.correoElectronico.trim()).toLowerCase())) {
                                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
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
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                                    localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else {
                            if ($scope.properties.formInput.padreInput.correoElectronico === "" || $scope.properties.formInput.padreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                            } else if (!re.test(String($scope.properties.formInput.padreInput.correoElectronico.trim()).toLowerCase())) {
                                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
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
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                                    localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                        localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                        $scope.properties.selectedIndex++;
                    }
                }
                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }*/

            } else if ($scope.properties.selectedIndex === 2) {
                console.log("valida a la madre")
                if ($scope.properties.formInput.madreInput.desconozcoDatosPadres) {
    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
        $scope.properties.selectedIndex--;
    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
        localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
        localS.setItem("selectedIndex", $scope.properties.selectedIndex);
        $scope.properties.selectedIndex++;
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
                } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                    swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
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
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                        localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                        $scope.properties.selectedIndex++;
                    }
                }
            } else {
                if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                    swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
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
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                        localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                        $scope.properties.selectedIndex++;
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
            } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
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
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                    localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                    $scope.properties.selectedIndex++;
                }
            }
        } else {
            if ($scope.properties.formInput.madreInput.correoElectronico === "" || $scope.properties.formInput.madreInput.correoElectronico === undefined) {
                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
            } else if (!re.test(String($scope.properties.formInput.madreInput.correoElectronico.trim()).toLowerCase())) {
                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
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
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                    localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                    $scope.properties.selectedIndex++;
                }
            }
        }
    }
} else {
    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
        $scope.properties.selectedIndex--;
    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
        localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
        localS.setItem("selectedIndex", $scope.properties.selectedIndex);
        $scope.properties.selectedIndex++;
    }
}
                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }*/

            } else if ($scope.properties.selectedIndex === 3) {
                console.log("validar 3");
                if ($scope.properties.formInput.contactoEmergenciaInput.length === 0) {
                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        localS.setItem("catSolicitudDeAdmisionInput", JSON.stringify($scope.properties.formInput));
                        localS.setItem("selectedIndex", $scope.properties.selectedIndex);
                        $scope.properties.selectedIndex++;
                    }
                }
                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }*/
            }
        } else if ($scope.properties.tabs === "Revisa Solicitud") {
            if ($scope.properties.selectedIndex === 0) {
                console.log("validar 0");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }

            } else if ($scope.properties.selectedIndex === 1) {

                console.log("validar 1");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }

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