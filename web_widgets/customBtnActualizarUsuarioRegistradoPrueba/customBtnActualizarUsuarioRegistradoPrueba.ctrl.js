function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {

    'use strict';

    var vm = this;

    this.action = function action() {



        if ($scope.properties.action === 'Remove from collection') {
            removeFromCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Add to collection') {
            addToCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Start process') {
            startProcess();
        } else if ($scope.properties.action === 'Submit task') {
            submitTask();
        } else if ($scope.properties.action === 'Open modal') {
            closeModal($scope.properties.closeOnSuccess);
            openModal($scope.properties.modalId);
        } else if ($scope.properties.action === 'Close modal') {
            closeModal(true);
        } else if ($scope.properties.url) {

            var existecambio = false;
            const re = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
            const regexEmail = "^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$";
            debugger;
            if ($scope.properties.editarSec1 == false) {
                if ($scope.properties.objSolicitudDeAdmision.catCampusEstudio === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar el campus donde se cursarán sus estudios.' | translate }}", "warning");
                    return;
                } else if ($scope.properties.objSolicitudDeAdmision.catGestionEscolar === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar una licenciatura.' | translate }}", "warning");
                    return;
                    /*}else if($scope.properties.objSolicitudDeAdmision.catPeriodo === null){
                        swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar un periodo", "warning");*/
                } else if ($scope.properties.objSolicitudDeAdmision.catPeriodo === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar el periodo de ingreso.' | translate }}", "warning");
                    return;
                } else if ($scope.properties.objSolicitudDeAdmision.catCampus === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar el campus de examen.' | translate }}", "warning");
                    return;
                } else if ($scope.properties.objSolicitudDeAdmision.catPresentasteEnOtroCampus === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar si has presentado solicitud de admisión en otra universidad.' | translate }}", "warning");
                    return;
                } else if ($scope.properties.objSolicitudDeAdmision.correoElectronico === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar correo electrónico.' | translate }}", "warning");
                    return;
                } else {
                    $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                    $scope.properties.JSONUsuarioRegistrado.catcampusestudio_pid = $scope.properties.objSolicitudDeAdmision.catCampusEstudio.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.catgestionescolar_pid = $scope.properties.objSolicitudDeAdmision.catGestionEscolar.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.catperiodo_pid = $scope.properties.objSolicitudDeAdmision.catPeriodo.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.catcampus_pid = $scope.properties.objSolicitudDeAdmision.catCampus.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.catpresentasteenotrocampus_pid = $scope.properties.objSolicitudDeAdmision.catPresentasteEnOtroCampus.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.correoelectronico = $scope.properties.objSolicitudDeAdmision.correoElectronico;
                    if ($scope.properties.objSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion == "No") {
                        $scope.properties.JSONUsuarioRegistrado.catconcluisteproceso_pid = null;
                        $scope.properties.JSONUsuarioRegistrado.catresultadoadmision_pid = null;
                        $scope.properties.JSONUsuarioRegistrado.catcampuspresentadosolicitud = null;
                    } else {
                        $scope.properties.JSONUsuarioRegistrado.catconcluisteproceso_pid = $scope.properties.objSolicitudDeAdmision.catConcluisteProceso ? $scope.properties.objSolicitudDeAdmision.catConcluisteProceso.persistenceId : null;
                        if ($scope.properties.objSolicitudDeAdmision.catConcluisteProceso === null) {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar concluiste proceso de admisión.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.catresultadoadmision_pid = $scope.properties.objSolicitudDeAdmision.catResultadoAdmision ? $scope.properties.objSolicitudDeAdmision.catResultadoAdmision.persistenceId : null;
                        if ($scope.properties.objSolicitudDeAdmision.catResultadoAdmision === null) {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar el resultado de la admisión.' | translate }}", "warning");
                            return;
                        }
                        //$scope.properties.JSONUsuarioRegistrado.catcampuspresentadosolicitud = $scope.properties.objSolicitudDeAdmision.catCampusPresentadoSolicitud;
                    }
                }
            } else if ($scope.properties.editarSec2 == false) {
                if ($scope.properties.objSolicitudDeAdmision.primerNombre === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar el primer nombre.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.apellidoPaterno === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar el apellido paterno.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.fechaNacimiento === "" || $scope.properties.objSolicitudDeAdmision.fechaNacimiento === undefined || $scope.properties.objSolicitudDeAdmision.fechaNacimiento === null) {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar la fecha de nacimiento.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.catNacionalidad === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar nacionalidad.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.catReligion === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar religión.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.curp === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar CURP.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.telefonoCelular === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar teléfono celular.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.catSexo === null) {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar el sexo.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.catEstado === null && $scope.properties.objSolicitudDeAdmision.estadoextranjero === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar el estado.' | translate }}", "warning");
                    /*}else if($scope.properties.objSolicitudDeAdmision.estadoextranjero === ""){
                        swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar el estado", "warning");*/
                } else {
                    $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                    $scope.properties.JSONUsuarioRegistrado.primernombre = $scope.properties.objSolicitudDeAdmision.primerNombre;
                    $scope.properties.JSONUsuarioRegistrado.segundonombre = $scope.properties.objSolicitudDeAdmision.segundoNombre;
                    $scope.properties.JSONUsuarioRegistrado.apellidopaterno = $scope.properties.objSolicitudDeAdmision.apellidoPaterno;
                    $scope.properties.JSONUsuarioRegistrado.apellidomaterno = $scope.properties.objSolicitudDeAdmision.apellidoMaterno;
                    $scope.properties.JSONUsuarioRegistrado.catsexo_pid = $scope.properties.objSolicitudDeAdmision.catSexo.persistenceId;
                    let d = new Date($scope.properties.objSolicitudDeAdmision.fechaNacimiento.toString())
                    let formatted_date = d.getFullYear() + "-" + ((d.getMonth() + 1) < 10 ? "0" + (d.getMonth() + 1) : (d.getMonth() + 1)) + "-" + (d.getDate() < 10 ? "0" + d.getDate() : d.getDate())
                    formatted_date += "t05:00:00.000";
                    $scope.properties.JSONUsuarioRegistrado.fechanacimiento = formatted_date;
                    if ($scope.properties.objSolicitudDeAdmision.catEstado === null || $scope.properties.objSolicitudDeAdmision.catEstado === undefined) {
                        $scope.properties.JSONUsuarioRegistrado.catestado_pid = null;
                        $scope.properties.JSONUsuarioRegistrado.estadoextranjero = $scope.properties.objSolicitudDeAdmision.estadoextranjero;
                    } else {
                        $scope.properties.JSONUsuarioRegistrado.catestado_pid = $scope.properties.objSolicitudDeAdmision.catEstado.persistenceId;
                        $scope.properties.JSONUsuarioRegistrado.estadoextranjero = "";
                        /*if ($scope.properties.objSolicitudDeAdmision.catEstado.descripcion !== $scope.properties.jsonOriginal.estado) {
                            existecambio = true;
                        }*/
                    }
                    $scope.properties.JSONUsuarioRegistrado.catnacionalidad_pid = $scope.properties.objSolicitudDeAdmision.catNacionalidad.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.catreligion_pid = $scope.properties.objSolicitudDeAdmision.catReligion.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.curp = $scope.properties.objSolicitudDeAdmision.curp;
                    $scope.properties.JSONUsuarioRegistrado.telefonocelular = $scope.properties.objSolicitudDeAdmision.telefonoCelular;
                    $scope.properties.JSONUsuarioRegistrado.catestadocivil_pid = $scope.properties.objSolicitudDeAdmision.catEstadoCivil.persistenceId;
                }
            } else if ($scope.properties.editarSec3 == false) {
                if ($scope.properties.objSolicitudDeAdmision.catPais === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar país.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.codigoPostal === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar codigo postal.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.catEstado === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar estado.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.ciudad === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar ciudad.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.colonia === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar colonia.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.calle === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar calle.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.numExterior === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar número exterior.' | translate }}", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.telefono === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar teléfono.' | translate }}", "warning");
                } else {
                    $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                    $scope.properties.JSONUsuarioRegistrado.catpais_pid = $scope.properties.objSolicitudDeAdmision.catPais.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.codigopostal = $scope.properties.objSolicitudDeAdmision.codigoPostal;
                    $scope.properties.JSONUsuarioRegistrado.catestado_pid = $scope.properties.objSolicitudDeAdmision.catEstado.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.ciudad = $scope.properties.objSolicitudDeAdmision.ciudad;
                    $scope.properties.JSONUsuarioRegistrado.delegacionmunicipio = $scope.properties.objSolicitudDeAdmision.delegacionMunicipio;
                    $scope.properties.JSONUsuarioRegistrado.colonia = $scope.properties.objSolicitudDeAdmision.colonia;
                    $scope.properties.JSONUsuarioRegistrado.calle = $scope.properties.objSolicitudDeAdmision.calle;
                    $scope.properties.JSONUsuarioRegistrado.calle2 = $scope.properties.objSolicitudDeAdmision.calle2;
                    $scope.properties.JSONUsuarioRegistrado.numexterior = $scope.properties.objSolicitudDeAdmision.numExterior;
                    $scope.properties.JSONUsuarioRegistrado.numinterior = $scope.properties.objSolicitudDeAdmision.numInterior;
                    $scope.properties.JSONUsuarioRegistrado.telefono = $scope.properties.objSolicitudDeAdmision.telefono;
                    $scope.properties.JSONUsuarioRegistrado.otrotelefonocontacto = $scope.properties.objSolicitudDeAdmision.otroTelefonoContacto;
                }
            } else if ($scope.properties.editarSec4 == false) {
                if (!$scope.properties.objSolicitudDeAdmision.catBachilleratos || $scope.properties.objSolicitudDeAdmision.catBachilleratos === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar bachillerato.' | translate }}", "warning");
                    return;
                } else if (!$scope.properties.objSolicitudDeAdmision.bachillerato || $scope.properties.objSolicitudDeAdmision.bachillerato === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar nombre del bachillerato.' | translate }}", "warning");
                    return;
                } else if (!$scope.properties.datosPreparatoria.estadoBachillerato || $scope.properties.datosPreparatoria.estadoBachillerato === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar estado de bachillerato.' | translate }}", "warning");
                    return;
                } else if (!$scope.properties.datosPreparatoria.paisBachillerato || $scope.properties.datosPreparatoria.paisBachillerato === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar país de bachillerato.' | translate }}", "warning");
                    return;
                } else if (!$scope.properties.datosPreparatoria.ciudadBachillerato || $scope.properties.datosPreparatoria.ciudadBachillerato === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar ciudad de bachillerato.' | translate }}", "warning");
                    return;
                } else if (!$scope.properties.objSolicitudDeAdmision.promedioGeneral || $scope.properties.objSolicitudDeAdmision.promedioGeneral === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar promedio general.' | translate }}", "warning");
                    return;
                } else {
                    $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                    $scope.properties.JSONUsuarioRegistrado.catbachilleratos_pid = parseInt($scope.properties.objSolicitudDeAdmision.catBachilleratos.persistenceid);
                    if ($scope.properties.objSolicitudDeAdmision.catBachilleratos.descripcion == "Otro") {
                        $scope.properties.JSONUsuarioRegistrado.bachillerato = $scope.properties.objSolicitudDeAdmision.bachillerato ? $scope.properties.objSolicitudDeAdmision.bachillerato : null;
                    }
                    $scope.properties.JSONUsuarioRegistrado.estado = $scope.properties.datosPreparatoria.estadoBachillerato;
                    $scope.properties.JSONUsuarioRegistrado.pais = $scope.properties.datosPreparatoria.paisBachillerato;
                    $scope.properties.JSONUsuarioRegistrado.ciudad = $scope.properties.datosPreparatoria.ciudadBachillerato;
                    $scope.properties.JSONUsuarioRegistrado.promediogeneral = $scope.properties.objSolicitudDeAdmision.promedioGeneral;
                }
            } else if ($scope.properties.editarSec5 == false) {
                if ($scope.properties.datosJson.catTitulo === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar título.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.catParentezco === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar parentesco.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.nombre === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar nombre.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.apellidos === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar apellido.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.correoElectronico === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar correo electrónico.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.catEgresoAnahuac === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar egresó de la universidad Anahuac.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.catTrabaja === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar tutor trabaja.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.catEscolaridad === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar escolaridad del tutor.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.catPais === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar país.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.codigoPostal === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar código postal.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.catEstado === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar estado.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.ciudad === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar ciudad.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.colonia === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar colonia.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.calle === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar calle.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.numeroExterior === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar número exterior.' | translate }}", "warning");
                } else if ($scope.properties.datosJson.telefono === "") {
                    swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar teléfono.' | translate }}", "warning");
                } else {
                    $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                    if ($scope.properties.datosJson.catParentezco.descripcion == "Padre") {
                        $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objPadre.persistenceId;
                    } else if ($scope.properties.datosJson.catParentezco.descripcion == "Madre") {
                        $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objMadre.persistenceId;
                    }
                    $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                    $scope.properties.JSONUsuarioRegistrado.cattitulo_pid = parseInt($scope.properties.datosJson.catTitulo.persistenceId);
                    $scope.properties.JSONUsuarioRegistrado.catparentezco_pid = parseInt($scope.properties.datosJson.catParentezco.persistenceId);
                    $scope.properties.JSONUsuarioRegistrado.nombre = $scope.properties.datosJson.nombre;
                    if ($scope.properties.datosJson.catParentezco.descripcion == "Otro") {
                        $scope.properties.JSONUsuarioRegistrado.otroparentesco = $scope.properties.datosJson.otroParentesco.descripcion;
                    }
                    $scope.properties.JSONUsuarioRegistrado.apellidos = $scope.properties.datosJson.apellidos;
                    $scope.properties.JSONUsuarioRegistrado.correoelectronico = $scope.properties.datosJson.correoElectronico;
                    $scope.properties.JSONUsuarioRegistrado.categresoanahuac_pid = parseInt($scope.properties.datosJson.catEgresoAnahuac.persistenceId);
                    $scope.properties.JSONUsuarioRegistrado.cattrabaja_pid = parseInt($scope.properties.datosJson.catTrabaja.persistenceId);
                    $scope.properties.JSONUsuarioRegistrado.catcampusegreso_pid = parseInt($scope.properties.datosJson.catCampusEgreso.persistenceId);
                    if ($scope.properties.datosJson.catEgresoAnahuac.descripcion === "Sí") {
                        if ($scope.properties.datosJson.catCampusEgreso.descripcion === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar campus de egreso.' | translate }}", "warning");
                            return;
                        }
                    }
                    $scope.properties.JSONUsuarioRegistrado.empresatrabaja = $scope.properties.datosJson.empresaTrabaja;
                    if ($scope.properties.datosJson.catTrabaja.descripcion == "Sí") {
                        if ($scope.properties.datosJson.empresaTrabaja === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar empresa en la que trabaja.' | translate }}", "warning");
                            return;
                        }
                    }
                    $scope.properties.JSONUsuarioRegistrado.catescolaridad_pid = parseInt($scope.properties.datosJson.catEscolaridad.persistenceId);
                    $scope.properties.JSONUsuarioRegistrado.giroempresa = $scope.properties.datosJson.giroEmpresa;
                    if ($scope.properties.datosJson.catTrabaja.descripcion == "Sí") {
                        if ($scope.properties.datosJson.giroEmpresa === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar giro de la empresa.' | translate }}", "warning");
                            return;
                        }
                    }
                    $scope.properties.JSONUsuarioRegistrado.puesto = $scope.properties.datosJson.puesto;
                    if ($scope.properties.datosJson.catTrabaja.descripcion == "Sí") {
                        if ($scope.properties.datosJson.puesto === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar puesto.' | translate }}", "warning");
                            return;
                        }
                    }
                    $scope.properties.JSONUsuarioRegistrado.catpais_pid = parseInt($scope.properties.datosJson.catPais.persistenceId);
                    $scope.properties.JSONUsuarioRegistrado.codigopostal = $scope.properties.datosJson.codigoPostal;
                    $scope.properties.JSONUsuarioRegistrado.catestado_pid = parseInt($scope.properties.datosJson.catEstado.persistenceId);
                    if ($scope.properties.datosJson.catPais.descripcion != "México") {
                        $scope.properties.JSONUsuarioRegistrado.estadoextranjero = $scope.properties.datosJson.estadoExtranjero.descripcion;
                    }
                    $scope.properties.JSONUsuarioRegistrado.ciudad = $scope.properties.datosJson.ciudad;
                    $scope.properties.JSONUsuarioRegistrado.delegacionmunicipio = $scope.properties.datosJson.delegacionMunicipio;
                    $scope.properties.JSONUsuarioRegistrado.colonia = $scope.properties.datosJson.colonia;
                    $scope.properties.JSONUsuarioRegistrado.calle = $scope.properties.datosJson.calle;
                    if ($scope.properties.datosJson.numeroExterior != null) {
                        $scope.properties.JSONUsuarioRegistrado.numeroexterior = $scope.properties.datosJson.numeroExterior.descripcion;
                    }
                    if ($scope.properties.datosJson.numeroInterior != null) {
                        $scope.properties.JSONUsuarioRegistrado.numerointerior = $scope.properties.datosJson.numeroInterior.descripcion;
                    }
                    $scope.properties.JSONUsuarioRegistrado.telefono = $scope.properties.datosJson.telefono;
                }
            } else if ($scope.properties.editarSec6 == false) {
                debugger;
                $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objPadre.persistenceId;
                $scope.properties.JSONUsuarioRegistrado.desconozcodatospadres = $scope.properties.objPadre.desconozcoDatosPadres;
                if ($scope.properties.objPadre.desconozcoDatosPadres == false) {
                    $scope.properties.JSONUsuarioRegistrado.vive_pid = parseInt($scope.properties.objPadre.vive.persistenceId);
                    $scope.properties.JSONUsuarioRegistrado.cattitulo_pid = parseInt($scope.properties.objPadre.catTitulo.persistenceId);
                    $scope.properties.JSONUsuarioRegistrado.nombre = $scope.properties.objPadre.nombre;
                    $scope.properties.JSONUsuarioRegistrado.apellidos = $scope.properties.objPadre.apellidos;
                    if ($scope.properties.objPadre.vive === "") {
                        swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar vive.' | translate }}", "warning");
                        return;
                    } else if ($scope.properties.objPadre.catTitulo === "") {
                        swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar título.' | translate }}", "warning");
                        return;
                    } else if ($scope.properties.objPadre.nombre === "") {
                        swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar nombre.' | translate }}", "warning");
                        return;
                    } else if ($scope.properties.objPadre.apellidos === "") {
                        swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar apellido.' | translate }}", "warning");
                        return;
                    }
                    if ($scope.properties.objPadre.vive.descripcion == "Sí") {
                        $scope.properties.JSONUsuarioRegistrado.categresoanahuac_pid = $scope.properties.objPadre.catEgresoAnahuac !== undefined ? parseInt($scope.properties.objPadre.catEgresoAnahuac.persistenceId) : null;
                        if ($scope.properties.objPadre.catEgresoAnahuac === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar egresó de la universidad Anahuac.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.cattrabaja_pid = $scope.properties.objPadre.catTrabaja !== undefined ? parseInt($scope.properties.objPadre.catTrabaja.persistenceId) : null;
                        if ($scope.properties.objPadre.catTrabaja === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar padre trabaja.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.catcampusegreso_pid = $scope.properties.objPadre.catCampusEgreso.persistenceId !== undefined && $scope.properties.objPadre.catCampusEgreso.persistenceId !== null ? parseInt($scope.properties.objPadre.catCampusEgreso.persistenceId) : null;
                        if ($scope.properties.objPadre.catCampusEgreso === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar campus de egreso.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.correoelectronico = $scope.properties.objPadre.correoElectronico !== undefined ? $scope.properties.objPadre.correoElectronico : null;
                        if ($scope.properties.objPadre.correoElectronico === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar correo electrónico.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.empresatrabaja = $scope.properties.objPadre.empresaTrabaja !== undefined ? $scope.properties.objPadre.empresaTrabaja : null;
                        if ($scope.properties.objPadre.empresaTrabaja === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar empresa en la que trabaja.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.giroempresa = $scope.properties.objPadre.giroEmpresa !== undefined ? $scope.properties.objPadre.giroEmpresa : null;
                        if ($scope.properties.objPadre.giroEmpresa === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar giro de la empresa.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.catescolaridad_pid = $scope.properties.objPadre.catEscolaridad !== undefined ? parseInt($scope.properties.objPadre.catEscolaridad.persistenceId) : null;
                        if ($scope.properties.objPadre.catEscolaridad === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar escolaridad del padre.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.puesto = $scope.properties.objPadre.puesto !== undefined ? $scope.properties.objPadre.puesto : null;
                        if ($scope.properties.objPadre.puesto === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar puesto.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.vivecontigo = $scope.properties.objPadre.viveContigo;
                        $scope.properties.JSONUsuarioRegistrado.catpais_pid = $scope.properties.objPadre.catPais.persistenceId !== undefined ? parseInt($scope.properties.objPadre.catPais.persistenceId) : null;
                        if ($scope.properties.objPadre.catPais === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar país.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.codigopostal = $scope.properties.objPadre.codigoPostal !== undefined ? $scope.properties.objPadre.codigoPostal : null;
                        if ($scope.properties.objPadre.codigoPostal === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar código postal.' | translate }}", "warning");
                            return;
                        }
                        //$scope.properties.JSONUsuarioRegistrado.catpais_pid = $scope.properties.objPadre.objPadreCatPais !== undefined ? parseInt($scope.properties.objPadre.objPadreCatPais.descripcion) : null;
                        $scope.properties.JSONUsuarioRegistrado.estadoextranjero = $scope.properties.objPadre.estadoExtranjero !== undefined ? $scope.properties.objPadre.estadoExtranjero : null;
                        if ($scope.properties.objPadre.catPais.descripcion != "México") {
                            if ($scope.properties.objPadre.estadoExtranjero === "") {
                                swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar estado extranjero.' | translate }}", "warning");
                                return;
                            }
                        }
                        $scope.properties.JSONUsuarioRegistrado.catestado_pid = $scope.properties.objPadre.catEstado !== undefined ? parseInt($scope.properties.objPadre.catEstado.persistenceId) : null;
                        $scope.properties.JSONUsuarioRegistrado.ciudad = $scope.properties.objPadre.ciudad !== undefined ? $scope.properties.objPadre.ciudad : null;
                        if ($scope.properties.objPadre.ciudad === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar ciudad.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.delegacionmunicipio = $scope.properties.objPadre.delegacionMunicipio !== undefined ? $scope.properties.objPadre.delegacionMunicipio : null;
                        $scope.properties.JSONUsuarioRegistrado.colonia = $scope.properties.objPadre.colonia !== undefined ? $scope.properties.objPadre.colonia : null;
                        $scope.properties.JSONUsuarioRegistrado.calle = $scope.properties.objPadre.calle !== undefined ? $scope.properties.objPadre.calle : null;
                        if ($scope.properties.objPadre.calle === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar calle.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.numeroexterior = $scope.properties.objPadre.numeroExterior !== undefined ? $scope.properties.objPadre.numeroExterior : null;
                        if ($scope.properties.objPadre.numeroExterior === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar número exterior.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.numerointerior = $scope.properties.objPadre.numeroInterior !== undefined ? $scope.properties.objPadre.numeroInterior : null;
                        $scope.properties.JSONUsuarioRegistrado.telefono = $scope.properties.objPadre.telefono !== undefined ? $scope.properties.objPadre.telefono : null;
                        if ($scope.properties.objPadre.telefono === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar teléfono.' | translate }}", "warning");
                            return;
                        }
                    }
                }
            } else if ($scope.properties.editarSec7 == false) {
                debugger;
                $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objMadre.persistenceId;
                $scope.properties.JSONUsuarioRegistrado.desconozcodatospadres = $scope.properties.objMadre.desconozcoDatosPadres;
                if ($scope.properties.objMadre.desconozcoDatosPadres == false) {
                    $scope.properties.JSONUsuarioRegistrado.vive_pid = parseInt($scope.properties.objMadre.vive.persistenceId);
                    $scope.properties.JSONUsuarioRegistrado.cattitulo_pid = parseInt($scope.properties.objMadre.catTitulo.persistenceId);
                    $scope.properties.JSONUsuarioRegistrado.nombre = $scope.properties.objMadre.nombre;
                    $scope.properties.JSONUsuarioRegistrado.apellidos = $scope.properties.objMadre.apellidos;
                    if ($scope.properties.objMadre.vive === "") {
                        swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar vive.' | translate }}", "warning");
                        return;
                    } else if ($scope.properties.objMadre.catTitulo === "") {
                        swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar título.' | translate }}", "warning");
                        return;
                    } else if ($scope.properties.objMadre.nombre === "") {
                        swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar nombre.' | translate }}", "warning");
                        return;
                    } else if ($scope.properties.objMadre.apellidos === "") {
                        swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar apellido.' | translate }}", "warning");
                        return;
                    }
                    if ($scope.properties.objMadre.vive.descripcion == "Sí") {
                        $scope.properties.JSONUsuarioRegistrado.categresoanahuac_pid = $scope.properties.objMadre.catEgresoAnahuac !== undefined ? parseInt($scope.properties.objMadre.catEgresoAnahuac.persistenceId) : null;
                        if ($scope.properties.objMadre.catEgresoAnahuac === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar egresó de la universidad Anahuac.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.cattrabaja_pid = $scope.properties.objMadre.catTrabaja !== undefined ? parseInt($scope.properties.objMadre.catTrabaja.persistenceId) : null;
                        if ($scope.properties.objMadre.catTrabaja === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar madre trabaja.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.catcampusegreso_pid = $scope.properties.objMadre.catCampusEgreso.persistenceId !== undefined && $scope.properties.objMadre.catCampusEgreso.persistenceId !== null ? parseInt($scope.properties.objMadre.catCampusEgreso.persistenceId) : null;
                        if ($scope.properties.objMadre.catCampusEgreso === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar campus de egreso.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.correoelectronico = $scope.properties.objMadre.correoElectronico !== undefined ? $scope.properties.objMadre.correoElectronico : null;
                        if ($scope.properties.objMadre.correoElectronico === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar correo electónico.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.empresatrabaja = $scope.properties.objMadre.empresaTrabaja !== undefined ? $scope.properties.objMadre.empresaTrabaja : null;
                        if ($scope.properties.objMadre.empresaTrabaja === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar empresa en la que trabaja.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.giroempresa = $scope.properties.objMadre.giroEmpresa !== undefined ? $scope.properties.objMadre.giroEmpresa : null;
                        if ($scope.properties.objMadre.giroEmpresa === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar giro de la empresa.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.catescolaridad_pid = $scope.properties.objMadre.catEscolaridad !== undefined ? parseInt($scope.properties.objMadre.catEscolaridad.persistenceId) : null;
                        if ($scope.properties.objMadre.catEscolaridad === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar escolaridad de la madre.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.puesto = $scope.properties.objMadre.puesto !== undefined ? $scope.properties.objMadre.puesto : null;
                        if ($scope.properties.objMadre.puesto === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar puesto.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.vivecontigo = $scope.properties.objMadre.viveContigo;
                        $scope.properties.JSONUsuarioRegistrado.catpais_pid = $scope.properties.objMadre.catPais.persistenceId !== undefined ? parseInt($scope.properties.objMadre.catPais.persistenceId) : null;
                        if ($scope.properties.objMadre.catPais === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe seleccionar país.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.codigopostal = $scope.properties.objMadre.codigoPostal !== undefined ? $scope.properties.objMadre.codigoPostal : null;
                        if ($scope.properties.objMadre.codigoPostal === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar código postal.' | translate }}", "warning");
                            return;
                        }
                        //$scope.properties.JSONUsuarioRegistrado.catpais_pid = $scope.properties.objMadre.objMadreCatPais !== undefined ? parseInt($scope.properties.objMadre.objMadreCatPais.descripcion) : null;
                        $scope.properties.JSONUsuarioRegistrado.estadoextranjero = $scope.properties.objMadre.estadoExtranjero !== undefined ? $scope.properties.objMadre.estadoExtranjero : null;
                        if ($scope.properties.objMadre.catPais.descripcion != "México") {
                            if ($scope.properties.objMadre.estadoExtranjero === "") {
                                swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar estado extranjero.' | translate }}", "warning");
                                return;
                            }
                        }
                        $scope.properties.JSONUsuarioRegistrado.catestado_pid = $scope.properties.objMadre.catEstado !== undefined ? parseInt($scope.properties.objMadre.catEstado.persistenceId) : null;
                        $scope.properties.JSONUsuarioRegistrado.ciudad = $scope.properties.objMadre.ciudad !== undefined ? $scope.properties.objMadre.ciudad : null;
                        if ($scope.properties.objMadre.ciudad === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar ciudad.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.delegacionmunicipio = $scope.properties.objMadre.delegacionMunicipio !== undefined ? $scope.properties.objMadre.delegacionMunicipio : null;
                        $scope.properties.JSONUsuarioRegistrado.colonia = $scope.properties.objMadre.colonia !== undefined ? $scope.properties.objMadre.colonia : null;
                        $scope.properties.JSONUsuarioRegistrado.calle = $scope.properties.objMadre.calle !== undefined ? $scope.properties.objMadre.calle : null;
                        if ($scope.properties.objMadre.calle === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar calle.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.numeroexterior = $scope.properties.objMadre.numeroExterior !== undefined ? $scope.properties.objMadre.numeroExterior : null;
                        if ($scope.properties.objMadre.numeroExterior === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar número exterior.' | translate }}", "warning");
                            return;
                        }
                        $scope.properties.JSONUsuarioRegistrado.numerointerior = $scope.properties.objMadre.numeroInterior !== undefined ? $scope.properties.objMadre.numeroInterior : null;
                        $scope.properties.JSONUsuarioRegistrado.telefono = $scope.properties.objMadre.telefono !== undefined ? $scope.properties.objMadre.telefono : null;
                        if ($scope.properties.objMadre.telefono === "") {
                            swal("{{ '¡Aviso!' | translate }}", "{{ 'Debe agregar teléfono.' | translate }}", "warning");
                            return;
                        }
                    }
                }
            } else if ($scope.properties.editarSec8 == false) {
                debugger;
                $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;

                var registrosAcumulados = [];

                for (var i = 0; i < $scope.properties.contactoEmergencia.length; i++) {
                    var contacto = $scope.properties.contactoEmergencia[i];

                    var parentescoDescripcion = contacto.parentesco;

                    const matchingParentesco = $scope.properties.catParentesco.find(parentesco => parentesco.descripcion === parentescoDescripcion);

                    var datosContacto = {
                        nombre: contacto.nombre,
                        parentesco: parentescoDescripcion,
                        telefono: contacto.telefono,
                        telefonocelular: contacto.telefonoCelular,
                        catcasodeemergencia_pid: contacto.catCasoDeEmergencia.persistenceId,
                        catparentesco_pid: matchingParentesco ? matchingParentesco.persistenceId : null
                    };

                    registrosAcumulados.push(datosContacto);
                }

                $scope.properties.JSONUsuarioRegistrado.registrosAcumulados = registrosAcumulados;
            }



            var jsonAnterior = {};
            var jsonNuevo = {};
            if ($scope.properties.editarSec1 == false) {

                jsonAnterior.catcampusestudio_pid = $scope.properties.objSolicitudDeAdmision.catCampusEstudio.persistenceId;
                jsonAnterior.catgestionescolar_pid = $scope.properties.objSolicitudDeAdmision.catGestionEscolar.persistenceId;
                jsonAnterior.catperiodo_pid = $scope.properties.objSolicitudDeAdmision.catPeriodo.persistenceId;
                jsonAnterior.catcampus_pid = $scope.properties.objSolicitudDeAdmision.catCampus.persistenceId;
                jsonAnterior.catpresentasteenotrocampus_pid = $scope.properties.objSolicitudDeAdmision.catPresentasteEnOtroCampus.persistenceId;
                jsonAnterior.correoelectronico = $scope.properties.objSolicitudDeAdmision.correoElectronico;
                if ($scope.properties.objSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion == "No") {
                    jsonAnterior.catconcluisteproceso_pid = null;
                    jsonAnterior.catresultadoadmision_pid = null;
                } else {
                    jsonAnterior.catconcluisteproceso_pid = $scope.properties.objSolicitudDeAdmision.catConcluisteProceso ? $scope.properties.objSolicitudDeAdmision.catConcluisteProceso.persistenceId : null;
                    jsonAnterior.catresultadoadmision_pid = $scope.properties.objSolicitudDeAdmision.catResultadoAdmision ? $scope.properties.objSolicitudDeAdmision.catResultadoAdmision.persistenceId : null;
                }

                jsonNuevo.catcampusestudio_pid = $scope.properties.JSONUsuarioRegistrado.catcampusestudio_pid;
                jsonNuevo.catgestionescolar_pid = $scope.properties.JSONUsuarioRegistrado.catgestionescolar_pid;
                jsonNuevo.catperiodo_pid = $scope.properties.JSONUsuarioRegistrado.catperiodo_pid;
                jsonNuevo.catcampus_pid = $scope.properties.JSONUsuarioRegistrado.catcampus_pid;
                jsonNuevo.catpresentasteenotrocampus_pid = $scope.properties.JSONUsuarioRegistrado.catpresentasteenotrocampus_pid;
                jsonNuevo.correoelectronico = $scope.properties.JSONUsuarioRegistrado.correoelectronico;
                jsonNuevo.catconcluisteproceso_pid = $scope.properties.JSONUsuarioRegistrado.catconcluisteproceso_pid;
                jsonNuevo.catresultadoadmision_pid = $scope.properties.JSONUsuarioRegistrado.catresultadoadmision_pid;

            } else if ($scope.properties.editarSec2 == false) {
                jsonAnterior.primernombre = $scope.properties.objSolicitudDeAdmision.primerNombre;
                jsonAnterior.segundonombre = $scope.properties.objSolicitudDeAdmision.segundoNombre;
                jsonAnterior.apellidopaterno = $scope.properties.objSolicitudDeAdmision.apellidoPaterno;
                jsonAnterior.apellidomaterno = $scope.properties.objSolicitudDeAdmision.apellidoMaterno;
                jsonAnterior.catsexo_pid = $scope.properties.objSolicitudDeAdmision.catSexo.persistenceId;

                let d = new Date($scope.properties.objSolicitudDeAdmision.fechaNacimiento.toString())
                let formatted_date = d.getFullYear() + "-" + ((d.getMonth() + 1) < 10 ? "0" + (d.getMonth() + 1) : (d.getMonth() + 1)) + "-" + (d.getDate() < 10 ? "0" + d.getDate() : d.getDate())
                formatted_date += "t05:00:00.000";
                $scope.properties.JSONUsuarioRegistrado.fechanacimiento = formatted_date;

                jsonAnterior.fechanacimiento = formatted_date;
                jsonAnterior.catestado_pid = $scope.properties.objSolicitudDeAdmision.catEstado.persistenceId;
                jsonAnterior.catreligion_pid = $scope.properties.objSolicitudDeAdmision.catReligion.persistenceId;
                jsonAnterior.curp = $scope.properties.objSolicitudDeAdmision.curp;
                jsonAnterior.telefonocelular = $scope.properties.objSolicitudDeAdmision.telefonoCelular;
                jsonAnterior.catestadocivil_pid = $scope.properties.objSolicitudDeAdmision.catEstadoCivil.persistenceId;

                jsonNuevo.primernombre = $scope.properties.JSONUsuarioRegistrado.primernombre;
                jsonNuevo.segundonombrenombre = $scope.properties.JSONUsuarioRegistrado.segundonombre;
                jsonNuevo.apellidopaterno = $scope.properties.JSONUsuarioRegistrado.apellidopaterno;
                jsonNuevo.apellidomaterno = $scope.properties.JSONUsuarioRegistrado.apellidomaterno;
                jsonNuevo.catsexo_pid = $scope.properties.JSONUsuarioRegistrado.catsexo_pid;
                jsonNuevo.fechanacimiento = $scope.properties.JSONUsuarioRegistrado.fechanacimiento;
                jsonNuevo.catestado_pid = $scope.properties.JSONUsuarioRegistrado.catestado_pid;
                jsonNuevo.catreligion_pid = $scope.properties.JSONUsuarioRegistrado.catreligion_pid;
                jsonNuevo.curp = $scope.properties.JSONUsuarioRegistrado.curp;
                jsonNuevo.telefonocelular = $scope.properties.JSONUsuarioRegistrado.telefonocelular;
                jsonNuevo.catestadocivil_pid = $scope.properties.JSONUsuarioRegistrado.catestadocivil_pid;

            } else if ($scope.properties.editarSec3 == false) {
                jsonAnterior.catpais_pid = $scope.properties.objSolicitudDeAdmision.catPais.persistenceId;
                jsonAnterior.codigopostal = $scope.properties.objSolicitudDeAdmision.codigoPostal;
                jsonAnterior.catestado_pid = $scope.properties.objSolicitudDeAdmision.catEstado.persistenceId;
                jsonAnterior.ciudad = $scope.properties.objSolicitudDeAdmision.ciudad;
                jsonAnterior.delegacionmunicipio = $scope.properties.objSolicitudDeAdmision.delegacionMunicipio;
                jsonAnterior.colonia = $scope.properties.objSolicitudDeAdmision.colonia;
                jsonAnterior.calle = $scope.properties.objSolicitudDeAdmision.calle;
                jsonAnterior.calle2 = $scope.properties.objSolicitudDeAdmision.calle2;
                jsonAnterior.numexterior = $scope.properties.objSolicitudDeAdmision.numExterior;
                jsonAnterior.numinterior = $scope.properties.objSolicitudDeAdmision.numInterior;
                jsonAnterior.telefono = $scope.properties.objSolicitudDeAdmision.telefono;
                jsonAnterior.otrotelefonocontacto = $scope.properties.objSolicitudDeAdmision.otroTelefonoContacto;

                jsonNuevo.catpais_pid = $scope.properties.JSONUsuarioRegistrado.catpais_pid;
                jsonNuevo.codigopostal = $scope.properties.JSONUsuarioRegistrado.codigopostal;
                jsonNuevo.catestado_pid = $scope.properties.JSONUsuarioRegistrado.catestado_pid;
                jsonNuevo.ciudad = $scope.properties.JSONUsuarioRegistrado.ciudad;
                jsonNuevo.delegacionmunicipio = $scope.properties.JSONUsuarioRegistrado.delegacionmunicipio;
                jsonNuevo.colonia = $scope.properties.JSONUsuarioRegistrado.colonia;
                jsonNuevo.calle = $scope.properties.JSONUsuarioRegistrado.calle;
                jsonNuevo.calle2 = $scope.properties.JSONUsuarioRegistrado.calle2;
                jsonNuevo.numexterior = $scope.properties.JSONUsuarioRegistrado.numexterior;
                jsonNuevo.numinterior = $scope.properties.JSONUsuarioRegistrado.numinterior;
                jsonNuevo.telefono = $scope.properties.JSONUsuarioRegistrado.telefono;
                jsonNuevo.otrotelefonocontacto = $scope.properties.JSONUsuarioRegistrado.otrotelefonocontacto;

            } else if ($scope.properties.editarSec4 == false) {

                jsonAnterior.catbachilleratos_pid = parseInt($scope.properties.objSolicitudDeAdmision.catBachilleratos.persistenceid);
                if ($scope.properties.objSolicitudDeAdmision.catBachilleratos.persistenceid == 1) {
                    jsonAnterior.descripcion = $scope.properties.datosPreparatoria.nombreBachillerato ? $scope.properties.datosPreparatoria.nombreBachillerato : null;
                    jsonAnterior.estado = $scope.properties.datosPreparatoria.estadoBachillerato;
                    jsonAnterior.pais = $scope.properties.datosPreparatoria.paisBachillerato;
                    jsonAnterior.ciudad = $scope.properties.datosPreparatoria.ciudadBachillerato;
                }
                jsonAnterior.promediogeneral = $scope.properties.objSolicitudDeAdmision.promedioGeneral;

                jsonNuevo.catbachilleratos_pid = $scope.properties.JSONUsuarioRegistrado.catbachilleratos_pid;
                if ($scope.properties.objSolicitudDeAdmision.catBachilleratos.persistenceid == 1) {
                    jsonNuevo.descripcion = $scope.properties.JSONUsuarioRegistrado.descripcion ? $scope.properties.JSONUsuarioRegistrado.descripcion : null;
                    jsonNuevo.estado = $scope.properties.JSONUsuarioRegistrado.estado;
                    jsonNuevo.pais = $scope.properties.JSONUsuarioRegistrado.pais;
                    jsonNuevo.ciudad = $scope.properties.JSONUsuarioRegistrado.ciudad;
                }
                jsonNuevo.promediogeneral = $scope.properties.JSONUsuarioRegistrado.promediogeneral;

            } else if ($scope.properties.editarSec5 == false) {

                $scope.properties.JSONUsuarioRegistrado.otro = false;
                $scope.properties.JSONUsuarioRegistrado.MaP = false;
                $scope.properties.JSONUsuarioRegistrado.PaM = false;

                $scope.properties.JSONUsuarioRegistrado.tutorPersistenceid = $scope.properties.datosJson.persistenceId;

                jsonAnterior.istutor = true;
                jsonNuevo.istutor = true;
                $scope.properties.JSONUsuarioRegistrado.istutor = true;

                jsonAnterior.vive_pid = 145289;
                jsonNuevo.vive_pid = 145289;
                $scope.properties.JSONUsuarioRegistrado.vive_pid = 145289;

                $scope.properties.JSONUsuarioRegistrado.vivecontigo = $scope.properties.datosJson.viveContigo;


                jsonAnterior.desconozcoDatosPadres = (jsonAnterior.desconozcoDatosPadres === "false");
                jsonNuevo.desconozcoDatosPadres = (jsonNuevo.desconozcoDatosPadres === "false");
                $scope.properties.JSONUsuarioRegistrado.desconozcoDatosPadres = ($scope.properties.JSONUsuarioRegistrado.desconozcoDatosPadres === "false");

                if ($scope.properties.datosJson.catParentezco.descripcion == "Padre") {
                    $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objPadre.persistenceId;

                    if ($scope.properties.objMadre.isTutor = true) {
                        $scope.properties.JSONUsuarioRegistrado.MaP = true;
                        $scope.properties.JSONUsuarioRegistrado.madrePersistenceid = $scope.properties.objMadre.persistenceId;

                        jsonAnterior.istutorPadres = (jsonAnterior.istutorPadres === "false");
                        jsonNuevo.istutorPadres = (jsonNuevo.istutorPadres === "false");
                        $scope.properties.JSONUsuarioRegistrado.istutorPadres = ($scope.properties.JSONUsuarioRegistrado.istutorPadres === "false");
                    }

                } else if ($scope.properties.datosJson.catParentezco.descripcion == "Madre") {
                    $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objMadre.persistenceId;

                    if ($scope.properties.objPadre.isTutor = true) {
                        $scope.properties.JSONUsuarioRegistrado.PaM = true;
                        $scope.properties.JSONUsuarioRegistrado.padrePersistenceid = $scope.properties.objPadre.persistenceId;

                        jsonAnterior.istutorPadres = (jsonAnterior.istutorPadres === "false");
                        jsonNuevo.istutorPadres = (jsonNuevo.istutorPadres === "false");
                        $scope.properties.JSONUsuarioRegistrado.istutorPadres = ($scope.properties.JSONUsuarioRegistrado.istutorPadres === "false");
                    }

                } else if ($scope.properties.datosJson.catParentezco.descripcion != "Padre" && $scope.properties.datosJson.catParentezco.descripcion != "Madre") {
                    $scope.properties.JSONUsuarioRegistrado.otro = true;

                    $scope.properties.JSONUsuarioRegistrado.padrePersistenceid = $scope.properties.objPadre.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.madrePersistenceid = $scope.properties.objMadre.persistenceId;

                    jsonAnterior.istutorPadres = (jsonAnterior.istutorPadres === "false");
                    jsonNuevo.istutorPadres = (jsonNuevo.istutorPadres === "false");
                    $scope.properties.JSONUsuarioRegistrado.istutorPadres = ($scope.properties.JSONUsuarioRegistrado.istutorPadres === "false");
                }

                jsonAnterior.cattitulo_pid = parseInt($scope.properties.datosJson.catTitulo.persistenceId);
                jsonAnterior.catparentezco_pid = parseInt($scope.properties.datosJson.catParentezco.persistenceId);
                jsonAnterior.nombre = $scope.properties.datosJson.nombre;
                jsonAnterior.otroparentesco = $scope.properties.datosJson.otroParentesco ?? null;
                jsonAnterior.apellidos = $scope.properties.datosJson.apellidos;
                jsonAnterior.correoelectronico = $scope.properties.datosJson.correoElectronico;
                jsonAnterior.categresoanahuac_pid = parseInt($scope.properties.datosJson.catEgresoAnahuac.persistenceId);
                jsonAnterior.cattrabaja_pid = parseInt($scope.properties.datosJson.catTrabaja.persistenceId);
                jsonAnterior.catcampusegreso_pid = parseInt($scope.properties.datosJson.catCampusEgreso.persistenceId);
                jsonAnterior.empresatrabaja = $scope.properties.datosJson.empresaTrabaja;
                jsonAnterior.catescolaridad_pid = parseInt($scope.properties.datosJson.catEscolaridad.persistenceId);
                jsonAnterior.giroempresa = $scope.properties.datosJson.giroEmpresa;
                jsonAnterior.puesto = $scope.properties.datosJson.puesto;
                jsonAnterior.catpais_pid = parseInt($scope.properties.datosJson.catPais.persistenceId);
                jsonAnterior.codigopostal = $scope.properties.datosJson.codigoPostal;
                jsonAnterior.catestado_pid = parseInt($scope.properties.datosJson.catEstado.persistenceId);
                jsonAnterior.estadoextranjero = $scope.properties.datosJson.estadoExtranjero ?? null;
                jsonAnterior.ciudad = $scope.properties.datosJson.ciudad;
                jsonAnterior.delegacionmunicipio = $scope.properties.datosJson.delegacionMunicipio ?? null;
                jsonAnterior.colonia = $scope.properties.datosJson.colonia;
                jsonAnterior.calle = $scope.properties.datosJson.calle;
                jsonAnterior.numeroexterior = $scope.properties.datosJson.numeroExterior ?? null;
                jsonAnterior.numerointerior = $scope.properties.datosJson.numeroInterior ?? null;
                jsonAnterior.telefono = $scope.properties.datosJson.telefono;

                jsonNuevo.cattitulo_pid = $scope.properties.JSONUsuarioRegistrado.cattitulo_pid;
                jsonNuevo.catparentezco_pid = $scope.properties.JSONUsuarioRegistrado.catparentezco_pid;
                jsonNuevo.nombre = $scope.properties.JSONUsuarioRegistrado.nombre;
                jsonNuevo.otroparentesco = $scope.properties.JSONUsuarioRegistrado.otroparentesco ?? null;
                jsonNuevo.apellidos = $scope.properties.JSONUsuarioRegistrado.apellidos;
                jsonNuevo.correoelectronico = $scope.properties.JSONUsuarioRegistrado.correoelectronico;
                jsonNuevo.categresoanahuac_pid = $scope.properties.JSONUsuarioRegistrado.categresoanahuac_pid;
                jsonNuevo.cattrabaja_pid = $scope.properties.JSONUsuarioRegistrado.cattrabaja_pid;
                jsonNuevo.catcampusegreso_pid = $scope.properties.JSONUsuarioRegistrado.catcampusegreso_pid;
                jsonNuevo.empresatrabaja = $scope.properties.JSONUsuarioRegistrado.empresatrabaja;
                jsonNuevo.catescolaridad_pid = $scope.properties.JSONUsuarioRegistrado.catescolaridad_pid;
                jsonNuevo.giroempresa = $scope.properties.JSONUsuarioRegistrado.giroempresa;
                jsonNuevo.puesto = $scope.properties.JSONUsuarioRegistrado.puesto;
                jsonNuevo.catpais_pid = $scope.properties.JSONUsuarioRegistrado.catpais_pid;
                jsonNuevo.codigopostal = $scope.properties.JSONUsuarioRegistrado.codigopostal;
                jsonNuevo.catestado_pid = $scope.properties.JSONUsuarioRegistrado.catestado_pid;
                jsonNuevo.estadoextranjero = $scope.properties.JSONUsuarioRegistrado.estadoextranjero ?? null;
                jsonNuevo.ciudad = $scope.properties.JSONUsuarioRegistrado.ciudad;
                jsonNuevo.delegacionmunicipio = $scope.properties.JSONUsuarioRegistrado.delegacionmunicipio;
                jsonNuevo.colonia = $scope.properties.JSONUsuarioRegistrado.colonia;
                jsonNuevo.calle = $scope.properties.JSONUsuarioRegistrado.calle;
                jsonNuevo.numeroexterior = $scope.properties.JSONUsuarioRegistrado.numeroexterior ?? null;
                jsonNuevo.numerointerior = $scope.properties.JSONUsuarioRegistrado.numerointerior ?? null;
                jsonNuevo.telefono = $scope.properties.JSONUsuarioRegistrado.telefono;

            } else if ($scope.properties.editarSec6 == false) {
                debugger;
                jsonAnterior.desconozcodatospadres = $scope.properties.objPadre.desconozcoDatosPadres;
                if ($scope.properties.objPadre.desconozcoDatosPadres == false) {
                    jsonAnterior.vive_pid = parseInt($scope.properties.objPadre.vive.persistenceId);
                    jsonAnterior.cattitulo_pid = parseInt($scope.properties.objPadre.catTitulo.persistenceId);
                    jsonAnterior.nombre = $scope.properties.objPadre.nombre;
                    jsonAnterior.apellidos = $scope.properties.objPadre.apellidos;
                    if ($scope.properties.objPadre.vive.descripcion == "Sí") {
                        jsonAnterior.correoelectronico = $scope.properties.objPadre.correoElectronico !== undefined ? $scope.properties.objPadre.correoElectronico : null;
                        jsonAnterior.categresoanahuac_pid = $scope.properties.objPadre.catEgresoAnahuac !== undefined ? parseInt($scope.properties.objPadre.catEgresoAnahuac.persistenceId) : null;
                        jsonAnterior.cattrabaja_pid = $scope.properties.objPadre.catTrabaja !== undefined ? parseInt($scope.properties.objPadre.catTrabaja.persistenceId) : null;
                        jsonAnterior.catcampusegreso_pid = $scope.properties.objPadre.catCampus !== undefined && $scope.properties.objPadre.catCampus !== null ? parseInt($scope.properties.objPadre.catCampusEgreso.persistenceId) : null;
                        jsonAnterior.empresatrabaja = $scope.properties.objPadre.empresaTrabaja;
                        jsonAnterior.catescolaridad_pid = $scope.properties.objPadre.catEscolaridad !== undefined ? parseInt($scope.properties.objPadre.catEscolaridad.persistenceId) : null;
                        jsonAnterior.giroempresa = $scope.properties.objPadre.giroEmpresa !== undefined ? $scope.properties.objPadre.giroEmpresa : null;
                        jsonAnterior.puesto = $scope.properties.objPadre.puesto !== undefined ? $scope.properties.objPadre.puesto : null;
                        jsonAnterior.vivecontigo = $scope.properties.objPadre.viveContigo;
                        jsonAnterior.catpais_pid = $scope.properties.objPadre.catPais.persistenceId !== undefined ? parseInt($scope.properties.objPadre.catPais.persistenceId) : null;
                        jsonAnterior.codigopostal = $scope.properties.objPadre.codigoPostal !== undefined ? $scope.properties.objPadre.codigoPostal : null;
                        jsonAnterior.catestado_pid = $scope.properties.objPadre.catEstado !== undefined ? parseInt($scope.properties.objPadre.catEstado.persistenceId) : null;
                        jsonAnterior.estadoextranjero = $scope.properties.objPadre.estadoExtranjero !== undefined ? $scope.properties.objPadre.estadoExtranjero : null;
                        jsonAnterior.ciudad = $scope.properties.objPadre.ciudad !== undefined ? $scope.properties.objPadre.ciudad : null;
                        jsonAnterior.delegacionmunicipio = $scope.properties.objPadre.delegacionMunicipio !== undefined ? $scope.properties.objPadre.delegacionMunicipio : null;
                        jsonAnterior.colonia = $scope.properties.objPadre.colonia !== undefined ? $scope.properties.objPadre.colonia : null;
                        jsonAnterior.calle = $scope.properties.objPadre.calle !== undefined ? $scope.properties.objPadre.calle : null;
                        jsonAnterior.numeroexterior = $scope.properties.objPadre.numeroExterior !== undefined ? $scope.properties.objPadre.numeroExterior : null;
                        jsonAnterior.numerointerior = $scope.properties.objPadre.numeroInterior !== undefined ? $scope.properties.objPadre.numeroInterior : null;
                        jsonAnterior.telefono = $scope.properties.objPadre.telefono !== undefined ? $scope.properties.objPadre.telefono : null;
                    }
                }

                jsonNuevo.desconozcodatospadres = $scope.properties.JSONUsuarioRegistrado.desconozcodatospadres;
                if ($scope.properties.objPadre.desconozcoDatosPadres == false) {
                    jsonNuevo.vive_pid = $scope.properties.JSONUsuarioRegistrado.vive_pid;
                    jsonNuevo.cattitulo_pid = $scope.properties.JSONUsuarioRegistrado.cattitulo_pid;
                    jsonNuevo.nombre = $scope.properties.JSONUsuarioRegistrado.nombre;
                    jsonNuevo.apellidos = $scope.properties.JSONUsuarioRegistrado.apellidos;
                    if ($scope.properties.objPadre.vive.descripcion == "Sí") {
                        jsonNuevo.correoelectronico = $scope.properties.JSONUsuarioRegistrado.correoelectronico;
                        jsonNuevo.categresoanahuac_pid = $scope.properties.JSONUsuarioRegistrado.categresoanahuac_pid;
                        jsonNuevo.cattrabaja_pid = $scope.properties.JSONUsuarioRegistrado.cattrabaja_pid;
                        jsonNuevo.catcampusegreso_pid = $scope.properties.JSONUsuarioRegistrado.catcampusegreso_pid;
                        jsonNuevo.empresatrabaja = $scope.properties.JSONUsuarioRegistrado.empresatrabaja;
                        jsonNuevo.catescolaridad_pid = $scope.properties.JSONUsuarioRegistrado.catescolaridad_pid;
                        jsonNuevo.giroempresa = $scope.properties.JSONUsuarioRegistrado.giroempresa;
                        jsonNuevo.puesto = $scope.properties.JSONUsuarioRegistrado.puesto;
                        jsonNuevo.vivecontigo = $scope.properties.JSONUsuarioRegistrado.vivecontigo;
                        jsonNuevo.catpais_pid = $scope.properties.JSONUsuarioRegistrado.catpais_pid;
                        jsonNuevo.codigopostal = $scope.properties.JSONUsuarioRegistrado.codigopostal;
                        jsonNuevo.catestado_pid = $scope.properties.JSONUsuarioRegistrado.catestado_pid;
                        jsonNuevo.estadoextranjero = $scope.properties.JSONUsuarioRegistrado.estadoextranjero ?? null;
                        jsonNuevo.ciudad = $scope.properties.JSONUsuarioRegistrado.ciudad;
                        jsonNuevo.delegacionmunicipio = $scope.properties.JSONUsuarioRegistrado.delegacionmunicipio;
                        jsonNuevo.colonia = $scope.properties.JSONUsuarioRegistrado.colonia;
                        jsonNuevo.calle = $scope.properties.JSONUsuarioRegistrado.calle;
                        jsonNuevo.numeroexterior = $scope.properties.JSONUsuarioRegistrado.numeroexterior ?? null;
                        jsonNuevo.numerointerior = $scope.properties.JSONUsuarioRegistrado.numerointerior ?? null;
                        jsonNuevo.telefono = $scope.properties.JSONUsuarioRegistrado.telefono;
                    }
                }
            } else if ($scope.properties.editarSec7 == false) {
                debugger;
                $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objMadre.persistenceId;
                jsonAnterior.desconozcodatospadres = $scope.properties.objMadre.desconozcoDatosPadres;
                if ($scope.properties.objMadre.desconozcoDatosPadres == false) {
                    jsonAnterior.vive_pid = parseInt($scope.properties.objMadre.vive.persistenceId);
                    jsonAnterior.cattitulo_pid = parseInt($scope.properties.objMadre.catTitulo.persistenceId);
                    jsonAnterior.nombre = $scope.properties.objMadre.nombre;
                    jsonAnterior.apellidos = $scope.properties.objMadre.apellidos;
                    if ($scope.properties.objMadre.vive.descripcion == "Sí") {
                        jsonAnterior.correoelectronico = $scope.properties.objMadre.correoElectronico !== undefined ? $scope.properties.objMadre.correoElectronico : null;
                        jsonAnterior.categresoanahuac_pid = $scope.properties.objMadre.catEgresoAnahuac !== undefined ? parseInt($scope.properties.objMadre.catEgresoAnahuac.persistenceId) : null;
                        jsonAnterior.cattrabaja_pid = $scope.properties.objMadre.catTrabaja !== undefined ? parseInt($scope.properties.objMadre.catTrabaja.persistenceId) : null;
                        jsonAnterior.catcampusegreso_pid = $scope.properties.objMadre.catCampus !== undefined && $scope.properties.objMadre.catCampus !== null ? parseInt($scope.properties.objMadre.catCampusEgreso.persistenceId) : null;
                        jsonAnterior.empresatrabaja = $scope.properties.objMadre.empresaTrabaja;
                        jsonAnterior.catescolaridad_pid = $scope.properties.objMadre.catEscolaridad !== undefined ? parseInt($scope.properties.objMadre.catEscolaridad.persistenceId) : null;
                        jsonAnterior.giroempresa = $scope.properties.objMadre.giroEmpresa !== undefined ? $scope.properties.objMadre.giroEmpresa : null;
                        jsonAnterior.puesto = $scope.properties.objMadre.puesto !== undefined ? $scope.properties.objMadre.puesto : null;
                        jsonAnterior.vivecontigo = $scope.properties.objMadre.viveContigo;
                        jsonAnterior.catpais_pid = $scope.properties.objMadre.catPais.persistenceId !== undefined ? parseInt($scope.properties.objMadre.catPais.persistenceId) : null;
                        jsonAnterior.codigopostal = $scope.properties.objMadre.codigoPostal !== undefined ? $scope.properties.objMadre.codigoPostal : null;
                        jsonAnterior.catestado_pid = $scope.properties.objMadre.catEstado !== undefined ? parseInt($scope.properties.objMadre.catEstado.persistenceId) : null;
                        jsonAnterior.estadoextranjero = $scope.properties.objMadre.estadoExtranjero !== undefined ? $scope.properties.objMadre.estadoExtranjero : null;
                        jsonAnterior.ciudad = $scope.properties.objMadre.ciudad !== undefined ? $scope.properties.objMadre.ciudad : null;
                        jsonAnterior.delegacionmunicipio = $scope.properties.objMadre.delegacionMunicipio !== undefined ? $scope.properties.objMadre.delegacionMunicipio : null;
                        jsonAnterior.colonia = $scope.properties.objMadre.colonia !== undefined ? $scope.properties.objMadre.colonia : null;
                        jsonAnterior.calle = $scope.properties.objMadre.calle !== undefined ? $scope.properties.objMadre.calle : null;
                        jsonAnterior.numeroexterior = $scope.properties.objMadre.numeroExterior !== undefined ? $scope.properties.objMadre.numeroExterior : null;
                        jsonAnterior.numerointerior = $scope.properties.objMadre.numeroInterior !== undefined ? $scope.properties.objMadre.numeroInterior : null;
                        jsonAnterior.telefono = $scope.properties.objMadre.telefono !== undefined ? $scope.properties.objMadre.telefono : null;
                    }
                }

                jsonNuevo.desconozcodatospadres = $scope.properties.JSONUsuarioRegistrado.desconozcodatospadres;
                if ($scope.properties.objMadre.desconozcoDatosPadres == false) {
                    jsonNuevo.vive_pid = $scope.properties.JSONUsuarioRegistrado.vive_pid;
                    jsonNuevo.cattitulo_pid = $scope.properties.JSONUsuarioRegistrado.cattitulo_pid;
                    jsonNuevo.nombre = $scope.properties.JSONUsuarioRegistrado.nombre;
                    jsonNuevo.apellidos = $scope.properties.JSONUsuarioRegistrado.apellidos;
                    if ($scope.properties.objMadre.vive.descripcion == "Sí") {
                        jsonNuevo.correoelectronico = $scope.properties.JSONUsuarioRegistrado.correoelectronico;
                        jsonNuevo.categresoanahuac_pid = $scope.properties.JSONUsuarioRegistrado.categresoanahuac_pid;
                        jsonNuevo.cattrabaja_pid = $scope.properties.JSONUsuarioRegistrado.cattrabaja_pid;
                        jsonNuevo.catcampusegreso_pid = $scope.properties.JSONUsuarioRegistrado.catcampusegreso_pid;
                        jsonNuevo.empresatrabaja = $scope.properties.JSONUsuarioRegistrado.empresatrabaja;
                        jsonNuevo.catescolaridad_pid = $scope.properties.JSONUsuarioRegistrado.catescolaridad_pid;
                        jsonNuevo.giroempresa = $scope.properties.JSONUsuarioRegistrado.giroempresa;
                        jsonNuevo.puesto = $scope.properties.JSONUsuarioRegistrado.puesto;
                        jsonNuevo.vivecontigo = $scope.properties.JSONUsuarioRegistrado.vivecontigo;
                        jsonNuevo.catpais_pid = $scope.properties.JSONUsuarioRegistrado.catpais_pid;
                        jsonNuevo.codigopostal = $scope.properties.JSONUsuarioRegistrado.codigopostal;
                        jsonNuevo.catestado_pid = $scope.properties.JSONUsuarioRegistrado.catestado_pid;
                        jsonNuevo.estadoextranjero = $scope.properties.JSONUsuarioRegistrado.estadoextranjero ?? null;
                        jsonNuevo.ciudad = $scope.properties.JSONUsuarioRegistrado.ciudad;
                        jsonNuevo.delegacionmunicipio = $scope.properties.JSONUsuarioRegistrado.delegacionmunicipio;
                        jsonNuevo.colonia = $scope.properties.JSONUsuarioRegistrado.colonia;
                        jsonNuevo.calle = $scope.properties.JSONUsuarioRegistrado.calle;
                        jsonNuevo.numeroexterior = $scope.properties.JSONUsuarioRegistrado.numeroexterior ?? null;
                        jsonNuevo.numerointerior = $scope.properties.JSONUsuarioRegistrado.numerointerior ?? null;
                        jsonNuevo.telefono = $scope.properties.JSONUsuarioRegistrado.telefono;
                    }
                }
            } else if ($scope.properties.editarSec8 == false) {
                debugger;
                var registrosAcumulados = [];

                for (var i = 0; i < $scope.properties.JSONUsuarioRegistrado.registrosAcumulados.length; i++) {
                    var contacto = $scope.properties.JSONUsuarioRegistrado.registrosAcumulados[i];

                    var datosContacto = {
                        nombre: contacto.nombre,
                        parentesco: contacto.parentesco,
                        telefono: contacto.telefono,
                        telefonocelular: contacto.telefonocelular
                    };

                    registrosAcumulados.push(datosContacto);

                    jsonAnterior.registrosAcumulados = registrosAcumulados;
                }
            }


            $scope.properties.JSONUsuarioRegistrado.editarSec1 = $scope.properties.editarSec1 ?? true;
            $scope.properties.JSONUsuarioRegistrado.editarSec2 = $scope.properties.editarSec2 ?? true;
            $scope.properties.JSONUsuarioRegistrado.editarSec3 = $scope.properties.editarSec3 ?? true;
            $scope.properties.JSONUsuarioRegistrado.editarSec4 = $scope.properties.editarSec4 ?? true;
            $scope.properties.JSONUsuarioRegistrado.editarSec5 = $scope.properties.editarSec5 ?? true;
            $scope.properties.JSONUsuarioRegistrado.editarSec6 = $scope.properties.editarSec6 ?? true;
            $scope.properties.JSONUsuarioRegistrado.editarSec7 = $scope.properties.editarSec7 ?? true;
            $scope.properties.JSONUsuarioRegistrado.editarSec8 = $scope.properties.editarSec8 ?? true;

            $scope.properties.actualizar = true;

            if ($scope.properties.editarSec1 == false) {
                $scope.properties.editarSec1 = true;

            } else if ($scope.properties.editarSec1 == true) {
                $scope.properties.editarSec1 = false;
            }
            if ($scope.properties.editarSec2 == false) {
                $scope.properties.editarSec2 = true;

            } else if ($scope.properties.editarSec2 == true) {
                $scope.properties.editarSec2 = false;
            }
            if ($scope.properties.editarSec3 == false) {
                $scope.properties.editarSec3 = true;

            } else if ($scope.properties.editarSec3 == true) {
                $scope.properties.editarSec3 = false;
            }
            if ($scope.properties.editarSec4 == false) {
                $scope.properties.editarSec4 = true;

            } else if ($scope.properties.editarSec4 == true) {
                $scope.properties.editarSec4 = false;
            }
            if ($scope.properties.editarSec5 == false) {
                $scope.properties.editarSec5 = true;

            } else if ($scope.properties.editarSec5 == true) {
                $scope.properties.editarSec5 = false;
            }
            if ($scope.properties.editarSec6 == false) {
                $scope.properties.editarSec6 = true;

            } else if ($scope.properties.editarSec6 == true) {
                $scope.properties.editarSec6 = false;
            }
            if ($scope.properties.editarSec7 == false) {
                $scope.properties.editarSec7 = true;

            } else if ($scope.properties.editarSec7 == true) {
                $scope.properties.editarSec7 = false;
            }
            if ($scope.properties.editarSec8 == false) {
                $scope.properties.editarSec8 = true;

            } else if ($scope.properties.editarSec8 == true) {
                $scope.properties.editarSec8 = false;
            }

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
            var prom = doRequest('POST', '../API/bpm/process/' + id + '/instantiation', getUserParam()).then(function () {
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
        debugger;
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.JSONUsuarioRegistrado),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                $scope.properties.dataFromSuccess = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
                notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status });
                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                $scope.properties.archivos = angular.copy($scope.properties.strArchivos)
                getLstUsuariosRegistrados("POST", $scope.properties.urlPost);
                $scope.properties.objSolicitudDeAdmision.caseId = 0;
                //closeModal($scope.properties.closeOnSuccess);
            })
            .error(function (data, status) {
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {
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
            var objSolicitudDeAdmision = angular.extend({}, $scope.properties, additionalProperties);
            $window.parent.postMessage(JSON.stringify(objSolicitudDeAdmision), '*');
        }
    }

    function getUserParam() {
        var userId = getUrlParam('user');
        if (userId) {
            return { 'user': userId };
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
            doRequest('POST', '../API/bpm/userTask/' + getUrlParam('id') + '/execution', params).then(function () {
                localStorageService.delete($window.location.href);
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }

    function getLstUsuariosRegistrados(method, url, params) {

        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.jsonenviar),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                $scope.properties.lstContenido = data.data;
                $scope.properties.editable = "tabla";
                swal("¡Usuario modificado!", "Se han modificado los datos del usuario correctamente", "success");
            })
            .error(function (data, status) {
                notifyParentFrame({
                    message: 'error',
                    status: status,
                    dataFromError: data,
                    dataFromSuccess: undefined,
                    responseStatusCode: status
                });
            })
            .finally(function () {
                blockUI.stop();
                closeModal($scope.properties.closeOnSuccess);
            });
    }

}