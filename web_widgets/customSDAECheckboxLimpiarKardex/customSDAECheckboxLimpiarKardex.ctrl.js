function PbCheckboxCtrl($scope, $log, widgetNameFactory) {
    $scope.$watch('properties.value', function(value) {
        if (value === 'true' || value === true) {
            $scope.properties.value = true;
        } else {
            $scope.properties.value = false;
            clearKardex();
        }
    });

    this.name = widgetNameFactory.getName('pbCheckbox');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbCheckbox property named "value" need to be bound to a variable');
    }
  
    function clearKardex(){
        debugger;
        $scope.properties.urlAzure = "";
        
        let obj = {
            "linkSource": "",
            "fileName":  "",
            "extension": ""
        };
        
        $scope.properties.selectedFile = obj;
        $scope.properties.nuevoPomedio = null;
    }
}