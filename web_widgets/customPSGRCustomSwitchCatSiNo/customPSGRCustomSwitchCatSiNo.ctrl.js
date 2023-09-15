function PbCheckboxCtrl($scope, $log, widgetNameFactory) {

    $scope.resetValues = function(_name){
        if(_name === "sino"){
            if($scope.properties.value.esSiNo){
                $scope.properties.value.esTalvez = false;
                $scope.properties.value.esOtro = false;
            }
        } else if(_name === "talvez"){
            if($scope.properties.value.esTalvez){
                $scope.properties.value.esSiNo = false;
                $scope.properties.value.esOtro = false;
            }
            
        } else if(_name === "otro"){
            if($scope.properties.value.esOtro){
                $scope.properties.value.esSiNo = false;
                $scope.properties.value.esTalvez = false;
            }
        }
    }
}