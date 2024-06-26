function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    
    $scope.checkCorreccion = $scope.properties.checkCorreccion;

    $scope.$watch('checkCorreccion', function(nuevoValor, antiguoValor) {
        if (nuevoValor) {
            Swal
            .fire({
                title: "Advertencia",
                text: "Estas a punto de cambiar la información de la solicitud del aspirante. Toma la precaución debida al realizar cambios.",
                icon: 'warning',
                confirmButtonText: "Ok",
            });
            /* SWAL con CancelButton
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
            });*/
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
            url: $scope.properties.urlServicio,
            data: angular.copy($scope.properties.dataToCorregir)
        };
        $http(req)
            .success(function(data, status) {
                Swal.fire("¡Corrección realizada correctamente!", "", "success");
                $scope.properties.statusResult = "success";
            })
            .error(function(data, status) {
                Swal.fire("Error", "Falló la solicitud de correción de datos.", "error");
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                $scope.properties.statusResult = "error";
            });
    }
}