function PbTableCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    this.isArray = Array.isArray;

    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        $scope.properties.selectedRow = row;
        $scope.properties.isSelected = 'editar';
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
                        $scope.properties.selectedRow["todelete"] = true;
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
            var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
    }

    function doRequest(method, url, params) {
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                doRequestGet();
                swal("Eliminado correctamente!", "", "success");
            })
            .error(function(data, status) {

            });
    }
    
    function doRequestGet() {
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
}