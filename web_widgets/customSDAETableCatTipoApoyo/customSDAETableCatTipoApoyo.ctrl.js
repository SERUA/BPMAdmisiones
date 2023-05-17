function PbTableCtrl($scope, $http, modalService) {
    this.isArray = Array.isArray;

    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 10;
    $scope.valorTotal = 10;
    
    $scope.loadPaginado = function(){
        $scope.valorTotal = Math.ceil($scope.value/$scope.properties.dataToFilter.limit);
        $scope.lstPaginado = [];

        if($scope.valorSeleccionado <= 5) {
            $scope.iniciarP = 1;
            $scope.finalP = $scope.valorTotal>10 ? 10 : $scope.valorTotal;
        } else {
            $scope.iniciarP = $scope.valorSeleccionado - 5;
            $scope.finalP = $scope.valorTotal>($scope.valorSeleccionado + 4) ? ($scope.valorSeleccionado + 4) : $scope.valorTotal;
        }

        for(var i=$scope.iniciarP; i<=$scope.finalP; i++){

            var obj = {
                "numero":i,
                "inicio":((i*10)-9),
                "fin":(i*10),
                "seleccionado": (i == $scope.valorSeleccionado)
            };
            $scope.lstPaginado.push(obj);
        }
    }

    this.selectRow = function(row) {
        $scope.properties.selectedRow = angular.copy(row);
        // modalService.open($scope.properties.idModal);
        $scope.properties.accion = "agregar";
    };
    
    $scope.setOrderBy= function(order){
        if($scope.properties.dataToFilter.orderby == order){
            $scope.properties.dataToFilter.orientation = ($scope.properties.dataToFilter.orientation=="ASC")?"DESC":"ASC";
        }else{
            $scope.properties.dataToFilter.orderby = order;
            $scope.properties.dataToFilter.orientation = "ASC";
        }
        getRegistrosCatalogo();
    }

    this.selectRowDelete = function(row) {
        
        swal("¿Esta seguro que desea eliminar?", {
            icon: "warning",
            buttons: {
                cancel: "No", 
                catch: {
                    text: "Si",
                    value: true,
                }
            },
        }).then((value) => {
            if(value){
                row.isDeleted = true;
                
                doRequest('POST', "../API/extension/AnahuacBecasRest?url=deleteCatTipoApoyo&p=0&c=0&id=" + row.persistenceId, row);
                // $scope.properties.accion = 'tabla';
            }
        }); 
    };

    function doRequest(method, url, datos, params) {
        var req = {
            method: method,
            url: url,
            data: datos,
            params: params
        };
        
        return $http(req) .success(function(data, status) {
            // $scope.properties.accion = "tabla";
            getRegistrosCatalogo();
            
            swal("!Correcto!", "Se actualizaron los datos correctamente.", "success");
        })
        .error(function(data, status) {
            //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {

        });
    }

    $scope.$watch("properties.dataToFilter", function(newValue, oldValue) {

        if (newValue !== undefined) {
            getRegistrosCatalogo();
        }
        console.log($scope.properties.dataToFilter);
    });

    $scope.$watch("properties.reloadTable", function(newValue, oldValue) {
        if (newValue === true || newValue === "true") {
            $scope.properties.reloadTable = false;
            setTimeout(function(){
                getRegistrosCatalogo();
            }, 500);
        }
    });
    
    function getRegistrosCatalogo() {
        var req = {
            method: "POST",
            url: $scope.properties.urlPost,
            data: angular.copy($scope.properties.dataToFilter)
        };

        return $http(req).success(function(data, status) {
                $scope.properties.contenido = data.data;
        })
        .error(function(data, status) {
            //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {

        });
    }

}