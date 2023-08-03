function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {

    'use strict';

    var vm = this;
    $scope.final = [];
    $scope.lstBanner = {};
    $scope.enviar = true;
    $scope.myFunc = function() {
        //$scope.properties.value = $scope.properties.texto;
        $scope.final = [];
        var count = 0;
        blockUI.start();
        $scope.lstBanner = { 'IDBANNER': "", "FECHA": "", "IDSESION": "" };
        try {

            $scope.enviar = true;
            if (!isNullOrUndefined($scope.properties.value)) {
                $scope.properties.value.forEach(element => {
                    $scope.lstBanner.IDBANNER += `${$scope.lstBanner.IDBANNER.length>0?",":""}'${element['IDBANNER']}'`;
                    $scope.lstBanner.FECHA += `${$scope.lstBanner.FECHA.length>0?",":""}'${element['fechaExamen']}'`;
                    $scope.lstBanner.IDSESION += `${$scope.lstBanner.IDSESION.length>0?",":""}'${element['IdSesion']}'`;
                });

                doRequest2("POST", $scope.properties.urlValidar, $scope.lstBanner).then(function() {
                    if ($scope.enviar) {
                        doRequest("POST", $scope.properties.urlPost, $scope.final);
                    } else {
                        swal("¡Aviso!", `¡Se ha actulizado la lista de errores, para enviar los registos sin error presiona otravez el boton!`, "warning");
                        $scope.properties.value = $scope.final;
                    }

                });


            }
        } catch (error) {} finally {
            blockUI.stop();
        }
    };


    function isNullOrUndefined(dato) {
        if (dato === undefined || dato === null || dato.length <= 0) {
            return true;
        }
        return false
    }

    function doRequest(method, url, datos) {
        var req = {
            method: method,
            url: url,
            data: angular.copy(datos)
        };
        return $http(req)
            .success(function(data, status) {
                console.log(status)
                console.log(data)
                $scope.properties.tabla = "tabla";
                $scope.properties.value = [];
                //swal('¡Carga de resultados exitosa!', "", "success")
                //doRequest3("POST","/bonita/API/extension/AnahuacRest?url=subirDatosBannerEthos&p=0&c=100",$scope.final)
            })
            .error(function(data, status) {
                swal("¡Carga incorrecta de resultados!", "", "error")
            })
            .finally(function() {


            });
    }

    function doRequest2(method, url, datos) {
        var req = {
            method: method,
            url: url,
            data: angular.copy(datos)
        };
        return $http(req)
            .success(function(data, status) {
                revisarDatos(data, $scope.properties.value)
            })
            .error(function(data, status) {})
    }
    
    function doRequest3(method, url,datos) {
        var req = {
            method: method,
            url: url,
            data: angular.copy(datos)
        };
        return $http(req)
            .success(function (data, status) {
                console.log("Se subieron los datos");
            })
            .error(function (data, status) {
            })
    }

    function revisarDatos(data, datos) {
        data.data.forEach((info, index) => {
            //let lstidBanner = info.idBanner.split(',')
            let indice = findData(info.idBanner.replaceAll("'", ""))
            if (!info.Existe) {
                $scope.properties.lstErrores = [...$scope.properties.lstErrores, { idBanner: datos[indice]['IDBANNER'], nombre: datos[indice]['Nombre'], Error: "Id banner incorrecto o no se encuentra" }]
                $scope.enviar = false;
            } else if (info.mismaFecha) {
                $scope.properties.lstErrores = [...$scope.properties.lstErrores, { idBanner: datos[indice]['IDBANNER'], nombre: datos[indice]['Nombre'], Error: `El aspirante ya tiene puntuación en la fecha ${datos[indice]['fechaExamen']}` }]
                $scope.enviar = false;
            } //else if (!info.EstaEnCarga) {
                //$scope.properties.lstErrores = [...$scope.properties.lstErrores, { idBanner: datos[indice]['IDBANNER'], nombre: datos[indice]['Nombre'], Error: "El aspirante no se encuentra en carga y consulta de resultados" }]
               // $scope.enviar = false;
            //} 
            else if (info.AA) {
                $scope.properties.lstErrores = [...$scope.properties.lstErrores, { idBanner: datos[indice]['IDBANNER'], nombre: datos[indice]['Nombre'], Error: "Este aspirante tendra que ser cargado manual ya que cuenta con una puntuacion registrada" }]
                $scope.enviar = false;
            } else if (!info.puede) {
                $scope.properties.lstErrores = [...$scope.properties.lstErrores, { idBanner: datos[indice].IDBANNER, nombre: datos[indice].Nombre, Error: "El aspirante ya cuenta con una puntuacion" }]
                $scope.enviar = false;
            } else {
                //hacer la conversion segun la tabla y guardar los valores originales para mostrar
                datos[indice].username = $scope.properties.username;
                $scope.final = [...$scope.final, datos[indice]]
            }
        })

    }

    function findData(valor) {
        let index = $scope.properties.value.findIndex(function(item, i) {
            return item.IDBANNER === valor
        });
        return index
    }

}