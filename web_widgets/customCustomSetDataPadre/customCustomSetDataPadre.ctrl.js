function($scope, $http) {

    $scope.$watch("properties.reload", function() {
        if ($scope.properties.reload === undefined) {
            $scope.properties.datosPadres = {
                "catTitulo": null,
                "nombre": "",
                "apellidos": "",
                "correoElectronico": "",
                "catEscolaridad": null,
                "catEgresoAnahuac": null,
                "catCampusEgreso": null,
                "catTrabaja": null,
                "empresaTrabaja": "",
                "giroEmpresa": "",
                "puesto": "",
                "isTutor": false,
                "vive": null,
                "calle": "",
                "catPais": null,
                "numeroExterior": "",
                "numeroInterior": "",
                "catEstado": null,
                "ciudad": "",
                "colonia": "",
                "telefono": "",
                "codigoPostal": "",
                "viveContigo": false,
                "desconozcoDatosPadres": false,
                "delegacionMunicipio": "",
                "estadoExtranjero": ""
            };
        } else {
            $scope.properties.datosTutor = $scope.properties.reload;
        }
    });
}