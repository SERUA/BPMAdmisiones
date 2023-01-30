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
            $scope.getTotalPreguntasContestadas();
            //checkSesionActiva();
            //checkSesion();
            //doRequest($scope.properties.action, $scope.properties.url);
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
            if(!$scope.terminadoexamen && $scope.contestopreguntas){
                window.top.location.href = '/bonita/apps/aspiranteinvp/examen/';
            }else{
                redirectIfNeeded();
            }
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
    
    function redirectIfNeeded() {
        let ipBonita = window.location.protocol + "//" + window.location.host;
        let url = ipBonita + $scope.properties.targetUrlOnSuccess;
        window.top.location.href = url;
    }
    
     $scope.$watch("properties.datosSolicitud", function(){
        if(($scope.properties.datosSolicitud === undefined || $scope.properties.datosSolicitud.length === 0)){
           var req = {
                method: "GET",
                url: `../API/identity/membership?p=0&c=10&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
            };

            return $http(req).success(function(data, status) {
                    $scope.lstMembership = data;
                })
                .error(function(data, status) {
                    console.error(data);
                })
                .finally(function() {});
        }else{
            
        }
    });
    
    //$scope.filtroaspirante = {"tarea":"Validar Información","lstFiltro":[{"columna":"NOMBRE,EMAIL,CURP","operador":"Que contengan","valor":$scope.properties.dataToSend,"type":"aspirantes_proceso_fechas","orderby":"","orientation":"DESC","limit":20,"offset":0,"usuario":"Mario.Icedo@soaswfactory.com","estatusSolicitud":"Aspirantes en proceso","aspirantes":"regular"}
    
    function checkSesion() {
        debugger;
        $scope.showLoading();
        vm.busy = true;

        const today = new Date();
        const yyyy = today.getFullYear();
        let mm = today.getMonth() + 1; // Months start at 0!
        let dd = today.getDate();

        if (dd < 10) dd = '0' + dd;
        if (mm < 10) mm = '0' + mm;

        const formattedToday = yyyy+'-'+mm+'-'+dd;
      
        let data = {
            "username" : $scope.properties.dataToSend.username,
            "aplicacion": formattedToday
        }

        var req = {
            method: 'POST',
            url: '../API/extension/AnahuacINVPRestAPI?url=getSesionLogin&p=0&c=10',
            data: data
        };
      
        return $http(req).success(function(data, status) {
            if($scope.properties.datosSolicitud === 0){
                Swal.fire({
                    title: '<strong>Atención</strong>',
                    icon: 'error',
                    //html:($scope.properties.targetUrlOnSuccess.includes('administrativo'))?'Correo electronico o Contraseña incorrecta.':'Correo electronico o Contraseña incorrecta. <br><br><br><br><p class="swal2-title">Recuerda</p> <p>Si iniciaste tu registro <strong>hasta</strong> el jueves 29 de abril del 2021 <br>da clic aquí </p>' + '<a class="btn btn-primary" href="https://servicios.redanahuac.mx/admisiones.php">Iniciar sesión</a> ', showCloseButton: false
                    html:'El usuario o la contraseña estan incorrectos.', showCloseButton: false
                });
            } else if(data.data.length === 0){
                getDatosSesion();
            }else{

                  let data = {
                      "username": $scope.properties.dataToSend.username,
                      "havesesion": true
                  }

                  var req = {
                      method: "POST",
                      url: "/bonita/API/extension/AnahuacINVPRestAPI?url=updateSesion&p=0&c=10",
                      data: data
                  };

                  return $http(req).success(function(data, status) {
                          $scope.properties.dataFromSuccess = data;
                          $scope.properties.responseStatusCode = status;
                          $scope.properties.dataFromError = undefined;
                          doRequest($scope.properties.action, $scope.properties.url);
                      })
                      .error(function(data, status) {
                          doRequest($scope.properties.action, $scope.properties.url);
                          // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                      })
                      .finally(function() {});  
            }
            
            
            //redirectIfNeeded();
        })
        .error(function(data, status) {
            console.error(data);
            Swal.fire({
                title: '<strong>Atención</strong>',
                icon: 'error',
                //html:($scope.properties.targetUrlOnSuccess.includes('administrativo'))?'Correo electronico o Contraseña incorrecta.':'Correo electronico o Contraseña incorrecta. <br><br><br><br><p class="swal2-title">Recuerda</p> <p>Si iniciaste tu registro <strong>hasta</strong> el jueves 29 de abril del 2021 <br>da clic aquí </p>' + '<a class="btn btn-primary" href="https://servicios.redanahuac.mx/admisiones.php">Iniciar sesión</a> ', showCloseButton: false
                html:'No existe una sesión activa.', showCloseButton: false
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

     function checkSesionActiva() {
        debugger;
        $scope.showLoading();
        vm.busy = true;
        let entro = false;
        let data = {
            "username" : $scope.properties.dataToSend.username
        }

        var req = {
            method: 'POST',
            url: '../API/extension/AnahuacINVPRestAPI?url=getSesionActiva&p=0&c=10',
            data: data
        };
      
        return $http(req).success(function(data, status) {
            if(data.data.length > 1){
                for (var i = 0; data.data.length; i++) {
                    if(data.data[i].havesesion === true){
                        entro = true;
                        Swal.fire({
                            title: '<strong>Atención</strong>',
                            icon: 'error',
                            //html:($scope.properties.targetUrlOnSuccess.includes('administrativo'))?'Correo electronico o Contraseña incorrecta.':'Correo electronico o Contraseña incorrecta. <br><br><br><br><p class="swal2-title">Recuerda</p> <p>Si iniciaste tu registro <strong>hasta</strong> el jueves 29 de abril del 2021 <br>da clic aquí </p>' + '<a class="btn btn-primary" href="https://servicios.redanahuac.mx/admisiones.php">Iniciar sesión</a> ', showCloseButton: false
                            html:'Existe una sesión activa con este usuario <br> Contacta a tu aplicador.', showCloseButton: false
                        });
                    }
                }
            }else if(data.data.length === 0){
                checkSesion();
            }
            else if(data.data[0].havesesion === true){
                entro = true;
                 Swal.fire({
                    title: '<strong>Atención</strong>',
                    icon: 'error',
                    //html:($scope.properties.targetUrlOnSuccess.includes('administrativo'))?'Correo electronico o Contraseña incorrecta.':'Correo electronico o Contraseña incorrecta. <br><br><br><br><p class="swal2-title">Recuerda</p> <p>Si iniciaste tu registro <strong>hasta</strong> el jueves 29 de abril del 2021 <br>da clic aquí </p>' + '<a class="btn btn-primary" href="https://servicios.redanahuac.mx/admisiones.php">Iniciar sesión</a> ', showCloseButton: false
                    html:'Existe una sesión activa con este usuario <br> Contacta a tu aplicador.', showCloseButton: false
                });
            }else{
               checkSesion();
            }

            if(!entro){
                checkSesion();
            }
            //redirectIfNeeded();
        })
        .error(function(data, status) {
            console.error(data);
            Swal.fire({
                title: '<strong>Atención</strong>',
                icon: 'error',
                //html:($scope.properties.targetUrlOnSuccess.includes('administrativo'))?'Correo electronico o Contraseña incorrecta.':'Correo electronico o Contraseña incorrecta. <br><br><br><br><p class="swal2-title">Recuerda</p> <p>Si iniciaste tu registro <strong>hasta</strong> el jueves 29 de abril del 2021 <br>da clic aquí </p>' + '<a class="btn btn-primary" href="https://servicios.redanahuac.mx/admisiones.php">Iniciar sesión</a> ', showCloseButton: false
                html:'No existe una sesión activa.', showCloseButton: false
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


    $scope.getTotalPreguntasContestadas = function() {
        debugger;
        var req = {
            method: "GET",
            url: "../API/extension/AnahuacINVPRestGet?url=getTotalPreguntasContestadas&p=0&c=10&username=" + $scope.properties.dataToSend.username
        };
        return $http(req).success(function(data, status) {
            if(data.data[0].totalPreguntas === 0){
              $scope.contestopreguntas = false;
            }else{
                $scope.contestopreguntas = true;
            }
                $scope.getTerminadoExamen();
            }).error(function(data, status) {
                console.log(data);
            })
            .finally(function() {
                //blockUI.stop();
            });
            
    }


    $scope.getTerminadoExamen = function() {
        debugger;
        var req = {
            method: "GET",
            url: "../API/extension/AnahuacINVPRestGet?url=getTerminadoExamen&p=0&c=10&username=" + $scope.properties.dataToSend.username
        };
        return $http(req).success(function(data, status) {
            if(data.data.length === 0){
               $scope.terminadoexamen = false;
            }else if(data.data[0].terminado === false){
                $scope.terminadoexamen = false;
            }else if(data.data[0].terminado === true){
                $scope.terminadoexamen = true;
                Swal.fire({
                    title: '<strong>Atención</strong>',
                    icon: 'error',
                    //html:($scope.properties.targetUrlOnSuccess.includes('administrativo'))?'Correo electronico o Contraseña incorrecta.':'Correo electronico o Contraseña incorrecta. <br><br><br><br><p class="swal2-title">Recuerda</p> <p>Si iniciaste tu registro <strong>hasta</strong> el jueves 29 de abril del 2021 <br>da clic aquí </p>' + '<a class="btn btn-primary" href="https://servicios.redanahuac.mx/admisiones.php">Iniciar sesión</a> ', showCloseButton: false
                    html:'Has concluido con tu examen.', showCloseButton: false
                });
            }

            /*if(!$scope.terminadoexamen && $scope.contestopreguntas){
                doRequest($scope.properties.action, $scope.properties.url);
            }else{
                checkSesionActiva();
            }*/

            checkSesionActiva();

            }).error(function(data, status) {
                console.log(data);
            })
            .finally(function() {
                //blockUI.stop();
            });
            
    }

     function getDatosSesion() {
        debugger;
        vm.busy = true;

        let data = {
            "username" : $scope.properties.dataToSend.username
        }

        var req = {
            method: 'POST',
            url: '../API/extension/AnahuacINVPRestAPI?url=getDatosSesionLogin&p=0&c=10',
            data: data
        };
      
        return $http(req).success(function(data, status) {
            if(data.data.length > 0){
                var pos = data.data.length - 1;
                var content = document.createElement('div');
                let message  = "<p style='text-align: justify;'>No existe una sesión activa para este usuario, estas programado para el día " + data.data[pos].aplicacion + " a las " + data.data[pos].entrada +", para más información contacta a tu aplicador</p>";
                content.innerHTML = message;
                
                Swal.fire({
                    title: '<strong>Atención</strong>',
                    icon: 'error',
                    //html:($scope.properties.targetUrlOnSuccess.includes('administrativo'))?'Correo electronico o Contraseña incorrecta.':'Correo electronico o Contraseña incorrecta. <br><br><br><br><p class="swal2-title">Recuerda</p> <p>Si iniciaste tu registro <strong>hasta</strong> el jueves 29 de abril del 2021 <br>da clic aquí </p>' + '<a class="btn btn-primary" href="https://servicios.redanahuac.mx/admisiones.php">Iniciar sesión</a> ', showCloseButton: false
                    html:"<p style='text-align: justify;'>No existe una sesión activa para este usuario, estas programado para el día " + data.data[pos].aplicacion + " a las " + data.data[pos].entrada +", para más información contacta a tu aplicador</p>", showCloseButton: false
                });
            }else{
                Swal.fire({
                    title: '<strong>Atención</strong>',
                    icon: 'error',
                    //html:($scope.properties.targetUrlOnSuccess.includes('administrativo'))?'Correo electronico o Contraseña incorrecta.':'Correo electronico o Contraseña incorrecta. <br><br><br><br><p class="swal2-title">Recuerda</p> <p>Si iniciaste tu registro <strong>hasta</strong> el jueves 29 de abril del 2021 <br>da clic aquí </p>' + '<a class="btn btn-primary" href="https://servicios.redanahuac.mx/admisiones.php">Iniciar sesión</a> ', showCloseButton: false
                    html:'Este usuario no tiene una sesión registrada.', showCloseButton: false
                });
            }
        })
        .error(function(data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            
        })
        .finally(function() {
            $scope.hideLoading();
            vm.busy = false;
        });
    }


  }