function ($scope, $log, widgetNameFactory) {

    'use strict';

    this.name = widgetNameFactory.getName('pbInput');
    //console.log($scope.properties.idInput)
    if (!$scope.properties.isBound('value')) {
        $log.error('the pbInput property named "value" need to be bound to a variable');
    }

    $scope.forceKeyPressUppercase = function (e) {
        var charInput = e.keyCode;
        var content = e.key;
        var limite = $scope.properties.maxLength === 1 ? 3 : $scope.properties.maxLength;
        let valid = true;
        
        if($scope.properties.type === "number"){
            if((e.keyCode > 47 && e.keyCode < 59)){
                valid = true;
            } else{
                valid = false;
            }
        } else {
            valid = true;
        }
        
        let fullNumber = parseInt(e.target.value + content);
        if ((e.target.value.length) >= limite || !valid || (fullNumber > $scope.properties.max)) {
            e.preventDefault();  
        } 
        
    }

    //document.getElementById(""+$scope.properties.idInput.toString()).addEventListener("keypress", forceKeyPressUppercase, false);
}