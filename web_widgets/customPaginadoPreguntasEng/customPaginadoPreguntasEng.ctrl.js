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
    $scope.lastIndex = 0;
    $scope.precargado = false;
    $scope.ultimaPregunta = false;

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

            if (entro && cambiorespuesta) {
                console.log("updateRespuesta")
                $scope.updateRespuesta();
            } else if (!entro) {
                console.log("insertRespuesta")
                $scope.insertRespuesta();
            } else if($scope.ultimaPregunta === true){
                getDatosRespuestas();
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

    $scope.siguiente = function(_ultimaPregunta) {
        $scope.ultimaPregunta = _ultimaPregunta;
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
                    $scope.properties.lstContestadas.push($scope.properties.valorSeleccionado);
                }
            } else {
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
    
    function showModal(){
        $("#modalConfirmacionFinalizar").modal("show");
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

                if($scope.ultimaPregunta === true){
                    getDatosRespuestas();
                }
            })
            .error(function(data, status) {
                if(data){
                    if(data.error === "test_end"){
                        mensaje = "Your session has ended. You cannot resume the test. If you have any questions, please contact your facilitator.";
                    } else {
                        mensaje = "There is a connection issue detected. Please check your internet service, <b>close your browser</b>, and contact your facilitator to reactivate your test."
                    }
                } else {
                    mensaje = "There is a connection issue detected. Please check your internet service, <b>close your browser</b>, and contact your facilitator to reactivate your test."
                }

                Swal.fire({
                    title: '<strong>Important!</strong>',
                    icon: 'error',
                    html:mensaje, showCloseButton: false
                }).then((result)=>{
                    if(data === null ){
                        window.top.location.href = window.location.protocol + "//" + window.location.host + "/apps/login/testinvp/" 
                    } else if(data.error === "test_end"){
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

                if($scope.ultimaPregunta === true){
                    getDatosRespuestas();
                }
            })
            .error(function(data, status) {
                let mensaje = "";

                if(data){
                    if(data.error === "test_end"){
                        mensaje = "The session has ended, you cant  keep on with the exam anymore. If you have doubts, please contact your advisor.";
                    } else {
                        mensaje = "There is a connection issue detected. Please check your internet service, <b>close your browser</b>, and contact your facilitator to reactivate your test."
                    }
                } else {
                    mensaje = "There is a connection issue detected. Please check your internet service, <b>close your browser</b>, and contact your facilitator to reactivate your test."
                }

                Swal.fire({
                    title: '<strong>Important!</strong>',
                    icon: 'error',
                    html:mensaje, showCloseButton: false
                }).then((result)=>{
                    if(data === null ){
                        // window.close();
                        window.top.location.href = window.location.protocol + "//" + window.location.host + "/apps/login/testinvp/" 
                    } else if(data.error === "test_end"){
                        window.location.reload();
                    }
                    // if(data.error === "test_end"){
                    //     window.location.reload();
                    // } else {
                    //     window.close();
                    // }
                });
                // Swal.fire({
                //     title: '<strong>Attention!</strong>',
                //     icon: 'error',
                //     html:mensaje, showCloseButton: false
                // }).then((result)=>{
                //     if(data.error === "test_end"){
                //         window.location.reload();
                //     }
                // });
                
            })
            .finally(function() {
                //vm.busy = false;
            });
    }
    
    $scope.precargado = false;
    $scope.$watch("properties.objPreguntasContestadas", function(){
        if($scope.precargado === false && ($scope.properties.objPreguntasContestadas.length > 0)){
            $scope.precargado = true;
            let objeto = ($scope.objPreguntasContestadas.length - 1);
            $scope.seleccionarPagina(objeto.pregunta);
        }
    });
    
    $scope.$watch("properties.reload", function() {
        if ($scope.properties.reload !== undefined) {
            $scope.loadPaginado();
        }
    });
    
    //Acciones de finalizar examen 
    function getDatosRespuestas(){
        let url = "../API/extension/AnahuacINVPRestGet?url=getInfoRespuestas&p=0&c=10&username=" + $scope.properties.userData.user_name
                
        return $http({
            method: "GET",
            url: url
        }).success(function (data, status) {
            $scope.datosRespuestas = data.data[0];
            showModal();
        })
        .error(function (data, status) {
            swal("Error", "Cannot get the answers information.", "error")
        });
    }

    function getIdiomaUsuario(){
        var req = {
            method: "GET",
            url: "../API/extension/AnahuacINVPRestGet?url=getIdiomaUsuario&p=0&c=10&username=" + $scope.properties.userData.user_name
        };

        return $http(req).success(function(data, status) {
            if(data.data.length === 0){
                $scope.properties.idioma = "ESP";
            } else if(data.data[0].idioma === null){
                $scope.properties.idioma = "ESP";
            } else{
                $scope.properties.idioma = data.data[0].idioma;
            }
        }).error(function(data, status) {
            console.log(data);
        });
    }

    $scope.$watch("properties.userData", function(){
        if($scope.properties.userData){
            getIdiomaUsuario();
        }
    });
    
    function updateterminado() {
        var data = {
            "terminado": true,
            "username": $scope.properties.userData.user_name
        }

        var req = {
            method: "POST",
            url: "../API/extension/AnahuacINVPRestAPI?url=updateterminado&p=0&c=10",
            data: data
        };

        return $http(req)
        .success(function(data, status) {
            // window.top.location.href = "/bonita/apps/aspiranteinvp/termino/";
        })
        .error(function(data, status) {
           swal("Error", "Cannot update the status to 'Finished'. pleas contact your applicator.", "error")
        })
        .finally(function() {
            updateterminadoGet();
        });
    }
    
    function updateterminadoGet() {
        var req = {
            method: "GET",
            url: "../API/extension/AnahuacINVPRestGet?url=updateterminado&p=0&c=100&username=" + $scope.properties.userData.user_name + "&terminado=true"
        };

        return $http(req)
        .success(function(data, status) {
            window.top.location.href = "/bonita/apps/aspiranteinvp/termino/";
        })
        .error(function(data, status) {
           swal("Error", "Cannot update the status to 'Finished'. pleas contact your applicator.", "error");
        })
        .finally(function() {
            window.top.location.href = "/bonita/apps/aspiranteinvp/termino/";
        });
    }
    
    $scope.executeTask = function(){
        getTaskInfo();
    }
    
    function getTaskInfo(){
        var req = {
            method: "GET",
            url: "../API/bpm/humanTask?p=0&c=10&f=assigned_id=" + $scope.properties.userData.user_id + "&f=name=Examen%20INVP"
        };

        return $http(req)
        .success(function(data, status) {
            let taskid = ""
            for(let reg of data){
                if(reg.assigned_id === $scope.properties.userData.user_id){
                    taskid = reg.id;
                    break;
                }
            }

            executeTask(taskid);
        })
        .error(function(data, status) {
            swal("Error", "Error trying to retrieve task details", "error");
        })
        .finally(function() {

        });
    }

    function executeTask(_taskid) {
        let dataToSend = {
            "isTerminado": true,
            "instanciaINVPInput": {
                "mensajeTermino": "",
            },
            "terminadoFInput": false,
        }

        var req = {
            method: "POST",
            url: "../API/bpm/userTask/" + _taskid + "/execution",
            data: dataToSend
        };

        return $http(req)
        .success(function(data, status) {
            updateterminado();
        })
        .error(function(data, status) {
            if(status == 405){
                window.location.reload();
            } else {
                swal("Error", "Error executing taask: " + status, "error");
            }
        })
        .finally(function() {

        });
    }
    
    function showConfirm(){
        Swal.fire({
            title: '<h3><strong>Confirmation</strong></h3>',
            icon: 'warning',
            html: "<p style='font-size: 10pt'>Once you send your test, you won't be able to change or finish your answers</p>", 
            showCancelButton: true,
            confirmButtonText: 'Send all and finish',
            cancelButtonText: 'Cancel', 
            customClass: {
                cancelButton: 'swal-cancel-button btn btn-info',
                confirmButton: 'btn btn-primary',
            }, 
            buttonsStyling: false,
        }).then((_result)=>{
            if(_result.isConfirmed === true ){
                getTaskInfo();
            } 
        });
    }
}