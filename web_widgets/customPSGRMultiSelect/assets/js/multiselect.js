angular.module('ui.multiselect', [])

//from bootstrap-ui typeahead parser
.factory('optionParser', ['$parse', function($parse) {

    //                      00000111000000000000022200000000000000003333333333333330000000000044000
    var TYPEAHEAD_REGEXP = /^\s*(.*?)(?:\s+as\s+(.*?))?\s+for\s+(?:([\$\w][\$\w\d]*))\s+in\s+(.*)$/;

    return {
        parse: function(input) {

            var match = input.match(TYPEAHEAD_REGEXP),
                modelMapper, viewMapper, source;
            if (!match) {
                throw new Error(
                    "Expected typeahead specification in form of '_modelValue_ (as _label_)? for _item_ in _collection_'" +
                    " but got '" + input + "'.");
            }

            return {
                itemName: match[3],
                source: $parse(match[4]),
                viewMapper: $parse(match[2] || match[1]),
                modelMapper: $parse(match[1])
            };
        }
    };
}])

.directive('multiselect', ['$parse', '$document', '$compile', 'optionParser',

    function($parse, $document, $compile, optionParser) {
        return {
            restrict: 'E',
            require: 'ngModel',
            link: function(originalScope, element, attrs, modelCtrl) {

                var exp = attrs.options,
                    parsedResult = optionParser.parse(exp),
                    isMultiple = attrs.multiple ? true : false,
                    required = false,
                    scope = originalScope.$new(),
                    key = attrs.key,
                    label = attrs.label,
                    valor = null,
                    changeHandler = attrs.change || anguler.noop;

                scope.items = [];
                scope.header = 'Seleccionar';
                scope.multiple = isMultiple;
                scope.disabled = false;
                scope.key = key;
                scope.label = label;

                originalScope.$on('$destroy', function() {
                    scope.$destroy();
                });

                var popUpEl = angular.element('<multiselect-popup></multiselect-popup>');

                //required validator
                if (attrs.required || attrs.ngRequired) {
                    required = true;
                }
                attrs.$observe('required', function(newVal) {
                    required = newVal;
                });

                //watch disabled state
                scope.$watch(function() {
                    return $parse(attrs.disabled)(originalScope);
                }, function(newVal) {
                    scope.disabled = newVal;
                });

                //watch single/multiple state for dynamically change single to multiple
                scope.$watch(function() {
                    return $parse(attrs.multiple)(originalScope);
                }, function(newVal) {
                    isMultiple = newVal || false;
                    scope.multiple = isMultiple;
                });

                //watch option changes for options that are populated dynamically
                scope.$watch(function() {
                    return parsedResult.source(originalScope);
                }, function(newVal) {
                    if (angular.isDefined(newVal))

                        parseModel();

                });

                //watch model change
                scope.$watch(function() {
                    return modelCtrl.$modelValue;
                }, function(newVal, oldVal) {
                    // Agregado para deseleccionar todo desde fuera
                    if (newVal === "") {
                        scope.uncheckAll();
                    }
                    //when directive initialize, newVal usually undefined. Also, if model value already set in the controller
                    //for preselected list then we need to mark checked in our scope item. But we don't want to do this every time
                    //model changes. We need to do this only if it is done outside directive scope, from controller, for example.
                    if (angular.isDefined(newVal)) {
                        valor = newVal;
                        markChecked(newVal);
                        scope.$eval(changeHandler);
                    }
                    getHeaderText();
                    modelCtrl.$setValidity('required', scope.valid());
                }, true);


                function parseModel() {
                    scope.items.length = 0;
                    var model = parsedResult.source(originalScope);
                    for (var i = 0; i < model.length; i++) {
                        var local = {};
                        local[parsedResult.itemName] = model[i];
                        scope.items.push({
                            label: model[i][scope.label],
                            model: model[i],
                            checked: false
                        });
                    }
                    if (valor != null) {
                        markChecked(modelCtrl.$modelValue);
                        getHeaderText();
                    }



                }

                parseModel();

                element.append($compile(popUpEl)(scope));

                function getHeaderText() {
                    scope.header = "Seleccionar";
                    if (isMultiple) {
                        scope.header = (modelCtrl.$modelValue == "" || modelCtrl.$modelValue == null || modelCtrl.$modelValue == undefined) ? "Seleccionar" : scope.singleHeader;
                    } else {
                        scope.header = (scope.singleHeader == undefined) ? "Seleccionar" : scope.singleHeader;
                    }
                }

                scope.valid = function validModel() {
                    if (!required) return true;
                    var value = modelCtrl.$modelValue;
                    return (angular.isArray(value) && value.length > 0) || (!angular.isArray(value) && value != null);
                };

                function selectSingle(item) {
                    if (item.checked) {
                        scope.uncheckAll();
                    } else {
                        scope.uncheckAll();
                        item.checked = !item.checked;
                    }
                    setModelValue(false);
                }

                function selectMultiple(item) {
                    item.checked = !item.checked;
                    setModelValue(true);
                }

                function setModelValue(isMultiple) {
                    var value;

                    if (isMultiple) {
                        var aux = [];
                        value = "";
                        angular.forEach(scope.items, function(item) {
                            if (item.checked) aux.push(item.model);
                        })
                        if (scope.key == null || scope.key == undefined) {
                            value = aux.length == 0 ? "" : aux.join();
                        } else {
                            for (var i = 0; i < aux.length; i++) {
                                if (aux.length == i + 1) {
                                    value += aux[i][scope.key]
                                } else {
                                    value += aux[i][scope.key] + ","
                                }

                            }
                        }

                    } else {
                        angular.forEach(scope.items, function(item) {
                            if (item.checked) {
                                scope.singleHeader = item.model[scope.label]
                                scope.header = item.model[scope.label]
                                value = item.model[scope.key];
                            }
                        })
                    }
                    (value == '') ? scope.singleHeader = "Seleccionar": null;
                    modelCtrl.$setViewValue(value);
                }

                function markChecked(newVal) {
                    if (!isMultiple) {

                        scope.singleHeader = "Seleccionar";
                        angular.forEach(scope.items, function(item) {
                            if (item.model[scope.key] == newVal) {
                                item.checked = true;
                                scope.singleHeader = item.model[scope.label]
                            } else {
                                item.checked = false;
                            }
                        });
                    } else {
                        scope.singleHeader = ""
                        try {
                            angular.forEach(newVal.split(","), function(i) {
                                angular.forEach(scope.items, function(item) {
                                    if (angular.equals(item.model[scope.key], i) || item.model[scope.key] == i) {
                                        item.checked = true;
                                        scope.singleHeader += item.model[scope.label] + ","
                                    }
                                });
                            });
                        } catch (exception) {

                        }

                        (scope.singleHeader == "") ? scope.singleHeader = "Seleccionar": (newVal.split(",").length == 1) ? scope.singleHeader = scope.singleHeader.slice(0, -1) : scope.singleHeader = newVal.split(",").length + " Seleccionados";
                    }
                }

                scope.checkAll = function() {
                    if (!isMultiple) return;
                    angular.forEach(scope.items, function(item) {
                        item.checked = true;
                    });
                    setModelValue(true);
                };

                scope.uncheckAll = function() {
                    angular.forEach(scope.items, function(item) {
                        item.checked = false;
                    });
                    setModelValue(true);
                };

                scope.select = function(item) {

                    if (isMultiple === false) {
                        selectSingle(item);
                        scope.toggleSelect();
                    } else {
                        selectMultiple(item);
                    }
                }
            }
        };
    }
])

