function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {

    'use strict';

    var vm = this;

    this.action = function action() {
        
        debugger;
        
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
            if ($scope.properties.dataToSend.primernombre === "") {
                swal("¡Aviso!", "Debe agregar el primer nombre", "warning");
            } else if ($scope.properties.dataToSend.apellidopaterno === "") {
                swal("¡Aviso!", "Debe agregar el apellido paterno", "warning");
            }
            /*else if($scope.properties.dataToSend.apellidomaterno === ""){
                      swal("¡Aviso!", "Debe agregar el apellido materno", "warning");
                  }*/
            else if ($scope.properties.dataToSend.correoelectronico === "") {
                swal("¡Aviso!", "Debe agregar el correo electrónico", "warning");
            } else if (!re.test(String($scope.properties.dataToSend.correoelectronico.trim()).toLowerCase())) {
                swal("¡Aviso!", "El formato del correo electrónico no es válido", "warning");
            } else if ($scope.properties.dataToSend.catCampusEstudio === null) {
                swal("¡Aviso!", "Debe seleccionar el campus de estudio", "warning");
            } else if ($scope.properties.dataToSend.catGestionEscolar === null) {
                swal("¡Aviso!", "Debe seleccionar una licenciatura", "warning");
                /*}else if($scope.properties.dataToSend.catPeriodo === null){
                    swal("¡Aviso!", "Debe seleccionar un periodo", "warning");*/
            } else if ($scope.properties.dataToSend.catGestionEscolar.propedeutico === null && $scope.properties.dataToSend.catPropedeutico === null) {
                swal("¡Aviso!", "Debe seleccionar un curso propedéutico", "warning");
            } else if ($scope.properties.dataToSend.catCampus === null) {
                swal("¡Aviso!", "Debe seleccionar el campus de examen", "warning");
            } else if ($scope.properties.dataToSend.catSexo === null) {
                swal("¡Aviso!", "Debe seleccionar el sexo", "warning");
            } else if ($scope.properties.dataToSend.fechanacimiento === "" || $scope.properties.dataToSend.fechanacimiento === undefined || $scope.properties.dataToSend.fechanacimiento === null) {
                swal("¡Aviso!", "Debe agregar la fecha de nacimiento", "warning");
            } else if ($scope.properties.dataToSend.catEstado === null && $scope.properties.dataToSend.estadoextranjero === "") {
                swal("¡Aviso!", "Debe seleccionar el estado", "warning");
                /*}else if($scope.properties.dataToSend.estadoextranjero === ""){
                    swal("¡Aviso!", "Debe agregar el estado", "warning");*/
            } else if ($scope.properties.dataToSend.catBachilleratos === null || $scope.properties.dataToSend.catBachilleratos === undefined) {
                swal("¡Aviso!", "Debe seleccionar la preparatoria", "warning");
            } else if ($scope.properties.dataToSend.catBachilleratos.descripcion === "Otro" && $scope.properties.datosPreparatoria.nombreBachillerato === "") {
                swal("¡Aviso!", "Debe agregar el nombre de la preparatoria", "warning");
            } else if ($scope.properties.datosPreparatoria.paisBachillerato === "") {
                swal("¡Aviso!", "Debe agregar el país de la preparatoria", "warning");
            } else if ($scope.properties.datosPreparatoria.estadoBachillerato === "") {
                swal("¡Aviso!", "Debe agregar el estado de la preparatoria", "warning");
            } else if ($scope.properties.datosPreparatoria.ciudadBachillerato === "") {
                swal("¡Aviso!", "Debe agregar la ciudad de la preparatoria", "warning");
            } else if ($scope.properties.dataToSend.promediogeneral === "" || $scope.properties.dataToSend.promediogeneral === undefined || $scope.properties.dataToSend.promediogeneral === null || isNaN($scope.properties.dataToSend.promediogeneral)) {
                swal("¡Aviso!", "Debe agregar el promedio", "warning");
            } else if ($scope.properties.dataToSend.resultadopaa === "" || $scope.properties.dataToSend.resultadopaa === undefined || $scope.properties.dataToSend.resultadopaa === null || isNaN($scope.properties.dataToSend.resultadopaa)) {
                swal("¡Aviso!", "Debe agregar el puntaje PAA", "warning");
            } else {
                $scope.properties.JSONUsuarioRegistrado.caseid = $scope.properties.dataToSend.caseid;
                $scope.properties.JSONUsuarioRegistrado.primernombre = $scope.properties.dataToSend.primernombre;
                $scope.properties.JSONUsuarioRegistrado.segundonombre = $scope.properties.dataToSend.segundonombre;
                $scope.properties.JSONUsuarioRegistrado.apellidopaterno = $scope.properties.dataToSend.apellidopaterno;
                $scope.properties.JSONUsuarioRegistrado.apellidomaterno = $scope.properties.dataToSend.apellidomaterno;
                $scope.properties.JSONUsuarioRegistrado.correoelectronico = $scope.properties.dataToSend.correoelectronico;
                $scope.properties.JSONUsuarioRegistrado.campusestudio = $scope.properties.dataToSend.catCampusEstudio.persistenceId;
                $scope.properties.JSONUsuarioRegistrado.licenciatura = $scope.properties.dataToSend.catGestionEscolar.persistenceId;
                $scope.properties.JSONUsuarioRegistrado.periodo = $scope.properties.dataToSend.catPeriodo.persistenceId;
                if (!$scope.properties.dataToSend.catGestionEscolar.propedeutico) {
                    $scope.properties.JSONUsuarioRegistrado.propedeutico = null;
                } else {
                    $scope.properties.JSONUsuarioRegistrado.propedeutico = $scope.properties.dataToSend.catPropedeutico.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.catPropedeutico = $scope.properties.dataToSend.catPropedeutico;
                }

                $scope.properties.JSONUsuarioRegistrado.campus = $scope.properties.dataToSend.catCampus.persistenceId;
                $scope.properties.JSONUsuarioRegistrado.sexo = $scope.properties.dataToSend.catSexo.persistenceId;
                //$scope.properties.JSONUsuarioRegistrado.fechanacimiento = angular.copy($scope.properties.dataToSend.fechanacimiento.toString().slice(0,-1));
                let d = new Date($scope.properties.dataToSend.fechanacimiento.toString())
                let formatted_date = d.getFullYear() + "-" + ((d.getMonth() + 1) < 10 ? "0" + (d.getMonth() + 1) : (d.getMonth() + 1)) + "-" + (d.getDate() < 10 ? "0" + d.getDate() : d.getDate())
                formatted_date += "t05:00:00.000";
                $scope.properties.JSONUsuarioRegistrado.fechanacimiento = formatted_date;
                if ($scope.properties.dataToSend.catEstado === null || $scope.properties.dataToSend.catEstado === undefined) {
                    $scope.properties.JSONUsuarioRegistrado.estado = null;
                    $scope.properties.JSONUsuarioRegistrado.estadoextranjero = $scope.properties.dataToSend.estadoextranjero;
                } else {
                    $scope.properties.JSONUsuarioRegistrado.estado = $scope.properties.dataToSend.catEstado.persistenceId;
                    $scope.properties.JSONUsuarioRegistrado.estadoextranjero = "";
                    if ($scope.properties.dataToSend.catEstado.descripcion !== $scope.properties.jsonOriginal.estado) {
                        existecambio = true;
                    }
                }

                $scope.properties.JSONUsuarioRegistrado.bachillerato = parseInt($scope.properties.dataToSend.catBachilleratos.persistenceid);
                $scope.properties.JSONUsuarioRegistrado.nombrebachillerato = $scope.properties.datosPreparatoria.nombreBachillerato;
                $scope.properties.JSONUsuarioRegistrado.paisbachillerato = $scope.properties.datosPreparatoria.paisBachillerato;
                $scope.properties.JSONUsuarioRegistrado.estadobachillerato = $scope.properties.datosPreparatoria.estadoBachillerato;
                $scope.properties.JSONUsuarioRegistrado.ciudadbachillerato = $scope.properties.datosPreparatoria.ciudadBachillerato;
                $scope.properties.JSONUsuarioRegistrado.promedio = $scope.properties.dataToSend.promediogeneral + "";
                $scope.properties.JSONUsuarioRegistrado.catCampusEstudio = $scope.properties.dataToSend.catCampusEstudio;
                $scope.properties.JSONUsuarioRegistrado.catGestionEscolar = $scope.properties.dataToSend.catGestionEscolar;
                $scope.properties.JSONUsuarioRegistrado.catPeriodo = $scope.properties.dataToSend.catPeriodo;
                $scope.properties.JSONUsuarioRegistrado.catCampus = $scope.properties.dataToSend.catCampus;
                $scope.properties.JSONUsuarioRegistrado.catSexo = $scope.properties.dataToSend.catSexo;
                $scope.properties.JSONUsuarioRegistrado.catEstado = $scope.properties.dataToSend.catEstado;
                $scope.properties.JSONUsuarioRegistrado.catBachilleratos = $scope.properties.dataToSend.catBachilleratos;

                $scope.properties.JSONUsuarioRegistrado.catEstadoExamen = $scope.properties.dataToSend.catEstadoExamen;
                $scope.properties.JSONUsuarioRegistrado.catPaisExamen = $scope.properties.dataToSend.catPaisExamen;
                $scope.properties.JSONUsuarioRegistrado.catCiudadExamen = $scope.properties.dataToSend.catCiudadExamen;
                $scope.properties.JSONUsuarioRegistrado.catResidencia = $scope.properties.dataToSend.catResidencia;
                $scope.properties.JSONUsuarioRegistrado.catTipoAlumno = $scope.properties.dataToSend.catTipoAlumno;
                $scope.properties.JSONUsuarioRegistrado.catTipoAdmision = $scope.properties.dataToSend.catTipoAdmision;
                $scope.properties.JSONUsuarioRegistrado.catLugarExamen = $scope.properties.dataToSend.catLugarExamen;

                $scope.properties.JSONUsuarioRegistrado.resultadoPAA = $scope.properties.dataToSend.resultadopaa;
                $scope.properties.JSONUsuarioRegistrado.tienePAA = ($scope.properties.dataToSend.tienepaa == "t" ? true : false);
                $scope.properties.JSONUsuarioRegistrado.cbCoincide = $scope.properties.dataToSend.cbcoincide2;
                $scope.properties.JSONUsuarioRegistrado.descuento = $scope.properties.dataToSend.descuento;
                $scope.properties.JSONUsuarioRegistrado.Documentos = angular.copy($scope.properties.archivos);

                $scope.properties.JSONUsuarioRegistrado.observacionListaRoja = $scope.properties.dataToSend.observacioneslistaroja;

                if ($scope.properties.dataToSend.tienepaa == "f" && $scope.properties.dataToSend.resultadopaa >= 1) {
                    $scope.properties.JSONUsuarioRegistrado.tienePAA = true;
                    $scope.properties.JSONUsuarioRegistrado.resultadoPAA = $scope.properties.dataToSend.resultadopaa;
                }

                if ($scope.properties.JSONUsuarioRegistrado.catLugarExamen.descripcion.toUpperCase() === "EN UN ESTADO") {
                    $scope.properties.JSONUsuarioRegistrado.catPaisExamen = null;
                } else if ($scope.properties.JSONUsuarioRegistrado.catLugarExamen.descripcion.toUpperCase() === "EN EL EXTRANJERO (SOLO SI VIVES FUERA DE MÉXICO)") {
                    $scope.properties.JSONUsuarioRegistrado.catEstadoExamen = null;
                    for (var pais = 0; pais < $scope.properties.catPais.length; pais++) {
                        if ($scope.properties.dataToSend.catPaisExamen === $scope.properties.catPais[pais].descripcion) {
                            $scope.properties.JSONUsuarioRegistrado.catPaisExamen = $scope.properties.catPais[pais];
                        }
                    }
                } else {
                    $scope.properties.JSONUsuarioRegistrado.catEstadoExamen = null;
                    $scope.properties.JSONUsuarioRegistrado.catPaisExamen = null;
                    $scope.properties.JSONUsuarioRegistrado.catCiudadExamen = null;
                }
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
                } else {
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
                }

            }



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
                $scope.properties.dataToSend.caseid = 0;
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
            var dataToSend = angular.extend({}, $scope.properties, additionalProperties);
            $window.parent.postMessage(JSON.stringify(dataToSend), '*');
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