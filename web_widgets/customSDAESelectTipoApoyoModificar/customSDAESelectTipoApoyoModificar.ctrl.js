function PbSelectCtrl($scope, $parse, $log, widgetNameFactory, $timeout, $window, $element) {
    var ctrl = this;

    $scope.backupValue = "";
    $scope.newValue = 0;

    $scope.clearPerc = function () {
        swal({
            title: "¿Realmente deseas cambiar el tipo de apoyo?",
            text: "Si cambias el tipo de apoyo perderás los documentos enviados previamente.",
            icon: "warning",
            buttons: [
                'No, cancelar',
                'Si, cambiar'
            ],
            cancelButtonColor: "#231F20",
            confirmButtonColor: "#FF5900",
            dangerMode: false,
        }).then(function (isConfirm) {
            if (isConfirm) {
                $scope.properties.modificarDocumentos = true;
                $scope.properties.catPorcentajeBecaId = null;
                $scope.properties.catPorcentajeFinanciamientoId = null;
                $scope.properties.value = $scope.newValue;
                $scope.$apply();

                swal({
                    title: 'Ok',
                    text: 'Tu tipo de apoyo ha sido cambiado',
                    icon: 'success'
                });
            } else {
                // $scope.properties.value = $scope.backupValue;
                $scope.newValue = $scope.backupValue;
                $scope.$apply();//Para forzar el cambio de valor 
                swal("Cancelado", "Tus cambios se han revertido.", "success");
            }
        });

        // $scope.properties.modificarDocumentos = true;
        // $scope.properties.catPorcentajeBecaId = null;
        // $scope.properties.catPorcentajeFinanciamientoId = null;
    }

    function comparator(initialValue, item) {
        return angular.equals(initialValue, ctrl.getValue(item));
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

    this.findSelectedItem = function (items) {
        return items.filter(comparator.bind(null, $scope.properties.value))
            .map(function (item) {
                return ctrl.getValue(item);
            })[0];
    };

    this.setSelectedValue = function (foundItem) {
        $timeout(function () {
            $scope.newValue = angular.isDefined(foundItem) ? foundItem : null;
            //   $scope.properties.value = angular.isDefined(foundItem) ? foundItem : null;
        }, 50);
    };

    $scope.$watchCollection('properties.availableValues', function (items) {
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
            // $scope.properties.value = undefined;
            $scope.newValue = undefined;
            ctrl.setSelectedValue(foundItem);
        }
    });

    $scope.$watch('properties.value', function (value) {
        if (angular.isDefined(value) && value !== null) {
            var items = $scope.properties.availableValues;
            if (Array.isArray(items)) {
                var foundItem = ctrl.findSelectedItem(items);
                ctrl.setSelectedValue(foundItem);
            }
        }
    });

    $scope.$watch('properties.returnCatValue', function (returnCatValue) {
        if (returnCatValue) {
            if (returnCatValue.catPorcentajeBeca.descripcion) {
                if (returnCatValue.catPorcentajeFinanciamiento.descripcion) {
                    let suma = parseInt(returnCatValue.catPorcentajeBeca.descripcion) + parseInt(returnCatValue.catPorcentajeFinanciamiento.descripcion);
                    if (suma > 100) {
                        $scope.properties.catPorcentajeFinanciamientoId = null;
                        swal("¡Atención!", "La suma del procentaje de financiamiento y de beca no debe ser mayor al 100%", "warning")
                    }
                }
            }
        }
    });

    this.name = widgetNameFactory.getName('pbSelect');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbSelect property named "value" need to be bound to a variable');
    }

}