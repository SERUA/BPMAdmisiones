function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    $scope.setVal = function() {
        $scope.properties.variableDestino = angular.copy($scope.properties.variableAcopiar);
        $scope.openCloseModal()

    };

    $scope.openCloseModal = function() {

            if ($scope.properties.OpenModal) {
                modalService.open($scope.properties.modalId);
            } else {
                $scope.properties.modalId.lstCatCampusInput[0] = {
                    "id": "",
                    "descripcion": "",
                    "usuarioBanner": "",
                    "clave": "",
                    "urlAvisoPrivacidad": "",
                    "isEliminado": false,
                    "urlDatosVeridicos": "",
                    "urlAutorDatos": "",
                    "isEnabled": false,
                    "fechaCreacion": null,
                    "fechaImplementacion": null,
                    "grupoBonita": "",
                    "orden": null,
                    "calle": "",
                    "colonia": "",
                    "numeroInterior": "",
                    "numeroExterior": "",
                    "codigoPostal": "",
                    "municipio": "",
                    "pais": null,
                    "estado": "",
                    "urlImagen": "",
                    "email": ""
                }
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