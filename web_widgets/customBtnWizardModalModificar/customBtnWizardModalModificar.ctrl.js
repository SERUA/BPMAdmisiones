function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    $scope.action = function() {
        if ($scope.properties.selectedIndex === 0) {
        } else if ($scope.properties.selectedIndex === 1) {
            console.log("Modal continuar");
            if ($scope.properties.catSolicitudDeAdmision.catCampus.persistenceId_string === "") {
                swal("Campus!", "Debe seleccionar un campus donde cursara tus estudios!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLicenciatura === null) {
                swal("Licenciatura!", "Debe seleccionar una licenciatura!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                swal("Periodo!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null || $scope.properties.catSolicitudDeAdmision.ciudadExamen === "") {
                        swal("Lugar de examen!", "Debe seleccionar un estado y una ciudad donde realizara el examen!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            closeModal($scope.properties.modalid);
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                    if ($scope.properties.catSolicitudDeAdmision.catPaisExamen === null || $scope.properties.catSolicitudDeAdmision.ciudadExamen === "") {
                        swal("Lugar de examen!", "Debe seleccionar un pais y una ciudad donde realizara el examen!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            closeModal($scope.properties.modalid);
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else {
                    $scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                    $scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        closeModal($scope.properties.modalid);
                        $scope.properties.selectedIndex++;
                    }
                }

            } else {
                swal("Lugar de examen!", "Debe seleccionar un lugar donde realizara el examen!", "warning");
            }


        } else if ($scope.properties.selectedIndex === 2) {
            
        } else if ($scope.properties.selectedIndex === 3) {

        } else if ($scope.properties.selectedIndex === 4) {
            
        }

    }
    
    function openModal(modalid) {

        modalService.open(modalid);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }
}