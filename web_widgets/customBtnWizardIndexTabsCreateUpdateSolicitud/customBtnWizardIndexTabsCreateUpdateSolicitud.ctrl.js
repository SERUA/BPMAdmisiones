function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    $scope.action = function() {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if ($scope.properties.tabs === "Informacion Personal") {
            if ($scope.properties.selectedIndex === 1) {
                console.log("validar 1");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                }
                if ($scope.properties.catSolicitudDeAdmision.catPais === null) {
                    swal("¡País!", "Debes seleccionar el país", "warning");
                } else if (($scope.properties.catSolicitudDeAdmision.codigoPostal === "" || $scope.properties.catSolicitudDeAdmision.codigoPostal === null) && $scope.properties.catSolicitudDeAdmision.catPais.descripcion === "México") {
                    swal("¡Código postal!", "Debes agregar el código postal", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catEstado === null && $scope.properties.catSolicitudDeAdmision.catPais.descripcion === "México") {
                    swal("¡Estado!", "Debes seleccionar el estado", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catPais.descripcion !== "México" && $scope.properties.catSolicitudDeAdmision.estadoExtranjero === "") {
                    swal("¡Estado!", "Debes agregar el estado", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.ciudad === "" || $scope.properties.catSolicitudDeAdmision.ciudad === null) {
                    swal("¡Ciudad!", "Debes agregar una ciudad", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.delegacionMunicipio === "" || $scope.properties.catSolicitudDeAdmision.delegacionMunicipio === null) {
                    swal("¡Municipio/Delegación/Poblado!", "Debes agregar una Ciudad/Municipio/Delegación/Poblado", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.calle === "" || $scope.properties.catSolicitudDeAdmision.calle === null) {
                    swal("¡Calle!", "Debes agregar la calle", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.ciudad === "" || $scope.properties.catSolicitudDeAdmision.ciudad === null) {
                    swal("¡Ciudad!", "Debes agregar una ciudad", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.numExterior === "" || $scope.properties.catSolicitudDeAdmision.numExterior === null) {
                    swal("¡Número!", "Debes agregar el número de tu domicilio", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.colonia === "" || $scope.properties.catSolicitudDeAdmision.colonia === null) {
                    swal("¡Colonia!", "Debes agregar la colonia", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.telefono === "" || $scope.properties.catSolicitudDeAdmision.telefono === null) {
                    swal("¡Teléfono!", "Debes el agregar el teléfono", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.telefono.length !== 10 && $scope.properties.catSolicitudDeAdmision.catPais.descripcion === "México") {
                    swal("¡Teléfono!", "Tu teléfono debe ser de 10 dígitos", "warning");
                    $scope.properties.catSolicitudDeAdmision.telefono = "";
                } else if ($scope.properties.catSolicitudDeAdmision.telefono.length < 10 && $scope.properties.catSolicitudDeAdmision.catPais.descripcion !== "México") {
                    swal("¡Teléfono!", "Tu teléfono debe ser mínimo de 10 dígitos a un máximo de 14 dígitos", "warning");
                    $scope.properties.catSolicitudDeAdmision.telefono = "";
                } else if ($scope.properties.catSolicitudDeAdmision.telefono.length > 14 && $scope.properties.catSolicitudDeAdmision.catPais.descripcion !== "México") {
                    swal("¡Teléfono!", "Tu teléfono debe ser mínimo de 10 dígitos a un máximo de 14 dígitos", "warning");
                    $scope.properties.catSolicitudDeAdmision.telefono = "";
                } else if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    topFunction();
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
                            $scope.properties.madre.telefonoCelular = $scope.properties.tutor[0].telefonoCelular;
                            $scope.properties.madre.codigoPostal = $scope.properties.tutor[0].codigoPostal;
                            $scope.properties.madre.delegacionMunicipio = $scope.properties.tutor[0].delegacionMunicipio;
                            $scope.properties.madre.estadoExtranjero = $scope.properties.tutor[0].estadoExtranjero;
                        }
                        $scope.properties.selectedIndex++;

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
                            $scope.properties.padre.telefono.telefonoCelular = $scope.properties.tutor.telefonoCelular;
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
                            $scope.properties.madre.telefonoCelular = $scope.properties.tutor.telefonoCelular;
                            $scope.properties.madre.codigoPostal = $scope.properties.tutor.codigoPostal;
                            $scope.properties.madre.delegacionMunicipio = $scope.properties.tutor.delegacionMunicipio;
                            $scope.properties.madre.estadoExtranjero = $scope.properties.tutor.estadoExtranjero;
                        }
                        $scope.properties.selectedIndex++;
                    }

                    //$scope.assignTask();
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
                    swal("¡Fecha de nacimiento!", "Debes agregar tu fecha de nacimiento", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.catNacionalidad === null) {
                    swal("¡Nacionalidad!", "Debes seleccionar tu nacionalidad", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.catReligion === null) {
                    swal("¡Religión!", "Debes seleccionar tu religión", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.curp === "" && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                    swal("¡CURP!", "Debes agregar tu CURP", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.curp.length < 18 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                    swal("¡CURP!", "Tu CURP debe tener 18 caracteres", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular === undefined || $scope.properties.catSolicitudDeAdmision.telefonoCelular === null) {
                    swal("¡Teléfono celular!", "Debes agregar tu número celular", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular.length !== 10 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                    swal("¡Teléfono celular!", "Tu teléfono celular debe ser de 10 dígitos", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular === "" || $scope.properties.catSolicitudDeAdmision.telefonoCelular === null || $scope.properties.catSolicitudDeAdmision.telefonoCelular === undefined) {
                    swal("¡Teléfono celular!", "Debes agregar tu número celular", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular.length < 10 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                    swal("¡Teléfono celular!", "Tu teléfono celular debe ser mínimo de 10 dígitos a un máximo de 14 dígitos", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.telefonoCelular.length > 14 && $scope.properties.catSolicitudDeAdmision.catPais.descripcion !== "Mexicana") {
                    swal("¡Teléfono celular!", "Tu teléfono celular debe ser mínimo de 10 dígitos a un máximo de 14 dígitos", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.catEstadoCivil === null) {
                    swal("¡Estado civil!", "Debes seleccionar tu estado civil", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.catSexo === null) {
                    swal("¡Sexo!", "Debes seleccionar tu sexo", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus === null) {
                    swal("¡Presentó examen en otro campus!", "Debes seleccionar si has realizado la solicitud en otro campus", "warning");
                    $scope.faltacampo = true;
                } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion === "Sí" || $scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion === "Si") {
                    if ($scope.properties.catSolicitudDeAdmision.catCampusPresentadoSolicitud.length === 0) {
                        swal("¡Campus presentado!", "Debes seleccionar el/los campus donde has presentado tu solicitud", "warning");
                        $scope.faltacampo = true;
                    } else if ($scope.properties.catSolicitudDeAdmision.catConcluisteProceso === null) {
                        swal("¡Concluiste proceso!", "Debes seleccionar si concluiste el proceso en el campus donde presentaste tu solicitud", "warning");
                        $scope.faltacampo = true;
                    } else if ($scope.properties.catSolicitudDeAdmision.catResultadoAdmision === null && $scope.properties.catSolicitudDeAdmision.catConcluisteProceso.descripcion === "Sí") {
                        swal("¡Resultado de admisión!", "Debes seleccionar el resultado de tu admisión en el campus donde presentaste tu solicitud", "warning");
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
                                    swal("¡Fotografía!", "Debes agregar una fotografía", "warning");
                                } else {
                                    if ($scope.properties.fotopasaporte !== undefined) {
                                        $scope.properties.fotopasaportearchivo[0]["newValue"] = $scope.properties.fotopasaporte;
                                    }
                                }
                            } else {
                                if ($scope.properties.fotopasaporte === undefined || JSON.stringify($scope.properties.fotopasaporte) == '{}') {
                                    $scope.fallo = true;
                                    swal("¡Fotografía!", "Debes agregar una fotografía", "warning");
                                } else {
                                    $scope.properties.fotopasaportearchivo = [];
                                    $scope.properties.fotopasaportearchivo.push({
                                        "newValue": angular.copy($scope.properties.fotopasaporte)
                                    });
                                }
                            }
                        } else {
                            if ($scope.properties.fotopasaportearchivo.length > 0) {
                                if ($scope.properties.fotopasaporte !== undefined) {
                                    $scope.properties.fotopasaportearchivo[0]["newValue"] = $scope.properties.fotopasaporte;
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
                                        swal("¡Acta de nacimiento!", "Debes agregar tu acta de nacimiento", "warning");
                                    } else {
                                        if ($scope.properties.actanacimiento !== undefined) {
                                            $scope.properties.actanacimientoarchivo[0]["newValue"] = $scope.properties.actanacimiento;
                                        }
                                    }
                                } else {
                                    if ($scope.properties.actanacimiento === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                                        $scope.fallo = true;
                                        swal("¡Acta de nacimiento!", "Debes agregar tu acta de nacimiento", "warning");
                                    } else {
                                        $scope.properties.actanacimientoarchivo = [];
                                        $scope.properties.actanacimientoarchivo.push({
                                            "newValue": angular.copy($scope.properties.actanacimiento)
                                        });
                                    }
                                }
                            } else {
                                if ($scope.properties.actanacimientoarchivo.length > 0) {

                                    if ($scope.properties.actanacimiento !== undefined) {
                                        $scope.properties.actanacimientoarchivo[0]["newValue"] = $scope.properties.actanacimiento;
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
                                if ($scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana") {
                                    if ($scope.properties.idExtranjero !== undefined) {
                                        $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                    }
                                }

                                topFunction();
                                $scope.properties.selectedIndex++;
                                //$scope.assignTask();
                            }
                        }
                    }
                }
            } else if ($scope.properties.selectedIndex === 2) {

                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos === null) {
                    swal("¡Preparatoria!", "Debes seleccionar una preparatoria en caso de no encontrar la suya seleccionar la opción otro", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion === "Otro") {
                    if ($scope.properties.datosPreparatoria.nombreBachillerato === "") {
                        swal("¡Preparatoria!", "Debes agregar el nombre de tu preparatoria", "warning");
                    } else if ($scope.properties.datosPreparatoria.paisBachillerato === undefined) {
                        swal("¡País de tu preparatoria!", "Debes agregar el país de tu preparatoria", "warning");
                    } else if ($scope.properties.datosPreparatoria.estadoBachillerato === undefined) {
                        swal("¡Estado de tu  preparatoria!", "Debes agregar el estado de tu preparatoria", "warning");
                    } else if ($scope.properties.datosPreparatoria.ciudadBachillerato === undefined) {
                        swal("¡Ciudad de tu  preparatoria!", "Debes agregar la ciudad de tu preparatoria", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "" || $scope.properties.catSolicitudDeAdmision.promedioGeneral=="0") {
                        swal("¡Promedio!", "Debes agregar el promedio que obtuviste en tu preparatoria", "warning");
                    } else if ($scope.properties.kardex === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                        swal("¡Contancia de calificaciones!", "Debes agregar la constancia de calificaciones de tu preparatoria", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.catSolicitudDeAdmision.promedioGeneral = $scope.properties.catSolicitudDeAdmision.promedioGeneral + "";
                            if ($scope.properties.fotopasaporte !== undefined) {
                                $scope.properties.fotopasaportearchivo[0]["newValue"] = $scope.properties.fotopasaporte;
                                console.log($scope.properties.fotopasaportearchivo);
                            }
                            if ($scope.properties.actanacimiento !== undefined) {
                                $scope.properties.actanacimientoarchivo[0]["newValue"] = $scope.properties.actanacimiento;
                            }
                            if ($scope.properties.constancia !== undefined) {
                                $scope.properties.constanciaarchivo[0]["newValue"] = $scope.properties.constancia;
                            }
                            if ($scope.properties.tieneDescuento === true) {
                                if ($scope.properties.descuento !== undefined) {
                                    $scope.properties.descuentoarchivo[0]["newValue"] = $scope.properties.descuento;
                                    if ($scope.properties.idExtranjero !== undefined) {
                                        $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                    }
                                    topFunction();
                                    $scope.properties.selectedIndex++;
                                    //$scope.assignTask();
                                } else {
                                    swal("¡Documento de descuento!", "Debes agregar el documento que acredita tu descuento", "warning");
                                }
                            } else {
                                if ($scope.properties.idExtranjero !== undefined) {
                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                }
                                topFunction();
                                $scope.properties.selectedIndex++;
                                //$scope.assignTask();
                            }
                        }
                    }
                } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "" || $scope.properties.catSolicitudDeAdmision.promedioGeneral=="0") {
                    swal("¡Promedio!", "Debes agregar el promedio que obtuviste en tu preparatoria", "warning");
                } else if ($scope.properties.kardex === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                    swal("¡Contancia de calificaciones!", "Debes agregar la constancia de calificaciones de tu preparatoria", "warning");
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        if ($scope.properties.constancia !== undefined) {
                            $scope.properties.constanciaarchivo[0]["newValue"] = $scope.properties.constancia;
                        }
                        if ($scope.properties.tieneDescuento === true) {
                            if ($scope.properties.descuento !== undefined) {
                                $scope.properties.descuentoarchivo[0]["newValue"] = $scope.properties.descuento;
                                if ($scope.properties.idExtranjero !== undefined) {
                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                }
                                topFunction();
                                $scope.properties.selectedIndex++;
                                //$scope.assignTask();
                            } else {
                                swal("¡Documento de descuento!", "Debes agregar el documento que acredita tu descuento", "warning");
                            }
                        } else {
                            if ($scope.properties.idExtranjero !== undefined) {
                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                            }
                            topFunction();
                            $scope.properties.selectedIndex++;
                            //$scope.assignTask();
                        }
                    }
                }
            }
        } else if ($scope.properties.tabs === "Informacion Familiar") {
            if ($scope.properties.selectedIndex === 0) {
                console.log("validar 0");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.tutor.length === 0) {
                    swal("¡Tutor!", "Debes agregar al menos un tutor", "warning");
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    $scope.properties.cp = undefined;
                    topFunction();
                    $scope.properties.selectedIndex++;
                    //$scope.assignTask();
                }

            } else if ($scope.properties.selectedIndex === 1) {

                console.log("validar 1");
                if ($scope.properties.padre.desconozcoDatosPadres) {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        //$scope.properties.cp = undefined;
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.cp = undefined;
                        topFunction();
                        $scope.properties.selectedIndex++;
                        //$scope.assignTask();
                    }
                } else if ($scope.properties.padre.vive === 0 || $scope.properties.padre.vive === null) {
                    swal("¡Padre vive!", "Debes seleccionar si tu padre vive", "warning");
                } else if ($scope.properties.padre.catTitulo === 0 || $scope.properties.padre.catTitulo === null) {
                    swal("¡Título!", "Debes seleccionar el título para identificar a tu padre", "warning");
                } else if ($scope.properties.padre.nombre === "" || $scope.properties.padre.nombre === undefined) {
                    swal("¡Nombre de tu padre!", "Debes agregar nombre de tu padre", "warning");
                } else if ($scope.properties.padre.apellidos === "" || $scope.properties.padre.apellidos === undefined) {
                    swal("¡Apellidos de tu padre!", "Debes agregar los apellidos de tu padre", "warning");
                } else if ($scope.properties.datosPadres.padrevive) {
                    if ($scope.properties.padre.catEgresoAnahuac === 0 || $scope.properties.padre.catEgresoAnahuac === null) {
                        swal("¡Egresado Anáhuac!", "Debes seleccionar si tu padre egresó del universidad Anáhuac", "warning");
                    } else if ($scope.properties.datosPadres.padreegresoanahuac) {
                        if ($scope.properties.padre.catCampusEgreso === 0 || $scope.properties.padre.catCampusEgreso === null) {
                            swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó tu padre", "warning");
                        } else {
                            if ($scope.properties.padre.catTrabaja === 0 || $scope.properties.padre.catTrabaja === null) {
                                swal("¿Trabaja?", "Debes seleccionar si tu padre trabaja", "warning");
                            } else if ($scope.properties.datosPadres.padretrabaja) {
                                if ($scope.properties.padre.empresaTrabaja === "" || $scope.properties.padre.empresaTrabaja === undefined) {
                                    swal("¡Empresa!", "Debes agregar el nombre del empresa donde tu padre trabaja", "warning");
                                } else if ($scope.properties.padre.giroEmpresa === "" || $scope.properties.padre.giroEmpresa === undefined) {
                                    swal("¡Giro empresa!!", "Debes agregar el giro de la empresa del de trabajo de tu padre!", "warning");
                                } else if ($scope.properties.padre.puesto === "" || $scope.properties.padre.puesto === undefined) {
                                    swal("¡Puesto!", "Debes agregar el puesto de trabajo de tu padre", "warning");
                                } else if ($scope.properties.padre.correoElectronico === "" || $scope.properties.padre.correoElectronico === undefined) {
                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de tu padre", "warning");
                                } else if (!re.test(String($scope.properties.padre.correoElectronico.trim()).toLowerCase())) {
                                    swal("¡Correo electrónico!", "El correo electrónico de tu padre no es válido", "warning");
                                } else if ($scope.properties.padre.catEscolaridad === 0 || $scope.properties.padre.catEscolaridad === null) {
                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de tu padre", "warning");
                                } else if (!$scope.properties.padre.telefonoCelular) {
                                    swal("¡Teléfono celular!", "Debes agregar el teléfono celular de tu padre", "warning");
                                } else if ($scope.properties.padre.catPais === 0 || $scope.properties.padre.catPais === null) {
                                    swal("¡País!", "Debes agregar el país del domicilio de tu padre", "warning");
                                } else if (($scope.properties.padre.codigoPostal === "" || $scope.properties.padre.codigoPostal === undefined) && $scope.properties.padre.catPais.descripcion === "México") {
                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de tu padre", "warning");
                                } else if (($scope.properties.padre.catEstado === 0 || $scope.properties.padre.catEstado === null) && $scope.properties.padre.catPais.descripcion === "México") {
                                    swal("¡Estado!", "Debes agregar el estado del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.estadoExtranjero === "" && $scope.properties.padre.catPais.descripcion !== "México") {
                                    swal("¡Estado!", "Debes agregar el estado del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.ciudad === "" || $scope.properties.padre.ciudad === undefined) {
                                    swal("¡Ciudad!", "Debes agregar la ciudad del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.delegacionMunicipio === "" || $scope.properties.padre.delegacionMunicipio === null) {
                                    swal("¡Municipio/Delegación/Poblado!", "Debes agregar una Ciudad/Municipio/Delegación/Poblado del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.colonia === "" || $scope.properties.padre.colonia === undefined) {
                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.calle === "" || $scope.properties.padre.calle === undefined) {
                                    swal("¡Calle!", "Debes agregar la calle del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.numeroExterior === "" || $scope.properties.padre.numeroExterior === undefined) {
                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.telefono === "" || $scope.properties.padre.telefono === undefined) {
                                    swal("¡Teléfono!", "Debes agregar el teléfono de tu padre", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.cp = undefined;
                                        topFunction();
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                    }
                                }
                            } else {
                                if ($scope.properties.padre.correoElectronico === "" || $scope.properties.padre.correoElectronico === undefined) {
                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de tu padre", "warning");
                                } else if (!re.test(String($scope.properties.padre.correoElectronico.trim()).toLowerCase())) {
                                    swal("¡Correo electrónico!", "El correo electrónico de tu padre no es válido", "warning");
                                } else if ($scope.properties.padre.catEscolaridad === 0 || $scope.properties.padre.catEscolaridad === null) {
                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de tu padre", "warning");
                                } else if (!$scope.properties.padre.telefonoCelular) {
                                    swal("¡Teléfono celular!", "Debes agregar el teléfono celular de tu padre", "warning");
                                } else if ($scope.properties.padre.catPais === 0 || $scope.properties.padre.catPais === null) {
                                    swal("¡País!", "Debes agregar el país del domicilio de tu padre", "warning");
                                } else if (($scope.properties.padre.codigoPostal === "" || $scope.properties.padre.codigoPostal === undefined) && $scope.properties.padre.catPais.descripcion === "México") {
                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de tu padre", "warning");
                                } else if (($scope.properties.padre.catEstado === 0 || $scope.properties.padre.catEstado === null) && $scope.properties.padre.catPais.descripcion === "México") {
                                    swal("¡Estado!", "Debes agregar el estado del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.estadoExtranjero === "" && $scope.properties.padre.catPais.descripcion !== "México") {
                                    swal("¡Estado!", "Debes agregar el estado del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.ciudad === "" || $scope.properties.padre.ciudad === undefined) {
                                    swal("¡Ciudad!", "Debes agregar la ciudad del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.delegacionMunicipio === "" || $scope.properties.padre.delegacionMunicipio === null) {
                                    swal("¡Municipio/Delegación/Poblado!", "Debes agregar una Ciudad/Municipio/Delegación/Poblado del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.colonia === "" || $scope.properties.padre.colonia === undefined) {
                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.calle === "" || $scope.properties.padre.calle === undefined) {
                                    swal("¡Calle!", "Debes agregar la calle del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.numeroExterior === "" || $scope.properties.padre.numeroExterior === undefined) {
                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de tu padre", "warning");
                                } else if ($scope.properties.padre.telefono === "" || $scope.properties.padre.telefono === undefined) {
                                    swal("¡Teléfono!", "Debes agregar el teléfono de tu padre", "warning");
                                }else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.cp = undefined;
                                        topFunction();
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.padre.catTrabaja === 0 || $scope.properties.padre.catTrabaja === null) {
                            swal("¿Trabaja?", "Debes seleccionar si tu padre trabaja", "warning");
                        } else if ($scope.properties.datosPadres.padretrabaja) {
                            if ($scope.properties.padre.empresaTrabaja === "" || $scope.properties.padre.empresaTrabaja === undefined) {
                                swal("¡Empresa!", "Debes agregar el nombre del empresa donde tu padre trabaja", "warning");
                            } else if ($scope.properties.padre.giroEmpresa === "" || $scope.properties.padre.giroEmpresa === undefined) {
                                swal("¡Giro empresa!!", "Debes agregar el giro de la empresa del de trabajo de tu padre!", "warning");
                            } else if ($scope.properties.padre.puesto === "" || $scope.properties.padre.puesto === undefined) {
                                swal("¡Puesto!", "Debes agregar el puesto de trabajo de tu padre", "warning");
                            } else if ($scope.properties.padre.correoElectronico === "" || $scope.properties.padre.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de tu padre", "warning");
                            } else if (!re.test(String($scope.properties.padre.correoElectronico.trim()).toLowerCase())) {
                                swal("¡Correo electrónico!", "El correo electrónico de tu padre no es válido", "warning");
                            } else if ($scope.properties.padre.catEscolaridad === 0 || $scope.properties.padre.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de tu padre", "warning");
                            } else if (!$scope.properties.padre.telefonoCelular) {
                                swal("¡Teléfono celular!", "Debes agregar el teléfono celular de tu padre", "warning");
                            } else if ($scope.properties.padre.catPais === 0 || $scope.properties.padre.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio de tu padre", "warning");
                            } else if (($scope.properties.padre.codigoPostal === "" || $scope.properties.padre.codigoPostal === undefined) && $scope.properties.padre.catPais.descripcion === "México") {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de tu padre", "warning");
                            } else if (($scope.properties.padre.catEstado === 0 || $scope.properties.padre.catEstado === null) && $scope.properties.padre.catPais.descripcion === "México") {
                                swal("¡Estado!", "Debes agregar el estado del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.estadoExtranjero === "" && $scope.properties.padre.catPais.descripcion !== "México") {
                                swal("¡Estado!", "Debes agregar el estado del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.ciudad === "" || $scope.properties.padre.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la ciudad del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.delegacionMunicipio === "" || $scope.properties.padre.delegacionMunicipio === null) {
                                swal("¡Municipio/Delegación/Poblado!", "Debes agregar una Ciudad/Municipio/Delegación/Poblado del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.colonia === "" || $scope.properties.padre.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.calle === "" || $scope.properties.padre.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.numeroExterior === "" || $scope.properties.padre.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.telefono === "" || $scope.properties.padre.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono de tu padre", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.cp = undefined;
                                    topFunction();
                                    $scope.properties.selectedIndex++;
                                    //$scope.assignTask();
                                }
                            }
                        } else {
                            if ($scope.properties.padre.correoElectronico === "" || $scope.properties.padre.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de tu padre", "warning");
                            } else if (!re.test(String($scope.properties.padre.correoElectronico.trim()).toLowerCase())) {
                                swal("¡Correo electrónico!", "El correo electrónico de tu padre no es válido", "warning");
                            } else if ($scope.properties.padre.catEscolaridad === 0 || $scope.properties.padre.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de tu padre", "warning");
                            } else if (!$scope.properties.padre.telefonoCelular) {
                                swal("¡Teléfono celular!", "Debes agregar el teléfono celular de tu padre", "warning");
                            } else if ($scope.properties.padre.catPais === 0 || $scope.properties.padre.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio de tu padre", "warning");
                            } else if (($scope.properties.padre.codigoPostal === "" || $scope.properties.padre.codigoPostal === undefined) && $scope.properties.padre.catPais.descripcion === "México") {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de tu padre", "warning");
                            } else if (($scope.properties.padre.catEstado === 0 || $scope.properties.padre.catEstado === null) && $scope.properties.padre.catPais.descripcion === "México") {
                                swal("¡Estado!", "Debes agregar el estado del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.estadoExtranjero === "" && $scope.properties.padre.catPais.descripcion !== "México") {
                                swal("¡Estado!", "Debes agregar el estado del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.ciudad === "" || $scope.properties.padre.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la ciudad del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.delegacionMunicipio === "" || $scope.properties.padre.delegacionMunicipio === null) {
                                swal("¡Municipio/Delegación/Poblado!", "Debes agregar una Ciudad/Municipio/Delegación/Poblado del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.colonia === "" || $scope.properties.padre.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.calle === "" || $scope.properties.padre.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.numeroExterior === "" || $scope.properties.padre.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de tu padre", "warning");
                            } else if ($scope.properties.padre.telefono === "" || $scope.properties.padre.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono de tu padre", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.cp = undefined;
                                    topFunction();
                                    $scope.properties.selectedIndex++;
                                    //$scope.assignTask();
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.cp = undefined;
                        topFunction();
                        $scope.properties.selectedIndex++;
                        //$scope.assignTask();
                    }
                }
            } else if ($scope.properties.selectedIndex === 2) {
                console.log("valida a la madre")
                if ($scope.properties.madre.desconozcoDatosPadres) {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        //$scope.properties.cp = undefined;
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.cp = undefined;
                        topFunction();
                        $scope.properties.selectedIndex++;
                        //$scope.assignTask();
                    }
                } else if ($scope.properties.madre.vive === 0 || $scope.properties.madre.vive === null) {
                    swal("¡Madre vive!", "Debes seleccionar si la madre vive", "warning");
                } else if ($scope.properties.madre.catTitulo === 0 || $scope.properties.madre.catTitulo === null) {
                    swal("¡Título!", "Debes seleccionar el título para identificar a la madre", "warning");
                } else if ($scope.properties.madre.nombre === "" || $scope.properties.madre.nombre === undefined) {
                    swal("¡Nombre de tu madre!", "Debes agregar nombre de tu madre", "warning");
                } else if ($scope.properties.madre.apellidos === "" || $scope.properties.madre.apellidos === undefined) {
                    swal("¡Apellidos de tu madre!", "Debes agregar los apellidos de tu madre", "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.madre.catEgresoAnahuac === 0 || $scope.properties.madre.catEgresoAnahuac === null) {
                        swal("¡Egreso Anáhuac!", "Debes seleccionar si tu madre egreso de la universidad Anáhuac", "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.madre.catCampusEgreso === 0 || $scope.properties.madre.catCampusEgreso === null) {
                            swal("¡Campus egresado!", "Debes seleccionar de que campus Anáhuac egresó tu madre", "warning");
                        } else {
                            if (!$scope.properties.madre.telefonoCelular) {
                                swal("¡Teléfono celular!", "Debes agregar el teléfono de tu madre", "warning");
                            } else if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                                swal("¿Trabaja?", "Debes seleccionar si tu madre trabaja", "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                    swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                                } else if ($scope.properties.madre.giroEmpresa === "" || $scope.properties.madre.giroEmpresa === undefined) {
                                    swal("¡Giro empresa!!", "Debes agregar el giro de la empresa del de trabajo de tu madre!", "warning");
                                } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                    swal("¡Puesto!", "Debes agregar el puesto de trabajo de tu madre", "warning");
                                } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de tu madre", "warning");
                                } else if (!re.test(String($scope.properties.madre.correoElectronico.trim()).toLowerCase())) {
                                    swal("¡Correo electrónico!", "El correo electrónico de tu madre no es válido", "warning");
                                } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de tu madre", "warning");
                                } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                    swal("¡País!", "Debes agregar el país del domicilio de tu madre", "warning");
                                } else if (($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) && $scope.properties.madre.catPais.descripcion === "México") {
                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de tu madre", "warning");
                                } else if (($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) && $scope.properties.madre.catPais.descripcion === "México") {
                                    swal("¡Estado!", "Debes agregar el estado del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.estadoExtranjero === "" && $scope.properties.madre.catPais.descripcion !== "México") {
                                    swal("¡Estado!", "Debes agregar el estado del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                    swal("¡Ciudad!", "Debes agregar la ciudad del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.delegacionMunicipio === "" || $scope.properties.madre.delegacionMunicipio === null) {
                                    swal("¡Municipio/Delegación/Poblado!", "Debes agregar una Ciudad/Municipio/Delegación/Poblado del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                    swal("¡Calle!", "Debes agregar la calle del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                    swal("¡Teléfono!", "Debes agregar el teléfono de tu madre", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.cp = undefined;
                                        topFunction();
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                    }
                                }
                            } else {
                                if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                    swal("¡Correo electrónico!", "Debes agregar el correo electrónico de tu madre", "warning");
                                } else if (!re.test(String($scope.properties.madre.correoElectronico.trim()).toLowerCase())) {
                                    swal("¡Correo electrónico!", "El correo electrónico de tu madre no es válido", "warning");
                                } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                    swal("¡Escolaridad!", "Debes seleccionar la escolaridad de tu madre", "warning");
                                } else if (!$scope.properties.madre.telefonoCelular) {
                                    swal("¡Teléfono celular!", "Debes agregar el teléfono de tu madre", "warning");
                                } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                    swal("¡País!", "Debes agregar el país del domicilio de tu madre", "warning");
                                } else if (($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) && $scope.properties.madre.catPais.descripcion === "México") {
                                    swal("¡Código postal!", "Debes agregar el código postal del domicilio de tu madre", "warning");
                                } else if (($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) && $scope.properties.madre.catPais.descripcion === "México") {
                                    swal("¡Estado!", "Debes agregar el estado del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.estadoExtranjero === "" && $scope.properties.madre.catPais.descripcion !== "México") {
                                    swal("¡Estado!", "Debes agregar el estado del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                    swal("¡Ciudad!", "Debes agregar la ciudad del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.delegacionMunicipio === "" || $scope.properties.madre.delegacionMunicipio === null) {
                                    swal("¡Municipio/Delegación/Poblado!", "Debes agregar una Ciudad/Municipio/Delegación/Poblado del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                    swal("¡Colonia!", "Debes agregar la colonia del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                    swal("¡Calle!", "Debes agregar la calle del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                    swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de tu madre", "warning");
                                } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                    swal("¡Teléfono!", "Debes agregar el teléfono de tu madre", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.cp = undefined;
                                        topFunction();
                                        $scope.properties.selectedIndex++;
                                        //$scope.assignTask();
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.madre.catTrabaja === 0 || $scope.properties.madre.catTrabaja === null) {
                            swal("¿Trabaja?", "Debes seleccionar si tu madre trabaja", "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.madre.empresaTrabaja === "" || $scope.properties.madre.empresaTrabaja === undefined) {
                                swal("¡Empresa!", "Debes agregar el nombre de la empresa donde tu madre trabaja", "warning");
                            } else if ($scope.properties.madre.giroEmpresa === "" || $scope.properties.madre.giroEmpresa === undefined) {
                                swal("¡Giro empresa!!", "Debes agregar el giro de la empresa del de trabajo de tu madre!", "warning");
                            } else if ($scope.properties.madre.puesto === "" || $scope.properties.madre.puesto === undefined) {
                                swal("¡Puesto!", "Debes agregar el puesto de trabajo de tu madre", "warning");
                            } else if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de tu madre", "warning");
                            } else if (!re.test(String($scope.properties.madre.correoElectronico.trim()).toLowerCase())) {
                                swal("¡Correo electrónico!", "El correo electrónico de tu madre no es válido", "warning");
                            } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de tu madre", "warning");
                            } else if (!$scope.properties.madre.telefonoCelular) {
                                swal("¡Teléfono celular!", "Debes agregar el teléfono de tu madre", "warning");
                            } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio de tu madre", "warning");
                            } else if (($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) && $scope.properties.madre.catPais.descripcion === "México") {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de tu madre", "warning");
                            } else if (($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) && $scope.properties.madre.catPais.descripcion === "México") {
                                swal("¡Estado!", "Debes agregar el estado del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.estadoExtranjero === "" && $scope.properties.madre.catPais.descripcion !== "México") {
                                swal("¡Estado!", "Debes agregar el estado del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la ciudad del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.delegacionMunicipio === "" || $scope.properties.madre.delegacionMunicipio === null) {
                                swal("¡Municipio/Delegación/Poblado!", "Debes agregar una Ciudad/Municipio/Delegación/Poblado del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono de tu madre", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.cp = undefined;
                                    topFunction();
                                    $scope.properties.selectedIndex++;
                                    //$scope.assignTask();
                                }
                            }
                        } else {
                            if ($scope.properties.madre.correoElectronico === "" || $scope.properties.madre.correoElectronico === undefined) {
                                swal("¡Correo electrónico!", "Debes agregar el correo electrónico de tu madre", "warning");
                            } else if (!re.test(String($scope.properties.madre.correoElectronico.trim()).toLowerCase())) {
                                swal("¡Correo electrónico!", "El correo electrónico de tu madre no es válido", "warning");
                            } else if ($scope.properties.madre.catEscolaridad === 0 || $scope.properties.madre.catEscolaridad === null) {
                                swal("¡Escolaridad!", "Debes seleccionar la escolaridad de tu madre", "warning");
                            } else if (!$scope.properties.madre.telefonoCelular) {
                                swal("¡Teléfono celular!", "Debes agregar el teléfono de tu madre", "warning");
                            } else if ($scope.properties.madre.catPais === 0 || $scope.properties.madre.catPais === null) {
                                swal("¡País!", "Debes agregar el país del domicilio de tu madre", "warning");
                            } else if (($scope.properties.madre.codigoPostal === "" || $scope.properties.madre.codigoPostal === undefined) && $scope.properties.madre.catPais.descripcion === "México") {
                                swal("¡Código postal!", "Debes agregar el código postal del domicilio de tu madre", "warning");
                            } else if (($scope.properties.madre.catEstado === 0 || $scope.properties.madre.catEstado === null) && $scope.properties.madre.catPais.descripcion === "México") {
                                swal("¡Estado!", "Debes agregar el estado del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.estadoExtranjero === "" && $scope.properties.madre.catPais.descripcion !== "México") {
                                swal("¡Estado!", "Debes agregar el estado del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.ciudad === "" || $scope.properties.madre.ciudad === undefined) {
                                swal("¡Ciudad!", "Debes agregar la ciudad del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.delegacionMunicipio === "" || $scope.properties.madre.delegacionMunicipio === null) {
                                swal("¡Municipio/Delegación/Poblado!", "Debes agregar una Ciudad/Municipio/Delegación/Poblado del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.colonia === "" || $scope.properties.madre.colonia === undefined) {
                                swal("¡Colonia!", "Debes agregar la colonia del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.calle === "" || $scope.properties.madre.calle === undefined) {
                                swal("¡Calle!", "Debes agregar la calle del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.numeroExterior === "" || $scope.properties.madre.numeroExterior === undefined) {
                                swal("¡Número exterior!", "Debes agregar el número exterior del domicilio de tu madre", "warning");
                            } else if ($scope.properties.madre.telefono === "" || $scope.properties.madre.telefono === undefined) {
                                swal("¡Teléfono!", "Debes agregar el teléfono de tu madre", "warning");
                            }  else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.cp = undefined;
                                    topFunction();
                                    $scope.properties.selectedIndex++;
                                    //$scope.assignTask();
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.cp = undefined;
                        topFunction();
                        $scope.properties.selectedIndex++;
                        //$scope.assignTask();
                    }
                }

            } else if ($scope.properties.selectedIndex === 3) {
                console.log("validar 3");
                if ($scope.properties.contactoEmergencia.length === 0) {
                    swal("¡Contacto de emergencia!", "Debes agregar al menos un contacto de emergencia", "warning");
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        topFunction();
                        $scope.properties.selectedIndex++;
                        //$scope.assignTask();
                    }
                }
            }
        } else if ($scope.properties.tabs === "Revisa Solicitud") {
            if ($scope.properties.selectedIndex === 0) {
                console.log("validar 0");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    topFunction();
                    $scope.properties.selectedIndex++;
                    //$scope.assignTask();
                }

            } else if ($scope.properties.selectedIndex === 1) {
                console.log("validar 1");
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                    $scope.properties.selectedIndex--;
                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                    topFunction();
                    $scope.properties.selectedIndex++;
                    //$scope.assignTask();
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
        let url = "../API/bpm/userTask/" + $scope.properties.taskId;
        var req2 = {
            method: "PUT",
            url: url,
            data: {
                "assigned_id": $scope.properties.userId
            }
        };

        return $http(req2).success(function(data, status) {
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
            $scope.properties.dataToSend.catSolicitudDeAdmisionInput.catBachilleratos.persistenceId_string = $scope.properties.Bachilleratopersistenceid;
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
                topFunction();
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

    function getTask() {
        setTimeout(function() {
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