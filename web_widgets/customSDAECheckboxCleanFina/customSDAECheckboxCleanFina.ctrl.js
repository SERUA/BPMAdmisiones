function PbCheckboxCtrl($scope, $log, widgetNameFactory) {

    $scope.$watch('properties.value', function(value) {
        if (value === 'true' || value === true) {
            $scope.properties.value = true;
        } else {
            $scope.properties.value = false;
            $scope.properties.procentajeFinanciamiento = "";
        }
    });

    this.name = widgetNameFactory.getName('SDAECheckboxCleanFina');

    if (!$scope.properties.isBound('value')) {
        $log.error('the SDAECheckboxCleanFina property named "value" need to be bound to a variable');
    }
}
