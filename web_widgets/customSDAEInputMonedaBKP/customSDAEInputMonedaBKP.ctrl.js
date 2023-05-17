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
        
        // document.getElementById(main_id).onkeydown = function (event) {
        //     if (event.which == 8 || event.which == 46) { 
        //         event.preventDefault();
        //     } 
        // };  
    }
    
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
        } else {
            e.preventDefault();
            blockVar = true;
            let model = $scope.properties.value ? $scope.properties.value : "0";
            
            if(model == 0 && e.key == 0){
                $scope.properties.value = 0;
            } else {
                $scope.properties.value = parseInt(model + e.key);
            }
            
        } 
    }
    
    $scope.$watch("properties.value", ()=>{
        // if($scope.properties.value == 0){
        //     debugger; 
        // }
        
        if($scope.properties.value || $scope.properties.value == 0){
            let valueString = $scope.properties.value  + "";
            if(document.getElementById(main_id)){
                document.getElementById(main_id).value = parseFloat(valueString.replace(/,/g, ""))
                .toFixed(2)
                .toString()
                .replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            } else {
                setTimeout(()=>{
                     document.getElementById(main_id).value = parseFloat(valueString.replace(/,/g, ""))
                        .toFixed(2)
                        .toString()
                        .replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                }, 1000);
            }
           
        }
    });
}