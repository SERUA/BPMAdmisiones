/**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function($scope, blockUI) {
    var white = 'white';

    // add a new variable in AngularJS scope. It'll be usable in the template directly with {{ backgroudColor }} 
    $scope.backgroudColor = white;

    // define a function to be used in template with ctrl.toggleBackgroundColor()
    this.toggleBackgroundColor = function() {
        if ($scope.backgroudColor === white) {
            // use the custom widget property backgroudColor with $scope.properties.backgroudColor
            $scope.backgroudColor = $scope.properties.background;
        } else {
            $scope.backgroudColor = white;
        }
    };
    $scope.$watch("properties.catSolicitudDeAdmision", function() {
        console.log("Datos del solicitante");
        if ($scope.properties.catSolicitudDeAdmision !== undefined && $scope.properties.catSolicitudDeAdmision !== null) {
            if ($scope.properties.getCatRegistro !== undefined) {
                if ($scope.properties.getCatRegistro.length > 0) {
                    if ($scope.properties.isDataRegistro === undefined || $scope.properties.isDataRegistro === null || $scope.properties.isDataRegistro === "") {
                        $scope.properties.isDataRegistro = "Lleno";
                        /*$scope.properties.catSolicitudDeAdmision.primerNombre = $scope.properties.getCatRegistro[0].primernombre;
                        $scope.properties.catSolicitudDeAdmision.segundoNombre = $scope.properties.getCatRegistro[0].segundonombre;
                        $scope.properties.catSolicitudDeAdmision.apellidoPaterno = $scope.properties.getCatRegistro[0].apellidopaterno;
                        $scope.properties.catSolicitudDeAdmision.apellidoMaterno = $scope.properties.getCatRegistro[0].apellidomaterno;*/
                        //$scope.properties.catSolicitudDeAdmision.catCampus = $scope.properties.getCatRegistro[0].catCampus;
                        //$scope.properties.catSolicitudDeAdmision.catGestionEscolar = $scope.properties.getCatRegistro[0].catGestionEscolar;
                        $scope.properties.catSolicitudDeAdmision.correoElectronico = $scope.properties.getCatRegistro[0].correoelectronico;
                        $scope.properties.caseId = $scope.properties.getCatRegistro[0].caseId;

                        if ($scope.properties.catSolicitudDeAdmision.selectedIndex !== null) {
                            $scope.properties.selectedIndex = $scope.properties.catSolicitudDeAdmision.selectedIndex;
                        } else {
                            $scope.properties.selectedIndex = 1;
                        }

                        if ($scope.properties.catSolicitudDeAdmision.selectedIndexPersonal !== null) {
                            $scope.properties.selectedIndexPersonal = $scope.properties.catSolicitudDeAdmision.selectedIndexPersonal;
                        } else {
                            $scope.properties.selectedIndexPersonal = 0;
                        }

                        if ($scope.properties.catSolicitudDeAdmision.selectedIndexFamiliar !== null) {
                            $scope.properties.selectedIndexFamiliar = $scope.properties.catSolicitudDeAdmision.selectedIndexFamiliar;
                        } else {
                            $scope.properties.selectedIndexFamiliar = 0;
                        }

                        if ($scope.properties.catSolicitudDeAdmision.selectedIndexRevision !== null) {
                            $scope.properties.selectedIndexRevision = $scope.properties.catSolicitudDeAdmision.selectedIndexRevision;
                        } else {
                            $scope.properties.selectedIndexRevision = 0;
                        }
                        if($scope.properties.catSolicitudDeAdmision.catBachilleratos !== null){
                           $scope.properties.Bachilleratopersistenceid = $scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceId_string;
                           if($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion !== "Otro"){
                               $scope.properties.preparatoriaSeleccionada = $scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion;
                               $scope.properties.datosPreparatoria.paisBachillerato = $scope.properties.catSolicitudDeAdmision.catBachilleratos.pais;
                               $scope.properties.datosPreparatoria.estadoBachillerato = $scope.properties.catSolicitudDeAdmision.catBachilleratos.estado;
                               $scope.properties.datosPreparatoria.ciudadBachillerato = $scope.properties.catSolicitudDeAdmision.catBachilleratos.ciudad;
                           }else{
                               $scope.properties.preparatoriaSeleccionada = $scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion;
                               $scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.catSolicitudDeAdmision.bachillerato;
                               $scope.properties.datosPreparatoria.paisBachillerato = $scope.properties.catSolicitudDeAdmision.paisBachillerato;
                               $scope.properties.datosPreparatoria.estadoBachillerato = $scope.properties.catSolicitudDeAdmision.estadoBachillerato;
                               $scope.properties.datosPreparatoria.ciudadBachillerato = $scope.properties.catSolicitudDeAdmision.ciudadBachillerato;
                           }
                           
                        }
                        if($scope.properties.catSolicitudDeAdmision.catNacionalidad !== null){
                            if($scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana"){
                                $scope.properties.idExtranjero = $scope.properties.catSolicitudDeAdmision.curp;
                            }
                        }
                        $scope.properties.showbuttons = false;
                        blockUI.stop();
                    }
                }
            }
        }
    });
}