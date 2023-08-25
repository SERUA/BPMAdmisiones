function PbButtonCtrl($scope, $http, $filter) {
    
    $scope.action = function(){
        if(validate()){
            $scope.cambioPasswordBonita();
        }
    };
    
    function validate(){
        const regexEmail = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
        let isValid = true;
        let errorMessage = "";
        if($scope.properties.objUser.email === ""){
            isValid = false;
            errorMessage = $filter('translate')('Por favor llena el campo Correo electrónico.');
        } else if (!regexEmail.test(String($scope.properties.objUser.email))){
            isValid = false;
            errorMessage = $filter('translate')('El formato del correo electrónico no es el correcto.');
        }
        
        if(!isValid){
            Swal.fire($filter('translate')('¡Atención!'), errorMessage, "warning");
        }
        
        return isValid;
    }
    
    $scope.cambioPasswordBonita = function() {
        let data = {
            "nombreusuario": $scope.properties.objUser.email,
            "password": "password"
        };
        
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=recuparaPassword&p=0&c=10",
            data: data
        };

        return $http(req).success(function(data, status) {
            if(data.success){
                $scope.properties.navigationVar = "login";
                let message = $filter('translate')('Revisa tu bandeja de entrada para continuar con el proceso de recuperación de contraseña');
                Swal.fire($filter('translate')('Ya casi está listo.'), message, "success");
                
            } else {
                Swal.fire("Error.", data.error, "error");
            }
        })
        .error(function(data, status) {
            let errorMessage = $filter('translate')('Ocurrió un error inesperado. Inténtalo de nuevo mas tarde.');
            if(data.error.includes("SUserNotFoundException")){
                errorMessage = $filter('translate')('El Correo electrónico ingresado no está registrado.');
            }
            Swal.fire("Error.", errorMessage, "error");
           // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {
            
        });
    }
}
