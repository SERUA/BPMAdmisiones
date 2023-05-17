function PbInputCtrl($scope, $log, widgetNameFactory) {

    var blockVar;
 
    this.name = widgetNameFactory.getName('pbInput');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbInput property named "value" need to be bound to a variable');
    }
    
    $scope.forceKeyPressUppercase = function (e) {
        var charInput = e.keyCode;
        var content = e.key;
        var limite = $scope.properties.maxLength === 1 ? 10 : $scope.properties.maxLength;
        let valid = true;
        let isNumber = false;
        let fullNumber = 0;
        
        if((e.keyCode > 47 && e.keyCode < 59)){
            isNumber = true;
            valid = true;
            fullNumber = parseInt(e.target.value + content);
        } else{
            valid = false;
        }
        
        let valueSize = 0;
        if($scope.properties.value){
            let auxValue = $scope.properties.value + "";
            valueSize = auxValue.length;
        }
        
        if(!isNumber){
            e.preventDefault();
        } else if ((valueSize) >= limite || !valid || (fullNumber > $scope.properties.max)) {
            e.preventDefault();
        }
    }
    
}