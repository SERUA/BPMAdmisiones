function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    $scope.myFunc = function () {
        if ($scope.properties.arregloOServicio) {
            doRequest($scope.properties.tipoDeUrl, $scope.properties.url);
        } else {
            $scope.searchArray();
        }
    };

    $scope.searchArray = function () {
        let str = {
            "valor": "",
            "index": null,
            "caseid": 0
        }
        $scope.properties.arregloDatos.forEach((datos, index) => {
            if ($scope.properties.datoActual == datos[$scope.properties.campoDato]) {

                if (index > 0 && !$scope.properties.accionNextOrReturn) {
                    str.valor == datos[index - 1][$scope.properties.campoDato];
                    str.index = index - 1;
                    str.caseid = datos[index - 1]["caseid"];
                } else if (index < ($scope.properties.arregloDatos.length - 1) && $scope.properties.accionNextOrReturn) {
                    str.valor == datos[index + 1][$scope.properties.campoDato];
                    str.caseid = datos[index + 1]["caseid"];
                    str.index = index + 1;
                } else {
                    swal(`¡El aspirante es el ${$scope.properties.accionNextOrReturn ? "último de la lista " : "primero de la lista "}, no se puede seleccionar otro!`, "", "")
                }
            }
        });

        $scope.properties.valor = str.valor;
        $scope.properties.posicionDato = str.index;
        $scope.proterties.caseid = str.caseid;
    }

    function doRequest(method, url, params) {
        var datos = "";
        if (method == "POST") {
            datos = angular.copy($scope.properties.datosPost);
            datos.accion = $scope.properties.accionNextOrReturn;
        }


        var req = {
            method: method,
            url: url,
            data: datos,
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                if (data.data[0].accion !== undefined && !data.data[0].accion) {
                    swal(`¡El aspirante es el ${$scope.properties.accionNextOrReturn ? "último de la lista, " : "primero de la lista, "}no se puede seleccionar otro!`, "", "")
                } else {

                    $scope.properties.value = data.data[0].idbanner
                    $scope.properties.caseid = data.data[0].caseid;
                    //var url = "/portal/resource/app/administrativo/ResultadoINVP/content/?idbanner="+data.data[0].idbanner+"&idsesion="+$scope.properties.sesiones;
                    //window.location.href= url
                    rebuildURL(data.data[0].caseid, data.data[0].idbanner);

                }
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {
            });
    }

    function rebuildURL(_caseid, _idbanner) {
        if (_caseid && _idbanner) {
            let urlParts = window.location.href.split("?");
            let paramsArray = urlParts[1].split("&");
            let idbannerParts = paramsArray[0].split("=");
            let caseidParts = paramsArray[2].split("=");

            let newUrl = urlParts[0] + "?" + (idbannerParts[0] + "=" + _idbanner) + "&" + paramsArray[1] + "&" + (caseidParts[0] + "=" + _caseid);
            window.location.href = newUrl;
        }
    }
}