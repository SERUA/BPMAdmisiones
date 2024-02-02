function PbChecklistCtrl($scope, $parse, widgetNameFactory, $log) {

  'use strict';
  var ctrl = this;

  /**
   * Watch the data source and set wrapChoices and $scope.properties.selectedValues
   */
  function comparator(item, initialValue) {
    // Se espera utilizar esta funcion para comparar dos objetos que representen el registro de un PSGRCatPeriodo.
    if (item.persistenceId && initialValue.persistenceId) {
      return item.persistenceId === initialValue.persistenceId;
    }
    return false;
  }

  function createGetter(accessor) {
    return accessor && $parse(accessor);
  }

  this.getLabel = createGetter($scope.properties.displayedKey) || function (item) {
      return typeof item === 'string' ? item : JSON.stringify(item);
    };

  this.getValue = createGetter($scope.properties.returnedKey) || function (item) {
      return item;
    };


  /**
   * update the scope.properties.selectedValues with the selected items
   */
  this.updateValues = function () {
    debugger
    $scope.properties.selectedValues = ctrl.selectedItems
      .map(function (checked, index) {
        if (checked !== true) {
          return false;
        }
        var item = $scope.properties.availableValues[index];
        return ctrl.getValue(item);
      }).filter(function (item) {
        return item !== false;
      });
  };

  function updateSelectedValues() {
    console.log("entre en updateSelectedValues()")
    console.log("availableValues:")
    console.log($scope.properties.availableValues)
    console.log("selectedValues:")
    console.log($scope.properties.selectedValues)
    
    ctrl.selectedItems = ($scope.properties.availableValues || []).map(function (item) {
      if (Array.isArray($scope.properties.selectedValues)) {
        return $scope.properties.selectedValues.some(comparator.bind(null, ctrl.getValue(item)));
      }
      return false;
    });
  }

  $scope.$watchCollection('properties.availableValues', updateSelectedValues);
  $scope.$watchCollection('properties.selectedValues', updateSelectedValues);

  this.name = widgetNameFactory.getName('pbChecklist');

  if (!$scope.properties.isBound('selectedValues')) {
    $log.error('the pbCheckList property named "selectedValues" need to be bound to a variable');
  }
}
