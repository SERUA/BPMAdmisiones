function PbInputCtrl($scope, $log, widgetNameFactory) {

  'use strict';

  this.name = widgetNameFactory.getName('pbInput');

  if (!$scope.properties.isBound('value')) {
    $log.error('the pbInput property named "value" need to be bound to a variable');
  }
  
  $scope.cambioCheck = function(){
            $scope.properties.variableCheck = !$scope.properties.variableCheck;
    }

}
