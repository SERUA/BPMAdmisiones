function PbSelectCtrl($scope, $parse, $log, widgetNameFactory, $timeout, $window, $element) {
    var ctrl = this;
    var conteo = 0;

    function comparator(initialValue, item) {
        return angular.equals(initialValue, ctrl.getValue(item));
    }

    function createGetter(accessor) {
        return accessor && $parse(accessor);
    }

    this.getLabel = createGetter($scope.properties.displayedKey) || function(item) {
        return typeof item === 'string' ? item : JSON.stringify(item);
    };

    this.getValue = createGetter($scope.properties.returnedKey) || function(item) {
        return item;
    };

    this.findSelectedItem = function(items) {
        return items.filter(comparator.bind(null, $scope.properties.value))
            .map(function(item) {
                return ctrl.getValue(item);
            })[0];
    };

    this.setSelectedValue = function(foundItem) {
        $timeout(function() {
            $scope.properties.value = angular.isDefined(foundItem) ? foundItem : null;

        }, 0);
        cambioDeDatos($scope.properties.value);
    };


    $scope.$watchCollection('properties.availableValues', function(items) {
        if (Array.isArray(items)) {
            var foundItem = ctrl.findSelectedItem(items);

            //force IE9 to rerender option list
            if ($window.navigator && $window.navigator.userAgent && $window.navigator.userAgent.indexOf('MSIE 9') >= 0) {
                var option = document.createElement('option');
                var select = $element.find('select')[0];
                select.add(option, null);
                select.remove(select.options.length - 1);
            }

            // terrible hack to force the select ui to show the correct options
            // so we change it's value to undefined and then delay to the correct value
            $scope.properties.value = undefined;
            ctrl.setSelectedValue(foundItem);
        }
    });

    $scope.$watch('properties.value', function(value) {
        if (angular.isDefined(value) && value !== null) {
            var items = $scope.properties.availableValues;
            if (Array.isArray(items)) {
                var foundItem = ctrl.findSelectedItem(items);
                ctrl.setSelectedValue(foundItem);
            }
        }
    });

    this.name = widgetNameFactory.getName('pbSelect');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbSelect property named "value" need to be bound to a variable');
    }


    function cambioDeDatos(codigo) {

        if ($scope.properties.acciones) {
            if (codigo) {
                debugger;
                if (codigo === "10") {

                    $scope.properties.datosActualizar.isSemestral = true;
                    $scope.properties.datosActualizar.isCuatrimestral = false;
                    $scope.properties.datosActualizar.isAnual = false;
                    $scope.properties.datosActualizar.fechaFin = ("02/01/" + $scope.properties.datosActualizar.year)

                } else if (codigo === "60") {

                    $scope.properties.datosActualizar.isCuatrimestral = false;
                    $scope.properties.datosActualizar.fechaFin = ("09/01/" + $scope.properties.datosActualizar.year)

                } else if (codigo === "05") {

                    $scope.properties.datosActualizar.isCuatrimestral = true;
                    $scope.properties.datosActualizar.isSemestral = false;
                    $scope.properties.datosActualizar.isAnual = false;
                    $scope.properties.datosActualizar.fechaFin = ("02/01/" + $scope.properties.datosActualizar.year)

                } else if (codigo === "35") {

                    $scope.properties.datosActualizar.isCuatrimestral = true;
                    $scope.properties.datosActualizar.isSemestral = false;
                    $scope.properties.datosActualizar.isAnual = false;
                    $scope.properties.datosActualizar.fechaFin = ("06/01/" + $scope.properties.datosActualizar.year)

                } else if (codigo === "75") {

                    $scope.properties.datosActualizar.isCuatrimestral = true;
                    $scope.properties.datosActualizar.isSemestral = false;
                    $scope.properties.datosActualizar.isAnual = false;
                    $scope.properties.datosActualizar.fechaFin = ("10/01/" + $scope.properties.datosActualizar.year)

                }
            }
        } else {
            if (conteo >= 2) {
                if (codigo) {
                    if (codigo === "10") {

                        $scope.properties.datosActualizar.isSemestral = true;
                        $scope.properties.datosActualizar.isCuatrimestral = false;
                        $scope.properties.datosActualizar.isAnual = false;
                        $scope.properties.datosActualizar.fechaFin = ("02/01/" + $scope.properties.datosActualizar.year)

                    } else if (codigo === "60") {

                        $scope.properties.datosActualizar.isCuatrimestral = false;
                        $scope.properties.datosActualizar.fechaFin = ("09/01/" + $scope.properties.datosActualizar.year)

                    } else if (codigo === "05") {

                        $scope.properties.datosActualizar.isCuatrimestral = true;
                        $scope.properties.datosActualizar.isSemestral = false;
                        $scope.properties.datosActualizar.isAnual = false;
                        $scope.properties.datosActualizar.fechaFin = ("02/01/" + $scope.properties.datosActualizar.year)

                    } else if (codigo === "35") {

                        $scope.properties.datosActualizar.isCuatrimestral = true;
                        $scope.properties.datosActualizar.isSemestral = false;
                        $scope.properties.datosActualizar.isAnual = false;
                        $scope.properties.datosActualizar.fechaFin = ("06/01/" + $scope.properties.datosActualizar.year)

                    } else if (codigo === "75") {

                        $scope.properties.datosActualizar.isCuatrimestral = true;
                        $scope.properties.datosActualizar.isSemestral = false;
                        $scope.properties.datosActualizar.isAnual = false;
                        $scope.properties.datosActualizar.fechaFin = ("10/01/" + $scope.properties.datosActualizar.year)

                    }
                }
            } else {
                conteo++;
            }
        }


    }
}