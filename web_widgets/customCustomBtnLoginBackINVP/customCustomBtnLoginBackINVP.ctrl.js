function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';
  
    var vm = this;
    var captchaElement;
    $scope.errorLoginCount = 0;
    $scope.isCaptchaLoaded = false;
  
    $scope.showLoading = function(){
        $("#loading").modal("show");
    }
    
    $scope.hideLoading = function(){
        $("#loading").modal("hide");
    }
    
    $scope.renderCaptcha = function(){
        $scope.showLoading();
        $scope.isCaptchaLoaded = false;
        captchaElement = grecaptcha.render(document.getElementById('captchaElement'), {
            'sitekey' : $scope.properties.reCAPTCHAkey
        });
        
        $scope.isCaptchaLoaded = true;
        
        setTimeout(function(){
            if($(window).width() >= 768){
                let height = $(".b-left").height() + "px";
                $(".re-dimension").css("height", height);
            }
        }, 200);
        
        $scope.hideLoading();
    }
    
    $scope.resetCaptcha = function(){
        $scope.showLoading();
        $scope.isCaptchaLoaded = false;
        grecaptcha.reset();   
        $scope.isCaptchaLoaded = true;
        
        setTimeout(function(){
            if($(window).width() >= 768){
                let height = $(".b-left").height() + "px";
                $(".re-dimension").css("height", height);
            }
        }, 200);
        
        $scope.hideLoading();
    };
    
    this.action = function action() {
        
        if($scope.errorLoginCount === 2){
            let captchaResponse = grecaptcha.getResponse();
            
            if(captchaResponse !== ""){
                // doRequest($scope.properties.action, $scope.properties.url);
                $scope.validateForm();
            } else {
                Swal.fire("¡Atención!", "Captcha inválido.", "warning");
                $scope.resetCaptcha();
            }
        } else {
            // doRequest($scope.properties.action, $scope.properties.url);
            $scope.validateForm();
        }
    };
    
    $scope.validateForm = function(){
        let username = $scope.properties.dataToSend.username;
        let password = $scope.properties.dataToSend.password;
  
        // const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        const re = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
        
        if(username  === ""){
            Swal.fire("¡Atención!", "El Correo electrónico no debe ir vacío.", "warning");
        } else if (!re.test(String(username))){
            Swal.fire("¡Atención!", "El formato de correo electrónico es inválido.", "warning");
        } else if (password === ""){
            Swal.fire("¡Atención!", "La Contraseña no debe ir vacía.", "warning");
        } 
        
        // else if (password.length > 8){
        //     Swal.fire("Error", "Tu contraseña es muy corta", "warning");
        // } 
        
        else {
            doRequest($scope.properties.action, $scope.properties.url);
        }
    }
  
    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params) {
        $scope.showLoading();
        vm.busy = true;
      
        // let data = {
        //     "username" : $scope.properties.dataToSend.username,
        //     "password" : $scope.properties.dataToSend.password
        // }

        let data = "redirect=false&username=" + $scope.properties.dataToSend.username + "&password=" + $scope.properties.dataToSend.password;

        // let url2 = "/bonita/loginservice?&redirect=false&username=" + $scope.properties.dataToSend.username + "&password=" + $scope.properties.dataToSend.password;
      
        let url2 = "/bonita/loginservice";

        var req = {
            method: method,
            url: url2,
            data: data,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        };
      
        return $http(req).success(function(data, status) {
            $scope.properties.dataFromSuccess = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromError = undefined;
            getSession();
            // redirectIfNeeded();
        })
        .error(function(data, status) {
        	console.error(data);
            Swal.fire({
                title: '<strong>Atención</strong>',
                icon: 'error',
                //html:($scope.properties.targetUrlOnSuccess.includes('administrativo'))?'Correo electronico o Contraseña incorrecta.':'Correo electronico o Contraseña incorrecta. <br><br><br><br><p class="swal2-title">Recuerda</p> <p>Si iniciaste tu registro <strong>hasta</strong> el jueves 29 de abril del 2021 <br>da clic aquí </p>' + '<a class="btn btn-primary" href="https://servicios.redanahuac.mx/admisiones.php">Iniciar sesión</a> ', showCloseButton: false
                html:'Correo electronico o Contraseña incorrecta.', showCloseButton: false
            });
            $scope.errorLoginCount ++;
            if($scope.errorLoginCount === 2){
                $scope.renderCaptcha();
            } else if ($scope.errorLoginCount > 2){
                $scope.resetCaptcha();
            }
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            
        })
        .finally(function() {
            $scope.hideLoading();
            vm.busy = false;
        });
    }

    function getMemberships(){
        let url = "../API/identity/membership?c=999&=p=0&f=user_id="+ $scope.user_id + "&d=role_id";

        $http.get(url).success(function(_success){
            let redirect = false;
            debugger;
            for(let membership of _success){
                if(membership.role_id.name === "PSICOLOGO"){
                    redirect = true;
                } else if(membership.role_id.name === "PSICOLOGO SUPERVISOR"){
                    redirect = true;
                } else if(membership.role_id.name === "TI SERUA"){
                    redirect = true;
                } else if(membership.role_id.name === "TI CAMPUS"){
                    redirect = true;
                } else if(membership.role_id.name === "ADMINISTRADOR"){
                    redirect = true;
                } else if(membership.role_id.name === "SERUA"){
                    redirect = true;
                }
            }

            if(redirect){
                redirectIfNeeded();
            } else {
                Swal.fire({
                    title: '<strong>Atención</strong>',
                    icon: 'error',
                    html:'No tiene permisos para ver esta sección. ', showCloseButton: false
                })
                // .then(function(){
                //     alert("si");
                // });
            }
        }).error(function(){
            swal("Error", "Error al intentar recuperar la información del usuario, intente de nuevo mas tarde.");
        });
    }

    function getSession(){
        let url = "../API/system/session/1";

        $http.get(url).success(function(_success){
            $scope.user_id = _success.user_id;
            getMemberships();
        }).error(function(){
            Swal.fire({
                title: '<strong>Atención</strong>',
                icon: 'error',
                html:'Error al obtener la información del usuario. Intente de nuevo ams tarde .', showCloseButton: false
            });
        });
    }
    
    function redirectIfNeeded() {
        let ipBonita = window.location.protocol + "//" + window.location.host;
        let url = ipBonita + $scope.properties.targetUrlOnSuccess;
        window.top.location.href = url;
    }
}