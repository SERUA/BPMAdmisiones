function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, $filter) {

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
                Swal.fire({ title: $filter('translate')('¡Atención!'), text: $filter('translate')('Captcha inválido.'), icon: 'warning' });
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
        
        if (username === "") {
            Swal.fire({ title: $filter('translate')('¡Atención!'), text: $filter('translate')('El Correo electrónico no debe ir vacío.'), icon: 'warning' });
        } else if (!re.test(String(username))) {
            Swal.fire({ title: $filter('translate')('¡Atención!'), text: $filter('translate')('El formato de correo electrónico es inválido.'), icon: 'warning' });
        } else if (password === "") {
            Swal.fire({ title: $filter('translate')('¡Atención!'), text: $filter('translate')('La Contraseña no debe ir vacía.'), icon: 'warning' });
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
            redirectIfNeeded();
        })
        .error(function(data, status) {
        	console.error(data);
            Swal.fire({ title: $filter('translate')('<strong>Atención</strong>'), icon: 'error', html: $filter('translate')('Correo electronico o Contraseña incorrecta.'), showCloseButton: false });
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
    
    function redirectIfNeeded() {
        let ipBonita = window.location.protocol + "//" + window.location.host;
        let url = ipBonita + $scope.properties.targetUrlOnSuccess;
        window.top.location.href = url;
    }
  }