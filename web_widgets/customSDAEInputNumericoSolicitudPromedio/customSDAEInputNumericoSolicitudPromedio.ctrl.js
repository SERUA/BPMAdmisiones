function PbInputCtrl($scope, $log, widgetNameFactory) {

    'use strict';
    //$scope.otroPais={"pais":""};
    this.name = widgetNameFactory.getName('pbInput');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbInput property named "value" need to be bound to a variable');
    }

    $scope.isValid = false;
    $scope.letterOrNumber = null; //letter: true, number: false
    $scope.forceKeyPressUppercase = function(e) {
            var re = /^[a-zA-Z0-9.]*$/g;
            var charInput = e.keyCode;
            var letter = /^[a-zA-Z+-]+$/;
            var number = /^[0-9.]+$/;
            
            debugger;
            if ($scope.properties.preparatoriaSeleccionada != 'México') {
            var limite = $scope.properties.maxLength === 0 ? 250 : $scope.properties.maxLength;
                if (e.target.value.length === 0) {
                    if (e.key.match(letter)) {
                        $scope.letterOrNumber = true;
                        $scope.properties.value = "";
                    } else if (e.key.match(number)) {
                        $scope.letterOrNumber = false;
                        $scope.properties.value = 0;
                    }
                } else if ($scope.letterOrNumber === null && $scope.properties.value.length > 0) {
                    if (e.key.match(letter)) {
                        $scope.letterOrNumber = true;
                    } else if (e.key.match(number)) {
                        $scope.letterOrNumber = false;
                    }
                }
                if ($scope.letterOrNumber) {
                    if ((e.key.match(letter)) && (e.target.value.length) < limite) {
                        $scope.properties.value += letter.test(e.key) ? e.key=='Enter'?"":e.key.toUpperCase() : "";
                        let start = e.target.selectionStart;
                        let end = e.target.selectionEnd;
                        e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
                        e.target.setSelectionRange(start + 1, start + 1);
                        e.preventDefault();
                    } else {
                        var start = e.target.selectionStart;
                        var end = e.target.selectionEnd;
                        e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
                        e.target.setSelectionRange(start + 1, start + 1);
                        e.preventDefault();
                    }
                } else if (!$scope.letterOrNumber && e.key.match(number)) {
                    if (charInput === 69) {
                        $scope.isValid = true;
                    }
                    if (charInput === 82) {
                        $scope.isValid = true;
                    }
                    if (charInput === 86) {
                        $scope.isValid = true;
                    }
                    if (charInput === 46) {
                        $scope.isValid = true;
                    }
                    if ($scope.isValid) {

                    } else {
                        if ((charInput >= 48) && (charInput <= 57) && (e.target.value.length) < limite) {
                            if ($scope.properties.value >= $scope.properties.max) {
                                $scope.properties.value = null;
                                $scope.properties.value = $scope.properties.max;
                            }
                        } else {
                            if ($scope.properties.value >= $scope.properties.max) {
                                $scope.properties.value = null;
                                $scope.properties.value = $scope.properties.max;
                            }
                        }
                    }
                } else {
                    var start = e.target.selectionStart;
                    var end = e.target.selectionEnd;
                    e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
                    e.target.setSelectionRange(start + 1, start + 1);
                    e.preventDefault();
                }
            } else {

                if (charInput === 69) {
                    $scope.isValid = true;
                }
                if (charInput === 82) {
                    $scope.isValid = true;
                }
                if (charInput === 86) {
                    $scope.isValid = true;
                }
                if (charInput === 46) {
                    $scope.isValid = true;
                }
                if ($scope.isValid) {

                } else {
                    //var limite = $scope.properties.maxLength === 0 ? 250 : $scope.properties.maxLength;
                    if ((charInput >= 48) && (charInput <= 57) && (e.target.value.length) < limite) {
                        if ($scope.properties.value >= $scope.properties.max) {
                            $scope.properties.value = null;
                            $scope.properties.value = $scope.properties.max;
                        }
                    } else {
                        if ($scope.properties.value >= $scope.properties.max) {
                            $scope.properties.value = null;
                            $scope.properties.value = $scope.properties.max;
                        }
                    }
                }

            }
        }
        /*$scope.$watch('otroPais.pais', function(value) {
            if (angular.isDefined(value) && value !== null) {
                var re=/^[a-zA-Z0-9.]*$/g;
                if(re.test(value)){
                    $scope.otroPais.pais=value.toUpperCase();
                    $scope.properties.value=value.toUpperCase();
                }else{
                    $scope.otroPais.pais = $scope.properties.value;
                }
            }
        });*/

    $scope.$watch("properties.value", function() {
        if ($scope.properties.preparatoriaSeleccionada == 'México') {
            if ($scope.properties.value === "E") {
                if ($scope.properties.value.length > 1) {
                    $scope.properties.value = null;
                }
            } else if ($scope.properties.value === "R") {
                if ($scope.properties.value.length > 1) {
                    $scope.properties.value = null;
                }
            } else if ($scope.properties.value === "RV") {
                if ($scope.properties.value.length > 2) {
                    $scope.properties.value = null;
                }
            } else if ($scope.properties.value.indexOf('.') != -1) {
                if ($scope.properties.value === "10") {
                    $scope.properties.value = null;
                    $scope.properties.value = $scope.properties.max;
                } else {
                    if ($scope.properties.value.length > 2) {
                        if (parseFloat($scope.properties.value) >= $scope.properties.max) {
                            $scope.properties.value = null;
                            $scope.properties.value = $scope.properties.max;
                        }
                        if (parseFloat($scope.properties.value) % 1 != 0) {
                            $scope.properties.value = parseFloat($scope.properties.value).toFixed(1);
                            $scope.properties.value = parseFloat($scope.properties.value);
                        } else {
                            $scope.properties.value = parseFloat($scope.properties.value);
                        }
                    }
                }
            } else {
                if (parseFloat($scope.properties.value) > $scope.properties.max) {
                    $scope.properties.value = null;
                    //$scope.properties.value = $scope.properties.max;
                }
                if (parseFloat($scope.properties.value) % 1 != 0) {
                    $scope.properties.value = parseFloat($scope.properties.value).toFixed(1);
                    $scope.properties.value = parseFloat($scope.properties.value);
                } else {
                    $scope.properties.value = parseFloat($scope.properties.value);
                }
            }

        } else if ($scope.letterOrNumber === false) {
            if ($scope.properties.value.indexOf('.') != -1) {
                if ($scope.properties.value === "10") {
                    $scope.properties.value = null;
                    $scope.properties.value = $scope.properties.max;
                } else {
                    if ($scope.properties.value.length > 2) {
                        if (parseFloat($scope.properties.value) >= $scope.properties.max) {
                            $scope.properties.value = null;
                            $scope.properties.value = $scope.properties.max;
                        }
                        if (parseFloat($scope.properties.value) % 1 != 0) {
                            $scope.properties.value = parseFloat($scope.properties.value).toFixed(1);
                            $scope.properties.value = parseFloat($scope.properties.value);
                        } else {
                            $scope.properties.value = parseFloat($scope.properties.value);
                        }
                    }
                }
            } else {
                if (parseFloat($scope.properties.value) > $scope.properties.max) {
                    $scope.properties.value = null;
                    //$scope.properties.value = $scope.properties.max;
                }
                if (parseFloat($scope.properties.value) % 1 != 0) {
                    $scope.properties.value = parseFloat($scope.properties.value).toFixed(1);
                    $scope.properties.value = parseFloat($scope.properties.value);
                } else {
                    $scope.properties.value = parseFloat($scope.properties.value);
                }
            }
            //$scope.otroPais.pais = isNaN($scope.properties.value)?"":$scope.properties.value.toString();
        }
        if( $scope.properties.value != null && ($scope.properties.value == "0" || $scope.properties.value == 0) && $scope.properties.value.toString().length == 1){
            $scope.properties.value = null;
        }

    });

}