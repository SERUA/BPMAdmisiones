function PbCheckboxCtrl($scope, $log, widgetNameFactory) {

    // $scope.$watch('properties.value', function (value) {
    //     if (value === 'true' || value === true) {
    //         $scope.properties.value = true;
    //     } else {
    //         $scope.properties.value = false;
    //     }
    // });
    
    $scope.value = null;
    
    $scope.setValue = function(_value, _clave){
        if(_clave == "si"){
            $scope.value = true;
        } else if (_clave == "no"){
            $scope.value = false;
        }
    }
    
    this.name = widgetNameFactory.getName('pbCheckbox');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbCheckbox property named "value" need to be bound to a variable');
    }
    
    $scope.$watch("properties.value", function(){
        if($scope.properties.value){
             $scope.setValue($scope.value, $scope.properties.value.clave);
        }
    });
}