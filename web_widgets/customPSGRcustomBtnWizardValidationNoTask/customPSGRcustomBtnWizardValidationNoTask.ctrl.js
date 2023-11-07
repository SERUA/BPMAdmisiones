function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
    $scope.action = function() {
        debugger;
        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
            $scope.properties.selectedIndex--;
        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
            if ($scope.properties.isValidStep) {
                debugger;
                $scope.properties.selectedIndex++;
            } else {
                swal($scope.properties.messageTitle, $scope.properties.errorMessage, "warning");
            }
        }
    }
}