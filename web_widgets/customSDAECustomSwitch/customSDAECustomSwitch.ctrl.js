function PbCheckboxCtrl($scope, $log, widgetNameFactory) {

    $scope.resetValues = function(_name){
        if(_name === "isArtistica"){
            if($scope.properties.value.isArtistica){
                $scope.properties.value.isDeportiva = false;
                $scope.properties.value.isAcademica = false;
            }
        } else if(_name === "isDeportiva"){
            if($scope.properties.value.isDeportiva){
                $scope.properties.value.isArtistica = false;
                $scope.properties.value.isAcademica = false;
            }
            
        } else if(_name === "isAcademica"){
            if($scope.properties.value.isAcademica){
                $scope.properties.value.isDeportiva = false;
                $scope.properties.value.isArtistica = false;
            }
        }
    }
}