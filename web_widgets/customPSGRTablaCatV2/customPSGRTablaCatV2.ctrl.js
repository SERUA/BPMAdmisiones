function PbTableCtrl($scope, $http) {

    this.isArray = Array.isArray;

    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function (row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
            $scope.properties.navigationVar = "editar";
        }
    };

    this.isSelected = function (row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    $scope.$watch("properties.urlGet", function(){
        if($scope.properties.urlGet){
            $scope.getCatalogo();
        }
    });

    $scope.$watch("properties.reload", function(){
        if($scope.properties.reload === true){
            $scope.properties.reload = false;
            $scope.getCatalogo();
        }
    });

    $scope.getCatalogo = function () {
        if($scope.properties.dataToFilter.persistenceid != null){
        var req = {
            method: 'POST',  // o el método HTTP que necesites
            url: $scope.properties.urlGet,  // o la URL que necesites
            data: angular.copy($scope.properties.dataToFilter),
        };

        $http(req)
            .then(function (response) {
                $scope.properties.content = response.data.data;
                $scope.value = response.data.totalRegistros;
                $scope.loadPaginado();
                console.log(response.data.data);
            })
            .catch(function (error) {
                // Puedes manejar el error según tus necesidades
                console.error("Error en la solicitud HTTP:", error);
                swal("¡Algo ha fallado!", error.data.error, "error");
            })
            .finally(function () {
                blockUI.stop();
            });
        }
    };
    
    $scope.lstMembership = [];
    $scope.$watch("properties.userId", function(newValue, oldValue) {
        if (Object.keys($scope.properties.lstCatCampus).length === 0) {
            if (newValue !== undefined) {
                var req = {
                    method: "GET",
                    url: `/API/identity/membership?p=0&c=100&f=user_id%3d${$scope.properties.userId.user_id}&d=role_id&d=group_id`
                };
    
                return $http(req)
                .success(function(data, status) {
                    $scope.lstMembership = data;
                    $scope.campusByUser();
                })
                .error(function(data, status) {
                    console.error(data);
                })
                .finally(function() {});
            }
        }
    });
  
    $scope.lstCampusByUser = [];

    $scope.campusByUser = function() {
        var resultado = [];
        resultado.push("Todos los campus");
        for (var x in $scope.lstMembership) {
            if ($scope.lstMembership[x].group_id.name.indexOf("CAMPUS") != -1) {
                let i = 0;
                resultado.forEach(value => {
                    if (value == $scope.lstMembership[x].group_id.name) {
                        i++;
                    }
                });
                if (i === 0) {
                    resultado.push($scope.lstMembership[x].group_id.name);
                }
            }
        }

        $scope.properties.lstCatCampus = resultado;

        // Llamar a $scope.$watch después de actualizar lstCatCampus
        $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
            if (newValue !== undefined) {
                doRequestEstado("POST", $scope.properties.urlGetCampus)
                    .then(function(response) {
                        // Aquí puedes trabajar con la respuesta en caso de éxito
                        console.log("Datos recibidos:", response.data);
                    })
                    .catch(function(error) {
                        // Aquí puedes manejar los errores
                        console.error("Error en la solicitud:", error);
                    });
            }
            console.log($scope.properties.dataToSend);
        });
    };

    function doRequestEstado(method, url, params) {
        return $http({
            method: method,
            url: url,
            data: {
                lstCatCampus: angular.copy($scope.properties.lstCatCampus),
                // Puedes agregar otros datos si es necesario
                // OtrosDatos: ValorDeOtrosDatos
            },
            params: params
        })
        .then(function(response) {
            console.log("Datos recibidos:", response.data);
            
            // Asignar los datos obtenidos a $scope.properties.lstCatCampus
            $scope.properties.lstCatCampus = response.data;
        
            // Llamar a $scope.getCatalogo() al finalizar
            $scope.getCatalogo();
        
            // Puedes realizar alguna acción con la respuesta si es necesario
            return response;
        })
        .catch(function(error) {
            console.error("Error en la solicitud HTTP:", error);
            throw error; // Propagar el error para su posterior manejo si es necesario
        });
    }

    $scope.$watch("properties.dataToFilter", function(newValue, oldValue) {
        if (newValue !== undefined) {
            $scope.getCatalogo("POST", $scope.properties.urlGet);
        }
    });

    $scope.loadPaginado = function() {
        $scope.valorTotal = Math.ceil($scope.value / $scope.properties.dataToFilter.limit);
        $scope.lstPaginado = []
        if ($scope.valorSeleccionado <= 5) {
            $scope.iniciarP = 1;
            $scope.finalP = $scope.valorTotal > 10 ? 10 : $scope.valorTotal;
        } else {
            $scope.iniciarP = $scope.valorSeleccionado - 5;
            $scope.finalP = $scope.valorTotal > ($scope.valorSeleccionado + 4) ? ($scope.valorSeleccionado + 4) : $scope.valorTotal;
        }
        for (var i = $scope.iniciarP; i <= $scope.finalP; i++) {

            var obj = {
                "numero": i,
                "inicio": ((i * 10) - 9),
                "fin": (i * 10),
                "seleccionado": (i == $scope.valorSeleccionado)
            };
            $scope.lstPaginado.push(obj);
        }
    }
}