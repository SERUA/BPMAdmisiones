function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        $scope.agregootro = false;
        $scope.faltaotro = false;
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
            openModal($scope.properties.modalId);
        } else if ($scope.properties.action === 'Close modal') {
            $scope.properties.tutor.isTutor = true;
            debugger;
            if ($scope.properties.tutor.catTitulo != $scope.properties.jsonModificarTutor.catTitulo) {
                $scope.properties.tutor.catTitulo = $scope.properties.jsonModificarTutor.catTitulo
            }
            if ($scope.properties.tutor.catParentezco != $scope.properties.jsonModificarTutor.catParentezco) {
                $scope.properties.tutor.catParentezco = $scope.properties.jsonModificarTutor.catParentezco
                if ($scope.properties.jsonModificarTutor.catParentezco.descripcion === "Padre") {
                                $scope.properties.padre.catTitulo = $scope.properties.tutor.catTitulo;
                                $scope.properties.padre.nombre = $scope.properties.tutor.nombre;
                                $scope.properties.padre.apellidos = $scope.properties.tutor.apellidos;
                                $scope.properties.padre.correoElectronico = $scope.properties.tutor.correoElectronico;
                                $scope.properties.padre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                                $scope.properties.padre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                                $scope.properties.padre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                                $scope.properties.padre.catTrabaja = $scope.properties.tutor.catTrabaja;
                                $scope.properties.padre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                                $scope.properties.padre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                                $scope.properties.padre.puesto = $scope.properties.tutor.puesto;
                                $scope.properties.padre.isTutor = $scope.properties.tutor.isTutor;
                                $scope.properties.padre.calle = $scope.properties.tutor.calle;
                                $scope.properties.padre.catPais = $scope.properties.tutor.catPais;
                                $scope.properties.padre.numeroExterior = $scope.properties.tutor.numeroExterior;
                                $scope.properties.padre.numeroInterior = $scope.properties.tutor.numeroInterior;
                                $scope.properties.padre.catEstado = $scope.properties.tutor.catEstado;
                                $scope.properties.padre.ciudad = $scope.properties.tutor.ciudad;
                                $scope.properties.padre.colonia = $scope.properties.tutor.colonia;
                                $scope.properties.padre.telefono = $scope.properties.tutor.telefono;
                                $scope.properties.padre.codigoPostal = $scope.properties.tutor.codigoPostal;
                                $scope.properties.padre.viveContigo = $scope.properties.tutor.viveContigo;
                                for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                    if ($scope.properties.catVive[i].descripcion === "Si") {
                                        $scope.properties.padre.vive = $scope.properties.catVive[i];
                                    }
                                }
                                
                                if($scope.properties.madre.vive.descripcion === "Si" && $scope.properties.madre.isTutor){
                                    $scope.properties.madre.vive = null;
                                    $scope.properties.madre.catTitulo = null;
                                    $scope.properties.madre.nombre = "";
                                    $scope.properties.madre.apellidos = "";
                                    $scope.properties.madre.correoElectronico = "";
                                    $scope.properties.madre.catEscolaridad = null;
                                    $scope.properties.madre.catEgresoAnahuac = null;
                                    $scope.properties.madre.catCampusEgreso = null;
                                    $scope.properties.madre.catTrabaja = null;
                                    $scope.properties.madre.empresaTrabaja = "";
                                    $scope.properties.madre.giroEmpresa = "";
                                    $scope.properties.madre.puesto = "";
                                    $scope.properties.madre.isTutor = false;
                                    $scope.properties.madre.calle = "";
                                    $scope.properties.madre.catPais = null;
                                    $scope.properties.madre.numeroExterior = "";
                                    $scope.properties.madre.numeroInterior = "";
                                    $scope.properties.madre.catEstado = null;
                                    $scope.properties.madre.ciudad = "";
                                    $scope.properties.madre.colonia = "";
                                    $scope.properties.madre.telefono = "";
                                    $scope.properties.madre.codigoPostal = "";
                                    $scope.properties.madre.viveContigo = false;
                                }
                            } else if ($scope.properties.jsonModificarTutor.catParentezco.descripcion === "Madre") {
                                $scope.properties.madre.catTitulo = $scope.properties.tutor.catTitulo;
                                $scope.properties.madre.nombre = $scope.properties.tutor.nombre;
                                $scope.properties.madre.apellidos = $scope.properties.tutor.apellidos;
                                $scope.properties.madre.correoElectronico = $scope.properties.tutor.correoElectronico;
                                $scope.properties.madre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                                $scope.properties.madre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                                $scope.properties.madre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                                $scope.properties.madre.catTrabaja = $scope.properties.tutor.catTrabaja;
                                $scope.properties.madre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                                $scope.properties.madre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                                $scope.properties.madre.puesto = $scope.properties.tutor.puesto;
                                $scope.properties.madre.isTutor = $scope.properties.tutor.isTutor;
                                $scope.properties.madre.calle = $scope.properties.tutor.calle;
                                $scope.properties.madre.catPais = $scope.properties.tutor.catPais;
                                $scope.properties.madre.numeroExterior = $scope.properties.tutor.numeroExterior;
                                $scope.properties.madre.numeroInterior = $scope.properties.tutor.numeroInterior;
                                $scope.properties.madre.catEstado = $scope.properties.tutor.catEstado;
                                $scope.properties.madre.ciudad = $scope.properties.tutor.ciudad;
                                $scope.properties.madre.colonia = $scope.properties.tutor.colonia;
                                $scope.properties.madre.telefono = $scope.properties.tutor.telefono;
                                $scope.properties.madre.codigoPostal = $scope.properties.tutor.codigoPostal;
                                $scope.properties.madre.viveContigo = $scope.properties.tutor.viveContigo;
                                for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                    if ($scope.properties.catVive[i].descripcion === "Si") {
                                        $scope.properties.madre.vive = $scope.properties.catVive[i];
                                    }
                                }
                                if($scope.properties.padre.vive.descripcion === "Si" && $scope.properties.padre.isTutor){
                                    $scope.properties.padre.catTitulo = null;
                                    $scope.properties.padre.nombre = "";
                                    $scope.properties.padre.apellidos = "";
                                    $scope.properties.padre.correoElectronico = "";
                                    $scope.properties.padre.catEscolaridad = null;
                                    $scope.properties.padre.catEgresoAnahuac = null;
                                    $scope.properties.padre.catCampusEgreso = null;
                                    $scope.properties.padre.catTrabaja = null;
                                    $scope.properties.padre.empresaTrabaja = "";
                                    $scope.properties.padre.giroEmpresa = "";
                                    $scope.properties.padre.puesto = "";
                                    $scope.properties.padre.isTutor = false;
                                    $scope.properties.padre.calle = "";
                                    $scope.properties.padre.catPais = null;
                                    $scope.properties.padre.numeroExterior = "";
                                    $scope.properties.padre.numeroInterior = "";
                                    $scope.properties.padre.catEstado = null;
                                    $scope.properties.padre.ciudad = "";
                                    $scope.properties.padre.colonia = "";
                                    $scope.properties.padre.telefono = "";
                                    $scope.properties.padre.codigoPostal = "";
                                    $scope.properties.padre.viveContigo = false;
                                    $scope.properties.padre.vive = null;
                                }
                            }else{
                                if($scope.properties.padre.vive.descripcion === "Si" && $scope.properties.padre.isTutor){
                                    $scope.properties.padre.catTitulo = null;
                                    $scope.properties.padre.nombre = "";
                                    $scope.properties.padre.apellidos = "";
                                    $scope.properties.padre.correoElectronico = "";
                                    $scope.properties.padre.catEscolaridad = null;
                                    $scope.properties.padre.catEgresoAnahuac = null;
                                    $scope.properties.padre.catCampusEgreso = null;
                                    $scope.properties.padre.catTrabaja = null;
                                    $scope.properties.padre.empresaTrabaja = "";
                                    $scope.properties.padre.giroEmpresa = "";
                                    $scope.properties.padre.puesto = "";
                                    $scope.properties.padre.isTutor = false;
                                    $scope.properties.padre.calle = "";
                                    $scope.properties.padre.catPais = null;
                                    $scope.properties.padre.numeroExterior = "";
                                    $scope.properties.padre.numeroInterior = "";
                                    $scope.properties.padre.catEstado = null;
                                    $scope.properties.padre.ciudad = "";
                                    $scope.properties.padre.colonia = "";
                                    $scope.properties.padre.telefono = "";
                                    $scope.properties.padre.codigoPostal = "";
                                    $scope.properties.padre.viveContigo = false;
                                    $scope.properties.padre.vive = null;
                                    if($scope.properties.contactoEmergencia.length > 0){
                                        for(var x=0; x<$scope.properties.contactoEmergencia.length; x++){
                                            if($scope.properties.contactoEmergencia[x].catParentesco.descripcion === "Padre"){
                                                $scope.properties.contactoEmergencia.splice(x,1);
                                            }
                                        }
                                    }
                                }
                                if($scope.properties.madre.vive.descripcion === "Si" && $scope.properties.madre.isTutor){
                                    $scope.properties.madre.vive = null;
                                    $scope.properties.madre.catTitulo = null;
                                    $scope.properties.madre.nombre = "";
                                    $scope.properties.madre.apellidos = "";
                                    $scope.properties.madre.correoElectronico = "";
                                    $scope.properties.madre.catEscolaridad = null;
                                    $scope.properties.madre.catEgresoAnahuac = null;
                                    $scope.properties.madre.catCampusEgreso = null;
                                    $scope.properties.madre.catTrabaja = null;
                                    $scope.properties.madre.empresaTrabaja = "";
                                    $scope.properties.madre.giroEmpresa = "";
                                    $scope.properties.madre.puesto = "";
                                    $scope.properties.madre.isTutor = false;
                                    $scope.properties.madre.calle = "";
                                    $scope.properties.madre.catPais = null;
                                    $scope.properties.madre.numeroExterior = "";
                                    $scope.properties.madre.numeroInterior = "";
                                    $scope.properties.madre.catEstado = null;
                                    $scope.properties.madre.ciudad = "";
                                    $scope.properties.madre.colonia = "";
                                    $scope.properties.madre.telefono = "";
                                    $scope.properties.madre.codigoPostal = "";
                                    $scope.properties.madre.viveContigo = false;
                                    if($scope.properties.contactoEmergencia.length > 0){
                                        for(var x=0; x<$scope.properties.contactoEmergencia.length; x++){
                                            if($scope.properties.contactoEmergencia[x].catParentesco.descripcion === "Madre"){
                                                $scope.properties.contactoEmergencia.splice(x,1);
                                            }
                                        }
                                    }
                                }
                            }
            }
            if ($scope.properties.tutor.giroEmpresa != $scope.properties.jsonModificarTutor.giroEmpresa) {
                $scope.properties.tutor.giroEmpresa = $scope.properties.jsonModificarTutor.giroEmpresa
            }
            if ($scope.properties.tutor.numeroInterior != $scope.properties.jsonModificarTutor.numeroInterior) {
                $scope.properties.tutor.numeroInterior = $scope.properties.jsonModificarTutor.numeroInterior
            }
            if ($scope.properties.tutor.viveContigo != $scope.properties.jsonModificarTutor.viveContigo) {
                $scope.properties.tutor.viveContigo = $scope.properties.jsonModificarTutor.viveContigo
            }
            if ($scope.properties.tutor.otroParentesco != $scope.properties.jsonModificarTutor.otroParentesco) {
                $scope.properties.tutor.otroParentesco = $scope.properties.jsonModificarTutor.otroParentesco
            }
            if ($scope.properties.tutor.nombre != $scope.properties.jsonModificarTutor.nombre) {
                $scope.properties.tutor.nombre = $scope.properties.jsonModificarTutor.nombre
            }
            if ($scope.properties.tutor.apellidos !== $scope.properties.jsonModificarTutor.apellidos) {
                $scope.properties.tutor.apellidos = $scope.properties.jsonModificarTutor.apellidos
            }
            if ($scope.properties.tutor.correoElectronico !== $scope.properties.jsonModificarTutor.correoElectronico) {
                $scope.properties.tutor.correoElectronico = $scope.properties.jsonModificarTutor.correoElectronico
            }
            if ($scope.properties.tutor.catEgresoAnahuac !== $scope.properties.jsonModificarTutor.catEgresoAnahuac) {
                $scope.properties.tutor.catEgresoAnahuac = $scope.properties.jsonModificarTutor.catEgresoAnahuac
            }
            if ($scope.properties.tutor.catCampusEgreso !== $scope.properties.jsonModificarTutor.catCampusEgreso) {
                $scope.properties.tutor.catCampusEgreso = $scope.properties.jsonModificarTutor.catCampusEgreso
            }
            if ($scope.properties.tutor.catTrabaja !== $scope.properties.jsonModificarTutor.catTrabaja) {
                $scope.properties.tutor.catTrabaja = $scope.properties.jsonModificarTutor.catTrabaja
            }
            if ($scope.properties.tutor.empresaTrabaja !== $scope.properties.jsonModificarTutor.empresaTrabaja) {
                $scope.properties.tutor.empresaTrabaja = $scope.properties.jsonModificarTutor.empresaTrabaja
            }
            if ($scope.properties.tutor.puesto !== $scope.properties.jsonModificarTutor.puesto) {
                $scope.properties.tutor.puesto = $scope.properties.jsonModificarTutor.puesto
            }
            if ($scope.properties.tutor.catEscolaridad !== $scope.properties.jsonModificarTutor.catEscolaridad) {
                $scope.properties.tutor.catEscolaridad = $scope.properties.jsonModificarTutor.catEscolaridad
            }
            if ($scope.properties.tutor.isTutor !== $scope.properties.jsonModificarTutor.isTutor) {
                $scope.properties.tutor.isTutor = $scope.properties.jsonModificarTutor.isTutor
            }
            if ($scope.properties.tutor.calle !== $scope.properties.jsonModificarTutor.calle) {
                $scope.properties.tutor.calle = $scope.properties.jsonModificarTutor.calle
            }
            if ($scope.properties.tutor.catPais !== $scope.properties.jsonModificarTutor.catPais) {
                $scope.properties.tutor.catPais = $scope.properties.jsonModificarTutor.catPais
            }
            if ($scope.properties.tutor.numeroExterior !== $scope.properties.jsonModificarTutor.numeroExterior) {
                $scope.properties.tutor.numeroExterior = $scope.properties.jsonModificarTutor.numeroExterior
            }
            if ($scope.properties.tutor.catEstado !== $scope.properties.jsonModificarTutor.catEstado) {
                $scope.properties.tutor.catEstado = $scope.properties.jsonModificarTutor.catEstado
            }
            if ($scope.properties.tutor.ciudad !== $scope.properties.jsonModificarTutor.ciudad) {
                $scope.properties.tutor.ciudad = $scope.properties.jsonModificarTutor.ciudad
            }
            if ($scope.properties.tutor.colonia !== $scope.properties.jsonModificarTutor.colonia) {
                $scope.properties.tutor.colonia = $scope.properties.jsonModificarTutor.colonia
            }
            if ($scope.properties.tutor.telefono !== $scope.properties.jsonModificarTutor.telefono) {
                $scope.properties.tutor.telefono = $scope.properties.jsonModificarTutor.telefono
            }
            if ($scope.properties.tutor.codigoPostal !== $scope.properties.jsonModificarTutor.codigoPostal) {
                $scope.properties.tutor.codigoPostal = $scope.properties.jsonModificarTutor.codigoPostal
            }
            if ($scope.properties.tutor.catTitulo === null) {
                swal("Título!", "Debe seleccionar el título para identificar al tutor!", "warning");
                $scope.faltaotro = true;
            } else if ($scope.properties.tutor.catParentezco === null) {
                swal("Parentesco!", "Debe seleccionar el parentesco con el tutor!", "warning");
                $scope.faltaotro = true;
            } else if ($scope.properties.otroparentesco) {
                if ($scope.properties.tutor.otroParentesco === undefined || $scope.properties.tutor.otroParentesco === "") {
                    swal("Parentesco!", "Debe especificar el parentesco con el tutor!", "warning");
                    $scope.faltaotro = true;
                } else {
                    $scope.agregootro = true;
                }
            }
            if ($scope.agregootro) {
                if ($scope.properties.tutor.nombre === "") {
                    swal("Nombre del tutor!", "Debe agregar nombre del tutor!", "warning");
                } else if ($scope.properties.tutor.apellidos === "") {
                    swal("Apellidos del tutor!", "Debe agregar los apellidos del tutor!", "warning");
                } else if ($scope.properties.tutor.correoElectronico === "") {
                    swal("Correo electrónico!", "Debe agregar el correo electrónico del tutor!", "warning");
                } else if ($scope.properties.tutor.catEgresoAnahuac === null) {
                    swal("Egreso Anahuac!", "Debe seleccionar si su tutor egresó de la universidad Anahuac!", "warning");
                } else if ($scope.properties.tutor.catEgresoAnahuac.descripcion === "Si") {
                    if ($scope.properties.tutor.catCampusEgreso === null) {
                        swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su tutor!", "warning");
                    } else if ($scope.properties.tutor.catTrabaja === null) {
                        swal("Trabaja!", "Debe seleccionar si su tutor trabaja!", "warning");
                    } else if ($scope.properties.tutor.catTrabaja.descripcion === "Si") {
                        if ($scope.properties.tutor.empresaTrabaja === "") {
                            swal("Empresa!", "Debe agregar el nombre de la empresa donde su tutor trabaja!", "warning");
                        } else if ($scope.properties.tutor.puesto === "") {
                            swal("Puesto!", "Debe agregar el puesto de trabajo del tutor!", "warning");
                        } else if ($scope.properties.tutor.catEscolaridad === null) {
                            swal("Escolaridad!", "Debe seleccionar la escolaridad del tutor!", "warning");
                        } else if (!$scope.properties.tutor.isTutor) {
                            console.log("falta tutor");
                        } else if ($scope.properties.tutor.calle === "") {
                            swal("Calle!", "Debe agregar la calle del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.catPais === null) {
                            swal("País!", "Debe agregar el país del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.numeroExterior === "") {
                            swal("Número exterior!", "Debe agregar el número exterior del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.catEstado === null) {
                            swal("Estado!", "Debe agregar el estado del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.ciudad === "") {
                            swal("Ciudad!", "Debe agregar la calle del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.colonia === "") {
                            swal("Colonia!", "Debe agregar la colonia del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.telefono === "") {
                            swal("Teléfono!", "Debe agregar el teléfono del tutor!", "warning");
                        } else if ($scope.properties.tutor.codigoPostal === "") {
                            swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                        } else {
                            if ($scope.properties.tutor.catParentezco.descripcion === "Padre") {
                                $scope.properties.padre.catTitulo = $scope.properties.tutor.catTitulo;
                                $scope.properties.padre.nombre = $scope.properties.tutor.nombre;
                                $scope.properties.padre.apellidos = $scope.properties.tutor.apellidos;
                                $scope.properties.padre.correoElectronico = $scope.properties.tutor.correoElectronico;
                                $scope.properties.padre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                                $scope.properties.padre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                                $scope.properties.padre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                                $scope.properties.padre.catTrabaja = $scope.properties.tutor.catTrabaja;
                                $scope.properties.padre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                                $scope.properties.padre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                                $scope.properties.padre.puesto = $scope.properties.tutor.puesto;
                                $scope.properties.padre.isTutor = $scope.properties.tutor.isTutor;
                                $scope.properties.padre.calle = $scope.properties.tutor.calle;
                                $scope.properties.padre.catPais = $scope.properties.tutor.catPais;
                                $scope.properties.padre.numeroExterior = $scope.properties.tutor.numeroExterior;
                                $scope.properties.padre.numeroInterior = $scope.properties.tutor.numeroInterior;
                                $scope.properties.padre.catEstado = $scope.properties.tutor.catEstado;
                                $scope.properties.padre.ciudad = $scope.properties.tutor.ciudad;
                                $scope.properties.padre.colonia = $scope.properties.tutor.colonia;
                                $scope.properties.padre.telefono = $scope.properties.tutor.telefono;
                                $scope.properties.padre.codigoPostal = $scope.properties.tutor.codigoPostal;
                                $scope.properties.padre.viveContigo = $scope.properties.tutor.viveContigo;
                                for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                    if ($scope.properties.catVive[i].descripcion === "Si") {
                                        $scope.properties.padre.vive = $scope.properties.catVive[i];
                                    }
                                }
                            } else if ($scope.properties.tutor.catParentezco.descripcion === "Madre") {
                                $scope.properties.madre.catTitulo = $scope.properties.tutor.catTitulo;
                                $scope.properties.madre.nombre = $scope.properties.tutor.nombre;
                                $scope.properties.madre.apellidos = $scope.properties.tutor.apellidos;
                                $scope.properties.madre.correoElectronico = $scope.properties.tutor.correoElectronico;
                                $scope.properties.madre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                                $scope.properties.madre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                                $scope.properties.madre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                                $scope.properties.madre.catTrabaja = $scope.properties.tutor.catTrabaja;
                                $scope.properties.madre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                                $scope.properties.madre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                                $scope.properties.madre.puesto = $scope.properties.tutor.puesto;
                                $scope.properties.madre.isTutor = $scope.properties.tutor.isTutor;
                                $scope.properties.madre.calle = $scope.properties.tutor.calle;
                                $scope.properties.madre.catPais = $scope.properties.tutor.catPais;
                                $scope.properties.madre.numeroExterior = $scope.properties.tutor.numeroExterior;
                                $scope.properties.madre.numeroInterior = $scope.properties.tutor.numeroInterior;
                                $scope.properties.madre.catEstado = $scope.properties.tutor.catEstado;
                                $scope.properties.madre.ciudad = $scope.properties.tutor.ciudad;
                                $scope.properties.madre.colonia = $scope.properties.tutor.colonia;
                                $scope.properties.madre.telefono = $scope.properties.tutor.telefono;
                                $scope.properties.madre.codigoPostal = $scope.properties.tutor.codigoPostal;
                                $scope.properties.madre.viveContigo = $scope.properties.tutor.viveContigo;
                                for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                    if ($scope.properties.catVive[i].descripcion === "Si") {
                                        $scope.properties.madre.vive = $scope.properties.catVive[i];
                                    }
                                }
                            }
                            $scope.properties.tutor = {
                                "catTitulo": {
                                    "persistenceId_string": ""
                                },
                                "catParentezco": {
                                    "persistenceId_string": ""
                                },
                                "nombre": "",
                                "apellidos": "",
                                "correoElectronico": "",
                                "catEscolaridad": {
                                    "persistenceId_string": ""
                                },
                                "catEgresoAnahuac": {
                                    "persistenceId_string": ""
                                },
                                "catCampusEgreso": {
                                    "persistenceId_string": ""
                                },
                                "catTrabaja": {
                                    "persistenceId_string": ""
                                },
                                "empresaTrabaja": "",
                                "giroEmpresa": "",
                                "puesto": "",
                                "isTutor": false,
                                "vive": {
                                    "persistenceId_string": ""
                                },
                                "calle": "",
                                "catPais": {
                                    "persistenceId_string": ""
                                },
                                "numeroExterior": "",
                                "numeroInterior": "",
                                "catEstado": {
                                    "persistenceId_string": ""
                                },
                                "ciudad": "",
                                "colonia": "",
                                "telefono": "",
                                "codigoPostal": "",
                                "viveContigo": false,
                                "otroParentesco": ""
                            };
                            closeModal(true);
                        }
                    } else if ($scope.properties.tutor.catEscolaridad === null) {
                        swal("Escolaridad!", "Debe seleccionar la escolaridad del tutor!", "warning");
                    } else if (!$scope.properties.tutor.isTutor) {
                        console.log("falta tutor");
                    } else if ($scope.properties.tutor.calle === "") {
                        swal("Calle!", "Debe agregar la calle del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.catPais === null) {
                        swal("País!", "Debe agregar el país del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.numeroExterior === "") {
                        swal("Número exterior!", "Debe agregar el número exterior del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.catEstado === null) {
                        swal("Estado!", "Debe agregar el estado del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.ciudad === "") {
                        swal("Ciudad!", "Debe agregar la calle del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.colonia === "") {
                        swal("Colonia!", "Debe agregar la colonia del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.telefono === "") {
                        swal("Teléfono!", "Debe agregar el teléfono del tutor!", "warning");
                    } else if ($scope.properties.tutor.codigoPostal === "") {
                        swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                    } else {
                        if ($scope.properties.tutor.catParentezco.descripcion === "Padre") {
                            $scope.properties.padre.catTitulo = $scope.properties.tutor.catTitulo;
                            $scope.properties.padre.nombre = $scope.properties.tutor.nombre;
                            $scope.properties.padre.apellidos = $scope.properties.tutor.apellidos;
                            $scope.properties.padre.correoElectronico = $scope.properties.tutor.correoElectronico;
                            $scope.properties.padre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                            $scope.properties.padre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                            $scope.properties.padre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                            $scope.properties.padre.catTrabaja = $scope.properties.tutor.catTrabaja;
                            $scope.properties.padre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                            $scope.properties.padre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                            $scope.properties.padre.puesto = $scope.properties.tutor.puesto;
                            $scope.properties.padre.isTutor = $scope.properties.tutor.isTutor;
                            $scope.properties.padre.calle = $scope.properties.tutor.calle;
                            $scope.properties.padre.catPais = $scope.properties.tutor.catPais;
                            $scope.properties.padre.numeroExterior = $scope.properties.tutor.numeroExterior;
                            $scope.properties.padre.numeroInterior = $scope.properties.tutor.numeroInterior;
                            $scope.properties.padre.catEstado = $scope.properties.tutor.catEstado;
                            $scope.properties.padre.ciudad = $scope.properties.tutor.ciudad;
                            $scope.properties.padre.colonia = $scope.properties.tutor.colonia;
                            $scope.properties.padre.telefono = $scope.properties.tutor.telefono;
                            $scope.properties.padre.codigoPostal = $scope.properties.tutor.codigoPostal;
                            $scope.properties.padre.viveContigo = $scope.properties.tutor.viveContigo;
                            for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                if ($scope.properties.catVive[i].descripcion === "Si") {
                                    $scope.properties.padre.vive = $scope.properties.catVive[i];
                                }
                            }
                        } else if ($scope.properties.tutor.catParentezco.descripcion === "Madre") {
                            $scope.properties.madre.catTitulo = $scope.properties.tutor.catTitulo;
                            $scope.properties.madre.nombre = $scope.properties.tutor.nombre;
                            $scope.properties.madre.apellidos = $scope.properties.tutor.apellidos;
                            $scope.properties.madre.correoElectronico = $scope.properties.tutor.correoElectronico;
                            $scope.properties.madre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                            $scope.properties.madre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                            $scope.properties.madre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                            $scope.properties.madre.catTrabaja = $scope.properties.tutor.catTrabaja;
                            $scope.properties.madre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                            $scope.properties.madre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                            $scope.properties.madre.puesto = $scope.properties.tutor.puesto;
                            $scope.properties.madre.isTutor = $scope.properties.tutor.isTutor;
                            $scope.properties.madre.calle = $scope.properties.tutor.calle;
                            $scope.properties.madre.catPais = $scope.properties.tutor.catPais;
                            $scope.properties.madre.numeroExterior = $scope.properties.tutor.numeroExterior;
                            $scope.properties.madre.numeroInterior = $scope.properties.tutor.numeroInterior;
                            $scope.properties.madre.catEstado = $scope.properties.tutor.catEstado;
                            $scope.properties.madre.ciudad = $scope.properties.tutor.ciudad;
                            $scope.properties.madre.colonia = $scope.properties.tutor.colonia;
                            $scope.properties.madre.telefono = $scope.properties.tutor.telefono;
                            $scope.properties.madre.codigoPostal = $scope.properties.tutor.codigoPostal;
                            $scope.properties.madre.viveContigo = $scope.properties.tutor.viveContigo;
                            for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                if ($scope.properties.catVive[i].descripcion === "Si") {
                                    $scope.properties.madre.vive = $scope.properties.catVive[i];
                                }
                            }
                        }
                        $scope.properties.tutor = {
                            "catTitulo": {
                                "persistenceId_string": ""
                            },
                            "catParentezco": {
                                "persistenceId_string": ""
                            },
                            "nombre": "",
                            "apellidos": "",
                            "correoElectronico": "",
                            "catEscolaridad": {
                                "persistenceId_string": ""
                            },
                            "catEgresoAnahuac": {
                                "persistenceId_string": ""
                            },
                            "catCampusEgreso": {
                                "persistenceId_string": ""
                            },
                            "catTrabaja": {
                                "persistenceId_string": ""
                            },
                            "empresaTrabaja": "",
                            "giroEmpresa": "",
                            "puesto": "",
                            "isTutor": false,
                            "vive": {
                                "persistenceId_string": ""
                            },
                            "calle": "",
                            "catPais": {
                                "persistenceId_string": ""
                            },
                            "numeroExterior": "",
                            "numeroInterior": "",
                            "catEstado": {
                                "persistenceId_string": ""
                            },
                            "ciudad": "",
                            "colonia": "",
                            "telefono": "",
                            "codigoPostal": "",
                            "viveContigo": false,
                            "otroParentesco": ""
                        };
                        closeModal(true);
                    }
                } else if ($scope.properties.tutor.catTrabaja === null) {
                    swal("Trabaja!", "Debe seleccionar si su tutor trabaja!", "warning");
                } else if ($scope.properties.tutor.catTrabaja.descripcion === "Si") {
                    if ($scope.properties.tutor.empresaTrabaja === "") {
                        swal("Empresa!", "Debe agregar el nombre de la empresa donde su tutor trabaja!", "warning");
                    } else if ($scope.properties.tutor.puesto === "") {
                        swal("Puesto!", "Debe agregar el puesto de trabajo del tutor!", "warning");
                    } else if ($scope.properties.tutor.catEscolaridad === null) {
                        swal("Escolaridad!", "Debe seleccionar la escolaridad del tutor!", "warning");
                    } else if (!$scope.properties.tutor.isTutor) {
                        console.log("falta tutor");
                    } else if ($scope.properties.tutor.calle === "") {
                        swal("Calle!", "Debe agregar la calle del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.catPais === null) {
                        swal("País!", "Debe agregar el país del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.numeroExterior === "") {
                        swal("Número exterior!", "Debe agregar el número exterior del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.catEstado === null) {
                        swal("Estado!", "Debe agregar el estado del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.ciudad === "") {
                        swal("Ciudad!", "Debe agregar la calle del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.colonia === "") {
                        swal("Colonia!", "Debe agregar la colonia del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.telefono === "") {
                        swal("Teléfono!", "Debe agregar el teléfono del tutor!", "warning");
                    } else if ($scope.properties.tutor.codigoPostal === "") {
                        swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                    } else {
                        if ($scope.properties.tutor.catParentezco.descripcion === "Padre") {
                            $scope.properties.padre.catTitulo = $scope.properties.tutor.catTitulo;
                            $scope.properties.padre.nombre = $scope.properties.tutor.nombre;
                            $scope.properties.padre.apellidos = $scope.properties.tutor.apellidos;
                            $scope.properties.padre.correoElectronico = $scope.properties.tutor.correoElectronico;
                            $scope.properties.padre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                            $scope.properties.padre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                            $scope.properties.padre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                            $scope.properties.padre.catTrabaja = $scope.properties.tutor.catTrabaja;
                            $scope.properties.padre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                            $scope.properties.padre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                            $scope.properties.padre.puesto = $scope.properties.tutor.puesto;
                            $scope.properties.padre.isTutor = $scope.properties.tutor.isTutor;
                            $scope.properties.padre.calle = $scope.properties.tutor.calle;
                            $scope.properties.padre.catPais = $scope.properties.tutor.catPais;
                            $scope.properties.padre.numeroExterior = $scope.properties.tutor.numeroExterior;
                            $scope.properties.padre.numeroInterior = $scope.properties.tutor.numeroInterior;
                            $scope.properties.padre.catEstado = $scope.properties.tutor.catEstado;
                            $scope.properties.padre.ciudad = $scope.properties.tutor.ciudad;
                            $scope.properties.padre.colonia = $scope.properties.tutor.colonia;
                            $scope.properties.padre.telefono = $scope.properties.tutor.telefono;
                            $scope.properties.padre.codigoPostal = $scope.properties.tutor.codigoPostal;
                            $scope.properties.padre.viveContigo = $scope.properties.tutor.viveContigo;
                            for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                if ($scope.properties.catVive[i].descripcion === "Si") {
                                    $scope.properties.padre.vive = $scope.properties.catVive[i];
                                }
                            }
                        } else if ($scope.properties.tutor.catParentezco.descripcion === "Madre") {
                            $scope.properties.madre.catTitulo = $scope.properties.tutor.catTitulo;
                            $scope.properties.madre.nombre = $scope.properties.tutor.nombre;
                            $scope.properties.madre.apellidos = $scope.properties.tutor.apellidos;
                            $scope.properties.madre.correoElectronico = $scope.properties.tutor.correoElectronico;
                            $scope.properties.madre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                            $scope.properties.madre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                            $scope.properties.madre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                            $scope.properties.madre.catTrabaja = $scope.properties.tutor.catTrabaja;
                            $scope.properties.madre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                            $scope.properties.madre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                            $scope.properties.madre.puesto = $scope.properties.tutor.puesto;
                            $scope.properties.madre.isTutor = $scope.properties.tutor.isTutor;
                            $scope.properties.madre.calle = $scope.properties.tutor.calle;
                            $scope.properties.madre.catPais = $scope.properties.tutor.catPais;
                            $scope.properties.madre.numeroExterior = $scope.properties.tutor.numeroExterior;
                            $scope.properties.madre.numeroInterior = $scope.properties.tutor.numeroInterior;
                            $scope.properties.madre.catEstado = $scope.properties.tutor.catEstado;
                            $scope.properties.madre.ciudad = $scope.properties.tutor.ciudad;
                            $scope.properties.madre.colonia = $scope.properties.tutor.colonia;
                            $scope.properties.madre.telefono = $scope.properties.tutor.telefono;
                            $scope.properties.madre.codigoPostal = $scope.properties.tutor.codigoPostal;
                            $scope.properties.madre.viveContigo = $scope.properties.tutor.viveContigo;
                            for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                if ($scope.properties.catVive[i].descripcion === "Si") {
                                    $scope.properties.madre.vive = $scope.properties.catVive[i];
                                }
                            }
                        }
                        $scope.properties.tutor = {
                            "catTitulo": {
                                "persistenceId_string": ""
                            },
                            "catParentezco": {
                                "persistenceId_string": ""
                            },
                            "nombre": "",
                            "apellidos": "",
                            "correoElectronico": "",
                            "catEscolaridad": {
                                "persistenceId_string": ""
                            },
                            "catEgresoAnahuac": {
                                "persistenceId_string": ""
                            },
                            "catCampusEgreso": {
                                "persistenceId_string": ""
                            },
                            "catTrabaja": {
                                "persistenceId_string": ""
                            },
                            "empresaTrabaja": "",
                            "giroEmpresa": "",
                            "puesto": "",
                            "isTutor": false,
                            "vive": {
                                "persistenceId_string": ""
                            },
                            "calle": "",
                            "catPais": {
                                "persistenceId_string": ""
                            },
                            "numeroExterior": "",
                            "numeroInterior": "",
                            "catEstado": {
                                "persistenceId_string": ""
                            },
                            "ciudad": "",
                            "colonia": "",
                            "telefono": "",
                            "codigoPostal": "",
                            "viveContigo": false,
                            "otroParentesco": ""
                        };
                        closeModal(true);
                    }
                } else if ($scope.properties.tutor.catEscolaridad === null) {
                    swal("Escolaridad!", "Debe seleccionar la escolaridad del tutor!", "warning");
                } else if (!$scope.properties.tutor.isTutor) {
                    console.log("falta tutor");
                } else if ($scope.properties.tutor.calle === "") {
                    swal("Calle!", "Debe agregar la calle del domicilio del tutor!", "warning");
                } else if ($scope.properties.tutor.catPais === null) {
                    swal("País!", "Debe agregar el país del domicilio del tutor!", "warning");
                } else if ($scope.properties.tutor.numeroExterior === "") {
                    swal("Número exterior!", "Debe agregar el número exterior del domicilio del tutor!", "warning");
                } else if ($scope.properties.tutor.catEstado === null) {
                    swal("Estado!", "Debe agregar el estado del domicilio del tutor!", "warning");
                } else if ($scope.properties.tutor.ciudad === "") {
                    swal("Ciudad!", "Debe agregar la calle del domicilio del tutor!", "warning");
                } else if ($scope.properties.tutor.colonia === "") {
                    swal("Colonia!", "Debe agregar la colonia del domicilio del tutor!", "warning");
                } else if ($scope.properties.tutor.telefono === "") {
                    swal("Teléfono!", "Debe agregar el teléfono del tutor!", "warning");
                } else if ($scope.properties.tutor.codigoPostal === "") {
                    swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                } else {
                    if ($scope.properties.tutor.catParentezco.descripcion === "Padre") {
                        $scope.properties.padre.catTitulo = $scope.properties.tutor.catTitulo;
                        $scope.properties.padre.nombre = $scope.properties.tutor.nombre;
                        $scope.properties.padre.apellidos = $scope.properties.tutor.apellidos;
                        $scope.properties.padre.correoElectronico = $scope.properties.tutor.correoElectronico;
                        $scope.properties.padre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                        $scope.properties.padre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                        $scope.properties.padre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                        $scope.properties.padre.catTrabaja = $scope.properties.tutor.catTrabaja;
                        $scope.properties.padre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                        $scope.properties.padre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                        $scope.properties.padre.puesto = $scope.properties.tutor.puesto;
                        $scope.properties.padre.isTutor = $scope.properties.tutor.isTutor;
                        $scope.properties.padre.calle = $scope.properties.tutor.calle;
                        $scope.properties.padre.catPais = $scope.properties.tutor.catPais;
                        $scope.properties.padre.numeroExterior = $scope.properties.tutor.numeroExterior;
                        $scope.properties.padre.numeroInterior = $scope.properties.tutor.numeroInterior;
                        $scope.properties.padre.catEstado = $scope.properties.tutor.catEstado;
                        $scope.properties.padre.ciudad = $scope.properties.tutor.ciudad;
                        $scope.properties.padre.colonia = $scope.properties.tutor.colonia;
                        $scope.properties.padre.telefono = $scope.properties.tutor.telefono;
                        $scope.properties.padre.codigoPostal = $scope.properties.tutor.codigoPostal;
                        $scope.properties.padre.viveContigo = $scope.properties.tutor.viveContigo;
                        for (var i = 0; i < $scope.properties.catVive.length; i++) {
                            if ($scope.properties.catVive[i].descripcion === "Si") {
                                $scope.properties.padre.vive = $scope.properties.catVive[i];
                            }
                        }
                    } else if ($scope.properties.tutor.catParentezco.descripcion === "Madre") {
                        $scope.properties.madre.catTitulo = $scope.properties.tutor.catTitulo;
                        $scope.properties.madre.nombre = $scope.properties.tutor.nombre;
                        $scope.properties.madre.apellidos = $scope.properties.tutor.apellidos;
                        $scope.properties.madre.correoElectronico = $scope.properties.tutor.correoElectronico;
                        $scope.properties.madre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                        $scope.properties.madre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                        $scope.properties.madre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                        $scope.properties.madre.catTrabaja = $scope.properties.tutor.catTrabaja;
                        $scope.properties.madre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                        $scope.properties.madre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                        $scope.properties.madre.puesto = $scope.properties.tutor.puesto;
                        $scope.properties.madre.isTutor = $scope.properties.tutor.isTutor;
                        $scope.properties.madre.calle = $scope.properties.tutor.calle;
                        $scope.properties.madre.catPais = $scope.properties.tutor.catPais;
                        $scope.properties.madre.numeroExterior = $scope.properties.tutor.numeroExterior;
                        $scope.properties.madre.numeroInterior = $scope.properties.tutor.numeroInterior;
                        $scope.properties.madre.catEstado = $scope.properties.tutor.catEstado;
                        $scope.properties.madre.ciudad = $scope.properties.tutor.ciudad;
                        $scope.properties.madre.colonia = $scope.properties.tutor.colonia;
                        $scope.properties.madre.telefono = $scope.properties.tutor.telefono;
                        $scope.properties.madre.codigoPostal = $scope.properties.tutor.codigoPostal;
                        $scope.properties.madre.viveContigo = $scope.properties.tutor.viveContigo;
                        for (var i = 0; i < $scope.properties.catVive.length; i++) {
                            if ($scope.properties.catVive[i].descripcion === "Si") {
                                $scope.properties.madre.vive = $scope.properties.catVive[i];
                            }
                        }
                    }
                    $scope.properties.tutor = {
                        "catTitulo": {
                            "persistenceId_string": ""
                        },
                        "catParentezco": {
                            "persistenceId_string": ""
                        },
                        "nombre": "",
                        "apellidos": "",
                        "correoElectronico": "",
                        "catEscolaridad": {
                            "persistenceId_string": ""
                        },
                        "catEgresoAnahuac": {
                            "persistenceId_string": ""
                        },
                        "catCampusEgreso": {
                            "persistenceId_string": ""
                        },
                        "catTrabaja": {
                            "persistenceId_string": ""
                        },
                        "empresaTrabaja": "",
                        "giroEmpresa": "",
                        "puesto": "",
                        "isTutor": false,
                        "vive": {
                            "persistenceId_string": ""
                        },
                        "calle": "",
                        "catPais": {
                            "persistenceId_string": ""
                        },
                        "numeroExterior": "",
                        "numeroInterior": "",
                        "catEstado": {
                            "persistenceId_string": ""
                        },
                        "ciudad": "",
                        "colonia": "",
                        "telefono": "",
                        "codigoPostal": "",
                        "viveContigo": false,
                        "otroParentesco": ""
                    };
                    closeModal(true);
                }
            } else {
                if (!$scope.faltaotro) {
                    if ($scope.properties.tutor.nombre === "") {
                        swal("Nombre del tutor!", "Debe agregar nombre del tutor!", "warning");
                    } else if ($scope.properties.tutor.apellidos === "") {
                        swal("Apellidos del tutor!", "Debe agregar los apellidos del tutor!", "warning");
                    } else if ($scope.properties.tutor.correoElectronico === "") {
                        swal("Correo electrónico!", "Debe agregar el correo electrónico del tutor!", "warning");
                    } else if ($scope.properties.tutor.catEgresoAnahuac === null) {
                        swal("Egreso Anahuac!", "Debe seleccionar si su tutor egresó de la universidad Anahuac!", "warning");
                    } else if ($scope.properties.tutor.catEgresoAnahuac.descripcion === "Si") {
                        if ($scope.properties.tutor.catCampusEgreso === null) {
                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su tutor!", "warning");
                        } else if ($scope.properties.tutor.catTrabaja === null) {
                            swal("Trabaja!", "Debe seleccionar si su tutor trabaja!", "warning");
                        } else if ($scope.properties.tutor.catTrabaja.descripcion === "Si") {
                            if ($scope.properties.tutor.empresaTrabaja === "") {
                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su tutor trabaja!", "warning");
                            } else if ($scope.properties.tutor.puesto === "") {
                                swal("Puesto!", "Debe agregar el puesto de trabajo del tutor!", "warning");
                            } else if ($scope.properties.tutor.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad del tutor!", "warning");
                            } else if (!$scope.properties.tutor.isTutor) {
                                console.log("falta tutor");
                            } else if ($scope.properties.tutor.calle === "") {
                                swal("Calle!", "Debe agregar la calle del domicilio del tutor!", "warning");
                            } else if ($scope.properties.tutor.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio del tutor!", "warning");
                            } else if ($scope.properties.tutor.numeroExterior === "") {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio del tutor!", "warning");
                            } else if ($scope.properties.tutor.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio del tutor!", "warning");
                            } else if ($scope.properties.tutor.ciudad === "") {
                                swal("Ciudad!", "Debe agregar la calle del domicilio del tutor!", "warning");
                            } else if ($scope.properties.tutor.colonia === "") {
                                swal("Colonia!", "Debe agregar la colonia del domicilio del tutor!", "warning");
                            } else if ($scope.properties.tutor.telefono === "") {
                                swal("Teléfono!", "Debe agregar el teléfono del tutor!", "warning");
                            } else if ($scope.properties.tutor.codigoPostal === "") {
                                swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                            } else {
                                if ($scope.properties.tutor.catParentezco.descripcion === "Padre") {
                                    $scope.properties.padre.catTitulo = $scope.properties.tutor.catTitulo;
                                    $scope.properties.padre.nombre = $scope.properties.tutor.nombre;
                                    $scope.properties.padre.apellidos = $scope.properties.tutor.apellidos;
                                    $scope.properties.padre.correoElectronico = $scope.properties.tutor.correoElectronico;
                                    $scope.properties.padre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                                    $scope.properties.padre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                                    $scope.properties.padre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                                    $scope.properties.padre.catTrabaja = $scope.properties.tutor.catTrabaja;
                                    $scope.properties.padre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                                    $scope.properties.padre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                                    $scope.properties.padre.puesto = $scope.properties.tutor.puesto;
                                    $scope.properties.padre.isTutor = $scope.properties.tutor.isTutor;
                                    $scope.properties.padre.calle = $scope.properties.tutor.calle;
                                    $scope.properties.padre.catPais = $scope.properties.tutor.catPais;
                                    $scope.properties.padre.numeroExterior = $scope.properties.tutor.numeroExterior;
                                    $scope.properties.padre.numeroInterior = $scope.properties.tutor.numeroInterior;
                                    $scope.properties.padre.catEstado = $scope.properties.tutor.catEstado;
                                    $scope.properties.padre.ciudad = $scope.properties.tutor.ciudad;
                                    $scope.properties.padre.colonia = $scope.properties.tutor.colonia;
                                    $scope.properties.padre.telefono = $scope.properties.tutor.telefono;
                                    $scope.properties.padre.codigoPostal = $scope.properties.tutor.codigoPostal;
                                    $scope.properties.padre.viveContigo = $scope.properties.tutor.viveContigo;
                                    for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                        if ($scope.properties.catVive[i].descripcion === "Si") {
                                            $scope.properties.padre.vive = $scope.properties.catVive[i];
                                        }
                                    }
                                } else if ($scope.properties.tutor.catParentezco.descripcion === "Madre") {
                                    $scope.properties.madre.catTitulo = $scope.properties.tutor.catTitulo;
                                    $scope.properties.madre.nombre = $scope.properties.tutor.nombre;
                                    $scope.properties.madre.apellidos = $scope.properties.tutor.apellidos;
                                    $scope.properties.madre.correoElectronico = $scope.properties.tutor.correoElectronico;
                                    $scope.properties.madre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                                    $scope.properties.madre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                                    $scope.properties.madre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                                    $scope.properties.madre.catTrabaja = $scope.properties.tutor.catTrabaja;
                                    $scope.properties.madre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                                    $scope.properties.madre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                                    $scope.properties.madre.puesto = $scope.properties.tutor.puesto;
                                    $scope.properties.madre.isTutor = $scope.properties.tutor.isTutor;
                                    $scope.properties.madre.calle = $scope.properties.tutor.calle;
                                    $scope.properties.madre.catPais = $scope.properties.tutor.catPais;
                                    $scope.properties.madre.numeroExterior = $scope.properties.tutor.numeroExterior;
                                    $scope.properties.madre.numeroInterior = $scope.properties.tutor.numeroInterior;
                                    $scope.properties.madre.catEstado = $scope.properties.tutor.catEstado;
                                    $scope.properties.madre.ciudad = $scope.properties.tutor.ciudad;
                                    $scope.properties.madre.colonia = $scope.properties.tutor.colonia;
                                    $scope.properties.madre.telefono = $scope.properties.tutor.telefono;
                                    $scope.properties.madre.codigoPostal = $scope.properties.tutor.codigoPostal;
                                    $scope.properties.madre.viveContigo = $scope.properties.tutor.viveContigo;
                                    for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                        if ($scope.properties.catVive[i].descripcion === "Si") {
                                            $scope.properties.madre.vive = $scope.properties.catVive[i];
                                        }
                                    }
                                }
                                $scope.properties.tutor = {
                                    "catTitulo": {
                                        "persistenceId_string": ""
                                    },
                                    "catParentezco": {
                                        "persistenceId_string": ""
                                    },
                                    "nombre": "",
                                    "apellidos": "",
                                    "correoElectronico": "",
                                    "catEscolaridad": {
                                        "persistenceId_string": ""
                                    },
                                    "catEgresoAnahuac": {
                                        "persistenceId_string": ""
                                    },
                                    "catCampusEgreso": {
                                        "persistenceId_string": ""
                                    },
                                    "catTrabaja": {
                                        "persistenceId_string": ""
                                    },
                                    "empresaTrabaja": "",
                                    "giroEmpresa": "",
                                    "puesto": "",
                                    "isTutor": false,
                                    "vive": {
                                        "persistenceId_string": ""
                                    },
                                    "calle": "",
                                    "catPais": {
                                        "persistenceId_string": ""
                                    },
                                    "numeroExterior": "",
                                    "numeroInterior": "",
                                    "catEstado": {
                                        "persistenceId_string": ""
                                    },
                                    "ciudad": "",
                                    "colonia": "",
                                    "telefono": "",
                                    "codigoPostal": "",
                                    "viveContigo": false,
                                    "otroParentesco": ""
                                };
                                closeModal(true);
                            }
                        } else if ($scope.properties.tutor.catEscolaridad === null) {
                            swal("Escolaridad!", "Debe seleccionar la escolaridad del tutor!", "warning");
                        } else if (!$scope.properties.tutor.isTutor) {
                            console.log("falta tutor");
                        } else if ($scope.properties.tutor.calle === "") {
                            swal("Calle!", "Debe agregar la calle del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.catPais === null) {
                            swal("País!", "Debe agregar el país del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.numeroExterior === "") {
                            swal("Número exterior!", "Debe agregar el número exterior del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.catEstado === null) {
                            swal("Estado!", "Debe agregar el estado del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.ciudad === "") {
                            swal("Ciudad!", "Debe agregar la calle del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.colonia === "") {
                            swal("Colonia!", "Debe agregar la colonia del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.telefono === "") {
                            swal("Teléfono!", "Debe agregar el teléfono del tutor!", "warning");
                        } else if ($scope.properties.tutor.codigoPostal === "") {
                            swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                        } else {
                            if ($scope.properties.tutor.catParentezco.descripcion === "Padre") {
                                $scope.properties.padre.catTitulo = $scope.properties.tutor.catTitulo;
                                $scope.properties.padre.nombre = $scope.properties.tutor.nombre;
                                $scope.properties.padre.apellidos = $scope.properties.tutor.apellidos;
                                $scope.properties.padre.correoElectronico = $scope.properties.tutor.correoElectronico;
                                $scope.properties.padre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                                $scope.properties.padre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                                $scope.properties.padre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                                $scope.properties.padre.catTrabaja = $scope.properties.tutor.catTrabaja;
                                $scope.properties.padre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                                $scope.properties.padre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                                $scope.properties.padre.puesto = $scope.properties.tutor.puesto;
                                $scope.properties.padre.isTutor = $scope.properties.tutor.isTutor;
                                $scope.properties.padre.calle = $scope.properties.tutor.calle;
                                $scope.properties.padre.catPais = $scope.properties.tutor.catPais;
                                $scope.properties.padre.numeroExterior = $scope.properties.tutor.numeroExterior;
                                $scope.properties.padre.numeroInterior = $scope.properties.tutor.numeroInterior;
                                $scope.properties.padre.catEstado = $scope.properties.tutor.catEstado;
                                $scope.properties.padre.ciudad = $scope.properties.tutor.ciudad;
                                $scope.properties.padre.colonia = $scope.properties.tutor.colonia;
                                $scope.properties.padre.telefono = $scope.properties.tutor.telefono;
                                $scope.properties.padre.codigoPostal = $scope.properties.tutor.codigoPostal;
                                $scope.properties.padre.viveContigo = $scope.properties.tutor.viveContigo;
                                for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                    if ($scope.properties.catVive[i].descripcion === "Si") {
                                        $scope.properties.padre.vive = $scope.properties.catVive[i];
                                    }
                                }
                            } else if ($scope.properties.tutor.catParentezco.descripcion === "Madre") {
                                $scope.properties.madre.catTitulo = $scope.properties.tutor.catTitulo;
                                $scope.properties.madre.nombre = $scope.properties.tutor.nombre;
                                $scope.properties.madre.apellidos = $scope.properties.tutor.apellidos;
                                $scope.properties.madre.correoElectronico = $scope.properties.tutor.correoElectronico;
                                $scope.properties.madre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                                $scope.properties.madre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                                $scope.properties.madre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                                $scope.properties.madre.catTrabaja = $scope.properties.tutor.catTrabaja;
                                $scope.properties.madre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                                $scope.properties.madre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                                $scope.properties.madre.puesto = $scope.properties.tutor.puesto;
                                $scope.properties.madre.isTutor = $scope.properties.tutor.isTutor;
                                $scope.properties.madre.calle = $scope.properties.tutor.calle;
                                $scope.properties.madre.catPais = $scope.properties.tutor.catPais;
                                $scope.properties.madre.numeroExterior = $scope.properties.tutor.numeroExterior;
                                $scope.properties.madre.numeroInterior = $scope.properties.tutor.numeroInterior;
                                $scope.properties.madre.catEstado = $scope.properties.tutor.catEstado;
                                $scope.properties.madre.ciudad = $scope.properties.tutor.ciudad;
                                $scope.properties.madre.colonia = $scope.properties.tutor.colonia;
                                $scope.properties.madre.telefono = $scope.properties.tutor.telefono;
                                $scope.properties.madre.codigoPostal = $scope.properties.tutor.codigoPostal;
                                $scope.properties.madre.viveContigo = $scope.properties.tutor.viveContigo;
                                for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                    if ($scope.properties.catVive[i].descripcion === "Si") {
                                        $scope.properties.madre.vive = $scope.properties.catVive[i];
                                    }
                                }
                            }
                            $scope.properties.tutor = {
                                "catTitulo": {
                                    "persistenceId_string": ""
                                },
                                "catParentezco": {
                                    "persistenceId_string": ""
                                },
                                "nombre": "",
                                "apellidos": "",
                                "correoElectronico": "",
                                "catEscolaridad": {
                                    "persistenceId_string": ""
                                },
                                "catEgresoAnahuac": {
                                    "persistenceId_string": ""
                                },
                                "catCampusEgreso": {
                                    "persistenceId_string": ""
                                },
                                "catTrabaja": {
                                    "persistenceId_string": ""
                                },
                                "empresaTrabaja": "",
                                "giroEmpresa": "",
                                "puesto": "",
                                "isTutor": false,
                                "vive": {
                                    "persistenceId_string": ""
                                },
                                "calle": "",
                                "catPais": {
                                    "persistenceId_string": ""
                                },
                                "numeroExterior": "",
                                "numeroInterior": "",
                                "catEstado": {
                                    "persistenceId_string": ""
                                },
                                "ciudad": "",
                                "colonia": "",
                                "telefono": "",
                                "codigoPostal": "",
                                "viveContigo": false,
                                "otroParentesco": ""
                            };
                            closeModal(true);
                        }
                    } else if ($scope.properties.tutor.catTrabaja === null) {
                        swal("Trabaja!", "Debe seleccionar si su tutor trabaja!", "warning");
                    } else if ($scope.properties.tutor.catTrabaja.descripcion === "Si") {
                        if ($scope.properties.tutor.empresaTrabaja === "") {
                            swal("Empresa!", "Debe agregar el nombre de la empresa donde su tutor trabaja!", "warning");
                        } else if ($scope.properties.tutor.puesto === "") {
                            swal("Puesto!", "Debe agregar el puesto de trabajo del tutor!", "warning");
                        } else if ($scope.properties.tutor.catEscolaridad === null) {
                            swal("Escolaridad!", "Debe seleccionar la escolaridad del tutor!", "warning");
                        } else if (!$scope.properties.tutor.isTutor) {
                            console.log("falta tutor");
                        } else if ($scope.properties.tutor.calle === "") {
                            swal("Calle!", "Debe agregar la calle del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.catPais === null) {
                            swal("País!", "Debe agregar el país del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.numeroExterior === "") {
                            swal("Número exterior!", "Debe agregar el número exterior del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.catEstado === null) {
                            swal("Estado!", "Debe agregar el estado del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.ciudad === "") {
                            swal("Ciudad!", "Debe agregar la calle del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.colonia === "") {
                            swal("Colonia!", "Debe agregar la colonia del domicilio del tutor!", "warning");
                        } else if ($scope.properties.tutor.telefono === "") {
                            swal("Teléfono!", "Debe agregar el teléfono del tutor!", "warning");
                        } else if ($scope.properties.tutor.codigoPostal === "") {
                            swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                        } else {
                            if ($scope.properties.tutor.catParentezco.descripcion === "Padre") {
                                $scope.properties.padre.catTitulo = $scope.properties.tutor.catTitulo;
                                $scope.properties.padre.nombre = $scope.properties.tutor.nombre;
                                $scope.properties.padre.apellidos = $scope.properties.tutor.apellidos;
                                $scope.properties.padre.correoElectronico = $scope.properties.tutor.correoElectronico;
                                $scope.properties.padre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                                $scope.properties.padre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                                $scope.properties.padre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                                $scope.properties.padre.catTrabaja = $scope.properties.tutor.catTrabaja;
                                $scope.properties.padre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                                $scope.properties.padre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                                $scope.properties.padre.puesto = $scope.properties.tutor.puesto;
                                $scope.properties.padre.isTutor = $scope.properties.tutor.isTutor;
                                $scope.properties.padre.calle = $scope.properties.tutor.calle;
                                $scope.properties.padre.catPais = $scope.properties.tutor.catPais;
                                $scope.properties.padre.numeroExterior = $scope.properties.tutor.numeroExterior;
                                $scope.properties.padre.numeroInterior = $scope.properties.tutor.numeroInterior;
                                $scope.properties.padre.catEstado = $scope.properties.tutor.catEstado;
                                $scope.properties.padre.ciudad = $scope.properties.tutor.ciudad;
                                $scope.properties.padre.colonia = $scope.properties.tutor.colonia;
                                $scope.properties.padre.telefono = $scope.properties.tutor.telefono;
                                $scope.properties.padre.codigoPostal = $scope.properties.tutor.codigoPostal;
                                $scope.properties.padre.viveContigo = $scope.properties.tutor.viveContigo;
                                for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                    if ($scope.properties.catVive[i].descripcion === "Si") {
                                        $scope.properties.padre.vive = $scope.properties.catVive[i];
                                    }
                                }
                            } else if ($scope.properties.tutor.catParentezco.descripcion === "Madre") {
                                $scope.properties.madre.catTitulo = $scope.properties.tutor.catTitulo;
                                $scope.properties.madre.nombre = $scope.properties.tutor.nombre;
                                $scope.properties.madre.apellidos = $scope.properties.tutor.apellidos;
                                $scope.properties.madre.correoElectronico = $scope.properties.tutor.correoElectronico;
                                $scope.properties.madre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                                $scope.properties.madre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                                $scope.properties.madre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                                $scope.properties.madre.catTrabaja = $scope.properties.tutor.catTrabaja;
                                $scope.properties.madre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                                $scope.properties.madre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                                $scope.properties.madre.puesto = $scope.properties.tutor.puesto;
                                $scope.properties.madre.isTutor = $scope.properties.tutor.isTutor;
                                $scope.properties.madre.calle = $scope.properties.tutor.calle;
                                $scope.properties.madre.catPais = $scope.properties.tutor.catPais;
                                $scope.properties.madre.numeroExterior = $scope.properties.tutor.numeroExterior;
                                $scope.properties.madre.numeroInterior = $scope.properties.tutor.numeroInterior;
                                $scope.properties.madre.catEstado = $scope.properties.tutor.catEstado;
                                $scope.properties.madre.ciudad = $scope.properties.tutor.ciudad;
                                $scope.properties.madre.colonia = $scope.properties.tutor.colonia;
                                $scope.properties.madre.telefono = $scope.properties.tutor.telefono;
                                $scope.properties.madre.codigoPostal = $scope.properties.tutor.codigoPostal;
                                $scope.properties.madre.viveContigo = $scope.properties.tutor.viveContigo;
                                for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                    if ($scope.properties.catVive[i].descripcion === "Si") {
                                        $scope.properties.madre.vive = $scope.properties.catVive[i];
                                    }
                                }
                            }
                            $scope.properties.tutor = {
                                "catTitulo": {
                                    "persistenceId_string": ""
                                },
                                "catParentezco": {
                                    "persistenceId_string": ""
                                },
                                "nombre": "",
                                "apellidos": "",
                                "correoElectronico": "",
                                "catEscolaridad": {
                                    "persistenceId_string": ""
                                },
                                "catEgresoAnahuac": {
                                    "persistenceId_string": ""
                                },
                                "catCampusEgreso": {
                                    "persistenceId_string": ""
                                },
                                "catTrabaja": {
                                    "persistenceId_string": ""
                                },
                                "empresaTrabaja": "",
                                "giroEmpresa": "",
                                "puesto": "",
                                "isTutor": false,
                                "vive": {
                                    "persistenceId_string": ""
                                },
                                "calle": "",
                                "catPais": {
                                    "persistenceId_string": ""
                                },
                                "numeroExterior": "",
                                "numeroInterior": "",
                                "catEstado": {
                                    "persistenceId_string": ""
                                },
                                "ciudad": "",
                                "colonia": "",
                                "telefono": "",
                                "codigoPostal": "",
                                "viveContigo": false,
                                "otroParentesco": ""
                            };
                            closeModal(true);
                        }
                    } else if ($scope.properties.tutor.catEscolaridad === null) {
                        swal("Escolaridad!", "Debe seleccionar la escolaridad del tutor!", "warning");
                    } else if (!$scope.properties.tutor.isTutor) {
                        console.log("falta tutor");
                    } else if ($scope.properties.tutor.calle === "") {
                        swal("Calle!", "Debe agregar la calle del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.catPais === null) {
                        swal("País!", "Debe agregar el país del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.numeroExterior === "") {
                        swal("Número exterior!", "Debe agregar el número exterior del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.catEstado === null) {
                        swal("Estado!", "Debe agregar el estado del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.ciudad === "") {
                        swal("Ciudad!", "Debe agregar la calle del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.colonia === "") {
                        swal("Colonia!", "Debe agregar la colonia del domicilio del tutor!", "warning");
                    } else if ($scope.properties.tutor.telefono === "") {
                        swal("Teléfono!", "Debe agregar el teléfono del tutor!", "warning");
                    } else if ($scope.properties.tutor.codigoPostal === "") {
                        swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                    } else {
                        if ($scope.properties.tutor.catParentezco.descripcion === "Padre") {
                            $scope.properties.padre.catTitulo = $scope.properties.tutor.catTitulo;
                            $scope.properties.padre.nombre = $scope.properties.tutor.nombre;
                            $scope.properties.padre.apellidos = $scope.properties.tutor.apellidos;
                            $scope.properties.padre.correoElectronico = $scope.properties.tutor.correoElectronico;
                            $scope.properties.padre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                            $scope.properties.padre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                            $scope.properties.padre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                            $scope.properties.padre.catTrabaja = $scope.properties.tutor.catTrabaja;
                            $scope.properties.padre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                            $scope.properties.padre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                            $scope.properties.padre.puesto = $scope.properties.tutor.puesto;
                            $scope.properties.padre.isTutor = $scope.properties.tutor.isTutor;
                            $scope.properties.padre.calle = $scope.properties.tutor.calle;
                            $scope.properties.padre.catPais = $scope.properties.tutor.catPais;
                            $scope.properties.padre.numeroExterior = $scope.properties.tutor.numeroExterior;
                            $scope.properties.padre.numeroInterior = $scope.properties.tutor.numeroInterior;
                            $scope.properties.padre.catEstado = $scope.properties.tutor.catEstado;
                            $scope.properties.padre.ciudad = $scope.properties.tutor.ciudad;
                            $scope.properties.padre.colonia = $scope.properties.tutor.colonia;
                            $scope.properties.padre.telefono = $scope.properties.tutor.telefono;
                            $scope.properties.padre.codigoPostal = $scope.properties.tutor.codigoPostal;
                            $scope.properties.padre.viveContigo = $scope.properties.tutor.viveContigo;
                            for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                if ($scope.properties.catVive[i].descripcion === "Si") {
                                    $scope.properties.padre.vive = $scope.properties.catVive[i];
                                }
                            }
                        } else if ($scope.properties.tutor.catParentezco.descripcion === "Madre") {
                            $scope.properties.madre.catTitulo = $scope.properties.tutor.catTitulo;
                            $scope.properties.madre.nombre = $scope.properties.tutor.nombre;
                            $scope.properties.madre.apellidos = $scope.properties.tutor.apellidos;
                            $scope.properties.madre.correoElectronico = $scope.properties.tutor.correoElectronico;
                            $scope.properties.madre.catEscolaridad = $scope.properties.tutor.catEscolaridad;
                            $scope.properties.madre.catEgresoAnahuac = $scope.properties.tutor.catEgresoAnahuac;
                            $scope.properties.madre.catCampusEgreso = $scope.properties.tutor.catCampusEgreso;
                            $scope.properties.madre.catTrabaja = $scope.properties.tutor.catTrabaja;
                            $scope.properties.madre.empresaTrabaja = $scope.properties.tutor.empresaTrabaja;
                            $scope.properties.madre.giroEmpresa = $scope.properties.tutor.giroEmpresa;
                            $scope.properties.madre.puesto = $scope.properties.tutor.puesto;
                            $scope.properties.madre.isTutor = $scope.properties.tutor.isTutor;
                            $scope.properties.madre.calle = $scope.properties.tutor.calle;
                            $scope.properties.madre.catPais = $scope.properties.tutor.catPais;
                            $scope.properties.madre.numeroExterior = $scope.properties.tutor.numeroExterior;
                            $scope.properties.madre.numeroInterior = $scope.properties.tutor.numeroInterior;
                            $scope.properties.madre.catEstado = $scope.properties.tutor.catEstado;
                            $scope.properties.madre.ciudad = $scope.properties.tutor.ciudad;
                            $scope.properties.madre.colonia = $scope.properties.tutor.colonia;
                            $scope.properties.madre.telefono = $scope.properties.tutor.telefono;
                            $scope.properties.madre.codigoPostal = $scope.properties.tutor.codigoPostal;
                            $scope.properties.madre.viveContigo = $scope.properties.tutor.viveContigo;
                            for (var i = 0; i < $scope.properties.catVive.length; i++) {
                                if ($scope.properties.catVive[i].descripcion === "Si") {
                                    $scope.properties.madre.vive = $scope.properties.catVive[i];
                                }
                            }
                        }
                        $scope.properties.tutor = {
                            "catTitulo": {
                                "persistenceId_string": ""
                            },
                            "catParentezco": {
                                "persistenceId_string": ""
                            },
                            "nombre": "",
                            "apellidos": "",
                            "correoElectronico": "",
                            "catEscolaridad": {
                                "persistenceId_string": ""
                            },
                            "catEgresoAnahuac": {
                                "persistenceId_string": ""
                            },
                            "catCampusEgreso": {
                                "persistenceId_string": ""
                            },
                            "catTrabaja": {
                                "persistenceId_string": ""
                            },
                            "empresaTrabaja": "",
                            "giroEmpresa": "",
                            "puesto": "",
                            "isTutor": false,
                            "vive": {
                                "persistenceId_string": ""
                            },
                            "calle": "",
                            "catPais": {
                                "persistenceId_string": ""
                            },
                            "numeroExterior": "",
                            "numeroInterior": "",
                            "catEstado": {
                                "persistenceId_string": ""
                            },
                            "ciudad": "",
                            "colonia": "",
                            "telefono": "",
                            "codigoPostal": "",
                            "viveContigo": false,
                            "otroParentesco": ""
                        };
                        closeModal(true);
                    }
                }
            }
        } else if ($scope.properties.url) {
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
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.dataFromSuccess = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
                notifyParentFrame({
                    message: 'success',
                    status: status,
                    dataFromSuccess: data,
                    dataFromError: undefined,
                    responseStatusCode: status
                });
                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                closeModal($scope.properties.closeOnSuccess);
            })
            .error(function(data, status) {
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({
                    message: 'error',
                    status: status,
                    dataFromError: data,
                    dataFromSuccess: undefined,
                    responseStatusCode: status
                });
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
            var dataToSend = angular.extend({}, $scope.properties, additionalProperties);
            $window.parent.postMessage(JSON.stringify(dataToSend), '*');
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

}