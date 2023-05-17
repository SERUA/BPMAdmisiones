function PbInputCtrl($scope, $log, widgetNameFactory) {

    var blockVar;
    $scope.clean = function(){
        $scope.properties.value = null;
        document.getElementById(main_id).value = '';
    }
    
    $scope.id = Math.random();
    var main_id = angular.copy($scope.id) + ''; 
    
    window.onload = function(){
        document.getElementById(main_id).onblur = function (){    
            this.value = parseFloat(this.value.replace(/,/g, ""))
            .toFixed(2)
            .toString()
            .replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
    }
    
    this.name = widgetNameFactory.getName('pbInput');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbInput property named "value" need to be bound to a variable');
    }
    
    function setModel(_value){
        $scope.properties.value = parseInt(_value.replace(/,/g, ""));
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
        
        if(!isNumber){
            e.preventDefault();
        } else if ((e.target.value.length) >= limite || !valid || (fullNumber > $scope.properties.max)) {
            e.preventDefault();
        } else {
            debugger;
            blockVar = true;
            let model = $scope.properties.value ? $scope.properties.value : ""
            setModel(model + e.key);
        } 
    }
    
    $scope.$watch("properties.value", ()=>{
        if($scope.properties.value){
            debugger;
            if($scope.properties.value != document.getElementById(main_id).value && !blockVar){
                let valueString = $scope.properties.value  + "";
                document.getElementById(main_id).value = parseFloat(valueString.replace(/,/g, ""))
                    .toFixed(2)
                    .toString()
                    .replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            }
        }
    });
}