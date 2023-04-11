function ($scope, $http) {

    var loaded = false;
    var publicKey = "";
    $scope.formInput = {
    	"solicitudApoyoEducativoInput":{
    		"ordenPagoConekta":""
    	},
    	"isPagoValidadoInput": true,
    	"isPagoConTarjetainput": true,
    	"isPagoRegresarInput": false
    };
    
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
        debugger;
        var req = {
            method: "GET",
            url: "../API/extension/AnahuacRestGet?url=getConektaPublicKeyV2&p=0&c=10&campus_id=" + $scope.properties.objSolicitudAdmision.catCampusEstudio.persistenceId,
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
                // getConektaTokenbObject();
                getConektaPublicKeyV2();
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
            publicKey: publicKey,
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
                debugger;
                var scope = angular.element($("custom-conekta-iframe")).scope();
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
            "last4": "4444",
            "nombrePago": "jose garcia",
            "campus": $scope.properties.objSolicitudAdmision.catCampus.descripcion,
            "concepto": $scope.properties.configuracionesPago.descripcion + " - ID: " + $scope.properties.idBanner 
        }
        
        var req = {
            method: "POST",
            url: "/API/extension/AnahuacRest?url=pagoTarjetaBecas&p=0&c=10",
            data: $scope.properties.objectCard
        };

        return $http(req).success(function (data, status) {
            $scope.formInput.solicitudApoyoEducativoInput.ordenPagoConekta = data.data[0].id;
            $scope.properties.solicitudApoyoEducativo.ordenPagoConekta = data.data[0].id;
            executeTask();
        }).error(function (error, status) {
            $scope.hideModal();
            swal("Error",error.data.message_to_purchaser, "error");
        });
    }
    
    function executeTask(){
        let url = "../API/bpm/userTask/" + $scope.properties.taskId + "/execution";
        
        $http.post(url, $scope.formInput).success(function(data){
            $scope.hideModal();
            swal("Pago realizado con éxito.", "", "success")
            .then(() => {
                // window.location.reload();
                insertBitacora();
            });
        }).error(function(error){
            $scope.hideModal();
            swal("Error", "Algo salió mal con el proceso de pago. Revisa que los datos de la tarjeta estén correctos.", "error");
        });
    }
    
    function insertBitacora(){
        let url = "../API/extension/AnahuacBecasRest?url=insertBitacoraSDAE&p=0&c=10";
        
        $http.post(url, $scope.formInput).success(function(data){
            
        }).error(function(error){
            $scope.hideModal();
            // swal("Error", "Algo salió mal con el proceso de pago. Revisa que los datos de la tarjeta estén correctos.", "error");
        }).finally(function(){
            $scope.hideModal();
            window.location.reload();
        });
    }
}