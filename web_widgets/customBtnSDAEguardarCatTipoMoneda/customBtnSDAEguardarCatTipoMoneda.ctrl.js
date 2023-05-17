function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;
    function fnctvalidar() {
        let validar = true;
        if ($scope.properties.accion === "editar") {
            if ($scope.properties.objCatGenerico.objCatGenerico.clave.trim() === "") {
                swal("¡Aviso!", "Faltó capturar información en: Clave.", "warning");
                validar = false;
            } else if ($scope.properties.objCatGenerico.objCatGenerico.descripcion.trim() === "") {
                swal("¡Aviso!", "Faltó capturar información en: Descripción.", "warning");
                validar = false;
            }
        } else if ($scope.properties.accion === "agregar") {
            if ($scope.properties.objCatGenerico.objCatGenerico.clave.trim() === "") {
                swal("¡Aviso!", "Faltó capturar información en: Clave.", "warning");
                validar = false;
            } else if ($scope.properties.objCatGenerico.objCatGenerico.descripcion.trim() === "") {
                swal("¡Aviso!", "Faltó capturar información en: Descripción.", "warning");
                validar = false;
            }
        }
        return validar;
    };

    $scope.Guardar = function () {
        $scope.properties.objCatGenerico.objCatGenerico.usuarioCreacion = $scope.properties.userData.user_name;
        if (fnctvalidar()) {
            var prom = doRequest('POST', '../API/extension/AnahuacBecasRest?url=insertUpdateCatGenerico&p=0&c=0').then(function () {
                // doRequest("GET", $scope.properties.url).then(function () {
                //     $scope.properties.dataToChange = $scope.properties.dataToSet;
                //     $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                // });
                // swal("Ok", "Guardado con éxito", "ok");
                // $scope.properties.accion = 'tabla';
            });
        }

    };

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }

    function doRequest(method, url, params) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.objCatGenerico),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                swal("¡Correcto!", "Se actualizaron los datos correctamente.", "success");
                $scope.properties.accion = 'tabla';
                // $scope.properties.dataFromSuccess = data;
                // $scope.properties.responseStatusCode = status;
                // $scope.properties.dataFromError = undefined;
                // notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status });
                // if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                //     redirectIfNeeded();
                // }
                //closeModal($scope.properties.closeOnSuccess);
            })
            .error(function (data, status) {
                //swal("¡Error!", "Algo salió mal.", "error");
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });


            })
            .finally(function () {
                vm.busy = false;
            });
    }
}