function PbButtonCtrl($scope, $http, $window, blockUI) {

    'use strict';

    var vm = this;

    $scope.$watch("properties.urlInfo", function(newValue, oldValue) {
        if (newValue !== undefined) {
            //if($scope.properties.lstContenido.length >1){return }
            doRequest();
        }
    });


    function doRequest() {

        blockUI.start();
        var req = {
            method: "GET",
            url: `/bonita/API/extension/AnahuacBecasRestGET?url=${$scope.properties.url}&p=0&c=100${$scope.properties.urlInfo == null ? '':$scope.properties.urlInfo}`,
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.value = data;
                console.log(data);
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            }).finally(function() {

                blockUI.stop();
            });
    }


}