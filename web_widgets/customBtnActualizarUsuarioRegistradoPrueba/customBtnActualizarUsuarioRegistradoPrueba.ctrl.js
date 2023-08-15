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
                if($scope.properties.editarSec1 == false){
                    if ($scope.properties.objSolicitudDeAdmision.catCampusEstudio === "") {
                        swal("¡Aviso!", "Debe agregar el campus donde se cursarán sus estudios.", "warning");
                        return;
                    } else if ($scope.properties.objSolicitudDeAdmision.catGestionEscolar === "") {
                        swal("¡Aviso!", "Debe seleccionar una licenciatura.", "warning");
                        return;
                        /*}else if($scope.properties.objSolicitudDeAdmision.catPeriodo === null){
                            swal("¡Aviso!", "Debe seleccionar un periodo", "warning");*/
                    } else if ($scope.properties.objSolicitudDeAdmision.catPeriodo === "") {
                        swal("¡Aviso!", "Debe seleccionar el periodo de ingreso.", "warning");
                        return;
                    } else if ($scope.properties.objSolicitudDeAdmision.catCampus === "") {
                        swal("¡Aviso!", "Debe seleccionar el campus de examen.", "warning");
                        return;
                    } else if ($scope.properties.objSolicitudDeAdmision.catPresentasteEnOtroCampus === "") {
                        swal("¡Aviso!", "Debe seleccionar si has presentado solicitud de admisión en otra universidad.", "warning");
                        return;
                    } else if ($scope.properties.objSolicitudDeAdmision.correoElectronico === "") {
                        swal("¡Aviso!", "Debe ingresar correo electónico.", "warning");
                        return;
                    } else{
                        $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                        $scope.properties.JSONUsuarioRegistrado.catcampusestudio_pid = $scope.properties.objSolicitudDeAdmision.catCampusEstudio.persistenceId;
                        $scope.properties.JSONUsuarioRegistrado.catgestionescolar_pid = $scope.properties.objSolicitudDeAdmision.catGestionEscolar.persistenceId;
                        $scope.properties.JSONUsuarioRegistrado.catperiodo_pid = $scope.properties.objSolicitudDeAdmision.catPeriodo.persistenceId;
                        $scope.properties.JSONUsuarioRegistrado.catcampus_pid = $scope.properties.objSolicitudDeAdmision.catCampus.persistenceId;
                        $scope.properties.JSONUsuarioRegistrado.catpresentasteenotrocampus_pid = $scope.properties.objSolicitudDeAdmision.catPresentasteEnOtroCampus.persistenceId;
                        $scope.properties.JSONUsuarioRegistrado.correoelectronico = $scope.properties.objSolicitudDeAdmision.correoElectronico;
                        if($scope.properties.objSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion == "No"){
                            $scope.properties.JSONUsuarioRegistrado.catconcluisteproceso_pid = null;
                            $scope.properties.JSONUsuarioRegistrado.catresultadoadmision_pid = null;
                            $scope.properties.JSONUsuarioRegistrado.catcampuspresentadosolicitud = null;
                        } else {
                            $scope.properties.JSONUsuarioRegistrado.catconcluisteproceso_pid = $scope.properties.objSolicitudDeAdmision.catConcluisteProceso ? $scope.properties.objSolicitudDeAdmision.catConcluisteProceso.persistenceId : null;
                            if($scope.properties.objSolicitudDeAdmision.catConcluisteProceso === null){
                                swal("¡Aviso!", "Debe seleccionar concluiste proceso de admisión.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.catresultadoadmision_pid = $scope.properties.objSolicitudDeAdmision.catResultadoAdmision ? $scope.properties.objSolicitudDeAdmision.catResultadoAdmision.persistenceId : null;
                            if($scope.properties.objSolicitudDeAdmision.catResultadoAdmision === null){
                                swal("¡Aviso!", "Debe seleccionar el resultado de la admisión.", "warning");
                                return;
                            }
                            //$scope.properties.JSONUsuarioRegistrado.catcampuspresentadosolicitud = $scope.properties.objSolicitudDeAdmision.catCampusPresentadoSolicitud;
                        }
                    }
                } else if($scope.properties.editarSec2 == false){
                    if ($scope.properties.objSolicitudDeAdmision.primerNombre === "") {
                        swal("¡Aviso!", "Debe agregar el primer nombre", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.apellidoPaterno === "") {
                        swal("¡Aviso!", "Debe agregar el apellido paterno", "warning");
                    }  else if ($scope.properties.objSolicitudDeAdmision.fechaNacimiento === "" || $scope.properties.objSolicitudDeAdmision.fechaNacimiento === undefined || $scope.properties.objSolicitudDeAdmision.fechaNacimiento === null) {
                        swal("¡Aviso!", "Debe agregar la fecha de nacimiento.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.catNacionalidad === "") {
                        swal("¡Aviso!", "Debe seleccionar nacionalidad.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.catReligion === "") {
                        swal("¡Aviso!", "Debe seleccionar religión.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.curp === "") {
                        swal("¡Aviso!", "Debe agregar CURP.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.telefonoCelular === "") {
                        swal("¡Aviso!", "Debe agregar teléfono celular.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.catSexo === null) {
                        swal("¡Aviso!", "Debe seleccionar el sexo", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.catEstado === null && $scope.properties.objSolicitudDeAdmision.estadoextranjero === "") {
                        swal("¡Aviso!", "Debe seleccionar el estado", "warning");
                        /*}else if($scope.properties.objSolicitudDeAdmision.estadoextranjero === ""){
                            swal("¡Aviso!", "Debe agregar el estado", "warning");*/
                    } else{
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
                } else if($scope.properties.editarSec3 == false){
                    if ($scope.properties.objSolicitudDeAdmision.catPais === "") {
                        swal("¡Aviso!", "Debe seleccionar país.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.codigoPostal === "") {
                        swal("¡Aviso!", "Debe agregar codigo postal.", "warning");
                    }  else if ($scope.properties.objSolicitudDeAdmision.catEstado === "") {
                        swal("¡Aviso!", "Debe agregar estado.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.ciudad === "") {
                        swal("¡Aviso!", "Debe agregar ciudad.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.colonia === "") {
                        swal("¡Aviso!", "Debe agregar colonia.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.calle === "") {
                        swal("¡Aviso!", "Debe agregar calle.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.numExterior === "") {
                        swal("¡Aviso!", "Debe agregar numero exterior.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.telefono === "") {
                        swal("¡Aviso!", "Debe agregar teléfono.", "warning");
                    } else{
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
                        $scope.properties.JSONUsuarioRegistrado.telefono= $scope.properties.objSolicitudDeAdmision.telefono;
                        $scope.properties.JSONUsuarioRegistrado.otrotelefonocontacto = $scope.properties.objSolicitudDeAdmision.otroTelefonoContacto;
                    }
                } else if($scope.properties.editarSec4 == false){
                    if ($scope.properties.objSolicitudDeAdmision.catBachilleratos === "") {
                        swal("¡Aviso!", "Debe agregar bachillerato.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.nombreBachillerato === "") {
                        swal("¡Aviso!", "Debe agregar nombre del bachillerato.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.estadoBachillerato === "") {
                        swal("¡Aviso!", "Debe agregar estado de bachillerato.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.paisBachillerato === "") {
                        swal("¡Aviso!", "Debe agregar país de bachillerato.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.ciudadBachillerato === "") {
                        swal("¡Aviso!", "Debe agregar ciudad de bachillerato.", "warning");
                    } else if ($scope.properties.objSolicitudDeAdmision.promedioGeneral === "") {
                        swal("¡Aviso!", "Debe agregar promedio general.", "warning");
                    } else {
                        $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                        $scope.properties.JSONUsuarioRegistrado.catbachilleratos_pid = parseInt($scope.properties.objSolicitudDeAdmision.catBachilleratos.persistenceid);
                        if($scope.properties.objSolicitudDeAdmision.catBachilleratos.descripcion == "Otro"){
                            $scope.properties.JSONUsuarioRegistrado.bachillerato = $scope.properties.objSolicitudDeAdmision.bachillerato ? $scope.properties.objSolicitudDeAdmision.bachillerato : null;
                        }
                        $scope.properties.JSONUsuarioRegistrado.estado = $scope.properties.datosPreparatoria.estadoBachillerato;
                        $scope.properties.JSONUsuarioRegistrado.pais = $scope.properties.datosPreparatoria.paisBachillerato;
                        $scope.properties.JSONUsuarioRegistrado.ciudad = $scope.properties.datosPreparatoria.ciudadBachillerato;
                        $scope.properties.JSONUsuarioRegistrado.promediogeneral = $scope.properties.objSolicitudDeAdmision.promedioGeneral;
                    }
                } else if($scope.properties.editarSec5 == false){
                    if ($scope.properties.datosJson.catTitulo === "") {
                        swal("¡Aviso!", "Debe seleccionar titulo.", "warning");
                    } else if ($scope.properties.datosJson.catParentezco === "") {
                        swal("¡Aviso!", "Debe seleccionar parentesco.", "warning");
                    } else if ($scope.properties.datosJson.nombre === "") {
                        swal("¡Aviso!", "Debe agregar nombre.", "warning");
                    } else if ($scope.properties.datosJson.apellidos === "") {
                        swal("¡Aviso!", "Debe agregar apellido.", "warning");
                    } else if ($scope.properties.datosJson.correoElectronico === "") {
                        swal("¡Aviso!", "Debe agregar correo electrónico.", "warning");
                    } else if ($scope.properties.datosJson.catEgresoAnahuac === "") {
                        swal("¡Aviso!", "Debe seleccionar egresó de la universidad Anahuac.", "warning");
                    }  else if ($scope.properties.datosJson.catTrabaja === "") {
                        swal("¡Aviso!", "Debe seleccionar tutor trabaja.", "warning");
                    } else if ($scope.properties.datosJson.catEscolaridad === "") {
                        swal("¡Aviso!", "Debe seleccionar escolaridad del tutor.", "warning");
                    } else if ($scope.properties.datosJson.catPais === "") {
                        swal("¡Aviso!", "Debe seleccionar país.", "warning");
                    } else if ($scope.properties.datosJson.codigoPostal === "") {
                        swal("¡Aviso!", "Debe agregar código postal.", "warning");
                    } else if ($scope.properties.datosJson.catEstado === "") {
                        swal("¡Aviso!", "Debe agregar estado.", "warning");
                    } else if ($scope.properties.datosJson.ciudad === "") {
                        swal("¡Aviso!", "Debe agregar ciudad.", "warning");
                    } else if ($scope.properties.datosJson.colonia === "") {
                        swal("¡Aviso!", "Debe agregar colonia.", "warning");
                    } else if ($scope.properties.datosJson.calle === "") {
                        swal("¡Aviso!", "Debe agregar calle.", "warning");
                    } else if ($scope.properties.datosJson.numeroExterior === "") {
                        swal("¡Aviso!", "Debe agregar numero exterior.", "warning");
                    } else if ($scope.properties.datosJson.telefono === "") {
                        swal("¡Aviso!", "Debe agregar teléfono.", "warning");
                    } else {
                        $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                        if($scope.properties.datosJson.catParentezco.descripcion == "Padre"){
                            $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objPadre.persistenceId;
                        } else if($scope.properties.datosJson.catParentezco.descripcion == "Madre"){
                            $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objMadre.persistenceId;
                        }
                        $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                        $scope.properties.JSONUsuarioRegistrado.cattitulo_pid  = parseInt($scope.properties.datosJson.catTitulo.persistenceId);
                        $scope.properties.JSONUsuarioRegistrado.catparentezco_pid  = parseInt($scope.properties.datosJson.catParentezco.persistenceId);
                        $scope.properties.JSONUsuarioRegistrado.nombre = $scope.properties.datosJson.nombre;
                        if($scope.properties.datosJson.catParentezco.descripcion == "Otro"){
                            $scope.properties.JSONUsuarioRegistrado.otroparentesco = $scope.properties.datosJson.otroParentesco.descripcion;
                        }
                        $scope.properties.JSONUsuarioRegistrado.apellidos = $scope.properties.datosJson.apellidos;
                        $scope.properties.JSONUsuarioRegistrado.correoelectronico = $scope.properties.datosJson.correoElectronico;
                        $scope.properties.JSONUsuarioRegistrado.categresoanahuac_pid = parseInt($scope.properties.datosJson.catEgresoAnahuac.persistenceId);
                        $scope.properties.JSONUsuarioRegistrado.cattrabaja_pid = parseInt($scope.properties.datosJson.catTrabaja.persistenceId);
                        $scope.properties.JSONUsuarioRegistrado.catcampusegreso_pid = parseInt($scope.properties.datosJson.catCampusEgreso.persistenceId);
                        if($scope.properties.datosJson.catEgresoAnahuac.descripcion === "Sí"){
                            if ($scope.properties.datosJson.catCampusEgreso.descripcion === "") {
                                swal("¡Aviso!", "Debe agregar campus de egreso.", "warning");
                                return;
                            }
                        }
                        $scope.properties.JSONUsuarioRegistrado.empresatrabaja = $scope.properties.datosJson.empresaTrabaja;
                        if($scope.properties.datosJson.catTrabaja.descripcion == "Sí"){
                            if ($scope.properties.datosJson.empresaTrabaja === "") {
                                swal("¡Aviso!", "Debe ingresar empresa en la que trabaja.", "warning");
                                return;
                            }
                        }
                        $scope.properties.JSONUsuarioRegistrado.catescolaridad_pid = parseInt($scope.properties.datosJson.catEscolaridad.persistenceId);
                        $scope.properties.JSONUsuarioRegistrado.giroempresa = $scope.properties.datosJson.giroEmpresa;
                        if($scope.properties.datosJson.catTrabaja.descripcion == "Sí"){
                            if ($scope.properties.datosJson.giroEmpresa === "") {
                                swal("¡Aviso!", "Debe ingresar giro de la empresa.", "warning");
                                return;
                            }
                        }
                        $scope.properties.JSONUsuarioRegistrado.puesto = $scope.properties.datosJson.puesto;
                        if($scope.properties.datosJson.catTrabaja.descripcion == "Sí"){
                            if ($scope.properties.datosJson.puesto === "") {
                                swal("¡Aviso!", "Debe ingresar puesto.", "warning");
                                return;
                            }
                        }
                        $scope.properties.JSONUsuarioRegistrado.catpais_pid  = parseInt($scope.properties.datosJson.catPais.persistenceId);
                        $scope.properties.JSONUsuarioRegistrado.codigopostal  = $scope.properties.datosJson.codigoPostal;
                        $scope.properties.JSONUsuarioRegistrado.catestado_pid  = parseInt($scope.properties.datosJson.catEstado.persistenceId);
                        if($scope.properties.datosJson.catPais.descripcion != "México"){
                            $scope.properties.JSONUsuarioRegistrado.estadoextranjero  = $scope.properties.datosJson.estadoExtranjero.descripcion;
                        }
                        $scope.properties.JSONUsuarioRegistrado.ciudad = $scope.properties.datosJson.ciudad;
                        $scope.properties.JSONUsuarioRegistrado.delegacionmunicipio  = $scope.properties.datosJson.delegacionMunicipio;
                        $scope.properties.JSONUsuarioRegistrado.colonia  = $scope.properties.datosJson.colonia;
                        $scope.properties.JSONUsuarioRegistrado.calle = $scope.properties.datosJson.calle;
                        if($scope.properties.datosJson.numeroExterior != null){
                            $scope.properties.JSONUsuarioRegistrado.numeroexterior = $scope.properties.datosJson.numeroExterior.descripcion;
                        }
                        if($scope.properties.datosJson.numeroInterior != null){
                            $scope.properties.JSONUsuarioRegistrado.numerointerior = $scope.properties.datosJson.numeroInterior.descripcion;
                        }
                        $scope.properties.JSONUsuarioRegistrado.telefono = $scope.properties.datosJson.telefono;
                    }
                } else if($scope.properties.editarSec6 == false){
                    debugger;
                    $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                    $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objPadre.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.desconozcodatospadres = $scope.properties.objPadre.desconozcoDatosPadres;
                    if($scope.properties.objPadre.desconozcoDatosPadres == false){
                        $scope.properties.JSONUsuarioRegistrado.vive_pid = parseInt($scope.properties.objPadre.vive.persistenceId);
                        $scope.properties.JSONUsuarioRegistrado.cattitulo_pid = parseInt($scope.properties.objPadre.catTitulo.persistenceId);
                        $scope.properties.JSONUsuarioRegistrado.nombre = $scope.properties.objPadre.nombre;
                        $scope.properties.JSONUsuarioRegistrado.apellidos = $scope.properties.objPadre.apellidos;
                        if ($scope.properties.objPadre.vive === "") {
                            swal("¡Aviso!", "Debe seleccionar vive.", "warning");
                            return;
                        } else if($scope.properties.objPadre.catTitulo === ""){
                            swal("¡Aviso!", "Debe seleccionar titulo.", "warning");
                            return;
                        } else if($scope.properties.objPadre.nombre === ""){
                            swal("¡Aviso!", "Debe ingresar nombre.", "warning");
                            return;
                        } else if($scope.properties.objPadre.apellidos === ""){
                            swal("¡Aviso!", "Debe ingresar apellido.", "warning");
                            return;
                        }
                        if($scope.properties.objPadre.vive.descripcion == "Sí"){
                            $scope.properties.JSONUsuarioRegistrado.categresoanahuac_pid = $scope.properties.objPadre.catEgresoAnahuac !== undefined ? parseInt($scope.properties.objPadre.catEgresoAnahuac.persistenceId) : null;
                            if ($scope.properties.objPadre.catEgresoAnahuac === "") {
                                swal("¡Aviso!", "Debe seleccionar egresó de la universidad.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.cattrabaja_pid = $scope.properties.objPadre.catTrabaja !== undefined ? parseInt($scope.properties.objPadre.catTrabaja.persistenceId) : null;
                            if ($scope.properties.objPadre.catTrabaja === "") {
                                swal("¡Aviso!", "Debe seleccionar trabaja.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.catcampusegreso_pid = $scope.properties.objPadre.catCampusEgreso.persistenceId !== undefined && $scope.properties.objPadre.catCampusEgreso.persistenceId !== null ? parseInt($scope.properties.objPadre.catCampusEgreso.persistenceId) : null;
                            if ($scope.properties.objPadre.catCampusEgreso === "") {
                                swal("¡Aviso!", "Debe seleccionar campus de egreso.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.correoelectronico = $scope.properties.objPadre.correoElectronico !== undefined ? $scope.properties.objPadre.correoElectronico : null;
                            if ($scope.properties.objPadre.correoElectronico === "") {
                                swal("¡Aviso!", "Debe ingresar correo electónico.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.empresatrabaja = $scope.properties.objPadre.empresaTrabaja !== undefined ? $scope.properties.objPadre.empresaTrabaja : null;
                            if ($scope.properties.objPadre.empresaTrabaja === "") {
                                swal("¡Aviso!", "Debe ingresar empresa en la que trabaja.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.giroempresa = $scope.properties.objPadre.giroEmpresa !== undefined ? $scope.properties.objPadre.giroEmpresa : null;
                            if ($scope.properties.objPadre.giroEmpresa === "") {
                                swal("¡Aviso!", "Debe ingresar giro de la empresa.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.catescolaridad_pid = $scope.properties.objPadre.catEscolaridad !== undefined ? parseInt($scope.properties.objPadre.catEscolaridad.persistenceId) : null;
                            if ($scope.properties.objPadre.catEscolaridad  === "") {
                                swal("¡Aviso!", "Debe seleccionar escolaridad del padre.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.puesto = $scope.properties.objPadre.puesto !== undefined ? $scope.properties.objPadre.puesto : null;
                            if ($scope.properties.objPadre.puesto  === "") {
                                swal("¡Aviso!", "Debe ingresar puesto.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.vivecontigo = $scope.properties.objPadre.viveContigo;
                            $scope.properties.JSONUsuarioRegistrado.catpais_pid = $scope.properties.objPadre.catPais.persistenceId !== undefined ? parseInt($scope.properties.objPadre.catPais.persistenceId) : null;
                            if ($scope.properties.objPadre.catPais  === "") {
                                swal("¡Aviso!", "Debe seleccionar país.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.codigopostal = $scope.properties.objPadre.codigoPostal !== undefined ? $scope.properties.objPadre.codigoPostal : null;
                            if ($scope.properties.objPadre.codigoPostal  === "") {
                                swal("¡Aviso!", "Debe ingresar codigo postal.", "warning");
                                return;
                            }
                            //$scope.properties.JSONUsuarioRegistrado.catpais_pid = $scope.properties.objPadre.objPadreCatPais !== undefined ? parseInt($scope.properties.objPadre.objPadreCatPais.descripcion) : null;
                            $scope.properties.JSONUsuarioRegistrado.estadoextranjero = $scope.properties.objPadre.estadoExtranjero !== undefined ? $scope.properties.objPadre.estadoExtranjero : null;
                            if($scope.properties.objPadre.catPais.descripcion != "México"){
                                if ($scope.properties.objPadre.estadoExtranjero  === "") {
                                    swal("¡Aviso!", "Debe ingresar estado extranjero.", "warning");
                                    return;
                                }
                            }
                            $scope.properties.JSONUsuarioRegistrado.catestado_pid = $scope.properties.objPadre.catEstado !== undefined ? parseInt($scope.properties.objPadre.catEstado.persistenceId) : null;
                            $scope.properties.JSONUsuarioRegistrado.ciudad = $scope.properties.objPadre.ciudad !== undefined ? $scope.properties.objPadre.ciudad : null;
                            if ($scope.properties.objPadre.ciudad  === "") {
                                swal("¡Aviso!", "Debe ingresar ciudad.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.delegacionmunicipio = $scope.properties.objPadre.delegacionMunicipio !== undefined ? $scope.properties.objPadre.delegacionMunicipio : null;
                            $scope.properties.JSONUsuarioRegistrado.colonia = $scope.properties.objPadre.colonia !== undefined ? $scope.properties.objPadre.colonia : null;
                            $scope.properties.JSONUsuarioRegistrado.calle = $scope.properties.objPadre.calle !== undefined ? $scope.properties.objPadre.calle : null;
                            if ($scope.properties.objPadre.calle  === "") {
                                swal("¡Aviso!", "Debe ingresar calle.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.numeroexterior = $scope.properties.objPadre.numeroExterior !== undefined ? $scope.properties.objPadre.numeroExterior : null;
                            if ($scope.properties.objPadre.numeroExterior  === "") {
                                swal("¡Aviso!", "Debe ingresar numero exterior.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.numerointerior = $scope.properties.objPadre.numeroInterior !== undefined ? $scope.properties.objPadre.numeroInterior : null;
                            $scope.properties.JSONUsuarioRegistrado.telefono = $scope.properties.objPadre.telefono !== undefined ? $scope.properties.objPadre.telefono : null;
                            if ($scope.properties.objPadre.telefono  === "") {
                                swal("¡Aviso!", "Debe ingresar telefono.", "warning");
                                return;
                            }
                        }
                    }
                } else if($scope.properties.editarSec7 == false){
                    debugger;
                    $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                    $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objMadre.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.desconozcodatospadres = $scope.properties.objMadre.desconozcoDatosPadres;
                    if($scope.properties.objMadre.desconozcoDatosPadres == false){
                        $scope.properties.JSONUsuarioRegistrado.vive_pid = parseInt($scope.properties.objMadre.vive.persistenceId);
                        $scope.properties.JSONUsuarioRegistrado.cattitulo_pid = parseInt($scope.properties.objMadre.catTitulo.persistenceId);
                        $scope.properties.JSONUsuarioRegistrado.nombre = $scope.properties.objMadre.nombre;
                        $scope.properties.JSONUsuarioRegistrado.apellidos = $scope.properties.objMadre.apellidos;
                        if ($scope.properties.objMadre.vive === "") {
                            swal("¡Aviso!", "Debe seleccionar vive.", "warning");
                            return;
                        } else if($scope.properties.objMadre.catTitulo === ""){
                            swal("¡Aviso!", "Debe seleccionar titulo.", "warning");
                            return;
                        } else if($scope.properties.objMadre.nombre === ""){
                            swal("¡Aviso!", "Debe ingresar nombre.", "warning");
                            return;
                        } else if($scope.properties.objMadre.apellidos === ""){
                            swal("¡Aviso!", "Debe ingresar apellido.", "warning");
                            return;
                        }
                        if($scope.properties.objMadre.vive.descripcion == "Sí"){
                            $scope.properties.JSONUsuarioRegistrado.categresoanahuac_pid = $scope.properties.objMadre.catEgresoAnahuac !== undefined ? parseInt($scope.properties.objMadre.catEgresoAnahuac.persistenceId) : null;
                            if ($scope.properties.objMadre.catEgresoAnahuac === "") {
                                swal("¡Aviso!", "Debe seleccionar egresó de la universidad.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.cattrabaja_pid = $scope.properties.objMadre.catTrabaja !== undefined ? parseInt($scope.properties.objMadre.catTrabaja.persistenceId) : null;
                            if ($scope.properties.objMadre.catTrabaja === "") {
                                swal("¡Aviso!", "Debe seleccionar trabaja.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.catcampusegreso_pid = $scope.properties.objMadre.catCampusEgreso.persistenceId !== undefined && $scope.properties.objMadre.catCampusEgreso.persistenceId !== null ? parseInt($scope.properties.objMadre.catCampusEgreso.persistenceId) : null;
                            if ($scope.properties.objMadre.catCampusEgreso === "") {
                                swal("¡Aviso!", "Debe seleccionar campus de egreso.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.correoelectronico = $scope.properties.objMadre.correoElectronico !== undefined ? $scope.properties.objMadre.correoElectronico : null;
                            if ($scope.properties.objMadre.correoElectronico === "") {
                                swal("¡Aviso!", "Debe ingresar correo electónico.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.empresatrabaja = $scope.properties.objMadre.empresaTrabaja !== undefined ? $scope.properties.objMadre.empresaTrabaja : null;
                            if ($scope.properties.objMadre.empresaTrabaja === "") {
                                swal("¡Aviso!", "Debe ingresar empresa en la que trabaja.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.giroempresa = $scope.properties.objMadre.giroEmpresa !== undefined ? $scope.properties.objMadre.giroEmpresa : null;
                            if ($scope.properties.objMadre.giroEmpresa === "") {
                                swal("¡Aviso!", "Debe ingresar giro de la empresa.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.catescolaridad_pid = $scope.properties.objMadre.catEscolaridad !== undefined ? parseInt($scope.properties.objMadre.catEscolaridad.persistenceId) : null;
                            if ($scope.properties.objMadre.catEscolaridad  === "") {
                                swal("¡Aviso!", "Debe seleccionar escolaridad del padre.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.puesto = $scope.properties.objMadre.puesto !== undefined ? $scope.properties.objMadre.puesto : null;
                            if ($scope.properties.objMadre.puesto  === "") {
                                swal("¡Aviso!", "Debe ingresar puesto.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.vivecontigo = $scope.properties.objMadre.viveContigo;
                            $scope.properties.JSONUsuarioRegistrado.catpais_pid = $scope.properties.objMadre.catPais.persistenceId !== undefined ? parseInt($scope.properties.objMadre.catPais.persistenceId) : null;
                            if ($scope.properties.objMadre.catPais  === "") {
                                swal("¡Aviso!", "Debe seleccionar país.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.codigopostal = $scope.properties.objMadre.codigoPostal !== undefined ? $scope.properties.objMadre.codigoPostal : null;
                            if ($scope.properties.objMadre.codigoPostal  === "") {
                                swal("¡Aviso!", "Debe ingresar codigo postal.", "warning");
                                return;
                            }
                            //$scope.properties.JSONUsuarioRegistrado.catpais_pid = $scope.properties.objMadre.objMadreCatPais !== undefined ? parseInt($scope.properties.objMadre.objMadreCatPais.descripcion) : null;
                            $scope.properties.JSONUsuarioRegistrado.estadoextranjero = $scope.properties.objMadre.estadoExtranjero !== undefined ? $scope.properties.objMadre.estadoExtranjero : null;
                            if($scope.properties.objMadre.catPais.descripcion != "México"){
                                if ($scope.properties.objMadre.estadoExtranjero  === "") {
                                    swal("¡Aviso!", "Debe ingresar estado extranjero.", "warning");
                                    return;
                                }
                            }
                            $scope.properties.JSONUsuarioRegistrado.catestado_pid = $scope.properties.objMadre.catEstado !== undefined ? parseInt($scope.properties.objMadre.catEstado.persistenceId) : null;
                            $scope.properties.JSONUsuarioRegistrado.ciudad = $scope.properties.objMadre.ciudad !== undefined ? $scope.properties.objMadre.ciudad : null;
                            if ($scope.properties.objMadre.ciudad  === "") {
                                swal("¡Aviso!", "Debe ingresar ciudad.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.delegacionmunicipio = $scope.properties.objMadre.delegacionMunicipio !== undefined ? $scope.properties.objMadre.delegacionMunicipio : null;
                            $scope.properties.JSONUsuarioRegistrado.colonia = $scope.properties.objMadre.colonia !== undefined ? $scope.properties.objMadre.colonia : null;
                            $scope.properties.JSONUsuarioRegistrado.calle = $scope.properties.objMadre.calle !== undefined ? $scope.properties.objMadre.calle : null;
                            if ($scope.properties.objMadre.calle  === "") {
                                swal("¡Aviso!", "Debe ingresar calle.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.numeroexterior = $scope.properties.objMadre.numeroExterior !== undefined ? $scope.properties.objMadre.numeroExterior : null;
                            if ($scope.properties.objMadre.numeroExterior  === "") {
                                swal("¡Aviso!", "Debe ingresar numero exterior.", "warning");
                                return;
                            }
                            $scope.properties.JSONUsuarioRegistrado.numerointerior = $scope.properties.objMadre.numeroInterior !== undefined ? $scope.properties.objMadre.numeroInterior : null;
                            $scope.properties.JSONUsuarioRegistrado.telefono = $scope.properties.objMadre.telefono !== undefined ? $scope.properties.objMadre.telefono : null;
                            if ($scope.properties.objMadre.telefono  === "") {
                                swal("¡Aviso!", "Debe ingresar telefono.", "warning");
                                return;
                            }
                        }
                    }
                } else if($scope.properties.editarSec8 == false){
                    debugger;
                    $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;

var registrosAcumulados = [];

// Recorrer los contactos de contactoEmergencia
for (var i = 0; i < $scope.properties.contactoEmergencia.length; i++) {
    var contacto = $scope.properties.contactoEmergencia[i];
    
    // Obtener la descripción del parentesco del contacto actual
    var parentescoDescripcion = contacto.parentesco;

    // Buscar el parentesco coincidente en el array catParentesco
    const matchingParentesco = $scope.properties.catParentesco.find(parentesco => parentesco.descripcion === parentescoDescripcion);

    // Crear un objeto para almacenar los datos del contacto actual
    var datosContacto = {
        nombre: contacto.nombre,
        parentesco: parentescoDescripcion, // Usar la descripción del parentesco
        telefono: contacto.telefono,
        telefonocelular: contacto.telefonoCelular,
        catcasodeemergencia_pid: contacto.catCasoDeEmergencia.persistenceId,
        catparentesco_pid: matchingParentesco ? matchingParentesco.persistenceId : null
    };

    // Agregar el objeto al arreglo de registros acumulados
    registrosAcumulados.push(datosContacto);
}

// Asignar el arreglo de registros acumulados a JSONUsuarioRegistrado
$scope.properties.JSONUsuarioRegistrado.registrosAcumulados = registrosAcumulados;
                }

                
                
                    var jsonAnterior = {};
                    var jsonNuevo = {};
                    if($scope.properties.editarSec1 == false){
                        
                        jsonAnterior.catcampusestudio_pid = $scope.properties.objSolicitudDeAdmision.catCampusEstudio.persistenceId;
                        jsonAnterior.catgestionescolar_pid = $scope.properties.objSolicitudDeAdmision.catGestionEscolar.persistenceId;
                        jsonAnterior.catperiodo_pid = $scope.properties.objSolicitudDeAdmision.catPeriodo.persistenceId;
                        jsonAnterior.catcampus_pid = $scope.properties.objSolicitudDeAdmision.catCampus.persistenceId;
                        jsonAnterior.catpresentasteenotrocampus_pid = $scope.properties.objSolicitudDeAdmision.catPresentasteEnOtroCampus.persistenceId;
                        jsonAnterior.correoelectronico = $scope.properties.objSolicitudDeAdmision.correoElectronico;
                        if($scope.properties.objSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion == "No"){
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

                    } else if ($scope.properties.editarSec2 == false){
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

                    } else if ($scope.properties.editarSec3 == false){
                        // Ejemplo para manejar valores nulos en jsonAnterior
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

                        // Ejemplo para manejar valores nulos en jsonNuevo
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

                    } else if ($scope.properties.editarSec4 == false){
                        
                        jsonAnterior.catbachilleratos_pid = parseInt($scope.properties.objSolicitudDeAdmision.catBachilleratos.persistenceid);
                        if($scope.properties.objSolicitudDeAdmision.catBachilleratos.persistenceid == 1){
                            jsonAnterior.descripcion = $scope.properties.datosPreparatoria.nombreBachillerato ? $scope.properties.datosPreparatoria.nombreBachillerato : null;
                        jsonAnterior.estado = $scope.properties.datosPreparatoria.estadoBachillerato;
                        jsonAnterior.pais = $scope.properties.datosPreparatoria.paisBachillerato;
                        jsonAnterior.ciudad = $scope.properties.datosPreparatoria.ciudadBachillerato;
                        }
                        jsonAnterior.promediogeneral = $scope.properties.objSolicitudDeAdmision.promedioGeneral;

                        jsonNuevo.catbachilleratos_pid = $scope.properties.JSONUsuarioRegistrado.catbachilleratos_pid;
                        if($scope.properties.objSolicitudDeAdmision.catBachilleratos.persistenceid == 1){
                            jsonNuevo.descripcion = $scope.properties.JSONUsuarioRegistrado.descripcion ? $scope.properties.JSONUsuarioRegistrado.descripcion : null;
                            jsonNuevo.estado = $scope.properties.JSONUsuarioRegistrado.estado;
                            jsonNuevo.pais = $scope.properties.JSONUsuarioRegistrado.pais;
                            jsonNuevo.ciudad = $scope.properties.JSONUsuarioRegistrado.ciudad;
                        }
                        jsonNuevo.promediogeneral = $scope.properties.JSONUsuarioRegistrado.promediogeneral;

                    } else if ($scope.properties.editarSec5 == false){
                        
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

                        if($scope.properties.datosJson.catParentezco.descripcion == "Padre"){
                            $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objPadre.persistenceId;

                            if($scope.properties.objMadre.isTutor = true){
                                $scope.properties.JSONUsuarioRegistrado.MaP = true;
                                $scope.properties.JSONUsuarioRegistrado.madrePersistenceid = $scope.properties.objMadre.persistenceId;

                                jsonAnterior.istutorPadres = (jsonAnterior.istutorPadres === "false");
                                jsonNuevo.istutorPadres = (jsonNuevo.istutorPadres === "false");
                                $scope.properties.JSONUsuarioRegistrado.istutorPadres = ($scope.properties.JSONUsuarioRegistrado.istutorPadres === "false");
                            }

                        } else if($scope.properties.datosJson.catParentezco.descripcion == "Madre"){
                            $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objMadre.persistenceId;                        

                            if($scope.properties.objPadre.isTutor = true){
                                $scope.properties.JSONUsuarioRegistrado.PaM = true;
                                $scope.properties.JSONUsuarioRegistrado.padrePersistenceid = $scope.properties.objPadre.persistenceId;

                                jsonAnterior.istutorPadres = (jsonAnterior.istutorPadres === "false");
                                jsonNuevo.istutorPadres = (jsonNuevo.istutorPadres === "false");
                                $scope.properties.JSONUsuarioRegistrado.istutorPadres = ($scope.properties.JSONUsuarioRegistrado.istutorPadres === "false");
                            }
                            
                        } else if($scope.properties.datosJson.catParentezco.descripcion != "Padre" && $scope.properties.datosJson.catParentezco.descripcion != "Madre"){
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

                    } else if ($scope.properties.editarSec6 == false){
                        debugger;
                        jsonAnterior.desconozcodatospadres = $scope.properties.objPadre.desconozcoDatosPadres;
                        if($scope.properties.objPadre.desconozcoDatosPadres == false){
                            jsonAnterior.vive_pid = parseInt($scope.properties.objPadre.vive.persistenceId);
                            jsonAnterior.cattitulo_pid = parseInt($scope.properties.objPadre.catTitulo.persistenceId);
                            jsonAnterior.nombre = $scope.properties.objPadre.nombre;
                            jsonAnterior.apellidos = $scope.properties.objPadre.apellidos;
                            if($scope.properties.objPadre.vive.descripcion == "Sí"){
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
                        if($scope.properties.objPadre.desconozcoDatosPadres == false){
                            jsonNuevo.vive_pid = $scope.properties.JSONUsuarioRegistrado.vive_pid;
                            jsonNuevo.cattitulo_pid = $scope.properties.JSONUsuarioRegistrado.cattitulo_pid;
                            jsonNuevo.nombre = $scope.properties.JSONUsuarioRegistrado.nombre;
                            jsonNuevo.apellidos = $scope.properties.JSONUsuarioRegistrado.apellidos;
                            if($scope.properties.objPadre.vive.descripcion == "Sí"){
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
                    } else if ($scope.properties.editarSec7 == false){
                        debugger;
                        $scope.properties.JSONUsuarioRegistrado.persistenceid = $scope.properties.objMadre.persistenceId;
                        jsonAnterior.desconozcodatospadres = $scope.properties.objMadre.desconozcoDatosPadres;
                        if($scope.properties.objMadre.desconozcoDatosPadres == false){
                            jsonAnterior.vive_pid = parseInt($scope.properties.objMadre.vive.persistenceId);
                            jsonAnterior.cattitulo_pid = parseInt($scope.properties.objMadre.catTitulo.persistenceId);
                            jsonAnterior.nombre = $scope.properties.objMadre.nombre;
                            jsonAnterior.apellidos = $scope.properties.objMadre.apellidos;
                            if($scope.properties.objMadre.vive.descripcion == "Sí"){
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
                        if($scope.properties.objMadre.desconozcoDatosPadres == false){
                            jsonNuevo.vive_pid = $scope.properties.JSONUsuarioRegistrado.vive_pid;
                            jsonNuevo.cattitulo_pid = $scope.properties.JSONUsuarioRegistrado.cattitulo_pid;
                            jsonNuevo.nombre = $scope.properties.JSONUsuarioRegistrado.nombre;
                            jsonNuevo.apellidos = $scope.properties.JSONUsuarioRegistrado.apellidos;
                            if($scope.properties.objMadre.vive.descripcion == "Sí"){
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
                    } else if ($scope.properties.editarSec8 == false){
                        debugger;
                        var registrosAcumulados = [];

                        // Recorrer los contactos de contactoEmergencia
                        for (var i = 0; i < $scope.properties.JSONUsuarioRegistrado.registrosAcumulados.length; i++) {
                            var contacto = $scope.properties.JSONUsuarioRegistrado.registrosAcumulados[i];
                            
                            // Crear un objeto para almacenar los datos del contacto actual
                            var datosContacto = {
                                nombre: contacto.nombre,
                                parentesco: contacto.parentesco,
                                telefono: contacto.telefono,
                                telefonocelular: contacto.telefonocelular
                            };

                            // Agregar el objeto al arreglo de registros acumulados
                            registrosAcumulados.push(datosContacto);
                            
                            // Aquí puedes realizar más acciones con los datos del contacto si es necesario
                            // Por ejemplo, podrías realizar alguna operación con jsonAnterior
                            jsonAnterior.registrosAcumulados = registrosAcumulados;
                        }

                        //AGREGAR LO DE NUEVOJSON
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
        
        if($scope.properties.editarSec1 == false){
            $scope.properties.editarSec1 = true;
            
            }else if($scope.properties.editarSec1 == true){
            $scope.properties.editarSec1 = false;
            }
            if($scope.properties.editarSec2 == false){
            $scope.properties.editarSec2 = true;
            
            }else if($scope.properties.editarSec2 == true){
            $scope.properties.editarSec2 = false;
            }
            if($scope.properties.editarSec3 == false){
            $scope.properties.editarSec3 = true;
            
            }else if($scope.properties.editarSec3 == true){
            $scope.properties.editarSec3 = false;
            }
            if($scope.properties.editarSec4 == false){
            $scope.properties.editarSec4 = true;
            
            }else if($scope.properties.editarSec4 == true){
            $scope.properties.editarSec4 = false;
            }
            if($scope.properties.editarSec5 == false){
            $scope.properties.editarSec5 = true;
            
            }else if($scope.properties.editarSec5 == true){
            $scope.properties.editarSec5 = false;
            }
            if($scope.properties.editarSec6 == false){
            $scope.properties.editarSec6 = true;
            
            }else if($scope.properties.editarSec6 == true){
            $scope.properties.editarSec6 = false;
            }
            if($scope.properties.editarSec7 == false){
            $scope.properties.editarSec7 = true;
            
            }else if($scope.properties.editarSec7 == true){
            $scope.properties.editarSec7 = false;
            }
            if($scope.properties.editarSec8 == false){
                $scope.properties.editarSec8 = true;
                
            }else if($scope.properties.editarSec8 == true){
                $scope.properties.editarSec8 = false;
            }

                    doRequest($scope.properties.action, $scope.properties.url);
                //else {
                /*   debugger;
                var jsonAnterior = {};
                var jsonNuevo = {};
                jsonAnterior.correoelectronico = $scope.properties.objSolicitudDeAdmision.correoelEctronico;
                jsonNuevo.correoelectronico = $scope.properties.JSONUsuarioRegistrado.correoElectronico;
                /*
                jsonAnterior.primernombre = $scope.properties.jsonOriginal.primernombre;
                jsonAnterior.segundonombre = $scope.properties.jsonOriginal.segundonombre;
                jsonAnterior.apellidopaterno = $scope.properties.jsonOriginal.apellidopaterno;
                jsonAnterior.apellidomaterno = $scope.properties.jsonOriginal.apellidomaterno;
                //jsonAnterior.correoelectronico = $scope.properties.jsonOriginal.correoelectronico;
                jsonAnterior.campusestudio = $scope.properties.jsonOriginal.campussede;
                jsonAnterior.licenciatura = $scope.properties.jsonOriginal.licenciatura;
                jsonAnterior.periodo = $scope.properties.jsonOriginal.ingreso;
                jsonAnterior.propedeutico = $scope.properties.jsonOriginal.propedeutico;
                jsonAnterior.campus = $scope.properties.jsonOriginal.campus;
                jsonAnterior.sexo = $scope.properties.jsonOriginal.sexo;
                jsonAnterior.estadoextranjero = $scope.properties.jsonOriginal.estadoextranjero;
                jsonAnterior.estado = $scope.properties.jsonOriginal.estado;
                jsonAnterior.bachillerato = $scope.properties.jsonOriginal.prepacatalogo;
                jsonAnterior.paisbachillerato = $scope.properties.jsonOriginal.paisbachillerato;
                jsonAnterior.estadobachillerato = $scope.properties.jsonOriginal.estadobachillerato;
                jsonAnterior.ciudadbachillerato = $scope.properties.jsonOriginal.ciudadbachillerato;
                jsonAnterior.promedio = $scope.properties.jsonOriginal.promediogeneral;
                jsonNuevo.primernombre = $scope.properties.JSONUsuarioRegistrado.primernombre;
                jsonNuevo.primernombre = $scope.properties.JSONUsuarioRegistrado.primernombre;
                jsonNuevo.segundonombre = $scope.properties.JSONUsuarioRegistrado.segundonombre;
                jsonNuevo.apellidopaterno = $scope.properties.JSONUsuarioRegistrado.apellidopaterno;
                jsonNuevo.apellidomaterno = $scope.properties.JSONUsuarioRegistrado.apellidomaterno;
                //jsonNuevo.correoelectronico = $scope.properties.JSONUsuarioRegistrado.correoelectronico;
                jsonNuevo.campusestudio = $scope.properties.JSONUsuarioRegistrado.campussede;
                jsonNuevo.licenciatura = $scope.properties.JSONUsuarioRegistrado.licenciatura;
                jsonNuevo.periodo = $scope.properties.JSONUsuarioRegistrado.ingreso;
                jsonNuevo.propedeutico = $scope.properties.JSONUsuarioRegistrado.propedeutico;
                jsonNuevo.campus = $scope.properties.JSONUsuarioRegistrado.campus;
                jsonNuevo.sexo = $scope.properties.JSONUsuarioRegistrado.sexo;
                jsonNuevo.estadoextranjero = $scope.properties.JSONUsuarioRegistrado.estadoextranjero;
                jsonNuevo.estado = $scope.properties.JSONUsuarioRegistrado.estado;
                jsonNuevo.bachillerato = $scope.properties.JSONUsuarioRegistrado.prepacatalogo;
                jsonNuevo.paisbachillerato = $scope.properties.JSONUsuarioRegistrado.paisbachillerato;
                jsonNuevo.estadobachillerato = $scope.properties.JSONUsuarioRegistrado.estadobachillerato;
                jsonNuevo.ciudadbachillerato = $scope.properties.JSONUsuarioRegistrado.ciudadbachillerato;
                jsonNuevo.promedio = $scope.properties.JSONUsuarioRegistrado.promediogeneral;
    */
                //doRequest($scope.properties.action, $scope.properties.url);
            //}

    // }
                /*
                if ($scope.properties.objSolicitudDeAdmision.primerNombre === "") {
                    swal("¡Aviso!", "Debe agregar el primer nombre", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.apellidoPaterno === "") {
                    swal("¡Aviso!", "Debe agregar el apellido paterno", "warning");
                }
                /*else if($scope.properties.objSolicitudDeAdmision.apellidomaterno === ""){
                        swal("¡Aviso!", "Debe agregar el apellido materno", "warning");
                    }
                else if ($scope.properties.objSolicitudDeAdmision.correoElectronico === "") {
                    swal("¡Aviso!", "Debe agregar el correo electrónico", "warning");
                } else if (!re.test(String($scope.properties.objSolicitudDeAdmision.correoElectronico.trim()).toLowerCase())) {
                    swal("¡Aviso!", "El formato del correo electrónico no es válido", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.catCampusEstudio === null) {
                    swal("¡Aviso!", "Debe seleccionar el campus de estudio", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.catGestionEscolar === null) {
                    swal("¡Aviso!", "Debe seleccionar una licenciatura", "warning");
                    /*}else if($scope.properties.objSolicitudDeAdmision.catPeriodo === null){
                        swal("¡Aviso!", "Debe seleccionar un periodo", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.catCampusEstudio === null) {
                    swal("¡Aviso!", "Debe seleccionar el campus de examen", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.catSexo === null) {
                    swal("¡Aviso!", "Debe seleccionar el sexo", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.fechaNacimiento === "" || $scope.properties.objSolicitudDeAdmision.fechaNacimiento === undefined || $scope.properties.objSolicitudDeAdmision.fechaNacimiento === null) {
                    swal("¡Aviso!", "Debe agregar la fecha de nacimiento", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.catEstado === null && $scope.properties.objSolicitudDeAdmision.estadoextranjero === "") {
                    swal("¡Aviso!", "Debe seleccionar el estado", "warning");
                    /*}else if($scope.properties.objSolicitudDeAdmision.estadoextranjero === ""){
                        swal("¡Aviso!", "Debe agregar el estado", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.catBachilleratos === null || $scope.properties.objSolicitudDeAdmision.catBachilleratos === undefined) {
                    swal("¡Aviso!", "Debe seleccionar la preparatoria", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.catBachilleratos === "Otro" && $scope.properties.datosPreparatoria.nombreBachillerato === "") {
                    swal("¡Aviso!", "Debe agregar el nombre de la preparatoria", "warning");
                } else if ($scope.properties.datosPreparatoria.paisBachillerato === "") {
                    swal("¡Aviso!", "Debe agregar el país de la preparatoria", "warning");
                } else if ($scope.properties.datosPreparatoria.estadoBachillerato === "") {
                    swal("¡Aviso!", "Debe agregar el estado de la preparatoria", "warning");
                } else if ($scope.properties.datosPreparatoria.ciudadBachillerato === "") {
                    swal("¡Aviso!", "Debe agregar la ciudad de la preparatoria", "warning");
                } else if ($scope.properties.objSolicitudDeAdmision.promedioGeneral === "" || $scope.properties.objSolicitudDeAdmision.promedioGeneral === undefined || $scope.properties.objSolicitudDeAdmision.promedioGeneral === null || isNaN($scope.properties.objSolicitudDeAdmision.promedioGeneral)) {
                    swal("¡Aviso!", "Debe agregar el promedio", "warning");
                } //else if ($scope.properties.objSolicitudDeAdmision.resultadopaa === "" || $scope.properties.objSolicitudDeAdmision.resultadopaa === undefined || $scope.properties.objSolicitudDeAdmision.resultadopaa === null || isNaN($scope.properties.objSolicitudDeAdmision.resultadopaa)) {
                    //swal("¡Aviso!", "Debe agregar el puntaje PAA", "warning");
                //} 
                else {
                    $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.objSolicitudDeAdmision.caseId;
                    $scope.properties.JSONUsuarioRegistrado.primernombre = $scope.properties.objSolicitudDeAdmision.primerNombre;
                    $scope.properties.JSONUsuarioRegistrado.segundonombre = $scope.properties.objSolicitudDeAdmision.segundoNombre;
                    $scope.properties.JSONUsuarioRegistrado.apellidopaterno = $scope.properties.objSolicitudDeAdmision.apellidoPaterno;
                    $scope.properties.JSONUsuarioRegistrado.apellidomaterno = $scope.properties.objSolicitudDeAdmision.apellidoMaterno;
                    $scope.properties.JSONUsuarioRegistrado.correoelectronico = $scope.properties.objSolicitudDeAdmision.correoElectronico;
                    $scope.properties.JSONUsuarioRegistrado.campusestudio = $scope.properties.objSolicitudDeAdmision.catCampusEstudio.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.licenciatura = $scope.properties.objSolicitudDeAdmision.catGestionEscolar.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.periodo = $scope.properties.objSolicitudDeAdmision.catPeriodo.persistenceId;
                    /*if (!$scope.properties.objSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                        $scope.properties.JSONUsuarioRegistrado.propedeutico = null;
                    } else {
                        $scope.properties.JSONUsuarioRegistrado.propedeutico = $scope.properties.objSolicitudDeAdmision.catPropedeutico.persistenceId;
                        $scope.properties.JSONUsuarioRegistrado.catPropedeutico = $scope.properties.objSolicitudDeAdmision.catPropedeutico;
                    }

                    $scope.properties.JSONUsuarioRegistrado.campus = $scope.properties.objSolicitudDeAdmision.catCampus.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.sexo = $scope.properties.objSolicitudDeAdmision.catSexo.persistenceId;
                    //$scope.properties.JSONUsuarioRegistrado.fechanacimiento = angular.copy($scope.properties.objSolicitudDeAdmision.fechanacimiento.toString().slice(0,-1));
                    let d = new Date($scope.properties.objSolicitudDeAdmision.fechaNacimiento.toString())
                    let formatted_date = d.getFullYear() + "-" + ((d.getMonth() + 1) < 10 ? "0" + (d.getMonth() + 1) : (d.getMonth() + 1)) + "-" + (d.getDate() < 10 ? "0" + d.getDate() : d.getDate())
                    formatted_date += "t05:00:00.000";
                    $scope.properties.JSONUsuarioRegistrado.fechanacimiento = formatted_date;
                    if ($scope.properties.objSolicitudDeAdmision.catEstado === null || $scope.properties.objSolicitudDeAdmision.catEstado === undefined) {
                        $scope.properties.JSONUsuarioRegistrado.estado = null;
                        $scope.properties.JSONUsuarioRegistrado.estadoextranjero = $scope.properties.objSolicitudDeAdmision.estadoextranjero;
                    } else {
                        $scope.properties.JSONUsuarioRegistrado.estado = $scope.properties.objSolicitudDeAdmision.catEstado.persistenceId;
                        $scope.properties.JSONUsuarioRegistrado.estadoextranjero = "";
                        /*if ($scope.properties.objSolicitudDeAdmision.catEstado.descripcion !== $scope.properties.jsonOriginal.estado) {
                            existecambio = true;
                        }
                    }

                    $scope.properties.JSONUsuarioRegistrado.bachillerato = parseInt($scope.properties.objSolicitudDeAdmision.catBachilleratos.persistenceid);
                    $scope.properties.JSONUsuarioRegistrado.nombrebachillerato = $scope.properties.datosPreparatoria.nombreBachillerato;
                    $scope.properties.JSONUsuarioRegistrado.paisbachillerato = $scope.properties.datosPreparatoria.paisBachillerato;
                    $scope.properties.JSONUsuarioRegistrado.estadobachillerato = $scope.properties.datosPreparatoria.estadoBachillerato;
                    $scope.properties.JSONUsuarioRegistrado.ciudadbachillerato = $scope.properties.datosPreparatoria.ciudadBachillerato;
                    $scope.properties.JSONUsuarioRegistrado.promedio = $scope.properties.objSolicitudDeAdmision.promediogeneral + "";
                    $scope.properties.JSONUsuarioRegistrado.catCampusEstudio = $scope.properties.objSolicitudDeAdmision.catCampusEstudio;
                    $scope.properties.JSONUsuarioRegistrado.catGestionEscolar = $scope.properties.objSolicitudDeAdmision.catGestionEscolar;
                    $scope.properties.JSONUsuarioRegistrado.catPeriodo = $scope.properties.objSolicitudDeAdmision.catPeriodo;
                    $scope.properties.JSONUsuarioRegistrado.catCampus = $scope.properties.objSolicitudDeAdmision.catCampus;
                    $scope.properties.JSONUsuarioRegistrado.catSexo = $scope.properties.objSolicitudDeAdmision.catSexo;
                    $scope.properties.JSONUsuarioRegistrado.catEstado = $scope.properties.objSolicitudDeAdmision.catEstado;
                    $scope.properties.JSONUsuarioRegistrado.catBachilleratos = $scope.properties.objSolicitudDeAdmision.catBachilleratos;

                    $scope.properties.JSONUsuarioRegistrado.catEstadoExamen = $scope.properties.objSolicitudDeAdmision.catEstadoExamen;
                    $scope.properties.JSONUsuarioRegistrado.catPaisExamen = $scope.properties.objSolicitudDeAdmision.catPaisExamen;
                    $scope.properties.JSONUsuarioRegistrado.catCiudadExamen = $scope.properties.objSolicitudDeAdmision.catCiudadExamen;
                    $scope.properties.JSONUsuarioRegistrado.catResidencia = $scope.properties.objSolicitudDeAdmision.catResidencia;
                    $scope.properties.JSONUsuarioRegistrado.catTipoAlumno = $scope.properties.objSolicitudDeAdmision.catTipoAlumno;
                    $scope.properties.JSONUsuarioRegistrado.catTipoAdmision = $scope.properties.objSolicitudDeAdmision.catTipoAdmision;
                    $scope.properties.JSONUsuarioRegistrado.catLugarExamen = $scope.properties.objSolicitudDeAdmision.catLugarExamen;

                    $scope.properties.JSONUsuarioRegistrado.resultadoPAA = $scope.properties.objSolicitudDeAdmision.resultadopaa;
                    $scope.properties.JSONUsuarioRegistrado.tienePAA = ($scope.properties.objSolicitudDeAdmision.tienepaa == "t" ? true : false);
                    $scope.properties.JSONUsuarioRegistrado.cbCoincide = $scope.properties.objSolicitudDeAdmision.cbcoincide2;
                    $scope.properties.JSONUsuarioRegistrado.descuento = $scope.properties.objSolicitudDeAdmision.descuento;
                    $scope.properties.JSONUsuarioRegistrado.Documentos = angular.copy($scope.properties.archivos);

                    $scope.properties.JSONUsuarioRegistrado.observacionListaRoja = $scope.properties.objSolicitudDeAdmision.observacioneslistaroja;

                    /*if ($scope.properties.objSolicitudDeAdmision.tienepaa == "f" && $scope.properties.objSolicitudDeAdmision.resultadopaa >= 1) {
                        $scope.properties.JSONUsuarioRegistrado.tienePAA = true;
                        $scope.properties.JSONUsuarioRegistrado.resultadoPAA = $scope.properties.objSolicitudDeAdmision.resultadopaa;
                    }

                    if ($scope.properties.JSONUsuarioRegistrado.catLugarExamen.descripcion.toUpperCase() === "EN UN ESTADO") {
                        $scope.properties.JSONUsuarioRegistrado.catPaisExamen = null;
                    } else if ($scope.properties.JSONUsuarioRegistrado.catLugarExamen.descripcion.toUpperCase() === "EN EL EXTRANJERO (SOLO SI VIVES FUERA DE MÉXICO)") {
                        $scope.properties.JSONUsuarioRegistrado.catEstadoExamen = null;
                        for (var pais = 0; pais < $scope.properties.catPais.length; pais++) {
                            if ($scope.properties.objSolicitudDeAdmision.catPaisExamen === $scope.properties.catPais[pais].descripcion) {
                                $scope.properties.JSONUsuarioRegistrado.catPaisExamen = $scope.properties.catPais[pais];
                            }
                        }
                    } else {
                        $scope.properties.JSONUsuarioRegistrado.catEstadoExamen = null;
                        $scope.properties.JSONUsuarioRegistrado.catPaisExamen = null;
                        $scope.properties.JSONUsuarioRegistrado.catCiudadExamen = null;
                    }
                    /*
                    if ($scope.properties.JSONUsuarioRegistrado.primernombre !== $scope.properties.jsonOriginal.primernombre) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.segundonombre !== $scope.properties.jsonOriginal.segundonombre) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.apellidopaterno !== $scope.properties.jsonOriginal.apellidopaterno) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.apellidomaterno !== $scope.properties.jsonOriginal.apellidomaterno) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.correoelectronico !== $scope.properties.jsonOriginal.correoelectronico) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.catCampusEstudio.descripcion !== $scope.properties.jsonOriginal.campussede) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.catGestionEscolar.descripcion !== $scope.properties.jsonOriginal.licenciatura) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.catPeriodo.descripcion !== $scope.properties.jsonOriginal.ingreso) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.propedeutico === null || $scope.properties.JSONUsuarioRegistrado.propedeutico === undefined) {
                        if ($scope.properties.JSONUsuarioRegistrado.propedeutico !== $scope.properties.jsonOriginal.propedeutico) {
                            existecambio = true;
                        }
                    } else {
                        if ($scope.properties.JSONUsuarioRegistrado.catPropedeutico.descripcion !== $scope.properties.jsonOriginal.propedeutico) {
                            existecambio = true;
                        }
                    }

                    if ($scope.properties.JSONUsuarioRegistrado.catCampus.descripcion !== $scope.properties.jsonOriginal.campus) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.catSexo.descripcion !== $scope.properties.jsonOriginal.sexo) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.estadoextranjero !== $scope.properties.jsonOriginal.estadoextranjero) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.catBachilleratos.descripcion !== $scope.properties.jsonOriginal.prepacatalogo) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.paisbachillerato !== $scope.properties.jsonOriginal.paisbachillerato) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.estadobachillerato !== $scope.properties.jsonOriginal.estadobachillerato) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.ciudadbachillerato !== $scope.properties.jsonOriginal.ciudadbachillerato) {
                        existecambio = true;
                    }
                    if ($scope.properties.JSONUsuarioRegistrado.promedio !== $scope.properties.jsonOriginal.promediogeneral) {
                        existecambio = true;
                    }
                    if (!existecambio) {
                        swal("¡Aviso!", "No se realizó ninguna modificación", "warning");
                    }  else {
                        var jsonAnterior = {};
                        var jsonNuevo = {};
                        jsonAnterior.primernombre = $scope.properties.jsonOriginal.primernombre;
                        jsonAnterior.segundonombre = $scope.properties.jsonOriginal.segundonombre;
                        jsonAnterior.apellidopaterno = $scope.properties.jsonOriginal.apellidopaterno;
                        jsonAnterior.apellidomaterno = $scope.properties.jsonOriginal.apellidomaterno;
                        //jsonAnterior.correoelectronico = $scope.properties.jsonOriginal.correoelectronico;
                        jsonAnterior.campusestudio = $scope.properties.jsonOriginal.campussede;
                        jsonAnterior.licenciatura = $scope.properties.jsonOriginal.licenciatura;
                        jsonAnterior.periodo = $scope.properties.jsonOriginal.ingreso;
                        jsonAnterior.propedeutico = $scope.properties.jsonOriginal.propedeutico;
                        jsonAnterior.campus = $scope.properties.jsonOriginal.campus;
                        jsonAnterior.sexo = $scope.properties.jsonOriginal.sexo;
                        jsonAnterior.estadoextranjero = $scope.properties.jsonOriginal.estadoextranjero;
                        jsonAnterior.estado = $scope.properties.jsonOriginal.estado;
                        jsonAnterior.bachillerato = $scope.properties.jsonOriginal.prepacatalogo;
                        jsonAnterior.paisbachillerato = $scope.properties.jsonOriginal.paisbachillerato;
                        jsonAnterior.estadobachillerato = $scope.properties.jsonOriginal.estadobachillerato;
                        jsonAnterior.ciudadbachillerato = $scope.properties.jsonOriginal.ciudadbachillerato;
                        jsonAnterior.promedio = $scope.properties.jsonOriginal.promediogeneral;

                        jsonNuevo.primernombre = $scope.properties.JSONUsuarioRegistrado.primernombre;
                        jsonNuevo.segundonombre = $scope.properties.JSONUsuarioRegistrado.segundonombre;
                        jsonNuevo.apellidopaterno = $scope.properties.JSONUsuarioRegistrado.apellidopaterno;
                        jsonNuevo.apellidomaterno = $scope.properties.JSONUsuarioRegistrado.apellidomaterno;
                        //jsonNuevo.correoelectronico = $scope.properties.JSONUsuarioRegistrado.correoelectronico;
                        jsonNuevo.campusestudio = $scope.properties.JSONUsuarioRegistrado.campussede;
                        jsonNuevo.licenciatura = $scope.properties.JSONUsuarioRegistrado.licenciatura;
                        jsonNuevo.periodo = $scope.properties.JSONUsuarioRegistrado.ingreso;
                        jsonNuevo.propedeutico = $scope.properties.JSONUsuarioRegistrado.propedeutico;
                        jsonNuevo.campus = $scope.properties.JSONUsuarioRegistrado.campus;
                        jsonNuevo.sexo = $scope.properties.JSONUsuarioRegistrado.sexo;
                        jsonNuevo.estadoextranjero = $scope.properties.JSONUsuarioRegistrado.estadoextranjero;
                        jsonNuevo.estado = $scope.properties.JSONUsuarioRegistrado.estado;
                        jsonNuevo.bachillerato = $scope.properties.JSONUsuarioRegistrado.prepacatalogo;
                        jsonNuevo.paisbachillerato = $scope.properties.JSONUsuarioRegistrado.paisbachillerato;
                        jsonNuevo.estadobachillerato = $scope.properties.JSONUsuarioRegistrado.estadobachillerato;
                        jsonNuevo.ciudadbachillerato = $scope.properties.JSONUsuarioRegistrado.ciudadbachillerato;
                        jsonNuevo.promedio = $scope.properties.JSONUsuarioRegistrado.promediogeneral;

                        doRequest($scope.properties.action, $scope.properties.url);
                    //}

                }*/



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
                var prom = doRequest('POST', '../API/bpm/process/' + id + '/instantiation', getUserParam()).then(function() {
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
                .success(function(data, status) {
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
                .error(function(data, status) {
                    $scope.properties.dataFromError = data;
                    $scope.properties.responseStatusCode = status;
                    $scope.properties.dataFromSuccess = undefined;
                    notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                })
                .finally(function() {
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
                doRequest('POST', '../API/bpm/userTask/' + getUrlParam('id') + '/execution', params).then(function() {
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
                .success(function(data, status) {
                    $scope.properties.lstContenido = data.data;
                    $scope.properties.editable = "tabla";
                    swal("¡Usuario modificado!", "Se han modificado los datos del usuario correctamente", "success");
                })
                .error(function(data, status) {
                    notifyParentFrame({
                        message: 'error',
                        status: status,
                        dataFromError: data,
                        dataFromSuccess: undefined,
                        responseStatusCode: status
                    });
                })
                .finally(function() {
                    blockUI.stop();
                    closeModal($scope.properties.closeOnSuccess);
                });
        }

    }