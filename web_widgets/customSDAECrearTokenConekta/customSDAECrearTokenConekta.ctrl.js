function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    getConektaTokenbObject();
    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function getConektaTokenbObject() {
        vm.busy = true;
        var req = {
            method: "POST",
            url: "https://api.conekta.io/tokens",
            data: angular.copy($scope.properties.dataToSend),
            headers:{
                "Authorization": "Basic a2V5X2hWNGVyNWtRTjVpWlhzQWlXaEtOdlE=",
                "Accept": "application/vnd.conekta-v2.0.0+json",
                "Accept-Language": "es"
            }
        };

        return $http(req).success(function (data, status) {
            $scope.properties.tokenObject = data;
            // $scope.properties.dataFromSuccess = data;
            // $scope.properties.responseStatusCode = status;
            // $scope.properties.dataFromError = undefined;
            // notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status });
            // if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
            //     redirectIfNeeded();
            // }
            // closeModal($scope.properties.closeOnSuccess);
        })
        .error(function (data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () {
            vm.busy = false;
        });
    }

}
