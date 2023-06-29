function($scope, $http) {

    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 10;
    $scope.valorTotal = 10;
    $scope.properties.valorSeleccionado = $scope.valorSeleccionado;
    $scope.contestado = false;
    $scope.contestadoanterior = false;
    $scope.objcontestada = {};
    $scope.isseleccion = false;
    $scope.preguntaAvanzado = false;
    $scope.lastIndex = 0;
    $scope.precargado = false;

    $scope.$watch('properties.value', function() {
        if ($scope.properties.value != undefined) {
            $scope.loadPaginado();
            
        }
    });

    $scope.loadPaginado = function() {
        $scope.valorTotal = Math.ceil($scope.properties.value / 1);
        $scope.lstPaginado = []
        if ($scope.valorSeleccionado <= 5) {
            $scope.iniciarP = 1;
            $scope.finalP = $scope.valorTotal;
        } else {
            $scope.iniciarP = 1;
            $scope.finalP = $scope.valorTotal;
        }
        for (var i = $scope.iniciarP; i <= $scope.finalP; i++) {

            $scope.properties.lstContestadas.sort(function(a, b) {
              return a - b;
            });

            for (var x = 0; x <= $scope.properties.lstContestadas.length; x++) {
                if ($scope.properties.lstContestadas[x] === i) {
                    $scope.contestadoanterior = true;
                    break;
                }
            }

            if ($scope.contestado) {
                var obj = {
                    "numero": i,
                    "inicio": 1,
                    "fin": (i * 10),
                    "seleccionado": (i == $scope.valorSeleccionado),
                    "contestado": $scope.contestadoanterior
                };
            } else {
                if ($scope.contestadoanterior) {
                    var obj = {
                        "numero": i,
                        "inicio": 1,
                        "fin": (i * 10),
                        "seleccionado": (i == $scope.valorSeleccionado),
                        "contestado": $scope.contestadoanterior
                    };
                } else {
                    var obj = {
                        "numero": i,
                        "inicio": 1,
                        "fin": (i * 10),
                        "seleccionado": (i == $scope.valorSeleccionado),
                        "contestado": false
                    };
                }

            }

            $scope.properties.valorSeleccionado = $scope.valorSeleccionado;
            $scope.lstPaginado.push(obj);
            $scope.lastIndex = obj.contestado ? ( $scope.lstPaginado.length < 567 ? $scope.lstPaginado.length + 1: $scope.lstPaginado.length): $scope.lastIndex;
            $scope.contestadoanterior = false;
        }

        if ($scope.properties.respuestaExamen !== null && $scope.properties.respuestaExamen !== undefined) {
            let contestadas = {
                "pregunta": $scope.properties.objRespuesta.pregunta,
                "respuesta": $scope.properties.objRespuesta.respuesta
            }

            let entro = false;
            let cambiorespuesta = false;
            for (var i = 0; i < $scope.properties.objPreguntasContestadas.length; i++) {
                if (contestadas.pregunta === $scope.properties.objPreguntasContestadas[i].pregunta) {
                    if ($scope.properties.objPreguntasContestadas[i].respuesta != contestadas.respuesta) {
                        cambiorespuesta = true;
                        $scope.properties.objPreguntasContestadas[i].respuesta = contestadas.respuesta
                    }

                    entro = true
                    break;
                }
            }

            if (!entro) {
                $scope.properties.objPreguntasContestadas.push(contestadas);
            }
            console.log("guardar pregunta")
            if (entro && cambiorespuesta) {
                $scope.updateRespuesta();
            } else if (!entro) {
                $scope.insertRespuesta();
            }
            $scope.isseleccion = false;
        }

        let havevalor = false;

        for (var i = 0; i < $scope.properties.objPreguntasContestadas.length; i++) {
            if ($scope.properties.valorSeleccionado === $scope.properties.objPreguntasContestadas[i].pregunta) {
                $scope.properties.respuesta = $scope.properties.objPreguntasContestadas[i].respuesta;
                havevalor = true;
                break;
            }
        }

        if (!havevalor) {
            $scope.properties.respuestaExamen = null;
        }

        if($scope.precargado === false && $scope.lastIndex > 0){
            $scope.precargado = true;
            $scope.seleccionarPagina($scope.lastIndex);
        }
    }

    $scope.siguiente = function() {
        $scope.objcontestada = angular.copy($scope.properties.objRespuesta);
        var objSelected = {};
        for (var i in $scope.lstPaginado) {
            if ($scope.lstPaginado[i].seleccionado) {
                objSelected = $scope.lstPaginado[i];
                $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
            }
        }
        $scope.valorSeleccionado = $scope.valorSeleccionado + 1;
        if ($scope.valorSeleccionado > Math.ceil($scope.properties.value / 1)) {
            $scope.valorSeleccionado = Math.ceil($scope.properties.value / 1);
        }
        $scope.seleccionarPagina($scope.valorSeleccionado);
    }

    $scope.anterior = function() {
        $scope.objcontestada = angular.copy($scope.properties.objRespuesta);
        var objSelected = {};
        for (var i in $scope.lstPaginado) {
            if ($scope.lstPaginado[i].seleccionado) {
                objSelected = $scope.lstPaginado[i];
                $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
            }
        }
        $scope.valorSeleccionado = $scope.valorSeleccionado - 1;
        if ($scope.valorSeleccionado === 0) {
            $scope.valorSeleccionado = 1;
        }
        $scope.seleccionarPagina($scope.valorSeleccionado);
    }

    $scope.seleccionarPagina = function(valorSeleccionado) {
        $scope.isseleccion = true;
        $scope.objcontestada = angular.copy($scope.properties.objRespuesta);
        if ($scope.properties.respuestaExamen !== null && $scope.properties.respuestaExamen !== undefined) {
            $scope.contestado = true;
            if ($scope.properties.lstContestadas.length != 0) {
                if (!($scope.properties.lstContestadas.indexOf($scope.properties.valorSeleccionado) >= 0)) {
                    $scope.preguntaAvanzado = true;
                    $scope.properties.lstContestadas.push($scope.properties.valorSeleccionado);
                }
            } else {
                $scope.preguntaAvanzado = true;
                $scope.properties.lstContestadas.push($scope.properties.valorSeleccionado);
            }
        } else {
            $scope.contestado = false;
        }

        var objSelected = {};
        for (var i in $scope.lstPaginado) {
            if ($scope.lstPaginado[i].numero == valorSeleccionado) {
                $scope.properties.inicio = ($scope.lstPaginado[i].numero - 1);
                $scope.properties.fin = $scope.lstPaginado[i].fin;
                $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
            }
        }

        $scope.loadPaginado();
    }

    $scope.insertRespuesta = function() {
        //vm.busy = true;

        var req = {
            method: "POST",
            url: "../API/extension/AnahuacINVPRestAPI?url=insertRespuesta&p=0&c=10",
            data: $scope.objcontestada
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.dataFromSuccess = true;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
            })
            .error(function(data, status) {
                let mensaje = "";

                if(data){
                    if(data.error === "test_end"){
                        mensaje = "La sesión ha concluido, ya no se puede continuar con la prueba. Si tienes dudas contacta con tu aplicador.";
                    } else {
                        if($scope.preguntaAvanzado === true){
                            $scope.preguntaAvanzado = false;
                            $scope.lstPaginado[$scope.lstPaginado.length - 1].contestado = false;
                            $scope.anterior();
                            $scope.properties.lstContestadas.splice($scope.properties.lstContestadas.length - 1, 1);
                        }
                        mensaje = "Se detecta una falla de conexión. No cierres el navegador, verifica tu conexión de internet y contacta a tu aplicador."
                    }
                } else {
                    if($scope.preguntaAvanzado === true){
                        $scope.preguntaAvanzado = false;
                        $scope.lstPaginado[$scope.lstPaginado.length - 1].contestado = false;
                        $scope.anterior();
                        $scope.properties.lstContestadas.splice($scope.properties.lstContestadas.length - 1, 1);
                    }
                    mensaje = "Se detecta una falla de conexión. No cierres el navegador, verifica tu conexión de internet y contacta a tu aplicador."
                }
                
                Swal.fire({
                    title: '<strong>Atención</strong>',
                    icon: 'error',
                    html:mensaje, showCloseButton: false
                }).then((result)=>{
                    if(data.error === "test_end"){
                        window.location.reload();
                    }
                });
            })
            .finally(function() {
                //vm.busy = false;
            });
    }

    $scope.updateRespuesta = function() {
        //vm.busy = true;

        var req = {
            method: "POST",
            url: "../API/extension/AnahuacINVPRestAPI?url=updateRespuesta&p=0&c=10",
            data: $scope.objcontestada
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.dataFromSuccess = true;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
            })
            .error(function(data, status) {
                let mensaje = "";
                let error = angular.copy(data.error);
                if(data){
                    if(data.error === "test_end"){
                        mensaje = "La sesión ha concluido, ya no se puede continuar con la prueba. Si tienes dudas contacta con tu aplicador.";
                    } else {
                        mensaje = "Se detecta una falla de conexión. No cierres el navegador, verifica tu conexión de internet y contacta a tu aplicador."
                    }
                } else {
                    mensaje = "Se detecta una falla de conexión. No cierres el navegador, verifica tu conexión de internet y contacta a tu aplicador."
                }
                
                Swal.fire({
                    title: '<strong>Atención</strong>',
                    icon: 'error',
                    html:mensaje, showCloseButton: false
                }).then((result)=>{
                    if(data.error === "test_end"){
                        window.location.reload();
                    }
                });
            })
            .finally(function() {
                //vm.busy = false;
            });
    }

    // $scope.precargado = false;
    // $scope.$watch("properties.objPreguntasContestadas", function(){
    //     debugger;
    //     if($scope.precargado === false && ($scope.properties.objPreguntasContestadas.length > 0)){
    //         $scope.precargado = true;
    //         debugger;
    //         let objeto = ($scope.objPreguntasContestadas.length - 1);
    //         $scope.seleccionarPagina(objeto.pregunta);
    //     }
    // });

    $scope.$watch("properties.reload", function() {
        if ($scope.properties.reload !== undefined) {
            $scope.loadPaginado();
        }
    });
}