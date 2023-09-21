function ($scope, $log, widgetNameFactory) {

    'use strict';

    this.name = widgetNameFactory.getName('pbInput');
    if (!$scope.properties.isBound('value')) {
        $log.error('the pbInput property named "value" need to be bound to a variable');
    }

    $scope.forceKeyPressUppercase = function (e) {
        var charInput = e.keyCode;
        var content = e.key;
        var limite = $scope.properties.maxLength === 1 ? 50 : $scope.properties.maxLength;
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
        
        if($scope.properties.type === "number"){
            let fullNumber = parseInt(e.target.value + content);
            if ((e.target.value.length) >= limite || !valid || (fullNumber > $scope.properties.max)) {
                e.preventDefault();  
            } 
        } else {
            if ((e.target.value.length) >= limite || !valid) {
                let start = e.target.selectionStart;
                let end = e.target.selectionEnd;
                e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
                e.target.setSelectionRange(start + 1, start + 1);
                e.preventDefault();  
            } 
        }
        
        // if ((e.target.value.length) >= limite || !valid) {
        //     var start = e.target.selectionStart;
        //     var end = e.target.selectionEnd;
        //     e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
        //     e.target.setSelectionRange(start + 1, start + 1);
        //     e.preventDefault();  
        // } 
    }

    //document.getElementById(""+$scope.properties.idInput.toString()).addEventListener("keypress", forceKeyPressUppercase, false);
}