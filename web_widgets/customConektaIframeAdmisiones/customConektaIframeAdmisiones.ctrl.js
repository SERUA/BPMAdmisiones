function ($scope, $http) {

    var publicKey = "";
    var loaded = false;
    // $scope.formInput = {
    // 	"solicitudApoyoEducativoInput":{
    // 		"ordenPagoConekta":""
    // 	},
    // 	"isPagoValidadoInput": true,
    // 	"isPagoConTarjetainput": true,
    // 	"isPagoRegresarInput": false
    // };
    
    $scope.formInput = { 
        "conIsPagoValidado" : false, 
        "isPagoTarjeta" : true ,
        "detalleSolicitudInput" : {
            "ordenPago":""
        },
        "isPagoCondonado": false,
        "isDescuento100":false
        
    }
    
    $scope.setCardObject = function (_token) {
        $scope.properties.tokenObject["card"] = _token;
    }

    $scope.showModal = function(){
        $("#loading").modal("show");
    };
    
    $scope.hideModal = function(){
        $("#loading").modal("hide");
    };
    
    function getConektaTokenbObject() {
        var req = {
            method: "GET",
            url: "../API/extension/AnahuacRestGet?url=createToken&p=0&c=10&campus_id=" + $scope.properties.objSolicitudAdmision.catCampus.persistenceId,
        };

        return $http(req).success(function (data, status) {
            $scope.properties.tokenObject = data[0];
        }).error(function (data, status) {
            // $scope.properties.dataFromError = data;
            swal("Error", "No se ha podido generar el token temporal, intente de nuevo mas tarde.", "error");
        }).finally(function () {
            if ($scope.properties.tokenObject) {
                setIframe();
            }
        });
    }
    
    function getConektaPublicKeyV2(){
        var req = {
            method: "GET",
            url: "../API/extension/AnahuacRestGet?url=getConektaPublicKeyV2&p=0&c=10&campus_id=" + $scope.properties.objSolicitudAdmision.catCampus.persistenceId,
        };

        return $http(req).success(function (data, status) {
            $scope.properties.publicKey = angular.copy(data.data[0]);
            publicKey = angular.copy(data.data[0]);
            getConektaTokenbObject();
        }).error(function (data, status) {
            // $scope.properties.dataFromError = data;
            swal("Error", "No se ha podido generar el token temporal, intente de nuevo mas tarde.", "error");
        }).finally(function () {
            // if ($scope.properties.tokenObject) {
            //     setIframe();
            // }
        });
    }

    $scope.$watch("properties.objSolicitudAdmision", () => {
        if ($scope.properties.objSolicitudAdmision) {
            if (!loaded) {
                loaded = true;
                getConektaPublicKeyV2();
                // getConektaTokenbObject();
            }
        }
    });

    function setIframe() {
        debugger;
        window.ConektaCheckoutComponents.Card({
            targetIFrame: "#conektaIframeContainer",
            allowTokenization: true,
            checkoutRequestId: $scope.properties.tokenObject.checkout.id,
            // publicKey: $scope.properties.publicKey,
            publicKey:  publicKey,
            options: {
                styles: {
                    inputType: 'basic', //'basic' | 'rounded' | 'line'
                    buttonType: 'basic', //'basic' | 'rounded' | 'sharp'
                    //Elemento que personaliza el borde de color de los elementos            
                    states: {
                        empty: {
                            borderColor: '#FFAA00'
                        },
                        invalid: {
                            borderColor: '#FF00E0'
                        },
                        valid: {
                            borderColor: '#FF5900'
                        }
                    }
                },
                languaje: 'es', // 'es' Español | 'en' Ingles
                button: {
                    colorText: '#ffffff',
                    text: 'Pagar',
                    backgroundColor: '#FF5900'
                },
                //Elemento que personaliza el diseño del iframe
                iframe: {
                    colorText: '#65A39B',
                    backgroundColor: '#FFFFFF'
                }
            },
            onCreateTokenSucceeded: function (_token) {
                var scope = angular.element($("custom-conekta-iframe-admisiones")).scope();
                scope.showModal();
                scope.$apply(function(){
                    scope.buildCardObject();
                })
            },
            onCreateTokenError: function (error) {
                $scope.hideModal();
                swal("Error", error.data.message_to_purchaser, "error");
            }
        });
    }

    $scope.buildCardObject = function(){
        $scope.properties.objectCard = {
            "name": $scope.properties.objSolicitudAdmision.primerNombre + " "+ $scope.properties.objSolicitudAdmision.apellidoPaterno,
            "email": $scope.properties.objSolicitudAdmision.correoElectronico,
            "phone": $scope.properties.objSolicitudAdmision.telefono,
            "campus_id": $scope.properties.objSolicitudAdmision.catCampus.persistenceId + "",
            "unit_price": $scope.properties.configuracionesPago.monto * 100,//Conekta acepta le pago en centavos
            "idToken": $scope.properties.tokenObject.id,
            "caseId" : $scope.properties.objSolicitudAdmision.caseId + "",
            "last4": "",
            "nombrePago": "",
            "campus": $scope.properties.objSolicitudAdmision.catCampus.descripcion,
            "concepto": $scope.properties.configuracionesPago.descripcion + " - ID: " + $scope.properties.idBanner 
        }
        
        var req = {
            method: "POST",
            url: "/API/extension/AnahuacRest?url=pagoTarjetaBecas&p=0&c=10",
            data: $scope.properties.objectCard
        };

        return $http(req).success(function (data, status) {
            $scope.formInput.detalleSolicitudInput.ordenPago = data.data[0].id;
            unassignTask();
        }).error(function (error, status) {
            $scope.hideModal();
            swal("Error",error.data.message_to_purchaser, "error");
        });
    }
    
    function unassignTask() {
        $scope.showModal();
        let url = "../API/bpm/userTask/" + $scope.properties.taskId;
        
        var req = {
            method: "PUT",
            url: url,
            data:{
                "assigned_id": ""
            }
        };

        return $http(req).success(function(data, status) {
            assignTask();
        })
        .error(function(data, status) {
            $scope.hideModal();
            swal("Error", data.message, "error");
        })
        .finally(function() {
            
        });
    }
    
    function assignTask() {
        $scope.showModal();
        let url = "../API/bpm/userTask/" + $scope.properties.taskId;
        
        var req = {
            method: "PUT",
            url: url,
            data:{
                "assigned_id": $scope.properties.userId
            }
        };

        return $http(req).success(function(data, status) {
            executeTask();
        })
        .error(function(data, status) {
            $scope.hideModal();
            swal("Error", data.message, "error");
        })
        .finally(function() {
            
        });
    }
    
    function executeTask(){
        let url = "../API/bpm/userTask/" + $scope.properties.taskId + "/execution";
        
        $http.post(url, $scope.formInput).success(function(data){
            $scope.properties.loadedOrder = true;
            setTimeout(function(){
                swal("Pago realizado con éxito.", "", "success").then(() => {
                    $scope.hideModal();
                    window.location.reload();
                });   
                
                setTimeout(function(){
                    window.location.reload();
                },  5000); 
            },  2000); 
        }).error(function(error){
            $scope.hideModal();
            swal("Error", "Algo salió mal con el proceso de pago. Revisa que los datos de la tarjeta estén correctos.", "error");
        });
    }
}