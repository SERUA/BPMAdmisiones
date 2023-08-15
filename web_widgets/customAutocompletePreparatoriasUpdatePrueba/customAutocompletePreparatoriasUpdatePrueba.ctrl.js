function PbAutocompleteCtrl($scope, $parse, $log, widgetNameFactory) {
    'use strict';

    function createGetter(accessor) {
        return accessor && $parse(accessor);
    }

    this.getLabel = createGetter($scope.properties.displayedKey) || function(item) {
        return typeof item === 'string' ? item : JSON.stringify(item);
    };

    this.name = widgetNameFactory.getName('pbAutocomplete');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbAutocomplete property named "value" needs to be bound to a variable');
    }

    $scope.testblur = function() {
        $scope.properties.urlCollageBoard = "";
        $scope.properties.isPDFCollageBoard = "true";
        $scope.properties.isImagenCollage = "true";
        $scope.properties.collageBoard = "";

        setTimeout(function() {
            var otro = true;

            $scope.properties.urlCollageBoard = "";
            $scope.properties.isPDFCollageBoard = "true";
            $scope.properties.isImagenCollage = "true";
            $scope.properties.collageBoard = "";

            for (var x = 0; x < $scope.properties.availableValues.length; x++) {
                if ($scope.properties.value === $scope.properties.availableValues[x].descripcion) {
                    otro = false;
                    if ($scope.properties.availableValues[x].descripcion === "Otro") {
                        $scope.properties.datosPreparatoria.nombreBachillerato = null;
                        $scope.properties.datosPreparatoria.paisBachillerato = null;
                        $scope.properties.datosPreparatoria.estadoBachillerato = null;
                        $scope.properties.datosPreparatoria.ciudadBachillerato = null;
                        $scope.properties.value = "Otro";
                    }
                    break;
                }
            }
            if (otro) {
                $scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.value;
                $scope.properties.datosPreparatoria.paisBachillerato = "";
                $scope.properties.datosPreparatoria.estadoBachillerato = "";
                $scope.properties.datosPreparatoria.ciudadBachillerato = "";
                $scope.properties.value = "Otro";
            }
        }, 500);
    }

    $scope.$watch("properties.objSolicitudDeAdmision.catBachilleratos.descripcion", function() {
        if (!$scope.llenoEntero) {
            if ($scope.properties.objSolicitudDeAdmision.catBachilleratos.descripcion !== null || $scope.properties.objSolicitudDeAdmision.catBachilleratos.descripcion !== undefined) {
                $scope.properties.value = $scope.properties.objSolicitudDeAdmision.catBachilleratos.descripcion;
                if ($scope.properties.objSolicitudDeAdmision.catBachilleratos.descripcion === "Otro") {
                    $scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.objSolicitudDeAdmision.bachillerato;
                    $scope.properties.datosPreparatoria.paisBachillerato = $scope.properties.objSolicitudDeAdmision.paisBachillerato;
                    $scope.properties.datosPreparatoria.estadoBachillerato = $scope.properties.objSolicitudDeAdmision.estadoBachillerato;
                    $scope.properties.datosPreparatoria.ciudadBachillerato = $scope.properties.objSolicitudDeAdmision.ciudadBachillerato;
                    $scope.llenoEntero = true;
                } else {
                    $scope.llenoEntero = true;
                    //$scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.value;
                    $scope.properties.datosPreparatoria.paisBachillerato = $scope.properties.objSolicitudDeAdmision.catBachilleratos.pais;
                    $scope.properties.datosPreparatoria.estadoBachillerato = $scope.properties.objSolicitudDeAdmision.catBachilleratos.estado;
                    $scope.properties.datosPreparatoria.ciudadBachillerato = $scope.properties.objSolicitudDeAdmision.catBachilleratos.ciudad;
                }
            }
            //$scope.properties.gestionEscolar = $scope.properties.objSolicitudDeAdmision.catGestionEscolar;
        }
    });
  }