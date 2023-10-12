function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    $scope.setVal = function() {

        $scope.properties.formOutputAgregar = {
            "lstCatGestionEscolarInput": [
                {
                    "nombre": "", 
                    "descripcion": "", 
                    "enlace": "", 
                    "propedeutico": false, 
                    "programaparcial": false, 
                    "matematicas": false, 
                    "inscripcionenero": "", 
                    "inscripcionagosto": "",
                    "propedeuticos":[],
                    "periodoDisponible": [], 
                    "tipocentroestudios": "", 
                    "isEliminado": false, 
                    "usuarioCreacion": "", 
                    "campus": null, 
                    "orden": null,
                    "fechaCreacion":null,
                    "caseId":null,
                    "clave":"",
                    "tipoLicenciatura": "",
                    "tipoCentroEstudio":"",
                    "inscripcionMayo":"",
                    "inscripcionSeptiembre":"",
                    "urlImgLicenciatura":"",
                    "isMedicina":false,
                    "idioma":""
                }
            ]
        };

        $scope.properties.variableDestino = "agregar"
        $scope.openCloseModal()



    };

    $scope.openCloseModal = function() {
            if ($scope.properties.OpenModal) {
                modalService.open($scope.properties.modalId);
            } else {
                modalService.close();
            }



        }
        /**
         * Execute a get/post request to an URL
         * It also bind custom data from success|error to a data
         * @return {void}
         */
    function doRequest(method, url, params, dataToSend, callback) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: dataToSend,
            params: params
        };

        return $http(req)
            .success(function(data, status) {

                callback(data.data[0]);
            })
            .error(function(data, status) {
                console.error("error al llamar" + url);

            })
            .finally(function() {
                vm.busy = false;
            });
    }
}