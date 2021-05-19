function PbInputCtrl($scope,$http, $log, widgetNameFactory,blockUI) {

  'use strict';

  this.name = widgetNameFactory.getName('pbInput');

  if (!$scope.properties.isBound('value')) {
    $log.error('the pbInput property named "value" need to be bound to a variable');
  }
  
  $scope.$watch("properties.value", function (newValue, oldValue) {
        if (newValue !== undefined) {
            if(newValue.length === 8  ){
                doRequest("GET", $scope.properties.urlGet);
            }
        }
        console.log($scope.properties.value);
    });
    
    
    
    function doRequest(method, url) {
        blockUI.start();
        let urlGet = angular.copy(url)+($scope.properties.value)
        var req = {
            method: method,
            url: urlGet
        };
        return $http(req)
            .success(function (data, status) {
                if(data.length < 1){
                    swal(`¡No se ha encontra un aspirante con ese id banner!`,"","warning")
                }else{
                    $scope.properties.valorRetorno = data[0].nombre;
                }
                console.log(data.data)
            })
            .error(function (data, status) {
                
            })
            .finally(function () {
                
                blockUI.stop();
            });
    }
    
    
    
    $scope.forceKeyPressUppercase=function(e) {
        var charInput = e.keyCode;
        var limite = $scope.properties.maxLength === 1?250:$scope.properties.maxLength;
        if((e.target.value.length) <limite){
        }else{
            var start = e.target.selectionStart;
            var end = e.target.selectionEnd;
            e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
            e.target.setSelectionRange(start+1, start+1);
            e.preventDefault();
        }
    }
    
    
}