.directive('multiselectPopup', ['$document', function($document) {
    return {
        restrict: 'E',
        scope: false,
        replace: true,
        template: "<div class=\"dropdown\"> <button style='white-space: nowrap;  overflow: hidden; text-overflow: ellipsis;' class=\"btn\" ng-click=\"toggleSelect()\" ng-disabled=\"disabled\" ng-class=\"{'error': !valid()}\"> <span title='{{header}}' class=\"pull-left\">{{header}}</span> <span class=\"caret pull-right\"></span> </button> <ul class=\"dropdown-menu\"> <li> <input class=\" form-control input-block-level\" type=\"text\" ng-model=\"searchText.label\" autofocus=\"autofocus\" placeholder=\"Filtro\" /> </li> <li ng-if=\"multiple\"> <button class=\"btn-link btn-small\" ng-click=\"checkAll()\"><i class=\"glyphicon glyphicon-ok\"></i> Seleccionar todos</button> <button class=\"btn-link btn-small\" ng-click=\"uncheckAll()\"><i class=\"glyphicon glyphicon-remove\"></i> Deseleccionar todos</button> </li> <li ng-repeat=\"i in items | filter:searchText\"> <a ng-click=\"select(i); focus()\"> <i ng-class=\"{'glyphicon glyphicon-check': i.checked, 'glyphicon glyphicon-unchecked': !i.checked}\"></i>{{i.label}}</a> </li> </ul> </div>",
        link: function(scope, element, attrs) {

            scope.isVisible = false;

            scope.toggleSelect = function() {
                if (element.hasClass('open')) {
                    element.removeClass('open');
                    $document.unbind('click', clickHandler);
                } else {
                    element.addClass('open');
                    scope.focus();
                    $document.bind('click', clickHandler);
                }
            };

            function clickHandler(event) {
                if (elementMatchesAnyInArray(event.target, element.find(event.target.tagName)))
                    return;
                element.removeClass('open');
                $document.unbind('click', clickHandler);
                scope.$digest();
            }

            scope.focus = function focus() {
                var searchBox = element.find('input')[0];
                searchBox.focus();
            }

            var elementMatchesAnyInArray = function(element, elementArray) {
                for (var i = 0; i < elementArray.length; i++)
                    if (element == elementArray[i])
                        return true;
                return false;
            }
        }
    }
}]).filter('Excludefilter', function() {
    return function(input, exclude) {
        for (var i = exclude.length - 1; i >= 0; i--) {
            for (var j = 0; j < input.length; j++) {

                if (exclude[i].id == input[j].id && !exclude[i].iseliminado) {
                    return false
                }
            }

        }
        return input
    }
});