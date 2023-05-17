function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;
    var validarPeriodoDisponible;
    var validarInscripcionagosto;
    var validarInscripcionenero;
    var validarInscripcionmayo;
    var validarInscripcionseptiembre;
    var validarPropedeuticos;
    var validarClave;
    this.action = function action() {
        if ($scope.properties.action === 'Remove from collection') {
            removeFromCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Add to collection') {
            addToCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Start process') {
            startProcess();
        } else if ($scope.properties.action === 'Submit task') {
            submitTask();
        } else if ($scope.properties.action === 'Open modal') {
            closeModal($scope.properties.closeOnSuccess);
            openModal($scope.properties.modalId);
        } else if ($scope.properties.action === 'Close modal') {
            closeModal(true);
        } else if ($scope.properties.url) {
            doRequest($scope.properties.action, $scope.properties.url);
        }
    };

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }

    function removeFromCollection() {
        if ($scope.properties.collectionToModify) {
            if (!Array.isArray($scope.properties.collectionToModify)) {
                throw 'Collection property for widget button should be an array, but was ' + $scope.properties.collectionToModify;
            }
            var index = -1;
            if ($scope.properties.collectionPosition === 'First') {
                index = 0;
            } else if ($scope.properties.collectionPosition === 'Last') {
                index = $scope.properties.collectionToModify.length - 1;
            } else if ($scope.properties.collectionPosition === 'Item') {
                index = $scope.properties.collectionToModify.indexOf($scope.properties.removeItem);
            }

            // Only remove element for valid index
            if (index !== -1) {
                $scope.properties.collectionToModify.splice(index, 1);
            }
        }
    }

    function addToCollection() {
        if (!$scope.properties.collectionToModify) {
            $scope.properties.collectionToModify = [];
        }
        if (!Array.isArray($scope.properties.collectionToModify)) {
            throw 'Collection property for widget button should be an array, but was ' + $scope.properties.collectionToModify;
        }
        var item = angular.copy($scope.properties.valueToAdd);

        if ($scope.properties.collectionPosition === 'First') {
            $scope.properties.collectionToModify.unshift(item);
        } else {
            $scope.properties.collectionToModify.push(item);
        }
    }

    function startProcess() {
        if(validacion($scope.properties.dataToChange2)){
            debugger;
            if ($scope.properties.dataToChange2[0]) {
                let prom = doRequest("POST","../API/extension/AnahuacBecasRest?url=updateSDAEGestionEscolar&p=0&c=10",1,2).then(function() {
                    if($scope.properties.dataToChange2[2]){
                        doRequest("POST","../API/extension/AnahuacBecasRest?url=updateSDAECreditoGE&p=0&c=10",3,2).then(function() {
                            doRequest("GET", $scope.properties.url).then(function() {
                                $scope.properties.dataToChange = $scope.properties.dataToSet;
                                $scope.properties.dataToChange3 = $scope.properties.dataToSet2;
                            });
                            localStorageService.delete($window.location.href);
                        });
                    }else{
                        doRequest("POST","../API/extension/AnahuacBecasRest?url=insertSDAECreditoGE&p=0&c=10",3,2).then(function() {
                            doRequest("GET", $scope.properties.url).then(function() {
                                $scope.properties.dataToChange = $scope.properties.dataToSet;
                                $scope.properties.dataToChange3 = $scope.properties.dataToSet2;
                            });
                            localStorageService.delete($window.location.href);
                        });
                    }
                    
                });
            }else {
                debugger;
                let prom = doRequest("POST","../API/extension/AnahuacBecasRest?url=insertSDAEGestionEscolar&p=0&c=10",1,1).then(function() {
                    debugger;
                    doRequest("POST","../API/extension/AnahuacBecasRest?url=insertSDAECreditoGE&p=0&c=10",3,2).then(function() {
                            doRequest("GET", $scope.properties.url).then(function() {
                                $scope.properties.dataToChange = $scope.properties.dataToSet;
                                $scope.properties.dataToChange3 = $scope.properties.dataToSet2;
                            });
                        localStorageService.delete($window.location.href);
                    });
                });
                
            }
        }
    }
    function isNumeric(num){
        return !isNaN(num)
      }
    function validacion(datos){
        if(datos.length < 4){
            return false;
        }else if(datos[1].parcialidad == null || datos[1].parcialidad.length<=0 || !isNumeric(datos[1].parcialidad) ){
            swal("¡Aviso!", "Faltó capturar información en: Parcialidad", "warning");
            return false;
        }else if(datos[1].creditosemestre == null || datos[1].creditosemestre.length<=0  || !isNumeric(datos[1].creditosemestre)){
            swal("¡Aviso!", "Faltó capturar información en: credito del semestre", "warning");
            return false;
        }
        else if(datos[3][0].creditoenero == null || datos[3][0].creditoenero.length<=0  || !isNumeric(datos[3][0].creditoenero)){
            swal("¡Aviso!", "Faltó capturar información en: Costo crédito enero "+$scope.properties.fecha, "warning");
            return false;
        }else if(datos[3][0].creditomayo == null || datos[3][0].creditomayo.length<=0  || !isNumeric(datos[3][0].creditomayo)){
            swal("¡Aviso!", "Faltó capturar información en: Costo crédito mayo "+$scope.properties.fecha, "warning");
            return false;
        }else if(datos[3][0].creditoagosto == null || datos[3][0].creditoagosto.length<=0  || !isNumeric(datos[3][0].creditoagosto)){
            swal("¡Aviso!", "Faltó capturar información en: Costo crédito agosto "+$scope.properties.fecha, "warning");
            return false;
        }else if(datos[3][0].creditoseptiembre == null || datos[3][0].creditoseptiembre.length<=0  || !isNumeric(datos[3][0].creditoseptiembre)){
            swal("¡Aviso!", "Faltó capturar información en: Costo crédito septimebre "+$scope.properties.fecha, "warning");
            return false;
        }
        else if(datos[3][1].creditoenero == null || datos[3][1].creditoenero.length<=0  || !isNumeric(datos[3][1].creditoenero)){
            swal("¡Aviso!", "Faltó capturar información en: Costo crédito enero "+(parseInt($scope.properties.fecha)+1), "warning");
            return false;
        }else if(datos[3][1].creditomayo == null || datos[3][1].creditomayo.length<=0  || !isNumeric(datos[3][1].creditomayo)){
            swal("¡Aviso!", "Faltó capturar información en: Costo crédito mayo "+(parseInt($scope.properties.fecha)+1), "warning");
            return false;
        }else if(datos[3][1].creditoagosto == null || datos[3][1].creditoagosto.length<=0  || !isNumeric(datos[3][1].creditoagosto)){
            swal("¡Aviso!", "Faltó capturar información en: Costo crédito agosto "+(parseInt($scope.properties.fecha)+1), "warning");
            return false;
        }else if(datos[3][1].creditoseptiembre == null || datos[3][1].creditoseptiembre.length<=0  || !isNumeric(datos[3][1].creditoseptiembre)){
            swal("¡Aviso!", "Faltó capturar información en: Costo crédito septiembre "+(parseInt($scope.properties.fecha)+1), "warning");
            return false;
        }
        return true;
    }

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, index,tipo) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToChange2[index]),
        };
       // params: params
        return $http(req).success(function(data, status) {
            if(tipo == 1 && index == 1){
                $scope.properties.dataToChange2[3][0].sdaecatgestionescolar_pid = parseInt(data.data[0]);
                $scope.properties.dataToChange2[3][1].sdaecatgestionescolar_pid = parseInt(data.data[0]);
            }
            console.log(data);
            })
            .error(function(data, status) {
            
            })
            .finally(function() {
                vm.busy = false;
            });
    }

}