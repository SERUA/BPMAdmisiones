function PbButtonCtrl($scope, $http) {

    'use strict';

    var vm = this;

    this.action = function action() {
        doRequest($scope.properties.action, $scope.properties.url);
    };

    function doRequest(method, url) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend)
        };

        return $http(req).success(function (data, status) {
            swal("Ok","Guardado exitosamente","success");
            $scope.properties.navigationVar = "tabla"
        })
        .error(function (data, status) {
            swal("Error", data.error,"error");
        })
        .finally(function () {
            vm.busy = false;
        });
    }
}