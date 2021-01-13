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
        $log.error('the pbAutocomplete property named "value" need to be bound to a variable');
    }

    $scope.testblur = function() {
        debugger;
        $scope.properties.urlCollageBoard = "";
        $scope.properties.isPDFCollageBoard = "true";
        $scope.properties.isImagenCollage = "true";
        $scope.properties.collageBoard = "";

        var otro = true;
        for (var x = 0; x < $scope.properties.availableValues.length; x++) {
            if ($scope.properties.value === $scope.properties.availableValues[x].descripcion) {
                otro = false;
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
    }
    
    $scope.$watch("properties.catSolicitudDeAdmision.catBachilleratos.descripcion", function(){
        debugger;
        if(!$scope.llenoEntero){
            if($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion !== null || $scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion !== undefined){
                $scope.properties.value = $scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion;
                if($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion === "Otro"){
                    /*$scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.value;*/
                    $scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.catSolicitudDeAdmision.bachillerato;
                    $scope.properties.datosPreparatoria.paisBachillerato = $scope.properties.catSolicitudDeAdmision.paisBachillerato;
                    $scope.properties.datosPreparatoria.estadoBachillerato = $scope.properties.catSolicitudDeAdmision.estadoBachillerato;
                    $scope.properties.datosPreparatoria.ciudadBachillerato = $scope.properties.catSolicitudDeAdmision.ciudadBachillerato;
                    $scope.llenoEntero = true;
                }else{
                    $scope.llenoEntero = true;
                    //$scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.value;
                    $scope.properties.datosPreparatoria.paisBachillerato = $scope.properties.catSolicitudDeAdmision.catBachilleratos.pais;
                    $scope.properties.datosPreparatoria.estadoBachillerato = $scope.properties.catSolicitudDeAdmision.catBachilleratos.estado;
                    $scope.properties.datosPreparatoria.ciudadBachillerato = $scope.properties.catSolicitudDeAdmision.catBachilleratos.ciudad;   
                }
            }
            //$scope.properties.gestionEscolar = $scope.properties.catSolicitudDeAdmision.catGestionEscolar;
        }
    });
}