function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    $scope.action = function() {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        var localS = localStorage;
        console.log("boton de atras");
        console.log($scope.properties.tabs);
        
        if($scope.properties.selectedIndex === 2){
            //selectedIndexPersonal
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndexPersonal > 0) {
                    $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
                    $scope.properties.selectedIndexPersonal--;
            }
        }else if($scope.properties.selectedIndex === 3){
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndexFamiliar > 0) {
                    $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
                    $scope.properties.selectedIndexFamiliar--;
            }
            //selectedIndexFamiliar
        }else if($scope.properties.selectedIndex === 4){
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndexRevision > 0) {
                    $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
                    $scope.properties.selectedIndexRevision--;
            }
            //selectedIndexRevision
        }
        
        
        
        if($scope.properties.tabs === "Informacion Personal"){
            if ($scope.properties.selectedIndex === 0) {
                console.log("validar 0");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
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
                    $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }
                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }*/
                
            } else if ($scope.properties.selectedIndex === 1) {
                $scope.faltacampo = false;
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
                        $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.formInput.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                        $scope.properties.formInput.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                        //localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                        localS.setItem("actanacimiento",JSON.stringify($scope.properties.actanacimiento));
                        localS.setItem("fotopasaporte",JSON.stringify($scope.properties.fotopasaporte));
                        if($scope.properties.idExtranjero !== undefined){
                            $scope.properties.formInput.catSolicitudDeAdmisionInput.curp = $scope.properties.idExtranjero;
                        }
                        $scope.properties.selectedIndex++;
                    }
                }
               
            } else if ($scope.properties.selectedIndex === 2) {
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
                    $scope.properties.selectedIndex--;
                }
                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.catBachilleratos === null) {
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
                            $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
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
                        $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
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
        }else if($scope.properties.tabs === "Informacion Familiar"){
            if ($scope.properties.selectedIndex === 0) {
                console.log("validar 0");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }
                
            } else if ($scope.properties.selectedIndex === 1) {

                console.log("validar 1");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }
               
            } else if ($scope.properties.selectedIndex === 2) {
                
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }

            } else if ($scope.properties.selectedIndex === 3) {
                console.log("validar 3");
             
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }
            }
        }else if($scope.properties.tabs === "Revisa Solicitud"){
             if ($scope.properties.selectedIndex === 0) {
                console.log("validar 0");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.selectedIndex++;
                }
                
            } else if ($scope.properties.selectedIndex === 1) {

                console.log("validar 1");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.fotopasaporte = undefined;
                    $scope.properties.actanacimiento = undefined; 
                    $scope.properties.kardex = undefined;
                    $scope.properties.descuento = undefined;
                    $scope.properties.collageBoard = undefined;
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