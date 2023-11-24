function PbInputCtrl($scope, $log, widgetNameFactory) {

  'use strict';

  this.name = widgetNameFactory.getName('pbInput');

  if (!$scope.properties.isBound('value')) {
    $log.error('the pbInput property named "value" needs to be bound to a variable');
  }

  // Agregar una función para convertir el valor a minúsculas
  $scope.convertToLowerCase = function() {
    if ($scope.properties.value) {
      $scope.properties.value = $scope.properties.value.toLowerCase();
    }
  };
}