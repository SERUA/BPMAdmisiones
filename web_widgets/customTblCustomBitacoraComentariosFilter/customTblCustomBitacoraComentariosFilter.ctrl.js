function PbTableCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {
    this.isArray = Array.isArray;
    $scope.content =[];
    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        $scope.properties.selectedRow = row;
        $scope.properties.isSelected = 'editar';
        $scope.properties.selectedRow["todelete"] = false;
    };

    this.selectRowDelete = function(row) {
        swal("Esta seguro que desea eliminar?", {
                buttons: {
                    cancel: "No",
                    catch: {
                        text: "Si",
                        value: "Si",
                    }
                },
            })
            .then((value) => {
                switch (value) {
                    case "Si":
                        $scope.properties.selectedRow = row;
                        $scope.properties.selectedRow["todelete"] = false;
                        $scope.properties.selectedRow["isEliminado"] = true;
                        $scope.$apply();
                        startProcess();
                        break;
                    default:

                }
            });
        /*
        
        $scope.properties.isSelected = 'editar';*/
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    function startProcess() {
        if ($scope.properties.processId) {
            var prom = doRequestDelete('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
    }
    
    function doRequest(method, url, params) {
        blockUI.start();
        var req = {
            method: "GET",
            url: `/bonita/API/extension/AnahuacRestGet?url=getCatBitacoraComentario&p=0&c=10&jsonData=${encodeURIComponent(JSON.stringify($scope.properties.filtroToSend))}`,
        };

        return $http(req)
            .success(function(data, status) {
                $scope.content = data.data;
                $scope.value = data.totalRegistros;
                $scope.loadPaginado();
                console.log(data.data)
                swal("!Período "+($scope.properties.dataToFilter.lstCatPeriodoInput[0].isEnabled ? "activado" : "desactivado")+" correctamente!", "", "success");
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                blockUI.stop();
            });
    }
    
    function doRequestDelete(method, url, params) {
        
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                doRequestGet();
                swal("!Eliminado correctamente!", "", "success");
                
            })
            .error(function(data, status) {
                
            });
    }
    
    function doRequestGet() {
        var cantidad = angular.copy($scope.properties.cantidad);
        var req = {
            method: "GET",
            url: $scope.properties.urlGet,
            data: angular.copy($scope.properties.dataToSend)
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.contenido = data;
            })
            .error(function(data, status) {

            });
    }
    

    
    
    $scope.$watch("properties.dataToFilter", function(newValue, oldValue) {
        if (newValue !== undefined) {
            doRequest("GET", $scope.properties.urlPost);
        }
        console.log($scope.properties.dataToFilter);
    });
    $scope.setOrderBy= function(order){
        if($scope.properties.dataToFilter.orderby == order){
            $scope.properties.dataToFilter.orientation = ($scope.properties.dataToFilter.orientation=="ASC")?"DESC":"ASC";
        }else{
            $scope.properties.dataToFilter.orderby = order;
            $scope.properties.dataToFilter.orientation = "ASC";
        }
        doRequest("GET", $scope.properties.urlPost);
    }
    
    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 10;
    $scope.valorTotal = 10;
    
    $scope.loadPaginado = function(){
        $scope.valorTotal = Math.ceil($scope.value/$scope.properties.dataToFilter.limit);
        $scope.lstPaginado=[]
        if($scope.valorSeleccionado <= 5) {
            $scope.iniciarP = 1;
            $scope.finalP = $scope.valorTotal>10 ? 10 : $scope.valorTotal;
        }
        else {
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
    
    $scope.siguiente = function(){
        var objSelected = {};
        for(var i in $scope.lstPaginado){
            if($scope.lstPaginado[i].seleccionado){
                objSelected = $scope.lstPaginado[i];
                $scope.valorSeleccionado=$scope.lstPaginado[i].numero;
            }
        }
        $scope.valorSeleccionado=$scope.valorSeleccionado+1;
        if($scope.valorSeleccionado>Math.ceil($scope.value/$scope.properties.dataToFilter.limit)){
            $scope.valorSeleccionado = Math.ceil($scope.value/$scope.properties.dataToFilter.limit);
        }
        $scope.seleccionarPagina($scope.valorSeleccionado);
    }

    $scope.anterior = function(){
        var objSelected = {};
        for(var i in $scope.lstPaginado){
            if($scope.lstPaginado[i].seleccionado){
                objSelected = $scope.lstPaginado[i];
                $scope.valorSeleccionado=$scope.lstPaginado[i].numero;
            }
        }
        $scope.valorSeleccionado=$scope.valorSeleccionado-1;
        if($scope.valorSeleccionado == 0){
            $scope.valorSeleccionado = 1;
        }
        $scope.seleccionarPagina($scope.valorSeleccionado);
    }

    $scope.seleccionarPagina = function(valorSeleccionado){
        var objSelected = {};
        for(var i in $scope.lstPaginado){
            if($scope.lstPaginado[i].numero == valorSeleccionado){
                $scope.inicio = ($scope.lstPaginado[i].numero-1);
                $scope.fin = $scope.lstPaginado[i].fin;
                $scope.valorSeleccionado=$scope.lstPaginado[i].numero;
                $scope.properties.dataToFilter.offset=(($scope.lstPaginado[i].numero - 1) * $scope.properties.dataToFilter.limit)
            }
        }

        doRequest("GET", $scope.properties.urlPost);
    }
    
    
    $scope.sizing=function(){
        $scope.lstPaginado = [];
        $scope.valorSeleccionado = 1;
        $scope.iniciarP = 1;
        $scope.finalP = 10;
        try{
            $scope.properties.dataToFilter.limit=parseInt($scope.properties.dataToFilter.limit);
        }catch(exception){
            
        }
        
        doRequest("GET", $scope.properties.urlPost);
    }

}