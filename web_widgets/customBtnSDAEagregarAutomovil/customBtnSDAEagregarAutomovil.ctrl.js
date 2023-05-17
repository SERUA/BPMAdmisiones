function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        let nuevoItem = {
            "persistenceId_string": "",
            "caseId": $scope.properties.caseId + "",
            "marca": "",
            "modelo": "",
            "ano": null,
            "costoAproximado": "",
            "catSituacionAuto_id": ""
        };
        $scope.properties.lst.push(nuevoItem);
    };
}