function PbInputCtrl($scope, $log, widgetNameFactory) {

  'use strict';

  this.name = widgetNameFactory.getName('pbInput');

  if (!$scope.properties.isBound('value')) {
    $log.error('the pbInput property named "value" need to be bound to a variable');
  }
  
    $scope.forceKeyPressUppercase = function(e)
  {
    var charInput = e.keyCode;
    var limite = $scope.properties.maxLength === 1?250:$scope.properties.maxLength;
    if((charInput >=48) && (charInput <=57)&&(e.target.value.length) <limite){

    }else{
        var start = e.target.selectionStart;
        var end = e.target.selectionEnd;
        e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
        e.target.setSelectionRange(start+1, start+1);
         e.preventDefault();
    }
     
    $scope.$watch("properties.value",function(){
        debugger;
        if($scope.properties.value >= $scope.properties.max){
            $scope.properties.value = null;
            $scope.properties.value = $scope.properties.max;
        }
        if($scope.properties.value % 1 != 0){
            $scope.properties.value = parseFloat($scope.properties.value).toFixed(0);
            $scope.properties.value = parseFloat($scope.properties.value);
            $scope.$apply();
        }
    });
    

  }
}
