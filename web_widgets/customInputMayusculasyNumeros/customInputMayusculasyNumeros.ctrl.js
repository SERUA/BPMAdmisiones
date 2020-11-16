function PbInputCtrl($scope, $log, widgetNameFactory) {

  'use strict';

  this.name = widgetNameFactory.getName('pbInput');

  if (!$scope.properties.isBound('value')) {
    $log.error('the pbInput property named "value" need to be bound to a variable');
  }
  
  
   $scope.forceKeyPressUppercase= function(e)
  {
      
    var charInput = e.keyCode;
    var limite = $scope.properties.maxLength === 1?250:$scope.properties.maxLength;
    if(((charInput >= 48)&& (charInput <=57)||(charInput >= 97) && (charInput <= 122)||(charInput >= 65) && (charInput <= 90))&&(e.target.value.length) <limite){
       if((charInput >= 97) && (charInput <= 122)) { // lowercase
        if(!e.ctrlKey && !e.metaKey && !e.altKey) { // no modifier key
            var newChar = charInput - 32;
            var start = e.target.selectionStart;
            var end = e.target.selectionEnd;
            e.target.value = e.target.value.substring(0, start) + String.fromCharCode(newChar) + e.target.value.substring(end);
            e.target.setSelectionRange(start+1, start+1);
            e.preventDefault();
       }
      }  
    }else{
        var start = e.target.selectionStart;
        var end = e.target.selectionEnd;
        e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
        e.target.setSelectionRange(start+1, start+1);
         e.preventDefault();
    }
  }


  
}
