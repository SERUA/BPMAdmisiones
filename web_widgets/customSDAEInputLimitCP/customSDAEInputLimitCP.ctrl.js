function ($scope, $log, widgetNameFactory, $http) {

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
    }
    
    $scope.$watch("properties.value", ()=>{
        if($scope.properties.value){
            if($scope.properties.value.length === 5){
                getCP($scope.properties.value);
            }
        }
    });
    
    function getCP(cp) {
        var req = {
            method: "GET",
            url: '/API/bdm/businessData/com.anahuac.catalogos.CatCodigoPostal?q=findByCodigoPostal&p=0&c=1000&f=codigoPostal=' + cp
        };
  
        return $http(req).success(function(data, status) {
            $scope.properties.lstCP = data;
        })
        .error(function(data, status) {
            console.error(data);
        })
        .finally(function() {

        });
    }
}