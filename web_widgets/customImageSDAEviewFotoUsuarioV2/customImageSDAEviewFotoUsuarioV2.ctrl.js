function PbButtonCtrl($scope, $http, blockUI) {
    var vm = this;
    $scope.lstDatos = null;
    /*$scope.$watchCollection('properties.correoUsuario', function(items) {
        if ($scope.properties.correoUsuario != undefined) {
            $scope.getDataImagenUsuario();
        }
    });*/
    function doRequest(method, url, params, dataToSend, extra, callback) {
        vm.busy = true;
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };
        return $http(req)
            .success(function(data, status) {
                callback(data, extra)
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {
                vm.busy = false;
                blockUI.stop();
            });
    }
    $scope.getDataImagenUsuario = function() {
        $scope.dataToSend = {
            "tarea": "Validar Información",
            "lstFiltro": [{ "columna": "EMAIL", "operador": "Igual a", "valor": $scope.properties.correoUsuario.userName }],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 20,
            "offset": 0,
            "usuario": "Administrador"
        }

        doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", null, $scope.dataToSend, null, function(datos, extra) {
            $scope.lstDatos = datos.data;
        })
    }
}