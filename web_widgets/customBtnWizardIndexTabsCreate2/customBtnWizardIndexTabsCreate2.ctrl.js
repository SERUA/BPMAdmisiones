function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI, $filter) {
    var cont = 0;
    $scope.action = function() {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        $scope.properties.disabled = true;
        if ($scope.properties.tabs === "Informacion Personal") {
            if ($scope.properties.selectedIndex === 1) {
                console.log("validar 1");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                }
                if ($scope.properties.catSolicitudDeAdmision.catPais === null) {
                    swal($filter('translate')("¡País!"), $filter('translate')("Debes seleccionar el país"), "warning");
                } else if (($scope.properties.catSolicitudDeAdmision.codigoPostal === "" || $scope.properties.catSolicitudDeAdmision.codigoPostal === null) && $scope.properties.catSolicitudDeAdmision.catPais.descripcion === "México") {
                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catEstado === null && $scope.properties.catSolicitudDeAdmision.catPais.descripcion === "México") {
                    swal($filter('translate')("¡Estado!"), $filter('translate')("Debes seleccionar el estado"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catPais.descripcion !== "México" && ($scope.properties.catSolicitudDeAdmision.estadoExtranjero === "" || $scope.properties.catSolicitudDeAdmision.estadoExtranjero === null)) {
                    swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.ciudad === "" || $scope.properties.catSolicitudDeAdmision.ciudad === null) {
                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar una ciudad"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.delegacionMunicipio === "" || $scope.properties.catSolicitudDeAdmision.delegacionMunicipio === null) {
                    swal($filter('translate')("¡Municipio/Delegación/Poblado!"), $filter('translate')("Debes agregar la Ciudad/Municipio/Delegación/Poblado"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.colonia === "" || $scope.properties.catSolicitudDeAdmision.colonia === null) {
                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.calle === "" || $scope.properties.catSolicitudDeAdmision.calle === null) {
                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.ciudad === "" || $scope.properties.catSolicitudDeAdmision.ciudad === null) {
                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar una ciudad"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.numExterior === "" || $scope.properties.catSolicitudDeAdmision.numExterior === null) {
                    swal($filter('translate')("¡Número!"), $filter('translate')("Debes agregar el número de tu domicilio"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.telefono === "" || $scope.properties.catSolicitudDeAdmision.telefono === null) {
                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes el agregar el teléfono"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.telefono.length !== 10 && $scope.properties.catSolicitudDeAdmision.catPais.descripcion === "México") {
                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Tu teléfono debe ser de 10 dígitos"), "warning");
                    $scope.properties.catSolicitudDeAdmision.telefono = "";
                } else if ($scope.properties.catSolicitudDeAdmision.telefono.length < 10 && $scope.properties.catSolicitudDeAdmision.catPais.descripcion !== "México") {
                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Tu teléfono debe ser mínimo de 10 dígitos a un máximo de 14 dígitos"), "warning");
                    $scope.properties.catSolicitudDeAdmision.telefono = "";
                } else if ($scope.properties.catSolicitudDeAdmision.telefono.length > 14 && $scope.properties.catSolicitudDeAdmision.catPais.descripcion !== "México") {
                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Tu teléfono debe ser mínimo de 10 dígitos a un máximo de 14 dígitos"), "warning");
                    $scope.properties.catSolicitudDeAdmision.telefono = "";
                } else if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    //$scope.properties.selectedIndex++;
                    if ($scope.properties.tutor.length > 0) {
                        for (var i = 0; i < $scope.properties.tutor.length; i++) {
                            if ($scope.properties.tutor[i].viveContigo) {}
                            $scope.properties.tutor[i].calle = $scope.properties.catSolicitudDeAdmision.calle;
                            $scope.properties.tutor[i].codigoPostal = $scope.properties.catSolicitudDeAdmision.codigoPostal;
                            $scope.properties.tutor[i].catPais = $scope.properties.catSolicitudDeAdmision.catPais;
                            $scope.properties.tutor[i].catEstado = $scope.properties.catSolicitudDeAdmision.catEstado;
                            $scope.properties.tutor[i].ciudad = $scope.properties.catSolicitudDeAdmision.ciudad;
                            $scope.properties.tutor[i].numeroExterior = $scope.properties.catSolicitudDeAdmision.numExterior;
                            $scope.properties.tutor[i].numeroInterior = $scope.properties.catSolicitudDeAdmision.numInterior;
                            $scope.properties.tutor[i].colonia = $scope.properties.catSolicitudDeAdmision.colonia;
                            $scope.properties.tutor[i].telefono = $scope.properties.catSolicitudDeAdmision.telefono;
                            $scope.properties.tutor[i].delegacionMunicipio = $scope.properties.catSolicitudDeAdmision.delegacionMunicipio;
                            $scope.properties.tutor[i].estadoExtranjero = $scope.properties.catSolicitudDeAdmision.estadoExtranjero;
                        }
                        if ($scope.properties.padre.viveContigo) {
                            $scope.properties.padre.calle = $scope.properties.tutor[0].calle;
                            $scope.properties.padre.catPais = $scope.properties.tutor[0].catPais;
                            $scope.properties.padre.numeroExterior = $scope.properties.tutor[0].numeroExterior;
                            $scope.properties.padre.numeroInterior = $scope.properties.tutor[0].numeroInterior;
                            $scope.properties.padre.catEstado = $scope.properties.tutor[0].catEstado;
                            $scope.properties.padre.ciudad = $scope.properties.tutor[0].ciudad;
                            $scope.properties.padre.colonia = $scope.properties.tutor[0].colonia;
                            $scope.properties.padre.telefono = $scope.properties.tutor[0].telefono;
                            $scope.properties.padre.codigoPostal = $scope.properties.tutor[0].codigoPostal;
                            $scope.properties.padre.delegacionMunicipio = $scope.properties.tutor[0].delegacionMunicipio;
                            $scope.properties.padre.estadoExtranjero = $scope.properties.tutor[0].estadoExtranjero;
                        }
                        if ($scope.properties.madre.viveContigo) {
                            $scope.properties.madre.calle = $scope.properties.tutor[0].calle;
                            $scope.properties.madre.catPais = $scope.properties.tutor[0].catPais;
                            $scope.properties.madre.numeroExterior = $scope.properties.tutor[0].numeroExterior;
                            $scope.properties.madre.numeroInterior = $scope.properties.tutor[0].numeroInterior;
                            $scope.properties.madre.catEstado = $scope.properties.tutor[0].catEstado;
                            $scope.properties.madre.ciudad = $scope.properties.tutor[0].ciudad;
                            $scope.properties.madre.colonia = $scope.properties.tutor[0].colonia;
                            $scope.properties.madre.telefono = $scope.properties.tutor[0].telefono;
                            $scope.properties.madre.codigoPostal = $scope.properties.tutor[0].codigoPostal;
                            $scope.properties.madre.delegacionMunicipio = $scope.properties.tutor[0].delegacionMunicipio;
                            $scope.properties.madre.estadoExtranjero = $scope.properties.tutor[0].estadoExtranjero;
                        }
                        $scope.assignTask();
                    } else {
                        if ($scope.properties.padre.viveContigo) {
                            $scope.properties.padre.calle = $scope.properties.tutor.calle;
                            $scope.properties.padre.catPais = $scope.properties.tutor.catPais;
                            $scope.properties.padre.numeroExterior = $scope.properties.tutor.numeroExterior;
                            $scope.properties.padre.numeroInterior = $scope.properties.tutor.numeroInterior;
                            $scope.properties.padre.catEstado = $scope.properties.tutor.catEstado;
                            $scope.properties.padre.ciudad = $scope.properties.tutor.ciudad;
                            $scope.properties.padre.colonia = $scope.properties.tutor.colonia;
                            $scope.properties.padre.telefono = $scope.properties.tutor.telefono;
                            $scope.properties.padre.codigoPostal = $scope.properties.tutor.codigoPostal;
                            $scope.properties.padre.delegacionMunicipio = $scope.properties.tutor.delegacionMunicipio;
                            $scope.properties.padre.estadoExtranjero = $scope.properties.tutor.estadoExtranjero;
                        }
                        if ($scope.properties.madre.viveContigo) {
                            $scope.properties.madre.calle = $scope.properties.tutor.calle;
                            $scope.properties.madre.catPais = $scope.properties.tutor.catPais;
                            $scope.properties.madre.numeroExterior = $scope.properties.tutor.numeroExterior;
                            $scope.properties.madre.numeroInterior = $scope.properties.tutor.numeroInterior;
                            $scope.properties.madre.catEstado = $scope.properties.tutor.catEstado;
                            $scope.properties.madre.ciudad = $scope.properties.tutor.ciudad;
                            $scope.properties.madre.colonia = $scope.properties.tutor.colonia;
                            $scope.properties.madre.telefono = $scope.properties.tutor.telefono;
                            $scope.properties.madre.codigoPostal = $scope.properties.tutor.codigoPostal;
                            $scope.properties.madre.delegacionMunicipio = $scope.properties.tutor.delegacionMunicipio;
                            $scope.properties.madre.estadoExtranjero = $scope.properties.tutor.estadoExtranjero;
                        }
                        $scope.assignTask();
                    }

                }

            } else if ($scope.properties.selectedIndex === 0) {
                $scope.faltacampo = false;
                console.log("validar 0");
                if ($scope.properties.catSolicitudDeAdmision.curp === null) {
                    $scope.properties.catSolicitudDeAdmision.curp = "";
                }
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.catSolicitudDeAdmision.fechaNacimiento === undefined || $scope.properties.catSolicitudDeAdmision.fechaNacimiento === null || $scope.properties.catSolicitudDeAdmision.fechaNacimiento === "") {
                    swal($filter('translate')("¡Fecha de nacimiento!"), $filter('translate')("Debes agregar tu fecha de nacimiento"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.catNacionalidad === null) {
                    swal($filter('translate')("¡Nacionalidad!"), $filter('translate')("Debes seleccionar tu nacionalidad"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.catReligion === null) {
                    swal($filter('translate')("¡Religión!"), $filter('translate')("Debes seleccionar tu religión"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.curp === "" && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                    swal($filter('translate')("¡CURP!"), $filter('translate')("Debes agregar tu CURP"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.curp.length < 18 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                    swal($filter('translate')("¡CURP!"), $filter('translate')("Tu CURP debe tener 18 caracteres"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular === undefined || $scope.properties.catSolicitudDeAdmision.telefonoCelular === null) {
                    swal($filter('translate')("¡Teléfono celular!"), $filter('translate')("Debes agregar tu número celular"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular.length !== 10 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                    swal($filter('translate')("¡Teléfono celular!"), $filter('translate')("Tu teléfono celular debe ser de 10 dígitos"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular === "" || $scope.properties.catSolicitudDeAdmision.telefonoCelular === null || $scope.properties.catSolicitudDeAdmision.telefonoCelular === undefined) {
                    swal($filter('translate')("¡Teléfono celular!"), $filter('translate')("Debes agregar tu número celular"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular.length < 10 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                    swal($filter('translate')("¡Teléfono celular!"), $filter('translate')("Tu teléfono celular debe ser mínimo de 10 dígitos a un máximo de 14 dígitos"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular.length > 14 && $scope.properties.catSolicitudDeAdmision.catPais.descripcion !== "Mexicana") {
                    swal($filter('translate')("¡Teléfono celular!"), $filter('translate')("Tu teléfono celular debe ser mínimo de 10 dígitos a un máximo de 14 dígitos"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.catEstadoCivil === null) {
                    swal($filter('translate')("¡Estado civil!"), $filter('translate')("Debes seleccionar tu estado civil"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.catSexo === null) {
                    swal($filter('translate')("¡Sexo!"), $filter('translate')("Debes seleccionar tu sexo"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus === null) {
                    swal($filter('translate')("¡Presentaste solicitud en otro campus!"), $filter('translate')("Debes seleccionar si has realizado la solicitud en otro campus"), "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion === "Sí" || $scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion === "Si") {
                    if ($scope.properties.catSolicitudDeAdmision.catCampusPresentadoSolicitud.length === 0) {
                        swal($filter('translate')("¡Campus presentado!"), $filter('translate')("Debes seleccionar el/los campus donde has presentado tu solicitud"), "warning");
                        $scope.faltacampo = true;
                    } else if ($scope.properties.catSolicitudDeAdmision.catConcluisteProceso === null) {
                        swal($filter('translate')("¡Concluiste proceso!"), $filter('translate')("Debes seleccionar si concluiste el proceso en el campus donde presentaste tu solicitud"), "warning");
                        $scope.faltacampo = true;
                    } else if ($scope.properties.catSolicitudDeAdmision.catResultadoAdmision === null && $scope.properties.catSolicitudDeAdmision.catConcluisteProceso.descripcion === "Sí") {
                        swal($filter('translate')("¡Resultado de admisión!"), $filter('translate')("Debes seleccionar el resultado de tu admisión en el campus donde presentaste tu solicitud"), "warning");
                        $scope.faltacampo = true;
                    }
                }
                if (!$scope.faltacampo) {

                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.fallo = false;
                        if ($scope.properties.catSolicitudDeAdmision.urlFoto === null || $scope.properties.catSolicitudDeAdmision.urlFoto === "") {
                            if ($scope.properties.fotopasaportearchivo.length > 0) {
                                if (JSON.stringify($scope.properties.fotopasaporte) === "{}") {
                                    $scope.fallo = true;
                                    swal($filter('translate')("¡Fotografía!"), $filter('translate')("Debes agregar una fotografía"), "warning");
                                } else {
                                    var auxData = null;
                                    if ($scope.properties.fotopasaportearchivo[0].newValue === undefined) {
                                        auxData = $scope.properties.fotopasaportearchivo[0];
                                    } else {
                                        auxData = angular.copy($scope.properties.fotopasaportearchivo[0].newValue);
                                    }
                                    auxData.filename = $scope.properties.fotopasaporte === undefined ? null : ($scope.properties.fotopasaporte.filename === '' ? null : $scope.properties.fotopasaporte.filename)
                                    auxData.tempPath = $scope.properties.fotopasaporte === undefined ? null : ($scope.properties.fotopasaporte.tempPath === '' ? null : $scope.properties.fotopasaporte.tempPath);
                                    auxData.contentType = $scope.properties.fotopasaporte === undefined ? null : ($scope.properties.fotopasaporte.contentType === '' ? null : $scope.properties.fotopasaporte.contentType);
                                    if (auxData.id !== undefined) {
                                        $scope.properties.fotopasaportearchivo[0] = {
                                            "id": angular.copy(auxData.id),
                                            "newValue": angular.copy(auxData)
                                        };
                                    } else {
                                        if ($scope.properties.fotopasaportearchivo[0].newValue.filename !== $scope.properties.fotopasaporte.filename) {
                                            $scope.properties.fotopasaportearchivo[0] = {
                                                "newValue": angular.copy(auxData)
                                            };
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.fotopasaporte === undefined || JSON.stringify($scope.properties.fotopasaporte) == '{}') {
                                    $scope.fallo = true;
                                    swal($filter('translate')("¡Fotografía!"), $filter('translate')("Debes agregar una fotografía"), "warning");
                                } else {
                                    $scope.properties.fotopasaportearchivo = [];
                                    $scope.properties.fotopasaportearchivo.push({
                                        "newValue": angular.copy($scope.properties.fotopasaporte)
                                    });
                                }
                            }
                        } else {
                            if ($scope.properties.fotopasaportearchivo.length > 0) {
                                var auxData = null;
                                if ($scope.properties.fotopasaportearchivo[0].newValue === undefined) {
                                    auxData = $scope.properties.fotopasaportearchivo[0];
                                } else {
                                    auxData = angular.copy($scope.properties.fotopasaportearchivo[0].newValue);
                                }
                                auxData.filename = $scope.properties.fotopasaporte === undefined ? null : ($scope.properties.fotopasaporte.filename === '' ? null : $scope.properties.fotopasaporte.filename)
                                auxData.tempPath = $scope.properties.fotopasaporte === undefined ? null : ($scope.properties.fotopasaporte.tempPath === '' ? null : $scope.properties.fotopasaporte.tempPath);
                                auxData.contentType = $scope.properties.fotopasaporte === undefined ? null : ($scope.properties.fotopasaporte.contentType === '' ? null : $scope.properties.fotopasaporte.contentType);
                                if (auxData.id !== undefined) {
                                    $scope.properties.fotopasaportearchivo[0] = {
                                        "id": angular.copy(auxData.id),
                                        "newValue": angular.copy(auxData)
                                    };
                                } else {
                                    if ($scope.properties.fotopasaportearchivo[0].newValue.filename !== $scope.properties.fotopasaporte.filename) {
                                        $scope.properties.fotopasaportearchivo[0] = {
                                            "newValue": angular.copy(auxData)
                                        };
                                    }
                                }
                                $scope.properties.catSolicitudDeAdmision.urlFoto = "";
                            } else {
                                if ($scope.properties.fotopasaporte !== undefined) {
                                    $scope.properties.fotopasaportearchivo = [];
                                    $scope.properties.fotopasaportearchivo.push({
                                        "newValue": angular.copy($scope.properties.fotopasaporte)
                                    });
                                    $scope.properties.catSolicitudDeAdmision.urlFoto = "";
                                } else {
                                    $scope.properties.fotopasaportearchivo = [];
                                }
                            }
                        }


                        if (!$scope.fallo) {
                            if ($scope.properties.catSolicitudDeAdmision.urlActaNacimiento === null || $scope.properties.catSolicitudDeAdmision.urlActaNacimiento === "") {
                                if ($scope.properties.actanacimientoarchivo.length > 0) {
                                    if (JSON.stringify($scope.properties.actanacimiento) === "{}") {
                                        $scope.fallo = true;
                                        swal($filter('translate')("¡Acta de nacimiento!"), $filter('translate')("Debes agregar tu acta de nacimiento"), "warning");
                                    } else {
                                        var auxData = null;
                                        if ($scope.properties.actanacimientoarchivo[0].newValue === undefined) {
                                            auxData = $scope.properties.actanacimientoarchivo[0];
                                        } else {
                                            auxData = angular.copy($scope.properties.actanacimientoarchivo[0].newValue);
                                        }
                                        auxData.filename = $scope.properties.actanacimiento === undefined ? null : ($scope.properties.actanacimiento.filename === '' ? null : $scope.properties.actanacimiento.filename);
                                        auxData.tempPath = $scope.properties.actanacimiento === undefined ? null : ($scope.properties.actanacimiento.tempPath === '' ? null : $scope.properties.actanacimiento.tempPath);
                                        auxData.contentType = $scope.properties.actanacimiento === undefined ? null : ($scope.properties.actanacimiento.contentType === '' ? null : $scope.properties.actanacimiento.contentType);
                                        if (auxData.id !== undefined) {
                                            $scope.properties.actanacimientoarchivo[0] = {
                                                "id": angular.copy(auxData.id),
                                                "newValue": angular.copy(auxData)
                                            };
                                        } else {
                                            if ($scope.properties.actanacimientoarchivo[0].newValue.filename !== $scope.properties.actanacimientoarchivo.filename) {
                                                $scope.properties.actanacimientoarchivo[0] = {
                                                    "newValue": angular.copy(auxData)
                                                };
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.actanacimiento === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                                        $scope.fallo = true;
                                        swal($filter('translate')("¡Acta de nacimiento!"), $filter('translate')("Debes agregar tu acta de nacimiento"), "warning");
                                    } else {
                                        $scope.properties.actanacimientoarchivo = [];
                                        $scope.properties.actanacimientoarchivo.push({
                                            "newValue": angular.copy($scope.properties.actanacimiento)
                                        });
                                    }
                                }
                            } else {
                                if ($scope.properties.actanacimientoarchivo.length > 0) {
                                    var auxData = null;
                                    if ($scope.properties.actanacimientoarchivo[0].newValue === undefined) {
                                        auxData = $scope.properties.actanacimientoarchivo[0];
                                    } else {
                                        auxData = angular.copy($scope.properties.actanacimientoarchivo[0].newValue);
                                    }
                                    auxData.filename = $scope.properties.actanacimiento === undefined ? null : ($scope.properties.actanacimiento.filename === '' ? null : $scope.properties.actanacimiento.filename);
                                    auxData.tempPath = $scope.properties.actanacimiento === undefined ? null : ($scope.properties.actanacimiento.tempPath === '' ? null : $scope.properties.actanacimiento.tempPath);
                                    auxData.contentType = $scope.properties.actanacimiento === undefined ? null : ($scope.properties.actanacimiento.contentType === '' ? null : $scope.properties.actanacimiento.contentType);
                                    if (auxData.id !== undefined) {
                                        $scope.properties.actanacimientoarchivo[0] = {
                                            "id": angular.copy(auxData.id),
                                            "newValue": angular.copy(auxData)
                                        };
                                    } else {
                                        if ($scope.properties.actanacimientoarchivo[0].newValue.filename !== $scope.properties.actanacimientoarchivo.filename) {
                                            $scope.properties.actanacimientoarchivo[0] = {
                                                "newValue": angular.copy(auxData)
                                            };
                                        }
                                    }
                                    $scope.properties.catSolicitudDeAdmision.urlActaNacimiento = "";
                                } else {
                                    if ($scope.properties.actanacimiento !== undefined) {
                                        $scope.properties.actanacimientoarchivo = [];
                                        $scope.properties.actanacimientoarchivo.push({
                                            "newValue": angular.copy($scope.properties.actanacimiento)
                                        });
                                        $scope.properties.catSolicitudDeAdmision.urlActaNacimiento = "";
                                    } else {
                                        $scope.properties.actanacimientoarchivo = [];
                                    }
                                }
                            }

                            if (!$scope.fallo) {
                                // if ($scope.properties.idExtranjero !== undefined) {
                                //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                // }
                                if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                } else {
                                    $scope.properties.idExtranjero = "";
                                }
                                //$scope.properties.selectedIndex++;
                                $scope.assignTask();
                            }
                        }
                    }
                }
            } else if ($scope.properties.selectedIndex === 2) {
                //
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos === null) {
                    swal($filter('translate')("¡Preparatoria!"), $filter('translate')("Debes seleccionar una preparatoria, en caso de no encontrar la tuya, seleccionar la opción Otro"), "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion === "Otro") {
                    if ($scope.properties.datosPreparatoria.nombreBachillerato === "") {
                        swal($filter('translate')("¡Preparatoria!"), $filter('translate')("Debes agregar el nombre de tu preparatoria"), "warning");
                    } else if ($scope.properties.datosPreparatoria.paisBachillerato === undefined) {
                        swal($filter('translate')("¡País preparatoria!"), $filter('translate')("Debes agregar el país de tu preparatoria"), "warning");
                    } else if ($scope.properties.datosPreparatoria.estadoBachillerato === undefined) {
                        swal($filter('translate')("¡Estado preparatoria!"), $filter('translate')("Debes agregar el estado de tu preparatoria"), "warning");
                    } else if ($scope.properties.datosPreparatoria.ciudadBachillerato === undefined) {
                        swal($filter('translate')("¡Ciudad de tu preparatoria!"), $filter('translate')("Debes agregar la ciudad de tu preparatoria"), "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "") {
                        swal($filter('translate')("¡Promedio!"), $filter('translate')("Debes agregar el promedio que obtuviste en tu preparatoria"), "warning");
                    } else if ($scope.properties.kardex === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                        swal($filter('translate')("¡Contancia de calificaciones!"), $filter('translate')("Debes agregar la constancia de calificaciones de tu preparatoria"), "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.catSolicitudDeAdmision.promedioGeneral = $scope.properties.catSolicitudDeAdmision.promedioGeneral + "";
                            if ($scope.properties.fotopasaporte !== undefined) {
                                $scope.properties.fotopasaportearchivo["newValue"] = $scope.properties.fotopasaporte;
                                console.log($scope.properties.fotopasaportearchivo);
                            }
                            if ($scope.properties.actanacimiento !== undefined) {
                                $scope.properties.actanacimientoarchivo["newValue"] = $scope.properties.actanacimiento;
                            }
                            if ($scope.properties.constancia !== undefined) {
                                $scope.properties.constanciaarchivo["newValue"] = $scope.properties.constancia;
                            }
                            if ($scope.properties.tieneDescuento === true) {
                                if ($scope.properties.descuento !== undefined) {
                                    $scope.properties.descuentoarchivo["newValue"] = $scope.properties.descuento;
                                    // if ($scope.properties.idExtranjero !== undefined) {
                                    //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                    // }
                                    if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                        $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                    } else {
                                        $scope.properties.idExtranjero = "";
                                    }
                                    //$scope.properties.selectedIndex++;
                                    $scope.assignTask();
                                } else {
                                    swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                                }
                            } else {
                                // if ($scope.properties.idExtranjero !== undefined) {
                                //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                // }
                                if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                } else {
                                    $scope.properties.idExtranjero = "";
                                }
                                //$scope.properties.selectedIndex++;
                                $scope.assignTask();
                            }
                        }
                    }
                } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "") {
                    swal($filter('translate')("¡Promedio!"), $filter('translate')("Debes agregar el promedio que obtuviste en tu preparatoria"), "warning");
                } else if ($scope.properties.kardex === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                    swal($filter('translate')("¡Contancia de calificaciones!"), $filter('translate')("Debes agregar la constancia de calificaciones de tu preparatoria"), "warning");
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        if ($scope.properties.constancia !== undefined) {
                            $scope.properties.constanciaarchivo["newValue"] = $scope.properties.constancia;
                        }
                        if ($scope.properties.tieneDescuento === true) {
                            if ($scope.properties.descuento !== undefined) {
                                $scope.properties.descuentoarchivo["newValue"] = $scope.properties.descuento;
                                // if ($scope.properties.idExtranjero !== undefined) {
                                //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                // }
                                if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                } else {
                                    $scope.properties.idExtranjero = "";
                                }
                                //$scope.properties.selectedIndex++;
                                $scope.assignTask();
                            } else {
                                swal($filter('translate')("¡Documento de descuento!"), $filter('translate')("Debes agregar el documento que acredita tu descuento"), "warning");
                            }
                        } else {
                            // if ($scope.properties.idExtranjero !== undefined) {
                            //     $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                            // }
                            if ($scope.properties.idExtranjero !== undefined && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                            } else {
                                $scope.properties.idExtranjero = "";
                            }
                            //$scope.properties.selectedIndex++;
                            $scope.assignTask();
                        }
                    }
                }
            }
        } else if ($scope.properties.tabs === "Informacion Familiar") {
            //
            if ($scope.properties.selectedIndex === 0) {
                console.log("validar 0");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.tutor.length === 0) {
                    swal($filter('translate')("¡Tutor!"), $filter('translate')("Debes agregar al menos un tutor"), "warning");
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.cp = undefined;
                    //$scope.properties.selectedIndex++;
                    $scope.assignTask();
                }

            } else if ($scope.properties.selectedIndex === 1) {

                console.log("validar 1");
                if ($scope.properties.padre.desconozcoDatosPadres) {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        //$scope.properties.cp = undefined;
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.cp = undefined;
                        //$scope.properties.selectedIndex++;
                        $scope.assignTask();
                    }
                } else if ($scope.properties.padre.vive === 0 || $scope.properties.padre.vive === null) {
                    swal($filter('translate')("¡Padre vive!"), $filter('translate')("Debes seleccionar si tu padre vive"), "warning");
                } else if ($scope.properties.padre.catTitulo === 0 || $scope.properties.padre.catTitulo === null) {
                    swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a tu padre"), "warning");
                } else if ($scope.properties.padre.nombre === "" || $scope.properties.padre.nombre === undefined) {
                    swal($filter('translate')("¡Nombre de tu padre!"), $filter('translate')("Debes agregar el nombre de tu padre"), "warning");
                } else if ($scope.properties.padre.apellidos === "" || $scope.properties.padre.apellidos === undefined) {
                    swal($filter('translate')("¡Apellidos de tu padre!"), $filter('translate')("Debes agregar los apellidos de tu padre"), "warning");
                } else if ($scope.properties.datosPadres.padrevive) {
                    if ($scope.properties.padre.catEgresoAnahuac === 0 || $scope.properties.padre.catEgresoAnahuac === null) {
                        swal($filter('translate')("¡Egresado Anáhuac!"), $filter('translate')("Debes seleccionar si tu padre egresó de la Universidad Anáhuac"), "warning");
                    } else if ($scope.properties.datosPadres.padreegresoanahuac) {
                        if ($scope.properties.padre.catCampusEgreso === 0 || $scope.properties.padre.catCampusEgreso === null) {
                            swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu padre"), "warning");
                        } else {
                            if ($scope.properties.padre.catTrabaja === 0 || $scope.properties.padre.catTrabaja === null) {
                                swal($filter('translate')("¿Trabaja?"), $filter('translate')("Debes seleccionar si tu padre trabaja"), "warning");
                            } else if ($scope.properties.datosPadres.padretrabaja) {
                                if ($scope.properties.padre.empresaTrabaja === "" || $scope.properties.padre.empresaTrabaja === undefined) {
                                    swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre del empresa donde tu padre trabaja"), "warning");
                                } else if ($scope.properties.padre.giroEmpresa === "" || $scope.properties.padre.giroEmpresa === undefined) {
                                    swal($filter('translate')("¡Giro empresa!"), $filter('translate')("Debes agregar el giro de la empresa del trabajo de tu padre"), "warning");
                                } else if ($scope.properties.padre.puesto === "" || $scope.properties.padre.puesto === undefined) {
                                    swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de tu padre"), "warning");
                                } else if ($scope.properties.padre.correoElectronico === "" || $scope.properties.padre.correoElectronico === undefined) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de tu padre"), "warning");
                                } else if (!re.test(String($scope.properties.padre.correoElectronico.trim()).toLowerCase())) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("El correo electrónico de tu padre no es válido"), "warning");
                                } else if ($scope.properties.padre.catEscolaridad === 0 || $scope.properties.padre.catEscolaridad === null) {
                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de tu padre"), "warning");
                                } else if ($scope.properties.padre.catPais === 0 || $scope.properties.padre.catPais === null) {
                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de tu padre"), "warning");
                                } else if (($scope.properties.padre.codigoPostal === "" || $scope.properties.padre.codigoPostal === undefined) && $scope.properties.padre.catPais.descripcion === "México") {
                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de tu padre"), "warning");
                                } else if (($scope.properties.padre.catEstado === 0 || $scope.properties.padre.catEstado === null) && $scope.properties.padre.catPais.descripcion === "México") {
                                    swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu padre"), "warning");
                                } else if ($scope.properties.padre.estadoExtranjero === "" && $scope.properties.padre.catPais.descripcion !== "México") {
                                    swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu padre"), "warning");
                                } else if ($scope.properties.padre.ciudad === "" || $scope.properties.padre.ciudad === undefined) {
                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la ciudad del domicilio de tu padre"), "warning");
                                } else if ($scope.properties.padre.delegacionMunicipio === "" || $scope.properties.padre.delegacionMunicipio === null) {
                                    swal($filter('translate')("¡Municipio/Delegación/Poblado!"), $filter('translate')("Debes agregar la Ciudad/Municipio/Delegación/Poblado del domicilio de tu padre"), "warning");
                                } else if ($scope.properties.padre.colonia === "" || $scope.properties.padre.colonia === undefined) {
                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de tu padre"), "warning");
                                } else if ($scope.properties.padre.calle === "" || $scope.properties.padre.calle === undefined) {
                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de tu padre"), "warning");
                                } else if ($scope.properties.padre.numeroExterior === "" || $scope.properties.padre.numeroExterior === undefined) {
                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de tu padre"), "warning");
                                } else if ($scope.properties.padre.telefono === "" || $scope.properties.padre.telefono === undefined) {
                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de tu padre"), "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.cp = undefined;
                                        //$scope.properties.selectedIndex++;
                                        $scope.assignTask();
                                    }
                                }
                            } else {
                                if ($scope.properties.padre.correoElectronico === "" || $scope.properties.padre.correoElectronico === undefined) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de tu padre"), "warning");
                            } else if (!re.test(String($scope.properties.padre.correoElectronico.trim()).toLowerCase())) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("El correo electrónico de tu padre no es válido"), "warning");
                            } else if ($scope.properties.padre.catEscolaridad === 0 || $scope.properties.padre.catEscolaridad === null) {
                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de tu padre"), "warning");
                            } else if ($scope.properties.padre.catPais === 0 || $scope.properties.padre.catPais === null) {
                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de tu padre"), "warning");
                            } else if (($scope.properties.padre.codigoPostal === "" || $scope.properties.padre.codigoPostal === undefined) && $scope.properties.padre.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de tu padre"), "warning");
                            } else if (($scope.properties.padre.catEstado === 0 || $scope.properties.padre.catEstado === null) && $scope.properties.padre.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.estadoExtranjero === "" && $scope.properties.padre.catPais.descripcion !== "México") {
                                swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.ciudad === "" || $scope.properties.padre.ciudad === undefined) {
                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la ciudad del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.delegacionMunicipio === "" || $scope.properties.padre.delegacionMunicipio === null) {
                                swal($filter('translate')("¡Municipio/Delegación/Poblado!"), $filter('translate')("Debes agregar la Ciudad/Municipio/Delegación/Poblado del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.colonia === "" || $scope.properties.padre.colonia === undefined) {
                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.calle === "" || $scope.properties.padre.calle === undefined) {
                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.numeroExterior === "" || $scope.properties.padre.numeroExterior === undefined) {
                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.telefono === "" || $scope.properties.padre.telefono === undefined) {
                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de tu padre"), "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.cp = undefined;
                                        //$scope.properties.selectedIndex++;
                                        $scope.assignTask();
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.padre.catTrabaja === 0 || $scope.properties.padre.catTrabaja === null) {
                            swal($filter('translate')("¿Trabaja?"), $filter('translate')("Debes seleccionar si tu padre trabaja"), "warning");
                        } else if ($scope.properties.datosPadres.padretrabaja) {
                            if ($scope.properties.padre.empresaTrabaja === "" || $scope.properties.padre.empresaTrabaja === undefined) {
                                swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre del empresa donde tu padre trabaja"), "warning");
                            } else if ($scope.properties.padre.giroEmpresa === "" || $scope.properties.padre.giroEmpresa === undefined) {
                                swal($filter('translate')("¡Giro empresa!"), $filter('translate')("Debes agregar el giro de la empresa del trabajo de tu padre"), "warning");
                            } else if ($scope.properties.padre.puesto === "" || $scope.properties.padre.puesto === undefined) {
                                swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de tu padre"), "warning");
                            } else if ($scope.properties.padre.correoElectronico === "" || $scope.properties.padre.correoElectronico === undefined) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de tu padre"), "warning");
                            } else if (!re.test(String($scope.properties.padre.correoElectronico.trim()).toLowerCase())) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("El correo electrónico de tu padre no es válido"), "warning");
                            } else if ($scope.properties.padre.catEscolaridad === 0 || $scope.properties.padre.catEscolaridad === null) {
                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de tu padre"), "warning");
                            } else if ($scope.properties.padre.catPais === 0 || $scope.properties.padre.catPais === null) {
                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de tu padre"), "warning");
                            } else if (($scope.properties.padre.codigoPostal === "" || $scope.properties.padre.codigoPostal === undefined) && $scope.properties.padre.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de tu padre"), "warning");
                            } else if (($scope.properties.padre.catEstado === 0 || $scope.properties.padre.catEstado === null) && $scope.properties.padre.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.estadoExtranjero === "" && $scope.properties.padre.catPais.descripcion !== "México") {
                                swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.ciudad === "" || $scope.properties.padre.ciudad === undefined) {
                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la ciudad del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.delegacionMunicipio === "" || $scope.properties.padre.delegacionMunicipio === null) {
                                swal($filter('translate')("¡Municipio/Delegación/Poblado!"), $filter('translate')("Debes agregar la Ciudad/Municipio/Delegación/Poblado del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.colonia === "" || $scope.properties.padre.colonia === undefined) {
                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.calle === "" || $scope.properties.padre.calle === undefined) {
                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.numeroExterior === "" || $scope.properties.padre.numeroExterior === undefined) {
                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de tu padre"), "warning");
                            } else if ($scope.properties.padre.telefono === "" || $scope.properties.padre.telefono === undefined) {
                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de tu padre"), "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.cp = undefined;
                                    //$scope.properties.selectedIndex++;
                                    $scope.assignTask();
                                }
                            }
                        } else {
                            if ($scope.properties.padre.correoElectronico === "" || $scope.properties.padre.correoElectronico === undefined) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de tu padre"), "warning");
                        } else if (!re.test(String($scope.properties.padre.correoElectronico.trim()).toLowerCase())) {
                            swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("El correo electrónico de tu padre no es válido"), "warning");
                        } else if ($scope.properties.padre.catEscolaridad === 0 || $scope.properties.padre.catEscolaridad === null) {
                            swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de tu padre"), "warning");
                        } else if ($scope.properties.padre.catPais === 0 || $scope.properties.padre.catPais === null) {
                            swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de tu padre"), "warning");
                        } else if (($scope.properties.padre.codigoPostal === "" || $scope.properties.padre.codigoPostal === undefined) && $scope.properties.padre.catPais.descripcion === "México") {
                            swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de tu padre"), "warning");
                        } else if (($scope.properties.padre.catEstado === 0 || $scope.properties.padre.catEstado === null) && $scope.properties.padre.catPais.descripcion === "México") {
                            swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu padre"), "warning");
                        } else if ($scope.properties.padre.estadoExtranjero === "" && $scope.properties.padre.catPais.descripcion !== "México") {
                            swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu padre"), "warning");
                        } else if ($scope.properties.padre.ciudad === "" || $scope.properties.padre.ciudad === undefined) {
                            swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la ciudad del domicilio de tu padre"), "warning");
                        } else if ($scope.properties.padre.delegacionMunicipio === "" || $scope.properties.padre.delegacionMunicipio === null) {
                            swal($filter('translate')("¡Municipio/Delegación/Poblado!"), $filter('translate')("Debes agregar la Ciudad/Municipio/Delegación/Poblado del domicilio de tu padre"), "warning");
                        } else if ($scope.properties.padre.colonia === "" || $scope.properties.padre.colonia === undefined) {
                            swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de tu padre"), "warning");
                        } else if ($scope.properties.padre.calle === "" || $scope.properties.padre.calle === undefined) {
                            swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de tu padre"), "warning");
                        } else if ($scope.properties.padre.numeroExterior === "" || $scope.properties.padre.numeroExterior === undefined) {
                            swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de tu padre"), "warning");
                        } else if ($scope.properties.padre.telefono === "" || $scope.properties.padre.telefono === undefined) {
                            swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de tu padre"), "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.cp = undefined;
                                    //$scope.properties.selectedIndex++;
                                    $scope.assignTask();
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.cp = undefined;
                        //$scope.properties.selectedIndex++;
                        $scope.assignTask();
                    }
                }
            } else if ($scope.properties.selectedIndex === 2) {
                console.log("valida a tu madre")
                if ($scope.properties.madre.desconozcoDatosPadres) {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        //$scope.properties.cp = undefined;
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.cp = undefined;
                        //$scope.properties.selectedIndex++;
                        $scope.assignTask();
                    }
                } else if ($scope.properties.madre.vive === 0 || $scope.properties.madre.vive === null) {
                    swal($filter('translate')("¡Madre vive!"), $filter('translate')("Debes seleccionar si tu madre vive"), "warning");
                } else if ($scope.properties.madre.catTitulo === 0 || $scope.properties.madre.catTitulo === null) {
                    swal($filter('translate')("¡Título!"), $filter('translate')("Debes seleccionar el título para identificar a tu madre"), "warning");
                } else if ($scope.properties.madre.nombre === "" || $scope.properties.madre.nombre === undefined) {
                    swal($filter('translate')("¡Nombre de tu madre!"), $filter('translate')("Debes agregar el nombre de tu madre"), "warning");
                } else if ($scope.properties.madre.apellidos === "" || $scope.properties.madre.apellidos === undefined) {
                    swal($filter('translate')("¡Apellidos de tu madre!"), $filter('translate')("Debes agregar los apellidos de tu madre"), "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.madre.catEgresoAnahuac === 0 || $scope.properties.madre.catEgresoAnahuac === null) {
                        swal($filter('translate')("¡Egresado Anáhuac!"), $filter('translate')("Debes seleccionar si tu madre egresó de la Universidad Anáhuac"), "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.madre.catCampusEgreso === 0 || $scope.properties.madre.catCampusEgreso === null) {
                            swal($filter('translate')("¡Campus egresado!"), $filter('translate')("Debes seleccionar de cuál campus Anáhuac egresó tu madre"), "warning");
                        } else {
                            if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                                swal($filter('translate')("¿Trabaja?"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                    swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                                } else if ($scope.properties.madre.giroEmpresa === "" || $scope.properties.madre.giroEmpresa === undefined) {
                                    swal($filter('translate')("¡Giro empresa!"), $filter('translate')("Debes agregar el giro de la empresa del trabajo de tu madre"), "warning");
                                } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                    swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de tu madre"), "warning");
                                } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de tu madre"), "warning");
                                } else if (!re.test(String($scope.properties.madre.correoElectronico.trim()).toLowerCase())) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("El correo electrónico de tu madre no es válido"), "warning");
                                } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de tu madre"), "warning");
                                } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de tu madre"), "warning");
                                } else if (($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) && $scope.properties.madre.catPais.descripcion === "México") {
                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de tu madre"), "warning");
                                } else if (($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) && $scope.properties.madre.catPais.descripcion === "México") {
                                    swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.estadoExtranjero === "" && $scope.properties.madre.catPais.descripcion !== "México") {
                                    swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la ciudad del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.delegacionMunicipio === "" || $scope.properties.madre.delegacionMunicipio === null) {
                                    swal($filter('translate')("¡Municipio/Delegación/Poblado!"), $filter('translate')("Debes agregar la Ciudad/Municipio/Delegación/Poblado del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de tu madre"), "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.cp = undefined;
                                        //$scope.properties.selectedIndex++;
                                        $scope.assignTask();
                                    }
                                }
                            } else {
                                if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de tu madre"), "warning");
                                } else if (!re.test(String($scope.properties.madre.correoElectronico.trim()).toLowerCase())) {
                                    swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("El correo electrónico de tu madre no es válido"), "warning");
                                } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                    swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de tu madre"), "warning");
                                } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                    swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de tu madre"), "warning");
                                } else if (($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) && $scope.properties.madre.catPais.descripcion === "México") {
                                    swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de tu madre"), "warning");
                                } else if (($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) && $scope.properties.madre.catPais.descripcion === "México") {
                                    swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.estadoExtranjero === "" && $scope.properties.madre.catPais.descripcion !== "México") {
                                    swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                    swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la ciudad del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.delegacionMunicipio === "" || $scope.properties.madre.delegacionMunicipio === null) {
                                    swal($filter('translate')("¡Municipio/Delegación/Poblado!"), $filter('translate')("Debes agregar la Ciudad/Municipio/Delegación/Poblado del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                    swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                    swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                    swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de tu madre"), "warning");
                                } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                    swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de tu madre"), "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.cp = undefined;
                                        //$scope.properties.selectedIndex++;
                                        $scope.assignTask();
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                            swal($filter('translate')("¿Trabaja?"), $filter('translate')("Debes seleccionar si tu madre trabaja"), "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                swal($filter('translate')("¡Empresa!"), $filter('translate')("Debes agregar el nombre de la empresa donde tu madre trabaja"), "warning");
                            } else if ($scope.properties.madre.giroEmpresa === "" || $scope.properties.madre.giroEmpresa === undefined) {
                                swal($filter('translate')("¡Giro empresa!"), $filter('translate')("Debes agregar el giro de la empresa del trabajo de tu madre"), "warning");
                            } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                swal($filter('translate')("¡Puesto!"), $filter('translate')("Debes agregar el puesto de trabajo de tu madre"), "warning");
                            } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de tu madre"), "warning");
                            } else if (!re.test(String($scope.properties.madre.correoElectronico.trim()).toLowerCase())) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("El correo electrónico de tu madre no es válido"), "warning");
                            } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de tu madre"), "warning");
                            } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de tu madre"), "warning");
                            } else if (($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) && $scope.properties.madre.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de tu madre"), "warning");
                            } else if (($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) && $scope.properties.madre.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.estadoExtranjero === "" && $scope.properties.madre.catPais.descripcion !== "México") {
                                swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la ciudad del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.delegacionMunicipio === "" || $scope.properties.madre.delegacionMunicipio === null) {
                                swal($filter('translate')("¡Municipio/Delegación/Poblado!"), $filter('translate')("Debes agregar la Ciudad/Municipio/Delegación/Poblado del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de tu madre"), "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.cp = undefined;
                                    //$scope.properties.selectedIndex++;
                                    $scope.assignTask();
                                }
                            }
                        } else {
                            if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("Debes agregar el correo electrónico de tu madre"), "warning");
                            } else if (!re.test(String($scope.properties.madre.correoElectronico.trim()).toLowerCase())) {
                                swal($filter('translate')("¡Correo electrónico!"), $filter('translate')("El correo electrónico de tu madre no es válido"), "warning");
                            } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                swal($filter('translate')("¡Escolaridad!"), $filter('translate')("Debes seleccionar la escolaridad de tu madre"), "warning");
                            } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                swal($filter('translate')("¡País!"), $filter('translate')("Debes agregar el país del domicilio de tu madre"), "warning");
                            } else if (($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) && $scope.properties.madre.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Código postal!"), $filter('translate')("Debes agregar el código postal del domicilio de tu madre"), "warning");
                            } else if (($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) && $scope.properties.madre.catPais.descripcion === "México") {
                                swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.estadoExtranjero === "" && $scope.properties.madre.catPais.descripcion !== "México") {
                                swal($filter('translate')("¡Estado!"), $filter('translate')("Debes agregar el estado del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                swal($filter('translate')("¡Ciudad!"), $filter('translate')("Debes agregar la ciudad del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.delegacionMunicipio === "" || $scope.properties.madre.delegacionMunicipio === null) {
                                swal($filter('translate')("¡Municipio/Delegación/Poblado!"), $filter('translate')("Debes agregar la Ciudad/Municipio/Delegación/Poblado del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                swal($filter('translate')("¡Colonia!"), $filter('translate')("Debes agregar la colonia del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                swal($filter('translate')("¡Calle!"), $filter('translate')("Debes agregar la calle del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                swal($filter('translate')("¡Número exterior!"), $filter('translate')("Debes agregar el número exterior del domicilio de tu madre"), "warning");
                            } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                swal($filter('translate')("¡Teléfono!"), $filter('translate')("Debes agregar el teléfono de tu madre"), "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.cp = undefined;
                                    //$scope.properties.selectedIndex++;
                                    $scope.assignTask();
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.cp = undefined;
                        //$scope.properties.selectedIndex++;
                        $scope.assignTask();
                    }
                }

            } else if ($scope.properties.selectedIndex === 3) {
                console.log("validar 3");
                if ($scope.properties.contactoEmergencia.length === 0) {
                    swal($filter('translate')("¡Contacto de emergencia!"), $filter('translate')("Debes agregar al menos un contacto de emergencia"), "warning");
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        //$scope.properties.selectedIndex++;
                        $scope.assignTask();
                    }
                }
            }
        } else if ($scope.properties.tabs === "Revisa Solicitud") {
            if ($scope.properties.selectedIndex === 0) {
                console.log("validar 0");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    //$scope.properties.selectedIndex++;
                    $scope.assignTask();
                }

            } else if ($scope.properties.selectedIndex === 1) {
                console.log("validar 1");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    //$scope.properties.selectedIndex++;
                    $scope.assignTask();
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

    $scope.assignTask = function() {
        //$scope.showModal();
        //blockUI.start();
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
                //$scope.hideModal();
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
        if ($scope.properties.tabs === "Informacion Personal") {
            $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndexPersonal = $scope.properties.selectedIndex + 1;

            /*if($scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string !== undefined){
                $scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos.persistenceId_string = $scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceid_string;
            }*/
        }
        if ($scope.properties.tabs === "Informacion Familiar") {
            $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndexFamiliar = $scope.properties.selectedIndex + 1;
        }
        if ($scope.properties.tabs === "Revisa Solicitud") {
            $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndexRevision = $scope.properties.selectedIndex + 1;
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
                getTask();
            })
            .error(function(data, status) {
                getTaskAgain();
            })
            .finally(function() {
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
                        blockUI.stop();
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

