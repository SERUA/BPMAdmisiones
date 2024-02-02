function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    'use strict';

    var vm = this;

    this.action = function action() {
        doRequest("POST", $scope.properties.url);
    };

    function doRequest(method, url, params) {
        vm.busy = true;
        let data = angular.copy($scope.properties.dataToSend);
        data.limit = 999999;
        data.buttonSource = '1';

        var req = {
            method: method,
            url: url,
            data: data,
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                const blob = b64toBlob(data.data[0]);
                const blobUrl = URL.createObjectURL(blob);

                var link = document.createElement('a');
                link.href = blobUrl;
                link.download = $scope.properties.nombre;

                document.body.appendChild(link);

                link.click();

                document.body.removeChild(link);
            })
            .error(function(data, status) {
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                vm.busy = false;
            });
    }

    function b64toBlob(dataURI) {
        var byteString = atob(dataURI);
        var ab = new ArrayBuffer(byteString.length);
        var ia = new Uint8Array(ab);
        let contentType = "";

        if ($scope.properties.fileExtension === "xls") {
            contentType = "application/vnd.ms-excel";
        } else {
            contentType = "application/pdf";
        }

        for (var i = 0; i < byteString.length; i++) {
            ia[i] = byteString.charCodeAt(i);
        }

        return new Blob([ab], { type: contentType });
    }

    function notifyParentFrame(additionalProperties) {
        if ($window.parent !== $window.self) {
            var dataToSend = angular.extend({}, $scope.properties, additionalProperties);
            $window.parent.postMessage(JSON.stringify(dataToSend), '*');
        }
    }
}