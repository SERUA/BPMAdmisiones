function PbTableCtrl($scope, $http, $window, blockUI) {

    this.isArray = Array.isArray;
  
    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };
  
    this.selectRow = function(row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };
  
    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }
  
    function doRequest(method, url, params) {
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };
  
        return $http(req).success(function(data, status) {
                $scope.properties.lstContenido = data.data;
                $scope.value = data.totalRegistros;
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                blockUI.stop();
            });
    }
  
    $(function() {
        doRequest("POST", $scope.properties.urlPost);
    })
  
  
    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        if (newValue !== undefined) {
            //if ($scope.properties.campusSeleccionado !== undefined) {
                doRequest("POST", $scope.properties.urlPost);
            //}
        }
        console.log($scope.properties.dataToSend);
    });
  }