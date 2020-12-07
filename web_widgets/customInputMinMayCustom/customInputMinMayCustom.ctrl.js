function PbInputCtrl($scope, $log, widgetNameFactory) {

  'use strict';

  this.name = widgetNameFactory.getName('pbInput');
  console.log($scope.properties.idInput)
  if (!$scope.properties.isBound('value')) {
    $log.error('the pbInput property named "value" need to be bound to a variable');
  }
  
   $scope.forceKeyPressUppercase=function(e)
  {
      
    var charInput = e.keyCode;
    var limite = $scope.properties.maxLength === 1?250:$scope.properties.maxLength;
    if((charInput < 48) || (charInput > 57)&&(e.target.value.length) <limite){
    }else{
        var start = e.target.selectionStart;
        var end = e.target.selectionEnd;
        e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
        e.target.setSelectionRange(start+1, start+1);
        e.preventDefault();
    }
     
    
    

  }

  //document.getElementById(""+$scope.properties.idInput.toString()).addEventListener("keypress", forceKeyPressUppercase, false);
}
