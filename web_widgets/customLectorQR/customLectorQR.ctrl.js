function PbButtonCtrl($scope, $http, $window) {
  var ctrl = this;
  this.camaraId = '';
  this.html5QrCode = '';
  this.activado = false;
  this.leyendo = false;
  this.desactivar = false;
  this.camaras = [];
  this.indexCamara = 0;
  this.selected = {};
  
    $scope.inicializar = function(){
        ctrl.leyendo = false;
        $scope.properties.Activado = true;
            
        ctrl.camaraId = ctrl.selected.id;
        $scope.abrirCamara();
        $scope.$apply();
    }

    $scope.iniciar = function(){
        ctrl.desactivar = true;
        $scope.inicializar();
        
    }
    
    $scope.abrirCamara = function(){
        // Create instance of the object. The only argument is the "id" of HTML element created above.
        ctrl.html5QrCode = new Html5Qrcode("reader");
        var isMobile = /iPhone|iPad|iPod|Android/i.test(navigator.userAgent);
       
        if(!isMobile){
            var w = window.innerWidth;
            var h = window.innerHeight;
            var pixel = 260;
            if(w>=1200){
                pixel = 350
            }
            ctrl.html5QrCode.start(
              ctrl.camaraId,     // retreived in the previous step.
              {
                fps: 10,    // sets the framerate to 10 frame per second
                qrbox: pixel  // sets only 250 X 250 region of viewfinder to
                            // scannable, rest shaded.
              },
              qrCodeMessage => {
                  
                // do something when code is read. For example:
                if(ctrl.leyendo){return}
                console.log(`QR Code detected: ${qrCodeMessage}`);
                if(!IsJsonString(qrCodeMessage)){
                    ctrl.leyendo = true;
                    swal("¡QR inválido!", `El código QR escaneado no es válido`,"warning", {
                        closeOnClickOutside: false,
                        buttons: {
                            catch: {
                                text: "OK",
                                value: "OK",
                            }
                        },
                    }).then((value) => { ctrl.leyendo = false });
                }else{
                    var jsonData =  JSON.parse(qrCodeMessage);
                    if(jsonData.nombre !== undefined && jsonData.nombre !== null && jsonData.idbanner !== undefined && jsonData.idbanner !== null){
                        ctrl.leyendo = true;
						///Funcion para pruebas
						$scope.doRequest(jsonData.idbanner);
						if($scope.properties.datosUsuario.length === 0){
                            swal("¡Aspirante no registrado!", `El aspirante no se encuentra registrado en esta sesión ${jsonData.nombre} ${jsonData.idbanner}`,"warning", {
                                closeOnClickOutside: false,
                                buttons: {
                                    catch: {
                                        text: "OK",
                                        value: "OK",
                                    }
                                },
                            }).then((value) => { ctrl.leyendo = false });
                        }else{
                            ctrl.leyendo = true;
                            $scope.properties.infoQR = jsonData;
                            $scope.properties.cambioPantalla = "lista";
                            swal("¡Aspirante encontrado!", `Aspirante: ${jsonData.nombre}`,"success");
                            $scope.fechaCheck();
                            $scope.cerrar();
                        }
						
                        /*$scope.doRequest(jsonData.usuario).then(function() {
                        if($scope.properties.datosUsuario.length === 0){
                            swal("¡Aspirante no registrado!", `El aspirante no se encuentra registrado en esta sesión ${jsonData.nombre} ${jsonData.usuario}`,"warning", {
                                closeOnClickOutside: false,
                                buttons: {
                                    catch: {
                                        text: "OK",
                                        value: "OK",
                                    }
                                },
                            }).then((value) => { ctrl.leyendo = false });
                        }else{
                            ctrl.leyendo = true;
                            $scope.properties.infoQR = jsonData;
                            $scope.properties.cambioPantalla = "lista";
                            swal("¡Aspirante encontrado!", `Aspirante: ${jsonData.nombre}`,"success");
                            $scope.fechaCheck();
                            $scope.cerrar();
                        }
                        }).catch(err => {
                            ctrl.leyendo = true;
                            swal("¡Se ha producido un error!", `Al momento de leer el QR se ha producido un error`,"warning", {
                                closeOnClickOutside: false,
                                buttons: {
                                    catch: {
                                        text: "OK",
                                        value: "OK",
                                    }
                                },
                            }).then((value) => { ctrl.leyendo = false });
                        });*/
                    }else{
                        ctrl.leyendo = true;
                        swal("¡QR inválido!", `El código QR escaneado no tiene datos validos`,"warning", {
                            closeOnClickOutside: false,
                            buttons: {
                                catch: {
                                    text: "OK",
                                    value: "OK",
                                }
                            },
                        }).then((value) => { ctrl.leyendo = false });
                    }
                }
              },
              errorMessage => {
                // parse error, ideally ignore it. For example:
                console.log(`QR Code no longer in front of camera.`);
              })
            .catch(err => {
              // Start failed, handle it. For example,
              console.log(`Unable to start scanning, error: ${err}`);
            });
            
        }else{
            
            ctrl.html5QrCode.start(
             ctrl.camaraId,     // retreived in the previous step.
              {
                fps: 10,    // sets the framerate to 10 frame per second
                qrbox: 260  // sets only 250 X 250 region of viewfinder to
                            // scannable, rest shaded.
              },
              qrCodeMessage => {
                    
                // do something when code is read. For example:
                if(ctrl.leyendo){return}
                console.log(`QR Code detected: ${qrCodeMessage}`);
                if(!IsJsonString(qrCodeMessage)){
                    ctrl.leyendo = true;
                    swal("¡QR inválido!", `El código QR escaneado no es válido`,"warning", {
                        closeOnClickOutside: false,
                        buttons: {
                            catch: {
                                text: "OK",
                                value: "OK",
                            }
                        },
                    }).then((value) => { ctrl.leyendo = false });
                }else{
                    var jsonData =  JSON.parse(qrCodeMessage);
                    if(jsonData.nombre !== undefined && jsonData.nombre !== null && jsonData.idbanner !== undefined && jsonData.idbanner !== null){
                        ctrl.leyendo = true;
						///datos de pruebas
						$scope.doRequest(jsonData.idbanner);
						if($scope.properties.datosUsuario.length === 0){
                            swal("¡Aspirante no registrado!", `El aspirante no se encuentra registrado en esta sesión ${jsonData.nombre} ${jsonData.idbanner}`,"warning", {
                                closeOnClickOutside: false,
                                buttons: {
                                    catch: {
                                        text: "OK",
                                        value: "OK",
                                    }
                                },
                            }).then((value) => { ctrl.leyendo = false });
                        }else{
                            ctrl.leyendo = true;
                            $scope.properties.infoQR = jsonData;
                            $scope.properties.cambioPantalla = "lista";
                            swal("¡Aspirante encontrado!", `Aspirante: ${jsonData.nombre}`,"success");
                            $scope.fechaCheck();
                            $scope.cerrar();
                        }
                        /*$scope.doRequest(jsonData.usuario).then(function() {
                        if($scope.properties.datosUsuario.length === 0){
                            swal("¡Aspirante no registrado!", `El aspirante no se encuentra registrado en esta sesión ${jsonData.nombre} ${jsonData.usuario}`,"warning", {
                                closeOnClickOutside: false,
                                buttons: {
                                    catch: {
                                        text: "OK",
                                        value: "OK",
                                    }
                                },
                            }).then((value) => { ctrl.leyendo = false });
                        }else{
                            ctrl.leyendo = true;
                            $scope.properties.infoQR = jsonData;
                            $scope.properties.cambioPantalla = "lista";
                            swal("¡Aspirante encontrado!", `Aspirante: ${jsonData.nombre}`,"success");
                            $scope.fechaCheck();
                            $scope.cerrar();
                        }
                        }).catch(err => {
                            ctrl.leyendo = true;
                            swal("¡Se ha producido un error!", `Al momento de leer el QR se ha producido un error`,"warning", {
                                closeOnClickOutside: false,
                                buttons: {
                                    catch: {
                                        text: "OK",
                                        value: "OK",
                                    }
                                },
                            }).then((value) => { ctrl.leyendo = false });
                        });*/
                    }else{
                        ctrl.leyendo = true;
                        swal("¡QR inválido!", `El código QR escaneado no tiene datos validos`,"warning", {
                            closeOnClickOutside: false,
                            buttons: {
                                catch: {
                                    text: "OK",
                                    value: "OK",
                                }
                            },
                        }).then((value) => { ctrl.leyendo = false });
                    }
                }
              },
              errorMessage => {
                // parse error, ideally ignore it. For example:
                console.log(`QR Code no longer in front of camera.`);
              })
            .catch(err => {
              // Start failed, handle it. For example,
              console.log(`Unable to start scanning, error: ${err}`);
            });
        }
        
         ctrl.activado = true;
        
    }
        
    $scope.cerrar = function(){
        //apagar
        if(!ctrl.activado){ return}
        ctrl.html5QrCode.stop().then(ignore => {
          // QR Code scanning is stopped.
          console.log("QR Code scanning stopped.");
          ctrl.html5QrCode.clear();
          ctrl.activado = false;
          $scope.properties.Activado = false;
          ctrl.desactivar = false;
          $scope.$apply();
        }).catch(err => {
          // Stop failed, handle it.
          console.log("Unable to stop scanning.");
        });
    }
    
    $scope.fechaCheck = function(){
        const date = new Date();
        const today = date.getDate().toString()+"/"+date.getMonth().toString()+"/"+date.getFullYear().toString();
        $scope.properties.ultimoCheck.fecha = today;
        $scope.properties.ultimoCheck.hora = date.getHours().toString()+":"+date.getMinutes().toString();
    }
    
    function IsJsonString(str) {
        try {
            JSON.parse(str);
        } catch (e) {
            return false;
        }
        return true;
    }
    
    $scope.doRequest = function(usuario) {
        /*var info =  {
            "tarea":"Validar Información",
            "lstFiltro": [
                {
                    columna: "EMAIL",
                    operador: "Igual a",
                    valor: usuario+""
                }
            ], 
            "type": "aspirantes_proceso",
            "orderby":"",
            "orientation":"DESC",
            "limit":20,
            "offset":0,
            "usuario":"Administrador"
        }*/
        
        /*
        var info = {
            "lstFiltro": [],
            "usuario":"alvarado.eduardo@correo.com",
            "orderby":"",
            "orientation":"DESC",
            "limit":20,
            "offset":0
        }*/
        
        /*var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100",
            data: info,
            params: "31"
        };

        return $http(req)
            .success(function (data, status) {
                //data.data[0]
                $scope.properties.datosUsuario = data.data[0];
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {
                
            });*/
            
            
            for(var i = 0; i < $scope.properties.lstContenido.length; i++){
                if($scope.properties.lstContenido[i].aspirantes[0].idbanner === usuario){
                   $scope.properties.datosUsuario = $scope.properties.lstContenido[i]; 
                }
            }
    }

    $scope.cambioCamara = function(){
        console.log("seleccionado");
        console.log(ctrl.selected);
        //$scope.cerrarCambio();
        //$scope.iniciar();
        
    }
    
    $scope.buscarCamaras = function(){
        // This method will trigger user permissions
        Html5Qrcode.getCameras().then(devices => {
            /**
            * devices would be an array of objects of type:
            * { id: "id", label: "label" }
            */
            if (devices && devices.length) {
            ctrl.camaras = devices;
            ctrl.selected = ctrl.camaras[0];
            $scope.$apply();
            }
        }).catch(err => {
            // handle err
        });
    }
    
}
