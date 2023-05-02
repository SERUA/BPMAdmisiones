function PbButtonCtrl($scope, $http) {
    
    $scope.action = function(){
        if(validate()){
            $scope.cambioPasswordBonita();
        }
    };
    
    function validate(){
        const regexEmail = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
        let isValid = true;
        let errorMessage = "";
        let idioma = getCookieValue("BOS_Locale");

        if($scope.properties.objUser.email === ""){
            isValid = false;
            errorMessage = idioma === "es" ? "Por favor llena el campo Correo electrónico ." : "Please fill the Email field."
        } else if (!regexEmail.test(String($scope.properties.objUser.email))){
            isValid = false;
            errorMessage =  idioma === "es" ? "El formato del correo electrónico no es el correcto." : "Email format is not correct";
        }
        
        if(!isValid){
            Swal.fire(idioma === "es" ? "¡Atención!" : "Warning!", errorMessage, "warning");
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
            let idioma = getCookieValue("BOS_Locale");
            if(data.success){
                $scope.properties.navigationVar = "login";
                let message = "";

                if(idioma === "es"){
                    message = "Revisa tu bandeja de entrada para continuar con el proceso de recuperación de contraseña.";
                } else {
                    message = "Check your email box in order to continue with your password recover process.";
                }
                Swal.fire(idiom === "es" ? "Ya casi está listo." : "Almost ready", message, "success");
                
            } else {
                debugger;
                Swal.fire("Error.", data.error, "error");
            }
        })
        .error(function(data, status) {
            let idioma = getCookieValue("BOS_Locale");
            let errorMessage = "Ocurrió un error inesperado. Inténtalo de nuevo mas tarde.";
            if(idioma === "es"){
                errorMessage = "Ocurrió un error inesperado. Inténtalo de nuevo mas tarde.";
            } else {
                errorMessage = "Unexpected error. Please try again later";
            }
            if(data.error.includes("SUserNotFoundException")){
                if(idioma === "es"){
                    errorMessage = "El Correo electrónico ingresado no está registrado."
                } else {
                    errorMessage = "Email is not registered."
                }
            }
            Swal.fire("Error.", errorMessage, "error");
           // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {
            
        });
    }

    function getCookieValue(name) {
        var cookies = document.cookie.split(';');
        for(var i = 0; i < cookies.length; i++) {
            var cookie = cookies[i].trim();
            if(cookie.indexOf(name + '=') === 0) {
                return cookie.substring(name.length + 1);
            }
        }
    
        return "es";
    }
}