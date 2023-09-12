function customSetValueDetallesSolicitud($scope, $http) {
    $scope.$watch("properties.formOutput", function() {
        if ($scope.properties.formOutput !== undefined && $scope.properties.objDetalleSolicitud !== undefined && $scope.properties.urlTipoAlumno !== undefined && $scope.properties.urlResidencia !== undefined && $scope.properties.urlTipoAdmision !== undefined) {
            
            let tienedescuento = true;
            $scope.properties.isCurpValidado = $scope.properties.objDetalleSolicitud.isCurpValidado;
            $scope.properties.descuento = $scope.properties.objDetalleSolicitud.descuento;
            debugger;
            if ($scope.properties.descuento !== null && $scope.properties.descuento !== undefined && $scope.properties.descuento !== "" && $scope.properties.descuento > 0) {
                $scope.properties.descuentoManual = true;
            } else {
                $scope.properties.descuentoManual = false;
            }
            $scope.properties.formOutput.detalleSolicitudInput.observacionesDescuento = $scope.properties.objDetalleSolicitud.observacionesDescuento;
            if ($scope.properties.objDetalleSolicitud.promedioCoincide !== null) {
                $scope.properties.formOutput.detalleSolicitudInput.promedioCoincide = $scope.properties.objDetalleSolicitud.promedioCoincide;
            }
            if ($scope.properties.objDetalleSolicitud.cbCoincide !== null) {
                $scope.properties.formOutput.detalleSolicitudInput.cbCoincide = $scope.properties.objDetalleSolicitud.cbCoincide;
            }
            if ($scope.properties.objDetalleSolicitud.revisado !== null) {
                $scope.properties.formOutput.detalleSolicitudInput.revisado = $scope.properties.objDetalleSolicitud.revisado;
            }
            $scope.properties.formOutput.detalleSolicitudInput.idBanner = $scope.properties.objDetalleSolicitud.idBanner;
            if ($scope.properties.objDetalleSolicitud.catDescuentos !== null) {
                for (var x = 0; x < $scope.properties.lstDescuentoCustom.length; x++) {
                    if ($scope.properties.lstDescuentoCustom[x].persistenceid === $scope.properties.objDetalleSolicitud.catDescuentos.persistenceId_string) {
                        $scope.properties.objCampanaSelected = $scope.properties.lstDescuentoCustom[x];
                        tienedescuento = false;
                    }
                }
            }

            if (tienedescuento) {
                for (var y = 0; y < $scope.properties.lstDescuentoCustom.length; y++) {
                    if ($scope.properties.lstDescuentoCustom[y].campana === "Colaboración") {
                        $scope.properties.objCampanaSelected = $scope.properties.lstDescuentoCustom[y];
                        $scope.properties.descuento = $scope.properties.objDetalleSolicitud.descuento;
                    }
                }
            }

            $scope.gettipoAlumno();
        }
    });

    $scope.gettipoAlumno = function() {
        var req = {
            method: "GET",
            url: ".." + $scope.properties.urlTipoAlumno
        };
        return $http(req)
            .success(function(data, status) {

                $scope.properties.formOutput.detalleSolicitudInput.catTipoAlumno = data;
                $scope.getResidencia();
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    $scope.getResidencia = function() {
        var req = {
            method: "GET",
            url: ".." + $scope.properties.urlResidencia
        };
        return $http(req)
            .success(function(data, status) {

                $scope.properties.formOutput.detalleSolicitudInput.catResidencia = data;
                $scope.gettipoAdmision();
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    $scope.gettipoAdmision = function() {
        var req = {
            method: "GET",
            url: ".." + $scope.properties.urlTipoAdmision
        };
        return $http(req)
            .success(function(data, status) {

                $scope.properties.formOutput.detalleSolicitudInput.catTipoAdmision = data;
                $scope.properties.tieneDatos = true;
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

}