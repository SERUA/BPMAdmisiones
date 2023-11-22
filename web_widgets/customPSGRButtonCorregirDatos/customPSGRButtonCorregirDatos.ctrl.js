function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    
    $scope.checkCorreccion = $scope.properties.checkCorreccion;

    $scope.$watch('checkCorreccion', function(nuevoValor, antiguoValor) {
        if (nuevoValor) {
            Swal
            .fire({
                title: "Advertencia",
                text: "Estas a punto de cambiar la información de la solicitud del aspirante. ¿Deseas continuar?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: "Continuar",
                cancelButtonText: "Cancelar",
            })
            .then(resultado => {
                if (resultado.isDismissed) {
                    $scope.properties.checkCorreccion = false;
                }
            });
        }
    });

    $scope.guardarCorrecciones = function() {
        if (validarDatos()) {
            doRequest();    
        }
    }
    
    // Validar datos Información aspirante
    function validarDatos() {
        
        let data = $scope.properties.dataToCorregir;
        let errorMessage = "";
        
        if (!data.datosPersonales.nombre) {
            errorMessage = "El campo Nombre no puede ir vacío."
        } 
        else if (!data.datosPersonales.apellido_paterno) {
            errorMessage = "El campo Apellido paterno no puede ir vacío."
        } 
        else if (!data.datosPersonales.apellido_materno) {
            errorMessage = "El campo Apellido materno no puede ir vacío."
        } 
        else if (!data.datosPersonales.curp) {
            errorMessage = "El campo CURP no puede ir vacío."
        } 
        else if (!data.datosContacto.correo_contacto) {
            errorMessage = "El campo Correo de contacto no puede ir vacío."
        }
        else if (!data.datosPrograma.campus) {
            errorMessage = "El campo Campus no puede ir vacío."
        }
        else if (!data.datosPrograma.posgrado) {
            errorMessage = "El campo Posgrado no puede ir vacío."
        }
        else if (!data.datosPrograma.programa_interes) {
            errorMessage = "El campo Pograma de interés no puede ir vacío."
        }
        else if (!data.datosPrograma.periodo_ingreso) {
            errorMessage = "El campo Periodo de ingreso no puede ir vacío."
        }
        
        if (errorMessage) {
            Swal.fire("Validación", errorMessage, "error");
            return false;
        }
        
        return true;
    }
    
    function doRequest() {
        var req = {
            method: "POST",
            url: $scope.properties.url,
            data: angular.copy($scope.properties.dataToCorregir)
        };
        $http(req)
            .success(function(data, status) {
                Swal.fire("¡Corrección realizada correctamente!", "", "success")
                $scope.properties.checkCorreccion = false;
            })
            .error(function(data, status) {
                Swal.fire("Error", "Falló la solicitud de correción de datos.", "error");
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            });
    }

    $scope.myFunc = function() {



        let nombrePrepa = $scope.properties.NombrePrepaNueva;
        let clave = $scope.properties.clavePrepa;
        let nuevaPrepa = "";
        let encontro = false;
        let Novalido = false;

        if (nombrePrepa === "Otro") {
            Swal.fire("¡Aviso!", "No debes modificar la preparatoria a 'Otro'", "warning");
            Novalido = true
        } else if (clave === "" || clave === " " || clave === undefined || clave === null) {
            Swal.fire("¡Aviso!", "No debes dejar el campo 'Clave preparatoria' vacío", "warning");
            Novalido = true
        } else if (clave !== $scope.properties.DatosPreparatoria.clave) {
            Swal.fire("¡Aviso!", "la clave proporcionada no existe", "warning");
            Novalido = true
        } else {
            for (var i = 0; i < $scope.properties.catBachillerato2.length; i++) {
                if ($scope.properties.catBachillerato2[i].descripcion === nombrePrepa) {
                    nuevaPrepa = $scope.properties.catBachillerato2[i].persistenceid;
                    encontro = true
                    break;
                }
            }
        }
        if (encontro) {

            var prepa = Number(nuevaPrepa);
            $scope.properties.prepaToSend.persistenceid = $scope.properties.idSolicitud;
            $scope.properties.prepaToSend.catbachilleratos_pid = prepa;

            // $scope.properties.prepaToSend.persistenceid = $scope.properties.idSolicitud.toString();
            // $scope.properties.prepaToSend.catbachilleratos_pid = nuevaPrepa.toString();
            //alert("solicitud no:"+$scope.properties.prepaToSend.persistenceid+" Prepa numero:"+$scope.properties.prepaToSend.catbachilleratos_pid)


            var req = {
                method: "POST",
                url: "/bonita/API/extension/AnahuacRest?url=updatePrepaSolicitudDeAdmision&p=0&c=10",
                data: angular.copy($scope.properties.prepaToSend)
            };
            return $http(req)
                .success(function(data, status) {
                    Swal.fire("¡Preparatoria modificada correctamente!", "", "success")
                    $scope.properties.isPrepaModificada = true;
                    $scope.properties.CheckCambiarPrepa = false;
                })
                .error(function(data, status) {
                    notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                });


        } else {
            if (!Novalido) {
                Swal.fire("¡Preparatoria no encontrada!", "Debe ingresar una preparatoria existente en el catálogo.", "warning")

            }
            //Swal.fire("¡Preparatoria no encontrada!","Debe ingresar una preparatoria existente en el catálogo.","warning")

        }




    };
}