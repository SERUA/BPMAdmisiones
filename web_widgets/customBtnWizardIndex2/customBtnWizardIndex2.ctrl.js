function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    $scope.action = function(){
        if($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0){
            $scope.properties.selectedIndex --; 
        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)){
            $scope.properties.selectedIndex ++; 
        }
    }
}
