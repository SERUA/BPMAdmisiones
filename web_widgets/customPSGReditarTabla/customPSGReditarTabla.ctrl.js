function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        $scope.properties.lista[$scope.properties.valor.index] = angular.copy($scope.properties.valor);
        closeModal(true);
    };

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if(shouldClose)
        {modalService.close();}
    }
}
